package de.evitonative.elytra_only_cape;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.world.entity.player.PlayerModelPart;

public class CapeToggleHelper {
    public static void updateCapeVisibility(boolean elytraIsEquipped) {
        Minecraft minecraft = Minecraft.getInstance();
        Options options = minecraft.options;

        boolean previousCapeVisibility = options.isModelPartEnabled(PlayerModelPart.CAPE);
        if (elytraIsEquipped != previousCapeVisibility) {
            options.setModelPart(PlayerModelPart.CAPE, elytraIsEquipped);
            options.broadcastOptions();
        }
    }
}
