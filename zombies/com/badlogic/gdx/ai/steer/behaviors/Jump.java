/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.steer.behaviors;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteerableAdapter;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.MatchVelocity;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector;

public class Jump<T extends Vector<T>>
extends MatchVelocity<T> {
    public static boolean DEBUG_ENABLED = false;
    protected JumpDescriptor<T> jumpDescriptor;
    protected T gravity;
    protected GravityComponentHandler<T> gravityComponentHandler;
    protected JumpCallback callback;
    protected float takeoffPositionTolerance;
    protected float takeoffVelocityTolerance;
    protected float maxVerticalVelocity;
    private boolean isJumpAchievable;
    protected float airborneTime = 0.0f;
    private JumpTarget<T> jumpTarget;
    private T planarVelocity;

    public Jump(Steerable<T> owner, JumpDescriptor<T> jumpDescriptor, T gravity, GravityComponentHandler<T> gravityComponentHandler, JumpCallback callback) {
        super(owner);
        this.gravity = gravity;
        this.gravityComponentHandler = gravityComponentHandler;
        this.setJumpDescriptor(jumpDescriptor);
        this.callback = callback;
        this.jumpTarget = new JumpTarget<T>(owner);
        this.planarVelocity = this.newVector(owner);
    }

    @Override
    public SteeringAcceleration<T> calculateRealSteering(SteeringAcceleration<T> steering) {
        if (this.target == null) {
            this.target = this.calculateTarget();
            this.callback.reportAchievability(this.isJumpAchievable);
        }
        if (!this.isJumpAchievable) {
            return steering.setZero();
        }
        if (this.owner.getPosition().epsilonEquals(this.target.getPosition(), this.takeoffPositionTolerance)) {
            if (DEBUG_ENABLED) {
                GdxAI.getLogger().info("Jump", "Good position!!!");
            }
            if (this.owner.getLinearVelocity().epsilonEquals(this.target.getLinearVelocity(), this.takeoffVelocityTolerance)) {
                if (DEBUG_ENABLED) {
                    GdxAI.getLogger().info("Jump", "Good Velocity!!!");
                }
                this.isJumpAchievable = false;
                this.callback.takeoff(this.maxVerticalVelocity, this.airborneTime);
                return steering.setZero();
            }
            if (DEBUG_ENABLED) {
                GdxAI.getLogger().info("Jump", "Bad Velocity: Speed diff. = " + this.planarVelocity.set(this.target.getLinearVelocity()).sub(this.owner.getLinearVelocity()).len() + ", diff = (" + this.planarVelocity + ")");
            }
        }
        return super.calculateRealSteering(steering);
    }

    private Steerable<T> calculateTarget() {
        this.jumpTarget.position = this.jumpDescriptor.takeoffPosition;
        this.airborneTime = this.calculateAirborneTimeAndVelocity(this.jumpTarget.linearVelocity, this.jumpDescriptor, this.getActualLimiter().getMaxLinearSpeed());
        this.isJumpAchievable = this.airborneTime >= 0.0f;
        return this.jumpTarget;
    }

    public float calculateAirborneTimeAndVelocity(T outVelocity, JumpDescriptor<T> jumpDescriptor, float maxLinearSpeed) {
        float g = this.gravityComponentHandler.getComponent(this.gravity);
        float sqrtTerm = (float)Math.sqrt(2.0f * g * this.gravityComponentHandler.getComponent(jumpDescriptor.delta) + this.maxVerticalVelocity * this.maxVerticalVelocity);
        float time = (- this.maxVerticalVelocity + sqrtTerm) / g;
        if (DEBUG_ENABLED) {
            GdxAI.getLogger().info("Jump", "1st jump time = " + time);
        }
        if (!this.checkAirborneTimeAndCalculateVelocity(outVelocity, time, jumpDescriptor, maxLinearSpeed)) {
            time = (- this.maxVerticalVelocity - sqrtTerm) / g;
            if (DEBUG_ENABLED) {
                GdxAI.getLogger().info("Jump", "2nd jump time = " + time);
            }
            if (!this.checkAirborneTimeAndCalculateVelocity(outVelocity, time, jumpDescriptor, maxLinearSpeed)) {
                return -1.0f;
            }
        }
        return time;
    }

    private boolean checkAirborneTimeAndCalculateVelocity(T outVelocity, float time, JumpDescriptor<T> jumpDescriptor, float maxLinearSpeed) {
        this.planarVelocity.set(jumpDescriptor.delta).scl((float)(1.0f / time));
        this.gravityComponentHandler.setComponent(this.planarVelocity, 0.0f);
        if (this.planarVelocity.len2() < maxLinearSpeed * maxLinearSpeed) {
            float verticalValue = this.gravityComponentHandler.getComponent(outVelocity);
            this.gravityComponentHandler.setComponent(outVelocity.set(this.planarVelocity), verticalValue);
            if (DEBUG_ENABLED) {
                GdxAI.getLogger().info("Jump", "targetLinearVelocity = " + outVelocity + "; targetLinearSpeed = " + outVelocity.len());
            }
            return true;
        }
        return false;
    }

    public JumpDescriptor<T> getJumpDescriptor() {
        return this.jumpDescriptor;
    }

    public Jump<T> setJumpDescriptor(JumpDescriptor<T> jumpDescriptor) {
        this.jumpDescriptor = jumpDescriptor;
        this.target = null;
        this.isJumpAchievable = false;
        return this;
    }

    public T getGravity() {
        return this.gravity;
    }

    public Jump<T> setGravity(T gravity) {
        this.gravity = gravity;
        return this;
    }

    public float getMaxVerticalVelocity() {
        return this.maxVerticalVelocity;
    }

    public Jump<T> setMaxVerticalVelocity(float maxVerticalVelocity) {
        this.maxVerticalVelocity = maxVerticalVelocity;
        return this;
    }

    public float getTakeoffPositionTolerance() {
        return this.takeoffPositionTolerance;
    }

    public Jump<T> setTakeoffPositionTolerance(float takeoffPositionTolerance) {
        this.takeoffPositionTolerance = takeoffPositionTolerance;
        return this;
    }

    public float getTakeoffVelocityTolerance() {
        return this.takeoffVelocityTolerance;
    }

    public Jump<T> setTakeoffVelocityTolerance(float takeoffVelocityTolerance) {
        this.takeoffVelocityTolerance = takeoffVelocityTolerance;
        return this;
    }

    public Jump<T> setTakeoffTolerance(float takeoffTolerance) {
        this.setTakeoffPositionTolerance(takeoffTolerance);
        this.setTakeoffVelocityTolerance(takeoffTolerance);
        return this;
    }

    @Override
    public Jump<T> setOwner(Steerable<T> owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public Jump<T> setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public Jump<T> setLimiter(Limiter limiter) {
        this.limiter = limiter;
        return this;
    }

    @Override
    public Jump<T> setTarget(Steerable<T> target) {
        this.target = target;
        return this;
    }

    @Override
    public Jump<T> setTimeToTarget(float timeToTarget) {
        this.timeToTarget = timeToTarget;
        return this;
    }

    public static interface JumpCallback {
        public void reportAchievability(boolean var1);

        public void takeoff(float var1, float var2);
    }

    public static interface GravityComponentHandler<T extends Vector<T>> {
        public float getComponent(T var1);

        public void setComponent(T var1, float var2);
    }

    public static class JumpDescriptor<T extends Vector<T>> {
        public T takeoffPosition;
        public T landingPosition;
        public T delta;

        public JumpDescriptor(T takeoffPosition, T landingPosition) {
            this.takeoffPosition = takeoffPosition;
            this.landingPosition = landingPosition;
            this.delta = landingPosition.cpy();
            this.set(takeoffPosition, landingPosition);
        }

        public void set(T takeoffPosition, T landingPosition) {
            this.takeoffPosition.set(takeoffPosition);
            this.landingPosition.set(landingPosition);
            this.delta.set(landingPosition).sub(takeoffPosition);
        }
    }

    private static class JumpTarget<T extends Vector<T>>
    extends SteerableAdapter<T> {
        T position = null;
        T linearVelocity;

        public JumpTarget(Steerable<T> other) {
            this.linearVelocity = other.getPosition().cpy().setZero();
        }

        @Override
        public T getPosition() {
            return this.position;
        }

        @Override
        public T getLinearVelocity() {
            return this.linearVelocity;
        }
    }

}

