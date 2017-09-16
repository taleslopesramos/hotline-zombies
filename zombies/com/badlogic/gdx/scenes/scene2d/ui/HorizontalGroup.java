/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.SnapshotArray;

public class HorizontalGroup
extends WidgetGroup {
    private float prefWidth;
    private float prefHeight;
    private boolean sizeInvalid = true;
    private int align;
    private boolean reverse;
    private boolean round = true;
    private float spacing;
    private float padTop;
    private float padLeft;
    private float padBottom;
    private float padRight;
    private float fill;

    public HorizontalGroup() {
        this.setTouchable(Touchable.childrenOnly);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        this.sizeInvalid = true;
    }

    private void computeSize() {
        this.sizeInvalid = false;
        SnapshotArray<Actor> children = this.getChildren();
        int n = children.size;
        this.prefWidth = this.padLeft + this.padRight + this.spacing * (float)(n - 1);
        this.prefHeight = 0.0f;
        for (int i = 0; i < n; ++i) {
            Actor child = children.get(i);
            if (child instanceof Layout) {
                Layout layout = (Layout)((Object)child);
                this.prefWidth += layout.getPrefWidth();
                this.prefHeight = Math.max(this.prefHeight, layout.getPrefHeight());
                continue;
            }
            this.prefWidth += child.getWidth();
            this.prefHeight = Math.max(this.prefHeight, child.getHeight());
        }
        this.prefHeight += this.padTop + this.padBottom;
        if (this.round) {
            this.prefWidth = Math.round(this.prefWidth);
            this.prefHeight = Math.round(this.prefHeight);
        }
    }

    @Override
    public void layout() {
        float spacing = this.spacing;
        float padBottom = this.padBottom;
        int align = this.align;
        boolean reverse = this.reverse;
        boolean round = this.round;
        float groupHeight = this.getHeight() - this.padTop - padBottom;
        float x = !reverse ? this.padLeft : this.getWidth() - this.padRight + spacing;
        SnapshotArray<Actor> children = this.getChildren();
        int n = children.size;
        for (int i = 0; i < n; ++i) {
            float width;
            float height;
            Actor child = children.get(i);
            Layout layout = null;
            if (child instanceof Layout) {
                layout = (Layout)((Object)child);
                height = this.fill > 0.0f ? groupHeight * this.fill : Math.min(layout.getPrefHeight(), groupHeight);
                height = Math.max(height, layout.getMinHeight());
                float maxHeight = layout.getMaxHeight();
                if (maxHeight > 0.0f && height > maxHeight) {
                    height = maxHeight;
                }
                width = layout.getPrefWidth();
            } else {
                width = child.getWidth();
                height = child.getHeight();
                if (this.fill > 0.0f) {
                    height *= this.fill;
                }
            }
            float y = padBottom;
            if ((align & 2) != 0) {
                y += groupHeight - height;
            } else if ((align & 4) == 0) {
                y += (groupHeight - height) / 2.0f;
            }
            if (reverse) {
                x -= width + spacing;
            }
            if (round) {
                child.setBounds(Math.round(x), Math.round(y), Math.round(width), Math.round(height));
            } else {
                child.setBounds(x, y, width, height);
            }
            if (!reverse) {
                x += width + spacing;
            }
            if (layout == null) continue;
            layout.validate();
        }
    }

    @Override
    public float getPrefWidth() {
        if (this.sizeInvalid) {
            this.computeSize();
        }
        return this.prefWidth;
    }

    @Override
    public float getPrefHeight() {
        if (this.sizeInvalid) {
            this.computeSize();
        }
        return this.prefHeight;
    }

    public void setRound(boolean round) {
        this.round = round;
    }

    public HorizontalGroup reverse() {
        this.reverse(true);
        return this;
    }

    public HorizontalGroup reverse(boolean reverse) {
        this.reverse = reverse;
        return this;
    }

    public boolean getReverse() {
        return this.reverse;
    }

    public HorizontalGroup space(float spacing) {
        this.spacing = spacing;
        return this;
    }

    public float getSpace() {
        return this.spacing;
    }

    public HorizontalGroup pad(float pad) {
        this.padTop = pad;
        this.padLeft = pad;
        this.padBottom = pad;
        this.padRight = pad;
        return this;
    }

    public HorizontalGroup pad(float top, float left, float bottom, float right) {
        this.padTop = top;
        this.padLeft = left;
        this.padBottom = bottom;
        this.padRight = right;
        return this;
    }

    public HorizontalGroup padTop(float padTop) {
        this.padTop = padTop;
        return this;
    }

    public HorizontalGroup padLeft(float padLeft) {
        this.padLeft = padLeft;
        return this;
    }

    public HorizontalGroup padBottom(float padBottom) {
        this.padBottom = padBottom;
        return this;
    }

    public HorizontalGroup padRight(float padRight) {
        this.padRight = padRight;
        return this;
    }

    public float getPadTop() {
        return this.padTop;
    }

    public float getPadLeft() {
        return this.padLeft;
    }

    public float getPadBottom() {
        return this.padBottom;
    }

    public float getPadRight() {
        return this.padRight;
    }

    public HorizontalGroup align(int align) {
        this.align = align;
        return this;
    }

    public HorizontalGroup center() {
        this.align = 1;
        return this;
    }

    public HorizontalGroup top() {
        this.align |= 2;
        this.align &= -5;
        return this;
    }

    public HorizontalGroup bottom() {
        this.align |= 4;
        this.align &= -3;
        return this;
    }

    public int getAlign() {
        return this.align;
    }

    public HorizontalGroup fill() {
        this.fill = 1.0f;
        return this;
    }

    public HorizontalGroup fill(float fill) {
        this.fill = fill;
        return this;
    }

    public float getFill() {
        return this.fill;
    }
}

