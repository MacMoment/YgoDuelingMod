package de.cas_ual_ty.ydm.clientutil.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;

public interface ITooltip
{
    void onTooltip(Renderable widget, GuiGraphics guiGraphics, int mouseX, int mouseY);
}