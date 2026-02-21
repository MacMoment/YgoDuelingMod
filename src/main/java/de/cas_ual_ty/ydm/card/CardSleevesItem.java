package de.cas_ual_ty.ydm.card;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class CardSleevesItem extends Item
{
    public final CardSleevesType sleeves;
    
    public CardSleevesItem(Properties properties, CardSleevesType sleeves)
    {
        super(properties);
        this.sleeves = sleeves;
    }
    
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext ctx, TooltipDisplay display, Consumer<Component> tooltipAdder, TooltipFlag flag)
    {
        super.appendHoverText(stack, ctx, display, tooltipAdder, flag);
        
        if(sleeves.isPatreonReward)
        {
            tooltipAdder.accept(Component.literal(sleeves.patronName + "'s Patreon Sleeves"));
        }
    }
}
