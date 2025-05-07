package com.fintech.banking.common.config.log;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class MaskingPatternLayout extends PatternLayout {

    @Override
    public String doLayout(ILoggingEvent event) {
        String original = super.doLayout(event);

        // Mask amount and balance (sensitive data)
        return original
                .replaceAll("(?i)(\"?amount\"?\\s*:\\s*)[\\d.]+", "$*****")
                .replaceAll("(?i)(\"?balance\"?\\s*:\\s*)[\\d.]+", "$*****");
    }
}
