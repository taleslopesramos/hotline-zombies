/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.physics.box2d;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

public interface ContactListener {
    public void beginContact(Contact var1);

    public void endContact(Contact var1);

    public void preSolve(Contact var1, Manifold var2);

    public void postSolve(Contact var1, ContactImpulse var2);
}

