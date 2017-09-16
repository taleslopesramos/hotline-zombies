/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.graphics.g3d.utils.shapebuilders;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BaseShapeBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class BoxShapeBuilder
extends BaseShapeBuilder {
    public static void build(MeshPartBuilder builder, BoundingBox box) {
        builder.box(box.getCorner000(tmpV0), box.getCorner010(tmpV1), box.getCorner100(tmpV2), box.getCorner110(tmpV3), box.getCorner001(tmpV4), box.getCorner011(tmpV5), box.getCorner101(tmpV6), box.getCorner111(tmpV7));
    }

    public static void build(MeshPartBuilder builder, MeshPartBuilder.VertexInfo corner000, MeshPartBuilder.VertexInfo corner010, MeshPartBuilder.VertexInfo corner100, MeshPartBuilder.VertexInfo corner110, MeshPartBuilder.VertexInfo corner001, MeshPartBuilder.VertexInfo corner011, MeshPartBuilder.VertexInfo corner101, MeshPartBuilder.VertexInfo corner111) {
        builder.ensureVertices(8);
        short i000 = builder.vertex(corner000);
        short i100 = builder.vertex(corner100);
        short i110 = builder.vertex(corner110);
        short i010 = builder.vertex(corner010);
        short i001 = builder.vertex(corner001);
        short i101 = builder.vertex(corner101);
        short i111 = builder.vertex(corner111);
        short i011 = builder.vertex(corner011);
        int primitiveType = builder.getPrimitiveType();
        if (primitiveType == 1) {
            builder.ensureIndices(24);
            builder.rect(i000, i100, i110, i010);
            builder.rect(i101, i001, i011, i111);
            builder.index(i000, i001, i010, i011, i110, i111, i100, i101);
        } else if (primitiveType == 0) {
            builder.ensureRectangleIndices(2);
            builder.rect(i000, i100, i110, i010);
            builder.rect(i101, i001, i011, i111);
        } else {
            builder.ensureRectangleIndices(6);
            builder.rect(i000, i100, i110, i010);
            builder.rect(i101, i001, i011, i111);
            builder.rect(i000, i010, i011, i001);
            builder.rect(i101, i111, i110, i100);
            builder.rect(i101, i100, i000, i001);
            builder.rect(i110, i111, i011, i010);
        }
    }

    public static void build(MeshPartBuilder builder, Vector3 corner000, Vector3 corner010, Vector3 corner100, Vector3 corner110, Vector3 corner001, Vector3 corner011, Vector3 corner101, Vector3 corner111) {
        if ((builder.getAttributes().getMask() & 408) == 0) {
            BoxShapeBuilder.build(builder, vertTmp1.set(corner000, null, null, null), vertTmp2.set(corner010, null, null, null), vertTmp3.set(corner100, null, null, null), vertTmp4.set(corner110, null, null, null), vertTmp5.set(corner001, null, null, null), vertTmp6.set(corner011, null, null, null), vertTmp7.set(corner101, null, null, null), vertTmp8.set(corner111, null, null, null));
        } else {
            builder.ensureVertices(24);
            builder.ensureRectangleIndices(6);
            Vector3 nor = tmpV1.set(corner000).lerp(corner110, 0.5f).sub(tmpV2.set(corner001).lerp(corner111, 0.5f)).nor();
            builder.rect(corner000, corner010, corner110, corner100, nor);
            builder.rect(corner011, corner001, corner101, corner111, nor.scl(-1.0f));
            nor = tmpV1.set(corner000).lerp(corner101, 0.5f).sub(tmpV2.set(corner010).lerp(corner111, 0.5f)).nor();
            builder.rect(corner001, corner000, corner100, corner101, nor);
            builder.rect(corner010, corner011, corner111, corner110, nor.scl(-1.0f));
            nor = tmpV1.set(corner000).lerp(corner011, 0.5f).sub(tmpV2.set(corner100).lerp(corner111, 0.5f)).nor();
            builder.rect(corner001, corner011, corner010, corner000, nor);
            builder.rect(corner100, corner110, corner111, corner101, nor.scl(-1.0f));
        }
    }

    public static void build(MeshPartBuilder builder, Matrix4 transform) {
        BoxShapeBuilder.build(builder, BoxShapeBuilder.obtainV3().set(-0.5f, -0.5f, -0.5f).mul(transform), BoxShapeBuilder.obtainV3().set(-0.5f, 0.5f, -0.5f).mul(transform), BoxShapeBuilder.obtainV3().set(0.5f, -0.5f, -0.5f).mul(transform), BoxShapeBuilder.obtainV3().set(0.5f, 0.5f, -0.5f).mul(transform), BoxShapeBuilder.obtainV3().set(-0.5f, -0.5f, 0.5f).mul(transform), BoxShapeBuilder.obtainV3().set(-0.5f, 0.5f, 0.5f).mul(transform), BoxShapeBuilder.obtainV3().set(0.5f, -0.5f, 0.5f).mul(transform), BoxShapeBuilder.obtainV3().set(0.5f, 0.5f, 0.5f).mul(transform));
        BoxShapeBuilder.freeAll();
    }

    public static void build(MeshPartBuilder builder, float width, float height, float depth) {
        BoxShapeBuilder.build(builder, 0.0f, 0.0f, 0.0f, width, height, depth);
    }

    public static void build(MeshPartBuilder builder, float x, float y, float z, float width, float height, float depth) {
        float hw = width * 0.5f;
        float hh = height * 0.5f;
        float hd = depth * 0.5f;
        float x0 = x - hw;
        float y0 = y - hh;
        float z0 = z - hd;
        float x1 = x + hw;
        float y1 = y + hh;
        float z1 = z + hd;
        BoxShapeBuilder.build(builder, BoxShapeBuilder.obtainV3().set(x0, y0, z0), BoxShapeBuilder.obtainV3().set(x0, y1, z0), BoxShapeBuilder.obtainV3().set(x1, y0, z0), BoxShapeBuilder.obtainV3().set(x1, y1, z0), BoxShapeBuilder.obtainV3().set(x0, y0, z1), BoxShapeBuilder.obtainV3().set(x0, y1, z1), BoxShapeBuilder.obtainV3().set(x1, y0, z1), BoxShapeBuilder.obtainV3().set(x1, y1, z1));
        BoxShapeBuilder.freeAll();
    }
}

