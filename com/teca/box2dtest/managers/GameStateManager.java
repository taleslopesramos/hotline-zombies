/*
 * Decompiled with CFR 0_122.
 */
package com.teca.box2dtest.managers;

import com.teca.box2dtest.Application;
import com.teca.box2dtest.states.GameOverState;
import com.teca.box2dtest.states.GameState;
import com.teca.box2dtest.states.MainMenuState;
import com.teca.box2dtest.states.PlayState;
import com.teca.box2dtest.states.SplashState;
import java.util.Stack;

public class GameStateManager {
    private final Application app;
    private Stack<GameState> states;
    private static /* synthetic */ int[] $SWITCH_TABLE$com$teca$box2dtest$managers$GameStateManager$State;

    public GameStateManager(Application application) {
        this.app = application;
        this.states = new Stack();
        this.setState(State.SPLASH);
    }

    public Application getApplication() {
        return this.app;
    }

    public void update(float delta) {
        this.states.peek().update(delta);
    }

    public void render() {
        this.states.peek().render();
    }

    public void dispose() {
        for (GameState gs : this.states) {
            gs.dispose();
        }
    }

    public void resize(int w, int h) {
        this.states.peek().resize(w, h);
    }

    public void setState(State state) {
        if (this.states.size() >= 1) {
            this.states.pop().dispose();
        }
        this.states.push(this.getState(state));
    }

    public GameState getState(State state) {
        switch (GameStateManager.$SWITCH_TABLE$com$teca$box2dtest$managers$GameStateManager$State()[state.ordinal()]) {
            case 1: {
                return new SplashState(this);
            }
            case 3: {
                return new MainMenuState(this);
            }
            case 2: {
                return new PlayState(this);
            }
            case 4: {
                return new GameOverState(this);
            }
        }
        return null;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$teca$box2dtest$managers$GameStateManager$State() {
        int[] arrn;
        int[] arrn2 = $SWITCH_TABLE$com$teca$box2dtest$managers$GameStateManager$State;
        if (arrn2 != null) {
            return arrn2;
        }
        arrn = new int[State.values().length];
        try {
            arrn[State.GAMEOVER.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[State.MAINMENU.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[State.PLAY.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[State.SPLASH.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        $SWITCH_TABLE$com$teca$box2dtest$managers$GameStateManager$State = arrn;
        return $SWITCH_TABLE$com$teca$box2dtest$managers$GameStateManager$State;
    }

    public static enum State {
        SPLASH,
        PLAY,
        MAINMENU,
        GAMEOVER;
        

        private State(String string2, int n2) {
        }
    }

}

