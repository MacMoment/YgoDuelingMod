package de.cas_ual_ty.ydm;

import de.cas_ual_ty.ydm.duel.block.DuelTileEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class YdmTileEntityTypes
{
    private static final DeferredRegister<BlockEntityType<?>> DEFERRED_REGISTER = DeferredRegister.create(net.minecraft.core.registries.Registries.BLOCK_ENTITY_TYPE, YDM.MOD_ID);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DuelTileEntity>> DUEL = DEFERRED_REGISTER.register("duel", () -> BlockEntityType.Builder.of((pos, state) -> new DuelTileEntity(YdmTileEntityTypes.DUEL.get(), pos, state), YdmBlocks.DUEL_PLAYMAT.get(), YdmBlocks.DUEL_TABLE.get()).build(null));
    
    public static void register(IEventBus bus)
    {
        DEFERRED_REGISTER.register(bus);
    }
}