package com.raiiiden.doomsdayfunctionality.system;

import com.raiiiden.doomsdayfunctionality.Doomsday;
import com.raiiiden.doomsdayfunctionality.blockentity.DoomsdayBlockEntity;
import com.raiiiden.doomsdayfunctionality.config.DoomsdayCommonConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Doomsday.MODID)
public class RefreshHandler {

    private static long lastProcessedDay = -1;

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.level.isClientSide) return;
        if (!(event.level instanceof ServerLevel level)) return;

        long currentDayTime = level.getDayTime();
        long currentDay = currentDayTime / 24000L;
        int interval = DoomsdayCommonConfig.ATM_REFRESH_DAYS.get();

        Doomsday.LOGGER.debug("Tick: dayTime={}, currentDay={}, lastProcessedDay={}, interval={}",
                currentDayTime, currentDay, lastProcessedDay, interval);

        if (interval <= 0 || currentDay <= lastProcessedDay || currentDay % interval != 0) return;
        lastProcessedDay = currentDay;

        int refreshed = 0;
        int scanRadius = 128;

        for (int cx = -scanRadius; cx <= scanRadius; cx++) {
            for (int cz = -scanRadius; cz <= scanRadius; cz++) {
                ChunkAccess chunk = level.getChunk(cx, cz, ChunkStatus.FULL, false);
                if (!(chunk instanceof LevelChunk levelChunk)) continue;

                for (BlockEntity be : levelChunk.getBlockEntities().values()) {
                    if (be instanceof DoomsdayBlockEntity lootable) {
                        boolean isEmpty = true;
                        for (int i = 0; i < lootable.getLootHandler().getSlots(); i++) {
                            if (!lootable.getLootHandler().getStackInSlot(i).isEmpty()) {
                                isEmpty = false;
                                break;
                            }
                        }

                        if (lootable.isLooted() || isEmpty) {
                            lootable.setLooted(false);
                            lootable.setFilledFromLoot(false);
                            lootable.tryLoadLoot(null);
                            refreshed++;
                        }
                    }
                }
            }
        }

        if (refreshed > 0) {
            Doomsday.LOGGER.info("✔ Auto-refreshed loot in {} block entities (day {}, interval {} days)",
                    refreshed, currentDay, interval);
        } else {
            Doomsday.LOGGER.info("No lootable containers needed refresh on day {}", currentDay);
        }
    }
}