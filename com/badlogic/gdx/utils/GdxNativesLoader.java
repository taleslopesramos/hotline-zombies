/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.utils;

import com.badlogic.gdx.utils.SharedLibraryLoader;
import java.io.PrintStream;

public class GdxNativesLoader {
    public static boolean disableNativesLoading = false;
    private static boolean nativesLoaded;

    public static synchronized void load() {
        if (nativesLoaded) {
            return;
        }
        nativesLoaded = true;
        if (disableNativesLoading) {
            System.out.println("Native loading is disabled.");
            return;
        }
        new SharedLibraryLoader().load("gdx");
    }
}

