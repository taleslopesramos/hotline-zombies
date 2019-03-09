/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.Logger;

public class GdxLogger
implements Logger {
    @Override
    public void debug(String tag, String message) {
        Gdx.app.debug(tag, message);
    }

    @Override
    public void debug(String tag, String message, Throwable exception) {
        Gdx.app.debug(tag, message, exception);
    }

    @Override
    public void info(String tag, String message) {
        Gdx.app.log(tag, message);
    }

    @Override
    public void info(String tag, String message, Throwable exception) {
        Gdx.app.log(tag, message, exception);
    }

    @Override
    public void error(String tag, String message) {
        Gdx.app.error(tag, message);
    }

    @Override
    public void error(String tag, String message, Throwable exception) {
        Gdx.app.error(tag, message, exception);
    }
}

