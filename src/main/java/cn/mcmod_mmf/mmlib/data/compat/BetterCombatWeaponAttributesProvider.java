package cn.mcmod_mmf.mmlib.data.compat;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;

import cn.mcmod_mmf.mmlib.utils.DataGenUtil;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BetterCombatWeaponAttributesProvider implements DataProvider {

    protected final DataGenerator generator;
    protected final String modId;
    protected final ExistingFileHelper existingFileHelper;
    private final ExistingFileHelper.IResourceType resourceType;
    protected final Map<ResourceLocation, String> datas = Maps.newLinkedHashMap();
    
    public BetterCombatWeaponAttributesProvider(DataGenerator generator, ExistingFileHelper existingFileHelper, String modId) {
        this.generator = generator;
        this.modId = modId;
        this.existingFileHelper = existingFileHelper;
        this.resourceType = new ExistingFileHelper.ResourceType(PackType.SERVER_DATA, ".json", "weapon_attributes");
    }
    
    public void addData(Item item, String attribute) {
        this.datas.computeIfAbsent(item.getRegistryName(), loc->{
            existingFileHelper.trackGenerated(loc, resourceType);
            return attribute;
        });
    }
    
    @Override
    public void run(HashCache cache) throws IOException {
        this.datas.clear();
        this.addDatas();
        final Path outputFolder = generator.getOutputFolder();
        this.datas.forEach( (loc, data) -> {
            String pathString = String.join("/", PackType.SERVER_DATA.getDirectory(), loc.getNamespace(), "weapon_attributes", loc.getPath()+".json");
            Path path = outputFolder.resolve(pathString);
            
            JsonObject jsonObj = new JsonObject();
            jsonObj.addProperty("parent", data);
            
            try {
                DataProvider.save(DataGenUtil.DATA_GSON, cache, jsonObj, path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        
    }

    private void addDatas() {
        
    }

    @Override
    public String getName() {
        return "SIMPLE Better Combat Weapon Attributes Provider";
    }

}
