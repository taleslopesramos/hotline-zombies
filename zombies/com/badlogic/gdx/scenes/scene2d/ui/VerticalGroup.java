/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.SnapshotArray;

public class VerticalGroup
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

    public VerticalGroup() {
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
        this.prefWidth = 0.0f;
        this.prefHeight = this.padTop + this.padBottom + this.spacing * (float)(n - 1);
        for (int i = 0; i < n; ++i) {
            Actor child = children.get(i);
            if (child instanceof Layout) {
                Layout layout = (Layout)((Object)child);
                this.prefWidth = Math.max(this.prefWidth, layout.getPrefWidth());
                this.prefHeight += layout.getPrefHeight();
                continue;
            }
            this.prefWidth = Math.max(this.prefWidth, child.getWidth());
            this.prefHeight += child.getHeight();
        }
        this.prefWidth += this.padLeft + this.padRight;
        if (this.round) {
            this.prefWidth = Math.round(this.prefWidth);
            this.prefHeight = Math.round(this.prefHeight);
        }
    }

    @Override
    public void layout() {
        float spacing = this.spacing;
        float padLeft = this.padLeft;
        int align = this.align;
        boolean reverse = this.reverse;
        boolean round = this.round;
        float groupWidth = this.getWidth() - padLeft - this.padRight;
        float y = reverse ? this.padBottom : this.getHeight() - this.padTop + spacing;
        SnapshotArray<Actor> children = this.getChildren();
        int n = children.size;
        for (int i = 0; i < n; ++i) {
            float width;
            float height;
            Actor child = children.get(i);
            Layout layout = null;
            if (child instanceof Layout) {
                layout = (Layout)((Object)child);
                width = this.fill > 0.0f ? groupWidth * this.fill : Math.min(layout.getPrefWidth(), groupWidth);
                width = Math.max(width, layout.getMinWidth());
                float maxWidth = layout.getMaxWidth();
                if (maxWidth > 0.0f && width > maxWidth) {
                    width = maxWidth;
                }
                height = layout.getPrefHeight();
            } else {
                width = child.getWidth();
                height = child.getHeight();
                if (this.fill > 0.0f) {
                    width *= this.fill;
                }
            }
            float x = padLeft;
            if ((align & 16) != 0) {
                x += groupWidth - width;
            } else if ((align & 8) == 0) {
                x += (groupWidth - width) / 2.0f;
            }
            if (!reverse) {
                y -= height + spacing;
            }
            if (round) {
                child.setBounds(Math.round(x), Math.round(y), Math.round(width), Math.round(height));
            } else {
                child.setBounds(x, y, width, height);
            }
            if (reverse) {
                y += height + spacing;
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

    public VerticalGroup reverse() {
        this.reverse(true);
        return this;
    }

    public VerticalGroup reverse(boolean reverse) {
        this.reverse = reverse;
        return this;
    }

    public boolean getReverse() {
        return this.reverse;
    }

    public VerticalGroup space(float spacing) {
        this.spacing = spacing;
        return this;
    }

    public float getSpace() {
        return this.spacing;
    }

    public VerticalGroup pad(float pad) {
        this.padTop = pad;
        this.padLeft = pad;
        this.padBottom = pad;
        this.padRight = pad;
        return this;
    }

    public VerticalGroup pad(float top, float left, float bottom, float right) {
        this.padTop = top;
        this.padLeft = left;
        this.padBottom = bottom;
        this.padRight = right;
        return this;
    }

    public VerticalGroup padTop(float padTop) {
        this.padTop = padTop;
        return this;
    }

    public VerticalGroup padLeft(float padLeft) {
        this.padLeft = padLeft;
        return this;
    }

    public VerticalGroup padBottom(float padBottom) {
        this.padBottom = padBottom;
        return this;
    }

    public VerticalGroup padRight(float padRight) {
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

    public VerticalGroup align(int align) {
        this.align = align;
        return this;
    }

    public VerticalGroup center() {
        this.align = 1;
        return this;
    }

    public VerticalGroup left() {
        this.align |= 8;
        this.align &= -17;
        return this;
    }

    public VerticalGroup right() {
        this.align |= 16;
        this.align &= -9;
        return this;
    }

    public int getAlign() {
        return this.align;
    }

    public VerticalGroup fill() {
        this.fill = 1.0f;
        return this;
    }

    public VerticalGroup fill(float fill) {
        this.fill = fill;
        return this;
    }

    public float getFill() {
        return this.fill;
    }
}

