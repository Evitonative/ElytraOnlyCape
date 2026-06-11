package de.evitonative.elytra_only_cape.config;

import dev.isxander.yacl3.api.ListOption;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class YaclOptionsFactory {
    private static <E> ListOption.Builder<E> makeBuilderWithoutController(
            String name,
            List<E> defaultValue,
            Supplier<List<E>> configGetter,
            Consumer<List<E>> configSetter
    ) {
        return ListOption.<E>createBuilder()
                .name(Component.translatable("elytra_only_cape.config." + name + ".title"))
                .description(OptionDescription.of(
                        Component.translatable("elytra_only_cape.config." + name + ".description"))
                )
                .binding(
                        defaultValue,
                        configGetter,
                        configSetter
                );
    }

    private static <E> Option.Builder<E> makeBuilderWithoutController(
            String name,
            E defaultValue,
            Supplier<E> configGetter,
            Consumer<E> configSetter
    ) {
        return Option.<E>createBuilder()
                .name(Component.translatable("elytra_only_cape.config." + name + ".title"))
                .description(OptionDescription.of(
                        Component.translatable("elytra_only_cape.config." + name + ".description"))
                )
                .binding(
                        defaultValue,
                        configGetter,
                        configSetter
                );
    }

    public static <E extends Enum<E> & ConfigEnum<E>> Option<E> createOption(
            String name,
            Class<E> clazz,
            E defaultValue,
            Supplier<E> configGetter,
            Consumer<E> configSetter
    ) {
        return makeBuilderWithoutController(name, defaultValue, configGetter, configSetter)
                .controller(opt -> EnumControllerBuilder
                        .create(opt)
                        .enumClass(clazz)
                        .formatValue(v -> v.format(name))
                )
                .build();
    }

    public static Option<Boolean> createOption(
            String name,
            boolean defaultValue,
            Supplier<Boolean> configGetter,
            Consumer<Boolean> configSetter
    ) {
        return makeBuilderWithoutController(name, defaultValue, configGetter, configSetter)
                .controller(TickBoxControllerBuilder::create)
                .build();
    }

    public static ListOption<String> createOption(
            String name,
            List<String> defaultValue,
            Supplier<List<String>> configGetter,
            Consumer<List<String>> configSetter
    ) {
        String initialStringValue = String.join(", ", defaultValue);

        return makeBuilderWithoutController(name, defaultValue, configGetter, configSetter)
                .controller(StringControllerBuilder::create)
                .initial(initialStringValue)
                .build();
    }
}
