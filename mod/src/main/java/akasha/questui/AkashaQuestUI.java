package akasha.questui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;

/**
 * FTB Quests style interface for BetterQuesting. Client only: on a dedicated server the mod does nothing, and
 * servers never need it installed (acceptableRemoteVersions = "*").
 */
@Mod(
    modid = AkashaQuestUI.MODID,
    name = "Akasha Quests UI",
    version = Tags.VERSION,
    dependencies = "required-after:betterquesting",
    acceptableRemoteVersions = "*")
public class AkashaQuestUI {

    public static final String MODID = "akashaquestui";
    public static final Logger LOG = LogManager.getLogger("AkashaQuestUI");

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        if (FMLCommonHandler.instance()
            .getSide() != Side.CLIENT) return;
        Config.load(event.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if (FMLCommonHandler.instance()
            .getSide() != Side.CLIENT) return;
        akasha.questui.client.ClientSetup.init();
    }
}
