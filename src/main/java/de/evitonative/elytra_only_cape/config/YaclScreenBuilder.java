package de.evitonative.elytra_only_cape.config;

import de.evitonative.elytra_only_cape.CapeToggleHelper;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.LinkedList;

public class YaclScreenBuilder {
    public static Screen createScreen(Screen parentScreen) {
        return YetAnotherConfigLib.createBuilder().title(Component.translatable("elytra_only_cape.config.title")).category(ConfigCategory.createBuilder().name(Component.translatable("elytra_only_cape.config.title")).tooltip(Component.translatable("elytra_only_cape.config.title"))
                // Mod Enabled
                .option(Option.<ActiveEnvironment>createBuilder()
                        .name(Component.translatable("elytra_only_cape.config.mode_mode.title"))
                        .description(OptionDescription.of(
                                Component.translatable("elytra_only_cape.config.mode_mode.description"))
                        )
                        .binding(
                                ActiveEnvironment.ANY,
                                () -> ModConfig.instance.modActiveEnvironment,
                                newVal -> ModConfig.instance.modActiveEnvironment = newVal
                        )
                        .controller(opt -> EnumControllerBuilder
                                .create(opt)
                                .enumClass(ActiveEnvironment.class)
                                .formatValue(v -> {
                                    String translation_key = "elytra_only_cape.config.mode_mode.option." + v.name().toLowerCase();

                                    ChatFormatting style = switch (v) {
                                        case ANY -> ChatFormatting.GREEN;
                                        case SINGLEPLAYER -> ChatFormatting.YELLOW;
                                        case NONE -> ChatFormatting.RED;
                                    };

                                    return Component
                                            .translatable(translation_key)
                                            .withStyle(style);
                                })
                        )
                        .build()
                )
                // Inverted Mode
                .option(Option.<Boolean>createBuilder()
                        .name(Component.translatable("elytra_only_cape.config.invert.title"))
                        .description(OptionDescription.of(
                                Component.translatable("elytra_only_cape.config.invert.description"))
                        )
                        .binding(
                                false,
                                () -> ModConfig.instance.invertBehaviour,
                                newVal -> ModConfig.instance.invertBehaviour = newVal)
                        .controller(TickBoxControllerBuilder::create).build()
                )
                // Fallback Mode
                .option(Option.<FallbackMode>createBuilder()
                        .name(Component.translatable("elytra_only_cape.config.fallback.title"))
                        .description(OptionDescription.of(
                                Component.translatable("elytra_only_cape.config.fallback.description"))
                        )
                        .binding(
                                FallbackMode.SHOW,
                                () -> ModConfig.instance.fallbackMode,
                                newVal -> ModConfig.instance.fallbackMode = newVal)
                        .controller(opt -> EnumControllerBuilder
                                .create(opt)
                                .enumClass(FallbackMode.class)
                                .formatValue(v -> {
                                    String translation_key = "elytra_only_cape.config.fallback.option." + v.name().toLowerCase();

                                    return net.minecraft.network.chat.Component.translatable(translation_key);
                                })
                        )
                        .build()
                )
                // Server Whitelist Enabled
                .option(Option.<Boolean>createBuilder()
                        .name(Component.translatable("elytra_only_cape.config.whitelist_enabled.title"))
                        .description(OptionDescription.of(
                                Component.translatable("elytra_only_cape.config.whitelist_enabled.description"))
                        )
                        .binding(
                                false,
                                () -> ModConfig.instance.serverWhitelistEnabled,
                                newVal -> ModConfig.instance.serverWhitelistEnabled = newVal
                        )
                        .controller(TickBoxControllerBuilder::create).build()
                )
                // Server Whitelist
                .option(ListOption.<String>createBuilder()
                        .name(Component.translatable("elytra_only_cape.config.whitelist.title"))
                        .description(OptionDescription.of(
                                Component.translatable("elytra_only_cape.config.whitelist.description"))
                        )
                        .binding(
                                new LinkedList<>(),
                                () -> ModConfig.instance.serverWhitelist,
                                newVal -> ModConfig.instance.serverWhitelist = newVal)
                        .controller(StringControllerBuilder::create).initial("")
                        .build()
                )
                // Server Blacklist
                .option(ListOption.<String>createBuilder()
                        .name(Component.translatable("elytra_only_cape.config.blacklist.title"))
                        .description(OptionDescription.of(
                                Component.translatable("elytra_only_cape.config.blacklist.description")
                        ))
                        .binding(new LinkedList<>(), () -> ModConfig.instance.serverBlacklist, newVal -> ModConfig.instance.serverBlacklist = newVal).controller(StringControllerBuilder::create).initial("").build())

                // Advanced Section
                .group(OptionGroup.createBuilder()
                        .name(Component.translatable("elytra_only_cape.config.advanced.title"))
                        .description(OptionDescription.of(
                                Component.translatable("elytra_only_cape.config.advanced.description")
                        ))
                        .collapsed(true)
                        // Always show vanilla option
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("elytra_only_cape.config.advanced.vanilla_button_enabled.title"))
                                .description(OptionDescription.of(
                                        Component.translatable("elytra_only_cape.config.advanced.vanilla_button_enabled.description")
                                ))
                                .binding(
                                        false,
                                        () -> ModConfig.instance.alwaysShowVanillaButton,
                                        newVal -> ModConfig.instance.alwaysShowVanillaButton = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build()
                        ).build()
                ).build()
        ).save(() -> {
            ModConfig.save();
            updateCapeVisibility();
        }).build().generateScreen(parentScreen);
    }


    private static void updateCapeVisibility() {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player != null) {
            ItemStack chestItem = minecraft.player.getItemBySlot(EquipmentSlot.CHEST);
            boolean elytraIsEquipped = chestItem.is(Items.ELYTRA);
            CapeToggleHelper.updateCapeVisibility(elytraIsEquipped);
        } else {
            CapeToggleHelper.fallbackHandling();
        }
    }
}
