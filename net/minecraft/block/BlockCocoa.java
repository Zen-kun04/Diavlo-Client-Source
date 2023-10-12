/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyInteger;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockCocoa
/*     */   extends BlockDirectional implements IGrowable {
/*  24 */   public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 2);
/*     */ 
/*     */   
/*     */   public BlockCocoa() {
/*  28 */     super(Material.plants);
/*  29 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)AGE, Integer.valueOf(0)));
/*  30 */     setTickRandomly(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/*  35 */     if (!canBlockStay(worldIn, pos, state)) {
/*     */       
/*  37 */       dropBlock(worldIn, pos, state);
/*     */     }
/*  39 */     else if (worldIn.rand.nextInt(5) == 0) {
/*     */       
/*  41 */       int i = ((Integer)state.getValue((IProperty)AGE)).intValue();
/*     */       
/*  43 */       if (i < 2)
/*     */       {
/*  45 */         worldIn.setBlockState(pos, state.withProperty((IProperty)AGE, Integer.valueOf(i + 1)), 2);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
/*  52 */     pos = pos.offset((EnumFacing)state.getValue((IProperty)FACING));
/*  53 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*  54 */     return (iblockstate.getBlock() == Blocks.log && iblockstate.getValue((IProperty)BlockPlanks.VARIANT) == BlockPlanks.EnumType.JUNGLE);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/*  59 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/*  64 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
/*  69 */     setBlockBoundsBasedOnState((IBlockAccess)worldIn, pos);
/*  70 */     return super.getCollisionBoundingBox(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
/*  75 */     setBlockBoundsBasedOnState((IBlockAccess)worldIn, pos);
/*  76 */     return super.getSelectedBoundingBox(worldIn, pos);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
/*  82 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*  83 */     EnumFacing enumfacing = (EnumFacing)iblockstate.getValue((IProperty)FACING);
/*  84 */     int i = ((Integer)iblockstate.getValue((IProperty)AGE)).intValue();
/*  85 */     int j = 4 + i * 2;
/*  86 */     int k = 5 + i * 2;
/*  87 */     float f = j / 2.0F;
/*     */     
/*  89 */     switch (enumfacing) {
/*     */       
/*     */       case SOUTH:
/*  92 */         setBlockBounds((8.0F - f) / 16.0F, (12.0F - k) / 16.0F, (15.0F - j) / 16.0F, (8.0F + f) / 16.0F, 0.75F, 0.9375F);
/*     */         break;
/*     */       
/*     */       case NORTH:
/*  96 */         setBlockBounds((8.0F - f) / 16.0F, (12.0F - k) / 16.0F, 0.0625F, (8.0F + f) / 16.0F, 0.75F, (1.0F + j) / 16.0F);
/*     */         break;
/*     */       
/*     */       case WEST:
/* 100 */         setBlockBounds(0.0625F, (12.0F - k) / 16.0F, (8.0F - f) / 16.0F, (1.0F + j) / 16.0F, 0.75F, (8.0F + f) / 16.0F);
/*     */         break;
/*     */       
/*     */       case EAST:
/* 104 */         setBlockBounds((15.0F - j) / 16.0F, (12.0F - k) / 16.0F, (8.0F - f) / 16.0F, 0.9375F, 0.75F, (8.0F + f) / 16.0F);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
/* 110 */     EnumFacing enumfacing = EnumFacing.fromAngle(placer.rotationYaw);
/* 111 */     worldIn.setBlockState(pos, state.withProperty((IProperty)FACING, (Comparable)enumfacing), 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
/* 116 */     if (!facing.getAxis().isHorizontal())
/*     */     {
/* 118 */       facing = EnumFacing.NORTH;
/*     */     }
/*     */     
/* 121 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)facing.getOpposite()).withProperty((IProperty)AGE, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/* 126 */     if (!canBlockStay(worldIn, pos, state))
/*     */     {
/* 128 */       dropBlock(worldIn, pos, state);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void dropBlock(World worldIn, BlockPos pos, IBlockState state) {
/* 134 */     worldIn.setBlockState(pos, Blocks.air.getDefaultState(), 3);
/* 135 */     dropBlockAsItem(worldIn, pos, state, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
/* 140 */     int i = ((Integer)state.getValue((IProperty)AGE)).intValue();
/* 141 */     int j = 1;
/*     */     
/* 143 */     if (i >= 2)
/*     */     {
/* 145 */       j = 3;
/*     */     }
/*     */     
/* 148 */     for (int k = 0; k < j; k++)
/*     */     {
/* 150 */       spawnAsEntity(worldIn, pos, new ItemStack(Items.dye, 1, EnumDyeColor.BROWN.getDyeDamage()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos) {
/* 156 */     return Items.dye;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDamageValue(World worldIn, BlockPos pos) {
/* 161 */     return EnumDyeColor.BROWN.getDyeDamage();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
/* 166 */     return (((Integer)state.getValue((IProperty)AGE)).intValue() < 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
/* 171 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
/* 176 */     worldIn.setBlockState(pos, state.withProperty((IProperty)AGE, Integer.valueOf(((Integer)state.getValue((IProperty)AGE)).intValue() + 1)), 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer() {
/* 181 */     return EnumWorldBlockLayer.CUTOUT;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 186 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.getHorizontal(meta)).withProperty((IProperty)AGE, Integer.valueOf((meta & 0xF) >> 2));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 191 */     int i = 0;
/* 192 */     i |= ((EnumFacing)state.getValue((IProperty)FACING)).getHorizontalIndex();
/* 193 */     i |= ((Integer)state.getValue((IProperty)AGE)).intValue() << 2;
/* 194 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 199 */     return new BlockState(this, new IProperty[] { (IProperty)FACING, (IProperty)AGE });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockCocoa.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */