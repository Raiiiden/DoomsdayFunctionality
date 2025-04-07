package com.raiiiden.doomsdayfunctionality.blockentity;

import com.raiiiden.doomsdayfunctionality.Doomsday;
import com.raiiiden.doomsdayfunctionality.init.BlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DoomsdayBlockEntity extends BlockEntity {

    public DoomsdayBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.GENERIC.get(), pos, state);
        Doomsday.LOGGER.info("Created DoomsdayBlockEntity at {}", pos);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        // Add your custom data here
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        Doomsday.LOGGER.debug("Loaded DoomsdayBlockEntity data");
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
}