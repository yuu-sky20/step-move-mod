package com.ysk.stepmove.event.tracker.state;

public class SneakState {

    public enum State {
        IDLE, // 初期状態、スニーク解除中
        SNEAKING // スニーク中
    }

    private State sneakState;

    public SneakState() {
        this.sneakState = State.IDLE;
    }

    public boolean isSneakingState() {
        return this.sneakState == State.SNEAKING;
    }

    public boolean isIdleState() {
        return this.sneakState == State.IDLE;
    }

    public void setSneakingState() {
        this.sneakState = State.SNEAKING;
    }

    public void setIdleState() {
        this.sneakState = State.IDLE;
    }
}
