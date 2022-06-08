package com.xdz.web.dsa.tree.huffman;

import com.google.common.collect.Maps;
import com.xdz.web.dsa.list.MyDoubleLinkedList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Description: huffman-tree & decode encode<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/6/4 12:15<br/>
 * Version: 1.0<br/>
 */
public class MyHuffmanTree {

    private Node root;

    public static MyHuffmanTree create(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        List<Map.Entry<Character, Integer>> countMap = count(input);
        MyDoubleLinkedList<Node> nodes = new MyDoubleLinkedList<>();
        countMap.forEach((entry) -> nodes.addLast(new Node(entry.getKey(), entry.getValue())));

        while (nodes.size() >= 2) {
            // one loop remove 2 and add 1 element, so size decr 1
            Node left = nodes.removeFirst();
            Node right = nodes.removeFirst();
            Node root = new Node(left.weight + right.weight, left, right);
            nodes.addAdjust(root, Comparator.comparingInt((Node o) -> o.weight));
        }

        MyHuffmanTree tree = new MyHuffmanTree();
        tree.root = nodes.get(0);
        return tree;
    }

    /**
     * 对rawData进行编码
     */
    public static Context encode(String rawData) {
        MyHuffmanTree tree = MyHuffmanTree.create(rawData);
        Map<Character, String> codeMap = tree.getCodeMap();
        StringBuilder builder = new StringBuilder();
        for (Character ch : rawData.toCharArray()) {
            builder.append(codeMap.get(ch));
        }

        return new Context(rawData, tree, codeMap, builder.toString());
    }

    /**
     * 使用context对context.encryptData进行解码
     * @param context
     * @return
     */
    public static String decode(MyHuffmanTree tree, String encryptData) {
        int idx = 0;
        Node node = null;
        StringBuilder result = new StringBuilder();
        while (idx < encryptData.length()) {
            // reset for a new char routine
            node = tree.root;
            while (!node.isLeaf()) {
                // read one and move encryptData pos
                char ch = encryptData.charAt(idx++);
                if (ch == '0') {
                    node = node.left;
                } else if (ch == '1') {
                    node = node.right;
                }
            }
            // now node.isLeaf. && assert node != null
            result.append(node.ch);
        }
        return result.toString();
    }

    private static List<Map.Entry<Character, Integer>> count(String input) {
        TreeMap<Character, Integer> map = Maps.newTreeMap();
        for (char ch : input.toCharArray()) {
            map.merge(ch, 1, Integer::sum);
        }
        // sort by value, help by entry-list sort
        List<Map.Entry<Character, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Comparator.comparingInt(Map.Entry::getValue));
        return list;
    }

    private Map<Character, String> getCodeMap() {
        Map<Character, String> map = Maps.newHashMap();
        getCodeMap(root, null, "", map);
        return map;
    }

    /**
     * we pass route `code` and then arrive node. and current codes is `codes` and current codeMap is `map`
     */
    private void getCodeMap(Node node, String code, String codes, Map<Character, String> map) {
        if (node == null) {
            return;
        }
        // root-pre-order
        if (code != null) {
            codes += code;
        }
        if (node.isLeaf()) {
            map.put(node.ch, codes);
        } else {
            getCodeMap(node.left, "0", codes, map);
            getCodeMap(node.right, "1", codes, map);
        }
    }

    private static class Node {
        Character ch;
        int weight;
        Node left;
        Node right;

        public Node(int weight) {
            this.weight = weight;
        }

        public Node(int weight, Node left, Node right) {
            this.weight = weight;
            this.left = left;
            this.right = right;
        }

        public Node(char ch, int weight) {
            this.ch = ch;
            this.weight = weight;
        }

        public boolean isLeaf() {
            return ch != null;
        }
    }

    public static class Context {
        String rawData;
        MyHuffmanTree tree;
        Map<Character, String> codeMap;
        String encryptData;

        public Context(String rawData, MyHuffmanTree tree, Map<Character, String> codeMap, String encryptData) {
            this.rawData = rawData;
            this.encryptData = encryptData;
            this.tree = tree;
            this.codeMap = codeMap;
        }
    }

    public static void main(String[] args) {
        String msg = "i love java";
        Context context = MyHuffmanTree.encode(msg);
        context.codeMap.forEach((ch, code) -> System.out.println(ch + "\t" + code));
        String result = MyHuffmanTree.decode(context.tree, context.encryptData);
        System.out.println(result);
    }
}
