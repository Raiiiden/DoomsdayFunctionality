package com.raiiiden.doomsdayfunctionality.init;

import com.raiiiden.doomsdayfunctionality.Doomsday;
import com.raiiiden.doomsdayfunctionality.blockentity.DoomsdayBlockEntity;
import com.raiiiden.doomsdayfunctionality.util.LootableBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Doomsday.MODID)
public class BlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Doomsday.MODID);

    public static final RegistryObject<BlockEntityType<DoomsdayBlockEntity>> GENERIC =
            BLOCK_ENTITIES.register("generic", () -> {
                try {
                    List<Block> blocksToRegister = LootableBlocks.LOOTABLE_BLOCK_RLS.stream()
                            .map(ForgeRegistries.BLOCKS::getValue)
                            .filter(Objects::nonNull)
                            .collect(java.util.ArrayList::new, java.util.ArrayList::add, java.util.ArrayList::addAll);

                    if (blocksToRegister.isEmpty()) {
                        Doomsday.LOGGER.error("No valid blocks found for BlockEntityType registration. The generic block entity will not be associated with any blocks.");
                        return BlockEntityType.Builder.<DoomsdayBlockEntity>of(DoomsdayBlockEntity::new)
                                .build(null);
                    }

                    Doomsday.LOGGER.info("Registering BlockEntityType for {} blocks.", blocksToRegister.size());
                    return BlockEntityType.Builder.of(DoomsdayBlockEntity::new, blocksToRegister.toArray(new Block[0]))
                            .build(null);
                } catch (Exception e) {
                    Doomsday.LOGGER.error("Error registering BlockEntityType", e);
                    return BlockEntityType.Builder.<DoomsdayBlockEntity>of(DoomsdayBlockEntity::new)
                            .build(null);
                }
            });

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}