package de.evitonative.elytra_only_cape.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.evitonative.elytra_only_cape.ElytraOnlyCape;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("elytra_only_cape.json").toFile();

    // Config
    public ActiveEnvironment modActiveEnvironment = ActiveEnvironment.ANY;
    public boolean invertBehaviour = false;
    public FallbackMode fallbackMode = FallbackMode.SHOW;

    public boolean alwaysShowVanillaButton = false;

    // Config Instance
    public static ModConfig instance = new ModConfig();

    public static void load() {
        if (!CONFIG_FILE.exists()) {
            save();
            return;
        }

        if (CONFIG_FILE.isDirectory()) {
            throw new UncheckedIOException(
                    new IOException("The config file cannot be a folder")
            );
        }

        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            ModConfig.instance = GSON.fromJson(reader, ModConfig.class);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load Elytra Only Cape config!", e);
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(ModConfig.instance, writer);
        } catch (IOException e) {
            ElytraOnlyCape.LOGGER.error("Failed to save Elytra Only Cape config!", e);
        }
    }
}
