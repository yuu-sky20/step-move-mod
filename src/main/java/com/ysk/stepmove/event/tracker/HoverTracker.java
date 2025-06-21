package com.ysk.stepmove.event.tracker;

import com.ysk.stepmove.event.model.HoverState;
import com.ysk.stepmove.event.util.SoundEffectPlayer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import net.minecraft.particle.DustColorTransitionParticleEffect;

import java.awt.*;
import java.util.*;

// TODO トラッキング処理と、副作用を持った状態管理を分離する

public class HoverTracker {
    // 高度を記録しておく（UUID → y座標）
    private static final Map<UUID, HoverState> hoveringPlayers = new HashMap<>();

    private static final DustColorTransitionParticleEffect particle = new DustColorTransitionParticleEffect(
            Color.MAGENTA.getRed(),
            Color.CYAN.getBlue(),
            1.0f
    );

    public static void tick(ServerWorld world) {
        Iterator<Map.Entry<UUID, HoverState>> it = hoveringPlayers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<UUID, HoverState> entry = it.next();
            UUID uuid = entry.getKey();
            HoverState state = entry.getValue();

            ServerPlayerEntity player = world.getServer().getPlayerManager().getPlayer(uuid);
            if (player == null || !player.isAlive()) {
                it.remove();
                continue;
            }

            if (state.isPlayerMoved(player.getPos())) {
                // TODO ホバー状態の確認
                // TODO 排他制御


                player.setNoGravity(false);
                player.velocityModified = true;
                it.remove();
                SoundEffectPlayer.playDetachSound(player);
                continue;
            }

            state.setLastPos(player.getPos());

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
        // TODO マネージャーでホバリングの状態確認

        player.setNoGravity(true);
        player.setVelocity(Vec3d.ZERO);
        player.setPos(player.getX(), player.getY(), player.getZ());
        player.fallDistance = 0.0F;
        player.velocityModified = true;
        SoundEffectPlayer.playTransportSound(player);

        hoveringPlayers.put(player.getUuid(), new HoverState(player.getPos()));
    }

}

