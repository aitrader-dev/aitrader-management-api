# aitrader-management-api Mission

## Goal

Provide shared domain model types (Java records and enums) for the management domain — markets, trade accounts, broker credentials, strategy configurations, and subscriptions — used across service boundaries.

## Target Users

- The `management-service` that manages account and market configuration.
- `strategy-stream-processor` and `trading-stream-processor` that consume management data.
- Developers who need to add or change a management-domain type used across services.

## Success Criteria

- Records and enums are immutable and follow consistent naming conventions.
- Types support Jackson serialization for JSON interchange.
- No runtime logic, no Spring Boot application, no persistence — a pure API artifact published to GitHub Packages.
