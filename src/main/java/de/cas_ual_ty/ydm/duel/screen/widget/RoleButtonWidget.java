package de.cas_ual_ty.ydm.duel.screen.widget;

import org.joml.Matrix3x2fStack;
import de.cas_ual_ty.ydm.duel.PlayerRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

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
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial)
    {
        active = available.get();
        super.render(guiGraphics, mouseX, mouseY, partial);
    }
}