package de.cas_ual_ty.ydm.duel.screen.widget;


import org.joml.Matrix3x2fStack;
import de.cas_ual_ty.ydm.clientutil.ScreenUtil;
import de.cas_ual_ty.ydm.duel.playfield.Zone;
import de.cas_ual_ty.ydm.duel.screen.IDuelScreenContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.function.Consumer;

public class StackZoneWidget extends ZoneWidget
{
    // this does not render counters
    
    public StackZoneWidget(Zone zone, IDuelScreenContext context, int width, int height, Component title, Consumer<ZoneWidget> onPress)
    {
        super(zone, context, width, height, title, onPress);
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
    {
        Matrix3x2fStack ms = guiGraphics.pose();
        Minecraft minecraft = Minecraft.getInstance();
        Font fontrenderer = minecraft.font;
        
        
        
        
        
        
        renderZoneSelectRect(ms, zone, x, y, width, height);
        
        hoverCard = renderCards(ms, mouseX, mouseY);
        
        
        
        if(zone.getCardsAmount() > 0)
        {
            // see font renderer, top static Vector3f
            // white is translated in front by that
            ms.pushPose();
            Screen.drawCenteredString(ms, fontrenderer, Component.literal(String.valueOf(zone.getCardsAmount())),
                    x + width / 2, y + height / 2 - fontrenderer.lineHeight / 2,
                    16777215 | Mth.ceil(alpha * 255.0F) << 24);
            ms.popPose();
        }
        
        if(active)
        {
            if(isHoveredOrFocused())
            {
                if(zone.getCardsAmount() == 0)
                {
                    ScreenUtil.renderHoverRect(ms, x, y, width, height);
                }
                
                renderToolTip(guiGraphics, mouseX, mouseY);
            }
        }
        else
        {
            ScreenUtil.renderDisabledRect(ms, x, y, width, height);
        }
    }
    
    @Override
    public boolean openAdvancedZoneView()
    {
        return !zone.getType().getIsSecret() && zone.getCardsAmount() > 0;
    }
}
