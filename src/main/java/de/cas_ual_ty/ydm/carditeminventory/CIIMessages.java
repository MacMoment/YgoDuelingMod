package de.cas_ual_ty.ydm.carditeminventory;

import de.cas_ual_ty.ydm.YDM;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
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
    
    public static class SetPage implements CustomPacketPayload
    {
        public static final CustomPacketPayload.Type<SetPage> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(YDM.MOD_ID, "cii_set_page"));
        public static final StreamCodec<FriendlyByteBuf, SetPage> STREAM_CODEC = new StreamCodec<>()
        {
            @Override
            public SetPage decode(FriendlyByteBuf buf)
            {
                return SetPage.decode(buf);
            }
            
            @Override
            public void encode(FriendlyByteBuf buf, SetPage msg)
            {
                SetPage.encode(msg, buf);
            }
        };
        
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
        
        public static void handle(SetPage msg)
        {
            CIIMessages.doForContainer(YDM.proxy.getClientPlayer(), (container) ->
            {
                container.setPage(msg.page);
            });
        }
        
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
        {
            return TYPE;
        }
    }
    
    public static class ChangePage implements CustomPacketPayload
    {
        public static final CustomPacketPayload.Type<ChangePage> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(YDM.MOD_ID, "cii_change_page"));
        public static final StreamCodec<FriendlyByteBuf, ChangePage> STREAM_CODEC = new StreamCodec<>()
        {
            @Override
            public ChangePage decode(FriendlyByteBuf buf)
            {
                return ChangePage.decode(buf);
            }
            
            @Override
            public void encode(FriendlyByteBuf buf, ChangePage msg)
            {
                ChangePage.encode(msg, buf);
            }
        };
        
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
        
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
        {
            return TYPE;
        }
    }
}
