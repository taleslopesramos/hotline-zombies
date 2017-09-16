/*
 * Decompiled with CFR 0_122.
 */
package com.teca.box2dtest.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class Bullet {
    private Vector2 bulletDestiny;
    private Vector2 bulletLocation;
    private Vector2 bulletGuidance;
    private float bulletVelocity;
    public Body bullet;
    private boolean destroyed;
    World world;
    final short CATEGORY_BULLET = 3;
    Texture img;

    public Bullet(Vector2 bulletSentLocation, Vector2 bulletDestiny, float bulletVelocity, World world) {
        this.world = world;
        this.destroyed = false;
        BodyDef bulletDef = new BodyDef();
        bulletDef.type = BodyDef.BodyType.DynamicBody;
        this.bulletDestiny = new Vector2(bulletDestiny);
        this.bulletLocation = new Vector2(bulletSentLocation);
        this.bulletGuidance = new Vector2();
        this.bulletVelocity = bulletVelocity;
        this.setGuidance();
        bulletDef.position.set(bulletSentLocation.x + this.bulletGuidance.x * 1.0f, bulletSentLocation.y + this.bulletGuidance.y * 1.0f);
        this.bullet = world.createBody(bulletDef);
        CircleShape circle = new CircleShape();
        circle.setRadius(0.1f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0.0f;
        this.bullet.createFixture(fixtureDef).setUserData(this);
        this.bullet.setBullet(true);
        circle.dispose();
        this.img = new Texture("bullet.png");
    }

    public void draw(SpriteBatch batch) {
        batch.draw(this.img, this.getBulletLocation().x * 32.0f, this.getBulletLocation().y * 32.0f, 3.0f, 3.0f);
    }

    private void setGuidance() {
        this.bulletGuidance.x = this.bulletDestiny.x - this.bulletLocation.x;
        this.bulletGuidance.y = this.bulletDestiny.y - this.bulletLocation.y;
        float mag = (float)Math.hypot(this.bulletGuidance.x, this.bulletGuidance.y);
        this.bulletGuidance.x /= mag;
        this.bulletGuidance.y /= mag;
    }

    public void update() {
        this.bullet.setLinearVelocity(this.bulletGuidance.x * this.bulletVelocity * 10.0f, this.bulletGuidance.y * this.bulletVelocity * 10.0f);
        this.bulletLocation.x = this.bullet.getPosition().x;
        this.bulletLocation.y = this.bullet.getPosition().y;
    }

    public void destroy() {
        this.destroyed = true;
    }

    public boolean isDestroyed() {
        return this.destroyed;
    }

    public Vector2 getBulletDestiny() {
        return this.bulletDestiny;
    }

    public void setBulletDestiny(Vector2 bulletDestiny) {
        this.bulletDestiny = bulletDestiny;
    }

    public float getBulletVelocity() {
        return this.bulletVelocity;
    }

    public void setBulletVelocity(float bulletVelocity) {
        this.bulletVelocity = bulletVelocity;
    }

    public Vector2 getBulletLocation() {
        return this.bulletLocation;
    }

    public void setBulletLocation(Vector2 bulletLocation) {
        this.bulletLocation = bulletLocation;
    }
}

