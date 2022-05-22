package com.mcsmds.restrictedtweaker.kubejs;

import java.util.List;

import com.mcsmds.restrictedtweaker.api.RTRecipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;

public class RTShapelessRecipes extends ShapelessRecipes {

    private List<String> classname;

    public RTShapelessRecipes(String group, ItemStack output, NonNullList<Ingredient> ingredients,
            List<String> classname) {
        super(group, output, ingredients);
        this.classname = classname;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return RTRecipe.restrictedContainer(super.getCraftingResult(inv), classname);
    }

}