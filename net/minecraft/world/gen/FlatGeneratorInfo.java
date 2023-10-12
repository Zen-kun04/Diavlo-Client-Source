/*     */ package net.minecraft.world.gen;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ 
/*     */ 
/*     */ public class FlatGeneratorInfo
/*     */ {
/*  15 */   private final List<FlatLayerInfo> flatLayers = Lists.newArrayList();
/*  16 */   private final Map<String, Map<String, String>> worldFeatures = Maps.newHashMap();
/*     */   
/*     */   private int biomeToUse;
/*     */   
/*     */   public int getBiome() {
/*  21 */     return this.biomeToUse;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBiome(int biome) {
/*  26 */     this.biomeToUse = biome;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Map<String, String>> getWorldFeatures() {
/*  31 */     return this.worldFeatures;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<FlatLayerInfo> getFlatLayers() {
/*  36 */     return this.flatLayers;
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_82645_d() {
/*  41 */     int i = 0;
/*     */     
/*  43 */     for (FlatLayerInfo flatlayerinfo : this.flatLayers) {
/*     */       
/*  45 */       flatlayerinfo.setMinY(i);
/*  46 */       i += flatlayerinfo.getLayerCount();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  52 */     StringBuilder stringbuilder = new StringBuilder();
/*  53 */     stringbuilder.append(3);
/*  54 */     stringbuilder.append(";");
/*     */     
/*  56 */     for (int i = 0; i < this.flatLayers.size(); i++) {
/*     */       
/*  58 */       if (i > 0)
/*     */       {
/*  60 */         stringbuilder.append(",");
/*     */       }
/*     */       
/*  63 */       stringbuilder.append(((FlatLayerInfo)this.flatLayers.get(i)).toString());
/*     */     } 
/*     */     
/*  66 */     stringbuilder.append(";");
/*  67 */     stringbuilder.append(this.biomeToUse);
/*     */     
/*  69 */     if (!this.worldFeatures.isEmpty()) {
/*     */       
/*  71 */       stringbuilder.append(";");
/*  72 */       int k = 0;
/*     */       
/*  74 */       for (Map.Entry<String, Map<String, String>> entry : this.worldFeatures.entrySet()) {
/*     */         
/*  76 */         if (k++ > 0)
/*     */         {
/*  78 */           stringbuilder.append(",");
/*     */         }
/*     */         
/*  81 */         stringbuilder.append(((String)entry.getKey()).toLowerCase());
/*  82 */         Map<String, String> map = entry.getValue();
/*     */         
/*  84 */         if (!map.isEmpty())
/*     */         {
/*  86 */           stringbuilder.append("(");
/*  87 */           int j = 0;
/*     */           
/*  89 */           for (Map.Entry<String, String> entry1 : map.entrySet()) {
/*     */             
/*  91 */             if (j++ > 0)
/*     */             {
/*  93 */               stringbuilder.append(" ");
/*     */             }
/*     */             
/*  96 */             stringbuilder.append(entry1.getKey());
/*  97 */             stringbuilder.append("=");
/*  98 */             stringbuilder.append(entry1.getValue());
/*     */           } 
/*     */           
/* 101 */           stringbuilder.append(")");
/*     */         }
/*     */       
/*     */       } 
/*     */     } else {
/*     */       
/* 107 */       stringbuilder.append(";");
/*     */     } 
/*     */     
/* 110 */     return stringbuilder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static FlatLayerInfo func_180715_a(int p_180715_0_, String p_180715_1_, int p_180715_2_) {
/* 115 */     String[] astring = (p_180715_0_ >= 3) ? p_180715_1_.split("\\*", 2) : p_180715_1_.split("x", 2);
/* 116 */     int i = 1;
/* 117 */     int j = 0;
/*     */     
/* 119 */     if (astring.length == 2) {
/*     */       
/*     */       try {
/*     */         
/* 123 */         i = Integer.parseInt(astring[0]);
/*     */         
/* 125 */         if (p_180715_2_ + i >= 256)
/*     */         {
/* 127 */           i = 256 - p_180715_2_;
/*     */         }
/*     */         
/* 130 */         if (i < 0)
/*     */         {
/* 132 */           i = 0;
/*     */         }
/*     */       }
/* 135 */       catch (Throwable var8) {
/*     */         
/* 137 */         return null;
/*     */       } 
/*     */     }
/*     */     
/* 141 */     Block block = null;
/*     */ 
/*     */     
/*     */     try {
/* 145 */       String s = astring[astring.length - 1];
/*     */       
/* 147 */       if (p_180715_0_ < 3) {
/*     */         
/* 149 */         astring = s.split(":", 2);
/*     */         
/* 151 */         if (astring.length > 1)
/*     */         {
/* 153 */           j = Integer.parseInt(astring[1]);
/*     */         }
/*     */         
/* 156 */         block = Block.getBlockById(Integer.parseInt(astring[0]));
/*     */       }
/*     */       else {
/*     */         
/* 160 */         astring = s.split(":", 3);
/* 161 */         block = (astring.length > 1) ? Block.getBlockFromName(astring[0] + ":" + astring[1]) : null;
/*     */         
/* 163 */         if (block != null) {
/*     */           
/* 165 */           j = (astring.length > 2) ? Integer.parseInt(astring[2]) : 0;
/*     */         }
/*     */         else {
/*     */           
/* 169 */           block = Block.getBlockFromName(astring[0]);
/*     */           
/* 171 */           if (block != null)
/*     */           {
/* 173 */             j = (astring.length > 1) ? Integer.parseInt(astring[1]) : 0;
/*     */           }
/*     */         } 
/*     */         
/* 177 */         if (block == null)
/*     */         {
/* 179 */           return null;
/*     */         }
/*     */       } 
/*     */       
/* 183 */       if (block == Blocks.air)
/*     */       {
/* 185 */         j = 0;
/*     */       }
/*     */       
/* 188 */       if (j < 0 || j > 15)
/*     */       {
/* 190 */         j = 0;
/*     */       }
/*     */     }
/* 193 */     catch (Throwable var9) {
/*     */       
/* 195 */       return null;
/*     */     } 
/*     */     
/* 198 */     FlatLayerInfo flatlayerinfo = new FlatLayerInfo(p_180715_0_, i, block, j);
/* 199 */     flatlayerinfo.setMinY(p_180715_2_);
/* 200 */     return flatlayerinfo;
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<FlatLayerInfo> func_180716_a(int p_180716_0_, String p_180716_1_) {
/* 205 */     if (p_180716_1_ != null && p_180716_1_.length() >= 1) {
/*     */       
/* 207 */       List<FlatLayerInfo> list = Lists.newArrayList();
/* 208 */       String[] astring = p_180716_1_.split(",");
/* 209 */       int i = 0;
/*     */       
/* 211 */       for (String s : astring) {
/*     */         
/* 213 */         FlatLayerInfo flatlayerinfo = func_180715_a(p_180716_0_, s, i);
/*     */         
/* 215 */         if (flatlayerinfo == null)
/*     */         {
/* 217 */           return null;
/*     */         }
/*     */         
/* 220 */         list.add(flatlayerinfo);
/* 221 */         i += flatlayerinfo.getLayerCount();
/*     */       } 
/*     */       
/* 224 */       return list;
/*     */     } 
/*     */ 
/*     */     
/* 228 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static FlatGeneratorInfo createFlatGeneratorFromString(String flatGeneratorSettings) {
/* 234 */     if (flatGeneratorSettings == null)
/*     */     {
/* 236 */       return getDefaultFlatGenerator();
/*     */     }
/*     */ 
/*     */     
/* 240 */     String[] astring = flatGeneratorSettings.split(";", -1);
/* 241 */     int i = (astring.length == 1) ? 0 : MathHelper.parseIntWithDefault(astring[0], 0);
/*     */     
/* 243 */     if (i >= 0 && i <= 3) {
/*     */       
/* 245 */       FlatGeneratorInfo flatgeneratorinfo = new FlatGeneratorInfo();
/* 246 */       int j = (astring.length == 1) ? 0 : 1;
/* 247 */       List<FlatLayerInfo> list = func_180716_a(i, astring[j++]);
/*     */       
/* 249 */       if (list != null && !list.isEmpty()) {
/*     */         
/* 251 */         flatgeneratorinfo.getFlatLayers().addAll(list);
/* 252 */         flatgeneratorinfo.func_82645_d();
/* 253 */         int k = BiomeGenBase.plains.biomeID;
/*     */         
/* 255 */         if (i > 0 && astring.length > j)
/*     */         {
/* 257 */           k = MathHelper.parseIntWithDefault(astring[j++], k);
/*     */         }
/*     */         
/* 260 */         flatgeneratorinfo.setBiome(k);
/*     */         
/* 262 */         if (i > 0 && astring.length > j) {
/*     */           
/* 264 */           String[] astring1 = astring[j++].toLowerCase().split(",");
/*     */           
/* 266 */           for (String s : astring1) {
/*     */             
/* 268 */             String[] astring2 = s.split("\\(", 2);
/* 269 */             Map<String, String> map = Maps.newHashMap();
/*     */             
/* 271 */             if (astring2[0].length() > 0) {
/*     */               
/* 273 */               flatgeneratorinfo.getWorldFeatures().put(astring2[0], map);
/*     */               
/* 275 */               if (astring2.length > 1 && astring2[1].endsWith(")") && astring2[1].length() > 1) {
/*     */                 
/* 277 */                 String[] astring3 = astring2[1].substring(0, astring2[1].length() - 1).split(" ");
/*     */                 
/* 279 */                 for (int l = 0; l < astring3.length; l++)
/*     */                 {
/* 281 */                   String[] astring4 = astring3[l].split("=", 2);
/*     */                   
/* 283 */                   if (astring4.length == 2)
/*     */                   {
/* 285 */                     map.put(astring4[0], astring4[1]);
/*     */                   }
/*     */                 }
/*     */               
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } else {
/*     */           
/* 294 */           flatgeneratorinfo.getWorldFeatures().put("village", Maps.newHashMap());
/*     */         } 
/*     */         
/* 297 */         return flatgeneratorinfo;
/*     */       } 
/*     */ 
/*     */       
/* 301 */       return getDefaultFlatGenerator();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 306 */     return getDefaultFlatGenerator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FlatGeneratorInfo getDefaultFlatGenerator() {
/* 313 */     FlatGeneratorInfo flatgeneratorinfo = new FlatGeneratorInfo();
/* 314 */     flatgeneratorinfo.setBiome(BiomeGenBase.plains.biomeID);
/* 315 */     flatgeneratorinfo.getFlatLayers().add(new FlatLayerInfo(1, Blocks.bedrock));
/* 316 */     flatgeneratorinfo.getFlatLayers().add(new FlatLayerInfo(2, Blocks.dirt));
/* 317 */     flatgeneratorinfo.getFlatLayers().add(new FlatLayerInfo(1, (Block)Blocks.grass));
/* 318 */     flatgeneratorinfo.func_82645_d();
/* 319 */     flatgeneratorinfo.getWorldFeatures().put("village", Maps.newHashMap());
/* 320 */     return flatgeneratorinfo;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\FlatGeneratorInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */