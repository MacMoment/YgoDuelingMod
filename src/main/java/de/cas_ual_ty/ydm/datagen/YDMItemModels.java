package de.cas_ual_ty.ydm.datagen;

import de.cas_ual_ty.ydm.card.CardSleevesType;
import de.cas_ual_ty.ydm.util.YdmUtil;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

/**
 * Item model data generator.
 * TODO: Migrate to new client item JSON generation system for NeoForge 1.21.11
 */
public class YDMItemModels implements DataProvider
{
    private final PackOutput packOutput;
    private final String modid;
    
    public YDMItemModels(PackOutput packOutput, String modid)
    {
        this.packOutput = packOutput;
        this.modid = modid;
    }
    
    @Override
    public CompletableFuture<?> run(CachedOutput cache)
    {
        // TODO: Generate client item JSON files for card sleeves using the new model system
        return CompletableFuture.completedFuture(null);
    }
    
    @Override
    public String getName()
    {
        return "YDM Item Models";
    }
}
