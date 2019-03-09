/*
 * Decompiled with CFR 0_122.
 */
package com.teca.box2dtest.handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.teca.box2dtest.entity.Bullet;
import com.teca.box2dtest.entity.Player;
import com.teca.box2dtest.entity.Zombie;
import java.io.PrintStream;

public class WorldListener
implements ContactListener {
    private boolean bulletContactZombie(Fixture f1, Fixture f2) {
        if (f1.getUserData() instanceof Bullet && f2.getUserData() instanceof Zombie) {
            return true;
        }
        if (f2.getUserData() instanceof Bullet && f1.getUserData() instanceof Zombie) {
            return true;
        }
        return false;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture f1 = contact.getFixtureA();
        Fixture f2 = contact.getFixtureB();
        if (f1 == null || f2 == null) {
            return;
        }
        if (f1.getUserData() == null || f2.getUserData() == null) {
            return;
        }
        if (f1.getUserData() instanceof Bullet) {
            System.out.println(f1.getUserData());
            ((Bullet)f1.getUserData()).destroy();
        }
        if (f2.getUserData() instanceof Bullet) {
            System.out.println(f2.getUserData());
            ((Bullet)f2.getUserData()).destroy();
        }
        System.out.println("contato de " + f1.getUserData().getClass() + " com " + f2.getUserData().getClass());
        if (this.bulletContactZombie(f1, f2)) {
            System.out.println("FINALMENTE");
            if (f1.getUserData() instanceof Zombie) {
                Zombie zombie = (Zombie)f1.getUserData();
                zombie.isDead(true);
                Bullet bullet = (Bullet)f2.getUserData();
                bullet.destroy();
            } else {
                Zombie zombie = (Zombie)f2.getUserData();
                zombie.isDead(true);
                Bullet bullet = (Bullet)f1.getUserData();
                bullet.destroy();
            }
        }
        if (this.zombieContactPlayer(f1, f2)) {
            System.out.println("DEU POURA Zumbie CHEGOU");
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    private boolean zombieContactPlayer(Fixture f1, Fixture f2) {
        if (f1.getUserData() instanceof Zombie && f2.getUserData() instanceof Player) {
            return true;
        }
        if (f2.getUserData() instanceof Player && f1.getUserData() instanceof Zombie) {
            return true;
        }
        return false;
    }
}

