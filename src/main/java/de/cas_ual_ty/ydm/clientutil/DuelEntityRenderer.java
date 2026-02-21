package de.cas_ual_ty.ydm.clientutil;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;

public class DuelEntityRenderer extends EntityRenderer<Entity, EntityRenderState>
{
    protected DuelEntityRenderer(EntityRendererProvider.Context context)
    {
        super(context);
    }
    
    @Override
    public EntityRenderState createRenderState()
    {
        return new EntityRenderState();
    }
}
