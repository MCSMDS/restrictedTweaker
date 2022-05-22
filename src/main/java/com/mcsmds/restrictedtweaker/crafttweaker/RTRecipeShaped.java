package com.mcsmds.restrictedtweaker.crafttweaker;

import java.util.List;

import com.mcsmds.restrictedtweaker.api.RTRecipe;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.recipes.IRecipeAction;
import crafttweaker.api.recipes.IRecipeFunction;
import crafttweaker.mc1120.recipes.MCRecipeShaped;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class RTRecipeShaped extends MCRecipeShaped {

    private List<String> classname;
    private MCRecipeShaped original;

    public RTRecipeShaped(IIngredient[][] ingredients, IItemStack output, List<String> classname,
            IRecipeFunction recipeFunction, IRecipeAction recipeAction, boolean isMirrored, boolean isHidden) {
        super(ingredients, output, recipeFunction, recipeAction, isMirrored, isHidden);
        this.classname = classname;
        this.original = new MCRecipeShaped(ingredients, output, recipeFunction, recipeAction, isMirrored, isHidden);
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return RTRecipe.restrictedContainer(original.getCraftingResult(inv), classname);
    }

}