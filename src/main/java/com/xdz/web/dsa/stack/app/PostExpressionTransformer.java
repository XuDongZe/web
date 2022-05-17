package com.xdz.web.dsa.stack.app;

import com.google.common.base.Joiner;
import com.xdz.web.dsa.list.IMyList;
import com.xdz.web.dsa.list.MyArrayList;
import com.xdz.web.dsa.stack.IMyStack;
import com.xdz.web.dsa.stack.MyArrayStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: 后缀表达式转换器：中缀=>后缀<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/5/17 12:51<br/>
 * Version: 1.0<br/>
 * <pre>
 * for such an infix-expression, we use + * ( ) to easy the problem:
 * all the operator is two-operand and left-associativity. we can put all those operators easily.
 *
 * a + b * c + (d * e + f) * g, the postfix-expression is:
 * a b c * + d e * f + g * +
 *
 * the core idea is, we use a stack to hold the operators we already see and not apply. that means
 * if we got a new operator, we need to judge when we need to apply: maybe right now or maybe later.
 * the answer is from the math's law: all operator is left-associativity, and operator has its own priority:
 *  1. high priority operator should apply before lower one
 *  2. if priority is same, then for left-associativity, the left one should apple first then the right one.
 *  3. if there is a matched ( and ) found, then should calculate the expression within ().
 * convert the math's law to stack and program process, we got:
 * 1. read infix-expression seq from left to right direction
 * 2. when we got a new operator then compare its priority to the stack top
 *   * stack is empty, no operator waited in the stack. we also do not know should we apply it: push to stack
 *   * stack top is lower priority, then stack top need to be applied now: for math's law 1: pop and record
 *   * stack top is same priority, then stack top need to be applied now : for math's law 2: pop and record
 *   * stack top is higher priority, then stack top still not know when to apply. hold it in stack.
 * 3. after read done, all operators in stack need to applied now. pop until stack is empty.
 *
 * for ( and ) operator, we got:
 * 1. if we got a (, then we should not pop any one. just push it to stack to wait match with ).
 * 2. if we got a ), then all operators should pop from stack util the first ( is pop, because ) come and all operators within the
 * matched ( and ) should apply right now, for math's law 3.
 *
 * () calculate like once function call, yes? we defined a sub-expression use () and when we found there is a sub one,
 * we first finish the sub calculate then return the result and return the right position of parent-expression then continue
 * our calculate: and we accomplish these just use a simple stack(real function call is also the same, using a stack)
 *
 * so stack is very simple (both in implement, concept and time-complexity)
 *
 * now we know an operator has 3 attributes:
 *   name, priority, associativity.
 * now we set the associativity all as left-first. just focus on the priority and record name to result.
 *
 * we also need to identity a tag is operand or operator. but we don't focus on it now.
 *
 * and for operands we will hold the relative-placement in the infix one. so just place operand to result.
 * </pre>
 */

public class PostExpressionTransformer {

    private static final int MIN_PRIORITY = 0;
    private static final Map<Character, Integer> priorityMap = new HashMap<Character, Integer>() {
        {
            put('(', MIN_PRIORITY); // sentinel
            put('+', 1);
            put('*', 2);
        }
    };

    public String transform(String infixExpression) {
        IMyList<Character> result = new MyArrayList<>(infixExpression.length());
        IMyStack<Character> stack = new MyArrayStack<>();
        char[] chars = infixExpression.toCharArray();
        for (char ch : chars) {
            if (ch == ')') {
                while (stack.top() != '(') {
                    result.addLast(stack.pop());
                }
                // pop )
                stack.pop();
                // now the sub-calculate end
            } else if (ch == '(') {
                // now a sub-calculate begin: we hold the parent-calculate and not change anything
                stack.push(ch);
            } else if (isOperator(ch)) {
                while (!stack.isEmpty() && priorityMap.get(stack.top()) >= priorityMap.get(ch)) {
                    result.addLast(stack.pop());
                }
                // now stack top is lower priority than ch
                stack.push(ch);
            } else {
                result.addLast(ch);
            }
        }
        while (!stack.isEmpty()) {
            result.addLast(stack.pop());
        }

        return Joiner.on(" ").join(result);
    }

    private boolean isOperator(char ch) {
        return priorityMap.containsKey(ch);
    }

    public static void main(String[] args) {
        PostExpressionTransformer transformer = new PostExpressionTransformer();
        String result = transformer.transform("a + b * c + (d * e + f) * g".replaceAll(" ", ""));
        System.out.println(result);
    }
}
