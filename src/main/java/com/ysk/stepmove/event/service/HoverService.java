package com.ysk.stepmove.event.service;

import com.ysk.stepmove.common.Result;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public interface HoverService {
    /**
     * プレイヤーの空中浮遊を開始する
     * @param player 空中浮遊させるプレイヤー
     * @return 成功結果の文字列をResult型でラップして返す
     */
    Result<String> startHovering(@NotNull ServerPlayerEntity player);

    /**
     * プレイヤーの空中浮遊を停止する
     * @param player 空中浮遊させるプレイヤー
     * @return 成功結果の文字列をResult型でラップして返す
     */
    Result<String> stopHovering(@NotNull ServerPlayerEntity player);
}
