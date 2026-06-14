package me.andaragk.inq_delayed.mixin;

import java.util.Set;
import me.andaragk.inq_delayed.InfectionStateData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.functions.CommandFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerFunctionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// NeoForge 1.21+ ships Mojang official names at runtime, so remap=false lets
// us use those names directly without MCP/searge lookup.
@Mixin(value = ServerFunctionManager.class, remap = false)
public abstract class SporeInquisitionFunctionGate {
    private static final String INQUISITION_NAMESPACE = "inqui";
    private static final Set<String> DORMANT_BLOCKED_FUNCTIONS = Set.of(
        "capullo",
        "calamity",
        "proto_count1",
        "proto_count2",
        "proto_count3",
        "gastcount",
        "evolcount",
        "hypercount",
        "close_inf",
        "age2",
        "age3",
        "age4",
        "age1_2",
        "age2_2",
        "age3_2",
        "age4_2",
        "bile_conversion",
        "misc_min_events",
        "20_sec_shit",
        "chunckloader",
        "min_events",
        "40_sec_shit",
        "15_sec_shit",
        "8min",
        "20min",
        "5min",
        "hour",
        "tick",
        "moundcount",
        "calls/maid_removal",
        "calls/undertale"
    );

    /**
     * Blocks Spore Inquisition's automatic datapack engine while dormant.
     *
     * This targets the functions used by load/tick tags and recurring schedules
     * instead of blocking every inqui:* function. Recipe, crafting, advancement and
     * item utility functions can remain available before the outbreak.
     *
     * /inqdel start sets the state to active before calling the init functions,
     * so the Inquisition startup chain runs normally even from the console.
     */
    @Inject(
        method = "execute(Lnet/minecraft/commands/functions/CommandFunction;Lnet/minecraft/commands/CommandSourceStack;)V",
        remap = false,
        at = @At("HEAD"),
        cancellable = true
    )
    private void inq_delayed$gateSporeFunction(
        CommandFunction<CommandSourceStack> function,
        CommandSourceStack source,
        CallbackInfo ci
    ) {
        ResourceLocation id = function.id();
        if (!id.getNamespace().equals(INQUISITION_NAMESPACE) || !DORMANT_BLOCKED_FUNCTIONS.contains(id.getPath())) {
            return;
        }

        MinecraftServer server = source.getServer();
        if (server == null) {
            return;
        }

        if (InfectionStateData.get(server).isDormant()) {
            ci.cancel();
        }
    }
}
