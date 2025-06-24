package com.ysk.stepmove.event.notifier;

import com.ysk.stepmove.event.tracker.HoverTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class HoverTrackerNotifier {
    public static void notifyChanging(@NotNull ServerPlayerEntity player) {
        UUID playerId = player.getUuid();
        HoverTracker.toggleChangingState(playerId);
    }

    public static void notifyStartHovering(@NotNull ServerPlayerEntity player) {
        UUID playerId = player.getUuid();
        HoverTracker.toggleCommitChangedState(playerId);
    }
}
