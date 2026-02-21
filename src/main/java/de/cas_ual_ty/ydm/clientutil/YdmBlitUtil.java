package de.cas_ual_ty.ydm.clientutil;

import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.RenderTypes;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;

import javax.annotation.Nullable;

public class YdmBlitUtil
{
    static float blitOffset = 0;
    
    // --- No-texture methods (backward compat for masked blit contexts) ---
    
    public static void fullBlit(PoseStack ms, float renderX, float renderY, float renderWidth, float renderHeight)
    {
        YdmBlitUtil.blit(ms, renderX, renderY, renderWidth, renderHeight, 0, 0, 1, 1, 1, 1);
    }
    
    public static void fullBlit90Degree(PoseStack ms, float renderX, float renderY, float renderWidth, float renderHeight)
    {
        YdmBlitUtil.blit90Degree(ms, renderX, renderY, renderWidth, renderHeight, 0, 0, 1, 1, 1, 1);
    }
    
    public static void fullBlit180Degree(PoseStack ms, float renderX, float renderY, float renderWidth, float renderHeight)
    {
        YdmBlitUtil.blit180Degree(ms, renderX, renderY, renderWidth, renderHeight, 0, 0, 1, 1, 1, 1);
    }
    
    public static void fullBlit270Degree(PoseStack ms, float renderX, float renderY, float renderWidth, float renderHeight)
    {
        YdmBlitUtil.blit270Degree(ms, renderX, renderY, renderWidth, renderHeight, 0, 0, 1, 1, 1, 1);
    }
    
    /**
     * Param 1-4: Where to and how big to draw on the screen
     * Param 5-8: What part of the texture file to cut out and draw
     * Param 9-10: How big the entire texture file is in general (pow2 only)
     */
    public static void blit(PoseStack ms, float renderX, float renderY, float renderWidth, float renderHeight, float textureX, float textureY, float textureWidth, float textureHeight, int totalTextureFileWidth, int totalTextureFileHeight)
    {
        YdmBlitUtil.customInnerBlit(ms.last().pose(), null, renderX, renderX + renderWidth, renderY, renderY + renderHeight, YdmBlitUtil.blitOffset, textureX / totalTextureFileWidth, (textureX + textureWidth) / totalTextureFileWidth, textureY / totalTextureFileHeight, (textureY + textureHeight) / totalTextureFileHeight);
    }
    
    public static void blit90Degree(PoseStack ms, float renderX, float renderY, float renderWidth, float renderHeight, float textureX, float textureY, float textureWidth, float textureHeight, int totalTextureFileWidth, int totalTextureFileHeight)
    {
        float x1 = textureX / totalTextureFileWidth;
        float y1 = textureY / totalTextureFileHeight;
        float x2 = (textureX + textureWidth) / totalTextureFileWidth;
        float y2 = (textureY + textureHeight) / totalTextureFileHeight;
        YdmBlitUtil.customInnerBlit(ms.last().pose(), null, renderX, renderX + renderWidth, renderY, renderY + renderHeight, YdmBlitUtil.blitOffset, x2, y1, x2, y2, x1, y2, x1, y1);
    }
    
    public static void blit180Degree(PoseStack ms, float renderX, float renderY, float renderWidth, float renderHeight, float textureX, float textureY, float textureWidth, float textureHeight, int totalTextureFileWidth, int totalTextureFileHeight)
    {
        float x1 = textureX / totalTextureFileWidth;
        float y1 = textureY / totalTextureFileHeight;
        float x2 = (textureX + textureWidth) / totalTextureFileWidth;
        float y2 = (textureY + textureHeight) / totalTextureFileHeight;
        YdmBlitUtil.customInnerBlit(ms.last().pose(), null, renderX, renderX + renderWidth, renderY, renderY + renderHeight, YdmBlitUtil.blitOffset, x2, y2, x1, y2, x1, y1, x2, y1);
    }
    
