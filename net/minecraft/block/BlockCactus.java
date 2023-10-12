/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyInteger;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockCactus
/*     */   extends Block {
/*  21 */   public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
/*     */ 
/*     */   
/*     */   protected BlockCactus() {
/*  25 */     super(Material.cactus);
/*  26 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)AGE, Integer.valueOf(0)));
/*  27 */     setTickRandomly(true);
/*  28 */     setCreativeTab(CreativeTabs.tabDecorations);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/*  33 */     BlockPos blockpos = pos.up();
/*     */     
/*  35 */     if (worldIn.isAirBlock(blockpos)) {
/*     */       int i;
/*     */ 
/*     */       
/*  39 */       for (i = 1; worldIn.getBlockState(pos.down(i)).getBlock() == this; i++);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  44 */       if (i < 3) {
/*     */         
/*  46 */         int j = ((Integer)state.getValue((IProperty)AGE)).intValue();
/*     */         
/*  48 */         if (j == 15) {
/*     */           
/*  50 */           worldIn.setBlockState(blockpos, getDefaultState());
/*  51 */           IBlockState iblockstate = state.withProperty((IProperty)AGE, Integer.valueOf(0));
/*  52 */           worldIn.setBlockState(pos, iblockstate, 4);
/*  53 */           onNeighborBlockChange(worldIn, blockpos, iblockstate, this);
/*     */         }
/*     */         else {
/*     */           
/*  57 */           worldIn.setBlockState(pos, state.withProperty((IProperty)AGE, Integer.valueOf(j + 1)), 4);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
/*  65 */     float f = 0.0625F;
/*  66 */     return new AxisAlignedBB((pos.getX() + f), pos.getY(), (pos.getZ() + f), ((pos.getX() + 1) - f), ((pos.getY() + 1) - f), ((pos.getZ() + 1) - f));
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
/*  71 */     float f = 0.0625F;
/*  72 */     return new AxisAlignedBB((pos.getX() + f), pos.getY(), (pos.getZ() + f), ((pos.getX() + 1) - f), (pos.getY() + 1), ((pos.getZ() + 1) - f));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/*  77 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/*  82 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
/*  87 */     return super.canPlaceBlockAt(worldIn, pos) ? canBlockStay(worldIn, pos) : false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/*  92 */     if (!canBlockStay(worldIn, pos))
/*     */     {
/*  94 */       worldIn.destroyBlock(pos, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBlockStay(World worldIn, BlockPos pos) {
/* 100 */     for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
/*     */       
/* 102 */       if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock().getMaterial().isSolid())
/*     */       {
/* 104 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 108 */     Block block = worldIn.getBlockState(pos.down()).getBlock();
/* 109 */     return (block == Blocks.cactus || block == Blocks.sand);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
/* 114 */     entityIn.attackEntityFrom(DamageSource.cactus, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer() {
/* 119 */     return EnumWorldBlockLayer.CUTOUT;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 124 */     return getDefaultState().withProperty((IProperty)AGE, Integer.valueOf(meta));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 129 */     return ((Integer)state.getValue((IProperty)AGE)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 134 */     return new BlockState(this, new IProperty[] { (IProperty)AGE });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockCactus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */