package com.fintech.banking.transaction.controller;

import com.fintech.banking.transaction.dto.request.TransactionPurchaseRequestDto;
import com.fintech.banking.transaction.dto.request.TransactionRefundRequestDto;
import com.fintech.banking.transaction.dto.response.TransactionResponseDto;
import com.fintech.banking.transaction.dto.request.TransactionTopUpRequestDto;
import com.fintech.banking.transaction.config.interceptor.IdempotencyInterceptor;
import com.fintech.banking.transaction.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/topup")
    public ResponseEntity<TransactionResponseDto> topUp(@Valid @RequestBody TransactionTopUpRequestDto request,
                                                        @RequestHeader("X-Idempotency-Key") String idempotencyKey) {

        TransactionResponseDto transactionResponseDto = transactionService.topUp(request);
        //add to map to idempotency validation
        IdempotencyInterceptor.storeResponse(idempotencyKey, transactionResponseDto);

        return ResponseEntity.ok(transactionResponseDto);
    }

    @PostMapping("/purchase")
    public ResponseEntity<TransactionResponseDto> purchase(@Valid @RequestBody TransactionPurchaseRequestDto request,
                                                           @RequestHeader("X-Idempotency-Key") String idempotencyKey) {

        TransactionResponseDto transactionResponseDto = transactionService.purchase(request);
        //add to map to idempotency validation
        IdempotencyInterceptor.storeResponse(idempotencyKey, transactionResponseDto);

        return ResponseEntity.ok(transactionResponseDto);
    }

    @PostMapping("/refund")
    public ResponseEntity<TransactionResponseDto> refund(@Valid @RequestBody TransactionRefundRequestDto request,
                                                         @RequestHeader("X-Idempotency-Key") String idempotencyKey) {

        TransactionResponseDto transactionResponseDto = transactionService.refund(request);
        //add to map to idempotency validation
        IdempotencyInterceptor.storeResponse(idempotencyKey, transactionResponseDto);

        return ResponseEntity.ok(transactionResponseDto);
    }
}