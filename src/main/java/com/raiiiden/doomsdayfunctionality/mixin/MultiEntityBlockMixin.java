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
        "net.mcreator.doomsdaydecoration.block.Goodsshelves6Block",
        "net.mcreator.doomsdaydecoration.block.APECUBEBlock",
        "net.mcreator.doomsdaydecoration.block.Accessorybox1Block",
        "net.mcreator.doomsdaydecoration.block.Accessorybox2Block",
        "net.mcreator.doomsdaydecoration.block.Acrate2Block",
        "net.mcreator.doomsdaydecoration.block.Acrate3Block",
        "net.mcreator.doomsdaydecoration.block.AcrateBlock",
        "net.mcreator.doomsdaydecoration.block.AirdropBlock",
        "net.mcreator.doomsdaydecoration.block.Ambulance1Block",
        "net.mcreator.doomsdaydecoration.block.Ambulance2Block",
        "net.mcreator.doomsdaydecoration.block.Ambulance3Block",
        "net.mcreator.doomsdaydecoration.block.AmmunitionboxBlock",
        "net.mcreator.doomsdaydecoration.block.Arcadegame2Block", // Arcade games might have coin slots or internal compartments
        "net.mcreator.doomsdaydecoration.block.Arcadegame3Block",
        "net.mcreator.doomsdaydecoration.block.Arcadegame4Block",
        "net.mcreator.doomsdaydecoration.block.Arcadegame5Block",
        "net.mcreator.doomsdaydecoration.block.ArcadegameBlock",
        "net.mcreator.doomsdaydecoration.block.BarrelBlock",
        "net.mcreator.doomsdaydecoration.block.BathtubBlock", // Could potentially hold water/items
        "net.mcreator.doomsdaydecoration.block.BeakerBlock",
        "net.mcreator.doomsdaydecoration.block.BlackJeep1Block",
        "net.mcreator.doomsdaydecoration.block.BlackJeep2Block",
        "net.mcreator.doomsdaydecoration.block.BlackluggageBlock",
        "net.mcreator.doomsdaydecoration.block.BlackpickuptruckBlock",
        "net.mcreator.doomsdaydecoration.block.BlackstationwagonBlock",
        "net.mcreator.doomsdaydecoration.block.Blackvan1Block",
        "net.mcreator.doomsdaydecoration.block.Blackvan2Block",
        "net.mcreator.doomsdaydecoration.block.BlueluggageBlock",
        "net.mcreator.doomsdaydecoration.block.BlueJeep1Block",
        "net.mcreator.doomsdaydecoration.block.BlueJeep2Block",
        "net.mcreator.doomsdaydecoration.block.BluepickuptruckBlock",
        "net.mcreator.doomsdaydecoration.block.BluestationwagonBlock",
        "net.mcreator.doomsdaydecoration.block.Bluevan1Block",
        "net.mcreator.doomsdaydecoration.block.Bluevan2Block",
        "net.mcreator.doomsdaydecoration.block.BodybagBlock", // Could contain "loot" in a darker game
        "net.mcreator.doomsdaydecoration.block.BookBlock",
        "net.mcreator.doomsdaydecoration.block.Bookpile2Block",
        "net.mcreator.doomsdaydecoration.block.BookpileBlock",
        "net.mcreator.doomsdaydecoration.block.Bottle2Block",
        "net.mcreator.doomsdaydecoration.block.BottleBlock",
        "net.mcreator.doomsdaydecoration.block.BrownJeep1Block",
        "net.mcreator.doomsdaydecoration.block.BrownJeep2Block",
        "net.mcreator.doomsdaydecoration.block.BrowningM2Block",
        "net.mcreator.doomsdaydecoration.block.BrownpickuptruckBlock",
        "net.mcreator.doomsdaydecoration.block.BucketBlock",
        "net.mcreator.doomsdaydecoration.block.Cabinet2Block",
        "net.mcreator.doomsdaydecoration.block.Cabinet3Block",
        "net.mcreator.doomsdaydecoration.block.CabinetBlock",
        "net.mcreator.doomsdaydecoration.block.Cart2Block",
        "net.mcreator.doomsdaydecoration.block.CartBlock",
        "net.mcreator.doomsdaydecoration.block.Carton2Block",
        "net.mcreator.doomsdaydecoration.block.Carton3Block",
        "net.mcreator.doomsdaydecoration.block.CartonBlock",
        "net.mcreator.doomsdaydecoration.block.CashregisterBlock",
        "net.mcreator.doomsdaydecoration.block.CentrifugeBlock",
        "net.mcreator.doomsdaydecoration.block.ChalkBlock",
        "net.mcreator.doomsdaydecoration.block.Chemicalprotectiveclothing2Block",
        "net.mcreator.doomsdaydecoration.block.ChemicalprotectiveclothingBlock",
        "net.mcreator.doomsdaydecoration.block.ClassroomdeskBlock",
        "net.mcreator.doomsdaydecoration.block.ClosestoolBlock",
        "net.mcreator.doomsdaydecoration.block.CoffeeBlock",
        "net.mcreator.doomsdaydecoration.block.CoffeeMachineBlock",
        "net.mcreator.doomsdaydecoration.block.ConstanttemperatureincubatorBlock",
        "net.mcreator.doomsdaydecoration.block.Cup2Block",
        "net.mcreator.doomsdaydecoration.block.CupBlock",
        "net.mcreator.doomsdaydecoration.block.Desk2Block",
        "net.mcreator.doomsdaydecoration.block.Desk3Block",
        "net.mcreator.doomsdaydecoration.block.DeskBlock",
        "net.mcreator.doomsdaydecoration.block.DiscardWhiteJeep1Block",
        "net.mcreator.doomsdaydecoration.block.DiscardWhiteJeep2Block",
        "net.mcreator.doomsdaydecoration.block.Discardblackcar1Block",
        "net.mcreator.doomsdaydecoration.block.Discardblackcar2Block",
        "net.mcreator.doomsdaydecoration.block.Discardblackjeep1Block",
        "net.mcreator.doomsdaydecoration.block.Discardblackjeep2Block",
        "net.mcreator.doomsdaydecoration.block.DiscardblackpickuptruckBlock",
        "net.mcreator.doomsdaydecoration.block.DiscardblackstationwagonBlock",
        "net.mcreator.doomsdaydecoration.block.Discardbluecar1Block",
        "net.mcreator.doomsdaydecoration.block.Discardbluecar2Block",
        "net.mcreator.doomsdaydecoration.block.Discardbluejeep1Block",
        "net.mcreator.doomsdaydecoration.block.Discardbluejeep2Block",
        "net.mcreator.doomsdaydecoration.block.DiscardbluepickuptruckBlock",
        "net.mcreator.doomsdaydecoration.block.DiscardbluestationwagonBlock",
        "net.mcreator.doomsdaydecoration.block.Discardbrownjeep1Block",
        "net.mcreator.doomsdaydecoration.block.Discardbrownjeep2Block",
        "net.mcreator.doomsdaydecoration.block.DiscardbrownpickuptruckBlock",
        "net.mcreator.doomsdaydecoration.block.Discardgreencar1Block",
        "net.mcreator.doomsdaydecoration.block.Discardgreencar2Block",
        "net.mcreator.doomsdaydecoration.block.Discardgreenjeep1Block",
        "net.mcreator.doomsdaydecoration.block.Discardgreenjeep2Block",
        "net.mcreator.doomsdaydecoration.block.DiscardgreenpickuptruckBlock",
        "net.mcreator.doomsdaydecoration.block.DiscardgreenstationwagonBlock",
        "net.mcreator.doomsdaydecoration.block.Discardgreycar1Block",
        "net.mcreator.doomsdaydecoration.block.Discardgreycar2Block",
        "net.mcreator.doomsdaydecoration.block.Discardgreyjeep1Block",
        "net.mcreator.doomsdaydecoration.block.Discardgreyjeep2Block",
        "net.mcreator.doomsdaydecoration.block.DiscardgreypickuptruckBlock",
        "net.mcreator.doomsdaydecoration.block.DiscardgreystationwagonBlock",
        "net.mcreator.doomsdaydecoration.block.Discardkhakicar1Block",
        "net.mcreator.doomsdaydecoration.block.Discardkhakicar2Block",
        "net.mcreator.doomsdaydecoration.block.DiscardkhakistationwagonBlock",
        "net.mcreator.doomsdaydecoration.block.Discardpolicecar1Block",
        "net.mcreator.doomsdaydecoration.block.Discardpolicecar2Block",
        "net.mcreator.doomsdaydecoration.block.Discardpolicecar3Block",
        "net.mcreator.doomsdaydecoration.block.Discardpolicecar4Block",
        "net.mcreator.doomsdaydecoration.block.Discardredcar1Block",
        "net.mcreator.doomsdaydecoration.block.Discardredcar2Block",
        "net.mcreator.doomsdaydecoration.block.Discardredjeep1Block",
        "net.mcreator.doomsdaydecoration.block.Discardredjeep2Block",
        "net.mcreator.doomsdaydecoration.block.DiscardredpickuptruckBlock",
        "net.mcreator.doomsdaydecoration.block.DiscardredstationwagonBlock",
        "net.mcreator.doomsdaydecoration.block.Discardwhitecar1Block",
        "net.mcreator.doomsdaydecoration.block.Discardwhitecar2Block",
        "net.mcreator.doomsdaydecoration.block.DiscardwhitepickuptruckBlock",
        "net.mcreator.doomsdaydecoration.block.DiscardwhitestationwagonBlock",
        "net.mcreator.doomsdaydecoration.block.DollyBlock",
        "net.mcreator.doomsdaydecoration.block.Draw2Block",
        "net.mcreator.doomsdaydecoration.block.Draw3Block",
        "net.mcreator.doomsdaydecoration.block.Draw4Block",
        "net.mcreator.doomsdaydecoration.block.Draw5Block",
        "net.mcreator.doomsdaydecoration.block.DrawBlock",
        "net.mcreator.doomsdaydecoration.block.DrugBlock",
        "net.mcreator.doomsdaydecoration.block.ElectricovenBlock",
        "net.mcreator.doomsdaydecoration.block.EmptytesttuberackBlock",
        "net.mcreator.doomsdaydecoration.block.FirstaidkitBlock",
        "net.mcreator.doomsdaydecoration.block.Foodstand1Block",
        "net.mcreator.doomsdaydecoration.block.Foodstand2Block",
        "net.mcreator.doomsdaydecoration.block.Foodstand3Block",
        "net.mcreator.doomsdaydecoration.block.ForklifttruckBlock",
        "net.mcreator.doomsdaydecoration.block.FreezerBlock",
        "net.mcreator.doomsdaydecoration.block.Fridge2Block",
        "net.mcreator.doomsdaydecoration.block.FridgeBlock",
        "net.mcreator.doomsdaydecoration.block.FrontBlueSedanBlock",
        "net.mcreator.doomsdaydecoration.block.FrontblacksedanBlock",
        "net.mcreator.doomsdaydecoration.block.FrontgraysedanBlock",
        "net.mcreator.doomsdaydecoration.block.FrontgreensedanBlock",
        "net.mcreator.doomsdaydecoration.block.FrontkhakisedanBlock",
        "net.mcreator.doomsdaydecoration.block.FrontredsedanBlock",
        "net.mcreator.doomsdaydecoration.block.FrontwhitesedanBlock",
        "net.mcreator.doomsdaydecoration.block.GasstationBlock",
        "net.mcreator.doomsdaydecoration.block.GastankBlock",
        "net.mcreator.doomsdaydecoration.block.GeneratorBlock",
        "net.mcreator.doomsdaydecoration.block.FixedgeneratorBlock",
        "net.mcreator.doomsdaydecoration.block.GlasswareBlock",
        "net.mcreator.doomsdaydecoration.block.GrillBlock",
        "net.mcreator.doomsdaydecoration.block.GreenHummer1Block",
        "net.mcreator.doomsdaydecoration.block.GreenHummer2Block",
        "net.mcreator.doomsdaydecoration.block.GreenHummer3Block",
        "net.mcreator.doomsdaydecoration.block.GreenHummer4Block",
        "net.mcreator.doomsdaydecoration.block.GreenHummer5Block",
        "net.mcreator.doomsdaydecoration.block.GreenHummer6Block",
        "net.mcreator.doomsdaydecoration.block.GreenHummer7Block",
        "net.mcreator.doomsdaydecoration.block.GreenHummer8Block",
        "net.mcreator.doomsdaydecoration.block.GreenHummer9Block",
        "net.mcreator.doomsdaydecoration.block.GreenJeep1Block",
        "net.mcreator.doomsdaydecoration.block.GreenJeep2Block",
        "net.mcreator.doomsdaydecoration.block.GreenluggageBlock",
        "net.mcreator.doomsdaydecoration.block.GreenpickuptruckBlock",
        "net.mcreator.doomsdaydecoration.block.GreentravelcarBlock",
        "net.mcreator.doomsdaydecoration.block.Greenvan1Block",
        "net.mcreator.doomsdaydecoration.block.Greenvan2Block",
        "net.mcreator.doomsdaydecoration.block.GreyJeep1Block",
        "net.mcreator.doomsdaydecoration.block.GreyJeep2Block",
        "net.mcreator.doomsdaydecoration.block.GreyluggageBlock",
        "net.mcreator.doomsdaydecoration.block.GreypickuptruckBlock",
        "net.mcreator.doomsdaydecoration.block.Greyvan1Block",
        "net.mcreator.doomsdaydecoration.block.Greyvan2Block",
        "net.mcreator.doomsdaydecoration.block.GreywagonBlock",
        "net.mcreator.doomsdaydecoration.block.HangingclothesBlock",
        "net.mcreator.doomsdaydecoration.block.Host2Block",
        "net.mcreator.doomsdaydecoration.block.HostBlock",
        "net.mcreator.doomsdaydecoration.block.IndoorgarbagebinBlock",
        "net.mcreator.doomsdaydecoration.block.InsulationcabinetBlock",
        "net.mcreator.doomsdaydecoration.block.JarBlock",
        "net.mcreator.doomsdaydecoration.block.KettleBlock",
        "net.mcreator.doomsdaydecoration.block.KhakiHummer1Block",
        "net.mcreator.doomsdaydecoration.block.KhakiHummer2Block",
        "net.mcreator.doomsdaydecoration.block.KhakiHummer3Block",
        "net.mcreator.doomsdaydecoration.block.KhakiHummer4Block",
        "net.mcreator.doomsdaydecoration.block.KhakiHummer5Block",
        "net.mcreator.doomsdaydecoration.block.KhakiHummer6Block",
        "net.mcreator.doomsdaydecoration.block.KhakiHummer7Block",
        "net.mcreator.doomsdaydecoration.block.KhakiHummer8Block",
        "net.mcreator.doomsdaydecoration.block.KhakiHummer9Block",
        "net.mcreator.doomsdaydecoration.block.KhakiluggageBlock",
        "net.mcreator.doomsdaydecoration.block.KhakistationwagonBlock",
        "net.mcreator.doomsdaydecoration.block.KnapsackBlock",
        "net.mcreator.doomsdaydecoration.block.RearBlueSedanBlock" ,
        "net.mcreator.doomsdaydecoration.block.RearblacksedanBlock" ,
        "net.mcreator.doomsdaydecoration.block.ReargraysedanBlock",
        "net.mcreator.doomsdaydecoration.block.ReargreensedanBlock",
        "net.mcreator.doomsdaydecoration.block.RearkhakisedanBlock",
        "net.mcreator.doomsdaydecoration.block.RearredsedanBlock",
        "net.mcreator.doomsdaydecoration.block.RearwhitesedanBlock",
        "net.mcreator.doomsdaydecoration.block.ShoppingCartBlock",
        "net.mcreator.doomsdaydecoration.block.Remains101010Block",
        "net.mcreator.doomsdaydecoration.block.Remains1010Block",
        "net.mcreator.doomsdaydecoration.block.Remains10Block",
        "net.mcreator.doomsdaydecoration.block.Remains111Block",
        "net.mcreator.doomsdaydecoration.block.Remains11Block",
        "net.mcreator.doomsdaydecoration.block.Remains1Block",
        "net.mcreator.doomsdaydecoration.block.Remains222Block",
        "net.mcreator.doomsdaydecoration.block.Remains22Block",
        "net.mcreator.doomsdaydecoration.block.Remains2Block",
        "net.mcreator.doomsdaydecoration.block.Remains333Block",
        "net.mcreator.doomsdaydecoration.block.Remains33Block",
        "net.mcreator.doomsdaydecoration.block.Remains3Block",
        "net.mcreator.doomsdaydecoration.block.Remains444Block",
        "net.mcreator.doomsdaydecoration.block.Remains44Block",
        "net.mcreator.doomsdaydecoration.block.Remains4Block",
        "net.mcreator.doomsdaydecoration.block.Remains555Block",
        "net.mcreator.doomsdaydecoration.block.Remains55Block",
        "net.mcreator.doomsdaydecoration.block.Remains5Block",
        "net.mcreator.doomsdaydecoration.block.Remains666Block",
        "net.mcreator.doomsdaydecoration.block.Remains66Block",
        "net.mcreator.doomsdaydecoration.block.Remains6Block",
        "net.mcreator.doomsdaydecoration.block.Remains777Block",
        "net.mcreator.doomsdaydecoration.block.Remains77Block",
        "net.mcreator.doomsdaydecoration.block.Remains7Block",
        "net.mcreator.doomsdaydecoration.block.Remains888Block",
        "net.mcreator.doomsdaydecoration.block.Remains88Block",
        "net.mcreator.doomsdaydecoration.block.Remains8Block",
        "net.mcreator.doomsdaydecoration.block.Remains999Block",
        "net.mcreator.doomsdaydecoration.block.Remains99Block",
        "net.mcreator.doomsdaydecoration.block.Remains9Block",
        "net.mcreator.doomsdaydecoration.block.Vendingmachine2Block",
        "net.mcreator.doomsdaydecoration.block.Vendingmachine3Block",
        "net.mcreator.doomsdaydecoration.block.Vendingmachine4Block",
        "net.mcreator.doomsdaydecoration.block.VendingmachineBlock"
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
