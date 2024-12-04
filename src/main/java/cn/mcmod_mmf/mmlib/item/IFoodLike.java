package cn.mcmod_mmf.mmlib.item;

import java.util.List;
import java.util.function.Consumer;
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
import net.minecraft.world.item.component.ItemAttributeModifiers;

public interface IFoodLike {
	public FoodInfo getFoodInfo();

	public boolean shouldAddEffectTooltips();
	public default void addEffectTooltips(Consumer<Component> tooltipAdder, float durationFactor, float tickRate) {

		List<Pair<Supplier<MobEffectInstance>, Float>> effectList = this.getFoodInfo().getEffects();
		List<Pair<Attribute, AttributeModifier>> attributeList = Lists.newArrayList();
		if (effectList.isEmpty()) {
			tooltipAdder.accept(Component.translatable("effect.none").withStyle(ChatFormatting.GRAY));
			return;
		}
		
		for (Pair<Supplier<MobEffectInstance>, Float> effectPair : effectList) {
			Supplier<MobEffectInstance> instance = effectPair.getFirst();
			MutableComponent mutableComponent = Component.translatable(instance.get().getDescriptionId());
			MobEffect effect = instance.get().getEffect().value();
			effect.createModifiers(instance.get().getAmplifier(), (attributeHolder, attributeModifier) -> {
				attributeList.add(new Pair<>(attributeHolder.value(), attributeModifier));

			});

			if (instance.get().getAmplifier() > 0) {
				mutableComponent = Component.translatable("potion.withAmplifier", mutableComponent,
						Component.translatable("potion.potency." + instance.get().getAmplifier()));
			}

			if (instance.get().getDuration() > 20) {
				mutableComponent = Component.translatable("potion.withDuration", mutableComponent,
						MobEffectUtil.formatDuration(instance.get(), 1.0F, tickRate));
			}

			tooltipAdder.accept(mutableComponent.withStyle(effect.getCategory().getTooltipFormatting()));
		}

		if (!attributeList.isEmpty()) {
			tooltipAdder.accept(CommonComponents.EMPTY);
			tooltipAdder.accept(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));

			for (Pair<Attribute, AttributeModifier> pair : attributeList) {
				AttributeModifier attributemodifier = pair.getSecond();
				double amount = attributemodifier.amount();
				double formattedAmount;
				if (attributemodifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_BASE
						&& attributemodifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
					formattedAmount = attributemodifier.amount();
				} else {
					formattedAmount = attributemodifier.amount() * 100.0;
				}

				if (amount > 0.0) {
					tooltipAdder.accept(Component.translatable(
							"attribute.modifier.plus." + attributemodifier.operation().id(),
							new Object[] { ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(formattedAmount),
									Component.translatable(pair.getFirst().getDescriptionId()) })
							.withStyle(ChatFormatting.BLUE));
				} else if (amount < 0.0) {
					formattedAmount *= -1.0;
					tooltipAdder.accept(Component.translatable(
							"attribute.modifier.take." + attributemodifier.operation().id(),
							new Object[] { ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(formattedAmount),
									Component.translatable(pair.getFirst().getDescriptionId()) })
							.withStyle(ChatFormatting.RED));
				}
			}
		}
	}
}
