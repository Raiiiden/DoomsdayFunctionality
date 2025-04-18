package com.raiiiden.doomsdayfunctionality.blockentity;

import com.raiiiden.doomsdayfunctionality.config.DoomsdayCommonConfig;
import com.raiiiden.doomsdayfunctionality.init.BlockEntities;
import com.raiiiden.doomsdayfunctionality.menu.DoomsdayContainerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class DoomsdayBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler lootHandler = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private boolean looted = false;
    private boolean filledFromLoot = false;
    private long lootSeed = 0L;
    private ResourceLocation lootTable = new ResourceLocation("doomsday_functionality", "chests/atm");

    public DoomsdayBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.GENERIC.get(), pos, state);
    }

    public ItemStackHandler getLootHandler() {
        return lootHandler;
    }

    public boolean isLooted() {
        return looted;
    }

    public void setLooted(boolean looted) {
        this.looted = looted;
    }

    public void setFilledFromLoot(boolean value) {
        this.filledFromLoot = value;
    }

    public void tryLoadLoot(Player player) {
        ResourceLocation id = ForgeRegistries.BLOCKS.getKey(getBlockState().getBlock());
        boolean lootAllowed = id != null && DoomsdayCommonConfig.LOOT_ENABLED.get().contains(id.toString());
        if (!lootAllowed) return;

        if (!filledFromLoot && level instanceof ServerLevel server && lootTable != null) {
            LootTable table = server.getServer().getLootData().getLootTable(lootTable);

            LootParams.Builder params = new LootParams.Builder(server)
                    .withParameter(LootContextParams.ORIGIN, this.getBlockPos().getCenter())
                    .withOptionalParameter(LootContextParams.THIS_ENTITY, player);

            List<ItemStack> items = table.getRandomItems(params.create(LootContextParamSets.CHEST));

            for (int i = 0; i < Math.min(lootHandler.getSlots(), items.size()); i++) {
                lootHandler.setStackInSlot(i, items.get(i));
            }

            filledFromLoot = true;
            looted = false;
            setChanged();
        }
    }

    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
        buf.writeBlockPos(getBlockPos());
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Loot", lootHandler.serializeNBT());
        tag.putBoolean("Looted", looted);
        tag.putBoolean("Filled", filledFromLoot);
        tag.putLong("Seed", lootSeed);
        if (lootTable != null) tag.putString("LootTable", lootTable.toString());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        lootHandler.deserializeNBT(tag.getCompound("Loot"));
        looted = tag.getBoolean("Looted");
        filledFromLoot = tag.getBoolean("Filled");
        lootSeed = tag.getLong("Seed");
        if (tag.contains("LootTable")) {
            lootTable = new ResourceLocation(tag.getString("LootTable"));
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        handleUpdateTag(pkt.getTag());
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("ATM");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        tryLoadLoot(player);
        return new DoomsdayContainerMenu(id, playerInventory, this);
    }
}