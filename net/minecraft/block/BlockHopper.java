/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.material.MapColor;
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
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.inventory.InventoryHelper;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityHopper;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockHopper extends BlockContainer {
/*  31 */   public static final PropertyDirection FACING = PropertyDirection.create("facing", new Predicate<EnumFacing>()
/*     */       {
/*     */         public boolean apply(EnumFacing p_apply_1_)
/*     */         {
/*  35 */           return (p_apply_1_ != EnumFacing.UP);
/*     */         }
/*     */       });
/*  38 */   public static final PropertyBool ENABLED = PropertyBool.create("enabled");
/*     */ 
/*     */   
/*     */   public BlockHopper() {
/*  42 */     super(Material.iron, MapColor.stoneColor);
/*  43 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.DOWN).withProperty((IProperty)ENABLED, Boolean.valueOf(true)));
/*  44 */     setCreativeTab(CreativeTabs.tabRedstone);
/*  45 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
/*  50 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
/*  55 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
/*  56 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*  57 */     float f = 0.125F;
/*  58 */     setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
/*  59 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*  60 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
/*  61 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*  62 */     setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*  63 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*  64 */     setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
/*  65 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*  66 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
/*  71 */     EnumFacing enumfacing = facing.getOpposite();
/*     */     
/*  73 */     if (enumfacing == EnumFacing.UP)
/*     */     {
/*  75 */       enumfacing = EnumFacing.DOWN;
/*     */     }
/*     */     
/*  78 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)enumfacing).withProperty((IProperty)ENABLED, Boolean.valueOf(true));
/*     */   }
/*     */ 
/*     */   
/*     */   public TileEntity createNewTileEntity(World worldIn, int meta) {
/*  83 */     return (TileEntity)new TileEntityHopper();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
/*  88 */     super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
/*     */     
/*  90 */     if (stack.hasDisplayName()) {
/*     */       
/*  92 */       TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */       
/*  94 */       if (tileentity instanceof TileEntityHopper)
/*     */       {
/*  96 */         ((TileEntityHopper)tileentity).setCustomName(stack.getDisplayName());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
/* 103 */     updateState(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
/* 108 */     if (worldIn.isRemote)
/*     */     {
/* 110 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 114 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/* 116 */     if (tileentity instanceof TileEntityHopper) {
/*     */       
/* 118 */       playerIn.displayGUIChest((IInventory)tileentity);
/* 119 */       playerIn.triggerAchievement(StatList.field_181732_P);
/*     */     } 
/*     */     
/* 122 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/* 128 */     updateState(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateState(World worldIn, BlockPos pos, IBlockState state) {
/* 133 */     boolean flag = !worldIn.isBlockPowered(pos);
/*     */     
/* 135 */     if (flag != ((Boolean)state.getValue((IProperty)ENABLED)).booleanValue())
/*     */     {
/* 137 */       worldIn.setBlockState(pos, state.withProperty((IProperty)ENABLED, Boolean.valueOf(flag)), 4);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
/* 143 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/* 145 */     if (tileentity instanceof TileEntityHopper) {
/*     */       
/* 147 */       InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
/* 148 */       worldIn.updateComparatorOutputLevel(pos, this);
/*     */     } 
/*     */     
/* 151 */     super.breakBlock(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRenderType() {
/* 156 */     return 3;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/* 161 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/* 166 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
/* 171 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static EnumFacing getFacing(int meta) {
/* 176 */     return EnumFacing.getFront(meta & 0x7);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isEnabled(int meta) {
/* 181 */     return ((meta & 0x8) != 8);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasComparatorInputOverride() {
/* 186 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getComparatorInputOverride(World worldIn, BlockPos pos) {
/* 191 */     return Container.calcRedstone(worldIn.getTileEntity(pos));
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer() {
/* 196 */     return EnumWorldBlockLayer.CUTOUT_MIPPED;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 201 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)getFacing(meta)).withProperty((IProperty)ENABLED, Boolean.valueOf(isEnabled(meta)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 206 */     int i = 0;
/* 207 */     i |= ((EnumFacing)state.getValue((IProperty)FACING)).getIndex();
/*     */     
/* 209 */     if (!((Boolean)state.getValue((IProperty)ENABLED)).booleanValue())
/*     */     {
/* 211 */       i |= 0x8;
/*     */     }
/*     */     
/* 214 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 219 */     return new BlockState(this, new IProperty[] { (IProperty)FACING, (IProperty)ENABLED });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockHopper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */