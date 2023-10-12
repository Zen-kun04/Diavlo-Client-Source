/*    */ package com.viaversion.viabackwards.protocol.protocol1_19_1to1_19_3.storage;
/*    */ 
/*    */ import com.viaversion.viaversion.api.connection.StorableObject;
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
/*    */ public final class NonceStorage
/*    */   implements StorableObject
/*    */ {
/*    */   private final byte[] nonce;
/*    */   
/*    */   public NonceStorage(byte[] nonce) {
/* 28 */     this.nonce = nonce;
/*    */   }
/*    */   
/*    */   public byte[] nonce() {
/* 32 */     return this.nonce;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viabackwards\protocol\protocol1_19_1to1_19_3\storage\NonceStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */