package com.xdz.leetcode.leetcode.editor.cn.sfmj.chapter02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Sort {
    /**
     * 冒泡排序
     * O(1)、O(n^2) ~ O(n)、稳定
     */
    private void bubbleSort(int[] nums) {
        int n = nums.length;
        for (int i = 0; i < n; i++) { // 冒泡n次
            boolean swap = false;
            for (int j = 0; j < n - 1 - i; j++) { // j枚举前面的元素下标 第i次 - i
                if (nums[j] > nums[j + 1]) {
                    swap(nums, j, j + 1);
                    swap = true;
                }
            }
            if (!swap) { // 两两比较 有序
                break;
            }
        }
    }

    /**
     * 选择排序
     * O(1) O(n^2) ~ O(n^2) 不稳定 无法利用输入的有序性 交换次数最少
     * 不稳定举例 [3,4,3,1]
     */
    private void selectSort(int[] nums) {
        int n = nums.length;
        for (int i = 0; i < n; i++) { // 选择最小值放到 [i]
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (nums[minIdx] > nums[j]) {
                    minIdx = j;
                }
            }
            if (minIdx != i) {
                swap(nums, minIdx, i);
            }
        }
    }

    /**
     * 插入排序
     * O(1) O(n^2) ~ O(n) 稳定
     */
    private void insertSort(int[] nums) {
        int n = nums.length;
        for (int i = 1; i < n; i++) {
            // [0...i)有序 将[i] => 插入到[0...i)中
            int j = i - 1, key = nums[i];
            while (j >= 0 && key < nums[j]) {
                nums[j + 1] = nums[j];
                j--;
            }
            // now j < 0 || nums[i] >= nums[j] i放在j的后面
            nums[j + 1] = key;
        }
    }

    /**
     * 快速排序
     * O(1) O(n*log n) ~ O(n^2) 不稳定
     */
    private void quickSort(int[] nums) {
        __quickSort(nums, 0, nums.length - 1);
    }

    // sort nums[i, j]
    private void __quickSort(int[] nums, int i, int j) {
        if (i >= j) {
            return;
        }
        int mid = __partition(nums, i, j);
        __quickSort(nums, i, mid - 1);
        __quickSort(nums, mid + 1, j);
    }

    // 所有 < pivot的放左边 >pivot的放右边 ==pivot的放两边都可
    private int __partition(int[] nums, int l, int r) {
        int pivot = nums[l];
        // 小堆 乱堆 大堆 探子指针
        int i = l, j = r + 1; // [l, i] <= pivot [i+1, j-1]乱堆 [j, r] > pivot
        int k = l + 1; // 探子指针
        while (j - i > 1) { // 乱堆长度(j-1)-(i+1)+1>0 => j-i>1
            if (nums[k] <= pivot) {
                i++;
                k++;
            } else {
                j--;
                swap(nums, j, k);
            }
        }
        swap(nums, l, i);
        return i;
    }

    /**
     * 归并排序
     * O(n) O(n * log n) ~ O(n * log n) 稳定
     */
    public void mergeSort(int[] nums) {
        __mergeSort(nums, 0, nums.length - 1);
    }

    private void __mergeSort(int[] nums, int l, int r) {
        if (l >= r) {
            return;
        }
        int mid = (l + r) / 2;
        __mergeSort(nums, l, mid);
        __mergeSort(nums, mid + 1, r);
        __merge(nums, l, mid, r);
    }

    // 合并有序数组 [l, mid] [mid+1, r]
    private void __merge(int[] nums, int l, int mid, int r) {
        int[] copy = new int[r - l + 1];

        int i = l, j = mid + 1, k = 0;
        while (i <= mid && j <= r) {
            if (nums[i] <= nums[j]) { // 等号 稳定性
                copy[k++] = nums[i++];
            } else {
                copy[k++] = nums[j++];
            }
        }

        while (i <= mid) {
            copy[k++] = nums[i++];
        }
        while (j <= r) {
            copy[k++] = nums[j++];
        }

        System.arraycopy(copy, 0, nums, l, copy.length);
    }

    /**
     * 堆排序
     * O(1) O(n * log n) ~ O(n * log n) 不稳定
     */
    public void heapSort(int[] nums) {
        int n = nums.length;
        // 将nums原地构造成大顶堆 nums[0]为堆顶 O(n * log n)
        // 类似于插入排序
        for (int i = 0; i < n; i++) {
            int num = nums[i];
            int p = i;
            while (p > 0 && nums[p] > nums[(p - 1) / 2]) {
                int pp = (p - 1) / 2;
                swap(nums, p, pp);
                p = pp;
            }
            // [i]值放到位置p
            nums[p] = num;
        }

        // 将最大值与未排序序列的末尾元素交换 然后调整堆
        for (int i = n - 1; i >= 0; i--) { // 枚举未排序序列的末尾元素
            swap(nums, 0, i);
            // [0,i - 1] 往下调整堆
            int p = 0;
            while (true) {
                int l = p * 2 + 1, r = p * 2 + 2;
                if (l > i - 1 && r > i - 1) {
                    break;
                }
                int maxIdx = l;
                if (r <= i - 1 && nums[r] > nums[l]) {
                    maxIdx = r;
                }
                if (nums[p] > nums[maxIdx]) {
                    break;
                }
                swap(nums, p, maxIdx);
                p = maxIdx;
            }
        }
    }

    /**
     * 复杂度
     * O(1) O(? ) 不稳定
     * 思想：枚举step 分组插入排序
     */
    public void shellSort(int[] nums) {
        int n = nums.length;
        // 如果n == 8 那么可以step序列可以是：4 2 1
        for (int step = n / 2; step >= 1; step /= 2) {
            // 对序列{i, i+step, i+2*step, i+3*step...i+k*step}进行插入排序
            for (int i = 1; i < n; i += step) { // [0...i)有序
                // 将[i]插入序列中
                int j = i - step, key = nums[i]; //
                while (j >= 0 && key < nums[j]) {
                    nums[j + step] = nums[j];
                    j -= step;
                }
                nums[j + step] = key;
            }
        }
    }


    /**
     * 思想 将原数组划分成N个有序的桶 桶内排序完成后 再归并
     * <p>
     * O(n) O(n) ~ O(n^2)[取决于桶的数量 以及桶内部排序的算法]
     * 稳定性：桶划分时是稳定的 桶内排序如果也是稳定的 那么就是稳定的
     * 适用范围：元素的数量不多 分布均匀(如果元素全等 那就都放到一个桶里面了)
     */
    public void bucketSort(int[] nums) {
        int k = 3; // 定义3个桶
        // 将[min, max]这max-min+1个位置 划分到k个桶中 保证后面的桶的最小值大于前面的桶的最大值
        // 每个桶分配的数字额度为ceil((max-min+1)/k) = t：
        // 第一个桶：[min, min+t) 第二个桶： [min+t, min+2t) => (nums[i]-min) / t == bucketId
        int max = nums[0], min = nums[0];
        for (int i = 1; i < nums.length; i++) {
            max = Math.max(nums[i], max);
            min = Math.min(nums[i], min);
        }
        int t = (int) Math.ceil(1.0 * (max - min + 1) / k); // 让k * t能够覆盖[min, max]
        // 划分数字到桶中
        List<Integer>[] buckets = new List[k];
        for (int i = 0; i < k; i++) buckets[i] = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            buckets[(nums[i] - min) / t].add(nums[i]);
        }
        // 桶内部排序
        for (int i = 0; i < k; i++) {
            Collections.sort(buckets[i]);
        }
        // 合并桶
        int idx = 0;
        for (List<Integer> bucket : buckets) {
            for (Integer n : bucket) {
                nums[idx++] = n;
            }
        }
    }


    /**
     * 相当于桶数量为元素划分的 桶排序
     * <p>
     * O(n) O(n) 可以稳定
     */
    public void countingSort(int[] nums) {
        int min = nums[0], max = nums[0];
        for (int num : nums) {
            min = Math.min(num, min);
            max = Math.max(num, max);
        }
        int[] bucket = new int[max - min + 1];
        // counting
        for (int num : nums) {
            bucket[num - min]++;
        }
        // 合并
        int idx = 0;
        for (int i = 0; i < bucket.length; i++) {
            for (int j = 0; j < bucket[i]; j++) {
                nums[idx++] = i + min;
            }
        }
    }


    /**
     * 基数排序 可以排序数字和字符串(将数字看成是字符串)
     * 思想：首先将第一位排序 然后第二位 直到所有的为都排序完毕。可以分成2种 LSD和MSD 本次是从右到左进行排序的
     * <p>
     * 内部排序算法必须是稳定的
     * <p>
     * 当字符串的长度M非常长时 采用传统排序是O(n * log n) * O(M)。
     * 基数排序只需要M遍内部排序即可 内部排序可以选择计数排序(因为字符集是有限的) 这样是O(M) * O(N)级别的
     * <p>
     * O(N) O(M*N) 稳定 其中N为字符串数量 M为字符串最大长度
     */
    public void radixSort(int[] nums) {
        // 假设没有负数 数字为 0~9(如果有负数 那么就把负数的放一起 转成正数然后逆序排列即可；正数的放一起排序 然后再merge)
        int bucketSize = 10; // 0~9
        int max = Arrays.stream(nums).max().getAsInt();
        // 需要进行maxLength次排序 每次排一位 从右到左 从个位出发
        for (int radix = 1; radix <= max; radix *= 10) {
            // 计数排序
            // 按位划分
            List<Integer>[] buckets = new List[bucketSize];
            for (int j = 0; j < nums.length; j ++) {
                int r = (nums[j] / radix) % 10;
                List<Integer> bucket = buckets[r];
                if (bucket == null) {
                    bucket = new ArrayList<>();
                    buckets[r] = bucket;
                }
                bucket.add(nums[j]);
            }
            // 合并
            int idx = 0;
            for (List<Integer> bucket : buckets) {
                if (bucket != null) {
                    for (Integer num : bucket) {
                        nums[idx++] = num;
                    }
                }
            }
        }
    }

    private static void swap(int[] nums, int i, int j) {
        int t = nums[i];
        nums[i] = nums[j];
        nums[j] = t;
    }

    private static boolean equals(int[] a, int[] b) {
        return Arrays.equals(a, b);
    }

    public static void main(String[] args) {
        int[] original = {1, 5, 3, 6, 2, 9, 3, 10, 4, 8, 7, 13, 12, 11};
        int[] answer = {1, 2, 3, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        int[] nums;

        Sort sort = new Sort();

        nums = Arrays.copyOf(original, original.length);
        sort.bubbleSort(nums);
        System.out.println("bubbleSort:\t" + Sort.equals(answer, nums));

        nums = Arrays.copyOf(original, original.length);
        sort.selectSort(nums);
        System.out.println("selectSort:\t" + Sort.equals(answer, nums));

        nums = Arrays.copyOf(original, original.length);
        sort.insertSort(nums);
        System.out.println("insertSort:\t" + Sort.equals(answer, nums));

        nums = Arrays.copyOf(original, original.length);
        sort.quickSort(nums);
        System.out.println("quickSort:\t" + Sort.equals(answer, nums));

        nums = Arrays.copyOf(original, original.length);
        sort.mergeSort(nums);
        System.out.println("mergeSort:\t" + Sort.equals(answer, nums));

        nums = Arrays.copyOf(original, original.length);
        sort.heapSort(nums);
        System.out.println("heapSort:\t" + Sort.equals(answer, nums));

        nums = Arrays.copyOf(original, original.length);
        sort.shellSort(nums);
        System.out.println("shellSort:\t" + Sort.equals(answer, nums));

        nums = Arrays.copyOf(original, original.length);
        sort.bucketSort(nums);
        System.out.println("bucketSort:\t" + Sort.equals(answer, nums));

        nums = Arrays.copyOf(original, original.length);
        sort.countingSort(nums);
        System.out.println("countingSort:\t" + Sort.equals(answer, nums));

        nums = Arrays.copyOf(original, original.length);
        sort.radixSort(nums);
        System.out.println("radixSort:\t" + Sort.equals(answer, nums));
    }
}
