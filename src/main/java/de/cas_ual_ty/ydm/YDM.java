package de.cas_ual_ty.ydm;

import de.cas_ual_ty.ydm.cardinventory.JsonCardsManager;
import de.cas_ual_ty.ydm.cardbinder.CardBinderMessages;
import de.cas_ual_ty.ydm.carditeminventory.CIIMessages;
import de.cas_ual_ty.ydm.cardsupply.CardSupplyMessages;
import de.cas_ual_ty.ydm.deckbox.DeckBoxItem;
import de.cas_ual_ty.ydm.deckbox.DeckHolder;
import de.cas_ual_ty.ydm.deckbox.ItemHandlerDeckHolder;
import de.cas_ual_ty.ydm.duel.FindDecksEvent;
import de.cas_ual_ty.ydm.duel.action.ActionIcon;
import de.cas_ual_ty.ydm.duel.action.ActionIcons;
import de.cas_ual_ty.ydm.duel.action.ActionType;
import de.cas_ual_ty.ydm.duel.action.ActionTypes;
import de.cas_ual_ty.ydm.duel.network.DuelMessageHeaderType;
import de.cas_ual_ty.ydm.duel.network.DuelMessageHeaders;
import de.cas_ual_ty.ydm.duel.network.DuelMessages;
import de.cas_ual_ty.ydm.duel.playfield.ZoneType;
import de.cas_ual_ty.ydm.duel.playfield.ZoneTypes;
import de.cas_ual_ty.ydm.serverutil.YdmCommand;
import de.cas_ual_ty.ydm.task.WorkerManager;
import de.cas_ual_ty.ydm.util.CooldownHolder;
import de.cas_ual_ty.ydm.util.ISidedProxy;
import de.cas_ual_ty.ydm.util.YDMItemHandler;
import de.cas_ual_ty.ydm.util.YdmIOUtil;
import de.cas_ual_ty.ydm.cardbinder.UUIDHolder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Random;
import java.util.function.Supplier;

@Mod(YDM.MOD_ID)
public class YDM
{
    public static final String MOD_ID = "ydm";
    public static final String MOD_ID_UP = YDM.MOD_ID.toUpperCase();
    public static final String PROTOCOL_VERSION = "1";
    
    private static final Logger LOGGER = LogManager.getLogger();
    
    public static YDM instance;
    public static ISidedProxy proxy;
    public static Random random;
    
    public static ModConfigSpec commonConfigSpec;
    public static CommonConfig commonConfig;
    
    public static String dbSourceUrl;
    
    public static File mainFolder;
    public static File cardsFolder;
    public static File setsFolder;
    public static File distributionsFolder;
    public static File raritiesFolder;
    public static File bindersFolder;
    
