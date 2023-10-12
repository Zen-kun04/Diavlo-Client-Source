/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityFallingBlock;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockDragonEgg
/*     */   extends Block {
/*     */   public BlockDragonEgg() {
/*  20 */     super(Material.dragonEgg, MapColor.blackColor);
/*  21 */     setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 1.0F, 0.9375F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
/*  26 */     worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/*  31 */     worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/*  36 */     checkFall(worldIn, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkFall(World worldIn, BlockPos pos) {
/*  41 */     if (BlockFalling.canFallInto(worldIn, pos.down()) && pos.getY() >= 0) {
/*     */       
/*  43 */       int i = 32;
/*     */       
/*  45 */       if (!BlockFalling.fallInstantly && worldIn.isAreaLoaded(pos.add(-i, -i, -i), pos.add(i, i, i))) {
/*     */         
/*  47 */         worldIn.spawnEntityInWorld((Entity)new EntityFallingBlock(worldIn, (pos.getX() + 0.5F), pos.getY(), (pos.getZ() + 0.5F), getDefaultState()));
/*     */       }
/*     */       else {
/*     */         
/*  51 */         worldIn.setBlockToAir(pos);
/*     */         
/*     */         BlockPos blockpos;
/*  54 */         for (blockpos = pos; BlockFalling.canFallInto(worldIn, blockpos) && blockpos.getY() > 0; blockpos = blockpos.down());
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  59 */         if (blockpos.getY() > 0)
/*     */         {
/*  61 */           worldIn.setBlockState(blockpos, getDefaultState(), 2);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
/*  69 */     teleport(worldIn, pos);
/*  70 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
/*  75 */     teleport(worldIn, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   private void teleport(World worldIn, BlockPos pos) {
/*  80 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */     
/*  82 */     if (iblockstate.getBlock() == this)
/*     */     {
/*  84 */       for (int i = 0; i < 1000; i++) {
/*     */         
/*  86 */         BlockPos blockpos = pos.add(worldIn.rand.nextInt(16) - worldIn.rand.nextInt(16), worldIn.rand.nextInt(8) - worldIn.rand.nextInt(8), worldIn.rand.nextInt(16) - worldIn.rand.nextInt(16));
/*     */         
/*  88 */         if ((worldIn.getBlockState(blockpos).getBlock()).blockMaterial == Material.air) {
/*     */           
/*  90 */           if (worldIn.isRemote) {
/*     */             
/*  92 */             for (int j = 0; j < 128; j++)
/*     */             {
/*  94 */               double d0 = worldIn.rand.nextDouble();
/*  95 */               float f = (worldIn.rand.nextFloat() - 0.5F) * 0.2F;
/*  96 */               float f1 = (worldIn.rand.nextFloat() - 0.5F) * 0.2F;
/*  97 */               float f2 = (worldIn.rand.nextFloat() - 0.5F) * 0.2F;
/*  98 */               double d1 = blockpos.getX() + (pos.getX() - blockpos.getX()) * d0 + (worldIn.rand.nextDouble() - 0.5D) * 1.0D + 0.5D;
/*  99 */               double d2 = blockpos.getY() + (pos.getY() - blockpos.getY()) * d0 + worldIn.rand.nextDouble() * 1.0D - 0.5D;
/* 100 */               double d3 = blockpos.getZ() + (pos.getZ() - blockpos.getZ()) * d0 + (worldIn.rand.nextDouble() - 0.5D) * 1.0D + 0.5D;
/* 101 */               worldIn.spawnParticle(EnumParticleTypes.PORTAL, d1, d2, d3, f, f1, f2, new int[0]);
/*     */             }
/*     */           
/*     */           } else {
/*     */             
/* 106 */             worldIn.setBlockState(blockpos, iblockstate, 2);
/* 107 */             worldIn.setBlockToAir(pos);
/*     */           } 
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int tickRate(World worldIn) {
/* 118 */     return 5;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/* 123 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/* 128 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
/* 133 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos) {
/* 138 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockDragonEgg.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */