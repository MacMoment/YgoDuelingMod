package de.cas_ual_ty.ydm.clientutil.widget;

import de.cas_ual_ty.ydm.YDM;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

import java.util.function.Supplier;

public class ColoredTextWidget extends AbstractWidget
{
    public static final Identifier RESOURCE = Identifier.fromNamespaceAndPath(YDM.MOD_ID, "textures/gui/colored_button.png");
    
    public Supplier<Component> msgGetter;
    public ITooltip tooltip;
    public int offset;
    
    public ColoredTextWidget(int xIn, int yIn, int widthIn, int heightIn, Supplier<Component> msgGetter, ITooltip tooltip)
    {
        super(xIn, yIn, widthIn, heightIn, Component.empty());
        this.msgGetter = msgGetter;
        active = false;
        this.tooltip = tooltip;
        offset = 0;
    }
    
    public ColoredTextWidget(int xIn, int yIn, int widthIn, int heightIn, Supplier<Component> msgGetter)
    {
        this(xIn, yIn, widthIn, heightIn, msgGetter, null);
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
    {
        Minecraft minecraft = Minecraft.getInstance();
        Font fontrenderer = minecraft.font;
        // In 1.21.11, getYImage is removed; compute texture row from hover/active state
        int i = isActive() ? (isHoveredOrFocused() ? 2 : 1) : 0;
        guiGraphics.blit(ColoredTextWidget.RESOURCE, x, y, 0, offset + i * 20, width / 2, height / 2);
        guiGraphics.blit(ColoredTextWidget.RESOURCE, x + width / 2, y, 200 - width / 2, offset + i * 20, width / 2, height / 2);
        guiGraphics.blit(ColoredTextWidget.RESOURCE, x, y + height / 2, 0, offset + (i + 1) * 20 - height / 2, width / 2, height / 2);
        guiGraphics.blit(ColoredTextWidget.RESOURCE, x + width / 2, y + height / 2, 200 - width / 2, offset + (i + 1) * 20 - height / 2, width / 2, height / 2);
        int j = getFGColor();
        guiGraphics.drawCenteredString(fontrenderer, getMessage(), x + width / 2, y + (height - 8) / 2, j | Mth.ceil(alpha * 255.0F) << 24);
        
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
    
    public ColoredTextWidget setBlue()
    {
        offset = 0;
        return this;
    }
    
    public ColoredTextWidget setRed()
    {
        offset = 60;
        return this;
    }
    
    @Override
    public void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput)
    {
    
    }
}
