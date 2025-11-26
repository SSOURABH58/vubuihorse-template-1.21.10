package github.ssourabh58.vubuihorse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;

@EventBusSubscriber(modid = VuBuiHorse.MODID)
public class HorseBreedingHandler {
    
    private static final String FIREBALL_TRAIT_TAG = "VuBuiHorseFireballTrait";

    @SubscribeEvent
    public static void onBabySpawn(BabyEntitySpawnEvent event) {
        if (event.getChild() instanceof AbstractHorse babyHorse) {
            // Baby horses inherit the fireball trait automatically
            // Since all horses in this mod can spit fireballs, we just mark them
            markHorseWithTrait(babyHorse);
            
            VuBuiHorse.LOGGER.info("A new fireball-spitting horse has been born!");
        }
    }

    public static void markHorseWithTrait(AbstractHorse horse) {
        CompoundTag tag = horse.getPersistentData();
        tag.putBoolean(FIREBALL_TRAIT_TAG, true);
    }

    public static boolean hasFireballTrait(AbstractHorse horse) {
        // In this mod, all horses have the trait by default
        return true;
    }
}
