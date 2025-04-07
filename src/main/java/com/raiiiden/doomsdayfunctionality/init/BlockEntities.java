package com.raiiiden.doomsdayfunctionality.init;

import com.raiiiden.doomsdayfunctionality.Doomsday;
import com.raiiiden.doomsdayfunctionality.blockentity.DoomsdayBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Doomsday.MODID)
public class BlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Doomsday.MODID);

    // Use DeferredRegister with late binding to make sure blocks are registered first
    public static final RegistryObject<BlockEntityType<DoomsdayBlockEntity>> GENERIC =
            BLOCK_ENTITIES.register("generic", () -> {
                try {
                    // Try to get blocks by registry name
                    Block atm = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("doomsdaydecoration", "atm"));
                    Block atm2 = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("doomsdaydecoration", "atm_2"));

                    if (atm == null || atm2 == null) {
                        Doomsday.LOGGER.error("ATM blocks not found during BlockEntityType registration");
                        Doomsday.LOGGER.error("ATM: {}", atm);
                        Doomsday.LOGGER.error("ATM2: {}", atm2);
                        return BlockEntityType.Builder.<DoomsdayBlockEntity>of(DoomsdayBlockEntity::new)
                                .build(null);
                    }

                    Doomsday.LOGGER.info("Registering BlockEntityType for ATM blocks");
                    return BlockEntityType.Builder.of(DoomsdayBlockEntity::new, atm, atm2)
                            .build(null);
                } catch (Exception e) {
                    Doomsday.LOGGER.error("Error registering BlockEntityType", e);
                    return BlockEntityType.Builder.<DoomsdayBlockEntity>of(DoomsdayBlockEntity::new)
                            .build(null);
                }
            });

    // Register this DeferredRegister to the event bus
    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}