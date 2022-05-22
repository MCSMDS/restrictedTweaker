package com.mcsmds.restrictedtweaker.kubejs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import dev.latvian.kubejs.KubeJS;
import dev.latvian.kubejs.event.EventJS;
import dev.latvian.kubejs.item.ItemStackJS;
import dev.latvian.kubejs.item.ingredient.IngredientJS;
import dev.latvian.kubejs.item.ingredient.VanillaIngredientWrapper;
import dev.latvian.kubejs.util.ID;
import dev.latvian.kubejs.util.JsonUtilsJS;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.registries.IForgeRegistry;

public class RTCraftingTableRecipeEventJS extends EventJS {

	private IForgeRegistry<IRecipe> registry;

	public RTCraftingTableRecipeEventJS(IForgeRegistry<IRecipe> registry) {
		this.registry = registry;
	}

	private static String appendModId(String id) {
		return id.indexOf(':') == -1 ? ("restrictedtweaker" + ":" + id) : id;
	}

	public void addShaped(String recipeID, Object output, String[] pattern, Map<String, Object> ingredients,
			List<String> classname) {
		String id = appendModId(recipeID);
		ItemStack outputItem = ItemStackJS.of(output).getItemStack();
		if (outputItem.isEmpty()) {
			KubeJS.LOGGER.warn("Recipe " + id + " has broken output!");
			return;
		}
		int w = pattern[0].length();
		int h = pattern.length;
		NonNullList<Ingredient> ingredientList = NonNullList.withSize(w * h, Ingredient.EMPTY);
		Map<Character, Ingredient> ingredientMap = new HashMap<>();
		for (Map.Entry<String, Object> entry : ingredients.entrySet()) {
			IngredientJS i = IngredientJS.of(entry.getValue());
			if (!i.isEmpty() && !entry.getKey().isEmpty()) {
				ingredientMap.put(entry.getKey().charAt(0), new VanillaIngredientWrapper(i));
			}
		}
		boolean errored = true;
		for (int i = 0; i < w * h; i++) {
			Ingredient in = ingredientMap.get(pattern[i / w].charAt(i % w));
			if (in != null) {
				ingredientList.set(i, in);
				errored = false;
			}
		}
		if (errored) {
			KubeJS.LOGGER.warn("Recipe " + id + " has broken items! Check the pattern/ingredient list.");
		} else {
			RTShapedRecipes r = new RTShapedRecipes(id, w, h, ingredientList, outputItem, classname);
			r.setRegistryName(new ResourceLocation(id));
			registry.register(r);
		}
	}

	public void addShaped(Object output, String[] pattern, Map<String, Object> ingredients,
			List<String> classname) {
		addShaped(output instanceof String ? output.toString() : String.valueOf(output).replaceAll("\\W", "_"),
				output, pattern, ingredients, classname);
	}

	public void addShapeless(String recipeID, Object output, Object[] ingredients,
			List<String> classname) {
		String id = appendModId(recipeID);
		ItemStack outputItem = ItemStackJS.of(output).getItemStack();
		if (outputItem.isEmpty()) {
			KubeJS.LOGGER.warn("Recipe " + id + " has broken output!");
			return;
		}
		NonNullList<Ingredient> ingredientList = NonNullList.create();
		for (Object ingredient : ingredients) {
			IngredientJS i = IngredientJS.of(ingredient);

			if (!i.isEmpty()) {
				ingredientList.add(new VanillaIngredientWrapper(i));
			}
		}
		if (ingredientList.isEmpty()) {
			KubeJS.LOGGER.warn("Recipe " + id + " has broken items! Check the ingredient list.");
		} else {
			RTShapelessRecipes r = new RTShapelessRecipes(id, outputItem, ingredientList, classname);
			r.setRegistryName(new ResourceLocation(id));
			registry.register(r);
		}
	}

	public void addShapeless(Object output, Object[] ingredients,
			List<String> classname) {
		addShapeless(output instanceof String ? output.toString() : String.valueOf(output).replaceAll("\\W", "_"),
				output, ingredients, classname);
	}

	public void add(String recipeID, Object recipe,
			List<String> classname) {
		ID id = ID.of(appendModId(recipeID));
		JsonElement e = JsonUtilsJS.of(recipe);
		if (!e.isJsonObject()) {
			KubeJS.LOGGER.warn("Recipe " + id + " is not an object!");
			return;
		}
		JsonObject o = e.getAsJsonObject();
		if (!o.has("type")) {
			KubeJS.LOGGER.warn("Recipe " + id + " doesn't have type!");
			return;
		}
		String type = o.get("type").getAsString();
		if (type.equals("minecraft:crafting_shaped") && o.has("key")) {
			JsonObject object = o.get("key").getAsJsonObject();
			for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
				object.add(entry.getKey(), fixIngredient(entry.getValue()));
			}
		} else if (type.equals("minecraft:crafting_shapeless") && o.has("ingredients")) {
			JsonArray array = o.get("ingredients").getAsJsonArray();
			for (int i = 0; i < array.size(); i++) {
				array.set(i, fixIngredient(array.get(i)));
			}
		}
		try {
			IRecipe r = CraftingHelper.getRecipe(o, new JsonContext(KubeJS.MOD_ID));
			if (r != null) {
				if (r instanceof ShapedRecipes) {
					ShapedRecipes recipes = (ShapedRecipes) r;
					String group = recipes.getGroup();
					int width = recipes.getRecipeWidth();
					int height = recipes.getRecipeHeight();
					NonNullList<Ingredient> ingredients = recipes.getIngredients();
					ItemStack result = recipes.getRecipeOutput();
					registry.register(new RTShapedRecipes(group, width, height, ingredients, result, classname)
							.setRegistryName(id.mc()));
				} else if (r instanceof ShapelessRecipes) {
					ShapelessRecipes recipes = (ShapelessRecipes) r;
					String group = recipes.getGroup();
					ItemStack result = recipes.getRecipeOutput();
					NonNullList<Ingredient> ingredients = recipes.getIngredients();
					registry.register(new RTShapelessRecipes(group, result, ingredients, classname)
							.setRegistryName(id.mc()));
				}
			}
		} catch (Exception ex) {
			KubeJS.LOGGER.warn("Failed to load a recipe with id '" + recipeID + "'!");
			ex.printStackTrace();
		}
	}

	private JsonElement fixIngredient(JsonElement element) {
		if (element.isJsonPrimitive()) {
			String s = element.getAsString();
			if (s.startsWith("ore:")) {
				JsonObject object = new JsonObject();
				object.addProperty("type", "forge:ore_dict");
				object.addProperty("ore", s.substring(4));
				return object;
			} else {
				JsonObject object = new JsonObject();
				object.addProperty("item", s);
				return object;
			}
		}
		return element;
	}

}
