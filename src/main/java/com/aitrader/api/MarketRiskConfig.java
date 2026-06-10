package com.aitrader.api;

import java.math.BigDecimal;

public record MarketRiskConfig(
        String id,
        String marketId,
        BigDecimal maxGrossExposure,
        BigDecimal maxLongExposure,
        BigDecimal maxShortExposure,
        BigDecimal slippageBps,
        BigDecimal gapAssumptionPct,
        BigDecimal leverageLimit
) {}
