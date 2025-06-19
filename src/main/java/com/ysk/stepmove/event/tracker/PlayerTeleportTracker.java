package com.ysk.stepmove.event.tracker;

import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import net.minecraft.particle.DustColorTransitionParticleEffect;

import java.awt.*;
import java.util.*;

public class PlayerTeleportTracker {
    // 高度を記録しておく（UUID → y座標）
    public static final Map<UUID, PlayerState> hoveringPlayers = new HashMap<>();

    private static final DustColorTransitionParticleEffect particle = new DustColorTransitionParticleEffect(
            Color.MAGENTA.getRed(),
            Color.CYAN.getBlue(),
            1.0f
    );

    public static void tick(ServerWorld world) {
        Iterator<Map.Entry<UUID, PlayerState>> it = hoveringPlayers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<UUID, PlayerState> entry = it.next();
            UUID uuid = entry.getKey();
            PlayerState state = entry.getValue();

            ServerPlayerEntity player = world.getServer().getPlayerManager().getPlayer(uuid);
            if (player == null || !player.isAlive()) {
                it.remove();
                continue;
            }

            if (player.isSwimming() || player.isClimbing() || state.isPlayerMoved(player.getPos())) {
                player.setNoGravity(false);
                player.velocityModified = true;
                it.remove();
                continue;
            }

            player.fallDistance = 0.0F;

            state.lastPos = player.getPos();

            world.spawnParticles(
                    particle,
                    player.getX(), player.getY() - 0.25, player.getZ(),
                    10,
                    0.4, 0.12, 0.4,
                    1.50
            );
        }
    }

    public static void startHovering(ServerPlayerEntity player) {
        player.setNoGravity(true);
        player.setVelocity(Vec3d.ZERO);
        player.velocityModified = true;
        hoveringPlayers.put(player.getUuid(), new PlayerState(player.getPos()));
    }

    private static class PlayerState {
        private Vec3d lastPos;
        public PlayerState(Vec3d lastPos) {
            this.lastPos = lastPos;
        }
        public boolean isPlayerMoved(Vec3d nowPos) {
            return !lastPos.equals(nowPos);
        }
    }
}

