package com.raiiiden.doomsdayfunctionality.client;

import com.raiiiden.doomsdayfunctionality.client.screen.DoomsdayScreen;
import com.raiiiden.doomsdayfunctionality.init.MenuTypeInit;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = "doomsday_functionality", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DoomsdayClient {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(MenuTypeInit.DOOMSDAY_CONTAINER.get(), DoomsdayScreen::new);
        });
    }
}
