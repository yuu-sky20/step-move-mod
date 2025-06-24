package com.ysk.stepmove.event.tracker;

import com.ysk.stepmove.common.Pair;
import com.ysk.stepmove.common.Result;
import com.ysk.stepmove.event.handler.HoverHandler;
import com.ysk.stepmove.event.tracker.state.HoverState;
import com.ysk.stepmove.event.tracker.common.ValidateTrackPlayer;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class HoverTracker {
    // ホバー状態のプレイヤーを管理
    private static final Map<UUID, HoverState> trackingPlayers = new ConcurrentHashMap<>();
    private static final Logger LOGGER = Logger.getLogger(HoverTracker.class.getName());

    /**
     * 毎tickごとにサーバーワールド内のプレイヤーのホバー状態をトラッキング
     * @param world トラッキング対象のワールド
     */
    public static void tick(@NotNull ServerWorld world) {
        addNewPlayers(world);
        // サーバー内にアクティブなプレイヤーがいればトラッキング
        for (Map.Entry<UUID, HoverState> trackedPlayer : trackingPlayers.entrySet()) {

            Result<Pair<ServerPlayerEntity, UUID>> result= ValidateTrackPlayer.validateTrackedPlayer(world, trackedPlayer);
            if (result.isFailure()) {
                removeInvalidPlayer(trackedPlayer.getKey());
                continue;
            }

            Pair<ServerPlayerEntity, UUID> resultPair = result.getData();
            ServerPlayerEntity player = resultPair.getKey();
            UUID playerId = resultPair.getValue();
            HoverState hoverState = trackedPlayer.getValue();

            updatePlayerStatusOnHovering(world, player, hoverState);

            processPlayerMovedOnHovering(player, playerId, hoverState);
        }
    }

    /**
     * 新規プレイヤーをセットに追加
     * @param world 対象のサーバーワールド
     */
    private static void addNewPlayers(@NotNull ServerWorld world) {
        for (var player : world.getPlayers()) {
            trackingPlayers.putIfAbsent(player.getUuid(), new HoverState());
        }
    }

    /**
     * 不正なプレイヤーをトラッキングから除外
     * @param playerId 除外対象のプレイヤーID
     */
    private static void removeInvalidPlayer(@NotNull UUID playerId) {
        trackingPlayers.remove(playerId);
    }

    /**
     * プレイヤーをホバー状態としてマーキング
     * @param player トラッキング対象のプレイヤー
     * @param playerId トラッキング対象のプレイヤーID
     * @param playerNowPos トラッキング対象のプレイヤーの現在の空間座標
      */
    public static void trackMarkAsHovering(@NotNull ServerPlayerEntity player, @NotNull UUID playerId, Vec3d playerNowPos) {
        HoverState hoverState = trackingPlayers.get(playerId);
        hoverState.setHoveringState(playerNowPos);
        trackingPlayers.put(playerId, hoverState);

        LOGGER.info("Tracker mark as hovering player: " + playerId + ", " + playerNowPos );

        Result<String> result = HoverHandler.startHovering(player, playerId);
        if (result.isFailure()) {
            LOGGER.warning(result.getErrorMessage());
        } else {
            LOGGER.info(result.getData());
        }
    }

    /**
     * プレイヤーをアイドル状態としてマーキング
     * @param playerId トラッキング対象のプレイヤー
     */
    private static void trackMarkAsIdle(@NotNull UUID playerId) {
        HoverState hoverState = trackingPlayers.get(playerId);
        hoverState.setIdleState();
        trackingPlayers.put(playerId, hoverState);
    }

    /**
     * ホバリング中に毎回行われる更新処理
     * @param world トラッキング対象のワールド
     * @param player トラッキング対象のプレイヤー
     * @param hoverState プレイヤーの浮遊状態
     */
    private static void updatePlayerStatusOnHovering(@NotNull ServerWorld world, @NotNull ServerPlayerEntity player, @NotNull HoverState hoverState) {
        if (hoverState.isHoveringState()) {
            HoverHandler.updatePlayerStatusOnHovering(world, player);
        }
    }

    /**
     * 浮遊状態のプレイヤーが動いた際の処理
     * @param player トラッキング対象のプレイヤー
     * @param playerId トラッキング対象のプレイヤーID
     * @param hoverState プレイヤーの浮遊状態
     */
    private static void processPlayerMovedOnHovering(@NotNull ServerPlayerEntity player, @NotNull UUID playerId, @NotNull HoverState hoverState) {
        // プレイヤーの現在位置を取得
        Vec3d playerPos = player.getPos();

        if (hoverState.isPlayerMovedOnHovering(playerPos)) {
            Result<String> result = tryStopHovering(player, playerId);
            if (result.isFailure()) {
                LOGGER.warning(result.getErrorMessage());
            } else {
                LOGGER.info(result.getData());
            }
        }
    }

    /**
     * プレイヤーの浮遊状態を解除する
     * @param player トラッキング対象のプレイヤー
     * @param playerId トラッキング対象のプレイヤーID
     */
    private static Result<String> tryStopHovering(@NotNull ServerPlayerEntity player, @NotNull UUID playerId) {
        try {
            trackMarkAsIdle(playerId);
            Result<String> result = HoverHandler.stopHovering(player, playerId);
            return Result.success("Stop hovering: " + result.getData());
        } catch (Exception e) {
            return Result.failure("Failed to stop hovering: " + e.getMessage());
        }
    }
}
