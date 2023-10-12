/*     */ package net.minecraft.world;
/*     */ 
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ 
/*     */ public class GameRules
/*     */ {
/*   9 */   private TreeMap<String, Value> theGameRules = new TreeMap<>();
/*     */ 
/*     */   
/*     */   public GameRules() {
/*  13 */     addGameRule("doFireTick", "true", ValueType.BOOLEAN_VALUE);
/*  14 */     addGameRule("mobGriefing", "true", ValueType.BOOLEAN_VALUE);
/*  15 */     addGameRule("keepInventory", "false", ValueType.BOOLEAN_VALUE);
/*  16 */     addGameRule("doMobSpawning", "true", ValueType.BOOLEAN_VALUE);
/*  17 */     addGameRule("doMobLoot", "true", ValueType.BOOLEAN_VALUE);
/*  18 */     addGameRule("doTileDrops", "true", ValueType.BOOLEAN_VALUE);
/*  19 */     addGameRule("doEntityDrops", "true", ValueType.BOOLEAN_VALUE);
/*  20 */     addGameRule("commandBlockOutput", "true", ValueType.BOOLEAN_VALUE);
/*  21 */     addGameRule("naturalRegeneration", "true", ValueType.BOOLEAN_VALUE);
/*  22 */     addGameRule("doDaylightCycle", "true", ValueType.BOOLEAN_VALUE);
/*  23 */     addGameRule("logAdminCommands", "true", ValueType.BOOLEAN_VALUE);
/*  24 */     addGameRule("showDeathMessages", "true", ValueType.BOOLEAN_VALUE);
/*  25 */     addGameRule("randomTickSpeed", "3", ValueType.NUMERICAL_VALUE);
/*  26 */     addGameRule("sendCommandFeedback", "true", ValueType.BOOLEAN_VALUE);
/*  27 */     addGameRule("reducedDebugInfo", "false", ValueType.BOOLEAN_VALUE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addGameRule(String key, String value, ValueType type) {
/*  32 */     this.theGameRules.put(key, new Value(value, type));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOrCreateGameRule(String key, String ruleValue) {
/*  37 */     Value gamerules$value = this.theGameRules.get(key);
/*     */     
/*  39 */     if (gamerules$value != null) {
/*     */       
/*  41 */       gamerules$value.setValue(ruleValue);
/*     */     }
/*     */     else {
/*     */       
/*  45 */       addGameRule(key, ruleValue, ValueType.ANY_VALUE);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getString(String name) {
/*  51 */     Value gamerules$value = this.theGameRules.get(name);
/*  52 */     return (gamerules$value != null) ? gamerules$value.getString() : "";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getBoolean(String name) {
/*  57 */     Value gamerules$value = this.theGameRules.get(name);
/*  58 */     return (gamerules$value != null) ? gamerules$value.getBoolean() : false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInt(String name) {
/*  63 */     Value gamerules$value = this.theGameRules.get(name);
/*  64 */     return (gamerules$value != null) ? gamerules$value.getInt() : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTTagCompound writeToNBT() {
/*  69 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/*     */     
/*  71 */     for (String s : this.theGameRules.keySet()) {
/*     */       
/*  73 */       Value gamerules$value = this.theGameRules.get(s);
/*  74 */       nbttagcompound.setString(s, gamerules$value.getString());
/*     */     } 
/*     */     
/*  77 */     return nbttagcompound;
/*     */   }
/*     */ 
/*     */   
/*     */   public void readFromNBT(NBTTagCompound nbt) {
/*  82 */     for (String s : nbt.getKeySet()) {
/*     */       
/*  84 */       String s1 = nbt.getString(s);
/*  85 */       setOrCreateGameRule(s, s1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getRules() {
/*  91 */     Set<String> set = this.theGameRules.keySet();
/*  92 */     return set.<String>toArray(new String[set.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasRule(String name) {
/*  97 */     return this.theGameRules.containsKey(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean areSameType(String key, ValueType otherValue) {
/* 102 */     Value gamerules$value = this.theGameRules.get(key);
/* 103 */     return (gamerules$value != null && (gamerules$value.getType() == otherValue || otherValue == ValueType.ANY_VALUE));
/*     */   }
/*     */ 
/*     */   
/*     */   static class Value
/*     */   {
/*     */     private String valueString;
/*     */     private boolean valueBoolean;
/*     */     private int valueInteger;
/*     */     private double valueDouble;
/*     */     private final GameRules.ValueType type;
/*     */     
/*     */     public Value(String value, GameRules.ValueType type) {
/* 116 */       this.type = type;
/* 117 */       setValue(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setValue(String value) {
/* 122 */       this.valueString = value;
/*     */       
/* 124 */       if (value != null) {
/*     */         
/* 126 */         if (value.equals("false")) {
/*     */           
/* 128 */           this.valueBoolean = false;
/*     */           
/*     */           return;
/*     */         } 
/* 132 */         if (value.equals("true")) {
/*     */           
/* 134 */           this.valueBoolean = true;
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/* 139 */       this.valueBoolean = Boolean.parseBoolean(value);
/* 140 */       this.valueInteger = this.valueBoolean ? 1 : 0;
/*     */ 
/*     */       
/*     */       try {
/* 144 */         this.valueInteger = Integer.parseInt(value);
/*     */       }
/* 146 */       catch (NumberFormatException numberFormatException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 153 */         this.valueDouble = Double.parseDouble(value);
/*     */       }
/* 155 */       catch (NumberFormatException numberFormatException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getString() {
/* 163 */       return this.valueString;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean getBoolean() {
/* 168 */       return this.valueBoolean;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getInt() {
/* 173 */       return this.valueInteger;
/*     */     }
/*     */ 
/*     */     
/*     */     public GameRules.ValueType getType() {
/* 178 */       return this.type;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum ValueType
/*     */   {
/* 184 */     ANY_VALUE,
/* 185 */     BOOLEAN_VALUE,
/* 186 */     NUMERICAL_VALUE;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\GameRules.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */