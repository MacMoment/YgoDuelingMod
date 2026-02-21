package de.cas_ual_ty.ydm.duel.screen;

import de.cas_ual_ty.ydm.duel.DuelContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class DuelScreenBase<E extends DuelContainer> extends DuelContainerScreen<E>
{
    public DuelScreenBase(E screenContainer, Inventory inv, Component titleIn)
    {
        super(screenContainer, inv, titleIn);
    }
    
    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int x, int y)
    {
        guiGraphics.drawString(font, "Waiting for server...", 8, 6, 0x404040);
    }
}
