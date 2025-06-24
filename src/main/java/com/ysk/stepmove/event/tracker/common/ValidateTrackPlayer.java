package com.ysk.stepmove.event.tracker.common;

import com.ysk.stepmove.common.Pair;
import com.ysk.stepmove.common.Result;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class ValidateTrackPlayer {
    /**
     * トラッキング対象のプレイヤーの不正チェック
     * @param world プレイヤーの所属するワールド
     * @param trackedPlayer トラッキング対象のプレイヤー
     * @return 成功時にPlayerとPlayerIDをペアにして返す
     */
    public static <S> Result<Pair<ServerPlayerEntity, UUID>> validateTrackedPlayer(@NotNull ServerWorld world, Map.Entry<UUID, S> trackedPlayer) {
        // NULLチェック
        if (hasNullValue(trackedPlayer)) return Result.failure("Tracked player: entry is null.");

        UUID playerId = trackedPlayer.getKey();
        ServerPlayerEntity player = world.getServer().getPlayerManager().getPlayer(playerId);
        // Player Invalidチェック
        if (isInvalidPlayer(player)) {
            return Result.failure("Tracked player: player is invalid.");
        }
        return Result.success(new Pair<>(player, playerId));
    }

    private static <K, V> boolean hasNullValue(Map.Entry<K, V> map) {
        return map == null || map.getKey() == null || map.getValue() == null;
    }

    private static boolean isInvalidPlayer(ServerPlayerEntity player) {
        return player == null || !player.isAlive();
    }
}
