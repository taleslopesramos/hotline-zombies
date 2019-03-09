/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.scenes.scene2d.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.utils.Selection;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.OrderedSet;

public class ArraySelection<T>
extends Selection<T> {
    private Array<T> array;
    private boolean rangeSelect = true;

    public ArraySelection(Array<T> array) {
        this.array = array;
    }

    @Override
    public void choose(T item) {
        if (item == null) {
            throw new IllegalArgumentException("item cannot be null.");
        }
        if (this.isDisabled) {
            return;
        }
        if (this.selected.size > 0 && this.rangeSelect && this.multiple && (Gdx.input.isKeyPressed(59) || Gdx.input.isKeyPressed(60))) {
            int high;
            int low = this.array.indexOf(this.getLastSelected(), false);
            if (low > (high = this.array.indexOf(item, false))) {
                int temp = low;
                low = high;
                high = temp;
            }
            this.snapshot();
            if (!UIUtils.ctrl()) {
                this.selected.clear();
            }
            while (low <= high) {
                this.selected.add(this.array.get(low));
                ++low;
            }
            if (this.fireChangeEvent()) {
                this.revert();
            }
            this.cleanup();
            return;
        }
        super.choose(item);
    }

    public boolean getRangeSelect() {
        return this.rangeSelect;
    }

    public void setRangeSelect(boolean rangeSelect) {
        this.rangeSelect = rangeSelect;
    }

    public void validate() {
        Array array = this.array;
        if (array.size == 0) {
            this.clear();
            return;
        }
        ObjectSet.ObjectSetIterator iter = this.items().iterator();
        while (iter.hasNext()) {
            Object selected = iter.next();
            if (array.contains(selected, false)) continue;
            iter.remove();
        }
        if (this.required && this.selected.size == 0) {
            this.set(array.first());
        }
    }
}

