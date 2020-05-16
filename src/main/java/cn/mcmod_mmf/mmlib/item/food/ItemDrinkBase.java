package cn.mcmod_mmf.mmlib.item.food;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.Optional.Method;
import toughasnails.api.stat.capability.ITemperature;
import toughasnails.api.stat.capability.IThirst;
import toughasnails.api.temperature.Temperature;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.thirst.ThirstHelper;

@Interface(iface = "toughasnails.api.thirst.IDrink", modid = "toughasnails")
public class ItemDrinkBase extends ItemFoodBase {
	private final PotionEffect[][] effect;
	private final ItemStack ContainerItem;

	public ItemDrinkBase(String modid,String name, FoodInfo[] info, PotionEffect[][] effects,
			ItemStack container) {
		super(modid,name, 1, info);
		this.ContainerItem = container;
		this.effect = effects != null && effects.length > 0 ? effects : null;
	}

	public PotionEffect[] getEffectList(ItemStack stack) {
		return stack.getMetadata() < getAllEffectList().length ? getAllEffectList()[stack.getMetadata()]
				: new PotionEffect[] {};
	}

	public PotionEffect[][] getAllEffectList() {
		return effect;
	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);

		if (playerIn.canEat(true) || playerIn.isCreative()) {
			playerIn.setActiveHand(handIn);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
	}

	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.DRINK;
	}

	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) entityLiving;
			entityplayer.getFoodStats().addStats(this, stack);
			worldIn.playSound((EntityPlayer) null, entityplayer.posX, entityplayer.posY, entityplayer.posZ,
					SoundEvents.ENTITY_VILLAGER_YES, SoundCategory.PLAYERS, 0.5F,
					worldIn.rand.nextFloat() * 0.1F + 0.9F);
			this.onFoodEaten(stack, worldIn, entityplayer);
			entityplayer.addStat(StatList.getObjectUseStats(this));
			if (entityplayer instanceof EntityPlayerMP) {
				CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP) entityplayer, stack);
			}
		}
		return this.getContainer();
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		if (getEffectList(stack) != null && getEffectList(stack).length > 0) {
			for (PotionEffect effect1 : getEffectList(stack)) {
				if (effect1 != null && effect1.getPotion() != null) {
					Potion por = effect1.getPotion();
					if (por == null)
						continue;
					int dur = effect1.getDuration();
					if (player.isPotionActive(effect1.getPotion())) {
						PotionEffect check = player.getActivePotionEffect(por);
						dur += check.getDuration();
					}
					player.addPotionEffect(new PotionEffect(effect1.getPotion(), dur, effect1.getAmplifier()));
				}
			}
		}
	}

	@Method(modid = "toughasnails")
	public void drink(EntityLivingBase entity) {
		EntityPlayer player = (EntityPlayer) entity;
		IThirst thirst = ThirstHelper.getThirstData(player);

		thirst.addStats(getThirst(), getHydration());
	}

	@Method(modid = "toughasnails")
	public int getThirst() {
		return 8;
	}

	@Method(modid = "toughasnails")
	public float getHydration() {
		return 0.6F;
	}

	@Method(modid = "toughasnails")
	public float getPoisonChance() {
		return 0.0F;
	}

	@Method(modid = "toughasnails")
	public void changeTemperature(EntityLivingBase entity) {
		EntityPlayer player = (EntityPlayer) entity;
		ITemperature temperature = TemperatureHelper.getTemperatureData(player);
		if (temperature.getTemperature().getRawValue() <= 10) {
			temperature.setTemperature(new Temperature(temperature.getTemperature().getRawValue() + 1));
		} else if (temperature.getTemperature().getRawValue() >= 14) {
			temperature.setTemperature(new Temperature(temperature.getTemperature().getRawValue() - 1));
		}
	}

	public ItemStack getContainer() {
		return ContainerItem;
	}
}
