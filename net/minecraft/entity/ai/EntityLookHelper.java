/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityLookHelper
/*     */ {
/*     */   private EntityLiving entity;
/*     */   private float deltaLookYaw;
/*     */   private float deltaLookPitch;
/*     */   private boolean isLooking;
/*     */   private double posX;
/*     */   private double posY;
/*     */   private double posZ;
/*     */   
/*     */   public EntityLookHelper(EntityLiving entitylivingIn) {
/*  20 */     this.entity = entitylivingIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLookPositionWithEntity(Entity entityIn, float deltaYaw, float deltaPitch) {
/*  25 */     this.posX = entityIn.posX;
/*     */     
/*  27 */     if (entityIn instanceof net.minecraft.entity.EntityLivingBase) {
/*     */       
/*  29 */       this.posY = entityIn.posY + entityIn.getEyeHeight();
/*     */     }
/*     */     else {
/*     */       
/*  33 */       this.posY = ((entityIn.getEntityBoundingBox()).minY + (entityIn.getEntityBoundingBox()).maxY) / 2.0D;
/*     */     } 
/*     */     
/*  36 */     this.posZ = entityIn.posZ;
/*  37 */     this.deltaLookYaw = deltaYaw;
/*  38 */     this.deltaLookPitch = deltaPitch;
/*  39 */     this.isLooking = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLookPosition(double x, double y, double z, float deltaYaw, float deltaPitch) {
/*  44 */     this.posX = x;
/*  45 */     this.posY = y;
/*  46 */     this.posZ = z;
/*  47 */     this.deltaLookYaw = deltaYaw;
/*  48 */     this.deltaLookPitch = deltaPitch;
/*  49 */     this.isLooking = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdateLook() {
/*  54 */     this.entity.rotationPitch = 0.0F;
/*     */     
/*  56 */     if (this.isLooking) {
/*     */       
/*  58 */       this.isLooking = false;
/*  59 */       double d0 = this.posX - this.entity.posX;
/*  60 */       double d1 = this.posY - this.entity.posY + this.entity.getEyeHeight();
/*  61 */       double d2 = this.posZ - this.entity.posZ;
/*  62 */       double d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
/*  63 */       float f = (float)(MathHelper.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
/*  64 */       float f1 = (float)-(MathHelper.atan2(d1, d3) * 180.0D / Math.PI);
/*  65 */       this.entity.rotationPitch = updateRotation(this.entity.rotationPitch, f1, this.deltaLookPitch);
/*  66 */       this.entity.rotationYawHead = updateRotation(this.entity.rotationYawHead, f, this.deltaLookYaw);
/*     */     }
/*     */     else {
/*     */       
/*  70 */       this.entity.rotationYawHead = updateRotation(this.entity.rotationYawHead, this.entity.renderYawOffset, 10.0F);
/*     */     } 
/*     */     
/*  73 */     float f2 = MathHelper.wrapAngleTo180_float(this.entity.rotationYawHead - this.entity.renderYawOffset);
/*     */     
/*  75 */     if (!this.entity.getNavigator().noPath()) {
/*     */       
/*  77 */       if (f2 < -75.0F)
/*     */       {
/*  79 */         this.entity.rotationYawHead = this.entity.renderYawOffset - 75.0F;
/*     */       }
/*     */       
/*  82 */       if (f2 > 75.0F)
/*     */       {
/*  84 */         this.entity.rotationYawHead = this.entity.renderYawOffset + 75.0F;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private float updateRotation(float p_75652_1_, float p_75652_2_, float p_75652_3_) {
/*  91 */     float f = MathHelper.wrapAngleTo180_float(p_75652_2_ - p_75652_1_);
/*     */     
/*  93 */     if (f > p_75652_3_)
/*     */     {
/*  95 */       f = p_75652_3_;
/*     */     }
/*     */     
/*  98 */     if (f < -p_75652_3_)
/*     */     {
/* 100 */       f = -p_75652_3_;
/*     */     }
/*     */     
/* 103 */     return p_75652_1_ + f;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getIsLooking() {
/* 108 */     return this.isLooking;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getLookPosX() {
/* 113 */     return this.posX;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getLookPosY() {
/* 118 */     return this.posY;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getLookPosZ() {
/* 123 */     return this.posZ;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityLookHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */