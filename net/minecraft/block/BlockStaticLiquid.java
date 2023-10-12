/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockStaticLiquid
/*     */   extends BlockLiquid {
/*     */   protected BlockStaticLiquid(Material materialIn) {
/*  15 */     super(materialIn);
/*  16 */     setTickRandomly(false);
/*     */     
/*  18 */     if (materialIn == Material.lava)
/*     */     {
/*  20 */       setTickRandomly(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/*  26 */     if (!checkForMixing(worldIn, pos, state))
/*     */     {
/*  28 */       updateLiquid(worldIn, pos, state);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateLiquid(World worldIn, BlockPos pos, IBlockState state) {
/*  34 */     BlockDynamicLiquid blockdynamicliquid = getFlowingBlock(this.blockMaterial);
/*  35 */     worldIn.setBlockState(pos, blockdynamicliquid.getDefaultState().withProperty((IProperty)LEVEL, state.getValue((IProperty)LEVEL)), 2);
/*  36 */     worldIn.scheduleUpdate(pos, blockdynamicliquid, tickRate(worldIn));
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/*  41 */     if (this.blockMaterial == Material.lava)
/*     */     {
/*  43 */       if (worldIn.getGameRules().getBoolean("doFireTick")) {
/*     */         
/*  45 */         int i = rand.nextInt(3);
/*     */         
/*  47 */         if (i > 0) {
/*     */           
/*  49 */           BlockPos blockpos = pos;
/*     */           
/*  51 */           for (int j = 0; j < i; j++) {
/*     */             
/*  53 */             blockpos = blockpos.add(rand.nextInt(3) - 1, 1, rand.nextInt(3) - 1);
/*  54 */             Block block = worldIn.getBlockState(blockpos).getBlock();
/*     */             
/*  56 */             if (block.blockMaterial == Material.air) {
/*     */               
/*  58 */               if (isSurroundingBlockFlammable(worldIn, blockpos)) {
/*     */                 
/*  60 */                 worldIn.setBlockState(blockpos, Blocks.fire.getDefaultState());
/*     */                 
/*     */                 return;
/*     */               } 
/*  64 */             } else if (block.blockMaterial.blocksMovement()) {
/*     */               
/*     */               return;
/*     */             }
/*     */           
/*     */           } 
/*     */         } else {
/*     */           
/*  72 */           for (int k = 0; k < 3; k++) {
/*     */             
/*  74 */             BlockPos blockpos1 = pos.add(rand.nextInt(3) - 1, 0, rand.nextInt(3) - 1);
/*     */             
/*  76 */             if (worldIn.isAirBlock(blockpos1.up()) && getCanBlockBurn(worldIn, blockpos1))
/*     */             {
/*  78 */               worldIn.setBlockState(blockpos1.up(), Blocks.fire.getDefaultState());
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSurroundingBlockFlammable(World worldIn, BlockPos pos) {
/*  88 */     for (EnumFacing enumfacing : EnumFacing.values()) {
/*     */       
/*  90 */       if (getCanBlockBurn(worldIn, pos.offset(enumfacing)))
/*     */       {
/*  92 */         return true;
/*     */       }
/*     */     } 
/*     */     
/*  96 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean getCanBlockBurn(World worldIn, BlockPos pos) {
/* 101 */     return worldIn.getBlockState(pos).getBlock().getMaterial().getCanBurn();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockStaticLiquid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */