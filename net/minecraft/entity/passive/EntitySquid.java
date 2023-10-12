/*     */ package net.minecraft.entity.passive;
/*     */ 
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntitySquid
/*     */   extends EntityWaterMob {
/*     */   public float squidPitch;
/*     */   public float prevSquidPitch;
/*     */   public float squidYaw;
/*     */   public float prevSquidYaw;
/*     */   public float squidRotation;
/*     */   public float prevSquidRotation;
/*     */   public float tentacleAngle;
/*     */   public float lastTentacleAngle;
/*     */   private float randomMotionSpeed;
/*     */   private float rotationVelocity;
/*     */   private float field_70871_bB;
/*     */   private float randomMotionVecX;
/*     */   private float randomMotionVecY;
/*     */   private float randomMotionVecZ;
/*     */   
/*     */   public EntitySquid(World worldIn) {
/*  32 */     super(worldIn);
/*  33 */     setSize(0.95F, 0.95F);
/*  34 */     this.rand.setSeed((1 + getEntityId()));
/*  35 */     this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
/*  36 */     this.tasks.addTask(0, new AIMoveRandom(this));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyEntityAttributes() {
/*  41 */     super.applyEntityAttributes();
/*  42 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getEyeHeight() {
/*  47 */     return this.height * 0.5F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getLivingSound() {
/*  52 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getHurtSound() {
/*  57 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getDeathSound() {
/*  62 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getSoundVolume() {
/*  67 */     return 0.4F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Item getDropItem() {
/*  72 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canTriggerWalking() {
/*  77 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
/*  82 */     int i = this.rand.nextInt(3 + lootingModifier) + 1;
/*     */     
/*  84 */     for (int j = 0; j < i; j++)
/*     */     {
/*  86 */       entityDropItem(new ItemStack(Items.dye, 1, EnumDyeColor.BLACK.getDyeDamage()), 0.0F);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInWater() {
/*  92 */     return this.worldObj.handleMaterialAcceleration(getEntityBoundingBox().expand(0.0D, -0.6000000238418579D, 0.0D), Material.water, (Entity)this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLivingUpdate() {
/*  97 */     super.onLivingUpdate();
/*  98 */     this.prevSquidPitch = this.squidPitch;
/*  99 */     this.prevSquidYaw = this.squidYaw;
/* 100 */     this.prevSquidRotation = this.squidRotation;
/* 101 */     this.lastTentacleAngle = this.tentacleAngle;
/* 102 */     this.squidRotation += this.rotationVelocity;
/*     */     
/* 104 */     if (this.squidRotation > 6.283185307179586D)
/*     */     {
/* 106 */       if (this.worldObj.isRemote) {
/*     */         
/* 108 */         this.squidRotation = 6.2831855F;
/*     */       }
/*     */       else {
/*     */         
/* 112 */         this.squidRotation = (float)(this.squidRotation - 6.283185307179586D);
/*     */         
/* 114 */         if (this.rand.nextInt(10) == 0)
/*     */         {
/* 116 */           this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
/*     */         }
/*     */         
/* 119 */         this.worldObj.setEntityState((Entity)this, (byte)19);
/*     */       } 
/*     */     }
/*     */     
/* 123 */     if (this.inWater) {
/*     */       
/* 125 */       if (this.squidRotation < 3.1415927F) {
/*     */         
/* 127 */         float f = this.squidRotation / 3.1415927F;
/* 128 */         this.tentacleAngle = MathHelper.sin(f * f * 3.1415927F) * 3.1415927F * 0.25F;
/*     */         
/* 130 */         if (f > 0.75D)
/*     */         {
/* 132 */           this.randomMotionSpeed = 1.0F;
/* 133 */           this.field_70871_bB = 1.0F;
/*     */         }
/*     */         else
/*     */         {
/* 137 */           this.field_70871_bB *= 0.8F;
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 142 */         this.tentacleAngle = 0.0F;
/* 143 */         this.randomMotionSpeed *= 0.9F;
/* 144 */         this.field_70871_bB *= 0.99F;
/*     */       } 
/*     */       
/* 147 */       if (!this.worldObj.isRemote) {
/*     */         
/* 149 */         this.motionX = (this.randomMotionVecX * this.randomMotionSpeed);
/* 150 */         this.motionY = (this.randomMotionVecY * this.randomMotionSpeed);
/* 151 */         this.motionZ = (this.randomMotionVecZ * this.randomMotionSpeed);
/*     */       } 
/*     */       
/* 154 */       float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
/* 155 */       this.renderYawOffset += (-((float)MathHelper.atan2(this.motionX, this.motionZ)) * 180.0F / 3.1415927F - this.renderYawOffset) * 0.1F;
/* 156 */       this.rotationYaw = this.renderYawOffset;
/* 157 */       this.squidYaw = (float)(this.squidYaw + Math.PI * this.field_70871_bB * 1.5D);
/* 158 */       this.squidPitch += (-((float)MathHelper.atan2(f1, this.motionY)) * 180.0F / 3.1415927F - this.squidPitch) * 0.1F;
/*     */     }
/*     */     else {
/*     */       
/* 162 */       this.tentacleAngle = MathHelper.abs(MathHelper.sin(this.squidRotation)) * 3.1415927F * 0.25F;
/*     */       
/* 164 */       if (!this.worldObj.isRemote) {
/*     */         
/* 166 */         this.motionX = 0.0D;
/* 167 */         this.motionY -= 0.08D;
/* 168 */         this.motionY *= 0.9800000190734863D;
/* 169 */         this.motionZ = 0.0D;
/*     */       } 
/*     */       
/* 172 */       this.squidPitch = (float)(this.squidPitch + (-90.0F - this.squidPitch) * 0.02D);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void moveEntityWithHeading(float strafe, float forward) {
/* 178 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getCanSpawnHere() {
/* 183 */     return (this.posY > 45.0D && this.posY < this.worldObj.getSeaLevel() && super.getCanSpawnHere());
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleStatusUpdate(byte id) {
/* 188 */     if (id == 19) {
/*     */       
/* 190 */       this.squidRotation = 0.0F;
/*     */     }
/*     */     else {
/*     */       
/* 194 */       super.handleStatusUpdate(id);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_175568_b(float randomMotionVecXIn, float randomMotionVecYIn, float randomMotionVecZIn) {
/* 200 */     this.randomMotionVecX = randomMotionVecXIn;
/* 201 */     this.randomMotionVecY = randomMotionVecYIn;
/* 202 */     this.randomMotionVecZ = randomMotionVecZIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean func_175567_n() {
/* 207 */     return (this.randomMotionVecX != 0.0F || this.randomMotionVecY != 0.0F || this.randomMotionVecZ != 0.0F);
/*     */   }
/*     */   
/*     */   static class AIMoveRandom
/*     */     extends EntityAIBase
/*     */   {
/*     */     private EntitySquid squid;
/*     */     
/*     */     public AIMoveRandom(EntitySquid p_i45859_1_) {
/* 216 */       this.squid = p_i45859_1_;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean shouldExecute() {
/* 221 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void updateTask() {
/* 226 */       int i = this.squid.getAge();
/*     */       
/* 228 */       if (i > 100) {
/*     */         
/* 230 */         this.squid.func_175568_b(0.0F, 0.0F, 0.0F);
/*     */       }
/* 232 */       else if (this.squid.getRNG().nextInt(50) == 0 || !this.squid.inWater || !this.squid.func_175567_n()) {
/*     */         
/* 234 */         float f = this.squid.getRNG().nextFloat() * 3.1415927F * 2.0F;
/* 235 */         float f1 = MathHelper.cos(f) * 0.2F;
/* 236 */         float f2 = -0.1F + this.squid.getRNG().nextFloat() * 0.2F;
/* 237 */         float f3 = MathHelper.sin(f) * 0.2F;
/* 238 */         this.squid.func_175568_b(f1, f2, f3);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\passive\EntitySquid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */