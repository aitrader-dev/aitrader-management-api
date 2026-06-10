package com.aitrader.api;

import java.math.BigDecimal;

public record AccountRiskConfig(
        String id,
        String accountId,
        Integer maxOpenPositions,
        BigDecimal marginWarningThreshold,
        BigDecimal marginCallThreshold,
        BigDecimal stopOutThreshold,
        BigDecimal forceCloseThreshold,
        String closeOutStrategy,
        BigDecimal concentrationLimitPct,
        Boolean requireStopLoss
) {}
