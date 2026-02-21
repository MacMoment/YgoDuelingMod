package de.cas_ual_ty.ydm.clientutil.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Widget;

public interface ITooltip
{
    void onTooltip(Widget widget, GuiGraphics guiGraphics, int mouseX, int mouseY);
}