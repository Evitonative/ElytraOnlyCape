package de.evitonative.elytra_only_cape;

import de.evitonative.elytra_only_cape.config.ModConfig;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElytraOnlyCape implements ModInitializer {

    public static final String MOD_ID = "elytra_only_cape";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModConfig.load();
    }
}
