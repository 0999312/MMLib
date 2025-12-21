package cn.mcmod_mmf.mmlib.villager;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;

public class RandomPriceSellItemListing implements ItemListing {

    private final ItemStack itemStack;
    private final int baseEmeraldCost;
    private final int minCount;
    private final int maxCount;
    private final int maxUses;
    private final int villagerXp;
    private final float priceMultiplier;
    
    private ItemStack moneyItem;

    public RandomPriceSellItemListing(ItemStack itemStack, int pBaseEmeraldCost, int pMaxUses, int pVillagerXp) {
        this(itemStack, pBaseEmeraldCost, 1, 1, pMaxUses, pVillagerXp, 0.05F);
     }
     
     public RandomPriceSellItemListing(ItemStack itemStack, int pBaseEmeraldCost, int pMaxUses, int pVillagerXp, float pPriceMultiplier) {
        this(itemStack, pBaseEmeraldCost, 1, 1, pMaxUses, pVillagerXp, pPriceMultiplier);
     }

     public RandomPriceSellItemListing(ItemStack itemStack, int pBaseEmeraldCost, int pMinCount, int pMaxCount, int pMaxUses, int pVillagerXp) {
        this(itemStack, pBaseEmeraldCost, pMinCount, pMaxCount, pMaxUses, pVillagerXp, 0.05F);
     }
     
     public RandomPriceSellItemListing(ItemStack itemStack, int pBaseEmeraldCost, int pMinCount, int pMaxCount, int pMaxUses, int pVillagerXp, float pPriceMultiplier) {
        this.itemStack = itemStack;
        this.baseEmeraldCost = pBaseEmeraldCost;
        this.minCount = pMinCount;
        this.maxCount = pMaxCount;
        this.maxUses = pMaxUses;
        this.villagerXp = pVillagerXp;
        this.priceMultiplier = pPriceMultiplier;
        
        this.moneyItem = new ItemStack(Items.EMERALD);
     }

    @Override
    public MerchantOffer getOffer(Entity pTrader, RandomSource pRand) {
       int j = Math.min(this.baseEmeraldCost, 64);
       ItemStack itemstack = itemStack.copy();
       int count = Math.min(pRand.nextInt(minCount, maxCount + 1), 64);
       itemstack.setCount(count);
       ItemStack itemstack1 = this.getMoneyItem().copy();
       itemstack1.setCount(j);
       return new MerchantOffer(itemstack, itemstack1, this.maxUses, this.villagerXp, this.priceMultiplier);
    }

	public ItemStack getMoneyItem() {
		return moneyItem;
	}

	public void setMoneyItem(ItemStack moneyItem) {
		this.moneyItem = moneyItem;
	}
}
