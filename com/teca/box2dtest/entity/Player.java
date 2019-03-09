/*
 * Decompiled with CFR 0_122.
 */
package com.teca.box2dtest.entity;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.teca.box2dtest.ai.SteeringBallEntity;
import com.teca.box2dtest.entity.Gun;
import com.teca.box2dtest.managers.Ammo;
import com.teca.box2dtest.utils.Animator;
import com.teca.box2dtest.utils.BodyBuilder;
import java.io.PrintStream;
import java.util.ArrayList;

public class Player {
    private SteeringBallEntity player;
    int boundingRadius;
    Vector2 position;
    private Sound zombieAtk;
    private Sound gunShoot;
    private Gun aGun;
    private ArrayList<Gun> guns;
    private int keyI;
    private Music machine;
    private Music footStep;
    private Texture heart;
    private Texture speedPerk;
    Vector3 touchPos;
    private float life;
    private boolean dead;
    private Body body;
    private int points;
    private BitmapFont interactStatus;
    private BitmapFont playerPoints;
    private BitmapFont displayAmmo;
    private String strStatus;
    private String strPoints;
    private Ammo ammo;
    private OrthographicCamera camera;
    private float angle;
    private int click;
    private Animator deadPlayer;
    private World world;
    private boolean speed;

    public Player(World world, Vector2 position, int radius, OrthographicCamera camera) {
        this.world = world;
        this.speedPerk = new Texture("speedcola.png");
        this.speed = false;
        this.deadPlayer = new Animator("animated/deadPlayer_strip4.png", 4, 1, 100, 80, false);
        this.interactStatus = new BitmapFont(Gdx.files.internal("fonts/Forte2.fnt"));
        this.playerPoints = new BitmapFont(Gdx.files.internal("fonts/Forte.fnt"));
        this.displayAmmo = new BitmapFont(Gdx.files.internal("fonts/Forte.fnt"));
        Color black = new Color(0.0f, 0.0f, 0.2f, 1.0f);
        this.interactStatus.setColor(black);
        this.boundingRadius = radius;
        this.body = BodyBuilder.createCircle(world, position, radius, false);
        CircleShape circle = new CircleShape();
        circle.setRadius(0.4f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.01f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0.0f;
        this.body.createFixture(fixtureDef).setUserData(this);
        this.player = new SteeringBallEntity(this.body, 10.0f);
        this.points = 500;
        this.strPoints = Integer.toString(this.points);
        this.life = 3.0f;
        this.heart = new Texture("heart.png");
        this.strStatus = "dsa";
        this.ammo = new Ammo(100, 100, world);
        this.camera = camera;
        this.touchPos = new Vector3();
        this.guns = new ArrayList();
        this.guns.add(new Gun("animated/player_pistol_movement_strip8.png", 1, 200, 1, false));
        this.guns.add(new Gun("animated/player_m16_movement_strip8.png", 0, 200, 1, true));
        this.guns.add(new Gun("animated/player_shootgun_movement.png", 2, 200, 1, false));
        this.guns.get(0).setGunAnimation("animated/player_pistol_movement_strip8.png", 8, 1, 70, 50);
        this.guns.get(1).setGunAnimation("animated/player_m16_movement_strip8.png", 8, 1, 70, 50);
        this.guns.get(2).setGunAnimation("animated/player_shootgun_movement.png", 8, 1, 70, 50);
        this.aGun = this.guns.get(0);
        this.footStep = Gdx.audio.newMusic(Gdx.files.internal("sounds/footStepWood.wav"));
        this.zombieAtk = Gdx.audio.newSound(Gdx.files.internal("sounds/zombie.wav"));
        this.gunShoot = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.mp3"));
        this.machine = Gdx.audio.newMusic(Gdx.files.internal("sounds/coinDrink.wav"));
    }

    public float getLife() {
        return this.life;
    }

    public void takeHit(boolean hit) {
        if (hit && this.life > 0.0f) {
            this.life -= 1.0f;
            this.zombieAtk.play();
            if (this.life == 0.0f) {
                this.dead = true;
            }
        } else {
            return;
        }
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public void update(float delta) {
        this.position = this.body.getPosition();
        this.playerInteract(false);
        this.playerInputUpdade();
        this.ammo.update();
    }

    private void playerInputUpdade() {
        int horizontalForce = 0;
        int verticalForce = 0;
        this.touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0.0f);
        this.camera.unproject(this.touchPos);
        this.angle = (float)Math.atan2(this.touchPos.y / 32.0f - this.position.y, this.touchPos.x / 32.0f - this.position.x);
        this.angle = (float)((double)this.angle * 57.29577951308232);
        if (this.angle < 0.0f) {
            this.angle = 360.0f - (- this.angle);
        }
        if (Gdx.input.isKeyJustPressed(131)) {
            Gdx.app.exit();
        }
        if (!this.dead) {
            if (Gdx.input.isKeyJustPressed(45)) {
                if (this.keyI == 2) {
                    if (this.aGun == this.guns.get(0)) {
                        this.aGun = this.guns.get(1);
                    } else if (this.aGun == this.guns.get(1)) {
                        this.aGun = this.guns.get(2);
                    } else if (this.aGun == this.guns.get(2)) {
                        this.aGun = this.guns.get(0);
                    }
                    this.keyI = 0;
                }
                ++this.keyI;
            }
            if (Gdx.input.isTouched() && this.aGun.isAutomatic()) {
                ++this.click;
                if (this.click == 8 * this.aGun.getFireRate()) {
                    this.click = 0;
                    if (this.aGun.getAmmo() > 0) {
                        this.touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0.0f);
                        this.camera.unproject(this.touchPos);
                        this.ammo.shoot(this.position, new Vector2(this.touchPos.x / 32.0f, this.touchPos.y / 32.0f), 1.9f);
                        this.aGun.shoot();
                        this.gunShoot.play();
                    }
                }
            } else if (Gdx.input.justTouched() && this.aGun.getAmmo() > 0) {
                this.touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0.0f);
                this.camera.unproject(this.touchPos);
                this.ammo.shoot(this.position, new Vector2(this.touchPos.x / 32.0f, this.touchPos.y / 32.0f), 1.9f);
                this.aGun.shoot();
                this.gunShoot.play();
            }
            if (Gdx.input.isKeyPressed(29) || Gdx.input.isKeyPressed(32) || Gdx.input.isKeyPressed(51) || Gdx.input.isKeyPressed(47)) {
                this.aGun.gunAUpdate();
                this.footStep.play();
            }
            if (!this.footStep.isPlaying()) {
                this.footStep.pause();
            }
            if (Gdx.input.isKeyPressed(29)) {
                --horizontalForce;
            }
            if (Gdx.input.isKeyPressed(32)) {
                ++horizontalForce;
            }
            if (Gdx.input.isKeyPressed(51)) {
                ++verticalForce;
            }
            if (Gdx.input.isKeyPressed(47)) {
                --verticalForce;
            }
            if (Gdx.input.isKeyJustPressed(33) && Gdx.input.isKeyJustPressed(33)) {
                System.out.println("E");
                this.playerInteract(true);
            }
        }
        if (this.speed) {
            this.player.getBody().setLinearVelocity(horizontalForce * 8, verticalForce * 8);
        } else {
            this.player.getBody().setLinearVelocity(horizontalForce * 5, verticalForce * 5);
        }
    }

    public Ammo draw(SpriteBatch batch) {
        if (this.life > 0.0f) {
            this.aGun.gunAnimate(batch, Gdx.graphics.getWidth() / 2 - 20, Gdx.graphics.getHeight() / 2 - 20, this.angle);
        }
        if (this.speed) {
            batch.draw(this.speedPerk, (float)(Gdx.graphics.getWidth() - 100), (float)(Gdx.graphics.getHeight() - 64), 64.0f, 64.0f);
        }
        this.interactStatus.draw((Batch)batch, this.strStatus, 1000.0f, 500.0f);
        this.displayAmmo.draw((Batch)batch, this.aGun.displayAmmo(), (float)(Gdx.graphics.getWidth() - 64), 50.0f);
        this.playerPoints.draw((Batch)batch, this.strPoints, 200.0f, 200.0f);
        int i = 0;
        while (i < 5) {
            if ((float)i < this.life) {
                batch.draw(this.heart, (float)(64 * i), (float)(Gdx.graphics.getHeight() - 64), 64.0f, 64.0f);
            }
            ++i;
        }
        if (this.life == 0.0f) {
            this.deadPlayer.animate(batch, Gdx.graphics.getWidth() / 2 - 40, Gdx.graphics.getHeight() / 2 - 40, 0.0f);
        }
        return this.ammo;
    }

    private void playerInteract(Boolean interact) {
        if (this.position.x >= 8.0f && this.position.x <= 12.0f) {
            if (this.position.y >= 8.0f && (double)this.position.y <= 10.7) {
                this.strStatus = "To get one life point, drink it! - 5000";
                if (interact.booleanValue()) {
                    this.buyItem(2500, 1);
                }
            }
        } else if (this.position.x >= 6.0f && this.position.x <= 9.0f) {
            if (this.position.y >= 22.0f && this.position.y <= 27.0f) {
                this.strStatus = "Shotgun - 2000";
                if (interact.booleanValue()) {
                    this.buyItem(1000, 2);
                }
            }
        } else if (this.position.x >= 27.0f && this.position.x <= 30.0f) {
            if (this.position.y >= 15.0f && this.position.y <= 19.0f) {
                this.strStatus = "To speed up, drink it! - 4000";
                if (interact.booleanValue()) {
                    this.buyItem(2000, 3);
                }
            }
            if (this.position.y >= 4.0f && this.position.y <= 8.0f) {
                this.strStatus = "Pistol - 2000";
                if (interact.booleanValue()) {
                    this.buyItem(1000, 5);
                }
            }
        } else if (this.position.x >= 19.0f && this.position.x <= 24.0f) {
            if (this.position.y >= 28.0f && this.position.y <= 30.0f) {
                this.strStatus = "Rifle - 2000";
                if (interact.booleanValue()) {
                    this.buyItem(1000, 4);
                }
            }
        } else {
            this.strStatus = "";
        }
    }

    private void buyItem(int cost, int itemId) {
        if (cost > this.points) {
            this.strStatus = "You don't have enought money";
        } else {
            if (itemId == 1) {
                this.life += 0.5f;
                this.points -= cost;
                this.strPoints = Integer.toString(this.points);
                this.machine.play();
            }
            if (itemId == 2) {
                this.guns.get(2).addAmmo(100);
                this.points -= cost;
                this.strPoints = Integer.toString(this.points);
            }
            if (itemId == 3) {
                this.machine.play();
                this.points -= cost;
                this.speed = true;
                this.strPoints = Integer.toString(this.points);
            }
            if (itemId == 4) {
                this.guns.get(1).addAmmo(100);
                this.points -= cost;
                this.strPoints = Integer.toString(this.points);
            }
            if (itemId == 5) {
                this.guns.get(0).addAmmo(100);
                this.points -= cost;
                this.strPoints = Integer.toString(this.points);
            }
        }
    }

    public Body getBody() {
        return this.body;
    }

    public void isDead(boolean dead) {
        this.dead = dead;
        this.dispose();
    }

    private void dispose() {
        this.dispose();
    }

    public SteeringBallEntity getSteeringEntity() {
        return this.player;
    }

    public void addPoints(int points) {
        this.points += points;
        this.strPoints = Integer.toString(this.points);
    }
}

