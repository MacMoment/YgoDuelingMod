package de.cas_ual_ty.ydm.deckbox;

import de.cas_ual_ty.ydm.YdmDatabase;
import de.cas_ual_ty.ydm.YdmItems;
import de.cas_ual_ty.ydm.duel.DeckSource;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;

public class PatreonDeckBoxItem extends DeckBoxItem
{
    public PatreonDeckBoxItem(Properties properties)
    {
        super(properties);
    }
    
    public ItemStack makeItemStackFromDeckSource(DeckSource s)
    {
        ItemStack itemStack = new ItemStack(YdmItems.PATREON_DECK_BOX.get());
        setDeckHolder(itemStack, s.deck);
        itemStack.set(DataComponents.CUSTOM_NAME, s.name);
        return itemStack;
    }
}
