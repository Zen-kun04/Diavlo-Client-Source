/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.ColorizerFoliage;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockOldLeaf
/*     */   extends BlockLeaves {
/*  23 */   public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class, new Predicate<BlockPlanks.EnumType>()
/*     */       {
/*     */         public boolean apply(BlockPlanks.EnumType p_apply_1_)
/*     */         {
/*  27 */           return (p_apply_1_.getMetadata() < 4);
/*     */         }
/*     */       });
/*     */ 
/*     */   
/*     */   public BlockOldLeaf() {
/*  33 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)VARIANT, BlockPlanks.EnumType.OAK).withProperty((IProperty)CHECK_DECAY, Boolean.valueOf(true)).withProperty((IProperty)DECAYABLE, Boolean.valueOf(true)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRenderColor(IBlockState state) {
/*  38 */     if (state.getBlock() != this)
/*     */     {
/*  40 */       return super.getRenderColor(state);
/*     */     }
/*     */ 
/*     */     
/*  44 */     BlockPlanks.EnumType blockplanks$enumtype = (BlockPlanks.EnumType)state.getValue((IProperty)VARIANT);
/*  45 */     return (blockplanks$enumtype == BlockPlanks.EnumType.SPRUCE) ? ColorizerFoliage.getFoliageColorPine() : ((blockplanks$enumtype == BlockPlanks.EnumType.BIRCH) ? ColorizerFoliage.getFoliageColorBirch() : super.getRenderColor(state));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
/*  51 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */     
/*  53 */     if (iblockstate.getBlock() == this) {
/*     */       
/*  55 */       BlockPlanks.EnumType blockplanks$enumtype = (BlockPlanks.EnumType)iblockstate.getValue((IProperty)VARIANT);
/*     */       
/*  57 */       if (blockplanks$enumtype == BlockPlanks.EnumType.SPRUCE)
/*     */       {
/*  59 */         return ColorizerFoliage.getFoliageColorPine();
/*     */       }
/*     */       
/*  62 */       if (blockplanks$enumtype == BlockPlanks.EnumType.BIRCH)
/*     */       {
/*  64 */         return ColorizerFoliage.getFoliageColorBirch();
/*     */       }
/*     */     } 
/*     */     
/*  68 */     return super.colorMultiplier(worldIn, pos, renderPass);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance) {
/*  73 */     if (state.getValue((IProperty)VARIANT) == BlockPlanks.EnumType.OAK && worldIn.rand.nextInt(chance) == 0)
/*     */     {
/*  75 */       spawnAsEntity(worldIn, pos, new ItemStack(Items.apple, 1, 0));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSaplingDropChance(IBlockState state) {
/*  81 */     return (state.getValue((IProperty)VARIANT) == BlockPlanks.EnumType.JUNGLE) ? 40 : super.getSaplingDropChance(state);
/*     */   }
/*     */ 
/*     */   
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
/*  86 */     list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.OAK.getMetadata()));
/*  87 */     list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()));
/*  88 */     list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.BIRCH.getMetadata()));
/*  89 */     list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack createStackedBlock(IBlockState state) {
/*  94 */     return new ItemStack(Item.getItemFromBlock(this), 1, ((BlockPlanks.EnumType)state.getValue((IProperty)VARIANT)).getMetadata());
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/*  99 */     return getDefaultState().withProperty((IProperty)VARIANT, getWoodType(meta)).withProperty((IProperty)DECAYABLE, Boolean.valueOf(((meta & 0x4) == 0))).withProperty((IProperty)CHECK_DECAY, Boolean.valueOf(((meta & 0x8) > 0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 104 */     int i = 0;
/* 105 */     i |= ((BlockPlanks.EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
/*     */     
/* 107 */     if (!((Boolean)state.getValue((IProperty)DECAYABLE)).booleanValue())
/*     */     {
/* 109 */       i |= 0x4;
/*     */     }
/*     */     
/* 112 */     if (((Boolean)state.getValue((IProperty)CHECK_DECAY)).booleanValue())
/*     */     {
/* 114 */       i |= 0x8;
/*     */     }
/*     */     
/* 117 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPlanks.EnumType getWoodType(int meta) {
/* 122 */     return BlockPlanks.EnumType.byMetadata((meta & 0x3) % 4);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 127 */     return new BlockState(this, new IProperty[] { (IProperty)VARIANT, (IProperty)CHECK_DECAY, (IProperty)DECAYABLE });
/*     */   }
/*     */ 
/*     */   
/*     */   public int damageDropped(IBlockState state) {
/* 132 */     return ((BlockPlanks.EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
/*     */   }
/*     */ 
/*     */   
/*     */   public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
/* 137 */     if (!worldIn.isRemote && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.shears) {
/*     */       
/* 139 */       player.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
/* 140 */       spawnAsEntity(worldIn, pos, new ItemStack(Item.getItemFromBlock(this), 1, ((BlockPlanks.EnumType)state.getValue((IProperty)VARIANT)).getMetadata()));
/*     */     }
/*     */     else {
/*     */       
/* 144 */       super.harvestBlock(worldIn, player, pos, state, te);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockOldLeaf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */