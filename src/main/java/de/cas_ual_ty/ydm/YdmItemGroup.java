package de.cas_ual_ty.ydm;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class YdmItemGroup
{
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, YDM.MOD_ID);
    
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> YDM_TAB = CREATIVE_MODE_TABS.register("ydm", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + YDM.MOD_ID))
            .icon(() -> new ItemStack(YdmItems.CARD_BACK.get()))
            .displayItems((parameters, output) -> {
                output.accept(YdmItems.CARD_BACK.get());
                output.accept(YdmItems.BLANC_CARD.get());
                output.accept(YdmItems.BLANC_SET.get());
                output.accept(YdmItems.CARD_BINDER.get());
                output.accept(YdmItems.DUEL_PLAYMAT.get());
                output.accept(YdmItems.DUEL_TABLE.get());
                output.accept(YdmItems.CARD_SUPPLY.get());
                output.accept(YdmItems.SIMPLE_BINDER_3.get());
                output.accept(YdmItems.SIMPLE_BINDER_9.get());
                output.accept(YdmItems.SIMPLE_BINDER_27.get());
                output.accept(YdmItems.MILLENIUM_EYE.get());
                output.accept(YdmItems.MILLENIUM_KEY.get());
                output.accept(YdmItems.MILLENIUM_NECKLACE.get());
                output.accept(YdmItems.MILLENIUM_PUZZLE.get());
                output.accept(YdmItems.MILLENIUM_RING.get());
                output.accept(YdmItems.MILLENIUM_ROD.get());
                output.accept(YdmItems.MILLENIUM_SCALE.get());
                output.accept(YdmItems.DUEL_DISK.get());
                output.accept(YdmItems.CHAOS_DISK.get());
                output.accept(YdmItems.ACADEMIA_DISK.get());
                output.accept(YdmItems.ACADEMIA_DISK_RED.get());
                output.accept(YdmItems.ACADEMIA_DISK_BLUE.get());
                output.accept(YdmItems.ACADEMIA_DISK_YELLOW.get());
                output.accept(YdmItems.ROCK_SPIRIT_DISK.get());
                output.accept(YdmItems.TRUEMAN_DISK.get());
                output.accept(YdmItems.JEWEL_DISK.get());
                output.accept(YdmItems.KAIBAMAN_DISK.get());
                output.accept(YdmItems.CYBER_DESIGN_INTERFACE.get());
                output.accept(YdmItems.BLACK_DECK_BOX.get());
                output.accept(YdmItems.RED_DECK_BOX.get());
                output.accept(YdmItems.GREEN_DECK_BOX.get());
                output.accept(YdmItems.BROWN_DECK_BOX.get());
                output.accept(YdmItems.BLUE_DECK_BOX.get());
                output.accept(YdmItems.PURPLE_DECK_BOX.get());
                output.accept(YdmItems.CYAN_DECK_BOX.get());
                output.accept(YdmItems.LIGHT_GRAY_DECK_BOX.get());
                output.accept(YdmItems.GRAY_DECK_BOX.get());
                output.accept(YdmItems.PINK_DECK_BOX.get());
                output.accept(YdmItems.LIME_DECK_BOX.get());
                output.accept(YdmItems.YELLOW_DECK_BOX.get());
                output.accept(YdmItems.LIGHT_BLUE_DECK_BOX.get());
                output.accept(YdmItems.MAGENTA_DECK_BOX.get());
                output.accept(YdmItems.ORANGE_DECK_BOX.get());
                output.accept(YdmItems.WHITE_DECK_BOX.get());
                output.accept(YdmItems.IRON_DECK_BOX.get());
                output.accept(YdmItems.GOLD_DECK_BOX.get());
                output.accept(YdmItems.DIAMOND_DECK_BOX.get());
                output.accept(YdmItems.EMERALD_DECK_BOX.get());
                output.accept(YdmItems.PATREON_DECK_BOX.get());
            })
            .build());
    
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CARDS_TAB = CREATIVE_MODE_TABS.register("cards", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + YDM.MOD_ID + ".cards"))
            .icon(() -> new ItemStack(YdmItems.BLANC_CARD.get()))
            .displayItems((parameters, output) -> {
                output.accept(YdmItems.CARD.get());
            })
            .build());
    
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SETS_TAB = CREATIVE_MODE_TABS.register("sets", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + YDM.MOD_ID + ".sets"))
            .icon(() -> new ItemStack(YdmItems.BLANC_SET.get()))
            .displayItems((parameters, output) -> {
                output.accept(YdmItems.SET.get());
            })
            .build());
    
    public static void register(IEventBus bus)
    {
        CREATIVE_MODE_TABS.register(bus);
    }
}
