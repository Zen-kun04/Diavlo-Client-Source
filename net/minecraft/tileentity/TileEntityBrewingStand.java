/*     */ package net.minecraft.tileentity;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.BlockBrewingStand;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.ContainerBrewingStand;
/*     */ import net.minecraft.inventory.ISidedInventory;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemPotion;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.potion.PotionHelper;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.ITickable;
/*     */ 
/*     */ public class TileEntityBrewingStand extends TileEntityLockable implements ITickable, ISidedInventory {
/*  25 */   private static final int[] inputSlots = new int[] { 3 };
/*  26 */   private static final int[] outputSlots = new int[] { 0, 1, 2 };
/*  27 */   private ItemStack[] brewingItemStacks = new ItemStack[4];
/*     */   
/*     */   private int brewTime;
/*     */   private boolean[] filledSlots;
/*     */   private Item ingredientID;
/*     */   private String customName;
/*     */   
/*     */   public String getName() {
/*  35 */     return hasCustomName() ? this.customName : "container.brewing";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasCustomName() {
/*  40 */     return (this.customName != null && this.customName.length() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/*  45 */     this.customName = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSizeInventory() {
/*  50 */     return this.brewingItemStacks.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public void update() {
/*  55 */     if (this.brewTime > 0) {
/*     */       
/*  57 */       this.brewTime--;
/*     */       
/*  59 */       if (this.brewTime == 0)
/*     */       {
/*  61 */         brewPotions();
/*  62 */         markDirty();
/*     */       }
/*  64 */       else if (!canBrew())
/*     */       {
/*  66 */         this.brewTime = 0;
/*  67 */         markDirty();
/*     */       }
/*  69 */       else if (this.ingredientID != this.brewingItemStacks[3].getItem())
/*     */       {
/*  71 */         this.brewTime = 0;
/*  72 */         markDirty();
/*     */       }
/*     */     
/*  75 */     } else if (canBrew()) {
/*     */       
/*  77 */       this.brewTime = 400;
/*  78 */       this.ingredientID = this.brewingItemStacks[3].getItem();
/*     */     } 
/*     */     
/*  81 */     if (!this.worldObj.isRemote) {
/*     */       
/*  83 */       boolean[] aboolean = func_174902_m();
/*     */       
/*  85 */       if (!Arrays.equals(aboolean, this.filledSlots)) {
/*     */         
/*  87 */         this.filledSlots = aboolean;
/*  88 */         IBlockState iblockstate = this.worldObj.getBlockState(getPos());
/*     */         
/*  90 */         if (!(iblockstate.getBlock() instanceof BlockBrewingStand)) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/*  95 */         for (int i = 0; i < BlockBrewingStand.HAS_BOTTLE.length; i++)
/*     */         {
/*  97 */           iblockstate = iblockstate.withProperty((IProperty)BlockBrewingStand.HAS_BOTTLE[i], Boolean.valueOf(aboolean[i]));
/*     */         }
/*     */         
/* 100 */         this.worldObj.setBlockState(this.pos, iblockstate, 2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean canBrew() {
/* 107 */     if (this.brewingItemStacks[3] != null && (this.brewingItemStacks[3]).stackSize > 0) {
/*     */       
/* 109 */       ItemStack itemstack = this.brewingItemStacks[3];
/*     */       
/* 111 */       if (!itemstack.getItem().isPotionIngredient(itemstack))
/*     */       {
/* 113 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 117 */       boolean flag = false;
/*     */       
/* 119 */       for (int i = 0; i < 3; i++) {
/*     */         
/* 121 */         if (this.brewingItemStacks[i] != null && this.brewingItemStacks[i].getItem() == Items.potionitem) {
/*     */           
/* 123 */           int j = this.brewingItemStacks[i].getMetadata();
/* 124 */           int k = getPotionResult(j, itemstack);
/*     */           
/* 126 */           if (!ItemPotion.isSplash(j) && ItemPotion.isSplash(k)) {
/*     */             
/* 128 */             flag = true;
/*     */             
/*     */             break;
/*     */           } 
/* 132 */           List<PotionEffect> list = Items.potionitem.getEffects(j);
/* 133 */           List<PotionEffect> list1 = Items.potionitem.getEffects(k);
/*     */           
/* 135 */           if ((j <= 0 || list != list1) && (list == null || (!list.equals(list1) && list1 != null)) && j != k) {
/*     */             
/* 137 */             flag = true;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/* 143 */       return flag;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 148 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void brewPotions() {
/* 154 */     if (canBrew()) {
/*     */       
/* 156 */       ItemStack itemstack = this.brewingItemStacks[3];
/*     */       
/* 158 */       for (int i = 0; i < 3; i++) {
/*     */         
/* 160 */         if (this.brewingItemStacks[i] != null && this.brewingItemStacks[i].getItem() == Items.potionitem) {
/*     */           
/* 162 */           int j = this.brewingItemStacks[i].getMetadata();
/* 163 */           int k = getPotionResult(j, itemstack);
/* 164 */           List<PotionEffect> list = Items.potionitem.getEffects(j);
/* 165 */           List<PotionEffect> list1 = Items.potionitem.getEffects(k);
/*     */           
/* 167 */           if ((j > 0 && list == list1) || (list != null && (list.equals(list1) || list1 == null))) {
/*     */             
/* 169 */             if (!ItemPotion.isSplash(j) && ItemPotion.isSplash(k))
/*     */             {
/* 171 */               this.brewingItemStacks[i].setItemDamage(k);
/*     */             }
/*     */           }
/* 174 */           else if (j != k) {
/*     */             
/* 176 */             this.brewingItemStacks[i].setItemDamage(k);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 181 */       if (itemstack.getItem().hasContainerItem()) {
/*     */         
/* 183 */         this.brewingItemStacks[3] = new ItemStack(itemstack.getItem().getContainerItem());
/*     */       }
/*     */       else {
/*     */         
/* 187 */         (this.brewingItemStacks[3]).stackSize--;
/*     */         
/* 189 */         if ((this.brewingItemStacks[3]).stackSize <= 0)
/*     */         {
/* 191 */           this.brewingItemStacks[3] = null;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private int getPotionResult(int meta, ItemStack stack) {
/* 199 */     return (stack == null) ? meta : (stack.getItem().isPotionIngredient(stack) ? PotionHelper.applyIngredient(meta, stack.getItem().getPotionEffect(stack)) : meta);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readFromNBT(NBTTagCompound compound) {
/* 204 */     super.readFromNBT(compound);
/* 205 */     NBTTagList nbttaglist = compound.getTagList("Items", 10);
/* 206 */     this.brewingItemStacks = new ItemStack[getSizeInventory()];
/*     */     
/* 208 */     for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*     */       
/* 210 */       NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/* 211 */       int j = nbttagcompound.getByte("Slot");
/*     */       
/* 213 */       if (j >= 0 && j < this.brewingItemStacks.length)
/*     */       {
/* 215 */         this.brewingItemStacks[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
/*     */       }
/*     */     } 
/*     */     
/* 219 */     this.brewTime = compound.getShort("BrewTime");
/*     */     
/* 221 */     if (compound.hasKey("CustomName", 8))
/*     */     {
/* 223 */       this.customName = compound.getString("CustomName");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeToNBT(NBTTagCompound compound) {
/* 229 */     super.writeToNBT(compound);
/* 230 */     compound.setShort("BrewTime", (short)this.brewTime);
/* 231 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/* 233 */     for (int i = 0; i < this.brewingItemStacks.length; i++) {
/*     */       
/* 235 */       if (this.brewingItemStacks[i] != null) {
/*     */         
/* 237 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 238 */         nbttagcompound.setByte("Slot", (byte)i);
/* 239 */         this.brewingItemStacks[i].writeToNBT(nbttagcompound);
/* 240 */         nbttaglist.appendTag((NBTBase)nbttagcompound);
/*     */       } 
/*     */     } 
/*     */     
/* 244 */     compound.setTag("Items", (NBTBase)nbttaglist);
/*     */     
/* 246 */     if (hasCustomName())
/*     */     {
/* 248 */       compound.setString("CustomName", this.customName);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getStackInSlot(int index) {
/* 254 */     return (index >= 0 && index < this.brewingItemStacks.length) ? this.brewingItemStacks[index] : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack decrStackSize(int index, int count) {
/* 259 */     if (index >= 0 && index < this.brewingItemStacks.length) {
/*     */       
/* 261 */       ItemStack itemstack = this.brewingItemStacks[index];
/* 262 */       this.brewingItemStacks[index] = null;
/* 263 */       return itemstack;
/*     */     } 
/*     */ 
/*     */     
/* 267 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack removeStackFromSlot(int index) {
/* 273 */     if (index >= 0 && index < this.brewingItemStacks.length) {
/*     */       
/* 275 */       ItemStack itemstack = this.brewingItemStacks[index];
/* 276 */       this.brewingItemStacks[index] = null;
/* 277 */       return itemstack;
/*     */     } 
/*     */ 
/*     */     
/* 281 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInventorySlotContents(int index, ItemStack stack) {
/* 287 */     if (index >= 0 && index < this.brewingItemStacks.length)
/*     */     {
/* 289 */       this.brewingItemStacks[index] = stack;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInventoryStackLimit() {
/* 295 */     return 64;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUseableByPlayer(EntityPlayer player) {
/* 300 */     return (this.worldObj.getTileEntity(this.pos) != this) ? false : ((player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void openInventory(EntityPlayer player) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void closeInventory(EntityPlayer player) {}
/*     */ 
/*     */   
/*     */   public boolean isItemValidForSlot(int index, ItemStack stack) {
/* 313 */     return (index == 3) ? stack.getItem().isPotionIngredient(stack) : ((stack.getItem() == Items.potionitem || stack.getItem() == Items.glass_bottle));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean[] func_174902_m() {
/* 318 */     boolean[] aboolean = new boolean[3];
/*     */     
/* 320 */     for (int i = 0; i < 3; i++) {
/*     */       
/* 322 */       if (this.brewingItemStacks[i] != null)
/*     */       {
/* 324 */         aboolean[i] = true;
/*     */       }
/*     */     } 
/*     */     
/* 328 */     return aboolean;
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] getSlotsForFace(EnumFacing side) {
/* 333 */     return (side == EnumFacing.UP) ? inputSlots : outputSlots;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
/* 338 */     return isItemValidForSlot(index, itemStackIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
/* 343 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGuiID() {
/* 348 */     return "minecraft:brewing_stand";
/*     */   }
/*     */ 
/*     */   
/*     */   public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
/* 353 */     return (Container)new ContainerBrewingStand(playerInventory, (IInventory)this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getField(int id) {
/* 358 */     switch (id) {
/*     */       
/*     */       case 0:
/* 361 */         return this.brewTime;
/*     */     } 
/*     */     
/* 364 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setField(int id, int value) {
/* 370 */     switch (id) {
/*     */       
/*     */       case 0:
/* 373 */         this.brewTime = value;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFieldCount() {
/* 381 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 386 */     for (int i = 0; i < this.brewingItemStacks.length; i++)
/*     */     {
/* 388 */       this.brewingItemStacks[i] = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\tileentity\TileEntityBrewingStand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */