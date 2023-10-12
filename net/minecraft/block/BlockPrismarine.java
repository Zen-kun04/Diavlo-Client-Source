/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.util.StatCollector;
/*     */ 
/*     */ public class BlockPrismarine
/*     */   extends Block {
/*  18 */   public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
/*  19 */   public static final int ROUGH_META = EnumType.ROUGH.getMetadata();
/*  20 */   public static final int BRICKS_META = EnumType.BRICKS.getMetadata();
/*  21 */   public static final int DARK_META = EnumType.DARK.getMetadata();
/*     */ 
/*     */   
/*     */   public BlockPrismarine() {
/*  25 */     super(Material.rock);
/*  26 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)VARIANT, EnumType.ROUGH));
/*  27 */     setCreativeTab(CreativeTabs.tabBlock);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLocalizedName() {
/*  32 */     return StatCollector.translateToLocal(getUnlocalizedName() + "." + EnumType.ROUGH.getUnlocalizedName() + ".name");
/*     */   }
/*     */ 
/*     */   
/*     */   public MapColor getMapColor(IBlockState state) {
/*  37 */     return (state.getValue((IProperty)VARIANT) == EnumType.ROUGH) ? MapColor.cyanColor : MapColor.diamondColor;
/*     */   }
/*     */ 
/*     */   
/*     */   public int damageDropped(IBlockState state) {
/*  42 */     return ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/*  47 */     return ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/*  52 */     return new BlockState(this, new IProperty[] { (IProperty)VARIANT });
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/*  57 */     return getDefaultState().withProperty((IProperty)VARIANT, EnumType.byMetadata(meta));
/*     */   }
/*     */ 
/*     */   
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
/*  62 */     list.add(new ItemStack(itemIn, 1, ROUGH_META));
/*  63 */     list.add(new ItemStack(itemIn, 1, BRICKS_META));
/*  64 */     list.add(new ItemStack(itemIn, 1, DARK_META));
/*     */   }
/*     */   
/*     */   public enum EnumType
/*     */     implements IStringSerializable {
/*  69 */     ROUGH(0, "prismarine", "rough"),
/*  70 */     BRICKS(1, "prismarine_bricks", "bricks"),
/*  71 */     DARK(2, "dark_prismarine", "dark");
/*     */     
/*  73 */     private static final EnumType[] META_LOOKUP = new EnumType[(values()).length];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int meta;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String unlocalizedName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/* 116 */       for (EnumType blockprismarine$enumtype : values())
/*     */       {
/* 118 */         META_LOOKUP[blockprismarine$enumtype.getMetadata()] = blockprismarine$enumtype;
/*     */       }
/*     */     }
/*     */     
/*     */     EnumType(int meta, String name, String unlocalizedName) {
/*     */       this.meta = meta;
/*     */       this.name = name;
/*     */       this.unlocalizedName = unlocalizedName;
/*     */     }
/*     */     
/*     */     public int getMetadata() {
/*     */       return this.meta;
/*     */     }
/*     */     
/*     */     public String toString() {
/*     */       return this.name;
/*     */     }
/*     */     
/*     */     public static EnumType byMetadata(int meta) {
/*     */       if (meta < 0 || meta >= META_LOOKUP.length)
/*     */         meta = 0; 
/*     */       return META_LOOKUP[meta];
/*     */     }
/*     */     
/*     */     public String getName() {
/*     */       return this.name;
/*     */     }
/*     */     
/*     */     public String getUnlocalizedName() {
/*     */       return this.unlocalizedName;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockPrismarine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */