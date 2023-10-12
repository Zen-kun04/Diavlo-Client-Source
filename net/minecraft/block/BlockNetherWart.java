/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyInteger;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockNetherWart
/*     */   extends BlockBush {
/*  20 */   public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 3);
/*     */ 
/*     */   
/*     */   protected BlockNetherWart() {
/*  24 */     super(Material.plants, MapColor.redColor);
/*  25 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)AGE, Integer.valueOf(0)));
/*  26 */     setTickRandomly(true);
/*  27 */     float f = 0.5F;
/*  28 */     setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
/*  29 */     setCreativeTab((CreativeTabs)null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canPlaceBlockOn(Block ground) {
/*  34 */     return (ground == Blocks.soul_sand);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
/*  39 */     return canPlaceBlockOn(worldIn.getBlockState(pos.down()).getBlock());
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/*  44 */     int i = ((Integer)state.getValue((IProperty)AGE)).intValue();
/*     */     
/*  46 */     if (i < 3 && rand.nextInt(10) == 0) {
/*     */       
/*  48 */       state = state.withProperty((IProperty)AGE, Integer.valueOf(i + 1));
/*  49 */       worldIn.setBlockState(pos, state, 2);
/*     */     } 
/*     */     
/*  52 */     super.updateTick(worldIn, pos, state, rand);
/*     */   }
/*     */ 
/*     */   
/*     */   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
/*  57 */     if (!worldIn.isRemote) {
/*     */       
/*  59 */       int i = 1;
/*     */       
/*  61 */       if (((Integer)state.getValue((IProperty)AGE)).intValue() >= 3) {
/*     */         
/*  63 */         i = 2 + worldIn.rand.nextInt(3);
/*     */         
/*  65 */         if (fortune > 0)
/*     */         {
/*  67 */           i += worldIn.rand.nextInt(fortune + 1);
/*     */         }
/*     */       } 
/*     */       
/*  71 */       for (int j = 0; j < i; j++)
/*     */       {
/*  73 */         spawnAsEntity(worldIn, pos, new ItemStack(Items.nether_wart));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/*  80 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int quantityDropped(Random random) {
/*  85 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos) {
/*  90 */     return Items.nether_wart;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/*  95 */     return getDefaultState().withProperty((IProperty)AGE, Integer.valueOf(meta));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 100 */     return ((Integer)state.getValue((IProperty)AGE)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 105 */     return new BlockState(this, new IProperty[] { (IProperty)AGE });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockNetherWart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */