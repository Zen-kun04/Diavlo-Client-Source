/*     */ package net.minecraft.item;
/*     */ import com.google.common.collect.HashMultimap;
/*     */ import com.google.common.collect.Multimap;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.enchantment.Enchantment;
/*     */ import net.minecraft.enchantment.EnchantmentDurability;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.entity.item.EntityItemFrame;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.event.HoverEvent;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.StatCollector;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public final class ItemStack {
/*  33 */   public static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.###");
/*     */   
/*     */   public int stackSize;
/*     */   public int animationsToGo;
/*     */   private Item item;
/*     */   private NBTTagCompound stackTagCompound;
/*     */   private int itemDamage;
/*     */   private EntityItemFrame itemFrame;
/*     */   private Block canDestroyCacheBlock;
/*     */   private boolean canDestroyCacheResult;
/*     */   private Block canPlaceOnCacheBlock;
/*     */   private boolean canPlaceOnCacheResult;
/*     */   
/*     */   public ItemStack(Block blockIn) {
/*  47 */     this(blockIn, 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack(Block blockIn, int amount) {
/*  52 */     this(blockIn, amount, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack(Block blockIn, int amount, int meta) {
/*  57 */     this(Item.getItemFromBlock(blockIn), amount, meta);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack(Item itemIn) {
/*  62 */     this(itemIn, 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack(Item itemIn, int amount) {
/*  67 */     this(itemIn, amount, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack(Item itemIn, int amount, int meta) {
/*  72 */     this.canDestroyCacheBlock = null;
/*  73 */     this.canDestroyCacheResult = false;
/*  74 */     this.canPlaceOnCacheBlock = null;
/*  75 */     this.canPlaceOnCacheResult = false;
/*  76 */     this.item = itemIn;
/*  77 */     this.stackSize = amount;
/*  78 */     this.itemDamage = meta;
/*     */     
/*  80 */     if (this.itemDamage < 0)
/*     */     {
/*  82 */       this.itemDamage = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static ItemStack loadItemStackFromNBT(NBTTagCompound nbt) {
/*  88 */     ItemStack itemstack = new ItemStack();
/*  89 */     itemstack.readFromNBT(nbt);
/*  90 */     return (itemstack.getItem() != null) ? itemstack : null;
/*     */   }
/*     */ 
/*     */   
/*     */   private ItemStack() {
/*  95 */     this.canDestroyCacheBlock = null;
/*  96 */     this.canDestroyCacheResult = false;
/*  97 */     this.canPlaceOnCacheBlock = null;
/*  98 */     this.canPlaceOnCacheResult = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack splitStack(int amount) {
/* 103 */     ItemStack itemstack = new ItemStack(this.item, amount, this.itemDamage);
/*     */     
/* 105 */     if (this.stackTagCompound != null)
/*     */     {
/* 107 */       itemstack.stackTagCompound = (NBTTagCompound)this.stackTagCompound.copy();
/*     */     }
/*     */     
/* 110 */     this.stackSize -= amount;
/* 111 */     return itemstack;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItem() {
/* 116 */     return this.item;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
/* 121 */     boolean flag = getItem().onItemUse(this, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
/*     */     
/* 123 */     if (flag)
/*     */     {
/* 125 */       playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this.item)]);
/*     */     }
/*     */     
/* 128 */     return flag;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getStrVsBlock(Block blockIn) {
/* 133 */     return getItem().getStrVsBlock(this, blockIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack useItemRightClick(World worldIn, EntityPlayer playerIn) {
/* 138 */     return getItem().onItemRightClick(this, worldIn, playerIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack onItemUseFinish(World worldIn, EntityPlayer playerIn) {
/* 143 */     return getItem().onItemUseFinish(this, worldIn, playerIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
/* 148 */     ResourceLocation resourcelocation = (ResourceLocation)Item.itemRegistry.getNameForObject(this.item);
/* 149 */     nbt.setString("id", (resourcelocation == null) ? "minecraft:air" : resourcelocation.toString());
/* 150 */     nbt.setByte("Count", (byte)this.stackSize);
/* 151 */     nbt.setShort("Damage", (short)this.itemDamage);
/*     */     
/* 153 */     if (this.stackTagCompound != null)
/*     */     {
/* 155 */       nbt.setTag("tag", (NBTBase)this.stackTagCompound);
/*     */     }
/*     */     
/* 158 */     return nbt;
/*     */   }
/*     */ 
/*     */   
/*     */   public void readFromNBT(NBTTagCompound nbt) {
/* 163 */     if (nbt.hasKey("id", 8)) {
/*     */       
/* 165 */       this.item = Item.getByNameOrId(nbt.getString("id"));
/*     */     }
/*     */     else {
/*     */       
/* 169 */       this.item = Item.getItemById(nbt.getShort("id"));
/*     */     } 
/*     */     
/* 172 */     this.stackSize = nbt.getByte("Count");
/* 173 */     this.itemDamage = nbt.getShort("Damage");
/*     */     
/* 175 */     if (this.itemDamage < 0)
/*     */     {
/* 177 */       this.itemDamage = 0;
/*     */     }
/*     */     
/* 180 */     if (nbt.hasKey("tag", 10)) {
/*     */       
/* 182 */       this.stackTagCompound = nbt.getCompoundTag("tag");
/*     */       
/* 184 */       if (this.item != null)
/*     */       {
/* 186 */         this.item.updateItemStackNBT(this.stackTagCompound);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxStackSize() {
/* 193 */     return getItem().getItemStackLimit();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStackable() {
/* 198 */     return (getMaxStackSize() > 1 && (!isItemStackDamageable() || !isItemDamaged()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isItemStackDamageable() {
/* 203 */     return (this.item == null) ? false : ((this.item.getMaxDamage() <= 0) ? false : ((!hasTagCompound() || !getTagCompound().getBoolean("Unbreakable"))));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getHasSubtypes() {
/* 208 */     return this.item.getHasSubtypes();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isItemDamaged() {
/* 213 */     return (isItemStackDamageable() && this.itemDamage > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getItemDamage() {
/* 218 */     return this.itemDamage;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetadata() {
/* 223 */     return this.itemDamage;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItemDamage(int meta) {
/* 228 */     this.itemDamage = meta;
/*     */     
/* 230 */     if (this.itemDamage < 0)
/*     */     {
/* 232 */       this.itemDamage = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxDamage() {
/* 238 */     return this.item.getMaxDamage();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attemptDamageItem(int amount, Random rand) {
/* 243 */     if (!isItemStackDamageable())
/*     */     {
/* 245 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 249 */     if (amount > 0) {
/*     */       
/* 251 */       int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, this);
/* 252 */       int j = 0;
/*     */       
/* 254 */       for (int k = 0; i > 0 && k < amount; k++) {
/*     */         
/* 256 */         if (EnchantmentDurability.negateDamage(this, i, rand))
/*     */         {
/* 258 */           j++;
/*     */         }
/*     */       } 
/*     */       
/* 262 */       amount -= j;
/*     */       
/* 264 */       if (amount <= 0)
/*     */       {
/* 266 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 270 */     this.itemDamage += amount;
/* 271 */     return (this.itemDamage > getMaxDamage());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void damageItem(int amount, EntityLivingBase entityIn) {
/* 277 */     if (!(entityIn instanceof EntityPlayer) || !((EntityPlayer)entityIn).capabilities.isCreativeMode)
/*     */     {
/* 279 */       if (isItemStackDamageable())
/*     */       {
/* 281 */         if (attemptDamageItem(amount, entityIn.getRNG())) {
/*     */           
/* 283 */           entityIn.renderBrokenItemStack(this);
/* 284 */           this.stackSize--;
/*     */           
/* 286 */           if (entityIn instanceof EntityPlayer) {
/*     */             
/* 288 */             EntityPlayer entityplayer = (EntityPlayer)entityIn;
/* 289 */             entityplayer.triggerAchievement(StatList.objectBreakStats[Item.getIdFromItem(this.item)]);
/*     */             
/* 291 */             if (this.stackSize == 0 && getItem() instanceof ItemBow)
/*     */             {
/* 293 */               entityplayer.destroyCurrentEquippedItem();
/*     */             }
/*     */           } 
/*     */           
/* 297 */           if (this.stackSize < 0)
/*     */           {
/* 299 */             this.stackSize = 0;
/*     */           }
/*     */           
/* 302 */           this.itemDamage = 0;
/*     */         } 
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void hitEntity(EntityLivingBase entityIn, EntityPlayer playerIn) {
/* 310 */     boolean flag = this.item.hitEntity(this, entityIn, (EntityLivingBase)playerIn);
/*     */     
/* 312 */     if (flag)
/*     */     {
/* 314 */       playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this.item)]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockDestroyed(World worldIn, Block blockIn, BlockPos pos, EntityPlayer playerIn) {
/* 320 */     boolean flag = this.item.onBlockDestroyed(this, worldIn, blockIn, pos, (EntityLivingBase)playerIn);
/*     */     
/* 322 */     if (flag)
/*     */     {
/* 324 */       playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this.item)]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canHarvestBlock(Block blockIn) {
/* 330 */     return this.item.canHarvestBlock(blockIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean interactWithEntity(EntityPlayer playerIn, EntityLivingBase entityIn) {
/* 335 */     return this.item.itemInteractionForEntity(this, playerIn, entityIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack copy() {
/* 340 */     ItemStack itemstack = new ItemStack(this.item, this.stackSize, this.itemDamage);
/*     */     
/* 342 */     if (this.stackTagCompound != null)
/*     */     {
/* 344 */       itemstack.stackTagCompound = (NBTTagCompound)this.stackTagCompound.copy();
/*     */     }
/*     */     
/* 347 */     return itemstack;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean areItemStackTagsEqual(ItemStack stackA, ItemStack stackB) {
/* 352 */     return (stackA == null && stackB == null) ? true : ((stackA != null && stackB != null) ? ((stackA.stackTagCompound == null && stackB.stackTagCompound != null) ? false : ((stackA.stackTagCompound == null || stackA.stackTagCompound.equals(stackB.stackTagCompound)))) : false);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean areItemStacksEqual(ItemStack stackA, ItemStack stackB) {
/* 357 */     return (stackA == null && stackB == null) ? true : ((stackA != null && stackB != null) ? stackA.isItemStackEqual(stackB) : false);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isItemStackEqual(ItemStack other) {
/* 362 */     return (this.stackSize != other.stackSize) ? false : ((this.item != other.item) ? false : ((this.itemDamage != other.itemDamage) ? false : ((this.stackTagCompound == null && other.stackTagCompound != null) ? false : ((this.stackTagCompound == null || this.stackTagCompound.equals(other.stackTagCompound))))));
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean areItemsEqual(ItemStack stackA, ItemStack stackB) {
/* 367 */     return (stackA == null && stackB == null) ? true : ((stackA != null && stackB != null) ? stackA.isItemEqual(stackB) : false);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isItemEqual(ItemStack other) {
/* 372 */     return (other != null && this.item == other.item && this.itemDamage == other.itemDamage);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUnlocalizedName() {
/* 377 */     return this.item.getUnlocalizedName(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ItemStack copyItemStack(ItemStack stack) {
/* 382 */     return (stack == null) ? null : stack.copy();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 387 */     return this.stackSize + "x" + this.item.getUnlocalizedName() + "@" + this.itemDamage;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateAnimation(World worldIn, Entity entityIn, int inventorySlot, boolean isCurrentItem) {
/* 392 */     if (this.animationsToGo > 0)
/*     */     {
/* 394 */       this.animationsToGo--;
/*     */     }
/*     */     
/* 397 */     this.item.onUpdate(this, worldIn, entityIn, inventorySlot, isCurrentItem);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCrafting(World worldIn, EntityPlayer playerIn, int amount) {
/* 402 */     playerIn.addStat(StatList.objectCraftStats[Item.getIdFromItem(this.item)], amount);
/* 403 */     this.item.onCreated(this, worldIn, playerIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getIsItemStackEqual(ItemStack p_179549_1_) {
/* 408 */     return isItemStackEqual(p_179549_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxItemUseDuration() {
/* 413 */     return getItem().getMaxItemUseDuration(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumAction getItemUseAction() {
/* 418 */     return getItem().getItemUseAction(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlayerStoppedUsing(World worldIn, EntityPlayer playerIn, int timeLeft) {
/* 423 */     getItem().onPlayerStoppedUsing(this, worldIn, playerIn, timeLeft);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasTagCompound() {
/* 428 */     return (this.stackTagCompound != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTTagCompound getTagCompound() {
/* 433 */     return this.stackTagCompound;
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTTagCompound getSubCompound(String key, boolean create) {
/* 438 */     if (this.stackTagCompound != null && this.stackTagCompound.hasKey(key, 10))
/*     */     {
/* 440 */       return this.stackTagCompound.getCompoundTag(key);
/*     */     }
/* 442 */     if (create) {
/*     */       
/* 444 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 445 */       setTagInfo(key, (NBTBase)nbttagcompound);
/* 446 */       return nbttagcompound;
/*     */     } 
/*     */ 
/*     */     
/* 450 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public NBTTagList getEnchantmentTagList() {
/* 456 */     return (this.stackTagCompound == null) ? null : this.stackTagCompound.getTagList("ench", 10);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTagCompound(NBTTagCompound nbt) {
/* 461 */     this.stackTagCompound = nbt;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayName() {
/* 466 */     String s = getItem().getItemStackDisplayName(this);
/*     */     
/* 468 */     if (this.stackTagCompound != null && this.stackTagCompound.hasKey("display", 10)) {
/*     */       
/* 470 */       NBTTagCompound nbttagcompound = this.stackTagCompound.getCompoundTag("display");
/*     */       
/* 472 */       if (nbttagcompound.hasKey("Name", 8))
/*     */       {
/* 474 */         s = nbttagcompound.getString("Name");
/*     */       }
/*     */     } 
/*     */     
/* 478 */     return s;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack setStackDisplayName(String displayName) {
/* 483 */     if (this.stackTagCompound == null)
/*     */     {
/* 485 */       this.stackTagCompound = new NBTTagCompound();
/*     */     }
/*     */     
/* 488 */     if (!this.stackTagCompound.hasKey("display", 10))
/*     */     {
/* 490 */       this.stackTagCompound.setTag("display", (NBTBase)new NBTTagCompound());
/*     */     }
/*     */     
/* 493 */     this.stackTagCompound.getCompoundTag("display").setString("Name", displayName);
/* 494 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearCustomName() {
/* 499 */     if (this.stackTagCompound != null)
/*     */     {
/* 501 */       if (this.stackTagCompound.hasKey("display", 10)) {
/*     */         
/* 503 */         NBTTagCompound nbttagcompound = this.stackTagCompound.getCompoundTag("display");
/* 504 */         nbttagcompound.removeTag("Name");
/*     */         
/* 506 */         if (nbttagcompound.hasNoTags()) {
/*     */           
/* 508 */           this.stackTagCompound.removeTag("display");
/*     */           
/* 510 */           if (this.stackTagCompound.hasNoTags())
/*     */           {
/* 512 */             setTagCompound((NBTTagCompound)null);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasDisplayName() {
/* 521 */     return (this.stackTagCompound == null) ? false : (!this.stackTagCompound.hasKey("display", 10) ? false : this.stackTagCompound.getCompoundTag("display").hasKey("Name", 8));
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getTooltip(EntityPlayer playerIn, boolean advanced) {
/* 526 */     List<String> list = Lists.newArrayList();
/* 527 */     String s = getDisplayName();
/*     */     
/* 529 */     if (hasDisplayName())
/*     */     {
/* 531 */       s = EnumChatFormatting.ITALIC + s;
/*     */     }
/*     */     
/* 534 */     s = s + EnumChatFormatting.RESET;
/*     */     
/* 536 */     if (advanced) {
/*     */       
/* 538 */       String s1 = "";
/*     */       
/* 540 */       if (s.length() > 0) {
/*     */         
/* 542 */         s = s + " (";
/* 543 */         s1 = ")";
/*     */       } 
/*     */       
/* 546 */       int i = Item.getIdFromItem(this.item);
/*     */       
/* 548 */       if (getHasSubtypes())
/*     */       {
/* 550 */         s = s + String.format("#%04d/%d%s", new Object[] { Integer.valueOf(i), Integer.valueOf(this.itemDamage), s1 });
/*     */       }
/*     */       else
/*     */       {
/* 554 */         s = s + String.format("#%04d%s", new Object[] { Integer.valueOf(i), s1 });
/*     */       }
/*     */     
/* 557 */     } else if (!hasDisplayName() && this.item == Items.filled_map) {
/*     */       
/* 559 */       s = s + " #" + this.itemDamage;
/*     */     } 
/*     */     
/* 562 */     list.add(s);
/* 563 */     int i1 = 0;
/*     */     
/* 565 */     if (hasTagCompound() && this.stackTagCompound.hasKey("HideFlags", 99))
/*     */     {
/* 567 */       i1 = this.stackTagCompound.getInteger("HideFlags");
/*     */     }
/*     */     
/* 570 */     if ((i1 & 0x20) == 0)
/*     */     {
/* 572 */       this.item.addInformation(this, playerIn, list, advanced);
/*     */     }
/*     */     
/* 575 */     if (hasTagCompound()) {
/*     */       
/* 577 */       if ((i1 & 0x1) == 0) {
/*     */         
/* 579 */         NBTTagList nbttaglist = getEnchantmentTagList();
/*     */         
/* 581 */         if (nbttaglist != null)
/*     */         {
/* 583 */           for (int j = 0; j < nbttaglist.tagCount(); j++) {
/*     */             
/* 585 */             int k = nbttaglist.getCompoundTagAt(j).getShort("id");
/* 586 */             int l = nbttaglist.getCompoundTagAt(j).getShort("lvl");
/*     */             
/* 588 */             if (Enchantment.getEnchantmentById(k) != null)
/*     */             {
/* 590 */               list.add(Enchantment.getEnchantmentById(k).getTranslatedName(l));
/*     */             }
/*     */           } 
/*     */         }
/*     */       } 
/*     */       
/* 596 */       if (this.stackTagCompound.hasKey("display", 10)) {
/*     */         
/* 598 */         NBTTagCompound nbttagcompound = this.stackTagCompound.getCompoundTag("display");
/*     */         
/* 600 */         if (nbttagcompound.hasKey("color", 3))
/*     */         {
/* 602 */           if (advanced) {
/*     */             
/* 604 */             list.add("Color: #" + Integer.toHexString(nbttagcompound.getInteger("color")).toUpperCase());
/*     */           }
/*     */           else {
/*     */             
/* 608 */             list.add(EnumChatFormatting.ITALIC + StatCollector.translateToLocal("item.dyed"));
/*     */           } 
/*     */         }
/*     */         
/* 612 */         if (nbttagcompound.getTagId("Lore") == 9) {
/*     */           
/* 614 */           NBTTagList nbttaglist1 = nbttagcompound.getTagList("Lore", 8);
/*     */           
/* 616 */           if (nbttaglist1.tagCount() > 0)
/*     */           {
/* 618 */             for (int j1 = 0; j1 < nbttaglist1.tagCount(); j1++)
/*     */             {
/* 620 */               list.add(EnumChatFormatting.DARK_PURPLE + "" + EnumChatFormatting.ITALIC + nbttaglist1.getStringTagAt(j1));
/*     */             }
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 627 */     Multimap<String, AttributeModifier> multimap = getAttributeModifiers();
/*     */     
/* 629 */     if (!multimap.isEmpty() && (i1 & 0x2) == 0) {
/*     */       
/* 631 */       list.add("");
/*     */       
/* 633 */       for (Map.Entry<String, AttributeModifier> entry : (Iterable<Map.Entry<String, AttributeModifier>>)multimap.entries()) {
/*     */         double d1;
/* 635 */         AttributeModifier attributemodifier = entry.getValue();
/* 636 */         double d0 = attributemodifier.getAmount();
/*     */         
/* 638 */         if (attributemodifier.getID() == Item.itemModifierUUID)
/*     */         {
/* 640 */           d0 += EnchantmentHelper.getModifierForCreature(this, EnumCreatureAttribute.UNDEFINED);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 645 */         if (attributemodifier.getOperation() != 1 && attributemodifier.getOperation() != 2) {
/*     */           
/* 647 */           d1 = d0;
/*     */         }
/*     */         else {
/*     */           
/* 651 */           d1 = d0 * 100.0D;
/*     */         } 
/*     */         
/* 654 */         if (d0 > 0.0D) {
/*     */           
/* 656 */           list.add(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted("attribute.modifier.plus." + attributemodifier.getOperation(), new Object[] { DECIMALFORMAT.format(d1), StatCollector.translateToLocal("attribute.name." + (String)entry.getKey()) })); continue;
/*     */         } 
/* 658 */         if (d0 < 0.0D) {
/*     */           
/* 660 */           d1 *= -1.0D;
/* 661 */           list.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted("attribute.modifier.take." + attributemodifier.getOperation(), new Object[] { DECIMALFORMAT.format(d1), StatCollector.translateToLocal("attribute.name." + (String)entry.getKey()) }));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 666 */     if (hasTagCompound() && getTagCompound().getBoolean("Unbreakable") && (i1 & 0x4) == 0)
/*     */     {
/* 668 */       list.add(EnumChatFormatting.BLUE + StatCollector.translateToLocal("item.unbreakable"));
/*     */     }
/*     */     
/* 671 */     if (hasTagCompound() && this.stackTagCompound.hasKey("CanDestroy", 9) && (i1 & 0x8) == 0) {
/*     */       
/* 673 */       NBTTagList nbttaglist2 = this.stackTagCompound.getTagList("CanDestroy", 8);
/*     */       
/* 675 */       if (nbttaglist2.tagCount() > 0) {
/*     */         
/* 677 */         list.add("");
/* 678 */         list.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("item.canBreak"));
/*     */         
/* 680 */         for (int k1 = 0; k1 < nbttaglist2.tagCount(); k1++) {
/*     */           
/* 682 */           Block block = Block.getBlockFromName(nbttaglist2.getStringTagAt(k1));
/*     */           
/* 684 */           if (block != null) {
/*     */             
/* 686 */             list.add(EnumChatFormatting.DARK_GRAY + block.getLocalizedName());
/*     */           }
/*     */           else {
/*     */             
/* 690 */             list.add(EnumChatFormatting.DARK_GRAY + "missingno");
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 696 */     if (hasTagCompound() && this.stackTagCompound.hasKey("CanPlaceOn", 9) && (i1 & 0x10) == 0) {
/*     */       
/* 698 */       NBTTagList nbttaglist3 = this.stackTagCompound.getTagList("CanPlaceOn", 8);
/*     */       
/* 700 */       if (nbttaglist3.tagCount() > 0) {
/*     */         
/* 702 */         list.add("");
/* 703 */         list.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("item.canPlace"));
/*     */         
/* 705 */         for (int l1 = 0; l1 < nbttaglist3.tagCount(); l1++) {
/*     */           
/* 707 */           Block block1 = Block.getBlockFromName(nbttaglist3.getStringTagAt(l1));
/*     */           
/* 709 */           if (block1 != null) {
/*     */             
/* 711 */             list.add(EnumChatFormatting.DARK_GRAY + block1.getLocalizedName());
/*     */           }
/*     */           else {
/*     */             
/* 715 */             list.add(EnumChatFormatting.DARK_GRAY + "missingno");
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 721 */     if (advanced) {
/*     */       
/* 723 */       if (isItemDamaged())
/*     */       {
/* 725 */         list.add("Durability: " + (getMaxDamage() - getItemDamage()) + " / " + getMaxDamage());
/*     */       }
/*     */       
/* 728 */       list.add(EnumChatFormatting.DARK_GRAY + ((ResourceLocation)Item.itemRegistry.getNameForObject(this.item)).toString());
/*     */       
/* 730 */       if (hasTagCompound())
/*     */       {
/* 732 */         list.add(EnumChatFormatting.DARK_GRAY + "NBT: " + getTagCompound().getKeySet().size() + " tag(s)");
/*     */       }
/*     */     } 
/*     */     
/* 736 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEffect() {
/* 741 */     return getItem().hasEffect(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumRarity getRarity() {
/* 746 */     return getItem().getRarity(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isItemEnchantable() {
/* 751 */     return !getItem().isItemTool(this) ? false : (!isItemEnchanted());
/*     */   }
/*     */ 
/*     */   
/*     */   public void addEnchantment(Enchantment ench, int level) {
/* 756 */     if (this.stackTagCompound == null)
/*     */     {
/* 758 */       setTagCompound(new NBTTagCompound());
/*     */     }
/*     */     
/* 761 */     if (!this.stackTagCompound.hasKey("ench", 9))
/*     */     {
/* 763 */       this.stackTagCompound.setTag("ench", (NBTBase)new NBTTagList());
/*     */     }
/*     */     
/* 766 */     NBTTagList nbttaglist = this.stackTagCompound.getTagList("ench", 10);
/* 767 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 768 */     nbttagcompound.setShort("id", (short)ench.effectId);
/* 769 */     nbttagcompound.setShort("lvl", (short)(byte)level);
/* 770 */     nbttaglist.appendTag((NBTBase)nbttagcompound);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isItemEnchanted() {
/* 775 */     return (this.stackTagCompound != null && this.stackTagCompound.hasKey("ench", 9));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTagInfo(String key, NBTBase value) {
/* 780 */     if (this.stackTagCompound == null)
/*     */     {
/* 782 */       setTagCompound(new NBTTagCompound());
/*     */     }
/*     */     
/* 785 */     this.stackTagCompound.setTag(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canEditBlocks() {
/* 790 */     return getItem().canItemEditBlocks();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOnItemFrame() {
/* 795 */     return (this.itemFrame != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItemFrame(EntityItemFrame frame) {
/* 800 */     this.itemFrame = frame;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityItemFrame getItemFrame() {
/* 805 */     return this.itemFrame;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRepairCost() {
/* 810 */     return (hasTagCompound() && this.stackTagCompound.hasKey("RepairCost", 3)) ? this.stackTagCompound.getInteger("RepairCost") : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRepairCost(int cost) {
/* 815 */     if (!hasTagCompound())
/*     */     {
/* 817 */       this.stackTagCompound = new NBTTagCompound();
/*     */     }
/*     */     
/* 820 */     this.stackTagCompound.setInteger("RepairCost", cost);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Multimap<String, AttributeModifier> getAttributeModifiers() {
/*     */     Multimap<String, AttributeModifier> multimap;
/* 827 */     if (hasTagCompound() && this.stackTagCompound.hasKey("AttributeModifiers", 9)) {
/*     */       
/* 829 */       HashMultimap hashMultimap = HashMultimap.create();
/* 830 */       NBTTagList nbttaglist = this.stackTagCompound.getTagList("AttributeModifiers", 10);
/*     */       
/* 832 */       for (int i = 0; i < nbttaglist.tagCount(); i++)
/*     */       {
/* 834 */         NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/* 835 */         AttributeModifier attributemodifier = SharedMonsterAttributes.readAttributeModifierFromNBT(nbttagcompound);
/*     */         
/* 837 */         if (attributemodifier != null && attributemodifier.getID().getLeastSignificantBits() != 0L && attributemodifier.getID().getMostSignificantBits() != 0L)
/*     */         {
/* 839 */           hashMultimap.put(nbttagcompound.getString("AttributeName"), attributemodifier);
/*     */         }
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 845 */       multimap = getItem().getItemAttributeModifiers();
/*     */     } 
/*     */     
/* 848 */     return multimap;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(Item newItem) {
/* 853 */     this.item = newItem;
/*     */   }
/*     */ 
/*     */   
/*     */   public IChatComponent getChatComponent() {
/* 858 */     ChatComponentText chatcomponenttext = new ChatComponentText(getDisplayName());
/*     */     
/* 860 */     if (hasDisplayName())
/*     */     {
/* 862 */       chatcomponenttext.getChatStyle().setItalic(Boolean.valueOf(true));
/*     */     }
/*     */     
/* 865 */     IChatComponent ichatcomponent = (new ChatComponentText("[")).appendSibling((IChatComponent)chatcomponenttext).appendText("]");
/*     */     
/* 867 */     if (this.item != null) {
/*     */       
/* 869 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 870 */       writeToNBT(nbttagcompound);
/* 871 */       ichatcomponent.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, (IChatComponent)new ChatComponentText(nbttagcompound.toString())));
/* 872 */       ichatcomponent.getChatStyle().setColor((getRarity()).rarityColor);
/*     */     } 
/*     */     
/* 875 */     return ichatcomponent;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canDestroy(Block blockIn) {
/* 880 */     if (blockIn == this.canDestroyCacheBlock)
/*     */     {
/* 882 */       return this.canDestroyCacheResult;
/*     */     }
/*     */ 
/*     */     
/* 886 */     this.canDestroyCacheBlock = blockIn;
/*     */     
/* 888 */     if (hasTagCompound() && this.stackTagCompound.hasKey("CanDestroy", 9)) {
/*     */       
/* 890 */       NBTTagList nbttaglist = this.stackTagCompound.getTagList("CanDestroy", 8);
/*     */       
/* 892 */       for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*     */         
/* 894 */         Block block = Block.getBlockFromName(nbttaglist.getStringTagAt(i));
/*     */         
/* 896 */         if (block == blockIn) {
/*     */           
/* 898 */           this.canDestroyCacheResult = true;
/* 899 */           return true;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 904 */     this.canDestroyCacheResult = false;
/* 905 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canPlaceOn(Block blockIn) {
/* 911 */     if (blockIn == this.canPlaceOnCacheBlock)
/*     */     {
/* 913 */       return this.canPlaceOnCacheResult;
/*     */     }
/*     */ 
/*     */     
/* 917 */     this.canPlaceOnCacheBlock = blockIn;
/*     */     
/* 919 */     if (hasTagCompound() && this.stackTagCompound.hasKey("CanPlaceOn", 9)) {
/*     */       
/* 921 */       NBTTagList nbttaglist = this.stackTagCompound.getTagList("CanPlaceOn", 8);
/*     */       
/* 923 */       for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*     */         
/* 925 */         Block block = Block.getBlockFromName(nbttaglist.getStringTagAt(i));
/*     */         
/* 927 */         if (block == blockIn) {
/*     */           
/* 929 */           this.canPlaceOnCacheResult = true;
/* 930 */           return true;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 935 */     this.canPlaceOnCacheResult = false;
/* 936 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemStack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */