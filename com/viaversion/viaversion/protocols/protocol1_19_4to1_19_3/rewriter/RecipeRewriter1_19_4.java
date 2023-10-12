/*    */ package com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.rewriter;
/*    */ 
/*    */ import com.viaversion.viaversion.api.protocol.Protocol;
/*    */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
/*    */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*    */ import com.viaversion.viaversion.api.type.Type;
/*    */ import com.viaversion.viaversion.protocols.protocol1_19_3to1_19_1.rewriter.RecipeRewriter1_19_3;
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
/*    */ public class RecipeRewriter1_19_4<C extends ClientboundPacketType>
/*    */   extends RecipeRewriter1_19_3<C>
/*    */ {
/*    */   public RecipeRewriter1_19_4(Protocol<C, ?, ?, ?> protocol) {
/* 29 */     super(protocol);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handleCraftingShaped(PacketWrapper wrapper) throws Exception {
/* 34 */     super.handleCraftingShaped(wrapper);
/* 35 */     wrapper.passthrough((Type)Type.BOOLEAN);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\protocol1_19_4to1_19_3\rewriter\RecipeRewriter1_19_4.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */