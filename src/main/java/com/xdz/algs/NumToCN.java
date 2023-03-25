package com.xdz.algs;

// 大部分回答的似是而非

// 浏览器中键入一个url到浏览器页面渲染出来，经历了哪些过程
// tcp TIME_WAIT状态 什么造成的
// https握手过程
// tcp四次挥手过程
// redis zset 数据结构
// redis 主从复制 集群 哨兵 选举 通信 slot切片

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 数字转汉字
 * 数字范围：0 ~ 9999_9999_9999。非负整数，非小数 => 非负数（包含小数） => 实数（包含负数）
 *
 * test case:
 *  1. 1111_1111_1111 => 一千一百一十一亿 一千一百一十一万 一千一百一十一 元整
 *  2. 0~9 => 零元整 壹元整 贰元整 ...
 *  3. 10 => 拾
 *  4. 11 => 壹拾壹 || 拾壹
 *  5. 101 => 壹佰零壹
 *  6. 10001 => 壹万零壹
 *  7. 1_0001_0001 => 壹亿零壹万零壹
 *
 * 规则归纳：
 *  1. 基本单位是：个 十 百 千 万 亿
 *  2. 两个层次：4位数字一个阶段。亿 万 个，每个阶段的单位是：千 百 十 个
 *  3. 零压缩
 *  4. 口语化
 *  5. 小数 点 + 直接汉字对应
 *  6. 负数 判断一下符号
 */
public class NumToCN {

    private static final String[] CN_STEPS = new String[] {"", "萬", "億"}; // "个" 省略为空: ""
    private static final String[] CN_UNITS = new String[] {"", "拾", "佰", "仟"}; // "个" 省略为空: ""
    private static final String[] CN_CHARS = new String[] {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
    private static final String POST_FIXED = "元整";

    private static final Map<String, Integer> UNIT_BASE_MAP = Maps.newHashMap();

    static {
        UNIT_BASE_MAP.put("",1);
        UNIT_BASE_MAP.put("拾",10);
        UNIT_BASE_MAP.put("佰",100);
        UNIT_BASE_MAP.put("仟",1000);
        UNIT_BASE_MAP.put("萬",1_0000);
        UNIT_BASE_MAP.put("億",1_0000_0000);
    }

    /**
     * positive integer converter
     * num: [0]
     */
    private static String convert(long num) {
        /**
         * basic parse
         */
        if (num >= 0 && num <= 9) {
            return CN_CHARS[(int) num];
        }

        // 1. 1111_1111_1111 => 一千一百一十一亿 一千一百一十一万 一千一百一十一 元整
        /**
         * first parse step "亿". 有几个 "亿" 呢？
         */
        StringBuilder result = new StringBuilder();
        for (int stepIdx = CN_STEPS.length - 1; stepIdx >= 0; stepIdx --) {
            num = parseStep(num, stepIdx, result);
            if (num <= 0) {
                break;
            }
        }

        // parse result.
        return result.toString();
    }

    /**
     * start parse stepIdx, num is initial number.
     * save parse result in StringBuilder.
     * @return after-parse number
     */
    private static long parseStep(long num, int stepIdx, StringBuilder result) {
        int stepBase = UNIT_BASE_MAP.get(CN_STEPS[stepIdx]);
        long unitNums = num / stepBase;
        if (unitNums == 0) {
            // do-nothing todo
            return num;
        }

        num = parseUnit(num, result);
        return num;
    }

    /**
     * @param num: range [0, 9999]。翻译为
     * @param result
     */
    private static long parseUnit(long num, StringBuilder result) {
        for (int unitIdx = CN_UNITS.length - 1; unitIdx >= 0; unitIdx--) {
            int base = UNIT_BASE_MAP.get(CN_UNITS[unitIdx]);
            long n = (num / base);
            assert n >= 0;
            if (n == 0) {

            } else {
                result.append(CN_CHARS[(int) n]);
                result.append(CN_UNITS[unitIdx]);
            }
            num -= n * base;
        }
        return num;
    }

    public static void main(String[] args) {
        long num = 1111_1111_1111L;
        String result = convert(num);
        System.out.println(result);
    }
}