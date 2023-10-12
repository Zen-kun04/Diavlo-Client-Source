/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyInteger;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockCrops
/*     */   extends BlockBush implements IGrowable {
/*  19 */   public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);
/*     */ 
/*     */   
/*     */   protected BlockCrops() {
/*  23 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)AGE, Integer.valueOf(0)));
/*  24 */     setTickRandomly(true);
/*  25 */     float f = 0.5F;
/*  26 */     setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
/*  27 */     setCreativeTab((CreativeTabs)null);
/*  28 */     setHardness(0.0F);
/*  29 */     setStepSound(soundTypeGrass);
/*  30 */     disableStats();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canPlaceBlockOn(Block ground) {
/*  35 */     return (ground == Blocks.farmland);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/*  40 */     super.updateTick(worldIn, pos, state, rand);
/*     */     
/*  42 */     if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
/*     */       
/*  44 */       int i = ((Integer)state.getValue((IProperty)AGE)).intValue();
/*     */       
/*  46 */       if (i < 7) {
/*     */         
/*  48 */         float f = getGrowthChance(this, worldIn, pos);
/*     */         
/*  50 */         if (rand.nextInt((int)(25.0F / f) + 1) == 0)
/*     */         {
/*  52 */           worldIn.setBlockState(pos, state.withProperty((IProperty)AGE, Integer.valueOf(i + 1)), 2);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void grow(World worldIn, BlockPos pos, IBlockState state) {
/*  60 */     int i = ((Integer)state.getValue((IProperty)AGE)).intValue() + MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5);
/*     */     
/*  62 */     if (i > 7)
/*     */     {
/*  64 */       i = 7;
/*     */     }
/*     */     
/*  67 */     worldIn.setBlockState(pos, state.withProperty((IProperty)AGE, Integer.valueOf(i)), 2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static float getGrowthChance(Block blockIn, World worldIn, BlockPos pos) {
/*  72 */     float f = 1.0F;
/*  73 */     BlockPos blockpos = pos.down();
/*     */     
/*  75 */     for (int i = -1; i <= 1; i++) {
/*     */       
/*  77 */       for (int j = -1; j <= 1; j++) {
/*     */         
/*  79 */         float f1 = 0.0F;
/*  80 */         IBlockState iblockstate = worldIn.getBlockState(blockpos.add(i, 0, j));
/*     */         
/*  82 */         if (iblockstate.getBlock() == Blocks.farmland) {
/*     */           
/*  84 */           f1 = 1.0F;
/*     */           
/*  86 */           if (((Integer)iblockstate.getValue((IProperty)BlockFarmland.MOISTURE)).intValue() > 0)
/*     */           {
/*  88 */             f1 = 3.0F;
/*     */           }
/*     */         } 
/*     */         
/*  92 */         if (i != 0 || j != 0)
/*     */         {
/*  94 */           f1 /= 4.0F;
/*     */         }
/*     */         
/*  97 */         f += f1;
/*     */       } 
/*     */     } 
/*     */     
/* 101 */     BlockPos blockpos1 = pos.north();
/* 102 */     BlockPos blockpos2 = pos.south();
/* 103 */     BlockPos blockpos3 = pos.west();
/* 104 */     BlockPos blockpos4 = pos.east();
/* 105 */     boolean flag = (blockIn == worldIn.getBlockState(blockpos3).getBlock() || blockIn == worldIn.getBlockState(blockpos4).getBlock());
/* 106 */     boolean flag1 = (blockIn == worldIn.getBlockState(blockpos1).getBlock() || blockIn == worldIn.getBlockState(blockpos2).getBlock());
/*     */     
/* 108 */     if (flag && flag1) {
/*     */       
/* 110 */       f /= 2.0F;
/*     */     }
/*     */     else {
/*     */       
/* 114 */       boolean flag2 = (blockIn == worldIn.getBlockState(blockpos3.north()).getBlock() || blockIn == worldIn.getBlockState(blockpos4.north()).getBlock() || blockIn == worldIn.getBlockState(blockpos4.south()).getBlock() || blockIn == worldIn.getBlockState(blockpos3.south()).getBlock());
/*     */       
/* 116 */       if (flag2)
/*     */       {
/* 118 */         f /= 2.0F;
/*     */       }
/*     */     } 
/*     */     
/* 122 */     return f;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
/* 127 */     return ((worldIn.getLight(pos) >= 8 || worldIn.canSeeSky(pos)) && canPlaceBlockOn(worldIn.getBlockState(pos.down()).getBlock()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected Item getSeed() {
/* 132 */     return Items.wheat_seeds;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Item getCrop() {
/* 137 */     return Items.wheat;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
/* 142 */     super.dropBlockAsItemWithChance(worldIn, pos, state, chance, 0);
/*     */     
/* 144 */     if (!worldIn.isRemote) {
/*     */       
/* 146 */       int i = ((Integer)state.getValue((IProperty)AGE)).intValue();
/*     */       
/* 148 */       if (i >= 7) {
/*     */         
/* 150 */         int j = 3 + fortune;
/*     */         
/* 152 */         for (int k = 0; k < j; k++) {
/*     */           
/* 154 */           if (worldIn.rand.nextInt(15) <= i)
/*     */           {
/* 156 */             spawnAsEntity(worldIn, pos, new ItemStack(getSeed(), 1, 0));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/* 165 */     return (((Integer)state.getValue((IProperty)AGE)).intValue() == 7) ? getCrop() : getSeed();
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos) {
/* 170 */     return getSeed();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
/* 175 */     return (((Integer)state.getValue((IProperty)AGE)).intValue() < 7);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
/* 180 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
/* 185 */     grow(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 190 */     return getDefaultState().withProperty((IProperty)AGE, Integer.valueOf(meta));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 195 */     return ((Integer)state.getValue((IProperty)AGE)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 200 */     return new BlockState(this, new IProperty[] { (IProperty)AGE });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockCrops.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */