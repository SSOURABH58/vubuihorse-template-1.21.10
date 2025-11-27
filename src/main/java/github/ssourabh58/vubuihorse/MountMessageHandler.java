package github.ssourabh58.vubuihorse;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityMountEvent;

@EventBusSubscriber(modid = VuBuiHorse.MODID, value = Dist.CLIENT)
public class MountMessageHandler {

    @SubscribeEvent
    public static void onMount(EntityMountEvent event) {
        if (event.isMounting() && event.getEntity() instanceof Player player) {
            if (event.getEntityBeingMounted() instanceof AbstractHorse horse) {
                if (horse.isTamed() && horse.isSaddled()) {
                    // Show hint message about fireball keybind
                    if (player.level().isClientSide()) {
                        String keyName = KeyBindings.horseFireballKey.getTranslatedKeyMessage().getString();
                        player.displayClientMessage(
                            Component.translatable("vubuihorse.mount.hint", keyName),
                            true // Show as action bar message
                        );
                    }
                }
            }
        }
    }
}
