/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.graphics.g3d.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderableSorter;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import java.util.Comparator;

public class DefaultRenderableSorter
implements RenderableSorter,
Comparator<Renderable> {
    private Camera camera;
    private final Vector3 tmpV1 = new Vector3();
    private final Vector3 tmpV2 = new Vector3();

    @Override
    public void sort(Camera camera, Array<Renderable> renderables) {
        this.camera = camera;
        renderables.sort(this);
    }

    @Override
    public int compare(Renderable o1, Renderable o2) {
        boolean b2;
        boolean b1 = o1.material.has(BlendingAttribute.Type) && ((BlendingAttribute)o1.material.get((long)BlendingAttribute.Type)).blended;
        boolean bl = b2 = o2.material.has(BlendingAttribute.Type) && ((BlendingAttribute)o2.material.get((long)BlendingAttribute.Type)).blended;
        if (b1 != b2) {
            return b1 ? 1 : -1;
        }
        o1.worldTransform.getTranslation(this.tmpV1);
        o2.worldTransform.getTranslation(this.tmpV2);
        float dst = (int)(1000.0f * this.camera.position.dst2(this.tmpV1)) - (int)(1000.0f * this.camera.position.dst2(this.tmpV2));
        int result = dst < 0.0f ? -1 : (dst > 0.0f ? 1 : 0);
        return b1 ? - result : result;
    }
}

