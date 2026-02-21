package de.cas_ual_ty.ydm.clientutil;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Final baked model for card set items that renders custom card set textures.
 * TODO: Restore full custom card set texture rendering using the new ItemModel system.
 */
public class FinalCardSetBakedModel implements ItemModel
{
    private final ItemModel baseModel;
    
    public FinalCardSetBakedModel(ItemModel baseModel)
    {
        this.baseModel = baseModel;
    }
    
    public FinalCardSetBakedModel setActiveItemStack(ItemStack itemStack)
    {
        return this;
    }
    
    @Override
    public void update(ItemStackRenderState renderState, ItemStack stack, ItemModelResolver modelResolver, ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed)
    {
        baseModel.update(renderState, stack, modelResolver, displayContext, level, entity, seed);
    }
}
