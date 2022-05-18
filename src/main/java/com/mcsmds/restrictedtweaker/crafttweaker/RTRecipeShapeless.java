package com.mcsmds.restrictedtweaker.crafttweaker;

import java.util.List;

import com.mcsmds.restrictedtweaker.DebounceDebug;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.recipes.IRecipeAction;
import crafttweaker.api.recipes.IRecipeFunction;
import crafttweaker.mc1120.recipes.MCRecipeShapeless;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class RTRecipeShapeless extends MCRecipeShapeless {

    private List<String> classname;
    private MCRecipeShapeless original;

    public RTRecipeShapeless(IIngredient[] ingredients, IItemStack output, List<String> classname,
            IRecipeFunction recipeFunction, IRecipeAction recipeAction, boolean isHidden) {
        super(ingredients, output, recipeFunction, recipeAction, isHidden);
        this.classname = classname;
        this.original = new MCRecipeShapeless(ingredients, output, recipeFunction, recipeAction, isHidden);
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        String container = new Throwable().getStackTrace()[1].getClassName();
        if (classname.contains(container)) {
            return original.getCraftingResult(inv);
        }
        DebounceDebug.debug("Beta",container);
        return ItemStack.EMPTY;
    }

}
