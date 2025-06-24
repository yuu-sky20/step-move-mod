package com.ysk.stepmove.event.util;

import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ParticleEffect {
    // パーティクルエフェクトの定義
    private static final DustColorTransitionParticleEffect particle = new DustColorTransitionParticleEffect(
            Color.MAGENTA.getRed(),
            Color.CYAN.getBlue(),
            1.0f
    );
    // ホバー中のパーティクルを生成
    public static void spawnParticle(@NotNull ServerWorld world, @NotNull ServerPlayerEntity player) {
        world.spawnParticles(
                particle,
                player.getX(), player.getY() - 0.25, player.getZ(),
                10,
                0.4, 0.12, 0.4,
                1.50
        );
    }
}
