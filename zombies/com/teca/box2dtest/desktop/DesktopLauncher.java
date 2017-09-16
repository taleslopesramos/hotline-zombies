/*
 * Decompiled with CFR 0_122.
 */
package com.teca.box2dtest.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.teca.box2dtest.Application;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 720;
        config.height = 480;
        config.backgroundFPS = 60;
        config.foregroundFPS = 60;
        config.resizable = false;
        config.fullscreen = false;
        new com.badlogic.gdx.backends.lwjgl.LwjglApplication((ApplicationListener)new Application(), config);
    }
}

