package de.evitonative.elytra_only_cape.config;

import de.evitonative.elytra_only_cape.CapeToggleHelper;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.LinkedList;

public class YaclScreenBuilder {
    public static Screen createScreen(Screen parentScreen) {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("elytra_only_cape.config.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("elytra_only_cape.config.title"))
                        .tooltip(Component.translatable("elytra_only_cape.config.title"))
                        // Mod Enabled
                        .option(YaclOptionsFactory.createOption(
                                "mode_mode",
                                ActiveEnvironment.class,
                                ActiveEnvironment.ANY,
                                () -> ModConfig.instance.modActiveEnvironment,
                                newVal -> ModConfig.instance.modActiveEnvironment = newVal
                        ))
                        // Inverted Mode
                        .option(YaclOptionsFactory.createOption(
                                "invert",
                                false,
                                () -> ModConfig.instance.invertBehaviour,
                                newVal -> ModConfig.instance.invertBehaviour = newVal
                        ))
                        // Fallback Mode
                        .option(YaclOptionsFactory.createOption(
                                "fallback",
                                FallbackMode.class,
                                FallbackMode.KEEP,
                                () -> ModConfig.instance.fallbackMode,
                                newVal -> ModConfig.instance.fallbackMode = newVal
                        ))
                        // Server Whitelist Enabled
                        .option(YaclOptionsFactory.createOption(
                                "whitelist_enabled",
                                false,
                                () -> ModConfig.instance.serverWhitelistEnabled,
                                newVal -> ModConfig.instance.serverWhitelistEnabled = newVal
                        ))
                        // Server Whitelist Enabled
                        .option(YaclOptionsFactory.createOption(
                                "whitelist_realms",
                                true,
                                () -> ModConfig.instance.whitelistRealms,
                                newVal -> ModConfig.instance.whitelistRealms = newVal
                        ))
                        // Server Whitelist
                        .option(YaclOptionsFactory.createOption(
                                "whitelist",
                                new LinkedList<>(),
                                () -> ModConfig.instance.serverWhitelist,
                                newVal -> ModConfig.instance.serverWhitelist = newVal
                        ))
                        // Server Blacklist
                        .option(YaclOptionsFactory.createOption(
                                "blacklist",
                                new LinkedList<>(),
                                () -> ModConfig.instance.serverBlacklist,
                                newVal -> ModConfig.instance.serverBlacklist = newVal
                        ))
                        // Advanced Section
                        .group(OptionGroup.createBuilder()
                                .name(Component.translatable("elytra_only_cape.config.advanced.title"))
                                .description(OptionDescription.of(
                                        Component.translatable("elytra_only_cape.config.advanced.description")
                                ))
                                .collapsed(true)
                                // Always show vanilla option
                                .option(YaclOptionsFactory.createOption(
                                        "advanced.vanilla_button_enabled",
                                        false,
                                        () -> ModConfig.instance.alwaysShowVanillaButton,
                                        newVal -> ModConfig.instance.alwaysShowVanillaButton = newVal
                                ))
                                .build()
                        ).build()
                ).save(() -> {
                    ModConfig.save();
                    updateCapeVisibility();
                }).build().generateScreen(parentScreen);
    }


    private static void updateCapeVisibility() {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null) {
            CapeToggleHelper.fallbackHandling();
            return;
        }

        ItemStack chestItem = minecraft.player.getItemBySlot(EquipmentSlot.CHEST);
        boolean elytraIsEquipped = chestItem.is(Items.ELYTRA);
        CapeToggleHelper.updateCapeVisibility(elytraIsEquipped);
    }
}
