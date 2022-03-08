package com.nurk.bigdecimalcalculator;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * source https://sites.google.com/site/drjohnbmatthews/enumerated-functions
 */
public class BigDecimalCalculator {
    private final RoundingMode roundingMode;
    private final BigDecimal value;
    private final int scale;
    private StreamTokenizer tokens;
    private int token;

    public static BigDecimal calculate(String formula, int scale, RoundingMode roundingMode) {
        return new BigDecimalCalculator(formula, scale, roundingMode).value;
    }

    private BigDecimalCalculator(String formula, int scale, RoundingMode roundingMode) {
        this.scale = scale;
        this.roundingMode = roundingMode;
        setupTokenizer(formula);

        getToken();
        value = expr().setScale(scale, roundingMode);
        if (!tokenIs(Symbol.END)) {
            throwError("syntax error");
        }
    }

    private void setupTokenizer(String formula) {
        Reader reader = new StringReader(formula);
        tokens = new StreamTokenizer(reader);
        tokens.resetSyntax();
        tokens.wordChars('a', 'z');
        tokens.wordChars('A', 'Z');
        tokens.wordChars(128 + 32, 255);
        tokens.whitespaceChars(0, 32);
        tokens.wordChars('0', '9');
        tokens.wordChars('.', '.');
        tokens.ordinaryChar(Symbol.SLASH.toChar());
    }

    private BigDecimal expr() {
        int sign = 1;
        accept(Symbol.PLUS);
        while (accept(Symbol.MINUS)) {
            sign *= -1;
        }
        BigDecimal value = term().multiply(BigDecimal.valueOf(sign));
        while (Symbol.isAddOp(token)) {
            if (accept(Symbol.PLUS)) {
                value = value.add(term());
            }
            if (accept(Symbol.MINUS)) {
                value = value.subtract(term());
            }
        }
        return value;
    }

    private BigDecimal term() {
        BigDecimal value = factor();
        while (Symbol.isMulOp(token)) {
            if (accept(Symbol.STAR)) {
                value = value.multiply(factor());
            }
            if (accept(Symbol.SLASH)) {
                value = value.divide(factor(), scale, roundingMode);
            }
        }
        return value;
    }

    private BigDecimal factor() {
        BigDecimal value = BigDecimal.ZERO;
        BigDecimal sign = BigDecimal.ONE;
        while (accept(Symbol.MINUS)) {
            sign = sign.multiply(new BigDecimal("-1"));
        }
        if (tokenIs(Symbol.WORD)) {
            try {
                value = new BigDecimal(tokens.sval).multiply(sign);
                getToken();
            } catch (Exception e) {
                throwError("factor is not a number");
            }
        } else if (accept(Symbol.OPEN)) {
            value = expr().multiply(sign);
            expect(Symbol.CLOSE);
        } else {
            throwError("factor error");
            getToken();
        }
        return value;
    }

    private void getToken() {
        try {
            token = tokens.nextToken();
        } catch (IOException e) {
            throwError("i/o error " + e.getMessage());
        }
    }

    private boolean tokenIs(Symbol symbol) {
        return token == symbol.token();
    }


    private void expect(Symbol symbol) {
        if (accept(symbol)) {
            return;
        }
        throwError("missing " + symbol.toChar());
    }

    private boolean accept(Symbol symbol) {
        if (tokenIs(symbol)) {
            getToken();
            return true;
        }
        return false;
    }

    private void throwError(String error) {
        throw new IllegalArgumentException(error + " at " + tokens.toString().replaceAll(",.*$", ""));
    }

    private enum Symbol {
        PLUS('+'), MINUS('-'), STAR('*'), SLASH('/'),
        OPEN('('), CLOSE(')'),
        END(StreamTokenizer.TT_EOF),
        WORD(StreamTokenizer.TT_WORD);

        private final int token;

        Symbol(int token) {
            this.token = token;
        }

        public int token() {
            return this.token;
        }

        public char toChar() {
            if (this.token < 32) {
                return '\ufffd';
            } else {
                return (char) this.token;
            }
        }

        public static boolean isAddOp(int token) {
            return token == PLUS.token || token == MINUS.token;
        }

        public static boolean isMulOp(int token) {
            return token == STAR.token || token == SLASH.token;
        }
    }
}
