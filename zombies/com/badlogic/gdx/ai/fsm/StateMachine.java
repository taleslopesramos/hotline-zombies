/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.fsm;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;

public interface StateMachine<E, S extends State<E>>
extends Telegraph {
    public void update();

    public void changeState(S var1);

    public boolean revertToPreviousState();

    public void setInitialState(S var1);

    public void setGlobalState(S var1);

    public S getCurrentState();

    public S getGlobalState();

    public S getPreviousState();

    public boolean isInState(S var1);

    @Override
    public boolean handleMessage(Telegram var1);
}

