package com.xdz.dsa.hashtable;

/**
 * Description: hash table ADT<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/6/8 22:15<br/>
 * Version: 1.0<br/>
 *
 * <pre>
 * a simple idea is, hash table is an array, the items evenly distributed in the array.
 * we can find array-item by key of the item, use a map-func we called hash func: hash(key) = idx(item)
 *
 * a fact is that array space is limit but key's we need to storage is un-limit.
 *
 * so we will solve two problems: choose a good hash-func, and if two or more item hashing to the same idx(we call
 * collision now), what should we do.
 *
 * 1. choose a good hash func to got less collision: we need to mapping different key to different idx as much as we can.
 *  1.1 hashing function need work well
 *  1.2 hashing function need to easily to cal.
 *
 * if the key is number, an easy way is: key MOD tableSize. but if tableSize is many number's common divisor, assume that
 * tableSize == 10, so 10, 20, 30 will be hashing to the same idx 0. now we can choose a prime-number to avoid that.
 *
 * if the key is string, and is ASCII string. we can got a hashing func:
 *   int hash(String str) {
 *       int hash = 0;
 *       for (char ch : str) {
 *           hash += (ch - '0');
 *       }
 *       return hash % tableSize;
 *   }
 *
 * how to judge the func is good or not?
 * we can consider like that: if the avg(len(str)) == 8, and each ch of str is ASCII (0 ~ 127). so the hash range is: [0, 8 * 127],
 * that is [0, 1016]. so we use 1027 spaces in array mostly. if the array space is a big-prime-number, such as 10007,
 * we got array-space size >> hash-space size. so 10007 - 1027 spaces is not used.
 * this func is not good.
 *
 * another hashing func is using a polynomial function. so we got a large hash-space size to avoid too many not-used array space.
 *   int hash(String str) {
 *       int hash = 0;
 *       for (char ch : str) {
 *           hash = hash * 37 + (ch - '0')
 *       }
 *       hash = hash % tableSize;
 *       if (hash < 0) {
 *           hash += tableSize;
 *       }
 *       return hash;
 *   }
 * this func is easy to cal and got large-enough hash space. maybe a good hash. so we need to use random string to test it.
 *
 * 2. now we need to handle collision: where to place a new item b if there is already a let hash(a) == hash(b)?
 * and if we find one array-space to place b, how we find b use b.key? how we diff a and b ?
 * there simply has two different solution:
 * 2.1 link chain
 *   hash table is an array of a linked-list. the list storage items with the same hash value.
 *
 *   we use a linked-list, not an array is, we need to insert a new item at list[0], and array will copy the whole space.
 *   we use a list, not other struct for faster search is, the fact is that now we are in a hash table, so the size() of list
 *   is not large, avg is the load factor, maybe 1 or 2. now a bst is log(2) and a list is 2. and bst is more expensive in space cost.
 *   and bst is more complex.
 *   we can use a double linked list rather than a single one to convenient us for remove func.
 *
 *
 *  insert(e):
 *    1. find the idx using hashing func.
 *    2. using idx we got a linked-list. if contains, return; otherwise insert item to list[0]
 *    3. if tableSize is too big, rehash.
 *    we insert a new item to list[0] for a simple fact: the newly-inserted item maybe used soon.
 *
 *  remove(e):
 *    1. find the idx using hashing func.
 *    2. using idx we got a linked-list. remove the e from list.
 *
 * 2.2
 *
 *
 * we don not stare null key.
 * </pre>
 */
public interface IMyHashTable<E> {
    /**
     * judge an element e exist in the hash table or not.
     * @return true if e is already exist in hash table.
     */
    boolean contains(E e);

    /**
     * insert element e to hash table.
     * @return true if truly insert e, that is before insert, contains(e) == false and after insert, contains(e) == true.
     */
    boolean insert(E e);

    /**
     * remove element e to hash table.
     * @return true if truly remove e, that is before remove, contains(e) == true and after remove, contains(e) == false.
     */
    boolean remove(E e);

    /**
     * judge two element e1 e2, is the same with each other.
     * @return true is e1.sameWith(e2)
     */
    default boolean same(E e1, E e2) {
        if ((e1 == null && e2 != null) || (e1 != null && e2 == null)) {
            return false;
        }
        // now (e1 == null && e2 == null) || (e1 != null && e2 != null)
        return e1 == e2 || (e1.hashCode() == e2.hashCode() && e1.equals(e2));
    }
}
