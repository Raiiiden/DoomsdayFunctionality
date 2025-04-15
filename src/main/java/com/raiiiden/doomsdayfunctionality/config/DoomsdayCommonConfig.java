package com.raiiiden.doomsdayfunctionality.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class DoomsdayCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue LOOT_RESTOCK_ENABLED;
    public static final ForgeConfigSpec.IntValue ATM_REFRESH_DAYS;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> BLOCK_ENTITY_ENABLED;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> LOOT_ENABLED;

    static {
        BUILDER.push("Loot Refresh Settings");

        LOOT_RESTOCK_ENABLED = BUILDER
                .comment("Enable loot restocking every X days")
                .define("lootRestockEnabled", true);

        ATM_REFRESH_DAYS = BUILDER
                .comment("Number of Minecraft days between loot refreshes")
                .defineInRange("atmRefreshDays", 1, 1, 365);

        BUILDER.pop();

        BUILDER.push("Block Entity Support");

        BLOCK_ENTITY_ENABLED = BUILDER
                .comment("List of block IDs that should become Doomsday block entities")
                .defineListAllowEmpty(
                        "blockEntityEnabled",
                        List.of("doomsdaydecoration:atm", "doomsdaydecoration:atm_2"),
                        obj -> obj instanceof String
                );

        BUILDER.pop();

        BUILDER.push("Lootable Support");

        LOOT_ENABLED = BUILDER
                .comment("List of block IDs that can generate loot")
                .defineListAllowEmpty(
                        "lootEnabled",
                        List.of("doomsdaydecoration:atm", "doomsdaydecoration:atm_2"),
                        obj -> obj instanceof String
                );

        BUILDER.pop();
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();
}