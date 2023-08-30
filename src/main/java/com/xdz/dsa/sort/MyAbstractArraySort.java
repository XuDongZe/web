package com.xdz.dsa.sort;

import com.xdz.util.CheckUtil;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Description: abstract array sort for param handle<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/6/12 14:12<br/>
 * Version: 1.0<br/>
 */
public abstract class MyAbstractArraySort<E extends Comparable<E>> implements IMyArraySort<E> {

    @Override
    public void sort(E[] array, int start, int end, Comparator<E> cmp) {
        CheckUtil.checkNotNull(array, "sort.array");
        CheckUtil.checkNotNull(cmp, "sort.cmp");

        assert end >= start;
        // element amount is just 1 or 0. no need to sort.
        if (end - start <= 1) {
            return;
        }

        doSort(array, start, end, cmp);
    }

    protected abstract void doSort(E[] array, int start, int end, Comparator<E> cmp);

    public static void test(IMyArraySort<Integer> sort) {
        System.out.println(sort.getClass().getSimpleName());

        Integer[] array = new Integer[]{81, 94, 11, 96, 12, 35, 17, 95, 28, 58, 41, 75, 15};
        System.out.println("before sort");
        System.out.println(Arrays.toString(array));

        sort.sort(array);
        System.out.println("after sort ascending");
        System.out.println(Arrays.toString(array));

        sort.reverseSort(array);
        System.out.println("after sort descending");
        System.out.println(Arrays.toString(array));
    }

    public static void testRange(IMyArraySort<Integer> sort) {
        System.out.println(sort.getClass().getSimpleName());

        Integer[] array = new Integer[]{81, 94, 11, 96, 12, 35, 17, 95, 28, 58, 41, 75, 15};

        System.out.println("before sort");
        System.out.println(Arrays.toString(array));

        // sort range: 94, 11, 96, 12, 35, 17, 95
        int i = 1;
        int j = 7 + 1;
        sort.sort(array, i, j);
        System.out.println("after sort range[" + i + "," + j + "] ascending");
        System.out.println(Arrays.toString(array));

        sort.reverseSort(array, i, j);
        System.out.println("after sort range[" + i + "," + j + "] descending");
        System.out.println(Arrays.toString(array));
    }
}
