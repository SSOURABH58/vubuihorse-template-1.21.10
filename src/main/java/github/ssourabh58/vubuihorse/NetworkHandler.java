package github.ssourabh58.vubuihorse;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkHandler {
    
    public static final ResourceLocation HORSE_FIREBALL_ID = ResourceLocation.fromNamespaceAndPath(VuBuiHorse.MODID, "horse_fireball");

    public static void register(PayloadRegistrar registrar) {
        registrar.playToServer(
            HorseFireballPacket.TYPE,
            HorseFireballPacket.STREAM_CODEC,
            HorseFireballPacket::handle
        );
    }

    public static void sendHorseFireballPacket() {
        Minecraft.getInstance().getConnection().send(new HorseFireballPacket());
    }

    public record HorseFireballPacket() implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<HorseFireballPacket> TYPE = 
            new CustomPacketPayload.Type<>(HORSE_FIREBALL_ID);

        public static final StreamCodec<FriendlyByteBuf, HorseFireballPacket> STREAM_CODEC = 
            StreamCodec.unit(new HorseFireballPacket());

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        public static void handle(HorseFireballPacket packet, IPayloadContext context) {
            context.enqueueWork(() -> {
                if (context.player() instanceof ServerPlayer serverPlayer) {
                    if (serverPlayer.getVehicle() instanceof AbstractHorse horse) {
                        if (horse.isTamed() && horse.isSaddled()) {
                            // Check cooldown
                            if (canHorseSpit(horse)) {
                                // Shoot in player's aim direction
                                shootInPlayerDirection(horse, serverPlayer);
                                updateCooldown(horse);
                            }
                        }
                    }
                }
            });
        }
    }

    private static boolean canHorseSpit(AbstractHorse horse) {
        return HorseFireballHandler.canHorseSpit(horse.getUUID());
    }

    private static void updateCooldown(AbstractHorse horse) {
        HorseFireballHandler.updateCooldown(horse.getUUID());
    }

    private static void shootInPlayerDirection(AbstractHorse horse, ServerPlayer player) {
        if (horse.level().isClientSide()) {
            return;
        }

        // Get player's look direction (not straight up)
        Vec3 lookVec = player.getLookAngle();
        Vec3 horsePos = horse.position().add(0, horse.getEyeHeight(), 0);

        // Create fireball with 0 explosion power (no block destruction)
        net.minecraft.world.entity.projectile.LargeFireball fireball = 
            new net.minecraft.world.entity.projectile.LargeFireball(
                horse.level(),
                horse,
                lookVec,
                0 // explosion power 0 = no block destruction
            );

        fireball.setPos(horsePos.x, horsePos.y, horsePos.z);
        horse.level().addFreshEntity(fireball);
        horse.playSound(net.minecraft.sounds.SoundEvents.GHAST_SHOOT, 1.0F, 1.0F);
    }
}
