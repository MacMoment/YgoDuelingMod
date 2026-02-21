package de.cas_ual_ty.ydm.card;

import de.cas_ual_ty.ydm.card.properties.Properties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class ItemStackCardHolder extends CardHolder
{
    private ItemStack itemStack;
    
    public ItemStackCardHolder(ItemStack itemStack)
    {
        super();
        this.itemStack = itemStack;
        readCardHolderFromNBT(getNBT());
    }
    
    private void saveToItemStack()
    {
        CompoundTag tag = getNBT();
        writeCardHolderToNBT(tag);
        itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }
    
    private CompoundTag getNBT()
    {
        return itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    }
    
    @Override
    public void setCard(Properties card)
    {
        super.setCard(card);
        saveToItemStack();
    }
    
    @Override
    public void setImageIndex(byte imageIndex)
    {
        super.setImageIndex(imageIndex);
        saveToItemStack();
    }
    
    @Override
    public void setRarity(String rarity)
    {
        super.setRarity(rarity);
        saveToItemStack();
    }
    
    @Override
    public void setCode(String code)
    {
        super.setCode(code);
        saveToItemStack();
    }
    
    @Override
    public void override(CardHolder cardHolder)
    {
        super.override(cardHolder);
        saveToItemStack();
    }
}
