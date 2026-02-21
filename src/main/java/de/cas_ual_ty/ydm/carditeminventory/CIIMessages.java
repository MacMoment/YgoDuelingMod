package de.cas_ual_ty.ydm.carditeminventory;

import de.cas_ual_ty.ydm.YDM;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class CIIMessages
{
    public static void doForContainer(Player player, Consumer<CIIContainer> consumer)
    {
        if(player != null && player.containerMenu instanceof CIIContainer)
        {
            consumer.accept((CIIContainer) player.containerMenu);
        }
    }
    
    public static class SetPage
    {
        public int page;
        
        public SetPage(int page)
        {
            this.page = page;
        }
        
        public static void encode(SetPage msg, FriendlyByteBuf buf)
        {
            buf.writeInt(msg.page);
        }
        
        public static SetPage decode(FriendlyByteBuf buf)
        {
            return new SetPage(buf.readInt());
        }
        
        // TODO: Port to NeoForge payload system - handle method stub
        public static void handle(SetPage msg)
        {
            CIIMessages.doForContainer(YDM.proxy.getClientPlayer(), (container) ->
            {
                container.setPage(msg.page);
            });
        }
    }
    
    public static class ChangePage
    {
        public boolean nextPage;
        
        public ChangePage(boolean nextPage)
        {
            this.nextPage = nextPage;
        }
        
        public static void encode(ChangePage msg, FriendlyByteBuf buf)
        {
            buf.writeBoolean(msg.nextPage);
        }
        
        public static ChangePage decode(FriendlyByteBuf buf)
        {
            return new ChangePage(buf.readBoolean());
        }
        
        // TODO: Port to NeoForge payload system - handle method stub
        public static void handle(ChangePage msg, Player sender)
        {
            CIIMessages.doForContainer(sender, (container) ->
            {
                if(msg.nextPage)
                {
                    container.nextPage();
                }
                else
                {
                    container.prevPage();
                }
            });
        }
    }
}
