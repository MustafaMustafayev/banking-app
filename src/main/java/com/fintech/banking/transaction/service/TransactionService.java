package com.fintech.banking.transaction.service;

import com.fintech.banking.transaction.dto.request.TransactionPurchaseRequestDto;
import com.fintech.banking.transaction.dto.request.TransactionRefundRequestDto;
import com.fintech.banking.transaction.dto.response.TransactionResponseDto;
import com.fintech.banking.transaction.dto.request.TransactionTopUpRequestDto;

public interface TransactionService {
    TransactionResponseDto topUp(TransactionTopUpRequestDto request);

    TransactionResponseDto purchase(TransactionPurchaseRequestDto request);

    TransactionResponseDto refund(TransactionRefundRequestDto request);
}
