package com.fintech.banking.transaction.schedule;

import com.fintech.banking.transaction.schedule.model.LockedTransactionDto;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LockedTransactionCache {
    public static Queue<LockedTransactionDto> failedTransactionCache = new ConcurrentLinkedQueue<>();

}
