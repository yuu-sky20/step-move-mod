package com.ysk.stepmove.event.tracker;
import com.ysk.stepmove.common.Result;
import com.ysk.stepmove.util.TeleportPlayer;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

public class UseBookItemTracker {


    private static boolean shouldPass(@NotNull World world, PlayerEntity player, Hand hand) {
        if (world.isClient()) return true;
        ItemStack itemStack = player.getStackInHand(hand);
        if (!(itemStack.getItem() == Items.BOOK)) return true;
        return !(player instanceof ServerPlayerEntity);
    }

    public static void register() {
        // 本を持って右クリックされたときの挙動
        UseItemCallback.EVENT.register((PlayerEntity player, World world, Hand hand) -> {
            if (shouldPass(world, player, hand)) return ActionResult.PASS;

            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;

            // テレポート実行
            Result<String> result = TeleportPlayer.teleportPlayer(serverPlayer, world);
            // テレポートが成功したかどうかを確認
            if (result.isFailure()) {
                LoggerFactory.getLogger(UseBookItemTracker.class).error("Failed to teleport player: " + result.getErrorMessage());
                return ActionResult.FAIL;
            }
            return ActionResult.SUCCESS;
        });
    }
}