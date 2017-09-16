/*
 * Decompiled with CFR 0_122.
 */
package com.teca.box2dtest.states;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.teca.box2dtest.managers.GameStateManager;
import com.teca.box2dtest.states.GameState;

public class GameOverState
extends GameState {
    private BitmapFont font;
    private Texture menu = new Texture("menu.png");
    private Texture quit = new Texture("quit.png");
    private Texture gameover = new Texture("gameover.png");
    private SpriteBatch batch = new SpriteBatch();
    private int roundsSurvived;
    private int zombiesKilled;
    private int totalPoints;
    private Vector3 touchPos = new Vector3();
    private Sound gameOverSound;

    public GameOverState(GameStateManager gsm) {
        super(gsm);
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 640.0f, 640.0f);
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(16384);
        this.batch.setProjectionMatrix(this.camera.combined);
        this.batch.begin();
        this.batch.draw(this.menu, 260.0f, 180.0f, 150.0f, 70.0f);
        this.batch.draw(this.quit, 260.0f, 90.0f, 150.0f, 70.0f);
        this.batch.draw(this.gameover, 80.0f, 300.0f, 500.0f, 200.0f);
        if (Gdx.input.justTouched()) {
            this.touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0.0f);
            this.camera.unproject(this.touchPos);
            if (this.touchPos.y >= 180.0f && this.touchPos.y <= 260.0f && this.touchPos.x >= 260.0f && this.touchPos.x <= 410.0f) {
                this.gsm.setState(GameStateManager.State.MAINMENU);
            }
            if (this.touchPos.y >= 90.0f && this.touchPos.y <= 170.0f && this.touchPos.x >= 260.0f && this.touchPos.x <= 410.0f) {
                Gdx.app.exit();
            }
        }
        this.batch.end();
    }

    @Override
    public void dispose() {
    }
}

