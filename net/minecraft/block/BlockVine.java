/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.world.ColorizerFoliage;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockVine
/*     */   extends Block {
/*  28 */   public static final PropertyBool UP = PropertyBool.create("up");
/*  29 */   public static final PropertyBool NORTH = PropertyBool.create("north");
/*  30 */   public static final PropertyBool EAST = PropertyBool.create("east");
/*  31 */   public static final PropertyBool SOUTH = PropertyBool.create("south");
/*  32 */   public static final PropertyBool WEST = PropertyBool.create("west");
/*  33 */   public static final PropertyBool[] ALL_FACES = new PropertyBool[] { UP, NORTH, SOUTH, WEST, EAST };
/*     */ 
/*     */   
/*     */   public BlockVine() {
/*  37 */     super(Material.vine);
/*  38 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)UP, Boolean.valueOf(false)).withProperty((IProperty)NORTH, Boolean.valueOf(false)).withProperty((IProperty)EAST, Boolean.valueOf(false)).withProperty((IProperty)SOUTH, Boolean.valueOf(false)).withProperty((IProperty)WEST, Boolean.valueOf(false)));
/*  39 */     setTickRandomly(true);
/*  40 */     setCreativeTab(CreativeTabs.tabDecorations);
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
/*  45 */     return state.withProperty((IProperty)UP, Boolean.valueOf(worldIn.getBlockState(pos.up()).getBlock().isBlockNormalCube()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockBoundsForItemRender() {
/*  50 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/*  55 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/*  60 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReplaceable(World worldIn, BlockPos pos) {
/*  65 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
/*  70 */     float f = 0.0625F;
/*  71 */     float f1 = 1.0F;
/*  72 */     float f2 = 1.0F;
/*  73 */     float f3 = 1.0F;
/*  74 */     float f4 = 0.0F;
/*  75 */     float f5 = 0.0F;
/*  76 */     float f6 = 0.0F;
/*  77 */     boolean flag = false;
/*     */     
/*  79 */     if (((Boolean)worldIn.getBlockState(pos).getValue((IProperty)WEST)).booleanValue()) {
/*     */       
/*  81 */       f4 = Math.max(f4, 0.0625F);
/*  82 */       f1 = 0.0F;
/*  83 */       f2 = 0.0F;
/*  84 */       f5 = 1.0F;
/*  85 */       f3 = 0.0F;
/*  86 */       f6 = 1.0F;
/*  87 */       flag = true;
/*     */     } 
/*     */     
/*  90 */     if (((Boolean)worldIn.getBlockState(pos).getValue((IProperty)EAST)).booleanValue()) {
/*     */       
/*  92 */       f1 = Math.min(f1, 0.9375F);
/*  93 */       f4 = 1.0F;
/*  94 */       f2 = 0.0F;
/*  95 */       f5 = 1.0F;
/*  96 */       f3 = 0.0F;
/*  97 */       f6 = 1.0F;
/*  98 */       flag = true;
/*     */     } 
/*     */     
/* 101 */     if (((Boolean)worldIn.getBlockState(pos).getValue((IProperty)NORTH)).booleanValue()) {
/*     */       
/* 103 */       f6 = Math.max(f6, 0.0625F);
/* 104 */       f3 = 0.0F;
/* 105 */       f1 = 0.0F;
/* 106 */       f4 = 1.0F;
/* 107 */       f2 = 0.0F;
/* 108 */       f5 = 1.0F;
/* 109 */       flag = true;
/*     */     } 
/*     */     
/* 112 */     if (((Boolean)worldIn.getBlockState(pos).getValue((IProperty)SOUTH)).booleanValue()) {
/*     */       
/* 114 */       f3 = Math.min(f3, 0.9375F);
/* 115 */       f6 = 1.0F;
/* 116 */       f1 = 0.0F;
/* 117 */       f4 = 1.0F;
/* 118 */       f2 = 0.0F;
/* 119 */       f5 = 1.0F;
/* 120 */       flag = true;
/*     */     } 
/*     */     
/* 123 */     if (!flag && canPlaceOn(worldIn.getBlockState(pos.up()).getBlock())) {
/*     */       
/* 125 */       f2 = Math.min(f2, 0.9375F);
/* 126 */       f5 = 1.0F;
/* 127 */       f1 = 0.0F;
/* 128 */       f4 = 1.0F;
/* 129 */       f3 = 0.0F;
/* 130 */       f6 = 1.0F;
/*     */     } 
/*     */     
/* 133 */     setBlockBounds(f1, f2, f3, f4, f5, f6);
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
/* 138 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
/* 143 */     switch (side) {
/*     */       
/*     */       case UP:
/* 146 */         return canPlaceOn(worldIn.getBlockState(pos.up()).getBlock());
/*     */       
/*     */       case NORTH:
/*     */       case SOUTH:
/*     */       case EAST:
/*     */       case WEST:
/* 152 */         return canPlaceOn(worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock());
/*     */     } 
/*     */     
/* 155 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean canPlaceOn(Block blockIn) {
/* 161 */     return (blockIn.isFullCube() && blockIn.blockMaterial.blocksMovement());
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean recheckGrownSides(World worldIn, BlockPos pos, IBlockState state) {
/* 166 */     IBlockState iblockstate = state;
/*     */     
/* 168 */     for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
/*     */       
/* 170 */       PropertyBool propertybool = getPropertyFor(enumfacing);
/*     */       
/* 172 */       if (((Boolean)state.getValue((IProperty)propertybool)).booleanValue() && !canPlaceOn(worldIn.getBlockState(pos.offset(enumfacing)).getBlock())) {
/*     */         
/* 174 */         IBlockState iblockstate1 = worldIn.getBlockState(pos.up());
/*     */         
/* 176 */         if (iblockstate1.getBlock() != this || !((Boolean)iblockstate1.getValue((IProperty)propertybool)).booleanValue())
/*     */         {
/* 178 */           state = state.withProperty((IProperty)propertybool, Boolean.valueOf(false));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 183 */     if (getNumGrownFaces(state) == 0)
/*     */     {
/* 185 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 189 */     if (iblockstate != state)
/*     */     {
/* 191 */       worldIn.setBlockState(pos, state, 2);
/*     */     }
/*     */     
/* 194 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBlockColor() {
/* 200 */     return ColorizerFoliage.getFoliageColorBasic();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRenderColor(IBlockState state) {
/* 205 */     return ColorizerFoliage.getFoliageColorBasic();
/*     */   }
/*     */ 
/*     */   
/*     */   public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
/* 210 */     return worldIn.getBiomeGenForCoords(pos).getFoliageColorAtPos(pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/* 215 */     if (!worldIn.isRemote && !recheckGrownSides(worldIn, pos, state)) {
/*     */       
/* 217 */       dropBlockAsItem(worldIn, pos, state, 0);
/* 218 */       worldIn.setBlockToAir(pos);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/* 224 */     if (!worldIn.isRemote)
/*     */     {
/* 226 */       if (worldIn.rand.nextInt(4) == 0) {
/*     */         
/* 228 */         int i = 4;
/* 229 */         int j = 5;
/* 230 */         boolean flag = false;
/*     */         
/*     */         int k;
/* 233 */         label103: for (k = -i; k <= i; k++) {
/*     */           
/* 235 */           for (int l = -i; l <= i; l++) {
/*     */             
/* 237 */             for (int i1 = -1; i1 <= 1; i1++) {
/*     */               
/* 239 */               if (worldIn.getBlockState(pos.add(k, i1, l)).getBlock() == this) {
/*     */                 
/* 241 */                 j--;
/*     */                 
/* 243 */                 if (j <= 0) {
/*     */                   
/* 245 */                   flag = true;
/*     */                   
/*     */                   break label103;
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/* 253 */         EnumFacing enumfacing1 = EnumFacing.random(rand);
/* 254 */         BlockPos blockpos1 = pos.up();
/*     */         
/* 256 */         if (enumfacing1 == EnumFacing.UP && pos.getY() < 255 && worldIn.isAirBlock(blockpos1)) {
/*     */           
/* 258 */           if (!flag)
/*     */           {
/* 260 */             IBlockState iblockstate2 = state;
/*     */             
/* 262 */             for (EnumFacing enumfacing3 : EnumFacing.Plane.HORIZONTAL) {
/*     */               
/* 264 */               if (rand.nextBoolean() || !canPlaceOn(worldIn.getBlockState(blockpos1.offset(enumfacing3)).getBlock()))
/*     */               {
/* 266 */                 iblockstate2 = iblockstate2.withProperty((IProperty)getPropertyFor(enumfacing3), Boolean.valueOf(false));
/*     */               }
/*     */             } 
/*     */             
/* 270 */             if (((Boolean)iblockstate2.getValue((IProperty)NORTH)).booleanValue() || ((Boolean)iblockstate2.getValue((IProperty)EAST)).booleanValue() || ((Boolean)iblockstate2.getValue((IProperty)SOUTH)).booleanValue() || ((Boolean)iblockstate2.getValue((IProperty)WEST)).booleanValue())
/*     */             {
/* 272 */               worldIn.setBlockState(blockpos1, iblockstate2, 2);
/*     */             }
/*     */           }
/*     */         
/* 276 */         } else if (enumfacing1.getAxis().isHorizontal() && !((Boolean)state.getValue((IProperty)getPropertyFor(enumfacing1))).booleanValue()) {
/*     */           
/* 278 */           if (!flag)
/*     */           {
/* 280 */             BlockPos blockpos3 = pos.offset(enumfacing1);
/* 281 */             Block block1 = worldIn.getBlockState(blockpos3).getBlock();
/*     */             
/* 283 */             if (block1.blockMaterial == Material.air) {
/*     */               
/* 285 */               EnumFacing enumfacing2 = enumfacing1.rotateY();
/* 286 */               EnumFacing enumfacing4 = enumfacing1.rotateYCCW();
/* 287 */               boolean flag1 = ((Boolean)state.getValue((IProperty)getPropertyFor(enumfacing2))).booleanValue();
/* 288 */               boolean flag2 = ((Boolean)state.getValue((IProperty)getPropertyFor(enumfacing4))).booleanValue();
/* 289 */               BlockPos blockpos4 = blockpos3.offset(enumfacing2);
/* 290 */               BlockPos blockpos = blockpos3.offset(enumfacing4);
/*     */               
/* 292 */               if (flag1 && canPlaceOn(worldIn.getBlockState(blockpos4).getBlock()))
/*     */               {
/* 294 */                 worldIn.setBlockState(blockpos3, getDefaultState().withProperty((IProperty)getPropertyFor(enumfacing2), Boolean.valueOf(true)), 2);
/*     */               }
/* 296 */               else if (flag2 && canPlaceOn(worldIn.getBlockState(blockpos).getBlock()))
/*     */               {
/* 298 */                 worldIn.setBlockState(blockpos3, getDefaultState().withProperty((IProperty)getPropertyFor(enumfacing4), Boolean.valueOf(true)), 2);
/*     */               }
/* 300 */               else if (flag1 && worldIn.isAirBlock(blockpos4) && canPlaceOn(worldIn.getBlockState(pos.offset(enumfacing2)).getBlock()))
/*     */               {
/* 302 */                 worldIn.setBlockState(blockpos4, getDefaultState().withProperty((IProperty)getPropertyFor(enumfacing1.getOpposite()), Boolean.valueOf(true)), 2);
/*     */               }
/* 304 */               else if (flag2 && worldIn.isAirBlock(blockpos) && canPlaceOn(worldIn.getBlockState(pos.offset(enumfacing4)).getBlock()))
/*     */               {
/* 306 */                 worldIn.setBlockState(blockpos, getDefaultState().withProperty((IProperty)getPropertyFor(enumfacing1.getOpposite()), Boolean.valueOf(true)), 2);
/*     */               }
/* 308 */               else if (canPlaceOn(worldIn.getBlockState(blockpos3.up()).getBlock()))
/*     */               {
/* 310 */                 worldIn.setBlockState(blockpos3, getDefaultState(), 2);
/*     */               }
/*     */             
/* 313 */             } else if (block1.blockMaterial.isOpaque() && block1.isFullCube()) {
/*     */               
/* 315 */               worldIn.setBlockState(pos, state.withProperty((IProperty)getPropertyFor(enumfacing1), Boolean.valueOf(true)), 2);
/*     */             }
/*     */           
/*     */           }
/*     */         
/*     */         }
/* 321 */         else if (pos.getY() > 1) {
/*     */           
/* 323 */           BlockPos blockpos2 = pos.down();
/* 324 */           IBlockState iblockstate = worldIn.getBlockState(blockpos2);
/* 325 */           Block block = iblockstate.getBlock();
/*     */           
/* 327 */           if (block.blockMaterial == Material.air) {
/*     */             
/* 329 */             IBlockState iblockstate1 = state;
/*     */             
/* 331 */             for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
/*     */               
/* 333 */               if (rand.nextBoolean())
/*     */               {
/* 335 */                 iblockstate1 = iblockstate1.withProperty((IProperty)getPropertyFor(enumfacing), Boolean.valueOf(false));
/*     */               }
/*     */             } 
/*     */             
/* 339 */             if (((Boolean)iblockstate1.getValue((IProperty)NORTH)).booleanValue() || ((Boolean)iblockstate1.getValue((IProperty)EAST)).booleanValue() || ((Boolean)iblockstate1.getValue((IProperty)SOUTH)).booleanValue() || ((Boolean)iblockstate1.getValue((IProperty)WEST)).booleanValue())
/*     */             {
/* 341 */               worldIn.setBlockState(blockpos2, iblockstate1, 2);
/*     */             }
/*     */           }
/* 344 */           else if (block == this) {
/*     */             
/* 346 */             IBlockState iblockstate3 = iblockstate;
/*     */             
/* 348 */             for (EnumFacing enumfacing5 : EnumFacing.Plane.HORIZONTAL) {
/*     */               
/* 350 */               PropertyBool propertybool = getPropertyFor(enumfacing5);
/*     */               
/* 352 */               if (rand.nextBoolean() && ((Boolean)state.getValue((IProperty)propertybool)).booleanValue())
/*     */               {
/* 354 */                 iblockstate3 = iblockstate3.withProperty((IProperty)propertybool, Boolean.valueOf(true));
/*     */               }
/*     */             } 
/*     */             
/* 358 */             if (((Boolean)iblockstate3.getValue((IProperty)NORTH)).booleanValue() || ((Boolean)iblockstate3.getValue((IProperty)EAST)).booleanValue() || ((Boolean)iblockstate3.getValue((IProperty)SOUTH)).booleanValue() || ((Boolean)iblockstate3.getValue((IProperty)WEST)).booleanValue())
/*     */             {
/* 360 */               worldIn.setBlockState(blockpos2, iblockstate3, 2);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
/* 371 */     IBlockState iblockstate = getDefaultState().withProperty((IProperty)UP, Boolean.valueOf(false)).withProperty((IProperty)NORTH, Boolean.valueOf(false)).withProperty((IProperty)EAST, Boolean.valueOf(false)).withProperty((IProperty)SOUTH, Boolean.valueOf(false)).withProperty((IProperty)WEST, Boolean.valueOf(false));
/* 372 */     return facing.getAxis().isHorizontal() ? iblockstate.withProperty((IProperty)getPropertyFor(facing.getOpposite()), Boolean.valueOf(true)) : iblockstate;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/* 377 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int quantityDropped(Random random) {
/* 382 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
/* 387 */     if (!worldIn.isRemote && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.shears) {
/*     */       
/* 389 */       player.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
/* 390 */       spawnAsEntity(worldIn, pos, new ItemStack(Blocks.vine, 1, 0));
/*     */     }
/*     */     else {
/*     */       
/* 394 */       super.harvestBlock(worldIn, player, pos, state, te);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer() {
/* 400 */     return EnumWorldBlockLayer.CUTOUT;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 405 */     return getDefaultState().withProperty((IProperty)SOUTH, Boolean.valueOf(((meta & 0x1) > 0))).withProperty((IProperty)WEST, Boolean.valueOf(((meta & 0x2) > 0))).withProperty((IProperty)NORTH, Boolean.valueOf(((meta & 0x4) > 0))).withProperty((IProperty)EAST, Boolean.valueOf(((meta & 0x8) > 0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 410 */     int i = 0;
/*     */     
/* 412 */     if (((Boolean)state.getValue((IProperty)SOUTH)).booleanValue())
/*     */     {
/* 414 */       i |= 0x1;
/*     */     }
/*     */     
/* 417 */     if (((Boolean)state.getValue((IProperty)WEST)).booleanValue())
/*     */     {
/* 419 */       i |= 0x2;
/*     */     }
/*     */     
/* 422 */     if (((Boolean)state.getValue((IProperty)NORTH)).booleanValue())
/*     */     {
/* 424 */       i |= 0x4;
/*     */     }
/*     */     
/* 427 */     if (((Boolean)state.getValue((IProperty)EAST)).booleanValue())
/*     */     {
/* 429 */       i |= 0x8;
/*     */     }
/*     */     
/* 432 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 437 */     return new BlockState(this, new IProperty[] { (IProperty)UP, (IProperty)NORTH, (IProperty)EAST, (IProperty)SOUTH, (IProperty)WEST });
/*     */   }
/*     */ 
/*     */   
/*     */   public static PropertyBool getPropertyFor(EnumFacing side) {
/* 442 */     switch (side) {
/*     */       
/*     */       case UP:
/* 445 */         return UP;
/*     */       
/*     */       case NORTH:
/* 448 */         return NORTH;
/*     */       
/*     */       case SOUTH:
/* 451 */         return SOUTH;
/*     */       
/*     */       case EAST:
/* 454 */         return EAST;
/*     */       
/*     */       case WEST:
/* 457 */         return WEST;
/*     */     } 
/*     */     
/* 460 */     throw new IllegalArgumentException(side + " is an invalid choice");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getNumGrownFaces(IBlockState state) {
/* 466 */     int i = 0;
/*     */     
/* 468 */     for (PropertyBool propertybool : ALL_FACES) {
/*     */       
/* 470 */       if (((Boolean)state.getValue((IProperty)propertybool)).booleanValue())
/*     */       {
/* 472 */         i++;
/*     */       }
/*     */     } 
/*     */     
/* 476 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockVine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */