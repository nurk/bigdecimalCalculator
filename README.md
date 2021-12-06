# Bigdecimal Calculator

Heavily inspired by https://sites.google.com/site/drjohnbmatthews/enumerated-functions

When you need to perform calculations of formulas that require a higher precision than a double, you can still write it as a formula instead of using bigDecimal chaining.

Example:

```BigDecimal result = BigDecimalCalculator.calculate(String.format("((%s - %s) / %s) * (%s + %s)", "2", 1d, 1L, 2, BigDecimal.ONE), 30, HALF_UP);```

This gives the following result: `3.000000000000000000000000000000`
