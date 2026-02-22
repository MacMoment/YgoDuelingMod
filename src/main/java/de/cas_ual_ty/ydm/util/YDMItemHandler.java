package de.cas_ual_ty.ydm.util;

import de.cas_ual_ty.ydm.YDM;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

public class YDMItemHandler extends ItemStacksResourceHandler
{
    public YDMItemHandler()
    {
        super(1);
    }
    
    public YDMItemHandler(int size)
    {
        super(size);
    }
    
    public YDMItemHandler(NonNullList<ItemStack> stacks)
    {
        super(stacks);
    }
    
    public int getSlots()
    {
        return size();
    }
    
    public ItemStack getStackInSlot(int index)
    {
        return getResource(index).toStack(getAmountAsInt(index));
    }
    
    public void setStackInSlot(int index, ItemStack stack)
    {
        set(index, ItemResource.of(stack), stack.getCount());
    }
    
    public void setSize(int size)
    {
        setStacks(NonNullList.withSize(size, ItemStack.EMPTY));
    }
    
    private static final RegistryAccess.Frozen REGISTRY_ACCESS = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
    
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
            protected void onContentsChanged(int index, ItemStack previousContents)
            {
                super.onContentsChanged(index, previousContents);
                TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, REGISTRY_ACCESS);
                this.serialize(output);
                itemStack.set(YDM.CARD_INVENTORY_DATA.get(), output.buildResult());
            }
        };
        if(!tag.isEmpty())
        {
            ValueInput input = TagValueInput.create(ProblemReporter.DISCARDING, REGISTRY_ACCESS, tag);
            handler.deserialize(input);
        }
        return handler;
    }
}
