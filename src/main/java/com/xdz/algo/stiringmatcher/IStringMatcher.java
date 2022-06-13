package com.xdz.algo.stiringmatcher;

public interface IStringMatcher {
    /**
     * 在source串中找到target字串，返回首次出现的下标
     * 不存在则返回-1
     */
    int match(String source, String target);
}
