package de.cas_ual_ty.ydm.simplebinder;

import de.cas_ual_ty.ydm.YDM;
import de.cas_ual_ty.ydm.YdmContainerTypes;
import de.cas_ual_ty.ydm.carditeminventory.HeldCIIContainer;
import de.cas_ual_ty.ydm.util.YDMItemHandler;
import de.cas_ual_ty.ydm.util.YdmUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.item.component.CustomData;
import java.util.List;

public class SimpleBinderItem extends Item
{
    public final int binderSize;
    
    public SimpleBinderItem(Properties properties, int binderSize)
    {
        super(properties);
        this.binderSize = binderSize;
    }
    
    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn)
    {
        super.appendHoverText(itemStack, context, tooltip, flagIn);
        //        tooltip.add(new Component(this.getTranslationKey() + ".desc").modifyStyle((s) -> s.applyFormatting(ChatFormatting.RED)));
    }
    
    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand)
    {
        if(!world.isClientSide() && hand == YdmUtil.getActiveItem(player, this))
        {
            ItemStack itemStack = player.getItemInHand(hand);
            
            YDMItemHandler handler = getItemHandler(itemStack);
            HeldCIIContainer.openGui(player, hand, binderSize, new MenuProvider()
            {
                @Override
                public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player)
                {
                    ItemStack itemStack = player.getItemInHand(hand);
                    return new SimpleBinderContainer(YdmContainerTypes.SIMPLE_BINDER.get(), id, playerInventory, handler, hand);
                }
                
                @Override
                public Component getDisplayName()
                {
                    return Component.translatable("container." + YDM.MOD_ID + ".simple_binder");
                }
            });
            
            return InteractionResult.SUCCESS;
        }
        
        return super.use(world, player, hand);
    }
    
    public YDMItemHandler getItemHandler(ItemStack itemStack)
    {
        return itemStack.getData(YDM.CARD_ITEM_INVENTORY);
    }
    
    public CompoundTag getNBT(ItemStack itemStack)
    {
        return itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    }
    
    public static SimpleBinderItem makeItem(String modId, int pagesAmt)
    {
        return new SimpleBinderItem(new Properties().stacksTo(1), 6 * 9 * pagesAmt);
    }
}