    public static void blit270Degree(PoseStack ms, float renderX, float renderY, float renderWidth, float renderHeight, float textureX, float textureY, float textureWidth, float textureHeight, int totalTextureFileWidth, int totalTextureFileHeight)
    {
        float x1 = textureX / totalTextureFileWidth;
        float y1 = textureY / totalTextureFileHeight;
        float x2 = (textureX + textureWidth) / totalTextureFileWidth;
        float y2 = (textureY + textureHeight) / totalTextureFileHeight;
        YdmBlitUtil.customInnerBlit(ms.last().pose(), null, renderX, renderX + renderWidth, renderY, renderY + renderHeight, YdmBlitUtil.blitOffset, x1, y2, x1, y1, x2, y1, x2, y2);
    }
    
    // --- Texture-aware methods ---
    
    public static void fullBlit(PoseStack ms, Identifier texture, float renderX, float renderY, float renderWidth, float renderHeight)
    {
        YdmBlitUtil.blit(ms, texture, renderX, renderY, renderWidth, renderHeight, 0, 0, 1, 1, 1, 1);
    }
    
    public static void fullBlit90Degree(PoseStack ms, Identifier texture, float renderX, float renderY, float renderWidth, float renderHeight)
    {
        YdmBlitUtil.blit90Degree(ms, texture, renderX, renderY, renderWidth, renderHeight, 0, 0, 1, 1, 1, 1);
    }
    
    public static void fullBlit180Degree(PoseStack ms, Identifier texture, float renderX, float renderY, float renderWidth, float renderHeight)
    {
        YdmBlitUtil.blit180Degree(ms, texture, renderX, renderY, renderWidth, renderHeight, 0, 0, 1, 1, 1, 1);
    }
    
    public static void fullBlit270Degree(PoseStack ms, Identifier texture, float renderX, float renderY, float renderWidth, float renderHeight)
    {
        YdmBlitUtil.blit270Degree(ms, texture, renderX, renderY, renderWidth, renderHeight, 0, 0, 1, 1, 1, 1);
    }
    
    public static void blit(PoseStack ms, Identifier texture, float renderX, float renderY, float renderWidth, float renderHeight, float textureX, float textureY, float textureWidth, float textureHeight, int totalTextureFileWidth, int totalTextureFileHeight)
    {
        YdmBlitUtil.customInnerBlit(ms.last().pose(), texture, renderX, renderX + renderWidth, renderY, renderY + renderHeight, YdmBlitUtil.blitOffset, textureX / totalTextureFileWidth, (textureX + textureWidth) / totalTextureFileWidth, textureY / totalTextureFileHeight, (textureY + textureHeight) / totalTextureFileHeight);
    }
    
    public static void blit90Degree(PoseStack ms, Identifier texture, float renderX, float renderY, float renderWidth, float renderHeight, float textureX, float textureY, float textureWidth, float textureHeight, int totalTextureFileWidth, int totalTextureFileHeight)
    {
        float x1 = textureX / totalTextureFileWidth;
        float y1 = textureY / totalTextureFileHeight;
        float x2 = (textureX + textureWidth) / totalTextureFileWidth;
        float y2 = (textureY + textureHeight) / totalTextureFileHeight;
        YdmBlitUtil.customInnerBlit(ms.last().pose(), texture, renderX, renderX + renderWidth, renderY, renderY + renderHeight, YdmBlitUtil.blitOffset, x2, y1, x2, y2, x1, y2, x1, y1);
    }
    
    public static void blit180Degree(PoseStack ms, Identifier texture, float renderX, float renderY, float renderWidth, float renderHeight, float textureX, float textureY, float textureWidth, float textureHeight, int totalTextureFileWidth, int totalTextureFileHeight)
    {
        float x1 = textureX / totalTextureFileWidth;
        float y1 = textureY / totalTextureFileHeight;
        float x2 = (textureX + textureWidth) / totalTextureFileWidth;
        float y2 = (textureY + textureHeight) / totalTextureFileHeight;
        YdmBlitUtil.customInnerBlit(ms.last().pose(), texture, renderX, renderX + renderWidth, renderY, renderY + renderHeight, YdmBlitUtil.blitOffset, x2, y2, x1, y2, x1, y1, x2, y1);
    }
    
    public static void blit270Degree(PoseStack ms, Identifier texture, float renderX, float renderY, float renderWidth, float renderHeight, float textureX, float textureY, float textureWidth, float textureHeight, int totalTextureFileWidth, int totalTextureFileHeight)
    {
        float x1 = textureX / totalTextureFileWidth;
        float y1 = textureY / totalTextureFileHeight;
        float x2 = (textureX + textureWidth) / totalTextureFileWidth;
        float y2 = (textureY + textureHeight) / totalTextureFileHeight;
        YdmBlitUtil.customInnerBlit(ms.last().pose(), texture, renderX, renderX + renderWidth, renderY, renderY + renderHeight, YdmBlitUtil.blitOffset, x1, y2, x1, y1, x2, y1, x2, y2);
    }
    
    // --- Masked blit (uses blend functions, still valid) ---
    
    // see https://github.com/CAS-ual-TY/UsefulCodeBitsForTheBlocksGame/blob/main/src/main/java/com/example/examplemod/client/screen/BlitUtil.java
    // use full mask (64x64) for 16x16 texture
    public static void advancedMaskedBlit(PoseStack ms, float renderX, float renderY, float renderWidth, float renderHeight, Runnable maskBinderAndDrawer, Runnable textureBinderAndDrawer, boolean inverted)
    {
        ScreenUtil.white();
        RenderSystem.enableBlend();
        
        // We want a blendfunc that doesn't change the color of any pixels,
        // but rather replaces the framebuffer alpha values with values based
        // on the whiteness of the mask. In other words, if a pixel is white in the mask,
        // then the corresponding framebuffer pixel's alpha will be set to 1.
        RenderSystem.blendFuncSeparate(SourceFactor.ZERO, DestFactor.ONE, SourceFactor.SRC_ALPHA, DestFactor.ZERO);
        
        // Now "draw" the mask (again, this doesn't produce a visible result, it just
        // changes the alpha values in the framebuffer)
        maskBinderAndDrawer.run();
        
        // Finally, we want a blendfunc that makes the foreground visible only in
        // areas with high alpha.
        
        if(!inverted)
        {
            RenderSystem.blendFuncSeparate(SourceFactor.ONE_MINUS_DST_ALPHA, DestFactor.DST_COLOR, SourceFactor.DST_ALPHA, DestFactor.ONE_MINUS_DST_ALPHA);
        }
        else
        {
            RenderSystem.blendFuncSeparate(SourceFactor.DST_ALPHA, DestFactor.DST_COLOR, SourceFactor.ONE_MINUS_DST_ALPHA, DestFactor.DST_ALPHA);
        }
        
        textureBinderAndDrawer.run();
        
        RenderSystem.disableBlend();
    }
    
    // --- Inner blit using new 1.21.11 rendering pipeline ---
    
    protected static void customInnerBlit(Matrix4f matrix, @Nullable Identifier texture, float posX1, float posX2, float posY1, float posY2, float posZ, float texX1, float texX2, float texY1, float texY2)
    {
        YdmBlitUtil.customInnerBlit(matrix, texture, posX1, posX2, posY1, posY2, posZ, texX1, texY1, texX2, texY1, texX2, texY2, texX1, texY2);
    }
    
    protected static void customInnerBlit(Matrix4f matrix, @Nullable Identifier texture, float posX1, float posX2, float posY1, float posY2, float posZ, float topLeftX, float topLeftY, float topRightX, float topRightY, float botRightX, float botRightY, float botLeftX, float botLeftY)
    {
        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferbuilder.addVertex(matrix, posX1, posY2, posZ).setUv(botLeftX, botLeftY).setColor(1.0f, 1.0f, 1.0f, 1.0f);
        bufferbuilder.addVertex(matrix, posX2, posY2, posZ).setUv(botRightX, botRightY).setColor(1.0f, 1.0f, 1.0f, 1.0f);
        bufferbuilder.addVertex(matrix, posX2, posY1, posZ).setUv(topRightX, topRightY).setColor(1.0f, 1.0f, 1.0f, 1.0f);
        bufferbuilder.addVertex(matrix, posX1, posY1, posZ).setUv(topLeftX, topLeftY).setColor(1.0f, 1.0f, 1.0f, 1.0f);
        MeshData meshData = bufferbuilder.buildOrThrow();
        if(texture != null)
        {
            RenderTypes.guiTextured(texture).draw(meshData);
        }
        else
        {
            BufferUploader.drawWithShader(meshData);
        }
    }
    
    public interface FullBlitMethod
    {
        void fullBlit(PoseStack ms, float x, float y, float width, float height);
    }
}
