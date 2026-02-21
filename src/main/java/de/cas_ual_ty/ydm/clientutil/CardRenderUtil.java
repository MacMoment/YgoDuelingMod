package de.cas_ual_ty.ydm.clientutil;


import org.joml.Matrix3x2fStack;
import de.cas_ual_ty.ydm.YDM;
import net.minecraft.client.gui.GuiGraphics;
import de.cas_ual_ty.ydm.YdmDatabase;
import de.cas_ual_ty.ydm.YdmItems;
import de.cas_ual_ty.ydm.card.CardHolder;
import de.cas_ual_ty.ydm.card.CardSleevesType;
import de.cas_ual_ty.ydm.card.properties.Properties;
import de.cas_ual_ty.ydm.duel.playfield.CardPosition;
import de.cas_ual_ty.ydm.duel.playfield.DuelCard;
import de.cas_ual_ty.ydm.rarity.RarityEntry;
import de.cas_ual_ty.ydm.rarity.RarityLayer;
import de.cas_ual_ty.ydm.rarity.RarityLayerType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.LinkedList;
import java.util.List;

public class CardRenderUtil
{
    public static final Identifier MASK_RL = Identifier.fromNamespaceAndPath(YDM.MOD_ID, "textures/gui/rarity_mask.png");
    
    private static LimitedTextureBinder infoTextureBinder;
    private static LimitedTextureBinder mainTextureBinder;
    
    // called from ClientProxy
    public static void init(int maxInfoImages, int maxMainImages)
    {
        CardRenderUtil.infoTextureBinder = new LimitedTextureBinder(ClientProxy.getMinecraft(), maxInfoImages);
        CardRenderUtil.mainTextureBinder = new LimitedTextureBinder(ClientProxy.getMinecraft(), maxMainImages);
    }
    
    public static void renderCardInfo(GuiGraphics guiGraphics, CardHolder card, AbstractContainerScreen<?> screen)
    {
        CardRenderUtil.renderCardInfo(guiGraphics, card, screen.getGuiLeft());
    }
    
    public static void renderCardInfo(GuiGraphics guiGraphics, CardHolder card)
    {
        CardRenderUtil.renderCardInfo(guiGraphics, card, 100);
    }
    
    public static void renderCardInfo(GuiGraphics guiGraphics, CardHolder card, int width)
    {
        CardRenderUtil.renderCardInfo(guiGraphics, card, false, width);
    }
    
    public static void renderCardInfo(GuiGraphics guiGraphics, CardHolder card, boolean token, int width)
    {
        if(card == null || card.getCard() == null)
        {
            return;
        }
        
        final float f = 0.5f;
        final int imageSize = 64;
        int margin = 2;
        
        int maxWidth = width - margin * 2;
        
        Matrix3x2fStack ms = guiGraphics.pose();
        ms.pushMatrix();
        ScreenUtil.white();
        
        int x = margin;
        
        if(maxWidth < imageSize)
        {
            // draw it centered if the space we got is limited
            // to make sure the image is NOT rendered more to the right of the center
            x = (maxWidth - imageSize) / 2 + margin;
        }
        
        // card texture
        
        CardRenderUtil.bindInfoResourceLocation(card);
        YdmBlitUtil.fullBlit(ms, x, margin, imageSize, imageSize);
        
        if(token)
        {
            YdmBlitUtil.fullBlit(ms, CardRenderUtil.getInfoTokenOverlay(), x, margin, imageSize, imageSize);
        }
        
        // need to multiply x2 because we are scaling the text to x0.5
        maxWidth *= 2;
        margin *= 2;
        ms.scale(f, f);
        
        // card description text
        
        Font fontRenderer = ClientProxy.getMinecraft().font;
        
        List<Component> list = new LinkedList<>();
        card.getCard().addInformation(list);
        
        ScreenUtil.drawSplitString(guiGraphics, fontRenderer, list, margin, imageSize * 2 + margin * 2, maxWidth, 0xFFFFFF);
        
        ms.popMatrix();
    }
    
