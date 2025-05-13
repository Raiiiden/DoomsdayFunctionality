package com.raiiiden.doomsdayfunctionality;

import com.raiiiden.doomsdayfunctionality.command.DoomsdayCommands;
import com.raiiiden.doomsdayfunctionality.config.DoomsdayCommonConfig;
import com.raiiiden.doomsdayfunctionality.init.BlockEntities;
import com.raiiiden.doomsdayfunctionality.init.MenuTypeInit;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Doomsday.MODID)
@Mod.EventBusSubscriber(modid = Doomsday.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Doomsday {
  public static final String MODID = "doomsday_functionality";
  public static final Logger LOGGER = LogManager.getLogger();

  public static long GLOBAL_FORCE_REFRESH_ID = 0;

  public Doomsday() {
    LOGGER.info("Initializing Doomsday Functionality");

    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    modEventBus.addListener(this::setup);

    BlockEntities.register(modEventBus);
    MenuTypeInit.register(modEventBus);
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DoomsdayCommonConfig.SPEC);

    MinecraftForge.EVENT_BUS.register(this);

    LOGGER.info("Doomsday Functionality initialized");
  }

  private void setup(final FMLCommonSetupEvent event) {
    LOGGER.info("Doomsday Functionality setup phase");

    event.enqueueWork(() -> {
      LOGGER.info("Checking for ATM blocks in registry...");
      var atm = net.minecraftforge.registries.ForgeRegistries.BLOCKS.getValue(
              new net.minecraft.resources.ResourceLocation("doomsdaydecoration", "atm"));
      var atm2 = net.minecraftforge.registries.ForgeRegistries.BLOCKS.getValue(
              new net.minecraft.resources.ResourceLocation("doomsdaydecoration", "atm_2"));

      LOGGER.info("ATM Block: {}", atm);
      LOGGER.info("ATM2 Block: {}", atm2);
    });
  }

  @SubscribeEvent
  public static void onRegisterCommands(RegisterCommandsEvent event) {
    DoomsdayCommands.register(event.getDispatcher());
  }
}