/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.graphics.g3d;

import com.badlogic.gdx.utils.Array;

public abstract class Attribute
implements Comparable<Attribute> {
    private static final Array<String> types = new Array<T>();
    public final long type;
    private final int typeBit;

    public static final long getAttributeType(String alias) {
        for (int i = 0; i < Attribute.types.size; ++i) {
            if (types.get(i).compareTo(alias) != 0) continue;
            return 1 << i;
        }
        return 0;
    }

    public static final String getAttributeAlias(long type) {
        int idx = -1;
        while (type != 0 && ++idx < 63 && (type >> idx & 1) == 0) {
        }
        return idx >= 0 && idx < Attribute.types.size ? types.get(idx) : null;
    }

    protected static final long register(String alias) {
        long result = Attribute.getAttributeType(alias);
        if (result > 0) {
            return result;
        }
        types.add(alias);
        return 1 << Attribute.types.size - 1;
    }

    protected Attribute(long type) {
        this.type = type;
        this.typeBit = Long.numberOfTrailingZeros(type);
    }

    public abstract Attribute copy();

    protected boolean equals(Attribute other) {
        return other.hashCode() == this.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Attribute)) {
            return false;
        }
        Attribute other = (Attribute)obj;
        if (this.type != other.type) {
            return false;
        }
        return this.equals(other);
    }

    public String toString() {
        return Attribute.getAttributeAlias(this.type);
    }

    public int hashCode() {
        return 7489 * this.typeBit;
    }
}

