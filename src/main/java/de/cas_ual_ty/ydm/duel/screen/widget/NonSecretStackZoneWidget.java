package de.cas_ual_ty.ydm.duel.screen.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import de.cas_ual_ty.ydm.duel.playfield.Zone;
import de.cas_ual_ty.ydm.duel.screen.IDuelScreenContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class NonSecretStackZoneWidget extends StackZoneWidget
{
    public NonSecretStackZoneWidget(Zone zone, IDuelScreenContext context, int width, int height, Component title, Consumer<ZoneWidget> onPress, OnTooltip onTooltip)
    {
        super(zone, context, width, height, title, onPress, onTooltip);
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
    {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTicks);
        hoverCard = null; // dont select top card when clicking on it, ever
    }
}
