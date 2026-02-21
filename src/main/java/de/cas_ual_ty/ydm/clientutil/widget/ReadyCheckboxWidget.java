package de.cas_ual_ty.ydm.clientutil.widget;

import de.cas_ual_ty.ydm.clientutil.ScreenUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.function.Supplier;

public class ReadyCheckboxWidget extends Button
{
    public Supplier<Boolean> isChecked;
    public Supplier<Boolean> isActive;
    
    public ReadyCheckboxWidget(int xIn, int yIn, int widthIn, int heightIn, String msg, OnPress onPress, Supplier<Boolean> isChecked, Supplier<Boolean> isActive)
    {
        super(xIn, yIn, widthIn, heightIn, Component.empty(), onPress, Button.DEFAULT_NARRATION);
        this.isChecked = isChecked;
        this.isActive = isActive;
    }
    
    @Override
    public void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float p_renderButton_3_)
    {
        active = isActive.get();
        Minecraft minecraft = Minecraft.getInstance();
        ScreenUtil.white();
        if(isChecked.get())
        {
            int j = getFGColor();
            guiGraphics.drawCenteredString(minecraft.font, Component.literal("✔"), getX() + width / 2, getY() + (height - 8) / 2, j | Mth.ceil(alpha * 255.0F) << 24);
        }
    }
}