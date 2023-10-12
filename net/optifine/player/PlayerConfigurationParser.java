/*     */ package net.optifine.player;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonParser;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import javax.imageio.ImageIO;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.src.Config;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.optifine.http.HttpPipeline;
/*     */ import net.optifine.http.HttpUtils;
/*     */ import net.optifine.util.Json;
/*     */ 
/*     */ public class PlayerConfigurationParser {
/*  19 */   private String player = null;
/*     */   
/*     */   public static final String CONFIG_ITEMS = "items";
/*     */   public static final String ITEM_TYPE = "type";
/*     */   public static final String ITEM_ACTIVE = "active";
/*     */   
/*     */   public PlayerConfigurationParser(String player) {
/*  26 */     this.player = player;
/*     */   }
/*     */ 
/*     */   
/*     */   public PlayerConfiguration parsePlayerConfiguration(JsonElement je) {
/*  31 */     if (je == null)
/*     */     {
/*  33 */       throw new JsonParseException("JSON object is null, player: " + this.player);
/*     */     }
/*     */ 
/*     */     
/*  37 */     JsonObject jsonobject = (JsonObject)je;
/*  38 */     PlayerConfiguration playerconfiguration = new PlayerConfiguration();
/*  39 */     JsonArray jsonarray = (JsonArray)jsonobject.get("items");
/*     */     
/*  41 */     if (jsonarray != null)
/*     */     {
/*  43 */       for (int i = 0; i < jsonarray.size(); i++) {
/*     */         
/*  45 */         JsonObject jsonobject1 = (JsonObject)jsonarray.get(i);
/*  46 */         boolean flag = Json.getBoolean(jsonobject1, "active", true);
/*     */         
/*  48 */         if (flag) {
/*     */           
/*  50 */           String s = Json.getString(jsonobject1, "type");
/*     */           
/*  52 */           if (s == null) {
/*     */             
/*  54 */             Config.warn("Item type is null, player: " + this.player);
/*     */             
/*     */             continue;
/*     */           } 
/*  58 */           String s1 = Json.getString(jsonobject1, "model");
/*     */           
/*  60 */           if (s1 == null)
/*     */           {
/*  62 */             s1 = "items/" + s + "/model.cfg";
/*     */           }
/*     */           
/*  65 */           PlayerItemModel playeritemmodel = downloadModel(s1);
/*     */           
/*  67 */           if (playeritemmodel != null) {
/*     */             
/*  69 */             if (!playeritemmodel.isUsePlayerTexture()) {
/*     */               
/*  71 */               String s2 = Json.getString(jsonobject1, "texture");
/*     */               
/*  73 */               if (s2 == null)
/*     */               {
/*  75 */                 s2 = "items/" + s + "/users/" + this.player + ".png";
/*     */               }
/*     */               
/*  78 */               BufferedImage bufferedimage = downloadTextureImage(s2);
/*     */               
/*  80 */               if (bufferedimage == null) {
/*     */                 continue;
/*     */               }
/*     */ 
/*     */               
/*  85 */               playeritemmodel.setTextureImage(bufferedimage);
/*  86 */               ResourceLocation resourcelocation = new ResourceLocation("optifine.net", s2);
/*  87 */               playeritemmodel.setTextureLocation(resourcelocation);
/*     */             } 
/*     */             
/*  90 */             playerconfiguration.addPlayerItemModel(playeritemmodel);
/*     */           } 
/*     */         } 
/*     */         
/*     */         continue;
/*     */       } 
/*     */     }
/*  97 */     return playerconfiguration;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private BufferedImage downloadTextureImage(String texturePath) {
/* 103 */     String s = HttpUtils.getPlayerItemsUrl() + "/" + texturePath;
/*     */ 
/*     */     
/*     */     try {
/* 107 */       byte[] abyte = HttpPipeline.get(s, Minecraft.getMinecraft().getProxy());
/* 108 */       BufferedImage bufferedimage = ImageIO.read(new ByteArrayInputStream(abyte));
/* 109 */       return bufferedimage;
/*     */     }
/* 111 */     catch (IOException ioexception) {
/*     */       
/* 113 */       Config.warn("Error loading item texture " + texturePath + ": " + ioexception.getClass().getName() + ": " + ioexception.getMessage());
/* 114 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private PlayerItemModel downloadModel(String modelPath) {
/* 120 */     String s = HttpUtils.getPlayerItemsUrl() + "/" + modelPath;
/*     */ 
/*     */     
/*     */     try {
/* 124 */       byte[] abyte = HttpPipeline.get(s, Minecraft.getMinecraft().getProxy());
/* 125 */       String s1 = new String(abyte, "ASCII");
/* 126 */       JsonParser jsonparser = new JsonParser();
/* 127 */       JsonObject jsonobject = (JsonObject)jsonparser.parse(s1);
/* 128 */       PlayerItemModel playeritemmodel = PlayerItemParser.parseItemModel(jsonobject);
/* 129 */       return playeritemmodel;
/*     */     }
/* 131 */     catch (Exception exception) {
/*     */       
/* 133 */       Config.warn("Error loading item model " + modelPath + ": " + exception.getClass().getName() + ": " + exception.getMessage());
/* 134 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\player\PlayerConfigurationParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */