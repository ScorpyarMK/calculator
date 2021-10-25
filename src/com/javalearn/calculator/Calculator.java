package com.javalearn.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {

    //Regex pattern to fill the list with correctly data
    private final Pattern regexPattern = Pattern.compile("(?<!\\d|\\))-?\\d*[.,]?\\d+|[-()+\\/*]");
    private final List<String> listForExpression = new ArrayList<>();
    private final List<String> listForCalculation = new ArrayList<>();
    private BigDecimal result = new BigDecimal(0);

    public BigDecimal getResult() {
        return result;
    }

    public BigDecimal evaluate(String expression) {

        if (!(expression.matches("[0-9+*\\/)(-]+")))
            throw new NumberFormatException("Non-Arithmetic expression");

        fillTheList(expression, listForExpression);

        //Check if expression contains "("
        if (listForExpression.contains("(")) {
            //  If "(" is found, we look for a suitable case.
            for (int i = listForExpression.indexOf("(") + 1; i < listForExpression.size(); i++) {
                //  Case 1: the first element we found was the second "("
                if (listForExpression.get(i).equals("(")) {
                    expression = expandNestedBracket(expression);
                }
                // Case 2: the first element we found was the second ")"
                else if (listForExpression.get(i).equals(")")) {
                    expression = expandBracket(expression);
                }
            }
        }

        //Filling the list for arithmetic operation
        fillTheList(expression, listForCalculation);

        //Looking for a suitable arithmetic operation by priority
        while (listForCalculation.size() != 0) {
            if (listForCalculation.contains("/")) {
                calculate("/");
            }
            else if (listForCalculation.contains("*")) {
                calculate("*");
            }
            else if (listForCalculation.contains("-")) {
                calculate("-");
            }
            else if (listForCalculation.contains("+")) {
                calculate("+");
            }

            // Check if list contains arithmetic operators
            if ((!listForCalculation.contains("*")) && (!listForCalculation.contains("/"))
                    && (!listForCalculation.contains("+")) && (!listForCalculation.contains("-"))) {
                return result;
            }
        }

        return new BigDecimal(listForCalculation.get(0));
    }

    /**
     * A method in which parentheses are expanded by recursively calling
     * the evaluate() method on expressions in parentheses.
     * */
    public String expandBracket (String expression) {
        StringBuilder recursionCalc = new StringBuilder();
        String nestedExpression;
        String resultAfterExpand;

        for (int j = listForExpression.indexOf("(") + 1; j < listForExpression.indexOf(")"); j++) {
            recursionCalc.append(listForExpression.get(j));
        }

        nestedExpression = expression.substring(expression.indexOf("("), expression.indexOf(")") + 1);
        resultAfterExpand = String.valueOf(evaluate(recursionCalc.toString()));
        expression = expression.replace(nestedExpression, resultAfterExpand);

        fillTheList(expression, listForExpression);

        return expression;
    }

    /**
     * The method in which the nested parentheses are expanded by recursively calling
     * the evaluate () method on the expressions nested in the parentheses.
     * */
    public String expandNestedBracket (String expression) {
        StringBuilder recursionCalc = new StringBuilder();
        String nestedExpression;
        String resultAfterExpand;

        for (int j = listForExpression.indexOf("(") + 1; j < listForExpression.lastIndexOf(")"); j++) {
            recursionCalc.append(listForExpression.get(j));
        }

        nestedExpression = expression.substring(expression.indexOf("(") , expression.lastIndexOf(")") + 1);
        resultAfterExpand = String.valueOf(evaluate(recursionCalc.toString()));
        expression = expression.replace(nestedExpression, resultAfterExpand);

        fillTheList(expression, listForExpression);

        return expression;
    }

    /**
     * The method in which the list is filled with an arithmetic expression
     * for further operations on it.
     * */
    public void fillTheList(String expression, List<String> expressionsList) {
        Matcher matcher = regexPattern.matcher(expression);
        expressionsList.clear();
        while (matcher.find()) {
            expressionsList.add(matcher.group());
        }
    }

    /**
     * The method in which arithmetic operations are performed.
     * Param: operator - the mathematical operator with which the method was called.
     * After the selection and execution of the operation, the result is entered
     * into the list and the operands and operator involved in the operation are removed.
     * */

    public void calculate(String operator) {
        int operatorIndex = listForCalculation.indexOf(operator);
        BigDecimal operandOne = new BigDecimal(listForCalculation.get(operatorIndex - 1));
        BigDecimal operandTwo = new BigDecimal(listForCalculation.get(operatorIndex + 1));

        switch (operator) {
            case "/" :
                try {
                    result = operandOne.divide(operandTwo, 2, RoundingMode.HALF_UP);
                } catch (ArithmeticException e) {
                    throw new ArithmeticException("Can't be divided by Zero");
                }
                break;
            case "*":
                result = operandOne.multiply(operandTwo);
                break;
            case "-":
                result = operandOne.subtract(operandTwo);
                break;
            case "+":
                result = operandOne.add(operandTwo);
                break;
        }
        listForCalculation.add(operatorIndex - 1, String.valueOf(result));
        listForCalculation.remove(operatorIndex + 2);
        listForCalculation.remove(operatorIndex + 1);
        listForCalculation.remove(operatorIndex);
    }
}