package de.evitonative.elytra_only_cape.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import de.evitonative.elytra_only_cape.CapeToggleHelper;
import de.evitonative.elytra_only_cape.config.FallbackMode;
import de.evitonative.elytra_only_cape.config.ModConfig;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.options.SkinCustomizationScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.PlayerModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SkinCustomizationScreen.class)
public class SkinCustomizationScreenMixin {
    @Inject(
            method = "addOptions",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            )
    )
    private void onAddOption(
            CallbackInfo ci,
            @Local(name = "widgets") List<AbstractWidget> widgets,
            @Local(name = "part") PlayerModelPart part
    ) {
        if (part != PlayerModelPart.CAPE) return;

        if (ModConfig.instance.alwaysShowVanillaButton) return;
        if (ModConfig.instance.fallbackMode == FallbackMode.KEEP && !CapeToggleHelper.isModActiveInEnvironment()) return;

        AbstractWidget widget = widgets.getLast();
        widget.active = false;
        widget.setTooltip(
            Tooltip.create(
                    Component.translatable("elytra_only_cape.vanilla_button.tooltip")
            )
        );
    }
}
