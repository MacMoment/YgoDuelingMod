package de.cas_ual_ty.ydm.cardinventory;

import de.cas_ual_ty.ydm.util.JsonKeys;
import de.cas_ual_ty.ydm.util.YdmUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;

import java.util.UUID;

public abstract class UUIDCardsManager extends JsonCardsManager
{
    private UUID uuid;
    
    public UUIDCardsManager()
    {
        uuid = null;
    }
    
    public UUID getUUID()
    {
        return uuid;
    }
    
    public void setUUID(UUID uuid)
    {
        this.uuid = uuid;
    }
    
    public void generateUUIDIfNull()
    {
        if(uuid == null)
        {
            uuid = YdmUtil.createRandomUUID();
        }
    }
    
    @Override
    public void readFromNBT(CompoundTag nbt)
    {
        if(nbt.contains(JsonKeys.UUID, Tag.TAG_INT_ARRAY))
        {
            Tag t = nbt.get(JsonKeys.UUID);
            if(t instanceof IntArrayTag)
            {
                uuid = NbtUtils.loadUUID((IntArrayTag) t);
            }
        }
    }
    
    @Override
    public void writeToNBT(CompoundTag nbt)
    {
        if(getUUID() != null)
        {
            nbt.put(JsonKeys.UUID, NbtUtils.createUUID(getUUID()));
        }
    }
}
