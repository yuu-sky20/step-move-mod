package com.ysk.stepmove.event.notifier;

import com.ysk.stepmove.event.tracker.HoverTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class HoverTrackerNotifier {
    public static void notifyStartHovering(@NotNull ServerPlayerEntity player) {
        UUID playerId = player.getUuid();
        Vec3d playerPos = player.getPos();
        HoverTracker.trackMarkAsHovering(player, playerId, playerPos);
    }
}
