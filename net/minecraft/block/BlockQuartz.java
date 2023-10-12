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
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockQuartz
/*     */   extends Block {
/*  21 */   public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
/*     */ 
/*     */   
/*     */   public BlockQuartz() {
/*  25 */     super(Material.rock);
/*  26 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)VARIANT, EnumType.DEFAULT));
/*  27 */     setCreativeTab(CreativeTabs.tabBlock);
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
/*  32 */     if (meta == EnumType.LINES_Y.getMetadata()) {
/*     */       
/*  34 */       switch (facing.getAxis()) {
/*     */         
/*     */         case Z:
/*  37 */           return getDefaultState().withProperty((IProperty)VARIANT, EnumType.LINES_Z);
/*     */         
/*     */         case X:
/*  40 */           return getDefaultState().withProperty((IProperty)VARIANT, EnumType.LINES_X);
/*     */       } 
/*     */ 
/*     */       
/*  44 */       return getDefaultState().withProperty((IProperty)VARIANT, EnumType.LINES_Y);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  49 */     return (meta == EnumType.CHISELED.getMetadata()) ? getDefaultState().withProperty((IProperty)VARIANT, EnumType.CHISELED) : getDefaultState().withProperty((IProperty)VARIANT, EnumType.DEFAULT);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int damageDropped(IBlockState state) {
/*  55 */     EnumType blockquartz$enumtype = (EnumType)state.getValue((IProperty)VARIANT);
/*  56 */     return (blockquartz$enumtype != EnumType.LINES_X && blockquartz$enumtype != EnumType.LINES_Z) ? blockquartz$enumtype.getMetadata() : EnumType.LINES_Y.getMetadata();
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack createStackedBlock(IBlockState state) {
/*  61 */     EnumType blockquartz$enumtype = (EnumType)state.getValue((IProperty)VARIANT);
/*  62 */     return (blockquartz$enumtype != EnumType.LINES_X && blockquartz$enumtype != EnumType.LINES_Z) ? super.createStackedBlock(state) : new ItemStack(Item.getItemFromBlock(this), 1, EnumType.LINES_Y.getMetadata());
/*     */   }
/*     */ 
/*     */   
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
/*  67 */     list.add(new ItemStack(itemIn, 1, EnumType.DEFAULT.getMetadata()));
/*  68 */     list.add(new ItemStack(itemIn, 1, EnumType.CHISELED.getMetadata()));
/*  69 */     list.add(new ItemStack(itemIn, 1, EnumType.LINES_Y.getMetadata()));
/*     */   }
/*     */ 
/*     */   
/*     */   public MapColor getMapColor(IBlockState state) {
/*  74 */     return MapColor.quartzColor;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/*  79 */     return getDefaultState().withProperty((IProperty)VARIANT, EnumType.byMetadata(meta));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/*  84 */     return ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/*  89 */     return new BlockState(this, new IProperty[] { (IProperty)VARIANT });
/*     */   }
/*     */   
/*     */   public enum EnumType
/*     */     implements IStringSerializable {
/*  94 */     DEFAULT(0, "default", "default"),
/*  95 */     CHISELED(1, "chiseled", "chiseled"),
/*  96 */     LINES_Y(2, "lines_y", "lines"),
/*  97 */     LINES_X(3, "lines_x", "lines"),
/*  98 */     LINES_Z(4, "lines_z", "lines");
/*     */     
/* 100 */     private static final EnumType[] META_LOOKUP = new EnumType[(values()).length];
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
/*     */     private final String field_176805_h;
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
/* 138 */       for (EnumType blockquartz$enumtype : values())
/*     */       {
/* 140 */         META_LOOKUP[blockquartz$enumtype.getMetadata()] = blockquartz$enumtype;
/*     */       }
/*     */     }
/*     */     
/*     */     EnumType(int meta, String name, String unlocalizedName) {
/*     */       this.meta = meta;
/*     */       this.field_176805_h = name;
/*     */       this.unlocalizedName = unlocalizedName;
/*     */     }
/*     */     
/*     */     public int getMetadata() {
/*     */       return this.meta;
/*     */     }
/*     */     
/*     */     public String toString() {
/*     */       return this.unlocalizedName;
/*     */     }
/*     */     
/*     */     public static EnumType byMetadata(int meta) {
/*     */       if (meta < 0 || meta >= META_LOOKUP.length)
/*     */         meta = 0; 
/*     */       return META_LOOKUP[meta];
/*     */     }
/*     */     
/*     */     public String getName() {
/*     */       return this.field_176805_h;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockQuartz.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */