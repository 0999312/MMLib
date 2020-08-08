package cn.mcmod_mmf.mmlib.item.food;

import javax.annotation.Nullable;

import net.dries007.tfc.api.capability.food.FoodData;
import net.dries007.tfc.api.capability.food.FoodHandler;
import net.dries007.tfc.api.capability.food.FoodHeatHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fml.common.Loader;

public class ItemFoodBase extends ItemFood {
	private final String modid;
	private final FoodInfo[] info;
	private boolean fastEat;


	public ItemFoodBase(String modid,String name, int stackSize, FoodInfo[] info) {
		super(info[0].getAmount(), info[0].getCalories(), info[0].isWolfFood());
		this.setUnlocalizedName(modid+"."+name);
		this.setHasSubtypes(info != null && info.length > 0);
		this.setMaxStackSize(stackSize);
		this.setMaxDamage(0);
		this.setNoRepair();
		this.modid=modid;
		this.info = info != null && info.length > 0 ? info : null;
	}

	@Override
	public int getHealAmount(ItemStack stack) {
		return stack.getMetadata() < getInfo().length ? getInfo()[stack.getMetadata()].getAmount() : 0;
	}

	@Override
	public float getSaturationModifier(ItemStack stack) {
		return stack.getMetadata() < getInfo().length ? getInfo()[stack.getMetadata()].getCalories() : 0;
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		super.onFoodEaten(stack, worldIn, player);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (this.isInCreativeTab(tab))
			if (getInfo() != null) {
				for (int i = 0; i < getInfo().length; i++)
					list.add(new ItemStack(this, 1, i));
			} else
				list.add(new ItemStack(this));
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if(getInfo()!=null) {
			StringBuilder name_builder = new StringBuilder("item.");
			String subName = stack.getMetadata() < getInfo().length?name_builder.append(modid).append('.').append(getInfo()[stack.getMetadata()].getName()).toString(): "null_food";
			return subName;
		}
		return this.getUnlocalizedName();
	}

	public FoodInfo[] getInfo() {
		return info;
	}
	
    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt){
    	if(Loader.isModLoaded("tfc")){
	    	if(getInfo()!=null) {
				FoodInfo info = stack.getMetadata() < getInfo().length?getInfo()[stack.getMetadata()]: new FoodInfo("null_food",0, 0, false);
				ICapabilitySerializable<NBTTagCompound> capa = info.isHeatable() ? new FoodHeatHandler(nbt, new FoodData(info.getAmount(), info.getWater(), info.getCalories(), info.getNutrients(), info.getDecayModifier()),info.getHeatCapacity(),info.getCookingTemp()):new FoodHandler(nbt, new FoodData(info.getAmount(), info.getWater(), info.getCalories(), info.getNutrients(), info.getDecayModifier()));
				if(info.isNeverDecay()){
					if(capa instanceof FoodHeatHandler){
						((FoodHeatHandler)capa).setNonDecaying();
					}
					if(capa instanceof FoodHandler){
						((FoodHandler)capa).setNonDecaying();
					}
				}
				return capa;
			}
	    }
		return null;
    }
    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        if (isFastEat()) {
            return 20;
        }
		return 32;
    }

    public ItemFoodBase setFastEat() {
        this.fastEat = true;
        return this;
    }

    public boolean isFastEat() {
        return fastEat;
    }
}
