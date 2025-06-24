package com.ysk.stepmove.event.tracker.state;

import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.ReentrantLock;

public class HoverState extends LastPosState {
    private final ReentrantLock lock = new ReentrantLock();
    // プレイヤーの状態を表す列挙型
    public enum State {
        IDLE, // 初期状態、ホバー解除中
        CHANGING, // サービスなどで状態を変更中
        COMMIT_CHANGED, // コミットされた状態
        HOVERING // ホバー中に確定した状態
    }

    private State hoverState;

    public HoverState() {
        super();
        this.hoverState = State.IDLE;
    }

    public void setChangingState() {
        lock.lock();
        try {
            this.hoverState = State.CHANGING;
        } finally {
            lock.unlock();
        }
    }

    public void setCommitChanged() {
        lock.lock();
        try {
            if (getState() == State.CHANGING) {
                this.hoverState = State.COMMIT_CHANGED;
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean applyCommitChanged(@NotNull Vec3d playerPos) {
        lock.lock();
        try {
            if (getState() == State.COMMIT_CHANGED) {
                setLastPos(playerPos);
                this.hoverState = State.HOVERING;
                return true;
            }
        } finally {
            lock.unlock();
        }
        return false;
    }

    public void setIdleState() {
        lock.lock();
        try {
            this.hoverState = State.IDLE;
        } finally {
            lock.unlock();
        }
    }

    public boolean isHoveringState() {
        return getState() == State.HOVERING;
    }

    public boolean isChangingState() {
        return getState() == State.CHANGING;
    }

    public boolean isCommitChangedState() {
        return getState() == State.COMMIT_CHANGED;
    }

    public boolean isPlayerMovedOnHovering(Vec3d nowPos) {
        if (getState() != State.HOVERING) return false;
        return isPlayerMoved(nowPos);
    }

    // プレイヤーの現在の状態を取得
    private State getState() {
        return this.hoverState;
    }
}
