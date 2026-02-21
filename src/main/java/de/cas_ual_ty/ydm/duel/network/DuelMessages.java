package de.cas_ual_ty.ydm.duel.network;

import de.cas_ual_ty.ydm.YDM;
import de.cas_ual_ty.ydm.deckbox.DeckHolder;
import de.cas_ual_ty.ydm.duel.DeckSource;
import de.cas_ual_ty.ydm.duel.DuelChatMessage;
import de.cas_ual_ty.ydm.duel.DuelState;
import de.cas_ual_ty.ydm.duel.PlayerRole;
import de.cas_ual_ty.ydm.duel.action.Action;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class DuelMessages
{
    public static class SelectRole extends DuelMessage.ServerBaseMessage
    {
        public static final CustomPacketPayload.Type<SelectRole> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(YDM.MOD_ID, "duel_select_role"));
        public static final StreamCodec<FriendlyByteBuf, SelectRole> STREAM_CODEC = new StreamCodec<>()
        {
            @Override
            public SelectRole decode(FriendlyByteBuf buf)
            {
                return new SelectRole(buf);
            }
            
            @Override
            public void encode(FriendlyByteBuf buf, SelectRole msg)
            {
                msg.encode(buf);
            }
        };
        
        public PlayerRole playerRole;
        
        public SelectRole(DuelMessageHeader header, PlayerRole playerRole)
        {
            super(header);
            this.playerRole = playerRole;
        }
        
        public SelectRole(FriendlyByteBuf buf)
        {
            super(buf);
        }
        
        @Override
        public void encodeMessage(FriendlyByteBuf buf)
        {
            DuelMessageUtility.encodePlayerRole(playerRole, buf);
        }
        
        @Override
        public void decodeMessage(FriendlyByteBuf buf)
        {
            playerRole = DuelMessageUtility.decodePlayerRole(buf);
        }
        
        @Override
        public void handleMessage(Player player, IDuelManagerProvider provider)
        {
            provider.getDuelManager().playerSelectRole(player, playerRole);
        }
        
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
        {
            return TYPE;
        }
    }
    
    public static class UpdateRole extends DuelMessage.ClientBaseMessage
    {
        public static final CustomPacketPayload.Type<UpdateRole> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(YDM.MOD_ID, "duel_update_role"));
        public static final StreamCodec<FriendlyByteBuf, UpdateRole> STREAM_CODEC = new StreamCodec<>()
        {
            @Override
            public UpdateRole decode(FriendlyByteBuf buf)
            {
                return new UpdateRole(buf);
            }
            
            @Override
            public void encode(FriendlyByteBuf buf, UpdateRole msg)
            {
                msg.encode(buf);
            }
        };
        
        @Nullable
        public PlayerRole role;
        
        public UUID rolePlayerId;
        
        public UpdateRole(DuelMessageHeader header, @Nullable PlayerRole role, UUID rolePlayerId)
        {
            super(header);
            this.role = role;
            this.rolePlayerId = rolePlayerId;
        }
        
        public UpdateRole(DuelMessageHeader header, @Nullable PlayerRole role, Player rolePlayer)
        {
            this(header, role, rolePlayer.getUUID());
        }
        
        public UpdateRole(FriendlyByteBuf buf)
        {
            super(buf);
        }
        
        @Override
        public void encodeMessage(FriendlyByteBuf buf)
        {
            if(role != null)
            {
                buf.writeBoolean(true);
                DuelMessageUtility.encodePlayerRole(role, buf);
            }
            else
            {
                buf.writeBoolean(false);
            }
            
            buf.writeUUID(rolePlayerId);
        }
        
        @Override
        public void decodeMessage(FriendlyByteBuf buf)
        {
            if(buf.readBoolean())
            {
                role = DuelMessageUtility.decodePlayerRole(buf);
            }
            else
            {
                role = null;
            }
            
            rolePlayerId = buf.readUUID();
        }
        
        @Override
        public void handleMessage(Player player, IDuelManagerProvider provider)
        {
            Player rolePlayer = player.level().getPlayerByUUID(rolePlayerId);
            
            if(role != null && rolePlayer != null)
            {
                provider.getDuelManager().playerSelectRole(rolePlayer, role);
            }
            else
            {
                provider.getDuelManager().playerCloseContainerClient(rolePlayerId);
            }
        }
        
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
        {
            return TYPE;
        }
    }
    
    public static class UpdateDuelState extends DuelMessage.ClientBaseMessage
    {
        public static final CustomPacketPayload.Type<UpdateDuelState> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(YDM.MOD_ID, "duel_update_state"));
        public static final StreamCodec<FriendlyByteBuf, UpdateDuelState> STREAM_CODEC = new StreamCodec<>()
        {
            @Override
            public UpdateDuelState decode(FriendlyByteBuf buf)
            {
                return new UpdateDuelState(buf);
            }
            
            @Override
            public void encode(FriendlyByteBuf buf, UpdateDuelState msg)
            {
                msg.encode(buf);
            }
        };
        
        public DuelState duelState;
        
        public UpdateDuelState(DuelMessageHeader header, DuelState duelState)
        {
            super(header);
            this.duelState = duelState;
        }
        
        public UpdateDuelState(FriendlyByteBuf buf)
        {
            super(buf);
        }
        
        @Override
        public void encodeMessage(FriendlyByteBuf buf)
        {
            DuelMessageUtility.encodeDuelState(duelState, buf);
        }
        
        @Override
        public void decodeMessage(FriendlyByteBuf buf)
        {
            duelState = DuelMessageUtility.decodeDuelState(buf);
        }
        
        @Override
        public void handleMessage(Player player, IDuelManagerProvider provider)
        {
            provider.updateDuelState(duelState);
        }
        
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
        {
            return TYPE;
        }
    }
    
    public static class RequestFullUpdate extends DuelMessage.ServerBaseMessage
    {
        public static final CustomPacketPayload.Type<RequestFullUpdate> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(YDM.MOD_ID, "duel_request_full_update"));
        public static final StreamCodec<FriendlyByteBuf, RequestFullUpdate> STREAM_CODEC = new StreamCodec<>()
        {
            @Override
            public RequestFullUpdate decode(FriendlyByteBuf buf)
            {
                return new RequestFullUpdate(buf);
            }
            
            @Override
            public void encode(FriendlyByteBuf buf, RequestFullUpdate msg)
            {
                msg.encode(buf);
            }
        };
        
        public RequestFullUpdate(DuelMessageHeader header)
        {
            super(header);
        }
        
        public RequestFullUpdate(FriendlyByteBuf buf)
        {
            super(buf);
        }
        
        @Override
        public void encodeMessage(FriendlyByteBuf buf)
        {
            
        }
        
        @Override
        public void decodeMessage(FriendlyByteBuf buf)
        {
            
        }
        
        @Override
        public void handleMessage(Player player, IDuelManagerProvider provider)
        {
            provider.getDuelManager().sendAllTo(player);
        }
        
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
        {
            return TYPE;
        }
    }
    
    public static class RequestReady extends DuelMessage.ServerBaseMessage
    {
        public static final CustomPacketPayload.Type<RequestReady> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(YDM.MOD_ID, "duel_request_ready"));
        public static final StreamCodec<FriendlyByteBuf, RequestReady> STREAM_CODEC = new StreamCodec<>()
        {
            @Override
            public RequestReady decode(FriendlyByteBuf buf)
            {
                return new RequestReady(buf);
            }
            
            @Override
            public void encode(FriendlyByteBuf buf, RequestReady msg)
            {
                msg.encode(buf);
            }
        };
        
        public boolean ready;
        
        public RequestReady(DuelMessageHeader header, boolean ready)
        {
            super(header);
            this.ready = ready;
        }
        
        public RequestReady(FriendlyByteBuf buf)
        {
            super(buf);
        }
        
        @Override
        public void encodeMessage(FriendlyByteBuf buf)
        {
            buf.writeBoolean(ready);
        }
        
        @Override
        public void decodeMessage(FriendlyByteBuf buf)
        {
            ready = buf.readBoolean();
        }
        
        @Override
        public void handleMessage(Player player, IDuelManagerProvider provider)
        {
            provider.getDuelManager().requestReady(player, ready);
        }
        
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
        {
            return TYPE;
        }
    }
    
    public static class UpdateReady extends DuelMessage.ClientBaseMessage
    {
        public static final CustomPacketPayload.Type<UpdateReady> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(YDM.MOD_ID, "duel_update_ready"));
        public static final StreamCodec<FriendlyByteBuf, UpdateReady> STREAM_CODEC = new StreamCodec<>()
        {
            @Override
            public UpdateReady decode(FriendlyByteBuf buf)
            {
                return new UpdateReady(buf);
            }
            
            @Override
            public void encode(FriendlyByteBuf buf, UpdateReady msg)
            {
                msg.encode(buf);
            }
        };
        
        public PlayerRole role;
        public boolean ready;
        
        public UpdateReady(DuelMessageHeader header, PlayerRole role, boolean ready)
        {
            super(header);
            this.role = role;
            this.ready = ready;
        }
        
        public UpdateReady(FriendlyByteBuf buf)
        {
            super(buf);
        }
        
        @Override
        public void encodeMessage(FriendlyByteBuf buf)
        {
            DuelMessageUtility.encodePlayerRole(role, buf);
            buf.writeBoolean(ready);
        }
        
        @Override
        public void decodeMessage(FriendlyByteBuf buf)
        {
            role = DuelMessageUtility.decodePlayerRole(buf);
            ready = buf.readBoolean();
        }
        
        @Override
        public void handleMessage(Player player, IDuelManagerProvider provider)
        {
            provider.getDuelManager().updateReady(role, ready);
        }
        
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
        {
            return TYPE;
        }
    }
    
    public static class SendAvailableDecks extends DuelMessage.ClientBaseMessage
    {
        public static final CustomPacketPayload.Type<SendAvailableDecks> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(YDM.MOD_ID, "duel_send_available_decks"));
        public static final StreamCodec<FriendlyByteBuf, SendAvailableDecks> STREAM_CODEC = new StreamCodec<>()
        {
            @Override
            public SendAvailableDecks decode(FriendlyByteBuf buf)
            {
                return new SendAvailableDecks(buf);
            }
            
            @Override
            public void encode(FriendlyByteBuf buf, SendAvailableDecks msg)
            {
                msg.encode(buf);
            }
        };
        
        public List<DeckSource> deckSources;
        
        public SendAvailableDecks(DuelMessageHeader header, List<DeckSource> deckSources)
        {
            super(header);
            this.deckSources = deckSources;
        }
        
        public SendAvailableDecks(FriendlyByteBuf buf)
        {
            super(buf);
        }
        
        @Override
        public void encodeMessage(FriendlyByteBuf buf)
        {
            DuelMessageUtility.encodeList(deckSources, buf, DuelMessageUtility::encodeDeckSourceParams);
        }
        
        @Override
        public void decodeMessage(FriendlyByteBuf buf)
        {
            deckSources = DuelMessageUtility.decodeList(buf, DuelMessageUtility::decodeDeckSourceParams);
        }
        
        @Override
        public void handleMessage(Player player, IDuelManagerProvider provider)
        {
            provider.receiveDeckSources(deckSources);
        }
        
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
        {
            return TYPE;
        }
    }
    
    public static class RequestDeck extends DuelMessage.ServerBaseMessage
    {
        public static final CustomPacketPayload.Type<RequestDeck> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(YDM.MOD_ID, "duel_request_deck"));
        public static final StreamCodec<FriendlyByteBuf, RequestDeck> STREAM_CODEC = new StreamCodec<>()
        {
            @Override
            public RequestDeck decode(FriendlyByteBuf buf)
            {
                return new RequestDeck(buf);
            }
            
            @Override
            public void encode(FriendlyByteBuf buf, RequestDeck msg)
            {
                msg.encode(buf);
            }
        };
        
        public int index;
        
        public RequestDeck(DuelMessageHeader header, int index)
        {
            super(header);
            this.index = index;
        }
        
        public RequestDeck(FriendlyByteBuf buf)
        {
            super(buf);
        }
        
        @Override
        public void encodeMessage(FriendlyByteBuf buf)
        {
            buf.writeInt(index);
        }
        
        @Override
        public void decodeMessage(FriendlyByteBuf buf)
        {
            index = buf.readInt();
        }
        
        @Override
        public void handleMessage(Player player, IDuelManagerProvider provider)
        {
            provider.getDuelManager().requestDeck(index, player);
        }
        
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
        {
            return TYPE;
        }
    }
    
    public static class SendDeck extends DuelMessage.ClientBaseMessage
    {
        public static final CustomPacketPayload.Type<SendDeck> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(YDM.MOD_ID, "duel_send_deck"));
        public static final StreamCodec<FriendlyByteBuf, SendDeck> STREAM_CODEC = new StreamCodec<>()
        {
            @Override
            public SendDeck decode(FriendlyByteBuf buf)
            {
                return new SendDeck(buf);
            }
            
            @Override
            public void encode(FriendlyByteBuf buf, SendDeck msg)
            {
                msg.encode(buf);
            }
        };
        
        public int index;
        public DeckHolder deck;
        
        public SendDeck(DuelMessageHeader header, int index, DeckHolder deck)
        {
            super(header);
            this.index = index;
            this.deck = deck;
        }
        
        public SendDeck(FriendlyByteBuf buf)
        {
            super(buf);
        }
        
        @Override
        public void encodeMessage(FriendlyByteBuf buf)
        {
            buf.writeInt(index);
            DuelMessageUtility.encodeDeckHolder(deck, buf);
        }
        
        @Override
        public void decodeMessage(FriendlyByteBuf buf)
        {
            index = buf.readInt();
            deck = DuelMessageUtility.decodeDeckHolder(buf);
        }
        
        @Override
        public void handleMessage(Player player, IDuelManagerProvider provider)
        {
            provider.receiveDeck(index, deck);
        }
        
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
        {
            return TYPE;
        }
    }
    
    public static class ChooseDeck extends DuelMessage.ServerBaseMessage
    {
        public static final CustomPacketPayload.Type<ChooseDeck> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(YDM.MOD_ID, "duel_choose_deck"));
        public static final StreamCodec<FriendlyByteBuf, ChooseDeck> STREAM_CODEC = new StreamCodec<>()
        {
            @Override
            public ChooseDeck decode(FriendlyByteBuf buf)
            {
                return new ChooseDeck(buf);
            }
            
            @Override
            public void encode(FriendlyByteBuf buf, ChooseDeck msg)
            {
                msg.encode(buf);
            }
        };
        
        public int index;
        
        public ChooseDeck(DuelMessageHeader header, int index)
        {
            super(header);
            this.index = index;
        }
        
        public ChooseDeck(FriendlyByteBuf buf)
        {
            super(buf);
        }
        
        @Override
        public void encodeMessage(FriendlyByteBuf buf)
        {
            buf.writeInt(index);
        }
        
        @Override
        public void decodeMessage(FriendlyByteBuf buf)
        {
            index = buf.readInt();
        }
        
        @Override
        public void handleMessage(Player player, IDuelManagerProvider provider)
        {
            provider.getDuelManager().chooseDeck(index, player);
        }
        
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
        {
            return TYPE;
        }
    }
    
    public static class DeckAccepted extends DuelMessage.ClientBaseMessage
    {
        public static final CustomPacketPayload.Type<DeckAccepted> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(YDM.MOD_ID, "duel_deck_accepted"));
        public static final StreamCodec<FriendlyByteBuf, DeckAccepted> STREAM_CODEC = new StreamCodec<>()
        {
            @Override
            public DeckAccepted decode(FriendlyByteBuf buf)
            {
                return new DeckAccepted(buf);
            }
            
            @Override
            public void encode(FriendlyByteBuf buf, DeckAccepted msg)
            {
                msg.encode(buf);
            }
        };
        
        public PlayerRole role;
        
        public DeckAccepted(DuelMessageHeader header, PlayerRole role)
        {
            super(header);
            this.role = role;
        }
        
        public DeckAccepted(FriendlyByteBuf buf)
        {
            super(buf);
        }
        
        @Override
        public void encodeMessage(FriendlyByteBuf buf)
        {
            DuelMessageUtility.encodePlayerRole(role, buf);
        }
        
        @Override
        public void decodeMessage(FriendlyByteBuf buf)
        {
            role = DuelMessageUtility.decodePlayerRole(buf);
        }
        
        @Override
        public void handleMessage(Player player, IDuelManagerProvider provider)
        {
            provider.deckAccepted(role);
        }
        
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
        {
            return TYPE;
        }
    }
    
    public static class RequestDuelAction extends DuelMessage.ServerBaseMessage
    {
        public static final CustomPacketPayload.Type<RequestDuelAction> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(YDM.MOD_ID, "duel_request_action"));
        public static final StreamCodec<FriendlyByteBuf, RequestDuelAction> STREAM_CODEC = new StreamCodec<>()
        {
            @Override
            public RequestDuelAction decode(FriendlyByteBuf buf)
            {
                return new RequestDuelAction(buf);
            }
            
            @Override
            public void encode(FriendlyByteBuf buf, RequestDuelAction msg)
            {
                msg.encode(buf);
            }
        };
        
        public Action action;
        
        public RequestDuelAction(DuelMessageHeader header, Action action)
        {
            super(header);
            this.action = action;
        }
        
        public RequestDuelAction(FriendlyByteBuf buf)
        {
            super(buf);
        }
        
        @Override
        public void encodeMessage(FriendlyByteBuf buf)
        {
            DuelMessageUtility.encodeAction(action, buf);
        }
        
        @Override
        public void decodeMessage(FriendlyByteBuf buf)
        {
            action = DuelMessageUtility.decodeAction(buf);
        }
        
        @Override
        public void handleMessage(Player player, IDuelManagerProvider provider)
        {
            provider.getDuelManager().receiveActionFrom(player, action);
        }
        
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
        {
            return TYPE;
        }
    }
    
    public static class DuelAction extends DuelMessage.ClientBaseMessage
    {
        public static final CustomPacketPayload.Type<DuelAction> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(YDM.MOD_ID, "duel_action"));
        public static final StreamCodec<FriendlyByteBuf, DuelAction> STREAM_CODEC = new StreamCodec<>()
        {
            @Override
            public DuelAction decode(FriendlyByteBuf buf)
            {
                return new DuelAction(buf);
            }
            
            @Override
            public void encode(FriendlyByteBuf buf, DuelAction msg)
            {
                msg.encode(buf);
            }
        };
        
        //        public PlayerRole source;
        public Action action;
        
        // must be PlayerRole (not ZoneOwner) because Judges will also be able to do stuff
        // player role require?
        public DuelAction(DuelMessageHeader header, /* PlayerRole source,*/ Action action)
        {
            super(header);
            //            this.source = source;
            this.action = action;
        }
        
        public DuelAction(FriendlyByteBuf buf)
        {
            super(buf);
        }
        
        @Override
        public void encodeMessage(FriendlyByteBuf buf)
        {
            //encodePlayerRole
            DuelMessageUtility.encodeAction(action, buf);
        }
        
        @Override
        public void decodeMessage(FriendlyByteBuf buf)
        {
            //decodePlayerRole
            action = DuelMessageUtility.decodeAction(buf);
        }
        
        @Override
        public void handleMessage(Player player, IDuelManagerProvider provider)
        {
            provider.handleAction(action);
        }
        
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
        {
            return TYPE;
        }
    }
    
    public static class AllDuelActions extends DuelMessage.ClientBaseMessage
    {
        public static final CustomPacketPayload.Type<AllDuelActions> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(YDM.MOD_ID, "duel_all_actions"));
        public static final StreamCodec<FriendlyByteBuf, AllDuelActions> STREAM_CODEC = new StreamCodec<>()
        {
            @Override
            public AllDuelActions decode(FriendlyByteBuf buf)
            {
                return new AllDuelActions(buf);
            }
            
            @Override
            public void encode(FriendlyByteBuf buf, AllDuelActions msg)
            {
                msg.encode(buf);
            }
        };
        
        public List<Action> actions;
        
        public AllDuelActions(DuelMessageHeader header, List<Action> actions)
        {
            super(header);
            this.actions = actions;
        }
        
        public AllDuelActions(FriendlyByteBuf buf)
        {
            super(buf);
        }
        
        @Override
        public void encodeMessage(FriendlyByteBuf buf)
        {
            //encodePlayerRole ?? if this is done in DuelAction class, might need to do it here too
            DuelMessageUtility.encodeActions(actions, buf);
        }
        
        @Override
        public void decodeMessage(FriendlyByteBuf buf)
        {
            actions = DuelMessageUtility.decodeActions(buf);
        }
        
        @Override
        public void handleMessage(Player player, IDuelManagerProvider provider)
        {
            provider.handleAllActions(actions);
        }
        
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
        {
            return TYPE;
        }
    }
    
    public static class SendMessageToServer extends DuelMessage.ServerBaseMessage
    {
        public static final CustomPacketPayload.Type<SendMessageToServer> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(YDM.MOD_ID, "duel_send_msg_server"));
        public static final StreamCodec<FriendlyByteBuf, SendMessageToServer> STREAM_CODEC = new StreamCodec<>()
        {
            @Override
            public SendMessageToServer decode(FriendlyByteBuf buf)
            {
                return new SendMessageToServer(buf);
            }
            
            @Override
            public void encode(FriendlyByteBuf buf, SendMessageToServer msg)
            {
                msg.encode(buf);
            }
        };
        
        public Component message;
        
        public SendMessageToServer(DuelMessageHeader header, Component message)
        {
            super(header);
            this.message = message;
        }
        
        public SendMessageToServer(FriendlyByteBuf buf)
        {
            super(buf);
        }
        
        @Override
        public void encodeMessage(FriendlyByteBuf buf)
        {
            net.minecraft.network.chat.ComponentSerialization.STREAM_CODEC.encode((net.minecraft.network.RegistryFriendlyByteBuf) buf, message);
        }
        
        @Override
        public void decodeMessage(FriendlyByteBuf buf)
        {
            message = net.minecraft.network.chat.ComponentSerialization.STREAM_CODEC.decode((net.minecraft.network.RegistryFriendlyByteBuf) buf);
        }
        
        @Override
        public void handleMessage(Player player, IDuelManagerProvider provider)
        {
            provider.getDuelManager().receiveMessageFromClient(player, message);
        }
        
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
        {
            return TYPE;
        }
    }
    
    public static class SendMessageToClient extends DuelMessage.ClientBaseMessage
    {
        public static final CustomPacketPayload.Type<SendMessageToClient> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(YDM.MOD_ID, "duel_send_msg_client"));
        public static final StreamCodec<FriendlyByteBuf, SendMessageToClient> STREAM_CODEC = new StreamCodec<>()
        {
            @Override
            public SendMessageToClient decode(FriendlyByteBuf buf)
            {
                return new SendMessageToClient(buf);
            }
            
            @Override
            public void encode(FriendlyByteBuf buf, SendMessageToClient msg)
            {
                msg.encode(buf);
            }
        };
        
        public DuelChatMessage message;
        
        public SendMessageToClient(DuelMessageHeader header, DuelChatMessage message)
        {
            super(header);
            this.message = message;
        }
        
        public SendMessageToClient(FriendlyByteBuf buf)
        {
            super(buf);
        }
        
        @Override
        public void encodeMessage(FriendlyByteBuf buf)
        {
            DuelMessageUtility.encodeDuelChatMessage(message, buf);
        }
        
        @Override
        public void decodeMessage(FriendlyByteBuf buf)
        {
            message = DuelMessageUtility.decodeDuelChatMessage(buf);
        }
        
        @Override
        public void handleMessage(Player player, IDuelManagerProvider provider)
        {
            provider.receiveMessage(player, message);
        }
        
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
        {
            return TYPE;
        }
    }
    
    public static class SendAllMessagesToClient extends DuelMessage.ClientBaseMessage
    {
        public static final CustomPacketPayload.Type<SendAllMessagesToClient> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(YDM.MOD_ID, "duel_send_all_msgs_client"));
        public static final StreamCodec<FriendlyByteBuf, SendAllMessagesToClient> STREAM_CODEC = new StreamCodec<>()
        {
            @Override
            public SendAllMessagesToClient decode(FriendlyByteBuf buf)
            {
                return new SendAllMessagesToClient(buf);
            }
            
            @Override
            public void encode(FriendlyByteBuf buf, SendAllMessagesToClient msg)
            {
                msg.encode(buf);
            }
        };
        
        public List<DuelChatMessage> messages;
        
        public SendAllMessagesToClient(DuelMessageHeader header, List<DuelChatMessage> message)
        {
            super(header);
            messages = message;
        }
        
        public SendAllMessagesToClient(FriendlyByteBuf buf)
        {
            super(buf);
        }
        
        @Override
        public void encodeMessage(FriendlyByteBuf buf0)
        {
            DuelMessageUtility.encodeList(messages, buf0, DuelMessageUtility::encodeDuelChatMessage);
        }
        
        @Override
        public void decodeMessage(FriendlyByteBuf buf0)
        {
            messages = DuelMessageUtility.decodeList(buf0, DuelMessageUtility::decodeDuelChatMessage);
        }
        
        @Override
        public void handleMessage(Player player, IDuelManagerProvider provider)
        {
            for(DuelChatMessage message : messages)
            {
                provider.receiveMessage(player, message);
            }
        }
        
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
        {
            return TYPE;
        }
    }
    
    public static class SendAdmitDefeat extends DuelMessage.ServerBaseMessage
    {
        public static final CustomPacketPayload.Type<SendAdmitDefeat> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(YDM.MOD_ID, "duel_admit_defeat"));
        public static final StreamCodec<FriendlyByteBuf, SendAdmitDefeat> STREAM_CODEC = new StreamCodec<>()
        {
            @Override
            public SendAdmitDefeat decode(FriendlyByteBuf buf)
            {
                return new SendAdmitDefeat(buf);
            }
            
            @Override
            public void encode(FriendlyByteBuf buf, SendAdmitDefeat msg)
            {
                msg.encode(buf);
            }
        };
        
        public SendAdmitDefeat(DuelMessageHeader header)
        {
            super(header);
        }
        
        public SendAdmitDefeat(FriendlyByteBuf buf)
        {
            super(buf);
        }
        
        @Override
        public void encodeMessage(FriendlyByteBuf buf)
        {
        
        }
        
        @Override
        public void decodeMessage(FriendlyByteBuf buf)
        {
        
        }
        
        @Override
        public void handleMessage(Player player, IDuelManagerProvider provider)
        {
            provider.getDuelManager().playerAdmitsDefeat(player);
        }
        
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
        {
            return TYPE;
        }
    }
    
    public static class SendOfferDraw extends DuelMessage.ServerBaseMessage
    {
        public static final CustomPacketPayload.Type<SendOfferDraw> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(YDM.MOD_ID, "duel_offer_draw"));
        public static final StreamCodec<FriendlyByteBuf, SendOfferDraw> STREAM_CODEC = new StreamCodec<>()
        {
            @Override
            public SendOfferDraw decode(FriendlyByteBuf buf)
            {
                return new SendOfferDraw(buf);
            }
            
            @Override
            public void encode(FriendlyByteBuf buf, SendOfferDraw msg)
            {
                msg.encode(buf);
            }
        };
        
        public SendOfferDraw(DuelMessageHeader header)
        {
            super(header);
        }
        
        public SendOfferDraw(FriendlyByteBuf buf)
        {
            super(buf);
        }
        
        @Override
        public void encodeMessage(FriendlyByteBuf buf)
        {
        
        }
        
        @Override
        public void decodeMessage(FriendlyByteBuf buf)
        {
        
        }
        
        @Override
        public void handleMessage(Player player, IDuelManagerProvider provider)
        {
            provider.getDuelManager().playerOffersDraw(player);
        }
        
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
        {
            return TYPE;
        }
    }
}
