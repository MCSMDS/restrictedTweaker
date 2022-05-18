package com.mcsmds.restrictedtweaker;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = "restrictedtweaker")
public class RestrictedTweaker {

    @Config(modid = "restrictedtweaker")
    public static class Configs {
        public static boolean debug;
    }

    @Mod.EventBusSubscriber
    public static class MyStaticClientOnlyEventHandler {
        @SubscribeEvent
        public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals("restrictedtweaker")) {
                ConfigManager.sync("restrictedtweaker", Config.Type.INSTANCE);
            }
        }

        @SubscribeEvent
        public static void displayGui(PlayerContainerEvent.Open event) {
            String guiName = event.getContainer().getClass().getName();
            if (guiName.indexOf("$") != -1)
                guiName = guiName.substring(0, guiName.indexOf("$"));
            DebounceDebug.debug("Alpha", guiName);
        }
    }

}