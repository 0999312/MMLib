package cn.mcmod_mmf.mmlib.data.loot;

import java.util.List;
import java.util.Set;

import org.apache.commons.compress.utils.Lists;

import com.google.common.collect.Sets;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;

public class LootTableProviderBuilder {
    private Set<ResourceLocation> requiredTables = Sets.newHashSet();
    private List<LootTableProvider.SubProviderEntry> subProviders = Lists.newArrayList();
    
    private LootTableProviderBuilder() {
    }
    
    public static LootTableProviderBuilder create() {
        return new LootTableProviderBuilder();
    }
    
    public void addRequireTable(ResourceLocation loc) {
        this.requiredTables.add(loc);
    }
    
    public void addRequireTables(ResourceLocation... locs) {
        for(ResourceLocation loc : locs)
            this.addRequireTable(loc);
    }
    
    public void setRequireTables(Set<ResourceLocation> set) {
        this.requiredTables = set;
    }
    
    public void addSubProvider(LootTableProvider.SubProviderEntry entry) {
        this.subProviders.add(entry);
    }
    
    public void addSubProvider(LootTableProvider.SubProviderEntry... entrys) {
        for(LootTableProvider.SubProviderEntry entry : entrys)
            this.addSubProvider(entry);
    }
    
    public LootTableProvider build(PackOutput output) {
        return new LootTableProvider(
                output, 
                this.getRequiredTables(), 
                this.getSubProviders()
                );
    }

    public Set<ResourceLocation> getRequiredTables() {
        return requiredTables;
    }

    public List<LootTableProvider.SubProviderEntry> getSubProviders() {
        return subProviders;
    }
}
