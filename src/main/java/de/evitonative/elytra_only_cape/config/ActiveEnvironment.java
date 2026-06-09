package de.evitonative.elytra_only_cape.config;

import dev.isxander.yacl3.api.NameableEnum;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public enum ActiveEnvironment implements NameableEnum {
    ANY,
    SINGLEPLAYER,
    NONE;

    @Override
    public Component getDisplayName() {
        String translation_key = "elytra_only_cape.config.mode_mode.option." + name().toLowerCase();

        ChatFormatting style =  switch (this) {
            case ANY -> ChatFormatting.GREEN;
            case SINGLEPLAYER -> ChatFormatting.YELLOW;
            case NONE -> ChatFormatting.RED;
        };

        return Component
                .translatable(translation_key)
                .withStyle(style);
    }
}
