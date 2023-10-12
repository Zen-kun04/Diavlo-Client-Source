/*     */ package net.minecraft.entity.item;
/*     */ 
/*     */ import net.minecraft.block.BlockRailBase;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.projectile.EntityArrow;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.world.Explosion;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityMinecartTNT
/*     */   extends EntityMinecart {
/*  18 */   private int minecartTNTFuse = -1;
/*     */ 
/*     */   
/*     */   public EntityMinecartTNT(World worldIn) {
/*  22 */     super(worldIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityMinecartTNT(World worldIn, double x, double y, double z) {
/*  27 */     super(worldIn, x, y, z);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityMinecart.EnumMinecartType getMinecartType() {
/*  32 */     return EntityMinecart.EnumMinecartType.TNT;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getDefaultDisplayTile() {
/*  37 */     return Blocks.tnt.getDefaultState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  42 */     super.onUpdate();
/*     */     
/*  44 */     if (this.minecartTNTFuse > 0) {
/*     */       
/*  46 */       this.minecartTNTFuse--;
/*  47 */       this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */     }
/*  49 */     else if (this.minecartTNTFuse == 0) {
/*     */       
/*  51 */       explodeCart(this.motionX * this.motionX + this.motionZ * this.motionZ);
/*     */     } 
/*     */     
/*  54 */     if (this.isCollidedHorizontally) {
/*     */       
/*  56 */       double d0 = this.motionX * this.motionX + this.motionZ * this.motionZ;
/*     */       
/*  58 */       if (d0 >= 0.009999999776482582D)
/*     */       {
/*  60 */         explodeCart(d0);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityFrom(DamageSource source, float amount) {
/*  67 */     Entity entity = source.getSourceOfDamage();
/*     */     
/*  69 */     if (entity instanceof EntityArrow) {
/*     */       
/*  71 */       EntityArrow entityarrow = (EntityArrow)entity;
/*     */       
/*  73 */       if (entityarrow.isBurning())
/*     */       {
/*  75 */         explodeCart(entityarrow.motionX * entityarrow.motionX + entityarrow.motionY * entityarrow.motionY + entityarrow.motionZ * entityarrow.motionZ);
/*     */       }
/*     */     } 
/*     */     
/*  79 */     return super.attackEntityFrom(source, amount);
/*     */   }
/*     */ 
/*     */   
/*     */   public void killMinecart(DamageSource source) {
/*  84 */     super.killMinecart(source);
/*  85 */     double d0 = this.motionX * this.motionX + this.motionZ * this.motionZ;
/*     */     
/*  87 */     if (!source.isExplosion() && this.worldObj.getGameRules().getBoolean("doEntityDrops"))
/*     */     {
/*  89 */       entityDropItem(new ItemStack(Blocks.tnt, 1), 0.0F);
/*     */     }
/*     */     
/*  92 */     if (source.isFireDamage() || source.isExplosion() || d0 >= 0.009999999776482582D)
/*     */     {
/*  94 */       explodeCart(d0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void explodeCart(double p_94103_1_) {
/* 100 */     if (!this.worldObj.isRemote) {
/*     */       
/* 102 */       double d0 = Math.sqrt(p_94103_1_);
/*     */       
/* 104 */       if (d0 > 5.0D)
/*     */       {
/* 106 */         d0 = 5.0D;
/*     */       }
/*     */       
/* 109 */       this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float)(4.0D + this.rand.nextDouble() * 1.5D * d0), true);
/* 110 */       setDead();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void fall(float distance, float damageMultiplier) {
/* 116 */     if (distance >= 3.0F) {
/*     */       
/* 118 */       float f = distance / 10.0F;
/* 119 */       explodeCart((f * f));
/*     */     } 
/*     */     
/* 122 */     super.fall(distance, damageMultiplier);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onActivatorRailPass(int x, int y, int z, boolean receivingPower) {
/* 127 */     if (receivingPower && this.minecartTNTFuse < 0)
/*     */     {
/* 129 */       ignite();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleStatusUpdate(byte id) {
/* 135 */     if (id == 10) {
/*     */       
/* 137 */       ignite();
/*     */     }
/*     */     else {
/*     */       
/* 141 */       super.handleStatusUpdate(id);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void ignite() {
/* 147 */     this.minecartTNTFuse = 80;
/*     */     
/* 149 */     if (!this.worldObj.isRemote) {
/*     */       
/* 151 */       this.worldObj.setEntityState(this, (byte)10);
/*     */       
/* 153 */       if (!isSilent())
/*     */       {
/* 155 */         this.worldObj.playSoundAtEntity(this, "game.tnt.primed", 1.0F, 1.0F);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFuseTicks() {
/* 162 */     return this.minecartTNTFuse;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIgnited() {
/* 167 */     return (this.minecartTNTFuse > -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
/* 172 */     return (!isIgnited() || (!BlockRailBase.isRailBlock(blockStateIn) && !BlockRailBase.isRailBlock(worldIn, pos.up()))) ? super.getExplosionResistance(explosionIn, worldIn, pos, blockStateIn) : 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean verifyExplosion(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn, float p_174816_5_) {
/* 177 */     return (!isIgnited() || (!BlockRailBase.isRailBlock(blockStateIn) && !BlockRailBase.isRailBlock(worldIn, pos.up()))) ? super.verifyExplosion(explosionIn, worldIn, pos, blockStateIn, p_174816_5_) : false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readEntityFromNBT(NBTTagCompound tagCompund) {
/* 182 */     super.readEntityFromNBT(tagCompund);
/*     */     
/* 184 */     if (tagCompund.hasKey("TNTFuse", 99))
/*     */     {
/* 186 */       this.minecartTNTFuse = tagCompund.getInteger("TNTFuse");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeEntityToNBT(NBTTagCompound tagCompound) {
/* 192 */     super.writeEntityToNBT(tagCompound);
/* 193 */     tagCompound.setInteger("TNTFuse", this.minecartTNTFuse);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\item\EntityMinecartTNT.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */