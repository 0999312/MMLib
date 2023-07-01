package cn.mcmod_mmf.mmlib.recipe;

import java.util.Random;

import net.minecraft.world.item.ItemStack;

public record ChanceResult(ItemStack stack, float chance) {
    public static final ChanceResult EMPTY = new ChanceResult(ItemStack.EMPTY, 1);
    
    public ItemStack rollOutput(Random rand, int fortuneLevel) {
        int outputAmount = stack.getCount();
        double fortuneBonus = fortuneLevel;
        for (int roll = 0; roll < stack.getCount(); roll++)
            if (rand.nextFloat() > chance + fortuneBonus)
                outputAmount--;
        if (outputAmount == 0)
            return ItemStack.EMPTY;
        ItemStack out = stack.copy();
        out.setCount(outputAmount);
        return out;
    }
}
