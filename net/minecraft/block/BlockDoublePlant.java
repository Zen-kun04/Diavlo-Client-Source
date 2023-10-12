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
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeColorHelper;
/*     */ 
/*     */ public class BlockDoublePlant
/*     */   extends BlockBush implements IGrowable {
/*  28 */   public static final PropertyEnum<EnumPlantType> VARIANT = PropertyEnum.create("variant", EnumPlantType.class);
/*  29 */   public static final PropertyEnum<EnumBlockHalf> HALF = PropertyEnum.create("half", EnumBlockHalf.class);
/*  30 */   public static final PropertyEnum<EnumFacing> FACING = (PropertyEnum<EnumFacing>)BlockDirectional.FACING;
/*     */ 
/*     */   
/*     */   public BlockDoublePlant() {
/*  34 */     super(Material.vine);
/*  35 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)VARIANT, EnumPlantType.SUNFLOWER).withProperty((IProperty)HALF, EnumBlockHalf.LOWER).withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH));
/*  36 */     setHardness(0.0F);
/*  37 */     setStepSound(soundTypeGrass);
/*  38 */     setUnlocalizedName("doublePlant");
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
/*  43 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumPlantType getVariant(IBlockAccess worldIn, BlockPos pos) {
/*  48 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */     
/*  50 */     if (iblockstate.getBlock() == this) {
/*     */       
/*  52 */       iblockstate = getActualState(iblockstate, worldIn, pos);
/*  53 */       return (EnumPlantType)iblockstate.getValue((IProperty)VARIANT);
/*     */     } 
/*     */ 
/*     */     
/*  57 */     return EnumPlantType.FERN;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
/*  63 */     return (super.canPlaceBlockAt(worldIn, pos) && worldIn.isAirBlock(pos.up()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReplaceable(World worldIn, BlockPos pos) {
/*  68 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */     
/*  70 */     if (iblockstate.getBlock() != this)
/*     */     {
/*  72 */       return true;
/*     */     }
/*     */ 
/*     */     
/*  76 */     EnumPlantType blockdoubleplant$enumplanttype = (EnumPlantType)getActualState(iblockstate, (IBlockAccess)worldIn, pos).getValue((IProperty)VARIANT);
/*  77 */     return (blockdoubleplant$enumplanttype == EnumPlantType.FERN || blockdoubleplant$enumplanttype == EnumPlantType.GRASS);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
/*  83 */     if (!canBlockStay(worldIn, pos, state)) {
/*     */       
/*  85 */       boolean flag = (state.getValue((IProperty)HALF) == EnumBlockHalf.UPPER);
/*  86 */       BlockPos blockpos = flag ? pos : pos.up();
/*  87 */       BlockPos blockpos1 = flag ? pos.down() : pos;
/*  88 */       Block block = flag ? this : worldIn.getBlockState(blockpos).getBlock();
/*  89 */       Block block1 = flag ? worldIn.getBlockState(blockpos1).getBlock() : this;
/*     */       
/*  91 */       if (block == this)
/*     */       {
/*  93 */         worldIn.setBlockState(blockpos, Blocks.air.getDefaultState(), 2);
/*     */       }
/*     */       
/*  96 */       if (block1 == this) {
/*     */         
/*  98 */         worldIn.setBlockState(blockpos1, Blocks.air.getDefaultState(), 3);
/*     */         
/* 100 */         if (!flag)
/*     */         {
/* 102 */           dropBlockAsItem(worldIn, blockpos1, state, 0);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
/* 110 */     if (state.getValue((IProperty)HALF) == EnumBlockHalf.UPPER)
/*     */     {
/* 112 */       return (worldIn.getBlockState(pos.down()).getBlock() == this);
/*     */     }
/*     */ 
/*     */     
/* 116 */     IBlockState iblockstate = worldIn.getBlockState(pos.up());
/* 117 */     return (iblockstate.getBlock() == this && super.canBlockStay(worldIn, pos, iblockstate));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/* 123 */     if (state.getValue((IProperty)HALF) == EnumBlockHalf.UPPER)
/*     */     {
/* 125 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 129 */     EnumPlantType blockdoubleplant$enumplanttype = (EnumPlantType)state.getValue((IProperty)VARIANT);
/* 130 */     return (blockdoubleplant$enumplanttype == EnumPlantType.FERN) ? null : ((blockdoubleplant$enumplanttype == EnumPlantType.GRASS) ? ((rand.nextInt(8) == 0) ? Items.wheat_seeds : null) : Item.getItemFromBlock(this));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int damageDropped(IBlockState state) {
/* 136 */     return (state.getValue((IProperty)HALF) != EnumBlockHalf.UPPER && state.getValue((IProperty)VARIANT) != EnumPlantType.GRASS) ? ((EnumPlantType)state.getValue((IProperty)VARIANT)).getMeta() : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
/* 141 */     EnumPlantType blockdoubleplant$enumplanttype = getVariant(worldIn, pos);
/* 142 */     return (blockdoubleplant$enumplanttype != EnumPlantType.GRASS && blockdoubleplant$enumplanttype != EnumPlantType.FERN) ? 16777215 : BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public void placeAt(World worldIn, BlockPos lowerPos, EnumPlantType variant, int flags) {
/* 147 */     worldIn.setBlockState(lowerPos, getDefaultState().withProperty((IProperty)HALF, EnumBlockHalf.LOWER).withProperty((IProperty)VARIANT, variant), flags);
/* 148 */     worldIn.setBlockState(lowerPos.up(), getDefaultState().withProperty((IProperty)HALF, EnumBlockHalf.UPPER), flags);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
/* 153 */     worldIn.setBlockState(pos.up(), getDefaultState().withProperty((IProperty)HALF, EnumBlockHalf.UPPER), 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
/* 158 */     if (worldIn.isRemote || player.getCurrentEquippedItem() == null || player.getCurrentEquippedItem().getItem() != Items.shears || state.getValue((IProperty)HALF) != EnumBlockHalf.LOWER || !onHarvest(worldIn, pos, state, player))
/*     */     {
/* 160 */       super.harvestBlock(worldIn, player, pos, state, te);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
/* 166 */     if (state.getValue((IProperty)HALF) == EnumBlockHalf.UPPER) {
/*     */       
/* 168 */       if (worldIn.getBlockState(pos.down()).getBlock() == this)
/*     */       {
/* 170 */         if (!player.capabilities.isCreativeMode) {
/*     */           
/* 172 */           IBlockState iblockstate = worldIn.getBlockState(pos.down());
/* 173 */           EnumPlantType blockdoubleplant$enumplanttype = (EnumPlantType)iblockstate.getValue((IProperty)VARIANT);
/*     */           
/* 175 */           if (blockdoubleplant$enumplanttype != EnumPlantType.FERN && blockdoubleplant$enumplanttype != EnumPlantType.GRASS) {
/*     */             
/* 177 */             worldIn.destroyBlock(pos.down(), true);
/*     */           }
/* 179 */           else if (!worldIn.isRemote) {
/*     */             
/* 181 */             if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.shears)
/*     */             {
/* 183 */               onHarvest(worldIn, pos, iblockstate, player);
/* 184 */               worldIn.setBlockToAir(pos.down());
/*     */             }
/*     */             else
/*     */             {
/* 188 */               worldIn.destroyBlock(pos.down(), true);
/*     */             }
/*     */           
/*     */           } else {
/*     */             
/* 193 */             worldIn.setBlockToAir(pos.down());
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 198 */           worldIn.setBlockToAir(pos.down());
/*     */         }
/*     */       
/*     */       }
/* 202 */     } else if (player.capabilities.isCreativeMode && worldIn.getBlockState(pos.up()).getBlock() == this) {
/*     */       
/* 204 */       worldIn.setBlockState(pos.up(), Blocks.air.getDefaultState(), 2);
/*     */     } 
/*     */     
/* 207 */     super.onBlockHarvested(worldIn, pos, state, player);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean onHarvest(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
/* 212 */     EnumPlantType blockdoubleplant$enumplanttype = (EnumPlantType)state.getValue((IProperty)VARIANT);
/*     */     
/* 214 */     if (blockdoubleplant$enumplanttype != EnumPlantType.FERN && blockdoubleplant$enumplanttype != EnumPlantType.GRASS)
/*     */     {
/* 216 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 220 */     player.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
/* 221 */     int i = ((blockdoubleplant$enumplanttype == EnumPlantType.GRASS) ? BlockTallGrass.EnumType.GRASS : BlockTallGrass.EnumType.FERN).getMeta();
/* 222 */     spawnAsEntity(worldIn, pos, new ItemStack(Blocks.tallgrass, 2, i));
/* 223 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
/* 229 */     for (EnumPlantType blockdoubleplant$enumplanttype : EnumPlantType.values())
/*     */     {
/* 231 */       list.add(new ItemStack(itemIn, 1, blockdoubleplant$enumplanttype.getMeta()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDamageValue(World worldIn, BlockPos pos) {
/* 237 */     return getVariant((IBlockAccess)worldIn, pos).getMeta();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
/* 242 */     EnumPlantType blockdoubleplant$enumplanttype = getVariant((IBlockAccess)worldIn, pos);
/* 243 */     return (blockdoubleplant$enumplanttype != EnumPlantType.GRASS && blockdoubleplant$enumplanttype != EnumPlantType.FERN);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
/* 248 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
/* 253 */     spawnAsEntity(worldIn, pos, new ItemStack(this, 1, getVariant((IBlockAccess)worldIn, pos).getMeta()));
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 258 */     return ((meta & 0x8) > 0) ? getDefaultState().withProperty((IProperty)HALF, EnumBlockHalf.UPPER) : getDefaultState().withProperty((IProperty)HALF, EnumBlockHalf.LOWER).withProperty((IProperty)VARIANT, EnumPlantType.byMetadata(meta & 0x7));
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
/* 263 */     if (state.getValue((IProperty)HALF) == EnumBlockHalf.UPPER) {
/*     */       
/* 265 */       IBlockState iblockstate = worldIn.getBlockState(pos.down());
/*     */       
/* 267 */       if (iblockstate.getBlock() == this)
/*     */       {
/* 269 */         state = state.withProperty((IProperty)VARIANT, iblockstate.getValue((IProperty)VARIANT));
/*     */       }
/*     */     } 
/*     */     
/* 273 */     return state;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 278 */     return (state.getValue((IProperty)HALF) == EnumBlockHalf.UPPER) ? (0x8 | ((EnumFacing)state.getValue((IProperty)FACING)).getHorizontalIndex()) : ((EnumPlantType)state.getValue((IProperty)VARIANT)).getMeta();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 283 */     return new BlockState(this, new IProperty[] { (IProperty)HALF, (IProperty)VARIANT, (IProperty)FACING });
/*     */   }
/*     */ 
/*     */   
/*     */   public Block.EnumOffsetType getOffsetType() {
/* 288 */     return Block.EnumOffsetType.XZ;
/*     */   }
/*     */   
/*     */   public enum EnumBlockHalf
/*     */     implements IStringSerializable {
/* 293 */     UPPER,
/* 294 */     LOWER;
/*     */ 
/*     */     
/*     */     public String toString() {
/* 298 */       return getName();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/* 303 */       return (this == UPPER) ? "upper" : "lower";
/*     */     }
/*     */   }
/*     */   
/*     */   public enum EnumPlantType
/*     */     implements IStringSerializable {
/* 309 */     SUNFLOWER(0, "sunflower"),
/* 310 */     SYRINGA(1, "syringa"),
/* 311 */     GRASS(2, "double_grass", "grass"),
/* 312 */     FERN(3, "double_fern", "fern"),
/* 313 */     ROSE(4, "double_rose", "rose"),
/* 314 */     PAEONIA(5, "paeonia");
/*     */     
/* 316 */     private static final EnumPlantType[] META_LOOKUP = new EnumPlantType[(values()).length];
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
/*     */     static {
/* 364 */       for (EnumPlantType blockdoubleplant$enumplanttype : values())
/*     */       {
/* 366 */         META_LOOKUP[blockdoubleplant$enumplanttype.getMeta()] = blockdoubleplant$enumplanttype;
/*     */       }
/*     */     }
/*     */     
/*     */     EnumPlantType(int meta, String name, String unlocalizedName) {
/*     */       this.meta = meta;
/*     */       this.name = name;
/*     */       this.unlocalizedName = unlocalizedName;
/*     */     }
/*     */     
/*     */     public int getMeta() {
/*     */       return this.meta;
/*     */     }
/*     */     
/*     */     public String toString() {
/*     */       return this.name;
/*     */     }
/*     */     
/*     */     public static EnumPlantType byMetadata(int meta) {
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


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockDoublePlant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */