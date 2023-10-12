/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.util.StatCollector;
/*     */ 
/*     */ public class BlockStone
/*     */   extends Block {
/*  20 */   public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
/*     */ 
/*     */   
/*     */   public BlockStone() {
/*  24 */     super(Material.rock);
/*  25 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)VARIANT, EnumType.STONE));
/*  26 */     setCreativeTab(CreativeTabs.tabBlock);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLocalizedName() {
/*  31 */     return StatCollector.translateToLocal(getUnlocalizedName() + "." + EnumType.STONE.getUnlocalizedName() + ".name");
/*     */   }
/*     */ 
/*     */   
/*     */   public MapColor getMapColor(IBlockState state) {
/*  36 */     return ((EnumType)state.getValue((IProperty)VARIANT)).func_181072_c();
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/*  41 */     return (state.getValue((IProperty)VARIANT) == EnumType.STONE) ? Item.getItemFromBlock(Blocks.cobblestone) : Item.getItemFromBlock(Blocks.stone);
/*     */   }
/*     */ 
/*     */   
/*     */   public int damageDropped(IBlockState state) {
/*  46 */     return ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
/*     */   }
/*     */ 
/*     */   
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
/*  51 */     for (EnumType blockstone$enumtype : EnumType.values())
/*     */     {
/*  53 */       list.add(new ItemStack(itemIn, 1, blockstone$enumtype.getMetadata()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/*  59 */     return getDefaultState().withProperty((IProperty)VARIANT, EnumType.byMetadata(meta));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/*  64 */     return ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/*  69 */     return new BlockState(this, new IProperty[] { (IProperty)VARIANT });
/*     */   }
/*     */   
/*     */   public enum EnumType
/*     */     implements IStringSerializable {
/*  74 */     STONE(0, MapColor.stoneColor, "stone"),
/*  75 */     GRANITE(1, MapColor.dirtColor, "granite"),
/*  76 */     GRANITE_SMOOTH(2, MapColor.dirtColor, "smooth_granite", (MapColor)"graniteSmooth"),
/*  77 */     DIORITE(3, MapColor.quartzColor, "diorite"),
/*  78 */     DIORITE_SMOOTH(4, MapColor.quartzColor, "smooth_diorite", (MapColor)"dioriteSmooth"),
/*  79 */     ANDESITE(5, MapColor.stoneColor, "andesite"),
/*  80 */     ANDESITE_SMOOTH(6, MapColor.stoneColor, "smooth_andesite", (MapColor)"andesiteSmooth");
/*     */     
/*  82 */     private static final EnumType[] META_LOOKUP = new EnumType[(values()).length];
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
/*     */     private final MapColor field_181073_l;
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
/* 137 */       for (EnumType blockstone$enumtype : values())
/*     */       {
/* 139 */         META_LOOKUP[blockstone$enumtype.getMetadata()] = blockstone$enumtype;
/*     */       }
/*     */     }
/*     */     
/*     */     EnumType(int p_i46384_3_, MapColor p_i46384_4_, String p_i46384_5_, String p_i46384_6_) {
/*     */       this.meta = p_i46384_3_;
/*     */       this.name = p_i46384_5_;
/*     */       this.unlocalizedName = p_i46384_6_;
/*     */       this.field_181073_l = p_i46384_4_;
/*     */     }
/*     */     
/*     */     public int getMetadata() {
/*     */       return this.meta;
/*     */     }
/*     */     
/*     */     public MapColor func_181072_c() {
/*     */       return this.field_181073_l;
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


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockStone.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */