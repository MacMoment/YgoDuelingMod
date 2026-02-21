package de.cas_ual_ty.ydm.util;

import de.cas_ual_ty.ydm.YDM;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

public class YDMItemHandler extends ItemStackHandler
{
    public YDMItemHandler()
    {
        super();
    }
    
    public YDMItemHandler(int size)
    {
        super(size);
    }
    
    public YDMItemHandler(NonNullList<ItemStack> stacks)
    {
        super(stacks);
    }
    
    /**
     * Creates a YDMItemHandler backed by the given ItemStack's DataComponent.
     * Changes to the handler are automatically saved back to the ItemStack.
     */
    public static YDMItemHandler fromItemStack(ItemStack itemStack)
    {
        CompoundTag tag = itemStack.getOrDefault(YDM.CARD_INVENTORY_DATA.get(), new CompoundTag());
        YDMItemHandler handler = new YDMItemHandler(0)
        {
            @Override
            protected void onContentsChanged(int slot)
            {
                super.onContentsChanged(slot);
                CompoundTag newTag = new CompoundTag();
                this.serialize(newTag);
                itemStack.set(YDM.CARD_INVENTORY_DATA.get(), newTag);
            }
        };
        if(!tag.isEmpty())
        {
            handler.deserialize(tag);
        }
        return handler;
    }
}
