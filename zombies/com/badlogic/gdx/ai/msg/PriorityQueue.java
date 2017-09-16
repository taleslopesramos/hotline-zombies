/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.msg;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectSet;

public class PriorityQueue<E extends Comparable<E>> {
    private static final int DEFAULT_INITIAL_CAPACITY = 11;
    private static final double CAPACITY_RATIO_LOW = 1.5;
    private static final double CAPACITY_RATIO_HI = 2.0;
    private Object[] queue;
    private ObjectSet<E> set;
    private boolean uniqueness;
    private int size = 0;

    public PriorityQueue() {
        this(11);
    }

    public PriorityQueue(int initialCapacity) {
        this.queue = new Object[initialCapacity];
        this.set = new ObjectSet(initialCapacity);
    }

    public boolean getUniqueness() {
        return this.uniqueness;
    }

    public void setUniqueness(boolean uniqueness) {
        this.uniqueness = uniqueness;
    }

    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        if (this.uniqueness && !this.set.add(e)) {
            return false;
        }
        int i = this.size;
        if (i >= this.queue.length) {
            this.growToSize(i + 1);
        }
        this.size = i + 1;
        if (i == 0) {
            this.queue[0] = e;
        } else {
            this.siftUp(i, e);
        }
        return true;
    }

    public E peek() {
        return (E)(this.size == 0 ? null : (Comparable)this.queue[0]);
    }

    public E get(int index) {
        return (E)(index >= this.size ? null : (Comparable)this.queue[index]);
    }

    public int size() {
        return this.size;
    }

    public void clear() {
        for (int i = 0; i < this.size; ++i) {
            this.queue[i] = null;
        }
        this.size = 0;
        this.set.clear();
    }

    public E poll() {
        if (this.size == 0) {
            return null;
        }
        int s = --this.size;
        Comparable result = (Comparable)this.queue[0];
        Comparable x = (Comparable)this.queue[s];
        this.queue[s] = null;
        if (s != 0) {
            this.siftDown(0, x);
        }
        if (this.uniqueness) {
            this.set.remove((Comparable)result);
        }
        return (E)result;
    }

    private void siftUp(int k, E x) {
        Comparable e;
        int parent;
        while (k > 0 && x.compareTo((Comparable)(e = (Comparable)this.queue[parent = k - 1 >>> 1])) < 0) {
            this.queue[k] = e;
            k = parent;
        }
        this.queue[k] = x;
    }

    private void siftDown(int k, E x) {
        int half = this.size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            Comparable c = (Comparable)this.queue[child];
            int right = child + 1;
            if (right < this.size && c.compareTo((Comparable)this.queue[right]) > 0) {
                child = right;
                c = (Comparable)this.queue[child];
            }
            if (x.compareTo((Comparable)c) <= 0) break;
            this.queue[k] = c;
            k = child;
        }
        this.queue[k] = x;
    }

    private void growToSize(int minCapacity) {
        if (minCapacity < 0) {
            throw new GdxRuntimeException("Capacity upper limit exceeded.");
        }
        int oldCapacity = this.queue.length;
        int newCapacity = (int)(oldCapacity < 64 ? (double)(oldCapacity + 1) * 2.0 : (double)oldCapacity * 1.5);
        if (newCapacity < 0) {
            newCapacity = Integer.MAX_VALUE;
        }
        if (newCapacity < minCapacity) {
            newCapacity = minCapacity;
        }
        Object[] newQueue = new Object[newCapacity];
        System.arraycopy(this.queue, 0, newQueue, 0, this.size);
        this.queue = newQueue;
    }
}

