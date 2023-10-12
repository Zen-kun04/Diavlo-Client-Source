/*     */ package net.minecraft.item;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.BlockDispenser;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
/*     */ import net.minecraft.dispenser.IBehaviorDispenseItem;
/*     */ import net.minecraft.dispenser.IBlockSource;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EntitySelectors;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ItemArmor extends Item {
/*  23 */   private static final int[] maxDamageArray = new int[] { 11, 16, 15, 13 };
/*  24 */   public static final String[] EMPTY_SLOT_NAMES = new String[] { "minecraft:items/empty_armor_slot_helmet", "minecraft:items/empty_armor_slot_chestplate", "minecraft:items/empty_armor_slot_leggings", "minecraft:items/empty_armor_slot_boots" };
/*  25 */   private static final IBehaviorDispenseItem dispenserBehavior = (IBehaviorDispenseItem)new BehaviorDefaultDispenseItem()
/*     */     {
/*     */       protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
/*     */       {
/*  29 */         BlockPos blockpos = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
/*  30 */         int i = blockpos.getX();
/*  31 */         int j = blockpos.getY();
/*  32 */         int k = blockpos.getZ();
/*  33 */         AxisAlignedBB axisalignedbb = new AxisAlignedBB(i, j, k, (i + 1), (j + 1), (k + 1));
/*  34 */         List<EntityLivingBase> list = source.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb, Predicates.and(EntitySelectors.NOT_SPECTATING, (Predicate)new EntitySelectors.ArmoredMob(stack)));
/*     */         
/*  36 */         if (list.size() > 0) {
/*     */           
/*  38 */           EntityLivingBase entitylivingbase = list.get(0);
/*  39 */           int l = (entitylivingbase instanceof EntityPlayer) ? 1 : 0;
/*  40 */           int i1 = EntityLiving.getArmorPosition(stack);
/*  41 */           ItemStack itemstack = stack.copy();
/*  42 */           itemstack.stackSize = 1;
/*  43 */           entitylivingbase.setCurrentItemOrArmor(i1 - l, itemstack);
/*     */           
/*  45 */           if (entitylivingbase instanceof EntityLiving)
/*     */           {
/*  47 */             ((EntityLiving)entitylivingbase).setEquipmentDropChance(i1, 2.0F);
/*     */           }
/*     */           
/*  50 */           stack.stackSize--;
/*  51 */           return stack;
/*     */         } 
/*     */ 
/*     */         
/*  55 */         return super.dispenseStack(source, stack);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   public final int armorType;
/*     */   public final int damageReduceAmount;
/*     */   public final int renderIndex;
/*     */   private final ArmorMaterial material;
/*     */   
/*     */   public ItemArmor(ArmorMaterial material, int renderIndex, int armorType) {
/*  66 */     this.material = material;
/*  67 */     this.armorType = armorType;
/*  68 */     this.renderIndex = renderIndex;
/*  69 */     this.damageReduceAmount = material.getDamageReductionAmount(armorType);
/*  70 */     setMaxDamage(material.getDurability(armorType));
/*  71 */     this.maxStackSize = 1;
/*  72 */     setCreativeTab(CreativeTabs.tabCombat);
/*  73 */     BlockDispenser.dispenseBehaviorRegistry.putObject(this, dispenserBehavior);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getColorFromItemStack(ItemStack stack, int renderPass) {
/*  78 */     if (renderPass > 0)
/*     */     {
/*  80 */       return 16777215;
/*     */     }
/*     */ 
/*     */     
/*  84 */     int i = getColor(stack);
/*     */     
/*  86 */     if (i < 0)
/*     */     {
/*  88 */       i = 16777215;
/*     */     }
/*     */     
/*  91 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getItemEnchantability() {
/*  97 */     return this.material.getEnchantability();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArmorMaterial getArmorMaterial() {
/* 102 */     return this.material;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasColor(ItemStack stack) {
/* 107 */     return (this.material != ArmorMaterial.LEATHER) ? false : (!stack.hasTagCompound() ? false : (!stack.getTagCompound().hasKey("display", 10) ? false : stack.getTagCompound().getCompoundTag("display").hasKey("color", 3)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getColor(ItemStack stack) {
/* 112 */     if (this.material != ArmorMaterial.LEATHER)
/*     */     {
/* 114 */       return -1;
/*     */     }
/*     */ 
/*     */     
/* 118 */     NBTTagCompound nbttagcompound = stack.getTagCompound();
/*     */     
/* 120 */     if (nbttagcompound != null) {
/*     */       
/* 122 */       NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");
/*     */       
/* 124 */       if (nbttagcompound1 != null && nbttagcompound1.hasKey("color", 3))
/*     */       {
/* 126 */         return nbttagcompound1.getInteger("color");
/*     */       }
/*     */     } 
/*     */     
/* 130 */     return 10511680;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeColor(ItemStack stack) {
/* 136 */     if (this.material == ArmorMaterial.LEATHER) {
/*     */       
/* 138 */       NBTTagCompound nbttagcompound = stack.getTagCompound();
/*     */       
/* 140 */       if (nbttagcompound != null) {
/*     */         
/* 142 */         NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");
/*     */         
/* 144 */         if (nbttagcompound1.hasKey("color"))
/*     */         {
/* 146 */           nbttagcompound1.removeTag("color");
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setColor(ItemStack stack, int color) {
/* 154 */     if (this.material != ArmorMaterial.LEATHER)
/*     */     {
/* 156 */       throw new UnsupportedOperationException("Can't dye non-leather!");
/*     */     }
/*     */ 
/*     */     
/* 160 */     NBTTagCompound nbttagcompound = stack.getTagCompound();
/*     */     
/* 162 */     if (nbttagcompound == null) {
/*     */       
/* 164 */       nbttagcompound = new NBTTagCompound();
/* 165 */       stack.setTagCompound(nbttagcompound);
/*     */     } 
/*     */     
/* 168 */     NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");
/*     */     
/* 170 */     if (!nbttagcompound.hasKey("display", 10))
/*     */     {
/* 172 */       nbttagcompound.setTag("display", (NBTBase)nbttagcompound1);
/*     */     }
/*     */     
/* 175 */     nbttagcompound1.setInteger("color", color);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
/* 181 */     return (this.material.getRepairItem() == repair.getItem()) ? true : super.getIsRepairable(toRepair, repair);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
/* 186 */     int i = EntityLiving.getArmorPosition(itemStackIn) - 1;
/* 187 */     ItemStack itemstack = playerIn.getCurrentArmor(i);
/*     */     
/* 189 */     if (itemstack == null) {
/*     */       
/* 191 */       playerIn.setCurrentItemOrArmor(i, itemStackIn.copy());
/* 192 */       itemStackIn.stackSize = 0;
/*     */     } 
/*     */     
/* 195 */     return itemStackIn;
/*     */   }
/*     */   
/*     */   public enum ArmorMaterial
/*     */   {
/* 200 */     LEATHER("leather", 5, (String)new int[] { 1, 3, 2, 1 }, 15),
/* 201 */     CHAIN("chainmail", 15, (String)new int[] { 2, 5, 4, 1 }, 12),
/* 202 */     IRON("iron", 15, (String)new int[] { 2, 6, 5, 2 }, 9),
/* 203 */     GOLD("gold", 7, (String)new int[] { 2, 5, 3, 1 }, 25),
/* 204 */     DIAMOND("diamond", 33, (String)new int[] { 3, 8, 6, 3 }, 10);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private final int maxDamageFactor;
/*     */     private final int[] damageReductionAmountArray;
/*     */     private final int enchantability;
/*     */     
/*     */     ArmorMaterial(String name, int maxDamage, int[] reductionAmounts, int enchantability) {
/* 213 */       this.name = name;
/* 214 */       this.maxDamageFactor = maxDamage;
/* 215 */       this.damageReductionAmountArray = reductionAmounts;
/* 216 */       this.enchantability = enchantability;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getDurability(int armorType) {
/* 221 */       return ItemArmor.maxDamageArray[armorType] * this.maxDamageFactor;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getDamageReductionAmount(int armorType) {
/* 226 */       return this.damageReductionAmountArray[armorType];
/*     */     }
/*     */ 
/*     */     
/*     */     public int getEnchantability() {
/* 231 */       return this.enchantability;
/*     */     }
/*     */ 
/*     */     
/*     */     public Item getRepairItem() {
/* 236 */       return (this == LEATHER) ? Items.leather : ((this == CHAIN) ? Items.iron_ingot : ((this == GOLD) ? Items.gold_ingot : ((this == IRON) ? Items.iron_ingot : ((this == DIAMOND) ? Items.diamond : null))));
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/* 241 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemArmor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */