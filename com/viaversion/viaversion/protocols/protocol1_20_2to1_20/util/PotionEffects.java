/*    */ package com.viaversion.viaversion.protocols.protocol1_20_2to1_20.util;
/*    */ 
/*    */ import com.viaversion.viaversion.libs.fastutil.objects.Object2IntMap;
/*    */ import com.viaversion.viaversion.libs.fastutil.objects.Object2IntOpenHashMap;
/*    */ import com.viaversion.viaversion.util.Key;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class PotionEffects
/*    */ {
/* 27 */   private static final Object2IntMap<String> KEY_TO_ID = (Object2IntMap<String>)new Object2IntOpenHashMap();
/* 28 */   private static final String[] POTION_EFFECTS = new String[] { "", "speed", "slowness", "haste", "mining_fatigue", "strength", "instant_health", "instant_damage", "jump_boost", "nausea", "regeneration", "resistance", "fire_resistance", "water_breathing", "invisibility", "blindness", "night_vision", "hunger", "weakness", "poison", "wither", "health_boost", "absorption", "saturation", "glowing", "levitation", "luck", "unluck", "slow_falling", "conduit_power", "dolphins_grace", "bad_omen", "hero_of_the_village", "darkness" };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 66 */     for (int i = 1; i < POTION_EFFECTS.length; i++) {
/* 67 */       String effect = POTION_EFFECTS[i];
/* 68 */       KEY_TO_ID.put(effect, i);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static String idToKey(int id) {
/* 73 */     return (id >= 1 && id < POTION_EFFECTS.length) ? Key.namespaced(POTION_EFFECTS[id]) : null;
/*    */   }
/*    */   
/*    */   public static String idToKeyOrLuck(int id) {
/* 77 */     return (id >= 1 && id < POTION_EFFECTS.length) ? Key.namespaced(POTION_EFFECTS[id]) : "minecraft:luck";
/*    */   }
/*    */   
/*    */   public static int keyToId(String key) {
/* 81 */     return KEY_TO_ID.getInt(Key.stripMinecraftNamespace(key));
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\protocol1_20_2to1_2\\util\PotionEffects.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */