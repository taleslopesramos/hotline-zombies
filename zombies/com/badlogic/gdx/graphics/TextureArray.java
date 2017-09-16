/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.graphics;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureArrayData;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TextureArray
extends GLTexture {
    static final Map<Application, Array<TextureArray>> managedTextureArrays = new HashMap<Application, Array<TextureArray>>();
    private TextureArrayData data;

    public /* varargs */ TextureArray(String ... internalPaths) {
        this(TextureArray.getInternalHandles(internalPaths));
    }

    public /* varargs */ TextureArray(FileHandle ... files) {
        this(false, files);
    }

    public /* varargs */ TextureArray(boolean useMipMaps, FileHandle ... files) {
        this(useMipMaps, Pixmap.Format.RGBA8888, files);
    }

    public /* varargs */ TextureArray(boolean useMipMaps, Pixmap.Format format, FileHandle ... files) {
        this(TextureArrayData.Factory.loadFromFiles(format, useMipMaps, files));
    }

    public TextureArray(TextureArrayData data) {
        super(35866, Gdx.gl.glGenTexture());
        if (Gdx.gl30 == null) {
            throw new GdxRuntimeException("TextureArray requires a device running with GLES 3.0 compatibilty");
        }
        this.load(data);
        if (data.isManaged()) {
            TextureArray.addManagedTexture(Gdx.app, this);
        }
    }

    private static /* varargs */ FileHandle[] getInternalHandles(String ... internalPaths) {
        FileHandle[] handles = new FileHandle[internalPaths.length];
        for (int i = 0; i < internalPaths.length; ++i) {
            handles[i] = Gdx.files.internal(internalPaths[i]);
        }
        return handles;
    }

    private void load(TextureArrayData data) {
        if (this.data != null && data.isManaged() != this.data.isManaged()) {
            throw new GdxRuntimeException("New data must have the same managed status as the old data");
        }
        this.data = data;
        this.bind();
        Gdx.gl30.glTexImage3D(35866, 0, data.getInternalFormat(), data.getWidth(), data.getHeight(), data.getDepth(), 0, data.getInternalFormat(), data.getGLType(), null);
        if (!data.isPrepared()) {
            data.prepare();
        }
        data.consumeTextureArrayData();
        this.setFilter(this.minFilter, this.magFilter);
        this.setWrap(this.uWrap, this.vWrap);
        Gdx.gl.glBindTexture(this.glTarget, 0);
    }

    @Override
    public int getWidth() {
        return this.data.getWidth();
    }

    @Override
    public int getHeight() {
        return this.data.getHeight();
    }

    @Override
    public int getDepth() {
        return this.data.getDepth();
    }

    @Override
    public boolean isManaged() {
        return this.data.isManaged();
    }

    @Override
    protected void reload() {
        if (!this.isManaged()) {
            throw new GdxRuntimeException("Tried to reload an unmanaged TextureArray");
        }
        this.glHandle = Gdx.gl.glGenTexture();
        this.load(this.data);
    }

    private static void addManagedTexture(Application app, TextureArray texture) {
        Array managedTextureArray = managedTextureArrays.get(app);
        if (managedTextureArray == null) {
            managedTextureArray = new Array();
        }
        managedTextureArray.add(texture);
        managedTextureArrays.put(app, managedTextureArray);
    }

    public static void clearAllTextureArrays(Application app) {
        managedTextureArrays.remove(app);
    }

    public static void invalidateAllTextureArrays(Application app) {
        Array<TextureArray> managedTextureArray = managedTextureArrays.get(app);
        if (managedTextureArray == null) {
            return;
        }
        for (int i = 0; i < managedTextureArray.size; ++i) {
            TextureArray textureArray = managedTextureArray.get(i);
            textureArray.reload();
        }
    }

    public static String getManagedStatus() {
        StringBuilder builder = new StringBuilder();
        builder.append("Managed TextureArrays/app: { ");
        for (Application app : managedTextureArrays.keySet()) {
            builder.append(TextureArray.managedTextureArrays.get((Object)app).size);
            builder.append(" ");
        }
        builder.append("}");
        return builder.toString();
    }

    public static int getNumManagedTextureArrays() {
        return TextureArray.managedTextureArrays.get((Object)Gdx.app).size;
    }
}

