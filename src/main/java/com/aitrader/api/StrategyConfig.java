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
        Map<String, Object> customConfig) {
}
