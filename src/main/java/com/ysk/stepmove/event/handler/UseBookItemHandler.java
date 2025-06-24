package com.ysk.stepmove.event.handler;
import com.ysk.stepmove.common.Result;
import com.ysk.stepmove.event.service.TeleportAfterHoverService;
import com.ysk.stepmove.event.service.TeleportService;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UseBookItemHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UseBookItemHandler.class);
    private static final TeleportService teleportService = new TeleportAfterHoverService();

    public static void register() {
        // 本を持って右クリックされたときの挙動
        UseItemCallback.EVENT.register((PlayerEntity player, World world, Hand hand) -> {
            if (shouldPass(world, player, hand)) return ActionResult.PASS;

            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            ServerWorld serverWorld = (ServerWorld) world;

            // テレポート実行
            Result<String> result = teleportService.teleportPlayer(serverWorld, serverPlayer);
            // テレポートが成功したかどうかを確認
            if (result.isFailure()) {
                LOGGER.error("Failed to call the service: " + result.getErrorMessage());
                return ActionResult.FAIL;
            }

            return ActionResult.SUCCESS;
        });
    }

    private static boolean shouldPass(@NotNull World world, PlayerEntity player, Hand hand) {
        if (world.isClient()) return true;
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.getItem() != Items.BOOK) return true;
        return !(player instanceof ServerPlayerEntity);
    }
}