/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemLead;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockFence
/*     */   extends Block {
/*  23 */   public static final PropertyBool NORTH = PropertyBool.create("north");
/*  24 */   public static final PropertyBool EAST = PropertyBool.create("east");
/*  25 */   public static final PropertyBool SOUTH = PropertyBool.create("south");
/*  26 */   public static final PropertyBool WEST = PropertyBool.create("west");
/*     */ 
/*     */   
/*     */   public BlockFence(Material materialIn) {
/*  30 */     this(materialIn, materialIn.getMaterialMapColor());
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockFence(Material p_i46395_1_, MapColor p_i46395_2_) {
/*  35 */     super(p_i46395_1_, p_i46395_2_);
/*  36 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)NORTH, Boolean.valueOf(false)).withProperty((IProperty)EAST, Boolean.valueOf(false)).withProperty((IProperty)SOUTH, Boolean.valueOf(false)).withProperty((IProperty)WEST, Boolean.valueOf(false)));
/*  37 */     setCreativeTab(CreativeTabs.tabDecorations);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
/*  42 */     boolean flag = canConnectTo((IBlockAccess)worldIn, pos.north());
/*  43 */     boolean flag1 = canConnectTo((IBlockAccess)worldIn, pos.south());
/*  44 */     boolean flag2 = canConnectTo((IBlockAccess)worldIn, pos.west());
/*  45 */     boolean flag3 = canConnectTo((IBlockAccess)worldIn, pos.east());
/*  46 */     float f = 0.375F;
/*  47 */     float f1 = 0.625F;
/*  48 */     float f2 = 0.375F;
/*  49 */     float f3 = 0.625F;
/*     */     
/*  51 */     if (flag)
/*     */     {
/*  53 */       f2 = 0.0F;
/*     */     }
/*     */     
/*  56 */     if (flag1)
/*     */     {
/*  58 */       f3 = 1.0F;
/*     */     }
/*     */     
/*  61 */     if (flag || flag1) {
/*     */       
/*  63 */       setBlockBounds(f, 0.0F, f2, f1, 1.5F, f3);
/*  64 */       super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*     */     } 
/*     */     
/*  67 */     f2 = 0.375F;
/*  68 */     f3 = 0.625F;
/*     */     
/*  70 */     if (flag2)
/*     */     {
/*  72 */       f = 0.0F;
/*     */     }
/*     */     
/*  75 */     if (flag3)
/*     */     {
/*  77 */       f1 = 1.0F;
/*     */     }
/*     */     
/*  80 */     if (flag2 || flag3 || (!flag && !flag1)) {
/*     */       
/*  82 */       setBlockBounds(f, 0.0F, f2, f1, 1.5F, f3);
/*  83 */       super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*     */     } 
/*     */     
/*  86 */     if (flag)
/*     */     {
/*  88 */       f2 = 0.0F;
/*     */     }
/*     */     
/*  91 */     if (flag1)
/*     */     {
/*  93 */       f3 = 1.0F;
/*     */     }
/*     */     
/*  96 */     setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
/* 101 */     boolean flag = canConnectTo(worldIn, pos.north());
/* 102 */     boolean flag1 = canConnectTo(worldIn, pos.south());
/* 103 */     boolean flag2 = canConnectTo(worldIn, pos.west());
/* 104 */     boolean flag3 = canConnectTo(worldIn, pos.east());
/* 105 */     float f = 0.375F;
/* 106 */     float f1 = 0.625F;
/* 107 */     float f2 = 0.375F;
/* 108 */     float f3 = 0.625F;
/*     */     
/* 110 */     if (flag)
/*     */     {
/* 112 */       f2 = 0.0F;
/*     */     }
/*     */     
/* 115 */     if (flag1)
/*     */     {
/* 117 */       f3 = 1.0F;
/*     */     }
/*     */     
/* 120 */     if (flag2)
/*     */     {
/* 122 */       f = 0.0F;
/*     */     }
/*     */     
/* 125 */     if (flag3)
/*     */     {
/* 127 */       f1 = 1.0F;
/*     */     }
/*     */     
/* 130 */     setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/* 135 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/* 140 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
/* 145 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canConnectTo(IBlockAccess worldIn, BlockPos pos) {
/* 150 */     Block block = worldIn.getBlockState(pos).getBlock();
/* 151 */     return (block == Blocks.barrier) ? false : (((!(block instanceof BlockFence) || block.blockMaterial != this.blockMaterial) && !(block instanceof BlockFenceGate)) ? ((block.blockMaterial.isOpaque() && block.isFullCube()) ? ((block.blockMaterial != Material.gourd)) : false) : true);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
/* 156 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
/* 161 */     return worldIn.isRemote ? true : ItemLead.attachToFence(playerIn, worldIn, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 166 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
/* 171 */     return state.withProperty((IProperty)NORTH, Boolean.valueOf(canConnectTo(worldIn, pos.north()))).withProperty((IProperty)EAST, Boolean.valueOf(canConnectTo(worldIn, pos.east()))).withProperty((IProperty)SOUTH, Boolean.valueOf(canConnectTo(worldIn, pos.south()))).withProperty((IProperty)WEST, Boolean.valueOf(canConnectTo(worldIn, pos.west())));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 176 */     return new BlockState(this, new IProperty[] { (IProperty)NORTH, (IProperty)EAST, (IProperty)WEST, (IProperty)SOUTH });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockFence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */