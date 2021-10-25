package com.javalearn.calculator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void getResult() {
        Calculator calculator = new Calculator();
        calculator.evaluate("5+(7*5)*10");

        BigDecimal expected = calculator.getResult();
        BigDecimal actual = new BigDecimal(355);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void evaluate() {
        Calculator calculator = new Calculator();

        Throwable thrown = assertThrows(NumberFormatException.class, () -> {
            calculator.evaluate("2*add+5");
        });

        assertNotNull(thrown.getMessage());
    }


    @Test
    void fillTheList() {
        Calculator calculator = new Calculator();

        List<String> expected = new ArrayList<>();
        calculator.fillTheList("2+2", expected);

        List<String> actual = new ArrayList<>();
        actual.add("2");
        actual.add("+");
        actual.add("2");

        Assertions.assertEquals(expected, actual);
    }


    @Test
    void calculate() {
        Calculator calculator = new Calculator();

        Exception exception = assertThrows(ArithmeticException.class, () ->
                calculator.evaluate("5/0"));

        assertEquals("Can't be divided by Zero", exception.getMessage());
    }
}