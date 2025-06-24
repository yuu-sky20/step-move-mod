package com.ysk.stepmove.event.tracker.state;

import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class HoverState extends LastPosState {
    // プレイヤーの状態を表す列挙型
    public enum State {
        IDLE, // 初期状態、ホバー解除中
        HOVERING // ホバー中
    }

    private State hoverState;

    public HoverState() {
        super();
        this.hoverState = State.IDLE;
    }

    public void setHoveringState(@NotNull Vec3d nowPos) {
        this.hoverState = State.HOVERING;
        setLastPos(nowPos);
    }

    public void setIdleState() {
        this.hoverState = State.IDLE;
    }

    public boolean isHoveringState() {
        return getState() == State.HOVERING;
    }

    public boolean isPlayerMovedOnHovering(Vec3d nowPos) {
        if (getState() == State.IDLE) return false;
        return isPlayerMoved(nowPos);
    }

    // プレイヤーの現在の状態を取得
    private State getState() {
        return this.hoverState;
    }
}
