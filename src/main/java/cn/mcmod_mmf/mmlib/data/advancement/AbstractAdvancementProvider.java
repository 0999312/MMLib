package cn.mcmod_mmf.mmlib.data.advancement;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.logging.log4j.Logger;

import com.google.common.collect.Sets;
import com.google.gson.Gson;

import net.minecraft.advancements.Advancement;
import net.minecraft.data.AdvancementProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractAdvancementProvider extends AdvancementProvider {

    private final DataGenerator using_generator;
    
    public AbstractAdvancementProvider(DataGenerator gen) {
        super(gen);
        this.using_generator = gen;
    }
    
    public abstract Logger getLogger();

    public abstract Gson getGSONInstance();

    public abstract Consumer<Consumer<Advancement>>[] getAdvancementTabs();

    @Override
    public void run(DirectoryCache cache) throws IOException {
        Path path = this.using_generator.getOutputFolder();
        Set<ResourceLocation> set = Sets.newHashSet();
        Consumer<Advancement> consumer = (advancement) -> {
           if (!set.add(advancement.getId())) {
              throw new IllegalStateException("Duplicate advancement " + advancement.getId());
           } else {
              Path path1 = createNewPath(path, advancement);

              try {
                 IDataProvider.save(this.getGSONInstance(), cache, advancement.deconstruct().serializeToJson(), path1);
              } catch (IOException ioexception) {
                 this.getLogger().error("Couldn't save advancement {}", path1, ioexception);
              }

           }
        };

        for(Consumer<Consumer<Advancement>> consumer1 : this.getAdvancementTabs()) {
           consumer1.accept(consumer);
        }
    }
    
    public static Path createNewPath(Path path, Advancement advancement) {
        return path.resolve("data/" + advancement.getId().getNamespace() + "/advancements/" + advancement.getId().getPath() + ".json");
    }
}
