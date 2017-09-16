/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  com.badlogic.gdx.jnigen.AntScriptGenerator
 *  com.badlogic.gdx.jnigen.BuildConfig
 *  com.badlogic.gdx.jnigen.BuildTarget
 *  com.badlogic.gdx.jnigen.BuildTarget$TargetOs
 *  com.badlogic.gdx.jnigen.NativeCodeGenerator
 */
package com.badlogic.gdx.physics.box2d.utils;

import com.badlogic.gdx.jnigen.AntScriptGenerator;
import com.badlogic.gdx.jnigen.BuildConfig;
import com.badlogic.gdx.jnigen.BuildTarget;
import com.badlogic.gdx.jnigen.NativeCodeGenerator;
import java.io.File;

public class Box2DBuild {
    public static void main(String[] args) throws Exception {
        BuildTarget win32 = BuildTarget.newDefaultTarget((BuildTarget.TargetOs)BuildTarget.TargetOs.Windows, (boolean)false);
        BuildTarget win64 = BuildTarget.newDefaultTarget((BuildTarget.TargetOs)BuildTarget.TargetOs.Windows, (boolean)true);
        BuildTarget lin32 = BuildTarget.newDefaultTarget((BuildTarget.TargetOs)BuildTarget.TargetOs.Linux, (boolean)false);
        BuildTarget lin64 = BuildTarget.newDefaultTarget((BuildTarget.TargetOs)BuildTarget.TargetOs.Linux, (boolean)true);
        BuildTarget android = BuildTarget.newDefaultTarget((BuildTarget.TargetOs)BuildTarget.TargetOs.Android, (boolean)false);
        BuildTarget mac32 = BuildTarget.newDefaultTarget((BuildTarget.TargetOs)BuildTarget.TargetOs.MacOsX, (boolean)false);
        BuildTarget mac64 = BuildTarget.newDefaultTarget((BuildTarget.TargetOs)BuildTarget.TargetOs.MacOsX, (boolean)true);
        BuildTarget ios = BuildTarget.newDefaultTarget((BuildTarget.TargetOs)BuildTarget.TargetOs.IOS, (boolean)false);
        new NativeCodeGenerator().generate("src", "bin" + File.pathSeparator + "../../../gdx/bin", "jni");
        new AntScriptGenerator().generate(new BuildConfig("gdx-box2d"), new BuildTarget[]{win32, win64, lin32, lin64, mac32, mac64, android, ios});
    }
}

