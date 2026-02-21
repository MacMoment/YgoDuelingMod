package de.cas_ual_ty.ydm.cardsupply;

import de.cas_ual_ty.ydm.YdmDatabase;
import de.cas_ual_ty.ydm.card.properties.Properties;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class CardSupplyMessages
{
    public static void doForBinderContainer(Player player, Consumer<CardSupplyContainer> consumer)
    {
        if(player != null && player.containerMenu instanceof CardSupplyContainer)
        {
            consumer.accept((CardSupplyContainer) player.containerMenu);
        }
    }
    
    public static class RequestCard
    {
        public Properties card;
        public byte imageIndex;
        
        public RequestCard(Properties card, byte imageIndex)
        {
            this.card = card;
            this.imageIndex = imageIndex;
        }
        
        public static void encode(RequestCard msg, FriendlyByteBuf buf)
        {
            buf.writeLong(msg.card.getId());
            buf.writeByte(msg.imageIndex);
        }
        
        public static RequestCard decode(FriendlyByteBuf buf)
        {
            return new RequestCard(YdmDatabase.PROPERTIES_LIST.get(buf.readLong()), buf.readByte());
        }
        
        // TODO: Port to NeoForge payload system - handle method stub
        public static void handle(RequestCard msg, Player sender)
        {
            if(msg.card != null && msg.card != Properties.DUMMY)
            {
                CardSupplyMessages.doForBinderContainer(sender, (container) ->
                {
                    container.giveCard(msg.card, msg.imageIndex);
                });
            }
        }
    }
}
