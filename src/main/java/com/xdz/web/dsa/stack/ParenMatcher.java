package com.xdz.web.dsa.stack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Description: 括号匹配算法实现<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/5/10 22:17<br/>
 * Version: 1.0<br/>
 */
public class ParenMatcher {
    private static final Map<Character, Character> matchMap = new HashMap<Character, Character>() {
        {
            put('[', ']');
            put('{', '}');
            put('(', ')');
        }
    };
    private static final Set<Character> rightSet = new HashSet<>(matchMap.values());

    public static boolean match(String p) {
        if (p == null || p.isEmpty()) {
            return true;
        }
        char[] data = p.toCharArray();
        IMyStack<Character> stack = new MyArrayStack<Character>();
        for (char ch : data) {
            if (matchMap.containsKey(ch)) {
                stack.push(ch);
            } else if (rightSet.contains(ch)) {
                if (stack.isEmpty() || matchMap.get(stack.pop()) != ch) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    public static void main(String[] args){
        System.out.println(match("[](){}"));
        System.out.println(match("[]{"));
        System.out.println(match("[}"));
        System.out.println(match("(([[]]))"));
    }
}
