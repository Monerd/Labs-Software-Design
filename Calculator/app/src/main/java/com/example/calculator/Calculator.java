package com.example.calculator;

import java.util.Stack;

public class Calculator {

    public static String searchResult(String mathText) {
        return getAnswer(getReversePoland(preparingZero(mathText)));
    }

    private static String getReversePoland(String exp) {
        Stack<Character> stack = new Stack<>();
        StringBuilder cur = new StringBuilder();
        int priority;

        for (int i = 0; i < exp.length(); i++) {
            priority = checkPriority(exp.charAt(i));
            if (priority == 0)
                cur.append(exp.charAt(i));
            else if (priority == 1)
                stack.push(exp.charAt(i));
            else if (priority > 1) {
                cur.append(' ');

                while (!stack.isEmpty()) {
                    if (checkPriority(stack.peek()) >= priority)
                        cur.append(stack.pop());
                    else
                        break;
                }

                stack.push(exp.charAt(i));
            }
            else if (priority == -1) {
                cur.append(' ');

                while (checkPriority(stack.peek()) != 1)
                    cur.append(stack.pop());

                stack.pop();
            }
        }

        while (!stack.isEmpty())
            cur.append(stack.pop());

        cur = new StringBuilder(preparingString(cur.toString()));

        return cur.toString();
    }

    private static String getAnswer(String rev) {
        StringBuilder operand = new StringBuilder();
        Stack<Double> stack = new Stack<>();
        int priority;
        int countNum = 0;
        char num = ' ';

        for (int i = 0; i < rev.length(); i++) {
            if (checkPriority(rev.charAt(i)) == 0 &&
                rev.charAt(i) != ' ') {
                countNum++;
                num = rev.charAt(i);
            }
            if (countNum > 1)
                break;
        }
        if (countNum == 1)
            return String.valueOf(num);

        for (int i = 0; i < rev.length(); i++) {
            priority = checkPriority(rev.charAt(i));
            if (rev.charAt(i) == ' ')
                continue;
            else if (priority == 0) {
                while (rev.charAt(i) != ' ' && checkPriority(rev.charAt(i)) == 0) {
                    operand.append(rev.charAt(i++));
                    if (i == rev.length())
                        break;
                }
                stack.push(Double.valueOf(operand.toString()));
                operand = new StringBuilder();
            }
            else if (priority > 1) {
                Double a = stack.pop();
                Double b = stack.pop();
                if (rev.charAt(i) == '+')
                    stack.push(b + a);
                else if (rev.charAt(i) == '-')
                    stack.push(b - a);
                else if (rev.charAt(i) == '*')
                    stack.push(b * a);
                else if (rev.charAt(i) == '/')
                    stack.push(b / a);
            }
        }
        return String.valueOf(stack.pop());
    }
    
    private static String preparingZero(String str) {
        StringBuilder preparedStr = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char sym = str.charAt(i);
            if (sym == '-' &&
                    i == 0)
                preparedStr.append('0');
            else if (i >= 1 &&
                    sym == '-' &&
                    str.charAt(i - 1) == '(')
                preparedStr.append('0');
            else if (i >= 1 &&
                    str.charAt(i) == '(' &&
                    (checkPriority(str.charAt(i - 1)) == 0 ||
                    checkPriority(str.charAt(i - 1)) == -1))
                preparedStr.append('*');
            preparedStr.append(sym);
        }
        return deleteExBracket(preparedStr.toString());
    }

    private static String deleteExBracket(String str) {
        StringBuffer preparedStr = new StringBuffer(str);
        for (int i = 0; i < str.length(); i++) {
            if (i >= 1 && str.charAt(i) == '(' && str.charAt(i - 1) == ')')
                preparedStr.delete(i - 1, i);
        }
        return String.valueOf(preparedStr);
    }

    private static String preparingString(String str) {
        StringBuffer preparedStr = new StringBuffer(str);
        int priority;
        for (int i = 0; i < preparedStr.length(); i++) {
            priority = checkPriority(preparedStr.charAt(i));
            if (priority != 0) {
                if (preparedStr.charAt(i - 1) != ' ')
                    preparedStr.insert(i, ' ');
            }
        }
        return String.valueOf(preparedStr);
    }

    private static Integer checkPriority(char op) {
        switch (op) {
            case '(':
                return 1;
            case ')':
                return -1;
            case '+':
            case '-':
                return 2;
            case '*':
            case '/':
                return 3;
        }
        return 0;
    }
}
