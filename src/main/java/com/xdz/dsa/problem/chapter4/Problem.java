package com.xdz.dsa.problem.chapter4;

/**
 * Description: chapter4 problems<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/6/6 20:49<br/>
 * Version: 1.0<br/>
 */
public class Problem {
    /**
     * <pre>
     * 4.4
     * Q: prove: for a binary-tree which has n nodes, there are n + 1 null-links.
     * A:
     * 1. n nodes, has 2 * n links.
     * 2. n nodes, each one hold by a not-null link except the root. so has n - 1 not-null link.
     * (a not-null-link is the link point to a not null node)
     * 3. one link is null or not-null.
     * 4. so null links is: 2 * n - (n - 1) =n + 1.
     * </pre>
     */
    private void prove4_4() {

    }

    /**
     * <pre>
     * 4.5
     * A: prove: the max node nums of a binary-tree is 2^(h+1) - 1.
     * for height(bst) = h, h >= 0. for just one root bst, height(bst) = 0
     * Q:
     * 1. if bst has just one node root, nums = 1 = 2 ^(0+1) - 1. yes.
     * 2. if bst has more than one node, max node nums occur while bst is full: each node has two not null child nodes.
     * and the h-level node nums is: nums(h) = 2^h.
     * 3. so nums(bst) = sum(nums(h)) for h: [0, h], that is sum of proportional sequence, so we got:
     * sum(bst) = 2^(h+1) - 1 for height(bst) == h && (height(bst) == 0 if nums(bst) == 1)
     * </pre>
     */
    private void prove4_5() {

    }

    /**
     * Q: prove: for a binary-tree, nums of full-nodes(has 2 child) = nums of  leaf-nodes(has 0 child) + 1
     * A: bst nodes can type of:
     *  1. two-child-nodes. we call nums(two-child-nodes) n2.
     *  2. one-child-nodes. we let nums(one-child-nodes) n1.
     *  3. none-child-nodes.we call nums(none-child-nodes) n0.
     *  so nums(bst) = n0 + n1 + n2
     *
     * another way: bst nodes can type of:
     *  1. root node. nums(root) == 1
     *  2. child node. nums(child) == n1 + 2 * n2. (nums(one-child-nodes) + nums(two-child-nodes) * 2)
     *  so nums(bst) = n1 + 2 * n2
     *
     *  so we got:
     *  n0 +n1 + n2 = n1 + 2 * n2
     */
    private void prove4_6() {

    }


}
