package com.xdz.dsa.string;

import org.checkerframework.checker.units.qual.A;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class Alphabet implements IAlphabet {
    public static Alphabet LOWERCASE = new Alphabet("abcdefghijklmnopqrstuvwxyz");
    public static Alphabet UPPPERCASE = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    public static Alphabet BASE62 = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxz0123456789");
    public static Alphabet BASE64 = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxz0123456789+/");
    public static Alphabet ASCII = new Alphabet("\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\t\n\u000B\f\n\u000E\u000F\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001A\u001B\u001C\u001D\u001E\u001F !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u007F");

    private int R;
    private int logR;
    private char[] tables;
    private Map<Character, Integer> indexMap;

    public Alphabet(String rawString) {
        if (rawString == null || rawString.isEmpty()) {
            throw new RuntimeException("Alphabet string is null or empty");
        }

        char[] charArray = rawString.toCharArray();
        Set<Character> set = new LinkedHashSet<>();
        for (char c : charArray) {
            set.add(c);
        }

        char[] t = new char[set.size()];
        int idx = 0;
        for (char c : set) {
            t[idx++] = c;
        }
        this.tables = t;

        Map<Character, Integer> map = new HashMap<>(this.tables.length);
        for (int i = 0; i < this.tables.length; i++) {
            map.put(this.tables[i], i);
        }
        this.indexMap = map;

        this.R = this.tables.length;
        this.logR = (int) Math.ceil((Math.log(R) / Math.log(2)));
    }

    @Override
    public char toChar(int index) {
        return tables[index];
    }

    @Override
    public int toIndex(char ch) {
        return indexMap.get(ch);
    }

    @Override
    public boolean contains(char ch) {
        return indexMap.containsKey(ch);
    }

    @Override
    public int R() {
        return R;
    }

    @Override
    public int logR() {
        return logR;
    }

    @Override
    public int[] toIndices(String s) {
        int[] ans = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            ans[i] = toIndex(s.charAt(i));
        }
        return ans;
    }

    @Override
    public String toChars(int[] indices) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indices.length; i++) {
            sb.append(toChar(indices[i]));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        StringBuilder asciiString = new StringBuilder();

        for (int i = 0; i < 128; i++) {
            asciiString.append((char) i);  // 将 ASCII 码转换为字符并追加到字符串中
        }

        String result = asciiString.toString();
        System.out.println(result);
    }
}
