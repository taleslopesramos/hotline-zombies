/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.DefaultTimepiece;
import com.badlogic.gdx.ai.FileSystem;
import com.badlogic.gdx.ai.GdxFileSystem;
import com.badlogic.gdx.ai.GdxLogger;
import com.badlogic.gdx.ai.Logger;
import com.badlogic.gdx.ai.NullLogger;
import com.badlogic.gdx.ai.StandaloneFileSystem;
import com.badlogic.gdx.ai.Timepiece;

public final class GdxAI {
    private static Timepiece timepiece = new DefaultTimepiece();
    private static Logger logger = Gdx.app == null ? new NullLogger() : new GdxLogger();
    private static FileSystem fileSystem = Gdx.files == null ? new StandaloneFileSystem() : new GdxFileSystem();

    private GdxAI() {
    }

    public static Timepiece getTimepiece() {
        return timepiece;
    }

    public static void setTimepiece(Timepiece timepiece) {
        GdxAI.timepiece = timepiece;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        GdxAI.logger = logger;
    }

    public static FileSystem getFileSystem() {
        return fileSystem;
    }

    public static void setFileSystem(FileSystem fileSystem) {
        GdxAI.fileSystem = fileSystem;
    }
}

