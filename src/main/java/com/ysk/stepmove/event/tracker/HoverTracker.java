package com.ysk.stepmove.event.tracker;
import com.ysk.stepmove.event.model.PlayerPosState;
import com.ysk.stepmove.event.handler.HoverHandler;

import com.ysk.stepmove.util.SoundEffectPlayer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class HoverTracker {
    // ホバー状態のプレイヤーを管理（トラッキングのみ担当）
    private static final Map<UUID, PlayerPosState> trackingPlayers = new HashMap<>();

    // 毎tick呼ばれる処理
    public static void tick(ServerWorld world) {
        // プレイヤーごとに状態を確認
        Iterator<Map.Entry<UUID, PlayerPosState>> it = trackingPlayers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<UUID, PlayerPosState> entry = it.next();
            UUID uuid = entry.getKey();
            PlayerPosState playerPosState = entry.getValue();

            // プレイヤーが有効かどうか判定
            ServerPlayerEntity player = world.getServer().getPlayerManager().getPlayer(uuid);
            if (!isValidPlayer(player)) {
                // 無効なプレイヤーの処理
                handleInvalidPlayer(player);
                it.remove();
                continue;
            }

            // プレイヤーが移動したかどうか判定
            if (playerPosState.isPlayerMoved(player.getPos())) {
                // 移動時の処理
                handlePlayerMoved(player);
                continue;
            }

            // パーティクルを生成
            spawnParticle(world, player);

            // プレイヤーの状態を更新
            playerPosState.setLastPos(player.getPos());
        }
    }

    // トラッキング開始
    public static void startTracking(@NotNull UUID playerId, Vec3d lastPos) {
        trackingPlayers.put(playerId, new PlayerPosState(lastPos));
    }

    // トラッキング終了
    public static void stopTracking(@NotNull UUID playerId) {
        trackingPlayers.remove(playerId);
    }

    // プレイヤーが有効かどうか判定
    private static boolean isValidPlayer(ServerPlayerEntity player) {
        return player != null && player.isAlive();
    }

    // 無効なプレイヤーの処理
    private static void handleInvalidPlayer(ServerPlayerEntity player) {
        if (player == null) return;
        UUID playerId = player.getUuid();

        HoverHandler.deleteState(player);
        stopTracking(playerId);
    }

    // プレイヤーが移動した場合の処理
    private static void handlePlayerMoved(@NotNull ServerPlayerEntity player) {
        UUID playerId = player.getUuid();
        // ホバー状態を解除
        HoverHandler.detach(player);
        // トラッキングを停止
        stopTracking(playerId);
        // ホバー解除時のサウンドを再生
        SoundEffectPlayer.playDetachSound(player);
    }

    // ホバー中のパーティクルを生成
    private static void spawnParticle(ServerWorld world, @NotNull ServerPlayerEntity player) {
        if (!HoverHandler.isHovering(player.getUuid())) {
            return; // プレイヤーがホバー中でない場合は何もしない
        }
        HoverHandler.spawnHoverParticle(world, player);
    }
}
