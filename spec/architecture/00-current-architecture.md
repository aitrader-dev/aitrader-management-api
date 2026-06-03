# Current Architecture

## Overview

A pure Java records library with no runtime framework or persistence. All types live in a single flat package `com.aitrader.api`.

## Dependencies

- `jackson-databind` — JSON serialization.
- `jackson-datatype-jsr310` — Java 8 date/time support (LocalTime in TimeFrame).
- No Lombok, no Reactor, no JPA, no Spring Framework.

## Package Structure

```
com.aitrader.api
  ├── BrokerCredentials.java
  ├── Market.java
  ├── StrategyConfig.java
  ├── StrategySubscription.java
  ├── StrategyType.java
  ├── TimeFrame.java
  ├── TradeAccount.java
  └── TradeAccountType.java
```

## Known Issues

- pom.xml description field incorrectly says `events-api` (copy-paste from aitrader-events-api).
- No tests exist.
