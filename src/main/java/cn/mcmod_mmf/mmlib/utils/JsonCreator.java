package cn.mcmod_mmf.mmlib.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.io.*;

import org.apache.commons.lang3.StringUtils;

public class JsonCreator {
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void genLangForItems(Item... items) {
        File fileDir = new File("json_create\\lang\\");
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(fileDir + "\\en_us.json"), "UTF-8");
            JsonWriter jw = gson.newJsonWriter(writer);

            jw.beginObject();
            for (Item item : items) {
                jw.name(item.getDescriptionId());
                jw.value(getEN_USvalue(item.getRegistryName().getPath()));
            }
            jw.endObject();

            writer.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getEN_USvalue(String key) {
        String str[] = key.split("_");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length; i++) {
            builder.append(StringUtils.capitalize(str[i]));
            if (i + 1 < str.length)
                builder.append(' ');
        }
        return builder.toString();
    }

    public static void genBlock(String modId, String blockName, String textureName) {
        File fileDir = new File("json_create\\blockstates\\");
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(fileDir + "\\" + blockName + ".json"), "UTF-8");
            JsonWriter jw = gson.newJsonWriter(writer);

            jw.beginObject();
            jw.name("variants");
            jw.beginObject();
            jw.name("");
            jw.beginObject();
            jw.name("model").value(modId + ":" + blockName);
            jw.endObject();
            jw.endObject();
            jw.endObject();

            writer.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        genBlockModel(modId, blockName, textureName);
        genBlockItemModel(modId, blockName);

    }

    public static void genTagForItems(String tag, ItemStack... components) {
        File fileDir = new File("json_create\\tags\\item\\");
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(fileDir + "\\" + tag + ".json"), "UTF-8");
            JsonWriter jw = gson.newJsonWriter(writer);

            jw.beginObject();
            jw.name("replace").value("false");
            jw.name("values");
            jw.beginArray();
            for (ItemStack item : components) {
                jw.value(item.getItem().getRegistryName().toString());
            }
            jw.endArray();
            jw.endObject();

            writer.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void genBlockModel(String modId, String blockName, String textureName) {
        File fileDir = new File("json_create\\models\\block\\");
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(fileDir + "\\" + blockName + ".json"), "UTF-8");
            JsonWriter jw = gson.newJsonWriter(writer);

            jw.beginObject();
            jw.name("parent").value("block/cube_all");
            jw.name("textures");
            jw.beginObject();
            jw.name("all").value(modId + ":blocks/" + textureName);
            jw.endObject();
            jw.endObject();

            writer.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void genCake(String modId, String blockName, String textureName) {
        File fileDir = new File("json_create\\blockstates\\");
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(fileDir + "\\" + blockName + ".json"), "UTF-8");
            JsonWriter jw = gson.newJsonWriter(writer);

            jw.beginObject();
            jw.name("variants");
            jw.beginObject();
            jw.name("bites=0");
            jw.beginObject();
            jw.name("model").value(modId + ":" + blockName + "_uneaten");
            jw.endObject();

            jw.name("bites=1");
            jw.beginObject();
            jw.name("model").value(modId + ":" + blockName + "_slice1");
            jw.endObject();

            jw.name("bites=2");
            jw.beginObject();
            jw.name("model").value(modId + ":" + blockName + "_slice2");
            jw.endObject();

            jw.name("bites=3");
            jw.beginObject();
            jw.name("model").value(modId + ":" + blockName + "_slice3");
            jw.endObject();

            jw.name("bites=4");
            jw.beginObject();
            jw.name("model").value(modId + ":" + blockName + "_slice4");
            jw.endObject();

            jw.name("bites=5");
            jw.beginObject();
            jw.name("model").value(modId + ":" + blockName + "_slice5");
            jw.endObject();

            jw.name("bites=6");
            jw.beginObject();
            jw.name("model").value(modId + ":" + blockName + "_slice6");
            jw.endObject();

            jw.endObject();
            jw.endObject();

            writer.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        genCakeModel(modId, blockName, textureName);
        genItem(modId, blockName, textureName);

    }

    private static void genCakeModel(String modId, String blockName, String textureName) {
        File fileDir = new File("json_create\\models\\block\\");
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        try {
            Writer writer = new OutputStreamWriter(
                    new FileOutputStream(fileDir + "\\" + blockName + "_uneaten" + ".json"), "UTF-8");
            JsonWriter jw = gson.newJsonWriter(writer);

            jw.beginObject();
            jw.name("parent").value("block/cake_uneaten");
            jw.name("textures");

            jw.beginObject();
            jw.name("particle").value(modId + ":blocks/" + textureName + "_side");
            jw.name("bottom").value(modId + ":blocks/" + textureName + "_bottom");
            jw.name("top").value(modId + ":blocks/" + textureName + "_top");
            jw.name("side").value(modId + ":blocks/" + textureName + "_side");

            jw.endObject();
            jw.endObject();

            writer.close();

            Writer writer1 = new OutputStreamWriter(
                    new FileOutputStream(fileDir + "\\" + blockName + "_slice1" + ".json"), "UTF-8");
            JsonWriter jw1 = gson.newJsonWriter(writer1);

            jw1.beginObject();
            jw1.name("parent").value("block/cake_slice1");
            jw1.name("textures");

            jw1.beginObject();
            jw1.name("particle").value(modId + ":blocks/" + textureName + "_side");
            jw1.name("bottom").value(modId + ":blocks/" + textureName + "_bottom");
            jw1.name("top").value(modId + ":blocks/" + textureName + "_top");
            jw1.name("side").value(modId + ":blocks/" + textureName + "_side");
            jw1.name("inside").value(modId + ":blocks/" + textureName + "_inner");

            jw1.endObject();
            jw1.endObject();

            writer1.close();

            Writer writer11 = new OutputStreamWriter(
                    new FileOutputStream(fileDir + "\\" + blockName + "_slice2" + ".json"), "UTF-8");
            JsonWriter jw11 = gson.newJsonWriter(writer11);

            jw11.beginObject();
            jw11.name("parent").value("block/cake_slice2");
            jw11.name("textures");

            jw11.beginObject();
            jw11.name("particle").value(modId + ":blocks/" + textureName + "_side");
            jw11.name("bottom").value(modId + ":blocks/" + textureName + "_bottom");
            jw11.name("top").value(modId + ":blocks/" + textureName + "_top");
            jw11.name("side").value(modId + ":blocks/" + textureName + "_side");
            jw11.name("inside").value(modId + ":blocks/" + textureName + "_inner");

            jw11.endObject();
            jw11.endObject();

            writer11.close();

            Writer writer111 = new OutputStreamWriter(
                    new FileOutputStream(fileDir + "\\" + blockName + "_slice3" + ".json"), "UTF-8");
            JsonWriter jw111 = gson.newJsonWriter(writer111);

            jw111.beginObject();
            jw111.name("parent").value("block/cake_slice3");
            jw111.name("textures");

            jw111.beginObject();
            jw111.name("particle").value(modId + ":blocks/" + textureName + "_side");
            jw111.name("bottom").value(modId + ":blocks/" + textureName + "_bottom");
            jw111.name("top").value(modId + ":blocks/" + textureName + "_top");
            jw111.name("side").value(modId + ":blocks/" + textureName + "_side");
            jw111.name("inside").value(modId + ":blocks/" + textureName + "_inner");

            jw111.endObject();
            jw111.endObject();

            writer111.close();

            Writer writer1111 = new OutputStreamWriter(
                    new FileOutputStream(fileDir + "\\" + blockName + "_slice4" + ".json"), "UTF-8");
            JsonWriter jw1111 = gson.newJsonWriter(writer1111);

            jw1111.beginObject();
            jw1111.name("parent").value("block/cake_slice4");
            jw1111.name("textures");

            jw1111.beginObject();
            jw1111.name("particle").value(modId + ":blocks/" + textureName + "_side");
            jw1111.name("bottom").value(modId + ":blocks/" + textureName + "_bottom");
            jw1111.name("top").value(modId + ":blocks/" + textureName + "_top");
            jw1111.name("side").value(modId + ":blocks/" + textureName + "_side");
            jw1111.name("inside").value(modId + ":blocks/" + textureName + "_inner");

            jw1111.endObject();
            jw1111.endObject();

            writer1111.close();

            Writer writer11111 = new OutputStreamWriter(
                    new FileOutputStream(fileDir + "\\" + blockName + "_slice5" + ".json"), "UTF-8");
            JsonWriter jw11111 = gson.newJsonWriter(writer11111);

            jw11111.beginObject();
            jw11111.name("parent").value("block/cake_slice5");
            jw11111.name("textures");

            jw11111.beginObject();
            jw11111.name("particle").value(modId + ":blocks/" + textureName + "_side");
            jw11111.name("bottom").value(modId + ":blocks/" + textureName + "_bottom");
            jw11111.name("top").value(modId + ":blocks/" + textureName + "_top");
            jw11111.name("side").value(modId + ":blocks/" + textureName + "_side");
            jw11111.name("inside").value(modId + ":blocks/" + textureName + "_inner");

            jw11111.endObject();
            jw11111.endObject();

            writer11111.close();

            Writer writer111111 = new OutputStreamWriter(
                    new FileOutputStream(fileDir + "\\" + blockName + "_slice6" + ".json"), "UTF-8");
            JsonWriter jw111111 = gson.newJsonWriter(writer111111);

            jw111111.beginObject();
            jw111111.name("parent").value("block/cake_slice6");
            jw111111.name("textures");

            jw111111.beginObject();
            jw111111.name("particle").value(modId + ":blocks/" + textureName + "_side");
            jw111111.name("bottom").value(modId + ":blocks/" + textureName + "_bottom");
            jw111111.name("top").value(modId + ":blocks/" + textureName + "_top");
            jw111111.name("side").value(modId + ":blocks/" + textureName + "_side");
            jw111111.name("inside").value(modId + ":blocks/" + textureName + "_inner");

            jw111111.endObject();
            jw111111.endObject();

            writer111111.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void genBlockItemModel(String modId, String blockName) {
        File fileDir = new File("json_create\\models\\item\\");
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(fileDir + "\\" + blockName + ".json"), "UTF-8");
            JsonWriter jw = gson.newJsonWriter(writer);

            jw.beginObject();
            jw.name("parent").value(modId + ":block/" + blockName);
            jw.endObject();

            writer.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void genItems(Item... items) {
        for (Item item : items) {
            genItem(item.getRegistryName().getNamespace(), item.getRegistryName().getPath(),
                    item.getRegistryName().getPath());
        }
    }

    public static void genItem(String modId, String itemName, String textureName) {
        File fileDir = new File("json_create\\models\\item\\");
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(fileDir + "\\" + itemName + ".json"), "UTF-8");
            JsonWriter jw = gson.newJsonWriter(writer);

            jw.beginObject();
            jw.name("parent").value("item/generated");
            jw.name("textures");
            jw.beginObject();
            jw.name("layer0").value(modId + ":items/" + textureName);
            jw.endObject();
            jw.endObject();

            writer.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void genTool(String modId, String itemName, String textureName) {
        File fileDir = new File("json_create\\models\\item\\");
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(fileDir + "\\" + itemName + ".json"), "UTF-8");
            JsonWriter jw = gson.newJsonWriter(writer);

            jw.beginObject();
            jw.name("parent").value("item/handheld");
            jw.name("textures");
            jw.beginObject();
            jw.name("layer0").value(modId + ":items/" + textureName);
            jw.endObject();
            jw.endObject();

            writer.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
