/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DataOutput
extends DataOutputStream {
    public DataOutput(OutputStream out) {
        super(out);
    }

    public int writeInt(int value, boolean optimizePositive) throws IOException {
        if (!optimizePositive) {
            value = value << 1 ^ value >> 31;
        }
        if (value >>> 7 == 0) {
            this.write((byte)value);
            return 1;
        }
        this.write((byte)(value & 127 | 128));
        if (value >>> 14 == 0) {
            this.write((byte)(value >>> 7));
            return 2;
        }
        this.write((byte)(value >>> 7 | 128));
        if (value >>> 21 == 0) {
            this.write((byte)(value >>> 14));
            return 3;
        }
        this.write((byte)(value >>> 14 | 128));
        if (value >>> 28 == 0) {
            this.write((byte)(value >>> 21));
            return 4;
        }
        this.write((byte)(value >>> 21 | 128));
        this.write((byte)(value >>> 28));
        return 5;
    }

    public void writeString(String value) throws IOException {
        char c;
        int charIndex;
        if (value == null) {
            this.write(0);
            return;
        }
        int charCount = value.length();
        if (charCount == 0) {
            this.writeByte(1);
            return;
        }
        this.writeInt(charCount + 1, true);
        for (charIndex = 0; charIndex < charCount && (c = value.charAt(charIndex)) <= ''; ++charIndex) {
            this.write((byte)c);
        }
        if (charIndex < charCount) {
            this.writeString_slow(value, charCount, charIndex);
        }
    }

    private void writeString_slow(String value, int charCount, int charIndex) throws IOException {
        while (charIndex < charCount) {
            char c = value.charAt(charIndex);
            if (c <= '') {
                this.write((byte)c);
            } else if (c > '\u07ff') {
                this.write((byte)(224 | c >> 12 & 15));
                this.write((byte)(128 | c >> 6 & 63));
                this.write((byte)(128 | c & 63));
            } else {
                this.write((byte)(192 | c >> 6 & 31));
                this.write((byte)(128 | c & 63));
            }
            ++charIndex;
        }
    }
}

