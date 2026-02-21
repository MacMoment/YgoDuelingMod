package de.cas_ual_ty.ydm.clientutil.widget;

import de.cas_ual_ty.ydm.clientutil.YdmBlitUtil;
import net.minecraft.client.gui.GuiGraphics;
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
        super(x, y, width, height, title, pressedAction, Button.DEFAULT_NARRATION);
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
    public void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
    {
        if(textureLocation != null)
        {
            YdmBlitUtil.blit(guiGraphics.pose(), textureLocation, getX(), getY(), width, height, texX, texY, texW, texH, 256, 256);
        }
        
        if(isHoveredOrFocused())
        {
            renderToolTip(guiGraphics, mouseX, mouseY);
        }
    }
}
