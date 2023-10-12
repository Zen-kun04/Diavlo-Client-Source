/*    */ package net.minecraft.block;
/*    */ 
/*    */ import java.util.List;
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
/*    */ 
/*    */ public class BlockColored
/*    */   extends Block {
/* 17 */   public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);
/*    */ 
/*    */   
/*    */   public BlockColored(Material materialIn) {
/* 21 */     super(materialIn);
/* 22 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)COLOR, (Comparable)EnumDyeColor.WHITE));
/* 23 */     setCreativeTab(CreativeTabs.tabBlock);
/*    */   }
/*    */ 
/*    */   
/*    */   public int damageDropped(IBlockState state) {
/* 28 */     return ((EnumDyeColor)state.getValue((IProperty)COLOR)).getMetadata();
/*    */   }
/*    */ 
/*    */   
/*    */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
/* 33 */     for (EnumDyeColor enumdyecolor : EnumDyeColor.values())
/*    */     {
/* 35 */       list.add(new ItemStack(itemIn, 1, enumdyecolor.getMetadata()));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public MapColor getMapColor(IBlockState state) {
/* 41 */     return ((EnumDyeColor)state.getValue((IProperty)COLOR)).getMapColor();
/*    */   }
/*    */ 
/*    */   
/*    */   public IBlockState getStateFromMeta(int meta) {
/* 46 */     return getDefaultState().withProperty((IProperty)COLOR, (Comparable)EnumDyeColor.byMetadata(meta));
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMetaFromState(IBlockState state) {
/* 51 */     return ((EnumDyeColor)state.getValue((IProperty)COLOR)).getMetadata();
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState createBlockState() {
/* 56 */     return new BlockState(this, new IProperty[] { (IProperty)COLOR });
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockColored.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */