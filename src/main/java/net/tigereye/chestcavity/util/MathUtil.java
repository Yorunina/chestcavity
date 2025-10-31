package net.tigereye.chestcavity.util;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MathUtil {
    private MathUtil() {
    }

    public static float horizontalDistanceTo(Entity entity1, Entity entity2) {
        float f = (float) (entity1.getX() - entity2.getX());
        float h = (float) (entity1.getZ() - entity2.getZ());
        return Mth.sqrt(f * f + h * h);
    }

    public static float getAngle(Vec2 a, Vec2 b) {
        return getAngle(a.x, a.y, b.x, b.y);
    }

    public static float getAngle(double ax, double ay, double bx, double by) {
        return (float) (Math.atan2(by - ay, bx - ax) + Math.PI);
    }

    private static final Map<String, Integer> OPERATOR_PRECEDENCE = new HashMap<>();

    static {
        // 设置运算符优先级（数字越大优先级越高）
        OPERATOR_PRECEDENCE.put("+", 1);
        OPERATOR_PRECEDENCE.put("-", 1);
        OPERATOR_PRECEDENCE.put("*", 2);
        OPERATOR_PRECEDENCE.put("/", 2);
        OPERATOR_PRECEDENCE.put(">", 0);
        OPERATOR_PRECEDENCE.put("<", 0);
        OPERATOR_PRECEDENCE.put(">=", 0);
        OPERATOR_PRECEDENCE.put("<=", 0);
        OPERATOR_PRECEDENCE.put("==", 0);
        OPERATOR_PRECEDENCE.put("!=", 0);
    }

    public static boolean evaluateBooleanExpression(String expression) {
        expression = preprocessExpression(expression);
        return parseComparison(expression);
    }

    public static double evaluateArithmeticExpression(String expression) {
        expression = preprocessExpression(expression);
        return parseArithmetic(expression);
    }

    private static String preprocessExpression(String expression) {
        return expression.replaceAll("\\s+", "").replaceAll("≥", ">=").replaceAll("≤", "<=");
    }

    private static boolean parseComparison(String expr) {
        String[] comparisonOperators = {">=", "<=", "==", "!=", ">", "<"};

        for (String op : comparisonOperators) {
            int index = findOuterOperatorIndex(expr, op);
            if (index != -1) {
                String leftExpr = expr.substring(0, index);
                String rightExpr = expr.substring(index + op.length());

                double leftValue = parseArithmetic(leftExpr);
                double rightValue = parseArithmetic(rightExpr);

                return performComparison(leftValue, rightValue, op);
            }
        }

        throw new IllegalArgumentException("No valid comparison operator found in: " + expr);
    }

    private static boolean performComparison(double left, double right, String operator) {
        final double EPSILON = 1e-10;

        switch (operator) {
            case ">": return left > right;
            case "<": return left < right;
            case ">=": return left >= right;
            case "<=": return left <= right;
            case "==": return Math.abs(left - right) < EPSILON;
            case "!=": return Math.abs(left - right) >= EPSILON;
            default: throw new IllegalArgumentException("Unknown comparison operator: " + operator);
        }
    }

    private static int findOuterOperatorIndex(String expr, String operator) {
        int parenCount = 0;

        for (int i = 0; i <= expr.length() - operator.length(); i++) {
            char c = expr.charAt(i);

            if (c == '(') parenCount++;
            else if (c == ')') parenCount--;

            if (parenCount == 0 && expr.startsWith(operator, i)) {
                return i;
            }
        }

        return -1;
    }

    private static double parseArithmetic(String expr) {
        if (expr.isEmpty()) {
            throw new IllegalArgumentException("Empty expression");
        }

        List<Object> tokens = tokenize(expr);
        return evaluateArithmeticTokens(tokens);
    }

    private static List<Object> tokenize(String expr) {
        List<Object> tokens = new ArrayList<>();
        int i = 0;
        int len = expr.length();

        while (i < len) {
            char c = expr.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                StringBuilder numberBuilder = new StringBuilder();
                boolean hasDecimal = false;

                while (i < len && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    if (expr.charAt(i) == '.') {
                        if (hasDecimal) {
                            throw new IllegalArgumentException("Invalid number with multiple decimal points");
                        }
                        hasDecimal = true;
                    }
                    numberBuilder.append(expr.charAt(i));
                    i++;
                }

                try {
                    tokens.add(Double.parseDouble(numberBuilder.toString()));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid number format: " + numberBuilder);
                }
            } else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')') {
                tokens.add(String.valueOf(c));
                i++;
            } else {
                throw new IllegalArgumentException("Invalid character in expression: " + c);
            }
        }

        return tokens;
    }

    private static double evaluateArithmeticTokens(List<Object> tokens) {
        return evaluateExpression(tokens, 0, tokens.size() - 1);
    }

    private static double evaluateExpression(List<Object> tokens, int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException("Invalid expression bounds");
        }

        if (start == end) {
            Object token = tokens.get(start);
            if (token instanceof Double) {
                return (Double) token;
            }
            throw new IllegalArgumentException("Expected number at position " + start);
        }

        int lowestPrecedenceIndex = -1;
        int lowestPrecedence = Integer.MAX_VALUE;
        int parenCount = 0;

        for (int i = start; i <= end; i++) {
            Object token = tokens.get(i);

            if (token instanceof String) {
                String strToken = (String) token;

                if ("(".equals(strToken)) {
                    parenCount++;
                } else if (")".equals(strToken)) {
                    parenCount--;
                    if (parenCount < 0) {
                        throw new IllegalArgumentException("Mismatched parentheses");
                    }
                } else if (parenCount == 0 && OPERATOR_PRECEDENCE.containsKey(strToken)) {
                    int precedence = OPERATOR_PRECEDENCE.get(strToken);
                    if (precedence <= lowestPrecedence) {
                        lowestPrecedence = precedence;
                        lowestPrecedenceIndex = i;
                    }
                }
            }
        }

        if (parenCount != 0) {
            throw new IllegalArgumentException("Mismatched parentheses");
        }

        if (lowestPrecedenceIndex != -1) {
            String operator = (String) tokens.get(lowestPrecedenceIndex);
            double left = evaluateExpression(tokens, start, lowestPrecedenceIndex - 1);
            double right = evaluateExpression(tokens, lowestPrecedenceIndex + 1, end);

            return performArithmeticOperation(left, right, operator);
        }

        if (tokens.get(start) instanceof String && "(".equals(tokens.get(start)) &&
                tokens.get(end) instanceof String && ")".equals(tokens.get(end))) {
            return evaluateExpression(tokens, start + 1, end - 1);
        }

        throw new IllegalArgumentException("Invalid expression structure");
    }

    private static double performArithmeticOperation(double left, double right, String operator) {
        switch (operator) {
            case "+": return left + right;
            case "-": return left - right;
            case "*": return left * right;
            case "/":
                if (Math.abs(right) < 1e-10) {
                    throw new ArithmeticException("Division by zero");
                }
                return left / right;
            default:
                throw new IllegalArgumentException("Unknown arithmetic operator: " + operator);
        }
    }
}
