package de.cas_ual_ty.ydm.clientutil;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Custom item model for card set items. Delegates to FinalCardSetBakedModel for custom card set texture rendering.
 * TODO: Implement full custom card set texture rendering using the new ItemModel system.
 */
public class CardSetBakedModel implements ItemModel
{
    private final ItemModel baseModel;
    
    public CardSetBakedModel(ItemModel baseModel)
    {
        this.baseModel = baseModel;
    }
    
    @Override
    public void update(ItemStackRenderState renderState, ItemStack stack, ItemModelResolver modelResolver, ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed)
    {
        baseModel.update(renderState, stack, modelResolver, displayContext, level, owner, seed);
    }
}
