package com.ysk.stepmove.event.util;

import com.ysk.stepmove.common.Result;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class Physics {
    private static final int MAX_BLOCK_DISTANCE = 100;
    private static final float TICK_PROGRESS = 1.0F;
    private static final float TELEPORT_OFFSET = 0.5f;

    public static Result<Vec3d> teleportPlayer(@NotNull World world, @NotNull ServerPlayerEntity player) {
        try {
            Vec3d teleportPos = calcTeleportPos(world, player).getData();
            player.requestTeleport(teleportPos.x, teleportPos.y, teleportPos.z);
            return Result.success(teleportPos);
        } catch (Exception e) {
            return Result.failure("Failed to teleport player: " + e.getMessage());
        }
    }

    public static Result<Vec3d> startHovering(@NotNull ServerPlayerEntity player) {
        try {
            player.fallDistance = 0.0F;
            player.setNoGravity(true);
            player.setVelocity(Vec3d.ZERO);
            player.velocityModified = true;
            Vec3d playerPos = player.getPos();
            return Result.success(playerPos);
        } catch (Exception e) {
            return Result.failure("Failed to start player hovering: " + e.getMessage());
        }
    }

    public static Result<String> stopHovering(@NotNull ServerPlayerEntity player) {
        try {
            player.fallDistance = 0.0F;
            player.setNoGravity(false);
            return Result.success("Success to stop player hovering.");
        } catch (Exception e) {
            return Result.failure("Failed to stop player hovering: " + e.getMessage());
        }
    }

    /**
     * テレポート先の空間座標を計算するメソッド
     * @param world テレポート先のワールド
     * @param player テレポートするプレイヤー
     * @return テレポート先の空間座標
     */
    private static Result<Vec3d> calcTeleportPos(@NotNull World world, @NotNull ServerPlayerEntity player) {
        try {
            Vec3d start = player.getEyePos();
            Vec3d direction = player.getRotationVec(TICK_PROGRESS);
            Vec3d end = start.add(direction.multiply(MAX_BLOCK_DISTANCE));
            BlockHitResult hitResult = world.raycast(new RaycastContext(
                    start, end,
                    RaycastContext.ShapeType.COLLIDER,
                    RaycastContext.FluidHandling.NONE,
                    player
            ));
            Vec3d teleportPos = hitResult.getPos().subtract(direction.multiply(TELEPORT_OFFSET));
            return Result.success(teleportPos);
        } catch (Exception e) {
            return Result.failure("Failed to calculate teleport pos: " + e.getMessage());
        }
    }
}
