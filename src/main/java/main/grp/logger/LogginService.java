package main.grp.logger;

import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class LogginService {
    private static final String LOG_FILE = "logs/transfer.log";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void logTransfer(String cardFrom, String cardTo, double amount,
                            double commission, String result) {
        String logEntry = String.format(
                "[%s] Transfer: %s -> %s | Amount: %.2f | Commission: %.2f | Result: %s",
                LocalDateTime.now().format(DATE_FORMATTER),
                maskCardNumber(cardFrom),
                maskCardNumber(cardTo),
                amount,
                commission,
                result
        );

        writeToFile(logEntry);
    }

    public void logError(String cardFrom, String cardTo, double amount, String error) {
        String logEntry = String.format(
                "[%s] ERROR: %s -> %s | Amount: %.2f | Error: %s",
                LocalDateTime.now().format(DATE_FORMATTER),
                maskCardNumber(cardFrom),
                maskCardNumber(cardTo),
                amount,
                error
        );

        writeToFile(logEntry);
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 8) {
            return cardNumber;
        }
        return cardNumber.substring(0, 4) + "****" + cardNumber.substring(cardNumber.length() - 4);
    } // Можно записывать и полный номер карты... Но у нас же тут банк и деньги...

    private synchronized void writeToFile(String logEntry) {
        try {
            File logDir = new File("logs");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            try (FileWriter fw = new FileWriter(LOG_FILE, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                out.println(logEntry);
            }
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }
}