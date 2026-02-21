package de.cas_ual_ty.ydm.deckbox;

import de.cas_ual_ty.ydm.YDM;
import de.cas_ual_ty.ydm.YdmContainerTypes;
import de.cas_ual_ty.ydm.YdmItems;
import de.cas_ual_ty.ydm.card.CardHolder;
import de.cas_ual_ty.ydm.util.YDMItemHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DeckBoxItem extends Item implements MenuProvider
{
    public static final String ITEM_HANDLER_KEY = "cards";
    public static final String CARD_SLEEVES_KEY = "sleeves";
    
    public DeckBoxItem(Properties properties)
    {
        super(properties);
    }
    
    public YDMItemHandler getItemHandler(ItemStack itemStack)
    {
        return itemStack.getData(YDM.CARD_ITEM_INVENTORY);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
    {
        ItemStack stack = DeckBoxItem.getActiveDeckBox(player);
        
        if(player.getItemInHand(hand) == stack)
        {
            player.openMenu(this);
            return InteractionResultHolder.success(stack);
        }
        
        return super.use(world, player, hand);
    }
    
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player)
    {
        return new DeckBoxContainer(YdmContainerTypes.DECK_BOX.get(), id, playerInv, DeckBoxItem.getActiveDeckBox(player));
    }
    
    @Override
    public Component getDisplayName()
    {
        return Component.translatable("container." + YDM.MOD_ID + ".deck_box");
    }
    
    public ItemHandlerDeckHolder getDeckHolder(ItemStack itemStack)
    {
        return new ItemHandlerDeckHolder(getItemHandler(itemStack));
    }
    
    public void setDeckHolder(ItemStack itemStack, DeckHolder holder)
    {
        YDMItemHandler itemHandler = getItemHandler(itemStack);
        
        CardHolder c;
        
        for(int i = 0; i < holder.getMainDeckSize(); ++i)
        {
            c = holder.getMainDeck().get(i);
            
            if(c != null)
            {
                itemHandler.setStackInSlot(DeckHolder.MAIN_DECK_INDEX_START + i, YdmItems.CARD.get().createItemForCardHolder(c));
            }
        }
        
        for(int i = holder.getMainDeckSize(); i < DeckHolder.MAIN_DECK_SIZE; ++i)
        {
            itemHandler.setStackInSlot(DeckHolder.MAIN_DECK_INDEX_START + i, ItemStack.EMPTY);
        }
        
        for(int i = 0; i < holder.getExtraDeckSize(); ++i)
        {
            c = holder.getExtraDeck().get(i);
            
            if(c != null)
            {
                itemHandler.setStackInSlot(DeckHolder.EXTRA_DECK_INDEX_START + i, YdmItems.CARD.get().createItemForCardHolder(c));
            }
        }
        
        for(int i = holder.getExtraDeckSize(); i < DeckHolder.EXTRA_DECK_SIZE; ++i)
        {
            itemHandler.setStackInSlot(DeckHolder.EXTRA_DECK_INDEX_START + i, ItemStack.EMPTY);
        }
        
        for(int i = 0; i < holder.getSideDeckSize(); ++i)
        {
            c = holder.getSideDeck().get(i);
            
            if(c != null)
            {
                itemHandler.setStackInSlot(DeckHolder.SIDE_DECK_INDEX_START + i, YdmItems.CARD.get().createItemForCardHolder(c));
            }
        }
        
        for(int i = holder.getSideDeckSize(); i < DeckHolder.SIDE_DECK_SIZE; ++i)
        {
            itemHandler.setStackInSlot(DeckHolder.SIDE_DECK_INDEX_START + i, ItemStack.EMPTY);
        }
        
        if(!holder.getSleeves().isCardBack())
        {
            itemHandler.setStackInSlot(DeckHolder.SLEEVES_INDEX, new ItemStack(holder.getSleeves().getItem()));
        }
        
    }
    
    public static ItemStack getActiveDeckBox(Player player)
    {
        if(player.getMainHandItem().getItem() instanceof DeckBoxItem)
        {
            return player.getMainHandItem();
        }
        else if(player.getOffhandItem().getItem() instanceof DeckBoxItem)
        {
            return player.getOffhandItem();
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }
}
