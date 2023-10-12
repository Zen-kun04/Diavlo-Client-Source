/*    */ package com.viaversion.viaversion.protocols.protocol1_20_2to1_20.rewriter;
/*    */ 
/*    */ import com.viaversion.viaversion.api.minecraft.item.Item;
/*    */ import com.viaversion.viaversion.api.protocol.Protocol;
/*    */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
/*    */ import com.viaversion.viaversion.api.type.Type;
/*    */ import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.rewriter.RecipeRewriter1_19_4;
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
/*    */ public class RecipeRewriter1_20_2<C extends ClientboundPacketType>
/*    */   extends RecipeRewriter1_19_4<C>
/*    */ {
/*    */   public RecipeRewriter1_20_2(Protocol<C, ?, ?, ?> protocol) {
/* 29 */     super(protocol);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Type<Item> itemType() {
/* 34 */     return Type.ITEM1_20_2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Type<Item[]> itemArrayType() {
/* 39 */     return Type.ITEM1_20_2_VAR_INT_ARRAY;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\protocol1_20_2to1_20\rewriter\RecipeRewriter1_20_2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */