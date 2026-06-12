package me.andaragk.inq_delayed;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public final class SporeEntityTargets {
    public static final TagKey<EntityType<?>> SPORE_FUNGUS_ENTITIES = TagKey.create(
        Registries.ENTITY_TYPE,
        ResourceLocation.fromNamespaceAndPath("spore", "fungus_entities")
    );

    public static final TagKey<EntityType<?>> EXTRA_BLOCKED_ENTITIES = TagKey.create(
        Registries.ENTITY_TYPE,
        ResourceLocation.fromNamespaceAndPath(InquisitionDelayed.MODID, "blocked_spore_entities")
    );

    private SporeEntityTargets() {
    }

    public static boolean shouldGate(Entity entity) {
        return shouldGate(entity.getType());
    }

    public static boolean shouldGate(EntityType<?> type) {
        if (type.is(SPORE_FUNGUS_ENTITIES) || type.is(EXTRA_BLOCKED_ENTITIES)) {
            return true;
        }

        String namespace = EntityType.getKey(type).getNamespace();
        return namespace.equals("spore")
            || namespace.equals("spore_inquisition")
            || namespace.equals("sporeinquisition");
    }
}
