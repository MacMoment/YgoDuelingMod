package de.cas_ual_ty.ydm.set;

import de.cas_ual_ty.ydm.YDM;
import de.cas_ual_ty.ydm.YdmContainerTypes;
import de.cas_ual_ty.ydm.carditeminventory.HeldCIIContainer;
import de.cas_ual_ty.ydm.util.YDMItemHandler;
import de.cas_ual_ty.ydm.util.YdmUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipContext;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import java.util.List;
import java.util.Random;

public class OpenedCardSetItem extends CardSetBaseItem
{
    public OpenedCardSetItem(Properties properties)
    {
        super(properties);
    }
    
    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn)
    {
        super.appendHoverText(itemStack, context, tooltip, flagIn);
        tooltip.add(Component.translatable(getDescriptionId() + ".desc").withStyle((s) -> s.applyFormat(ChatFormatting.RED)));
    }
    
    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand)
    {
        if(!world.isClientSide() && hand == YdmUtil.getActiveItem(player, this))
        {
            ItemStack itemStack = player.getItemInHand(hand);
            
            YDMItemHandler itemHandler = getItemHandler(itemStack);
            HeldCIIContainer.openGui(player, hand, itemHandler.getSlots(), new MenuProvider()
            {
                @Override
                public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player)
                {
                    return new CardSetContainer(YdmContainerTypes.CARD_SET.get(), id, playerInventory, itemHandler, hand);
                }
                
                @Override
                public Component getDisplayName()
                {
                    return Component.translatable("container." + YDM.MOD_ID + ".card_set");
                }
            });
            
            return InteractionResult.SUCCESS;
        }
        
        return super.use(world, player, hand);
    }
    
    public int getSize(ItemStack itemStack)
    {
        return getNBT(itemStack).getInt("size");
    }
    
    public YDMItemHandler getItemHandler(ItemStack itemStack)
    {
        return itemStack.getData(YDM.CARD_ITEM_INVENTORY);
    }
    
    public ItemStack createItemForSet(CardSet set, YDMItemHandler itemHandler)
    {
        ItemStack itemStack = new ItemStack(this);
        setCardSet(itemStack, set);
        getItemHandler(itemStack).deserializeNBT(itemHandler.serializeNBT());
        return itemStack;
    }
    
    public ItemStack createItemForSet(CardSet set)
    {
        List<ItemStack> cards = set.open(new Random());
        NonNullList<ItemStack> items;
        
        if(cards == null)
        {
            items = NonNullList.withSize(0, ItemStack.EMPTY);
        }
        else
        {
            items = NonNullList.of(ItemStack.EMPTY, cards.toArray(ItemStack[]::new));
        }
        
        return createItemForSet(set, items);
    }
    
    public ItemStack createItemForSet(CardSet set, NonNullList<ItemStack> items)
    {
        ItemStack itemStack = new ItemStack(this);
        setCardSet(itemStack, set);
        YDMItemHandler current = getItemHandler(itemStack);
        current.setSize(items.size());
        for(int i = 0; i < items.size(); i++)
        {
            current.setStackInSlot(i, items.get(i));
        }
        return itemStack;
    }
}
