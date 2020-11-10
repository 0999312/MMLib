package cn.mcmod_mmf.mmlib;

import cn.mcmod_mmf.mmlib.util.ClientUtils;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemStrawHat extends ItemArmor {
    public ItemStrawHat(EntityEquipmentSlot slot) {
        super(ArmorMaterial.CHAIN, 0, slot);
    }

    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, net.minecraft.client.model.ModelBiped _default) {
		ModelBiped model1 = null;
		ModelBiped model2 = null;
		ModelBiped model = null;
		if (model1 == null) {
			model1 = ClientUtils.getInstance().getArmorModelFromJSON(new ResourceLocation(Main.MODID, "models/test.json"));
		}
		if (model2 == null) {
			model2 = ClientUtils.getInstance().getArmorModelFromJSON(new ResourceLocation(Main.MODID, "models/test.json"));
		}

		model = ClientUtils.getInstance().getCustomArmorModel(entityLiving, itemStack, armorSlot, model, model1, model2);
		return model;
    }

    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return Main.MODID + ":" + "models/test.png";
    }
}
