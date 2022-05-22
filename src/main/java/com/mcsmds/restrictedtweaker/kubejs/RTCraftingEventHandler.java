package com.mcsmds.restrictedtweaker.kubejs;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;

public class RTCraftingEventHandler {

    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        RTCraftingTableRecipeEventJS eventjs = new RTCraftingTableRecipeEventJS(event.getRegistry());
        dev.latvian.kubejs.event.EventsJS.post("restrictedtweaker.crafting_table", eventjs);
    }

}
