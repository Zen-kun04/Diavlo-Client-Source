/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockHugeMushroom
/*     */   extends Block {
/*  19 */   public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
/*     */   
/*     */   private final Block smallBlock;
/*     */   
/*     */   public BlockHugeMushroom(Material p_i46392_1_, MapColor p_i46392_2_, Block p_i46392_3_) {
/*  24 */     super(p_i46392_1_, p_i46392_2_);
/*  25 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)VARIANT, EnumType.ALL_OUTSIDE));
/*  26 */     this.smallBlock = p_i46392_3_;
/*     */   }
/*     */ 
/*     */   
/*     */   public int quantityDropped(Random random) {
/*  31 */     return Math.max(0, random.nextInt(10) - 7);
/*     */   }
/*     */ 
/*     */   
/*     */   public MapColor getMapColor(IBlockState state) {
/*  36 */     switch ((EnumType)state.getValue((IProperty)VARIANT)) {
/*     */       
/*     */       case ALL_STEM:
/*  39 */         return MapColor.clothColor;
/*     */       
/*     */       case ALL_INSIDE:
/*  42 */         return MapColor.sandColor;
/*     */       
/*     */       case STEM:
/*  45 */         return MapColor.sandColor;
/*     */     } 
/*     */     
/*  48 */     return super.getMapColor(state);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/*  54 */     return Item.getItemFromBlock(this.smallBlock);
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos) {
/*  59 */     return Item.getItemFromBlock(this.smallBlock);
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
/*  64 */     return getDefaultState();
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/*  69 */     return getDefaultState().withProperty((IProperty)VARIANT, EnumType.byMetadata(meta));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/*  74 */     return ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/*  79 */     return new BlockState(this, new IProperty[] { (IProperty)VARIANT });
/*     */   }
/*     */   
/*     */   public enum EnumType
/*     */     implements IStringSerializable {
/*  84 */     NORTH_WEST(1, "north_west"),
/*  85 */     NORTH(2, "north"),
/*  86 */     NORTH_EAST(3, "north_east"),
/*  87 */     WEST(4, "west"),
/*  88 */     CENTER(5, "center"),
/*  89 */     EAST(6, "east"),
/*  90 */     SOUTH_WEST(7, "south_west"),
/*  91 */     SOUTH(8, "south"),
/*  92 */     SOUTH_EAST(9, "south_east"),
/*  93 */     STEM(10, "stem"),
/*  94 */     ALL_INSIDE(0, "all_inside"),
/*  95 */     ALL_OUTSIDE(14, "all_outside"),
/*  96 */     ALL_STEM(15, "all_stem");
/*     */     
/*  98 */     private static final EnumType[] META_LOOKUP = new EnumType[16];
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
/*     */     
/*     */     static {
/* 135 */       for (EnumType blockhugemushroom$enumtype : values())
/*     */       {
/* 137 */         META_LOOKUP[blockhugemushroom$enumtype.getMetadata()] = blockhugemushroom$enumtype;
/*     */       }
/*     */     }
/*     */     
/*     */     EnumType(int meta, String name) {
/*     */       this.meta = meta;
/*     */       this.name = name;
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
/*     */       EnumType blockhugemushroom$enumtype = META_LOOKUP[meta];
/*     */       return (blockhugemushroom$enumtype == null) ? META_LOOKUP[0] : blockhugemushroom$enumtype;
/*     */     }
/*     */     
/*     */     public String getName() {
/*     */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockHugeMushroom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */