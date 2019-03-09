/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.physics.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;

public interface RayCastCallback {
    public float reportRayFixture(Fixture var1, Vector2 var2, Vector2 var3, float var4);
}

