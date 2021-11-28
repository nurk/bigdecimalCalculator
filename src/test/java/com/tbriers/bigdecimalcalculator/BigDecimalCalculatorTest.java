package com.tbriers.bigdecimalcalculator;

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
        assertThat(BigDecimalCalculator.calculate("0.03 - 0.02", 2, HALF_UP)).isEqualByComparingTo("0.01");
    }
}