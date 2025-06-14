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

    // ステップの強度設定
    public static double STEP_STRENGTH = 8.0;

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
        STEP_STRENGTH = 2.0;
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
        if (STEP_STRENGTH < 0) STEP_STRENGTH = 0;
        if (STEP_STRENGTH > 10) STEP_STRENGTH = 10;
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
                    STEP_STRENGTH,
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

            STEP_STRENGTH = data.stepStrength;
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
        double stepStrength;
        long doubleTapTimeout;
        long holdPreventionTime;
        boolean disableWhileSneaking;
        boolean disableInAir;
        boolean enableInCreative;

        StepMoveConfigData(double stepStrength, long doubleTapTimeout, long holdPreventionTime,
                           boolean disableWhileSneaking, boolean disableInAir, boolean enableInCreative) {
            this.stepStrength = stepStrength;
            this.doubleTapTimeout = doubleTapTimeout;
            this.holdPreventionTime = holdPreventionTime;
            this.disableWhileSneaking = disableWhileSneaking;
            this.disableInAir = disableInAir;
            this.enableInCreative = enableInCreative;
        }
    }}
