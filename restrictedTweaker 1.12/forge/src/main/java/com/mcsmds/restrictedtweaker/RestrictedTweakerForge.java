package com.mcsmds.restrictedtweaker;

import com.mcsmds.restrictedtweaker.kubejs.RTCraftingEventHandler;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = RestrictedTweaker.MODID)
public class RestrictedTweakerForge {

    @Config(modid = RestrictedTweaker.MODID)
    public static class Configs {
        public static boolean debug;
    }

    @Mod.EventBusSubscriber
    public static class MyStaticClientOnlyEventHandler {

        @SubscribeEvent
        public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(RestrictedTweaker.MODID)) {
                ConfigManager.sync(RestrictedTweaker.MODID, Config.Type.INSTANCE);
            }
        }

        @SubscribeEvent(priority = EventPriority.LOW)
        public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
            try {
                Thread.currentThread().getContextClassLoader().loadClass("dev.latvian.kubejs.KubeJS");
                RTCraftingEventHandler.registerRecipes(event);
            } catch (ClassNotFoundException e) {
            }
        }

    }

}