package com.ysk.stepmove;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StepMoveClient implements ClientModInitializer {

    // キーの状態を管理する内部クラス
    private static class KeyState {
        public long lastPressTime = 0;
        public boolean wasPressed = false;
    }

    // 対象キーとその状態を保持
    private final Map<Integer, KeyState> keyStates = new HashMap<>();

    @Override
    public void onInitializeClient() {
        // 対象キーを初期化
        keyStates.put(GLFW.GLFW_KEY_W, new KeyState());
        keyStates.put(GLFW.GLFW_KEY_A, new KeyState());
        keyStates.put(GLFW.GLFW_KEY_S, new KeyState());
        keyStates.put(GLFW.GLFW_KEY_D, new KeyState());

        // 毎ティックごとにキー入力を監視
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null) return;

            // 各キーごとに状態をチェック
            for (int key : keyStates.keySet()) {
                processKeyInput(client, key, keyStates.get(key));
            }
        });

        // 設定の読み込み
        StepMoveConfig.load();
        // 設定の妥当性チェック
        StepMoveConfig.validateSettings();
    }

    // キー入力の処理を分離
    private void processKeyInput(MinecraftClient client, int key, KeyState state) {
        boolean isPressed = GLFW.glfwGetKey(client.getWindow().getHandle(), key) == GLFW.GLFW_PRESS;

        if (isPressed && !state.wasPressed) {
            long now = System.currentTimeMillis();

            // ダブルタップ検出 + 長押し防止
            if (isDoubleTap(now, state.lastPressTime)) {
                attemptStep(client, key);
                state.lastPressTime = 0;
            } else {
                state.lastPressTime = now;
            }
        }

        state.wasPressed = isPressed;
    }

    // ダブルタップ判定をメソッド化
    private boolean isDoubleTap(long now, long lastPressTime) {
        return now - lastPressTime > StepMoveConfig.HOLD_PREVENTION_TIME &&
               now - lastPressTime < StepMoveConfig.DOUBLE_TAP_TIMEOUT;
    }

private void attemptStep(MinecraftClient client, int key) {
    var player = client.player;

    // --- 条件チェック ---
    // スニーク中は無効
    if (StepMoveConfig.DISABLE_WHILE_SNEAKING && Objects.requireNonNull(player).isSneaking()) return;
    // 空中では無効
    if (StepMoveConfig.DISABLE_IN_AIR && !Objects.requireNonNull(player).isOnGround()) return;
    // クリエイティブモードでは無効
    if (!StepMoveConfig.ENABLE_IN_CREATIVE && Objects.requireNonNull(player).isCreative()) return;

    // --- 向きベクトル取得 ---
    Vec3d stepVec = getStepVector(player, key, 0); // デフォルト値は使用しないので0を渡す

    // プレイヤーに速度を加える
    if (stepVec.equals(Vec3d.ZERO)) {
        // ステップが無効な場合は何もしない
        return;
    }
    Objects.requireNonNull(player).addVelocity(stepVec.x, 0, stepVec.z);
    player.velocityModified = true;
}

private Vec3d getStepVector(net.minecraft.entity.player.PlayerEntity player, int key, double defaultPower) {
    Vec3d forward = Objects.requireNonNull(player).getRotationVecClient().normalize();
    Vec3d right = forward.crossProduct(new Vec3d(0, 1, 0)).normalize();
    
    StepMoveConfig.KeyStepConfig config = switch (key) {
        case GLFW.GLFW_KEY_W -> StepMoveConfig.KEY_STEP_CONFIGS.stepW;
        case GLFW.GLFW_KEY_S -> StepMoveConfig.KEY_STEP_CONFIGS.stepS;
        case GLFW.GLFW_KEY_D -> StepMoveConfig.KEY_STEP_CONFIGS.stepD;
        case GLFW.GLFW_KEY_A -> StepMoveConfig.KEY_STEP_CONFIGS.stepA;
        default -> null;
    };
    
    // configがnullまたはenabledがfalseの場合、ゼロベクトルを返す
    if (config == null || !config.enabled) {
        return Vec3d.ZERO;
    }

    // 有効な場合のみ、対応する方向への移動ベクトルを計算
    return switch (key) {
        case GLFW.GLFW_KEY_W -> forward.multiply(config.strength);
        case GLFW.GLFW_KEY_S -> forward.multiply(-config.strength);
        case GLFW.GLFW_KEY_D -> right.multiply(config.strength);
        case GLFW.GLFW_KEY_A -> right.multiply(-config.strength);
        default -> Vec3d.ZERO;
    };
}
}