package de.evitonative.elytra_only_cape.config;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public enum ActiveEnvironment implements ConfigEnum<ActiveEnvironment> {
    ANY,
    SINGLEPLAYER,
    NONE;

    @Override
    public @NotNull MutableComponent format(String name) {
        MutableComponent component = ConfigEnum.super.format(name);

        ChatFormatting style = switch (this) {
            case ANY -> ChatFormatting.GREEN;
            case SINGLEPLAYER -> ChatFormatting.YELLOW;
            case NONE -> ChatFormatting.RED;
        };

        return component.withStyle(style);
    }

}
