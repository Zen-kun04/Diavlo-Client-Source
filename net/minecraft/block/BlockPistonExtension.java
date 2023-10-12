/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.properties.PropertyDirection;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockPistonExtension
/*     */   extends Block {
/*  25 */   public static final PropertyDirection FACING = PropertyDirection.create("facing");
/*  26 */   public static final PropertyEnum<EnumPistonType> TYPE = PropertyEnum.create("type", EnumPistonType.class);
/*  27 */   public static final PropertyBool SHORT = PropertyBool.create("short");
/*     */ 
/*     */   
/*     */   public BlockPistonExtension() {
/*  31 */     super(Material.piston);
/*  32 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)TYPE, EnumPistonType.DEFAULT).withProperty((IProperty)SHORT, Boolean.valueOf(false)));
/*  33 */     setStepSound(soundTypePiston);
/*  34 */     setHardness(0.5F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
/*  39 */     if (player.capabilities.isCreativeMode) {
/*     */       
/*  41 */       EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
/*     */       
/*  43 */       if (enumfacing != null) {
/*     */         
/*  45 */         BlockPos blockpos = pos.offset(enumfacing.getOpposite());
/*  46 */         Block block = worldIn.getBlockState(blockpos).getBlock();
/*     */         
/*  48 */         if (block == Blocks.piston || block == Blocks.sticky_piston)
/*     */         {
/*  50 */           worldIn.setBlockToAir(blockpos);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  55 */     super.onBlockHarvested(worldIn, pos, state, player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
/*  60 */     super.breakBlock(worldIn, pos, state);
/*  61 */     EnumFacing enumfacing = ((EnumFacing)state.getValue((IProperty)FACING)).getOpposite();
/*  62 */     pos = pos.offset(enumfacing);
/*  63 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */     
/*  65 */     if ((iblockstate.getBlock() == Blocks.piston || iblockstate.getBlock() == Blocks.sticky_piston) && ((Boolean)iblockstate.getValue((IProperty)BlockPistonBase.EXTENDED)).booleanValue()) {
/*     */       
/*  67 */       iblockstate.getBlock().dropBlockAsItem(worldIn, pos, iblockstate, 0);
/*  68 */       worldIn.setBlockToAir(pos);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/*  74 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/*  79 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
/*  84 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
/*  89 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int quantityDropped(Random random) {
/*  94 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
/*  99 */     applyHeadBounds(state);
/* 100 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/* 101 */     applyCoreBounds(state);
/* 102 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/* 103 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   private void applyCoreBounds(IBlockState state) {
/* 108 */     float f = 0.25F;
/* 109 */     float f1 = 0.375F;
/* 110 */     float f2 = 0.625F;
/* 111 */     float f3 = 0.25F;
/* 112 */     float f4 = 0.75F;
/*     */     
/* 114 */     switch ((EnumFacing)state.getValue((IProperty)FACING)) {
/*     */       
/*     */       case DOWN:
/* 117 */         setBlockBounds(0.375F, 0.25F, 0.375F, 0.625F, 1.0F, 0.625F);
/*     */         break;
/*     */       
/*     */       case UP:
/* 121 */         setBlockBounds(0.375F, 0.0F, 0.375F, 0.625F, 0.75F, 0.625F);
/*     */         break;
/*     */       
/*     */       case NORTH:
/* 125 */         setBlockBounds(0.25F, 0.375F, 0.25F, 0.75F, 0.625F, 1.0F);
/*     */         break;
/*     */       
/*     */       case SOUTH:
/* 129 */         setBlockBounds(0.25F, 0.375F, 0.0F, 0.75F, 0.625F, 0.75F);
/*     */         break;
/*     */       
/*     */       case WEST:
/* 133 */         setBlockBounds(0.375F, 0.25F, 0.25F, 0.625F, 0.75F, 1.0F);
/*     */         break;
/*     */       
/*     */       case EAST:
/* 137 */         setBlockBounds(0.0F, 0.375F, 0.25F, 0.75F, 0.625F, 0.75F);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
/* 143 */     applyHeadBounds(worldIn.getBlockState(pos));
/*     */   }
/*     */ 
/*     */   
/*     */   public void applyHeadBounds(IBlockState state) {
/* 148 */     float f = 0.25F;
/* 149 */     EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
/*     */     
/* 151 */     if (enumfacing != null)
/*     */     {
/* 153 */       switch (enumfacing) {
/*     */         
/*     */         case DOWN:
/* 156 */           setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
/*     */           break;
/*     */         
/*     */         case UP:
/* 160 */           setBlockBounds(0.0F, 0.75F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */           break;
/*     */         
/*     */         case NORTH:
/* 164 */           setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
/*     */           break;
/*     */         
/*     */         case SOUTH:
/* 168 */           setBlockBounds(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
/*     */           break;
/*     */         
/*     */         case WEST:
/* 172 */           setBlockBounds(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
/*     */           break;
/*     */         
/*     */         case EAST:
/* 176 */           setBlockBounds(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */           break;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/* 183 */     EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
/* 184 */     BlockPos blockpos = pos.offset(enumfacing.getOpposite());
/* 185 */     IBlockState iblockstate = worldIn.getBlockState(blockpos);
/*     */     
/* 187 */     if (iblockstate.getBlock() != Blocks.piston && iblockstate.getBlock() != Blocks.sticky_piston) {
/*     */       
/* 189 */       worldIn.setBlockToAir(pos);
/*     */     }
/*     */     else {
/*     */       
/* 193 */       iblockstate.getBlock().onNeighborBlockChange(worldIn, blockpos, iblockstate, neighborBlock);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
/* 199 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static EnumFacing getFacing(int meta) {
/* 204 */     int i = meta & 0x7;
/* 205 */     return (i > 5) ? null : EnumFacing.getFront(i);
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos) {
/* 210 */     return (worldIn.getBlockState(pos).getValue((IProperty)TYPE) == EnumPistonType.STICKY) ? Item.getItemFromBlock(Blocks.sticky_piston) : Item.getItemFromBlock(Blocks.piston);
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 215 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)getFacing(meta)).withProperty((IProperty)TYPE, ((meta & 0x8) > 0) ? EnumPistonType.STICKY : EnumPistonType.DEFAULT);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 220 */     int i = 0;
/* 221 */     i |= ((EnumFacing)state.getValue((IProperty)FACING)).getIndex();
/*     */     
/* 223 */     if (state.getValue((IProperty)TYPE) == EnumPistonType.STICKY)
/*     */     {
/* 225 */       i |= 0x8;
/*     */     }
/*     */     
/* 228 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 233 */     return new BlockState(this, new IProperty[] { (IProperty)FACING, (IProperty)TYPE, (IProperty)SHORT });
/*     */   }
/*     */   
/*     */   public enum EnumPistonType
/*     */     implements IStringSerializable {
/* 238 */     DEFAULT("normal"),
/* 239 */     STICKY("sticky");
/*     */     
/*     */     private final String VARIANT;
/*     */ 
/*     */     
/*     */     EnumPistonType(String name) {
/* 245 */       this.VARIANT = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 250 */       return this.VARIANT;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/* 255 */       return this.VARIANT;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockPistonExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */