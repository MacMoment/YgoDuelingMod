package de.cas_ual_ty.ydm.clientutil;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.joml.Matrix3x2fStack;
import org.joml.Matrix4f;

import javax.annotation.Nullable;

public class YdmBlitUtil
{
    static float blitOffset = 0;

    /**
     * Convert the current 2D GUI matrix stack to a 4x4 matrix for vertex buffers.
     * Embeds the 2D affine transform into the top-left 2x2 with the translation in column 3.
     * Note: new Matrix4f() initializes to identity (m22=1, m33=1), so Z/W dimensions are correct.
     */
    private static Matrix4f toMat4(Matrix3x2fStack ms)
    {
        Matrix4f m = new Matrix4f(); // identity: m22=1, m33=1, others=0
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
    // Note: In 1.21.11 GlStateManager and RenderSystem blend methods were removed;
    // using direct LWJGL OpenGL calls for blend control.

    public static void advancedMaskedBlit(Matrix3x2fStack ms, float renderX, float renderY, float renderWidth, float renderHeight, Runnable maskBinderAndDrawer, Runnable textureBinderAndDrawer, boolean inverted)
    {
        ScreenUtil.white();
        GL11.glEnable(GL11.GL_BLEND);

        // Draw mask to set framebuffer alpha values
        GL14.glBlendFuncSeparate(GL11.GL_ZERO, GL11.GL_ONE, GL11.GL_SRC_ALPHA, GL11.GL_ZERO);
        maskBinderAndDrawer.run();

        if(!inverted)
        {
            GL14.glBlendFuncSeparate(GL11.GL_ONE_MINUS_DST_ALPHA, GL11.GL_DST_COLOR, GL11.GL_DST_ALPHA, GL11.GL_ONE_MINUS_DST_ALPHA);
        }
        else
        {
            GL14.glBlendFuncSeparate(GL11.GL_DST_ALPHA, GL11.GL_DST_COLOR, GL11.GL_ONE_MINUS_DST_ALPHA, GL11.GL_DST_ALPHA);
        }

        textureBinderAndDrawer.run();

        GL11.glDisable(GL11.GL_BLEND);
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
            RenderType.gui().draw(meshData);
        }
    }

    public interface FullBlitMethod
    {
        void fullBlit(Matrix3x2fStack ms, float x, float y, float width, float height);
    }
}
