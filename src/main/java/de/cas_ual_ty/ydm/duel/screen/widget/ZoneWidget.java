package de.cas_ual_ty.ydm.duel.screen.widget;


import org.joml.Matrix3x2fStack;
import de.cas_ual_ty.ydm.clientutil.CardRenderUtil;
import de.cas_ual_ty.ydm.clientutil.ScreenUtil;
import de.cas_ual_ty.ydm.duel.DuelManager;
import de.cas_ual_ty.ydm.duel.playfield.DuelCard;
import de.cas_ual_ty.ydm.duel.playfield.Zone;
import de.cas_ual_ty.ydm.duel.playfield.ZoneInteraction;
import de.cas_ual_ty.ydm.duel.playfield.ZoneOwner;
import de.cas_ual_ty.ydm.duel.screen.DuelScreenDueling;
import de.cas_ual_ty.ydm.duel.screen.IDuelScreenContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class ZoneWidget extends Button
{
    public final Zone zone;
    public final IDuelScreenContext context;
    public boolean isFlipped;
    public DuelCard hoverCard;
    
    public ZoneWidget(Zone zone, IDuelScreenContext context, int width, int height, Component title, Consumer<ZoneWidget> onPress)
    {
        super(0, 0, width, height, title, (w) -> onPress.accept((ZoneWidget) w), Button.DEFAULT_NARRATION);
        this.zone = zone;
        this.context = context;
        shift();
        hoverCard = null;
    }
    
    protected void shift()
    {
        setX(getX() - width / 2);
        setY(getY() - height / 2);
    }
    
    protected void unshift()
    {
        setX(getX() + width / 2);
        setY(getY() + height / 2);
    }
    
    public ZoneWidget flip(int guiWidth, int guiHeight)
    {
        guiWidth /= 2;
        guiHeight /= 2;
        
        unshift();
        
        setX(getX() - guiWidth);
        setY(getY() - guiHeight);
        
        setX(-getX());
        setY(-getY());
        
        setX(getX() + guiWidth);
        setY(getY() + guiHeight);
        
        shift();
        
        isFlipped = !isFlipped;
        
        return this;
    }
    
    public ZoneWidget setPositionRelative(int x, int y, int guiWidth, int guiHeight)
    {
        setX(x + guiWidth / 2);
        setY(y + guiHeight / 2);
        
        shift();
        
        isFlipped = false;
        
        return this;
    }
    
    public ZoneWidget setPositionRelativeFlipped(int x, int y, int guiWidth, int guiHeight)
    {
        setX(guiWidth / 2 - x);
        setY(guiHeight / 2 - y);
        
        shift();
        
        isFlipped = true;
        
        return this;
    }
    
    @Override
    public void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
    {
        Matrix3x2fStack ms = guiGraphics.pose();
        Minecraft minecraft = Minecraft.getInstance();
        Font Font = minecraft.font;
        
        
        
        
        
        
        renderZoneSelectRect(ms, zone, getX(), getY(), width, height);
        
        hoverCard = renderCards(guiGraphics, mouseX, mouseY);
        
        
        
        if(zone.type.getCanHaveCounters() && zone.getCounters() > 0)
        {
            // see font renderer, top static Vector3f
            // white is translated in front by that
            ms.pushMatrix();
            guiGraphics.drawCenteredString(Font, Component.literal("(" + zone.getCounters() + ")"),
                    getX() + width / 2, getY() + height / 2 - Font.lineHeight / 2,
                    16777215 | Mth.ceil(alpha * 255.0F) << 24);
            ms.popMatrix();
        }
        
        if(active)
        {
            if(isHoveredOrFocused())
            {
                if(zone.getCardsAmount() == 0)
                {
                    ScreenUtil.renderHoverRect(ms, getX(), getY(), width, height);
                }
                
                // Tooltip rendering handled by the widget tooltip system
            }
        }
        else
        {
            ScreenUtil.renderDisabledRect(ms, getX(), getY(), width, height);
        }
    }
    
    public void renderZoneSelectRect(Matrix3x2fStack ms, Zone zone, float x, float y, float width, float height)
    {
        if(context.getClickedZone() == zone && context.getClickedCard() == null)
        {
            if(context.getOpponentClickedZone() == zone && context.getOpponentClickedCard() == null)
            {
                DuelScreenDueling.renderBothSelectedRect(ms, x, y, width, height);
            }
            else
            {
                DuelScreenDueling.renderSelectedRect(ms, x, y, width, height);
            }
        }
        else
        {
            if(context.getOpponentClickedZone() == zone && context.getOpponentClickedCard() == null)
            {
                DuelScreenDueling.renderEnemySelectedRect(ms, x, y, width, height);
            }
            else
            {
                //
            }
        }
    }
    
    public void renderCardSelectRect(Matrix3x2fStack ms, DuelCard card, float x, float y, float width, float height)
    {
        if(context.getClickedCard() == card)
        {
            if(context.getOpponentClickedCard() == card)
            {
                DuelScreenDueling.renderBothSelectedRect(ms, x, y, width, height);
            }
            else
            {
                DuelScreenDueling.renderSelectedRect(ms, x, y, width, height);
            }
        }
        else
        {
            if(context.getOpponentClickedCard() == card)
            {
                DuelScreenDueling.renderEnemySelectedRect(ms, x, y, width, height);
            }
            else
            {
                //
            }
        }
    }
    
    @Nullable
    public DuelCard renderCards(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {
        Matrix3x2fStack ms = guiGraphics.pose();
        if(zone.getCardsAmount() <= 0)
        {
            return null;
        }
        
        boolean isOwner = zone.getOwner() == context.getZoneOwner();
        DuelCard c = zone.getTopCard();
        
        if(c != null)
        {
            if(drawCard(guiGraphics, c, getX(), getY(), width, height, mouseX, mouseY, getX(), getY(), width, height))
            {
                if(c.getCardPosition().isFaceUp || (isOwner && !zone.getType().getIsSecret()))
                {
                    context.renderCardInfo(ms, c);
                }
                
                if(active)
                {
                    ScreenUtil.renderHoverRect(ms, getX(), getY(), width, height);
                    return c;
                }
            }
        }
        
        if(context.getClickedZone() == zone)
        {
            DuelScreenDueling.renderSelectedRect(ms, getX(), getY(), width, height);
        }
        
        return null;
    }
    
    protected boolean drawCard(GuiGraphics guiGraphics, DuelCard duelCard, int renderX, int renderY, int renderWidth, int renderHeight, int mouseX, int mouseY, int cardsWidth, int cardsHeight)
    {
        int offset = cardsHeight - cardsWidth;
        
        int hoverX = renderX;
        int hoverY = renderY;
        int hoverWidth;
        int hoverHeight;
        
        if(duelCard.getCardPosition().isStraight)
        {
            hoverX += offset;
            hoverWidth = cardsWidth;
            hoverHeight = cardsHeight;
        }
        else
        {
            hoverY += offset;
            hoverWidth = cardsHeight;
            hoverHeight = cardsWidth;
        }
        
        return drawCard(guiGraphics, duelCard, renderX, renderY, renderWidth, renderHeight, mouseX, mouseY, hoverX, hoverY, hoverWidth, hoverHeight);
    }
    
    protected boolean drawCard(GuiGraphics guiGraphics, DuelCard duelCard, float renderX, float renderY, float renderWidth, float renderHeight, int mouseX, int mouseY, float hoverX, float hoverY, float hoverWidth, float hoverHeight)
    {
        Matrix3x2fStack ms = guiGraphics.pose();
        boolean isOwner = zone.getOwner() == context.getZoneOwner();
        boolean faceUp = zone.getType().getShowFaceDownCardsToOwner() && isOwner;
        boolean isOpponentView = zone.getOwner() != context.getView();
        
        renderCardSelectRect(ms, duelCard, hoverX, hoverY, hoverWidth, hoverHeight);
        
        if(!isOpponentView)
        {
            CardRenderUtil.renderDuelCardCentered(guiGraphics, zone.getSleeves(), mouseX, mouseY, renderX, renderY, renderWidth, renderHeight, duelCard, faceUp);
        }
        else
        {
            CardRenderUtil.renderDuelCardReversedCentered(guiGraphics, zone.getSleeves(), mouseX, mouseY, renderX, renderY, renderWidth, renderHeight, duelCard, faceUp);
        }
        
        if(isHoveredOrFocused() && mouseX >= hoverX && mouseX < hoverX + hoverWidth && mouseY >= hoverY && mouseY < hoverY + hoverHeight)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public void addInteractionWidgets(ZoneOwner player, Zone interactor, DuelCard interactorCard, DuelManager m, List<InteractionWidget> list, Consumer<InteractionWidget> onPress, boolean isAdvanced)
    {
        List<ZoneInteraction> interactions;
        
        if(!isAdvanced)
        {
            interactions = m.getActionsFor(player, interactor, interactorCard, zone);
        }
        else
        {
            interactions = m.getAdvancedActionsFor(player, interactor, interactorCard, zone);
        }
        
        if(interactions.size() == 0)
        {
            return;
        }
        
        if(interactions.size() == 1)
        {
            list.add(new InteractionWidget(interactions.get(0), context, getX(), getY(), width, height, onPress));
        }
        else if(interactions.size() == 2)
        {
            if(width <= height)
            {
                // Split them horizontally (1 action on top, 1 on bottom)
                list.add(new InteractionWidget(interactions.get(0), context, getX(), getY(), width, height / 2, onPress));
                list.add(new InteractionWidget(interactions.get(1), context, getX(), getY() + height / 2, width, height / 2, onPress));
            }
            else
            {
                // Split them vertically (1 left, 1 right)
                list.add(new InteractionWidget(interactions.get(0), context, getX(), getY(), width / 2, height, onPress));
                list.add(new InteractionWidget(interactions.get(1), context, getX() + width / 2, getY(), width / 2, height, onPress));
            }
        }
        else if(interactions.size() == 3)
        {
            if(width == height)
            {
                // 1 on top half, 1 bottom left, 1 bottom right
                list.add(new InteractionWidget(interactions.get(0), context, getX(), getY(), width, height / 2, onPress));
                list.add(new InteractionWidget(interactions.get(1), context, getX(), getY() + height / 2, width / 2, height / 2, onPress));
                list.add(new InteractionWidget(interactions.get(2), context, getX() + width / 2, getY() + height / 2, width / 2, height / 2, onPress));
            }
            else if(width < height)
            {
                // Horizontally split
                list.add(new InteractionWidget(interactions.get(0), context, getX(), getY(), width, height / 3, onPress));
                list.add(new InteractionWidget(interactions.get(1), context, getX(), getY() + height / 3, width, height / 3, onPress));
                list.add(new InteractionWidget(interactions.get(2), context, getX(), getY() + height * 2 / 3, width, height / 3, onPress));
            }
            else //if(this.width > this.height)
            {
                // Vertically split
                list.add(new InteractionWidget(interactions.get(0), context, getX(), getY(), width / 3, height, onPress));
                list.add(new InteractionWidget(interactions.get(1), context, getX() + width / 3, getY(), width / 3, height, onPress));
                list.add(new InteractionWidget(interactions.get(2), context, getX() + width * 2 / 3, getY(), width / 3, height, onPress));
            }
        }
        else if(interactions.size() == 4 && width == height)
        {
            // 1 on top left, 1 top right, 1 bottom left, 1 bottom right
            list.add(new InteractionWidget(interactions.get(0), context, getX(), getY(), width / 2, height / 2, onPress));
            list.add(new InteractionWidget(interactions.get(1), context, getX() + width / 2, getY(), width / 2, height / 2, onPress));
            list.add(new InteractionWidget(interactions.get(2), context, getX(), getY() + height / 2, width / 2, height / 2, onPress));
            list.add(new InteractionWidget(interactions.get(3), context, getX() + width / 2, getY() + height / 2, width / 2, height / 2, onPress));
        }
        else
        {
            if(width < height)
            {
                // Horizontally split
                for(int i = 0; i < interactions.size(); ++i)
                {
                    list.add(new InteractionWidget(interactions.get(i), context, getX(), getY() + height * i / interactions.size(), width, height / interactions.size(), onPress));
                }
            }
            else //if(this.width > this.height)
            {
                // Vertically split
                for(int i = 0; i < interactions.size(); ++i)
                {
                    list.add(new InteractionWidget(interactions.get(i), context, getX() + width * i / interactions.size(), getY(), width / interactions.size(), height, onPress));
                }
            }
        }
    }
    
    public int getAnimationSourceX()
    {
        return getX() + width / 2;
    }
    
    public int getAnimationSourceY()
    {
        return getY() + height / 2;
    }
    
    public int getAnimationDestX()
    {
        return getX() + width / 2;
    }
    
    public int getAnimationDestY()
    {
        return getY() + height / 2;
    }
    
    public Component getTranslation()
    {
        return Component.translatable(zone.getType().getRegistryName().getNamespace() + ".zone." + zone.getType().getRegistryName().getPath());
    }
    
    public boolean openAdvancedZoneView()
    {
        return false;
    }
}