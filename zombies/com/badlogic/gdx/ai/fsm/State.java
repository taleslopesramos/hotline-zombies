/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.fsm;

import com.badlogic.gdx.ai.msg.Telegram;

public interface State<E> {
    public void enter(E var1);

    public void update(E var1);

    public void exit(E var1);

    public boolean onMessage(E var1, Telegram var2);
}

