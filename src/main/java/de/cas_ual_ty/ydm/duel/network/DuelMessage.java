package de.cas_ual_ty.ydm.duel.network;

import de.cas_ual_ty.ydm.YDM;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public abstract class DuelMessage
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
            // TODO: Port to NeoForge payload system - obtain sender from payload context
            return null;
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
    
    // TODO: Port to NeoForge payload system - handle method stub
    public void handle()
    {
        Player player = getPlayer();
        if(player != null && decodedHeader != null)
        {
            handleMessage(player, decodedHeader.getDuelManager(player));
        }
    }
}