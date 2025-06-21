package com.ysk.stepmove.event.util;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.jetbrains.annotations.NotNull;

public class SoundEffectPlayer {
    public static void playTransportSound(@NotNull ServerPlayerEntity player) {
        ServerWorld world = (ServerWorld) player.getWorld();
        world.playSound(
                null,
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                SoundCategory.PLAYERS,
                1.0f, 1.0f
        );
    }
    public static void playDetachSound(@NotNull ServerPlayerEntity player) {
        ServerWorld world = (ServerWorld) player.getWorld();
        world.playSound(
                null,
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.UI_HUD_BUBBLE_POP,
                SoundCategory.PLAYERS,
                1.0f, 1.0f
        );
    }
}
