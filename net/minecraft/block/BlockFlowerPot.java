/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.properties.PropertyInteger;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityFlowerPot;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.util.StatCollector;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockFlowerPot
/*     */   extends BlockContainer
/*     */ {
/*  29 */   public static final PropertyInteger LEGACY_DATA = PropertyInteger.create("legacy_data", 0, 15);
/*  30 */   public static final PropertyEnum<EnumFlowerType> CONTENTS = PropertyEnum.create("contents", EnumFlowerType.class);
/*     */ 
/*     */   
/*     */   public BlockFlowerPot() {
/*  34 */     super(Material.circuits);
/*  35 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)CONTENTS, EnumFlowerType.EMPTY).withProperty((IProperty)LEGACY_DATA, Integer.valueOf(0)));
/*  36 */     setBlockBoundsForItemRender();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLocalizedName() {
/*  41 */     return StatCollector.translateToLocal("item.flowerPot.name");
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockBoundsForItemRender() {
/*  46 */     float f = 0.375F;
/*  47 */     float f1 = f / 2.0F;
/*  48 */     setBlockBounds(0.5F - f1, 0.0F, 0.5F - f1, 0.5F + f1, f, 0.5F + f1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/*  53 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRenderType() {
/*  58 */     return 3;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/*  63 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
/*  68 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/*  70 */     if (tileentity instanceof TileEntityFlowerPot) {
/*     */       
/*  72 */       Item item = ((TileEntityFlowerPot)tileentity).getFlowerPotItem();
/*     */       
/*  74 */       if (item instanceof net.minecraft.item.ItemBlock)
/*     */       {
/*  76 */         return Block.getBlockFromItem(item).colorMultiplier(worldIn, pos, renderPass);
/*     */       }
/*     */     } 
/*     */     
/*  80 */     return 16777215;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
/*  85 */     ItemStack itemstack = playerIn.inventory.getCurrentItem();
/*     */     
/*  87 */     if (itemstack != null && itemstack.getItem() instanceof net.minecraft.item.ItemBlock) {
/*     */       
/*  89 */       TileEntityFlowerPot tileentityflowerpot = getTileEntity(worldIn, pos);
/*     */       
/*  91 */       if (tileentityflowerpot == null)
/*     */       {
/*  93 */         return false;
/*     */       }
/*  95 */       if (tileentityflowerpot.getFlowerPotItem() != null)
/*     */       {
/*  97 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 101 */       Block block = Block.getBlockFromItem(itemstack.getItem());
/*     */       
/* 103 */       if (!canNotContain(block, itemstack.getMetadata()))
/*     */       {
/* 105 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 109 */       tileentityflowerpot.setFlowerPotData(itemstack.getItem(), itemstack.getMetadata());
/* 110 */       tileentityflowerpot.markDirty();
/* 111 */       worldIn.markBlockForUpdate(pos);
/* 112 */       playerIn.triggerAchievement(StatList.field_181736_T);
/*     */       
/* 114 */       if (!playerIn.capabilities.isCreativeMode && --itemstack.stackSize <= 0)
/*     */       {
/* 116 */         playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, (ItemStack)null);
/*     */       }
/*     */       
/* 119 */       return true;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean canNotContain(Block blockIn, int meta) {
/* 131 */     return (blockIn != Blocks.yellow_flower && blockIn != Blocks.red_flower && blockIn != Blocks.cactus && blockIn != Blocks.brown_mushroom && blockIn != Blocks.red_mushroom && blockIn != Blocks.sapling && blockIn != Blocks.deadbush) ? ((blockIn == Blocks.tallgrass && meta == BlockTallGrass.EnumType.FERN.getMeta())) : true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos) {
/* 136 */     TileEntityFlowerPot tileentityflowerpot = getTileEntity(worldIn, pos);
/* 137 */     return (tileentityflowerpot != null && tileentityflowerpot.getFlowerPotItem() != null) ? tileentityflowerpot.getFlowerPotItem() : Items.flower_pot;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDamageValue(World worldIn, BlockPos pos) {
/* 142 */     TileEntityFlowerPot tileentityflowerpot = getTileEntity(worldIn, pos);
/* 143 */     return (tileentityflowerpot != null && tileentityflowerpot.getFlowerPotItem() != null) ? tileentityflowerpot.getFlowerPotData() : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFlowerPot() {
/* 148 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
/* 153 */     return (super.canPlaceBlockAt(worldIn, pos) && World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.down()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/* 158 */     if (!World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.down())) {
/*     */       
/* 160 */       dropBlockAsItem(worldIn, pos, state, 0);
/* 161 */       worldIn.setBlockToAir(pos);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
/* 167 */     TileEntityFlowerPot tileentityflowerpot = getTileEntity(worldIn, pos);
/*     */     
/* 169 */     if (tileentityflowerpot != null && tileentityflowerpot.getFlowerPotItem() != null)
/*     */     {
/* 171 */       spawnAsEntity(worldIn, pos, new ItemStack(tileentityflowerpot.getFlowerPotItem(), 1, tileentityflowerpot.getFlowerPotData()));
/*     */     }
/*     */     
/* 174 */     super.breakBlock(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
/* 179 */     super.onBlockHarvested(worldIn, pos, state, player);
/*     */     
/* 181 */     if (player.capabilities.isCreativeMode) {
/*     */       
/* 183 */       TileEntityFlowerPot tileentityflowerpot = getTileEntity(worldIn, pos);
/*     */       
/* 185 */       if (tileentityflowerpot != null)
/*     */       {
/* 187 */         tileentityflowerpot.setFlowerPotData((Item)null, 0);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/* 194 */     return Items.flower_pot;
/*     */   }
/*     */ 
/*     */   
/*     */   private TileEntityFlowerPot getTileEntity(World worldIn, BlockPos pos) {
/* 199 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/* 200 */     return (tileentity instanceof TileEntityFlowerPot) ? (TileEntityFlowerPot)tileentity : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public TileEntity createNewTileEntity(World worldIn, int meta) {
/* 205 */     Block block = null;
/* 206 */     int i = 0;
/*     */     
/* 208 */     switch (meta) {
/*     */       
/*     */       case 1:
/* 211 */         block = Blocks.red_flower;
/* 212 */         i = BlockFlower.EnumFlowerType.POPPY.getMeta();
/*     */         break;
/*     */       
/*     */       case 2:
/* 216 */         block = Blocks.yellow_flower;
/*     */         break;
/*     */       
/*     */       case 3:
/* 220 */         block = Blocks.sapling;
/* 221 */         i = BlockPlanks.EnumType.OAK.getMetadata();
/*     */         break;
/*     */       
/*     */       case 4:
/* 225 */         block = Blocks.sapling;
/* 226 */         i = BlockPlanks.EnumType.SPRUCE.getMetadata();
/*     */         break;
/*     */       
/*     */       case 5:
/* 230 */         block = Blocks.sapling;
/* 231 */         i = BlockPlanks.EnumType.BIRCH.getMetadata();
/*     */         break;
/*     */       
/*     */       case 6:
/* 235 */         block = Blocks.sapling;
/* 236 */         i = BlockPlanks.EnumType.JUNGLE.getMetadata();
/*     */         break;
/*     */       
/*     */       case 7:
/* 240 */         block = Blocks.red_mushroom;
/*     */         break;
/*     */       
/*     */       case 8:
/* 244 */         block = Blocks.brown_mushroom;
/*     */         break;
/*     */       
/*     */       case 9:
/* 248 */         block = Blocks.cactus;
/*     */         break;
/*     */       
/*     */       case 10:
/* 252 */         block = Blocks.deadbush;
/*     */         break;
/*     */       
/*     */       case 11:
/* 256 */         block = Blocks.tallgrass;
/* 257 */         i = BlockTallGrass.EnumType.FERN.getMeta();
/*     */         break;
/*     */       
/*     */       case 12:
/* 261 */         block = Blocks.sapling;
/* 262 */         i = BlockPlanks.EnumType.ACACIA.getMetadata();
/*     */         break;
/*     */       
/*     */       case 13:
/* 266 */         block = Blocks.sapling;
/* 267 */         i = BlockPlanks.EnumType.DARK_OAK.getMetadata();
/*     */         break;
/*     */     } 
/* 270 */     return (TileEntity)new TileEntityFlowerPot(Item.getItemFromBlock(block), i);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 275 */     return new BlockState(this, new IProperty[] { (IProperty)CONTENTS, (IProperty)LEGACY_DATA });
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 280 */     return ((Integer)state.getValue((IProperty)LEGACY_DATA)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
/* 285 */     EnumFlowerType blockflowerpot$enumflowertype = EnumFlowerType.EMPTY;
/* 286 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/* 288 */     if (tileentity instanceof TileEntityFlowerPot)
/*     */     
/* 290 */     { TileEntityFlowerPot tileentityflowerpot = (TileEntityFlowerPot)tileentity;
/* 291 */       Item item = tileentityflowerpot.getFlowerPotItem();
/*     */       
/* 293 */       if (item instanceof net.minecraft.item.ItemBlock)
/*     */       
/* 295 */       { int i = tileentityflowerpot.getFlowerPotData();
/* 296 */         Block block = Block.getBlockFromItem(item);
/*     */         
/* 298 */         if (block == Blocks.sapling)
/*     */         
/* 300 */         { switch (BlockPlanks.EnumType.byMetadata(i))
/*     */           
/*     */           { case POPPY:
/* 303 */               blockflowerpot$enumflowertype = EnumFlowerType.OAK_SAPLING;
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
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 413 */               return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);case BLUE_ORCHID: blockflowerpot$enumflowertype = EnumFlowerType.SPRUCE_SAPLING; return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);case ALLIUM: blockflowerpot$enumflowertype = EnumFlowerType.BIRCH_SAPLING; return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);case HOUSTONIA: blockflowerpot$enumflowertype = EnumFlowerType.JUNGLE_SAPLING; return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);case RED_TULIP: blockflowerpot$enumflowertype = EnumFlowerType.ACACIA_SAPLING; return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);case ORANGE_TULIP: blockflowerpot$enumflowertype = EnumFlowerType.DARK_OAK_SAPLING; return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype); }  blockflowerpot$enumflowertype = EnumFlowerType.EMPTY; } else if (block == Blocks.tallgrass) { switch (i) { case 0: blockflowerpot$enumflowertype = EnumFlowerType.DEAD_BUSH; return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);case 2: blockflowerpot$enumflowertype = EnumFlowerType.FERN; return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype); }  blockflowerpot$enumflowertype = EnumFlowerType.EMPTY; } else if (block == Blocks.yellow_flower) { blockflowerpot$enumflowertype = EnumFlowerType.DANDELION; } else if (block == Blocks.red_flower) { switch (BlockFlower.EnumFlowerType.getType(BlockFlower.EnumFlowerColor.RED, i)) { case POPPY: blockflowerpot$enumflowertype = EnumFlowerType.POPPY; return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);case BLUE_ORCHID: blockflowerpot$enumflowertype = EnumFlowerType.BLUE_ORCHID; return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);case ALLIUM: blockflowerpot$enumflowertype = EnumFlowerType.ALLIUM; return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);case HOUSTONIA: blockflowerpot$enumflowertype = EnumFlowerType.HOUSTONIA; return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);case RED_TULIP: blockflowerpot$enumflowertype = EnumFlowerType.RED_TULIP; return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);case ORANGE_TULIP: blockflowerpot$enumflowertype = EnumFlowerType.ORANGE_TULIP; return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);case WHITE_TULIP: blockflowerpot$enumflowertype = EnumFlowerType.WHITE_TULIP; return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);case PINK_TULIP: blockflowerpot$enumflowertype = EnumFlowerType.PINK_TULIP; return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);case OXEYE_DAISY: blockflowerpot$enumflowertype = EnumFlowerType.OXEYE_DAISY; return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype); }  blockflowerpot$enumflowertype = EnumFlowerType.EMPTY; } else if (block == Blocks.red_mushroom) { blockflowerpot$enumflowertype = EnumFlowerType.MUSHROOM_RED; } else if (block == Blocks.brown_mushroom) { blockflowerpot$enumflowertype = EnumFlowerType.MUSHROOM_BROWN; } else if (block == Blocks.deadbush) { blockflowerpot$enumflowertype = EnumFlowerType.DEAD_BUSH; } else if (block == Blocks.cactus) { blockflowerpot$enumflowertype = EnumFlowerType.CACTUS; }  }  }  return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer() {
/* 418 */     return EnumWorldBlockLayer.CUTOUT;
/*     */   }
/*     */   
/*     */   public enum EnumFlowerType
/*     */     implements IStringSerializable {
/* 423 */     EMPTY("empty"),
/* 424 */     POPPY("rose"),
/* 425 */     BLUE_ORCHID("blue_orchid"),
/* 426 */     ALLIUM("allium"),
/* 427 */     HOUSTONIA("houstonia"),
/* 428 */     RED_TULIP("red_tulip"),
/* 429 */     ORANGE_TULIP("orange_tulip"),
/* 430 */     WHITE_TULIP("white_tulip"),
/* 431 */     PINK_TULIP("pink_tulip"),
/* 432 */     OXEYE_DAISY("oxeye_daisy"),
/* 433 */     DANDELION("dandelion"),
/* 434 */     OAK_SAPLING("oak_sapling"),
/* 435 */     SPRUCE_SAPLING("spruce_sapling"),
/* 436 */     BIRCH_SAPLING("birch_sapling"),
/* 437 */     JUNGLE_SAPLING("jungle_sapling"),
/* 438 */     ACACIA_SAPLING("acacia_sapling"),
/* 439 */     DARK_OAK_SAPLING("dark_oak_sapling"),
/* 440 */     MUSHROOM_RED("mushroom_red"),
/* 441 */     MUSHROOM_BROWN("mushroom_brown"),
/* 442 */     DEAD_BUSH("dead_bush"),
/* 443 */     FERN("fern"),
/* 444 */     CACTUS("cactus");
/*     */     
/*     */     private final String name;
/*     */ 
/*     */     
/*     */     EnumFlowerType(String name) {
/* 450 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 455 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/* 460 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockFlowerPot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */