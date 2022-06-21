package com.xdz.dsa.unionfindset;

/**
 * Description: union-find & opt<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/6/21 23:09<br/>
 * Version: 1.0<br/>
 */
public class MyUnionFindSet implements IMyUnionFindSet {

    private final static Version inner = Version.UnionByHeightWithRouteCompression;

    @Override
    public int find(int n) {
        return inner.find(n);
    }

    @Override
    public int union(int root1, int root2) {
        return inner.union(root1, root2);
    }

    private enum Version implements IMyUnionFindSet {

        /**
         * <pre>
         * array[i] = a, means element i's parent is a.
         * init with array[i] = -1 to indicate i has no parent.
         * </pre>
         */
        Simple {
            @Override
            public int find(int n) {
                int p = array[n];
                if (p < 0) {
                    return n;
                } else {
                    return find(p);
                }
            }

            /**<pre>
             * simple let {b} join to {a}.
             * after join {a} -> {a, b}, {b} removed
             * </pre>
             */
            @Override
            public int union(int root1, int root2) {
                return array[root2] = root1;
            }
        },

        /**
         * <pre>
         * array[i] == a means i-node's parent is a.
         * for init array[i] == -1, and we store the set's size at the set-root.
         * so array[set-root] means the -num-amount of set. for example:
         * {1, 2, 3, 4} and the root is 1. so array[i] maybe:
         * array[1] = -3, (3 is the nums of set, and < 0 means 1 is the set-root)
         * array[2] = 1 (we first union 1 & 2; if size is the same, we just union b to a just like SIMPLE)
         * array[3] = 1 (we then union 2 & 3; and use route-compression)
         * array[4] = 1 (we then union 4 & 1; and use union-by-size, so more-size node 1 is pick for root)
         *
         * we apply route-compression in find op for find is a recursive-algo.
         * </pre>
         */
        UnionBySizeWithRouteCompression {
            @Override
            public int find(int n) {
                int p = array[n];
                if (p < 0) {
                    return n;
                } else {
                    // we let the route (n -> root)'s all node directly parent to root.
                    return array[n] = find(p);
                }
            }

            @Override
            public int union(int root1, int root2) {
                if (array[root1] >= 0) {
                    root1 = find(root1);
                }
                if (array[root2] >= 0) {
                    root2 = find(root2);
                }

                int a = root1, b = root2;
                if (array[root2] < array[root1]) {
                    a = root2;
                    b = root1;
                }
                // union {b} -> {a}
                array[a] = array[a] + array[b];
                return array[b] = a;
            }
        },

        UnionByHeightWithRouteCompression {
            @Override
            public int find(int n) {
                int p = array[n];
                if (p < 0) {
                    return n;
                } else {
                    return array[n] = p;
                }
            }

            @Override
            public int union(int root1, int root2) {
                if (array[root1] >= 0) {
                    root1 = find(root1);
                }
                if (array[root2] >= 0) {
                    root2 = find(root2);
                }

                if (array[root2] < array[root1]) {
                    // height information not changed at root1
                    // {root1} -> {root2} for root2 is deeper
                    return array[root1] = root2;
                } else {
                    if (array[root2] == array[root1]) {
                        // root1's deep incr 1
                        array[root1]--;
                    }
                    return array[root2] = root1;
                }
            }
        };

        int[] array;

        public void reset(int n) {
            array = new int[n];
            for (int i = 0; i < n; i++) {
                array[i] = -1;
            }
        }
    }

    public static void main(String[] args) {
        // 1 & 2, 2 & 3, 4 & 1
        for (Version version : Version.values()) {
            // init for {0} {1} {2} {3}
            version.reset(4);
            // {0, 1} -> {0}
            int root = version.union(0,1);
            root = version.find(0);
            root = version.find(1);
            root = version.union(1, 2);
            root = version.union(3, 0);
            root = version.find(2);
            root = version.find(3);
        }
    }
}
