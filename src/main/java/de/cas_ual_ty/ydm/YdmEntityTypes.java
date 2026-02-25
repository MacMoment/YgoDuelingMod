package de.cas_ual_ty.ydm;

import de.cas_ual_ty.ydm.duel.dueldisk.DuelEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class YdmEntityTypes
{
    private static final DeferredRegister<EntityType<?>> DEFERRED_REGISTER = DeferredRegister.create(Registries.ENTITY_TYPE, YDM.MOD_ID);
    private static final ResourceKey<EntityType<?>> DUEL_KEY = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(YDM.MOD_ID, "duel"));
    public static final DeferredHolder<EntityType<?>, EntityType<?>> DUEL = DEFERRED_REGISTER.register("duel", () -> EntityType.Builder.of(DuelEntity::new, MobCategory.MISC).noSave().setShouldReceiveVelocityUpdates(false).sized(0, 0).fireImmune().build(DUEL_KEY));
    
    public static void register(IEventBus bus)
    {
        DEFERRED_REGISTER.register(bus);
    }
}