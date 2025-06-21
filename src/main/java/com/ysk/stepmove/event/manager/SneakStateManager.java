package com.ysk.stepmove.event.manager;

public class SneakStateManager {
    public enum State {
        IDLE, // 初期状態、スニーク解除中
        SNEAKING // スニーク中
    }

    private volatile State currentState;

    public SneakStateManager() {
        this.currentState = State.IDLE;
    }

    public synchronized State getState() {
        return this.currentState;
    }

    public synchronized boolean isSneaking() {
        return this.currentState == State.SNEAKING;
    }

    // スニーク状態に応じて状態遷移を行う
    public synchronized void updateState(boolean isSneakingNow) {
        if (isSneakingNow && currentState == State.IDLE) {
            currentState = State.SNEAKING;
        } else if (!isSneakingNow && currentState == State.SNEAKING) {
            currentState = State.IDLE;
        }
    }
}
