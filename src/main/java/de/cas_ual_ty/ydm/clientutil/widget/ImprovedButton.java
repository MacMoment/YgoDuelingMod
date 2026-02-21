package de.cas_ual_ty.ydm.clientutil.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class ImprovedButton extends Button
{
    public ImprovedButton(int x, int y, int width, int height, Component title, Button.OnPress pressedAction)
    {
        super(x, y, width, height, title, pressedAction);
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
        
        int j = getFGColor();
        guiGraphics.drawCenteredString(fontrenderer, getMessage(), x + width / 2, y + (height - 8) / 2, j | Mth.ceil(alpha * 255.0F) << 24);
        
        if(isHoveredOrFocused())
        {
            renderToolTip(guiGraphics, mouseX, mouseY);
        }
    }
}
