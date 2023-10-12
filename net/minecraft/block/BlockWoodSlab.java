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
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class BlockWoodSlab
/*     */   extends BlockSlab {
/*  20 */   public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class);
/*     */ 
/*     */   
/*     */   public BlockWoodSlab() {
/*  24 */     super(Material.wood);
/*  25 */     IBlockState iblockstate = this.blockState.getBaseState();
/*     */     
/*  27 */     if (!isDouble())
/*     */     {
/*  29 */       iblockstate = iblockstate.withProperty((IProperty)HALF, BlockSlab.EnumBlockHalf.BOTTOM);
/*     */     }
/*     */     
/*  32 */     setDefaultState(iblockstate.withProperty((IProperty)VARIANT, BlockPlanks.EnumType.OAK));
/*  33 */     setCreativeTab(CreativeTabs.tabBlock);
/*     */   }
/*     */ 
/*     */   
/*     */   public MapColor getMapColor(IBlockState state) {
/*  38 */     return ((BlockPlanks.EnumType)state.getValue((IProperty)VARIANT)).getMapColor();
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/*  43 */     return Item.getItemFromBlock(Blocks.wooden_slab);
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos) {
/*  48 */     return Item.getItemFromBlock(Blocks.wooden_slab);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUnlocalizedName(int meta) {
/*  53 */     return getUnlocalizedName() + "." + BlockPlanks.EnumType.byMetadata(meta).getUnlocalizedName();
/*     */   }
/*     */ 
/*     */   
/*     */   public IProperty<?> getVariantProperty() {
/*  58 */     return (IProperty<?>)VARIANT;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getVariant(ItemStack stack) {
/*  63 */     return BlockPlanks.EnumType.byMetadata(stack.getMetadata() & 0x7);
/*     */   }
/*     */ 
/*     */   
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
/*  68 */     if (itemIn != Item.getItemFromBlock(Blocks.double_wooden_slab))
/*     */     {
/*  70 */       for (BlockPlanks.EnumType blockplanks$enumtype : BlockPlanks.EnumType.values())
/*     */       {
/*  72 */         list.add(new ItemStack(itemIn, 1, blockplanks$enumtype.getMetadata()));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/*  79 */     IBlockState iblockstate = getDefaultState().withProperty((IProperty)VARIANT, BlockPlanks.EnumType.byMetadata(meta & 0x7));
/*     */     
/*  81 */     if (!isDouble())
/*     */     {
/*  83 */       iblockstate = iblockstate.withProperty((IProperty)HALF, ((meta & 0x8) == 0) ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
/*     */     }
/*     */     
/*  86 */     return iblockstate;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/*  91 */     int i = 0;
/*  92 */     i |= ((BlockPlanks.EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
/*     */     
/*  94 */     if (!isDouble() && state.getValue((IProperty)HALF) == BlockSlab.EnumBlockHalf.TOP)
/*     */     {
/*  96 */       i |= 0x8;
/*     */     }
/*     */     
/*  99 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 104 */     return isDouble() ? new BlockState(this, new IProperty[] { (IProperty)VARIANT }) : new BlockState(this, new IProperty[] { (IProperty)HALF, (IProperty)VARIANT });
/*     */   }
/*     */ 
/*     */   
/*     */   public int damageDropped(IBlockState state) {
/* 109 */     return ((BlockPlanks.EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockWoodSlab.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */