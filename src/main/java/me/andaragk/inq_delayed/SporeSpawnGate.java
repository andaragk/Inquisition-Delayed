package me.andaragk.inq_delayed;

import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;

public final class SporeSpawnGate {
    private SporeSpawnGate() {
    }

    public static void onSpawnPlacementCheck(MobSpawnEvent.SpawnPlacementCheck event) {
        if (isDormant(event.getLevel().getLevel()) && SporeEntityTargets.shouldGate(event.getEntityType())) {
            event.setResult(MobSpawnEvent.SpawnPlacementCheck.Result.FAIL);
        }
    }

    public static void onPositionCheck(MobSpawnEvent.PositionCheck event) {
        if (isDormant(event.getLevel().getLevel()) && SporeEntityTargets.shouldGate(event.getEntity())) {
            event.setResult(MobSpawnEvent.PositionCheck.Result.FAIL);
        }
    }

    public static void onFinalizeSpawn(FinalizeSpawnEvent event) {
        if (isDormant(event.getLevel().getLevel()) && SporeEntityTargets.shouldGate(event.getEntity())) {
            event.setSpawnCancelled(true);
        }
    }

    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }

        if (isDormant(level) && SporeEntityTargets.shouldGate(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    private static boolean isDormant(ServerLevel level) {
        return InfectionStateData.get(level.getServer()).isDormant();
    }
}
