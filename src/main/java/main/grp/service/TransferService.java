package main.grp.service;

import main.grp.logger.LogginService;
import main.grp.model.ConfirmRequest;
import main.grp.model.TransferRequest;
import main.grp.model.TransferResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TransferService {
    private final LogginService loggingService;
    private final ConcurrentHashMap<String, TransferRequest> pendingOperations = new ConcurrentHashMap<>();

    private static final double COMMISSION_RATE = 0.01;

    public TransferService(LogginService loggingService) {
        this.loggingService = loggingService;
    }

    public TransferResponse transfer(TransferRequest request) {
        try {

            if (!isValidCard(request.getCardFromNumber(), request.getCardFromValidTill(), request.getCardFromCVV())) {
                loggingService.logError(request.getCardFromNumber(), request.getCardToNumber(),
                        request.getAmount().getValue().doubleValue(), "Invalid card data");
                throw new RuntimeException("Invalid card data");
            }

            if (!hasSufficientBalance(request.getCardFromNumber(), request.getAmount().getValue().doubleValue())) {
                loggingService.logError(request.getCardFromNumber(), request.getCardToNumber(),
                        request.getAmount().getValue().doubleValue(), "Insufficient balance");
                throw new RuntimeException("Insufficient balance");
            }

            String operationId = UUID.randomUUID().toString();
            pendingOperations.put(operationId, request);

            double commission = calculateCommission(request.getAmount().getValue().doubleValue());
            loggingService.logTransfer(request.getCardFromNumber(), request.getCardToNumber(),
                    request.getAmount().getValue().doubleValue(), commission, "PENDING");

            return new TransferResponse(operationId);

        } catch (Exception e) {
            loggingService.logError(request.getCardFromNumber(), request.getCardToNumber(),
                    request.getAmount().getValue().doubleValue(), e.getMessage());
            throw e;
        }
    }

    public TransferResponse confirmOperation(ConfirmRequest request) {
        TransferRequest transferRequest = pendingOperations.get(request.getOperationId());
        if (transferRequest == null) {
            throw new RuntimeException("Operation not found");
        }

        if (!"0000".equals(request.getCode())) { //Сверка кода...
            loggingService.logError(transferRequest.getCardFromNumber(), transferRequest.getCardToNumber(),
                    transferRequest.getAmount().getValue().doubleValue(), "Invalid confirmation code");
            throw new RuntimeException("Invalid confirmation code");
        }

        try {

            processTransfer(transferRequest);

            double commission = calculateCommission(transferRequest.getAmount().getValue().doubleValue());
            loggingService.logTransfer(transferRequest.getCardFromNumber(), transferRequest.getCardToNumber(),
                    transferRequest.getAmount().getValue().doubleValue(), commission, "SUCCESS");

            pendingOperations.remove(request.getOperationId());
            return new TransferResponse(request.getOperationId());

        } catch (Exception e) {
            loggingService.logError(transferRequest.getCardFromNumber(), transferRequest.getCardToNumber(),
                    transferRequest.getAmount().getValue().doubleValue(), "Transfer failed: " + e.getMessage());
            throw new RuntimeException("Transfer failed", e);
        }
    }

    private boolean isValidCard(String cardNumber, String validTill, String cvv) {

        return cardNumber != null && cardNumber.matches("\\d{16}") &&
                validTill != null && validTill.matches("(0[1-9]|1[0-2])/[0-9]{2}") &&
                cvv != null && cvv.matches("\\d{3}");
    }

    private boolean hasSufficientBalance(String cardNumber, double amount) {

        return true; // С баллансом всё в порядке... Всегда
    }

    private double calculateCommission(double amount) {
        return amount * COMMISSION_RATE;
    }

    private void processTransfer(TransferRequest request) {
        // Типа что-то делаем усиленно...

        //Сюда обработка платежа
    }
}