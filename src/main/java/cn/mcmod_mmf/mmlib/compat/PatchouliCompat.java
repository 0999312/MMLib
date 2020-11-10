package cn.mcmod_mmf.mmlib.compat;

import java.util.Map;

import com.google.common.collect.Maps;

import cn.mcmod_mmf.mmlib.Main;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.items.ItemHandlerHelper;
@EventBusSubscriber(modid = Main.MODID)
public class PatchouliCompat {
	private static final Map<String,String> BOOKS = Maps.newHashMap();
	public static void addBook(String name) {
		BOOKS.put(name, "patchouli");
	}
	public static void addBook(String name,String modid) {
		BOOKS.put(name, modid);
	}
	public static void removeBook(String name) {
		BOOKS.remove(name);
	}
	public static ItemStack getBook(String name) {
		ItemStack book = new ItemStack(Item.getByNameOrId("patchouli:guide_book"));
        NBTTagCompound tag = new NBTTagCompound();
        StringBuilder builder2 = new StringBuilder(BOOKS.get(name)).append(':').append(name);
        tag.setString("patchouli:book", builder2.toString());
        book.setTagCompound(tag);
        return book;
	}
    @SubscribeEvent
    public static void onPlayerEnterServer(PlayerLoggedInEvent event) {
    	if(!Loader.isModLoaded("patchouli"))
    		return ;
        if (event.player instanceof EntityPlayerMP) {
        	BOOKS.forEach((name,modid)->{giveGuideBook((EntityPlayerMP) event.player, name, modid);});
        }
    }
    @Method(modid="patchouli")
    private static void giveGuideBook(EntityPlayerMP player,String Book_name,String modid) {
		NBTTagCompound playerData = player.getEntityData();
		NBTTagCompound data = getTagSafe(playerData, EntityPlayer.PERSISTED_NBT_TAG);
		StringBuilder builder =new StringBuilder(Book_name).append("_hasbook");
		if (!data.getBoolean(builder.toString())) {
			ItemStack book = new ItemStack(Item.getByNameOrId("patchouli:guide_book"));
            NBTTagCompound tag = new NBTTagCompound();
            StringBuilder builder2 = new StringBuilder(modid).append(':').append(Book_name);
            tag.setString("patchouli:book", builder2.toString());
            book.setTagCompound(tag);
			ItemHandlerHelper.giveItemToPlayer(player, book);
			data.setBoolean(builder.toString(), true);
			playerData.setTag(EntityPlayer.PERSISTED_NBT_TAG, data);
		}
    }
    private static NBTTagCompound getTagSafe(NBTTagCompound tag, String key) {
        if(tag == null) {
          return new NBTTagCompound();
        }

        return tag.getCompoundTag(key);
      }
}
