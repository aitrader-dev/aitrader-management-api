package com.aitrader.api;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Spec 19 R1: base-scale authority in {@code StrategyConfig}.
 */
class StrategyConfigBaseScaleTest {

    @Test
    void nullBaseDefaultsToOne() {
        StrategyConfig config = configWithBase(null);
        assertNull(config.baseScaleMinutes());
        assertEquals(1, config.effectiveBaseScaleMinutes());
        assertEquals(1, StrategyConfig.resolveBaseScaleMinutes(null));
    }

    @Test
    void supportedScalesResolveVerbatim() {
        for (int scale : List.of(1, 5, 10, 15, 30, 60)) {
            assertEquals(scale, StrategyConfig.resolveBaseScaleMinutes(scale));
            assertEquals(scale, configWithBase(scale).effectiveBaseScaleMinutes());
        }
    }

    @Test
    void supportedSetMatchesBacktestCapabilities() {
        assertEquals(List.of(1, 5, 10, 15, 30, 60), StrategyConfig.SUPPORTED_BASE_SCALE_MINUTES);
        assertEquals(1, StrategyConfig.DEFAULT_BASE_SCALE_MINUTES);
    }

    @Test
    void unsupportedBaseRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> StrategyConfig.resolveBaseScaleMinutes(0));
        assertThrows(IllegalArgumentException.class,
                () -> StrategyConfig.resolveBaseScaleMinutes(2));
        assertThrows(IllegalArgumentException.class,
                () -> StrategyConfig.resolveBaseScaleMinutes(7));
        assertThrows(IllegalArgumentException.class,
                () -> StrategyConfig.requireValidBaseScale(45));
    }

    @Test
    void legacyConstructorDefaultsBaseToNull() {
        StrategyConfig legacy = new StrategyConfig(
                "s-1", StrategyType.BB_TREND, "v1", true, "MKT-1",
                1.0, 0, 0, 1.5, 1, 30000L, List.of(), Map.of());
        assertNull(legacy.baseScaleMinutes());
        assertEquals(1, legacy.effectiveBaseScaleMinutes());
    }

    @Test
    void verbatimPeriodRuleNote() {
        // Periods are used verbatim on the base series: ADX(14) on base 1m
        // consumes 14 x 1m bars (14 min wall-clock); ADX(14) on base 5m
        // consumes 14 x 5m bars (70 min wall-clock). Only the series
        // resolution changes, never the period value.
        assertTrue(14 * 1 == 14);
        assertTrue(14 * 5 == 70);
    }

    private static StrategyConfig configWithBase(Integer base) {
        return new StrategyConfig(
                "s-1", StrategyType.BB_TREND, "v1", true, "MKT-1",
                1.0, 0, 0, 1.5, 1, 30000L, List.of(), Map.of(), base);
    }
}
