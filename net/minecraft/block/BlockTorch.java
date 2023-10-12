/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyDirection;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockTorch extends Block {
/*  24 */   public static final PropertyDirection FACING = PropertyDirection.create("facing", new Predicate<EnumFacing>()
/*     */       {
/*     */         public boolean apply(EnumFacing p_apply_1_)
/*     */         {
/*  28 */           return (p_apply_1_ != EnumFacing.DOWN);
/*     */         }
/*     */       });
/*     */ 
/*     */   
/*     */   protected BlockTorch() {
/*  34 */     super(Material.circuits);
/*  35 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.UP));
/*  36 */     setTickRandomly(true);
/*  37 */     setCreativeTab(CreativeTabs.tabDecorations);
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
/*  42 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/*  47 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/*  52 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean canPlaceOn(World worldIn, BlockPos pos) {
/*  57 */     if (World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos))
/*     */     {
/*  59 */       return true;
/*     */     }
/*     */ 
/*     */     
/*  63 */     Block block = worldIn.getBlockState(pos).getBlock();
/*  64 */     return (block instanceof BlockFence || block == Blocks.glass || block == Blocks.cobblestone_wall || block == Blocks.stained_glass);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
/*  70 */     for (EnumFacing enumfacing : FACING.getAllowedValues()) {
/*     */       
/*  72 */       if (canPlaceAt(worldIn, pos, enumfacing))
/*     */       {
/*  74 */         return true;
/*     */       }
/*     */     } 
/*     */     
/*  78 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean canPlaceAt(World worldIn, BlockPos pos, EnumFacing facing) {
/*  83 */     BlockPos blockpos = pos.offset(facing.getOpposite());
/*  84 */     boolean flag = facing.getAxis().isHorizontal();
/*  85 */     return ((flag && worldIn.isBlockNormalCube(blockpos, true)) || (facing.equals(EnumFacing.UP) && canPlaceOn(worldIn, blockpos)));
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
/*  90 */     if (canPlaceAt(worldIn, pos, facing))
/*     */     {
/*  92 */       return getDefaultState().withProperty((IProperty)FACING, (Comparable)facing);
/*     */     }
/*     */ 
/*     */     
/*  96 */     for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
/*     */       
/*  98 */       if (worldIn.isBlockNormalCube(pos.offset(enumfacing.getOpposite()), true))
/*     */       {
/* 100 */         return getDefaultState().withProperty((IProperty)FACING, (Comparable)enumfacing);
/*     */       }
/*     */     } 
/*     */     
/* 104 */     return getDefaultState();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
/* 110 */     checkForDrop(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/* 115 */     onNeighborChangeInternal(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean onNeighborChangeInternal(World worldIn, BlockPos pos, IBlockState state) {
/* 120 */     if (!checkForDrop(worldIn, pos, state))
/*     */     {
/* 122 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 126 */     EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
/* 127 */     EnumFacing.Axis enumfacing$axis = enumfacing.getAxis();
/* 128 */     EnumFacing enumfacing1 = enumfacing.getOpposite();
/* 129 */     boolean flag = false;
/*     */     
/* 131 */     if (enumfacing$axis.isHorizontal() && !worldIn.isBlockNormalCube(pos.offset(enumfacing1), true)) {
/*     */       
/* 133 */       flag = true;
/*     */     }
/* 135 */     else if (enumfacing$axis.isVertical() && !canPlaceOn(worldIn, pos.offset(enumfacing1))) {
/*     */       
/* 137 */       flag = true;
/*     */     } 
/*     */     
/* 140 */     if (flag) {
/*     */       
/* 142 */       dropBlockAsItem(worldIn, pos, state, 0);
/* 143 */       worldIn.setBlockToAir(pos);
/* 144 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 148 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
/* 155 */     if (state.getBlock() == this && canPlaceAt(worldIn, pos, (EnumFacing)state.getValue((IProperty)FACING)))
/*     */     {
/* 157 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 161 */     if (worldIn.getBlockState(pos).getBlock() == this) {
/*     */       
/* 163 */       dropBlockAsItem(worldIn, pos, state, 0);
/* 164 */       worldIn.setBlockToAir(pos);
/*     */     } 
/*     */     
/* 167 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end) {
/* 173 */     EnumFacing enumfacing = (EnumFacing)worldIn.getBlockState(pos).getValue((IProperty)FACING);
/* 174 */     float f = 0.15F;
/*     */     
/* 176 */     if (enumfacing == EnumFacing.EAST) {
/*     */       
/* 178 */       setBlockBounds(0.0F, 0.2F, 0.5F - f, f * 2.0F, 0.8F, 0.5F + f);
/*     */     }
/* 180 */     else if (enumfacing == EnumFacing.WEST) {
/*     */       
/* 182 */       setBlockBounds(1.0F - f * 2.0F, 0.2F, 0.5F - f, 1.0F, 0.8F, 0.5F + f);
/*     */     }
/* 184 */     else if (enumfacing == EnumFacing.SOUTH) {
/*     */       
/* 186 */       setBlockBounds(0.5F - f, 0.2F, 0.0F, 0.5F + f, 0.8F, f * 2.0F);
/*     */     }
/* 188 */     else if (enumfacing == EnumFacing.NORTH) {
/*     */       
/* 190 */       setBlockBounds(0.5F - f, 0.2F, 1.0F - f * 2.0F, 0.5F + f, 0.8F, 1.0F);
/*     */     }
/*     */     else {
/*     */       
/* 194 */       f = 0.1F;
/* 195 */       setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.6F, 0.5F + f);
/*     */     } 
/*     */     
/* 198 */     return super.collisionRayTrace(worldIn, pos, start, end);
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/* 203 */     EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
/* 204 */     double d0 = pos.getX() + 0.5D;
/* 205 */     double d1 = pos.getY() + 0.7D;
/* 206 */     double d2 = pos.getZ() + 0.5D;
/* 207 */     double d3 = 0.22D;
/* 208 */     double d4 = 0.27D;
/*     */     
/* 210 */     if (enumfacing.getAxis().isHorizontal()) {
/*     */       
/* 212 */       EnumFacing enumfacing1 = enumfacing.getOpposite();
/* 213 */       worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4 * enumfacing1.getFrontOffsetX(), d1 + d3, d2 + d4 * enumfacing1.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D, new int[0]);
/* 214 */       worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4 * enumfacing1.getFrontOffsetX(), d1 + d3, d2 + d4 * enumfacing1.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D, new int[0]);
/*     */     }
/*     */     else {
/*     */       
/* 218 */       worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
/* 219 */       worldIn.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer() {
/* 225 */     return EnumWorldBlockLayer.CUTOUT;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 230 */     IBlockState iblockstate = getDefaultState();
/*     */     
/* 232 */     switch (meta)
/*     */     
/*     */     { case 1:
/* 235 */         iblockstate = iblockstate.withProperty((IProperty)FACING, (Comparable)EnumFacing.EAST);
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
/* 255 */         return iblockstate;case 2: iblockstate = iblockstate.withProperty((IProperty)FACING, (Comparable)EnumFacing.WEST); return iblockstate;case 3: iblockstate = iblockstate.withProperty((IProperty)FACING, (Comparable)EnumFacing.SOUTH); return iblockstate;case 4: iblockstate = iblockstate.withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH); return iblockstate; }  iblockstate = iblockstate.withProperty((IProperty)FACING, (Comparable)EnumFacing.UP); return iblockstate;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 260 */     int i = 0;
/*     */     
/* 262 */     switch ((EnumFacing)state.getValue((IProperty)FACING))
/*     */     
/*     */     { case EAST:
/* 265 */         i |= 0x1;
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
/* 286 */         return i;case WEST: i |= 0x2; return i;case SOUTH: i |= 0x3; return i;case NORTH: i |= 0x4; return i; }  i |= 0x5; return i;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 291 */     return new BlockState(this, new IProperty[] { (IProperty)FACING });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockTorch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */