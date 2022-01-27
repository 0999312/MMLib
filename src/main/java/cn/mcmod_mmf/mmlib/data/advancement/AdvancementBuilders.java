package cn.mcmod_mmf.mmlib.data.advancement;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AdvancementBuilders {
    public static Advancement.Builder findItem(String category, ItemStack item, boolean isChallenge, boolean isHidden) {
        FrameType type = isChallenge ? FrameType.CHALLENGE : FrameType.TASK;
        return Advancement.Builder.advancement()
                .display(item, new TranslatableComponent("advancements." + item.getDescriptionId() + ".title"),
                        new TranslatableComponent("advancements." + item.getDescriptionId() + ".desc"),
                        (ResourceLocation) null, type, false, false, false)
                .addCriterion("item", InventoryChangeTrigger.TriggerInstance.hasItems(item.getItem()));
    }

    public static Advancement.Builder changeDim(String name, ItemStack item, ResourceKey<Level> dim,
            boolean isChallenge, boolean isHidden) {
        FrameType type = isChallenge ? FrameType.CHALLENGE : FrameType.TASK;
        return Advancement.Builder.advancement()
                .display(item, new TranslatableComponent("advancements." + name + ".title"),
                        new TranslatableComponent("advancements." + name + ".desc"), (ResourceLocation) null, type,
                        false, false, false)
                .addCriterion("entered_dim", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(dim));
    }

    public static Advancement.Builder customTrigger(String name, ItemStack item, Criterion trigger, boolean isChallenge,
            boolean isHidden) {
        FrameType type = isChallenge ? FrameType.CHALLENGE : FrameType.TASK;
        return Advancement.Builder.advancement()
                .display(item, new TranslatableComponent("advancements." + name + ".title"),
                        new TranslatableComponent("advancements." + name + ".desc"), (ResourceLocation) null, type,
                        false, false, false)
                .addCriterion("custom_trigger", trigger);
    }
}
