package com.fintech.banking.transaction;

import com.fintech.banking.common.exception.*;
import com.fintech.banking.customer.entity.CustomerEntity;
import com.fintech.banking.customer.repository.CustomerRepository;
import com.fintech.banking.transaction.dto.request.TransactionPurchaseRequestDto;
import com.fintech.banking.transaction.dto.request.TransactionRefundRequestDto;
import com.fintech.banking.transaction.dto.request.TransactionTopUpRequestDto;
import com.fintech.banking.transaction.dto.response.TransactionResponseDto;
import com.fintech.banking.transaction.entity.TransactionEntity;
import com.fintech.banking.transaction.entity.TransactionType;
import com.fintech.banking.transaction.mapper.TransactionMapper;
import com.fintech.banking.transaction.repository.TransactionRepository;
import com.fintech.banking.transaction.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionServiceImpl transactionServiceImpl;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    private final Long originalTransactionId = 1L;

    private TransactionEntity setupOriginalTransaction(CustomerEntity customer, BigDecimal amount, TransactionType type) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setId(originalTransactionId);
        transactionEntity.setAmount(amount);
        transactionEntity.setCustomer(customer);
        transactionEntity.setType(type);

        return transactionEntity;
    }

    /*
    @Test
    void topUp_shouldTopUpBalanceCreateTransactionAndReturnDto() {
        // Arrange
        BigDecimal topUpAmount = BigDecimal.valueOf(50.00);
        BigDecimal initialBalance = BigDecimal.valueOf(100.00);
        BigDecimal expectedBalance = initialBalance.add(topUpAmount);

        CustomerEntity customer = DummyDataProvider.createCustomerEntity(initialBalance);
        Long customerId = customer.getId();
        // Set up request DTO
        TransactionTopUpRequestDto request = new TransactionTopUpRequestDto();
        request.setCustomerId(customerId);
        request.setAmount(topUpAmount);

        // Set up CustomerEntity

        // Set up TransactionEntity
        TransactionEntity transaction = new TransactionEntity();
        transaction.setId(1L);
        transaction.setAmount(topUpAmount);
        transaction.setCustomer(customer);

        // Set up response DTO
        TransactionResponseDto responseDto = new TransactionResponseDto();
        responseDto.setId(transaction.getId());
        responseDto.setAmount(topUpAmount);
        responseDto.setCustomerId(customerId);

        // Stubbing repository & mapper behavior
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(transactionMapper.toDto(any(TransactionEntity.class)))
                .thenReturn(responseDto);

        // Act
        TransactionResponseDto result;
        result = transactionServiceImpl.topUp(request);

        // Assert
        assertEquals(expectedBalance, customer.getBalance());
        verify(transactionRepository).save(any(TransactionEntity.class));
        verify(transactionMapper).toDto(any(TransactionEntity.class));
        assertEquals(responseDto, result);
    }
*/

    @Test
    void purchase_shouldDeductBalanceCreateTransactionAndReturnDto() {
        // Arrange
        BigDecimal purchaseAmount = BigDecimal.valueOf(30.00);
        BigDecimal initialBalance = BigDecimal.valueOf(100.00);
        BigDecimal expectedBalance = initialBalance.subtract(purchaseAmount);
        CustomerEntity customer = DummyDataProvider.createCustomerEntity(initialBalance);
        Long customerId = customer.getId();

        TransactionPurchaseRequestDto request = new TransactionPurchaseRequestDto();
        request.setCustomerId(customerId);
        request.setAmount(purchaseAmount);


        TransactionEntity transaction = new TransactionEntity();
        transaction.setId(1L);
        transaction.setAmount(purchaseAmount);
        transaction.setCustomer(customer);

        TransactionResponseDto responseDto = new TransactionResponseDto();
        responseDto.setId(1L);
        responseDto.setAmount(purchaseAmount);
        responseDto.setCustomerId(customerId);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(transactionMapper.toDto(any(TransactionEntity.class)))
                .thenReturn(responseDto);

        // Act
        TransactionResponseDto result;
        result = transactionServiceImpl.purchase(request);

        // Assert
        assertEquals(expectedBalance, customer.getBalance());
        verify(transactionRepository).save(any(TransactionEntity.class));
        verify(transactionMapper).toDto(any(TransactionEntity.class));
        assertEquals(responseDto, result);
    }

    @Test
    void purchase_shouldThrowException_whenBalanceIsInsufficient() {
        // Arrange
        BigDecimal purchaseAmount = BigDecimal.valueOf(150.00);
        BigDecimal initialBalance = BigDecimal.valueOf(100.00);
        CustomerEntity customer = DummyDataProvider.createCustomerEntity(initialBalance);
        Long customerId = customer.getId();

        TransactionPurchaseRequestDto request = new TransactionPurchaseRequestDto();
        request.setCustomerId(customerId);
        request.setAmount(purchaseAmount);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // Act & Assert
        InsufficientBalanceException exception = assertThrows(
                InsufficientBalanceException.class,
                () -> transactionServiceImpl.purchase(request)
        );
        assertEquals("Insufficient balance", exception.getMessage());

        verify(transactionRepository, never()).save(any());
        verify(transactionMapper, never()).toDto(any());
    }

    @Test
    void refund_shouldAddBalanceCreateRefundTransactionAndReturnDto() {
        // Arrange
        BigDecimal originalAmount = BigDecimal.valueOf(15);
        BigDecimal refundAmount = BigDecimal.valueOf(5);
        BigDecimal initialBalance = BigDecimal.valueOf(60);

        BigDecimal expectedBalance = initialBalance.add(refundAmount);

        CustomerEntity customer = DummyDataProvider.createCustomerEntity(initialBalance);
        Long customerId = customer.getId();
        TransactionEntity originalTx = setupOriginalTransaction(customer, originalAmount, TransactionType.PURCHASE);

        TransactionEntity refundTx = new TransactionEntity();
        refundTx.setId(1L);
        refundTx.setAmount(originalAmount);
        refundTx.setCustomer(customer);
        refundTx.setType(TransactionType.REFUND);

        TransactionResponseDto responseDto = new TransactionResponseDto();
        responseDto.setId(refundTx.getId());
        responseDto.setCustomerId(customerId);
        responseDto.setAmount(originalAmount);
        responseDto.setType(TransactionType.REFUND.toString());

        TransactionRefundRequestDto request = new TransactionRefundRequestDto();
        request.setCustomerId(customerId);
        request.setAmount(refundAmount);
        request.setOriginalTransactionId(originalTransactionId);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(transactionRepository.findById(originalTransactionId)).thenReturn(Optional.of(originalTx));
        when(transactionMapper.toDto(any())).thenReturn(responseDto);

        // Act
        TransactionResponseDto result;
        result = transactionServiceImpl.refund(request);

        // Assert
        assertEquals(expectedBalance, customer.getBalance());
        verify(transactionRepository).save(any(TransactionEntity.class));
        assertEquals(responseDto, result);
    }

    @Test
    void refund_shouldThrow_whenTransactionNotFound() {
        // Arrange
        Long customerId = 1L;
        TransactionRefundRequestDto request = new TransactionRefundRequestDto();
        request.setCustomerId(customerId);
        request.setOriginalTransactionId(originalTransactionId);

        CustomerEntity customer = DummyDataProvider.createCustomerEntity(BigDecimal.valueOf(100));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(transactionRepository.findById(originalTransactionId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TransactionNotFoundException.class, () -> transactionServiceImpl.refund(request));

        verify(transactionRepository, never()).save(any());
    }

    @Test
    void refund_shouldThrow_whenTransactionHasNoCustomer() {
        // Arrange
        Long customerId = 1L;
        TransactionEntity tx = new TransactionEntity();
        tx.setId(originalTransactionId);
        tx.setAmount(BigDecimal.valueOf(50));
        tx.setType(TransactionType.PURCHASE);

        TransactionRefundRequestDto request = new TransactionRefundRequestDto();
        request.setCustomerId(customerId);
        request.setOriginalTransactionId(originalTransactionId);

        CustomerEntity customer = DummyDataProvider.createCustomerEntity(BigDecimal.valueOf(100));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(transactionRepository.findById(originalTransactionId)).thenReturn(Optional.of(tx));

        // Act & Assert
        assertThrows(TransactionOwnerNotFoundException.class, () -> transactionServiceImpl.refund(request));
    }

    @Test
    void refund_shouldThrow_whenCustomerDoesNotOwnTransaction() {
        // Arrange
        CustomerEntity customer = DummyDataProvider.createCustomerEntity(BigDecimal.valueOf(100));
        Long customerId = customer.getId(), differentCustomerId = 2L;

        CustomerEntity differentCustomer = new CustomerEntity();
        differentCustomer.setId(differentCustomerId);

        TransactionEntity tx = setupOriginalTransaction(differentCustomer, BigDecimal.valueOf(20), TransactionType.PURCHASE);

        TransactionRefundRequestDto request = new TransactionRefundRequestDto();
        request.setCustomerId(customerId);
        request.setOriginalTransactionId(originalTransactionId);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(transactionRepository.findById(originalTransactionId)).thenReturn(Optional.of(tx));

        // Act & Assert
        assertThrows(InvalidTransactionOwnerException.class, () -> transactionServiceImpl.refund(request));
    }

    @Test
    void refund_shouldThrow_whenTransactionIsNotPurchase() {
        // Arrange
        CustomerEntity customer = DummyDataProvider.createCustomerEntity(BigDecimal.valueOf(100));
        Long customerId = customer.getId();
        TransactionEntity tx = setupOriginalTransaction(customer, BigDecimal.valueOf(20), TransactionType.TOPUP);

        TransactionRefundRequestDto request = new TransactionRefundRequestDto();
        request.setCustomerId(customerId);
        request.setOriginalTransactionId(originalTransactionId);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(transactionRepository.findById(originalTransactionId)).thenReturn(Optional.of(tx));

        // Act & Assert
        assertThrows(InvalidTransactionTypeException.class, () -> transactionServiceImpl.refund(request));
    }

    @Test
    void refund_shouldThrow_whenRequestedRefundAmountGreaterThanTransactionAmount() {
        // Arrange
        CustomerEntity customer = DummyDataProvider.createCustomerEntity(BigDecimal.valueOf(100));
        Long customerId = customer.getId();
        TransactionEntity tx = setupOriginalTransaction(customer, BigDecimal.valueOf(20), TransactionType.PURCHASE);

        TransactionRefundRequestDto request = new TransactionRefundRequestDto();
        request.setCustomerId(customerId);
        request.setAmount(BigDecimal.valueOf(25L));
        request.setOriginalTransactionId(originalTransactionId);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(transactionRepository.findById(originalTransactionId)).thenReturn(Optional.of(tx));

        // Act & Assert
        assertThrows(GreaterRefundAmountThanTransactionException.class, () -> transactionServiceImpl.refund(request));
    }
}