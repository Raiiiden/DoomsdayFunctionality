package com.raiiiden.doomsdayfunctionality.client.screen;

import com.raiiiden.doomsdayfunctionality.menu.DoomsdayContainerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DoomsdayScreen extends AbstractContainerScreen<DoomsdayContainerMenu> {
    private static final ResourceLocation ATM_TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/shulker_box.png");

    public DoomsdayScreen(DoomsdayContainerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 222;
        this.inventoryLabelY = this.imageHeight - 150;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);

        int slotSize = 18;
        int startX = this.leftPos + 8;
        int startY = this.topPos + 18;

        for (int col = 0; col < 9; col++) {
            int x = startX + col * slotSize;
            int y = startY;
            guiGraphics.fill(x, y, x + slotSize, y + slotSize, 0xFF444444);
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(ATM_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}
