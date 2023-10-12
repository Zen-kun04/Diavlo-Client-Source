/*    */ package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.json;
/*    */ 
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.util.Services;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Supplier;
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
/*    */ final class JSONComponentSerializerAccessor
/*    */ {
/* 31 */   private static final Optional<JSONComponentSerializer.Provider> SERVICE = Services.serviceWithFallback(JSONComponentSerializer.Provider.class);
/*    */ 
/*    */ 
/*    */   
/*    */   static final class Instances
/*    */   {
/* 37 */     static final JSONComponentSerializer INSTANCE = JSONComponentSerializerAccessor.SERVICE
/* 38 */       .map(JSONComponentSerializer.Provider::instance)
/* 39 */       .orElse(DummyJSONComponentSerializer.INSTANCE);
/*    */     
/* 41 */     static final Supplier<JSONComponentSerializer.Builder> BUILDER_SUPPLIER = JSONComponentSerializerAccessor.SERVICE
/* 42 */       .map(JSONComponentSerializer.Provider::builder)
/* 43 */       .orElse(BuilderImpl::new);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\text\serializer\json\JSONComponentSerializerAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */