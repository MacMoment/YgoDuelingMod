package de.cas_ual_ty.ydm.cardsupply;


import de.cas_ual_ty.ydm.YDM;
import de.cas_ual_ty.ydm.YdmDatabase;
import de.cas_ual_ty.ydm.card.CardHolder;
import de.cas_ual_ty.ydm.cardbinder.CardButton;
import de.cas_ual_ty.ydm.cardinventory.CardInventory;
import de.cas_ual_ty.ydm.clientutil.CardRenderUtil;
import de.cas_ual_ty.ydm.clientutil.widget.ImprovedButton;
import de.cas_ual_ty.ydm.rarity.Rarities;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;

public class CardSupplyScreen extends AbstractContainerScreen<CardSupplyContainer>
{
    private static final Identifier CARD_SUPPLY_GUI_TEXTURE = Identifier.fromNamespaceAndPath(YDM.MOD_ID, "textures/gui/card_supply.png");
    
    public static final int ROWS = 6;
    public static final int COLUMNS = 9;
    public static final int PAGE = CardSupplyScreen.ROWS * CardSupplyScreen.COLUMNS;
    
    public List<CardHolder> cardsList;
    public EditBox textField;
    protected Button prevButton;
    protected Button nextButton;
    public int page;
    
    public CardButton[] cardButtons;
    
    public CardSupplyScreen(CardSupplyContainer screenContainer, Inventory inv, Component titleIn)
    {
        super(screenContainer, inv, titleIn);
        imageWidth = 176;
        imageHeight = 114 + 6 * 18; //222
        cardsList = new ArrayList<>(YdmDatabase.getTotalCardsAndVariants());
    }
    
    @Override
    protected void init()
    {
        super.init();
        
        addRenderableWidget(textField = new EditBox(font, leftPos + imageWidth - 80 - 8 - 1, topPos + 6 - 1, 80 + 2, font.lineHeight + 2, Component.empty()));
        
        int index;
        CardButton button;
        cardButtons = new CardButton[CardSupplyScreen.PAGE];
        
        for(int y = 0; y < CardInventory.DEFAULT_PAGE_ROWS; ++y)
        {
            for(int x = 0; x < CardInventory.DEFAULT_PAGE_COLUMNS; ++x)
            {
                index = x + y * 9;
                button = new CardButton(leftPos + 7 + x * 18, topPos + 17 + y * 18, 18, 18, index, this::onCardClicked, this::getCard);
                cardButtons[index] = button;
                addRenderableWidget(button);
            }
        }
        
        addRenderableWidget(prevButton = new ImprovedButton(leftPos + imageWidth - 80 - 8, topPos + imageHeight - 96, 40, 12, Component.translatable("container.ydm.card_supply.prev"), this::onButtonClicked));
        addRenderableWidget(nextButton = new ImprovedButton(leftPos + imageWidth - 40 - 8, topPos + imageHeight - 96, 40, 12, Component.translatable("container.ydm.card_supply.next"), this::onButtonClicked));
        
        applyName();
        updateCards();
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
    {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        renderTooltip(guiGraphics, mouseX, mouseY);
        
        for(CardButton button : cardButtons)
        {
            if(button.isHoveredOrFocused())
            {
                if(button.getCard() != null)
                {
                    CardRenderUtil.renderCardInfo(guiGraphics, button.getCard(), this);
                    
                    List<Component> list = new LinkedList<>();
                    button.getCard().addInformation(list);
                    
                    List<Component> tooltip = new ArrayList<>(list.size());
                    for(Component t : list)
                    {
                        tooltip.add(t);
                    }
                    
                    //renderTooltip
                    guiGraphics.setTooltipForNextFrame(font, tooltip.stream().map(Component::getVisualOrderText).toList(), mouseX, mouseY);
                }
                
                break;
            }
        }
    }
    
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int x, int y)
    {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, CardSupplyScreen.CARD_SUPPLY_GUI_TEXTURE, leftPos, topPos, 0.0F, 0.0F, imageWidth, imageHeight, 256, 256);
    }
    
    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {
        guiGraphics.drawString(font, title, 8, 6, 0x404040, false);
        guiGraphics.drawString(font, playerInventoryTitle, 8, imageHeight - 96 + 2, 0x404040, false);
    }
    
    @Override
    public boolean keyPressed(KeyEvent event)
    {
        if(textField != null && textField.isFocused())
        {
            if(event.key() == GLFW.GLFW_KEY_ENTER)
            {
                applyName();
                return true;
            }
            else
            {
                return textField.keyPressed(event);
            }
        }
        else
        {
            return super.keyPressed(event);
        }
    }
    
    protected void onButtonClicked(Button button)
    {
        int minPage = 0;
        int maxPage = cardsList.size() / CardSupplyScreen.PAGE + 1;
        
        if(button == prevButton)
        {
            --page;
            
            if(page < minPage)
            {
                page = maxPage;
            }
        }
        else if(button == nextButton)
        {
            ++page;
            
            if(page > maxPage)
            {
                page = minPage;
            }
        }
    }
    
    public void applyName()
    {
        String name = textField.getValue().toLowerCase();
        
        cardsList.clear();
        page = 0;
        
        YdmDatabase.forAllCardVariants((card, imageIndex) ->
        {
            if(card.getName().toLowerCase().contains(name))
            {
                cardsList.add(new CardHolder(card, imageIndex, Rarities.SUPPLY.name));
            }
        });
    }
    
    public void updateCards()
    {
        page = 0;
        cardsList.clear();
        
        YdmDatabase.forAllCardVariants((card, imageIndex) ->
        {
            cardsList.add(new CardHolder(card, imageIndex, Rarities.SUPPLY.name));
        });
    }
    
    protected void onCardClicked(CardButton button, int index)
    {
        if(button.getCard() != null && button.getCard().getCard() != null)
        {
            ClientPacketDistributor.sendToServer(new CardSupplyMessages.RequestCard(button.getCard().getCard(), button.getCard().getImageIndex()));
        }
    }
    
    protected CardHolder getCard(int index0)
    {
        int index = scopeIndex(index0);
        return index < cardsList.size() ? cardsList.get(index) : null;
    }
    
    protected int scopeIndex(int cardButtonIndex)
    {
        return page * CardSupplyScreen.PAGE + cardButtonIndex;
    }
}
