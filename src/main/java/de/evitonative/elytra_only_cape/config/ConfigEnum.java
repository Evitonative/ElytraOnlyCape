package de.evitonative.elytra_only_cape.config;

import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public interface ConfigEnum<E extends Enum<E> & ConfigEnum<E>> {
    private E getAsEnum() {
        //noinspection unchecked
        return (E) this;
    }

    @NotNull
    default MutableComponent format(String name) {
        Enum<?> v = getAsEnum();

        String translation_key = "elytra_only_cape.config." + name + ".option." + v.name().toLowerCase();
        return net.minecraft.network.chat.Component.translatable(translation_key);
    }
}
