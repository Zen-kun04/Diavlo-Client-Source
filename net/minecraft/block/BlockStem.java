/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyDirection;
/*     */ import net.minecraft.block.properties.PropertyInteger;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockStem
/*     */   extends BlockBush implements IGrowable {
/*  24 */   public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);
/*  25 */   public static final PropertyDirection FACING = PropertyDirection.create("facing", new Predicate<EnumFacing>()
/*     */       {
/*     */         public boolean apply(EnumFacing p_apply_1_)
/*     */         {
/*  29 */           return (p_apply_1_ != EnumFacing.DOWN);
/*     */         }
/*     */       });
/*     */   
/*     */   private final Block crop;
/*     */   
/*     */   protected BlockStem(Block crop) {
/*  36 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)AGE, Integer.valueOf(0)).withProperty((IProperty)FACING, (Comparable)EnumFacing.UP));
/*  37 */     this.crop = crop;
/*  38 */     setTickRandomly(true);
/*  39 */     float f = 0.125F;
/*  40 */     setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
/*  41 */     setCreativeTab((CreativeTabs)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
/*  46 */     state = state.withProperty((IProperty)FACING, (Comparable)EnumFacing.UP);
/*     */     
/*  48 */     for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
/*     */       
/*  50 */       if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() == this.crop) {
/*     */         
/*  52 */         state = state.withProperty((IProperty)FACING, (Comparable)enumfacing);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*  57 */     return state;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canPlaceBlockOn(Block ground) {
/*  62 */     return (ground == Blocks.farmland);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/*  67 */     super.updateTick(worldIn, pos, state, rand);
/*     */     
/*  69 */     if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
/*     */       
/*  71 */       float f = BlockCrops.getGrowthChance(this, worldIn, pos);
/*     */       
/*  73 */       if (rand.nextInt((int)(25.0F / f) + 1) == 0) {
/*     */         
/*  75 */         int i = ((Integer)state.getValue((IProperty)AGE)).intValue();
/*     */         
/*  77 */         if (i < 7) {
/*     */           
/*  79 */           state = state.withProperty((IProperty)AGE, Integer.valueOf(i + 1));
/*  80 */           worldIn.setBlockState(pos, state, 2);
/*     */         }
/*     */         else {
/*     */           
/*  84 */           for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
/*     */             
/*  86 */             if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() == this.crop) {
/*     */               return;
/*     */             }
/*     */           } 
/*     */ 
/*     */           
/*  92 */           pos = pos.offset(EnumFacing.Plane.HORIZONTAL.random(rand));
/*  93 */           Block block = worldIn.getBlockState(pos.down()).getBlock();
/*     */           
/*  95 */           if ((worldIn.getBlockState(pos).getBlock()).blockMaterial == Material.air && (block == Blocks.farmland || block == Blocks.dirt || block == Blocks.grass))
/*     */           {
/*  97 */             worldIn.setBlockState(pos, this.crop.getDefaultState());
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void growStem(World worldIn, BlockPos pos, IBlockState state) {
/* 106 */     int i = ((Integer)state.getValue((IProperty)AGE)).intValue() + MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5);
/* 107 */     worldIn.setBlockState(pos, state.withProperty((IProperty)AGE, Integer.valueOf(Math.min(7, i))), 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRenderColor(IBlockState state) {
/* 112 */     if (state.getBlock() != this)
/*     */     {
/* 114 */       return super.getRenderColor(state);
/*     */     }
/*     */ 
/*     */     
/* 118 */     int i = ((Integer)state.getValue((IProperty)AGE)).intValue();
/* 119 */     int j = i * 32;
/* 120 */     int k = 255 - i * 8;
/* 121 */     int l = i * 4;
/* 122 */     return j << 16 | k << 8 | l;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
/* 128 */     return getRenderColor(worldIn.getBlockState(pos));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockBoundsForItemRender() {
/* 133 */     float f = 0.125F;
/* 134 */     setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
/* 139 */     this.maxY = ((((Integer)worldIn.getBlockState(pos).getValue((IProperty)AGE)).intValue() * 2 + 2) / 16.0F);
/* 140 */     float f = 0.125F;
/* 141 */     setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, (float)this.maxY, 0.5F + f);
/*     */   }
/*     */ 
/*     */   
/*     */   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
/* 146 */     super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
/*     */     
/* 148 */     if (!worldIn.isRemote) {
/*     */       
/* 150 */       Item item = getSeedItem();
/*     */       
/* 152 */       if (item != null) {
/*     */         
/* 154 */         int i = ((Integer)state.getValue((IProperty)AGE)).intValue();
/*     */         
/* 156 */         for (int j = 0; j < 3; j++) {
/*     */           
/* 158 */           if (worldIn.rand.nextInt(15) <= i)
/*     */           {
/* 160 */             spawnAsEntity(worldIn, pos, new ItemStack(item));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Item getSeedItem() {
/* 169 */     return (this.crop == Blocks.pumpkin) ? Items.pumpkin_seeds : ((this.crop == Blocks.melon_block) ? Items.melon_seeds : null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/* 174 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos) {
/* 179 */     Item item = getSeedItem();
/* 180 */     return (item != null) ? item : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
/* 185 */     return (((Integer)state.getValue((IProperty)AGE)).intValue() != 7);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
/* 190 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
/* 195 */     growStem(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 200 */     return getDefaultState().withProperty((IProperty)AGE, Integer.valueOf(meta));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 205 */     return ((Integer)state.getValue((IProperty)AGE)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 210 */     return new BlockState(this, new IProperty[] { (IProperty)AGE, (IProperty)FACING });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockStem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */