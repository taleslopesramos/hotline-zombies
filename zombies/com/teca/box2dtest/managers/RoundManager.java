/*
 * Decompiled with CFR 0_122.
 */
package com.teca.box2dtest.managers;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.teca.box2dtest.entity.Player;
import com.teca.box2dtest.entity.Zombie;
import com.teca.box2dtest.managers.Room;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class RoundManager {
    private int actualRound;
    private int roundZombies;
    private int maxZombies;
    private int roundDeathCount;
    private int deathCount;
    private int zombiesCount;
    private ArrayList<Zombie> zombies;
    private World world;
    private Player player;
    float acc = 0.0f;
    private Boolean spawn;
    private ArrayList<Room> rooms;
    private Vector2[] roomSpawn;
    private BitmapFont roundFont;
    private String strRound;

    public RoundManager(World world, Player player) {
        this.world = world;
        this.player = player;
        this.roundFont = new BitmapFont(Gdx.files.internal("fonts/Forte.fnt"));
        this.zombies = new ArrayList();
        this.roomSpawn = new Vector2[6];
        this.roomSpawn[0] = new Vector2(17.0f, 0.5f);
        this.roomSpawn[1] = new Vector2(6.0f, 29.5f);
        this.roomSpawn[2] = new Vector2(20.0f, 0.5f);
        this.roomSpawn[3] = new Vector2(18.0f, 29.5f);
        this.roomSpawn[4] = new Vector2(0.5f, 18.0f);
        this.roomSpawn[5] = new Vector2(29.5f, 24.0f);
        this.actualRound = 1;
        this.deathCount = 0;
        this.roundDeathCount = 0;
        this.zombiesCount = 0;
        this.spawn = true;
    }

    public void update(float delta) {
        this.acc += delta;
        if (this.acc >= 1.0f) {
            this.spawnZombies();
            this.acc = 0.0f;
        }
        int i = 0;
        while (i < this.zombies.size()) {
            this.zombies.get(i).update(delta);
            ++i;
        }
        this.killZombie();
        this.roundManaging();
    }

    public void spawnZombies() {
        if (!this.spawn.booleanValue()) {
            return;
        }
        Random geradorRoom = new Random();
        int room = geradorRoom.nextInt() % 6;
        int i = 0;
        while (i < 6) {
            if (room == i) {
                if (this.zombies.size() < 12 && this.zombiesCount < this.roundZombies) {
                    this.zombies.add(new Zombie(this.world, this.player, this.roomSpawn[i], 10));
                }
                ++this.zombiesCount;
            }
            ++i;
        }
    }

    public void draw(SpriteBatch batch) {
        this.roundFont.draw((Batch)batch, this.strRound, 10.0f, 50.0f);
        for (Zombie zombie : this.zombies) {
            if (!zombie.getDead()) {
                zombie.draw(batch, true);
            }
            if (!zombie.getDead()) continue;
            zombie.draw(batch, false);
        }
    }

    public void killZombie() {
        Iterator<Zombie> iterator = this.zombies.iterator();
        while (iterator.hasNext()) {
            Zombie zombie2 = iterator.next();
            if (!zombie2.getDead()) continue;
            iterator.remove();
            this.world.destroyBody(zombie2.getBody());
            ++this.deathCount;
            ++this.roundDeathCount;
            this.player.addPoints(100);
        }
    }

    public BitmapFont getFont() {
        return this.roundFont;
    }

    public String getRoundStr() {
        return this.strRound;
    }

    public void roundManaging() {
        this.killZombie();
        this.roundDisplaying();
        this.roundZombies = this.actualRound * 6;
        if (this.roundDeathCount == this.roundZombies) {
            this.roundTurning();
        }
    }

    public void roundTurning() {
        ++this.actualRound;
        this.spawn = false;
        this.zombiesCount = 0;
        this.roundDeathCount = 0;
        Timer.schedule(new Timer.Task(){

            @Override
            public void run() {
                RoundManager.access$0(RoundManager.this, true);
            }
        }, 5.0f);
    }

    public void roundDisplaying() {
        if (this.actualRound == 1) {
            this.strRound = "I";
        }
        if (this.actualRound == 2) {
            this.strRound = "II";
        }
        if (this.actualRound == 3) {
            this.strRound = "III";
        }
        if (this.actualRound == 4) {
            this.strRound = "IV";
        }
        if (this.actualRound == 5) {
            this.strRound = "V";
        }
        if (this.actualRound == 6) {
            this.strRound = "VI";
        }
        if (this.actualRound == 7) {
            this.strRound = "VII";
        }
        if (this.actualRound == 8) {
            this.strRound = "VIII";
        }
        if (this.actualRound == 9) {
            this.strRound = "IX";
        }
        if (this.actualRound > 9) {
            this.strRound = Integer.toString(this.actualRound);
        }
    }

    static /* synthetic */ void access$0(RoundManager roundManager, Boolean bl) {
        roundManager.spawn = bl;
    }

}

