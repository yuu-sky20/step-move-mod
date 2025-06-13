package com.ysk.stepmove;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class StepMoveConfigScreen {

    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("Step Move Config"));

        builder.setSavingRunnable(() -> {
            StepMoveConfig.validateSettings();
            StepMoveConfig.save();
        });

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory category = builder.getOrCreateCategory(Text.literal("Settings"));

        category.addEntry(entryBuilder
                .startDoubleField(Text.literal("Step Strength"), StepMoveConfig.STEP_STRENGTH)
                .setMin(0.0).setMax(10.0)
                .setDefaultValue(2.0)
                .setSaveConsumer(newValue -> StepMoveConfig.STEP_STRENGTH = newValue)
                .build());

        category.addEntry(entryBuilder
                .startIntField(Text.literal("Double Tap Timeout (ms)"), (int) StepMoveConfig.DOUBLE_TAP_TIMEOUT)
                .setMin(100).setMax(1000)
                .setDefaultValue(300)
                .setSaveConsumer(newValue -> StepMoveConfig.DOUBLE_TAP_TIMEOUT = newValue)
                .build());

        category.addEntry(entryBuilder
                .startIntField(Text.literal("Hold Prevention Time (ms)"), (int) StepMoveConfig.HOLD_PREVENTION_TIME)
                .setMin(50).setMax(500)
                .setDefaultValue(100)
                .setSaveConsumer(newValue -> StepMoveConfig.HOLD_PREVENTION_TIME = newValue)
                .build());

        category.addEntry(entryBuilder
                .startBooleanToggle(Text.literal("Disable While Sneaking"), StepMoveConfig.DISABLE_WHILE_SNEAKING)
                .setDefaultValue(true)
                .setSaveConsumer(val -> StepMoveConfig.DISABLE_WHILE_SNEAKING = val)
                .build());

        category.addEntry(entryBuilder
                .startBooleanToggle(Text.literal("Disable In Air"), StepMoveConfig.DISABLE_IN_AIR)
                .setDefaultValue(true)
                .setSaveConsumer(val -> StepMoveConfig.DISABLE_IN_AIR = val)
                .build());

        category.addEntry(entryBuilder
                .startBooleanToggle(Text.literal("Enable In Creative"), StepMoveConfig.ENABLE_IN_CREATIVE)
                .setDefaultValue(true)
                .setSaveConsumer(val -> StepMoveConfig.ENABLE_IN_CREATIVE = val)
                .build());

        return builder.build();
    }
}
