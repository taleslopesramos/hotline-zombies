/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector;

public class SteerableAdapter<T extends Vector<T>>
implements Steerable<T> {
    @Override
    public float getZeroLinearSpeedThreshold() {
        return 0.001f;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {
    }

    @Override
    public float getMaxLinearSpeed() {
        return 0.0f;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
    }

    @Override
    public float getMaxLinearAcceleration() {
        return 0.0f;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
    }

    @Override
    public float getMaxAngularSpeed() {
        return 0.0f;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
    }

    @Override
    public float getMaxAngularAcceleration() {
        return 0.0f;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
    }

    @Override
    public T getPosition() {
        return null;
    }

    @Override
    public float getOrientation() {
        return 0.0f;
    }

    @Override
    public void setOrientation(float orientation) {
    }

    @Override
    public T getLinearVelocity() {
        return null;
    }

    @Override
    public float getAngularVelocity() {
        return 0.0f;
    }

    @Override
    public float getBoundingRadius() {
        return 0.0f;
    }

    @Override
    public boolean isTagged() {
        return false;
    }

    @Override
    public void setTagged(boolean tagged) {
    }

    @Override
    public Location<T> newLocation() {
        return null;
    }

    @Override
    public float vectorToAngle(T vector) {
        return 0.0f;
    }

    @Override
    public T angleToVector(T outVector, float angle) {
        return null;
    }
}

