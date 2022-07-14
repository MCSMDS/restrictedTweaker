package com.mcsmds.restrictedtweaker.api;

import java.util.List;

import com.mcsmds.restrictedtweaker.DebounceDebug;

import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class RTRecipe {

    public static ItemStack restrictedContainer(ItemStack original, List<String> classname) {
        String container = new Throwable().getStackTrace()[2].getClassName();
        if (container.equals(Container.class.getName())) {
            container = new Throwable().getStackTrace()[3].getClassName();
        }
        if (classname.contains(container)) {
            return original;
        }
        DebounceDebug.debug(container);
        return ItemStack.EMPTY;
    }

}
