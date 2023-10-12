/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.world.ColorizerGrass;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeColorHelper;
/*     */ 
/*     */ public class BlockGrass
/*     */   extends Block implements IGrowable {
/*  21 */   public static final PropertyBool SNOWY = PropertyBool.create("snowy");
/*     */ 
/*     */   
/*     */   protected BlockGrass() {
/*  25 */     super(Material.grass);
/*  26 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)SNOWY, Boolean.valueOf(false)));
/*  27 */     setTickRandomly(true);
/*  28 */     setCreativeTab(CreativeTabs.tabBlock);
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
/*  33 */     Block block = worldIn.getBlockState(pos.up()).getBlock();
/*  34 */     return state.withProperty((IProperty)SNOWY, Boolean.valueOf((block == Blocks.snow || block == Blocks.snow_layer)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBlockColor() {
/*  39 */     return ColorizerGrass.getGrassColor(0.5D, 1.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRenderColor(IBlockState state) {
/*  44 */     return getBlockColor();
/*     */   }
/*     */ 
/*     */   
/*     */   public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
/*  49 */     return BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/*  54 */     if (!worldIn.isRemote)
/*     */     {
/*  56 */       if (worldIn.getLightFromNeighbors(pos.up()) < 4 && worldIn.getBlockState(pos.up()).getBlock().getLightOpacity() > 2) {
/*     */         
/*  58 */         worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
/*     */ 
/*     */       
/*     */       }
/*  62 */       else if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
/*     */         
/*  64 */         for (int i = 0; i < 4; i++) {
/*     */           
/*  66 */           BlockPos blockpos = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
/*  67 */           Block block = worldIn.getBlockState(blockpos.up()).getBlock();
/*  68 */           IBlockState iblockstate = worldIn.getBlockState(blockpos);
/*     */           
/*  70 */           if (iblockstate.getBlock() == Blocks.dirt && iblockstate.getValue((IProperty)BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT && worldIn.getLightFromNeighbors(blockpos.up()) >= 4 && block.getLightOpacity() <= 2)
/*     */           {
/*  72 */             worldIn.setBlockState(blockpos, Blocks.grass.getDefaultState());
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/*  82 */     return Blocks.dirt.getItemDropped(Blocks.dirt.getDefaultState().withProperty((IProperty)BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), rand, fortune);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
/*  87 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
/*  92 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
/*  97 */     BlockPos blockpos = pos.up();
/*     */     
/*  99 */     for (int i = 0; i < 128; i++) {
/*     */       
/* 101 */       BlockPos blockpos1 = blockpos;
/* 102 */       int j = 0;
/*     */ 
/*     */       
/*     */       while (true) {
/* 106 */         if (j >= i / 16) {
/*     */           
/* 108 */           if ((worldIn.getBlockState(blockpos1).getBlock()).blockMaterial == Material.air) {
/*     */             
/* 110 */             if (rand.nextInt(8) == 0) {
/*     */               
/* 112 */               BlockFlower.EnumFlowerType blockflower$enumflowertype = worldIn.getBiomeGenForCoords(blockpos1).pickRandomFlower(rand, blockpos1);
/* 113 */               BlockFlower blockflower = blockflower$enumflowertype.getBlockType().getBlock();
/* 114 */               IBlockState iblockstate = blockflower.getDefaultState().withProperty(blockflower.getTypeProperty(), blockflower$enumflowertype);
/*     */               
/* 116 */               if (blockflower.canBlockStay(worldIn, blockpos1, iblockstate))
/*     */               {
/* 118 */                 worldIn.setBlockState(blockpos1, iblockstate, 3);
/*     */               }
/*     */               
/*     */               break;
/*     */             } 
/* 123 */             IBlockState iblockstate1 = Blocks.tallgrass.getDefaultState().withProperty((IProperty)BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS);
/*     */             
/* 125 */             if (Blocks.tallgrass.canBlockStay(worldIn, blockpos1, iblockstate1))
/*     */             {
/* 127 */               worldIn.setBlockState(blockpos1, iblockstate1, 3);
/*     */             }
/*     */           } 
/*     */ 
/*     */           
/*     */           break;
/*     */         } 
/*     */         
/* 135 */         blockpos1 = blockpos1.add(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);
/*     */         
/* 137 */         if (worldIn.getBlockState(blockpos1.down()).getBlock() != Blocks.grass || worldIn.getBlockState(blockpos1).getBlock().isNormalCube()) {
/*     */           break;
/*     */         }
/*     */ 
/*     */         
/* 142 */         j++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer() {
/* 149 */     return EnumWorldBlockLayer.CUTOUT_MIPPED;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 154 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 159 */     return new BlockState(this, new IProperty[] { (IProperty)SNOWY });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockGrass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */