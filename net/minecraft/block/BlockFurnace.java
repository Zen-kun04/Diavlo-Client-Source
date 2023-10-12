/*     */ package net.minecraft.block;
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyDirection;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.inventory.InventoryHelper;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityFurnace;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockFurnace extends BlockContainer {
/*  26 */   public static final PropertyDirection FACING = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
/*     */   
/*     */   private final boolean isBurning;
/*     */   private static boolean keepInventory;
/*     */   
/*     */   protected BlockFurnace(boolean isBurning) {
/*  32 */     super(Material.rock);
/*  33 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH));
/*  34 */     this.isBurning = isBurning;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/*  39 */     return Item.getItemFromBlock(Blocks.furnace);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
/*  44 */     setDefaultFacing(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state) {
/*  49 */     if (!worldIn.isRemote) {
/*     */       
/*  51 */       Block block = worldIn.getBlockState(pos.north()).getBlock();
/*  52 */       Block block1 = worldIn.getBlockState(pos.south()).getBlock();
/*  53 */       Block block2 = worldIn.getBlockState(pos.west()).getBlock();
/*  54 */       Block block3 = worldIn.getBlockState(pos.east()).getBlock();
/*  55 */       EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
/*     */       
/*  57 */       if (enumfacing == EnumFacing.NORTH && block.isFullBlock() && !block1.isFullBlock()) {
/*     */         
/*  59 */         enumfacing = EnumFacing.SOUTH;
/*     */       }
/*  61 */       else if (enumfacing == EnumFacing.SOUTH && block1.isFullBlock() && !block.isFullBlock()) {
/*     */         
/*  63 */         enumfacing = EnumFacing.NORTH;
/*     */       }
/*  65 */       else if (enumfacing == EnumFacing.WEST && block2.isFullBlock() && !block3.isFullBlock()) {
/*     */         
/*  67 */         enumfacing = EnumFacing.EAST;
/*     */       }
/*  69 */       else if (enumfacing == EnumFacing.EAST && block3.isFullBlock() && !block2.isFullBlock()) {
/*     */         
/*  71 */         enumfacing = EnumFacing.WEST;
/*     */       } 
/*     */       
/*  74 */       worldIn.setBlockState(pos, state.withProperty((IProperty)FACING, (Comparable)enumfacing), 2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/*  81 */     if (this.isBurning) {
/*     */       
/*  83 */       EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
/*  84 */       double d0 = pos.getX() + 0.5D;
/*  85 */       double d1 = pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
/*  86 */       double d2 = pos.getZ() + 0.5D;
/*  87 */       double d3 = 0.52D;
/*  88 */       double d4 = rand.nextDouble() * 0.6D - 0.3D;
/*     */       
/*  90 */       switch (enumfacing) {
/*     */         
/*     */         case WEST:
/*  93 */           worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
/*  94 */           worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */           break;
/*     */         
/*     */         case EAST:
/*  98 */           worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
/*  99 */           worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */           break;
/*     */         
/*     */         case NORTH:
/* 103 */           worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D, new int[0]);
/* 104 */           worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */           break;
/*     */         
/*     */         case SOUTH:
/* 108 */           worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D, new int[0]);
/* 109 */           worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
/* 116 */     if (worldIn.isRemote)
/*     */     {
/* 118 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 122 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/* 124 */     if (tileentity instanceof TileEntityFurnace) {
/*     */       
/* 126 */       playerIn.displayGUIChest((IInventory)tileentity);
/* 127 */       playerIn.triggerAchievement(StatList.field_181741_Y);
/*     */     } 
/*     */     
/* 130 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setState(boolean active, World worldIn, BlockPos pos) {
/* 136 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/* 137 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/* 138 */     keepInventory = true;
/*     */     
/* 140 */     if (active) {
/*     */       
/* 142 */       worldIn.setBlockState(pos, Blocks.lit_furnace.getDefaultState().withProperty((IProperty)FACING, iblockstate.getValue((IProperty)FACING)), 3);
/* 143 */       worldIn.setBlockState(pos, Blocks.lit_furnace.getDefaultState().withProperty((IProperty)FACING, iblockstate.getValue((IProperty)FACING)), 3);
/*     */     }
/*     */     else {
/*     */       
/* 147 */       worldIn.setBlockState(pos, Blocks.furnace.getDefaultState().withProperty((IProperty)FACING, iblockstate.getValue((IProperty)FACING)), 3);
/* 148 */       worldIn.setBlockState(pos, Blocks.furnace.getDefaultState().withProperty((IProperty)FACING, iblockstate.getValue((IProperty)FACING)), 3);
/*     */     } 
/*     */     
/* 151 */     keepInventory = false;
/*     */     
/* 153 */     if (tileentity != null) {
/*     */       
/* 155 */       tileentity.validate();
/* 156 */       worldIn.setTileEntity(pos, tileentity);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public TileEntity createNewTileEntity(World worldIn, int meta) {
/* 162 */     return (TileEntity)new TileEntityFurnace();
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
/* 167 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)placer.getHorizontalFacing().getOpposite());
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
/* 172 */     worldIn.setBlockState(pos, state.withProperty((IProperty)FACING, (Comparable)placer.getHorizontalFacing().getOpposite()), 2);
/*     */     
/* 174 */     if (stack.hasDisplayName()) {
/*     */       
/* 176 */       TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */       
/* 178 */       if (tileentity instanceof TileEntityFurnace)
/*     */       {
/* 180 */         ((TileEntityFurnace)tileentity).setCustomInventoryName(stack.getDisplayName());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
/* 187 */     if (!keepInventory) {
/*     */       
/* 189 */       TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */       
/* 191 */       if (tileentity instanceof TileEntityFurnace) {
/*     */         
/* 193 */         InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
/* 194 */         worldIn.updateComparatorOutputLevel(pos, this);
/*     */       } 
/*     */     } 
/*     */     
/* 198 */     super.breakBlock(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasComparatorInputOverride() {
/* 203 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getComparatorInputOverride(World worldIn, BlockPos pos) {
/* 208 */     return Container.calcRedstone(worldIn.getTileEntity(pos));
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos) {
/* 213 */     return Item.getItemFromBlock(Blocks.furnace);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRenderType() {
/* 218 */     return 3;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateForEntityRender(IBlockState state) {
/* 223 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.SOUTH);
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 228 */     EnumFacing enumfacing = EnumFacing.getFront(meta);
/*     */     
/* 230 */     if (enumfacing.getAxis() == EnumFacing.Axis.Y)
/*     */     {
/* 232 */       enumfacing = EnumFacing.NORTH;
/*     */     }
/*     */     
/* 235 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)enumfacing);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 240 */     return ((EnumFacing)state.getValue((IProperty)FACING)).getIndex();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 245 */     return new BlockState(this, new IProperty[] { (IProperty)FACING });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockFurnace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */