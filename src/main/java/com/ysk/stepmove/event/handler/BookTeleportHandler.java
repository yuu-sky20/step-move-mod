package com.ysk.stepmove.event.handler;

import com.ysk.stepmove.common.Result;

import com.ysk.stepmove.event.tracker.HoverTracker;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class BookTeleportHandler {

    public static void register() {
        UseItemCallback.EVENT.register((PlayerEntity player, World world, Hand hand) -> {
            if (shouldPass(world, player, hand)) return ActionResult.PASS;

            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            teleportPlayer(serverPlayer, world);

            if (isHoverStartFailure(serverPlayer)) {
                return ActionResult.FAIL;
            }
            return ActionResult.SUCCESS;
        });
    }

    private static boolean shouldPass(@NotNull World world, PlayerEntity player, Hand hand) {
        if (world.isClient()) return true;
        ItemStack itemStack = player.getStackInHand(hand);
        if (!(itemStack.getItem() == Items.BOOK)) return true;
        return !(player instanceof ServerPlayerEntity);
    }

    private static boolean isHoverStartFailure(ServerPlayerEntity serverPlayer) {
        Result<String> result = HoverTracker.startHovering(serverPlayer);
        return result.isFailure();
    }

    private static void teleportPlayer(@NotNull ServerPlayerEntity player, @NotNull World world) {
        Vec3d start = player.getEyePos();
        Vec3d direction = player.getRotationVec(1.0F);
        Vec3d end = start.add(direction.multiply(100));
        BlockHitResult hitResult = world.raycast(new RaycastContext(
                start, end,
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                player
        ));
        Vec3d teleportPos = hitResult.getPos().subtract(direction.multiply(0.5));

        HoverTracker.stopHovering(player.getUuid()); // ホバーを停止

        player.requestTeleport(teleportPos.x, teleportPos.y, teleportPos.z);
    }
}