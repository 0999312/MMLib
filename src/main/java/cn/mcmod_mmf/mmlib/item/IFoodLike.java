package cn.mcmod_mmf.mmlib.item;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;

import cn.mcmod_mmf.mmlib.item.info.FoodInfo;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

public interface IFoodLike {
	public FoodInfo getFoodInfo();

	public boolean shouldAddEffectTooltips();

	public default void addEffectTooltips(List<Component> tooltips) {
		List<Pair<Supplier<MobEffectInstance>, Float>> effectList = this.getFoodInfo().getEffects();
		if (effectList.isEmpty()) {
			tooltips.add(Component.translatable("effect.none").withStyle(ChatFormatting.GRAY));
			return;
		}
		List<Pair<Attribute, AttributeModifier>> attributeList = Lists.newArrayList();
		for (Pair<Supplier<MobEffectInstance>, Float> effectPair : effectList) {
			Supplier<MobEffectInstance> instance = effectPair.getFirst();
			MutableComponent iformattabletextcomponent = Component.translatable(instance.get().getDescriptionId());
			MobEffect effect = instance.get().getEffect();
			Map<Attribute, AttributeModifier> attributeMap = effect.getAttributeModifiers();
			if (!attributeMap.isEmpty()) {
				for (Map.Entry<Attribute, AttributeModifier> entry : attributeMap.entrySet()) {
					AttributeModifier rawModifier = entry.getValue();
					AttributeModifier modifier = new AttributeModifier(rawModifier.getName(),
							effect.getAttributeModifierValue(instance.get().getAmplifier(), rawModifier),
							rawModifier.getOperation());
					attributeList.add(new Pair<>(entry.getKey(), modifier));
				}
			}

			if (instance.get().getAmplifier() > 0) {
				iformattabletextcomponent = Component.translatable("potion.withAmplifier", iformattabletextcomponent,
						Component.translatable("potion.potency." + instance.get().getAmplifier()));
			}

			if (instance.get().getDuration() > 20) {
				iformattabletextcomponent = Component.translatable("potion.withDuration", iformattabletextcomponent,
						MobEffectUtil.formatDuration(instance.get(), 1.0F));
			}

			tooltips.add(iformattabletextcomponent.withStyle(effect.getCategory().getTooltipFormatting()));
		}

		if (!attributeList.isEmpty()) {
			tooltips.add(CommonComponents.EMPTY);
			tooltips.add((Component.translatable("potion.whenDrank")).withStyle(ChatFormatting.DARK_PURPLE));

			for (Pair<Attribute, AttributeModifier> pair : attributeList) {
				AttributeModifier modifier = pair.getSecond();
				double amount = modifier.getAmount();
				double formattedAmount;
				if (modifier.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE
						&& modifier.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
					formattedAmount = modifier.getAmount();
				} else {
					formattedAmount = modifier.getAmount() * 100.0D;
				}

				if (amount > 0.0D) {
					tooltips.add((Component.translatable("attribute.modifier.plus." + modifier.getOperation().toValue(),
							ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(formattedAmount),
							Component.translatable(pair.getFirst().getDescriptionId())))
							.withStyle(ChatFormatting.BLUE));
				} else if (amount < 0.0D) {
					formattedAmount = formattedAmount * -1.0D;
					tooltips.add((Component.translatable("attribute.modifier.take." + modifier.getOperation().toValue(),
							ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(formattedAmount),
							Component.translatable(pair.getFirst().getDescriptionId()))).withStyle(ChatFormatting.RED));
				}
			}
		}
	}
}
