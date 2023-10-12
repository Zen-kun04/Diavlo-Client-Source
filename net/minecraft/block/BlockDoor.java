/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.properties.PropertyDirection;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.StatCollector;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockDoor extends Block {
/*  28 */   public static final PropertyDirection FACING = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
/*  29 */   public static final PropertyBool OPEN = PropertyBool.create("open");
/*  30 */   public static final PropertyEnum<EnumHingePosition> HINGE = PropertyEnum.create("hinge", EnumHingePosition.class);
/*  31 */   public static final PropertyBool POWERED = PropertyBool.create("powered");
/*  32 */   public static final PropertyEnum<EnumDoorHalf> HALF = PropertyEnum.create("half", EnumDoorHalf.class);
/*     */ 
/*     */   
/*     */   protected BlockDoor(Material materialIn) {
/*  36 */     super(materialIn);
/*  37 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)OPEN, Boolean.valueOf(false)).withProperty((IProperty)HINGE, EnumHingePosition.LEFT).withProperty((IProperty)POWERED, Boolean.valueOf(false)).withProperty((IProperty)HALF, EnumDoorHalf.LOWER));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLocalizedName() {
/*  42 */     return StatCollector.translateToLocal((getUnlocalizedName() + ".name").replaceAll("tile", "item"));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/*  47 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
/*  52 */     return isOpen(combineMetadata(worldIn, pos));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/*  57 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
/*  62 */     setBlockBoundsBasedOnState((IBlockAccess)worldIn, pos);
/*  63 */     return super.getSelectedBoundingBox(worldIn, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
/*  68 */     setBlockBoundsBasedOnState((IBlockAccess)worldIn, pos);
/*  69 */     return super.getCollisionBoundingBox(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
/*  74 */     setBoundBasedOnMeta(combineMetadata(worldIn, pos));
/*     */   }
/*     */ 
/*     */   
/*     */   private void setBoundBasedOnMeta(int combinedMeta) {
/*  79 */     float f = 0.1875F;
/*  80 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
/*  81 */     EnumFacing enumfacing = getFacing(combinedMeta);
/*  82 */     boolean flag = isOpen(combinedMeta);
/*  83 */     boolean flag1 = isHingeLeft(combinedMeta);
/*     */     
/*  85 */     if (flag) {
/*     */       
/*  87 */       if (enumfacing == EnumFacing.EAST) {
/*     */         
/*  89 */         if (!flag1)
/*     */         {
/*  91 */           setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
/*     */         }
/*     */         else
/*     */         {
/*  95 */           setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
/*     */         }
/*     */       
/*  98 */       } else if (enumfacing == EnumFacing.SOUTH) {
/*     */         
/* 100 */         if (!flag1)
/*     */         {
/* 102 */           setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */         }
/*     */         else
/*     */         {
/* 106 */           setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
/*     */         }
/*     */       
/* 109 */       } else if (enumfacing == EnumFacing.WEST) {
/*     */         
/* 111 */         if (!flag1)
/*     */         {
/* 113 */           setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
/*     */         }
/*     */         else
/*     */         {
/* 117 */           setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
/*     */         }
/*     */       
/* 120 */       } else if (enumfacing == EnumFacing.NORTH) {
/*     */         
/* 122 */         if (!flag1)
/*     */         {
/* 124 */           setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
/*     */         }
/*     */         else
/*     */         {
/* 128 */           setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */         }
/*     */       
/*     */       } 
/* 132 */     } else if (enumfacing == EnumFacing.EAST) {
/*     */       
/* 134 */       setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
/*     */     }
/* 136 */     else if (enumfacing == EnumFacing.SOUTH) {
/*     */       
/* 138 */       setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
/*     */     }
/* 140 */     else if (enumfacing == EnumFacing.WEST) {
/*     */       
/* 142 */       setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */     }
/* 144 */     else if (enumfacing == EnumFacing.NORTH) {
/*     */       
/* 146 */       setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
/* 152 */     if (this.blockMaterial == Material.iron)
/*     */     {
/* 154 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 158 */     BlockPos blockpos = (state.getValue((IProperty)HALF) == EnumDoorHalf.LOWER) ? pos : pos.down();
/* 159 */     IBlockState iblockstate = pos.equals(blockpos) ? state : worldIn.getBlockState(blockpos);
/*     */     
/* 161 */     if (iblockstate.getBlock() != this)
/*     */     {
/* 163 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 167 */     state = iblockstate.cycleProperty((IProperty)OPEN);
/* 168 */     worldIn.setBlockState(blockpos, state, 2);
/* 169 */     worldIn.markBlockRangeForRenderUpdate(blockpos, pos);
/* 170 */     worldIn.playAuxSFXAtEntity(playerIn, ((Boolean)state.getValue((IProperty)OPEN)).booleanValue() ? 1003 : 1006, pos, 0);
/* 171 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void toggleDoor(World worldIn, BlockPos pos, boolean open) {
/* 178 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */     
/* 180 */     if (iblockstate.getBlock() == this) {
/*     */       
/* 182 */       BlockPos blockpos = (iblockstate.getValue((IProperty)HALF) == EnumDoorHalf.LOWER) ? pos : pos.down();
/* 183 */       IBlockState iblockstate1 = (pos == blockpos) ? iblockstate : worldIn.getBlockState(blockpos);
/*     */       
/* 185 */       if (iblockstate1.getBlock() == this && ((Boolean)iblockstate1.getValue((IProperty)OPEN)).booleanValue() != open) {
/*     */         
/* 187 */         worldIn.setBlockState(blockpos, iblockstate1.withProperty((IProperty)OPEN, Boolean.valueOf(open)), 2);
/* 188 */         worldIn.markBlockRangeForRenderUpdate(blockpos, pos);
/* 189 */         worldIn.playAuxSFXAtEntity((EntityPlayer)null, open ? 1003 : 1006, pos, 0);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/* 196 */     if (state.getValue((IProperty)HALF) == EnumDoorHalf.UPPER) {
/*     */       
/* 198 */       BlockPos blockpos = pos.down();
/* 199 */       IBlockState iblockstate = worldIn.getBlockState(blockpos);
/*     */       
/* 201 */       if (iblockstate.getBlock() != this)
/*     */       {
/* 203 */         worldIn.setBlockToAir(pos);
/*     */       }
/* 205 */       else if (neighborBlock != this)
/*     */       {
/* 207 */         onNeighborBlockChange(worldIn, blockpos, iblockstate, neighborBlock);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 212 */       boolean flag1 = false;
/* 213 */       BlockPos blockpos1 = pos.up();
/* 214 */       IBlockState iblockstate1 = worldIn.getBlockState(blockpos1);
/*     */       
/* 216 */       if (iblockstate1.getBlock() != this) {
/*     */         
/* 218 */         worldIn.setBlockToAir(pos);
/* 219 */         flag1 = true;
/*     */       } 
/*     */       
/* 222 */       if (!World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.down())) {
/*     */         
/* 224 */         worldIn.setBlockToAir(pos);
/* 225 */         flag1 = true;
/*     */         
/* 227 */         if (iblockstate1.getBlock() == this)
/*     */         {
/* 229 */           worldIn.setBlockToAir(blockpos1);
/*     */         }
/*     */       } 
/*     */       
/* 233 */       if (flag1) {
/*     */         
/* 235 */         if (!worldIn.isRemote)
/*     */         {
/* 237 */           dropBlockAsItem(worldIn, pos, state, 0);
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/* 242 */         boolean flag = (worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(blockpos1));
/*     */         
/* 244 */         if ((flag || neighborBlock.canProvidePower()) && neighborBlock != this && flag != ((Boolean)iblockstate1.getValue((IProperty)POWERED)).booleanValue()) {
/*     */           
/* 246 */           worldIn.setBlockState(blockpos1, iblockstate1.withProperty((IProperty)POWERED, Boolean.valueOf(flag)), 2);
/*     */           
/* 248 */           if (flag != ((Boolean)state.getValue((IProperty)OPEN)).booleanValue()) {
/*     */             
/* 250 */             worldIn.setBlockState(pos, state.withProperty((IProperty)OPEN, Boolean.valueOf(flag)), 2);
/* 251 */             worldIn.markBlockRangeForRenderUpdate(pos, pos);
/* 252 */             worldIn.playAuxSFXAtEntity((EntityPlayer)null, flag ? 1003 : 1006, pos, 0);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/* 261 */     return (state.getValue((IProperty)HALF) == EnumDoorHalf.UPPER) ? null : getItem();
/*     */   }
/*     */ 
/*     */   
/*     */   public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end) {
/* 266 */     setBlockBoundsBasedOnState((IBlockAccess)worldIn, pos);
/* 267 */     return super.collisionRayTrace(worldIn, pos, start, end);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
/* 272 */     return (pos.getY() >= 255) ? false : ((World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.down()) && super.canPlaceBlockAt(worldIn, pos) && super.canPlaceBlockAt(worldIn, pos.up())));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMobilityFlag() {
/* 277 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int combineMetadata(IBlockAccess worldIn, BlockPos pos) {
/* 282 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/* 283 */     int i = iblockstate.getBlock().getMetaFromState(iblockstate);
/* 284 */     boolean flag = isTop(i);
/* 285 */     IBlockState iblockstate1 = worldIn.getBlockState(pos.down());
/* 286 */     int j = iblockstate1.getBlock().getMetaFromState(iblockstate1);
/* 287 */     int k = flag ? j : i;
/* 288 */     IBlockState iblockstate2 = worldIn.getBlockState(pos.up());
/* 289 */     int l = iblockstate2.getBlock().getMetaFromState(iblockstate2);
/* 290 */     int i1 = flag ? i : l;
/* 291 */     boolean flag1 = ((i1 & 0x1) != 0);
/* 292 */     boolean flag2 = ((i1 & 0x2) != 0);
/* 293 */     return removeHalfBit(k) | (flag ? 8 : 0) | (flag1 ? 16 : 0) | (flag2 ? 32 : 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos) {
/* 298 */     return getItem();
/*     */   }
/*     */ 
/*     */   
/*     */   private Item getItem() {
/* 303 */     return (this == Blocks.iron_door) ? Items.iron_door : ((this == Blocks.spruce_door) ? Items.spruce_door : ((this == Blocks.birch_door) ? Items.birch_door : ((this == Blocks.jungle_door) ? Items.jungle_door : ((this == Blocks.acacia_door) ? Items.acacia_door : ((this == Blocks.dark_oak_door) ? Items.dark_oak_door : Items.oak_door)))));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
/* 308 */     BlockPos blockpos = pos.down();
/*     */     
/* 310 */     if (player.capabilities.isCreativeMode && state.getValue((IProperty)HALF) == EnumDoorHalf.UPPER && worldIn.getBlockState(blockpos).getBlock() == this)
/*     */     {
/* 312 */       worldIn.setBlockToAir(blockpos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer() {
/* 318 */     return EnumWorldBlockLayer.CUTOUT;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
/* 323 */     if (state.getValue((IProperty)HALF) == EnumDoorHalf.LOWER) {
/*     */       
/* 325 */       IBlockState iblockstate = worldIn.getBlockState(pos.up());
/*     */       
/* 327 */       if (iblockstate.getBlock() == this)
/*     */       {
/* 329 */         state = state.withProperty((IProperty)HINGE, iblockstate.getValue((IProperty)HINGE)).withProperty((IProperty)POWERED, iblockstate.getValue((IProperty)POWERED));
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 334 */       IBlockState iblockstate1 = worldIn.getBlockState(pos.down());
/*     */       
/* 336 */       if (iblockstate1.getBlock() == this)
/*     */       {
/* 338 */         state = state.withProperty((IProperty)FACING, iblockstate1.getValue((IProperty)FACING)).withProperty((IProperty)OPEN, iblockstate1.getValue((IProperty)OPEN));
/*     */       }
/*     */     } 
/*     */     
/* 342 */     return state;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 347 */     return ((meta & 0x8) > 0) ? getDefaultState().withProperty((IProperty)HALF, EnumDoorHalf.UPPER).withProperty((IProperty)HINGE, ((meta & 0x1) > 0) ? EnumHingePosition.RIGHT : EnumHingePosition.LEFT).withProperty((IProperty)POWERED, Boolean.valueOf(((meta & 0x2) > 0))) : getDefaultState().withProperty((IProperty)HALF, EnumDoorHalf.LOWER).withProperty((IProperty)FACING, (Comparable)EnumFacing.getHorizontal(meta & 0x3).rotateYCCW()).withProperty((IProperty)OPEN, Boolean.valueOf(((meta & 0x4) > 0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 352 */     int i = 0;
/*     */     
/* 354 */     if (state.getValue((IProperty)HALF) == EnumDoorHalf.UPPER) {
/*     */       
/* 356 */       i |= 0x8;
/*     */       
/* 358 */       if (state.getValue((IProperty)HINGE) == EnumHingePosition.RIGHT)
/*     */       {
/* 360 */         i |= 0x1;
/*     */       }
/*     */       
/* 363 */       if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
/*     */       {
/* 365 */         i |= 0x2;
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 370 */       i |= ((EnumFacing)state.getValue((IProperty)FACING)).rotateY().getHorizontalIndex();
/*     */       
/* 372 */       if (((Boolean)state.getValue((IProperty)OPEN)).booleanValue())
/*     */       {
/* 374 */         i |= 0x4;
/*     */       }
/*     */     } 
/*     */     
/* 378 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static int removeHalfBit(int meta) {
/* 383 */     return meta & 0x7;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isOpen(IBlockAccess worldIn, BlockPos pos) {
/* 388 */     return isOpen(combineMetadata(worldIn, pos));
/*     */   }
/*     */ 
/*     */   
/*     */   public static EnumFacing getFacing(IBlockAccess worldIn, BlockPos pos) {
/* 393 */     return getFacing(combineMetadata(worldIn, pos));
/*     */   }
/*     */ 
/*     */   
/*     */   public static EnumFacing getFacing(int combinedMeta) {
/* 398 */     return EnumFacing.getHorizontal(combinedMeta & 0x3).rotateYCCW();
/*     */   }
/*     */ 
/*     */   
/*     */   protected static boolean isOpen(int combinedMeta) {
/* 403 */     return ((combinedMeta & 0x4) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static boolean isTop(int meta) {
/* 408 */     return ((meta & 0x8) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static boolean isHingeLeft(int combinedMeta) {
/* 413 */     return ((combinedMeta & 0x10) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 418 */     return new BlockState(this, new IProperty[] { (IProperty)HALF, (IProperty)FACING, (IProperty)OPEN, (IProperty)HINGE, (IProperty)POWERED });
/*     */   }
/*     */   
/*     */   public enum EnumDoorHalf
/*     */     implements IStringSerializable {
/* 423 */     UPPER,
/* 424 */     LOWER;
/*     */ 
/*     */     
/*     */     public String toString() {
/* 428 */       return getName();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/* 433 */       return (this == UPPER) ? "upper" : "lower";
/*     */     }
/*     */   }
/*     */   
/*     */   public enum EnumHingePosition
/*     */     implements IStringSerializable {
/* 439 */     LEFT,
/* 440 */     RIGHT;
/*     */ 
/*     */     
/*     */     public String toString() {
/* 444 */       return getName();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/* 449 */       return (this == LEFT) ? "left" : "right";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockDoor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */