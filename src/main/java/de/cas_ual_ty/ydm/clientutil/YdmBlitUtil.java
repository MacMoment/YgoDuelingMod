package de.cas_ual_ty.ydm.clientutil;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.Identifier;
import org.joml.Matrix3x2fStack;
import org.joml.Matrix4f;

import javax.annotation.Nullable;

public class YdmBlitUtil
{
    static float blitOffset = 0;

    /**
     * Convert the current 2D GUI matrix stack to a 4x4 matrix for vertex buffers.
     * Embeds the 2D affine transform into the top-left 2x2 with the translation in column 3.
     */
    private static Matrix4f toMat4(Matrix3x2fStack ms)
    {
        Matrix4f m = new Matrix4f();
        m.m00(ms.m00).m01(ms.m01);
        m.m10(ms.m10).m11(ms.m11);
        m.m30(ms.m20).m31(ms.m21);
        return m;
    }

    // --- No-texture methods (backward compat for masked blit contexts) ---

    public static void fullBlit(Matrix3x2fStack ms, float renderX, float renderY, float renderWidth, float renderHeight)
    {
        YdmBlitUtil.blit(ms, renderX, renderY, renderWidth, renderHeight, 0, 0, 1, 1, 1, 1);
    }

    public static void fullBlit90Degree(Matrix3x2fStack ms, float renderX, float renderY, float renderWidth, float renderHeight)
    {
        YdmBlitUtil.blit90Degree(ms, renderX, renderY, renderWidth, renderHeight, 0, 0, 1, 1, 1, 1);
    }

    public static void fullBlit180Degree(Matrix3x2fStack ms, float renderX, float renderY, float renderWidth, float renderHeight)
    {
        YdmBlitUtil.blit180Degree(ms, renderX, renderY, renderWidth, renderHeight, 0, 0, 1, 1, 1, 1);
    }

    public static void fullBlit270Degree(Matrix3x2fStack ms, float renderX, float renderY, float renderWidth, float renderHeight)
    {
        YdmBlitUtil.blit270Degree(ms, renderX, renderY, renderWidth, renderHeight, 0, 0, 1, 1, 1, 1);
    }

    /**
     * Param 1-4: Where to and how big to draw on the screen
     * Param 5-8: What part of the texture file to cut out and draw
     * Param 9-10: How big the entire texture file is in general (pow2 only)
     */
    public static void blit(Matrix3x2fStack ms, float renderX, float renderY, float renderWidth, float renderHeight, float textureX, float textureY, float textureWidth, float textureHeight, int totalTextureFileWidth, int totalTextureFileHeight)
    {
        YdmBlitUtil.customInnerBlit(toMat4(ms), null, renderX, renderX + renderWidth, renderY, renderY + renderHeight, YdmBlitUtil.blitOffset, textureX / totalTextureFileWidth, (textureX + textureWidth) / totalTextureFileWidth, textureY / totalTextureFileHeight, (textureY + textureHeight) / totalTextureFileHeight);
    }

    public static void blit90Degree(Matrix3x2fStack ms, float renderX, float renderY, float renderWidth, float renderHeight, float textureX, float textureY, float textureWidth, float textureHeight, int totalTextureFileWidth, int totalTextureFileHeight)
    {
        float x1 = textureX / totalTextureFileWidth;
        float y1 = textureY / totalTextureFileHeight;
        float x2 = (textureX + textureWidth) / totalTextureFileWidth;
        float y2 = (textureY + textureHeight) / totalTextureFileHeight;
        YdmBlitUtil.customInnerBlit(toMat4(ms), null, renderX, renderX + renderWidth, renderY, renderY + renderHeight, YdmBlitUtil.blitOffset, x2, y1, x2, y2, x1, y2, x1, y1);
    }

    public static void blit180Degree(Matrix3x2fStack ms, float renderX, float renderY, float renderWidth, float renderHeight, float textureX, float textureY, float textureWidth, float textureHeight, int totalTextureFileWidth, int totalTextureFileHeight)
    {
        float x1 = textureX / totalTextureFileWidth;
        float y1 = textureY / totalTextureFileHeight;
        float x2 = (textureX + textureWidth) / totalTextureFileWidth;
        float y2 = (textureY + textureHeight) / totalTextureFileHeight;
        YdmBlitUtil.customInnerBlit(toMat4(ms), null, renderX, renderX + renderWidth, renderY, renderY + renderHeight, YdmBlitUtil.blitOffset, x2, y2, x1, y2, x1, y1, x2, y1);
    }

    public static void blit270Degree(Matrix3x2fStack ms, float renderX, float renderY, float renderWidth, float renderHeight, float textureX, float textureY, float textureWidth, float textureHeight, int totalTextureFileWidth, int totalTextureFileHeight)
    {
        float x1 = textureX / totalTextureFileWidth;
        float y1 = textureY / totalTextureFileHeight;
        float x2 = (textureX + textureWidth) / totalTextureFileWidth;
        float y2 = (textureY + textureHeight) / totalTextureFileHeight;
        YdmBlitUtil.customInnerBlit(toMat4(ms), null, renderX, renderX + renderWidth, renderY, renderY + renderHeight, YdmBlitUtil.blitOffset, x1, y2, x1, y1, x2, y1, x2, y2);
    }

    // --- Texture-aware methods ---

    public static void fullBlit(Matrix3x2fStack ms, Identifier texture, float renderX, float renderY, float renderWidth, float renderHeight)
    {
        YdmBlitUtil.blit(ms, texture, renderX, renderY, renderWidth, renderHeight, 0, 0, 1, 1, 1, 1);
    }

    public static void fullBlit90Degree(Matrix3x2fStack ms, Identifier texture, float renderX, float renderY, float renderWidth, float renderHeight)
    {
        YdmBlitUtil.blit90Degree(ms, texture, renderX, renderY, renderWidth, renderHeight, 0, 0, 1, 1, 1, 1);
    }

    public static void fullBlit180Degree(Matrix3x2fStack ms, Identifier texture, float renderX, float renderY, float renderWidth, float renderHeight)
    {
        YdmBlitUtil.blit180Degree(ms, texture, renderX, renderY, renderWidth, renderHeight, 0, 0, 1, 1, 1, 1);
    }

    public static void fullBlit270Degree(Matrix3x2fStack ms, Identifier texture, float renderX, float renderY, float renderWidth, float renderHeight)
    {
        YdmBlitUtil.blit270Degree(ms, texture, renderX, renderY, renderWidth, renderHeight, 0, 0, 1, 1, 1, 1);
    }

    public static void blit(Matrix3x2fStack ms, Identifier texture, float renderX, float renderY, float renderWidth, float renderHeight, float textureX, float textureY, float textureWidth, float textureHeight, int totalTextureFileWidth, int totalTextureFileHeight)
    {
        YdmBlitUtil.customInnerBlit(toMat4(ms), texture, renderX, renderX + renderWidth, renderY, renderY + renderHeight, YdmBlitUtil.blitOffset, textureX / totalTextureFileWidth, (textureX + textureWidth) / totalTextureFileWidth, textureY / totalTextureFileHeight, (textureY + textureHeight) / totalTextureFileHeight);
    }

    public static void blit90Degree(Matrix3x2fStack ms, Identifier texture, float renderX, float renderY, float renderWidth, float renderHeight, float textureX, float textureY, float textureWidth, float textureHeight, int totalTextureFileWidth, int totalTextureFileHeight)
    {
        float x1 = textureX / totalTextureFileWidth;
        float y1 = textureY / totalTextureFileHeight;
        float x2 = (textureX + textureWidth) / totalTextureFileWidth;
        float y2 = (textureY + textureHeight) / totalTextureFileHeight;
        YdmBlitUtil.customInnerBlit(toMat4(ms), texture, renderX, renderX + renderWidth, renderY, renderY + renderHeight, YdmBlitUtil.blitOffset, x2, y1, x2, y2, x1, y2, x1, y1);
    }

    public static void blit180Degree(Matrix3x2fStack ms, Identifier texture, float renderX, float renderY, float renderWidth, float renderHeight, float textureX, float textureY, float textureWidth, float textureHeight, int totalTextureFileWidth, int totalTextureFileHeight)
    {
        float x1 = textureX / totalTextureFileWidth;
        float y1 = textureY / totalTextureFileHeight;
        float x2 = (textureX + textureWidth) / totalTextureFileWidth;
        float y2 = (textureY + textureHeight) / totalTextureFileHeight;
        YdmBlitUtil.customInnerBlit(toMat4(ms), texture, renderX, renderX + renderWidth, renderY, renderY + renderHeight, YdmBlitUtil.blitOffset, x2, y2, x1, y2, x1, y1, x2, y1);
    }

    public static void blit270Degree(Matrix3x2fStack ms, Identifier texture, float renderX, float renderY, float renderWidth, float renderHeight, float textureX, float textureY, float textureWidth, float textureHeight, int totalTextureFileWidth, int totalTextureFileHeight)
    {
        float x1 = textureX / totalTextureFileWidth;
        float y1 = textureY / totalTextureFileHeight;
        float x2 = (textureX + textureWidth) / totalTextureFileWidth;
        float y2 = (textureY + textureHeight) / totalTextureFileHeight;
        YdmBlitUtil.customInnerBlit(toMat4(ms), texture, renderX, renderX + renderWidth, renderY, renderY + renderHeight, YdmBlitUtil.blitOffset, x1, y2, x1, y1, x2, y1, x2, y2);
    }

    // --- Masked blit ---
    // Note: In 1.21.11 RenderSystem blend methods were removed; using GlStateManager directly.
    // The visual mask effect requires direct GL blend control.

    public static void advancedMaskedBlit(Matrix3x2fStack ms, float renderX, float renderY, float renderWidth, float renderHeight, Runnable maskBinderAndDrawer, Runnable textureBinderAndDrawer, boolean inverted)
    {
        ScreenUtil.white();
        GlStateManager._enableBlend();

        // Draw mask to set framebuffer alpha values
        GlStateManager._blendFuncSeparate(
            GlStateManager.SourceFactor.ZERO.value, GlStateManager.DestFactor.ONE.value,
            GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ZERO.value);
        maskBinderAndDrawer.run();

        if(!inverted)
        {
            GlStateManager._blendFuncSeparate(
                GlStateManager.SourceFactor.ONE_MINUS_DST_ALPHA.value, GlStateManager.DestFactor.DST_COLOR.value,
                GlStateManager.SourceFactor.DST_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_DST_ALPHA.value);
        }
        else
        {
            GlStateManager._blendFuncSeparate(
                GlStateManager.SourceFactor.DST_ALPHA.value, GlStateManager.DestFactor.DST_COLOR.value,
                GlStateManager.SourceFactor.ONE_MINUS_DST_ALPHA.value, GlStateManager.DestFactor.DST_ALPHA.value);
        }

        textureBinderAndDrawer.run();

        GlStateManager._disableBlend();
    }

    // --- Inner blit using 1.21.11 rendering pipeline ---

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
            RenderType.guiTextured(texture).draw(meshData);
        }
        else
        {
            BufferUploader.drawWithShader(meshData);
        }
    }

    public interface FullBlitMethod
    {
        void fullBlit(Matrix3x2fStack ms, float x, float y, float width, float height);
    }
}
