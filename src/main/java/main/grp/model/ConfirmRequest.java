package main.grp.model;

import jakarta.validation.constraints.NotBlank;

public class ConfirmRequest {
    @NotBlank(message = "Требуется ID операции")
    private String operationId;

    @NotBlank(message = "Нужен код")
    private String code;

    // Constructors, getters and setters
    public ConfirmRequest() {
    }

    public ConfirmRequest(String operationId, String code) {
        this.operationId = operationId;
        this.code = code;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}