package com.raiiiden.doomsdayfunctionality.init;

import com.raiiiden.doomsdayfunctionality.Doomsday;
import com.raiiiden.doomsdayfunctionality.menu.DoomsdayContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuTypeInit {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, Doomsday.MODID);

    public static final RegistryObject<MenuType<DoomsdayContainerMenu>> DOOMSDAY_CONTAINER =
            MENUS.register("doomsday_container",
                    () -> IForgeMenuType.create(DoomsdayContainerMenu::new));

    public static void register(IEventBus bus) {
        MENUS.register(bus);
    }
}
