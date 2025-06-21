package com.ysk.stepmove.event.tracker;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.*;

public class SneakTracker {

    private static final Map<UUID, Boolean> players = new HashMap<>();

    public static void tick(ServerWorld world) {
        Iterator<Map.Entry<UUID, Boolean>> it = players.entrySet().iterator();

        for (ServerPlayerEntity player : world.getPlayers()) {
            players.put(player.getUuid(), false);
        }

        while (it.hasNext()) {
            Map.Entry<UUID, Boolean> entry = it.next();
            UUID uuid = entry.getKey();
            boolean wasSneaked = entry.getValue();

            ServerPlayerEntity player = world.getServer().getPlayerManager().getPlayer(uuid);

            if (player == null || !player.isAlive()) {
                it.remove();
                continue;
            }

            boolean isSneaking = Objects.requireNonNull(player).isSneaking();

            if (isSneaking) {
                if (!wasSneaked) {
                    entry.setValue(true);
                    HoverTracker.startHovering(player);
                }
            } else {
                entry.setValue(false);
            }
        }
    }
}
