package com.rlapcs.radiotransfer.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NBTUtils {
    public static NBTTagList serializeList(Collection<INBTSerializable<NBTTagCompound>> collection) {
        NBTTagList tagList = new NBTTagList();
        for (INBTSerializable c : collection) {
            tagList.appendTag(c.serializeNBT());
        }
        return tagList;
    }

    public static <T extends INBTSerializable<NBTTagCompound>> List<T> deserializeList(NBTTagList tagList, Class<T> elementClass) throws InstantiationException, IllegalAccessException {
        List<T> outList = new ArrayList<>();
        for(int i=0; i<tagList.tagCount(); i++) {
            T newElem = elementClass.newInstance();
            NBTTagCompound tag = tagList.getCompoundTagAt(i);
            newElem.deserializeNBT(tag);
            outList.add(newElem);
        }
        return outList;
    }

    public static NBTTagCompound serializeInstant(Instant inst) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setLong("seconds", inst.getEpochSecond());
        nbt.setInteger("nanos", inst.getNano());
        return nbt;
    }
    public static Instant deserializeInstant(NBTTagCompound nbt) throws IllegalArgumentException {
        if(nbt.hasKey("seconds") && nbt.hasKey("nanos")) {
            long seconds = nbt.getLong("seconds");
            int nanos = nbt.getInteger("nanos");
            return Instant.ofEpochSecond(seconds, nanos);
        } else {
            throw new IllegalArgumentException("NBTTagCompound argument of deserializeInstant() must contain keys \"seconds\" (long) and \"nanos\" (int)");
        }
    }
    /*
    public static NBTTagCompound writeEnumToNBT(NBTTagCompound nbt, Enum<?> enom, String tag) {
        nbt.setString(enom.name(), tag);
        return nbt;
    }

    public static Enum<?> readEnumFromNBT(NBTTagCompound nbt, String tag) throws IllegalArgumentException {
        if(nbt.hasKey(tag)) {
            Enum.nbt.getString(tag);
        }
    }
    */

}
