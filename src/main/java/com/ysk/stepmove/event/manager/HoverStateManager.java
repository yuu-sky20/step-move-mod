package com.ysk.stepmove.event.manager;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class HoverStateManager {
    // プレイヤーの状態を表す列挙型
    public enum State {
        IDLE, // 初期状態、ホバー解除中
        HOVERING // ホバー中
    }

    // プレイヤーごとの状態管理
    private final Map<UUID, State> playerStates = new ConcurrentHashMap<>();

    // プレイヤーの現在の状態を取得
    private State getState(UUID playerId) {
        return playerStates.getOrDefault(playerId, State.IDLE);
    }

    // プレイヤーがホバー中か判定
    public boolean isHovering(UUID playerId) {
        return getState(playerId) == State.HOVERING;
    }

    // プレイヤーの状態をアタッチ
    public void attach(UUID playerId) {
        playerStates.putIfAbsent(playerId, State.HOVERING);
    }

    // プレイヤーの状態をデタッチ
    public void detach(UUID playerId) {
        playerStates.putIfAbsent(playerId, State.IDLE);
    }

    // プレイヤーの状態を削除
    public void deleteState(UUID playerId) {
        playerStates.remove(playerId);
    }
}
