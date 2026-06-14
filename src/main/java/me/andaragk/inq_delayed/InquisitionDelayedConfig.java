package me.andaragk.inq_delayed;

import java.util.List;
import java.util.Locale;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class InquisitionDelayedConfig {
    private static final List<String> MESSAGE_PRESETS = List.of(
        "sober",
        "dark_fantasy",
        "fragile_calm",
        "technical"
    );

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.ConfigValue<String> MESSAGE_PRESET = BUILDER
        .comment(
            "Message preset used for /inqdel start and /inqdel stop.",
            "Allowed values: sober, dark_fantasy, fragile_calm, technical."
        )
        .define("messagePreset", "sober", InquisitionDelayedConfig::isValidMessagePreset);

    static final ModConfigSpec SPEC = BUILDER.build();

    private static String messagePreset = "sober";

    private InquisitionDelayedConfig() {
    }

    public static String messagePreset() {
        return messagePreset;
    }

    private static boolean isValidMessagePreset(Object value) {
        return value instanceof String preset
            && MESSAGE_PRESETS.contains(preset.toLowerCase(Locale.ROOT));
    }

    static void onLoad(ModConfigEvent event) {
        if (event.getConfig().getSpec() != SPEC) {
            return;
        }

        messagePreset = MESSAGE_PRESET.get().toLowerCase(Locale.ROOT);
    }
}
