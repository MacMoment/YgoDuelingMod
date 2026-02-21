package de.cas_ual_ty.ydm.card;

import com.mojang.blaze3d.platform.InputConstants;
import de.cas_ual_ty.ydm.clientutil.CardRenderUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;

public class InspectCardScreen extends Screen
{
    public final CardHolder cardHolder;
    
    public InspectCardScreen(Component pTitle, CardHolder cardHolder)
    {
        super(pTitle);
        this.cardHolder = cardHolder;
    }
    
    public InspectCardScreen(CardHolder cardHolder)
    {
        this(Component.empty(), cardHolder);
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick)
    {
        super.render(guiGraphics, pMouseX, pMouseY, pPartialTick);
        
        CardRenderUtil.renderInfoCardWithRarity(guiGraphics, pMouseX, pMouseY, width / 2, height / 2, 128, 128, cardHolder);
    }
    
    @Override
    public boolean keyPressed(KeyEvent event)
    {
        InputConstants.Key mouseKey = InputConstants.Type.KEYSYM.getOrCreate(event.key());
        
        if(minecraft.options.keyInventory.isActiveAndMatches(mouseKey))
        {
            onClose();
            return true;
        }
        
        return super.keyPressed(event);
    }
    
    @Override
    public void handleDelayedNarration()
    {
    }
    
    @Override
    public void triggerImmediateNarration(boolean p_169408_)
    {
    }
}
