/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.assets.loaders.resolvers;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

public class ClasspathFileHandleResolver
implements FileHandleResolver {
    @Override
    public FileHandle resolve(String fileName) {
        return Gdx.files.classpath(fileName);
    }
}

