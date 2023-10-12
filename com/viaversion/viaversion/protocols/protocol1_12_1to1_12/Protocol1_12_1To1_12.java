/*    */ package com.viaversion.viaversion.protocols.protocol1_12_1to1_12;
/*    */ 
/*    */ import com.viaversion.viaversion.api.protocol.AbstractProtocol;
/*    */ import com.viaversion.viaversion.protocols.protocol1_12to1_11_1.ClientboundPackets1_12;
/*    */ import com.viaversion.viaversion.protocols.protocol1_12to1_11_1.ServerboundPackets1_12;
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
/*    */ public class Protocol1_12_1To1_12
/*    */   extends AbstractProtocol<ClientboundPackets1_12, ClientboundPackets1_12_1, ServerboundPackets1_12, ServerboundPackets1_12_1>
/*    */ {
/*    */   public Protocol1_12_1To1_12() {
/* 27 */     super(ClientboundPackets1_12.class, ClientboundPackets1_12_1.class, ServerboundPackets1_12.class, ServerboundPackets1_12_1.class);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void registerPackets() {
/* 32 */     cancelServerbound(ServerboundPackets1_12_1.CRAFT_RECIPE_REQUEST);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\protocol1_12_1to1_12\Protocol1_12_1To1_12.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */