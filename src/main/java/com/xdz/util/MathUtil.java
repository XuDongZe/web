package com.xdz.util;

/**
 * Description: math util<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/6/8 23:54<br/>
 * Version: 1.0<br/>
 */
public class MathUtil {

    /**
     * <pre>
     * prime number: n % i == 0 just when i == 1 or i == n.
     * so if there exist one number i let n % i == 0 for i: (1, n), then n is not a prime number
     * otherwise is prime.
     *
     * 1 is not prime for except.
     *
     * O(n), we test (n - 2) times.
     * </pre>
     *
     * @return true if number n is prime.
     */
    public static boolean isPrime(int n) {
        if (n == 1) {
            return false;
        }
        for (int i = 2; i < n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * find the min prime number >= n.
     * n > 1
     */
    public static int nextPrime(int n) {
        for (int i = n; ; i++) {
            if (isPrime(i)) {
                return i;
            }
        }
    }

    public static void main(String[] args) {
        boolean result = isPrime(1);
        result = isPrime(2);
        result = isPrime(3);
        result = isPrime(101);
        int next = nextPrime(100);
        next = nextPrime(next + 1);
        next = nextPrime(next + 1);
    }
}
