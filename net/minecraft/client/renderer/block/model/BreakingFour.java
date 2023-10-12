/*    */ package net.minecraft.client.renderer.block.model;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ 
/*    */ public class BreakingFour
/*    */   extends BakedQuad {
/*    */   private final TextureAtlasSprite texture;
/*    */   
/*    */   public BreakingFour(BakedQuad quad, TextureAtlasSprite textureIn) {
/* 12 */     super(Arrays.copyOf(quad.getVertexData(), (quad.getVertexData()).length), quad.tintIndex, FaceBakery.getFacingFromVertexData(quad.getVertexData()));
/* 13 */     this.texture = textureIn;
/* 14 */     remapQuad();
/* 15 */     fixVertexData();
/*    */   }
/*    */ 
/*    */   
/*    */   private void remapQuad() {
/* 20 */     for (int i = 0; i < 4; i++)
/*    */     {
/* 22 */       remapVert(i);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   private void remapVert(int vertex) {
/* 28 */     int i = this.vertexData.length / 4;
/* 29 */     int j = i * vertex;
/* 30 */     float f = Float.intBitsToFloat(this.vertexData[j]);
/* 31 */     float f1 = Float.intBitsToFloat(this.vertexData[j + 1]);
/* 32 */     float f2 = Float.intBitsToFloat(this.vertexData[j + 2]);
/* 33 */     float f3 = 0.0F;
/* 34 */     float f4 = 0.0F;
/*    */     
/* 36 */     switch (this.face) {
/*    */       
/*    */       case DOWN:
/* 39 */         f3 = f * 16.0F;
/* 40 */         f4 = (1.0F - f2) * 16.0F;
/*    */         break;
/*    */       
/*    */       case UP:
/* 44 */         f3 = f * 16.0F;
/* 45 */         f4 = f2 * 16.0F;
/*    */         break;
/*    */       
/*    */       case NORTH:
/* 49 */         f3 = (1.0F - f) * 16.0F;
/* 50 */         f4 = (1.0F - f1) * 16.0F;
/*    */         break;
/*    */       
/*    */       case SOUTH:
/* 54 */         f3 = f * 16.0F;
/* 55 */         f4 = (1.0F - f1) * 16.0F;
/*    */         break;
/*    */       
/*    */       case WEST:
/* 59 */         f3 = f2 * 16.0F;
/* 60 */         f4 = (1.0F - f1) * 16.0F;
/*    */         break;
/*    */       
/*    */       case EAST:
/* 64 */         f3 = (1.0F - f2) * 16.0F;
/* 65 */         f4 = (1.0F - f1) * 16.0F;
/*    */         break;
/*    */     } 
/* 68 */     this.vertexData[j + 4] = Float.floatToRawIntBits(this.texture.getInterpolatedU(f3));
/* 69 */     this.vertexData[j + 4 + 1] = Float.floatToRawIntBits(this.texture.getInterpolatedV(f4));
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\block\model\BreakingFour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */