/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.msg;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.msg.PriorityQueue;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.TelegramProvider;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.reflect.ClassReflection;

public class MessageDispatcher
implements Telegraph {
    private static final String LOG_TAG = MessageDispatcher.class.getSimpleName();
    private static final Pool<Telegram> pool = new Pool<Telegram>(16){

        @Override
        protected Telegram newObject() {
            return new Telegram();
        }
    };
    private PriorityQueue<Telegram> queue = new PriorityQueue();
    private IntMap<Array<Telegraph>> msgListeners = new IntMap();
    private IntMap<Array<TelegramProvider>> msgProviders = new IntMap();
    private boolean debugEnabled;

    public boolean isDebugEnabled() {
        return this.debugEnabled;
    }

    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }

    public void addListener(Telegraph listener, int msg) {
        Array listeners = this.msgListeners.get(msg);
        if (listeners == null) {
            listeners = new Array(false, 16);
            this.msgListeners.put(msg, listeners);
        }
        listeners.add(listener);
        Array<TelegramProvider> providers = this.msgProviders.get(msg);
        if (providers != null) {
            int n = providers.size;
            for (int i = 0; i < n; ++i) {
                TelegramProvider provider = providers.get(i);
                Object info = provider.provideMessageInfo(msg, listener);
                if (info == null) continue;
                Telegraph sender = ClassReflection.isInstance(Telegraph.class, provider) ? (Telegraph)((Object)provider) : null;
                this.dispatchMessage(0.0f, sender, listener, msg, info, false);
            }
        }
    }

    public /* varargs */ void addListeners(Telegraph listener, int ... msgs) {
        for (int msg : msgs) {
            this.addListener(listener, msg);
        }
    }

    public void addProvider(TelegramProvider provider, int msg) {
        Array providers = this.msgProviders.get(msg);
        if (providers == null) {
            providers = new Array(false, 16);
            this.msgProviders.put(msg, providers);
        }
        providers.add(provider);
    }

    public /* varargs */ void addProviders(TelegramProvider provider, int ... msgs) {
        for (int msg : msgs) {
            this.addProvider(provider, msg);
        }
    }

    public void removeListener(Telegraph listener, int msg) {
        Array<Telegraph> listeners = this.msgListeners.get(msg);
        if (listeners != null) {
            listeners.removeValue(listener, true);
        }
    }

    public /* varargs */ void removeListener(Telegraph listener, int ... msgs) {
        for (int msg : msgs) {
            this.removeListener(listener, msg);
        }
    }

    public void clearListeners(int msg) {
        this.msgListeners.remove(msg);
    }

    public /* varargs */ void clearListeners(int ... msgs) {
        for (int msg : msgs) {
            this.clearListeners(msg);
        }
    }

    public void clearListeners() {
        this.msgListeners.clear();
    }

    public void clearProviders(int msg) {
        this.msgProviders.remove(msg);
    }

    public /* varargs */ void clearProviders(int ... msgs) {
        for (int msg : msgs) {
            this.clearProviders(msg);
        }
    }

    public void clearProviders() {
        this.msgProviders.clear();
    }

    public void clearQueue() {
        for (int i = 0; i < this.queue.size(); ++i) {
            pool.free(this.queue.get(i));
        }
        this.queue.clear();
    }

    public void clear() {
        this.clearQueue();
        this.clearListeners();
        this.clearProviders();
    }

    public void dispatchMessage(int msg) {
        this.dispatchMessage(0.0f, null, null, msg, null, false);
    }

    public void dispatchMessage(Telegraph sender, int msg) {
        this.dispatchMessage(0.0f, sender, null, msg, null, false);
    }

    public void dispatchMessage(Telegraph sender, int msg, boolean needsReturnReceipt) {
        this.dispatchMessage(0.0f, sender, null, msg, null, needsReturnReceipt);
    }

    public void dispatchMessage(int msg, Object extraInfo) {
        this.dispatchMessage(0.0f, null, null, msg, extraInfo, false);
    }

    public void dispatchMessage(Telegraph sender, int msg, Object extraInfo) {
        this.dispatchMessage(0.0f, sender, null, msg, extraInfo, false);
    }

    public void dispatchMessage(Telegraph sender, int msg, Object extraInfo, boolean needsReturnReceipt) {
        this.dispatchMessage(0.0f, sender, null, msg, extraInfo, needsReturnReceipt);
    }

    public void dispatchMessage(Telegraph sender, Telegraph receiver, int msg) {
        this.dispatchMessage(0.0f, sender, receiver, msg, null, false);
    }

    public void dispatchMessage(Telegraph sender, Telegraph receiver, int msg, boolean needsReturnReceipt) {
        this.dispatchMessage(0.0f, sender, receiver, msg, null, needsReturnReceipt);
    }

    public void dispatchMessage(Telegraph sender, Telegraph receiver, int msg, Object extraInfo) {
        this.dispatchMessage(0.0f, sender, receiver, msg, extraInfo, false);
    }

    public void dispatchMessage(Telegraph sender, Telegraph receiver, int msg, Object extraInfo, boolean needsReturnReceipt) {
        this.dispatchMessage(0.0f, sender, receiver, msg, extraInfo, needsReturnReceipt);
    }

    public void dispatchMessage(float delay, int msg) {
        this.dispatchMessage(delay, null, null, msg, null, false);
    }

    public void dispatchMessage(float delay, Telegraph sender, int msg) {
        this.dispatchMessage(delay, sender, null, msg, null, false);
    }

    public void dispatchMessage(float delay, Telegraph sender, int msg, boolean needsReturnReceipt) {
        this.dispatchMessage(delay, sender, null, msg, null, needsReturnReceipt);
    }

    public void dispatchMessage(float delay, int msg, Object extraInfo) {
        this.dispatchMessage(delay, null, null, msg, extraInfo, false);
    }

    public void dispatchMessage(float delay, Telegraph sender, int msg, Object extraInfo) {
        this.dispatchMessage(delay, sender, null, msg, extraInfo, false);
    }

    public void dispatchMessage(float delay, Telegraph sender, int msg, Object extraInfo, boolean needsReturnReceipt) {
        this.dispatchMessage(delay, sender, null, msg, extraInfo, needsReturnReceipt);
    }

    public void dispatchMessage(float delay, Telegraph sender, Telegraph receiver, int msg) {
        this.dispatchMessage(delay, sender, receiver, msg, null, false);
    }

    public void dispatchMessage(float delay, Telegraph sender, Telegraph receiver, int msg, boolean needsReturnReceipt) {
        this.dispatchMessage(delay, sender, receiver, msg, null, needsReturnReceipt);
    }

    public void dispatchMessage(float delay, Telegraph sender, Telegraph receiver, int msg, Object extraInfo) {
        this.dispatchMessage(delay, sender, receiver, msg, extraInfo, false);
    }

    public void dispatchMessage(float delay, Telegraph sender, Telegraph receiver, int msg, Object extraInfo, boolean needsReturnReceipt) {
        if (sender == null && needsReturnReceipt) {
            throw new IllegalArgumentException("Sender cannot be null when a return receipt is needed");
        }
        Telegram telegram = pool.obtain();
        telegram.sender = sender;
        telegram.receiver = receiver;
        telegram.message = msg;
        telegram.extraInfo = extraInfo;
        int n = telegram.returnReceiptStatus = needsReturnReceipt ? 1 : 0;
        if (delay <= 0.0f) {
            if (this.debugEnabled) {
                float currentTime = GdxAI.getTimepiece().getTime();
                GdxAI.getLogger().info(LOG_TAG, "Instant telegram dispatched at time: " + currentTime + " by " + sender + " for " + receiver + ". Message code is " + msg);
            }
            this.discharge(telegram);
        } else {
            float currentTime = GdxAI.getTimepiece().getTime();
            telegram.setTimestamp(currentTime + delay);
            boolean added = this.queue.add(telegram);
            if (!added) {
                pool.free(telegram);
            }
            if (this.debugEnabled) {
                if (added) {
                    GdxAI.getLogger().info(LOG_TAG, "Delayed telegram from " + sender + " for " + receiver + " recorded at time " + currentTime + ". Message code is " + msg);
                } else {
                    GdxAI.getLogger().info(LOG_TAG, "Delayed telegram from " + sender + " for " + receiver + " rejected by the queue. Message code is " + msg);
                }
            }
        }
    }

    public void update() {
        Telegram telegram;
        float currentTime = GdxAI.getTimepiece().getTime();
        while ((telegram = this.queue.peek()) != null && telegram.getTimestamp() <= currentTime) {
            if (this.debugEnabled) {
                GdxAI.getLogger().info(LOG_TAG, "Queued telegram ready for dispatch: Sent to " + telegram.receiver + ". Message code is " + telegram.message);
            }
            this.discharge(telegram);
            this.queue.poll();
        }
    }

    public void scanQueue(PendingMessageCallback callback) {
        float currentTime = GdxAI.getTimepiece().getTime();
        int queueSize = this.queue.size();
        for (int i = 0; i < queueSize; ++i) {
            Telegram telegram = this.queue.get(i);
            callback.report(telegram.getTimestamp() - currentTime, telegram.sender, telegram.receiver, telegram.message, telegram.extraInfo, telegram.returnReceiptStatus);
        }
    }

    private void discharge(Telegram telegram) {
        if (telegram.receiver != null) {
            if (!telegram.receiver.handleMessage(telegram) && this.debugEnabled) {
                GdxAI.getLogger().info(LOG_TAG, "Message " + telegram.message + " not handled");
            }
        } else {
            int handledCount = 0;
            Array<Telegraph> listeners = this.msgListeners.get(telegram.message);
            if (listeners != null) {
                for (int i = 0; i < listeners.size; ++i) {
                    if (!listeners.get(i).handleMessage(telegram)) continue;
                    ++handledCount;
                }
            }
            if (this.debugEnabled && handledCount == 0) {
                GdxAI.getLogger().info(LOG_TAG, "Message " + telegram.message + " not handled");
            }
        }
        if (telegram.returnReceiptStatus == 1) {
            telegram.receiver = telegram.sender;
            telegram.sender = this;
            telegram.returnReceiptStatus = 2;
            this.discharge(telegram);
        } else {
            pool.free(telegram);
        }
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        return false;
    }

    public static interface PendingMessageCallback {
        public void report(float var1, Telegraph var2, Telegraph var3, int var4, Object var5, int var6);
    }

}

