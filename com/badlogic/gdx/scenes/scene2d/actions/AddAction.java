/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class AddAction
extends Action {
    private Action action;

    @Override
    public boolean act(float delta) {
        this.target.addAction(this.action);
        return true;
    }

    public Action getAction() {
        return this.action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    @Override
    public void restart() {
        if (this.action != null) {
            this.action.restart();
        }
    }

    @Override
    public void reset() {
        super.reset();
        this.action = null;
    }
}

