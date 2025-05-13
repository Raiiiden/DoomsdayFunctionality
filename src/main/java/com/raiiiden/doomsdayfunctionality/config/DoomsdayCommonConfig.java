package com.raiiiden.doomsdayfunctionality.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class DoomsdayCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.IntValue LOOT_REFRESH_DAYS;

    static {
        LOOT_REFRESH_DAYS = BUILDER
                .comment("Number of Minecraft days between loot refreshes")
                .defineInRange("lootRefreshDays", 1, 1, 365);

    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();
}
