package de.evitonative.elytra_only_cape;

import de.evitonative.elytra_only_cape.config.ActiveEnvironment;
import de.evitonative.elytra_only_cape.config.FallbackMode;
import de.evitonative.elytra_only_cape.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.world.entity.player.PlayerModelPart;

public class CapeToggleHelper {
    public static void updateCapeVisibility(boolean elytraIsEquipped) {
        if (fallbackHandling()) return;

        boolean targetCapeVisibility = elytraIsEquipped ^ ModConfig.instance.invertBehaviour;
        updateCapeVisibilityUnchecked(targetCapeVisibility);
    }

    public static void updateCapeVisibilityUnchecked(boolean newVisibility) {
        Minecraft minecraft = Minecraft.getInstance();
        Options options = minecraft.options;

        boolean previousCapeVisibility = options.isModelPartEnabled(PlayerModelPart.CAPE);
        if (previousCapeVisibility != newVisibility) {
            options.setModelPart(PlayerModelPart.CAPE, newVisibility);
            options.broadcastOptions();
        }
    }

    /// @return false if the cape should be enabled by the normal logic, if false the fallback cape was enabled if required
    public static boolean fallbackHandling() {
        if (isActiveInEnvironment()) return false;

        FallbackMode fallbackMode = ModConfig.instance.fallbackMode;
        if (fallbackMode == FallbackMode.KEEP) return true;

        boolean showCape = fallbackMode == FallbackMode.SHOW;
        CapeToggleHelper.updateCapeVisibilityUnchecked(showCape);
        return true;
    }

    public static boolean isActiveInEnvironment() {
        if (ModConfig.instance.modActiveEnvironment == ActiveEnvironment.ANY)
            return true;

        Minecraft minecraft = Minecraft.getInstance();
        boolean isInMenu = minecraft.player == null;
        boolean isActiveSinglePlayer = minecraft.isSingleplayer() && !isInMenu;
        boolean isModEnabledOnlySinglePlayer = ModConfig.instance.modActiveEnvironment == ActiveEnvironment.SINGLEPLAYER;

        return isActiveSinglePlayer && isModEnabledOnlySinglePlayer;
    }
}
