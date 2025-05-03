package com.fintech.banking.common.config.log;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;

public class MaskingPatternLayoutEncoder extends PatternLayoutEncoder {
    @Override
    public void start() {
        MaskingPatternLayout layout = new MaskingPatternLayout();
        layout.setContext(context);
        layout.setPattern(getPattern());
        layout.start();
        this.layout = layout;
        super.start();
    }
}
