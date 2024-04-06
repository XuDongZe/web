package com.xdz.leetcode.leetcode.editor.cn.sfmj.chapter01;

import com.google.common.collect.Maps;
import com.xdz.leetcode.leetcode.editor.cn.pjs.__dfs_2_1_4_1;
import io.reactivex.Single;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * <pre>
 * 哈夫曼树: 无损 二进制比特压缩  权值最小
 *
 * 要求知道元数据的全貌 解码时需要知道密码本
 *
 * 数据传输方: 统计字符频率 构造哈夫曼树 计算密码本：将密码本和压缩后的无损数据 一起传输
 * 数据接收方: 使用密码本 构造哈夫曼树(或者不需要构造树 直接使用密码本解密即可)
 *
 * </pre>
 */
public class HuffmanTree {

    private HNode root;
    private Map<Character, String> encodeMap = new HashMap<>(); // 编码 密码本

    private static class HNode {
        private char ch; // 叶节点值
        private int weight; // 权重
        private HNode left;
        private HNode right;

        /**
         * 叶节点
         */
        public HNode(char ch, int weight) {
            this.ch = ch;
            this.weight = weight;
        }

        /**
         * 非叶节点
         */
        public HNode(int w, HNode l, HNode r) {
            this.weight = w;
            this.left = l;
            this.right = r;
        }
    }

    public HuffmanTree(String str) {
        if (str == null || str.isEmpty()) {
            throw new RuntimeException("empty raw");
        }
        this.root = __build(str);
        __buildEncodeMap(this.root, this.encodeMap, new StringBuilder());
    }

    private HNode __build(String str) {
        // 字符频率统计
        Map<Character, Integer> counter = new HashMap<>();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            counter.put(ch, counter.getOrDefault(ch, 0) + 1);
        }
        // 优先队列
        Queue<HNode> queue = new PriorityQueue<>(Comparator.comparingInt((node) -> node.weight));
        counter.forEach((ch, weight) -> queue.add(new HNode(ch, weight)));
        // 贪心 构造哈夫曼树
        while (queue.size() > 1) {
            HNode left = queue.poll();
            HNode right = queue.poll();
            HNode p = new HNode(left.weight + right.weight, left, right);
            queue.add(p);
        }
        // 树根
        return queue.poll();
    }

    /**
     * 从树根node开始编码 将每个字符的编码结果放入map中
     */
    private void __buildEncodeMap(HNode node, Map<Character, String> map, StringBuilder bits) {
        if (node == null) {
            return;
        }
        if (__isLeaf(node)) {
            // 先序遍历 兼容一下 node==root的情况 即只有一个根节点 此时bits是空的 随便给个0或者1都可以
            map.put(node.ch, bits.length() == 0 ? "0" : bits.toString());
            return;
        }

        bits.append("0");
        __buildEncodeMap(node.left, map, bits);
        bits.deleteCharAt(bits.length() - 1);

        bits.append("1");
        __buildEncodeMap(node.right, map, bits);
        bits.deleteCharAt(bits.length() - 1);
    }

    /**
     * 基于已经构造好的哈夫曼树
     * 输出字符串str的哈夫曼编码
     * <p>
     * 我们认为给定的str是符合提前构建好的encodeMap的分布的
     */
    public String encode(String str) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            String encodeStr = encodeMap.get(str.charAt(i));
            if (encodeStr == null) {
                throw new RuntimeException("can not encode char: " + str.charAt(i));
            }
            s.append(encodeStr);
        }
        return s.toString();
    }

    /**
     * 将编码值转化为叶节点列表
     */
    public String decode(String encodeStr) {
        StringBuilder ans = new StringBuilder();
        int idx = 0;
        while (idx < encodeStr.length()) {
            // 每找到一个叶节点编码值 就返回根节点 重新查找下一个
            idx = __decode(root, encodeStr, idx, ans);
        }
        return ans.toString();
    }

    /**
     * 树根为node 根据encodeStr[idx]的指示向左或者向右 找到叶节点
     * node is not null
     */
    private int __decode(HNode node, String encodeStr, int idx, StringBuilder ans) {
        if (node == null) {
            throw new RuntimeException("decode error. node is null");
        }
        if (__isLeaf(node)) {
            ans.append(node.ch);
            return idx;
        }
        if (idx >= encodeStr.length()) {
            // node is not leaf but encodeStr is done
            throw new RuntimeException("decode error. idx: " + idx + " encodeStr: " + encodeStr);
        }

        char ch = encodeStr.charAt(idx);
        // 尾递归
        if (ch == '0') {
            return __decode(node.left, encodeStr, idx + 1, ans);
        } else {
            return __decode(node.right, encodeStr, idx + 1, ans);
        }
    }

    private boolean __isLeaf(HNode node) {
        return node.left == null && node.right == null;
    }

    public static void main(String[] args) {
        Map<Character, Integer> map = Maps.newHashMap();
        map.put('a', 50);
        map.put('b', 2);
        map.put('c', 5);
        map.put('d', 10);
        StringBuilder s = new StringBuilder();
        map.forEach((ch, w) -> {
            for (int i = 0; i < w; i ++) {
                s.append(ch);
            }
        });

        HuffmanTree obj = new HuffmanTree(s.toString());

        String encode = obj.encode(s.toString());
        System.out.println(s.length() + "\t" + s);
        System.out.println(encode.length() + "\t" + encode);
        String decode = obj.decode(encode);
        System.out.println(s.toString().equals(decode));
    }
}
