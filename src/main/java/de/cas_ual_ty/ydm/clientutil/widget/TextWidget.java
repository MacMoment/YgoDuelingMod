package de.cas_ual_ty.ydm.clientutil.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.function.Supplier;

public class TextWidget extends AbstractWidget
{
    public Supplier<Component> msgGetter;
    public ITooltip tooltip;
    
    public TextWidget(int xIn, int yIn, int widthIn, int heightIn, Supplier<Component> msgGetter, ITooltip tooltip)
    {
        super(xIn, yIn, widthIn, heightIn, Component.empty());
        this.msgGetter = msgGetter;
        active = false;
        this.tooltip = tooltip;
    }
    
    public TextWidget(int xIn, int yIn, int widthIn, int heightIn, Supplier<Component> msgGetter)
    {
        this(xIn, yIn, widthIn, heightIn, msgGetter, null);
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
    {
        Minecraft minecraft = Minecraft.getInstance();
        Font fontrenderer = minecraft.font;
        int bgColor = isActive() ? (isHoveredOrFocused() ? 0xA0808080 : 0xA0606060) : 0xA0404040;
        guiGraphics.fill(getX(), getY(), getX() + width, getY() + height, bgColor);
        int j = getFGColor();
        guiGraphics.drawCenteredString(fontrenderer, getMessage(), getX() + width / 2, getY() + (height - 8) / 2, j | Mth.ceil(alpha * 255.0F) << 24);
        
        if(isHoveredOrFocused() && tooltip != null)
        {
            tooltip.onTooltip(this, guiGraphics, mouseX, mouseY);
        }
    }
    
    @Override
    public int getFGColor()
    {
        return 16777215; //From super
    }
    
    @Override
    public Component getMessage()
    {
        return msgGetter.get();
    }
    
    @Override
    public void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput)
    {
    
    }
}
