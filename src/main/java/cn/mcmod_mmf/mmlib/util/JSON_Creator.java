package cn.mcmod_mmf.mmlib.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;

import java.io.*;

public final class JSON_Creator {
    private static final JSON_Creator instance = new JSON_Creator();  
    	private JSON_Creator (){}  
    public static JSON_Creator getInstance() {  
    	return instance;  
    }  
	public final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	public void genBlock(String modId,String blockName, String textureName, String path){
        File fileDir = new File(path + "\\blockstates\\");
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }
        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(fileDir + "\\" + blockName + ".json"), "UTF-8");
			JsonWriter jw = GSON.newJsonWriter(writer);

            jw.beginObject();
            jw.name("variants");
            jw.beginObject();
            jw.name("normal");
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

        genBlockModel(modId,blockName, textureName, path);
        genBlockItemModel(modId,blockName, path);

    }

	
    private void genBlockModel(String modId,String blockName, String textureName, String path){
        File fileDir = new File(path + "\\models\\block\\");
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }

        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(fileDir + "\\" + blockName + ".json"), "UTF-8");
			JsonWriter jw = GSON.newJsonWriter(writer);

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

    public void genCake(String modId, String blockName, String textureName, String path){
        File fileDir = new File(path + "\\blockstates\\");
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }

        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(fileDir + "\\" + blockName + ".json"), "UTF-8");
			JsonWriter jw = GSON.newJsonWriter(writer);

            jw.beginObject();
            jw.name("variants");
            jw.beginObject();
            jw.name("bites=0");
            jw.beginObject();
            jw.name("model").value(modId + ":" + blockName+"_uneaten");
            jw.endObject();

            jw.name("bites=1");
            jw.beginObject();
            jw.name("model").value(modId + ":" + blockName+"_slice1");
            jw.endObject();


            jw.name("bites=2");
            jw.beginObject();
            jw.name("model").value(modId + ":" + blockName+"_slice2");
            jw.endObject();


            jw.name("bites=3");
            jw.beginObject();
            jw.name("model").value(modId + ":" + blockName+"_slice3");
            jw.endObject();


            jw.name("bites=4");
            jw.beginObject();
            jw.name("model").value(modId + ":" + blockName+"_slice4");
            jw.endObject();
            
            jw.name("bites=5");
            jw.beginObject();
            jw.name("model").value(modId + ":" + blockName+"_slice5");
            jw.endObject();
            
            jw.name("bites=6");
            jw.beginObject();
            jw.name("model").value(modId + ":" + blockName+"_slice6");
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

        genCakeModel(modId,blockName, textureName, path);
        genItem(modId,blockName, textureName, path);

    }

	
    private void genCakeModel(String modId, String blockName, String textureName, String path){
        File fileDir = new File(path + "\\models\\block\\");
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }
        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(fileDir + "\\" + blockName+"_uneaten"+ ".json"), "UTF-8");
			JsonWriter jw = GSON.newJsonWriter(writer);

            jw.beginObject();
            jw.name("parent").value("block/cake_uneaten");
            jw.name("textures");
            
            jw.beginObject();
            jw.name("particle").value(modId + ":blocks/" + textureName+"_side");
            jw.name("bottom").value(modId + ":blocks/" + textureName+"_bottom");
            jw.name("top").value(modId + ":blocks/" + textureName+"_top");
            jw.name("side").value(modId + ":blocks/" + textureName+"_side");
           
            jw.endObject();
            jw.endObject();

            writer.close();
            
            Writer writer1 = new OutputStreamWriter(new FileOutputStream(fileDir + "\\" + blockName+"_slice1"+ ".json"), "UTF-8");
			JsonWriter jw1 = GSON.newJsonWriter(writer1);

            jw1.beginObject();
            jw1.name("parent").value("block/cake_slice1");
            jw1.name("textures");
            
            jw1.beginObject();
            jw1.name("particle").value(modId + ":blocks/" + textureName+"_side");
            jw1.name("bottom").value(modId + ":blocks/" + textureName+"_bottom");
            jw1.name("top").value(modId + ":blocks/" + textureName+"_top");
            jw1.name("side").value(modId + ":blocks/" + textureName+"_side");
            jw1.name("inside").value(modId + ":blocks/" + textureName+"_inner");
            
            jw1.endObject();
            jw1.endObject();

            writer1.close();
            
            Writer writer11 = new OutputStreamWriter(new FileOutputStream(fileDir + "\\" + blockName+"_slice2"+ ".json"), "UTF-8");
			JsonWriter jw11 = GSON.newJsonWriter(writer11);

            jw11.beginObject();
            jw11.name("parent").value("block/cake_slice2");
            jw11.name("textures");
            
            jw11.beginObject();
            jw11.name("particle").value(modId + ":blocks/" + textureName+"_side");
            jw11.name("bottom").value(modId + ":blocks/" + textureName+"_bottom");
            jw11.name("top").value(modId + ":blocks/" + textureName+"_top");
            jw11.name("side").value(modId + ":blocks/" + textureName+"_side");
            jw11.name("inside").value(modId + ":blocks/" + textureName+"_inner");
            
            jw11.endObject();
            jw11.endObject();

            writer11.close();
            
            Writer writer111 = new OutputStreamWriter(new FileOutputStream(fileDir + "\\" + blockName+"_slice3"+ ".json"), "UTF-8");
			JsonWriter jw111 = GSON.newJsonWriter(writer111);

            jw111.beginObject();
            jw111.name("parent").value("block/cake_slice3");
            jw111.name("textures");
            
            jw111.beginObject();
            jw111.name("particle").value(modId + ":blocks/" + textureName+"_side");
            jw111.name("bottom").value(modId + ":blocks/" + textureName+"_bottom");
            jw111.name("top").value(modId + ":blocks/" + textureName+"_top");
            jw111.name("side").value(modId + ":blocks/" + textureName+"_side");
            jw111.name("inside").value(modId + ":blocks/" + textureName+"_inner");
            
            jw111.endObject();
            jw111.endObject();

            writer111.close();
            
            Writer writer1111 = new OutputStreamWriter(new FileOutputStream(fileDir + "\\" + blockName+"_slice4"+ ".json"), "UTF-8");
			JsonWriter jw1111 = GSON.newJsonWriter(writer1111);

            jw1111.beginObject();
            jw1111.name("parent").value("block/cake_slice4");
            jw1111.name("textures");
            
            jw1111.beginObject();
            jw1111.name("particle").value(modId + ":blocks/" + textureName+"_side");
            jw1111.name("bottom").value(modId + ":blocks/" + textureName+"_bottom");
            jw1111.name("top").value(modId + ":blocks/" + textureName+"_top");
            jw1111.name("side").value(modId + ":blocks/" + textureName+"_side");
            jw1111.name("inside").value(modId + ":blocks/" + textureName+"_inner");
            
            jw1111.endObject();
            jw1111.endObject();

            writer1111.close();
            
            Writer writer11111 = new OutputStreamWriter(new FileOutputStream(fileDir + "\\" + blockName+"_slice5"+ ".json"), "UTF-8");
			JsonWriter jw11111 = GSON.newJsonWriter(writer11111);

            jw11111.beginObject();
            jw11111.name("parent").value("block/cake_slice5");
            jw11111.name("textures");
            
            jw11111.beginObject();
            jw11111.name("particle").value(modId + ":blocks/" + textureName+"_side");
            jw11111.name("bottom").value(modId + ":blocks/" + textureName+"_bottom");
            jw11111.name("top").value(modId + ":blocks/" + textureName+"_top");
            jw11111.name("side").value(modId + ":blocks/" + textureName+"_side");
            jw11111.name("inside").value(modId + ":blocks/" + textureName+"_inner");
            
            jw11111.endObject();
            jw11111.endObject();

            writer11111.close();
            
            Writer writer111111 = new OutputStreamWriter(new FileOutputStream(fileDir + "\\" + blockName+"_slice6"+ ".json"), "UTF-8");
			JsonWriter jw111111 = GSON.newJsonWriter(writer111111);

            jw111111.beginObject();
            jw111111.name("parent").value("block/cake_slice6");
            jw111111.name("textures");
            
            jw111111.beginObject();
            jw111111.name("particle").value(modId + ":blocks/" + textureName+"_side");
            jw111111.name("bottom").value(modId + ":blocks/" + textureName+"_bottom");
            jw111111.name("top").value(modId + ":blocks/" + textureName+"_top");
            jw111111.name("side").value(modId + ":blocks/" + textureName+"_side");
            jw111111.name("inside").value(modId + ":blocks/" + textureName+"_inner");
            
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
    
    private void genBlockItemModel(String modId, String blockName, String path){
        File fileDir = new File(path + "\\models\\item\\");
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }

        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(fileDir + "\\" + blockName + ".json"), "UTF-8");
			JsonWriter jw = GSON.newJsonWriter(writer);

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
    
    public void genItem(String modId, String itemName, String textureName, String path){
        File fileDir = new File(path + "\\models\\item\\");
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }
        
        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(fileDir + "\\" + itemName + ".json"), "UTF-8");
			JsonWriter jw = GSON.newJsonWriter(writer);

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

    public void genTool(String modId, String itemName, String textureName, String path){
        File fileDir = new File(path + "\\models\\item\\");
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }
        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(fileDir + "\\" + itemName + ".json"), "UTF-8");
			JsonWriter jw = GSON.newJsonWriter(writer);

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
