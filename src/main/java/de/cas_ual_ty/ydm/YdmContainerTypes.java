package de.cas_ual_ty.ydm;

import de.cas_ual_ty.ydm.cardbinder.CardBinderContainer;
import de.cas_ual_ty.ydm.carditeminventory.CIIContainer;
import de.cas_ual_ty.ydm.cardsupply.CardSupplyContainer;
import de.cas_ual_ty.ydm.deckbox.DeckBoxContainer;
import de.cas_ual_ty.ydm.duel.block.DuelBlockContainer;
import de.cas_ual_ty.ydm.duel.dueldisk.DuelEntityContainer;
import de.cas_ual_ty.ydm.set.CardSetContainer;
import de.cas_ual_ty.ydm.set.CardSetContentsContainer;
import de.cas_ual_ty.ydm.simplebinder.SimpleBinderContainer;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class YdmContainerTypes
{
    private static final DeferredRegister<MenuType<?>> DEFERRED_REGISTER = DeferredRegister.create(net.minecraft.core.registries.Registries.MENU, YDM.MOD_ID);
    
    public static final DeferredHolder<MenuType<?>, MenuType<CardBinderContainer>> CARD_BINDER = DEFERRED_REGISTER.register("card_binder", () -> new MenuType<>((id, playerInv) -> new CardBinderContainer(YdmContainerTypes.CARD_BINDER.get(), id, playerInv), FeatureFlags.VANILLA_SET));
    public static final DeferredHolder<MenuType<?>, MenuType<DeckBoxContainer>> DECK_BOX = DEFERRED_REGISTER.register("deck_box", () -> new MenuType<>((id, playerInv) -> new DeckBoxContainer(YdmContainerTypes.DECK_BOX.get(), id, playerInv), FeatureFlags.VANILLA_SET));
    public static final DeferredHolder<MenuType<?>, MenuType<DuelBlockContainer>> DUEL_BLOCK_CONTAINER = DEFERRED_REGISTER.register("duel_block_container", () -> IMenuTypeExtension.create((id, playerInv, extraData) -> new DuelBlockContainer(YdmContainerTypes.DUEL_BLOCK_CONTAINER.get(), id, playerInv, extraData)));
    public static final DeferredHolder<MenuType<?>, MenuType<DuelEntityContainer>> DUEL_ENTITY_CONTAINER = DEFERRED_REGISTER.register("duel_entity_container", () -> IMenuTypeExtension.create((id, playerInv, extraData) -> new DuelEntityContainer(YdmContainerTypes.DUEL_ENTITY_CONTAINER.get(), id, playerInv, extraData)));
    public static final DeferredHolder<MenuType<?>, MenuType<CardSupplyContainer>> CARD_SUPPLY = DEFERRED_REGISTER.register("card_supply", () -> IMenuTypeExtension.create((id, playerInv, extraData) -> new CardSupplyContainer(YdmContainerTypes.CARD_SUPPLY.get(), id, playerInv, extraData)));
    public static final DeferredHolder<MenuType<?>, MenuType<CIIContainer>> CARD_SET = DEFERRED_REGISTER.register("card_set", () -> IMenuTypeExtension.create((id, playerInv, extraData) -> new CardSetContainer(YdmContainerTypes.CARD_SET.get(), id, playerInv, extraData)));
    public static final DeferredHolder<MenuType<?>, MenuType<CIIContainer>> CARD_SET_CONTENTS = DEFERRED_REGISTER.register("card_set_contents", () -> IMenuTypeExtension.create((id, playerInv, extraData) -> new CardSetContentsContainer(YdmContainerTypes.CARD_SET_CONTENTS.get(), id, playerInv, extraData)));
    public static final DeferredHolder<MenuType<?>, MenuType<CIIContainer>> SIMPLE_BINDER = DEFERRED_REGISTER.register("simple_binder", () -> IMenuTypeExtension.create((id, playerInv, extraData) -> new SimpleBinderContainer(YdmContainerTypes.SIMPLE_BINDER.get(), id, playerInv, extraData)));
    
    public static void register(IEventBus bus)
    {
        DEFERRED_REGISTER.register(bus);
    }
}