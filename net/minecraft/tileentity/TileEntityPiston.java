/*     */ package net.minecraft.tileentity;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.ITickable;
/*     */ 
/*     */ public class TileEntityPiston
/*     */   extends TileEntity implements ITickable {
/*     */   private IBlockState pistonState;
/*     */   private EnumFacing pistonFacing;
/*     */   private boolean extending;
/*     */   private boolean shouldHeadBeRendered;
/*     */   private float progress;
/*     */   private float lastProgress;
/*  22 */   private List<Entity> field_174933_k = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */   
/*     */   public TileEntityPiston() {}
/*     */ 
/*     */   
/*     */   public TileEntityPiston(IBlockState pistonStateIn, EnumFacing pistonFacingIn, boolean extendingIn, boolean shouldHeadBeRenderedIn) {
/*  30 */     this.pistonState = pistonStateIn;
/*  31 */     this.pistonFacing = pistonFacingIn;
/*  32 */     this.extending = extendingIn;
/*  33 */     this.shouldHeadBeRendered = shouldHeadBeRenderedIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getPistonState() {
/*  38 */     return this.pistonState;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBlockMetadata() {
/*  43 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isExtending() {
/*  48 */     return this.extending;
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumFacing getFacing() {
/*  53 */     return this.pistonFacing;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldPistonHeadBeRendered() {
/*  58 */     return this.shouldHeadBeRendered;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getProgress(float ticks) {
/*  63 */     if (ticks > 1.0F)
/*     */     {
/*  65 */       ticks = 1.0F;
/*     */     }
/*     */     
/*  68 */     return this.lastProgress + (this.progress - this.lastProgress) * ticks;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getOffsetX(float ticks) {
/*  73 */     return this.extending ? ((getProgress(ticks) - 1.0F) * this.pistonFacing.getFrontOffsetX()) : ((1.0F - getProgress(ticks)) * this.pistonFacing.getFrontOffsetX());
/*     */   }
/*     */ 
/*     */   
/*     */   public float getOffsetY(float ticks) {
/*  78 */     return this.extending ? ((getProgress(ticks) - 1.0F) * this.pistonFacing.getFrontOffsetY()) : ((1.0F - getProgress(ticks)) * this.pistonFacing.getFrontOffsetY());
/*     */   }
/*     */ 
/*     */   
/*     */   public float getOffsetZ(float ticks) {
/*  83 */     return this.extending ? ((getProgress(ticks) - 1.0F) * this.pistonFacing.getFrontOffsetZ()) : ((1.0F - getProgress(ticks)) * this.pistonFacing.getFrontOffsetZ());
/*     */   }
/*     */ 
/*     */   
/*     */   private void launchWithSlimeBlock(float p_145863_1_, float p_145863_2_) {
/*  88 */     if (this.extending) {
/*     */       
/*  90 */       p_145863_1_ = 1.0F - p_145863_1_;
/*     */     }
/*     */     else {
/*     */       
/*  94 */       p_145863_1_--;
/*     */     } 
/*     */     
/*  97 */     AxisAlignedBB axisalignedbb = Blocks.piston_extension.getBoundingBox(this.worldObj, this.pos, this.pistonState, p_145863_1_, this.pistonFacing);
/*     */     
/*  99 */     if (axisalignedbb != null) {
/*     */       
/* 101 */       List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity((Entity)null, axisalignedbb);
/*     */       
/* 103 */       if (!list.isEmpty()) {
/*     */         
/* 105 */         this.field_174933_k.addAll(list);
/*     */         
/* 107 */         for (Entity entity : this.field_174933_k) {
/*     */           
/* 109 */           if (this.pistonState.getBlock() == Blocks.slime_block && this.extending) {
/*     */             
/* 111 */             switch (this.pistonFacing.getAxis()) {
/*     */               
/*     */               case X:
/* 114 */                 entity.motionX = this.pistonFacing.getFrontOffsetX();
/*     */                 continue;
/*     */               
/*     */               case Y:
/* 118 */                 entity.motionY = this.pistonFacing.getFrontOffsetY();
/*     */                 continue;
/*     */               
/*     */               case Z:
/* 122 */                 entity.motionZ = this.pistonFacing.getFrontOffsetZ();
/*     */                 continue;
/*     */             } 
/*     */             continue;
/*     */           } 
/* 127 */           entity.moveEntity((p_145863_2_ * this.pistonFacing.getFrontOffsetX()), (p_145863_2_ * this.pistonFacing.getFrontOffsetY()), (p_145863_2_ * this.pistonFacing.getFrontOffsetZ()));
/*     */         } 
/*     */ 
/*     */         
/* 131 */         this.field_174933_k.clear();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearPistonTileEntity() {
/* 138 */     if (this.lastProgress < 1.0F && this.worldObj != null) {
/*     */       
/* 140 */       this.lastProgress = this.progress = 1.0F;
/* 141 */       this.worldObj.removeTileEntity(this.pos);
/* 142 */       invalidate();
/*     */       
/* 144 */       if (this.worldObj.getBlockState(this.pos).getBlock() == Blocks.piston_extension) {
/*     */         
/* 146 */         this.worldObj.setBlockState(this.pos, this.pistonState, 3);
/* 147 */         this.worldObj.notifyBlockOfStateChange(this.pos, this.pistonState.getBlock());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void update() {
/* 154 */     this.lastProgress = this.progress;
/*     */     
/* 156 */     if (this.lastProgress >= 1.0F) {
/*     */       
/* 158 */       launchWithSlimeBlock(1.0F, 0.25F);
/* 159 */       this.worldObj.removeTileEntity(this.pos);
/* 160 */       invalidate();
/*     */       
/* 162 */       if (this.worldObj.getBlockState(this.pos).getBlock() == Blocks.piston_extension)
/*     */       {
/* 164 */         this.worldObj.setBlockState(this.pos, this.pistonState, 3);
/* 165 */         this.worldObj.notifyBlockOfStateChange(this.pos, this.pistonState.getBlock());
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 170 */       this.progress += 0.5F;
/*     */       
/* 172 */       if (this.progress >= 1.0F)
/*     */       {
/* 174 */         this.progress = 1.0F;
/*     */       }
/*     */       
/* 177 */       if (this.extending)
/*     */       {
/* 179 */         launchWithSlimeBlock(this.progress, this.progress - this.lastProgress + 0.0625F);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void readFromNBT(NBTTagCompound compound) {
/* 186 */     super.readFromNBT(compound);
/* 187 */     this.pistonState = Block.getBlockById(compound.getInteger("blockId")).getStateFromMeta(compound.getInteger("blockData"));
/* 188 */     this.pistonFacing = EnumFacing.getFront(compound.getInteger("facing"));
/* 189 */     this.lastProgress = this.progress = compound.getFloat("progress");
/* 190 */     this.extending = compound.getBoolean("extending");
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeToNBT(NBTTagCompound compound) {
/* 195 */     super.writeToNBT(compound);
/* 196 */     compound.setInteger("blockId", Block.getIdFromBlock(this.pistonState.getBlock()));
/* 197 */     compound.setInteger("blockData", this.pistonState.getBlock().getMetaFromState(this.pistonState));
/* 198 */     compound.setInteger("facing", this.pistonFacing.getIndex());
/* 199 */     compound.setFloat("progress", this.lastProgress);
/* 200 */     compound.setBoolean("extending", this.extending);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\tileentity\TileEntityPiston.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */