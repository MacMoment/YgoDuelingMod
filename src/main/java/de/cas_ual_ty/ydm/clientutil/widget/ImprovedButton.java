package de.cas_ual_ty.ydm.clientutil.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class ImprovedButton extends Button
{
    public ImprovedButton(int x, int y, int width, int height, Component title, Button.OnPress pressedAction)
    {
        super(x, y, width, height, title, pressedAction, Button.DEFAULT_NARRATION);
    }
    
    @Override
    public void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
    {
        Minecraft minecraft = Minecraft.getInstance();
        Font fontrenderer = minecraft.font;
        
        int j = getFGColor();
        guiGraphics.drawCenteredString(fontrenderer, getMessage(), getX() + width / 2, getY() + (height - 8) / 2, j | Mth.ceil(alpha * 255.0F) << 24);
        
        if(isHoveredOrFocused())
        {
            renderToolTip(guiGraphics, mouseX, mouseY);
        }
    }
}
