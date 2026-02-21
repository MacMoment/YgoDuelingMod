package de.cas_ual_ty.ydm.card;

import de.cas_ual_ty.ydm.YDM;
import de.cas_ual_ty.ydm.rarity.Rarities;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class CardItem extends Item
{
    public CardItem(Properties properties)
    {
        super(properties);
    }
    
    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext ctx, TooltipDisplay display, Consumer<Component> tooltipAdder, TooltipFlag flag)
    {
        CardHolder holder = getCardHolder(itemStack);
        java.util.List<Component> list = new java.util.ArrayList<>();
        holder.addInformation(list);
        list.forEach(tooltipAdder);
    }
    
    @Override
    public Component getName(ItemStack itemStack)
    {
        CardHolder holder = getCardHolder(itemStack);
        return Component.literal(holder.getCard().getName());
    }
    
    @Override
    public InteractionResult use(Level pLevel, Player pPlayer, InteractionHand pUsedHand)
    {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        CardHolder cardHolder = getCardHolder(itemStack);
        if(cardHolder != null && pPlayer.level().isClientSide())
        {
            YDM.proxy.openCardInspectScreen(cardHolder);
            return InteractionResult.SUCCESS;
        }
        
        return super.use(pLevel, pPlayer, pUsedHand);
    }
    
    public CardHolder getCardHolder(ItemStack itemStack)
    {
        return new ItemStackCardHolder(itemStack);
    }
    
    public ItemStack createItemForCard(de.cas_ual_ty.ydm.card.properties.Properties card, byte imageIndex, String rarity, String code)
    {
        ItemStack itemStack = new ItemStack(this);
        getCardHolder(itemStack).override(new CardHolder(card, imageIndex, rarity, code));
        return itemStack;
    }
    
    public ItemStack createItemForCard(de.cas_ual_ty.ydm.card.properties.Properties card, byte imageIndex, String rarity)
    {
        ItemStack itemStack = new ItemStack(this);
        getCardHolder(itemStack).override(new CardHolder(card, imageIndex, rarity));
        return itemStack;
    }
    
    public ItemStack createItemForCard(de.cas_ual_ty.ydm.card.properties.Properties card)
    {
        return createItemForCard(card, (byte) 0, Rarities.CREATIVE.name);
    }
    
    public ItemStack createItemForCardHolder(CardHolder card)
    {
        ItemStack itemStack = new ItemStack(this);
        getCardHolder(itemStack).override(card);
        return itemStack;
    }
}
