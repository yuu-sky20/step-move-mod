package com.ysk.stepmove.event.service;
import com.ysk.stepmove.common.Result;
import com.ysk.stepmove.event.notifier.HoverTrackerNotifier;
import com.ysk.stepmove.event.util.Physics;
import com.ysk.stepmove.event.util.SoundEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class PlayerHoverService implements HoverService {
    @Override
    public Result<String> startHovering(@NotNull ServerPlayerEntity player) {
        Result<String> result = Physics.startHovering(player);
        HoverTrackerNotifier.notifyStartHovering(player);
        SoundEffect.playTeleportSound(player);
        return result;
    }

    @Override
    public Result<String> stopHovering(@NotNull ServerPlayerEntity player) {
        Result<String> result = Physics.stopHovering(player);
        SoundEffect.playDetachSound(player);
        return result;
    }
}
