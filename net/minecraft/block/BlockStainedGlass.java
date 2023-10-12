/*    */ package net.minecraft.block;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.material.MapColor;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.properties.IProperty;
/*    */ import net.minecraft.block.properties.PropertyEnum;
/*    */ import net.minecraft.block.state.BlockState;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.item.EnumDyeColor;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumWorldBlockLayer;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockStainedGlass
/*    */   extends BlockBreakable {
/* 21 */   public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);
/*    */ 
/*    */   
/*    */   public BlockStainedGlass(Material materialIn) {
/* 25 */     super(materialIn, false);
/* 26 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)COLOR, (Comparable)EnumDyeColor.WHITE));
/* 27 */     setCreativeTab(CreativeTabs.tabBlock);
/*    */   }
/*    */ 
/*    */   
/*    */   public int damageDropped(IBlockState state) {
/* 32 */     return ((EnumDyeColor)state.getValue((IProperty)COLOR)).getMetadata();
/*    */   }
/*    */ 
/*    */   
/*    */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
/* 37 */     for (EnumDyeColor enumdyecolor : EnumDyeColor.values())
/*    */     {
/* 39 */       list.add(new ItemStack(itemIn, 1, enumdyecolor.getMetadata()));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public MapColor getMapColor(IBlockState state) {
/* 45 */     return ((EnumDyeColor)state.getValue((IProperty)COLOR)).getMapColor();
/*    */   }
/*    */ 
/*    */   
/*    */   public EnumWorldBlockLayer getBlockLayer() {
/* 50 */     return EnumWorldBlockLayer.TRANSLUCENT;
/*    */   }
/*    */ 
/*    */   
/*    */   public int quantityDropped(Random random) {
/* 55 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canSilkHarvest() {
/* 60 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isFullCube() {
/* 65 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public IBlockState getStateFromMeta(int meta) {
/* 70 */     return getDefaultState().withProperty((IProperty)COLOR, (Comparable)EnumDyeColor.byMetadata(meta));
/*    */   }
/*    */ 
/*    */   
/*    */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
/* 75 */     if (!worldIn.isRemote)
/*    */     {
/* 77 */       BlockBeacon.updateColorAsync(worldIn, pos);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
/* 83 */     if (!worldIn.isRemote)
/*    */     {
/* 85 */       BlockBeacon.updateColorAsync(worldIn, pos);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMetaFromState(IBlockState state) {
/* 91 */     return ((EnumDyeColor)state.getValue((IProperty)COLOR)).getMetadata();
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState createBlockState() {
/* 96 */     return new BlockState(this, new IProperty[] { (IProperty)COLOR });
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockStainedGlass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */