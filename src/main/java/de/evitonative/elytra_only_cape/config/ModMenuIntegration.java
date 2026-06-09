package de.evitonative.elytra_only_cape.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import de.evitonative.elytra_only_cape.CapeToggleHelper;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parentScreen -> YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("elytra_only_cape.config.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("elytra_only_cape.config.title"))
                        .tooltip(Component.translatable("elytra_only_cape.config.title"))
                        // Mod Enabled
                        .option(Option.<ActiveEnvironment>createBuilder()
                                .name(Component.translatable("elytra_only_cape.config.mode_mode.title"))
                                .description(OptionDescription.of(
                                        Component.translatable("elytra_only_cape.config.mode_mode.description")
                                ))
                                .binding(
                                        ActiveEnvironment.ANY,
                                        () -> ModConfig.instance.modActiveEnvironment,
                                        newVal -> {
                                            ModConfig.instance.modActiveEnvironment = newVal;
                                            updateCapeVisibility();
                                        }
                                )
                                .controller(opt ->
                                        EnumControllerBuilder.create(opt).enumClass(ActiveEnvironment.class)
                                )
                                .build())
                        // Inverted Mode
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("elytra_only_cape.config.invert.title"))
                                .description(OptionDescription.of(
                                        Component.translatable("elytra_only_cape.config.invert.description")
                                ))
                                .binding(
                                        false,
                                        () -> ModConfig.instance.invertBehaviour,
                                        newVal -> {
                                            ModConfig.instance.invertBehaviour = newVal;
                                            updateCapeVisibility();
                                        }
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        // Fallback Mode
                        .option(Option.<FallbackMode>createBuilder()
                                .name(Component.translatable("elytra_only_cape.config.fallback.title"))
                                .description(OptionDescription.of(
                                        Component.translatable("elytra_only_cape.config.fallback.description")
                                ))
                                .binding(
                                        FallbackMode.SHOW,
                                        () -> ModConfig.instance.fallbackMode,
                                        newVal -> {
                                            ModConfig.instance.fallbackMode = newVal;
                                            updateCapeVisibility();
                                        }
                                )
                                .controller(opt ->
                                        EnumControllerBuilder.create(opt).enumClass(FallbackMode.class)
                                )
                                .build())

                        // Advanced Section
                        .group(OptionGroup.createBuilder()
                                .name(Component.translatable("elytra_only_cape.config.advanced.title"))
                                .collapsed(true)
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.translatable("elytra_only_cape.config.advanced.vanilla_button_enabled.title"))
                                        .description(OptionDescription.of(
                                                Component.translatable("elytra_only_cape.config.advanced.vanilla_button_enabled.description")
                                        ))
                                        .binding(
                                                false,
                                                () -> ModConfig.instance.alwaysShowVanillaButton,
                                                newVal -> ModConfig.instance.alwaysShowVanillaButton = newVal
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .build())
                .save(ModConfig::save)
                .build()
                .generateScreen(parentScreen);
    }

    private void updateCapeVisibility() {
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
