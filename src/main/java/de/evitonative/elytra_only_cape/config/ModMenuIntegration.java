package de.evitonative.elytra_only_cape.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.NoticeWithLinkScreen;
import net.minecraft.network.chat.Component;

import java.net.URI;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3")) return YaclScreenBuilder::createScreen;

        return parent -> {
            Minecraft minecraft = Minecraft.getInstance();

            String version = FabricLoader.getInstance().getModContainer("minecraft").map(container ->
                    container.getMetadata().getVersion().getFriendlyString()
            ).orElse(null);

            StringBuilder stringBuilder = new StringBuilder("https://modrinth.com/mod/yacl/versions");
            if (version != null) {
                stringBuilder
                        .append("?version=").append(version)
                        .append("&g=").append(version)
                        .append("&");
            } else stringBuilder.append("?");

            stringBuilder.append("loader=fabric&l=fabric#download");

            return new NoticeWithLinkScreen(
                    Component
                            .translatable("elytra_only_cape.yacl_missing.title")
                            .withStyle(ChatFormatting.BOLD, ChatFormatting.RED),
                    Component
                            .translatable("elytra_only_cape.yacl_missing.description")
                            .withStyle(ChatFormatting.RED),
                    URI.create(stringBuilder.toString()),
                    () -> minecraft.setScreen(parent)
            );
        };
    }
}
