package akasha.questui.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraftforge.common.MinecraftForge;

import akasha.questui.client.data.ImageSources;
import akasha.questui.client.gfx.Textures;

/** Client wiring: screen interception and texture (re)loading. */
public final class ClientSetup {

    private ClientSetup() {}

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new ScreenInterceptor());
        MinecraftForge.EVENT_BUS.register(new HudFilter());
        Textures.load();
        ((IReloadableResourceManager) Minecraft.getMinecraft()
            .getResourceManager()).registerReloadListener(new IResourceManagerReloadListener() {

                @Override
                public void onResourceManagerReload(IResourceManager manager) {
                    // generated textures are reloaded by the texture manager; chapter images are cached here
                    ImageSources.clear();
                }
            });
    }
}
