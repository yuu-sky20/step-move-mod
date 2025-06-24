package com.ysk.stepmove.event.tracker;

import com.ysk.stepmove.common.Pair;
import com.ysk.stepmove.common.Result;
import com.ysk.stepmove.event.notifier.HoverTrackerNotifier;
import com.ysk.stepmove.event.tracker.common.ValidateTrackPlayer;
import com.ysk.stepmove.event.tracker.state.SneakState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class SneakTracker {
    // トラッキング対象プレイヤー
    private static final Map<UUID, SneakState> trackingPlayers = new ConcurrentHashMap<>();
    private static final Logger LOGGER = Logger.getLogger(SneakTracker.class.getName());
    /**
     * 毎tickごとにサーバーワールド内のプレイヤーのスニーク状態をトラッキング
     * @param world トラッキング対象のワールド
     */
    public static void tick(@NotNull ServerWorld world) {
        addNewPlayers(world);

        for (Map.Entry<UUID, SneakState> trackedPlayer : trackingPlayers.entrySet()) {
            Result<Pair<ServerPlayerEntity, UUID>> result= ValidateTrackPlayer.validateTrackedPlayer(world, trackedPlayer);
            if (result.isFailure()) {
                removeInvalidPlayer(trackedPlayer.getKey());
                continue;
            }

            Pair<ServerPlayerEntity, UUID> resultPair = result.getData();
            ServerPlayerEntity player = resultPair.getKey();
            UUID playerId = resultPair.getValue();
            SneakState sneakState = trackedPlayer.getValue();

            updatePlayerSneakState(player, playerId, sneakState);
        }
    }

    /**
     * 新規プレイヤーをセットに追加
     * @param world 対象のサーバーワールド
     */
    private static void addNewPlayers(@NotNull ServerWorld world) {
        for (var player : world.getPlayers()) {
            trackingPlayers.putIfAbsent(player.getUuid(), new SneakState());
        }
    }

    /**
     * 不正なプレイヤーをトラッキングから除外
     * @param playerId 除外対象のプレイヤーID
     */
    private static void removeInvalidPlayer(@NotNull UUID playerId) {
        trackingPlayers.remove(playerId);
    }

    private static void updatePlayerSneakState(@NotNull ServerPlayerEntity player, @NotNull UUID playerId, @NotNull SneakState sneakState) {
        boolean isSneaking = player.isSneaking();
        boolean wasSneaking = sneakState.isSneakingState();

        if (!wasSneaking && isSneaking) {
            sneakState.setSneakingState();
            trackingPlayers.put(playerId, sneakState);

            handleSneakStart(player, sneakState);
        } else if (wasSneaking && !isSneaking) {
            sneakState.setIdleState();
            trackingPlayers.put(playerId, sneakState);
        }
    }

    private static void handleSneakStart(@NotNull ServerPlayerEntity player, @NotNull SneakState sneakState) {
        if (sneakState.isIdleState()) {
            tryHoverPlayer(player);
        }
    }

    private static void tryHoverPlayer(@NotNull ServerPlayerEntity player) {
        Result<String> result = hoverPlayer(player);
        if (result.isFailure()) {
            LOGGER.warning("Failed to hover player: " + result.getErrorMessage());
        } else {
            LOGGER.info("Success to hover player: " + result.getData());
        }
    }

    private static Result<String> hoverPlayer(@NotNull ServerPlayerEntity player) {
        if (!canPlayerHover(player)) {
            return Result.success("The player is not in a state where hovering is possible.");
        }
        try {
            HoverTrackerNotifier.notifyStartHovering(player);
            return Result.success("Player hovered: " + player.getPos());
        } catch (Exception e) {
            return Result.failure("Failed to hover player: " + e.getMessage());
        }
    }

    private static boolean canPlayerHover(@NotNull ServerPlayerEntity player) {
        return !player.isOnGround()
                && !player.isSwimming()
                && !player.isInLava()
                && !player.isClimbing();
    }
}
