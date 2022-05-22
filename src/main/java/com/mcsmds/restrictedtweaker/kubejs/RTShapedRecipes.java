package com.mcsmds.restrictedtweaker.kubejs;

import java.util.List;

import com.mcsmds.restrictedtweaker.api.RTRecipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;

public class RTShapedRecipes extends ShapedRecipes {

    private List<String> classname;

    public RTShapedRecipes(String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result,
            List<String> classname) {
        super(group, width, height, ingredients, result);
        this.classname = classname;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return RTRecipe.restrictedContainer(super.getCraftingResult(inv), classname);
    }

}