    public static void bindInfoResourceLocation(CardHolder c)
    {
        CardRenderUtil.infoTextureBinder.bind(c.getInfoImageResourceLocation());
    }
    
    public static void bindMainResourceLocation(CardHolder c)
    {
        CardRenderUtil.mainTextureBinder.bind(c.getMainImageResourceLocation());
    }
    
    public static void bindInfoResourceLocation(Properties p, byte imageIndex)
    {
        CardRenderUtil.infoTextureBinder.bind(p.getInfoImageResourceLocation(imageIndex));
    }
    
    public static void bindMainResourceLocation(Properties p, byte imageIndex)
    {
        CardRenderUtil.mainTextureBinder.bind(p.getMainImageResourceLocation(imageIndex));
    }
    
    public static void bindInfoResourceLocation(Identifier r)
    {
        CardRenderUtil.infoTextureBinder.bind(r);
    }
    
    public static void bindMainResourceLocation(Identifier r)
    {
        CardRenderUtil.mainTextureBinder.bind(r);
    }
    
    public static void bindSleeves(CardSleevesType s)
    {
    }
    
    public static Identifier getInfoCardBack()
    {
        return Identifier.fromNamespaceAndPath(YDM.MOD_ID, "textures/item/" + ClientProxy.activeCardInfoImageSize + "/" + YdmItems.CARD_BACK.getId().getPath() + ".png");
    }
    
    public static Identifier getMainCardBack()
    {
        return Identifier.fromNamespaceAndPath(YDM.MOD_ID, "textures/item/" + ClientProxy.activeCardMainImageSize + "/" + YdmItems.CARD_BACK.getId().getPath() + ".png");
    }
    
    public static Identifier getInfoTokenOverlay()
    {
        return Identifier.fromNamespaceAndPath(YDM.MOD_ID, "textures/item/" + ClientProxy.activeCardInfoImageSize + "/" + "token_overlay" + ".png");
    }
    
    public static Identifier getMainTokenOverlay()
    {
        return Identifier.fromNamespaceAndPath(YDM.MOD_ID, "textures/item/" + ClientProxy.activeCardMainImageSize + "/" + "token_overlay" + ".png");
    }
    
    public static Identifier getRarityOverlay()
    {
        return Identifier.fromNamespaceAndPath(YDM.MOD_ID, "textures/item/" + ClientProxy.activeCardInfoImageSize + "/" + "token_overlay" + ".png");
    }
    
    public static void renderInfoCardWithRarity(GuiGraphics guiGraphics, int mouseX, int mouseY, float x, float y, float width, float height, CardHolder card)
    {
        Matrix3x2fStack ms = guiGraphics.pose();
        Minecraft mc = ClientProxy.getMinecraft();
        
        // bind the texture depending on faceup or facedown
        CardRenderUtil.bindInfoResourceLocation(card);
        YdmBlitUtil.fullBlit(ms, x - width / 2, y - height / 2, width, height);
        
        RarityEntry rarity = YdmDatabase.getRarity(card.getRarity());
        
        if(rarity != null)
        {
            for(RarityLayer layer : rarity.layers)
            {
                if(layer.type == RarityLayerType.INVERTED)
                {
                }
                
                Runnable mask = () ->
                {
                    YdmBlitUtil.fullBlit(ms, MASK_RL, mouseX - width / 2, mouseY - height / 2, width, height);
                };
                
                Runnable renderer = () ->
                {
                    YdmBlitUtil.fullBlit(ms, layer.getInfoImageResourceLocation(), x - width / 2, y - height / 2, width, height);
                };
                
                YdmBlitUtil.advancedMaskedBlit(ms, x, y, width, height, mask, renderer, layer.type.invertedRendering);
            }
        }
    }
    
    public static void renderDuelCardAdvanced(GuiGraphics guiGraphics, CardSleevesType back, int mouseX, int mouseY, float x, float y, float width, float height, DuelCard card, YdmBlitUtil.FullBlitMethod blitMethod, boolean forceFaceUp)
    {
        CardPosition position = card.getCardPosition();
        
        // bind the texture depending on faceup or facedown
        if(!card.getCardPosition().isFaceUp && forceFaceUp)
        {
            position = position.flip();
        }
        
        CardRenderUtil.renderDuelCardAdvanced(guiGraphics, back, mouseX, mouseY, x, y, width, height, card, position, blitMethod);
    }
    
    public static void renderDuelCardAdvanced(GuiGraphics guiGraphics, CardSleevesType back, int mouseX, int mouseY, float x, float y, float width, float height, DuelCard card, CardPosition position, YdmBlitUtil.FullBlitMethod blitMethod)
    {
        Matrix3x2fStack ms = guiGraphics.pose();
        Minecraft mc = ClientProxy.getMinecraft();
        
        // bind the texture depending on faceup or facedown
        if(position.isFaceUp)
        {
            CardRenderUtil.bindMainResourceLocation(card.getCardHolder());
        }
        else
        {
        }
        
        blitMethod.fullBlit(ms, x, y, width, height);
        
        if(card.getIsToken())
        {
            YdmBlitUtil.fullBlit(ms, CardRenderUtil.getMainTokenOverlay(), x, y, width, height);
        }
        
        if(position.isFaceUp && !card.getIsToken())
        {
            RarityEntry rarity = YdmDatabase.getRarity(card.getCardHolder().getRarity());
            
            if(rarity != null)
            {
                for(RarityLayer layer : rarity.layers)
                {
                    Runnable mask = () ->
                    {
                        YdmBlitUtil.fullBlit(ms, MASK_RL, mouseX - width / 2, mouseY - height / 2, width, height);
                    };
                    
                    Runnable renderer = () ->
                    {
                        YdmBlitUtil.fullBlit(ms, layer.getMainImageResourceLocation(), x, y, width, height);
                    };
                    
                    YdmBlitUtil.advancedMaskedBlit(ms, x, y, width, height, mask, renderer, layer.type.invertedRendering);
                }
            }
        }
    }
    
    public static void renderDuelCard(GuiGraphics guiGraphics, CardSleevesType back, int mouseX, int mouseY, float x, float y, float width, float height, DuelCard card, boolean forceFaceUp)
    {
        CardRenderUtil.renderDuelCardAdvanced(guiGraphics, back, mouseX, mouseY, x, y, width, height, card,
                card.getCardPosition().isStraight
                        ? YdmBlitUtil::fullBlit
                        : YdmBlitUtil::fullBlit90Degree, forceFaceUp);
    }
    
    public static void renderDuelCardReversed(GuiGraphics guiGraphics, CardSleevesType back, int mouseX, int mouseY, float x, float y, float width, float height, DuelCard card, boolean forceFaceUp)
    {
        CardRenderUtil.renderDuelCardAdvanced(guiGraphics, back, mouseX, mouseY, x, y, width, height, card,
                card.getCardPosition().isStraight
                        ? YdmBlitUtil::fullBlit180Degree
                        : YdmBlitUtil::fullBlit270Degree, forceFaceUp);
    }
    
    public static void renderDuelCardCentered(GuiGraphics guiGraphics, CardSleevesType back, int mouseX, int mouseY, float x, float y, float width, float height, DuelCard card, boolean forceFaceUp)
    {
        // if width and height are more of a rectangle, this centers the texture horizontally
        x -= (height - width) / 2;
        width = height;
        
        CardRenderUtil.renderDuelCard(guiGraphics, back, mouseX, mouseY, x, y, width, height, card, forceFaceUp);
    }
    
    public static void renderDuelCardReversedCentered(GuiGraphics guiGraphics, CardSleevesType back, int mouseX, int mouseY, float x, float y, float width, float height, DuelCard card, boolean forceFaceUp)
    {
        // if width and height are more of a rectangle, this centers the texture horizontally
        x -= (height - width) / 2;
        width = height;
        
        CardRenderUtil.renderDuelCardReversed(guiGraphics, back, mouseX, mouseY, x, y, width, height, card, forceFaceUp);
    }
}
