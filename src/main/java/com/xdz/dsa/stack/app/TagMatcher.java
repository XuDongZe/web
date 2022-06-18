package com.xdz.dsa.stack.app;

import com.xdz.dsa.stack.MyArrayStack;
import com.xdz.dsa.stack.IMyStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Description: 括号匹配算法实现<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/5/10 22:17<br/>
 * Version: 1.0<br/>
 *
 * tag-match。problem is: text is compose with matched pair of tag, that means each tag has one
 * open tag and one close tag, open and close tag are matched.
 *
 * give you such a text, judge weather it's matched.
 *
 * 1. init an empty stack. it will hold all not-matched open tag.
 * 2. loop scan the text step by tag: that's read the text tag after tag(maybe you need parse the text
 * to tags and then for-each or maybe ignore some words)
 * 3. each of loop, you get a tag:
 *   * if it's open tag: push to stack
 *   * if it's close tag. try to match with the stack top.
 *     * stack is empty: close tag is more. NOT MATCH
 *     * stack top is not matched with current close tag: count is right but type is not match. NOT MATCH
 *     * stack top is matched. pop it from stack, that means no need to wait for a close-tag to match. and then step to next tag.
 * 4. after tag-read-loop,analyse the stack:
 *   * if it's not empty: open tag is more than close tag. NOT MATCHED
 *   * if it's emtpy: all open tag is matched. MATCHED
 */
public class TagMatcher {
    private static final Map<Character, Character> matchMap = new HashMap<Character, Character>() {
        {
            put('[', ']');
            put('{', '}');
            put('(', ')');
        }
    };
    private static final Set<Character> closeTagSet = new HashSet<>(matchMap.values());

    public static boolean match(String p) {
        if (p == null || p.isEmpty()) {
            return true;
        }
        char[] data = p.toCharArray();
        IMyStack<Character> stack = new MyArrayStack<Character>();
        for (char ch : data) {
            if (isOpenTag(ch)) {
                stack.push(ch);
            } else if (isCloseTag(ch)) {
                if (stack.isEmpty() || matchMap.get(stack.pop()) != ch) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    private static boolean isOpenTag(char ch) {
        return matchMap.containsKey(ch);
    }

    private static boolean isCloseTag(char ch) {
        return closeTagSet.contains(ch);
    }

    public static void main(String[] args){
        System.out.println(match("[](){}"));
        System.out.println(match("[]{"));
        System.out.println(match("[}"));
        System.out.println(match("(([[]]))"));
    }
}
