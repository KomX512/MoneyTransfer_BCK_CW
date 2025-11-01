package main.grp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransferControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    static GenericContainer<?> appContainer = new GenericContainer<>("mtservice:latest")
            .withExposedPorts(5500)
            .withReuse(true);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("server.port", () -> appContainer.getMappedPort(5500));
    }

    @Test
    void testTransferEndpoint() {
        // Given
        String baseUrl = "http://localhost:" + appContainer.getMappedPort(5500);

        Map<String, Object> amount = new HashMap<>();
        amount.put("value", 1000);
        amount.put("currency", "RUB");

        Map<String, Object> transferRequest = new HashMap<>();
        transferRequest.put("cardFromNumber", "1234567812345678");
        transferRequest.put("cardFromValidTill", "12/25");
        transferRequest.put("cardFromCVV", "123");
        transferRequest.put("cardToNumber", "8765432187654321");
        transferRequest.put("amount", amount);

        // When
        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl + "/transfer",
                transferRequest,
                Map.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().get("operationId"));
    }

    @Test
    void testConfirmOperationEndpoint() {
        // Given
        String baseUrl = "http://localhost:" + appContainer.getMappedPort(5500);

        // First, create a transfer
        Map<String, Object> amount = new HashMap<>();
        amount.put("value", 1000);
        amount.put("currency", "RUB");

        Map<String, Object> transferRequest = new HashMap<>();
        transferRequest.put("cardFromNumber", "1234567812345678");
        transferRequest.put("cardFromValidTill", "12/25");
        transferRequest.put("cardFromCVV", "123");
        transferRequest.put("cardToNumber", "8765432187654321");
        transferRequest.put("amount", amount);

        ResponseEntity<Map> transferResponse = restTemplate.postForEntity(
                baseUrl + "/transfer",
                transferRequest,
                Map.class
        );

        String operationId = (String) transferResponse.getBody().get("operationId");

        Map<String, Object> confirmRequest = new HashMap<>();
        confirmRequest.put("operationId", operationId);
        confirmRequest.put("code", "0000");

        ResponseEntity<Map> confirmResponse = restTemplate.postForEntity(
                baseUrl + "/confirmOperation",
                confirmRequest,
                Map.class
        );

        assertEquals(HttpStatus.OK, confirmResponse.getStatusCode());
        assertNotNull(confirmResponse.getBody());
        assertEquals(operationId, confirmResponse.getBody().get("operationId"));
    }
}