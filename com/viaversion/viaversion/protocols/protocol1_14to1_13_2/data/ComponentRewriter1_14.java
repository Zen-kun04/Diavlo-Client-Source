/*    */ package com.viaversion.viaversion.protocols.protocol1_14to1_13_2.data;
/*    */ 
/*    */ import com.viaversion.viaversion.api.protocol.Protocol;
/*    */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
/*    */ import com.viaversion.viaversion.libs.gson.JsonObject;
/*    */ import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data.ComponentRewriter1_13;
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
/*    */ public class ComponentRewriter1_14<C extends ClientboundPacketType>
/*    */   extends ComponentRewriter1_13<C>
/*    */ {
/*    */   public ComponentRewriter1_14(Protocol<C, ?, ?, ?> protocol) {
/* 28 */     super(protocol);
/*    */   }
/*    */   
/*    */   protected void handleTranslate(JsonObject object, String translate) {}
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\protocol1_14to1_13_2\data\ComponentRewriter1_14.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */