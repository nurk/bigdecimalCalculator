package com.nurk.bigdecimalcalculator;

import org.junit.jupiter.api.Test;

import static java.math.RoundingMode.*;
import static org.assertj.core.api.Assertions.*;

class BigDecimalCalculatorTest {

    @Test
    void calculate_plus() {
        assertThat(BigDecimalCalculator.calculate("1.0 + 2.0", 2, HALF_UP)).isEqualByComparingTo("3.00");
    }

    @Test
    void calculate_minus() {
        assertThat(BigDecimalCalculator.calculate("2.0 - 1.0", 2, HALF_UP)).isEqualByComparingTo("1.00");
    }

    @Test
    void calculate_multiply() {
        assertThat(BigDecimalCalculator.calculate("2.0 * 0.5", 2, HALF_UP)).isEqualByComparingTo("1.00");
    }

    @Test
    void calculate_divide() {
        assertThat(BigDecimalCalculator.calculate("2.0 / 2", 2, HALF_UP)).isEqualByComparingTo("1.00");
    }

    @Test
    void calculate_parentheses() {
        assertThat(BigDecimalCalculator.calculate("3 * (-2.0 / 2)", 2, HALF_UP)).isEqualByComparingTo("-3.00");
    }

    @Test
    void calculate_parentheses_noSpaces() {
        assertThat(BigDecimalCalculator.calculate("3*(-2.0/2)", 2, HALF_UP)).isEqualByComparingTo("-3.00");
    }

    @Test
    void calculate_wrongNumberOfParentheses_thenException() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> BigDecimalCalculator.calculate("3*-2.0/2)", 2, HALF_UP));
    }

    @Test
    void calculate_letters_thenException() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> BigDecimalCalculator.calculate("a - 2", 2, HALF_UP));
    }

    @Test
    void calculate_noBinaryRepresentationError() {
        assertThat(BigDecimalCalculator.calculate("0.03 - 0.02", 4, HALF_UP)).isEqualByComparingTo("0.0100");
    }

    @Test
    void calculate_higherPrecisionThanDouble() {
        assertThat(BigDecimalCalculator.calculate("1 + 1 / 3 + 1", 30, HALF_UP)).isEqualByComparingTo("2.333333333333333333333333333333");
    }

    @Test
    void calculate_doubleNegativeFactor() {
        assertThat(BigDecimalCalculator.calculate("(1 - -2) - (1 - -2)", 1, HALF_UP)).isEqualByComparingTo("0.0");
    }
}