package de.cas_ual_ty.ydm.duel.screen.widget;


import org.joml.Matrix3x2fStack;
import de.cas_ual_ty.ydm.YDM;
import de.cas_ual_ty.ydm.clientutil.widget.ITooltip;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

import java.util.function.Supplier;

public class LifePointsWidget extends AbstractWidget
{
    public static final Identifier DUEL_WIDGETS = Identifier.fromNamespaceAndPath(YDM.MOD_ID, "textures/gui/duel_widgets.png");
    
    public Supplier<Integer> lpGetter;
    public int maxLP;
    public ITooltip tooltip;
    
    public LifePointsWidget(int x, int y, int width, int height, Supplier<Integer> lpGetter, int maxLP, ITooltip tooltip)
    {
        super(x, y, width, height, Component.empty());
        this.lpGetter = lpGetter;
        this.maxLP = maxLP;
        this.tooltip = tooltip;
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
    {
        Matrix3x2fStack ms = guiGraphics.pose();
        Minecraft minecraft = Minecraft.getInstance();
        Font fontrenderer = minecraft.font;
        
        
        
        
        int lp = lpGetter.get();
        float relativeLP = Math.min(1F, lp / (float) maxLP);
        
        final int margin = 1;
        int x = this.getX();
        int y = this.getY();
        int w = width;
        int h = height;
        
        
        guiGraphics.blit(LifePointsWidget.DUEL_WIDGETS, x, y, 0, 1 * 8, width, height);
        guiGraphics.blit(LifePointsWidget.DUEL_WIDGETS, x, y, 0, 0, Mth.ceil(width * relativeLP), height);
        guiGraphics.blit(LifePointsWidget.DUEL_WIDGETS, x, y, 0, 2 * 8, width, height);
        // renderBg removed in 1.21.11; LP bar is fully rendered by the blit calls above
        
        x = this.getX() + width / 2;
        y = this.getY() + height / 2;
        
        ms.pushMatrix();
        
        ms.scale(0.5F, 0.5F);
        
        int j = getFGColor();
        guiGraphics.drawCenteredString(fontrenderer, Component.literal(String.valueOf(lp)), x * 2, y * 2 - fontrenderer.lineHeight / 2, j | Mth.ceil(alpha * 255.0F) << 24);
        
        ms.popMatrix();
        
        if(isHoveredOrFocused())
        {
            tooltip.onTooltip(this, guiGraphics, mouseX, mouseY);
        }
    }
    
    @Override
    public void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput)
    {
    
    }
}
