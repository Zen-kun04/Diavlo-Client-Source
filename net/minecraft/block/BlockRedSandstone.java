/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
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
/*     */ public class BlockRedSandstone
/*     */   extends Block {
/*  16 */   public static final PropertyEnum<EnumType> TYPE = PropertyEnum.create("type", EnumType.class);
/*     */ 
/*     */   
/*     */   public BlockRedSandstone() {
/*  20 */     super(Material.rock, BlockSand.EnumType.RED_SAND.getMapColor());
/*  21 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)TYPE, EnumType.DEFAULT));
/*  22 */     setCreativeTab(CreativeTabs.tabBlock);
/*     */   }
/*     */ 
/*     */   
/*     */   public int damageDropped(IBlockState state) {
/*  27 */     return ((EnumType)state.getValue((IProperty)TYPE)).getMetadata();
/*     */   }
/*     */ 
/*     */   
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
/*  32 */     for (EnumType blockredsandstone$enumtype : EnumType.values())
/*     */     {
/*  34 */       list.add(new ItemStack(itemIn, 1, blockredsandstone$enumtype.getMetadata()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/*  40 */     return getDefaultState().withProperty((IProperty)TYPE, EnumType.byMetadata(meta));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/*  45 */     return ((EnumType)state.getValue((IProperty)TYPE)).getMetadata();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/*  50 */     return new BlockState(this, new IProperty[] { (IProperty)TYPE });
/*     */   }
/*     */   
/*     */   public enum EnumType
/*     */     implements IStringSerializable {
/*  55 */     DEFAULT(0, "red_sandstone", "default"),
/*  56 */     CHISELED(1, "chiseled_red_sandstone", "chiseled"),
/*  57 */     SMOOTH(2, "smooth_red_sandstone", "smooth");
/*     */     
/*  59 */     private static final EnumType[] META_LOOKUP = new EnumType[(values()).length];
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
/* 102 */       for (EnumType blockredsandstone$enumtype : values())
/*     */       {
/* 104 */         META_LOOKUP[blockredsandstone$enumtype.getMetadata()] = blockredsandstone$enumtype;
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


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockRedSandstone.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */