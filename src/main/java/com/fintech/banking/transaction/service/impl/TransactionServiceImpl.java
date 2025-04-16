package com.fintech.banking.transaction.service.impl;

import com.fintech.banking.common.exception.*;
import com.fintech.banking.customer.entity.CustomerEntity;
import com.fintech.banking.customer.repository.CustomerRepository;
import com.fintech.banking.transaction.schedule.model.LockedTransactionDto;
import com.fintech.banking.transaction.dto.request.TransactionPurchaseRequestDto;
import com.fintech.banking.transaction.dto.request.TransactionRefundRequestDto;
import com.fintech.banking.transaction.dto.response.TransactionResponseDto;
import com.fintech.banking.transaction.dto.request.TransactionTopUpRequestDto;
import com.fintech.banking.transaction.entity.TransactionEntity;
import com.fintech.banking.transaction.entity.TransactionType;
import com.fintech.banking.transaction.mapper.TransactionMapper;
import com.fintech.banking.transaction.repository.TransactionRepository;
import com.fintech.banking.transaction.schedule.LockedTransactionCache;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.fintech.banking.transaction.entity.TransactionType.TOPUP;

@Service
public class TransactionServiceImpl implements com.fintech.banking.transaction.service.TransactionService {

    private static final int ZERO = 0;

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final TransactionMapper transactionMapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  CustomerRepository customerRepository,
                                  TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
        this.transactionMapper = transactionMapper;
    }

    @Transactional
    @Override
    public TransactionResponseDto topUp(TransactionTopUpRequestDto request) {
        try {
            CustomerEntity customer = getCustomer(request.getCustomerId());
            customer.setBalance(customer.getBalance().add(request.getAmount()));

            TransactionEntity topUpTransaction = createTransaction(customer, request.getAmount(), TOPUP);

            transactionRepository.save(topUpTransaction);
            return transactionMapper.toDto(topUpTransaction);
        } catch (OptimisticLockingFailureException exception) {

            LockedTransactionCache.failedTransactionCache.add(LockedTransactionDto.builder()
                    .customerId(request.getCustomerId())
                    .amount(request.getAmount())
                    .transactionType(TOPUP)
                    .build());

            throw new OptimisticLockingFailureException("Customer is being updated. Will retry.");
        }
    }

    @Transactional
    @Override
    public TransactionResponseDto purchase(TransactionPurchaseRequestDto request) {

        CustomerEntity customer = getCustomer(request.getCustomerId());

        if (customer.getBalance().compareTo(request.getAmount()) < ZERO) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        try {
            customer.setBalance(customer.getBalance().subtract(request.getAmount()));
            TransactionEntity purchaseTransaction = createTransaction(customer, request.getAmount(), TransactionType.PURCHASE);

            transactionRepository.save(purchaseTransaction);
            return transactionMapper.toDto(purchaseTransaction);
        } catch (OptimisticLockingFailureException exception) {
            LockedTransactionCache.failedTransactionCache.add(LockedTransactionDto.builder()
                    .customerId(request.getCustomerId())
                    .amount(request.getAmount())
                    .transactionType(TransactionType.PURCHASE)
                    .build());

            throw new OptimisticLockingFailureException("Customer is being updated. Will retry.");
        }
    }

    /*for production based approach we must check also transaction refunded early or not,
      if refunded it must be blocked, for this implementation we should store parent transaction id for refunded transactions*/
    @Transactional
    @Override
    public TransactionResponseDto refund(TransactionRefundRequestDto request) {

        CustomerEntity customer = getCustomer(request.getCustomerId());

        TransactionEntity originalTransaction = transactionRepository.findById(request.getOriginalTransactionId())
                .orElseThrow(() -> new TransactionNotFoundException("Original transaction not found"));

        if (originalTransaction.getCustomer() == null) {
            throw new TransactionOwnerNotFoundException("Transaction customer not found");
        }

        if (!originalTransaction.getCustomer().getId().equals(request.getCustomerId())) {
            throw new InvalidTransactionOwnerException("Transaction does not belong to the customer");
        }

        if (originalTransaction.getType() != TransactionType.PURCHASE) {
            throw new InvalidTransactionTypeException("Only purchase transactions can be refunded");
        }

        if (originalTransaction.getAmount().compareTo(request.getAmount()) < ZERO) {
            throw new GreaterRefundAmountThanTransactionException("Requested refund amount can not be greater than transaction amount");
        }

        try {
            customer.setBalance(customer.getBalance().add(request.getAmount()));
            TransactionEntity refundTransaction = createTransaction(customer, request.getAmount(), TransactionType.REFUND);

            transactionRepository.save(refundTransaction);
            return transactionMapper.toDto(refundTransaction);
        } catch (OptimisticLockingFailureException exception) {
            LockedTransactionCache.failedTransactionCache.add(LockedTransactionDto.builder()
                    .customerId(request.getCustomerId())
                    .transactionType(TransactionType.REFUND)
                    .amount(request.getAmount())
                    .build());

            throw new OptimisticLockingFailureException("Customer is being updated. Will retry.");
        }
    }

    private CustomerEntity getCustomer(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    private TransactionEntity createTransaction(CustomerEntity customer, BigDecimal amount, TransactionType type) {
        return TransactionEntity.builder()
                .customer(customer)
                .amount(amount)
                .type(type)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
