/*    */ package com.viaversion.viaversion.protocols.protocol1_20_2to1_20.packet;
/*    */ 
/*    */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
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
/*    */ public enum ClientboundConfigurationPackets1_20_2
/*    */   implements ClientboundPacketType
/*    */ {
/* 25 */   CUSTOM_PAYLOAD,
/* 26 */   DISCONNECT,
/* 27 */   FINISH_CONFIGURATION,
/* 28 */   KEEP_ALIVE,
/* 29 */   PING,
/* 30 */   REGISTRY_DATA,
/* 31 */   RESOURCE_PACK,
/* 32 */   UPDATE_ENABLED_FEATURES,
/* 33 */   UPDATE_TAGS;
/*    */ 
/*    */   
/*    */   public int getId() {
/* 37 */     return ordinal();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 42 */     return name();
/*    */   }
/*    */ 
/*    */   
/*    */   public State state() {
/* 47 */     return State.CONFIGURATION;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\protocol1_20_2to1_20\packet\ClientboundConfigurationPackets1_20_2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */