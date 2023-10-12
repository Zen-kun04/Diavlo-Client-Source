/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.monster.EntitySilverfish;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockSilverfish extends Block {
/*  21 */   public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
/*     */ 
/*     */   
/*     */   public BlockSilverfish() {
/*  25 */     super(Material.clay);
/*  26 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)VARIANT, EnumType.STONE));
/*  27 */     setHardness(0.0F);
/*  28 */     setCreativeTab(CreativeTabs.tabDecorations);
/*     */   }
/*     */ 
/*     */   
/*     */   public int quantityDropped(Random random) {
/*  33 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean canContainSilverfish(IBlockState blockState) {
/*  38 */     Block block = blockState.getBlock();
/*  39 */     return (blockState == Blocks.stone.getDefaultState().withProperty((IProperty)BlockStone.VARIANT, BlockStone.EnumType.STONE) || block == Blocks.cobblestone || block == Blocks.stonebrick);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack createStackedBlock(IBlockState state) {
/*  44 */     switch ((EnumType)state.getValue((IProperty)VARIANT)) {
/*     */       
/*     */       case COBBLESTONE:
/*  47 */         return new ItemStack(Blocks.cobblestone);
/*     */       
/*     */       case STONEBRICK:
/*  50 */         return new ItemStack(Blocks.stonebrick);
/*     */       
/*     */       case MOSSY_STONEBRICK:
/*  53 */         return new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.EnumType.MOSSY.getMetadata());
/*     */       
/*     */       case CRACKED_STONEBRICK:
/*  56 */         return new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.EnumType.CRACKED.getMetadata());
/*     */       
/*     */       case CHISELED_STONEBRICK:
/*  59 */         return new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.EnumType.CHISELED.getMetadata());
/*     */     } 
/*     */     
/*  62 */     return new ItemStack(Blocks.stone);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
/*  68 */     if (!worldIn.isRemote && worldIn.getGameRules().getBoolean("doTileDrops")) {
/*     */       
/*  70 */       EntitySilverfish entitysilverfish = new EntitySilverfish(worldIn);
/*  71 */       entitysilverfish.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
/*  72 */       worldIn.spawnEntityInWorld((Entity)entitysilverfish);
/*  73 */       entitysilverfish.spawnExplosionParticle();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDamageValue(World worldIn, BlockPos pos) {
/*  79 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*  80 */     return iblockstate.getBlock().getMetaFromState(iblockstate);
/*     */   }
/*     */ 
/*     */   
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
/*  85 */     for (EnumType blocksilverfish$enumtype : EnumType.values())
/*     */     {
/*  87 */       list.add(new ItemStack(itemIn, 1, blocksilverfish$enumtype.getMetadata()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/*  93 */     return getDefaultState().withProperty((IProperty)VARIANT, EnumType.byMetadata(meta));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/*  98 */     return ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 103 */     return new BlockState(this, new IProperty[] { (IProperty)VARIANT });
/*     */   }
/*     */   
/*     */   public enum EnumType
/*     */     implements IStringSerializable {
/* 108 */     STONE(0, "stone")
/*     */     {
/*     */       public IBlockState getModelBlock()
/*     */       {
/* 112 */         return Blocks.stone.getDefaultState().withProperty((IProperty)BlockStone.VARIANT, BlockStone.EnumType.STONE);
/*     */       }
/*     */     },
/* 115 */     COBBLESTONE(1, "cobblestone", "cobble")
/*     */     {
/*     */       public IBlockState getModelBlock()
/*     */       {
/* 119 */         return Blocks.cobblestone.getDefaultState();
/*     */       }
/*     */     },
/* 122 */     STONEBRICK(2, "stone_brick", "brick")
/*     */     {
/*     */       public IBlockState getModelBlock()
/*     */       {
/* 126 */         return Blocks.stonebrick.getDefaultState().withProperty((IProperty)BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.DEFAULT);
/*     */       }
/*     */     },
/* 129 */     MOSSY_STONEBRICK(3, "mossy_brick", "mossybrick")
/*     */     {
/*     */       public IBlockState getModelBlock()
/*     */       {
/* 133 */         return Blocks.stonebrick.getDefaultState().withProperty((IProperty)BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY);
/*     */       }
/*     */     },
/* 136 */     CRACKED_STONEBRICK(4, "cracked_brick", "crackedbrick")
/*     */     {
/*     */       public IBlockState getModelBlock()
/*     */       {
/* 140 */         return Blocks.stonebrick.getDefaultState().withProperty((IProperty)BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED);
/*     */       }
/*     */     },
/* 143 */     CHISELED_STONEBRICK(5, "chiseled_brick", "chiseledbrick")
/*     */     {
/*     */       public IBlockState getModelBlock()
/*     */       {
/* 147 */         return Blocks.stonebrick.getDefaultState().withProperty((IProperty)BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED);
/*     */       }
/*     */     };
/*     */     
/* 151 */     private static final EnumType[] META_LOOKUP = new EnumType[(values()).length];
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
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/* 214 */       for (EnumType blocksilverfish$enumtype : values())
/*     */       {
/* 216 */         META_LOOKUP[blocksilverfish$enumtype.getMetadata()] = blocksilverfish$enumtype;
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
/*     */     
/*     */     public static EnumType forModelBlock(IBlockState model) {
/*     */       for (EnumType blocksilverfish$enumtype : values()) {
/*     */         if (model == blocksilverfish$enumtype.getModelBlock())
/*     */           return blocksilverfish$enumtype; 
/*     */       } 
/*     */       return STONE;
/*     */     }
/*     */     
/*     */     public abstract IBlockState getModelBlock();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockSilverfish.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */