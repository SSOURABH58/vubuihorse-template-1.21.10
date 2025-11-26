package github.ssourabh58.vubuihorse;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // Fireball cooldown configuration
    public static final ModConfigSpec.IntValue FIREBALL_COOLDOWN_MIN = BUILDER
            .comment("Minimum cooldown time in seconds for horse fireball spitting")
            .defineInRange("fireballCooldownMin", 5, 1, 60);

    public static final ModConfigSpec.IntValue FIREBALL_COOLDOWN_MAX = BUILDER
            .comment("Maximum cooldown time in seconds for horse fireball spitting")
            .defineInRange("fireballCooldownMax", 10, 1, 60);

    public static final ModConfigSpec.DoubleValue FIREBALL_DAMAGE = BUILDER
            .comment("Damage dealt by horse fireballs")
            .defineInRange("fireballDamage", 6.0, 1.0, 20.0);

    public static final ModConfigSpec.BooleanValue ENABLE_FIRE_SPREAD = BUILDER
            .comment("Whether horse fireballs can spread fire")
            .define("enableFireSpread", true);

    public static final ModConfigSpec.DoubleValue WILD_HORSE_SPIT_CHANCE = BUILDER
            .comment("Chance per tick for wild horses to spit at nearby threats (0.0 to 1.0)")
            .defineInRange("wildHorseSpitChance", 0.01, 0.0, 1.0);

    static final ModConfigSpec SPEC = BUILDER.build();
}
