package de.cas_ual_ty.ydm.duel.network;

import de.cas_ual_ty.ydm.YDM;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public abstract class DuelMessage implements CustomPacketPayload
{
    // Server -> Client
    public abstract static class ClientBaseMessage extends DuelMessage
    {
        public ClientBaseMessage(DuelMessageHeader header)
        {
            super(header);
        }
        
        public ClientBaseMessage(FriendlyByteBuf buf)
        {
            super(buf);
        }
        
        @Override
        public final Player getPlayer()
        {
            return YDM.proxy.getClientPlayer();
        }
    }
    
    // Client -> Server
    public abstract static class ServerBaseMessage extends DuelMessage
    {
        public transient Player sender;
        
        public ServerBaseMessage(DuelMessageHeader header)
        {
            super(header);
        }
        
        public ServerBaseMessage(FriendlyByteBuf buf)
        {
            super(buf);
        }
        
        @Override
        public final Player getPlayer()
        {
            return sender;
        }
    }
    
    private DuelMessageHeader header;
    private DuelMessageHeader decodedHeader;
    
    public DuelMessage(DuelMessageHeader header)
    {
        this.header = header;
    }
    
    public DuelMessage(FriendlyByteBuf buf)
    {
        decodedHeader = DuelMessageUtility.decodeHeader(buf);
        decodeMessage(buf);
    }
    
    public void encode(FriendlyByteBuf buf)
    {
        DuelMessageUtility.encodeHeader(header, buf);
        encodeMessage(buf);
    }
    
    public abstract void encodeMessage(FriendlyByteBuf buf);
    
    public abstract void decodeMessage(FriendlyByteBuf buf);
    
    public abstract void handleMessage(Player player, IDuelManagerProvider provider);
    
    public abstract Player getPlayer();
    
    public void handle()
    {
        Player player = getPlayer();
        if(player != null && decodedHeader != null)
        {
            handleMessage(player, decodedHeader.getDuelManager(player));
        }
    }
}