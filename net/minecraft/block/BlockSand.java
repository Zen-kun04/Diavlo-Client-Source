/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ 
/*     */ public class BlockSand
/*     */   extends BlockFalling {
/*  16 */   public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
/*     */ 
/*     */   
/*     */   public BlockSand() {
/*  20 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)VARIANT, EnumType.SAND));
/*     */   }
/*     */ 
/*     */   
/*     */   public int damageDropped(IBlockState state) {
/*  25 */     return ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
/*     */   }
/*     */ 
/*     */   
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
/*  30 */     for (EnumType blocksand$enumtype : EnumType.values())
/*     */     {
/*  32 */       list.add(new ItemStack(itemIn, 1, blocksand$enumtype.getMetadata()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public MapColor getMapColor(IBlockState state) {
/*  38 */     return ((EnumType)state.getValue((IProperty)VARIANT)).getMapColor();
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/*  43 */     return getDefaultState().withProperty((IProperty)VARIANT, EnumType.byMetadata(meta));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/*  48 */     return ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/*  53 */     return new BlockState(this, new IProperty[] { (IProperty)VARIANT });
/*     */   }
/*     */   
/*     */   public enum EnumType
/*     */     implements IStringSerializable {
/*  58 */     SAND(0, "sand", "default", (String)MapColor.sandColor),
/*  59 */     RED_SAND(1, "red_sand", "red", (String)MapColor.adobeColor);
/*     */     
/*  61 */     private static final EnumType[] META_LOOKUP = new EnumType[(values()).length];
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
/*     */     private final MapColor mapColor;
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
/*     */     static {
/* 111 */       for (EnumType blocksand$enumtype : values())
/*     */       {
/* 113 */         META_LOOKUP[blocksand$enumtype.getMetadata()] = blocksand$enumtype;
/*     */       }
/*     */     }
/*     */     
/*     */     EnumType(int meta, String name, String unlocalizedName, MapColor mapColor) {
/*     */       this.meta = meta;
/*     */       this.name = name;
/*     */       this.mapColor = mapColor;
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
/*     */     public MapColor getMapColor() {
/*     */       return this.mapColor;
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


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockSand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */