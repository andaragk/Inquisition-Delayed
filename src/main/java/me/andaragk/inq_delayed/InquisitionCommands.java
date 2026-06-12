package me.andaragk.inq_delayed;

import static net.minecraft.commands.Commands.literal;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.functions.CommandFunction;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public final class InquisitionCommands {
    private static final ResourceLocation START_HOOK = ResourceLocation.fromNamespaceAndPath("inqdel", "internal/start_inquisition");
    private static final ResourceLocation STOP_HOOK = ResourceLocation.fromNamespaceAndPath("inqdel", "internal/stop_inquisition");

    private InquisitionCommands() {
    }

    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(
            literal(InquisitionDelayed.COMMAND)
                .then(literal("status")
                    .executes(context -> status(context.getSource())))
                .then(literal("start")
                    .requires(source -> source.hasPermission(2))
                    .executes(context -> start(context.getSource())))
                .then(literal("stop")
                    .requires(source -> source.hasPermission(2))
                    .executes(context -> stop(context.getSource())))
        );
    }

    private static int status(CommandSourceStack source) {
        InfectionStateData data = InfectionStateData.get(source.getServer());
        source.sendSuccess(() -> Component.translatable("commands.inq_delayed.status." + data.displayName()), false);
        return data.isActive() ? 1 : 0;
    }

    private static int start(CommandSourceStack source) {
        InfectionStateData data = InfectionStateData.get(source.getServer());
        boolean changed = data.setActive(true);
        boolean functionRan = runFunction(source.getServer(), START_HOOK);

        if (changed) {
            broadcastPresetMessage(source.getServer(), "start", ChatFormatting.DARK_RED, ChatFormatting.BOLD);
        } else {
            source.sendSuccess(() -> Component.translatable("commands.inq_delayed.start.already_active"), false);
        }

        InquisitionDelayed.LOGGER.info("Inquisition Delayed start command completed. State changed: {}. Hook executed: {}.", changed, functionRan);
        return 1;
    }

    private static int stop(CommandSourceStack source) {
        InfectionStateData data = InfectionStateData.get(source.getServer());
        boolean changed = data.setActive(false);
        int removed = removeActiveSporeEntities(source.getServer());
        boolean functionRan = runFunction(source.getServer(), STOP_HOOK);

        if (changed) {
            broadcastPresetMessage(source.getServer(), "stop", ChatFormatting.GRAY);
        } else {
            source.sendSuccess(() -> Component.translatable("commands.inq_delayed.stop.already_dormant"), false);
        }

        InquisitionDelayed.LOGGER.info("Inquisition Delayed stop command completed. State changed: {}. Removed entities: {}. Hook executed: {}.", changed, removed, functionRan);
        return removed;
    }

    private static void broadcastPresetMessage(MinecraftServer server, String action, ChatFormatting... styles) {
        String key = "commands.inq_delayed." + action + "." + InquisitionDelayedConfig.messagePreset();
        server.getPlayerList().broadcastSystemMessage(Component.translatable(key).withStyle(styles), false);
    }

    private static int removeActiveSporeEntities(MinecraftServer server) {
        int removed = 0;

        for (ServerLevel level : server.getAllLevels()) {
            List<Entity> targets = StreamSupport.stream(level.getAllEntities().spliterator(), false)
                .filter(SporeEntityTargets::shouldGate)
                .toList();

            targets.forEach(Entity::discard);
            removed += targets.size();
        }

        return removed;
    }

    private static boolean runFunction(MinecraftServer server, ResourceLocation functionId) {
        Optional<CommandFunction<CommandSourceStack>> function = server.getFunctions().get(functionId);
        if (function.isEmpty()) {
            return false;
        }

        CommandSourceStack source = server.createCommandSourceStack()
            .withPermission(2)
            .withSuppressedOutput();
        server.getFunctions().execute(function.get(), source);
        return true;
    }
}
