/*     */ package net.minecraft.block;
/*     */ 
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockFenceGate
/*     */   extends BlockDirectional {
/*  20 */   public static final PropertyBool OPEN = PropertyBool.create("open");
/*  21 */   public static final PropertyBool POWERED = PropertyBool.create("powered");
/*  22 */   public static final PropertyBool IN_WALL = PropertyBool.create("in_wall");
/*     */ 
/*     */   
/*     */   public BlockFenceGate(BlockPlanks.EnumType p_i46394_1_) {
/*  26 */     super(Material.wood, p_i46394_1_.getMapColor());
/*  27 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)OPEN, Boolean.valueOf(false)).withProperty((IProperty)POWERED, Boolean.valueOf(false)).withProperty((IProperty)IN_WALL, Boolean.valueOf(false)));
/*  28 */     setCreativeTab(CreativeTabs.tabRedstone);
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
/*  33 */     EnumFacing.Axis enumfacing$axis = ((EnumFacing)state.getValue((IProperty)FACING)).getAxis();
/*     */     
/*  35 */     if ((enumfacing$axis == EnumFacing.Axis.Z && (worldIn.getBlockState(pos.west()).getBlock() == Blocks.cobblestone_wall || worldIn.getBlockState(pos.east()).getBlock() == Blocks.cobblestone_wall)) || (enumfacing$axis == EnumFacing.Axis.X && (worldIn.getBlockState(pos.north()).getBlock() == Blocks.cobblestone_wall || worldIn.getBlockState(pos.south()).getBlock() == Blocks.cobblestone_wall)))
/*     */     {
/*  37 */       state = state.withProperty((IProperty)IN_WALL, Boolean.valueOf(true));
/*     */     }
/*     */     
/*  40 */     return state;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
/*  45 */     return worldIn.getBlockState(pos.down()).getBlock().getMaterial().isSolid() ? super.canPlaceBlockAt(worldIn, pos) : false;
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
/*  50 */     if (((Boolean)state.getValue((IProperty)OPEN)).booleanValue())
/*     */     {
/*  52 */       return null;
/*     */     }
/*     */ 
/*     */     
/*  56 */     EnumFacing.Axis enumfacing$axis = ((EnumFacing)state.getValue((IProperty)FACING)).getAxis();
/*  57 */     return (enumfacing$axis == EnumFacing.Axis.Z) ? new AxisAlignedBB(pos.getX(), pos.getY(), (pos.getZ() + 0.375F), (pos.getX() + 1), (pos.getY() + 1.5F), (pos.getZ() + 0.625F)) : new AxisAlignedBB((pos.getX() + 0.375F), pos.getY(), pos.getZ(), (pos.getX() + 0.625F), (pos.getY() + 1.5F), (pos.getZ() + 1));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
/*  63 */     EnumFacing.Axis enumfacing$axis = ((EnumFacing)worldIn.getBlockState(pos).getValue((IProperty)FACING)).getAxis();
/*     */     
/*  65 */     if (enumfacing$axis == EnumFacing.Axis.Z) {
/*     */       
/*  67 */       setBlockBounds(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
/*     */     }
/*     */     else {
/*     */       
/*  71 */       setBlockBounds(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/*  77 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/*  82 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
/*  87 */     return ((Boolean)worldIn.getBlockState(pos).getValue((IProperty)OPEN)).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
/*  92 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)placer.getHorizontalFacing()).withProperty((IProperty)OPEN, Boolean.valueOf(false)).withProperty((IProperty)POWERED, Boolean.valueOf(false)).withProperty((IProperty)IN_WALL, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
/*  97 */     if (((Boolean)state.getValue((IProperty)OPEN)).booleanValue()) {
/*     */       
/*  99 */       state = state.withProperty((IProperty)OPEN, Boolean.valueOf(false));
/* 100 */       worldIn.setBlockState(pos, state, 2);
/*     */     }
/*     */     else {
/*     */       
/* 104 */       EnumFacing enumfacing = EnumFacing.fromAngle(playerIn.rotationYaw);
/*     */       
/* 106 */       if (state.getValue((IProperty)FACING) == enumfacing.getOpposite())
/*     */       {
/* 108 */         state = state.withProperty((IProperty)FACING, (Comparable)enumfacing);
/*     */       }
/*     */       
/* 111 */       state = state.withProperty((IProperty)OPEN, Boolean.valueOf(true));
/* 112 */       worldIn.setBlockState(pos, state, 2);
/*     */     } 
/*     */     
/* 115 */     worldIn.playAuxSFXAtEntity(playerIn, ((Boolean)state.getValue((IProperty)OPEN)).booleanValue() ? 1003 : 1006, pos, 0);
/* 116 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/* 121 */     if (!worldIn.isRemote) {
/*     */       
/* 123 */       boolean flag = worldIn.isBlockPowered(pos);
/*     */       
/* 125 */       if (flag || neighborBlock.canProvidePower())
/*     */       {
/* 127 */         if (flag && !((Boolean)state.getValue((IProperty)OPEN)).booleanValue() && !((Boolean)state.getValue((IProperty)POWERED)).booleanValue()) {
/*     */           
/* 129 */           worldIn.setBlockState(pos, state.withProperty((IProperty)OPEN, Boolean.valueOf(true)).withProperty((IProperty)POWERED, Boolean.valueOf(true)), 2);
/* 130 */           worldIn.playAuxSFXAtEntity((EntityPlayer)null, 1003, pos, 0);
/*     */         }
/* 132 */         else if (!flag && ((Boolean)state.getValue((IProperty)OPEN)).booleanValue() && ((Boolean)state.getValue((IProperty)POWERED)).booleanValue()) {
/*     */           
/* 134 */           worldIn.setBlockState(pos, state.withProperty((IProperty)OPEN, Boolean.valueOf(false)).withProperty((IProperty)POWERED, Boolean.valueOf(false)), 2);
/* 135 */           worldIn.playAuxSFXAtEntity((EntityPlayer)null, 1006, pos, 0);
/*     */         }
/* 137 */         else if (flag != ((Boolean)state.getValue((IProperty)POWERED)).booleanValue()) {
/*     */           
/* 139 */           worldIn.setBlockState(pos, state.withProperty((IProperty)POWERED, Boolean.valueOf(flag)), 2);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
/* 147 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 152 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.getHorizontal(meta)).withProperty((IProperty)OPEN, Boolean.valueOf(((meta & 0x4) != 0))).withProperty((IProperty)POWERED, Boolean.valueOf(((meta & 0x8) != 0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 157 */     int i = 0;
/* 158 */     i |= ((EnumFacing)state.getValue((IProperty)FACING)).getHorizontalIndex();
/*     */     
/* 160 */     if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
/*     */     {
/* 162 */       i |= 0x8;
/*     */     }
/*     */     
/* 165 */     if (((Boolean)state.getValue((IProperty)OPEN)).booleanValue())
/*     */     {
/* 167 */       i |= 0x4;
/*     */     }
/*     */     
/* 170 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 175 */     return new BlockState(this, new IProperty[] { (IProperty)FACING, (IProperty)OPEN, (IProperty)POWERED, (IProperty)IN_WALL });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockFenceGate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */