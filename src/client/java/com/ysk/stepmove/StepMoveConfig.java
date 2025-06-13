package com.ysk.stepmove;

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

    /**
     * 設定をデフォルト値にリセット
     */
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
}