package github.ssourabh58.vubuihorse;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;

@EventBusSubscriber(modid = VuBuiHorse.MODID)
public class FireballImpactHandler {

    @SubscribeEvent
    public static void onFireballImpact(ProjectileImpactEvent event) {
        if (!(event.getProjectile() instanceof LargeFireball fireball)) {
            return;
        }

        // Only handle fireballs shot by horses
        if (!(fireball.getOwner() instanceof AbstractHorse)) {
            return;
        }

        // If hitting an entity, set it on fire
        if (event.getRayTraceResult() instanceof EntityHitResult entityHit) {
            if (entityHit.getEntity() != null) {
                entityHit.getEntity().setRemainingFireTicks(100); // 5 seconds of fire
            }
        }

        // If hitting a block, try to set fire above it (but don't burn the block itself)
        if (event.getRayTraceResult() instanceof BlockHitResult blockHit) {
            BlockPos hitPos = blockHit.getBlockPos();
            BlockPos abovePos = hitPos.above();
            
            // Only place fire if there's air above and the block below can support fire
            if (fireball.level().getBlockState(abovePos).isAir()) {
                BlockState belowState = fireball.level().getBlockState(hitPos);
                if (belowState.isSolid()) {
                    fireball.level().setBlock(abovePos, Blocks.FIRE.defaultBlockState(), 11);
                }
            }
        }
    }
}
