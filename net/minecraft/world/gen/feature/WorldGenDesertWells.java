/*     */ package net.minecraft.world.gen.feature;
/*     */ import com.google.common.base.Predicates;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockSand;
/*     */ import net.minecraft.block.BlockSlab;
/*     */ import net.minecraft.block.BlockStoneSlab;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.block.state.pattern.BlockStateHelper;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class WorldGenDesertWells extends WorldGenerator {
/*  17 */   private static final BlockStateHelper field_175913_a = BlockStateHelper.forBlock((Block)Blocks.sand).where((IProperty)BlockSand.VARIANT, Predicates.equalTo(BlockSand.EnumType.SAND));
/*  18 */   private final IBlockState field_175911_b = Blocks.stone_slab.getDefaultState().withProperty((IProperty)BlockStoneSlab.VARIANT, (Comparable)BlockStoneSlab.EnumType.SAND).withProperty((IProperty)BlockSlab.HALF, (Comparable)BlockSlab.EnumBlockHalf.BOTTOM);
/*  19 */   private final IBlockState field_175912_c = Blocks.sandstone.getDefaultState();
/*  20 */   private final IBlockState field_175910_d = Blocks.flowing_water.getDefaultState();
/*     */ 
/*     */   
/*     */   public boolean generate(World worldIn, Random rand, BlockPos position) {
/*  24 */     while (worldIn.isAirBlock(position) && position.getY() > 2)
/*     */     {
/*  26 */       position = position.down();
/*     */     }
/*     */     
/*  29 */     if (!field_175913_a.apply(worldIn.getBlockState(position)))
/*     */     {
/*  31 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  35 */     for (int i = -2; i <= 2; i++) {
/*     */       
/*  37 */       for (int j = -2; j <= 2; j++) {
/*     */         
/*  39 */         if (worldIn.isAirBlock(position.add(i, -1, j)) && worldIn.isAirBlock(position.add(i, -2, j)))
/*     */         {
/*  41 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  46 */     for (int l = -1; l <= 0; l++) {
/*     */       
/*  48 */       for (int l1 = -2; l1 <= 2; l1++) {
/*     */         
/*  50 */         for (int k = -2; k <= 2; k++)
/*     */         {
/*  52 */           worldIn.setBlockState(position.add(l1, l, k), this.field_175912_c, 2);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  57 */     worldIn.setBlockState(position, this.field_175910_d, 2);
/*     */     
/*  59 */     for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
/*     */     {
/*  61 */       worldIn.setBlockState(position.offset(enumfacing), this.field_175910_d, 2);
/*     */     }
/*     */     
/*  64 */     for (int i1 = -2; i1 <= 2; i1++) {
/*     */       
/*  66 */       for (int i2 = -2; i2 <= 2; i2++) {
/*     */         
/*  68 */         if (i1 == -2 || i1 == 2 || i2 == -2 || i2 == 2)
/*     */         {
/*  70 */           worldIn.setBlockState(position.add(i1, 1, i2), this.field_175912_c, 2);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  75 */     worldIn.setBlockState(position.add(2, 1, 0), this.field_175911_b, 2);
/*  76 */     worldIn.setBlockState(position.add(-2, 1, 0), this.field_175911_b, 2);
/*  77 */     worldIn.setBlockState(position.add(0, 1, 2), this.field_175911_b, 2);
/*  78 */     worldIn.setBlockState(position.add(0, 1, -2), this.field_175911_b, 2);
/*     */     
/*  80 */     for (int j1 = -1; j1 <= 1; j1++) {
/*     */       
/*  82 */       for (int j2 = -1; j2 <= 1; j2++) {
/*     */         
/*  84 */         if (j1 == 0 && j2 == 0) {
/*     */           
/*  86 */           worldIn.setBlockState(position.add(j1, 4, j2), this.field_175912_c, 2);
/*     */         }
/*     */         else {
/*     */           
/*  90 */           worldIn.setBlockState(position.add(j1, 4, j2), this.field_175911_b, 2);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  95 */     for (int k1 = 1; k1 <= 3; k1++) {
/*     */       
/*  97 */       worldIn.setBlockState(position.add(-1, k1, -1), this.field_175912_c, 2);
/*  98 */       worldIn.setBlockState(position.add(-1, k1, 1), this.field_175912_c, 2);
/*  99 */       worldIn.setBlockState(position.add(1, k1, -1), this.field_175912_c, 2);
/* 100 */       worldIn.setBlockState(position.add(1, k1, 1), this.field_175912_c, 2);
/*     */     } 
/*     */     
/* 103 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\feature\WorldGenDesertWells.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */