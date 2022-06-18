package com.xdz.dsa.stack.app;

import com.xdz.dsa.stack.MyArrayStack;
import com.xdz.dsa.stack.IMyStack;

/**
 * Description: 后缀表达式求值<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/5/16 21:59<br/>
 * Version: 1.0<br/>
 *
 * post expression, come from usual expression calculation activity.
 * it is un-common compare with the mid-expression we use, but one the contrary it
 * is common with our thinking: so it's not difficult and in fact we are familiar with the thinking process.
 *
 * for example, here is a mid-express. to easy the problem and get the core idea of common-thinking, we choose one not contains ( or ).
 * it's simple: 4 * 2 + 5 * 3 - 6 / 2
 *
 * the process we calculate it is:
 * 1. got 4 and 2, and use * to operate the pair. then we got 8, the result to replace 4 * 2
 * 2. got 5 and 3, and use * to operate the pair. then we got 15.
 * 3. got 8 and 15, and use +, then we got 23
 * 4. got 6 and 2, and use /, then we got 3
 * 5. got 23 and 3, and use -,then we got 20
 * the result is 20.
 *
 * the op seq we talk is:
 * 4 2 * 5 3 * + 6 2 / -
 * and that is what we called postfix-expression.
 *
 * so if we use postfix-expression to calculate, we not need to consider priority of operations.
 *
 * so the core idea is: got the operator to op and find numbers. then op them.
 * and save the op result to a hold the result(we will use the result after)
 */
public class PostExpressionCalculator {

    /**
     * we do not consider the number identity.
     *
     * we just pass one on the seq. so time is O(n).
     */
    public int calculate(String postfixExpression) {
        IMyStack<Integer> opNumStack = new MyArrayStack<>();
        char[] chars = postfixExpression.toCharArray();
        for (char ch : chars) {
            if (ch >= '0' && ch <= '9') {
                // save params and we will use it later.
                opNumStack.push(ch - '0');
            } else {
                // now it is an operator. handle it.
                int result = op(ch, opNumStack);
                opNumStack.push(result);
            }
        }
        return opNumStack.pop();
    }

    // it seems like a function call: you pass the func params using a stack and return the func result using a same stack.
    private int op(char operator, IMyStack<Integer> operandStack) {
        // we just consider two-operand operators.
        // we first pop op2(the operand we read later) and then pop op1.
        // notice the order for some op such as - or / is not symmetry(对称性)
        return doTwoOperandOp(operator, operandStack.pop(), operandStack.pop());
    }

    private int doTwoOperandOp(char operator, int op2, int op1) {
        switch (operator) {
            case '+':
                return op1 + op2;
            case '-':
                return op1 - op2;
            case '*':
                return op1 * op2;
            case '/':
                return op1 / op2;
            default:
                throw new RuntimeException("operator is not defined: " + operator);
        }
    }

    public static void main(String[] args){
        PostExpressionCalculator calculator = new PostExpressionCalculator();
        int result = calculator.calculate("4 2 * 5 3 * + 6 2 / -".replace(" ", ""));
        System.out.println(result);
    }
}
