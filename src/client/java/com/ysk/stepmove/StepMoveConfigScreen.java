package com.ysk.stepmove;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class StepMoveConfigScreen {

    // 設定画面を生成するメソッド
    public static Screen create(Screen parent) {
        // 設定画面のビルダーを作成
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("Step Move Config"));

        // 保存時の処理を設定
        builder.setSavingRunnable(() -> {
            StepMoveConfig.validateSettings();
            StepMoveConfig.save();
        });

        // エントリービルダーを取得
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        // キー設定カテゴリを作成
        ConfigCategory keyCategory = builder.getOrCreateCategory(Text.literal("Key Settings"));
        // 一般設定カテゴリを作成
        ConfigCategory generalCategory = builder.getOrCreateCategory(Text.literal("General Settings"));

        // Wキーの設定項目を追加
        keyCategory.addEntry(entryBuilder
                .startBooleanToggle(Text.literal("Enable Step W"), StepMoveConfig.KEY_STEP_CONFIGS.stepW.enabled)
                .setDefaultValue(true)
                .setSaveConsumer(val -> StepMoveConfig.KEY_STEP_CONFIGS.stepW.enabled = val)
                .build());
        keyCategory.addEntry(entryBuilder
                .startDoubleField(Text.literal("Step Strength W"), StepMoveConfig.KEY_STEP_CONFIGS.stepW.strength)
                .setMin(0.0).setMax(10.0)
                .setDefaultValue(2.0)
                .setSaveConsumer(newValue -> StepMoveConfig.KEY_STEP_CONFIGS.stepW.strength = newValue)
                .build());

        // Aキーの設定項目を追加
        keyCategory.addEntry(entryBuilder
                .startBooleanToggle(Text.literal("Enable Step A"), StepMoveConfig.KEY_STEP_CONFIGS.stepA.enabled)
                .setDefaultValue(true)
                .setSaveConsumer(val -> StepMoveConfig.KEY_STEP_CONFIGS.stepA.enabled = val)
                .build());
        keyCategory.addEntry(entryBuilder
                .startDoubleField(Text.literal("Step Strength A"), StepMoveConfig.KEY_STEP_CONFIGS.stepA.strength)
                .setMin(0.0).setMax(10.0)
                .setDefaultValue(2.0)
                .setSaveConsumer(newValue -> StepMoveConfig.KEY_STEP_CONFIGS.stepA.strength = newValue)
                .build());

        // Sキーの設定項目を追加
        keyCategory.addEntry(entryBuilder
                .startBooleanToggle(Text.literal("Enable Step S"), StepMoveConfig.KEY_STEP_CONFIGS.stepS.enabled)
                .setDefaultValue(true)
                .setSaveConsumer(val -> StepMoveConfig.KEY_STEP_CONFIGS.stepS.enabled = val)
                .build());
        keyCategory.addEntry(entryBuilder
                .startDoubleField(Text.literal("Step Strength S"), StepMoveConfig.KEY_STEP_CONFIGS.stepS.strength)
                .setMin(0.0).setMax(10.0)
                .setDefaultValue(2.0)
                .setSaveConsumer(newValue -> StepMoveConfig.KEY_STEP_CONFIGS.stepS.strength = newValue)
                .build());

        // Dキーの設定項目を追加
        keyCategory.addEntry(entryBuilder
                .startBooleanToggle(Text.literal("Enable Step D"), StepMoveConfig.KEY_STEP_CONFIGS.stepD.enabled)
                .setDefaultValue(true)
                .setSaveConsumer(val -> StepMoveConfig.KEY_STEP_CONFIGS.stepD.enabled = val)
                .build());
        keyCategory.addEntry(entryBuilder
                .startDoubleField(Text.literal("Step Strength D"), StepMoveConfig.KEY_STEP_CONFIGS.stepD.strength)
                .setMin(0.0).setMax(10.0)
                .setDefaultValue(2.0)
                .setSaveConsumer(newValue -> StepMoveConfig.KEY_STEP_CONFIGS.stepD.strength = newValue)
                .build());

        // ダブルタップのタイムアウト設定
        generalCategory.addEntry(entryBuilder
                .startIntField(Text.literal("Double Tap Timeout (ms)"), (int) StepMoveConfig.DOUBLE_TAP_TIMEOUT)
                .setMin(100).setMax(1000)
                .setDefaultValue(300)
                .setSaveConsumer(newValue -> StepMoveConfig.DOUBLE_TAP_TIMEOUT = newValue)
                .build());

        // ホールド防止時間の設定
        generalCategory.addEntry(entryBuilder
                .startIntField(Text.literal("Hold Prevention Time (ms)"), (int) StepMoveConfig.HOLD_PREVENTION_TIME)
                .setMin(50).setMax(500)
                .setDefaultValue(100)
                .setSaveConsumer(newValue -> StepMoveConfig.HOLD_PREVENTION_TIME = newValue)
                .build());

        // スニーク中無効化設定
        generalCategory.addEntry(entryBuilder
                .startBooleanToggle(Text.literal("Disable While Sneaking"), StepMoveConfig.DISABLE_WHILE_SNEAKING)
                .setDefaultValue(true)
                .setSaveConsumer(val -> StepMoveConfig.DISABLE_WHILE_SNEAKING = val)
                .build());

        // 空中時無効化設定
        generalCategory.addEntry(entryBuilder
                .startBooleanToggle(Text.literal("Disable In Air"), StepMoveConfig.DISABLE_IN_AIR)
                .setDefaultValue(true)
                .setSaveConsumer(val -> StepMoveConfig.DISABLE_IN_AIR = val)
                .build());

        // クリエイティブ時有効化設定
        generalCategory.addEntry(entryBuilder
                .startBooleanToggle(Text.literal("Enable In Creative"), StepMoveConfig.ENABLE_IN_CREATIVE)
                .setDefaultValue(true)
                .setSaveConsumer(val -> StepMoveConfig.ENABLE_IN_CREATIVE = val)
                .build());

        // 設定画面を構築して返す
        return builder.build();
    }
}
