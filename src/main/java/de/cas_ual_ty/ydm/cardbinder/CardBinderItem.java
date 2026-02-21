package de.cas_ual_ty.ydm.cardbinder;

import de.cas_ual_ty.ydm.YDM;
import de.cas_ual_ty.ydm.YdmContainerTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.CustomData;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public class CardBinderItem extends Item implements MenuProvider
{
    private static final HashMap<UUID, CardBinderCardsManager> MANAGER_MAP = new HashMap<>();
    public static final String MANAGER_UUID_KEY_OLD = "binder_uuid";
    public static final String MANAGER_UUID_KEY = "binder_uuid_cap";
    
    public CardBinderItem(Properties properties)
    {
        super(properties);
    }
    
    public CardBinderCardsManager getInventoryManager(ItemStack itemStack)
    {
        CardBinderCardsManager manager;
        UUID uuid = getUUID(itemStack);
        
        if(uuid == null || !CardBinderItem.MANAGER_MAP.containsKey(uuid))
        {
            manager = new CardBinderCardsManager();
            
            if(uuid == null)
            {
                manager.generateUUIDIfNull();
                uuid = manager.getUUID();
                setUUID(itemStack, uuid);
            }
            else
            {
                manager.setUUID(uuid);
            }
            
            CardBinderItem.MANAGER_MAP.put(uuid, manager);
        }
        else
        {
            manager = CardBinderItem.MANAGER_MAP.get(uuid);
        }
        
        return manager;
    }
    
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext ctx, TooltipDisplay display, Consumer<Component> tooltipAdder, TooltipFlag flag)
    {
        super.appendHoverText(stack, ctx, display, tooltipAdder, flag);
        
        tooltipAdder.accept(Component.translatable(getDescriptionId() + ".uuid"));
        
        UUID uuid = getUUID(stack);
        
        if(uuid != null)
        {
            tooltipAdder.accept(Component.literal(uuid.toString()));
        }
        else
        {
            tooltipAdder.accept(Component.translatable(getDescriptionId() + ".uuid.empty"));
        }
    }
    
    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand)
    {
        // must also fix UUID on client side if player is in creative mode
        getUUID(player.getItemInHand(hand));
        
        ItemStack stack = getActiveBinder(player);
        
        if(player.getItemInHand(hand) == stack)
        {
            player.openMenu(this);
            return InteractionResult.SUCCESS;
        }
        
        return super.use(world, player, hand);
    }
    
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player)
    {
        ItemStack s = getActiveBinder(player);
        return new CardBinderContainer(YdmContainerTypes.CARD_BINDER.get(), id, playerInv, getInventoryManager(s), s);
    }
    
    @Override
    public Component getDisplayName()
    {
        return Component.translatable("container." + YDM.MOD_ID + ".card_binder");
    }
    
    public ItemStack getActiveBinder(Player player)
    {
        if(player.getMainHandItem().getItem() == this)
        {
            return player.getMainHandItem();
        }
        else if(player.getOffhandItem().getItem() == this)
        {
            return player.getOffhandItem();
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }
    
    public UUID getUUID(ItemStack itemStack)
    {
        UUID uuid;
        
        String uuidStr = itemStack.getOrDefault(YDM.UUID_DATA.get(), "");
        
        CompoundTag tag = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if(tag.contains(CardBinderItem.MANAGER_UUID_KEY_OLD))
        {
            Tag t = tag.get(CardBinderItem.MANAGER_UUID_KEY_OLD);
            if(t instanceof IntArrayTag)
            {
                uuid = UUIDUtil.uuidFromIntArray(((IntArrayTag) t).getAsIntArray());
            }
            else
            {
                uuid = parseUUID(uuidStr);
            }
            itemStack.set(YDM.UUID_DATA.get(), uuid == null ? "" : uuid.toString());
            tag.remove(MANAGER_UUID_KEY_OLD);
            itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }
        else
        {
            uuid = parseUUID(uuidStr);
        }
        
        return uuid;
    }
    
    private static UUID parseUUID(String uuidStr)
    {
        if(uuidStr.isEmpty())
        {
            return null;
        }
        try
        {
            return UUID.fromString(uuidStr);
        }
        catch(IllegalArgumentException e)
        {
            return null;
        }
    }
    
    public void setUUID(ItemStack itemStack, UUID uuid)
    {
        itemStack.set(YDM.UUID_DATA.get(), uuid == null ? "" : uuid.toString());
    }
    
    public void setUUIDAndUpdateManager(ItemStack itemStack, UUID uuid)
    {
        CardBinderCardsManager manager = getInventoryManager(itemStack);
        itemStack.set(YDM.UUID_DATA.get(), uuid == null ? "" : uuid.toString());
        MANAGER_MAP.remove(manager.getUUID());
        manager.setUUID(uuid);
        MANAGER_MAP.put(uuid, manager);
    }
}
