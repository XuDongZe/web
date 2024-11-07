package com.xdz.dsa.string;

/**
 * 字母表
 */
public interface IAlphabet {
    
    /**
     * 获取字母表索引位置为index处的字符
     */
    char toChar(int index);

    /**
     * 获取字符ch在字母表中的索引
     * valid index 0~R-1
     * not found: -1
     */
    int toIndex(char ch);

    /**
     * 字母表中包含字符ch吗
     */
    boolean contains(char ch);

    /**
     * num of chars of 字母表
     */
    int R();

    /**
     * 表示一个索引需要的比特数
     */
    int logR();

    /**
     * 字符串s的R进制整数序列表示：结果数组中的第i个元素 是 s的第i个字符 在字母表中的索引
     * ans[idx] = toIndex(s[index])
     */
    int[] toIndices(String s);

    /**
     * toIndices的逆过程 将一个索引列表转换为一个字符串
     */
    String toChars(int[] indices);
}
