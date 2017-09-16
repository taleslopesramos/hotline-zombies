/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.backends.lwjgl;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglNativesLoader;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.LayoutManager;

public class LwjglApplet
extends Applet {
    final Canvas canvas;
    LwjglApplication app;

    public LwjglApplet(final ApplicationListener listener, final LwjglApplicationConfiguration config) {
        LwjglNativesLoader.load = false;
        this.canvas = new Canvas(){

            @Override
            public final void addNotify() {
                super.addNotify();
                LwjglApplet.this.app = new LwjglAppletApplication(LwjglApplet.this, listener, LwjglApplet.this.canvas, config);
            }

            @Override
            public final void removeNotify() {
                LwjglApplet.this.app.stop();
                super.removeNotify();
            }
        };
        this.setLayout(new BorderLayout());
        this.canvas.setIgnoreRepaint(true);
        this.add(this.canvas);
        this.canvas.setFocusable(true);
        this.canvas.requestFocus();
    }

    public LwjglApplet(final ApplicationListener listener) {
        LwjglNativesLoader.load = false;
        this.canvas = new Canvas(){

            @Override
            public final void addNotify() {
                super.addNotify();
                LwjglApplet.this.app = new LwjglAppletApplication(LwjglApplet.this, listener, LwjglApplet.this.canvas);
            }

            @Override
            public final void removeNotify() {
                LwjglApplet.this.app.stop();
                super.removeNotify();
            }
        };
        this.setLayout(new BorderLayout());
        this.canvas.setIgnoreRepaint(true);
        this.add(this.canvas);
        this.canvas.setFocusable(true);
        this.canvas.requestFocus();
    }

    @Override
    public void destroy() {
        this.remove(this.canvas);
        super.destroy();
    }

    class LwjglAppletApplication
    extends LwjglApplication {
        final /* synthetic */ LwjglApplet this$0;

        public LwjglAppletApplication(LwjglApplet this$0, ApplicationListener listener, Canvas canvas) {
            this.this$0 = this$0;
            super(listener, canvas);
        }

        public LwjglAppletApplication(LwjglApplet this$0, ApplicationListener listener, Canvas canvas, LwjglApplicationConfiguration config) {
            this.this$0 = this$0;
            super(listener, config, canvas);
        }

        @Override
        public Application.ApplicationType getType() {
            return Application.ApplicationType.Applet;
        }
    }

}

