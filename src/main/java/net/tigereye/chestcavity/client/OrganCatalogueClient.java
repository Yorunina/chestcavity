package net.tigereye.chestcavity.client;

import net.minecraft.client.Minecraft;
import net.tigereye.chestcavity.ui.OrganHoloScreen;

/**
 * Client-only entry point for the organ catalogue item.
 *
 * Keeping all client classes in this class prevents Forge's dedicated-server
 * runtime dist cleaner from resolving client-only types while common item
 * classes are being loaded.
 */
public final class OrganCatalogueClient {
    private OrganCatalogueClient() {
    }

    public static void open() {
        Minecraft.getInstance().setScreen(new OrganHoloScreen());
    }
}
