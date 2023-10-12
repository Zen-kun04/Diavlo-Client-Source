/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockPane
/*     */   extends Block {
/*  23 */   public static final PropertyBool NORTH = PropertyBool.create("north");
/*  24 */   public static final PropertyBool EAST = PropertyBool.create("east");
/*  25 */   public static final PropertyBool SOUTH = PropertyBool.create("south");
/*  26 */   public static final PropertyBool WEST = PropertyBool.create("west");
/*     */   
/*     */   private final boolean canDrop;
/*     */   
/*     */   protected BlockPane(Material materialIn, boolean canDrop) {
/*  31 */     super(materialIn);
/*  32 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)NORTH, Boolean.valueOf(false)).withProperty((IProperty)EAST, Boolean.valueOf(false)).withProperty((IProperty)SOUTH, Boolean.valueOf(false)).withProperty((IProperty)WEST, Boolean.valueOf(false)));
/*  33 */     this.canDrop = canDrop;
/*  34 */     setCreativeTab(CreativeTabs.tabDecorations);
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
/*  39 */     return state.withProperty((IProperty)NORTH, Boolean.valueOf(canPaneConnectToBlock(worldIn.getBlockState(pos.north()).getBlock()))).withProperty((IProperty)SOUTH, Boolean.valueOf(canPaneConnectToBlock(worldIn.getBlockState(pos.south()).getBlock()))).withProperty((IProperty)WEST, Boolean.valueOf(canPaneConnectToBlock(worldIn.getBlockState(pos.west()).getBlock()))).withProperty((IProperty)EAST, Boolean.valueOf(canPaneConnectToBlock(worldIn.getBlockState(pos.east()).getBlock())));
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/*  44 */     return !this.canDrop ? null : super.getItemDropped(state, rand, fortune);
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
/*     */   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
/*  59 */     return (worldIn.getBlockState(pos).getBlock() == this) ? false : super.shouldSideBeRendered(worldIn, pos, side);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
/*  64 */     boolean flag = canPaneConnectToBlock(worldIn.getBlockState(pos.north()).getBlock());
/*  65 */     boolean flag1 = canPaneConnectToBlock(worldIn.getBlockState(pos.south()).getBlock());
/*  66 */     boolean flag2 = canPaneConnectToBlock(worldIn.getBlockState(pos.west()).getBlock());
/*  67 */     boolean flag3 = canPaneConnectToBlock(worldIn.getBlockState(pos.east()).getBlock());
/*     */     
/*  69 */     if ((!flag2 || !flag3) && (flag2 || flag3 || flag || flag1)) {
/*     */       
/*  71 */       if (flag2)
/*     */       {
/*  73 */         setBlockBounds(0.0F, 0.0F, 0.4375F, 0.5F, 1.0F, 0.5625F);
/*  74 */         super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*     */       }
/*  76 */       else if (flag3)
/*     */       {
/*  78 */         setBlockBounds(0.5F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
/*  79 */         super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/*  84 */       setBlockBounds(0.0F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
/*  85 */       super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*     */     } 
/*     */     
/*  88 */     if ((!flag || !flag1) && (flag2 || flag3 || flag || flag1)) {
/*     */       
/*  90 */       if (flag)
/*     */       {
/*  92 */         setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 0.5F);
/*  93 */         super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*     */       }
/*  95 */       else if (flag1)
/*     */       {
/*  97 */         setBlockBounds(0.4375F, 0.0F, 0.5F, 0.5625F, 1.0F, 1.0F);
/*  98 */         super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 103 */       setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 1.0F);
/* 104 */       super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockBoundsForItemRender() {
/* 110 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
/* 115 */     float f = 0.4375F;
/* 116 */     float f1 = 0.5625F;
/* 117 */     float f2 = 0.4375F;
/* 118 */     float f3 = 0.5625F;
/* 119 */     boolean flag = canPaneConnectToBlock(worldIn.getBlockState(pos.north()).getBlock());
/* 120 */     boolean flag1 = canPaneConnectToBlock(worldIn.getBlockState(pos.south()).getBlock());
/* 121 */     boolean flag2 = canPaneConnectToBlock(worldIn.getBlockState(pos.west()).getBlock());
/* 122 */     boolean flag3 = canPaneConnectToBlock(worldIn.getBlockState(pos.east()).getBlock());
/*     */     
/* 124 */     if ((!flag2 || !flag3) && (flag2 || flag3 || flag || flag1)) {
/*     */       
/* 126 */       if (flag2)
/*     */       {
/* 128 */         f = 0.0F;
/*     */       }
/* 130 */       else if (flag3)
/*     */       {
/* 132 */         f1 = 1.0F;
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 137 */       f = 0.0F;
/* 138 */       f1 = 1.0F;
/*     */     } 
/*     */     
/* 141 */     if ((!flag || !flag1) && (flag2 || flag3 || flag || flag1)) {
/*     */       
/* 143 */       if (flag)
/*     */       {
/* 145 */         f2 = 0.0F;
/*     */       }
/* 147 */       else if (flag1)
/*     */       {
/* 149 */         f3 = 1.0F;
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 154 */       f2 = 0.0F;
/* 155 */       f3 = 1.0F;
/*     */     } 
/*     */     
/* 158 */     setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean canPaneConnectToBlock(Block blockIn) {
/* 163 */     return (blockIn.isFullBlock() || blockIn == this || blockIn == Blocks.glass || blockIn == Blocks.stained_glass || blockIn == Blocks.stained_glass_pane || blockIn instanceof BlockPane);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSilkHarvest() {
/* 168 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer() {
/* 173 */     return EnumWorldBlockLayer.CUTOUT_MIPPED;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 178 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 183 */     return new BlockState(this, new IProperty[] { (IProperty)NORTH, (IProperty)EAST, (IProperty)WEST, (IProperty)SOUTH });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockPane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */