/*
 * Decompiled with CFR 0_122.
 */
package com.teca.box2dtest.ai;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.teca.box2dtest.utils.SteeringUtils;

public class SteeringBallEntity
implements Steerable<Vector2> {
    Body body;
    boolean tagged;
    float boundingRadius;
    float maxLinearSpeed;
    float maxLinearAcceleration;
    float maxAngularSpeed;
    float maxAngularAcceleration;
    SteeringBehavior<Vector2> behavior;
    SteeringAcceleration<Vector2> steeringOutput;

    public SteeringBallEntity(Body body, float boundingRadius) {
        this.body = body;
        this.boundingRadius = boundingRadius;
        this.maxLinearSpeed = 5.0f;
        this.maxLinearAcceleration = 10.0f;
        this.maxAngularSpeed = 30.0f;
        this.maxAngularAcceleration = 5.0f;
        this.tagged = false;
        this.steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());
        this.body.setUserData(this);
    }

    public void update(float delta) {
        if (this.behavior != null) {
            this.behavior.calculateSteering(this.steeringOutput);
            this.applySteering(1.0f);
        }
    }

    public void applySteering(float delta) {
        if (this.behavior instanceof Arrive) {
            boolean anyAcceleration = false;
            if (!((Vector2)this.steeringOutput.linear).isZero()) {
                Vector2 force = ((Vector2)this.steeringOutput.linear).scl(delta);
                this.body.applyForceToCenter(force, true);
                anyAcceleration = true;
            }
            if (this.steeringOutput.angular != 0.0f) {
                this.body.applyTorque(this.steeringOutput.angular * delta, true);
                anyAcceleration = true;
            } else {
                Vector2 linVel = this.getLinearVelocity();
                if (!linVel.isZero()) {
                    float newOrientation = this.vectorToAngle(linVel);
                    this.body.setAngularVelocity((newOrientation - this.getAngularVelocity()) * delta);
                    this.body.setTransform(this.body.getPosition(), newOrientation);
                }
            }
            if (anyAcceleration) {
                Vector2 velocity = this.body.getLinearVelocity();
                float currentSpeedSquare = velocity.len2();
                if (currentSpeedSquare > this.maxLinearSpeed * this.maxLinearSpeed) {
                    this.body.setLinearVelocity(velocity.scl(this.maxLinearSpeed / (float)Math.sqrt(currentSpeedSquare)));
                }
                if (this.body.getAngularVelocity() > this.maxAngularSpeed) {
                    this.body.setAngularVelocity(this.maxAngularSpeed);
                }
            } else if (this.behavior instanceof FollowPath) {
                this.body.getPosition().mulAdd(this.body.getLinearVelocity(), delta);
                this.body.getLinearVelocity().mulAdd((Vector2)this.steeringOutput.linear, delta).limit(this.getMaxLinearSpeed());
            }
        }
    }

    @Override
    public Vector2 getPosition() {
        return this.body.getPosition();
    }

    @Override
    public float getOrientation() {
        return this.body.getAngle();
    }

    @Override
    public void setOrientation(float orientation) {
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return SteeringUtils.vectorToAngle(vector);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return SteeringUtils.angleToVector(outVector, angle);
    }

    @Override
    public Location<Vector2> newLocation() {
        return (Location)((Object)new Vector2());
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return 0.0f;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {
    }

    @Override
    public float getMaxLinearSpeed() {
        return this.maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return this.maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return this.maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return this.maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    @Override
    public Vector2 getLinearVelocity() {
        return this.body.getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        return this.body.getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        return this.boundingRadius;
    }

    @Override
    public boolean isTagged() {
        return this.tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    public Body getBody() {
        return this.body;
    }

    public void setBehavior(SteeringBehavior<Vector2> behavior) {
        this.behavior = behavior;
    }

    public SteeringBehavior<Vector2> getBehavior() {
        return this.behavior;
    }
}

