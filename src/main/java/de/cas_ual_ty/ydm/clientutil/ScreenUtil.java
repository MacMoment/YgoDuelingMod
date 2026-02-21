package de.cas_ual_ty.ydm.clientutil;

import com.mojang.blaze3d.vertex.*;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix3x2fStack;
import org.joml.Matrix4f;

import java.util.List;

public class ScreenUtil
{
    public static void drawLineRect(Matrix3x2fStack ms, float x, float y, float w, float h, float lineWidth, float r, float g, float b, float a)
    {
        ScreenUtil.drawRect(ms, x, y, w, lineWidth, r, g, b, a); //top
        ScreenUtil.drawRect(ms, x, y + h - lineWidth, w, lineWidth, r, g, b, a); //bot
        ScreenUtil.drawRect(ms, x, y, lineWidth, h, r, g, b, a); //left
        ScreenUtil.drawRect(ms, x + w - lineWidth, y, lineWidth, h, r, g, b, a); //right
    }

    public static void drawRect(Matrix3x2fStack ms, float x, float y, float w, float h, float r, float g, float b, float a)
    {
        // Embed 2D matrix into 4x4 for vertex buffer usage
        Matrix4f m = new Matrix4f();
        m.m00(ms.m00).m01(ms.m01);
        m.m10(ms.m10).m11(ms.m11);
        m.m30(ms.m20).m31(ms.m21);

        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferbuilder.addVertex(m, x, y + h, 0F).setColor(r, g, b, a); // BL
        bufferbuilder.addVertex(m, x + w, y + h, 0F).setColor(r, g, b, a); // BR
        bufferbuilder.addVertex(m, x + w, y, 0F).setColor(r, g, b, a); // TR
        bufferbuilder.addVertex(m, x, y, 0F).setColor(r, g, b, a); // TL
        RenderTypes.gui().draw(bufferbuilder.buildOrThrow());
    }

    public static void drawRect(GuiGraphics guiGraphics, float x, float y, float w, float h, float r, float g, float b, float a)
    {
        drawRect(guiGraphics.pose(), x, y, w, h, r, g, b, a);
    }

    public static void drawSplitString(GuiGraphics guiGraphics, Font fontRenderer, List<Component> list, float x, float y, int maxWidth, int color)
    {
        for(Component t : list)
        {
            if(t.getString().isEmpty() && t.getSiblings().isEmpty())
            {
                y += fontRenderer.lineHeight;
            }
            else
            {
                for(FormattedCharSequence p : fontRenderer.split(t, maxWidth))
                {
                    guiGraphics.drawString(fontRenderer, p, (int) x, (int) y, color, true);
                    y += fontRenderer.lineHeight;
                }
            }
        }
    }

    public static void renderHoverRect(Matrix3x2fStack ms, float x, float y, float w, float h)
    {
        // In 1.21.11 colorMask is removed; just draw the semi-transparent overlay
        ScreenUtil.drawRect(ms, x, y, w, h, 1F, 1F, 1F, 0.5F);
    }

    public static void renderHoverRect(GuiGraphics guiGraphics, float x, float y, float w, float h)
    {
        renderHoverRect(guiGraphics.pose(), x, y, w, h);
    }

    public static void renderDisabledRect(Matrix3x2fStack ms, float x, float y, float w, float h)
    {
        ScreenUtil.drawRect(ms, x, y, w, h, 0F, 0F, 0F, 0.5F);
    }

    public static void renderDisabledRect(GuiGraphics guiGraphics, float x, float y, float w, float h)
    {
        renderDisabledRect(guiGraphics.pose(), x, y, w, h);
    }

    public static void white()
    {
        // RenderSystem.setShaderColor removed in 1.21.11; no-op (color is 1,1,1,1 by default)
    }
}
