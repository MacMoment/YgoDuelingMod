package de.cas_ual_ty.ydm.clientutil;

import de.cas_ual_ty.ydm.YDM;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.metadata.pack.PackFormat;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.util.InclusiveRange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.Set;

/**
 * Custom resource pack that serves card/set/rarity images from disk.
 *
 * <p>Every public method is wrapped in a {@code try-catch(Throwable)} so that
 * an error in this pack can <b>never</b> cascade into the NeoForge lifecycle
 * and prevent other reload listeners (in particular {@code AnimationLoader})
 * from being registered, which would cause the well-known
 * {@code NullPointerException} in {@code ModelManager.reload}.</p>
 *
 * <p>Metadata is served via {@link #getMetadataSection} directly (matching
 * NeoForge's own {@code EmptyPackResources} pattern) instead of relying on
 * {@code pack.mcmeta} JSON parsing.</p>
 */
public class YdmCardResourcePack extends AbstractPackResources
{
    private static final Logger LOGGER = LogManager.getLogger();
    
    public static final String PATH_PREFIX = "textures/item/";
    
    /**
     * May be {@code null} if metadata construction failed during the
     * constructor.  In that case {@link #getMetadataSection} returns
     * {@code null} for every query, which causes
     * {@code Pack.readMetaAndCreate} to skip this pack entirely — a safe
     * no-op.
     */
    @Nullable
    private final PackMetadataSection packMeta;
    
    public YdmCardResourcePack()
    {
        super(createLocationInfo());
        this.packMeta = createPackMeta();
    }
    
    // ---- factory helpers (each catches its own errors) --------------------
    
    private static PackLocationInfo createLocationInfo()
    {
        try
        {
            return new PackLocationInfo(
                    YDM.MOD_ID,
                    Component.literal("YDM Images"),
                    PackSource.DEFAULT,
                    Optional.empty()
            );
        }
        catch(Throwable t)
        {
            LOGGER.error("[{}] Failed to create PackLocationInfo – using fallback", YDM.MOD_ID, t);
            return new PackLocationInfo(
                    "ydm_fallback",
                    Component.literal("YDM Images (fallback)"),
                    PackSource.DEFAULT,
                    Optional.empty()
            );
        }
    }
    
    @Nullable
    private static PackMetadataSection createPackMeta()
    {
        try
        {
            PackFormat packVersion = SharedConstants.getCurrentVersion()
                    .packVersion(PackType.CLIENT_RESOURCES);
            return new PackMetadataSection(
                    Component.literal("YDM Images"),
                    new InclusiveRange<>(packVersion)
            );
        }
        catch(Throwable t)
        {
            // If metadata creation fails the pack will simply be skipped by
            // Pack.readMetaAndCreate (returns null for null metadata).
            LOGGER.error("[{}] Failed to create pack metadata – pack will be inactive", YDM.MOD_ID, t);
            return null;
        }
    }
    
    // ---- AbstractPackResources overrides ----------------------------------
    
    @Nullable
    @Override
    public <T> T getMetadataSection(MetadataSectionType<T> type)
    {
        try
        {
            if(this.packMeta == null)
            {
                return null;
            }
            if(PackMetadataSection.CLIENT_TYPE.equals(type)
                    || PackMetadataSection.SERVER_TYPE.equals(type))
            {
                @SuppressWarnings("unchecked")
                T result = (T) this.packMeta;
                return result;
            }
        }
        catch(Throwable t)
        {
            LOGGER.error("[{}] Error in getMetadataSection", YDM.MOD_ID, t);
        }
        return null;
    }
    
    @Nullable
    @Override
    public IoSupplier<InputStream> getRootResource(String... path)
    {
        // Metadata is served via getMetadataSection(); no root resources needed
        return null;
    }
    
    @Nullable
    @Override
    public IoSupplier<InputStream> getResource(PackType type, Identifier id)
    {
        try
        {
            if(YDM.disabled)
            {
                return null;
            }
            
            if(type != PackType.CLIENT_RESOURCES || !id.getNamespace().equals(YDM.MOD_ID))
            {
                return null;
            }
            
            String resourcePath = id.getPath();
            
            if(!resourcePath.startsWith(PATH_PREFIX) || !resourcePath.endsWith(".png"))
            {
                return null;
            }
            
            String filename = resourcePath.substring(PATH_PREFIX.length());
            File image = getImageFile(filename);
            
            if(image != null)
            {
                return () ->
                {
                    try
                    {
                        return new FileInputStream(image);
                    }
                    catch(Throwable e)
                    {
                        LOGGER.debug("[{}] Failed to open image file {} – {}", YDM.MOD_ID, image, e.getMessage());
                        return new ByteArrayInputStream(new byte[0]);
                    }
                };
            }
        }
        catch(Throwable t)
        {
            LOGGER.error("[{}] Error in getResource for {}", YDM.MOD_ID, id, t);
        }
        
        return null;
    }
    
    @Override
    public void listResources(PackType type, String namespace, String path, ResourceOutput output)
    {
        // This is only needed for fonts and sounds, not for textures referenced by models
    }
    
    @Override
    public Set<String> getNamespaces(PackType type)
    {
        try
        {
            return type == PackType.CLIENT_RESOURCES ? Set.of(YDM.MOD_ID) : Set.of();
        }
        catch(Throwable t)
        {
            LOGGER.error("[{}] Error in getNamespaces", YDM.MOD_ID, t);
            return Set.of();
        }
    }
    
    @Override
    public void close()
    {
    }
    
    // ---- internal helpers -------------------------------------------------
    
    @Nullable
    private File getImageFile(String filename)
    {
        try
        {
            File image = ImageHandler.getCardFile(filename);
            if(image != null && image.exists())
            {
                return image;
            }
            
            image = ImageHandler.getSetFile(filename);
            if(image != null && image.exists())
            {
                return image;
            }
            
            image = ImageHandler.getRarityFile(filename);
            if(image != null && image.exists())
            {
                return image;
            }
        }
        catch(Throwable t)
        {
            // Guard against any error from ImageHandler (e.g., uninitialized
            // folders, SecurityException, or class-loading issues)
            LOGGER.debug("[{}] Error resolving image file: {} - {}", YDM.MOD_ID, filename, t.getMessage());
        }
        
        return null;
    }
}
