package github.ssourabh58.vubuihorse;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = VuBuiHorse.MODID, value = Dist.CLIENT)
public class KeyBindings {
    
    // Custom category - translation key will be "key.category.vubuihorse.controls"
    public static final KeyMapping.Category VUBUIHORSE_CATEGORY = new KeyMapping.Category(
        ResourceLocation.fromNamespaceAndPath(VuBuiHorse.MODID, "controls")
    );
    
    public static final String KEY_HORSE_FIREBALL = "key.vubuihorse.horse_fireball";

    public static KeyMapping horseFireballKey;

    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        // IMPORTANT: Register the category FIRST
        event.registerCategory(VUBUIHORSE_CATEGORY);
        
        // Then register the keybind with the custom category
        // Translation: "key.category.vubuihorse.controls" = "VuBui Horse" in en_us.json
        horseFireballKey = new KeyMapping(
            KEY_HORSE_FIREBALL,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            VUBUIHORSE_CATEGORY
        );
        event.register(horseFireballKey);
    }
}
