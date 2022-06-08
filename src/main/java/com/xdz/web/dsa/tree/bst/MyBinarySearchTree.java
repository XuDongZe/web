package com.xdz.web.dsa.tree.bst;

import com.xdz.web.dsa.list.IMyList;
import com.xdz.web.dsa.list.MyArrayList;
import com.xdz.web.dsa.tree.bt.IMyBinaryTree;
import com.xdz.web.dsa.tree.bt.MyBinaryTree;
import org.python.modules.itertools.repeat;

/**
 * Description: 二叉查找数<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/5/18 17:20<br/>
 * Version: 1.0<br/>
 *
 * <pre>
 * we assume that BST Node is comparable and not repeat.
 * if there are repeat nodes, we can hold a data field called count which means the node appeared times in the tree.
 * these can lower the tree and then improve the performance.
 * </pre>
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class MyBinarySearchTree<E extends Comparable<E>> extends MyBinaryTree<E> implements IMyBinarySearchTree<E> {

    public MyBinarySearchTree() {

    }

    @Override
    public Node<E> find(E e) {
        return findIterate(root, e);
    }

    @Override
    public Node<E> findMin() {
        return findMin(root);
    }

    @Override
    public Node<E> findMax() {
        return findMax(root);
    }

    @Override
    public void insert(E e) {
        insertIterate(e);
    }

    @Override
    public void remove(E e) {
        removeIterate(e);
    }

    private Node<E> findIterate(Node<E> root, E e) {
        while (root != null) {
            int compareResult = e.compareTo(root.element);
            if (compareResult < 0) {
                root = root.left;
            } else if (compareResult > 0) {
                root = root.right;
            } else {
                return root;
            }
        }
        return null;
    }

    /**
     * find the minValue in subTree roots by root.
     *
     * @param root the subTree's root
     * @return the min element or null if root is empty
     */
    private Node<E> findMin(Node<E> root) {
        if (root == null) {
            return null;
        } else if (root.left == null) {
            return root;
        } else {
            return findMin(root.left);
        }
    }

    /**
     * find max element in subTree which roots by root
     * we use a none-recursive one.
     *
     * @param root the subTree's root
     * @return the max element
     */
    private Node<E> findMax(Node<E> root) {
        if (root == null) {
            return null;
        }
        // we use the root's reference copy (in function call stack frame), so not need to consider the safety.
        while (root.right != null) {
            root = root.right;
        }
        // now root != null and root.right == null => root is the right-max node
        return root;
    }

    /**
     * <pre>
     * when insert a new node in bst, it must a leaf-node: it must have one parent node. because:
     * first we need find one place to put the new node p, consider that:
     * if root == null, then root = p;
     * else if current node we called cur, inited with root.
     *   if cur.e == e, already inserted. return
     *   else if cur.e > e, if cur have no right node, then cur.right = p; else compare cur.right with p and finally we will find one null node and place p to here.
     *   else if cur.e < e, the same with the top.
     *   so finally we find one parent node: pp.e > e && pp.left == null => pp.left = p; or pp.e < e && pp.right == null => pp.right = p
     *
     * so in the insert func, we do not need to consider the parent's original left | right pointer will move to where:
     * for it is forever null and have no sub-tree here.
     *
     * so it is easier than remove func
     * </pre>
     */
    private void insertIterate(E e) {
        if (root == null) {
            root = new Node<>(e);
            return;
        }
        // now root != null
        Node<E> t = root;
        while (true) {
            int compareResult = e.compareTo(t.element);
            if (compareResult < 0) {
                if (t.left == null) {
                    t.left = new Node<>(e);
                    return;
                }
                t = t.left;
            } else if (compareResult > 0) {
                if (t.right == null) {
                    t.right = new Node<>(e);
                    return;
                }
                t = t.right;
            } else {
                // already-insert do nothing
                // may we need to handle the count-field
                return;
            }
        }
    }

    private void removeIterate(E e) {
        if (root == null) {
            return;
        }

        Node<E> p = root; // node which to be removed
        Node<E> pp = null; // p node's parent
        while (p != null) {
            // pp step one to p, p step one
            int compareResult = e.compareTo(p.element);
            if (compareResult < 0) {
                pp = p;
                p = p.left;
            } else if (compareResult > 0) {
                pp = p;
                p = p.right;
            } else {
                // now p.element == e && p.parent == pp
                break;
            }
        }
        // now p == null or p.element == e
        if (p == null) {
            // already removed (e is not contains)
            return;
        }

        /*
         * <pre>
         * now p.element == e && p.parent == pp. we need to remove p node.
         * more likely in list to remove one node. assume a single-linked-list, and remove a node identify by pointer p.
         * we maybe consider that:
         * 1. if p is first node of list, then list = null.
         * 2. else p has a prev node: p.prev = pp. we just  do: pp.next = p.next; to remove p node link.
         *
         * so what different and the same to remove one node p in a bst?
         * the same is that tree is linked of node. see it ? linked.
         * the different is that single-linked list has just one pointer to its next but tree bst node has two.
         *
         * and another different and the most import different is that an ordered list is logic-seq just iterate by its next pointer
         * but a bst is not: its logic-seq is infix-order.
         *
         * so p.prev.next = p.next, abstract says that: logicNextNode(p.prev) = logicNextNode(p); this is suitable for both list and bst.
         * for list logicNextNode(p) func is simple: just p.next.
         * but for bst is much more: the minNode in right-sub-bst.
         *
         * now we find the logicNextNode(p) call child, then we need linked it to p.prev: prev.next = child;
         * the next pointer is left or right link.
         *
         * so we maybe consider that:
         * 1. if p is first node of tree, that means p == tree.root. then root = null.
         * 2. else p has a parent node pp. now consider:
         *   1. p is pp left-node. that means pp.left == p: pp.left = child;
         *   2. p is pp right-node. that mens pp.right == p: pp.right = child;
         * </pre>
         */

        // now p != null && pp != null && p.parent is pp: pp.left == p or pp.right == p

        if (p.left != null && p.right != null) {
            // find minNode in right-sub-tree(or maxNode in left-sub-tree)
            Node<E> minP = p.right;
            Node<E> minPP = p;
            while (minP.left != null) {
                minPP = minP;
                minP = minP.left;
            }
            // todo 这里需要再想想 多debug几遍
            p.element = minP.element;
            // now remove minP
            p = minP;
            pp = minPP;
        }

        Node<E> next;
        if (p.left != null) {
            // link not-null left-sub-tree
            next = p.left;
        } else if (p.right != null) {
            // link not-null right-sub-tree
            next = p.right;
        } else {
            next = null;
        }

        // now link p.parent pp.next to p.next
        if (pp == null) {
            // removed node p is root.
            root = next;
        } else if (pp.left == p) {
            pp.left = next;
        } else {
            // pp.right == p
            pp.right = next;
        }
    }

    private Node<E> findRecursive(Node<E> root, E e) {
        if (root == null) {
            return null;
        }

        int compareResult = e.compareTo(root.element);
        if (compareResult < 0) {
            return findRecursive(root.left, e);
        } else if (compareResult > 0) {
            return findRecursive(root.right, e);
        } else {
            return root;
        }
    }

    /**
     * <pre>
     * add a new node which element is e in subTree which roots by root.
     * we need to put e to a correct position to hold BST feature: left <= root <= right
     * 1. assume that e is a new max element, then it should be placed to right-max, that means first get moxNode then put to its right.
     * 2. assume that e is a new min element, then it should be placed to left-max, that means first get minNode then put to its left.
     * 3. assume that e is not new-max or new-min, just normal one. then there is one min node A is larger than it and another one max node B is smaller than it:
     * </pre>
     *
     * @param root the subTree's root
     * @param e    element need to be added
     * @return new root
     */
    protected Node<E> insertRecursive(Node<E> root, E e) {
        if (root == null) {
            return new Node<>(e);
        }

        int compareResult = e.compareTo(root.element);
        if (compareResult < 0) {
            root.left = insertRecursive(root.left, e);
        } else if (compareResult > 0) {
            root.right = insertRecursive(root.right, e);
        } else {
            ; // duplicate, now do nothing or maybe incr the count data field
        }
        return root;
    }

    /**
     * remove node(node.element == e) in bst-tree node.
     *
     * @param root
     * @param e
     * @return
     */
    protected Node<E> removeRecursive(Node<E> root, E e) {
        if (root == null) {
            return null;
        }

        int compareResult = e.compareTo(root.element);
        if (compareResult < 0) {
            root.left = removeRecursive(root.left, e);
        } else if (compareResult > 0) {
            root.right = removeRecursive(root.right, e);
        } else {
            if (root.left == null && root.right == null) {
                // no child
                root = null;
            } else if (root.left != null && root.right == null) {
                // one child. new root is the not null child.
                root = root.left;
            } else if (root.right != null && root.left == null) {
                root = root.right;
            } else {
                /**
                 * <pre>
                 * two child
                 * just the same with array-delete
                 * copy root's successor's e to root and then remove the successor. (we call logicNextNode as successor)
                 * for the remove, just do root.next = successor.next.
                 * we repeat the op, and we will finally remove a no-child node then complete the whole process.
                 * for bst, a root's successor is minValue(root.right). we use the `findMin` func to got it.
                 * the successor.next will return by recursive-func as the new root of the removed-node subtree.
                 *
                 * </pre>
                 */
                // replace root.element use logic-next
                Node<E> next = findMin(root.right);
                root.element = next.element;
                // remove node successor: we know it in root.right
                root.right = removeRecursive(root.right, next.element);
                // we maybe merge find & remove the min(root.right) in one pass: findAndRemove(), just as pop() func in stack or queue.
            }
        }
        // root maybe null: when remove a leaf node, the new root is null.
        return root;
    }

    public static void main(String[] args) {
        IMyList<Integer> list = new MyArrayList<>(7);
        list.addLast(5);
        list.addLast(6);
        list.addLast(9);
        list.addLast(4);
        list.addLast(8);
        list.addLast(2);
        list.addLast(3);
        // now list.size() == 7
        IMyBinarySearchTree<Integer> tree = new MyBinarySearchTree<>();
        tree.insertAll(list);
        boolean bst = tree.isBst();
        IMyBinaryTree<Integer> bt = new MyBinaryTree<>();
        bt.insertAll(list);
        bst = bt.isBst();
        boolean contains = tree.contains(8); // contains
        contains = tree.contains(-1); // not contains
        Node<Integer> p = tree.find(8);
        int min = tree.findMin().element; // 2
        int max = tree.findMax().element; // 9
        tree.insert(2); // repeat one
        tree.insert(1); // new min
        tree.insert(10); // new max
        tree.remove(8); // contains
        tree.remove(8); // not contains
        tree.remove(2); // 1 is 2's left and 3 is 2's right
        tree.remove(1); // min
        tree.remove(10); // max
        tree.remove(5); // root
        boolean empty = tree.isEmpty();
        tree.clear();
        empty = tree.isEmpty();
    }
}
