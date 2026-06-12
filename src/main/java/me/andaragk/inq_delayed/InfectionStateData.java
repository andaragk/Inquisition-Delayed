package me.andaragk.inq_delayed;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

public final class InfectionStateData extends SavedData {
    private static final String FILE_ID = InquisitionDelayed.MODID;
    private static final String ACTIVE_KEY = "Active";
    private static final Factory<InfectionStateData> FACTORY = new Factory<>(
        InfectionStateData::new,
        InfectionStateData::load
    );

    private boolean active;

    public static InfectionStateData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(FACTORY, FILE_ID);
    }

    private static InfectionStateData load(CompoundTag tag, HolderLookup.Provider registries) {
        InfectionStateData data = new InfectionStateData();
        data.active = tag.getBoolean(ACTIVE_KEY);
        return data;
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean isDormant() {
        return !this.active;
    }

    public boolean setActive(boolean active) {
        if (this.active == active) {
            return false;
        }

        this.active = active;
        this.setDirty();
        return true;
    }

    public String displayName() {
        return this.active ? "active" : "dormant";
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putBoolean(ACTIVE_KEY, this.active);
        return tag;
    }
}
