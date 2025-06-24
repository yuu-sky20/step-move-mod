package com.ysk.stepmove.event.notifier;

import com.ysk.stepmove.event.tracker.HoverTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class HoverTrackerNotifier {
    public static void notifyStartHovering(@NotNull ServerPlayerEntity player, @NotNull Vec3d playerPos) {
        UUID playerId = player.getUuid();
        HoverTracker.trackMarkAsHovering(playerId, playerPos);
    }
}
