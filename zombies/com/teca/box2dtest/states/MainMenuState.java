/*
 * Decompiled with CFR 0_122.
 */
package com.teca.box2dtest.states;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.teca.box2dtest.managers.GameStateManager;
import com.teca.box2dtest.states.GameState;
import com.teca.box2dtest.utils.Animator;

public class MainMenuState
extends GameState {
    private SpriteBatch batch;
    private Vector3 touchPos;
    private Animator background;
    private Animator player;
    private Animator zombie;
    OrthographicCamera camera;
    private Texture versus;
    private Texture title;
    private Texture play;
    private Texture quit;
    private Texture mute;
    private Texture unmute;
    private Music music;
    boolean muted;

    public MainMenuState(GameStateManager gsm) {
        super(gsm);
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        this.touchPos = new Vector3();
        this.batch = new SpriteBatch();
        this.background = new Animator("animated/background_strip6.png", 6, 1, 640, 640, true);
        this.player = new Animator("animated/ChiefFace_strip16.png", 16, 1, 256, 256, true);
        this.zombie = new Animator("animated/DeadFace_strip10.png", 10, 1, 256, 256, true);
        this.versus = new Texture("vs.png");
        this.title = new Texture("hotlinezombies.png");
        this.play = new Texture("play.png");
        this.quit = new Texture("quit.png");
        this.mute = new Texture("mute.png");
        this.unmute = new Texture("unmute.png");
        this.muted = false;
        this.background.update();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 640.0f, 640.0f);
        this.music = Gdx.audio.newMusic(Gdx.files.internal("sounds/mainmenumusic.mp3"));
        this.music.setLooping(true);
        this.music.play();
        this.music.setVolume(0.3f);
    }

    @Override
    public void update(float delta) {
        this.background.update();
        this.player.update();
        this.zombie.update();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(16384);
        this.camera.update();
        this.batch.setProjectionMatrix(this.camera.combined);
        this.update(Gdx.graphics.getDeltaTime());
        this.batch.begin();
        this.background.animate(this.batch, 0.0f, 0.0f, 0.0f);
        this.batch.draw(this.title, 50.0f, 450.0f, 550.0f, 150.0f);
        this.batch.draw(this.versus, 280.0f, 300.0f, 100.0f, 100.0f);
        this.player.animate(this.batch, 0.0f, 200.0f, 0.0f);
        this.zombie.animate(this.batch, 400.0f, 200.0f, 0.0f);
        this.batch.draw(this.play, 260.0f, 180.0f, 150.0f, 70.0f);
        this.batch.draw(this.quit, 260.0f, 90.0f, 150.0f, 70.0f);
        if (!this.muted) {
            this.batch.draw(this.unmute, 530.0f, 30.0f, 100.0f, 100.0f);
        } else {
            this.batch.draw(this.mute, 530.0f, 30.0f, 100.0f, 100.0f);
        }
        this.batch.end();
        if (Gdx.input.justTouched()) {
            this.touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0.0f);
            this.camera.unproject(this.touchPos);
            if (this.touchPos.y >= 180.0f && this.touchPos.y <= 260.0f && this.touchPos.x >= 260.0f && this.touchPos.x <= 410.0f) {
                this.gsm.setState(GameStateManager.State.PLAY);
                this.music.stop();
            }
            if (this.touchPos.y >= 90.0f && this.touchPos.y <= 170.0f && this.touchPos.x >= 260.0f && this.touchPos.x <= 410.0f) {
                Gdx.app.exit();
            }
            if (this.touchPos.y >= 30.0f && this.touchPos.y <= 130.0f && this.touchPos.x >= 530.0f && this.touchPos.x <= 630.0f) {
                if (this.music.getVolume() == 0.3f) {
                    this.music.setVolume(0.0f);
                    this.muted = true;
                } else {
                    this.music.setVolume(0.3f);
                    this.muted = false;
                }
            }
        }
    }

    @Override
    public void dispose() {
        this.title.dispose();
        this.play.dispose();
        this.quit.dispose();
        this.unmute.dispose();
        this.mute.dispose();
        this.music.dispose();
        this.batch.dispose();
    }
}

