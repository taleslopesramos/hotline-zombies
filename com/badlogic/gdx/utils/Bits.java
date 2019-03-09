/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.utils;

public class Bits {
    long[] bits = new long[]{0};

    public Bits() {
    }

    public Bits(int nbits) {
        this.checkCapacity(nbits >>> 6);
    }

    public boolean get(int index) {
        int word = index >>> 6;
        if (word >= this.bits.length) {
            return false;
        }
        return (this.bits[word] & 1 << (index & 63)) != 0;
    }

    public boolean getAndClear(int index) {
        int word = index >>> 6;
        if (word >= this.bits.length) {
            return false;
        }
        long oldBits = this.bits[word];
        long[] arrl = this.bits;
        int n = word;
        arrl[n] = arrl[n] & (1 << (index & 63) ^ -1);
        return this.bits[word] != oldBits;
    }

    public boolean getAndSet(int index) {
        int word = index >>> 6;
        this.checkCapacity(word);
        long oldBits = this.bits[word];
        long[] arrl = this.bits;
        int n = word;
        arrl[n] = arrl[n] | 1 << (index & 63);
        return this.bits[word] == oldBits;
    }

    public void set(int index) {
        int word = index >>> 6;
        this.checkCapacity(word);
        long[] arrl = this.bits;
        int n = word;
        arrl[n] = arrl[n] | 1 << (index & 63);
    }

    public void flip(int index) {
        int word = index >>> 6;
        this.checkCapacity(word);
        long[] arrl = this.bits;
        int n = word;
        arrl[n] = arrl[n] ^ 1 << (index & 63);
    }

    private void checkCapacity(int len) {
        if (len >= this.bits.length) {
            long[] newBits = new long[len + 1];
            System.arraycopy(this.bits, 0, newBits, 0, this.bits.length);
            this.bits = newBits;
        }
    }

    public void clear(int index) {
        int word = index >>> 6;
        if (word >= this.bits.length) {
            return;
        }
        long[] arrl = this.bits;
        int n = word;
        arrl[n] = arrl[n] & (1 << (index & 63) ^ -1);
    }

    public void clear() {
        long[] bits = this.bits;
        int length = bits.length;
        for (int i = 0; i < length; ++i) {
            bits[i] = 0;
        }
    }

    public int numBits() {
        return this.bits.length << 6;
    }

    public int length() {
        long[] bits = this.bits;
        for (int word = bits.length - 1; word >= 0; --word) {
            long bitsAtWord = bits[word];
            if (bitsAtWord == 0) continue;
            for (int bit = 63; bit >= 0; --bit) {
                if ((bitsAtWord & 1 << (bit & 63)) == 0) continue;
                return (word << 6) + bit + 1;
            }
        }
        return 0;
    }

    public boolean isEmpty() {
        long[] bits = this.bits;
        int length = bits.length;
        for (int i = 0; i < length; ++i) {
            if (bits[i] == 0) continue;
            return false;
        }
        return true;
    }

    public int nextSetBit(int fromIndex) {
        int i;
        int word = fromIndex >>> 6;
        long[] bits = this.bits;
        int bitsLength = bits.length;
        if (word >= bitsLength) {
            return -1;
        }
        long bitsAtWord = bits[word];
        if (bitsAtWord != 0) {
            for (i = fromIndex & 63; i < 64; ++i) {
                if ((bitsAtWord & 1 << (i & 63)) == 0) continue;
                return (word << 6) + i;
            }
        }
        ++word;
        while (word < bitsLength) {
            if (word != 0 && (bitsAtWord = bits[word]) != 0) {
                for (i = 0; i < 64; ++i) {
                    if ((bitsAtWord & 1 << (i & 63)) == 0) continue;
                    return (word << 6) + i;
                }
            }
            ++word;
        }
        return -1;
    }

