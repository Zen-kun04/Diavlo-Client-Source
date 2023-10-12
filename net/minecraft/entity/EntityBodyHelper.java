/*    */ package net.minecraft.entity;
/*    */ 
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ 
/*    */ public class EntityBodyHelper
/*    */ {
/*    */   private EntityLivingBase theLiving;
/*    */   private int rotationTickCounter;
/*    */   private float prevRenderYawHead;
/*    */   
/*    */   public EntityBodyHelper(EntityLivingBase p_i1611_1_) {
/* 13 */     this.theLiving = p_i1611_1_;
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateRenderAngles() {
/* 18 */     double d0 = this.theLiving.posX - this.theLiving.prevPosX;
/* 19 */     double d1 = this.theLiving.posZ - this.theLiving.prevPosZ;
/*    */     
/* 21 */     if (d0 * d0 + d1 * d1 > 2.500000277905201E-7D) {
/*    */       
/* 23 */       this.theLiving.renderYawOffset = this.theLiving.rotationYaw;
/* 24 */       this.theLiving.rotationYawHead = computeAngleWithBound(this.theLiving.renderYawOffset, this.theLiving.rotationYawHead, 75.0F);
/* 25 */       this.prevRenderYawHead = this.theLiving.rotationYawHead;
/* 26 */       this.rotationTickCounter = 0;
/*    */     }
/*    */     else {
/*    */       
/* 30 */       float f = 75.0F;
/*    */       
/* 32 */       if (Math.abs(this.theLiving.rotationYawHead - this.prevRenderYawHead) > 15.0F) {
/*    */         
/* 34 */         this.rotationTickCounter = 0;
/* 35 */         this.prevRenderYawHead = this.theLiving.rotationYawHead;
/*    */       }
/*    */       else {
/*    */         
/* 39 */         this.rotationTickCounter++;
/* 40 */         int i = 10;
/*    */         
/* 42 */         if (this.rotationTickCounter > 10)
/*    */         {
/* 44 */           f = Math.max(1.0F - (this.rotationTickCounter - 10) / 10.0F, 0.0F) * 75.0F;
/*    */         }
/*    */       } 
/*    */       
/* 48 */       this.theLiving.renderYawOffset = computeAngleWithBound(this.theLiving.rotationYawHead, this.theLiving.renderYawOffset, f);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private float computeAngleWithBound(float p_75665_1_, float p_75665_2_, float p_75665_3_) {
/* 54 */     float f = MathHelper.wrapAngleTo180_float(p_75665_1_ - p_75665_2_);
/*    */     
/* 56 */     if (f < -p_75665_3_)
/*    */     {
/* 58 */       f = -p_75665_3_;
/*    */     }
/*    */     
/* 61 */     if (f >= p_75665_3_)
/*    */     {
/* 63 */       f = p_75665_3_;
/*    */     }
/*    */     
/* 66 */     return p_75665_1_ - f;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\EntityBodyHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */