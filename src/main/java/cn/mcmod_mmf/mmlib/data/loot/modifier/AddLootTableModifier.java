package cn.mcmod_mmf.mmlib.data.loot.modifier;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import javax.annotation.Nonnull;
import java.util.List;

/**
 * Credits to Commoble for this implementation!
 */
public class AddLootTableModifier extends LootModifier {
    private final ResourceLocation lootTable;

    public AddLootTableModifier(LootItemCondition[] conditionsIn, ResourceLocation lootTable) {
        super(conditionsIn);
        this.lootTable = lootTable;
    }
    
    public boolean canApplyModifier() {
        return true;
    }
    
    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        if(this.canApplyModifier()) {
            LootTable extraTable = context.getLootTable(this.lootTable);
            extraTable.getRandomItemsRaw(context, LootTable.createStackSplitter(generatedLoot::add));
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<AddLootTableModifier> {
        @Override
        public AddLootTableModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] conditions) {
            ResourceLocation lootTable = new ResourceLocation(GsonHelper.getAsString(object, "lootTable"));
            return new AddLootTableModifier(conditions, lootTable);
        }

        @Override
        public JsonObject write(AddLootTableModifier instance) {
            JsonObject object = this.makeConditions(instance.conditions);
            object.addProperty("lootTable", instance.lootTable.toString());
            return object;
        }
    }
}
