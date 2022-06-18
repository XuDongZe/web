package com.xdz.dsa.string.matcher;

public enum StringMatcher implements IStringMatcher {
    /**
     * 每次匹配开始时，主串从index开始, 模式串从0开始。
     * 匹配结束后(某个字符不匹配或者匹配完毕)，主串为i，模式串为j
     * 且每次移动，i j同步加1。有：
     * 主串：  index => i
     * 模式串：0 => j
     * i - index = j - 0 => index = i - j
     *
     * 即，若匹配结束位置为(i, j)，则主串中匹配开始位置为i - j。
     *
     * 时间复杂度：O(n * m)。n为source.length() m为target.length()：每次匹配，子串从0到最后，匹配失败主串移动一个字符，所以匹配次数为n。
     * 实际很多在中途就匹配失败了。效率会比理论高。
     * 最坏复杂度来自于每次匹配，都到最后一个字符才匹配失败：
     * source: aaaaaaaaa
     * target: aaaab
     *
     * 空间复杂度：O(1)
     */
    BF {
        @Override
        public int doMatch(String source, String target) {
            int i = 0, j = 0;
            int sl = source.length(), tl = target.length();
            while (i < sl && j < tl) {
                if (source.charAt(i) == target.charAt(j)) {
                    i ++;
                    j ++;
                } else {
                    // 主串回溯到本次匹配起始位置的下一个字符
                    i = i - j + 1;
                    // 模式串回溯到第一个字符
                    j = 0;
                }
            }
            return j == tl ? i - j : -1;
        }
    },


    KMP {
        @Override
        protected int doMatch(String source, String target) {
            int[] next = getNext(target);
            int i = 0, j = 0;
            int sl = source.length(), tl = target.length();
            while (i < sl && j < tl) {
                if (source.charAt(i) == target.charAt(j)) {
                    i ++;
                    j ++;
                } else if (j > 0) {
                    j = next[j - 1];
                } else {
                    i ++;
                }
            }
            return j == tl ? i - j : -1;
        }

        private int[] getNext(String target) {
            int[] next = new int[target.length()];
            // init
            next[0] = 0;
            int tmp = 0;

            // next[0] ~ next[i - 1] => next[i]
            int i = 1;
            int len = target.length();
            while (i < len) {
                if (target.charAt(i) == target.charAt(tmp)) {
                    tmp ++;
                    next[i] = tmp;
                    i ++;
                } else if (tmp > 0) {
                    tmp = next[tmp - 1];
                } else {
                    next[i] = 0;
                    i ++;
                }
            }
            return next;
        }
    }

    ;
    @Override
    public int match(String source, String target) {
        if (source == null) {
            return -1;
        }
        if (target == null || target.length() == 0) {
            return 0;
        }

        int sl = source.length(), tl = target.length();
        // fast-fail
        if (sl < tl) {
            return -1;
        }

        return doMatch(source, target);
    }

    protected abstract int doMatch(String source, String target);

    public static void main(String[] args) {
        String s = "ababaabaabac";
        String t = "abaabac";
        System.out.println(StringMatcher.BF.match(s, t));
        System.out.println(StringMatcher.KMP.match(s, t));
    }
}
