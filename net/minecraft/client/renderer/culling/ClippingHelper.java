/*    */ package net.minecraft.client.renderer.culling;
/*    */ 
/*    */ public class ClippingHelper
/*    */ {
/*  5 */   public float[][] frustum = new float[6][4];
/*  6 */   public float[] projectionMatrix = new float[16];
/*  7 */   public float[] modelviewMatrix = new float[16];
/*  8 */   public float[] clippingMatrix = new float[16];
/*    */   
/*    */   public boolean disabled = false;
/*    */   
/*    */   private float dot(float[] p_dot_1_, float p_dot_2_, float p_dot_3_, float p_dot_4_) {
/* 13 */     return p_dot_1_[0] * p_dot_2_ + p_dot_1_[1] * p_dot_3_ + p_dot_1_[2] * p_dot_4_ + p_dot_1_[3];
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBoxInFrustum(double p_78553_1_, double p_78553_3_, double p_78553_5_, double p_78553_7_, double p_78553_9_, double p_78553_11_) {
/* 18 */     if (this.disabled)
/*    */     {
/* 20 */       return true;
/*    */     }
/*    */ 
/*    */     
/* 24 */     float f = (float)p_78553_1_;
/* 25 */     float f1 = (float)p_78553_3_;
/* 26 */     float f2 = (float)p_78553_5_;
/* 27 */     float f3 = (float)p_78553_7_;
/* 28 */     float f4 = (float)p_78553_9_;
/* 29 */     float f5 = (float)p_78553_11_;
/*    */     
/* 31 */     for (int i = 0; i < 6; i++) {
/*    */       
/* 33 */       float[] afloat = this.frustum[i];
/* 34 */       float f6 = afloat[0];
/* 35 */       float f7 = afloat[1];
/* 36 */       float f8 = afloat[2];
/* 37 */       float f9 = afloat[3];
/*    */       
/* 39 */       if (f6 * f + f7 * f1 + f8 * f2 + f9 <= 0.0F && f6 * f3 + f7 * f1 + f8 * f2 + f9 <= 0.0F && f6 * f + f7 * f4 + f8 * f2 + f9 <= 0.0F && f6 * f3 + f7 * f4 + f8 * f2 + f9 <= 0.0F && f6 * f + f7 * f1 + f8 * f5 + f9 <= 0.0F && f6 * f3 + f7 * f1 + f8 * f5 + f9 <= 0.0F && f6 * f + f7 * f4 + f8 * f5 + f9 <= 0.0F && f6 * f3 + f7 * f4 + f8 * f5 + f9 <= 0.0F)
/*    */       {
/* 41 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 45 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isBoxInFrustumFully(double p_isBoxInFrustumFully_1_, double p_isBoxInFrustumFully_3_, double p_isBoxInFrustumFully_5_, double p_isBoxInFrustumFully_7_, double p_isBoxInFrustumFully_9_, double p_isBoxInFrustumFully_11_) {
/* 51 */     if (this.disabled)
/*    */     {
/* 53 */       return true;
/*    */     }
/*    */ 
/*    */     
/* 57 */     float f = (float)p_isBoxInFrustumFully_1_;
/* 58 */     float f1 = (float)p_isBoxInFrustumFully_3_;
/* 59 */     float f2 = (float)p_isBoxInFrustumFully_5_;
/* 60 */     float f3 = (float)p_isBoxInFrustumFully_7_;
/* 61 */     float f4 = (float)p_isBoxInFrustumFully_9_;
/* 62 */     float f5 = (float)p_isBoxInFrustumFully_11_;
/*    */     
/* 64 */     for (int i = 0; i < 6; i++) {
/*    */       
/* 66 */       float[] afloat = this.frustum[i];
/* 67 */       float f6 = afloat[0];
/* 68 */       float f7 = afloat[1];
/* 69 */       float f8 = afloat[2];
/* 70 */       float f9 = afloat[3];
/*    */       
/* 72 */       if (i < 4) {
/*    */         
/* 74 */         if (f6 * f + f7 * f1 + f8 * f2 + f9 <= 0.0F || f6 * f3 + f7 * f1 + f8 * f2 + f9 <= 0.0F || f6 * f + f7 * f4 + f8 * f2 + f9 <= 0.0F || f6 * f3 + f7 * f4 + f8 * f2 + f9 <= 0.0F || f6 * f + f7 * f1 + f8 * f5 + f9 <= 0.0F || f6 * f3 + f7 * f1 + f8 * f5 + f9 <= 0.0F || f6 * f + f7 * f4 + f8 * f5 + f9 <= 0.0F || f6 * f3 + f7 * f4 + f8 * f5 + f9 <= 0.0F)
/*    */         {
/* 76 */           return false;
/*    */         }
/*    */       }
/* 79 */       else if (f6 * f + f7 * f1 + f8 * f2 + f9 <= 0.0F && f6 * f3 + f7 * f1 + f8 * f2 + f9 <= 0.0F && f6 * f + f7 * f4 + f8 * f2 + f9 <= 0.0F && f6 * f3 + f7 * f4 + f8 * f2 + f9 <= 0.0F && f6 * f + f7 * f1 + f8 * f5 + f9 <= 0.0F && f6 * f3 + f7 * f1 + f8 * f5 + f9 <= 0.0F && f6 * f + f7 * f4 + f8 * f5 + f9 <= 0.0F && f6 * f3 + f7 * f4 + f8 * f5 + f9 <= 0.0F) {
/*    */         
/* 81 */         return false;
/*    */       } 
/*    */     } 
/*    */     
/* 85 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\culling\ClippingHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */