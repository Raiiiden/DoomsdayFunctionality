package com.raiiiden.doomsdayfunctionality.menu;

import com.raiiiden.doomsdayfunctionality.blockentity.DoomsdayBlockEntity;
import com.raiiiden.doomsdayfunctionality.init.MenuTypeInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class DoomsdayContainerMenu extends AbstractContainerMenu {

    private final DoomsdayBlockEntity blockEntity;

    public DoomsdayContainerMenu(int id, Inventory playerInventory, DoomsdayBlockEntity be) {
        super(MenuTypeInit.DOOMSDAY_CONTAINER.get(), id);
        this.blockEntity = be;

        IItemHandler handler = be.getLootHandler();

        int startX = 8;
        int backpackStartY = 18;

        for (int col = 0; col < 9; col++) {
            this.addSlot(new SlotItemHandler(handler, col, startX + col * 18, backpackStartY) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false; // Disallow placing items into inv
                }
            });
        }

        int inventoryStartY = backpackStartY + 66;

        // Player inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, inventoryStartY + row * 18));
            }
        }

        // Hotbar
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, inventoryStartY + 58));
        }
    }

    public DoomsdayContainerMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, (DoomsdayBlockEntity) inv.player.level().getBlockEntity(buf.readBlockPos()));
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (slot == null || !slot.hasItem()) return ItemStack.EMPTY;

        ItemStack current = slot.getItem();
        ItemStack copy = current.copy();

        int containerSlots = 9;

        if (index < containerSlots) {
            if (!this.moveItemStackTo(current, containerSlots, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }

        if (current.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();

        return copy;
    }
}