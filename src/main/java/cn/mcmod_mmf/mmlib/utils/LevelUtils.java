package cn.mcmod_mmf.mmlib.utils;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class LevelUtils {
    public static void splitAndSpawnExperience(Level world, Vec3 pos, int craftedAmount, float experience) {
        int expTotal = Mth.floor((float) craftedAmount * experience);
        float expFraction = Mth.frac((float) craftedAmount * experience);
        if (expFraction != 0.0F && Math.random() < (double) expFraction) {
            ++expTotal;
        }

        while (expTotal > 0) {
            int expValue = ExperienceOrb.getExperienceValue(expTotal);
            expTotal -= expValue;
            world.addFreshEntity(new ExperienceOrb(world, pos.x, pos.y, pos.z, expValue));
        }
    }
    
    public static void spawnItemEntity(Level world, ItemStack stack, double x, double y, double z, double xMotion, double yMotion, double zMotion) {
        ItemEntity entity = new ItemEntity(world, x, y, z, stack);
        entity.setDeltaMovement(xMotion, yMotion, zMotion);
        world.addFreshEntity(entity);
    }
}
