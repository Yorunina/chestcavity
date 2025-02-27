package net.tigereye.chestcavity.registration;

import com.mojang.blaze3d.platform.InputConstants.Type;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class CCKeybindings {
    private static final String ORGAN_ABILITY_KEY_CATEGORY = "organ_abilities";
    public static ResourceLocation UTILITY_ABILITIES_ID = new ResourceLocation("chestcavity", "utility_abilities");
    public static KeyMapping UTILITY_ABILITIES;
    public static List<ResourceLocation> UTILITY_ABILITY_LIST;
    public static ResourceLocation ATTACK_ABILITIES_ID;
    public static KeyMapping ATTACK_ABILITIES;
    public static List<ResourceLocation> ATTACK_ABILITY_LIST;
    public static KeyMapping CREEPY;
    public static KeyMapping DRAGON_BREATH;
    public static KeyMapping BUOYANT_EXHALE;
    public static KeyMapping DRAGON_BOMBS;
    public static KeyMapping FORCEFUL_SPIT;
    public static KeyMapping FURNACE_POWERED;
    public static KeyMapping IRON_REPAIR;
    public static KeyMapping PYROMANCY;
    public static KeyMapping GHASTLY;
    public static KeyMapping GRAZING;
    public static KeyMapping SHULKER_BULLETS;
    public static KeyMapping SILK;

    public CCKeybindings() {
    }

    public static void init() {
    }

    public static KeyMapping register(ResourceLocation id, String category, int defaultKey) {
        String namespace = id.getNamespace();
        return new KeyMapping("key." + namespace + "." + id.getPath(),
                Type.KEYSYM, defaultKey,
                "category." + namespace + "." + category);
    }

    public static KeyMapping register(ResourceLocation id, String category, int defaultKey, boolean isAttack) {
        if (isAttack) {
            ATTACK_ABILITY_LIST.add(id);
        } else {
            UTILITY_ABILITY_LIST.add(id);
        }

        return register(id, category, defaultKey);
    }

    static {
        UTILITY_ABILITIES = register(UTILITY_ABILITIES_ID, "organ_abilities", 86);
        UTILITY_ABILITY_LIST = new ArrayList();
        ATTACK_ABILITIES_ID = new ResourceLocation("chestcavity", "attack_abilities");
        ATTACK_ABILITIES = register(ATTACK_ABILITIES_ID, "organ_abilities", 82);
        ATTACK_ABILITY_LIST = new ArrayList();
        CREEPY = register(CCOrganScores.CREEPY, "organ_abilities", GLFW.GLFW_KEY_UNKNOWN, true);
        BUOYANT_EXHALE = register(CCOrganScores.BUOYANT, ORGAN_ABILITY_KEY_CATEGORY, GLFW.GLFW_KEY_UNKNOWN, false);
        DRAGON_BREATH = register(CCOrganScores.DRAGON_BREATH, "organ_abilities", GLFW.GLFW_KEY_UNKNOWN, true);
        DRAGON_BOMBS = register(CCOrganScores.DRAGON_BOMBS, "organ_abilities", GLFW.GLFW_KEY_UNKNOWN, true);
        FORCEFUL_SPIT = register(CCOrganScores.FORCEFUL_SPIT, "organ_abilities", GLFW.GLFW_KEY_UNKNOWN, true);
        FURNACE_POWERED = register(CCOrganScores.FURNACE_POWERED, "organ_abilities", GLFW.GLFW_KEY_UNKNOWN, false);
        IRON_REPAIR = register(CCOrganScores.IRON_REPAIR, "organ_abilities", GLFW.GLFW_KEY_UNKNOWN, false);
        PYROMANCY = register(CCOrganScores.PYROMANCY, "organ_abilities", GLFW.GLFW_KEY_UNKNOWN, true);
        GHASTLY = register(CCOrganScores.GHASTLY, "organ_abilities", GLFW.GLFW_KEY_UNKNOWN, true);
        GRAZING = register(CCOrganScores.GRAZING, "organ_abilities", GLFW.GLFW_KEY_UNKNOWN, false);
        SHULKER_BULLETS = register(CCOrganScores.SHULKER_BULLETS, "organ_abilities", GLFW.GLFW_KEY_UNKNOWN, true);
    }
}
