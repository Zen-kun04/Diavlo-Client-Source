/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityMinecartCommandBlock;
/*     */ import net.minecraft.entity.item.EntityMinecart;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EntitySelectors;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockRailDetector
/*     */   extends BlockRailBase {
/*  25 */   public static final PropertyEnum<BlockRailBase.EnumRailDirection> SHAPE = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class, new Predicate<BlockRailBase.EnumRailDirection>()
/*     */       {
/*     */         public boolean apply(BlockRailBase.EnumRailDirection p_apply_1_)
/*     */         {
/*  29 */           return (p_apply_1_ != BlockRailBase.EnumRailDirection.NORTH_EAST && p_apply_1_ != BlockRailBase.EnumRailDirection.NORTH_WEST && p_apply_1_ != BlockRailBase.EnumRailDirection.SOUTH_EAST && p_apply_1_ != BlockRailBase.EnumRailDirection.SOUTH_WEST);
/*     */         }
/*     */       });
/*  32 */   public static final PropertyBool POWERED = PropertyBool.create("powered");
/*     */ 
/*     */   
/*     */   public BlockRailDetector() {
/*  36 */     super(true);
/*  37 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)POWERED, Boolean.valueOf(false)).withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH));
/*  38 */     setTickRandomly(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public int tickRate(World worldIn) {
/*  43 */     return 20;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canProvidePower() {
/*  48 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
/*  53 */     if (!worldIn.isRemote)
/*     */     {
/*  55 */       if (!((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
/*     */       {
/*  57 */         updatePoweredState(worldIn, pos, state);
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
/*  68 */     if (!worldIn.isRemote && ((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
/*     */     {
/*  70 */       updatePoweredState(worldIn, pos, state);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
/*  76 */     return ((Boolean)state.getValue((IProperty)POWERED)).booleanValue() ? 15 : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
/*  81 */     return !((Boolean)state.getValue((IProperty)POWERED)).booleanValue() ? 0 : ((side == EnumFacing.UP) ? 15 : 0);
/*     */   }
/*     */ 
/*     */   
/*     */   private void updatePoweredState(World worldIn, BlockPos pos, IBlockState state) {
/*  86 */     boolean flag = ((Boolean)state.getValue((IProperty)POWERED)).booleanValue();
/*  87 */     boolean flag1 = false;
/*  88 */     List<EntityMinecart> list = findMinecarts(worldIn, pos, EntityMinecart.class, (Predicate<Entity>[])new Predicate[0]);
/*     */     
/*  90 */     if (!list.isEmpty())
/*     */     {
/*  92 */       flag1 = true;
/*     */     }
/*     */     
/*  95 */     if (flag1 && !flag) {
/*     */       
/*  97 */       worldIn.setBlockState(pos, state.withProperty((IProperty)POWERED, Boolean.valueOf(true)), 3);
/*  98 */       worldIn.notifyNeighborsOfStateChange(pos, this);
/*  99 */       worldIn.notifyNeighborsOfStateChange(pos.down(), this);
/* 100 */       worldIn.markBlockRangeForRenderUpdate(pos, pos);
/*     */     } 
/*     */     
/* 103 */     if (!flag1 && flag) {
/*     */       
/* 105 */       worldIn.setBlockState(pos, state.withProperty((IProperty)POWERED, Boolean.valueOf(false)), 3);
/* 106 */       worldIn.notifyNeighborsOfStateChange(pos, this);
/* 107 */       worldIn.notifyNeighborsOfStateChange(pos.down(), this);
/* 108 */       worldIn.markBlockRangeForRenderUpdate(pos, pos);
/*     */     } 
/*     */     
/* 111 */     if (flag1)
/*     */     {
/* 113 */       worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
/*     */     }
/*     */     
/* 116 */     worldIn.updateComparatorOutputLevel(pos, this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
/* 121 */     super.onBlockAdded(worldIn, pos, state);
/* 122 */     updatePoweredState(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public IProperty<BlockRailBase.EnumRailDirection> getShapeProperty() {
/* 127 */     return (IProperty<BlockRailBase.EnumRailDirection>)SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasComparatorInputOverride() {
/* 132 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getComparatorInputOverride(World worldIn, BlockPos pos) {
/* 137 */     if (((Boolean)worldIn.getBlockState(pos).getValue((IProperty)POWERED)).booleanValue()) {
/*     */       
/* 139 */       List<EntityMinecartCommandBlock> list = findMinecarts(worldIn, pos, EntityMinecartCommandBlock.class, (Predicate<Entity>[])new Predicate[0]);
/*     */       
/* 141 */       if (!list.isEmpty())
/*     */       {
/* 143 */         return ((EntityMinecartCommandBlock)list.get(0)).getCommandBlockLogic().getSuccessCount();
/*     */       }
/*     */       
/* 146 */       List<EntityMinecart> list1 = findMinecarts(worldIn, pos, EntityMinecart.class, (Predicate<Entity>[])new Predicate[] { EntitySelectors.selectInventories });
/*     */       
/* 148 */       if (!list1.isEmpty())
/*     */       {
/* 150 */         return Container.calcRedstoneFromInventory((IInventory)list1.get(0));
/*     */       }
/*     */     } 
/*     */     
/* 154 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T extends EntityMinecart> List<T> findMinecarts(World worldIn, BlockPos pos, Class<T> clazz, Predicate<Entity>... filter) {
/* 159 */     AxisAlignedBB axisalignedbb = getDectectionBox(pos);
/* 160 */     return (filter.length != 1) ? worldIn.getEntitiesWithinAABB(clazz, axisalignedbb) : worldIn.getEntitiesWithinAABB(clazz, axisalignedbb, filter[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   private AxisAlignedBB getDectectionBox(BlockPos pos) {
/* 165 */     float f = 0.2F;
/* 166 */     return new AxisAlignedBB((pos.getX() + 0.2F), pos.getY(), (pos.getZ() + 0.2F), ((pos.getX() + 1) - 0.2F), ((pos.getY() + 1) - 0.2F), ((pos.getZ() + 1) - 0.2F));
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 171 */     return getDefaultState().withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.byMetadata(meta & 0x7)).withProperty((IProperty)POWERED, Boolean.valueOf(((meta & 0x8) > 0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 176 */     int i = 0;
/* 177 */     i |= ((BlockRailBase.EnumRailDirection)state.getValue((IProperty)SHAPE)).getMetadata();
/*     */     
/* 179 */     if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
/*     */     {
/* 181 */       i |= 0x8;
/*     */     }
/*     */     
/* 184 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 189 */     return new BlockState(this, new IProperty[] { (IProperty)SHAPE, (IProperty)POWERED });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockRailDetector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */