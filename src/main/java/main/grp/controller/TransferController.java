package main.grp.controller;

import jakarta.validation.Valid;
import main.grp.model.ConfirmRequest;
import main.grp.model.ErrorResponse;
import main.grp.model.TransferRequest;
import main.grp.model.TransferResponse;
import main.grp.service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping()
@CrossOrigin(origins = "*")
public class TransferController {
    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@Valid @RequestBody TransferRequest request) {
        TransferResponse response = transferService.transfer(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirmOperation")
    public ResponseEntity<TransferResponse> confirmOperation(@Valid @RequestBody ConfirmRequest request) {
        TransferResponse response = transferService.confirmOperation(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<String> sayHi() {
        return ResponseEntity.ok("ON-LINE " + LocalDateTime.now());
    }

}