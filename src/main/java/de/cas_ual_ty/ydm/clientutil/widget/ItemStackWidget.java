package de.cas_ual_ty.ydm.clientutil.widget;

import de.cas_ual_ty.ydm.YdmItems;
import de.cas_ual_ty.ydm.card.CardHolder;
import de.cas_ual_ty.ydm.clientutil.YdmBlitUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;


public class ItemStackWidget extends AbstractWidget
{
    public ItemStack itemStack;
    public ItemRenderer itemRenderer;
    public Identifier replacement;
    
    public ItemStackWidget(int xIn, int yIn, int size, ItemRenderer itemRenderer, Identifier replacement)
    {
        super(xIn, yIn, size, size, Component.empty());
        itemStack = ItemStack.EMPTY;
        this.itemRenderer = itemRenderer;
        this.replacement = replacement;
    }
    
    public ItemStackWidget setItemStack(ItemStack itemStack)
    {
        this.itemStack = itemStack;
        return this;
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial)
    {
        Identifier rl = replacement;
        
        if(!itemStack.isEmpty())
        {
            if(itemStack.getItem() == YdmItems.CARD.get())
            {
                CardHolder c = YdmItems.CARD.get().getCardHolder(itemStack);
                
                if(c.getCard() != null)
                {
                    rl = c.getMainImageResourceLocation();
                }
            }
            else
            {
                // Use GuiGraphics to render the item (modern 1.21+ approach)
                guiGraphics.renderItem(itemStack, getX(), getY());
                return;
            }
        }
        
        YdmBlitUtil.fullBlit(guiGraphics.pose(), rl, getX(), getY(), width, height);
    }
    
    @Override
    public void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput)
    {
    
    }
}