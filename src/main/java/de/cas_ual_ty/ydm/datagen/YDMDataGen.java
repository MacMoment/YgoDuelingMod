package de.cas_ual_ty.ydm.datagen;

import de.cas_ual_ty.ydm.YDM;
import de.cas_ual_ty.ydm.card.CardSleevesType;
import de.cas_ual_ty.ydm.clientutil.ImageHandler;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.io.IOException;

@EventBusSubscriber(modid = YDM.MOD_ID)
public class YDMDataGen
{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event)
    {
        YDM.safeModEvent(() ->
        {
            try
            {
                ImageHandler.createCustomSleevesImages(CardSleevesType.DUELING_MC, "png");
            }
            catch(IOException e)
            {
                YDM.log("Failed to create custom sleeves images: " + e.getMessage());
            }
            
            PackOutput packOutput = event.getGenerator().getPackOutput();
            event.getGenerator().addProvider(true, new YDMItemModels(packOutput, YDM.MOD_ID));
        }, "gatherData");
    }
}