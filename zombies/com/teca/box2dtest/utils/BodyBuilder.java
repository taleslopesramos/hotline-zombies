/*
 * Decompiled with CFR 0_122.
 */
package com.teca.box2dtest.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class BodyBuilder {
    public static Body createBox(World world, Vector2 position, int width, int height, boolean isStatic) {
        BodyDef def = new BodyDef();
        def.type = isStatic ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
        def.position.set(position.x / 32.0f, position.y / 32.0f);
        def.fixedRotation = true;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((float)(width / 2) / 32.0f, (float)(height / 2) / 32.0f);
        FixtureDef fixdef = new FixtureDef();
        fixdef.shape = shape;
        fixdef.density = 1.0f;
        Body pBody = world.createBody(def);
        pBody.createFixture(fixdef);
        shape.dispose();
        return pBody;
    }

    public static Body createCircle(World world, Vector2 position, int radius, boolean isStatic) {
        BodyDef def = new BodyDef();
        def.type = isStatic ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
        def.position.set(position.x, position.y);
        def.fixedRotation = true;
        Body pBody = world.createBody(def);
        CircleShape shape = new CircleShape();
        shape.setRadius((float)radius / 32.0f);
        pBody.createFixture(shape, 1.0f);
        shape.dispose();
        return pBody;
    }
}

