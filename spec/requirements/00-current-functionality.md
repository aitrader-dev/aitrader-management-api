# Current Functionality Requirements

## Goal

Document the existing management domain types so that changes are made against explicit, testable definitions.

## Types in `com.aitrader.api`

### Records

- `Market` — name, brokerMarketName, enabled.
- `TradeAccount` — id, userName, accountType, enabled, systemAccount, brokerCredentials, strategySubscriptions. Includes convenience method `getStrategySubscription(String)`.
- `BrokerCredentials` — id, brokerAccountId, brokerUsername, password, apiKey, passwordEncrypted.
- `StrategyConfig` — strategyId, strategyType, strategyVersion, enabled, marketId, orderSize, limitDistance, stopDistance, maxOpenOrders, executionTimeout, activeTimeFrames, customConfig (Map).
- `StrategySubscription` — id, strategyName, marketName, enabled, orderSize.
- `TimeFrame` — from, to (LocalTime), with validation (from before to) and contains(LocalTime) method.

### Enums

- `TradeAccountType` — LIVE, DEMO.
- `StrategyType` — ADX, EMA, DEBUG_LOGGING.

## Build Requirements

- Published as a JAR to GitHub Packages.
- Dependencies: jackson-databind, jackson-datatype-jsr310 only.
- No test sources currently exist.

## Acceptance Criteria

- All types are serializable/deserializable with Jackson.
- Adding a new type updates this file.
