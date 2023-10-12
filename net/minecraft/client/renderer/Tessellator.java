/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import net.optifine.SmartAnimations;
/*    */ 
/*    */ public class Tessellator
/*    */ {
/*    */   private WorldRenderer worldRenderer;
/*  8 */   private WorldVertexBufferUploader vboUploader = new WorldVertexBufferUploader();
/*  9 */   private static final Tessellator instance = new Tessellator(2097152);
/*    */ 
/*    */   
/*    */   public static Tessellator getInstance() {
/* 13 */     return instance;
/*    */   }
/*    */ 
/*    */   
/*    */   public Tessellator(int bufferSize) {
/* 18 */     this.worldRenderer = new WorldRenderer(bufferSize);
/*    */   }
/*    */ 
/*    */   
/*    */   public void draw() {
/* 23 */     if (this.worldRenderer.animatedSprites != null)
/*    */     {
/* 25 */       SmartAnimations.spritesRendered(this.worldRenderer.animatedSprites);
/*    */     }
/*    */     
/* 28 */     this.worldRenderer.finishDrawing();
/* 29 */     this.vboUploader.draw(this.worldRenderer);
/*    */   }
/*    */ 
/*    */   
/*    */   public WorldRenderer getWorldRenderer() {
/* 34 */     return this.worldRenderer;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\Tessellator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */