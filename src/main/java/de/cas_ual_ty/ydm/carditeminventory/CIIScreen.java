package de.cas_ual_ty.ydm.carditeminventory;


import de.cas_ual_ty.ydm.YDM;
import de.cas_ual_ty.ydm.clientutil.ScreenUtil;
import de.cas_ual_ty.ydm.clientutil.widget.ImprovedButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;

public class CIIScreen<T extends CIIContainer> extends AbstractContainerScreen<T>
{
    private static final Identifier CHEST_GUI_TEXTURE = Identifier.parse("textures/gui/container/generic_54.png");
    
    private final int inventoryRows;
    
    protected Button prevButton;
    protected Button nextButton;
    
    public CIIScreen(T container, Inventory playerInventory, Component title)
    {
        super(container, playerInventory, title);
        //int i = 222;
        //int j = 114;
        inventoryRows = 6;
        imageHeight = 114 + inventoryRows * 18;
        inventoryLabelY = imageHeight - 94;
    }
    
    @Override
    protected void init()
    {
        super.init();
        
        addRenderableWidget(prevButton = new ImprovedButton(leftPos + imageWidth - 24 - 8, topPos + 4, 12, 12, Component.translatable("generic.ydm.left_arrow"), this::onButtonClicked));
        addRenderableWidget(nextButton = new ImprovedButton(leftPos + imageWidth - 12 - 8, topPos + 4, 12, 12, Component.translatable("generic.ydm.right_arrow"), this::onButtonClicked));
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
    {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
    
    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int x, int y)
    {
        MutableComponent title = Component.literal(this.title.getString());
        title = title.append(" ").append(Component.literal((menu.getPage() + 1) + "/" + menu.getMaxPage()));
        guiGraphics.drawString(font, title, 8, 6, 0x404040, false);
    }
    
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int x, int y)
    {
        ScreenUtil.white();
        int i = (width - imageWidth) / 2;
        int j = (height - imageHeight) / 2;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, CIIScreen.CHEST_GUI_TEXTURE, i, j, 0.0F, 0.0F, imageWidth, inventoryRows * 18 + 17, 256, 256);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, CIIScreen.CHEST_GUI_TEXTURE, i, j + inventoryRows * 18 + 17, 0.0F, 126.0F, imageWidth, 96, 256, 256);
    }
    
    protected void onButtonClicked(Button button)
    {
        if(button == prevButton)
        {
            ClientPacketDistributor.sendToServer(new CIIMessages.ChangePage(false));
        }
        else if(button == nextButton)
        {
            ClientPacketDistributor.sendToServer(new CIIMessages.ChangePage(true));
        }
    }
    
    @Override
    public boolean keyPressed(KeyEvent event)
    {
        if(event.key() >= 49 && event.key() <= 57)
        {
            return false;
        }
        
        return super.keyPressed(event);
    }
}