    public int nextClearBit(int fromIndex) {
        int i;
        int word = fromIndex >>> 6;
        long[] bits = this.bits;
        int bitsLength = bits.length;
        if (word >= bitsLength) {
            return bits.length << 6;
        }
        long bitsAtWord = bits[word];
        for (i = fromIndex & 63; i < 64; ++i) {
            if ((bitsAtWord & 1 << (i & 63)) != 0) continue;
            return (word << 6) + i;
        }
        ++word;
        while (word < bitsLength) {
            if (word == 0) {
                return word << 6;
            }
            bitsAtWord = bits[word];
            for (i = 0; i < 64; ++i) {
                if ((bitsAtWord & 1 << (i & 63)) != 0) continue;
                return (word << 6) + i;
            }
            ++word;
        }
        return bits.length << 6;
    }

    public void and(Bits other) {
        int i;
        int commonWords = Math.min(this.bits.length, other.bits.length);
        for (i = 0; commonWords > i; ++i) {
            long[] arrl = this.bits;
            int n = i;
            arrl[n] = arrl[n] & other.bits[i];
        }
        if (this.bits.length > commonWords) {
            int s = this.bits.length;
            for (i = commonWords; s > i; ++i) {
                this.bits[i] = 0;
            }
        }
    }

    public void andNot(Bits other) {
        int j = this.bits.length;
        int k = other.bits.length;
        for (int i = 0; i < j && i < k; ++i) {
            long[] arrl = this.bits;
            int n = i;
            arrl[n] = arrl[n] & (other.bits[i] ^ -1);
        }
    }

    public void or(Bits other) {
        int i;
        int commonWords = Math.min(this.bits.length, other.bits.length);
        for (i = 0; commonWords > i; ++i) {
            long[] arrl = this.bits;
            int n = i;
            arrl[n] = arrl[n] | other.bits[i];
        }
        if (commonWords < other.bits.length) {
            this.checkCapacity(other.bits.length);
            int s = other.bits.length;
            for (i = commonWords; s > i; ++i) {
                this.bits[i] = other.bits[i];
            }
        }
    }

    public void xor(Bits other) {
        int i;
        int commonWords = Math.min(this.bits.length, other.bits.length);
        for (i = 0; commonWords > i; ++i) {
            long[] arrl = this.bits;
            int n = i;
            arrl[n] = arrl[n] ^ other.bits[i];
        }
        if (commonWords < other.bits.length) {
            this.checkCapacity(other.bits.length);
            int s = other.bits.length;
            for (i = commonWords; s > i; ++i) {
                this.bits[i] = other.bits[i];
            }
        }
    }

    public boolean intersects(Bits other) {
        long[] bits = this.bits;
        long[] otherBits = other.bits;
        for (int i = Math.min((int)bits.length, (int)otherBits.length) - 1; i >= 0; --i) {
            if ((bits[i] & otherBits[i]) == 0) continue;
            return true;
        }
        return false;
    }

    public boolean containsAll(Bits other) {
        int i;
        long[] bits = this.bits;
        long[] otherBits = other.bits;
        int otherBitsLength = otherBits.length;
        for (i = bitsLength = bits.length; i < otherBitsLength; ++i) {
            if (otherBits[i] == 0) continue;
            return false;
        }
        for (i = Math.min((int)bitsLength, (int)otherBitsLength) - 1; i >= 0; --i) {
            if ((bits[i] & otherBits[i]) == otherBits[i]) continue;
            return false;
        }
        return true;
    }

    public int hashCode() {
        int word = this.length() >>> 6;
        int hash = 0;
        for (int i = 0; word >= i; ++i) {
            hash = 127 * hash + (int)(this.bits[i] ^ this.bits[i] >>> 32);
        }
        return hash;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Bits other = (Bits)obj;
        long[] otherBits = other.bits;
        int commonWords = Math.min(this.bits.length, otherBits.length);
        for (int i = 0; commonWords > i; ++i) {
            if (this.bits[i] == otherBits[i]) continue;
            return false;
        }
        if (this.bits.length == otherBits.length) {
            return true;
        }
        return this.length() == other.length();
    }
}

