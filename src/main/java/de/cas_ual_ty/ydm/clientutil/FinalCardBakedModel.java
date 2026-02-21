package de.cas_ual_ty.ydm.clientutil;

import com.mojang.math.Transformation;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.model.ComposedModelState;
import net.neoforged.neoforge.client.model.UnbakedElementsHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

/**
 * Final baked model for card items that renders custom card textures.
 * TODO: Restore full custom card texture rendering using the new ItemModel system.
 */
public class FinalCardBakedModel implements ItemModel
{
    private final ItemModel baseModel;
    
    public FinalCardBakedModel(ItemModel baseModel)
    {
        this.baseModel = baseModel;
    }
    
    public FinalCardBakedModel setActiveItemStack(ItemStack itemStack)
    {
        return this;
    }
    
    @Override
    public void update(ItemStackRenderState renderState, ItemStack stack, ItemModelResolver modelResolver, ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed)
    {
        baseModel.update(renderState, stack, modelResolver, displayContext, level, owner, seed);
    }
    
    /**
     * Converts a texture sprite into baked quads for a specific face direction.
     * Note: The Identifier parameter was removed as UnbakedElementsHelper.bakeElements no longer requires it.
     */
    public static List<BakedQuad> convertTexture(Transformation t, TextureAtlasSprite sprite, float off, Direction direction, int color, int layerIdx)
    {
        ModelState modelState = new ComposedModelState(BlockModelRotation.IDENTITY, new Transformation(new Vector3f(), new Quaternionf(), new Vector3f(1F, 1F, 1F), new Quaternionf()));
        
        List<BlockElement> unbaked = UnbakedElementsHelper.createUnbakedItemElements(layerIdx, sprite);
        // TODO: 1.21.11 - BlockElement fields are now private/immutable, face filtering and offset adjustment disabled
        
        return UnbakedElementsHelper.bakeElements(unbaked, m -> sprite, modelState);
    }
}
