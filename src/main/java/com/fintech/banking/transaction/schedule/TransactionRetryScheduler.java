package com.fintech.banking.transaction.schedule;

import com.fintech.banking.common.exception.TransactionRetryScheduleException;
import com.fintech.banking.common.exception.InvalidLockedTransactionException;
import com.fintech.banking.transaction.dto.request.TransactionPurchaseRequestDto;
import com.fintech.banking.transaction.dto.request.TransactionRefundRequestDto;
import com.fintech.banking.transaction.dto.request.TransactionTopUpRequestDto;
import com.fintech.banking.transaction.schedule.model.LockedTransactionDto;
import com.fintech.banking.transaction.service.TransactionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class TransactionRetryScheduler {

    private final TransactionService transactionService;

    public TransactionRetryScheduler(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /*When an OptimisticLockingFailureException occurs, the operation is added to a queue. A scheduled task runs every 10 seconds, reads from the queue, and reprocesses the operations.
     For a more robust solution, we should use Kafka, where consumers can pull failed operations from a topic and reprocess them asynchronously.
     */
    @Scheduled(fixedRate = 10000) // every 10 seconds
    public void retryFailedTransactions() {

        while (!LockedTransactionCache.failedTransactionCache.isEmpty()) {
            try {
                System.out.println("------------schedule worked-----------");
                LockedTransactionDto lockedTransactionDto = LockedTransactionCache.failedTransactionCache.poll();

                if (lockedTransactionDto != null) {
                    switch (lockedTransactionDto.getTransactionType()) {
                        case TOPUP:
                            TransactionTopUpRequestDto topUpRequest = new TransactionTopUpRequestDto();

                            topUpRequest.setCustomerId(lockedTransactionDto.getCustomerId());
                            topUpRequest.setAmount(lockedTransactionDto.getAmount());

                            transactionService.topUp(topUpRequest);
                            break;
                        case PURCHASE:
                            TransactionPurchaseRequestDto purchaseRequest = new TransactionPurchaseRequestDto();

                            purchaseRequest.setCustomerId(lockedTransactionDto.getCustomerId());
                            purchaseRequest.setAmount(lockedTransactionDto.getAmount());

                            transactionService.purchase(purchaseRequest);
                            break;
                        case REFUND:
                            if (lockedTransactionDto.getTransactionId().isEmpty()) {
                                throw new InvalidLockedTransactionException("Locked transaction id is empty");
                            }

                            TransactionRefundRequestDto refundRequest = new TransactionRefundRequestDto();

                            refundRequest.setCustomerId(lockedTransactionDto.getCustomerId());
                            refundRequest.setAmount(lockedTransactionDto.getAmount());
                            refundRequest.setOriginalTransactionId(lockedTransactionDto.getTransactionId().getAsLong());

                            transactionService.refund(refundRequest);
                            break;
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                throw new TransactionRetryScheduleException("Schedule have some problems");
            }
        }
    }
}
