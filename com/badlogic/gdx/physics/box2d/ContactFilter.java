/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.physics.box2d;

import com.badlogic.gdx.physics.box2d.Fixture;

public interface ContactFilter {
    public boolean shouldCollide(Fixture var1, Fixture var2);
}

