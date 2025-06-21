package com.ysk.stepmove.event.handler;

import com.ysk.stepmove.event.manager.HoverStateManager;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.UUID;

// ホバー状態の管理やパーティクル生成を行うハンドラークラス
public class HoverHandler {
    // ホバー状態を管理するマネージャのインスタンス
    private static final HoverStateManager hoverStateManager = new HoverStateManager();

    // パーティクルエフェクトの定義
    private static final DustColorTransitionParticleEffect particle = new DustColorTransitionParticleEffect(
            Color.MAGENTA.getRed(),
            Color.CYAN.getBlue(),
            1.0f
    );

    // プレイヤーをホバー状態にする
    public static void attach(@NotNull ServerPlayerEntity player) {
        UUID playerId = player.getUuid();
        // 既にホバー中なら何もしない
        if (hoverStateManager.isHovering(playerId)) return;

        // プレイヤー座標の停止処理
        player.fallDistance = 0.0F;
        player.setNoGravity(true);
        player.setVelocity(Vec3d.ZERO);
        player.velocityModified = true;

        // ホバー状態のアタッチ
        hoverStateManager.attach(playerId);
    }

    // プレイヤーをホバー状態から解除する
    public static void detach(@NotNull ServerPlayerEntity player) {
        UUID playerId = player.getUuid();
        // プレイヤー座標の停止処理を解除
        player.fallDistance = 0.0F;
        player.setNoGravity(false);
        player.velocityModified = true;

        // ホバー状態のデタッチ
        hoverStateManager.detach(playerId);
    }

    // プレイヤーの状態を削除する
    public static void deleteState(ServerPlayerEntity player) {
        if (player == null) return;
        UUID playerId = player.getUuid();
        // プレイヤー座標の停止処理を解除
        player.fallDistance = 0.0F;
        player.setNoGravity(false);
        player.velocityModified = true;

        hoverStateManager.deleteState(playerId);
    }

    // プレイヤーがホバー中かどうかを判定する
    public static boolean isHovering(UUID playerId) {
        return hoverStateManager.isHovering(playerId);
    }

    // ホバー中のパーティクルを生成する
    public static void spawnHoverParticle(@NotNull ServerWorld world, @NotNull ServerPlayerEntity player) {
        world.spawnParticles(
                particle,
                player.getX(), player.getY() - 0.25, player.getZ(),
                10,
                0.4, 0.12, 0.4,
                1.50
        );
    }
}
