/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockDirt
/*     */   extends Block {
/*  22 */   public static final PropertyEnum<DirtType> VARIANT = PropertyEnum.create("variant", DirtType.class);
/*  23 */   public static final PropertyBool SNOWY = PropertyBool.create("snowy");
/*     */ 
/*     */   
/*     */   protected BlockDirt() {
/*  27 */     super(Material.ground);
/*  28 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)VARIANT, DirtType.DIRT).withProperty((IProperty)SNOWY, Boolean.valueOf(false)));
/*  29 */     setCreativeTab(CreativeTabs.tabBlock);
/*     */   }
/*     */ 
/*     */   
/*     */   public MapColor getMapColor(IBlockState state) {
/*  34 */     return ((DirtType)state.getValue((IProperty)VARIANT)).func_181066_d();
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
/*  39 */     if (state.getValue((IProperty)VARIANT) == DirtType.PODZOL) {
/*     */       
/*  41 */       Block block = worldIn.getBlockState(pos.up()).getBlock();
/*  42 */       state = state.withProperty((IProperty)SNOWY, Boolean.valueOf((block == Blocks.snow || block == Blocks.snow_layer)));
/*     */     } 
/*     */     
/*  45 */     return state;
/*     */   }
/*     */ 
/*     */   
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
/*  50 */     list.add(new ItemStack(this, 1, DirtType.DIRT.getMetadata()));
/*  51 */     list.add(new ItemStack(this, 1, DirtType.COARSE_DIRT.getMetadata()));
/*  52 */     list.add(new ItemStack(this, 1, DirtType.PODZOL.getMetadata()));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDamageValue(World worldIn, BlockPos pos) {
/*  57 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*  58 */     return (iblockstate.getBlock() != this) ? 0 : ((DirtType)iblockstate.getValue((IProperty)VARIANT)).getMetadata();
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/*  63 */     return getDefaultState().withProperty((IProperty)VARIANT, DirtType.byMetadata(meta));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/*  68 */     return ((DirtType)state.getValue((IProperty)VARIANT)).getMetadata();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/*  73 */     return new BlockState(this, new IProperty[] { (IProperty)VARIANT, (IProperty)SNOWY });
/*     */   }
/*     */ 
/*     */   
/*     */   public int damageDropped(IBlockState state) {
/*  78 */     DirtType blockdirt$dirttype = (DirtType)state.getValue((IProperty)VARIANT);
/*     */     
/*  80 */     if (blockdirt$dirttype == DirtType.PODZOL)
/*     */     {
/*  82 */       blockdirt$dirttype = DirtType.DIRT;
/*     */     }
/*     */     
/*  85 */     return blockdirt$dirttype.getMetadata();
/*     */   }
/*     */   
/*     */   public enum DirtType
/*     */     implements IStringSerializable {
/*  90 */     DIRT(0, "dirt", "default", (String)MapColor.dirtColor),
/*  91 */     COARSE_DIRT(1, "coarse_dirt", "coarse", (String)MapColor.dirtColor),
/*  92 */     PODZOL(2, "podzol", MapColor.obsidianColor);
/*     */     
/*  94 */     private static final DirtType[] METADATA_LOOKUP = new DirtType[(values()).length];
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
/*     */     private final MapColor field_181067_h;
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
/* 149 */       for (DirtType blockdirt$dirttype : values())
/*     */       {
/* 151 */         METADATA_LOOKUP[blockdirt$dirttype.getMetadata()] = blockdirt$dirttype;
/*     */       }
/*     */     }
/*     */     
/*     */     DirtType(int p_i46397_3_, String p_i46397_4_, String p_i46397_5_, MapColor p_i46397_6_) {
/*     */       this.metadata = p_i46397_3_;
/*     */       this.name = p_i46397_4_;
/*     */       this.unlocalizedName = p_i46397_5_;
/*     */       this.field_181067_h = p_i46397_6_;
/*     */     }
/*     */     
/*     */     public int getMetadata() {
/*     */       return this.metadata;
/*     */     }
/*     */     
/*     */     public String getUnlocalizedName() {
/*     */       return this.unlocalizedName;
/*     */     }
/*     */     
/*     */     public MapColor func_181066_d() {
/*     */       return this.field_181067_h;
/*     */     }
/*     */     
/*     */     public String toString() {
/*     */       return this.name;
/*     */     }
/*     */     
/*     */     public static DirtType byMetadata(int metadata) {
/*     */       if (metadata < 0 || metadata >= METADATA_LOOKUP.length)
/*     */         metadata = 0; 
/*     */       return METADATA_LOOKUP[metadata];
/*     */     }
/*     */     
/*     */     public String getName() {
/*     */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockDirt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */