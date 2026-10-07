package com.aitrader.api;

import java.util.List;
import java.util.Map;

public record StrategyConfig(
        String strategyId,
        StrategyType strategyType,
        String strategyVersion,
        Boolean enabled,
        String marketId,
        Double orderSize,
        Integer limitDistance,
        Integer stopDistance,
        Double chanceRiskRatio,
        Integer maxOpenOrders,
        Long executionTimeout,
        List<TimeFrame> activeTimeFrames,
        Map<String, Object> customConfig,
        Integer baseScaleMinutes) {

    /** Default base scale in whole minutes (back-compat, current live behavior). */
    public static final int DEFAULT_BASE_SCALE_MINUTES = 1;

    /**
     * Supported base scales in whole minutes (mirrors
     * {@code MtfScalePolicy.SUPPORTED_SCALE_MINUTES} and the backtest
     * capabilities list).
     */
    public static final List<Integer> SUPPORTED_BASE_SCALE_MINUTES =
            List.of(1, 5, 10, 15, 30, 60);

    /**
     * Backward-compatible constructor for callers that predate
     * {@code baseScaleMinutes}. A null base means the default scale.
     */
    public StrategyConfig(
            String strategyId,
            StrategyType strategyType,
            String strategyVersion,
            Boolean enabled,
            String marketId,
            Double orderSize,
            Integer limitDistance,
            Integer stopDistance,
            Double chanceRiskRatio,
            Integer maxOpenOrders,
            Long executionTimeout,
            List<TimeFrame> activeTimeFrames,
            Map<String, Object> customConfig) {
        this(strategyId, strategyType, strategyVersion, enabled, marketId,
                orderSize, limitDistance, stopDistance, chanceRiskRatio,
                maxOpenOrders, executionTimeout, activeTimeFrames, customConfig,
                null);
    }

    /**
     * Effective base scale in whole minutes. Null means the default.
     *
     * @return configured base scale, or 1 when missing/null
     */
    public int effectiveBaseScaleMinutes() {
        return baseScaleMinutes == null ? DEFAULT_BASE_SCALE_MINUTES : baseScaleMinutes;
    }

    /**
     * Resolves and validates a base scale value.
     *
     * @param value configured base scale, null means the default
     * @return effective base scale, 1 when null
     * @throws IllegalArgumentException when the scale is unsupported
     */
    public static int resolveBaseScaleMinutes(Integer value) {
        int effective = value == null ? DEFAULT_BASE_SCALE_MINUTES : value;
        if (!SUPPORTED_BASE_SCALE_MINUTES.contains(effective)) {
            throw new IllegalArgumentException(
                    "Unsupported chart scale: requested " + effective
                            + " minutes, supported scales are "
                            + SUPPORTED_BASE_SCALE_MINUTES + " minutes.");
        }
        return effective;
    }

    /**
     * Validates a base scale value.
     *
     * @param value configured base scale, null means the default
     * @throws IllegalArgumentException when the scale is unsupported
     */
    public static void requireValidBaseScale(Integer value) {
        resolveBaseScaleMinutes(value);
    }
}
