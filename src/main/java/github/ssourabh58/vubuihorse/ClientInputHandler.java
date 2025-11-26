package github.ssourabh58.vubuihorse;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = VuBuiHorse.MODID, value = Dist.CLIENT)
public class ClientInputHandler {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null) {
            return;
        }

        // Check if player pressed the fireball key
        if (KeyBindings.horseFireballKey.consumeClick()) {
            // Check if player is riding a tamed horse with saddle
            if (player.getVehicle() instanceof AbstractHorse horse) {
                if (horse.isTamed() && horse.isSaddled()) {
                    // Send packet to server to shoot fireball
                    NetworkHandler.sendHorseFireballPacket();
                }
            }
        }
    }
}
