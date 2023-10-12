/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ 
/*     */ public class EntityMoveHelper
/*     */ {
/*     */   protected EntityLiving entity;
/*     */   protected double posX;
/*     */   protected double posY;
/*     */   protected double posZ;
/*     */   protected double speed;
/*     */   protected boolean update;
/*     */   
/*     */   public EntityMoveHelper(EntityLiving entitylivingIn) {
/*  18 */     this.entity = entitylivingIn;
/*  19 */     this.posX = entitylivingIn.posX;
/*  20 */     this.posY = entitylivingIn.posY;
/*  21 */     this.posZ = entitylivingIn.posZ;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUpdating() {
/*  26 */     return this.update;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getSpeed() {
/*  31 */     return this.speed;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMoveTo(double x, double y, double z, double speedIn) {
/*  36 */     this.posX = x;
/*  37 */     this.posY = y;
/*  38 */     this.posZ = z;
/*  39 */     this.speed = speedIn;
/*  40 */     this.update = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdateMoveHelper() {
/*  45 */     this.entity.setMoveForward(0.0F);
/*     */     
/*  47 */     if (this.update) {
/*     */       
/*  49 */       this.update = false;
/*  50 */       int i = MathHelper.floor_double((this.entity.getEntityBoundingBox()).minY + 0.5D);
/*  51 */       double d0 = this.posX - this.entity.posX;
/*  52 */       double d1 = this.posZ - this.entity.posZ;
/*  53 */       double d2 = this.posY - i;
/*  54 */       double d3 = d0 * d0 + d2 * d2 + d1 * d1;
/*     */       
/*  56 */       if (d3 >= 2.500000277905201E-7D) {
/*     */         
/*  58 */         float f = (float)(MathHelper.atan2(d1, d0) * 180.0D / Math.PI) - 90.0F;
/*  59 */         this.entity.rotationYaw = limitAngle(this.entity.rotationYaw, f, 30.0F);
/*  60 */         this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
/*     */         
/*  62 */         if (d2 > 0.0D && d0 * d0 + d1 * d1 < 1.0D)
/*     */         {
/*  64 */           this.entity.getJumpHelper().setJumping();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected float limitAngle(float p_75639_1_, float p_75639_2_, float p_75639_3_) {
/*  72 */     float f = MathHelper.wrapAngleTo180_float(p_75639_2_ - p_75639_1_);
/*     */     
/*  74 */     if (f > p_75639_3_)
/*     */     {
/*  76 */       f = p_75639_3_;
/*     */     }
/*     */     
/*  79 */     if (f < -p_75639_3_)
/*     */     {
/*  81 */       f = -p_75639_3_;
/*     */     }
/*     */     
/*  84 */     float f1 = p_75639_1_ + f;
/*     */     
/*  86 */     if (f1 < 0.0F) {
/*     */       
/*  88 */       f1 += 360.0F;
/*     */     }
/*  90 */     else if (f1 > 360.0F) {
/*     */       
/*  92 */       f1 -= 360.0F;
/*     */     } 
/*     */     
/*  95 */     return f1;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getX() {
/* 100 */     return this.posX;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getY() {
/* 105 */     return this.posY;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getZ() {
/* 110 */     return this.posZ;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityMoveHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */