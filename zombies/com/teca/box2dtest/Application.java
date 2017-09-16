/*
 * Decompiled with CFR 0_122.
 */
package com.teca.box2dtest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teca.box2dtest.managers.GameStateManager;

public class Application
extends ApplicationAdapter {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private GameStateManager gsm;

    @Override
    public void create() {
        this.camera = new OrthographicCamera();
        this.gsm = new GameStateManager(this);
    }

    @Override
    public void render() {
        this.gsm.update(Gdx.graphics.getDeltaTime());
        this.gsm.render();
    }

    private void update(float deltaTime) {
    }

    @Override
    public void resize(int width, int height) {
        this.gsm.resize(width, height);
    }

    public OrthographicCamera getCamera() {
        return this.camera;
    }

    public SpriteBatch getBatch() {
        return this.batch;
    }

    @Override
    public void dispose() {
        this.gsm.dispose();
    }
}

