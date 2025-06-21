package com.ysk.stepmove.event.model;

import net.minecraft.util.math.Vec3d;

public class PlayerPosState {
    // 最後に記録したプレイヤーの位置
    private Vec3d lastPos;

    // 最終位置を更新
    public void setLastPos(Vec3d lastPos) {
        this.lastPos = lastPos;
    }

    // コンストラクタ（初期位置を記録）
    public PlayerPosState(Vec3d lastPos) {
        this.lastPos = lastPos;
    }

    // プレイヤーが移動したかどうか判定
    public boolean isPlayerMoved(Vec3d nowPos) {
        return !lastPos.equals(nowPos);
    }
}