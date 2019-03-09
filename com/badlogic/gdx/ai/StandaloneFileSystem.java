/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.ai.FileSystem;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import java.io.File;

public class StandaloneFileSystem
implements FileSystem {
    @Override
    public FileHandleResolver newResolver(final Files.FileType fileType) {
        return new FileHandleResolver(){

            @Override
            public FileHandle resolve(String fileName) {
                return new DesktopFileHandle(fileName, fileType);
            }
        };
    }

    @Override
    public FileHandle newFileHandle(String fileName) {
        return new DesktopFileHandle(fileName, Files.FileType.Absolute);
    }

    @Override
    public FileHandle newFileHandle(File file) {
        return new DesktopFileHandle(file, Files.FileType.Absolute);
    }

    @Override
    public FileHandle newFileHandle(String fileName, Files.FileType type) {
        return new DesktopFileHandle(fileName, type);
    }

    @Override
    public FileHandle newFileHandle(File file, Files.FileType type) {
        return new DesktopFileHandle(file, type);
    }

    public static class DesktopFileHandle
    extends FileHandle {
        public static final String externalPath = System.getProperty("user.home") + File.separator;
        public static final String localPath = new File("").getAbsolutePath() + File.separator;

        public DesktopFileHandle(String fileName, Files.FileType type) {
            super(fileName, type);
        }

        public DesktopFileHandle(File file, Files.FileType type) {
            super(file, type);
        }

        @Override
        public FileHandle child(String name) {
            if (this.file.getPath().length() == 0) {
                return new DesktopFileHandle(new File(name), this.type);
            }
            return new DesktopFileHandle(new File(this.file, name), this.type);
        }

        @Override
        public FileHandle sibling(String name) {
            if (this.file.getPath().length() == 0) {
                throw new GdxRuntimeException("Cannot get the sibling of the root.");
            }
            return new DesktopFileHandle(new File(this.file.getParent(), name), this.type);
        }

        @Override
        public FileHandle parent() {
            File parent = this.file.getParentFile();
            if (parent == null) {
                parent = this.type == Files.FileType.Absolute ? new File("/") : new File("");
            }
            return new DesktopFileHandle(parent, this.type);
        }

        @Override
        public File file() {
            if (this.type == Files.FileType.External) {
                return new File(externalPath, this.file.getPath());
            }
            if (this.type == Files.FileType.Local) {
                return new File(localPath, this.file.getPath());
            }
            return this.file;
        }
    }

}

