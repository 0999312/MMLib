package cn.mcmod_mmf.mmlib.data.advancement;

import java.util.function.Consumer;

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

public class AbstractAdvancements implements Consumer<Consumer<Advancement>>{

    @Override
    public void accept(Consumer<Advancement> comsumer) {
        
    }
    
    public static Advancement.Builder getRoot(ItemStack display, ResourceLocation bg, FrameType frame, boolean showToast, boolean announceToChat, boolean hidden) {
        return Advancement.Builder.advancement().display(display,
                new TranslatableComponent("advancement."+display.getItem().getRegistryName().getNamespace()+".root.title"),
                new TranslatableComponent("advancement."+display.getItem().getRegistryName().getNamespace()+".root.desc"),
                bg, frame, showToast, announceToChat, hidden);
    }
    
    public static Advancement.Builder getAdvancement(Advancement parent, ItemStack display, String name, FrameType frame, boolean showToast, boolean announceToChat, boolean hidden) {
        return Advancement.Builder.advancement().parent(parent).display(display,
                new TranslatableComponent("advancement."+display.getItem().getRegistryName().getNamespace()+'.'+name + ".title"),
                new TranslatableComponent("advancement."+display.getItem().getRegistryName().getNamespace()+'.'+name + ".desc"),
                null, frame, showToast, announceToChat, hidden);
    }
    
    public static Advancement.Builder findItem(Advancement.Builder builder, ItemStack item) {
        return builder.addCriterion("item", InventoryChangeTrigger.TriggerInstance.hasItems(item.getItem()));
    }

    public static Advancement.Builder changeDim(Advancement.Builder builder, ResourceKey<Level> dim) {
        return builder.addCriterion("entered_dim", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(dim));
    }

    public static Advancement.Builder customTrigger(Advancement.Builder builder, Criterion trigger) {
        return builder.addCriterion("custom_trigger", trigger);
    }
}
