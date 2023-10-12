/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyDirection;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityPiston;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockPistonMoving
/*     */   extends BlockContainer {
/*  25 */   public static final PropertyDirection FACING = BlockPistonExtension.FACING;
/*  26 */   public static final PropertyEnum<BlockPistonExtension.EnumPistonType> TYPE = BlockPistonExtension.TYPE;
/*     */ 
/*     */   
/*     */   public BlockPistonMoving() {
/*  30 */     super(Material.piston);
/*  31 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)TYPE, BlockPistonExtension.EnumPistonType.DEFAULT));
/*  32 */     setHardness(-1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public TileEntity createNewTileEntity(World worldIn, int meta) {
/*  37 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static TileEntity newTileEntity(IBlockState state, EnumFacing facing, boolean extending, boolean renderHead) {
/*  42 */     return (TileEntity)new TileEntityPiston(state, facing, extending, renderHead);
/*     */   }
/*     */ 
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
/*  47 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/*  49 */     if (tileentity instanceof TileEntityPiston) {
/*     */       
/*  51 */       ((TileEntityPiston)tileentity).clearPistonTileEntity();
/*     */     }
/*     */     else {
/*     */       
/*  55 */       super.breakBlock(worldIn, pos, state);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
/*  61 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
/*  66 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
/*  71 */     BlockPos blockpos = pos.offset(((EnumFacing)state.getValue((IProperty)FACING)).getOpposite());
/*  72 */     IBlockState iblockstate = worldIn.getBlockState(blockpos);
/*     */     
/*  74 */     if (iblockstate.getBlock() instanceof BlockPistonBase && ((Boolean)iblockstate.getValue((IProperty)BlockPistonBase.EXTENDED)).booleanValue())
/*     */     {
/*  76 */       worldIn.setBlockToAir(blockpos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/*  82 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/*  87 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
/*  92 */     if (!worldIn.isRemote && worldIn.getTileEntity(pos) == null) {
/*     */       
/*  94 */       worldIn.setBlockToAir(pos);
/*  95 */       return true;
/*     */     } 
/*     */ 
/*     */     
/*  99 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/* 105 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
/* 110 */     if (!worldIn.isRemote) {
/*     */       
/* 112 */       TileEntityPiston tileentitypiston = getTileEntity((IBlockAccess)worldIn, pos);
/*     */       
/* 114 */       if (tileentitypiston != null) {
/*     */         
/* 116 */         IBlockState iblockstate = tileentitypiston.getPistonState();
/* 117 */         iblockstate.getBlock().dropBlockAsItem(worldIn, pos, iblockstate, 0);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end) {
/* 124 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/* 129 */     if (!worldIn.isRemote)
/*     */     {
/* 131 */       worldIn.getTileEntity(pos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
/* 137 */     TileEntityPiston tileentitypiston = getTileEntity((IBlockAccess)worldIn, pos);
/*     */     
/* 139 */     if (tileentitypiston == null)
/*     */     {
/* 141 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 145 */     float f = tileentitypiston.getProgress(0.0F);
/*     */     
/* 147 */     if (tileentitypiston.isExtending())
/*     */     {
/* 149 */       f = 1.0F - f;
/*     */     }
/*     */     
/* 152 */     return getBoundingBox(worldIn, pos, tileentitypiston.getPistonState(), f, tileentitypiston.getFacing());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
/* 158 */     TileEntityPiston tileentitypiston = getTileEntity(worldIn, pos);
/*     */     
/* 160 */     if (tileentitypiston != null) {
/*     */       
/* 162 */       IBlockState iblockstate = tileentitypiston.getPistonState();
/* 163 */       Block block = iblockstate.getBlock();
/*     */       
/* 165 */       if (block == this || block.getMaterial() == Material.air) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 170 */       float f = tileentitypiston.getProgress(0.0F);
/*     */       
/* 172 */       if (tileentitypiston.isExtending())
/*     */       {
/* 174 */         f = 1.0F - f;
/*     */       }
/*     */       
/* 177 */       block.setBlockBoundsBasedOnState(worldIn, pos);
/*     */       
/* 179 */       if (block == Blocks.piston || block == Blocks.sticky_piston)
/*     */       {
/* 181 */         f = 0.0F;
/*     */       }
/*     */       
/* 184 */       EnumFacing enumfacing = tileentitypiston.getFacing();
/* 185 */       this.minX = block.getBlockBoundsMinX() - (enumfacing.getFrontOffsetX() * f);
/* 186 */       this.minY = block.getBlockBoundsMinY() - (enumfacing.getFrontOffsetY() * f);
/* 187 */       this.minZ = block.getBlockBoundsMinZ() - (enumfacing.getFrontOffsetZ() * f);
/* 188 */       this.maxX = block.getBlockBoundsMaxX() - (enumfacing.getFrontOffsetX() * f);
/* 189 */       this.maxY = block.getBlockBoundsMaxY() - (enumfacing.getFrontOffsetY() * f);
/* 190 */       this.maxZ = block.getBlockBoundsMaxZ() - (enumfacing.getFrontOffsetZ() * f);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getBoundingBox(World worldIn, BlockPos pos, IBlockState extendingBlock, float progress, EnumFacing direction) {
/* 196 */     if (extendingBlock.getBlock() != this && extendingBlock.getBlock().getMaterial() != Material.air) {
/*     */       
/* 198 */       AxisAlignedBB axisalignedbb = extendingBlock.getBlock().getCollisionBoundingBox(worldIn, pos, extendingBlock);
/*     */       
/* 200 */       if (axisalignedbb == null)
/*     */       {
/* 202 */         return null;
/*     */       }
/*     */ 
/*     */       
/* 206 */       double d0 = axisalignedbb.minX;
/* 207 */       double d1 = axisalignedbb.minY;
/* 208 */       double d2 = axisalignedbb.minZ;
/* 209 */       double d3 = axisalignedbb.maxX;
/* 210 */       double d4 = axisalignedbb.maxY;
/* 211 */       double d5 = axisalignedbb.maxZ;
/*     */       
/* 213 */       if (direction.getFrontOffsetX() < 0) {
/*     */         
/* 215 */         d0 -= (direction.getFrontOffsetX() * progress);
/*     */       }
/*     */       else {
/*     */         
/* 219 */         d3 -= (direction.getFrontOffsetX() * progress);
/*     */       } 
/*     */       
/* 222 */       if (direction.getFrontOffsetY() < 0) {
/*     */         
/* 224 */         d1 -= (direction.getFrontOffsetY() * progress);
/*     */       }
/*     */       else {
/*     */         
/* 228 */         d4 -= (direction.getFrontOffsetY() * progress);
/*     */       } 
/*     */       
/* 231 */       if (direction.getFrontOffsetZ() < 0) {
/*     */         
/* 233 */         d2 -= (direction.getFrontOffsetZ() * progress);
/*     */       }
/*     */       else {
/*     */         
/* 237 */         d5 -= (direction.getFrontOffsetZ() * progress);
/*     */       } 
/*     */       
/* 240 */       return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 245 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private TileEntityPiston getTileEntity(IBlockAccess worldIn, BlockPos pos) {
/* 251 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/* 252 */     return (tileentity instanceof TileEntityPiston) ? (TileEntityPiston)tileentity : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos) {
/* 257 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 262 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)BlockPistonExtension.getFacing(meta)).withProperty((IProperty)TYPE, ((meta & 0x8) > 0) ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 267 */     int i = 0;
/* 268 */     i |= ((EnumFacing)state.getValue((IProperty)FACING)).getIndex();
/*     */     
/* 270 */     if (state.getValue((IProperty)TYPE) == BlockPistonExtension.EnumPistonType.STICKY)
/*     */     {
/* 272 */       i |= 0x8;
/*     */     }
/*     */     
/* 275 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 280 */     return new BlockState(this, new IProperty[] { (IProperty)FACING, (IProperty)TYPE });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockPistonMoving.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */