package cn.mcmod_mmf.mmlib.data.compat;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;

import cn.mcmod_mmf.mmlib.item.IFoodLike;
import cn.mcmod_mmf.mmlib.item.info.FoodInfo;
import cn.mcmod_mmf.mmlib.utils.DataGenUtil;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class TFCFoodDefinitionProvider implements DataProvider {

    protected final DataGenerator generator;
    protected final String modId;
    protected final ExistingFileHelper existingFileHelper;
    private final ExistingFileHelper.IResourceType resourceType;
    protected final Map<ResourceLocation, FoodInfo> datas = Maps.newLinkedHashMap();
    
    public TFCFoodDefinitionProvider(DataGenerator generator, ExistingFileHelper existingFileHelper, String modId) {
        this.generator = generator;
        this.modId = modId;
        this.existingFileHelper = existingFileHelper;
        this.resourceType = new ExistingFileHelper.ResourceType(PackType.SERVER_DATA, ".json", "tfc/food_items");
    }
    
    public void addData(Item item) {
        if(item instanceof IFoodLike food) 
            this.addData(item, food.getFoodInfo());
    }
    
    public void addData(Item item, FoodInfo data) {
        this.datas.computeIfAbsent(item.getRegistryName(), loc->{
            existingFileHelper.trackGenerated(loc, resourceType);
            return data;
        });
    }
    
    @Override
    public void run(HashCache cache) throws IOException {
        this.datas.clear();
        this.addDatas();
        final Path outputFolder = generator.getOutputFolder();
        this.datas.forEach( (loc, data) -> {
            String pathString = String.join("/", PackType.SERVER_DATA.getDirectory(), this.modId, "tfc", "food_items", loc.getPath()+".json");
            Path path = outputFolder.resolve(pathString);
            
            JsonObject jsonObj = new JsonObject();
            jsonObj.add("ingredient", Ingredient.of(ForgeRegistries.ITEMS.getValue(loc)).toJson());
            jsonObj.addProperty("hunger", data.getAmount());
            jsonObj.addProperty("saturation", data.getCalories());
            jsonObj.addProperty("decayModifier", data.getDecayModifier());
            jsonObj.addProperty("water", data.getWater());
            jsonObj.addProperty("grain", data.getNutrients()[0]);
            jsonObj.addProperty("fruit", data.getNutrients()[1]);
            jsonObj.addProperty("vegetables", data.getNutrients()[2]);
            jsonObj.addProperty("protein", data.getNutrients()[3]);
            jsonObj.addProperty("dairy", data.getNutrients()[4]);
            
            try {
                DataProvider.save(DataGenUtil.DATA_GSON, cache, jsonObj, path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        
    }

    public void addDatas() {

    }

    @Override
    public String getName() {
        return "MMLib FoodInfo to TFC FoodDefinition Provider";
    }

}
