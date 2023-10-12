/*     */ package net.minecraft.util;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ 
/*     */ public class JsonUtils {
/*     */   public static boolean isString(JsonObject p_151205_0_, String p_151205_1_) {
/*   9 */     return !isJsonPrimitive(p_151205_0_, p_151205_1_) ? false : p_151205_0_.getAsJsonPrimitive(p_151205_1_).isString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isString(JsonElement p_151211_0_) {
/*  14 */     return !p_151211_0_.isJsonPrimitive() ? false : p_151211_0_.getAsJsonPrimitive().isString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isBoolean(JsonObject p_180199_0_, String p_180199_1_) {
/*  19 */     return !isJsonPrimitive(p_180199_0_, p_180199_1_) ? false : p_180199_0_.getAsJsonPrimitive(p_180199_1_).isBoolean();
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isJsonArray(JsonObject p_151202_0_, String p_151202_1_) {
/*  24 */     return !hasField(p_151202_0_, p_151202_1_) ? false : p_151202_0_.get(p_151202_1_).isJsonArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isJsonPrimitive(JsonObject p_151201_0_, String p_151201_1_) {
/*  29 */     return !hasField(p_151201_0_, p_151201_1_) ? false : p_151201_0_.get(p_151201_1_).isJsonPrimitive();
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean hasField(JsonObject p_151204_0_, String p_151204_1_) {
/*  34 */     return (p_151204_0_ == null) ? false : ((p_151204_0_.get(p_151204_1_) != null));
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getString(JsonElement p_151206_0_, String p_151206_1_) {
/*  39 */     if (p_151206_0_.isJsonPrimitive())
/*     */     {
/*  41 */       return p_151206_0_.getAsString();
/*     */     }
/*     */ 
/*     */     
/*  45 */     throw new JsonSyntaxException("Expected " + p_151206_1_ + " to be a string, was " + toString(p_151206_0_));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getString(JsonObject p_151200_0_, String p_151200_1_) {
/*  51 */     if (p_151200_0_.has(p_151200_1_))
/*     */     {
/*  53 */       return getString(p_151200_0_.get(p_151200_1_), p_151200_1_);
/*     */     }
/*     */ 
/*     */     
/*  57 */     throw new JsonSyntaxException("Missing " + p_151200_1_ + ", expected to find a string");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getString(JsonObject p_151219_0_, String p_151219_1_, String p_151219_2_) {
/*  63 */     return p_151219_0_.has(p_151219_1_) ? getString(p_151219_0_.get(p_151219_1_), p_151219_1_) : p_151219_2_;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean getBoolean(JsonElement p_151216_0_, String p_151216_1_) {
/*  68 */     if (p_151216_0_.isJsonPrimitive())
/*     */     {
/*  70 */       return p_151216_0_.getAsBoolean();
/*     */     }
/*     */ 
/*     */     
/*  74 */     throw new JsonSyntaxException("Expected " + p_151216_1_ + " to be a Boolean, was " + toString(p_151216_0_));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean getBoolean(JsonObject p_151212_0_, String p_151212_1_) {
/*  80 */     if (p_151212_0_.has(p_151212_1_))
/*     */     {
/*  82 */       return getBoolean(p_151212_0_.get(p_151212_1_), p_151212_1_);
/*     */     }
/*     */ 
/*     */     
/*  86 */     throw new JsonSyntaxException("Missing " + p_151212_1_ + ", expected to find a Boolean");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean getBoolean(JsonObject p_151209_0_, String p_151209_1_, boolean p_151209_2_) {
/*  92 */     return p_151209_0_.has(p_151209_1_) ? getBoolean(p_151209_0_.get(p_151209_1_), p_151209_1_) : p_151209_2_;
/*     */   }
/*     */ 
/*     */   
/*     */   public static float getFloat(JsonElement p_151220_0_, String p_151220_1_) {
/*  97 */     if (p_151220_0_.isJsonPrimitive() && p_151220_0_.getAsJsonPrimitive().isNumber())
/*     */     {
/*  99 */       return p_151220_0_.getAsFloat();
/*     */     }
/*     */ 
/*     */     
/* 103 */     throw new JsonSyntaxException("Expected " + p_151220_1_ + " to be a Float, was " + toString(p_151220_0_));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static float getFloat(JsonObject p_151217_0_, String p_151217_1_) {
/* 109 */     if (p_151217_0_.has(p_151217_1_))
/*     */     {
/* 111 */       return getFloat(p_151217_0_.get(p_151217_1_), p_151217_1_);
/*     */     }
/*     */ 
/*     */     
/* 115 */     throw new JsonSyntaxException("Missing " + p_151217_1_ + ", expected to find a Float");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static float getFloat(JsonObject p_151221_0_, String p_151221_1_, float p_151221_2_) {
/* 121 */     return p_151221_0_.has(p_151221_1_) ? getFloat(p_151221_0_.get(p_151221_1_), p_151221_1_) : p_151221_2_;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getInt(JsonElement p_151215_0_, String p_151215_1_) {
/* 126 */     if (p_151215_0_.isJsonPrimitive() && p_151215_0_.getAsJsonPrimitive().isNumber())
/*     */     {
/* 128 */       return p_151215_0_.getAsInt();
/*     */     }
/*     */ 
/*     */     
/* 132 */     throw new JsonSyntaxException("Expected " + p_151215_1_ + " to be a Int, was " + toString(p_151215_0_));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getInt(JsonObject p_151203_0_, String p_151203_1_) {
/* 138 */     if (p_151203_0_.has(p_151203_1_))
/*     */     {
/* 140 */       return getInt(p_151203_0_.get(p_151203_1_), p_151203_1_);
/*     */     }
/*     */ 
/*     */     
/* 144 */     throw new JsonSyntaxException("Missing " + p_151203_1_ + ", expected to find a Int");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getInt(JsonObject p_151208_0_, String p_151208_1_, int p_151208_2_) {
/* 150 */     return p_151208_0_.has(p_151208_1_) ? getInt(p_151208_0_.get(p_151208_1_), p_151208_1_) : p_151208_2_;
/*     */   }
/*     */ 
/*     */   
/*     */   public static JsonObject getJsonObject(JsonElement p_151210_0_, String p_151210_1_) {
/* 155 */     if (p_151210_0_.isJsonObject())
/*     */     {
/* 157 */       return p_151210_0_.getAsJsonObject();
/*     */     }
/*     */ 
/*     */     
/* 161 */     throw new JsonSyntaxException("Expected " + p_151210_1_ + " to be a JsonObject, was " + toString(p_151210_0_));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonObject getJsonObject(JsonObject base, String key) {
/* 167 */     if (base.has(key))
/*     */     {
/* 169 */       return getJsonObject(base.get(key), key);
/*     */     }
/*     */ 
/*     */     
/* 173 */     throw new JsonSyntaxException("Missing " + key + ", expected to find a JsonObject");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonObject getJsonObject(JsonObject p_151218_0_, String p_151218_1_, JsonObject p_151218_2_) {
/* 179 */     return p_151218_0_.has(p_151218_1_) ? getJsonObject(p_151218_0_.get(p_151218_1_), p_151218_1_) : p_151218_2_;
/*     */   }
/*     */ 
/*     */   
/*     */   public static JsonArray getJsonArray(JsonElement p_151207_0_, String p_151207_1_) {
/* 184 */     if (p_151207_0_.isJsonArray())
/*     */     {
/* 186 */       return p_151207_0_.getAsJsonArray();
/*     */     }
/*     */ 
/*     */     
/* 190 */     throw new JsonSyntaxException("Expected " + p_151207_1_ + " to be a JsonArray, was " + toString(p_151207_0_));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonArray getJsonArray(JsonObject p_151214_0_, String p_151214_1_) {
/* 196 */     if (p_151214_0_.has(p_151214_1_))
/*     */     {
/* 198 */       return getJsonArray(p_151214_0_.get(p_151214_1_), p_151214_1_);
/*     */     }
/*     */ 
/*     */     
/* 202 */     throw new JsonSyntaxException("Missing " + p_151214_1_ + ", expected to find a JsonArray");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonArray getJsonArray(JsonObject p_151213_0_, String p_151213_1_, JsonArray p_151213_2_) {
/* 208 */     return p_151213_0_.has(p_151213_1_) ? getJsonArray(p_151213_0_.get(p_151213_1_), p_151213_1_) : p_151213_2_;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String toString(JsonElement p_151222_0_) {
/* 213 */     String s = StringUtils.abbreviateMiddle(String.valueOf(p_151222_0_), "...", 10);
/*     */     
/* 215 */     if (p_151222_0_ == null)
/*     */     {
/* 217 */       return "null (missing)";
/*     */     }
/* 219 */     if (p_151222_0_.isJsonNull())
/*     */     {
/* 221 */       return "null (json)";
/*     */     }
/* 223 */     if (p_151222_0_.isJsonArray())
/*     */     {
/* 225 */       return "an array (" + s + ")";
/*     */     }
/* 227 */     if (p_151222_0_.isJsonObject())
/*     */     {
/* 229 */       return "an object (" + s + ")";
/*     */     }
/*     */ 
/*     */     
/* 233 */     if (p_151222_0_.isJsonPrimitive()) {
/*     */       
/* 235 */       JsonPrimitive jsonprimitive = p_151222_0_.getAsJsonPrimitive();
/*     */       
/* 237 */       if (jsonprimitive.isNumber())
/*     */       {
/* 239 */         return "a number (" + s + ")";
/*     */       }
/*     */       
/* 242 */       if (jsonprimitive.isBoolean())
/*     */       {
/* 244 */         return "a boolean (" + s + ")";
/*     */       }
/*     */     } 
/*     */     
/* 248 */     return s;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\JsonUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */