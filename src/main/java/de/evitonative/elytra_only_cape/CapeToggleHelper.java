package de.evitonative.elytra_only_cape;

import de.evitonative.elytra_only_cape.config.ActiveEnvironment;
import de.evitonative.elytra_only_cape.config.FallbackMode;
import de.evitonative.elytra_only_cape.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.server.IntegratedServer;
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
        if (isModActiveInEnvironment()) return false;

        FallbackMode fallbackMode = ModConfig.instance.fallbackMode;
        if (fallbackMode == FallbackMode.KEEP) return true;

        boolean showCape = fallbackMode == FallbackMode.SHOW;
        CapeToggleHelper.updateCapeVisibilityUnchecked(showCape);
        return true;
    }

    public static boolean isModActiveInEnvironment() {
        ActiveEnvironment env = ModConfig.instance.modActiveEnvironment;

        if (env == ActiveEnvironment.NONE)
            return false;

        Minecraft minecraft = Minecraft.getInstance();

        boolean isInMenu = minecraft.player == null;
        if (isInMenu) return false;
        if (isSingleplayer(minecraft)) return true;
        if (env == ActiveEnvironment.SINGLEPLAYER) return false;

        ServerData currentServer = minecraft.getCurrentServer();
        if (currentServer == null) return false; // this should be impossible, but make the linter happy

        String currentIp = currentServer.ip.toLowerCase();

        if (ModConfig.instance.serverBlacklist.stream()
                .anyMatch(ip -> ip.toLowerCase().equals(currentIp))) return false;

        if (!ModConfig.instance.serverWhitelistEnabled) return true;

        if (ModConfig.instance.whitelistRealms && currentServer.isRealm()) return true;

        return ModConfig.instance.serverWhitelist.stream()
                .anyMatch(ip -> ip.toLowerCase().equals(currentIp));
    }

    public static boolean isSingleplayer(Minecraft minecraft) {
        IntegratedServer singleplayerServer = minecraft.getSingleplayerServer();
        return singleplayerServer != null && !singleplayerServer.isPublished();
    }
}
