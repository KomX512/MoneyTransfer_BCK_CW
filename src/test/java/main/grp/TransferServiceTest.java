package main.grp;


import main.grp.logger.LogginService;
import main.grp.model.*;
import main.grp.service.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private LogginService loggingService;

    private TransferService transferService;

    @BeforeEach
    void setUp() {
        transferService = new TransferService(loggingService);
    }

    @Test
    void testSuccessfulTransfer() {
        // Given
        TransferRequest request = createValidTransferRequest();

        // When
        TransferResponse response = transferService.transfer(request);

        // Then
        assertNotNull(response);
        assertNotNull(response.getOperationId());
        verify(loggingService).logTransfer(anyString(), anyString(), anyDouble(), anyDouble(), eq("PENDING"));
    }


    @Test
    void testSuccessfulConfirmation() {
        // Given
        TransferRequest transferRequest = createValidTransferRequest();
        TransferResponse transferResponse = transferService.transfer(transferRequest);
        ConfirmRequest confirmRequest = new ConfirmRequest(transferResponse.getOperationId(), "0000");

        // When
        TransferResponse response = transferService.confirmOperation(confirmRequest);

        // Then
        assertNotNull(response);
        assertEquals(transferResponse.getOperationId(), response.getOperationId());
        verify(loggingService).logTransfer(anyString(), anyString(), anyDouble(), anyDouble(), eq("SUCCESS"));
    }

    @Test
    void testConfirmationWithInvalidCode() {
        // Given
        TransferRequest transferRequest = createValidTransferRequest();
        TransferResponse transferResponse = transferService.transfer(transferRequest);
        ConfirmRequest confirmRequest = new ConfirmRequest(transferResponse.getOperationId(), "9999");

        // When & Then
        assertThrows(RuntimeException.class, () -> transferService.confirmOperation(confirmRequest));
        verify(loggingService).logError(anyString(), anyString(), anyDouble(), anyString());
    }

    private TransferRequest createValidTransferRequest() {
        Amount amount = new Amount(new BigDecimal("1234.00"), "RUB");
        return new TransferRequest(
                "1234567812345678",
                "12/26",
                "123",
                "8765432187654321",
                amount
        );
    }
}