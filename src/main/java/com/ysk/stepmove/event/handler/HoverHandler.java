package com.ysk.stepmove.event.handler;

import com.ysk.stepmove.common.Result;
import com.ysk.stepmove.event.service.HoverService;
import com.ysk.stepmove.event.service.PlayerHoverService;
import com.ysk.stepmove.event.util.ParticleEffect;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class HoverHandler {
    private static final HoverService hoverService = new PlayerHoverService();

    public static Result<String> stopHovering(@NotNull ServerPlayerEntity player, @NotNull UUID playerId) {
        try {
            Result<String> result = hoverService.stopHovering(player);
            return Result.success("Stop hovering player: " + playerId + ", " + result.getData());
        } catch (Exception e) {
            return Result.failure("Failed to stop hovering player: " + e.getMessage());
        }
    }

    public static void updatePlayerStatusOnHovering(@NotNull ServerWorld world, @NotNull ServerPlayerEntity player) {
        ParticleEffect.spawnParticle(world, player);
    }

}
