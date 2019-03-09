/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.fsm;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;

public class DefaultStateMachine<E, S extends State<E>>
implements StateMachine<E, S> {
    protected E owner;
    protected S currentState;
    protected S previousState;
    protected S globalState;

    public DefaultStateMachine() {
        this(null, null, null);
    }

    public DefaultStateMachine(E owner) {
        this(owner, null, null);
    }

    public DefaultStateMachine(E owner, S initialState) {
        this(owner, initialState, null);
    }

    public DefaultStateMachine(E owner, S initialState, S globalState) {
        this.owner = owner;
        this.setInitialState(initialState);
        this.setGlobalState(globalState);
    }

    public E getOwner() {
        return this.owner;
    }

    public void setOwner(E owner) {
        this.owner = owner;
    }

    @Override
    public void setInitialState(S state) {
        this.previousState = null;
        this.currentState = state;
    }

    @Override
    public void setGlobalState(S state) {
        this.globalState = state;
    }

    @Override
    public S getCurrentState() {
        return this.currentState;
    }

    @Override
    public S getGlobalState() {
        return this.globalState;
    }

    @Override
    public S getPreviousState() {
        return this.previousState;
    }

    @Override
    public void update() {
        if (this.globalState != null) {
            this.globalState.update(this.owner);
        }
        if (this.currentState != null) {
            this.currentState.update(this.owner);
        }
    }

    @Override
    public void changeState(S newState) {
        this.previousState = this.currentState;
        if (this.currentState != null) {
            this.currentState.exit(this.owner);
        }
        this.currentState = newState;
        if (this.currentState != null) {
            this.currentState.enter(this.owner);
        }
    }

    @Override
    public boolean revertToPreviousState() {
        if (this.previousState == null) {
            return false;
        }
        this.changeState(this.previousState);
        return true;
    }

    @Override
    public boolean isInState(S state) {
        return this.currentState == state;
    }

    @Override
    public boolean handleMessage(Telegram telegram) {
        if (this.currentState != null && this.currentState.onMessage(this.owner, telegram)) {
            return true;
        }
        if (this.globalState != null && this.globalState.onMessage(this.owner, telegram)) {
            return true;
        }
        return false;
    }
}

