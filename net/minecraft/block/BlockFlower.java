/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.collect.Collections2;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ 
/*     */ public abstract class BlockFlower
/*     */   extends BlockBush
/*     */ {
/*     */   protected PropertyEnum<EnumFlowerType> type;
/*     */   
/*     */   protected BlockFlower() {
/*  24 */     setDefaultState(this.blockState.getBaseState().withProperty(getTypeProperty(), (getBlockType() == EnumFlowerColor.RED) ? EnumFlowerType.POPPY : EnumFlowerType.DANDELION));
/*     */   }
/*     */ 
/*     */   
/*     */   public int damageDropped(IBlockState state) {
/*  29 */     return ((EnumFlowerType)state.getValue(getTypeProperty())).getMeta();
/*     */   }
/*     */ 
/*     */   
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
/*  34 */     for (EnumFlowerType blockflower$enumflowertype : EnumFlowerType.getTypes(getBlockType()))
/*     */     {
/*  36 */       list.add(new ItemStack(itemIn, 1, blockflower$enumflowertype.getMeta()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/*  42 */     return getDefaultState().withProperty(getTypeProperty(), EnumFlowerType.getType(getBlockType(), meta));
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract EnumFlowerColor getBlockType();
/*     */   
/*     */   public IProperty<EnumFlowerType> getTypeProperty() {
/*  49 */     if (this.type == null)
/*     */     {
/*  51 */       this.type = PropertyEnum.create("type", EnumFlowerType.class, new Predicate<EnumFlowerType>()
/*     */           {
/*     */             public boolean apply(BlockFlower.EnumFlowerType p_apply_1_)
/*     */             {
/*  55 */               return (p_apply_1_.getBlockType() == BlockFlower.this.getBlockType());
/*     */             }
/*     */           });
/*     */     }
/*     */     
/*  60 */     return (IProperty<EnumFlowerType>)this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/*  65 */     return ((EnumFlowerType)state.getValue(getTypeProperty())).getMeta();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/*  70 */     return new BlockState(this, new IProperty[] { getTypeProperty() });
/*     */   }
/*     */ 
/*     */   
/*     */   public Block.EnumOffsetType getOffsetType() {
/*  75 */     return Block.EnumOffsetType.XZ;
/*     */   }
/*     */   
/*     */   public enum EnumFlowerColor
/*     */   {
/*  80 */     YELLOW,
/*  81 */     RED;
/*     */ 
/*     */     
/*     */     public BlockFlower getBlock() {
/*  85 */       return (this == YELLOW) ? Blocks.yellow_flower : Blocks.red_flower;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum EnumFlowerType
/*     */     implements IStringSerializable {
/*  91 */     DANDELION((String)BlockFlower.EnumFlowerColor.YELLOW, 0, (BlockFlower.EnumFlowerColor)"dandelion"),
/*  92 */     POPPY((String)BlockFlower.EnumFlowerColor.RED, 0, (BlockFlower.EnumFlowerColor)"poppy"),
/*  93 */     BLUE_ORCHID((String)BlockFlower.EnumFlowerColor.RED, 1, (BlockFlower.EnumFlowerColor)"blue_orchid", "blueOrchid"),
/*  94 */     ALLIUM((String)BlockFlower.EnumFlowerColor.RED, 2, (BlockFlower.EnumFlowerColor)"allium"),
/*  95 */     HOUSTONIA((String)BlockFlower.EnumFlowerColor.RED, 3, (BlockFlower.EnumFlowerColor)"houstonia"),
/*  96 */     RED_TULIP((String)BlockFlower.EnumFlowerColor.RED, 4, (BlockFlower.EnumFlowerColor)"red_tulip", "tulipRed"),
/*  97 */     ORANGE_TULIP((String)BlockFlower.EnumFlowerColor.RED, 5, (BlockFlower.EnumFlowerColor)"orange_tulip", "tulipOrange"),
/*  98 */     WHITE_TULIP((String)BlockFlower.EnumFlowerColor.RED, 6, (BlockFlower.EnumFlowerColor)"white_tulip", "tulipWhite"),
/*  99 */     PINK_TULIP((String)BlockFlower.EnumFlowerColor.RED, 7, (BlockFlower.EnumFlowerColor)"pink_tulip", "tulipPink"),
/* 100 */     OXEYE_DAISY((String)BlockFlower.EnumFlowerColor.RED, 8, (BlockFlower.EnumFlowerColor)"oxeye_daisy", "oxeyeDaisy");
/*     */     
/* 102 */     private static final EnumFlowerType[][] TYPES_FOR_BLOCK = new EnumFlowerType[(BlockFlower.EnumFlowerColor.values()).length][];
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
/*     */     private final BlockFlower.EnumFlowerColor blockType;
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
/*     */ 
/*     */     
/*     */     static {
/* 164 */       for (BlockFlower.EnumFlowerColor blockflower$enumflowercolor : BlockFlower.EnumFlowerColor.values()) {
/*     */         
/* 166 */         Collection<EnumFlowerType> collection = Collections2.filter(Lists.newArrayList((Object[])values()), new Predicate<EnumFlowerType>()
/*     */             {
/*     */               public boolean apply(BlockFlower.EnumFlowerType p_apply_1_)
/*     */               {
/* 170 */                 return (p_apply_1_.getBlockType() == blockflower$enumflowercolor);
/*     */               }
/*     */             });
/* 173 */         TYPES_FOR_BLOCK[blockflower$enumflowercolor.ordinal()] = collection.<EnumFlowerType>toArray(new EnumFlowerType[collection.size()]);
/*     */       } 
/*     */     }
/*     */     
/*     */     EnumFlowerType(BlockFlower.EnumFlowerColor blockType, int meta, String name, String unlocalizedName) {
/*     */       this.blockType = blockType;
/*     */       this.meta = meta;
/*     */       this.name = name;
/*     */       this.unlocalizedName = unlocalizedName;
/*     */     }
/*     */     
/*     */     public BlockFlower.EnumFlowerColor getBlockType() {
/*     */       return this.blockType;
/*     */     }
/*     */     
/*     */     public int getMeta() {
/*     */       return this.meta;
/*     */     }
/*     */     
/*     */     public static EnumFlowerType getType(BlockFlower.EnumFlowerColor blockType, int meta) {
/*     */       EnumFlowerType[] ablockflower$enumflowertype = TYPES_FOR_BLOCK[blockType.ordinal()];
/*     */       if (meta < 0 || meta >= ablockflower$enumflowertype.length)
/*     */         meta = 0; 
/*     */       return ablockflower$enumflowertype[meta];
/*     */     }
/*     */     
/*     */     public static EnumFlowerType[] getTypes(BlockFlower.EnumFlowerColor flowerColor) {
/*     */       return TYPES_FOR_BLOCK[flowerColor.ordinal()];
/*     */     }
/*     */     
/*     */     public String toString() {
/*     */       return this.name;
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


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockFlower.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */