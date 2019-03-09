/*
 * Decompiled with CFR 0_122.
 */
package com.teca.box2dtest.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teca.box2dtest.Application;
import com.teca.box2dtest.managers.GameStateManager;

public abstract class GameState {
    protected GameStateManager gsm;
    protected Application app;
    protected SpriteBatch batch;
    protected OrthographicCamera camera;

    protected GameState(GameStateManager gsm) {
        this.gsm = gsm;
        this.app = gsm.getApplication();
        this.batch = this.app.getBatch();
        this.camera = this.app.getCamera();
    }

    public void resize(int w, int h) {
        this.camera.setToOrtho(false, w, h);
    }

    public abstract void update(float var1);

    public abstract void render();

    public abstract void dispose();
}

