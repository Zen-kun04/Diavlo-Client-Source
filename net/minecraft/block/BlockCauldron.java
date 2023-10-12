/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyInteger;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.tileentity.TileEntityBanner;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockCauldron
/*     */   extends Block
/*     */ {
/*  30 */   public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 3);
/*     */ 
/*     */   
/*     */   public BlockCauldron() {
/*  34 */     super(Material.iron, MapColor.stoneColor);
/*  35 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)LEVEL, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
/*  40 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
/*  41 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*  42 */     float f = 0.125F;
/*  43 */     setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
/*  44 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*  45 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
/*  46 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*  47 */     setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*  48 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*  49 */     setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
/*  50 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*  51 */     setBlockBoundsForItemRender();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockBoundsForItemRender() {
/*  56 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/*  61 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/*  66 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
/*  71 */     int i = ((Integer)state.getValue((IProperty)LEVEL)).intValue();
/*  72 */     float f = pos.getY() + (6.0F + (3 * i)) / 16.0F;
/*     */     
/*  74 */     if (!worldIn.isRemote && entityIn.isBurning() && i > 0 && (entityIn.getEntityBoundingBox()).minY <= f) {
/*     */       
/*  76 */       entityIn.extinguish();
/*  77 */       setWaterLevel(worldIn, pos, state, i - 1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
/*  83 */     if (worldIn.isRemote)
/*     */     {
/*  85 */       return true;
/*     */     }
/*     */ 
/*     */     
/*  89 */     ItemStack itemstack = playerIn.inventory.getCurrentItem();
/*     */     
/*  91 */     if (itemstack == null)
/*     */     {
/*  93 */       return true;
/*     */     }
/*     */ 
/*     */     
/*  97 */     int i = ((Integer)state.getValue((IProperty)LEVEL)).intValue();
/*  98 */     Item item = itemstack.getItem();
/*     */     
/* 100 */     if (item == Items.water_bucket) {
/*     */       
/* 102 */       if (i < 3) {
/*     */         
/* 104 */         if (!playerIn.capabilities.isCreativeMode)
/*     */         {
/* 106 */           playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, new ItemStack(Items.bucket));
/*     */         }
/*     */         
/* 109 */         playerIn.triggerAchievement(StatList.field_181725_I);
/* 110 */         setWaterLevel(worldIn, pos, state, 3);
/*     */       } 
/*     */       
/* 113 */       return true;
/*     */     } 
/* 115 */     if (item == Items.glass_bottle) {
/*     */       
/* 117 */       if (i > 0) {
/*     */         
/* 119 */         if (!playerIn.capabilities.isCreativeMode) {
/*     */           
/* 121 */           ItemStack itemstack2 = new ItemStack((Item)Items.potionitem, 1, 0);
/*     */           
/* 123 */           if (!playerIn.inventory.addItemStackToInventory(itemstack2)) {
/*     */             
/* 125 */             worldIn.spawnEntityInWorld((Entity)new EntityItem(worldIn, pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, itemstack2));
/*     */           }
/* 127 */           else if (playerIn instanceof EntityPlayerMP) {
/*     */             
/* 129 */             ((EntityPlayerMP)playerIn).sendContainerToPlayer(playerIn.inventoryContainer);
/*     */           } 
/*     */           
/* 132 */           playerIn.triggerAchievement(StatList.field_181726_J);
/* 133 */           itemstack.stackSize--;
/*     */           
/* 135 */           if (itemstack.stackSize <= 0)
/*     */           {
/* 137 */             playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, (ItemStack)null);
/*     */           }
/*     */         } 
/*     */         
/* 141 */         setWaterLevel(worldIn, pos, state, i - 1);
/*     */       } 
/*     */       
/* 144 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 148 */     if (i > 0 && item instanceof ItemArmor) {
/*     */       
/* 150 */       ItemArmor itemarmor = (ItemArmor)item;
/*     */       
/* 152 */       if (itemarmor.getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER && itemarmor.hasColor(itemstack)) {
/*     */         
/* 154 */         itemarmor.removeColor(itemstack);
/* 155 */         setWaterLevel(worldIn, pos, state, i - 1);
/* 156 */         playerIn.triggerAchievement(StatList.field_181727_K);
/* 157 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 161 */     if (i > 0 && item instanceof net.minecraft.item.ItemBanner && TileEntityBanner.getPatterns(itemstack) > 0) {
/*     */       
/* 163 */       ItemStack itemstack1 = itemstack.copy();
/* 164 */       itemstack1.stackSize = 1;
/* 165 */       TileEntityBanner.removeBannerData(itemstack1);
/*     */       
/* 167 */       if (itemstack.stackSize <= 1 && !playerIn.capabilities.isCreativeMode) {
/*     */         
/* 169 */         playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, itemstack1);
/*     */       }
/*     */       else {
/*     */         
/* 173 */         if (!playerIn.inventory.addItemStackToInventory(itemstack1)) {
/*     */           
/* 175 */           worldIn.spawnEntityInWorld((Entity)new EntityItem(worldIn, pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, itemstack1));
/*     */         }
/* 177 */         else if (playerIn instanceof EntityPlayerMP) {
/*     */           
/* 179 */           ((EntityPlayerMP)playerIn).sendContainerToPlayer(playerIn.inventoryContainer);
/*     */         } 
/*     */         
/* 182 */         playerIn.triggerAchievement(StatList.field_181728_L);
/*     */         
/* 184 */         if (!playerIn.capabilities.isCreativeMode)
/*     */         {
/* 186 */           itemstack.stackSize--;
/*     */         }
/*     */       } 
/*     */       
/* 190 */       if (!playerIn.capabilities.isCreativeMode)
/*     */       {
/* 192 */         setWaterLevel(worldIn, pos, state, i - 1);
/*     */       }
/*     */       
/* 195 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 199 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWaterLevel(World worldIn, BlockPos pos, IBlockState state, int level) {
/* 208 */     worldIn.setBlockState(pos, state.withProperty((IProperty)LEVEL, Integer.valueOf(MathHelper.clamp_int(level, 0, 3))), 2);
/* 209 */     worldIn.updateComparatorOutputLevel(pos, this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillWithRain(World worldIn, BlockPos pos) {
/* 214 */     if (worldIn.rand.nextInt(20) == 1) {
/*     */       
/* 216 */       IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */       
/* 218 */       if (((Integer)iblockstate.getValue((IProperty)LEVEL)).intValue() < 3)
/*     */       {
/* 220 */         worldIn.setBlockState(pos, iblockstate.cycleProperty((IProperty)LEVEL), 2);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/* 227 */     return Items.cauldron;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos) {
/* 232 */     return Items.cauldron;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasComparatorInputOverride() {
/* 237 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getComparatorInputOverride(World worldIn, BlockPos pos) {
/* 242 */     return ((Integer)worldIn.getBlockState(pos).getValue((IProperty)LEVEL)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 247 */     return getDefaultState().withProperty((IProperty)LEVEL, Integer.valueOf(meta));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 252 */     return ((Integer)state.getValue((IProperty)LEVEL)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 257 */     return new BlockState(this, new IProperty[] { (IProperty)LEVEL });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockCauldron.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */