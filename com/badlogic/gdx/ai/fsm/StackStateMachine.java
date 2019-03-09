/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.fsm;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.utils.Array;

public class StackStateMachine<E, S extends State<E>>
extends DefaultStateMachine<E, S> {
    private Array<S> stateStack;

    public StackStateMachine() {
        this(null, null, null);
    }

    public StackStateMachine(E owner) {
        this(owner, null, null);
    }

    public StackStateMachine(E owner, S initialState) {
        this(owner, initialState, null);
    }

    public StackStateMachine(E owner, S initialState, S globalState) {
        super(owner, initialState, globalState);
    }

    @Override
    public void setInitialState(S state) {
        if (this.stateStack == null) {
            this.stateStack = new Array();
        }
        this.stateStack.clear();
        this.currentState = state;
    }

    @Override
    public S getCurrentState() {
        return (S)this.currentState;
    }

    @Override
    public S getPreviousState() {
        if (this.stateStack.size == 0) {
            return null;
        }
        return (S)((State)this.stateStack.peek());
    }

    @Override
    public void changeState(S newState) {
        this.changeState(newState, true);
    }

    @Override
    public boolean revertToPreviousState() {
        if (this.stateStack.size == 0) {
            return false;
        }
        State previousState = (State)this.stateStack.pop();
        this.changeState(previousState, false);
        return true;
    }

    private void changeState(S newState, boolean pushCurrentStateToStack) {
        if (pushCurrentStateToStack && this.currentState != null) {
            this.stateStack.add((State)this.currentState);
        }
        if (this.currentState != null) {
            this.currentState.exit(this.owner);
        }
        this.currentState = newState;
        this.currentState.enter(this.owner);
    }
}

