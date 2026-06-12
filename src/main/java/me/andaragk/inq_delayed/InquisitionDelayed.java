package me.andaragk.inq_delayed;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(InquisitionDelayed.MODID)
public final class InquisitionDelayed {
    public static final String MODID = "inq_delayed";
    public static final String COMMAND = "inqdel";
    public static final Logger LOGGER = LogUtils.getLogger();

    public InquisitionDelayed(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(InquisitionDelayedConfig::onLoad);
        modContainer.registerConfig(ModConfig.Type.COMMON, InquisitionDelayedConfig.SPEC);

        NeoForge.EVENT_BUS.addListener(InquisitionCommands::register);
        NeoForge.EVENT_BUS.addListener(SporeSpawnGate::onSpawnPlacementCheck);
        NeoForge.EVENT_BUS.addListener(SporeSpawnGate::onPositionCheck);
        NeoForge.EVENT_BUS.addListener(SporeSpawnGate::onFinalizeSpawn);
        NeoForge.EVENT_BUS.addListener(SporeSpawnGate::onEntityJoinLevel);

        LOGGER.info("Loaded Inquisition Delayed");
    }
}
