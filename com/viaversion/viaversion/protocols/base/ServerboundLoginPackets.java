/*    */ package com.viaversion.viaversion.protocols.base;
/*    */ 
/*    */ import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
/*    */ import com.viaversion.viaversion.api.protocol.packet.State;
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
/*    */ public enum ServerboundLoginPackets
/*    */   implements ServerboundPacketType
/*    */ {
/* 24 */   HELLO,
/* 25 */   ENCRYPTION_KEY,
/* 26 */   CUSTOM_QUERY_ANSWER,
/* 27 */   LOGIN_ACKNOWLEDGED;
/*    */ 
/*    */   
/*    */   public final int getId() {
/* 31 */     return ordinal();
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getName() {
/* 36 */     return name();
/*    */   }
/*    */ 
/*    */   
/*    */   public final State state() {
/* 41 */     return State.LOGIN;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\base\ServerboundLoginPackets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */