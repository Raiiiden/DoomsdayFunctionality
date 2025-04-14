package com.raiiiden.doomsdayfunctionality.command;

import com.mojang.brigadier.CommandDispatcher;
import com.raiiiden.doomsdayfunctionality.blockentity.DoomsdayBlockEntity;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;

public class DoomsdayCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("refreshloot")
                .requires(source -> source.hasPermission(2))
                .executes(ctx -> {
                    ServerLevel level = ctx.getSource().getLevel();
                    BlockPos center = ctx.getSource().getPlayerOrException().blockPosition();
                    int radius = 2; // chunk radius = 2 -> scans 5x5 chunks = 80 block radius

                    int refreshed = 0;

                    for (int dx = -radius; dx <= radius; dx++) {
                        for (int dz = -radius; dz <= radius; dz++) {
                            ChunkPos chunkPos = new ChunkPos((center.getX() >> 4) + dx, (center.getZ() >> 4) + dz);
                            LevelChunk chunk = level.getChunk(chunkPos.x, chunkPos.z); // loads if present

                            for (BlockEntity be : chunk.getBlockEntities().values()) {
                                if (be instanceof DoomsdayBlockEntity atm) {
                                    atm.setLooted(false);
                                    atm.setFilledFromLoot(false); // ✅ Allow reloading
                                    atm.tryLoadLoot(ctx.getSource().getPlayerOrException());
                                    refreshed++;
                                }
                            }
                        }
                    }

                    Component msg = Component.literal("Refreshed loot in " + refreshed + " ATMs nearby.");
                    ctx.getSource().sendSuccess(() -> msg, true);
                    return refreshed;
                }));
    }
}