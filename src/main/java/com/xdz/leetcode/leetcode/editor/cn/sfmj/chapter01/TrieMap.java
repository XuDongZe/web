package com.xdz.leetcode.leetcode.editor.cn.sfmj.chapter01;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 前缀树
 * </pre>
 */
public class TrieMap {

    TrieNode root;

    public TrieMap() {
        root = new TrieNode();
    }

    private static class TrieNode {
        boolean isWord;
        // 26个字符的子节点
        TrieNode[] chirdren;

        public TrieNode() {
            this.isWord = false;
            chirdren = new TrieNode[26];
        }
    }

    /**
     * 插入word
     */
    public void insert(String word) {
        TrieNode p = root, cur = null;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            int idx = ch - 'a';
            cur = p.chirdren[idx];
            if (cur == null) {
                cur = new TrieNode();
                p.chirdren[idx] = cur;
            }
            p = cur;
        }
        if (cur != null) {
            cur.isWord = true;
        }
    }

    public boolean search(String word) {
        TrieNode node = __find(word);
        return node != null && node.isWord;
    }

    public boolean prefix(String word) {
        TrieNode node = __find(word);
        return node != null;
    }

    private TrieNode __find(String word) {
        TrieNode p = root;
        for (int i = 0; i < word.length(); i++) {
            if (p == null) {
                return null;
            }
            int idx = word.charAt(i) - 'a';
            p = p.chirdren[idx];
        }
        return p;
    }

    @Override
    public String toString() {
        List<String> list = new ArrayList<>();
        StringBuilder s = new StringBuilder();
        __dfs(root, list, s);
        return Arrays.toString(list.toArray(new String[]{}));
    }

    private void __dfs(TrieNode node, List<String> list, StringBuilder s) {
        if (node == null) {
            return;
        }
        if (node.isWord) {
            list.add(s.toString());
        }
        for (int i = 0; i < 26; i++) {
            if (node.chirdren[i] != null) {
                s.append((char) (i + 'a'));
                __dfs(node.chirdren[i], list, s);
                s.deleteCharAt(s.length() - 1);
            }
        }
    }

    public static void main(String[] args) {
        String[] words = {"wang", "yibo", "yi", "bo"};
        TrieMap obj = new TrieMap();
        for (String word : words) {
            obj.insert(word);
        }

        System.out.println(obj);

        System.out.println(obj.search("wang"));
        System.out.println(obj.search("b"));
        System.out.println(obj.search("bo"));
        System.out.println(obj.search("wangkk"));
        System.out.println(obj.prefix("ang"));
        System.out.println(obj.prefix("yib"));
    }
}
