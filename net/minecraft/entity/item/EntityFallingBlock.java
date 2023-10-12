/*     */ package net.minecraft.entity.item;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockAnvil;
/*     */ import net.minecraft.block.BlockFalling;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityFallingBlock
/*     */   extends Entity {
/*     */   private IBlockState fallTile;
/*     */   public int fallTime;
/*     */   public boolean shouldDropItem = true;
/*     */   private boolean canSetAsBlock;
/*     */   private boolean hurtEntities;
/*  32 */   private int fallHurtMax = 40;
/*  33 */   private float fallHurtAmount = 2.0F;
/*     */   
/*     */   public NBTTagCompound tileEntityData;
/*     */   
/*     */   public EntityFallingBlock(World worldIn) {
/*  38 */     super(worldIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityFallingBlock(World worldIn, double x, double y, double z, IBlockState fallingBlockState) {
/*  43 */     super(worldIn);
/*  44 */     this.fallTile = fallingBlockState;
/*  45 */     this.preventEntitySpawning = true;
/*  46 */     setSize(0.98F, 0.98F);
/*  47 */     setPosition(x, y, z);
/*  48 */     this.motionX = 0.0D;
/*  49 */     this.motionY = 0.0D;
/*  50 */     this.motionZ = 0.0D;
/*  51 */     this.prevPosX = x;
/*  52 */     this.prevPosY = y;
/*  53 */     this.prevPosZ = z;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canTriggerWalking() {
/*  58 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void entityInit() {}
/*     */ 
/*     */   
/*     */   public boolean canBeCollidedWith() {
/*  67 */     return !this.isDead;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  72 */     Block block = this.fallTile.getBlock();
/*     */     
/*  74 */     if (block.getMaterial() == Material.air) {
/*     */       
/*  76 */       setDead();
/*     */     }
/*     */     else {
/*     */       
/*  80 */       this.prevPosX = this.posX;
/*  81 */       this.prevPosY = this.posY;
/*  82 */       this.prevPosZ = this.posZ;
/*     */       
/*  84 */       if (this.fallTime++ == 0) {
/*     */         
/*  86 */         BlockPos blockpos = new BlockPos(this);
/*     */         
/*  88 */         if (this.worldObj.getBlockState(blockpos).getBlock() == block) {
/*     */           
/*  90 */           this.worldObj.setBlockToAir(blockpos);
/*     */         }
/*  92 */         else if (!this.worldObj.isRemote) {
/*     */           
/*  94 */           setDead();
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*  99 */       this.motionY -= 0.03999999910593033D;
/* 100 */       moveEntity(this.motionX, this.motionY, this.motionZ);
/* 101 */       this.motionX *= 0.9800000190734863D;
/* 102 */       this.motionY *= 0.9800000190734863D;
/* 103 */       this.motionZ *= 0.9800000190734863D;
/*     */       
/* 105 */       if (!this.worldObj.isRemote) {
/*     */         
/* 107 */         BlockPos blockpos1 = new BlockPos(this);
/*     */         
/* 109 */         if (this.onGround) {
/*     */           
/* 111 */           this.motionX *= 0.699999988079071D;
/* 112 */           this.motionZ *= 0.699999988079071D;
/* 113 */           this.motionY *= -0.5D;
/*     */           
/* 115 */           if (this.worldObj.getBlockState(blockpos1).getBlock() != Blocks.piston_extension) {
/*     */             
/* 117 */             setDead();
/*     */             
/* 119 */             if (!this.canSetAsBlock)
/*     */             {
/* 121 */               if (this.worldObj.canBlockBePlaced(block, blockpos1, true, EnumFacing.UP, (Entity)null, (ItemStack)null) && !BlockFalling.canFallInto(this.worldObj, blockpos1.down()) && this.worldObj.setBlockState(blockpos1, this.fallTile, 3)) {
/*     */                 
/* 123 */                 if (block instanceof BlockFalling)
/*     */                 {
/* 125 */                   ((BlockFalling)block).onEndFalling(this.worldObj, blockpos1);
/*     */                 }
/*     */                 
/* 128 */                 if (this.tileEntityData != null && block instanceof net.minecraft.block.ITileEntityProvider) {
/*     */                   
/* 130 */                   TileEntity tileentity = this.worldObj.getTileEntity(blockpos1);
/*     */                   
/* 132 */                   if (tileentity != null)
/*     */                   {
/* 134 */                     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 135 */                     tileentity.writeToNBT(nbttagcompound);
/*     */                     
/* 137 */                     for (String s : this.tileEntityData.getKeySet()) {
/*     */                       
/* 139 */                       NBTBase nbtbase = this.tileEntityData.getTag(s);
/*     */                       
/* 141 */                       if (!s.equals("x") && !s.equals("y") && !s.equals("z"))
/*     */                       {
/* 143 */                         nbttagcompound.setTag(s, nbtbase.copy());
/*     */                       }
/*     */                     } 
/*     */                     
/* 147 */                     tileentity.readFromNBT(nbttagcompound);
/* 148 */                     tileentity.markDirty();
/*     */                   }
/*     */                 
/*     */                 } 
/* 152 */               } else if (this.shouldDropItem && this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
/*     */                 
/* 154 */                 entityDropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0.0F);
/*     */               }
/*     */             
/*     */             }
/*     */           } 
/* 159 */         } else if ((this.fallTime > 100 && !this.worldObj.isRemote && (blockpos1.getY() < 1 || blockpos1.getY() > 256)) || this.fallTime > 600) {
/*     */           
/* 161 */           if (this.shouldDropItem && this.worldObj.getGameRules().getBoolean("doEntityDrops"))
/*     */           {
/* 163 */             entityDropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0.0F);
/*     */           }
/*     */           
/* 166 */           setDead();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void fall(float distance, float damageMultiplier) {
/* 174 */     Block block = this.fallTile.getBlock();
/*     */     
/* 176 */     if (this.hurtEntities) {
/*     */       
/* 178 */       int i = MathHelper.ceiling_float_int(distance - 1.0F);
/*     */       
/* 180 */       if (i > 0) {
/*     */         
/* 182 */         List<Entity> list = Lists.newArrayList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox()));
/* 183 */         boolean flag = (block == Blocks.anvil);
/* 184 */         DamageSource damagesource = flag ? DamageSource.anvil : DamageSource.fallingBlock;
/*     */         
/* 186 */         for (Entity entity : list)
/*     */         {
/* 188 */           entity.attackEntityFrom(damagesource, Math.min(MathHelper.floor_float(i * this.fallHurtAmount), this.fallHurtMax));
/*     */         }
/*     */         
/* 191 */         if (flag && this.rand.nextFloat() < 0.05000000074505806D + i * 0.05D) {
/*     */           
/* 193 */           int j = ((Integer)this.fallTile.getValue((IProperty)BlockAnvil.DAMAGE)).intValue();
/* 194 */           j++;
/*     */           
/* 196 */           if (j > 2) {
/*     */             
/* 198 */             this.canSetAsBlock = true;
/*     */           }
/*     */           else {
/*     */             
/* 202 */             this.fallTile = this.fallTile.withProperty((IProperty)BlockAnvil.DAMAGE, Integer.valueOf(j));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeEntityToNBT(NBTTagCompound tagCompound) {
/* 211 */     Block block = (this.fallTile != null) ? this.fallTile.getBlock() : Blocks.air;
/* 212 */     ResourceLocation resourcelocation = (ResourceLocation)Block.blockRegistry.getNameForObject(block);
/* 213 */     tagCompound.setString("Block", (resourcelocation == null) ? "" : resourcelocation.toString());
/* 214 */     tagCompound.setByte("Data", (byte)block.getMetaFromState(this.fallTile));
/* 215 */     tagCompound.setByte("Time", (byte)this.fallTime);
/* 216 */     tagCompound.setBoolean("DropItem", this.shouldDropItem);
/* 217 */     tagCompound.setBoolean("HurtEntities", this.hurtEntities);
/* 218 */     tagCompound.setFloat("FallHurtAmount", this.fallHurtAmount);
/* 219 */     tagCompound.setInteger("FallHurtMax", this.fallHurtMax);
/*     */     
/* 221 */     if (this.tileEntityData != null)
/*     */     {
/* 223 */       tagCompound.setTag("TileEntityData", (NBTBase)this.tileEntityData);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readEntityFromNBT(NBTTagCompound tagCompund) {
/* 229 */     int i = tagCompund.getByte("Data") & 0xFF;
/*     */     
/* 231 */     if (tagCompund.hasKey("Block", 8)) {
/*     */       
/* 233 */       this.fallTile = Block.getBlockFromName(tagCompund.getString("Block")).getStateFromMeta(i);
/*     */     }
/* 235 */     else if (tagCompund.hasKey("TileID", 99)) {
/*     */       
/* 237 */       this.fallTile = Block.getBlockById(tagCompund.getInteger("TileID")).getStateFromMeta(i);
/*     */     }
/*     */     else {
/*     */       
/* 241 */       this.fallTile = Block.getBlockById(tagCompund.getByte("Tile") & 0xFF).getStateFromMeta(i);
/*     */     } 
/*     */     
/* 244 */     this.fallTime = tagCompund.getByte("Time") & 0xFF;
/* 245 */     Block block = this.fallTile.getBlock();
/*     */     
/* 247 */     if (tagCompund.hasKey("HurtEntities", 99)) {
/*     */       
/* 249 */       this.hurtEntities = tagCompund.getBoolean("HurtEntities");
/* 250 */       this.fallHurtAmount = tagCompund.getFloat("FallHurtAmount");
/* 251 */       this.fallHurtMax = tagCompund.getInteger("FallHurtMax");
/*     */     }
/* 253 */     else if (block == Blocks.anvil) {
/*     */       
/* 255 */       this.hurtEntities = true;
/*     */     } 
/*     */     
/* 258 */     if (tagCompund.hasKey("DropItem", 99))
/*     */     {
/* 260 */       this.shouldDropItem = tagCompund.getBoolean("DropItem");
/*     */     }
/*     */     
/* 263 */     if (tagCompund.hasKey("TileEntityData", 10))
/*     */     {
/* 265 */       this.tileEntityData = tagCompund.getCompoundTag("TileEntityData");
/*     */     }
/*     */     
/* 268 */     if (block == null || block.getMaterial() == Material.air)
/*     */     {
/* 270 */       this.fallTile = Blocks.sand.getDefaultState();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public World getWorldObj() {
/* 276 */     return this.worldObj;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHurtEntities(boolean p_145806_1_) {
/* 281 */     this.hurtEntities = p_145806_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canRenderOnFire() {
/* 286 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addEntityCrashInfo(CrashReportCategory category) {
/* 291 */     super.addEntityCrashInfo(category);
/*     */     
/* 293 */     if (this.fallTile != null) {
/*     */       
/* 295 */       Block block = this.fallTile.getBlock();
/* 296 */       category.addCrashSection("Immitating block ID", Integer.valueOf(Block.getIdFromBlock(block)));
/* 297 */       category.addCrashSection("Immitating block data", Integer.valueOf(block.getMetaFromState(this.fallTile)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getBlock() {
/* 303 */     return this.fallTile;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\item\EntityFallingBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */