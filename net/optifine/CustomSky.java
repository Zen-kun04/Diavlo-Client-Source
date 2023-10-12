/*     */ package net.optifine;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import net.minecraft.client.renderer.texture.ITextureObject;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.src.Config;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.World;
/*     */ import net.optifine.render.Blender;
/*     */ import net.optifine.util.PropertiesOrdered;
/*     */ import net.optifine.util.TextureUtils;
/*     */ 
/*     */ 
/*     */ public class CustomSky
/*     */ {
/*  21 */   private static CustomSkyLayer[][] worldSkyLayers = (CustomSkyLayer[][])null;
/*     */ 
/*     */   
/*     */   public static void reset() {
/*  25 */     worldSkyLayers = (CustomSkyLayer[][])null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void update() {
/*  30 */     reset();
/*     */     
/*  32 */     if (Config.isCustomSky())
/*     */     {
/*  34 */       worldSkyLayers = readCustomSkies();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static CustomSkyLayer[][] readCustomSkies() {
/*  40 */     CustomSkyLayer[][] acustomskylayer = new CustomSkyLayer[10][0];
/*  41 */     String s = "mcpatcher/sky/world";
/*  42 */     int i = -1;
/*     */     
/*  44 */     for (int j = 0; j < acustomskylayer.length; j++) {
/*     */       
/*  46 */       String s1 = s + j + "/sky";
/*  47 */       List<CustomSkyLayer> list = new ArrayList();
/*     */       
/*  49 */       for (int k = 1; k < 1000; k++) {
/*     */         
/*  51 */         String s2 = s1 + k + ".properties";
/*     */ 
/*     */         
/*     */         try {
/*  55 */           ResourceLocation resourcelocation = new ResourceLocation(s2);
/*  56 */           InputStream inputstream = Config.getResourceStream(resourcelocation);
/*     */           
/*  58 */           if (inputstream == null) {
/*     */             break;
/*     */           }
/*     */ 
/*     */           
/*  63 */           PropertiesOrdered propertiesOrdered = new PropertiesOrdered();
/*  64 */           propertiesOrdered.load(inputstream);
/*  65 */           inputstream.close();
/*  66 */           Config.dbg("CustomSky properties: " + s2);
/*  67 */           String s3 = s1 + k + ".png";
/*  68 */           CustomSkyLayer customskylayer = new CustomSkyLayer((Properties)propertiesOrdered, s3);
/*     */           
/*  70 */           if (customskylayer.isValid(s2)) {
/*     */             
/*  72 */             ResourceLocation resourcelocation1 = new ResourceLocation(customskylayer.source);
/*  73 */             ITextureObject itextureobject = TextureUtils.getTexture(resourcelocation1);
/*     */             
/*  75 */             if (itextureobject == null)
/*     */             {
/*  77 */               Config.log("CustomSky: Texture not found: " + resourcelocation1);
/*     */             }
/*     */             else
/*     */             {
/*  81 */               customskylayer.textureId = itextureobject.getGlTextureId();
/*  82 */               list.add(customskylayer);
/*  83 */               inputstream.close();
/*     */             }
/*     */           
/*     */           } 
/*  87 */         } catch (FileNotFoundException var15) {
/*     */           
/*     */           break;
/*     */         }
/*  91 */         catch (IOException ioexception) {
/*     */           
/*  93 */           ioexception.printStackTrace();
/*     */         } 
/*     */       } 
/*     */       
/*  97 */       if (list.size() > 0) {
/*     */         
/*  99 */         CustomSkyLayer[] acustomskylayer2 = list.<CustomSkyLayer>toArray(new CustomSkyLayer[list.size()]);
/* 100 */         acustomskylayer[j] = acustomskylayer2;
/* 101 */         i = j;
/*     */       } 
/*     */     } 
/*     */     
/* 105 */     if (i < 0)
/*     */     {
/* 107 */       return (CustomSkyLayer[][])null;
/*     */     }
/*     */ 
/*     */     
/* 111 */     int l = i + 1;
/* 112 */     CustomSkyLayer[][] acustomskylayer1 = new CustomSkyLayer[l][0];
/*     */     
/* 114 */     for (int i1 = 0; i1 < acustomskylayer1.length; i1++)
/*     */     {
/* 116 */       acustomskylayer1[i1] = acustomskylayer[i1];
/*     */     }
/*     */     
/* 119 */     return acustomskylayer1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void renderSky(World world, TextureManager re, float partialTicks) {
/* 125 */     if (worldSkyLayers != null) {
/*     */       
/* 127 */       int i = world.provider.getDimensionId();
/*     */       
/* 129 */       if (i >= 0 && i < worldSkyLayers.length) {
/*     */         
/* 131 */         CustomSkyLayer[] acustomskylayer = worldSkyLayers[i];
/*     */         
/* 133 */         if (acustomskylayer != null) {
/*     */           
/* 135 */           long j = world.getWorldTime();
/* 136 */           int k = (int)(j % 24000L);
/* 137 */           float f = world.getCelestialAngle(partialTicks);
/* 138 */           float f1 = world.getRainStrength(partialTicks);
/* 139 */           float f2 = world.getThunderStrength(partialTicks);
/*     */           
/* 141 */           if (f1 > 0.0F)
/*     */           {
/* 143 */             f2 /= f1;
/*     */           }
/*     */           
/* 146 */           for (int l = 0; l < acustomskylayer.length; l++) {
/*     */             
/* 148 */             CustomSkyLayer customskylayer = acustomskylayer[l];
/*     */             
/* 150 */             if (customskylayer.isActive(world, k))
/*     */             {
/* 152 */               customskylayer.render(world, k, f, f1, f2);
/*     */             }
/*     */           } 
/*     */           
/* 156 */           float f3 = 1.0F - f1;
/* 157 */           Blender.clearBlend(f3);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean hasSkyLayers(World world) {
/* 165 */     if (worldSkyLayers == null)
/*     */     {
/* 167 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 171 */     int i = world.provider.getDimensionId();
/*     */     
/* 173 */     if (i >= 0 && i < worldSkyLayers.length) {
/*     */       
/* 175 */       CustomSkyLayer[] acustomskylayer = worldSkyLayers[i];
/* 176 */       return (acustomskylayer == null) ? false : ((acustomskylayer.length > 0));
/*     */     } 
/*     */ 
/*     */     
/* 180 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\CustomSky.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */