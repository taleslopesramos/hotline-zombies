/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import java.io.File;

public interface FileSystem {
    public FileHandleResolver newResolver(Files.FileType var1);

    public FileHandle newFileHandle(String var1);

    public FileHandle newFileHandle(File var1);

    public FileHandle newFileHandle(String var1, Files.FileType var2);

    public FileHandle newFileHandle(File var1, Files.FileType var2);
}

