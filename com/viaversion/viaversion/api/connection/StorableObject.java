/*    */ package com.viaversion.viaversion.api.connection;
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
/*    */ public interface StorableObject
/*    */ {
/*    */   default boolean clearOnServerSwitch() {
/* 39 */     return true;
/*    */   }
/*    */   
/*    */   default void onRemove() {}
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\api\connection\StorableObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */