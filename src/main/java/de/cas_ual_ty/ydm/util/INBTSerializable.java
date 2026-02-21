package de.cas_ual_ty.ydm.util;

import net.minecraft.nbt.Tag;

/**
 * Simple NBT serialization interface, replacing the removed NeoForge INBTSerializable.
 */
public interface INBTSerializable<T extends Tag>
{
    T serializeNBT();
    
    void deserializeNBT(T nbt);
}
