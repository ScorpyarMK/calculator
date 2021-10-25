package com.javalearn.calculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CalculatorRunner {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Calculator calculator = new Calculator();

        while (true) {
            System.out.print("Enter expression: \n");

            String input = reader.readLine();

            if (input.equals("exit"))
                break;

            calculator.evaluate(input);
            System.out.println("Solution: " + calculator.getResult() + "\n");
        }
    }
}
