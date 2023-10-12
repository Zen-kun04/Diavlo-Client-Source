/*     */ package net.minecraft.block;
/*     */ 
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockLever
/*     */   extends Block {
/*  21 */   public static final PropertyEnum<EnumOrientation> FACING = PropertyEnum.create("facing", EnumOrientation.class);
/*  22 */   public static final PropertyBool POWERED = PropertyBool.create("powered");
/*     */ 
/*     */   
/*     */   protected BlockLever() {
/*  26 */     super(Material.circuits);
/*  27 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, EnumOrientation.NORTH).withProperty((IProperty)POWERED, Boolean.valueOf(false)));
/*  28 */     setCreativeTab(CreativeTabs.tabRedstone);
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
/*  33 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/*  38 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/*  43 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
/*  48 */     return func_181090_a(worldIn, pos, side.getOpposite());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
/*  53 */     for (EnumFacing enumfacing : EnumFacing.values()) {
/*     */       
/*  55 */       if (func_181090_a(worldIn, pos, enumfacing))
/*     */       {
/*  57 */         return true;
/*     */       }
/*     */     } 
/*     */     
/*  61 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static boolean func_181090_a(World p_181090_0_, BlockPos p_181090_1_, EnumFacing p_181090_2_) {
/*  66 */     return BlockButton.func_181088_a(p_181090_0_, p_181090_1_, p_181090_2_);
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
/*  71 */     IBlockState iblockstate = getDefaultState().withProperty((IProperty)POWERED, Boolean.valueOf(false));
/*     */     
/*  73 */     if (func_181090_a(worldIn, pos, facing.getOpposite()))
/*     */     {
/*  75 */       return iblockstate.withProperty((IProperty)FACING, EnumOrientation.forFacings(facing, placer.getHorizontalFacing()));
/*     */     }
/*     */ 
/*     */     
/*  79 */     for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
/*     */       
/*  81 */       if (enumfacing != facing && func_181090_a(worldIn, pos, enumfacing.getOpposite()))
/*     */       {
/*  83 */         return iblockstate.withProperty((IProperty)FACING, EnumOrientation.forFacings(enumfacing, placer.getHorizontalFacing()));
/*     */       }
/*     */     } 
/*     */     
/*  87 */     if (World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.down()))
/*     */     {
/*  89 */       return iblockstate.withProperty((IProperty)FACING, EnumOrientation.forFacings(EnumFacing.UP, placer.getHorizontalFacing()));
/*     */     }
/*     */ 
/*     */     
/*  93 */     return iblockstate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getMetadataForFacing(EnumFacing facing) {
/* 100 */     switch (facing) {
/*     */       
/*     */       case X:
/* 103 */         return 0;
/*     */       
/*     */       case Z:
/* 106 */         return 5;
/*     */       
/*     */       case null:
/* 109 */         return 4;
/*     */       
/*     */       case null:
/* 112 */         return 3;
/*     */       
/*     */       case null:
/* 115 */         return 2;
/*     */       
/*     */       case null:
/* 118 */         return 1;
/*     */     } 
/*     */     
/* 121 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/* 127 */     if (func_181091_e(worldIn, pos, state) && !func_181090_a(worldIn, pos, ((EnumOrientation)state.getValue((IProperty)FACING)).getFacing().getOpposite())) {
/*     */       
/* 129 */       dropBlockAsItem(worldIn, pos, state, 0);
/* 130 */       worldIn.setBlockToAir(pos);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean func_181091_e(World p_181091_1_, BlockPos p_181091_2_, IBlockState p_181091_3_) {
/* 136 */     if (canPlaceBlockAt(p_181091_1_, p_181091_2_))
/*     */     {
/* 138 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 142 */     dropBlockAsItem(p_181091_1_, p_181091_2_, p_181091_3_, 0);
/* 143 */     p_181091_1_.setBlockToAir(p_181091_2_);
/* 144 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
/* 150 */     float f = 0.1875F;
/*     */     
/* 152 */     switch ((EnumOrientation)worldIn.getBlockState(pos).getValue((IProperty)FACING)) {
/*     */       
/*     */       case X:
/* 155 */         setBlockBounds(0.0F, 0.2F, 0.5F - f, f * 2.0F, 0.8F, 0.5F + f);
/*     */         break;
/*     */       
/*     */       case Z:
/* 159 */         setBlockBounds(1.0F - f * 2.0F, 0.2F, 0.5F - f, 1.0F, 0.8F, 0.5F + f);
/*     */         break;
/*     */       
/*     */       case null:
/* 163 */         setBlockBounds(0.5F - f, 0.2F, 0.0F, 0.5F + f, 0.8F, f * 2.0F);
/*     */         break;
/*     */       
/*     */       case null:
/* 167 */         setBlockBounds(0.5F - f, 0.2F, 1.0F - f * 2.0F, 0.5F + f, 0.8F, 1.0F);
/*     */         break;
/*     */       
/*     */       case null:
/*     */       case null:
/* 172 */         f = 0.25F;
/* 173 */         setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.6F, 0.5F + f);
/*     */         break;
/*     */       
/*     */       case null:
/*     */       case null:
/* 178 */         f = 0.25F;
/* 179 */         setBlockBounds(0.5F - f, 0.4F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
/* 185 */     if (worldIn.isRemote)
/*     */     {
/* 187 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 191 */     state = state.cycleProperty((IProperty)POWERED);
/* 192 */     worldIn.setBlockState(pos, state, 3);
/* 193 */     worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "random.click", 0.3F, ((Boolean)state.getValue((IProperty)POWERED)).booleanValue() ? 0.6F : 0.5F);
/* 194 */     worldIn.notifyNeighborsOfStateChange(pos, this);
/* 195 */     EnumFacing enumfacing = ((EnumOrientation)state.getValue((IProperty)FACING)).getFacing();
/* 196 */     worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing.getOpposite()), this);
/* 197 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
/* 203 */     if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue()) {
/*     */       
/* 205 */       worldIn.notifyNeighborsOfStateChange(pos, this);
/* 206 */       EnumFacing enumfacing = ((EnumOrientation)state.getValue((IProperty)FACING)).getFacing();
/* 207 */       worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing.getOpposite()), this);
/*     */     } 
/*     */     
/* 210 */     super.breakBlock(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
/* 215 */     return ((Boolean)state.getValue((IProperty)POWERED)).booleanValue() ? 15 : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
/* 220 */     return !((Boolean)state.getValue((IProperty)POWERED)).booleanValue() ? 0 : ((((EnumOrientation)state.getValue((IProperty)FACING)).getFacing() == side) ? 15 : 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canProvidePower() {
/* 225 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 230 */     return getDefaultState().withProperty((IProperty)FACING, EnumOrientation.byMetadata(meta & 0x7)).withProperty((IProperty)POWERED, Boolean.valueOf(((meta & 0x8) > 0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 235 */     int i = 0;
/* 236 */     i |= ((EnumOrientation)state.getValue((IProperty)FACING)).getMetadata();
/*     */     
/* 238 */     if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
/*     */     {
/* 240 */       i |= 0x8;
/*     */     }
/*     */     
/* 243 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 248 */     return new BlockState(this, new IProperty[] { (IProperty)FACING, (IProperty)POWERED });
/*     */   }
/*     */   
/*     */   public enum EnumOrientation
/*     */     implements IStringSerializable {
/* 253 */     DOWN_X(0, "down_x", EnumFacing.DOWN),
/* 254 */     EAST(1, "east", EnumFacing.EAST),
/* 255 */     WEST(2, "west", EnumFacing.WEST),
/* 256 */     SOUTH(3, "south", EnumFacing.SOUTH),
/* 257 */     NORTH(4, "north", EnumFacing.NORTH),
/* 258 */     UP_Z(5, "up_z", EnumFacing.UP),
/* 259 */     UP_X(6, "up_x", EnumFacing.UP),
/* 260 */     DOWN_Z(7, "down_z", EnumFacing.DOWN);
/*     */     
/* 262 */     private static final EnumOrientation[] META_LOOKUP = new EnumOrientation[(values()).length];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int meta;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final EnumFacing facing;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/* 352 */       for (EnumOrientation blocklever$enumorientation : values())
/*     */       {
/* 354 */         META_LOOKUP[blocklever$enumorientation.getMetadata()] = blocklever$enumorientation;
/*     */       }
/*     */     }
/*     */     
/*     */     EnumOrientation(int meta, String name, EnumFacing facing) {
/*     */       this.meta = meta;
/*     */       this.name = name;
/*     */       this.facing = facing;
/*     */     }
/*     */     
/*     */     public int getMetadata() {
/*     */       return this.meta;
/*     */     }
/*     */     
/*     */     public EnumFacing getFacing() {
/*     */       return this.facing;
/*     */     }
/*     */     
/*     */     public String toString() {
/*     */       return this.name;
/*     */     }
/*     */     
/*     */     public static EnumOrientation byMetadata(int meta) {
/*     */       if (meta < 0 || meta >= META_LOOKUP.length)
/*     */         meta = 0; 
/*     */       return META_LOOKUP[meta];
/*     */     }
/*     */     
/*     */     public static EnumOrientation forFacings(EnumFacing clickedSide, EnumFacing entityFacing) {
/*     */       switch (clickedSide) {
/*     */         case X:
/*     */           switch (entityFacing.getAxis()) {
/*     */             case X:
/*     */               return DOWN_X;
/*     */             case Z:
/*     */               return DOWN_Z;
/*     */           } 
/*     */           throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
/*     */         case Z:
/*     */           switch (entityFacing.getAxis()) {
/*     */             case X:
/*     */               return UP_X;
/*     */             case Z:
/*     */               return UP_Z;
/*     */           } 
/*     */           throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
/*     */         case null:
/*     */           return NORTH;
/*     */         case null:
/*     */           return SOUTH;
/*     */         case null:
/*     */           return WEST;
/*     */         case null:
/*     */           return EAST;
/*     */       } 
/*     */       throw new IllegalArgumentException("Invalid facing: " + clickedSide);
/*     */     }
/*     */     
/*     */     public String getName() {
/*     */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockLever.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */