package com.raiiiden.doomsdayfunctionality.blockentity;

import com.raiiiden.doomsdayfunctionality.Doomsday;
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
import java.util.Collections;
import java.util.stream.IntStream;

import java.util.List;

public class DoomsdayBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler lootHandler = new ItemStackHandler(27) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private boolean looted = false;
    private boolean filledFromLoot = false;
    private long lootSeed = 0L;
    private long lastLootDay = -1L;
    private long lastForceId = -1L;
    private ResourceLocation lootTable = new ResourceLocation("doomsday_functionality", "chests/generic");

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

    public void setLastLootDay(long day) {
        this.lastLootDay = day;
        setChanged();
    }
    public void setDefaultLootTableFromBlock() {
        if (level != null && !filledFromLoot && (lootTable == null || lootTable.toString().equals("doomsday_functionality:chests/generic"))) {
            ResourceLocation blockId = getBlockState().getBlock().builtInRegistryHolder().key().location();
            if (blockId != null) {
                this.lootTable = new ResourceLocation("doomsday_functionality", "chests/" + blockId.getPath());
                Doomsday.LOGGER.debug("Auto-assigned loot table: {}", this.lootTable);
            }
        }
    }

    public void tryLoadLoot(Player player) {
        if (!(level instanceof ServerLevel server)) return;

        setDefaultLootTableFromBlock();
        long currentDay = server.getDayTime() / 24000L;
        int interval = DoomsdayCommonConfig.LOOT_REFRESH_DAYS.get();
        boolean forceRefresh = Doomsday.GLOBAL_FORCE_REFRESH_ID > lastForceId;

        if (!forceRefresh && lastLootDay != -1 && currentDay - lastLootDay < interval) {
            return;
        }

        LootTable table = server.getServer().getLootData().getLootTable(lootTable);
        if (table == LootTable.EMPTY) {
            Doomsday.LOGGER.warn("Loot table {} not found!", lootTable);
            return;
        }

        LootParams.Builder params = new LootParams.Builder(server)
                .withParameter(LootContextParams.ORIGIN, this.getBlockPos().getCenter())
                .withOptionalParameter(LootContextParams.THIS_ENTITY, player);

        List<ItemStack> items = table.getRandomItems(params.create(LootContextParamSets.CHEST));

        List<Integer> slots = new java.util.ArrayList<>(IntStream.range(0, lootHandler.getSlots())
                .boxed()
                .toList());
        Collections.shuffle(slots, new java.util.Random(server.getRandom().nextLong()));

        for (int i = 0; i < Math.min(slots.size(), items.size()); i++) {
            lootHandler.setStackInSlot(slots.get(i), items.get(i));
        }

        filledFromLoot = true;
        looted = false;
        lastLootDay = currentDay;
        lastForceId = Doomsday.GLOBAL_FORCE_REFRESH_ID;
        setChanged();
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
        tag.putLong("LastLootDay", lastLootDay);
        tag.putLong("LastForceId", lastForceId);
        if (lootTable != null) tag.putString("LootTable", lootTable.toString());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        lootHandler.deserializeNBT(tag.getCompound("Loot"));
        looted = tag.getBoolean("Looted");
        filledFromLoot = tag.getBoolean("Filled");
        lootSeed = tag.getLong("Seed");
        lastLootDay = tag.getLong("LastLootDay");
        lastForceId = tag.getLong("LastForceId");
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
        return Component.translatable(getBlockState().getBlock().getDescriptionId());
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        tryLoadLoot(player);
        return new DoomsdayContainerMenu(id, playerInventory, this);
    }
}
