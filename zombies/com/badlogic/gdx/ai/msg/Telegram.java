/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.msg;

import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.utils.Pool;

public class Telegram
implements Comparable<Telegram>,
Pool.Poolable {
    public static final int RETURN_RECEIPT_UNNEEDED = 0;
    public static final int RETURN_RECEIPT_NEEDED = 1;
    public static final int RETURN_RECEIPT_SENT = 2;
    public Telegraph sender;
    public Telegraph receiver;
    public int message;
    public int returnReceiptStatus;
    private float timestamp;
    public Object extraInfo;

    public float getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(float timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public void reset() {
        this.sender = null;
        this.receiver = null;
        this.message = 0;
        this.returnReceiptStatus = 0;
        this.extraInfo = null;
        this.timestamp = 0.0f;
    }

    @Override
    public int compareTo(Telegram other) {
        if (this.equals(other)) {
            return 0;
        }
        return this.timestamp - other.timestamp < 0.0f ? -1 : 1;
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + this.message;
        result = 31 * result + (this.receiver == null ? 0 : this.receiver.hashCode());
        result = 31 * result + (this.sender == null ? 0 : this.sender.hashCode());
        result = 31 * result + Float.floatToIntBits(this.timestamp);
        return result;
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
        Telegram other = (Telegram)obj;
        if (this.message != other.message) {
            return false;
        }
        if (Float.floatToIntBits(this.timestamp) != Float.floatToIntBits(other.timestamp)) {
            return false;
        }
        if (this.sender == null ? other.sender != null : !this.sender.equals(other.sender)) {
            return false;
        }
        if (this.receiver == null ? other.receiver != null : !this.receiver.equals(other.receiver)) {
            return false;
        }
        return true;
    }
}

