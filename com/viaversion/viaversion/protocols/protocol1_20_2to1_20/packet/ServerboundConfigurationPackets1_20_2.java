/*    */ package com.viaversion.viaversion.protocols.protocol1_20_2to1_20.packet;
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
/*    */ 
/*    */ public enum ServerboundConfigurationPackets1_20_2
/*    */   implements ServerboundPacketType
/*    */ {
/* 25 */   CLIENT_INFORMATION,
/* 26 */   CUSTOM_PAYLOAD,
/* 27 */   FINISH_CONFIGURATION,
/* 28 */   KEEP_ALIVE,
/* 29 */   PONG,
/* 30 */   RESOURCE_PACK;
/*    */ 
/*    */   
/*    */   public int getId() {
/* 34 */     return ordinal();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 39 */     return name();
/*    */   }
/*    */ 
/*    */   
/*    */   public State state() {
/* 44 */     return State.CONFIGURATION;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\protocol1_20_2to1_20\packet\ServerboundConfigurationPackets1_20_2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */