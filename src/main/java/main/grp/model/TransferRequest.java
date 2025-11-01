package main.grp.model;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class TransferRequest {
    @NotBlank(message = "Требуется номер карты")
    @Pattern(regexp = "\\d{16}", message = "Номер карты 16 символов")
    private String cardFromNumber;

    @NotBlank(message = "Card from valid till is required")
    @Pattern(regexp = "(0[1-9]|1[0-2])/[0-9]{2}", message = "НЕ верный формат MM/YY")
    private String cardFromValidTill;

    @NotBlank(message = "CVV не задан")
    @Pattern(regexp = "\\d{3}", message = "CVV 3 символа")
    private String cardFromCVV;

    @NotBlank(message = "Требуется номер карты")
    @Pattern(regexp = "\\d{16}", message = "Номер карты 16 символов\"")
    private String cardToNumber;

    @NotNull(message = "Сумма не указана")
    @DecimalMin(value = "0.01", message = "Сумма не указана не верно!")
    private Amount amount;

    // Constructors, getters and setters
    public TransferRequest() {
    }

    public TransferRequest(String cardFromNumber, String cardFromValidTill,
                           String cardFromCVV, String cardToNumber, Amount amount) {
        this.cardFromNumber = cardFromNumber;
        this.cardFromValidTill = cardFromValidTill;
        this.cardFromCVV = cardFromCVV;
        this.cardToNumber = cardToNumber;
        this.amount = amount;
    }

    // Getters and setters
    public String getCardFromNumber() {
        return cardFromNumber;
    }

    public void setCardFromNumber(String cardFromNumber) {
        this.cardFromNumber = cardFromNumber;
    }

    public String getCardFromValidTill() {
        return cardFromValidTill;
    }

    public void setCardFromValidTill(String cardFromValidTill) {
        this.cardFromValidTill = cardFromValidTill;
    }

    public String getCardFromCVV() {
        return cardFromCVV;
    }

    public void setCardFromCVV(String cardFromCVV) {
        this.cardFromCVV = cardFromCVV;
    }

    public String getCardToNumber() {
        return cardToNumber;
    }

    public void setCardToNumber(String cardToNumber) {
        this.cardToNumber = cardToNumber;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }
}