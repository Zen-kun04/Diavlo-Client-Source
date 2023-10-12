/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.ForwardingSet;
/*    */ import com.google.common.collect.Sets;
/*    */ import com.google.gson.JsonArray;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonPrimitive;
/*    */ import java.util.Collection;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class JsonSerializableSet
/*    */   extends ForwardingSet<String> implements IJsonSerializable {
/* 13 */   private final Set<String> underlyingSet = Sets.newHashSet();
/*    */ 
/*    */   
/*    */   public void fromJson(JsonElement json) {
/* 17 */     if (json.isJsonArray())
/*    */     {
/* 19 */       for (JsonElement jsonelement : json.getAsJsonArray())
/*    */       {
/* 21 */         add(jsonelement.getAsString());
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public JsonElement getSerializableElement() {
/* 28 */     JsonArray jsonarray = new JsonArray();
/*    */     
/* 30 */     for (String s : this)
/*    */     {
/* 32 */       jsonarray.add((JsonElement)new JsonPrimitive(s));
/*    */     }
/*    */     
/* 35 */     return (JsonElement)jsonarray;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Set<String> delegate() {
/* 40 */     return this.underlyingSet;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\JsonSerializableSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */