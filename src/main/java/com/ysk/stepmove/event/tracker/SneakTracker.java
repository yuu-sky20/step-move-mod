package com.ysk.stepmove.event.tracker;

import com.ysk.stepmove.common.Pair;
import com.ysk.stepmove.common.Result;
import com.ysk.stepmove.event.service.HoverService;
import com.ysk.stepmove.event.service.PlayerHoverService;
import com.ysk.stepmove.event.tracker.common.ValidateTrackPlayer;
import com.ysk.stepmove.event.tracker.state.SneakState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SneakTracker {
    private static final HoverService hoverService = new PlayerHoverService();
    // トラッキング対象プレイヤー
    private static final Map<UUID, SneakState> trackingPlayers = new ConcurrentHashMap<>();

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

            processIdleToSneaking(player, playerId, sneakState);
            processSneakingToIdle(player, playerId, sneakState);
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

    // IDLE -> SNEAKING
    private static void processIdleToSneaking(@NotNull ServerPlayerEntity player, @NotNull UUID playerId, @NotNull SneakState sneakState) {
        boolean wasIdle = sneakState.isIdleState();
        boolean isSneaking = player.isSneaking();
        if (wasIdle && isSneaking) {
            sneakState.setSneakingState();
            trackingPlayers.put(playerId, sneakState);
            tryStartHovering(player);
        }
    }

    // SNEAKING -> IDLE
    private static void processSneakingToIdle(@NotNull ServerPlayerEntity player, @NotNull UUID playerId, @NotNull SneakState sneakState) {
        boolean wasSneaking = sneakState.isSneakingState();
        boolean isIdle = !player.isSneaking();
        if (wasSneaking && isIdle) {
            sneakState.setIdleState();
            trackingPlayers.put(playerId, sneakState);
        }
    }

    private static void tryStartHovering(@NotNull ServerPlayerEntity player) {
        if (canPlayerHover(player)) {
            hoverService.startHovering(player);
        }
    }

    private static boolean canPlayerHover(@NotNull ServerPlayerEntity player) {
        return !player.isOnGround()
                && !player.isSwimming()
                && !player.isInLava()
                && !player.isClimbing()
                && !(player.getAbilities().creativeMode && player.getAbilities().flying);
    }
}
