package de.cas_ual_ty.ydm.util;

import net.minecraft.core.NonNullList;
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
}