    // Data Attachments (replaces old Capability system)
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, MOD_ID);
    public static final Supplier<AttachmentType<UUIDHolder>> UUID_HOLDER = ATTACHMENT_TYPES.register("uuid_holder", () -> AttachmentType.serializable(UUIDHolder::new).build());
    public static final Supplier<AttachmentType<YDMItemHandler>> CARD_ITEM_INVENTORY = ATTACHMENT_TYPES.register("card_item_inventory", () -> AttachmentType.serializable(() -> new YDMItemHandler(0)).build());
    public static final Supplier<AttachmentType<CooldownHolder>> COOLDOWN_HOLDER = ATTACHMENT_TYPES.register("cooldown_holder", () -> AttachmentType.serializable(CooldownHolder::new).build());
    
    // Custom Registry Keys
    public static final ResourceKey<Registry<ActionIcon>> ACTION_ICON_KEY = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(MOD_ID, "action_icons"));
    public static final ResourceKey<Registry<ZoneType>> ZONE_TYPE_KEY = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(MOD_ID, "zone_types"));
    public static final ResourceKey<Registry<ActionType>> ACTION_TYPE_KEY = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(MOD_ID, "action_types"));
    public static final ResourceKey<Registry<DuelMessageHeaderType>> DUEL_MESSAGE_HEADER_KEY = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(MOD_ID, "duel_message_headers"));
    
    public static Supplier<Registry<ActionIcon>> actionIconRegistry;
    public static Supplier<Registry<ZoneType>> zoneTypeRegistry;
    public static Supplier<Registry<ActionType>> actionTypeRegistry;
    public static Supplier<Registry<DuelMessageHeaderType>> duelMessageHeaderRegistry;
    
    public static volatile boolean continueTasks = true;
    public static volatile boolean forceTaskStop = false;
    
    public YDM(IEventBus modBus, ModContainer modContainer)
    {
        YDM.instance = this;
        
        if (FMLEnvironment.dist == Dist.CLIENT) {
            YDM.proxy = new de.cas_ual_ty.ydm.clientutil.ClientProxy();
        } else {
            YDM.proxy = new de.cas_ual_ty.ydm.serverutil.ServerProxy();
        }
        
        YDM.random = new Random();
        
        Pair<CommonConfig, ModConfigSpec> common = new ModConfigSpec.Builder().configure(CommonConfig::new);
        YDM.commonConfig = common.getLeft();
        YDM.commonConfigSpec = common.getRight();
        modContainer.registerConfig(ModConfig.Type.COMMON, YDM.commonConfigSpec);
        
        modBus.addListener(this::init);
        modBus.addListener(this::modConfig);
        modBus.addListener(this::newRegistry);
        modBus.addListener(this::registerPayloads);
        YDM.proxy.registerModEventListeners(modBus);
        
        YdmBlocks.register(modBus);
        YdmItems.register(modBus);
        YdmContainerTypes.register(modBus);
        YdmEntityTypes.register(modBus);
        YdmTileEntityTypes.register(modBus);
        ATTACHMENT_TYPES.register(modBus);
        ActionIcons.register(modBus);
        ZoneTypes.register(modBus);
        ActionTypes.register(modBus);
        DuelMessageHeaders.register(modBus);
        
        IEventBus forgeBus = NeoForge.EVENT_BUS;
        forgeBus.addListener(this::playerClone);
        forgeBus.addListener(this::playerTick);
        forgeBus.addListener(this::registerCommands);
        forgeBus.addListener(this::findDecks);
        forgeBus.addListener(this::serverStopped);
        YDM.proxy.registerForgeEventListeners(forgeBus);
        
        YDM.proxy.preInit();
        initFolders();
    }
    
    private void init(FMLCommonSetupEvent event)
    {
        initFiles();
        YDM.proxy.init();
        WorkerManager.init();
    }
    
    private void registerPayloads(RegisterPayloadHandlersEvent event)
    {
        PayloadRegistrar registrar = event.registrar(MOD_ID);
        
        // Card Supply
        registrar.playToServer(CardSupplyMessages.RequestCard.TYPE, CardSupplyMessages.RequestCard.STREAM_CODEC, (msg, ctx) ->
        {
            ctx.enqueueWork(() -> CardSupplyMessages.RequestCard.handle(msg, ctx.player()));
        });
        
        // Card Item Inventory
        registrar.playToClient(CIIMessages.SetPage.TYPE, CIIMessages.SetPage.STREAM_CODEC, (msg, ctx) ->
        {
            ctx.enqueueWork(() -> CIIMessages.SetPage.handle(msg));
        });
        registrar.playToServer(CIIMessages.ChangePage.TYPE, CIIMessages.ChangePage.STREAM_CODEC, (msg, ctx) ->
        {
            ctx.enqueueWork(() -> CIIMessages.ChangePage.handle(msg, ctx.player()));
        });
        
        // Card Binder
        registrar.playToServer(CardBinderMessages.ChangePage.TYPE, CardBinderMessages.ChangePage.STREAM_CODEC, (msg, ctx) ->
        {
            ctx.enqueueWork(() -> CardBinderMessages.ChangePage.handle(msg, ctx.player()));
        });
        registrar.playToServer(CardBinderMessages.ChangeSearch.TYPE, CardBinderMessages.ChangeSearch.STREAM_CODEC, (msg, ctx) ->
        {
            ctx.enqueueWork(() -> CardBinderMessages.ChangeSearch.handle(msg, ctx.player()));
        });
        registrar.playToClient(CardBinderMessages.UpdatePage.TYPE, CardBinderMessages.UpdatePage.STREAM_CODEC, (msg, ctx) ->
        {
            ctx.enqueueWork(() -> CardBinderMessages.UpdatePage.handle(msg));
        });
        registrar.playToClient(CardBinderMessages.UpdateList.TYPE, CardBinderMessages.UpdateList.STREAM_CODEC, (msg, ctx) ->
        {
            ctx.enqueueWork(() -> CardBinderMessages.UpdateList.handle(msg));
        });
        registrar.playToServer(CardBinderMessages.IndexClicked.TYPE, CardBinderMessages.IndexClicked.STREAM_CODEC, (msg, ctx) ->
        {
            ctx.enqueueWork(() -> CardBinderMessages.IndexClicked.handle(msg, ctx.player()));
        });
        registrar.playToServer(CardBinderMessages.IndexDropped.TYPE, CardBinderMessages.IndexDropped.STREAM_CODEC, (msg, ctx) ->
        {
            ctx.enqueueWork(() -> CardBinderMessages.IndexDropped.handle(msg, ctx.player()));
        });
        
        // Duel Messages - Server bound (client -> server)
        registrar.playToServer(DuelMessages.SelectRole.TYPE, DuelMessages.SelectRole.STREAM_CODEC, (msg, ctx) ->
        {
            ctx.enqueueWork(() -> { msg.sender = ctx.player(); msg.handle(); });
        });
        registrar.playToServer(DuelMessages.RequestFullUpdate.TYPE, DuelMessages.RequestFullUpdate.STREAM_CODEC, (msg, ctx) ->
        {
            ctx.enqueueWork(() -> { msg.sender = ctx.player(); msg.handle(); });
        });
        registrar.playToServer(DuelMessages.RequestReady.TYPE, DuelMessages.RequestReady.STREAM_CODEC, (msg, ctx) ->
        {
            ctx.enqueueWork(() -> { msg.sender = ctx.player(); msg.handle(); });
        });
        registrar.playToServer(DuelMessages.RequestDeck.TYPE, DuelMessages.RequestDeck.STREAM_CODEC, (msg, ctx) ->
        {
            ctx.enqueueWork(() -> { msg.sender = ctx.player(); msg.handle(); });
        });
        registrar.playToServer(DuelMessages.ChooseDeck.TYPE, DuelMessages.ChooseDeck.STREAM_CODEC, (msg, ctx) ->
        {
            ctx.enqueueWork(() -> { msg.sender = ctx.player(); msg.handle(); });
        });
        registrar.playToServer(DuelMessages.RequestDuelAction.TYPE, DuelMessages.RequestDuelAction.STREAM_CODEC, (msg, ctx) ->
        {
            ctx.enqueueWork(() -> { msg.sender = ctx.player(); msg.handle(); });
        });
        registrar.playToServer(DuelMessages.SendMessageToServer.TYPE, DuelMessages.SendMessageToServer.STREAM_CODEC, (msg, ctx) ->
        {
            ctx.enqueueWork(() -> { msg.sender = ctx.player(); msg.handle(); });
        });
        registrar.playToServer(DuelMessages.SendAdmitDefeat.TYPE, DuelMessages.SendAdmitDefeat.STREAM_CODEC, (msg, ctx) ->
        {
            ctx.enqueueWork(() -> { msg.sender = ctx.player(); msg.handle(); });
        });
        registrar.playToServer(DuelMessages.SendOfferDraw.TYPE, DuelMessages.SendOfferDraw.STREAM_CODEC, (msg, ctx) ->
        {
            ctx.enqueueWork(() -> { msg.sender = ctx.player(); msg.handle(); });
        });
        
        // Duel Messages - Client bound (server -> client)
        registrar.playToClient(DuelMessages.UpdateRole.TYPE, DuelMessages.UpdateRole.STREAM_CODEC, (msg, ctx) ->
        {
            ctx.enqueueWork(msg::handle);
        });
        registrar.playToClient(DuelMessages.UpdateDuelState.TYPE, DuelMessages.UpdateDuelState.STREAM_CODEC, (msg, ctx) ->
        {
            ctx.enqueueWork(msg::handle);
        });
        registrar.playToClient(DuelMessages.UpdateReady.TYPE, DuelMessages.UpdateReady.STREAM_CODEC, (msg, ctx) ->
        {
            ctx.enqueueWork(msg::handle);
        });
        registrar.playToClient(DuelMessages.SendAvailableDecks.TYPE, DuelMessages.SendAvailableDecks.STREAM_CODEC, (msg, ctx) ->
        {
            ctx.enqueueWork(msg::handle);
        });
        registrar.playToClient(DuelMessages.SendDeck.TYPE, DuelMessages.SendDeck.STREAM_CODEC, (msg, ctx) ->
        {
            ctx.enqueueWork(msg::handle);
        });
        registrar.playToClient(DuelMessages.DeckAccepted.TYPE, DuelMessages.DeckAccepted.STREAM_CODEC, (msg, ctx) ->
        {
            ctx.enqueueWork(msg::handle);
        });
        registrar.playToClient(DuelMessages.DuelAction.TYPE, DuelMessages.DuelAction.STREAM_CODEC, (msg, ctx) ->
        {
            ctx.enqueueWork(msg::handle);
        });
        registrar.playToClient(DuelMessages.AllDuelActions.TYPE, DuelMessages.AllDuelActions.STREAM_CODEC, (msg, ctx) ->
        {
            ctx.enqueueWork(msg::handle);
        });
        registrar.playToClient(DuelMessages.SendMessageToClient.TYPE, DuelMessages.SendMessageToClient.STREAM_CODEC, (msg, ctx) ->
        {
            ctx.enqueueWork(msg::handle);
        });
        registrar.playToClient(DuelMessages.SendAllMessagesToClient.TYPE, DuelMessages.SendAllMessagesToClient.STREAM_CODEC, (msg, ctx) ->
        {
            ctx.enqueueWork(msg::handle);
        });
    }
    
    public void initFolders()
    {
        YDM.mainFolder = new File("ydm_db");
        YDM.cardsFolder = new File(YDM.mainFolder, "cards");
        YDM.setsFolder = new File(YDM.mainFolder, "sets");
        YDM.distributionsFolder = new File(YDM.mainFolder, "distributions");
        YDM.raritiesFolder = new File(YDM.mainFolder, "rarities");
        
        YDM.bindersFolder = new File("ydm_binders");
        YdmIOUtil.createDirIfNonExistant(YDM.bindersFolder);
        
        YDM.proxy.initFolders();
    }
    
    private void initFiles()
    {
        YDM.proxy.initFiles();
        YdmIOUtil.setAgent();
        YdmDatabase.initDatabase();
    }
    
    private void playerClone(PlayerEvent.Clone event)
    {
        final Player original = event.getOriginal();
        final Player current = event.getEntity();
        
        if (original.hasData(COOLDOWN_HOLDER))
        {
            current.getData(COOLDOWN_HOLDER).deserializeNBT(original.registryAccess(), original.getData(COOLDOWN_HOLDER).serializeNBT(original.registryAccess()));
        }
    }
    
    private void playerTick(PlayerTickEvent.Post event)
    {
        Player player = event.getEntity();
        if (player.hasData(COOLDOWN_HOLDER))
        {
            player.getData(COOLDOWN_HOLDER).tick();
        }
    }
    
    private void registerCommands(RegisterCommandsEvent event)
    {
        YdmCommand.registerCommand(event.getDispatcher());
    }
    
    private void modConfig(ModConfigEvent event)
    {
        if(event.getConfig().getSpec() == YDM.commonConfigSpec)
        {
            YDM.log("Baking common config!");
            YDM.dbSourceUrl = YDM.commonConfig.dbSourceUrl.get();
        }
    }
    
    private void findDecks(FindDecksEvent event)
    {
        Player player = event.getEntity();
        
        ItemStack itemStack;
        DeckHolder dh;
        
        for(int i = 0; i < player.getInventory().getContainerSize(); ++i)
        {
            itemStack = player.getInventory().getItem(i);
            
            if(itemStack.getItem() instanceof DeckBoxItem deckBoxItem)
            {
                dh = new ItemHandlerDeckHolder(deckBoxItem.getItemHandler(itemStack));
                
                if(!dh.isEmpty())
                {
                    event.addDeck(dh, itemStack);
                }
            }
        }
        
        itemStack = player.getOffhandItem();
        if(itemStack.getItem() instanceof DeckBoxItem deckBoxItem)
        {
            dh = new ItemHandlerDeckHolder(deckBoxItem.getItemHandler(itemStack));
            
            if(!dh.isEmpty())
            {
                event.addDeck(dh, itemStack);
            }
        }
    }
    
    private void newRegistry(NewRegistryEvent event)
    {
        YDM.actionIconRegistry = event.create(new RegistryBuilder<>(ACTION_ICON_KEY).sync(true).maxId(511));
        YDM.zoneTypeRegistry = event.create(new RegistryBuilder<>(ZONE_TYPE_KEY).sync(true).maxId(511));
        YDM.actionTypeRegistry = event.create(new RegistryBuilder<>(ACTION_TYPE_KEY).sync(true).maxId(511));
        YDM.duelMessageHeaderRegistry = event.create(new RegistryBuilder<>(DUEL_MESSAGE_HEADER_KEY).sync(true).maxId(63));
    }
    
    private void serverStopped(ServerStoppedEvent event)
    {
        synchronized(JsonCardsManager.LOADED_MANAGERS)
        {
            for(JsonCardsManager m : JsonCardsManager.LOADED_MANAGERS)
            {
                m.safe(() ->
                {
                });
            }
        }
    }
    
    public static void log(String s)
    {
        YDM.LOGGER.info("[" + YDM.MOD_ID + "] " + s);
    }
    
    public static void debug(String s)
    {
        YDM.LOGGER.debug("[" + YDM.MOD_ID + "_debug] " + s);
    }
    
    public static void debug(Object s)
    {
        if(s == null)
        {
            YDM.debug("null");
        }
        else
        {
            YDM.debug(s.toString());
        }
    }
}
