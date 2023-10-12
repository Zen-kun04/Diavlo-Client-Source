/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.properties.PropertyInteger;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.StatCollector;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockRedstoneRepeater
/*     */   extends BlockRedstoneDiode {
/*  22 */   public static final PropertyBool LOCKED = PropertyBool.create("locked");
/*  23 */   public static final PropertyInteger DELAY = PropertyInteger.create("delay", 1, 4);
/*     */ 
/*     */   
/*     */   protected BlockRedstoneRepeater(boolean powered) {
/*  27 */     super(powered);
/*  28 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)DELAY, Integer.valueOf(1)).withProperty((IProperty)LOCKED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLocalizedName() {
/*  33 */     return StatCollector.translateToLocal("item.diode.name");
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
/*  38 */     return state.withProperty((IProperty)LOCKED, Boolean.valueOf(isLocked(worldIn, pos, state)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
/*  43 */     if (!playerIn.capabilities.allowEdit)
/*     */     {
/*  45 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  49 */     worldIn.setBlockState(pos, state.cycleProperty((IProperty)DELAY), 3);
/*  50 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getDelay(IBlockState state) {
/*  56 */     return ((Integer)state.getValue((IProperty)DELAY)).intValue() * 2;
/*     */   }
/*     */ 
/*     */   
/*     */   protected IBlockState getPoweredState(IBlockState unpoweredState) {
/*  61 */     Integer integer = (Integer)unpoweredState.getValue((IProperty)DELAY);
/*  62 */     Boolean obool = (Boolean)unpoweredState.getValue((IProperty)LOCKED);
/*  63 */     EnumFacing enumfacing = (EnumFacing)unpoweredState.getValue((IProperty)FACING);
/*  64 */     return Blocks.powered_repeater.getDefaultState().withProperty((IProperty)FACING, (Comparable)enumfacing).withProperty((IProperty)DELAY, integer).withProperty((IProperty)LOCKED, obool);
/*     */   }
/*     */ 
/*     */   
/*     */   protected IBlockState getUnpoweredState(IBlockState poweredState) {
/*  69 */     Integer integer = (Integer)poweredState.getValue((IProperty)DELAY);
/*  70 */     Boolean obool = (Boolean)poweredState.getValue((IProperty)LOCKED);
/*  71 */     EnumFacing enumfacing = (EnumFacing)poweredState.getValue((IProperty)FACING);
/*  72 */     return Blocks.unpowered_repeater.getDefaultState().withProperty((IProperty)FACING, (Comparable)enumfacing).withProperty((IProperty)DELAY, integer).withProperty((IProperty)LOCKED, obool);
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/*  77 */     return Items.repeater;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos) {
/*  82 */     return Items.repeater;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLocked(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
/*  87 */     return (getPowerOnSides(worldIn, pos, state) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canPowerSide(Block blockIn) {
/*  92 */     return isRedstoneRepeaterBlockID(blockIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/*  97 */     if (this.isRepeaterPowered) {
/*     */       
/*  99 */       EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
/* 100 */       double d0 = (pos.getX() + 0.5F) + (rand.nextFloat() - 0.5F) * 0.2D;
/* 101 */       double d1 = (pos.getY() + 0.4F) + (rand.nextFloat() - 0.5F) * 0.2D;
/* 102 */       double d2 = (pos.getZ() + 0.5F) + (rand.nextFloat() - 0.5F) * 0.2D;
/* 103 */       float f = -5.0F;
/*     */       
/* 105 */       if (rand.nextBoolean())
/*     */       {
/* 107 */         f = (((Integer)state.getValue((IProperty)DELAY)).intValue() * 2 - 1);
/*     */       }
/*     */       
/* 110 */       f /= 16.0F;
/* 111 */       double d3 = (f * enumfacing.getFrontOffsetX());
/* 112 */       double d4 = (f * enumfacing.getFrontOffsetZ());
/* 113 */       worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
/* 119 */     super.breakBlock(worldIn, pos, state);
/* 120 */     notifyNeighbors(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 125 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.getHorizontal(meta)).withProperty((IProperty)LOCKED, Boolean.valueOf(false)).withProperty((IProperty)DELAY, Integer.valueOf(1 + (meta >> 2)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 130 */     int i = 0;
/* 131 */     i |= ((EnumFacing)state.getValue((IProperty)FACING)).getHorizontalIndex();
/* 132 */     i |= ((Integer)state.getValue((IProperty)DELAY)).intValue() - 1 << 2;
/* 133 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 138 */     return new BlockState(this, new IProperty[] { (IProperty)FACING, (IProperty)DELAY, (IProperty)LOCKED });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockRedstoneRepeater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */