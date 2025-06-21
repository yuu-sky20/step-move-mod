package com.ysk.stepmove.event.tracker;

import com.ysk.stepmove.event.manager.SneakStateManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SneakTracker {

    // プレイヤーUUIDとスニーク状態管理インスタンスのマップ
    private static final Map<UUID, SneakStateManager> players = new HashMap<>();

    // 毎tick呼び出されるメソッド
    public static void tick(@NotNull ServerWorld world) {
        removeInvalidPlayers(world);
        addNewPlayers(world);
        updateSneakStates(world);
    }

    // ワールドに存在しない/死亡したプレイヤーをマップから削除
    private static void removeInvalidPlayers(ServerWorld world) {
        Iterator<Map.Entry<UUID, SneakStateManager>> it = players.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<UUID, SneakStateManager> entry = it.next();
            ServerPlayerEntity player = world.getServer().getPlayerManager().getPlayer(entry.getKey());
            if (player == null || !player.isAlive()) {
                it.remove();
            }
        }
    }

    // 新規プレイヤーをマップに追加
    private static void addNewPlayers(ServerWorld world) {
        for (ServerPlayerEntity player : world.getPlayers()) {
            players.computeIfAbsent(player.getUuid(), k -> new SneakStateManager());
        }
    }

    // プレイヤーごとにスニーク状態を更新
    private static void updateSneakStates(ServerWorld world) {
        for (ServerPlayerEntity player : world.getPlayers()) {
            SneakStateManager stateManager = players.get(player.getUuid());
            if (stateManager == null) continue;
            boolean isSneaking = player.isSneaking();
            SneakStateManager.State prevState = stateManager.getState();
            stateManager.updateState(isSneaking);
            // スニーク開始時の処理
            if (prevState == SneakStateManager.State.IDLE && stateManager.isSneaking()) {
                HoverTracker.startHovering(player);
            }
            // スニーク解除時の追加処理が必要ならここに記述
        }
    }
}
