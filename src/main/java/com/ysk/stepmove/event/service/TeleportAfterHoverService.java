package com.ysk.stepmove.event.service;

import com.ysk.stepmove.common.Result;
import com.ysk.stepmove.event.notifier.HoverTrackerNotifier;
import com.ysk.stepmove.event.util.Physics;
import com.ysk.stepmove.event.util.SoundEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class TeleportAfterHoverService implements TeleportService {
    private static final HoverService hoverService = new PlayerHoverService();
    @Override
    public Result<String> teleportPlayer(@NotNull ServerWorld world, @NotNull ServerPlayerEntity player) {
        HoverTrackerNotifier.notifyChanging(player);
        try {
            Result<Vec3d> result = Physics.teleportPlayer(world, player);
            if (result.isFailure()) {
                throw new Exception("Failed to teleport player: " + result.getErrorMessage());
            }

        } catch (Exception e) {
            return Result.failure(e.getMessage());
        } finally {
            hoverService.startHovering(player);
        }

        SoundEffect.playTeleportSound(player);

        return Result.success("Success to teleport player.");
    }

    public Result<String> teleportPlayerAbove(@NotNull ServerPlayerEntity player) {
        HoverTrackerNotifier.notifyChanging(player);
        try {
            Result<Vec3d> result = Physics.teleportPlayerAbove(player);
            if (result.isFailure()) {
                throw new Exception("Failed to teleport player: " + result.getErrorMessage());
            }

        } catch (Exception e) {
            return Result.failure(e.getMessage());
        } finally {
            hoverService.startHovering(player);
        }

        SoundEffect.playTeleportSound(player);

        return Result.success("Success to teleport player.");
    }
}
