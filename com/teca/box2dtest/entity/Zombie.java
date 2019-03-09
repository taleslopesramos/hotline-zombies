/*
 * Decompiled with CFR 0_122.
 */
package com.teca.box2dtest.entity;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.utils.Path;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.teca.box2dtest.ai.SteeringBallEntity;
import com.teca.box2dtest.entity.Player;
import com.teca.box2dtest.utils.Animator;
import com.teca.box2dtest.utils.BodyBuilder;
import java.io.PrintStream;

public class Zombie {
    private SteeringBallEntity zombie;
    int boundingRadius;
    public Vector2 position;
    Player player;
    private Array<Vector2> roomOut;
    Array<Vector2> waypoints;
    private boolean dead;
    private Body body;
    private Arrive<Vector2> arrive;
    private FollowPath<Vector2, LinePath.LinePathParam> follow;
    boolean hit = true;
    private Animator animacao;
    private Animator deadZombie;
    private World world;

    public Zombie(World world, Player player, Vector2 position, int boundingRadius) {
        this.player = player;
        this.world = world;
        this.body = BodyBuilder.createCircle(world, position, boundingRadius, false);
        CircleShape circle = new CircleShape();
        circle.setRadius(0.3f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.01f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0.0f;
        this.body.createFixture(fixtureDef).setUserData(this);
        this.zombie = new SteeringBallEntity(this.body, 10.0f);
        this.dead = false;
        this.arrive = new Arrive(this.zombie, this.player.getSteeringEntity()).setArrivalTolerance(0.1f).setDecelerationRadius(5.0f);
        this.roomOut = new Array();
        this.waypoints = new Array();
        this.roomOut.add(new Vector2(19.0f, 9.0f));
        this.roomOut.add(new Vector2(20.0f, 9.0f));
        this.roomOut.add(new Vector2(17.0f, 9.0f));
        this.roomOut.add(new Vector2(18.0f, 9.0f));
        this.roomOut.add(new Vector2(6.0f, 21.0f));
        this.roomOut.add(new Vector2(7.0f, 21.0f));
        this.animacao = new Animator("animated/sprZumbieWalk.png", 8, 1, 57, 45, true);
        this.deadZombie = new Animator("animated/deadZombie_strip4.png", 4, 1, 120, 90, false);
    }

    public void update(float delta) {
        this.position = this.body.getPosition();
        this.zombie.update(delta);
        this.zombieHit();
        this.zombiePosition();
        this.animacao.update();
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public Body getBody() {
        return this.body;
    }

    public void zombiePosition() {
        if (!this.dead) {
            LinePath<Vector2> linepath;
            if (this.testPosition(this.getBody(), new Vector2(18.0f, 30.0f), new Vector2(0.0f, 9.0f)) && !this.testPosition(this.player.getBody(), new Vector2(18.0f, 30.0f), new Vector2(0.0f, 9.0f))) {
                this.waypoints.clear();
                this.waypoints.add(this.roomOut.get(0));
                this.waypoints.add(this.roomOut.get(1));
                linepath = new LinePath<Vector2>(this.waypoints);
                this.follow = new FollowPath(this.zombie, linepath);
                this.zombie.setBehavior(this.follow);
            } else {
                this.zombie.setBehavior(this.arrive);
            }
            if (this.testPosition(this.getBody(), new Vector2(0.0f, 18.0f), new Vector2(0.0f, 11.0f)) && !this.testPosition(this.player.getBody(), new Vector2(0.0f, 18.0f), new Vector2(0.0f, 11.0f))) {
                this.waypoints.clear();
                this.waypoints.add(this.roomOut.get(2));
                this.waypoints.add(this.roomOut.get(3));
                linepath = new LinePath<Vector2>(this.waypoints);
                this.follow = new FollowPath(this.zombie, linepath);
                this.zombie.setBehavior(this.follow);
            } else {
                this.zombie.setBehavior(this.arrive);
            }
            if (this.testPosition(this.getBody(), new Vector2(0.0f, 8.0f), new Vector2(21.0f, 30.0f)) && !this.testPosition(this.player.getBody(), new Vector2(0.0f, 8.0f), new Vector2(21.0f, 30.0f))) {
                this.waypoints.clear();
                this.waypoints.add(this.roomOut.get(4));
                this.waypoints.add(this.roomOut.get(5));
                linepath = new LinePath<Vector2>(this.waypoints);
                this.follow = new FollowPath(this.zombie, linepath);
                this.zombie.setBehavior(this.follow);
            } else {
                this.zombie.setBehavior(this.arrive);
            }
        } else {
            this.zombie.setBehavior(null);
        }
    }

    public void zombieHit() {
        if (this.checkNearZombie(1.0f) && this.hit) {
            Timer.schedule(new Timer.Task(){

                @Override
                public void run() {
                    if (Zombie.this.checkNearZombie(1.0f)) {
                        Zombie.this.player.takeHit(Zombie.this.hit);
                    }
                    Zombie.this.hit = false;
                    Timer.schedule(new Timer.Task(){

                        @Override
                        public void run() {
                            .access$0(()1.this).hit = true;
                        }
                    }, 0.3f);
                }

                static /* synthetic */ Zombie access$0( var0) {
                    return var0.Zombie.this;
                }

            }, 0.3f);
        }
    }

    public boolean checkNearZombie(float distance) {
        if (this.position.x >= this.player.getPosition().x - distance && this.position.x <= this.player.getPosition().x + distance && this.position.y >= this.player.getPosition().y - distance && this.position.y <= this.player.getPosition().y + distance) {
            return true;
        }
        return false;
    }

    public boolean testPosition(Body body, Vector2 xAxys, Vector2 yAxys) {
        if (body.getPosition().x >= xAxys.x && body.getPosition().x <= xAxys.y) {
            if (body.getPosition().y >= yAxys.y && body.getPosition().y <= yAxys.y) {
                return true;
            }
            return false;
        }
        return false;
    }

    public void isDead(boolean dead) {
        this.dead = dead;
        System.out.println("ESTOU MORTOOOOOOOOOOOOO KRAAAAAAIIIIIIIIIIII");
    }

    public boolean getDead() {
        return this.dead;
    }

    public void dispose() {
        this.dispose();
    }

    public void draw(SpriteBatch batch, boolean alive) {
        System.out.println(String.valueOf(this.position.x * 480.0f / 15.0f) + " " + this.position.y * 480.0f / 15.0f);
        if (alive) {
            this.animacao.animate(batch, this.position.x * 32.0f - 20.0f, this.position.y * 32.0f - 20.0f, (float)((double)(this.zombie.getOrientation() * 180.0f) / 3.141592653589793 + 90.0));
        }
        if (!alive) {
            this.deadZombie.animate(batch, this.position.x * 32.0f - 15.0f, this.position.y * 32.0f - 15.0f, (float)((double)(this.zombie.getOrientation() * 180.0f) / 3.141592653589793 + 90.0));
        }
    }

}

