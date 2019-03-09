/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree.utils;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;

public class BehaviorTreeLibrary {
    protected ObjectMap<String, BehaviorTree<?>> repository;
    protected FileHandleResolver resolver;
    protected BehaviorTreeParser<?> parser;

    public BehaviorTreeLibrary() {
        this(0);
    }

    public BehaviorTreeLibrary(int parseDebugLevel) {
        this(GdxAI.getFileSystem().newResolver(Files.FileType.Internal), parseDebugLevel);
    }

    public BehaviorTreeLibrary(FileHandleResolver resolver) {
        this(resolver, 0);
    }

    public BehaviorTreeLibrary(FileHandleResolver resolver, int parseDebugLevel) {
        this(resolver, null, parseDebugLevel);
    }

    private BehaviorTreeLibrary(FileHandleResolver resolver, AssetManager assetManager, int parseDebugLevel) {
        this.resolver = resolver;
        this.repository = new ObjectMap();
        this.parser = new BehaviorTreeParser(parseDebugLevel);
    }

    public <T> Task<T> createRootTask(String treeReference) {
        return this.retrieveArchetypeTree(treeReference).getChild(0).cloneTask();
    }

    public <T> BehaviorTree<T> createBehaviorTree(String treeReference) {
        return this.createBehaviorTree(treeReference, null);
    }

    public <T> BehaviorTree<T> createBehaviorTree(String treeReference, T blackboard) {
        BehaviorTree bt = (BehaviorTree)this.retrieveArchetypeTree(treeReference).cloneTask();
        bt.setObject(blackboard);
        return bt;
    }

    protected BehaviorTree<?> retrieveArchetypeTree(String treeReference) {
        BehaviorTree archetypeTree = this.repository.get(treeReference);
        if (archetypeTree == null) {
            archetypeTree = this.parser.parse(this.resolver.resolve(treeReference), null);
            this.registerArchetypeTree(treeReference, archetypeTree);
        }
        return archetypeTree;
    }

    public void registerArchetypeTree(String treeReference, BehaviorTree<?> archetypeTree) {
        if (archetypeTree == null) {
            throw new IllegalArgumentException("The registered archetype must not be null.");
        }
        this.repository.put(treeReference, archetypeTree);
    }
}

