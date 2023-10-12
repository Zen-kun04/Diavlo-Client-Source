/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.properties.PropertyDirection;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.projectile.EntityArrow;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class BlockButton
/*     */   extends Block {
/*  24 */   public static final PropertyDirection FACING = PropertyDirection.create("facing");
/*  25 */   public static final PropertyBool POWERED = PropertyBool.create("powered");
/*     */   
/*     */   private final boolean wooden;
/*     */   
/*     */   protected BlockButton(boolean wooden) {
/*  30 */     super(Material.circuits);
/*  31 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)POWERED, Boolean.valueOf(false)));
/*  32 */     setTickRandomly(true);
/*  33 */     setCreativeTab(CreativeTabs.tabRedstone);
/*  34 */     this.wooden = wooden;
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
/*  39 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int tickRate(World worldIn) {
/*  44 */     return this.wooden ? 30 : 20;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/*  49 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/*  54 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
/*  59 */     return func_181088_a(worldIn, pos, side.getOpposite());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
/*  64 */     for (EnumFacing enumfacing : EnumFacing.values()) {
/*     */       
/*  66 */       if (func_181088_a(worldIn, pos, enumfacing))
/*     */       {
/*  68 */         return true;
/*     */       }
/*     */     } 
/*     */     
/*  72 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static boolean func_181088_a(World p_181088_0_, BlockPos p_181088_1_, EnumFacing p_181088_2_) {
/*  77 */     BlockPos blockpos = p_181088_1_.offset(p_181088_2_);
/*  78 */     return (p_181088_2_ == EnumFacing.DOWN) ? World.doesBlockHaveSolidTopSurface((IBlockAccess)p_181088_0_, blockpos) : p_181088_0_.getBlockState(blockpos).getBlock().isNormalCube();
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
/*  83 */     return func_181088_a(worldIn, pos, facing.getOpposite()) ? getDefaultState().withProperty((IProperty)FACING, (Comparable)facing).withProperty((IProperty)POWERED, Boolean.valueOf(false)) : getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.DOWN).withProperty((IProperty)POWERED, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/*  88 */     if (checkForDrop(worldIn, pos, state) && !func_181088_a(worldIn, pos, ((EnumFacing)state.getValue((IProperty)FACING)).getOpposite())) {
/*     */       
/*  90 */       dropBlockAsItem(worldIn, pos, state, 0);
/*  91 */       worldIn.setBlockToAir(pos);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
/*  97 */     if (canPlaceBlockAt(worldIn, pos))
/*     */     {
/*  99 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 103 */     dropBlockAsItem(worldIn, pos, state, 0);
/* 104 */     worldIn.setBlockToAir(pos);
/* 105 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
/* 111 */     updateBlockBounds(worldIn.getBlockState(pos));
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateBlockBounds(IBlockState state) {
/* 116 */     EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
/* 117 */     boolean flag = ((Boolean)state.getValue((IProperty)POWERED)).booleanValue();
/* 118 */     float f = 0.25F;
/* 119 */     float f1 = 0.375F;
/* 120 */     float f2 = (flag ? true : 2) / 16.0F;
/* 121 */     float f3 = 0.125F;
/* 122 */     float f4 = 0.1875F;
/*     */     
/* 124 */     switch (enumfacing) {
/*     */       
/*     */       case EAST:
/* 127 */         setBlockBounds(0.0F, 0.375F, 0.3125F, f2, 0.625F, 0.6875F);
/*     */         break;
/*     */       
/*     */       case WEST:
/* 131 */         setBlockBounds(1.0F - f2, 0.375F, 0.3125F, 1.0F, 0.625F, 0.6875F);
/*     */         break;
/*     */       
/*     */       case SOUTH:
/* 135 */         setBlockBounds(0.3125F, 0.375F, 0.0F, 0.6875F, 0.625F, f2);
/*     */         break;
/*     */       
/*     */       case NORTH:
/* 139 */         setBlockBounds(0.3125F, 0.375F, 1.0F - f2, 0.6875F, 0.625F, 1.0F);
/*     */         break;
/*     */       
/*     */       case UP:
/* 143 */         setBlockBounds(0.3125F, 0.0F, 0.375F, 0.6875F, 0.0F + f2, 0.625F);
/*     */         break;
/*     */       
/*     */       case DOWN:
/* 147 */         setBlockBounds(0.3125F, 1.0F - f2, 0.375F, 0.6875F, 1.0F, 0.625F);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
/* 153 */     if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
/*     */     {
/* 155 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 159 */     worldIn.setBlockState(pos, state.withProperty((IProperty)POWERED, Boolean.valueOf(true)), 3);
/* 160 */     worldIn.markBlockRangeForRenderUpdate(pos, pos);
/* 161 */     worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "random.click", 0.3F, 0.6F);
/* 162 */     notifyNeighbors(worldIn, pos, (EnumFacing)state.getValue((IProperty)FACING));
/* 163 */     worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
/* 164 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
/* 170 */     if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
/*     */     {
/* 172 */       notifyNeighbors(worldIn, pos, (EnumFacing)state.getValue((IProperty)FACING));
/*     */     }
/*     */     
/* 175 */     super.breakBlock(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
/* 180 */     return ((Boolean)state.getValue((IProperty)POWERED)).booleanValue() ? 15 : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
/* 185 */     return !((Boolean)state.getValue((IProperty)POWERED)).booleanValue() ? 0 : ((state.getValue((IProperty)FACING) == side) ? 15 : 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canProvidePower() {
/* 190 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {}
/*     */ 
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/* 199 */     if (!worldIn.isRemote)
/*     */     {
/* 201 */       if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
/*     */       {
/* 203 */         if (this.wooden) {
/*     */           
/* 205 */           checkForArrows(worldIn, pos, state);
/*     */         }
/*     */         else {
/*     */           
/* 209 */           worldIn.setBlockState(pos, state.withProperty((IProperty)POWERED, Boolean.valueOf(false)));
/* 210 */           notifyNeighbors(worldIn, pos, (EnumFacing)state.getValue((IProperty)FACING));
/* 211 */           worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "random.click", 0.3F, 0.5F);
/* 212 */           worldIn.markBlockRangeForRenderUpdate(pos, pos);
/*     */         } 
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockBoundsForItemRender() {
/* 220 */     float f = 0.1875F;
/* 221 */     float f1 = 0.125F;
/* 222 */     float f2 = 0.125F;
/* 223 */     setBlockBounds(0.5F - f, 0.5F - f1, 0.5F - f2, 0.5F + f, 0.5F + f1, 0.5F + f2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
/* 228 */     if (!worldIn.isRemote)
/*     */     {
/* 230 */       if (this.wooden)
/*     */       {
/* 232 */         if (!((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
/*     */         {
/* 234 */           checkForArrows(worldIn, pos, state);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkForArrows(World worldIn, BlockPos pos, IBlockState state) {
/* 242 */     updateBlockBounds(state);
/* 243 */     List<? extends Entity> list = worldIn.getEntitiesWithinAABB(EntityArrow.class, new AxisAlignedBB(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ, pos.getX() + this.maxX, pos.getY() + this.maxY, pos.getZ() + this.maxZ));
/* 244 */     boolean flag = !list.isEmpty();
/* 245 */     boolean flag1 = ((Boolean)state.getValue((IProperty)POWERED)).booleanValue();
/*     */     
/* 247 */     if (flag && !flag1) {
/*     */       
/* 249 */       worldIn.setBlockState(pos, state.withProperty((IProperty)POWERED, Boolean.valueOf(true)));
/* 250 */       notifyNeighbors(worldIn, pos, (EnumFacing)state.getValue((IProperty)FACING));
/* 251 */       worldIn.markBlockRangeForRenderUpdate(pos, pos);
/* 252 */       worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "random.click", 0.3F, 0.6F);
/*     */     } 
/*     */     
/* 255 */     if (!flag && flag1) {
/*     */       
/* 257 */       worldIn.setBlockState(pos, state.withProperty((IProperty)POWERED, Boolean.valueOf(false)));
/* 258 */       notifyNeighbors(worldIn, pos, (EnumFacing)state.getValue((IProperty)FACING));
/* 259 */       worldIn.markBlockRangeForRenderUpdate(pos, pos);
/* 260 */       worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "random.click", 0.3F, 0.5F);
/*     */     } 
/*     */     
/* 263 */     if (flag)
/*     */     {
/* 265 */       worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void notifyNeighbors(World worldIn, BlockPos pos, EnumFacing facing) {
/* 271 */     worldIn.notifyNeighborsOfStateChange(pos, this);
/* 272 */     worldIn.notifyNeighborsOfStateChange(pos.offset(facing.getOpposite()), this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/*     */     EnumFacing enumfacing;
/* 279 */     switch (meta & 0x7) {
/*     */       
/*     */       case 0:
/* 282 */         enumfacing = EnumFacing.DOWN;
/*     */         break;
/*     */       
/*     */       case 1:
/* 286 */         enumfacing = EnumFacing.EAST;
/*     */         break;
/*     */       
/*     */       case 2:
/* 290 */         enumfacing = EnumFacing.WEST;
/*     */         break;
/*     */       
/*     */       case 3:
/* 294 */         enumfacing = EnumFacing.SOUTH;
/*     */         break;
/*     */       
/*     */       case 4:
/* 298 */         enumfacing = EnumFacing.NORTH;
/*     */         break;
/*     */ 
/*     */       
/*     */       default:
/* 303 */         enumfacing = EnumFacing.UP;
/*     */         break;
/*     */     } 
/* 306 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)enumfacing).withProperty((IProperty)POWERED, Boolean.valueOf(((meta & 0x8) > 0)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/*     */     int i;
/* 313 */     switch ((EnumFacing)state.getValue((IProperty)FACING)) {
/*     */       
/*     */       case EAST:
/* 316 */         i = 1;
/*     */         break;
/*     */       
/*     */       case WEST:
/* 320 */         i = 2;
/*     */         break;
/*     */       
/*     */       case SOUTH:
/* 324 */         i = 3;
/*     */         break;
/*     */       
/*     */       case NORTH:
/* 328 */         i = 4;
/*     */         break;
/*     */ 
/*     */       
/*     */       default:
/* 333 */         i = 5;
/*     */         break;
/*     */       
/*     */       case DOWN:
/* 337 */         i = 0;
/*     */         break;
/*     */     } 
/* 340 */     if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
/*     */     {
/* 342 */       i |= 0x8;
/*     */     }
/*     */     
/* 345 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 350 */     return new BlockState(this, new IProperty[] { (IProperty)FACING, (IProperty)POWERED });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */