package com.ysk.stepmove.event.tracker.state;

import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class LastPosState {
    // 移動してないとみなす座標誤差範囲
    private static final double MAX_MOVE_DISTANCE = 0.1;
    // 最後に記録したプレイヤーの位置
    private Vec3d lastPos;
    // コンストラクタ
    public LastPosState() {
        this.lastPos = Vec3d.ZERO;
    }
    // 最終位置を更新
    public void setLastPos(Vec3d lastPos) {
        this.lastPos = lastPos;
    }
    // プレイヤーが移動したかどうか判定
    protected boolean isPlayerMoved(@NotNull Vec3d nowPos) {
        // 誤差の範囲内なら成功とする
        Vec3d delta = nowPos.subtract(lastPos);
        return !(delta.lengthSquared() < MAX_MOVE_DISTANCE);
    }
}