package de.cas_ual_ty.ydm.clientutil;

import de.cas_ual_ty.ydm.YDM;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Discovers and registers the YDM card image resource pack.
 *
 * <p>Every failure path is caught at the {@code Throwable} level to
 * guarantee that a broken image pack can never prevent NeoForge's
 * lifecycle events (especially {@code AddClientReloadListenersEvent})
 * from being dispatched to other mods.</p>
 */
public class YdmResourcePackFinder implements RepositorySource
{
    private static final Logger LOGGER = LogManager.getLogger();
    
    public YdmResourcePackFinder()
    {
    }
    
    @Override
    public void loadPacks(Consumer<Pack> infoConsumer)
    {
        if(YDM.disabled)
        {
            return;
        }
        
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
            else
            {
                LOGGER.warn("[{}] YDM card resource pack was not created (metadata invalid or missing) – card images will be unavailable", YDM.MOD_ID);
            }
        }
        catch(Throwable e)
        {
            // Catch Throwable (not just Exception) to prevent NoClassDefFoundError,
            // LinkageError, NoSuchFieldError, or any other error from cascading into
            // NeoForge's event pipeline and disrupting reload listener registration.
            LOGGER.error("[{}] Failed to load YDM card resource pack – card images will be unavailable", YDM.MOD_ID, e);
        }
    }
}