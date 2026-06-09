package de.evitonative.elytra_only_cape.config;

import dev.isxander.yacl3.api.NameableEnum;
import net.minecraft.network.chat.Component;

public enum FallbackMode implements NameableEnum {
    SHOW,
    HIDE,
    KEEP;

    @Override
    public Component getDisplayName() {
        String translation_key = "elytra_only_cape.config.fallback.option." + name().toLowerCase();

        return net.minecraft.network.chat.Component.translatable(translation_key);
    }

}
