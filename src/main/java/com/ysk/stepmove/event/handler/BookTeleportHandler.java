package com.ysk.stepmove.event.handler;

import com.ysk.stepmove.event.tracker.HoverTracker;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class BookTeleportHandler {

    public static void register() {
        UseItemCallback.EVENT.register((PlayerEntity player, World world, Hand hand) -> {
            if (world.isClient()) return ActionResult.PASS;

            ItemStack itemStack = player.getStackInHand(hand);
            if (!(itemStack.getItem() == Items.BOOK)) return ActionResult.PASS;

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

            if (player instanceof ServerPlayerEntity serverPlayer) {
                serverPlayer.requestTeleport(teleportPos.x, teleportPos.y, teleportPos.z);
            }

            world.playSound(
                    null,
                    player.getBlockPos(),
                    SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.0F
            );

            if (player instanceof ServerPlayerEntity) {
                HoverTracker.startHovering((ServerPlayerEntity) player);
            }

            return ActionResult.SUCCESS;
        });
    }
}