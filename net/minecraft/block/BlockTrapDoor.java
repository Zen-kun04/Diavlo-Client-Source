/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.properties.PropertyDirection;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockTrapDoor extends Block {
/*  26 */   public static final PropertyDirection FACING = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
/*  27 */   public static final PropertyBool OPEN = PropertyBool.create("open");
/*  28 */   public static final PropertyEnum<DoorHalf> HALF = PropertyEnum.create("half", DoorHalf.class);
/*     */ 
/*     */   
/*     */   protected BlockTrapDoor(Material materialIn) {
/*  32 */     super(materialIn);
/*  33 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)OPEN, Boolean.valueOf(false)).withProperty((IProperty)HALF, DoorHalf.BOTTOM));
/*  34 */     float f = 0.5F;
/*  35 */     float f1 = 1.0F;
/*  36 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*  37 */     setCreativeTab(CreativeTabs.tabRedstone);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/*  42 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/*  47 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
/*  52 */     return !((Boolean)worldIn.getBlockState(pos).getValue((IProperty)OPEN)).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
/*  57 */     setBlockBoundsBasedOnState((IBlockAccess)worldIn, pos);
/*  58 */     return super.getSelectedBoundingBox(worldIn, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
/*  63 */     setBlockBoundsBasedOnState((IBlockAccess)worldIn, pos);
/*  64 */     return super.getCollisionBoundingBox(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
/*  69 */     setBounds(worldIn.getBlockState(pos));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockBoundsForItemRender() {
/*  74 */     float f = 0.1875F;
/*  75 */     setBlockBounds(0.0F, 0.40625F, 0.0F, 1.0F, 0.59375F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBounds(IBlockState state) {
/*  80 */     if (state.getBlock() == this) {
/*     */       
/*  82 */       boolean flag = (state.getValue((IProperty)HALF) == DoorHalf.TOP);
/*  83 */       Boolean obool = (Boolean)state.getValue((IProperty)OPEN);
/*  84 */       EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
/*  85 */       float f = 0.1875F;
/*     */       
/*  87 */       if (flag) {
/*     */         
/*  89 */         setBlockBounds(0.0F, 0.8125F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */       }
/*     */       else {
/*     */         
/*  93 */         setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);
/*     */       } 
/*     */       
/*  96 */       if (obool.booleanValue()) {
/*     */         
/*  98 */         if (enumfacing == EnumFacing.NORTH)
/*     */         {
/* 100 */           setBlockBounds(0.0F, 0.0F, 0.8125F, 1.0F, 1.0F, 1.0F);
/*     */         }
/*     */         
/* 103 */         if (enumfacing == EnumFacing.SOUTH)
/*     */         {
/* 105 */           setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.1875F);
/*     */         }
/*     */         
/* 108 */         if (enumfacing == EnumFacing.WEST)
/*     */         {
/* 110 */           setBlockBounds(0.8125F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */         }
/*     */         
/* 113 */         if (enumfacing == EnumFacing.EAST)
/*     */         {
/* 115 */           setBlockBounds(0.0F, 0.0F, 0.0F, 0.1875F, 1.0F, 1.0F);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
/* 123 */     if (this.blockMaterial == Material.iron)
/*     */     {
/* 125 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 129 */     state = state.cycleProperty((IProperty)OPEN);
/* 130 */     worldIn.setBlockState(pos, state, 2);
/* 131 */     worldIn.playAuxSFXAtEntity(playerIn, ((Boolean)state.getValue((IProperty)OPEN)).booleanValue() ? 1003 : 1006, pos, 0);
/* 132 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/* 138 */     if (!worldIn.isRemote) {
/*     */       
/* 140 */       BlockPos blockpos = pos.offset(((EnumFacing)state.getValue((IProperty)FACING)).getOpposite());
/*     */       
/* 142 */       if (!isValidSupportBlock(worldIn.getBlockState(blockpos).getBlock())) {
/*     */         
/* 144 */         worldIn.setBlockToAir(pos);
/* 145 */         dropBlockAsItem(worldIn, pos, state, 0);
/*     */       }
/*     */       else {
/*     */         
/* 149 */         boolean flag = worldIn.isBlockPowered(pos);
/*     */         
/* 151 */         if (flag || neighborBlock.canProvidePower()) {
/*     */           
/* 153 */           boolean flag1 = ((Boolean)state.getValue((IProperty)OPEN)).booleanValue();
/*     */           
/* 155 */           if (flag1 != flag) {
/*     */             
/* 157 */             worldIn.setBlockState(pos, state.withProperty((IProperty)OPEN, Boolean.valueOf(flag)), 2);
/* 158 */             worldIn.playAuxSFXAtEntity((EntityPlayer)null, flag ? 1003 : 1006, pos, 0);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end) {
/* 167 */     setBlockBoundsBasedOnState((IBlockAccess)worldIn, pos);
/* 168 */     return super.collisionRayTrace(worldIn, pos, start, end);
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
/* 173 */     IBlockState iblockstate = getDefaultState();
/*     */     
/* 175 */     if (facing.getAxis().isHorizontal()) {
/*     */       
/* 177 */       iblockstate = iblockstate.withProperty((IProperty)FACING, (Comparable)facing).withProperty((IProperty)OPEN, Boolean.valueOf(false));
/* 178 */       iblockstate = iblockstate.withProperty((IProperty)HALF, (hitY > 0.5F) ? DoorHalf.TOP : DoorHalf.BOTTOM);
/*     */     } 
/*     */     
/* 181 */     return iblockstate;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
/* 186 */     return (!side.getAxis().isVertical() && isValidSupportBlock(worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected static EnumFacing getFacing(int meta) {
/* 191 */     switch (meta & 0x3) {
/*     */       
/*     */       case 0:
/* 194 */         return EnumFacing.NORTH;
/*     */       
/*     */       case 1:
/* 197 */         return EnumFacing.SOUTH;
/*     */       
/*     */       case 2:
/* 200 */         return EnumFacing.WEST;
/*     */     } 
/*     */ 
/*     */     
/* 204 */     return EnumFacing.EAST;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static int getMetaForFacing(EnumFacing facing) {
/* 210 */     switch (facing) {
/*     */       
/*     */       case NORTH:
/* 213 */         return 0;
/*     */       
/*     */       case SOUTH:
/* 216 */         return 1;
/*     */       
/*     */       case WEST:
/* 219 */         return 2;
/*     */     } 
/*     */ 
/*     */     
/* 223 */     return 3;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isValidSupportBlock(Block blockIn) {
/* 229 */     return ((blockIn.blockMaterial.isOpaque() && blockIn.isFullCube()) || blockIn == Blocks.glowstone || blockIn instanceof BlockSlab || blockIn instanceof BlockStairs);
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer() {
/* 234 */     return EnumWorldBlockLayer.CUTOUT;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 239 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)getFacing(meta)).withProperty((IProperty)OPEN, Boolean.valueOf(((meta & 0x4) != 0))).withProperty((IProperty)HALF, ((meta & 0x8) == 0) ? DoorHalf.BOTTOM : DoorHalf.TOP);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 244 */     int i = 0;
/* 245 */     i |= getMetaForFacing((EnumFacing)state.getValue((IProperty)FACING));
/*     */     
/* 247 */     if (((Boolean)state.getValue((IProperty)OPEN)).booleanValue())
/*     */     {
/* 249 */       i |= 0x4;
/*     */     }
/*     */     
/* 252 */     if (state.getValue((IProperty)HALF) == DoorHalf.TOP)
/*     */     {
/* 254 */       i |= 0x8;
/*     */     }
/*     */     
/* 257 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 262 */     return new BlockState(this, new IProperty[] { (IProperty)FACING, (IProperty)OPEN, (IProperty)HALF });
/*     */   }
/*     */   
/*     */   public enum DoorHalf
/*     */     implements IStringSerializable {
/* 267 */     TOP("top"),
/* 268 */     BOTTOM("bottom");
/*     */     
/*     */     private final String name;
/*     */ 
/*     */     
/*     */     DoorHalf(String name) {
/* 274 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 279 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/* 284 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockTrapDoor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */