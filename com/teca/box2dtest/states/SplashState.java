/*
 * Decompiled with CFR 0_122.
 */
package com.teca.box2dtest.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.teca.box2dtest.managers.GameStateManager;
import com.teca.box2dtest.states.GameState;
import java.io.PrintStream;

public class SplashState
extends GameState {
    float acc = 0.0f;
    Texture tecagames = new Texture("tecagames.png");
    SpriteBatch batch = new SpriteBatch();
    OrthographicCamera camera = new OrthographicCamera();

    public SplashState(GameStateManager gsm) {
        super(gsm);
        this.camera.setToOrtho(false, 640.0f, 640.0f);
    }

    @Override
    public void update(float delta) {
        this.acc += delta;
        System.out.println(this.acc);
        if (this.acc >= 1.0f) {
            this.gsm.setState(GameStateManager.State.MAINMENU);
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(16384);
        this.camera.update();
        this.batch.setProjectionMatrix(this.camera.combined);
        this.batch.begin();
        this.batch.draw(this.tecagames, 50.0f, 50.0f, 500.0f, 500.0f);
        this.batch.end();
    }

    @Override
    public void dispose() {
        this.batch.dispose();
        this.tecagames.dispose();
    }
}

