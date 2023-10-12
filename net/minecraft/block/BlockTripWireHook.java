/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.properties.PropertyDirection;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockTripWireHook extends Block {
/*  24 */   public static final PropertyDirection FACING = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
/*  25 */   public static final PropertyBool POWERED = PropertyBool.create("powered");
/*  26 */   public static final PropertyBool ATTACHED = PropertyBool.create("attached");
/*  27 */   public static final PropertyBool SUSPENDED = PropertyBool.create("suspended");
/*     */ 
/*     */   
/*     */   public BlockTripWireHook() {
/*  31 */     super(Material.circuits);
/*  32 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)POWERED, Boolean.valueOf(false)).withProperty((IProperty)ATTACHED, Boolean.valueOf(false)).withProperty((IProperty)SUSPENDED, Boolean.valueOf(false)));
/*  33 */     setCreativeTab(CreativeTabs.tabRedstone);
/*  34 */     setTickRandomly(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
/*  39 */     return state.withProperty((IProperty)SUSPENDED, Boolean.valueOf(!World.doesBlockHaveSolidTopSurface(worldIn, pos.down())));
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
/*  44 */     return null;
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
/*  59 */     return (side.getAxis().isHorizontal() && worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock().isNormalCube());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
/*  64 */     for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
/*     */       
/*  66 */       if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock().isNormalCube())
/*     */       {
/*  68 */         return true;
/*     */       }
/*     */     } 
/*     */     
/*  72 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
/*  77 */     IBlockState iblockstate = getDefaultState().withProperty((IProperty)POWERED, Boolean.valueOf(false)).withProperty((IProperty)ATTACHED, Boolean.valueOf(false)).withProperty((IProperty)SUSPENDED, Boolean.valueOf(false));
/*     */     
/*  79 */     if (facing.getAxis().isHorizontal())
/*     */     {
/*  81 */       iblockstate = iblockstate.withProperty((IProperty)FACING, (Comparable)facing);
/*     */     }
/*     */     
/*  84 */     return iblockstate;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
/*  89 */     func_176260_a(worldIn, pos, state, false, false, -1, (IBlockState)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/*  94 */     if (neighborBlock != this)
/*     */     {
/*  96 */       if (checkForDrop(worldIn, pos, state)) {
/*     */         
/*  98 */         EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
/*     */         
/* 100 */         if (!worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock().isNormalCube()) {
/*     */           
/* 102 */           dropBlockAsItem(worldIn, pos, state, 0);
/* 103 */           worldIn.setBlockToAir(pos);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_176260_a(World worldIn, BlockPos pos, IBlockState hookState, boolean p_176260_4_, boolean p_176260_5_, int p_176260_6_, IBlockState p_176260_7_) {
/*     */     int k, m;
/* 111 */     EnumFacing enumfacing = (EnumFacing)hookState.getValue((IProperty)FACING);
/* 112 */     int flag = ((Boolean)hookState.getValue((IProperty)ATTACHED)).booleanValue();
/* 113 */     boolean flag1 = ((Boolean)hookState.getValue((IProperty)POWERED)).booleanValue();
/* 114 */     boolean flag2 = !World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.down());
/* 115 */     boolean flag3 = !p_176260_4_;
/* 116 */     boolean flag4 = false;
/* 117 */     int i = 0;
/* 118 */     IBlockState[] aiblockstate = new IBlockState[42];
/*     */     
/* 120 */     for (int j = 1; j < 42; j++) {
/*     */       
/* 122 */       BlockPos blockpos = pos.offset(enumfacing, j);
/* 123 */       IBlockState iblockstate = worldIn.getBlockState(blockpos);
/*     */       
/* 125 */       if (iblockstate.getBlock() == Blocks.tripwire_hook) {
/*     */         
/* 127 */         if (iblockstate.getValue((IProperty)FACING) == enumfacing.getOpposite())
/*     */         {
/* 129 */           i = j;
/*     */         }
/*     */         
/*     */         break;
/*     */       } 
/*     */       
/* 135 */       if (iblockstate.getBlock() != Blocks.tripwire && j != p_176260_6_) {
/*     */         
/* 137 */         aiblockstate[j] = null;
/* 138 */         flag3 = false;
/*     */       }
/*     */       else {
/*     */         
/* 142 */         if (j == p_176260_6_)
/*     */         {
/* 144 */           iblockstate = (IBlockState)Objects.firstNonNull(p_176260_7_, iblockstate);
/*     */         }
/*     */         
/* 147 */         int flag5 = !((Boolean)iblockstate.getValue((IProperty)BlockTripWire.DISARMED)).booleanValue() ? 1 : 0;
/* 148 */         boolean flag6 = ((Boolean)iblockstate.getValue((IProperty)BlockTripWire.POWERED)).booleanValue();
/* 149 */         boolean flag7 = ((Boolean)iblockstate.getValue((IProperty)BlockTripWire.SUSPENDED)).booleanValue();
/* 150 */         int n = flag3 & ((flag7 == flag2) ? 1 : 0);
/* 151 */         m = flag4 | ((flag5 && flag6) ? 1 : 0);
/* 152 */         aiblockstate[j] = iblockstate;
/*     */         
/* 154 */         if (j == p_176260_6_) {
/*     */           
/* 156 */           worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
/* 157 */           k = n & flag5;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 162 */     k &= (i > 1) ? 1 : 0;
/* 163 */     m &= k;
/* 164 */     IBlockState iblockstate1 = getDefaultState().withProperty((IProperty)ATTACHED, Boolean.valueOf(k)).withProperty((IProperty)POWERED, Boolean.valueOf(m));
/*     */     
/* 166 */     if (i > 0) {
/*     */       
/* 168 */       BlockPos blockpos1 = pos.offset(enumfacing, i);
/* 169 */       EnumFacing enumfacing1 = enumfacing.getOpposite();
/* 170 */       worldIn.setBlockState(blockpos1, iblockstate1.withProperty((IProperty)FACING, (Comparable)enumfacing1), 3);
/* 171 */       func_176262_b(worldIn, blockpos1, enumfacing1);
/* 172 */       func_180694_a(worldIn, blockpos1, k, m, flag, flag1);
/*     */     } 
/*     */     
/* 175 */     func_180694_a(worldIn, pos, k, m, flag, flag1);
/*     */     
/* 177 */     if (!p_176260_4_) {
/*     */       
/* 179 */       worldIn.setBlockState(pos, iblockstate1.withProperty((IProperty)FACING, (Comparable)enumfacing), 3);
/*     */       
/* 181 */       if (p_176260_5_)
/*     */       {
/* 183 */         func_176262_b(worldIn, pos, enumfacing);
/*     */       }
/*     */     } 
/*     */     
/* 187 */     if (flag != k)
/*     */     {
/* 189 */       for (int n = 1; n < i; n++) {
/*     */         
/* 191 */         BlockPos blockpos2 = pos.offset(enumfacing, n);
/* 192 */         IBlockState iblockstate2 = aiblockstate[n];
/*     */         
/* 194 */         if (iblockstate2 != null && worldIn.getBlockState(blockpos2).getBlock() != Blocks.air)
/*     */         {
/* 196 */           worldIn.setBlockState(blockpos2, iblockstate2.withProperty((IProperty)ATTACHED, Boolean.valueOf(k)), 3);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {}
/*     */ 
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/* 208 */     func_176260_a(worldIn, pos, state, false, true, -1, (IBlockState)null);
/*     */   }
/*     */ 
/*     */   
/*     */   private void func_180694_a(World worldIn, BlockPos pos, boolean p_180694_3_, boolean p_180694_4_, boolean p_180694_5_, boolean p_180694_6_) {
/* 213 */     if (p_180694_4_ && !p_180694_6_) {
/*     */       
/* 215 */       worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D, "random.click", 0.4F, 0.6F);
/*     */     }
/* 217 */     else if (!p_180694_4_ && p_180694_6_) {
/*     */       
/* 219 */       worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D, "random.click", 0.4F, 0.5F);
/*     */     }
/* 221 */     else if (p_180694_3_ && !p_180694_5_) {
/*     */       
/* 223 */       worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D, "random.click", 0.4F, 0.7F);
/*     */     }
/* 225 */     else if (!p_180694_3_ && p_180694_5_) {
/*     */       
/* 227 */       worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D, "random.bowhit", 0.4F, 1.2F / (worldIn.rand.nextFloat() * 0.2F + 0.9F));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void func_176262_b(World worldIn, BlockPos p_176262_2_, EnumFacing p_176262_3_) {
/* 233 */     worldIn.notifyNeighborsOfStateChange(p_176262_2_, this);
/* 234 */     worldIn.notifyNeighborsOfStateChange(p_176262_2_.offset(p_176262_3_.getOpposite()), this);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
/* 239 */     if (!canPlaceBlockAt(worldIn, pos)) {
/*     */       
/* 241 */       dropBlockAsItem(worldIn, pos, state, 0);
/* 242 */       worldIn.setBlockToAir(pos);
/* 243 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 247 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
/* 254 */     float f = 0.1875F;
/*     */     
/* 256 */     switch ((EnumFacing)worldIn.getBlockState(pos).getValue((IProperty)FACING)) {
/*     */       
/*     */       case EAST:
/* 259 */         setBlockBounds(0.0F, 0.2F, 0.5F - f, f * 2.0F, 0.8F, 0.5F + f);
/*     */         break;
/*     */       
/*     */       case WEST:
/* 263 */         setBlockBounds(1.0F - f * 2.0F, 0.2F, 0.5F - f, 1.0F, 0.8F, 0.5F + f);
/*     */         break;
/*     */       
/*     */       case SOUTH:
/* 267 */         setBlockBounds(0.5F - f, 0.2F, 0.0F, 0.5F + f, 0.8F, f * 2.0F);
/*     */         break;
/*     */       
/*     */       case NORTH:
/* 271 */         setBlockBounds(0.5F - f, 0.2F, 1.0F - f * 2.0F, 0.5F + f, 0.8F, 1.0F);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
/* 277 */     boolean flag = ((Boolean)state.getValue((IProperty)ATTACHED)).booleanValue();
/* 278 */     boolean flag1 = ((Boolean)state.getValue((IProperty)POWERED)).booleanValue();
/*     */     
/* 280 */     if (flag || flag1)
/*     */     {
/* 282 */       func_176260_a(worldIn, pos, state, true, false, -1, (IBlockState)null);
/*     */     }
/*     */     
/* 285 */     if (flag1) {
/*     */       
/* 287 */       worldIn.notifyNeighborsOfStateChange(pos, this);
/* 288 */       worldIn.notifyNeighborsOfStateChange(pos.offset(((EnumFacing)state.getValue((IProperty)FACING)).getOpposite()), this);
/*     */     } 
/*     */     
/* 291 */     super.breakBlock(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
/* 296 */     return ((Boolean)state.getValue((IProperty)POWERED)).booleanValue() ? 15 : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
/* 301 */     return !((Boolean)state.getValue((IProperty)POWERED)).booleanValue() ? 0 : ((state.getValue((IProperty)FACING) == side) ? 15 : 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canProvidePower() {
/* 306 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer() {
/* 311 */     return EnumWorldBlockLayer.CUTOUT_MIPPED;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 316 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.getHorizontal(meta & 0x3)).withProperty((IProperty)POWERED, Boolean.valueOf(((meta & 0x8) > 0))).withProperty((IProperty)ATTACHED, Boolean.valueOf(((meta & 0x4) > 0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 321 */     int i = 0;
/* 322 */     i |= ((EnumFacing)state.getValue((IProperty)FACING)).getHorizontalIndex();
/*     */     
/* 324 */     if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
/*     */     {
/* 326 */       i |= 0x8;
/*     */     }
/*     */     
/* 329 */     if (((Boolean)state.getValue((IProperty)ATTACHED)).booleanValue())
/*     */     {
/* 331 */       i |= 0x4;
/*     */     }
/*     */     
/* 334 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 339 */     return new BlockState(this, new IProperty[] { (IProperty)FACING, (IProperty)POWERED, (IProperty)ATTACHED, (IProperty)SUSPENDED });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockTripWireHook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */