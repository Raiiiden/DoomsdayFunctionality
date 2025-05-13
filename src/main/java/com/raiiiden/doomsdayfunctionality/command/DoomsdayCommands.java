package com.raiiiden.doomsdayfunctionality.command;

import com.mojang.brigadier.CommandDispatcher;
import com.raiiiden.doomsdayfunctionality.Doomsday;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class DoomsdayCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("refreshloot")
                .requires(source -> source.hasPermission(2))
                .executes(ctx -> {
                    Doomsday.GLOBAL_FORCE_REFRESH_ID++;

                    ctx.getSource().sendSuccess(() ->
                            Component.literal("✔ Loot will refresh on next interaction (refresh ID " + Doomsday.GLOBAL_FORCE_REFRESH_ID + ")."), true);

                    return 1;
                }));
    }
}
