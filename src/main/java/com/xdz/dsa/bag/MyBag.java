package com.xdz.dsa.bag;

import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Set;

public class MyBag<Item> implements IMyBag<Item> {

    private final Set<Item> set;

    public MyBag() {
        set = Sets.newHashSet();
    }

    @Override
    public void add(Item item) {
        set.add(item);
    }

    @Override
    public boolean contains(Item item) {
        return set.contains(item);
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public int size() {
        return set.size();
    }

    @NotNull
    @Override
    public Iterator<Item> iterator() {
        return set.iterator();
    }
}
