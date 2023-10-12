/*     */ package net.minecraft.item;
/*     */ 
/*     */ import com.google.common.collect.HashMultimap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.entity.ai.attributes.IAttribute;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.projectile.EntityPotion;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.potion.PotionHelper;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.StatCollector;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ItemPotion
/*     */   extends Item
/*     */ {
/*  30 */   private Map<Integer, List<PotionEffect>> effectCache = Maps.newHashMap();
/*  31 */   private static final Map<List<PotionEffect>, Integer> SUB_ITEMS_CACHE = Maps.newLinkedHashMap();
/*     */ 
/*     */   
/*     */   public ItemPotion() {
/*  35 */     setMaxStackSize(1);
/*  36 */     setHasSubtypes(true);
/*  37 */     setMaxDamage(0);
/*  38 */     setCreativeTab(CreativeTabs.tabBrewing);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<PotionEffect> getEffects(ItemStack stack) {
/*  43 */     if (stack.hasTagCompound() && stack.getTagCompound().hasKey("CustomPotionEffects", 9)) {
/*     */       
/*  45 */       List<PotionEffect> list1 = Lists.newArrayList();
/*  46 */       NBTTagList nbttaglist = stack.getTagCompound().getTagList("CustomPotionEffects", 10);
/*     */       
/*  48 */       for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*     */         
/*  50 */         NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/*  51 */         PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttagcompound);
/*     */         
/*  53 */         if (potioneffect != null)
/*     */         {
/*  55 */           list1.add(potioneffect);
/*     */         }
/*     */       } 
/*     */       
/*  59 */       return list1;
/*     */     } 
/*     */ 
/*     */     
/*  63 */     List<PotionEffect> list = this.effectCache.get(Integer.valueOf(stack.getMetadata()));
/*     */     
/*  65 */     if (list == null) {
/*     */       
/*  67 */       list = PotionHelper.getPotionEffects(stack.getMetadata(), false);
/*  68 */       this.effectCache.put(Integer.valueOf(stack.getMetadata()), list);
/*     */     } 
/*     */     
/*  71 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<PotionEffect> getEffects(int meta) {
/*  77 */     List<PotionEffect> list = this.effectCache.get(Integer.valueOf(meta));
/*     */     
/*  79 */     if (list == null) {
/*     */       
/*  81 */       list = PotionHelper.getPotionEffects(meta, false);
/*  82 */       this.effectCache.put(Integer.valueOf(meta), list);
/*     */     } 
/*     */     
/*  85 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn) {
/*  90 */     if (!playerIn.capabilities.isCreativeMode)
/*     */     {
/*  92 */       stack.stackSize--;
/*     */     }
/*     */     
/*  95 */     if (!worldIn.isRemote) {
/*     */       
/*  97 */       List<PotionEffect> list = getEffects(stack);
/*     */       
/*  99 */       if (list != null)
/*     */       {
/* 101 */         for (PotionEffect potioneffect : list)
/*     */         {
/* 103 */           playerIn.addPotionEffect(new PotionEffect(potioneffect));
/*     */         }
/*     */       }
/*     */     } 
/*     */     
/* 108 */     playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
/*     */     
/* 110 */     if (!playerIn.capabilities.isCreativeMode) {
/*     */       
/* 112 */       if (stack.stackSize <= 0)
/*     */       {
/* 114 */         return new ItemStack(Items.glass_bottle);
/*     */       }
/*     */       
/* 117 */       playerIn.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
/*     */     } 
/*     */     
/* 120 */     return stack;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxItemUseDuration(ItemStack stack) {
/* 125 */     return 32;
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumAction getItemUseAction(ItemStack stack) {
/* 130 */     return EnumAction.DRINK;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
/* 135 */     if (isSplash(itemStackIn.getMetadata())) {
/*     */       
/* 137 */       if (!playerIn.capabilities.isCreativeMode)
/*     */       {
/* 139 */         itemStackIn.stackSize--;
/*     */       }
/*     */       
/* 142 */       worldIn.playSoundAtEntity((Entity)playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
/*     */       
/* 144 */       if (!worldIn.isRemote)
/*     */       {
/* 146 */         worldIn.spawnEntityInWorld((Entity)new EntityPotion(worldIn, (EntityLivingBase)playerIn, itemStackIn));
/*     */       }
/*     */       
/* 149 */       playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
/* 150 */       return itemStackIn;
/*     */     } 
/*     */ 
/*     */     
/* 154 */     playerIn.setItemInUse(itemStackIn, getMaxItemUseDuration(itemStackIn));
/* 155 */     return itemStackIn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isSplash(int meta) {
/* 161 */     return ((meta & 0x4000) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getColorFromDamage(int meta) {
/* 166 */     return PotionHelper.getLiquidColor(meta, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getColorFromItemStack(ItemStack stack, int renderPass) {
/* 171 */     return (renderPass > 0) ? 16777215 : getColorFromDamage(stack.getMetadata());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEffectInstant(int meta) {
/* 176 */     List<PotionEffect> list = getEffects(meta);
/*     */     
/* 178 */     if (list != null && !list.isEmpty()) {
/*     */       
/* 180 */       for (PotionEffect potioneffect : list) {
/*     */         
/* 182 */         if (Potion.potionTypes[potioneffect.getPotionID()].isInstant())
/*     */         {
/* 184 */           return true;
/*     */         }
/*     */       } 
/*     */       
/* 188 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 192 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getItemStackDisplayName(ItemStack stack) {
/* 198 */     if (stack.getMetadata() == 0)
/*     */     {
/* 200 */       return StatCollector.translateToLocal("item.emptyPotion.name").trim();
/*     */     }
/*     */ 
/*     */     
/* 204 */     String s = "";
/*     */     
/* 206 */     if (isSplash(stack.getMetadata()))
/*     */     {
/* 208 */       s = StatCollector.translateToLocal("potion.prefix.grenade").trim() + " ";
/*     */     }
/*     */     
/* 211 */     List<PotionEffect> list = Items.potionitem.getEffects(stack);
/*     */     
/* 213 */     if (list != null && !list.isEmpty()) {
/*     */       
/* 215 */       String s2 = ((PotionEffect)list.get(0)).getEffectName();
/* 216 */       s2 = s2 + ".postfix";
/* 217 */       return s + StatCollector.translateToLocal(s2).trim();
/*     */     } 
/*     */ 
/*     */     
/* 221 */     String s1 = PotionHelper.getPotionPrefix(stack.getMetadata());
/* 222 */     return StatCollector.translateToLocal(s1).trim() + " " + super.getItemStackDisplayName(stack);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
/* 229 */     if (stack.getMetadata() != 0) {
/*     */       
/* 231 */       List<PotionEffect> list = Items.potionitem.getEffects(stack);
/* 232 */       HashMultimap hashMultimap = HashMultimap.create();
/*     */       
/* 234 */       if (list != null && !list.isEmpty()) {
/*     */         
/* 236 */         for (PotionEffect potioneffect : list)
/*     */         {
/* 238 */           String s1 = StatCollector.translateToLocal(potioneffect.getEffectName()).trim();
/* 239 */           Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
/* 240 */           Map<IAttribute, AttributeModifier> map = potion.getAttributeModifierMap();
/*     */           
/* 242 */           if (map != null && map.size() > 0)
/*     */           {
/* 244 */             for (Map.Entry<IAttribute, AttributeModifier> entry : map.entrySet()) {
/*     */               
/* 246 */               AttributeModifier attributemodifier = entry.getValue();
/* 247 */               AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), potion.getAttributeModifierAmount(potioneffect.getAmplifier(), attributemodifier), attributemodifier.getOperation());
/* 248 */               hashMultimap.put(((IAttribute)entry.getKey()).getAttributeUnlocalizedName(), attributemodifier1);
/*     */             } 
/*     */           }
/*     */           
/* 252 */           if (potioneffect.getAmplifier() > 0)
/*     */           {
/* 254 */             s1 = s1 + " " + StatCollector.translateToLocal("potion.potency." + potioneffect.getAmplifier()).trim();
/*     */           }
/*     */           
/* 257 */           if (potioneffect.getDuration() > 20)
/*     */           {
/* 259 */             s1 = s1 + " (" + Potion.getDurationString(potioneffect) + ")";
/*     */           }
/*     */           
/* 262 */           if (potion.isBadEffect()) {
/*     */             
/* 264 */             tooltip.add(EnumChatFormatting.RED + s1);
/*     */             
/*     */             continue;
/*     */           } 
/* 268 */           tooltip.add(EnumChatFormatting.GRAY + s1);
/*     */         }
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 274 */         String s = StatCollector.translateToLocal("potion.empty").trim();
/* 275 */         tooltip.add(EnumChatFormatting.GRAY + s);
/*     */       } 
/*     */       
/* 278 */       if (!hashMultimap.isEmpty()) {
/*     */         
/* 280 */         tooltip.add("");
/* 281 */         tooltip.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("potion.effects.whenDrank"));
/*     */         
/* 283 */         for (Map.Entry<String, AttributeModifier> entry1 : (Iterable<Map.Entry<String, AttributeModifier>>)hashMultimap.entries()) {
/*     */           double d1;
/* 285 */           AttributeModifier attributemodifier2 = entry1.getValue();
/* 286 */           double d0 = attributemodifier2.getAmount();
/*     */ 
/*     */           
/* 289 */           if (attributemodifier2.getOperation() != 1 && attributemodifier2.getOperation() != 2) {
/*     */             
/* 291 */             d1 = attributemodifier2.getAmount();
/*     */           }
/*     */           else {
/*     */             
/* 295 */             d1 = attributemodifier2.getAmount() * 100.0D;
/*     */           } 
/*     */           
/* 298 */           if (d0 > 0.0D) {
/*     */             
/* 300 */             tooltip.add(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted("attribute.modifier.plus." + attributemodifier2.getOperation(), new Object[] { ItemStack.DECIMALFORMAT.format(d1), StatCollector.translateToLocal("attribute.name." + (String)entry1.getKey()) })); continue;
/*     */           } 
/* 302 */           if (d0 < 0.0D) {
/*     */             
/* 304 */             d1 *= -1.0D;
/* 305 */             tooltip.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted("attribute.modifier.take." + attributemodifier2.getOperation(), new Object[] { ItemStack.DECIMALFORMAT.format(d1), StatCollector.translateToLocal("attribute.name." + (String)entry1.getKey()) }));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEffect(ItemStack stack) {
/* 314 */     List<PotionEffect> list = getEffects(stack);
/* 315 */     return (list != null && !list.isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
/* 320 */     super.getSubItems(itemIn, tab, subItems);
/*     */     
/* 322 */     if (SUB_ITEMS_CACHE.isEmpty())
/*     */     {
/* 324 */       for (int i = 0; i <= 15; i++) {
/*     */         
/* 326 */         for (int j = 0; j <= 1; j++) {
/*     */           int lvt_6_1_;
/*     */ 
/*     */           
/* 330 */           if (j == 0) {
/*     */             
/* 332 */             lvt_6_1_ = i | 0x2000;
/*     */           }
/*     */           else {
/*     */             
/* 336 */             lvt_6_1_ = i | 0x4000;
/*     */           } 
/*     */           
/* 339 */           for (int l = 0; l <= 2; l++) {
/*     */             
/* 341 */             int i1 = lvt_6_1_;
/*     */             
/* 343 */             if (l != 0)
/*     */             {
/* 345 */               if (l == 1) {
/*     */                 
/* 347 */                 i1 = lvt_6_1_ | 0x20;
/*     */               }
/* 349 */               else if (l == 2) {
/*     */                 
/* 351 */                 i1 = lvt_6_1_ | 0x40;
/*     */               } 
/*     */             }
/*     */             
/* 355 */             List<PotionEffect> list = PotionHelper.getPotionEffects(i1, false);
/*     */             
/* 357 */             if (list != null && !list.isEmpty())
/*     */             {
/* 359 */               SUB_ITEMS_CACHE.put(list, Integer.valueOf(i1));
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 366 */     Iterator<Integer> iterator = SUB_ITEMS_CACHE.values().iterator();
/*     */     
/* 368 */     while (iterator.hasNext()) {
/*     */       
/* 370 */       int j1 = ((Integer)iterator.next()).intValue();
/* 371 */       subItems.add(new ItemStack(itemIn, 1, j1));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemPotion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */