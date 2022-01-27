package cn.mcmod_mmf.mmlib.data.advancement;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.criterion.ChangeDimensionTrigger;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.item.ItemStack;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class AdvancementBuilders {
    public static Advancement.Builder findItem(String category, ItemStack item, boolean isChallenge, boolean isHidden) {
        FrameType type = isChallenge ? FrameType.CHALLENGE: FrameType.TASK;
        return Advancement.Builder.advancement()
                .display(item, new TranslationTextComponent("advancements."+item.getDescriptionId()+".title"),
                        new TranslationTextComponent("advancements."+item.getDescriptionId()+".desc"),
                        (ResourceLocation) null, type, false, false, false)
                .addCriterion("item", InventoryChangeTrigger.Instance.hasItems(item.getItem()));
    }
    
    public static Advancement.Builder changeDim(String name, ItemStack item, RegistryKey<World> dim, boolean isChallenge, boolean isHidden) {
        FrameType type = isChallenge ? FrameType.CHALLENGE: FrameType.TASK;
        return Advancement.Builder.advancement()
                .display(item, new TranslationTextComponent("advancements."+ name +".title"),
                        new TranslationTextComponent("advancements."+ name +".desc"),
                        (ResourceLocation) null, type, false, false, false)
                .addCriterion("entered_dim", ChangeDimensionTrigger.Instance.changedDimensionTo(dim));
    }
    
    public static Advancement.Builder customTrigger(String name, ItemStack item, ICriterionInstance trigger, boolean isChallenge, boolean isHidden) {
        FrameType type = isChallenge ? FrameType.CHALLENGE: FrameType.TASK;
        return Advancement.Builder.advancement()
                .display(item, new TranslationTextComponent("advancements."+ name +".title"),
                        new TranslationTextComponent("advancements."+ name +".desc"),
                        (ResourceLocation) null, type, false, false, false)
                .addCriterion("custom_trigger", trigger);
    }
}
