/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.msg;

import com.badlogic.gdx.ai.msg.MessageDispatcher;

public class MessageManager
extends MessageDispatcher {
    private static final MessageManager instance = new MessageManager();

    private MessageManager() {
    }

    public static MessageManager getInstance() {
        return instance;
    }
}

