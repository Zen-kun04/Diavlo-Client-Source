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
/*     */ 
/*     */ public class BlockSandStone
/*     */   extends Block {
/*  17 */   public static final PropertyEnum<EnumType> TYPE = PropertyEnum.create("type", EnumType.class);
/*     */ 
/*     */   
/*     */   public BlockSandStone() {
/*  21 */     super(Material.rock);
/*  22 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)TYPE, EnumType.DEFAULT));
/*  23 */     setCreativeTab(CreativeTabs.tabBlock);
/*     */   }
/*     */ 
/*     */   
/*     */   public int damageDropped(IBlockState state) {
/*  28 */     return ((EnumType)state.getValue((IProperty)TYPE)).getMetadata();
/*     */   }
/*     */ 
/*     */   
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
/*  33 */     for (EnumType blocksandstone$enumtype : EnumType.values())
/*     */     {
/*  35 */       list.add(new ItemStack(itemIn, 1, blocksandstone$enumtype.getMetadata()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public MapColor getMapColor(IBlockState state) {
/*  41 */     return MapColor.sandColor;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/*  46 */     return getDefaultState().withProperty((IProperty)TYPE, EnumType.byMetadata(meta));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/*  51 */     return ((EnumType)state.getValue((IProperty)TYPE)).getMetadata();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/*  56 */     return new BlockState(this, new IProperty[] { (IProperty)TYPE });
/*     */   }
/*     */   
/*     */   public enum EnumType
/*     */     implements IStringSerializable {
/*  61 */     DEFAULT(0, "sandstone", "default"),
/*  62 */     CHISELED(1, "chiseled_sandstone", "chiseled"),
/*  63 */     SMOOTH(2, "smooth_sandstone", "smooth");
/*     */     
/*  65 */     private static final EnumType[] META_LOOKUP = new EnumType[(values()).length];
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
/*     */     private final int metadata;
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
/* 108 */       for (EnumType blocksandstone$enumtype : values())
/*     */       {
/* 110 */         META_LOOKUP[blocksandstone$enumtype.getMetadata()] = blocksandstone$enumtype;
/*     */       }
/*     */     }
/*     */     
/*     */     EnumType(int meta, String name, String unlocalizedName) {
/*     */       this.metadata = meta;
/*     */       this.name = name;
/*     */       this.unlocalizedName = unlocalizedName;
/*     */     }
/*     */     
/*     */     public int getMetadata() {
/*     */       return this.metadata;
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


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockSandStone.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */