/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai;

import com.badlogic.gdx.ai.Timepiece;

public class DefaultTimepiece
implements Timepiece {
    private float time = 0.0f;
    private float deltaTime = 0.0f;
    private float maxDeltaTime;

    public DefaultTimepiece() {
        this(Float.POSITIVE_INFINITY);
    }

    public DefaultTimepiece(float maxDeltaTime) {
        this.maxDeltaTime = maxDeltaTime;
    }

    @Override
    public float getTime() {
        return this.time;
    }

    @Override
    public float getDeltaTime() {
        return this.deltaTime;
    }

    @Override
    public void update(float deltaTime) {
        this.deltaTime = deltaTime > this.maxDeltaTime ? this.maxDeltaTime : deltaTime;
        this.time += this.deltaTime;
    }
}

