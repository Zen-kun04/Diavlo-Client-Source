/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockTripWire
/*     */   extends Block {
/*  24 */   public static final PropertyBool POWERED = PropertyBool.create("powered");
/*  25 */   public static final PropertyBool SUSPENDED = PropertyBool.create("suspended");
/*  26 */   public static final PropertyBool ATTACHED = PropertyBool.create("attached");
/*  27 */   public static final PropertyBool DISARMED = PropertyBool.create("disarmed");
/*  28 */   public static final PropertyBool NORTH = PropertyBool.create("north");
/*  29 */   public static final PropertyBool EAST = PropertyBool.create("east");
/*  30 */   public static final PropertyBool SOUTH = PropertyBool.create("south");
/*  31 */   public static final PropertyBool WEST = PropertyBool.create("west");
/*     */ 
/*     */   
/*     */   public BlockTripWire() {
/*  35 */     super(Material.circuits);
/*  36 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)POWERED, Boolean.valueOf(false)).withProperty((IProperty)SUSPENDED, Boolean.valueOf(false)).withProperty((IProperty)ATTACHED, Boolean.valueOf(false)).withProperty((IProperty)DISARMED, Boolean.valueOf(false)).withProperty((IProperty)NORTH, Boolean.valueOf(false)).withProperty((IProperty)EAST, Boolean.valueOf(false)).withProperty((IProperty)SOUTH, Boolean.valueOf(false)).withProperty((IProperty)WEST, Boolean.valueOf(false)));
/*  37 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.15625F, 1.0F);
/*  38 */     setTickRandomly(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
/*  43 */     return state.withProperty((IProperty)NORTH, Boolean.valueOf(isConnectedTo(worldIn, pos, state, EnumFacing.NORTH))).withProperty((IProperty)EAST, Boolean.valueOf(isConnectedTo(worldIn, pos, state, EnumFacing.EAST))).withProperty((IProperty)SOUTH, Boolean.valueOf(isConnectedTo(worldIn, pos, state, EnumFacing.SOUTH))).withProperty((IProperty)WEST, Boolean.valueOf(isConnectedTo(worldIn, pos, state, EnumFacing.WEST)));
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
/*  48 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/*  53 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/*  58 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer() {
/*  63 */     return EnumWorldBlockLayer.TRANSLUCENT;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/*  68 */     return Items.string;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos) {
/*  73 */     return Items.string;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/*  78 */     boolean flag = ((Boolean)state.getValue((IProperty)SUSPENDED)).booleanValue();
/*  79 */     boolean flag1 = !World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.down());
/*     */     
/*  81 */     if (flag != flag1) {
/*     */       
/*  83 */       dropBlockAsItem(worldIn, pos, state, 0);
/*  84 */       worldIn.setBlockToAir(pos);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
/*  90 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*  91 */     boolean flag = ((Boolean)iblockstate.getValue((IProperty)ATTACHED)).booleanValue();
/*  92 */     boolean flag1 = ((Boolean)iblockstate.getValue((IProperty)SUSPENDED)).booleanValue();
/*     */     
/*  94 */     if (!flag1) {
/*     */       
/*  96 */       setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.09375F, 1.0F);
/*     */     }
/*  98 */     else if (!flag) {
/*     */       
/* 100 */       setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
/*     */     }
/*     */     else {
/*     */       
/* 104 */       setBlockBounds(0.0F, 0.0625F, 0.0F, 1.0F, 0.15625F, 1.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
/* 110 */     state = state.withProperty((IProperty)SUSPENDED, Boolean.valueOf(!World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.down())));
/* 111 */     worldIn.setBlockState(pos, state, 3);
/* 112 */     notifyHook(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
/* 117 */     notifyHook(worldIn, pos, state.withProperty((IProperty)POWERED, Boolean.valueOf(true)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
/* 122 */     if (!worldIn.isRemote)
/*     */     {
/* 124 */       if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.shears)
/*     */       {
/* 126 */         worldIn.setBlockState(pos, state.withProperty((IProperty)DISARMED, Boolean.valueOf(true)), 4);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void notifyHook(World worldIn, BlockPos pos, IBlockState state) {
/* 133 */     for (EnumFacing enumfacing : new EnumFacing[] { EnumFacing.SOUTH, EnumFacing.WEST }) {
/*     */       
/* 135 */       for (int i = 1; i < 42; i++) {
/*     */         
/* 137 */         BlockPos blockpos = pos.offset(enumfacing, i);
/* 138 */         IBlockState iblockstate = worldIn.getBlockState(blockpos);
/*     */         
/* 140 */         if (iblockstate.getBlock() == Blocks.tripwire_hook) {
/*     */           
/* 142 */           if (iblockstate.getValue((IProperty)BlockTripWireHook.FACING) == enumfacing.getOpposite())
/*     */           {
/* 144 */             Blocks.tripwire_hook.func_176260_a(worldIn, blockpos, iblockstate, false, true, i, state);
/*     */           }
/*     */           
/*     */           break;
/*     */         } 
/*     */         
/* 150 */         if (iblockstate.getBlock() != Blocks.tripwire) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
/* 160 */     if (!worldIn.isRemote)
/*     */     {
/* 162 */       if (!((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
/*     */       {
/* 164 */         updateState(worldIn, pos);
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
/* 175 */     if (!worldIn.isRemote)
/*     */     {
/* 177 */       if (((Boolean)worldIn.getBlockState(pos).getValue((IProperty)POWERED)).booleanValue())
/*     */       {
/* 179 */         updateState(worldIn, pos);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateState(World worldIn, BlockPos pos) {
/* 186 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/* 187 */     boolean flag = ((Boolean)iblockstate.getValue((IProperty)POWERED)).booleanValue();
/* 188 */     boolean flag1 = false;
/* 189 */     List<? extends Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ, pos.getX() + this.maxX, pos.getY() + this.maxY, pos.getZ() + this.maxZ));
/*     */     
/* 191 */     if (!list.isEmpty())
/*     */     {
/* 193 */       for (Entity entity : list) {
/*     */         
/* 195 */         if (!entity.doesEntityNotTriggerPressurePlate()) {
/*     */           
/* 197 */           flag1 = true;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/* 203 */     if (flag1 != flag) {
/*     */       
/* 205 */       iblockstate = iblockstate.withProperty((IProperty)POWERED, Boolean.valueOf(flag1));
/* 206 */       worldIn.setBlockState(pos, iblockstate, 3);
/* 207 */       notifyHook(worldIn, pos, iblockstate);
/*     */     } 
/*     */     
/* 210 */     if (flag1)
/*     */     {
/* 212 */       worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isConnectedTo(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing direction) {
/* 218 */     BlockPos blockpos = pos.offset(direction);
/* 219 */     IBlockState iblockstate = worldIn.getBlockState(blockpos);
/* 220 */     Block block = iblockstate.getBlock();
/*     */     
/* 222 */     if (block == Blocks.tripwire_hook) {
/*     */       
/* 224 */       EnumFacing enumfacing = direction.getOpposite();
/* 225 */       return (iblockstate.getValue((IProperty)BlockTripWireHook.FACING) == enumfacing);
/*     */     } 
/* 227 */     if (block == Blocks.tripwire) {
/*     */       
/* 229 */       boolean flag = ((Boolean)state.getValue((IProperty)SUSPENDED)).booleanValue();
/* 230 */       boolean flag1 = ((Boolean)iblockstate.getValue((IProperty)SUSPENDED)).booleanValue();
/* 231 */       return (flag == flag1);
/*     */     } 
/*     */ 
/*     */     
/* 235 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 241 */     return getDefaultState().withProperty((IProperty)POWERED, Boolean.valueOf(((meta & 0x1) > 0))).withProperty((IProperty)SUSPENDED, Boolean.valueOf(((meta & 0x2) > 0))).withProperty((IProperty)ATTACHED, Boolean.valueOf(((meta & 0x4) > 0))).withProperty((IProperty)DISARMED, Boolean.valueOf(((meta & 0x8) > 0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 246 */     int i = 0;
/*     */     
/* 248 */     if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
/*     */     {
/* 250 */       i |= 0x1;
/*     */     }
/*     */     
/* 253 */     if (((Boolean)state.getValue((IProperty)SUSPENDED)).booleanValue())
/*     */     {
/* 255 */       i |= 0x2;
/*     */     }
/*     */     
/* 258 */     if (((Boolean)state.getValue((IProperty)ATTACHED)).booleanValue())
/*     */     {
/* 260 */       i |= 0x4;
/*     */     }
/*     */     
/* 263 */     if (((Boolean)state.getValue((IProperty)DISARMED)).booleanValue())
/*     */     {
/* 265 */       i |= 0x8;
/*     */     }
/*     */     
/* 268 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 273 */     return new BlockState(this, new IProperty[] { (IProperty)POWERED, (IProperty)SUSPENDED, (IProperty)ATTACHED, (IProperty)DISARMED, (IProperty)NORTH, (IProperty)EAST, (IProperty)WEST, (IProperty)SOUTH });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockTripWire.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */