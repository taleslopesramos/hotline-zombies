/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree.utils;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeLibrary;

public class BehaviorTreeLibraryManager {
    private static BehaviorTreeLibraryManager instance = new BehaviorTreeLibraryManager();
    protected BehaviorTreeLibrary library;

    private BehaviorTreeLibraryManager() {
        this.setLibrary(new BehaviorTreeLibrary());
    }

    public static BehaviorTreeLibraryManager getInstance() {
        return instance;
    }

    public BehaviorTreeLibrary getLibrary() {
        return this.library;
    }

    public void setLibrary(BehaviorTreeLibrary library) {
        this.library = library;
    }

    public <T> Task<T> createRootTask(String treeReference) {
        return this.library.createRootTask(treeReference);
    }

    public <T> BehaviorTree<T> createBehaviorTree(String treeReference) {
        return this.library.createBehaviorTree(treeReference);
    }

    public <T> BehaviorTree<T> createBehaviorTree(String treeReference, T blackboard) {
        return this.library.createBehaviorTree(treeReference, blackboard);
    }
}

