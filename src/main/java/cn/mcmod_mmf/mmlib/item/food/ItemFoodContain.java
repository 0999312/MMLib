package cn.mcmod_mmf.mmlib.item.food;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemFoodContain extends ItemFoodBase {
	public ItemStack[] foodcontainer;

	public ItemFoodContain(String modid,String name, int stackSize, FoodInfo[] info, ItemStack[] container) {
		super(modid,name, stackSize, info);
		foodcontainer = container;
	}

	public ItemStack getFoodContainerItem(ItemStack stack) {
		return stack.getMetadata() < getContainers().length ? getContainers()[stack.getMetadata()] : null;
	}

	public ItemStack[] getContainers() {
		return foodcontainer;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) entityLiving;
			entityplayer.getFoodStats().addStats(this, stack);
			worldIn.playSound((EntityPlayer) null, entityplayer.posX, entityplayer.posY, entityplayer.posZ,
					SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F,
					worldIn.rand.nextFloat() * 0.1F + 0.9F);
			this.onFoodEaten(stack, worldIn, entityplayer);
			entityplayer.addStat(StatList.getObjectUseStats(this));

			if (entityplayer instanceof EntityPlayerMP) {
				CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP) entityplayer, stack);
			}
			if(this.maxStackSize>1){
				if(!entityplayer.inventory.addItemStackToInventory(getFoodContainerItem(stack)))
					entityplayer.dropItem(getFoodContainerItem(stack), true);
				stack.shrink(1);
		        return stack;
			}
		}
		return getFoodContainerItem(stack);
	}
}
