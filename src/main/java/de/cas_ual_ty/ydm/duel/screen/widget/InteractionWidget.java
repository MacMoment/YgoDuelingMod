package de.cas_ual_ty.ydm.duel.screen.widget;


import org.joml.Matrix3x2fStack;
import de.cas_ual_ty.ydm.clientutil.ScreenUtil;
import de.cas_ual_ty.ydm.clientutil.YdmBlitUtil;
import de.cas_ual_ty.ydm.duel.action.ActionIcon;
import de.cas_ual_ty.ydm.duel.playfield.ZoneInteraction;
import de.cas_ual_ty.ydm.duel.screen.IDuelScreenContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class InteractionWidget extends Button
{
    public final ZoneInteraction interaction;
    public final IDuelScreenContext context;
    
    public InteractionWidget(ZoneInteraction interaction, IDuelScreenContext context, int x, int y, int width, int height, Component title, Consumer<InteractionWidget> onPress)
    {
        super(x, y, width, height, title, (w) -> onPress.accept((InteractionWidget) w));
        this.interaction = interaction;
        this.context = context;
    }
    
    public InteractionWidget(ZoneInteraction interaction, IDuelScreenContext context, int x, int y, int width, int height, Consumer<InteractionWidget> onPress)
    {
        super(x, y, width, height, interaction.icon.getLocal(), (w) -> onPress.accept((InteractionWidget) w));
        this.interaction = interaction;
        this.context = context;
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
    {
        Matrix3x2fStack ms = guiGraphics.pose();
        ms.pushPose();
        
        ActionIcon icon = interaction.icon;
        
        int iconWidth = icon.iconWidth;
        int iconHeight = icon.iconHeight;
        
        if(iconHeight >= height)
        {
            iconWidth = height * iconWidth / iconHeight;
            iconHeight = height;
        }
        
        if(iconWidth >= width)
        {
            iconHeight = width * iconHeight / iconWidth;
            iconWidth = width;
        }
        
        ScreenUtil.white();
        
        
        
        
        YdmBlitUtil.blit(ms, icon.sourceFile, x + (width - iconWidth) / 2, y + (height - iconHeight) / 2, iconWidth, iconHeight, icon.iconX, icon.iconY, icon.iconWidth, icon.iconHeight, icon.fileSize, icon.fileSize);
        
        if(isHoveredOrFocused() && active)
        {
            ScreenUtil.renderHoverRect(ms, x, y, width, height);
            renderToolTip(guiGraphics, mouseX, mouseY);
        }
        
        ms.popPose();
    }
}