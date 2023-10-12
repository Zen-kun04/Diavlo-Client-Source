/*     */ package net.minecraft.entity;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockRedstoneDiode;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.World;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ 
/*     */ public abstract class EntityHanging
/*     */   extends Entity
/*     */ {
/*     */   private int tickCounter1;
/*     */   protected BlockPos hangingPosition;
/*     */   public EnumFacing facingDirection;
/*     */   
/*     */   public EntityHanging(World worldIn) {
/*  22 */     super(worldIn);
/*  23 */     setSize(0.5F, 0.5F);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityHanging(World worldIn, BlockPos hangingPositionIn) {
/*  28 */     this(worldIn);
/*  29 */     this.hangingPosition = hangingPositionIn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void entityInit() {}
/*     */ 
/*     */   
/*     */   protected void updateFacingWithBoundingBox(EnumFacing facingDirectionIn) {
/*  38 */     Validate.notNull(facingDirectionIn);
/*  39 */     Validate.isTrue(facingDirectionIn.getAxis().isHorizontal());
/*  40 */     this.facingDirection = facingDirectionIn;
/*  41 */     this.prevRotationYaw = this.rotationYaw = (this.facingDirection.getHorizontalIndex() * 90);
/*  42 */     updateBoundingBox();
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateBoundingBox() {
/*  47 */     if (this.facingDirection != null) {
/*     */       
/*  49 */       double d0 = this.hangingPosition.getX() + 0.5D;
/*  50 */       double d1 = this.hangingPosition.getY() + 0.5D;
/*  51 */       double d2 = this.hangingPosition.getZ() + 0.5D;
/*  52 */       double d3 = 0.46875D;
/*  53 */       double d4 = func_174858_a(getWidthPixels());
/*  54 */       double d5 = func_174858_a(getHeightPixels());
/*  55 */       d0 -= this.facingDirection.getFrontOffsetX() * 0.46875D;
/*  56 */       d2 -= this.facingDirection.getFrontOffsetZ() * 0.46875D;
/*  57 */       d1 += d5;
/*  58 */       EnumFacing enumfacing = this.facingDirection.rotateYCCW();
/*  59 */       d0 += d4 * enumfacing.getFrontOffsetX();
/*  60 */       d2 += d4 * enumfacing.getFrontOffsetZ();
/*  61 */       this.posX = d0;
/*  62 */       this.posY = d1;
/*  63 */       this.posZ = d2;
/*  64 */       double d6 = getWidthPixels();
/*  65 */       double d7 = getHeightPixels();
/*  66 */       double d8 = getWidthPixels();
/*     */       
/*  68 */       if (this.facingDirection.getAxis() == EnumFacing.Axis.Z) {
/*     */         
/*  70 */         d8 = 1.0D;
/*     */       }
/*     */       else {
/*     */         
/*  74 */         d6 = 1.0D;
/*     */       } 
/*     */       
/*  77 */       d6 /= 32.0D;
/*  78 */       d7 /= 32.0D;
/*  79 */       d8 /= 32.0D;
/*  80 */       setEntityBoundingBox(new AxisAlignedBB(d0 - d6, d1 - d7, d2 - d8, d0 + d6, d1 + d7, d2 + d8));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private double func_174858_a(int p_174858_1_) {
/*  86 */     return (p_174858_1_ % 32 == 0) ? 0.5D : 0.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  91 */     this.prevPosX = this.posX;
/*  92 */     this.prevPosY = this.posY;
/*  93 */     this.prevPosZ = this.posZ;
/*     */     
/*  95 */     if (this.tickCounter1++ == 100 && !this.worldObj.isRemote) {
/*     */       
/*  97 */       this.tickCounter1 = 0;
/*     */       
/*  99 */       if (!this.isDead && !onValidSurface()) {
/*     */         
/* 101 */         setDead();
/* 102 */         onBroken((Entity)null);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onValidSurface() {
/* 109 */     if (!this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox()).isEmpty())
/*     */     {
/* 111 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 115 */     int i = Math.max(1, getWidthPixels() / 16);
/* 116 */     int j = Math.max(1, getHeightPixels() / 16);
/* 117 */     BlockPos blockpos = this.hangingPosition.offset(this.facingDirection.getOpposite());
/* 118 */     EnumFacing enumfacing = this.facingDirection.rotateYCCW();
/*     */     
/* 120 */     for (int k = 0; k < i; k++) {
/*     */       
/* 122 */       for (int l = 0; l < j; l++) {
/*     */         
/* 124 */         BlockPos blockpos1 = blockpos.offset(enumfacing, k).up(l);
/* 125 */         Block block = this.worldObj.getBlockState(blockpos1).getBlock();
/*     */         
/* 127 */         if (!block.getMaterial().isSolid() && !BlockRedstoneDiode.isRedstoneRepeaterBlockID(block))
/*     */         {
/* 129 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 134 */     for (Entity entity : this.worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox())) {
/*     */       
/* 136 */       if (entity instanceof EntityHanging)
/*     */       {
/* 138 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 142 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canBeCollidedWith() {
/* 148 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hitByEntity(Entity entityIn) {
/* 153 */     return (entityIn instanceof EntityPlayer) ? attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)entityIn), 0.0F) : false;
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumFacing getHorizontalFacing() {
/* 158 */     return this.facingDirection;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityFrom(DamageSource source, float amount) {
/* 163 */     if (isEntityInvulnerable(source))
/*     */     {
/* 165 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 169 */     if (!this.isDead && !this.worldObj.isRemote) {
/*     */       
/* 171 */       setDead();
/* 172 */       setBeenAttacked();
/* 173 */       onBroken(source.getEntity());
/*     */     } 
/*     */     
/* 176 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void moveEntity(double x, double y, double z) {
/* 182 */     if (!this.worldObj.isRemote && !this.isDead && x * x + y * y + z * z > 0.0D) {
/*     */       
/* 184 */       setDead();
/* 185 */       onBroken((Entity)null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addVelocity(double x, double y, double z) {
/* 191 */     if (!this.worldObj.isRemote && !this.isDead && x * x + y * y + z * z > 0.0D) {
/*     */       
/* 193 */       setDead();
/* 194 */       onBroken((Entity)null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/* 200 */     tagCompound.setByte("Facing", (byte)this.facingDirection.getHorizontalIndex());
/* 201 */     tagCompound.setInteger("TileX", getHangingPosition().getX());
/* 202 */     tagCompound.setInteger("TileY", getHangingPosition().getY());
/* 203 */     tagCompound.setInteger("TileZ", getHangingPosition().getZ());
/*     */   }
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/*     */     EnumFacing enumfacing;
/* 208 */     this.hangingPosition = new BlockPos(tagCompund.getInteger("TileX"), tagCompund.getInteger("TileY"), tagCompund.getInteger("TileZ"));
/*     */ 
/*     */     
/* 211 */     if (tagCompund.hasKey("Direction", 99)) {
/*     */       
/* 213 */       enumfacing = EnumFacing.getHorizontal(tagCompund.getByte("Direction"));
/* 214 */       this.hangingPosition = this.hangingPosition.offset(enumfacing);
/*     */     }
/* 216 */     else if (tagCompund.hasKey("Facing", 99)) {
/*     */       
/* 218 */       enumfacing = EnumFacing.getHorizontal(tagCompund.getByte("Facing"));
/*     */     }
/*     */     else {
/*     */       
/* 222 */       enumfacing = EnumFacing.getHorizontal(tagCompund.getByte("Dir"));
/*     */     } 
/*     */     
/* 225 */     updateFacingWithBoundingBox(enumfacing);
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract int getWidthPixels();
/*     */   
/*     */   public abstract int getHeightPixels();
/*     */   
/*     */   public abstract void onBroken(Entity paramEntity);
/*     */   
/*     */   protected boolean shouldSetPosAfterLoading() {
/* 236 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPosition(double x, double y, double z) {
/* 241 */     this.posX = x;
/* 242 */     this.posY = y;
/* 243 */     this.posZ = z;
/* 244 */     BlockPos blockpos = this.hangingPosition;
/* 245 */     this.hangingPosition = new BlockPos(x, y, z);
/*     */     
/* 247 */     if (!this.hangingPosition.equals(blockpos)) {
/*     */       
/* 249 */       updateBoundingBox();
/* 250 */       this.isAirBorne = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos getHangingPosition() {
/* 256 */     return this.hangingPosition;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\EntityHanging.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */