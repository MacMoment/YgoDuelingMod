package de.cas_ual_ty.ydm.duel.network;

import de.cas_ual_ty.ydm.YDM;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class DuelMessageHeaders
{
    private static final DeferredRegister<DuelMessageHeaderType> DEFERRED_REGISTER = DeferredRegister.create(YDM.DUEL_MESSAGE_HEADER_KEY, YDM.MOD_ID);
    
    public static final DeferredHolder<DuelMessageHeaderType, DuelMessageHeaderType> CONTAINER = DEFERRED_REGISTER.register("container", () -> new DuelMessageHeaderType(() -> new DuelMessageHeader.ContainerHeader(DuelMessageHeaders.CONTAINER.get())));
    public static final DeferredHolder<DuelMessageHeaderType, DuelMessageHeaderType> TILE_ENTITY = DEFERRED_REGISTER.register("tile_entity", () -> new DuelMessageHeaderType(() -> new DuelMessageHeader.TileEntityHeader(DuelMessageHeaders.TILE_ENTITY.get())));
    public static final DeferredHolder<DuelMessageHeaderType, DuelMessageHeaderType> ENTITY = DEFERRED_REGISTER.register("entity", () -> new DuelMessageHeaderType(() -> new DuelMessageHeader.EntityHeader(DuelMessageHeaders.ENTITY.get())));
    
    public static void register(IEventBus bus)
    {
        DEFERRED_REGISTER.register(bus);
    }
}