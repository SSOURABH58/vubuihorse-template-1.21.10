package github.ssourabh58.vubuihorse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;


@EventBusSubscriber(modid = VuBuiHorse.MODID, value = Dist.CLIENT)
public class CooldownHudOverlay {

    @SubscribeEvent
    public static void onRenderGuiPost(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        
        if (player == null || mc.options.hideGui) {
            return;
        }

        // Only show when riding a tamed, saddled horse
        if (!(player.getVehicle() instanceof AbstractHorse horse)) {
            return;
        }

        if (!horse.isTamed() || !horse.isSaddled()) {
            return;
        }

        long cooldownMs = HorseFireballHandler.getCooldownRemaining(horse.getUUID());
        
       

        int cooldownSeconds = (int) Math.ceil(cooldownMs / 1000.0);
        
        // Position at top left corner with some padding
        int x = 8;
        int y = 8;
        int iconSize = 16;

        GuiGraphics guiGraphics = event.getGuiGraphics();
         // Render fire charge item as icon
        net.minecraft.world.item.ItemStack fireballStack = new net.minecraft.world.item.ItemStack(
            net.minecraft.world.item.Items.FIRE_CHARGE
        );

        
         if (cooldownMs <= 0) {
             guiGraphics.renderItem(fireballStack, x, y);
            return; // No cooldown, don't show
        }
        // Set opacity to 75% (192 out of 255)
     
        // guiGraphics.renderItem(fireballStack, x, y); // Renders the icon with the active transparency
  
       
       
        
    }
}
