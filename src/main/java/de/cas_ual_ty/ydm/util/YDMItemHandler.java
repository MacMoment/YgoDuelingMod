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
    
    private static RegistryAccess.Frozen getRegistryAccess()
    {
        return RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
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
                TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, getRegistryAccess());
                this.serialize(output);
                itemStack.set(YDM.CARD_INVENTORY_DATA.get(), output.buildResult());
            }
        };
        if(!tag.isEmpty())
        {
            ValueInput input = TagValueInput.create(ProblemReporter.DISCARDING, getRegistryAccess(), tag);
            handler.deserialize(input);
        }
        return handler;
    }
}
