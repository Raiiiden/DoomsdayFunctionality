package com.raiiiden.doomsdayfunctionality.mixin;

import com.raiiiden.doomsdayfunctionality.Doomsday;
import com.raiiiden.doomsdayfunctionality.blockentity.DoomsdayBlockEntity;
import com.raiiiden.doomsdayfunctionality.init.BlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = {
        "net.mcreator.doomsdaydecoration.block.ATMBlock",
        "net.mcreator.doomsdaydecoration.block.ATM2Block"
})
public abstract class MultiEntityBlockMixin extends Block implements EntityBlock {

    public MultiEntityBlockMixin(Properties props) {
        super(props);
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        Doomsday.LOGGER.info("Mixin applied to ATM block: " + this.getClass().getName());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        Doomsday.LOGGER.info("Creating block entity for: " + this + " at " + pos);
        return new DoomsdayBlockEntity(pos, state);
    }
}