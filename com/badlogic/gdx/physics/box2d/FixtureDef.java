/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.physics.box2d;

import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Shape;

public class FixtureDef {
    public Shape shape;
    public float friction = 0.2f;
    public float restitution = 0.0f;
    public float density = 0.0f;
    public boolean isSensor = false;
    public final Filter filter = new Filter();
}

