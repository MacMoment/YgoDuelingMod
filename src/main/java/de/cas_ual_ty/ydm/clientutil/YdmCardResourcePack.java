package de.cas_ual_ty.ydm.clientutil;

import de.cas_ual_ty.ydm.YDM;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.util.InclusiveRange;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Set;

/**
 * Custom resource pack that serves card/set/rarity images from disk.
 *
 * Overrides {@link #getMetadataSection} directly to return a proper
 * {@link PackMetadataSection} object, bypassing the fragile JSON parsing
 * path in {@link AbstractPackResources}. This matches the pattern used by
 * NeoForge's own {@code EmptyPackResources} and ensures the pack metadata
 * is always valid for the running Minecraft version, regardless of
 * pack.mcmeta format changes across versions.
 */
public class YdmCardResourcePack extends AbstractPackResources
{
    public static final String PATH_PREFIX = "textures/item/";
    
    private final PackMetadataSection packMeta;
    
    public YdmCardResourcePack()
    {
        super(new PackLocationInfo(YDM.MOD_ID, Component.literal("YDM Images"), PackSource.DEFAULT, Optional.empty()));
        var packVersion = SharedConstants.getCurrentVersion().packVersion(PackType.CLIENT_RESOURCES);
        this.packMeta = new PackMetadataSection(
                Component.literal("YDM Images"),
                new InclusiveRange<>(packVersion, packVersion)
        );
    }
    
    @Nullable
    @Override
    public <T> T getMetadataSection(MetadataSectionType<T> type)
    {
        if(PackMetadataSection.CLIENT_TYPE.equals(type) || PackMetadataSection.SERVER_TYPE.equals(type))
        {
            @SuppressWarnings("unchecked")
            T result = (T) this.packMeta;
            return result;
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
                catch(FileNotFoundException e)
                {
                    return new ByteArrayInputStream(new byte[0]);
                }
            };
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
        return type == PackType.CLIENT_RESOURCES ? Set.of(YDM.MOD_ID) : Set.of();
    }
    
    @Override
    public void close()
    {
    }
    
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
        catch(Exception e)
        {
            // Guard against any exception from ImageHandler (e.g., uninitialized folders)
            YDM.log("Error resolving image file: " + filename + " - " + e.getMessage());
        }
        
        return null;
    }
}
