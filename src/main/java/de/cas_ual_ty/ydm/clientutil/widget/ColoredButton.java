package de.cas_ual_ty.ydm.clientutil.widget;

import de.cas_ual_ty.ydm.YDM;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;


public class ColoredButton extends Button
{
    public static final Identifier RESOURCE = Identifier.fromNamespaceAndPath(YDM.MOD_ID, "textures/gui/colored_button.png");
    
    public int offset;
    
    public ColoredButton(int x, int y, int width, int height, Component title, OnPress pressedAction)
    {
        super(x, y, width, height, title, pressedAction, Button.DEFAULT_NARRATION);
        offset = 0;
    }
    
    @Override
    public void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
    {
        Minecraft minecraft = Minecraft.getInstance();
        Font fontrenderer = minecraft.font;
        int bgColor;
        if(offset == 0)
        {
            bgColor = isActive() ? (isHoveredOrFocused() ? 0xA08080FF : 0xA06060CC) : 0xA0404099;
        }
        else
        {
            bgColor = isActive() ? (isHoveredOrFocused() ? 0xA0FF8080 : 0xA0CC6060) : 0xA0994040;
        }
        guiGraphics.fill(getX(), getY(), getX() + width, getY() + height, bgColor);
        
        int j = getFGColor();
        guiGraphics.drawCenteredString(fontrenderer, getMessage(), getX() + width / 2, getY() + (height - 8) / 2, j | Mth.ceil(alpha * 255.0F) << 24);
        
        if(isHoveredOrFocused())
        {
            renderToolTip(guiGraphics, mouseX, mouseY);
        }
    }
    
    public ColoredButton setBlue()
    {
        offset = 0;
        return this;
    }
    
    public ColoredButton setRed()
    {
        offset = 60;
        return this;
    }
}
