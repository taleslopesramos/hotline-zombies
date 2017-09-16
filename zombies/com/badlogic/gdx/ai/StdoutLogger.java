/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai;

import com.badlogic.gdx.ai.Logger;
import java.io.PrintStream;

public class StdoutLogger
implements Logger {
    @Override
    public void debug(String tag, String message) {
        this.println("DEBUG", tag, message);
    }

    @Override
    public void debug(String tag, String message, Throwable exception) {
        this.println("DEBUG", tag, message, exception);
    }

    @Override
    public void info(String tag, String message) {
        this.println("INFO", tag, message);
    }

    @Override
    public void info(String tag, String message, Throwable exception) {
        this.println("INFO", tag, message, exception);
    }

    @Override
    public void error(String tag, String message) {
        this.println("ERROR", tag, message);
    }

    @Override
    public void error(String tag, String message, Throwable exception) {
        this.println("ERROR", tag, message, exception);
    }

    private void println(String level, String tag, String message) {
        System.out.println(level + " " + tag + ": " + message);
    }

    private void println(String level, String tag, String message, Throwable exception) {
        this.println(level, tag, message);
        exception.printStackTrace();
    }
}

