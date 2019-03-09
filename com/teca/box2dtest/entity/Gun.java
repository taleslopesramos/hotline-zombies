/*
 * Decompiled with CFR 0_122.
 */
package com.teca.box2dtest.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teca.box2dtest.utils.Animator;

class Gun {
    private int gunId;
    private int ammoCount;
    private int fireRate;
    private boolean automatic;
    private String FILE;
    private Animator gunAnimation;

    public Gun(String FileGun, int gunId, int ammo, int fireRate, boolean isAutomatic) {
        this.FILE = FileGun;
        this.gunId = gunId;
        this.ammoCount = ammo;
        this.automatic = isAutomatic;
        this.fireRate = fireRate;
    }

    public void setGunAnimation(String FileGun, int frame_c, int frame_r, int width, int height) {
        this.gunAnimation = new Animator(FileGun, frame_c, frame_r, width, height, true);
    }

    public void gunAnimate(SpriteBatch batch, float x, float y, float rotate) {
        this.gunAnimation.animate(batch, x, y, rotate);
    }

    public void gunAUpdate() {
        this.gunAnimation.update();
    }

    public void shoot() {
        --this.ammoCount;
    }

    public int getAmmo() {
        return this.ammoCount;
    }

    public int getFireRate() {
        return this.fireRate;
    }

    public boolean isAutomatic() {
        return this.automatic;
    }

    public void addAmmo(int ammo) {
        this.ammoCount += ammo;
    }

    public String displayAmmo() {
        return Integer.toString(this.ammoCount);
    }
}

