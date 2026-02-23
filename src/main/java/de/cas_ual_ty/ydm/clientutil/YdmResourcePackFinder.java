package de.cas_ual_ty.ydm.clientutil;

import de.cas_ual_ty.ydm.YDM;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;

import java.util.Optional;
import java.util.function.Consumer;

public class YdmResourcePackFinder implements RepositorySource
{
    public YdmResourcePackFinder()
    {
    }
    
    @Override
    public void loadPacks(Consumer<Pack> infoConsumer)
    {
        try
        {
            PackLocationInfo locationInfo = new PackLocationInfo(
                    YDM.MOD_ID,
                    Component.literal(YDM.MOD_ID),
                    PackSource.DEFAULT,
                    Optional.empty()
            );
            PackSelectionConfig selectionConfig = new PackSelectionConfig(true, Pack.Position.TOP, false);
            Pack pack = Pack.readMetaAndCreate(
                    locationInfo,
                    new Pack.ResourcesSupplier()
                    {
                        @Override
                        public net.minecraft.server.packs.PackResources openPrimary(PackLocationInfo info)
                        {
                            return new YdmCardResourcePack();
                        }
                        
                        @Override
                        public net.minecraft.server.packs.PackResources openFull(PackLocationInfo info, Pack.Metadata metadata)
                        {
                            return new YdmCardResourcePack();
                        }
                    },
                    PackType.CLIENT_RESOURCES,
                    selectionConfig
            );
            
            if(pack != null)
            {
                infoConsumer.accept(pack);
            }
        }
        catch(Exception e)
        {
            YDM.log("Failed to load YDM card resource pack: " + e.getMessage());
        }
    }
}