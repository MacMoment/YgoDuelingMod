package de.cas_ual_ty.ydm.clientutil;

import de.cas_ual_ty.ydm.YDM;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.IoSupplier;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Set;

public class YdmCardResourcePack extends AbstractPackResources
{
    public static final String PATH_PREFIX = "textures/item/";
    
    public YdmCardResourcePack()
    {
        super(new PackLocationInfo(YDM.MOD_ID, net.minecraft.network.chat.Component.literal("YDM Images"), net.minecraft.server.packs.repository.PackSource.DEFAULT, java.util.Optional.empty()));
    }
    
    @Nullable
    @Override
    public IoSupplier<InputStream> getRootResource(String... path)
    {
        // pack.mcmeta and pack.png are handled by the modloader
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
            return () -> new FileInputStream(image);
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
        File image = ImageHandler.getCardFile(filename);
        
        if(image.exists())
        {
            return image;
        }
        
        image = ImageHandler.getSetFile(filename);
        
        if(image.exists())
        {
            return image;
        }
        
        image = ImageHandler.getRarityFile(filename);
        
        if(image.exists())
        {
            return image;
        }
        
        return null;
    }
}
