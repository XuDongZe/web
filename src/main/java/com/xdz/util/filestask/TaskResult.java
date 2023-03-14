package com.xdz.util.filestask;

/**
 * Description: 这个类工作在单线程下。计数器功能不保证线程安全<br/>
 * Author: dongze.xu<br/>
 * Date: 2023/2/3 11:05<br/>
 * Version: 1.0<br/>
 */

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xdz.util.MapUtil;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TaskResult {
    public int lines; // read lines;
    public int sum; // real handle
    public int finish; // handle success
    public List<String> msgList = Lists.newArrayList();
    public Map<String, Long> counter; // 计数器

    public TaskResult() {
    }

    public TaskResult(int lines, int sum, int finish) {
        this.lines = lines;
        this.sum = sum;
        this.finish = finish;
    }

    public void pushMsg(String msg) {
        msgList.add(msg);
    }

    public TaskResult setCounter(Map<String, Long> counter) {
        this.counter = counter;
        return this;
    }

    /**
     *  合并另一个result
     */
    public TaskResult merge(TaskResult other) {
        if (other == null) {
            return this;
        }
        addSum(other.sum);
        addFinish(other.finish);
        mergeCounter(other.counter);
        return this;
    }

    public TaskResult addSum(int added) {
        sum += added;
        return this;
    }

    public TaskResult addFinish(int added) {
        finish += added;
        return this;
    }

    public TaskResult mergeCounter(Map<String, Long> other) {
        if (counter == null) {
            counter = Maps.newHashMap();
        }
        if (other != null && !other.isEmpty()) {
            for (Map.Entry<String, Long> entry : other.entrySet()) {
                // 忽略溢出
                String key = entry.getKey();
                MapUtil.incrNum(this.counter, entry.getKey(), other.get(key));
            }
        }
        return this;
    }
}