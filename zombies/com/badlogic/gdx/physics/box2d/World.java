/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.physics.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.DestructionListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.FrictionJoint;
import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;
import com.badlogic.gdx.physics.box2d.joints.GearJoint;
import com.badlogic.gdx.physics.box2d.joints.GearJointDef;
import com.badlogic.gdx.physics.box2d.joints.MotorJoint;
import com.badlogic.gdx.physics.box2d.joints.MotorJointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.PulleyJoint;
import com.badlogic.gdx.physics.box2d.joints.PulleyJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.LongMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.SharedLibraryLoader;

public final class World
implements Disposable {
    protected final Pool<Body> freeBodies;
    protected final Pool<Fixture> freeFixtures;
    protected final long addr;
    protected final LongMap<Body> bodies;
    protected final LongMap<Fixture> fixtures;
    protected final LongMap<Joint> joints;
    protected ContactFilter contactFilter;
    protected ContactListener contactListener;
    final float[] tmpGravity;
    final Vector2 gravity;
    private QueryCallback queryCallback;
    private long[] contactAddrs;
    private final Array<Contact> contacts;
    private final Array<Contact> freeContacts;
    private final Contact contact;
    private final Manifold manifold;
    private final ContactImpulse impulse;
    private RayCastCallback rayCastCallback;
    private Vector2 rayPoint;
    private Vector2 rayNormal;

    public World(Vector2 gravity, boolean doSleep) {
        this.freeBodies = new Pool<Body>(100, 200){

            @Override
            protected Body newObject() {
                return new Body(World.this, 0);
            }
        };
        this.freeFixtures = new Pool<Fixture>(100, 200){

            @Override
            protected Fixture newObject() {
                return new Fixture(null, 0);
            }
        };
        this.bodies = new LongMap(100);
        this.fixtures = new LongMap(100);
        this.joints = new LongMap(100);
        this.contactFilter = null;
        this.contactListener = null;
        this.tmpGravity = new float[2];
        this.gravity = new Vector2();
        this.queryCallback = null;
        this.contactAddrs = new long[200];
        this.contacts = new Array();
        this.freeContacts = new Array();
        this.contact = new Contact(this, 0);
        this.manifold = new Manifold(0);
        this.impulse = new ContactImpulse(this, 0);
        this.rayCastCallback = null;
        this.rayPoint = new Vector2();
        this.rayNormal = new Vector2();
        this.addr = this.newWorld(gravity.x, gravity.y, doSleep);
        this.contacts.ensureCapacity(this.contactAddrs.length);
        this.freeContacts.ensureCapacity(this.contactAddrs.length);
        for (int i = 0; i < this.contactAddrs.length; ++i) {
            this.freeContacts.add(new Contact(this, 0));
        }
    }

    private native long newWorld(float var1, float var2, boolean var3);

    public void setDestructionListener(DestructionListener listener) {
    }

    public void setContactFilter(ContactFilter filter) {
        this.contactFilter = filter;
        this.setUseDefaultContactFilter(filter == null);
    }

    private native void setUseDefaultContactFilter(boolean var1);

    public void setContactListener(ContactListener listener) {
        this.contactListener = listener;
    }

    public Body createBody(BodyDef def) {
        long bodyAddr = this.jniCreateBody(this.addr, def.type.getValue(), def.position.x, def.position.y, def.angle, def.linearVelocity.x, def.linearVelocity.y, def.angularVelocity, def.linearDamping, def.angularDamping, def.allowSleep, def.awake, def.fixedRotation, def.bullet, def.active, def.gravityScale);
        Body body = this.freeBodies.obtain();
        body.reset(bodyAddr);
        this.bodies.put(body.addr, body);
        return body;
    }

    private native long jniCreateBody(long var1, int var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, boolean var12, boolean var13, boolean var14, boolean var15, boolean var16, float var17);

    public void destroyBody(Body body) {
        Array<JointEdge> jointList = body.getJointList();
        while (jointList.size > 0) {
            this.destroyJoint(body.getJointList().get((int)0).joint);
        }
        this.jniDestroyBody(this.addr, body.addr);
        body.setUserData(null);
        this.bodies.remove(body.addr);
        Array<Fixture> fixtureList = body.getFixtureList();
        while (fixtureList.size > 0) {
            Fixture fixtureToDelete = fixtureList.removeIndex(0);
            this.fixtures.remove(fixtureToDelete.addr).setUserData(null);
            this.freeFixtures.free(fixtureToDelete);
        }
        this.freeBodies.free(body);
    }

    private native void jniDestroyBody(long var1, long var3);

    void destroyFixture(Body body, Fixture fixture) {
        this.jniDestroyFixture(this.addr, body.addr, fixture.addr);
    }

    private native void jniDestroyFixture(long var1, long var3, long var5);

    void deactivateBody(Body body) {
        this.jniDeactivateBody(this.addr, body.addr);
    }

    private native void jniDeactivateBody(long var1, long var3);

    public Joint createJoint(JointDef def) {
        long jointAddr = this.createProperJoint(def);
        Joint joint = null;
        if (def.type == JointDef.JointType.DistanceJoint) {
            joint = new DistanceJoint(this, jointAddr);
        }
        if (def.type == JointDef.JointType.FrictionJoint) {
            joint = new FrictionJoint(this, jointAddr);
        }
        if (def.type == JointDef.JointType.GearJoint) {
            joint = new GearJoint(this, jointAddr, ((GearJointDef)def).joint1, ((GearJointDef)def).joint2);
        }
        if (def.type == JointDef.JointType.MotorJoint) {
            joint = new MotorJoint(this, jointAddr);
        }
        if (def.type == JointDef.JointType.MouseJoint) {
            joint = new MouseJoint(this, jointAddr);
        }
        if (def.type == JointDef.JointType.PrismaticJoint) {
            joint = new PrismaticJoint(this, jointAddr);
        }
        if (def.type == JointDef.JointType.PulleyJoint) {
            joint = new PulleyJoint(this, jointAddr);
        }
        if (def.type == JointDef.JointType.RevoluteJoint) {
            joint = new RevoluteJoint(this, jointAddr);
        }
        if (def.type == JointDef.JointType.RopeJoint) {
            joint = new RopeJoint(this, jointAddr);
        }
        if (def.type == JointDef.JointType.WeldJoint) {
            joint = new WeldJoint(this, jointAddr);
        }
        if (def.type == JointDef.JointType.WheelJoint) {
            joint = new WheelJoint(this, jointAddr);
        }
        if (joint != null) {
            this.joints.put(joint.addr, joint);
        }
        JointEdge jointEdgeA = new JointEdge(def.bodyB, joint);
        JointEdge jointEdgeB = new JointEdge(def.bodyA, joint);
        joint.jointEdgeA = jointEdgeA;
        joint.jointEdgeB = jointEdgeB;
        def.bodyA.joints.add(jointEdgeA);
        def.bodyB.joints.add(jointEdgeB);
        return joint;
    }

    private long createProperJoint(JointDef def) {
        if (def.type == JointDef.JointType.DistanceJoint) {
            DistanceJointDef d = (DistanceJointDef)def;
            return this.jniCreateDistanceJoint(this.addr, d.bodyA.addr, d.bodyB.addr, d.collideConnected, d.localAnchorA.x, d.localAnchorA.y, d.localAnchorB.x, d.localAnchorB.y, d.length, d.frequencyHz, d.dampingRatio);
        }
        if (def.type == JointDef.JointType.FrictionJoint) {
            FrictionJointDef d = (FrictionJointDef)def;
            return this.jniCreateFrictionJoint(this.addr, d.bodyA.addr, d.bodyB.addr, d.collideConnected, d.localAnchorA.x, d.localAnchorA.y, d.localAnchorB.x, d.localAnchorB.y, d.maxForce, d.maxTorque);
        }
        if (def.type == JointDef.JointType.GearJoint) {
            GearJointDef d = (GearJointDef)def;
            return this.jniCreateGearJoint(this.addr, d.bodyA.addr, d.bodyB.addr, d.collideConnected, d.joint1.addr, d.joint2.addr, d.ratio);
        }
        if (def.type == JointDef.JointType.MotorJoint) {
            MotorJointDef d = (MotorJointDef)def;
            return this.jniCreateMotorJoint(this.addr, d.bodyA.addr, d.bodyB.addr, d.collideConnected, d.linearOffset.x, d.linearOffset.y, d.angularOffset, d.maxForce, d.maxTorque, d.correctionFactor);
        }
        if (def.type == JointDef.JointType.MouseJoint) {
            MouseJointDef d = (MouseJointDef)def;
            return this.jniCreateMouseJoint(this.addr, d.bodyA.addr, d.bodyB.addr, d.collideConnected, d.target.x, d.target.y, d.maxForce, d.frequencyHz, d.dampingRatio);
        }
        if (def.type == JointDef.JointType.PrismaticJoint) {
            PrismaticJointDef d = (PrismaticJointDef)def;
            return this.jniCreatePrismaticJoint(this.addr, d.bodyA.addr, d.bodyB.addr, d.collideConnected, d.localAnchorA.x, d.localAnchorA.y, d.localAnchorB.x, d.localAnchorB.y, d.localAxisA.x, d.localAxisA.y, d.referenceAngle, d.enableLimit, d.lowerTranslation, d.upperTranslation, d.enableMotor, d.maxMotorForce, d.motorSpeed);
        }
        if (def.type == JointDef.JointType.PulleyJoint) {
            PulleyJointDef d = (PulleyJointDef)def;
            return this.jniCreatePulleyJoint(this.addr, d.bodyA.addr, d.bodyB.addr, d.collideConnected, d.groundAnchorA.x, d.groundAnchorA.y, d.groundAnchorB.x, d.groundAnchorB.y, d.localAnchorA.x, d.localAnchorA.y, d.localAnchorB.x, d.localAnchorB.y, d.lengthA, d.lengthB, d.ratio);
        }
        if (def.type == JointDef.JointType.RevoluteJoint) {
            RevoluteJointDef d = (RevoluteJointDef)def;
            return this.jniCreateRevoluteJoint(this.addr, d.bodyA.addr, d.bodyB.addr, d.collideConnected, d.localAnchorA.x, d.localAnchorA.y, d.localAnchorB.x, d.localAnchorB.y, d.referenceAngle, d.enableLimit, d.lowerAngle, d.upperAngle, d.enableMotor, d.motorSpeed, d.maxMotorTorque);
        }
        if (def.type == JointDef.JointType.RopeJoint) {
            RopeJointDef d = (RopeJointDef)def;
            return this.jniCreateRopeJoint(this.addr, d.bodyA.addr, d.bodyB.addr, d.collideConnected, d.localAnchorA.x, d.localAnchorA.y, d.localAnchorB.x, d.localAnchorB.y, d.maxLength);
        }
        if (def.type == JointDef.JointType.WeldJoint) {
            WeldJointDef d = (WeldJointDef)def;
            return this.jniCreateWeldJoint(this.addr, d.bodyA.addr, d.bodyB.addr, d.collideConnected, d.localAnchorA.x, d.localAnchorA.y, d.localAnchorB.x, d.localAnchorB.y, d.referenceAngle, d.frequencyHz, d.dampingRatio);
        }
        if (def.type == JointDef.JointType.WheelJoint) {
            WheelJointDef d = (WheelJointDef)def;
            return this.jniCreateWheelJoint(this.addr, d.bodyA.addr, d.bodyB.addr, d.collideConnected, d.localAnchorA.x, d.localAnchorA.y, d.localAnchorB.x, d.localAnchorB.y, d.localAxisA.x, d.localAxisA.y, d.enableMotor, d.maxMotorTorque, d.motorSpeed, d.frequencyHz, d.dampingRatio);
        }
        return 0;
    }

    private native long jniCreateWheelJoint(long var1, long var3, long var5, boolean var7, float var8, float var9, float var10, float var11, float var12, float var13, boolean var14, float var15, float var16, float var17, float var18);

    private native long jniCreateRopeJoint(long var1, long var3, long var5, boolean var7, float var8, float var9, float var10, float var11, float var12);

    private native long jniCreateDistanceJoint(long var1, long var3, long var5, boolean var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14);

    private native long jniCreateFrictionJoint(long var1, long var3, long var5, boolean var7, float var8, float var9, float var10, float var11, float var12, float var13);

    private native long jniCreateGearJoint(long var1, long var3, long var5, boolean var7, long var8, long var10, float var12);

    private native long jniCreateMotorJoint(long var1, long var3, long var5, boolean var7, float var8, float var9, float var10, float var11, float var12, float var13);

    private native long jniCreateMouseJoint(long var1, long var3, long var5, boolean var7, float var8, float var9, float var10, float var11, float var12);

    private native long jniCreatePrismaticJoint(long var1, long var3, long var5, boolean var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, boolean var15, float var16, float var17, boolean var18, float var19, float var20);

    private native long jniCreatePulleyJoint(long var1, long var3, long var5, boolean var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18);

    private native long jniCreateRevoluteJoint(long var1, long var3, long var5, boolean var7, float var8, float var9, float var10, float var11, float var12, boolean var13, float var14, float var15, boolean var16, float var17, float var18);

    private native long jniCreateWeldJoint(long var1, long var3, long var5, boolean var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14);

    public void destroyJoint(Joint joint) {
        joint.setUserData(null);
        this.joints.remove(joint.addr);
        joint.jointEdgeA.other.joints.removeValue(joint.jointEdgeB, true);
        joint.jointEdgeB.other.joints.removeValue(joint.jointEdgeA, true);
        this.jniDestroyJoint(this.addr, joint.addr);
    }

    private native void jniDestroyJoint(long var1, long var3);

    public void step(float timeStep, int velocityIterations, int positionIterations) {
        this.jniStep(this.addr, timeStep, velocityIterations, positionIterations);
    }

    private native void jniStep(long var1, float var3, int var4, int var5);

    public void clearForces() {
        this.jniClearForces(this.addr);
    }

    private native void jniClearForces(long var1);

    public void setWarmStarting(boolean flag) {
        this.jniSetWarmStarting(this.addr, flag);
    }

    private native void jniSetWarmStarting(long var1, boolean var3);

    public void setContinuousPhysics(boolean flag) {
        this.jniSetContiousPhysics(this.addr, flag);
    }

    private native void jniSetContiousPhysics(long var1, boolean var3);

    public int getProxyCount() {
        return this.jniGetProxyCount(this.addr);
    }

    private native int jniGetProxyCount(long var1);

    public int getBodyCount() {
        return this.jniGetBodyCount(this.addr);
    }

    private native int jniGetBodyCount(long var1);

    public int getFixtureCount() {
        return this.fixtures.size;
    }

    public int getJointCount() {
        return this.jniGetJointcount(this.addr);
    }

    private native int jniGetJointcount(long var1);

    public int getContactCount() {
        return this.jniGetContactCount(this.addr);
    }

    private native int jniGetContactCount(long var1);

    public void setGravity(Vector2 gravity) {
        this.jniSetGravity(this.addr, gravity.x, gravity.y);
    }

    private native void jniSetGravity(long var1, float var3, float var4);

    public Vector2 getGravity() {
        this.jniGetGravity(this.addr, this.tmpGravity);
        this.gravity.x = this.tmpGravity[0];
        this.gravity.y = this.tmpGravity[1];
        return this.gravity;
    }

    private native void jniGetGravity(long var1, float[] var3);

    public boolean isLocked() {
        return this.jniIsLocked(this.addr);
    }

    private native boolean jniIsLocked(long var1);

    public void setAutoClearForces(boolean flag) {
        this.jniSetAutoClearForces(this.addr, flag);
    }

    private native void jniSetAutoClearForces(long var1, boolean var3);

    public boolean getAutoClearForces() {
        return this.jniGetAutoClearForces(this.addr);
    }

    private native boolean jniGetAutoClearForces(long var1);

    public void QueryAABB(QueryCallback callback, float lowerX, float lowerY, float upperX, float upperY) {
        this.queryCallback = callback;
        this.jniQueryAABB(this.addr, lowerX, lowerY, upperX, upperY);
    }

    private native void jniQueryAABB(long var1, float var3, float var4, float var5, float var6);

    public Array<Contact> getContactList() {
        int numContacts = this.getContactCount();
        if (numContacts > this.contactAddrs.length) {
            int newSize = 2 * numContacts;
            this.contactAddrs = new long[newSize];
            this.contacts.ensureCapacity(newSize);
            this.freeContacts.ensureCapacity(newSize);
        }
        if (numContacts > this.freeContacts.size) {
            int freeConts = this.freeContacts.size;
            for (int i = 0; i < numContacts - freeConts; ++i) {
                this.freeContacts.add(new Contact(this, 0));
            }
        }
        this.jniGetContactList(this.addr, this.contactAddrs);
        this.contacts.clear();
        for (int i = 0; i < numContacts; ++i) {
            Contact contact = this.freeContacts.get(i);
            contact.addr = this.contactAddrs[i];
            this.contacts.add(contact);
        }
        return this.contacts;
    }

    public void getBodies(Array<Body> bodies) {
        bodies.clear();
        bodies.ensureCapacity(this.bodies.size);
        LongMap.Values<Body> iter = this.bodies.values();
        while (iter.hasNext()) {
            bodies.add(iter.next());
        }
    }

    public void getFixtures(Array<Fixture> fixtures) {
        fixtures.clear();
        fixtures.ensureCapacity(this.fixtures.size);
        LongMap.Values<Fixture> iter = this.fixtures.values();
        while (iter.hasNext()) {
            fixtures.add(iter.next());
        }
    }

    public void getJoints(Array<Joint> joints) {
        joints.clear();
        joints.ensureCapacity(this.joints.size);
        LongMap.Values<Joint> iter = this.joints.values();
        while (iter.hasNext()) {
            joints.add(iter.next());
        }
    }

    private native void jniGetContactList(long var1, long[] var3);

    @Override
    public void dispose() {
        this.jniDispose(this.addr);
    }

    private native void jniDispose(long var1);

    private boolean contactFilter(long fixtureA, long fixtureB) {
        if (this.contactFilter != null) {
            return this.contactFilter.shouldCollide(this.fixtures.get(fixtureA), this.fixtures.get(fixtureB));
        }
        Filter filterA = this.fixtures.get(fixtureA).getFilterData();
        Filter filterB = this.fixtures.get(fixtureB).getFilterData();
        if (filterA.groupIndex == filterB.groupIndex && filterA.groupIndex != 0) {
            return filterA.groupIndex > 0;
        }
        boolean collide = (filterA.maskBits & filterB.categoryBits) != 0 && (filterA.categoryBits & filterB.maskBits) != 0;
        return collide;
    }

    private void beginContact(long contactAddr) {
        this.contact.addr = contactAddr;
        if (this.contactListener != null) {
            this.contactListener.beginContact(this.contact);
        }
    }

    private void endContact(long contactAddr) {
        this.contact.addr = contactAddr;
        if (this.contactListener != null) {
            this.contactListener.endContact(this.contact);
        }
    }

    private void preSolve(long contactAddr, long manifoldAddr) {
        this.contact.addr = contactAddr;
        this.manifold.addr = manifoldAddr;
        if (this.contactListener != null) {
            this.contactListener.preSolve(this.contact, this.manifold);
        }
    }

    private void postSolve(long contactAddr, long impulseAddr) {
        this.contact.addr = contactAddr;
        this.impulse.addr = impulseAddr;
        if (this.contactListener != null) {
            this.contactListener.postSolve(this.contact, this.impulse);
        }
    }

    private boolean reportFixture(long addr) {
        if (this.queryCallback != null) {
            return this.queryCallback.reportFixture(this.fixtures.get(addr));
        }
        return false;
    }

    public static native void setVelocityThreshold(float var0);

    public static native float getVelocityThreshold();

    public void rayCast(RayCastCallback callback, Vector2 point1, Vector2 point2) {
        this.rayCast(callback, point1.x, point1.y, point2.x, point2.y);
    }

    public void rayCast(RayCastCallback callback, float point1X, float point1Y, float point2X, float point2Y) {
        this.rayCastCallback = callback;
        this.jniRayCast(this.addr, point1X, point1Y, point2X, point2Y);
    }

    private native void jniRayCast(long var1, float var3, float var4, float var5, float var6);

    private float reportRayFixture(long addr, float pX, float pY, float nX, float nY, float fraction) {
        if (this.rayCastCallback != null) {
            this.rayPoint.x = pX;
            this.rayPoint.y = pY;
            this.rayNormal.x = nX;
            this.rayNormal.y = nY;
            return this.rayCastCallback.reportRayFixture(this.fixtures.get(addr), this.rayPoint, this.rayNormal, fraction);
        }
        return 0.0f;
    }

    static {
        new SharedLibraryLoader().load("gdx-box2d");
    }

}

