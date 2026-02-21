package de.cas_ual_ty.ydm.duel.screen.widget;

import de.cas_ual_ty.ydm.duel.PlayerRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.function.Supplier;

public class RoleButtonWidget extends Button
{
    public Supplier<Boolean> available;
    public PlayerRole role;
    
    public RoleButtonWidget(int xIn, int yIn, int widthIn, int heightIn, Component text, Button.OnPress onPress, Supplier<Boolean> available, PlayerRole role)
    {
        super(xIn, yIn, widthIn, heightIn, text, onPress, Button.DEFAULT_NARRATION);
        this.available = available;
        this.role = role;
    }
    
    @Override
    protected void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial)
    {
        active = available.get();
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, this.getFGColor() | Mth.ceil(this.alpha * 255.0F) << 24);
    }
}