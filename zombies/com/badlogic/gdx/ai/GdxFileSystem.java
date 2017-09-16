/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.FileSystem;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.AbsoluteFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import java.io.File;

public class GdxFileSystem
implements FileSystem {
    @Override
    public FileHandleResolver newResolver(Files.FileType fileType) {
        switch (fileType) {
            case Absolute: {
                return new AbsoluteFileHandleResolver();
            }
            case Classpath: {
                return new ClasspathFileHandleResolver();
            }
            case External: {
                return new ExternalFileHandleResolver();
            }
            case Internal: {
                return new InternalFileHandleResolver();
            }
            case Local: {
                return new LocalFileHandleResolver();
            }
        }
        return null;
    }

    @Override
    public FileHandle newFileHandle(String fileName) {
        return Gdx.files.absolute(fileName);
    }

    @Override
    public FileHandle newFileHandle(File file) {
        return Gdx.files.absolute(file.getAbsolutePath());
    }

    @Override
    public FileHandle newFileHandle(String fileName, Files.FileType type) {
        return Gdx.files.getFileHandle(fileName, type);
    }

    @Override
    public FileHandle newFileHandle(File file, Files.FileType type) {
        return Gdx.files.getFileHandle(file.getPath(), type);
    }

}

