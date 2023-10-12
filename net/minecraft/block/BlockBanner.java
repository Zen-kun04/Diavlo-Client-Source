/*     */ package net.minecraft.block;
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyDirection;
/*     */ import net.minecraft.block.properties.PropertyInteger;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityBanner;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.StatCollector;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockBanner extends BlockContainer {
/*  26 */   public static final PropertyDirection FACING = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
/*  27 */   public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);
/*     */ 
/*     */   
/*     */   protected BlockBanner() {
/*  31 */     super(Material.wood);
/*  32 */     float f = 0.25F;
/*  33 */     float f1 = 1.0F;
/*  34 */     setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLocalizedName() {
/*  39 */     return StatCollector.translateToLocal("item.banner.white.name");
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
/*  44 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
/*  49 */     setBlockBoundsBasedOnState((IBlockAccess)worldIn, pos);
/*  50 */     return super.getSelectedBoundingBox(worldIn, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/*  55 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
/*  60 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/*  65 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSpawnInBlock() {
/*  70 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public TileEntity createNewTileEntity(World worldIn, int meta) {
/*  75 */     return (TileEntity)new TileEntityBanner();
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/*  80 */     return Items.banner;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos) {
/*  85 */     return Items.banner;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
/*  90 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/*  92 */     if (tileentity instanceof TileEntityBanner) {
/*     */       
/*  94 */       ItemStack itemstack = new ItemStack(Items.banner, 1, ((TileEntityBanner)tileentity).getBaseColor());
/*  95 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  96 */       tileentity.writeToNBT(nbttagcompound);
/*  97 */       nbttagcompound.removeTag("x");
/*  98 */       nbttagcompound.removeTag("y");
/*  99 */       nbttagcompound.removeTag("z");
/* 100 */       nbttagcompound.removeTag("id");
/* 101 */       itemstack.setTagInfo("BlockEntityTag", (NBTBase)nbttagcompound);
/* 102 */       spawnAsEntity(worldIn, pos, itemstack);
/*     */     }
/*     */     else {
/*     */       
/* 106 */       super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
/* 112 */     return (!hasInvalidNeighbor(worldIn, pos) && super.canPlaceBlockAt(worldIn, pos));
/*     */   }
/*     */ 
/*     */   
/*     */   public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
/* 117 */     if (te instanceof TileEntityBanner) {
/*     */       
/* 119 */       TileEntityBanner tileentitybanner = (TileEntityBanner)te;
/* 120 */       ItemStack itemstack = new ItemStack(Items.banner, 1, ((TileEntityBanner)te).getBaseColor());
/* 121 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 122 */       TileEntityBanner.setBaseColorAndPatterns(nbttagcompound, tileentitybanner.getBaseColor(), tileentitybanner.getPatterns());
/* 123 */       itemstack.setTagInfo("BlockEntityTag", (NBTBase)nbttagcompound);
/* 124 */       spawnAsEntity(worldIn, pos, itemstack);
/*     */     }
/*     */     else {
/*     */       
/* 128 */       super.harvestBlock(worldIn, player, pos, state, (TileEntity)null);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static class BlockBannerHanging
/*     */     extends BlockBanner
/*     */   {
/*     */     public BlockBannerHanging() {
/* 136 */       setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH));
/*     */     }
/*     */ 
/*     */     
/*     */     public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
/* 141 */       EnumFacing enumfacing = (EnumFacing)worldIn.getBlockState(pos).getValue((IProperty)FACING);
/* 142 */       float f = 0.0F;
/* 143 */       float f1 = 0.78125F;
/* 144 */       float f2 = 0.0F;
/* 145 */       float f3 = 1.0F;
/* 146 */       float f4 = 0.125F;
/* 147 */       setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */       
/* 149 */       switch (enumfacing) {
/*     */ 
/*     */         
/*     */         default:
/* 153 */           setBlockBounds(f2, f, 1.0F - f4, f3, f1, 1.0F);
/*     */           return;
/*     */         
/*     */         case SOUTH:
/* 157 */           setBlockBounds(f2, f, 0.0F, f3, f1, f4);
/*     */           return;
/*     */         
/*     */         case WEST:
/* 161 */           setBlockBounds(1.0F - f4, f, f2, 1.0F, f1, f3); return;
/*     */         case EAST:
/*     */           break;
/*     */       } 
/* 165 */       setBlockBounds(0.0F, f, f2, f4, f1, f3);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/* 171 */       EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
/*     */       
/* 173 */       if (!worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock().getMaterial().isSolid()) {
/*     */         
/* 175 */         dropBlockAsItem(worldIn, pos, state, 0);
/* 176 */         worldIn.setBlockToAir(pos);
/*     */       } 
/*     */       
/* 179 */       super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
/*     */     }
/*     */ 
/*     */     
/*     */     public IBlockState getStateFromMeta(int meta) {
/* 184 */       EnumFacing enumfacing = EnumFacing.getFront(meta);
/*     */       
/* 186 */       if (enumfacing.getAxis() == EnumFacing.Axis.Y)
/*     */       {
/* 188 */         enumfacing = EnumFacing.NORTH;
/*     */       }
/*     */       
/* 191 */       return getDefaultState().withProperty((IProperty)FACING, (Comparable)enumfacing);
/*     */     }
/*     */ 
/*     */     
/*     */     public int getMetaFromState(IBlockState state) {
/* 196 */       return ((EnumFacing)state.getValue((IProperty)FACING)).getIndex();
/*     */     }
/*     */ 
/*     */     
/*     */     protected BlockState createBlockState() {
/* 201 */       return new BlockState(this, new IProperty[] { (IProperty)FACING });
/*     */     }
/*     */   }
/*     */   
/*     */   public static class BlockBannerStanding
/*     */     extends BlockBanner
/*     */   {
/*     */     public BlockBannerStanding() {
/* 209 */       setDefaultState(this.blockState.getBaseState().withProperty((IProperty)ROTATION, Integer.valueOf(0)));
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/* 214 */       if (!worldIn.getBlockState(pos.down()).getBlock().getMaterial().isSolid()) {
/*     */         
/* 216 */         dropBlockAsItem(worldIn, pos, state, 0);
/* 217 */         worldIn.setBlockToAir(pos);
/*     */       } 
/*     */       
/* 220 */       super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
/*     */     }
/*     */ 
/*     */     
/*     */     public IBlockState getStateFromMeta(int meta) {
/* 225 */       return getDefaultState().withProperty((IProperty)ROTATION, Integer.valueOf(meta));
/*     */     }
/*     */ 
/*     */     
/*     */     public int getMetaFromState(IBlockState state) {
/* 230 */       return ((Integer)state.getValue((IProperty)ROTATION)).intValue();
/*     */     }
/*     */ 
/*     */     
/*     */     protected BlockState createBlockState() {
/* 235 */       return new BlockState(this, new IProperty[] { (IProperty)ROTATION });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockBanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */