package com.mcsmds.restrictedtweaker.crafttweaker;

import java.util.List;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.recipes.IRecipeAction;
import crafttweaker.api.recipes.IRecipeFunction;
import crafttweaker.mc1120.recipes.MCRecipeBase;
import crafttweaker.mc1120.recipes.MCRecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("restrictedtweaker.restrictedRecipe")
public class RTRecipeManager {

    @ZenMethod
    public static void addShaped(IItemStack output, IIngredient[][] ingredients,
            List<String> classname, @Optional IRecipeFunction function, @Optional IRecipeAction action) {
        MCRecipeManager.recipesToAdd
                .add(new RTActionAddShapedRecipe(output, ingredients, classname, function, action, false, false));
    }

    @ZenMethod
    public static void addShaped(String name, IItemStack output, IIngredient[][] ingredients,
            List<String> classname, @Optional IRecipeFunction function, @Optional IRecipeAction action) {
        MCRecipeManager.recipesToAdd
                .add(new RTActionAddShapedRecipe(name, output, ingredients, classname, function, action, false, false));
    }

    @ZenMethod
    public static void addShapedMirrored(IItemStack output, IIngredient[][] ingredients,
            List<String> classname, @Optional IRecipeFunction function, @Optional IRecipeAction action) {
        MCRecipeManager.recipesToAdd
                .add(new RTActionAddShapedRecipe(output, ingredients, classname, function, action, true, false));
    }

    @ZenMethod
    public static void addShapedMirrored(String name, IItemStack output, IIngredient[][] ingredients,
            List<String> classname, @Optional IRecipeFunction function, @Optional IRecipeAction action) {
        MCRecipeManager.recipesToAdd
                .add(new RTActionAddShapedRecipe(name, output, ingredients, classname, function, action, true, false));
    }

    @ZenMethod
    public static void addShapeless(IItemStack output, IIngredient[] ingredients,
            List<String> classname, @Optional IRecipeFunction function, @Optional IRecipeAction action) {
        if (checkShapelessNulls(output, ingredients))
            return;
        MCRecipeManager.recipesToAdd
                .add(new RTActionAddShapelessRecipe(output, ingredients, classname, function, action));
    }

    @ZenMethod
    public static void addShapeless(String name, IItemStack output, IIngredient[] ingredients,
            List<String> classname, @Optional IRecipeFunction function, @Optional IRecipeAction action) {
        if (checkShapelessNulls(output, ingredients))
            return;
        MCRecipeManager.recipesToAdd
                .add(new RTActionAddShapelessRecipe(name, output, ingredients, classname, function, action, false));
    }

    @ZenMethod
    public static void addHiddenShaped(String name, IItemStack output, IIngredient[][] ingredients,
            List<String> classname, @Optional IRecipeFunction function, @Optional IRecipeAction action,
            @Optional boolean mirrored) {
        MCRecipeManager.recipesToAdd.add(
                new RTActionAddShapedRecipe(name, output, ingredients, classname, function, action, mirrored, true));
    }

    @ZenMethod
    public static void addHiddenShapeless(String name, IItemStack output, IIngredient[] ingredients,
            List<String> classname, @Optional IRecipeFunction function, @Optional IRecipeAction action) {
        if (checkShapelessNulls(output, ingredients))
            return;
        MCRecipeManager.recipesToAdd
                .add(new RTActionAddShapelessRecipe(name, output, ingredients, classname, function, action, true));
    }

    private static boolean checkShapelessNulls(IItemStack output, IIngredient[] ingredients) {
        boolean valid = output != null;
        for (IIngredient ing : ingredients) {
            if (ing == null) {
                valid = false;
                break;
            }
        }
        if (!valid) {
            CraftTweakerAPI.logError("Null not allowed in shapeless recipes! Recipe for: " + output + " not created!");
            return true;
        }
        return false;
    }

    private static class RTActionAddShapedRecipe extends RTActionBaseAddRecipe {
        public RTActionAddShapedRecipe(IItemStack output, IIngredient[][] ingredients,
                List<String> classname,
                IRecipeFunction function, IRecipeAction action, boolean mirrored, boolean hidden) {
            this(null, output, ingredients, classname, function, action, mirrored, hidden);
        }

        public RTActionAddShapedRecipe(String name, IItemStack output, IIngredient[][] ingredients,
                List<String> classname,
                IRecipeFunction function, IRecipeAction action, boolean mirrored, boolean hidden) {
            super(new RTRecipeShaped(ingredients, output, classname, function, action, mirrored, hidden), output, true);
            setName(name);
        }
    }

    private static class RTActionAddShapelessRecipe extends RTActionBaseAddRecipe {
        public RTActionAddShapelessRecipe(IItemStack output, IIngredient[] ingredients,
                List<String> classname,
                @Optional IRecipeFunction function, @Optional IRecipeAction action) {
            this(null, output, ingredients, classname, function, action, false);
        }

        public RTActionAddShapelessRecipe(String name, IItemStack output, IIngredient[] ingredients,
                List<String> classname,
                @Optional IRecipeFunction function, @Optional IRecipeAction action,
                boolean hidden) {
            super(new RTRecipeShapeless(ingredients, output, classname, function, action, hidden), output, false);
            setName(name);
        }
    }

    private static class RTActionBaseAddRecipe extends MCRecipeManager.ActionBaseAddRecipe {
        @SuppressWarnings("deprecation")
        private RTActionBaseAddRecipe(MCRecipeBase recipe, IItemStack output, boolean isShaped) {
            this.recipe = recipe;
            this.output = output;
            this.isShaped = isShaped;
            if (recipe.hasTransformers())
                MCRecipeManager.transformerRecipes.add(recipe);
            if (recipe.hasRecipeAction())
                MCRecipeManager.actionRecipes.add(recipe);
        }

        @Override
        public void apply() {
            ForgeRegistries.RECIPES.register(recipe.setRegistryName(new ResourceLocation("restrictedtweaker", name)));
        }
    }

}
