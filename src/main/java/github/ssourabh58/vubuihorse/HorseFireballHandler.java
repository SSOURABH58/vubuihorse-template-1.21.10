package github.ssourabh58.vubuihorse;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@EventBusSubscriber(modid = VuBuiHorse.MODID)
public class HorseFireballHandler {
    
    private static final Map<UUID, Long> horseLastSpitTime = new HashMap<>();
    private static final Map<UUID, LivingEntity> horseTargets = new HashMap<>();
    private static final Random random = new Random();

    @SubscribeEvent
    public static void onHorseDamaged(LivingDamageEvent.Pre event) {
        if (!(event.getEntity() instanceof AbstractHorse horse)) {
            return;
        }

        Entity attacker = event.getSource().getEntity();
        // Only target players who hurt the horse
        if (attacker instanceof Player player) {
            horseTargets.put(horse.getUUID(), player);
        }
    }

    @SubscribeEvent
    public static void onHorseTick(EntityTickEvent.Pre event) {
        if (!(event.getEntity() instanceof AbstractHorse horse)) {
            return;
        }

        // Tamed horses don't auto-spit, only on player command
        if (horse.isTamed()) {
            return;
        }

        // Check if horse has a target and should spit
        UUID horseId = horse.getUUID();
        LivingEntity target = horseTargets.get(horseId);

        if (target != null && target.isAlive() && horse.distanceTo(target) < 16.0) {
            // Horse has a valid target within range
            if (canHorseSpit(horseId)) {
                spitFireball(horse, target);
                updateCooldown(horseId);
            }
        } else if (target != null && (!target.isAlive() || horse.distanceTo(target) >= 20.0)) {
            // Clear target if dead or too far
            horseTargets.remove(horseId);
        }

        // Wild horses occasionally spit at nearby threats (non-players)
        if (!horse.isTamed() && random.nextDouble() < Config.WILD_HORSE_SPIT_CHANCE.get()) {
            LivingEntity nearbyThreat = findNearbyThreat(horse);
            if (nearbyThreat != null && canHorseSpit(horseId)) {
                spitFireball(horse, nearbyThreat);
                updateCooldown(horseId);
            }
        }
    }

    public static boolean canHorseSpit(UUID horseId) {
        Long lastSpitTime = horseLastSpitTime.get(horseId);
        if (lastSpitTime == null) {
            return true;
        }

        long currentTime = System.currentTimeMillis();
        int minCooldown = Config.FIREBALL_COOLDOWN_MIN.get() * 1000;
        int maxCooldown = Config.FIREBALL_COOLDOWN_MAX.get() * 1000;
        int cooldown = minCooldown + random.nextInt(maxCooldown - minCooldown + 1);

        return (currentTime - lastSpitTime) >= cooldown;
    }

    public static void updateCooldown(UUID horseId) {
        horseLastSpitTime.put(horseId, System.currentTimeMillis());
    }

    public static void spitFireball(AbstractHorse horse, LivingEntity target) {
        if (horse.level().isClientSide()) {
            return;
        }

        Vec3 horsePos = horse.position().add(0, horse.getEyeHeight(), 0);
        Vec3 targetPos = target.position().add(0, target.getEyeHeight() * 0.5, 0);
        Vec3 direction = targetPos.subtract(horsePos).normalize();

        // Create fireball with 0 explosion power (no block destruction)
        LargeFireball fireball = new LargeFireball(
            horse.level(),
            horse,
            direction,
            0 // explosion power 0 = no block destruction
        );

        fireball.setPos(horsePos.x, horsePos.y, horsePos.z);
        horse.level().addFreshEntity(fireball);

        // Play sound
        horse.playSound(net.minecraft.sounds.SoundEvents.GHAST_SHOOT, 1.0F, 1.0F);
    }

    private static LivingEntity findNearbyThreat(AbstractHorse horse) {
        java.util.List<LivingEntity> nearbyEntities = horse.level().getEntitiesOfClass(
            LivingEntity.class,
            horse.getBoundingBox().inflate(10.0),
            entity -> entity != horse && entity.isAlive() && !(entity instanceof Player)
        );
        
        if (!nearbyEntities.isEmpty()) {
            return nearbyEntities.get(0);
        }
        return null;
    }
    
    public static long getCooldownRemaining(UUID horseId) {
        Long lastSpitTime = horseLastSpitTime.get(horseId);
        if (lastSpitTime == null) {
            return 0;
        }
        
        long currentTime = System.currentTimeMillis();
        int minCooldown = Config.FIREBALL_COOLDOWN_MIN.get() * 1000;
        int maxCooldown = Config.FIREBALL_COOLDOWN_MAX.get() * 1000;
        int cooldown = minCooldown + random.nextInt(maxCooldown - minCooldown + 1);
        
        long elapsed = currentTime - lastSpitTime;
        return Math.max(0, cooldown - elapsed);
    }

    public static void clearHorseData(UUID horseId) {
        horseLastSpitTime.remove(horseId);
        horseTargets.remove(horseId);
    }
}
