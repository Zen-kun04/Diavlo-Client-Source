/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.properties.PropertyDirection;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
/*     */ import net.minecraft.dispenser.IBehaviorDispenseItem;
/*     */ import net.minecraft.dispenser.IBlockSource;
/*     */ import net.minecraft.dispenser.IPosition;
/*     */ import net.minecraft.dispenser.PositionImpl;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.inventory.InventoryHelper;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityDispenser;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.RegistryDefaulted;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockDispenser
/*     */   extends BlockContainer {
/*  33 */   public static final PropertyDirection FACING = PropertyDirection.create("facing");
/*  34 */   public static final PropertyBool TRIGGERED = PropertyBool.create("triggered");
/*  35 */   public static final RegistryDefaulted<Item, IBehaviorDispenseItem> dispenseBehaviorRegistry = new RegistryDefaulted(new BehaviorDefaultDispenseItem());
/*  36 */   protected Random rand = new Random();
/*     */ 
/*     */   
/*     */   protected BlockDispenser() {
/*  40 */     super(Material.rock);
/*  41 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)TRIGGERED, Boolean.valueOf(false)));
/*  42 */     setCreativeTab(CreativeTabs.tabRedstone);
/*     */   }
/*     */ 
/*     */   
/*     */   public int tickRate(World worldIn) {
/*  47 */     return 4;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
/*  52 */     super.onBlockAdded(worldIn, pos, state);
/*  53 */     setDefaultDirection(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setDefaultDirection(World worldIn, BlockPos pos, IBlockState state) {
/*  58 */     if (!worldIn.isRemote) {
/*     */       
/*  60 */       EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
/*  61 */       boolean flag = worldIn.getBlockState(pos.north()).getBlock().isFullBlock();
/*  62 */       boolean flag1 = worldIn.getBlockState(pos.south()).getBlock().isFullBlock();
/*     */       
/*  64 */       if (enumfacing == EnumFacing.NORTH && flag && !flag1) {
/*     */         
/*  66 */         enumfacing = EnumFacing.SOUTH;
/*     */       }
/*  68 */       else if (enumfacing == EnumFacing.SOUTH && flag1 && !flag) {
/*     */         
/*  70 */         enumfacing = EnumFacing.NORTH;
/*     */       }
/*     */       else {
/*     */         
/*  74 */         boolean flag2 = worldIn.getBlockState(pos.west()).getBlock().isFullBlock();
/*  75 */         boolean flag3 = worldIn.getBlockState(pos.east()).getBlock().isFullBlock();
/*     */         
/*  77 */         if (enumfacing == EnumFacing.WEST && flag2 && !flag3) {
/*     */           
/*  79 */           enumfacing = EnumFacing.EAST;
/*     */         }
/*  81 */         else if (enumfacing == EnumFacing.EAST && flag3 && !flag2) {
/*     */           
/*  83 */           enumfacing = EnumFacing.WEST;
/*     */         } 
/*     */       } 
/*     */       
/*  87 */       worldIn.setBlockState(pos, state.withProperty((IProperty)FACING, (Comparable)enumfacing).withProperty((IProperty)TRIGGERED, Boolean.valueOf(false)), 2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
/*  93 */     if (worldIn.isRemote)
/*     */     {
/*  95 */       return true;
/*     */     }
/*     */ 
/*     */     
/*  99 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/* 101 */     if (tileentity instanceof TileEntityDispenser) {
/*     */       
/* 103 */       playerIn.displayGUIChest((IInventory)tileentity);
/*     */       
/* 105 */       if (tileentity instanceof net.minecraft.tileentity.TileEntityDropper) {
/*     */         
/* 107 */         playerIn.triggerAchievement(StatList.field_181731_O);
/*     */       }
/*     */       else {
/*     */         
/* 111 */         playerIn.triggerAchievement(StatList.field_181733_Q);
/*     */       } 
/*     */     } 
/*     */     
/* 115 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void dispense(World worldIn, BlockPos pos) {
/* 121 */     BlockSourceImpl blocksourceimpl = new BlockSourceImpl(worldIn, pos);
/* 122 */     TileEntityDispenser tileentitydispenser = blocksourceimpl.<TileEntityDispenser>getBlockTileEntity();
/*     */     
/* 124 */     if (tileentitydispenser != null) {
/*     */       
/* 126 */       int i = tileentitydispenser.getDispenseSlot();
/*     */       
/* 128 */       if (i < 0) {
/*     */         
/* 130 */         worldIn.playAuxSFX(1001, pos, 0);
/*     */       }
/*     */       else {
/*     */         
/* 134 */         ItemStack itemstack = tileentitydispenser.getStackInSlot(i);
/* 135 */         IBehaviorDispenseItem ibehaviordispenseitem = getBehavior(itemstack);
/*     */         
/* 137 */         if (ibehaviordispenseitem != IBehaviorDispenseItem.itemDispenseBehaviorProvider) {
/*     */           
/* 139 */           ItemStack itemstack1 = ibehaviordispenseitem.dispense(blocksourceimpl, itemstack);
/* 140 */           tileentitydispenser.setInventorySlotContents(i, (itemstack1.stackSize <= 0) ? null : itemstack1);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected IBehaviorDispenseItem getBehavior(ItemStack stack) {
/* 148 */     return (IBehaviorDispenseItem)dispenseBehaviorRegistry.getObject((stack == null) ? null : stack.getItem());
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/* 153 */     boolean flag = (worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.up()));
/* 154 */     boolean flag1 = ((Boolean)state.getValue((IProperty)TRIGGERED)).booleanValue();
/*     */     
/* 156 */     if (flag && !flag1) {
/*     */       
/* 158 */       worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
/* 159 */       worldIn.setBlockState(pos, state.withProperty((IProperty)TRIGGERED, Boolean.valueOf(true)), 4);
/*     */     }
/* 161 */     else if (!flag && flag1) {
/*     */       
/* 163 */       worldIn.setBlockState(pos, state.withProperty((IProperty)TRIGGERED, Boolean.valueOf(false)), 4);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/* 169 */     if (!worldIn.isRemote)
/*     */     {
/* 171 */       dispense(worldIn, pos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public TileEntity createNewTileEntity(World worldIn, int meta) {
/* 177 */     return (TileEntity)new TileEntityDispenser();
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
/* 182 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)BlockPistonBase.getFacingFromEntity(worldIn, pos, placer)).withProperty((IProperty)TRIGGERED, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
/* 187 */     worldIn.setBlockState(pos, state.withProperty((IProperty)FACING, (Comparable)BlockPistonBase.getFacingFromEntity(worldIn, pos, placer)), 2);
/*     */     
/* 189 */     if (stack.hasDisplayName()) {
/*     */       
/* 191 */       TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */       
/* 193 */       if (tileentity instanceof TileEntityDispenser)
/*     */       {
/* 195 */         ((TileEntityDispenser)tileentity).setCustomName(stack.getDisplayName());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
/* 202 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/* 204 */     if (tileentity instanceof TileEntityDispenser) {
/*     */       
/* 206 */       InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
/* 207 */       worldIn.updateComparatorOutputLevel(pos, this);
/*     */     } 
/*     */     
/* 210 */     super.breakBlock(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public static IPosition getDispensePosition(IBlockSource coords) {
/* 215 */     EnumFacing enumfacing = getFacing(coords.getBlockMetadata());
/* 216 */     double d0 = coords.getX() + 0.7D * enumfacing.getFrontOffsetX();
/* 217 */     double d1 = coords.getY() + 0.7D * enumfacing.getFrontOffsetY();
/* 218 */     double d2 = coords.getZ() + 0.7D * enumfacing.getFrontOffsetZ();
/* 219 */     return (IPosition)new PositionImpl(d0, d1, d2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static EnumFacing getFacing(int meta) {
/* 224 */     return EnumFacing.getFront(meta & 0x7);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasComparatorInputOverride() {
/* 229 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getComparatorInputOverride(World worldIn, BlockPos pos) {
/* 234 */     return Container.calcRedstone(worldIn.getTileEntity(pos));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRenderType() {
/* 239 */     return 3;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateForEntityRender(IBlockState state) {
/* 244 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.SOUTH);
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 249 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)getFacing(meta)).withProperty((IProperty)TRIGGERED, Boolean.valueOf(((meta & 0x8) > 0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 254 */     int i = 0;
/* 255 */     i |= ((EnumFacing)state.getValue((IProperty)FACING)).getIndex();
/*     */     
/* 257 */     if (((Boolean)state.getValue((IProperty)TRIGGERED)).booleanValue())
/*     */     {
/* 259 */       i |= 0x8;
/*     */     }
/*     */     
/* 262 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 267 */     return new BlockState(this, new IProperty[] { (IProperty)FACING, (IProperty)TRIGGERED });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockDispenser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */