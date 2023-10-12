/*     */ package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data;
/*     */ 
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.HashBiMap;
/*     */ import com.google.common.io.CharStreams;
/*     */ import com.viaversion.viaversion.api.Via;
/*     */ import com.viaversion.viaversion.api.data.BiMappings;
/*     */ import com.viaversion.viaversion.api.data.Int2IntMapBiMappings;
/*     */ import com.viaversion.viaversion.api.data.MappingDataBase;
/*     */ import com.viaversion.viaversion.api.data.MappingDataLoader;
/*     */ import com.viaversion.viaversion.api.data.Mappings;
/*     */ import com.viaversion.viaversion.libs.gson.JsonElement;
/*     */ import com.viaversion.viaversion.libs.gson.JsonObject;
/*     */ import com.viaversion.viaversion.libs.gson.reflect.TypeToken;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntArrayTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
/*     */ import com.viaversion.viaversion.util.GsonUtil;
/*     */ import com.viaversion.viaversion.util.Int2IntBiHashMap;
/*     */ import com.viaversion.viaversion.util.Int2IntBiMap;
/*     */ import com.viaversion.viaversion.util.Key;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MappingData
/*     */   extends MappingDataBase
/*     */ {
/*  48 */   private final Map<String, int[]> blockTags = (Map)new HashMap<>();
/*  49 */   private final Map<String, int[]> itemTags = (Map)new HashMap<>();
/*  50 */   private final Map<String, int[]> fluidTags = (Map)new HashMap<>();
/*  51 */   private final BiMap<Short, String> oldEnchantmentsIds = (BiMap<Short, String>)HashBiMap.create();
/*  52 */   private final Map<String, String> translateMapping = new HashMap<>();
/*  53 */   private final Map<String, String> mojangTranslation = new HashMap<>();
/*  54 */   private final BiMap<String, String> channelMappings = (BiMap<String, String>)HashBiMap.create();
/*     */   
/*     */   public MappingData() {
/*  57 */     super("1.12", "1.13");
/*     */   }
/*     */   
/*     */   protected void loadExtras(CompoundTag data) {
/*     */     String[] unmappedTranslationLines;
/*  62 */     loadTags(this.blockTags, (CompoundTag)data.get("block_tags"));
/*  63 */     loadTags(this.itemTags, (CompoundTag)data.get("item_tags"));
/*  64 */     loadTags(this.fluidTags, (CompoundTag)data.get("fluid_tags"));
/*     */     
/*  66 */     CompoundTag legacyEnchantments = (CompoundTag)data.get("legacy_enchantments");
/*  67 */     loadEnchantments((Map<Short, String>)this.oldEnchantmentsIds, legacyEnchantments);
/*     */ 
/*     */     
/*  70 */     if (Via.getConfig().isSnowCollisionFix()) {
/*  71 */       this.blockMappings.setNewId(1248, 3416);
/*     */     }
/*     */ 
/*     */     
/*  75 */     if (Via.getConfig().isInfestedBlocksFix()) {
/*  76 */       this.blockMappings.setNewId(1552, 1);
/*  77 */       this.blockMappings.setNewId(1553, 14);
/*  78 */       this.blockMappings.setNewId(1554, 3983);
/*  79 */       this.blockMappings.setNewId(1555, 3984);
/*  80 */       this.blockMappings.setNewId(1556, 3985);
/*  81 */       this.blockMappings.setNewId(1557, 3986);
/*     */     } 
/*     */     
/*  84 */     JsonObject object = MappingDataLoader.loadFromDataDir("channelmappings-1.13.json");
/*  85 */     if (object != null) {
/*  86 */       for (Map.Entry<String, JsonElement> entry : (Iterable<Map.Entry<String, JsonElement>>)object.entrySet()) {
/*  87 */         String oldChannel = entry.getKey();
/*  88 */         String newChannel = ((JsonElement)entry.getValue()).getAsString();
/*  89 */         if (!isValid1_13Channel(newChannel)) {
/*  90 */           Via.getPlatform().getLogger().warning("Channel '" + newChannel + "' is not a valid 1.13 plugin channel, please check your configuration!");
/*     */           continue;
/*     */         } 
/*  93 */         this.channelMappings.put(oldChannel, newChannel);
/*     */       } 
/*     */     }
/*     */     
/*  97 */     Map<String, String> translationMappingData = (Map<String, String>)GsonUtil.getGson().fromJson(new InputStreamReader(MappingData.class
/*  98 */           .getClassLoader().getResourceAsStream("assets/viaversion/data/mapping-lang-1.12-1.13.json")), (new TypeToken<Map<String, String>>() {
/*     */         
/* 100 */         }).getType());
/*     */ 
/*     */     
/* 103 */     try (Reader reader = new InputStreamReader(MappingData.class.getClassLoader()
/* 104 */           .getResourceAsStream("assets/viaversion/data/en_US.properties"), StandardCharsets.UTF_8)) {
/* 105 */       unmappedTranslationLines = CharStreams.toString(reader).split("\n");
/* 106 */     } catch (IOException e) {
/* 107 */       throw new RuntimeException(e);
/*     */     } 
/*     */     
/* 110 */     for (String line : unmappedTranslationLines) {
/* 111 */       if (!line.isEmpty()) {
/*     */ 
/*     */ 
/*     */         
/* 115 */         String[] keyAndTranslation = line.split("=", 2);
/* 116 */         if (keyAndTranslation.length == 2) {
/*     */ 
/*     */ 
/*     */           
/* 120 */           String key = keyAndTranslation[0];
/* 121 */           String translation = keyAndTranslation[1].replaceAll("%(\\d\\$)?d", "%$1s").trim();
/* 122 */           this.mojangTranslation.put(key, translation);
/*     */ 
/*     */           
/* 125 */           if (translationMappingData.containsKey(key)) {
/* 126 */             String mappedKey = translationMappingData.get(key);
/* 127 */             this.translateMapping.put(key, (mappedKey != null) ? mappedKey : key);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Mappings loadMappings(CompoundTag data, String key) {
/* 135 */     if (key.equals("blocks"))
/* 136 */       return super.loadMappings(data, "blockstates"); 
/* 137 */     if (key.equals("blockstates")) {
/* 138 */       return null;
/*     */     }
/* 140 */     return super.loadMappings(data, key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BiMappings loadBiMappings(CompoundTag data, String key) {
/* 147 */     if (key.equals("items")) {
/* 148 */       return (BiMappings)MappingDataLoader.loadMappings(data, "items", size -> { Int2IntBiHashMap map = new Int2IntBiHashMap(size); map.defaultReturnValue(-1); return map; }Int2IntBiHashMap::put, (v, mappedSize) -> Int2IntMapBiMappings.of((Int2IntBiMap)v));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 154 */     return super.loadBiMappings(data, key);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String validateNewChannel(String newId) {
/* 159 */     if (!isValid1_13Channel(newId)) {
/* 160 */       return null;
/*     */     }
/* 162 */     int separatorIndex = newId.indexOf(':');
/*     */     
/* 164 */     if (separatorIndex == -1)
/* 165 */       return "minecraft:" + newId; 
/* 166 */     if (separatorIndex == 0) {
/* 167 */       return "minecraft" + newId;
/*     */     }
/* 169 */     return newId;
/*     */   }
/*     */   
/*     */   public static boolean isValid1_13Channel(String channelId) {
/* 173 */     return channelId.matches("([0-9a-z_.-]+:)?[0-9a-z_/.-]+");
/*     */   }
/*     */   
/*     */   private void loadTags(Map<String, int[]> output, CompoundTag newTags) {
/* 177 */     for (Map.Entry<String, Tag> entry : (Iterable<Map.Entry<String, Tag>>)newTags.entrySet()) {
/* 178 */       IntArrayTag ids = (IntArrayTag)entry.getValue();
/* 179 */       output.put(Key.namespaced(entry.getKey()), ids.getValue());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void loadEnchantments(Map<Short, String> output, CompoundTag enchantments) {
/* 184 */     for (Map.Entry<String, Tag> enty : (Iterable<Map.Entry<String, Tag>>)enchantments.entrySet()) {
/* 185 */       output.put(Short.valueOf(Short.parseShort(enty.getKey())), ((StringTag)enty.getValue()).getValue());
/*     */     }
/*     */   }
/*     */   
/*     */   public Map<String, int[]> getBlockTags() {
/* 190 */     return this.blockTags;
/*     */   }
/*     */   
/*     */   public Map<String, int[]> getItemTags() {
/* 194 */     return this.itemTags;
/*     */   }
/*     */   
/*     */   public Map<String, int[]> getFluidTags() {
/* 198 */     return this.fluidTags;
/*     */   }
/*     */   
/*     */   public BiMap<Short, String> getOldEnchantmentsIds() {
/* 202 */     return this.oldEnchantmentsIds;
/*     */   }
/*     */   
/*     */   public Map<String, String> getTranslateMapping() {
/* 206 */     return this.translateMapping;
/*     */   }
/*     */   
/*     */   public Map<String, String> getMojangTranslation() {
/* 210 */     return this.mojangTranslation;
/*     */   }
/*     */   
/*     */   public BiMap<String, String> getChannelMappings() {
/* 214 */     return this.channelMappings;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\protocol1_13to1_12_2\data\MappingData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */