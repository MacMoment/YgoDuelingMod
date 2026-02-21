package de.cas_ual_ty.ydm.deckbox;


import de.cas_ual_ty.ydm.YDM;
import de.cas_ual_ty.ydm.clientutil.ScreenUtil;
import de.cas_ual_ty.ydm.clientutil.YdmBlitUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;


public class DeckBoxScreen extends AbstractContainerScreen<DeckBoxContainer>
{
    public static final Identifier DECK_BOX_GUI_TEXTURE = Identifier.fromNamespaceAndPath(YDM.MOD_ID, "textures/gui/deck_box.png");
    
    public DeckBoxScreen(DeckBoxContainer screenContainer, Inventory inv, Component titleIn)
    {
        super(screenContainer, inv, titleIn);
    }
    
    @Override
    protected void init()
    {
        imageWidth = 284;
        imageHeight = 250;
        super.init();
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
    {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
    
    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {
        Slot s;
        int amount;
        
        // main deck
        
        amount = 0;
        for(int i = DeckHolder.MAIN_DECK_INDEX_START; i < DeckHolder.MAIN_DECK_INDEX_END; ++i)
        {
            s = getMenu().getSlot(i);
            
            if(s != null && s.hasItem())
            {
                amount++;
            }
        }
        
        //drawString
        guiGraphics.drawString(font, Component.translatable("container.ydm.deck_box.main").append(" " + amount + "/" + DeckHolder.MAIN_DECK_SIZE), 8, 6, 0x404040, false);
        
        // extra deck
        
        amount = 0;
        for(int i = DeckHolder.EXTRA_DECK_INDEX_START; i < DeckHolder.EXTRA_DECK_INDEX_END; ++i)
        {
            s = getMenu().getSlot(i);
            
            if(s != null && s.hasItem())
            {
                amount++;
            }
        }
        
        //drawString
        guiGraphics.drawString(font, Component.translatable("container.ydm.deck_box.extra").append(" " + amount + "/" + DeckHolder.EXTRA_DECK_SIZE), 8, 92, 0x404040, false);
        
        // side deck
        
        amount = 0;
        for(int i = DeckHolder.SIDE_DECK_INDEX_START; i < DeckHolder.SIDE_DECK_INDEX_END; ++i)
        {
            s = getMenu().getSlot(i);
            
            if(s != null && s.hasItem())
            {
                amount++;
            }
        }
        
        //drawString
        guiGraphics.drawString(font, Component.translatable("container.ydm.deck_box.side").append(" " + amount + "/" + DeckHolder.SIDE_DECK_SIZE), 8, 124, 0x404040, false);
        
        guiGraphics.drawString(font, Component.translatable("container.ydm.deck_box.sleeves"), 224, imageHeight - 96 + 2, 0x404040, false);
        
        guiGraphics.drawString(font, playerInventoryTitle, 8, imageHeight - 96 + 2, 0x404040, false);
    }
    
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY)
    {
        ScreenUtil.white();
        YdmBlitUtil.blit(guiGraphics.pose(), DeckBoxScreen.DECK_BOX_GUI_TEXTURE, leftPos, topPos, imageWidth, imageHeight, 0, 0, imageWidth, imageHeight, 512, 256);
    }
}
