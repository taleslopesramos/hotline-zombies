/*
 * Decompiled with CFR 0_122.
 */
package com.teca.box2dtest.utils;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animator {
    private int FRAME_COLS;
    private int FRAME_ROWS;
    Animation walkAnimation;
    Texture walkSheet;
    TextureRegion[] walkFrames;
    TextureRegion currentFrame;
    float stateTime;
    private int width;
    private int height;
    private boolean looping;

    public Animator(String file, int frame_c, int frame_r, int width, int height, boolean looping) {
        this.looping = looping;
        this.FRAME_COLS = frame_c;
        this.FRAME_ROWS = frame_r;
        this.width = width;
        this.height = height;
        this.walkSheet = new Texture(Gdx.files.internal(file));
        TextureRegion[][] tmp = TextureRegion.split(this.walkSheet, this.walkSheet.getWidth() / this.FRAME_COLS, this.walkSheet.getHeight() / this.FRAME_ROWS);
        this.walkFrames = new TextureRegion[this.FRAME_COLS * this.FRAME_ROWS];
        int index = 0;
        int i = 0;
        while (i < this.FRAME_ROWS) {
            int j = 0;
            while (j < this.FRAME_COLS) {
                this.walkFrames[index++] = tmp[i][j];
                ++j;
            }
            ++i;
        }
        this.walkAnimation = new Animation(0.025f, this.walkFrames);
        this.stateTime = 0.0f;
        this.update();
    }

    public void setAnimator(String file, int frame_c, int frame_r, int width, int height) {
        this.FRAME_COLS = frame_c;
        this.FRAME_ROWS = frame_r;
        this.width = width;
        this.height = height;
        this.walkSheet = new Texture(Gdx.files.internal(file));
        TextureRegion[][] tmp = TextureRegion.split(this.walkSheet, this.walkSheet.getWidth() / this.FRAME_COLS, this.walkSheet.getHeight() / this.FRAME_ROWS);
        this.walkFrames = new TextureRegion[this.FRAME_COLS * this.FRAME_ROWS];
        int index = 0;
        int i = 0;
        while (i < this.FRAME_ROWS) {
            int j = 0;
            while (j < this.FRAME_COLS) {
                this.walkFrames[index++] = tmp[i][j];
                ++j;
            }
            ++i;
        }
        this.walkAnimation = new Animation(0.025f, this.walkFrames);
        this.stateTime = 0.0f;
        this.update();
    }

    public void update() {
        this.stateTime += Gdx.graphics.getDeltaTime() / 12.0f;
        this.currentFrame = this.walkAnimation.getKeyFrame(this.stateTime, this.looping);
    }

    public void animate(SpriteBatch batch, float x, float y, float rotate) {
        batch.draw(this.currentFrame, x, y, this.width / 2, this.height / 2, this.width, this.height, 1.0f, 1.0f, rotate);
    }
}

