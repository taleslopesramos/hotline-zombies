/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.utils;

import com.badlogic.gdx.utils.reflect.ArrayReflection;

public class CircularBuffer<T> {
    private T[] items;
    private boolean resizable;
    private int head;
    private int tail;
    private int size;

    public CircularBuffer() {
        this(16, true);
    }

    public CircularBuffer(int initialCapacity) {
        this(initialCapacity, true);
    }

    public CircularBuffer(int initialCapacity, boolean resizable) {
        this.items = new Object[initialCapacity];
        this.resizable = resizable;
        this.head = 0;
        this.tail = 0;
        this.size = 0;
    }

    public boolean store(T item) {
        if (this.size == this.items.length) {
            if (!this.resizable) {
                return false;
            }
            this.resize(Math.max(8, (int)((float)this.items.length * 1.75f)));
        }
        ++this.size;
        this.items[this.tail++] = item;
        if (this.tail == this.items.length) {
            this.tail = 0;
        }
        return true;
    }

    public T read() {
        if (this.size > 0) {
            --this.size;
            T item = this.items[this.head];
            this.items[this.head] = null;
            if (++this.head == this.items.length) {
                this.head = 0;
            }
            return item;
        }
        return null;
    }

    public void clear() {
        T[] items = this.items;
        if (this.tail > this.head) {
            int i = this.head;
            int n = this.tail;
            do {
                items[i++] = null;
            } while (i < n);
        } else if (this.size > 0) {
            int i;
            int n = items.length;
            for (i = this.head; i < n; ++i) {
                items[i] = null;
            }
            n = this.tail;
            for (i = 0; i < n; ++i) {
                items[i] = null;
            }
        }
        this.head = 0;
        this.tail = 0;
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public boolean isFull() {
        return this.size == this.items.length;
    }

    public int size() {
        return this.size;
    }

    public boolean isResizable() {
        return this.resizable;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    public void ensureCapacity(int additionalCapacity) {
        int newCapacity = this.size + additionalCapacity;
        if (this.items.length < newCapacity) {
            this.resize(newCapacity);
        }
    }

    protected void resize(int newCapacity) {
        Object[] newItems = (Object[])ArrayReflection.newInstance(this.items.getClass().getComponentType(), newCapacity);
        if (this.tail > this.head) {
            System.arraycopy(this.items, this.head, newItems, 0, this.size);
        } else if (this.size > 0) {
            System.arraycopy(this.items, this.head, newItems, 0, this.items.length - this.head);
            System.arraycopy(this.items, 0, newItems, this.items.length - this.head, this.tail);
        }
        this.head = 0;
        this.tail = this.size;
        this.items = newItems;
    }
}

