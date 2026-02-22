package de.cas_ual_ty.ydm.deckbox;

import de.cas_ual_ty.ydm.YdmItems;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

public class DeckBoxSlot extends ResourceHandlerSlot
{
    public DeckBoxSlot(ItemStacksResourceHandler itemHandler, int index, int xPosition, int yPosition)
    {
        super(itemHandler, itemHandler::set, index, xPosition, yPosition);
    }
    
    @Override
    public boolean mayPlace(ItemStack stack)
    {
        return stack.getItem() == YdmItems.CARD.get();
    }
}
