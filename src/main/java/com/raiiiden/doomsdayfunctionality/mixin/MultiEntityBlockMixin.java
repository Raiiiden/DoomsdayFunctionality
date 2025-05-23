package com.raiiiden.doomsdayfunctionality.mixin;

import com.raiiiden.doomsdayfunctionality.blockentity.DoomsdayBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = {
        "net.mcreator.doomsdaydecoration.block.ATMBlock",
        "net.mcreator.doomsdaydecoration.block.ATM2Block",
        "net.mcreator.doomsdaydecoration.block.GoodsshelvesBlock",
        "net.mcreator.doomsdaydecoration.block.Goodsshelves2Block",
        "net.mcreator.doomsdaydecoration.block.Goodsshelves3Block",
        "net.mcreator.doomsdaydecoration.block.Goodsshelves4Block",
        "net.mcreator.doomsdaydecoration.block.Goodsshelves5Block",
        "net.mcreator.doomsdaydecoration.block.Goodsshelves6Block"
})
public abstract class MultiEntityBlockMixin extends Block implements EntityBlock {

    public MultiEntityBlockMixin(Properties props) {
        super(props);
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        // Log mixin attached
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DoomsdayBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof DoomsdayBlockEntity lootBe) {
                lootBe.tryLoadLoot(player); // loot is refreshed before checking if empty

                boolean isEmpty = true;
                for (int i = 0; i < lootBe.getLootHandler().getSlots(); i++) {
                    if (!lootBe.getLootHandler().getStackInSlot(i).isEmpty()) {
                        isEmpty = false;
                        break;
                    }
                }

                if (isEmpty) {
                    player.displayClientMessage(Component.literal("Empty"), true);
                    return InteractionResult.CONSUME;
                }

                NetworkHooks.openScreen((ServerPlayer) player, lootBe, pos);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
