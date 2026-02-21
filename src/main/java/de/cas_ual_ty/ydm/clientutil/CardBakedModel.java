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
 * Custom item model for card items. Delegates to FinalCardBakedModel for custom card texture rendering.
 * TODO: Implement full custom card texture rendering using the new ItemModel system.
 */
public class CardBakedModel implements ItemModel
{
    private final ItemModel baseModel;
    
    public CardBakedModel(ItemModel baseModel)
    {
        this.baseModel = baseModel;
    }
    
    @Override
    public void update(ItemStackRenderState renderState, ItemStack stack, ItemModelResolver modelResolver, ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed)
    {
        // TODO: Intercept here to apply custom card texture based on stack's CardHolder data
        baseModel.update(renderState, stack, modelResolver, displayContext, level, entity, seed);
    }
}
