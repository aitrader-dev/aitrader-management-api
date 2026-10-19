package com.aitrader.api;

import java.math.BigDecimal;

public record StrategySubscription(
        String id,
        String strategyName,
        String marketName,
        Boolean enabled,
        BigDecimal orderSize,
        Integer strategyVersion
) {
    /**
     * Backward-compatible constructor for callers that predate the exact-version
     * pin. A null pin means "unpinned" and is rejected by the server (400);
     * callers must supply an explicit integer version.
     */
    public StrategySubscription(
            String id,
            String strategyName,
            String marketName,
            Boolean enabled,
            BigDecimal orderSize) {
        this(id, strategyName, marketName, enabled, orderSize, null);
    }
}
