package de.cas_ual_ty.ydm.clientutil;

import com.google.common.collect.ImmutableList;

import org.joml.Matrix3x2fStack;
import de.cas_ual_ty.ydm.*;
import de.cas_ual_ty.ydm.card.CardHolder;
import de.cas_ual_ty.ydm.card.CardSleevesItem;
import de.cas_ual_ty.ydm.card.CardSleevesType;
import de.cas_ual_ty.ydm.card.InspectCardScreen;
import de.cas_ual_ty.ydm.card.properties.Properties;
import de.cas_ual_ty.ydm.cardbinder.CardBinderScreen;
import de.cas_ual_ty.ydm.carditeminventory.CIIContainer;
import de.cas_ual_ty.ydm.carditeminventory.CIIScreen;
import de.cas_ual_ty.ydm.cardsupply.CardSupplyScreen;
import de.cas_ual_ty.ydm.deckbox.DeckBoxScreen;
import de.cas_ual_ty.ydm.duel.DuelContainer;
import de.cas_ual_ty.ydm.duel.block.DuelBlockContainer;
import de.cas_ual_ty.ydm.duel.dueldisk.DuelEntityContainer;
import de.cas_ual_ty.ydm.duel.screen.DuelContainerScreen;
import de.cas_ual_ty.ydm.duel.screen.DuelScreenBase;
import de.cas_ual_ty.ydm.rarity.RarityLayer;
import de.cas_ual_ty.ydm.set.CardSet;
import de.cas_ual_ty.ydm.util.ISidedProxy;
import de.cas_ual_ty.ydm.util.YdmIOUtil;
import de.cas_ual_ty.ydm.util.YdmUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ClientProxy implements ISidedProxy
{
    public static ModConfigSpec clientConfigSpec;
    public static ClientConfig clientConfig;
    
    public static int activeCardInfoImageSize;
    public static volatile int activeCardItemImageSize;
    public static int activeCardMainImageSize;
    public static int activeSetInfoImageSize;
    public static volatile int activeSetItemImageSize;
    public static boolean keepCachedImages;
    public static boolean itemsUseCardImages;
    public static boolean itemsUseSetImages;
    public static boolean showBinderId;
    public static int maxInfoImages;
    public static int maxMainImages;
    public static double duelChatSize;
    public static int moveAnimationLength;
    public static int specialAnimationLength;
    public static int attackAnimationLength;
    public static int announcementAnimationLength;
    
    public static volatile boolean itemsUseCardImagesActive;
    public static volatile boolean itemsUseCardImagesFailed;
    public static volatile boolean itemsUseSetImagesActive;
    public static volatile boolean itemsUseSetImagesFailed;
    
    public static File imagesParentFolder;
    public static File cardImagesFolder;
    public static File setImagesFolder;
    public static File rarityImagesFolder;
    public static File rarityMainImagesFolder;
    public static File rarityInfoImagesFolder;
    public static File rawCardImagesFolder;
    public static File rawSetImagesFolder;
    public static File rawRarityImagesFolder;
    private static File cardInfoImagesFolder;
    private static File cardItemImagesFolder;
    private static File cardMainImagesFolder;
    private static File setInfoImagesFolder;
    private static File setItemImagesFolder;
    
    @Override
    public void registerModEventListeners(IEventBus bus)
    {
        bus.addListener(this::entityRenderers);
        bus.addListener(this::modelBake);
        bus.addListener(this::modConfig);
        bus.addListener(this::registerMenuScreens);
        bus.addListener(this::addPackFinders);
    }
    
    @Override
    public void registerForgeEventListeners(IEventBus bus)
    {
        bus.addListener(this::guiScreenDrawScreenPost);
        bus.addListener(this::renderGameOverlayPost);
        bus.addListener(this::clientChatReceived);
    }
    
    @Override
    public void preInit()
    {
        ClientProxy.itemsUseCardImagesActive = false;
        ClientProxy.itemsUseCardImagesFailed = false;
        ClientProxy.itemsUseSetImagesActive = false;
        ClientProxy.itemsUseSetImagesFailed = false;
        
        Pair<ClientConfig, ModConfigSpec> client = new ModConfigSpec.Builder().configure(ClientConfig::new);
        ClientProxy.clientConfig = client.getLeft();
        ClientProxy.clientConfigSpec = client.getRight();
        ModList.get().getModContainerById(YDM.MOD_ID).ifPresent(c -> c.registerConfig(ModConfig.Type.CLIENT, ClientProxy.clientConfigSpec));
    }
    
    @Override
    public void init()
    {
        YDM.log("Sizes from client config (info/item/main): " + ClientProxy.activeCardInfoImageSize + " / " + ClientProxy.activeCardItemImageSize + " (" + ClientProxy.itemsUseCardImages + ") / " + ClientProxy.activeCardMainImageSize);
        
        if(ClientProxy.itemsUseCardImages)
        {
            try
            {
                List<CardHolder> list = ImageHandler.getMissingItemImages();
                
                if(list.size() == 0)
                {
                    YDM.log("Items will use card images!");
                    ClientProxy.itemsUseCardImagesActive = true;
                }
                else
                {
                    YDM.log("Items will not use card images, still missing " + list.size() + " images. Fetching...");
                    ImageHandler.downloadCardImages(list);
                    ClientProxy.itemsUseCardImagesFailed = true;
                }
            }
            catch(Exception e)
            {
                YDM.log("Failed checking missing item images!");
                e.printStackTrace();
                ClientProxy.itemsUseCardImagesFailed = true;
            }
        }
        
        if(ClientProxy.itemsUseSetImages)
        {
            try
            {
                List<CardSet> list = ImageHandler.getMissingSetImages();
                
                if(list.size() == 0)
                {
                    YDM.log("Items will use set images!");
                    ClientProxy.itemsUseSetImagesActive = true;
                }
                else
                {
                    YDM.log("Items will not use set images, still missing " + list.size() + " images. Fetching...");
                    ImageHandler.downloadSetImages(list);
                    ClientProxy.itemsUseSetImagesFailed = true;
                }
            }
            catch(Exception e)
            {
                YDM.log("Failed checking missing set images!");
                e.printStackTrace();
                ClientProxy.itemsUseSetImagesFailed = true;
            }
        }
        
        // Screen registration moved to registerMenuScreens(RegisterMenuScreensEvent)
        
        ImageHandler.prepareRarityImages(ClientProxy.activeCardMainImageSize);
        ImageHandler.prepareRarityImages(ClientProxy.activeCardInfoImageSize);
        CardRenderUtil.init(ClientProxy.maxInfoImages, ClientProxy.maxMainImages);
    }
    
    @Override
    public void initFolders()
    {
        ClientProxy.imagesParentFolder = new File("ydm_db_images");
        ClientProxy.cardImagesFolder = new File(ClientProxy.imagesParentFolder, "cards");
        ClientProxy.setImagesFolder = new File(ClientProxy.imagesParentFolder, "sets");
        ClientProxy.rarityImagesFolder = new File(ClientProxy.imagesParentFolder, "rarities");
        ClientProxy.rawCardImagesFolder = new File(ClientProxy.cardImagesFolder, "raw");
        ClientProxy.rawSetImagesFolder = new File(ClientProxy.setImagesFolder, "raw");
        ClientProxy.rawRarityImagesFolder = new File(YDM.mainFolder, "rarity_images");
        
        YdmIOUtil.createDirIfNonExistant(ClientProxy.imagesParentFolder);
        YdmIOUtil.createDirIfNonExistant(ClientProxy.cardImagesFolder);
        YdmIOUtil.createDirIfNonExistant(ClientProxy.setImagesFolder);
        YdmIOUtil.createDirIfNonExistant(ClientProxy.rarityImagesFolder);
        YdmIOUtil.createDirIfNonExistant(ClientProxy.rawCardImagesFolder);
        YdmIOUtil.createDirIfNonExistant(ClientProxy.rawSetImagesFolder);
        YdmIOUtil.createDirIfNonExistant(ClientProxy.rawRarityImagesFolder);
    }
    
    @Override
    public void initFiles() // done before #init
    {
        // change this depending on resolution (64/128/256) and anime (yes/no) settings
        ClientProxy.cardInfoImagesFolder = new File(ClientProxy.cardImagesFolder, "" + ClientProxy.activeCardInfoImageSize);
        ClientProxy.cardItemImagesFolder = new File(ClientProxy.cardImagesFolder, "" + ClientProxy.activeCardItemImageSize);
        ClientProxy.cardMainImagesFolder = new File(ClientProxy.cardImagesFolder, "" + ClientProxy.activeCardMainImageSize);
        
        ClientProxy.rarityMainImagesFolder = new File(rarityImagesFolder, "" + ClientProxy.activeCardMainImageSize);
        ClientProxy.rarityInfoImagesFolder = new File(rarityImagesFolder, "" + ClientProxy.activeCardInfoImageSize);
        
        YdmIOUtil.createDirIfNonExistant(ClientProxy.cardInfoImagesFolder);
        YdmIOUtil.createDirIfNonExistant(ClientProxy.cardItemImagesFolder);
        YdmIOUtil.createDirIfNonExistant(ClientProxy.cardMainImagesFolder);
        
        YdmIOUtil.createDirIfNonExistant(ClientProxy.rarityMainImagesFolder);
        YdmIOUtil.createDirIfNonExistant(ClientProxy.rarityInfoImagesFolder);
        
        // change this depending on resolution (64/128/256)
        ClientProxy.setInfoImagesFolder = new File(ClientProxy.setImagesFolder, "" + ClientProxy.activeSetInfoImageSize);
        ClientProxy.setItemImagesFolder = new File(ClientProxy.setImagesFolder, "" + ClientProxy.activeSetItemImageSize);
        
        YdmIOUtil.createDirIfNonExistant(ClientProxy.setInfoImagesFolder);
        YdmIOUtil.createDirIfNonExistant(ClientProxy.setItemImagesFolder);
    }
    
    @Override
    public Player getClientPlayer()
    {
        return ClientProxy.getPlayer();
    }
    
    @Override
    public String addCardInfoTag(String imageName)
    {
        return ClientProxy.activeCardInfoImageSize + "/" + imageName;
    }
    
    @Override
    public String addCardItemTag(String imageName)
    {
        return ClientProxy.activeCardItemImageSize + "/" + imageName;
    }
    
    @Override
    public String addCardMainTag(String imageName)
    {
        return ClientProxy.activeCardMainImageSize + "/" + imageName;
    }
    
    @Override
    public String addSetInfoTag(String imageName)
    {
        return ClientProxy.activeSetInfoImageSize + "/" + imageName;
    }
    
    @Override
    public String addSetItemTag(String imageName)
    {
        return ClientProxy.activeSetItemImageSize + "/" + imageName;
    }
    
    @Override
    public String getCardInfoReplacementImage(Properties properties, byte imageIndex)
    {
        return ImageHandler.getInfoReplacementImage(properties, imageIndex);
    }
    
    @Override
    public String getCardMainReplacementImage(Properties properties, byte imageIndex)
    {
        return ImageHandler.getMainReplacementImage(properties, imageIndex);
    }
    
    @Override
    public String getSetInfoReplacementImage(CardSet set)
    {
        return ImageHandler.getInfoReplacementImage(set);
    }
    
    @Override
    public String getRarityMainImage(RarityLayer layer)
    {
        return ImageHandler.getRarityMainImage(layer);
    }
    
    @Override
    public String getRarityInfoImage(RarityLayer layer)
    {
        return ImageHandler.getRarityInfoImage(layer);
    }
    
    @Override
    public void openCardInspectScreen(CardHolder card)
    {
        Minecraft.getInstance().setScreen(new InspectCardScreen(card));
    }
    
    private void entityRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
        try
        {
            event.registerEntityRenderer(YdmEntityTypes.DUEL.get(), DuelEntityRenderer::new);
        }
        catch(Throwable e)
        {
            YDM.log("Failed to register entity renderers: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("unchecked")
    private void registerMenuScreens(RegisterMenuScreensEvent event)
    {
        try
        {
            event.register(YdmContainerTypes.CARD_BINDER.get(), CardBinderScreen::new);
            event.register(YdmContainerTypes.DECK_BOX.get(), DeckBoxScreen::new);
            event.register(YdmContainerTypes.DUEL_BLOCK_CONTAINER.get(), DuelScreenBase::new);
            event.register(YdmContainerTypes.DUEL_ENTITY_CONTAINER.get(), DuelScreenBase::new);
            event.register(YdmContainerTypes.CARD_SUPPLY.get(), CardSupplyScreen::new);
            event.register(YdmContainerTypes.CARD_SET.get(), CIIScreen::new);
            event.register(YdmContainerTypes.CARD_SET_CONTENTS.get(), CIIScreen::new);
            event.register(YdmContainerTypes.SIMPLE_BINDER.get(), CIIScreen::new);
        }
        catch(Throwable e)
        {
            YDM.log("Failed to register menu screens: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void addPackFinders(net.neoforged.neoforge.event.AddPackFindersEvent event)
    {
        try
        {
            if(event.getPackType() == net.minecraft.server.packs.PackType.CLIENT_RESOURCES)
            {
                event.addRepositorySource(new YdmResourcePackFinder());
            }
        }
        catch(Throwable e)
        {
            YDM.log("Failed to add pack finders: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // TextureStitchEvent removed in NeoForge 1.21 - texture stitching is no longer needed
    
    // TODO: 1.21.11 - ModelEvent.RegisterAdditional removed; item models are now data-driven via JSON
    // private void modelRegistry(ModelEvent.RegisterAdditional event)
    // {
    //     boolean flag = false;
    //     
    //     while((ClientProxy.activeCardItemImageSize == 0 || ClientProxy.activeSetItemImageSize == 0))
    //     {
    //         if(!flag)
    //         {
    //             flag = true;
    //             YDM.log("Sleeping for a couple seconds to give the worker enough time to read the config...");
    //         }
    //         
    //         try
    //         {
    //             TimeUnit.SECONDS.sleep(1);
    //         }
    //         catch(InterruptedException e)
    //         {
    //             YDM.log("Tried sleeping to give textures enough time... It didnt work :(");
    //             e.printStackTrace();
    //             break;
    //         }
    //     }
    //     
    //     YDM.log("Registering models (size: " + ClientProxy.activeCardItemImageSize + ") for " + YdmItems.BLANC_CARD.getId().toString() + " and " + YdmItems.CARD_BACK.getId().toString());
    //     
    //     if(ClientProxy.activeCardItemImageSize != 16)
    //     {
    //         event.register(ModelResourceLocation.inventory(Identifier.fromNamespaceAndPath(YdmItems.BLANC_CARD.getId().toString() + "_" + ClientProxy.activeCardItemImageSize)));
    //         event.register(ModelResourceLocation.inventory(Identifier.fromNamespaceAndPath(YdmItems.CARD_BACK.getId().toString() + "_" + ClientProxy.activeCardItemImageSize)));
    //         
    //         for(CardSleevesType sleeves : CardSleevesType.VALUES)
    //         {
    //             if(!sleeves.isCardBack())
    //             {
    //                 event.register(ModelResourceLocation.inventory(sleeves.getItemModelRL(ClientProxy.activeCardItemImageSize)));
    //             }
    //         }
    //     }
    //     
    //     YDM.log("Registering models (size: " + ClientProxy.activeSetItemImageSize + ") for " + YdmItems.BLANC_SET.getId().toString());
    //     
    //     if(ClientProxy.activeSetItemImageSize != 16)
    //     {
    //         event.register(ModelResourceLocation.inventory(Identifier.fromNamespaceAndPath(YdmItems.BLANC_SET.getId().toString() + "_" + ClientProxy.activeSetItemImageSize)));
    //     }
    // }
    
    private void modelBake(ModelEvent.ModifyBakingResult event)
    {
        try
        {
            modelBakeInner(event);
        }
        catch(Throwable e)
        {
            YDM.log("Failed to modify baking result: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void modelBakeInner(ModelEvent.ModifyBakingResult event)
    {
        YDM.log("Baking models (size: " + ClientProxy.activeCardItemImageSize + ") for " + YdmItems.BLANC_CARD.getId().toString() + " and " + YdmItems.CARD_BACK.getId().toString());
        
        var itemModels = event.getBakingResult().itemStackModels();
        
        // 16 is default texture; no need to do anything special in that case
        if(ClientProxy.activeCardItemImageSize != 16)
        {
            var blancCardModel = itemModels.get(Identifier.parse(YdmItems.BLANC_CARD.getId().toString() + "_" + ClientProxy.activeCardItemImageSize));
            if(blancCardModel != null)
            {
                itemModels.put(YdmItems.BLANC_CARD.getId(), blancCardModel);
            }
            
            var cardBackModel = itemModels.get(Identifier.parse(YdmItems.CARD_BACK.getId().toString() + "_" + ClientProxy.activeCardItemImageSize));
            if(cardBackModel != null)
            {
                itemModels.put(YdmItems.CARD_BACK.getId(), cardBackModel);
            }
            
            for(CardSleevesType sleeves : CardSleevesType.VALUES)
            {
                if(!sleeves.isCardBack())
                {
                    var sleevesModel = itemModels.get(sleeves.getItemModelRL(ClientProxy.activeCardItemImageSize));
                    if(sleevesModel != null)
                    {
                        itemModels.put(Identifier.parse(YDM.MOD_ID + ":sleeves_" + sleeves.name), sleevesModel);
                    }
                }
            }
        }
        
        if(ClientProxy.activeSetItemImageSize != 16)
        {
            var blancSetModel = itemModels.get(Identifier.parse(YdmItems.BLANC_SET.getId().toString() + "_" + ClientProxy.activeSetItemImageSize));
            if(blancSetModel != null)
            {
                itemModels.put(YdmItems.BLANC_SET.getId(), blancSetModel);
            }
        }
        
        Identifier key = YdmItems.CARD.getId();
        var cardModel = itemModels.get(key);
        if(cardModel != null)
        {
            itemModels.put(key, new CardBakedModel(cardModel));
        }
        
        key = YdmItems.SET.getId();
        var setModel = itemModels.get(key);
        if(setModel != null)
        {
            itemModels.put(key, new CardSetBakedModel(setModel));
        }
        key = YdmItems.OPENED_SET.getId();
        var openedSetModel = itemModels.get(key);
        if(openedSetModel != null)
        {
            itemModels.put(key, new CardSetBakedModel(openedSetModel));
        }
    }
    
    private void modConfig(ModConfigEvent event)
    {
        if(event.getConfig().getSpec() == ClientProxy.clientConfigSpec)
        {
            YDM.log("Baking client config!");
            ClientProxy.activeCardInfoImageSize = YdmUtil.toPow2ConfigValue(ClientProxy.clientConfig.activeCardInfoImageSize.get(), 4);
            ClientProxy.activeCardItemImageSize = YdmUtil.toPow2ConfigValue(ClientProxy.clientConfig.activeCardItemImageSize.get(), 4);
            ClientProxy.activeCardMainImageSize = YdmUtil.toPow2ConfigValue(ClientProxy.clientConfig.activeCardMainImageSize.get(), 4);
            ClientProxy.activeSetInfoImageSize = YdmUtil.toPow2ConfigValue(ClientProxy.clientConfig.activeSetInfoImageSize.get(), 4);
            ClientProxy.activeSetItemImageSize = YdmUtil.toPow2ConfigValue(ClientProxy.clientConfig.activeSetItemImageSize.get(), 4);
            ClientProxy.keepCachedImages = ClientProxy.clientConfig.keepCachedImages.get();
            ClientProxy.itemsUseCardImages = ClientProxy.clientConfig.itemsUseCardImages.get();
            ClientProxy.itemsUseSetImages = ClientProxy.clientConfig.itemsUseSetImages.get();
            ClientProxy.showBinderId = ClientProxy.clientConfig.showBinderId.get();
            ClientProxy.maxInfoImages = ClientProxy.clientConfig.maxInfoImages.get();
            ClientProxy.maxMainImages = ClientProxy.clientConfig.maxMainImages.get();
            ClientProxy.duelChatSize = ClientProxy.clientConfig.duelChatSize.get();
            ClientProxy.moveAnimationLength = ClientProxy.clientConfig.moveAnimationLength.get();
            ClientProxy.attackAnimationLength = ClientProxy.clientConfig.attackAnimationLength.get();
            ClientProxy.specialAnimationLength = ClientProxy.clientConfig.specialAnimationLength.get();
            ClientProxy.announcementAnimationLength = ClientProxy.clientConfig.announcementAnimationLength.get();
        }
    }
    
    private void guiScreenDrawScreenPost(ScreenEvent.Render.Post event)
    {
        if(event.getScreen() instanceof AbstractContainerScreen)
        {
            AbstractContainerScreen<?> containerScreen = (AbstractContainerScreen<?>) event.getScreen();
            
            if(containerScreen.getSlotUnderMouse() != null && !containerScreen.getSlotUnderMouse().getItem().isEmpty())
            {
                ItemStack itemStack = containerScreen.getSlotUnderMouse().getItem();
                GuiGraphics guiGraphics = event.getGuiGraphics();
                var ms = guiGraphics.pose();
                
                if(itemStack.getItem() == YdmItems.CARD.get())
                {
                    CardRenderUtil.renderCardInfo(guiGraphics, YdmItems.CARD.get().getCardHolder(itemStack), containerScreen);
                }
                else if(itemStack.getItem() == YdmItems.SET.get())
                {
                    renderSetInfo(guiGraphics, YdmItems.SET.get().getCardSet(itemStack), containerScreen.getGuiLeft());
                }
                else if(itemStack.getItem() == YdmItems.OPENED_SET.get())
                {
                    renderSetInfo(guiGraphics, YdmItems.OPENED_SET.get().getCardSet(itemStack), containerScreen.getGuiLeft());
                }
                else if(itemStack.getItem() instanceof CardSleevesItem)
                {
                    renderSleevesInfo(guiGraphics, ((CardSleevesItem) itemStack.getItem()).sleeves, containerScreen.getGuiLeft());
                }
            }
        }
    }
    
    private void renderGameOverlayPost(RenderGuiLayerEvent.Post event)
    {
        if(!VanillaGuiLayers.HOTBAR.equals(event.getName()))
        {
            return;
        }
        
        if(getClientPlayer() != null && ClientProxy.getMinecraft().screen == null)
        {
            Player player = getClientPlayer();
            GuiGraphics guiGraphics = event.getGuiGraphics();
            var ms = guiGraphics.pose();
            
            if(player.getMainHandItem().getItem() == YdmItems.CARD.get())
            {
                CardRenderUtil.renderCardInfo(guiGraphics, YdmItems.CARD.get().getCardHolder(player.getMainHandItem()));
            }
            else if(player.getMainHandItem().getItem() == YdmItems.SET.get())
            {
                renderSetInfo(guiGraphics, YdmItems.SET.get().getCardSet(player.getMainHandItem()));
            }
            else if(player.getMainHandItem().getItem() == YdmItems.OPENED_SET.get())
            {
                renderSetInfo(guiGraphics, YdmItems.OPENED_SET.get().getCardSet(player.getMainHandItem()));
            }
            else if(player.getMainHandItem().getItem() instanceof CardSleevesItem)
            {
                renderSleevesInfo(guiGraphics, ((CardSleevesItem) player.getMainHandItem().getItem()).sleeves);
            }
            else if(player.getOffhandItem().getItem() == YdmItems.CARD.get())
            {
                CardRenderUtil.renderCardInfo(guiGraphics, YdmItems.CARD.get().getCardHolder(player.getOffhandItem()));
            }
            else if(player.getOffhandItem().getItem() == YdmItems.SET.get())
            {
                renderSetInfo(guiGraphics, YdmItems.SET.get().getCardSet(player.getOffhandItem()));
            }
            else if(player.getOffhandItem().getItem() == YdmItems.OPENED_SET.get())
            {
                renderSetInfo(guiGraphics, YdmItems.OPENED_SET.get().getCardSet(player.getOffhandItem()));
            }
            else if(player.getOffhandItem().getItem() instanceof CardSleevesItem)
            {
                renderSleevesInfo(guiGraphics, ((CardSleevesItem) player.getOffhandItem().getItem()).sleeves);
            }
        }
    }
    
    private void renderSetInfo(GuiGraphics guiGraphics, CardSet set)
    {
        renderSetInfo(guiGraphics, set, 150);
    }
    
    private void renderSetInfo(GuiGraphics guiGraphics, CardSet set, int width)
    {
        if(set == null)
        {
            return;
        }
        
        var ms = guiGraphics.pose();
        final float f = 0.5f;
        final int imageSize = 64;
        int margin = 2;
        
        int maxWidth = width - margin * 2;
        
        ms.pushMatrix();
        ScreenUtil.white();
        
        int x = margin;
        
        if(maxWidth < imageSize)
        {
            // draw it centered if the space we got is limited
            // to make sure the image is NOT rendered more to the right of the center
            x = (maxWidth - imageSize) / 2 + margin;
        }
        
        // card texture
        
        YdmBlitUtil.fullBlit(ms, set.getInfoImageResourceLocation(), x, margin, imageSize, imageSize);
        
        // need to multiply x2 because we are scaling the text to x0.5
        maxWidth *= 2;
        margin *= 2;
        ms.scale(f, f);
        
        // card description text
        
        Font fontRenderer = ClientProxy.getMinecraft().font;
        
        List<Component> list = new LinkedList<>();
        set.addInformation(list);
        
        ScreenUtil.drawSplitString(guiGraphics, fontRenderer, list, margin, imageSize * 2 + margin * 2, maxWidth, 0xFFFFFF);
        
        ms.popMatrix();
    }
    
    private void renderSleevesInfo(GuiGraphics guiGraphics, CardSleevesType sleeves)
    {
        renderSleevesInfo(guiGraphics, sleeves, 150);
    }
    
    private void renderSleevesInfo(GuiGraphics guiGraphics, CardSleevesType sleeves, int width)
    {
        if(sleeves == null)
        {
            return;
        }
        
        var ms = guiGraphics.pose();
        final float f = 0.5f;
        final int imageSize = 64;
        int margin = 2;
        
        int maxWidth = width - margin * 2;
        
        ms.pushMatrix();
        ScreenUtil.white();
        
        int x = margin;
        
        if(maxWidth < imageSize)
        {
            // draw it centered if the space we got is limited
            // to make sure the image is NOT rendered more to the right of the center
            x = (maxWidth - imageSize) / 2 + margin;
        }
        
        // card texture
        
        YdmBlitUtil.fullBlit(ms, sleeves.getMainRL(ClientProxy.activeCardInfoImageSize), x, margin, imageSize, imageSize);
        
        // need to multiply x2 because we are scaling the text to x0.5
        maxWidth *= 2;
        margin *= 2;
        ms.scale(f, f);
        
        // card description text
        
        @SuppressWarnings("resource")
        Font fontRenderer = ClientProxy.getMinecraft().font;
        
        ScreenUtil.drawSplitString(guiGraphics, fontRenderer, ImmutableList.of(Component.translatable("item.ydm." + sleeves.getResourceName())), margin, imageSize * 2 + margin * 2, maxWidth, 0xFFFFFF);
        
        ms.popMatrix();
    }
    
    public static int maxMessages = 50; //TODO make configurable
    public static List<Component> chatMessages = new ArrayList<>(50);
    
    private void clientChatReceived(ClientChatReceivedEvent event)
    {
        if(!event.isCanceled() && event.getMessage() != null && !event.getMessage().getString().isEmpty())
        {
            if(ClientProxy.chatMessages.size() >= ClientProxy.maxMessages)
            {
                ClientProxy.chatMessages.remove(0);
            }
            
            ClientProxy.chatMessages.add(event.getMessage());
        }
    }
    
    public static Minecraft getMinecraft()
    {
        return Minecraft.getInstance();
    }
    
    @SuppressWarnings("resource")
    public static Player getPlayer()
    {
        return ClientProxy.getMinecraft().player;
    }
}
