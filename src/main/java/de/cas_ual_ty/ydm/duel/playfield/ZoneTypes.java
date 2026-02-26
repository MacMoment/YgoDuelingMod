package de.cas_ual_ty.ydm.duel.playfield;

import de.cas_ual_ty.ydm.YDM;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ZoneTypes
{
    private static final DeferredRegister<ZoneType> DEFERRED_REGISTER = DeferredRegister.create(YDM.ZONE_TYPE_KEY, YDM.MOD_ID);
    
    public static final DeferredHolder<ZoneType, ZoneType> HAND = DEFERRED_REGISTER.register("hand", () -> new ZoneType().showFaceDownCardsToOwner());
    public static final DeferredHolder<ZoneType, ZoneType> DECK = DEFERRED_REGISTER.register("deck", () -> new ZoneType().secret().keepFocusedAfterInteraction().defaultCardPosition(CardPosition.FD));
    public static final DeferredHolder<ZoneType, ZoneType> SPELL_TRAP = DEFERRED_REGISTER.register("spell_trap", () -> new ZoneType().canHaveCounters());
    public static final DeferredHolder<ZoneType, ZoneType> EXTRA_DECK = DEFERRED_REGISTER.register("extra_deck", () -> new ZoneType().keepFocusedAfterInteraction());
    public static final DeferredHolder<ZoneType, ZoneType> GRAVEYARD = DEFERRED_REGISTER.register("graveyard", () -> new ZoneType().strict().keepFocusedAfterInteraction());
    public static final DeferredHolder<ZoneType, ZoneType> MONSTER = DEFERRED_REGISTER.register("monster", () -> new ZoneType().allowSideways().canHaveCounters());
    public static final DeferredHolder<ZoneType, ZoneType> FIELD_SPELL = DEFERRED_REGISTER.register("field_spell", () -> new ZoneType().canHaveCounters());
    public static final DeferredHolder<ZoneType, ZoneType> BANISHED = DEFERRED_REGISTER.register("banished", () -> new ZoneType().strict().keepFocusedAfterInteraction());
    public static final DeferredHolder<ZoneType, ZoneType> EXTRA = DEFERRED_REGISTER.register("extra", () -> new ZoneType().keepFocusedAfterInteraction());
    public static final DeferredHolder<ZoneType, ZoneType> EXTRA_MONSTER_RIGHT = DEFERRED_REGISTER.register("extra_monster_right", () -> new ZoneType().noOwner().canHaveCounters());
    public static final DeferredHolder<ZoneType, ZoneType> EXTRA_MONSTER_LEFT = DEFERRED_REGISTER.register("extra_monster_left", () -> new ZoneType().noOwner().canHaveCounters());
    
    public static void register(IEventBus bus)
    {
        DEFERRED_REGISTER.register(bus);
    }
}