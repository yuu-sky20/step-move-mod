package com.ysk.stepmove.event.service;

import com.ysk.stepmove.common.Result;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

public interface TeleportService {
    /**
     * プレイヤーをテレポートさせるメソッド
     * @param world  テレポート先のワールド
     * @param player テレポートさせるプレイヤー
     * @return 成功結果の文字列をResult型でラップして返す
     */
    Result<String> teleportPlayer(@NotNull ServerWorld world, @NotNull ServerPlayerEntity player);
}
