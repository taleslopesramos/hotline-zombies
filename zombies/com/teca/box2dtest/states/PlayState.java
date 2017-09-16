/*
 * Decompiled with CFR 0_122.
 */
package com.teca.box2dtest.states;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.teca.box2dtest.entity.Player;
import com.teca.box2dtest.handlers.WorldListener;
import com.teca.box2dtest.managers.Ammo;
import com.teca.box2dtest.managers.GameStateManager;
import com.teca.box2dtest.managers.RoundManager;
import com.teca.box2dtest.states.GameState;
import com.teca.box2dtest.utils.TiledObjectUtil;
import java.io.PrintStream;

public class PlayState
extends GameState {
    private Box2DDebugRenderer b2dr;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private World world;
    private SpriteBatch batch;
    private SpriteBatch batch2;
    private RoundManager round;
    private Ammo ammo;
    private Music playmusic;
    Player player;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        System.out.println("ola");
        this.batch = new SpriteBatch();
        this.batch2 = new SpriteBatch();
        this.map = new TmxMapLoader().load("house.tmx");
        this.renderer = new OrthogonalTiledMapRenderer(this.map);
        this.world = new World(new Vector2(0.0f, 0.0f), false);
        this.world.setContactListener(new WorldListener());
        this.b2dr = new Box2DDebugRenderer();
        this.player = new Player(this.world, new Vector2(15.0f, 15.0f), 10, this.camera);
        TiledObjectUtil.parseTiledObjectLayer(this.world, this.map.getLayers().get("Collision Layer").getObjects());
        this.round = new RoundManager(this.world, this.player);
        this.playmusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/playmusic.mp3"));
        this.playmusic.setLooping(true);
        this.playmusic.setVolume(0.01f);
        this.playmusic.play();
    }

    @Override
    public void update(float delta) {
        this.world.step(0.016666668f, 6, 2);
        this.round.update(delta);
        this.player.update(delta);
        this.cameraUpdate(delta);
        if (this.player.getLife() <= 0.0f) {
            Timer.schedule(new Timer.Task(){

                @Override
                public void run() {
                    PlayState.this.gsm.setState(GameStateManager.State.GAMEOVER);
                    PlayState.this.playmusic.stop();
                }
            }, 3.0f);
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.25f, 0.25f, 0.25f, 1.0f);
        Gdx.gl.glClear(16384);
        this.update(Gdx.graphics.getDeltaTime());
        this.camera.update();
        this.batch2.setProjectionMatrix(this.camera.combined);
        this.renderer.setView(this.camera);
        this.renderer.render();
        this.batch.begin();
        this.ammo = this.player.draw(this.batch);
        this.round.getFont().draw((Batch)this.batch, this.round.getRoundStr(), 10.0f, 50.0f);
        this.batch.end();
        this.batch2.begin();
        this.round.draw(this.batch2);
        this.ammo.draw(this.batch2);
        this.batch2.end();
    }

    @Override
    public void resize(int w, int h) {
        this.camera.setToOrtho(false, w, h);
        this.camera.viewportWidth = 640.0f;
        this.camera.viewportHeight = 640.0f;
        this.camera.update();
    }

    private void cameraUpdate(float deltaTime) {
        Vector3 position = this.camera.position;
        position.x = this.player.getPosition().x * 32.0f;
        position.y = this.player.getPosition().y * 32.0f;
        this.camera.position.set(position);
        this.renderer.setView(this.camera);
        this.camera.update();
    }

    @Override
    public void dispose() {
        this.map.dispose();
        this.batch.dispose();
        this.batch2.dispose();
        this.playmusic.dispose();
        this.world.dispose();
        this.b2dr.dispose();
    }

}

