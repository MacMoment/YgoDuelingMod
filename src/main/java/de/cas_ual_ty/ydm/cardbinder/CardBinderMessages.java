package de.cas_ual_ty.ydm.cardbinder;

import de.cas_ual_ty.ydm.YDM;
import de.cas_ual_ty.ydm.card.CardHolder;
import de.cas_ual_ty.ydm.duel.network.DuelMessageUtility;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.function.Consumer;

public class CardBinderMessages
{
    public static void doForBinderContainer(Player player, Consumer<CardBinderContainer> consumer)
    {
        if(player != null && player.containerMenu instanceof CardBinderContainer)
        {
            consumer.accept((CardBinderContainer) player.containerMenu);
        }
    }
    
    // client changes page, tells server
    public static class ChangePage implements CustomPacketPayload
    {
        public static final CustomPacketPayload.Type<ChangePage> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(YDM.MOD_ID, "binder_change_page"));
        
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
            CardBinderMessages.doForBinderContainer(sender, (container) ->
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
    
    // client changes search, tells server
    public static class ChangeSearch implements CustomPacketPayload
    {
        public static final CustomPacketPayload.Type<ChangeSearch> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(YDM.MOD_ID, "binder_change_search"));
        
        public static final StreamCodec<FriendlyByteBuf, ChangeSearch> STREAM_CODEC = new StreamCodec<>()
        {
            @Override
            public ChangeSearch decode(FriendlyByteBuf buf)
            {
                return ChangeSearch.decode(buf);
            }
            
            @Override
            public void encode(FriendlyByteBuf buf, ChangeSearch msg)
            {
                ChangeSearch.encode(msg, buf);
            }
        };
        
        public String search;
        
        public ChangeSearch(String search)
        {
            this.search = search;
        }
        
        public static void encode(ChangeSearch msg, FriendlyByteBuf buf)
        {
            buf.writeUtf(msg.search, Short.MAX_VALUE);
        }
        
        public static ChangeSearch decode(FriendlyByteBuf buf)
        {
            return new ChangeSearch(buf.readUtf(Short.MAX_VALUE));
        }
        
        public static void handle(ChangeSearch msg, Player sender)
        {
            CardBinderMessages.doForBinderContainer(sender, (container) ->
            {
                container.updateSearch(msg.search);
            });
        }
        
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
        {
            return TYPE;
        }
    }
    
    // update pages to client
    public static class UpdatePage implements CustomPacketPayload
    {
        public static final CustomPacketPayload.Type<UpdatePage> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(YDM.MOD_ID, "binder_update_page"));
        
        public static final StreamCodec<FriendlyByteBuf, UpdatePage> STREAM_CODEC = new StreamCodec<>()
        {
            @Override
            public UpdatePage decode(FriendlyByteBuf buf)
            {
                return UpdatePage.decode(buf);
            }
            
            @Override
            public void encode(FriendlyByteBuf buf, UpdatePage msg)
            {
                UpdatePage.encode(msg, buf);
            }
        };
        
        public int page;
        public int maxPage;
        
        public UpdatePage(int page, int maxPage)
        {
            this.page = page;
            this.maxPage = maxPage;
        }
        
        public static void encode(UpdatePage msg, FriendlyByteBuf buf)
        {
            buf.writeInt(msg.page);
            buf.writeInt(msg.maxPage);
        }
        
        public static UpdatePage decode(FriendlyByteBuf buf)
        {
            return new UpdatePage(buf.readInt(), buf.readInt());
        }
        
        public static void handle(UpdatePage msg)
        {
            CardBinderMessages.doForBinderContainer(YDM.proxy.getClientPlayer(), (container) ->
            {
                container.setClientPage(msg.page);
                container.setClientMaxPage(msg.maxPage);
            });
        }
        
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
        {
            return TYPE;
        }
    }
    
    // update cards list to client
    public static class UpdateList implements CustomPacketPayload
    {
        public static final CustomPacketPayload.Type<UpdateList> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(YDM.MOD_ID, "binder_update_list"));
        
        public static final StreamCodec<FriendlyByteBuf, UpdateList> STREAM_CODEC = new StreamCodec<>()
        {
            @Override
            public UpdateList decode(FriendlyByteBuf buf)
            {
                return UpdateList.decode(buf);
            }
            
            @Override
            public void encode(FriendlyByteBuf buf, UpdateList msg)
            {
                UpdateList.encode(msg, buf);
            }
        };
        
        public int page;
        public List<CardHolder> list;
        
        public UpdateList(int page, List<CardHolder> list)
        {
            this.page = page;
            this.list = list;
        }
        
        public static void encode(UpdateList msg, FriendlyByteBuf buf)
        {
            buf.writeInt(msg.page);
            DuelMessageUtility.encodeList(msg.list, buf, DuelMessageUtility::encodeCardHolder);
        }
        
        public static UpdateList decode(FriendlyByteBuf buf)
        {
            return new UpdateList(buf.readInt(), DuelMessageUtility.decodeList(buf, DuelMessageUtility::decodeCardHolder));
        }
        
        public static void handle(UpdateList msg)
        {
            CardBinderMessages.doForBinderContainer(YDM.proxy.getClientPlayer(), (container) ->
            {
                container.setClientList(msg.page, msg.list);
            });
        }
        
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
        {
            return TYPE;
        }
    }
    
    // client clicks index, tells server
    public static class IndexClicked implements CustomPacketPayload
    {
        public static final CustomPacketPayload.Type<IndexClicked> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(YDM.MOD_ID, "binder_index_clicked"));
        
        public static final StreamCodec<FriendlyByteBuf, IndexClicked> STREAM_CODEC = new StreamCodec<>()
        {
            @Override
            public IndexClicked decode(FriendlyByteBuf buf)
            {
                return IndexClicked.decode(buf);
            }
            
            @Override
            public void encode(FriendlyByteBuf buf, IndexClicked msg)
            {
                IndexClicked.encode(msg, buf);
            }
        };
        
        public int index;
        
        public IndexClicked(int index)
        {
            this.index = index;
        }
        
        public static void encode(IndexClicked msg, FriendlyByteBuf buf)
        {
            buf.writeInt(msg.index);
        }
        
        public static IndexClicked decode(FriendlyByteBuf buf)
        {
            return new IndexClicked(buf.readInt());
        }
        
        public static void handle(IndexClicked msg, Player sender)
        {
            CardBinderMessages.doForBinderContainer(sender, (container) ->
            {
                container.indexClicked(msg.index);
            });
        }
        
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
        {
            return TYPE;
        }
    }
    
    public static class IndexDropped implements CustomPacketPayload
    {
        public static final CustomPacketPayload.Type<IndexDropped> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(YDM.MOD_ID, "binder_index_dropped"));
        
        public static final StreamCodec<FriendlyByteBuf, IndexDropped> STREAM_CODEC = new StreamCodec<>()
        {
            @Override
            public IndexDropped decode(FriendlyByteBuf buf)
            {
                return IndexDropped.decode(buf);
            }
            
            @Override
            public void encode(FriendlyByteBuf buf, IndexDropped msg)
            {
                IndexDropped.encode(msg, buf);
            }
        };
        
        public int index;
        
        public IndexDropped(int index)
        {
            this.index = index;
        }
        
        public static void encode(IndexDropped msg, FriendlyByteBuf buf)
        {
            buf.writeInt(msg.index);
        }
        
        public static IndexDropped decode(FriendlyByteBuf buf)
        {
            return new IndexDropped(buf.readInt());
        }
        
        public static void handle(IndexDropped msg, Player sender)
        {
            CardBinderMessages.doForBinderContainer(sender, (container) ->
            {
                container.indexDropped(msg.index);
            });
        }
        
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
        {
            return TYPE;
        }
    }
}
