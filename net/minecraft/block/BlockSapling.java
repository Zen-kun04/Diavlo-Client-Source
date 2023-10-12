/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.properties.PropertyInteger;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.StatCollector;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.gen.feature.WorldGenBigTree;
/*     */ import net.minecraft.world.gen.feature.WorldGenCanopyTree;
/*     */ import net.minecraft.world.gen.feature.WorldGenForest;
/*     */ import net.minecraft.world.gen.feature.WorldGenMegaJungle;
/*     */ import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
/*     */ import net.minecraft.world.gen.feature.WorldGenSavannaTree;
/*     */ import net.minecraft.world.gen.feature.WorldGenTaiga2;
/*     */ import net.minecraft.world.gen.feature.WorldGenTrees;
/*     */ import net.minecraft.world.gen.feature.WorldGenerator;
/*     */ 
/*     */ public class BlockSapling
/*     */   extends BlockBush implements IGrowable {
/*  29 */   public static final PropertyEnum<BlockPlanks.EnumType> TYPE = PropertyEnum.create("type", BlockPlanks.EnumType.class);
/*  30 */   public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);
/*     */ 
/*     */   
/*     */   protected BlockSapling() {
/*  34 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)TYPE, BlockPlanks.EnumType.OAK).withProperty((IProperty)STAGE, Integer.valueOf(0)));
/*  35 */     float f = 0.4F;
/*  36 */     setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
/*  37 */     setCreativeTab(CreativeTabs.tabDecorations);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLocalizedName() {
/*  42 */     return StatCollector.translateToLocal(getUnlocalizedName() + "." + BlockPlanks.EnumType.OAK.getUnlocalizedName() + ".name");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/*  47 */     if (!worldIn.isRemote) {
/*     */       
/*  49 */       super.updateTick(worldIn, pos, state, rand);
/*     */       
/*  51 */       if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0)
/*     */       {
/*  53 */         grow(worldIn, pos, state, rand);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void grow(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/*  60 */     if (((Integer)state.getValue((IProperty)STAGE)).intValue() == 0) {
/*     */       
/*  62 */       worldIn.setBlockState(pos, state.cycleProperty((IProperty)STAGE), 4);
/*     */     }
/*     */     else {
/*     */       
/*  66 */       generateTree(worldIn, pos, state, rand);
/*     */     }  } public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/*     */     WorldGenForest worldGenForest;
/*     */     WorldGenSavannaTree worldGenSavannaTree;
/*     */     WorldGenCanopyTree worldGenCanopyTree;
/*     */     IBlockState iblockstate, iblockstate1;
/*  72 */     WorldGenerator worldgenerator = (rand.nextInt(10) == 0) ? (WorldGenerator)new WorldGenBigTree(true) : (WorldGenerator)new WorldGenTrees(true);
/*  73 */     int i = 0;
/*  74 */     int j = 0;
/*  75 */     boolean flag = false;
/*     */     
/*  77 */     switch ((BlockPlanks.EnumType)state.getValue((IProperty)TYPE)) {
/*     */ 
/*     */       
/*     */       case SPRUCE:
/*  81 */         label68: for (i = 0; i >= -1; i--) {
/*     */           
/*  83 */           for (j = 0; j >= -1; j--) {
/*     */             
/*  85 */             if (func_181624_a(worldIn, pos, i, j, BlockPlanks.EnumType.SPRUCE)) {
/*     */               
/*  87 */               WorldGenMegaPineTree worldGenMegaPineTree = new WorldGenMegaPineTree(false, rand.nextBoolean());
/*  88 */               flag = true;
/*     */               
/*     */               break label68;
/*     */             } 
/*     */           } 
/*     */         } 
/*  94 */         if (!flag) {
/*     */           
/*  96 */           j = 0;
/*  97 */           i = 0;
/*  98 */           WorldGenTaiga2 worldGenTaiga2 = new WorldGenTaiga2(true);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case BIRCH:
/* 104 */         worldGenForest = new WorldGenForest(true, false);
/*     */         break;
/*     */       
/*     */       case JUNGLE:
/* 108 */         iblockstate = Blocks.log.getDefaultState().withProperty((IProperty)BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
/* 109 */         iblockstate1 = Blocks.leaves.getDefaultState().withProperty((IProperty)BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty((IProperty)BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
/*     */ 
/*     */         
/* 112 */         label69: for (i = 0; i >= -1; i--) {
/*     */           
/* 114 */           for (j = 0; j >= -1; j--) {
/*     */             
/* 116 */             if (func_181624_a(worldIn, pos, i, j, BlockPlanks.EnumType.JUNGLE)) {
/*     */               
/* 118 */               WorldGenMegaJungle worldGenMegaJungle = new WorldGenMegaJungle(true, 10, 20, iblockstate, iblockstate1);
/* 119 */               flag = true;
/*     */               
/*     */               break label69;
/*     */             } 
/*     */           } 
/*     */         } 
/* 125 */         if (!flag) {
/*     */           
/* 127 */           j = 0;
/* 128 */           i = 0;
/* 129 */           WorldGenTrees worldGenTrees = new WorldGenTrees(true, 4 + rand.nextInt(7), iblockstate, iblockstate1, false);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case ACACIA:
/* 135 */         worldGenSavannaTree = new WorldGenSavannaTree(true);
/*     */         break;
/*     */ 
/*     */       
/*     */       case DARK_OAK:
/* 140 */         label70: for (i = 0; i >= -1; i--) {
/*     */           
/* 142 */           for (j = 0; j >= -1; j--) {
/*     */             
/* 144 */             if (func_181624_a(worldIn, pos, i, j, BlockPlanks.EnumType.DARK_OAK)) {
/*     */               
/* 146 */               worldGenCanopyTree = new WorldGenCanopyTree(true);
/* 147 */               flag = true;
/*     */               
/*     */               break label70;
/*     */             } 
/*     */           } 
/*     */         } 
/* 153 */         if (!flag) {
/*     */           return;
/*     */         }
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 161 */     IBlockState iblockstate2 = Blocks.air.getDefaultState();
/*     */     
/* 163 */     if (flag) {
/*     */       
/* 165 */       worldIn.setBlockState(pos.add(i, 0, j), iblockstate2, 4);
/* 166 */       worldIn.setBlockState(pos.add(i + 1, 0, j), iblockstate2, 4);
/* 167 */       worldIn.setBlockState(pos.add(i, 0, j + 1), iblockstate2, 4);
/* 168 */       worldIn.setBlockState(pos.add(i + 1, 0, j + 1), iblockstate2, 4);
/*     */     }
/*     */     else {
/*     */       
/* 172 */       worldIn.setBlockState(pos, iblockstate2, 4);
/*     */     } 
/*     */     
/* 175 */     if (!worldGenCanopyTree.generate(worldIn, rand, pos.add(i, 0, j)))
/*     */     {
/* 177 */       if (flag) {
/*     */         
/* 179 */         worldIn.setBlockState(pos.add(i, 0, j), state, 4);
/* 180 */         worldIn.setBlockState(pos.add(i + 1, 0, j), state, 4);
/* 181 */         worldIn.setBlockState(pos.add(i, 0, j + 1), state, 4);
/* 182 */         worldIn.setBlockState(pos.add(i + 1, 0, j + 1), state, 4);
/*     */       }
/*     */       else {
/*     */         
/* 186 */         worldIn.setBlockState(pos, state, 4);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean func_181624_a(World p_181624_1_, BlockPos p_181624_2_, int p_181624_3_, int p_181624_4_, BlockPlanks.EnumType p_181624_5_) {
/* 193 */     return (isTypeAt(p_181624_1_, p_181624_2_.add(p_181624_3_, 0, p_181624_4_), p_181624_5_) && isTypeAt(p_181624_1_, p_181624_2_.add(p_181624_3_ + 1, 0, p_181624_4_), p_181624_5_) && isTypeAt(p_181624_1_, p_181624_2_.add(p_181624_3_, 0, p_181624_4_ + 1), p_181624_5_) && isTypeAt(p_181624_1_, p_181624_2_.add(p_181624_3_ + 1, 0, p_181624_4_ + 1), p_181624_5_));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTypeAt(World worldIn, BlockPos pos, BlockPlanks.EnumType type) {
/* 198 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/* 199 */     return (iblockstate.getBlock() == this && iblockstate.getValue((IProperty)TYPE) == type);
/*     */   }
/*     */ 
/*     */   
/*     */   public int damageDropped(IBlockState state) {
/* 204 */     return ((BlockPlanks.EnumType)state.getValue((IProperty)TYPE)).getMetadata();
/*     */   }
/*     */ 
/*     */   
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
/* 209 */     for (BlockPlanks.EnumType blockplanks$enumtype : BlockPlanks.EnumType.values())
/*     */     {
/* 211 */       list.add(new ItemStack(itemIn, 1, blockplanks$enumtype.getMetadata()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
/* 217 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
/* 222 */     return (worldIn.rand.nextFloat() < 0.45D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
/* 227 */     grow(worldIn, pos, state, rand);
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 232 */     return getDefaultState().withProperty((IProperty)TYPE, BlockPlanks.EnumType.byMetadata(meta & 0x7)).withProperty((IProperty)STAGE, Integer.valueOf((meta & 0x8) >> 3));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 237 */     int i = 0;
/* 238 */     i |= ((BlockPlanks.EnumType)state.getValue((IProperty)TYPE)).getMetadata();
/* 239 */     i |= ((Integer)state.getValue((IProperty)STAGE)).intValue() << 3;
/* 240 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 245 */     return new BlockState(this, new IProperty[] { (IProperty)TYPE, (IProperty)STAGE });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockSapling.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */