package com.ysk.stepmove.util;
import com.ysk.stepmove.common.Result;
import com.ysk.stepmove.event.handler.HoverHandler;
import com.ysk.stepmove.event.tracker.HoverTracker;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class TeleportPlayer {
    private static final int MAX_BLOCK_DISTANCE = 100;
    private static final float TICK_PROGRESS = 1.0F;
    private static final float TELEPORT_OFFSET = 0.5f;
    /**
     * プレイヤーをテレポートさせるメソッド
     *
     * @param player テレポートさせるプレイヤー
     * @param world  テレポート先のワールド
     */
    public static Result<String> teleportPlayer(@NotNull ServerPlayerEntity player, @NotNull World world) {
        // 座標計算
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

        try {
            // ホバー状態のデタッチ
            HoverHandler.detach(player);
            // プレイヤーをテレポート
            player.requestTeleport(teleportPos.x, teleportPos.y, teleportPos.z);
            // ホバー状態をアタッチ
            HoverHandler.attach(player);
            // ホバー状態のトラッキングを開始
            HoverTracker.startTracking(player.getUuid(), teleportPos);
            // ホバー開始時のサウンドを再生
            SoundEffectPlayer.playTransportSound(player);

            return Result.success("Player teleported to: " + teleportPos);
        } catch (Exception e) {
            HoverHandler.deleteState(player);
            HoverTracker.stopTracking(player.getUuid());
            return Result.failure("Failed to teleport player: " + e.getMessage());
        }
    }
}
