package com.ysk.stepmove.event.model;

import net.minecraft.util.math.Vec3d;

public class HoverState {
    private Vec3d lastPos;

    public void setLastPos(Vec3d lastPos) {
        this.lastPos = lastPos;
    }

    public HoverState(Vec3d lastPos) {
        this.lastPos = lastPos;
    }

    public boolean isPlayerMoved(Vec3d nowPos) {
        return !lastPos.equals(nowPos);
    }
}