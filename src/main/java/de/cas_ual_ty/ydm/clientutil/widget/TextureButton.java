package de.cas_ual_ty.ydm.clientutil.widget;

import de.cas_ual_ty.ydm.clientutil.YdmBlitUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;


public class TextureButton extends Button
{
    public Identifier textureLocation;
    
    public int texX;
    public int texY;
    public int texW;
    public int texH;
    
    public TextureButton(int x, int y, int width, int height, Component title, OnPress pressedAction)
    {
        super(x, y, width, height, title, pressedAction);
        textureLocation = null;
    }
    
    public TextureButton setTexture(Identifier textureLocation, int texX, int texY, int texW, int texH)
    {
        this.textureLocation = textureLocation;
        this.texX = texX;
        this.texY = texY;
        this.texW = texW;
        this.texH = texH;
        return this;
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
    {
        Minecraft minecraft = Minecraft.getInstance();
        Font fontrenderer = minecraft.font;
        // In 1.21.11, getYImage is removed; compute texture row from hover/active state
        int i = isActive() ? (isHoveredOrFocused() ? 2 : 1) : 0;
        guiGraphics.blit(AbstractWidget.WIDGETS_LOCATION, x, y, 0, 46 + i * 20, width / 2, height / 2);
        guiGraphics.blit(AbstractWidget.WIDGETS_LOCATION, x + width / 2, y, 200 - width / 2, 46 + i * 20, width / 2, height / 2);
        guiGraphics.blit(AbstractWidget.WIDGETS_LOCATION, x, y + height / 2, 0, 46 + (i + 1) * 20 - height / 2, width / 2, height / 2);
        guiGraphics.blit(AbstractWidget.WIDGETS_LOCATION, x + width / 2, y + height / 2, 200 - width / 2, 46 + (i + 1) * 20 - height / 2, width / 2, height / 2);
        
        if(textureLocation != null)
        {
            YdmBlitUtil.blit(guiGraphics.pose(), textureLocation, x, y, width, height, texX, texY, texW, texH, 256, 256);
        }
        
        if(isHoveredOrFocused())
        {
            renderToolTip(guiGraphics, mouseX, mouseY);
        }
    }
}
