package de.cas_ual_ty.ydm.util;

import net.minecraft.nbt.CompoundTag;
import de.cas_ual_ty.ydm.util.INBTSerializable;

public interface ICooldownHolder extends INBTSerializable<CompoundTag>
{
    void tick();
    
    boolean isOffCooldown();
    
    void setCooldown(int cooldown);
}
