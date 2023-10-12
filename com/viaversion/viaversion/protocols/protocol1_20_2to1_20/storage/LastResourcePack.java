/*    */ package com.viaversion.viaversion.protocols.protocol1_20_2to1_20.storage;
/*    */ 
/*    */ import com.viaversion.viaversion.api.connection.StorableObject;
/*    */ import com.viaversion.viaversion.libs.gson.JsonElement;
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
/*    */ public final class LastResourcePack
/*    */   implements StorableObject
/*    */ {
/*    */   private final String url;
/*    */   private final String hash;
/*    */   private final boolean required;
/*    */   private final JsonElement prompt;
/*    */   
/*    */   public LastResourcePack(String url, String hash, boolean required, JsonElement prompt) {
/* 32 */     this.url = url;
/* 33 */     this.hash = hash;
/* 34 */     this.required = required;
/* 35 */     this.prompt = prompt;
/*    */   }
/*    */   
/*    */   public String url() {
/* 39 */     return this.url;
/*    */   }
/*    */   
/*    */   public String hash() {
/* 43 */     return this.hash;
/*    */   }
/*    */   
/*    */   public boolean required() {
/* 47 */     return this.required;
/*    */   }
/*    */   
/*    */   public JsonElement prompt() {
/* 51 */     return this.prompt;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\protocol1_20_2to1_20\storage\LastResourcePack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */