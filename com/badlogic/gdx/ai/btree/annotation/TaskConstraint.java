/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
@Inherited
@Documented
public @interface TaskConstraint {
    public int minChildren() default 0;

    public int maxChildren() default Integer.MAX_VALUE;
}

