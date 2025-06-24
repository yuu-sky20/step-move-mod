package com.ysk.stepmove.event.service;

import com.ysk.stepmove.common.Result;
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
        Result<Vec3d> result = Physics.teleportPlayer(world, player);
        Vec3d playerPos = result.getData();

        hoverService.startHovering(player);

        SoundEffect.playTeleportSound(player);
        return Result.success("Teleport player: " + playerPos + ", " + result.getData());
    }
}
