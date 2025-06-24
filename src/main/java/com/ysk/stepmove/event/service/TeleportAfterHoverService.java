package com.ysk.stepmove.event.service;

import com.ysk.stepmove.common.Result;
import com.ysk.stepmove.event.util.Physics;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

public class TeleportAfterHoverService implements TeleportService {
    private static final HoverService hoverService = new PlayerHoverService();
    @Override
    public Result<String> teleportPlayer(@NotNull ServerWorld world, @NotNull ServerPlayerEntity player) {
        Result<String> result = Physics.teleportPlayer(world, player);
        hoverService.startHovering(player);
        return result;
    }
}
