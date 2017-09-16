/*
 * Decompiled with CFR 0_122.
 */
package com.teca.box2dtest.managers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.teca.box2dtest.entity.Bullet;
import java.util.ArrayList;
import java.util.Iterator;

public class Ammo {
    private ArrayList<Bullet> bullet = new ArrayList();
    private int camWidth;
    private int camHeigth;
    private int i;
    private Texture img;
    private World world;

    public Ammo(int camWidth, int camHeigth, World world) {
        this.camWidth = camWidth;
        this.camHeigth = camHeigth;
        this.world = world;
    }

    public void shoot(Vector2 bulletSentLocation, Vector2 bulletDestiny, float bulletVelocity) {
        this.bullet.add(new Bullet(bulletSentLocation, bulletDestiny, bulletVelocity, this.world));
    }

    public void draw(SpriteBatch batch) {
        for (Bullet bullet2 : this.bullet) {
            bullet2.draw(batch);
        }
    }

    public void update() {
        Iterator<Bullet> iterator = this.bullet.iterator();
        while (iterator.hasNext()) {
            Bullet bullet2 = iterator.next();
            if (bullet2.isDestroyed()) {
                this.world.destroyBody(bullet2.bullet);
                iterator.remove();
                continue;
            }
            bullet2.update();
        }
    }
}

