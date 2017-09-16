/*
 * Decompiled with CFR 0_122.
 */
package com.jcraft.jorbis;

public class JOrbisException
extends Exception {
    private static final long serialVersionUID = 1;

    public JOrbisException() {
    }

    public JOrbisException(String s) {
        super("JOrbis: " + s);
    }
}

