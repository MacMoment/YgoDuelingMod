package de.cas_ual_ty.ydm.datagen;

import de.cas_ual_ty.ydm.YDM;
import de.cas_ual_ty.ydm.card.CardSleevesType;
import de.cas_ual_ty.ydm.clientutil.ImageHandler;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.io.IOException;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class YDMDataGen
{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        try
        {
            ImageHandler.createCustomSleevesImages(CardSleevesType.DUELING_MC, "png");
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        PackOutput packOutput = event.getGenerator().getPackOutput();
        event.getGenerator().addProvider(event.includeClient(), new YDMItemModels(packOutput, YDM.MOD_ID, event.getExistingFileHelper()));
    }
}