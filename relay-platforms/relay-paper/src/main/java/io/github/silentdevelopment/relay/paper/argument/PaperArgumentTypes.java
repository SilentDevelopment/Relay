package io.github.silentdevelopment.relay.paper.argument;

import io.github.silentdevelopment.relay.argument.ArgumentType;
import io.github.silentdevelopment.relay.argument.ArgumentTypeRegistry;
import io.github.silentdevelopment.relay.core.argument.DefaultArgumentType;
import io.github.silentdevelopment.relay.core.argument.DefaultSuggestingArgumentType;
import io.github.silentdevelopment.relay.core.argument.parser.BooleanParser;
import io.github.silentdevelopment.relay.core.argument.parser.DoubleParser;
import io.github.silentdevelopment.relay.core.argument.parser.EnumParser;
import io.github.silentdevelopment.relay.core.argument.parser.FloatParser;
import io.github.silentdevelopment.relay.core.argument.parser.GreedyStringParser;
import io.github.silentdevelopment.relay.core.argument.parser.IntegerParser;
import io.github.silentdevelopment.relay.core.argument.parser.LongParser;
import io.github.silentdevelopment.relay.core.argument.parser.StringParser;
import io.github.silentdevelopment.relay.core.argument.parser.UUIDParser;
import io.github.silentdevelopment.relay.paper.argument.parser.AttributeParser;
import io.github.silentdevelopment.relay.paper.argument.parser.BiomeParser;
import io.github.silentdevelopment.relay.paper.argument.parser.ComponentParser;
import io.github.silentdevelopment.relay.paper.argument.parser.EnchantmentParser;
import io.github.silentdevelopment.relay.paper.argument.parser.EntityTypeParser;
import io.github.silentdevelopment.relay.paper.argument.parser.GameModeParser;
import io.github.silentdevelopment.relay.paper.argument.parser.GreedyComponentParser;
import io.github.silentdevelopment.relay.paper.argument.parser.MaterialParser;
import io.github.silentdevelopment.relay.paper.argument.parser.NamespacedKeyParser;
import io.github.silentdevelopment.relay.paper.argument.parser.OfflinePlayerParser;
import io.github.silentdevelopment.relay.paper.argument.parser.ParticleParser;
import io.github.silentdevelopment.relay.paper.argument.parser.PlayerParser;
import io.github.silentdevelopment.relay.paper.argument.parser.PotionEffectTypeParser;
import io.github.silentdevelopment.relay.paper.argument.parser.SoundParser;
import io.github.silentdevelopment.relay.paper.argument.parser.WorldParser;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public final class PaperArgumentTypes {

    private PaperArgumentTypes() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    public static void registerDefaults(ArgumentTypeRegistry registry) {
        Objects.requireNonNull(registry, "registry");
        registry.register(COMPONENT);
        registry.register(GREEDY_COMPONENT);
        registry.register(PLAYER);
        registry.register(OFFLINE_PLAYER);
        registry.register(WORLD);
        registry.register(GAME_MODE);
        registry.register(MATERIAL);
        registry.register(ENTITY_TYPE);
        registry.register(ENCHANTMENT);
        registry.register(NAMESPACED_KEY);
        registry.register(SOUND);
        registry.register(PARTICLE);
        registry.register(BIOME);
        registry.register(POTION_EFFECT_TYPE);
        registry.register(ATTRIBUTE);
    }

    public static final ArgumentType<Component> COMPONENT = new DefaultArgumentType<>("component", Component.class, new ComponentParser());
    public static final ArgumentType<Component> GREEDY_COMPONENT = new DefaultArgumentType<>("greedy_component", Component.class, new GreedyComponentParser());

    public static final ArgumentType<Player> PLAYER = new DefaultSuggestingArgumentType<>(
            "player",
            Player.class,
            new PlayerParser(),
            ctx -> Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .toList()
    );

    public static final ArgumentType<OfflinePlayer> OFFLINE_PLAYER = new DefaultSuggestingArgumentType<>(
            "offline_player",
            OfflinePlayer.class,
            new OfflinePlayerParser(),
            ctx -> Arrays.stream(Bukkit.getOfflinePlayers())
                    .filter(OfflinePlayer::hasPlayedBefore)
                    .map(OfflinePlayer::getName)
                    .filter(Objects::nonNull)
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .toList()
    );

    public static final ArgumentType<World> WORLD = new DefaultSuggestingArgumentType<>(
            "world",
            World.class,
            new WorldParser(),
            ctx -> Bukkit.getWorlds().stream()
                    .map(World::getName)
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .toList()
    );

    public static final ArgumentType<GameMode> GAME_MODE = new DefaultSuggestingArgumentType<>(
            "game_mode",
            GameMode.class,
            new GameModeParser(),
            ctx -> Arrays.stream(GameMode.values())
                    .map(value -> value.name().toLowerCase(Locale.ROOT))
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .toList()
    );

    public static final ArgumentType<Material> MATERIAL = new DefaultSuggestingArgumentType<>(
            "material",
            Material.class,
            new MaterialParser(),
            ctx -> Arrays.stream(Material.values())
                    .filter(Material::isItem)
                    .map(value -> value.name().toLowerCase(Locale.ROOT))
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .toList()
    );

    public static final ArgumentType<EntityType> ENTITY_TYPE = new DefaultSuggestingArgumentType<>(
            "entity_type",
            EntityType.class,
            new EntityTypeParser(),
            ctx -> Arrays.stream(EntityType.values())
                    .filter(EntityType::isAlive)
                    .map(value -> value.name().toLowerCase(Locale.ROOT))
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .toList()
    );

    public static final ArgumentType<NamespacedKey> NAMESPACED_KEY = new DefaultArgumentType<>(
            "namespaced_key",
            NamespacedKey.class,
            new NamespacedKeyParser()
    );

    public static final ArgumentType<Enchantment> ENCHANTMENT = new DefaultSuggestingArgumentType<>(
            "enchantment",
            Enchantment.class,
            new EnchantmentParser(),
            ctx -> {
                Registry<Enchantment> registry = EnchantmentParser.enchantmentRegistry();
                return registry.stream()
                        .map(registry::getKeyOrThrow)
                        .map(NamespacedKey::toString)
                        .sorted(String.CASE_INSENSITIVE_ORDER)
                        .toList();
            }
    );

    public static final ArgumentType<Sound> SOUND = new DefaultSuggestingArgumentType<>(
            "sound",
            Sound.class,
            new SoundParser(),
            ctx -> Registry.SOUND_EVENT.stream()
                    .map(Registry.SOUND_EVENT::getKeyOrThrow)
                    .map(NamespacedKey::toString)
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .toList()
    );

    public static final ArgumentType<Attribute> ATTRIBUTE = new DefaultSuggestingArgumentType<>(
            "attribute",
            Attribute.class,
            new AttributeParser(),
            ctx -> Registry.ATTRIBUTE.stream()
                    .map(Registry.ATTRIBUTE::getKeyOrThrow)
                    .map(NamespacedKey::toString)
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .toList()
    );

    public static final ArgumentType<Biome> BIOME = new DefaultSuggestingArgumentType<>(
            "biome",
            Biome.class,
            new BiomeParser(),
            ctx -> {
                Registry<Biome> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME);
                return registry.stream()
                        .map(registry::getKeyOrThrow)
                        .map(NamespacedKey::toString)
                        .sorted(String.CASE_INSENSITIVE_ORDER)
                        .toList();
            }
    );

    public static final ArgumentType<Particle> PARTICLE = new DefaultSuggestingArgumentType<>(
            "particle",
            Particle.class,
            new ParticleParser(),
            ctx -> Arrays.stream(Particle.values())
                    .map(value -> value.name().toLowerCase(Locale.ROOT))
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .toList()
    );

    public static final ArgumentType<PotionEffectType> POTION_EFFECT_TYPE = new DefaultSuggestingArgumentType<>(
            "potion_effect_type",
            PotionEffectType.class,
            new PotionEffectTypeParser(),
            ctx -> {
                Registry<PotionEffectType> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.MOB_EFFECT);
                return registry.stream()
                        .map(registry::getKeyOrThrow)
                        .map(NamespacedKey::toString)
                        .sorted(String.CASE_INSENSITIVE_ORDER)
                        .toList();
            }
    );

    //region Default argument types
    public static final ArgumentType<String> STRING = new DefaultArgumentType<>("string", String.class, new StringParser());
    public static final ArgumentType<String> GREEDY_STRING = new DefaultArgumentType<>("greedy_string", String.class, new GreedyStringParser());
    public static final ArgumentType<Boolean> BOOLEAN = new DefaultArgumentType<>("boolean", Boolean.class, new BooleanParser());
    public static final ArgumentType<Integer> INTEGER = new DefaultArgumentType<>("integer", Integer.class, new IntegerParser());
    public static final ArgumentType<Long> LONG = new DefaultArgumentType<>("long", Long.class, new LongParser());
    public static final ArgumentType<Double> DOUBLE = new DefaultArgumentType<>("double", Double.class, new DoubleParser());
    public static final ArgumentType<Float> FLOAT = new DefaultArgumentType<>("float", Float.class, new FloatParser());
    public static final ArgumentType<UUID> UUID = new DefaultArgumentType<>("uuid", UUID.class, new UUIDParser());

    public static <E extends Enum<E>> ArgumentType<E> enumType(Class<E> enumType) {
        if (enumType == null) {
            throw new IllegalArgumentException("enumType cannot be null.");
        }

        return new DefaultArgumentType<>(
                "enum:" + enumType.getSimpleName().toLowerCase(Locale.ROOT),
                enumType,
                new EnumParser<>(enumType)
        );
    }
    //endregion

}