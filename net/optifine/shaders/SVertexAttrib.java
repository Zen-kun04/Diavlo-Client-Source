/*    */ package net.optifine.shaders;
/*    */ 
/*    */ import net.minecraft.client.renderer.vertex.VertexFormatElement;
/*    */ 
/*    */ 
/*    */ public class SVertexAttrib
/*    */ {
/*    */   public int index;
/*    */   public int count;
/*    */   public VertexFormatElement.EnumType type;
/*    */   public int offset;
/*    */   
/*    */   public SVertexAttrib(int index, int count, VertexFormatElement.EnumType type) {
/* 14 */     this.index = index;
/* 15 */     this.count = count;
/* 16 */     this.type = type;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\shaders\SVertexAttrib.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */