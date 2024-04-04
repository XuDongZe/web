package com.xdz.leetcode.leetcode.editor.cn.sfmj.chapter01;

/**
 * <pre>
 * 线段树
 *
 * 解决的问题：
 * 数组a[] 单点修改 单点查询 区间修改 区间查询(区间最值、区间和等)
 *
 * 使用的是类二叉树的构造
 *
 * </pre>
 */
public class SegmentTree {
    private static class SegmentTreeNode {
        int l, r; // 区间
        int sum; // 区间和
        int addLazy; // 懒标记

        public SegmentTreeNode(int l, int r) {
            this.l = l;
            this.r = r;
        }
    }

    int a[];
    SegmentTreeNode[] tr;

    public SegmentTree(int a[]) {
        this.a = a;
        int n = a.length;
        tr = new SegmentTreeNode[4 * n]; // 开一个4*n的数组
        __build(1, 0, n - 1);
    }

    /**
     * 构造节点 区间为[l, r] 节点编号为u
     */
    private void __build(int u, int l, int r) {
        tr[u] = new SegmentTreeNode(l, r);
        if (l == r) { // 叶节点
            tr[u].sum = a[l];
        } else {
            // 向下构建
            int mid = (l + r) >>> 1;
            __build(u << 1, l, mid);
            __build(u << 1 | 1, mid + 1, r);
            // 回溯
            tr[u].sum = tr[u << 1].sum + tr[u << 1 | 1].sum;
        }
    }

    /**
     * 单点查询 叶子节点
     * 返回a[i]的值
     */
    public int query(int i) {
        return __query(1, i);
    }

    /**
     * 根节点为u 的树中查找在 nums数组中 idx==i的值
     * 返回nums[i]
     */
    private int __query(int u, int i) {
        int l = tr[u].l, r = tr[u].r;
        if (l == r) {
            return tr[u].sum;
        } else {
            int mid = (l + r) >>> 1;
            if (i <= mid) return __query(u << 1, i);
            else return __query(u << 1 | 1, i);
        }
    }

    /**
     * 单点修改
     * a[i] += v
     */
    public void update(int i, int v) {
        __update(1, i, v);
    }

    /**
     * 在根节点为u的树中 idx==i的叶子节 +=k
     * 注意 因为是树型结构 叶子节点 += k后 父节点也得修改
     */
    private void __update(int u, int i, int v) {
        int l = tr[u].l, r = tr[u].r;
        if (l == r) {
            tr[u].sum += v;
        } else {
            int mid = (l + r) >>> 1;
            if (i <= mid) __update(u << 1, i, v);
            else __update(u << 1 | 1, i, v);
            // 回溯时 更新本节点值
            tr[u].sum = tr[u << 1].sum + tr[u << 1 | 1].sum;
        }
    }

    /**
     * 区间查询
     * a[x, y] 的区间和
     */
    public int queryRange(int x, int y) {
        return __queryRange(1, x, y);
    }

    /**
     * 在子树u中 查找 对 区间[l,r]的区间和 的贡献
     */
    private int __queryRange(int u, int x, int y) {
        int l = tr[u].l, r = tr[u].r;
        // 如果[l,r] 完全属于 [x,y] 直接计算贡献
        if (x <= l && r <= y) {
            return tr[u].sum;
        } else {
            int mid = (l + r) >>> 1;
            int ans = 0;
            if (x <= mid) ans += __queryRange(u << 1, x, y);
            if (y > mid) ans += __queryRange(u << 1 | 1, x, y);
            return ans;
        }
    }

    /**
     * 区间修改
     * a[x,y]区间内的每一个值 += v
     * O(n)
     */
    public void updateRange(int x, int y, int v) {
        __updateRange(1, x, y, v);
    }

    /**
     * 在根节点u的树中 [x,y]区间内的 += v
     */
    private void __updateRange(int u, int x, int y, int v) {
        int l = tr[u].l, r = tr[u].r;
        if (l == r) {
            tr[u].sum += v;
        } else {
            int mid = (l + r) >>> 1;
            if (x <= mid) __updateRange(u << 1, x, y, v);
            if (y > mid) __updateRange(u << 1 | 1, x, y, v);
            // 回溯
            tr[u].sum = tr[u << 1].sum + tr[u << 1 | 1].sum;
        }
    }

    /**
     * 懒标记优化 的 区间修改
     */
    public void updateRangeLazy(int x, int y, int v) {
        __updateRangeLazy(1, x, y, v);
    }

    /**
     * 在根节点为u的树 中 更新 a[x, y]区间 += v
     */
    private void __updateRangeLazy(int u, int x, int y, int v) {
        int l = tr[u].l, r = tr[u].r;
        // 如果[l, r] 完全属于 [x, y]
        // 加一个懒标记即可 不需要更新其子节点 也就是不需要到叶节点
        if (x <= l && r <= y) {
            tr[u].sum += (r - l + 1) * v; // 区间中的每个值都 += v 因此[l,r]的区间和 += len * v
            // 增加懒标记 含义是: 该节点的区间和 已经更新 但是其子节点没有更新
            tr[u].addLazy += v; // 子节点需要更新v值
        } else {
            // 在进入子节点修改之前(本节点的sum会被子节点的sum影响) 必须要将本节点的懒标记下发到子节点中
            // 这样子节点的sum值才是对的 回溯过来后本节点的sum值也就是对的
            pushDown(u);

            int mid = (l + r) >>> 1;
            if (x <= mid) __updateRangeLazy(u << 1, x, y, v);
            if (y > mid) __updateRangeLazy(u << 1 | 1, x, y, v);
            // 回溯
            tr[u].sum = tr[u << 1].sum + tr[u << 1 | 1].sum;
        }
    }

    /**
     * 懒标记优化 区间查询
     * 返回 a[x,y]区间的 区间和
     */
    public int queryRangeLazy(int x, int y) {
        return __queryRangeLazy(1, x, y);
    }

    /**
     * 在根节点u中 寻找 对[x,y]的区间和贡献
     */
    private int __queryRangeLazy(int u, int x, int y) {
        int l = tr[u].l, r = tr[u].r;
        if (x <= l && r <= y) {
            // 不用管lazy sum已经包含了lazy操作 在本节点上
            return tr[u].sum;
        } else {
            // 使用子节点的sum之前 先应用lazy
            pushDown(u);

            int ans = 0;
            int mid = (l + r) >>> 1;
            if (x <= mid) ans += __queryRangeLazy(u << 1, x, y);
            if (y > mid) ans += __queryRangeLazy(u << 1 | 1, x, y);
            return ans;
        }
    }

    /**
     * 下发节点u的lazy操作到它的儿子节点
     */
    private void pushDown(int u) {
        int l = tr[u].l, r = tr[u].r, mid = (l + r) >>> 1;
        if (tr[u].addLazy != 0) {
            // addLazy最终加到u节点的子节点
            tr[u << 1].sum += (mid - l + 1) * tr[u].addLazy;
            tr[u << 1 | 1].sum += (r - mid) * tr[u].addLazy;
            // 继续向下传递
            tr[u << 1].addLazy += tr[u].addLazy;
            tr[u << 1 | 1].addLazy += tr[u].addLazy;
            // 清空 不要重复计算 已经被子节点继承了
            tr[u].addLazy = 0;
        }
    }

    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        int n = nums.length;
        SegmentTree obj = new SegmentTree(nums);
        // 单点查询
        System.out.println(obj.query(0));
        System.out.println(obj.query(n - 1));
        // 单点修改
        obj.update(0, 1);
        System.out.println(obj.query(0));
        obj.update(0, -1);
        // 范围查询
        System.out.println(obj.queryRange(0, n - 1));
        System.out.println(obj.queryRangeLazy(0, n - 1));
        // 范围修改
        obj.updateRange(0, 1, 1);
        System.out.println(obj.queryRange(0,1));
        System.out.println(obj.queryRange(2,3));
        System.out.println(obj.queryRange(0, n - 1));
        obj.updateRange(0, 1, -1);
        System.out.println(obj.queryRange(0,1));
        obj.updateRangeLazy(0, 1, 1);
        System.out.println(obj.queryRangeLazy(0,1));
        System.out.println(obj.queryRangeLazy(0, n - 1));
    }

}
