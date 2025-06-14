package com.ysk.stepmove;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * ステップ移動の設定を管理するクラス
 * 将来的に設定ファイル対応も可能
 */
public class StepMoveConfig {

    // ステップの強さとステップの有無を管理するクラス
    public static class KeyStepConfig {
        public boolean enabled;
        public double strength;

        public KeyStepConfig(boolean enabled, double strength) {
            this.enabled = enabled;
            this.strength = strength;
        }
    }

    // キーごとのステップ設定をまとめて管理するクラス
    public static class KeyStepConfigs {
        public KeyStepConfig stepW;
        public KeyStepConfig stepA;
        public KeyStepConfig stepS;
        public KeyStepConfig stepD;

        public KeyStepConfigs() {
            // デフォルト値で初期化
            this.stepW = new KeyStepConfig(true, 5.0);
            this.stepA = new KeyStepConfig(true, 4.0);
            this.stepS = new KeyStepConfig(true, 10.0);
            this.stepD = new KeyStepConfig(true, 4.0);
        }

        // すべてのキー設定を配列として取得するメソッド
        public KeyStepConfig[] getAllConfigs() {
            KeyStepConfig[] configs = new KeyStepConfig[]{stepW, stepA, stepS, stepD};
            if (configs[0] == null || configs[1] == null || configs[2] == null || configs[3] == null) {
                return new KeyStepConfig[0];
            }
            return configs;
        }
    }

    // StepMoveConfigクラス内での使用
    public static KeyStepConfigs KEY_STEP_CONFIGS = new KeyStepConfigs();

    // ダブルタップのタイムアウト（ミリ秒）
    public static long DOUBLE_TAP_TIMEOUT = 300;

    // 長押し防止時間（ミリ秒）
    public static long HOLD_PREVENTION_TIME = 100;

    // スニーク時の無効化
    public static boolean DISABLE_WHILE_SNEAKING = true;

    // 空中での無効化
    public static boolean DISABLE_IN_AIR = true;

    // クリエイティブモードでの動作
    public static boolean ENABLE_IN_CREATIVE = true;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = Paths.get("config/step_move_config.json");

    /**
     * 設定をデフォルト値にリセット
     */
    @SuppressWarnings("unused")
    public static void resetToDefaults() {
        KEY_STEP_CONFIGS = new KeyStepConfigs();
        DOUBLE_TAP_TIMEOUT = 300;
        HOLD_PREVENTION_TIME = 100;
        DISABLE_WHILE_SNEAKING = true;
        DISABLE_IN_AIR = true;
        ENABLE_IN_CREATIVE = true;
    }

    /**
     * 設定の妥当性チェック
     */
    public static void validateSettings() {
        // 各キーのステップ設定をチェック
        for (KeyStepConfig config : KEY_STEP_CONFIGS.getAllConfigs()) {
            if (config.strength < 0) config.strength = 0; // 負の値は無効
            if (config.strength > 10) config.strength = 10; // 上限を設定
        }

        if (DOUBLE_TAP_TIMEOUT < 100) DOUBLE_TAP_TIMEOUT = 100;
        if (DOUBLE_TAP_TIMEOUT > 1000) DOUBLE_TAP_TIMEOUT = 1000;
        if (HOLD_PREVENTION_TIME < 50) HOLD_PREVENTION_TIME = 50;
        if (HOLD_PREVENTION_TIME > 500) HOLD_PREVENTION_TIME = 500;
    }

    // json形式で設定を保存
    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());

            StepMoveConfigData data = new StepMoveConfigData(
                    KEY_STEP_CONFIGS,
                    DOUBLE_TAP_TIMEOUT,
                    HOLD_PREVENTION_TIME,
                    DISABLE_WHILE_SNEAKING,
                    DISABLE_IN_AIR,
                    ENABLE_IN_CREATIVE
            );

            try (Writer writer = new FileWriter(CONFIG_PATH.toFile())) {
                GSON.toJson(data, writer);
                System.out.println("設定ファイルを保存しました: " + CONFIG_PATH.toAbsolutePath());
            }

        } catch (IOException e) {
            System.err.println("設定の保存に失敗しました: " + e.getMessage());
        }
    }

    // json形式で設定を読み込み
    public static void load() {
        if (!Files.exists(CONFIG_PATH)) {
            System.out.println("設定ファイルが見つかりません。デフォルトを使用します。");
            return;
        }

        try (Reader reader = new FileReader(CONFIG_PATH.toFile())) {
            StepMoveConfigData data = GSON.fromJson(reader, StepMoveConfigData.class);

            KEY_STEP_CONFIGS = (data.keyStepConfigs != null) ? data.keyStepConfigs : new KeyStepConfigs();
            DOUBLE_TAP_TIMEOUT = data.doubleTapTimeout;
            HOLD_PREVENTION_TIME = data.holdPreventionTime;
            DISABLE_WHILE_SNEAKING = data.disableWhileSneaking;
            DISABLE_IN_AIR = data.disableInAir;
            ENABLE_IN_CREATIVE = data.enableInCreative;
            System.out.println("設定ファイルを読み込みました: " + CONFIG_PATH.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("設定ファイルの読み込みに失敗しました: " + e.getMessage());
        }
    }

    // 内部クラス：データ構造
    private static class StepMoveConfigData {
        KeyStepConfigs keyStepConfigs;
        long doubleTapTimeout;
        long holdPreventionTime;
        boolean disableWhileSneaking;
        boolean disableInAir;
        boolean enableInCreative;

        StepMoveConfigData(KeyStepConfigs keyStepConfigs, long doubleTapTimeout, long holdPreventionTime,
                           boolean disableWhileSneaking, boolean disableInAir, boolean enableInCreative) {
            this.keyStepConfigs = keyStepConfigs;
            this.doubleTapTimeout = doubleTapTimeout;
            this.holdPreventionTime = holdPreventionTime;
            this.disableWhileSneaking = disableWhileSneaking;
            this.disableInAir = disableInAir;
            this.enableInCreative = enableInCreative;
        }
    }}