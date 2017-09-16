/*
 * Decompiled with CFR 0_122.
 */
package com.teca.box2dtest.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class TiledObjectUtil {
    public static void parseTiledObjectLayer(World world, MapObjects objects) {
        for (MapObject object : objects) {
            if (!(object instanceof PolylineMapObject)) continue;
            ChainShape shape = TiledObjectUtil.createPolyLine((PolylineMapObject)object);
            BodyDef bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            Body body = world.createBody(bdef);
            body.createFixture(shape, 1.0f);
            shape.dispose();
        }
    }

    public static ChainShape createPolyLine(PolylineMapObject polyLine) {
        float[] vertices = polyLine.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];
        int i = 0;
        while (i < worldVertices.length) {
            worldVertices[i] = new Vector2(vertices[i * 2] / 32.0f, vertices[i * 2 + 1] / 32.0f);
            ++i;
        }
        ChainShape cs = new ChainShape();
        cs.createChain(worldVertices);
        return cs;
    }
}

