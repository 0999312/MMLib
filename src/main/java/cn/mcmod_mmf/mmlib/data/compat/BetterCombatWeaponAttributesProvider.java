package cn.mcmod_mmf.mmlib.data.compat;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.compress.utils.Lists;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class BetterCombatWeaponAttributesProvider implements DataProvider {

    protected final PackOutput output;
    protected final String modId;
    protected final ExistingFileHelper existingFileHelper;
    private final ExistingFileHelper.IResourceType resourceType;
    protected final Map<ResourceLocation, String> datas = Maps.newLinkedHashMap();
    
    public BetterCombatWeaponAttributesProvider(PackOutput output, ExistingFileHelper existingFileHelper, String modId) {
        this.output = output;
        this.modId = modId;
        this.existingFileHelper = existingFileHelper;
        this.resourceType = new ExistingFileHelper.ResourceType(PackType.SERVER_DATA, ".json", "weapon_attributes");
    }
    
    public void addData(Item item, String attribute) {
        this.datas.computeIfAbsent(ForgeRegistries.ITEMS.getKey(item), loc->{
            existingFileHelper.trackGenerated(loc, resourceType);
            return attribute;
        });
    }

    private void addDatas() {
        
    }

    @Override
    public String getName() {
        return "SIMPLE Better Combat Weapon Attributes Provider";
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        this.datas.clear();
        this.addDatas();
        final Path outputFolder = output.getOutputFolder();
        List<CompletableFuture<?>> futureList = Lists.newArrayList();
        
        this.datas.forEach( (loc, data) -> {
            String pathString = String.join("/", PackType.SERVER_DATA.getDirectory(), loc.getNamespace(), "weapon_attributes", loc.getPath()+".json");
            Path path = outputFolder.resolve(pathString);
            
            JsonObject jsonObj = new JsonObject();
            jsonObj.addProperty("parent", data);
            
            futureList.add(DataProvider.saveStable(cache, jsonObj, path));
        });
        return CompletableFuture.allOf(futureList.stream().toArray(CompletableFuture<?>[]::new));
    }

}
