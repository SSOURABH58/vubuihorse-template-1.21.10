package github.ssourabh58.vubuihorse;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = VuBuiHorse.MODID, value = Dist.CLIENT)
public class CooldownHudOverlay {
    
    private static final ResourceLocation FIRE_ICON = ResourceLocation.withDefaultNamespace("textures/mob_effect/fire_resistance.png");

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(
            VanillaGuiLayers.HOTBAR,
            ResourceLocation.fromNamespaceAndPath(VuBuiHorse.MODID, "fireball_cooldown"),
            CooldownHudOverlay::renderCooldown
        );
    }

    private static void renderCooldown(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        
        if (player == null) {
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
        
        if (cooldownMs <= 0) {
            return; // No cooldown, don't show
        }

        int cooldownSeconds = (int) Math.ceil(cooldownMs / 1000.0);
        
        // Position at top left (like potion effects)
        int x = 10;
        int y = 10;
        int iconSize = 18;

        // Draw fire icon
        guiGraphics.blit(FIRE_ICON, x, y, 0, 0, iconSize, iconSize, iconSize, iconSize);
        
        // Draw cooldown text
        String cooldownText = String.valueOf(cooldownSeconds);
        int textX = x + iconSize + 4;
        int textY = y + (iconSize / 2) - 4;
        
        guiGraphics.drawString(mc.font, cooldownText + "s", textX, textY, 0xFFFFFF, true);
    }
}
