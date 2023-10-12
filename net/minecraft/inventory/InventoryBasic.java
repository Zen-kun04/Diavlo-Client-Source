/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ 
/*     */ public class InventoryBasic
/*     */   implements IInventory
/*     */ {
/*     */   private String inventoryTitle;
/*     */   private int slotsCount;
/*     */   private ItemStack[] inventoryContents;
/*     */   private List<IInvBasic> changeListeners;
/*     */   private boolean hasCustomName;
/*     */   
/*     */   public InventoryBasic(String title, boolean customName, int slotCount) {
/*  21 */     this.inventoryTitle = title;
/*  22 */     this.hasCustomName = customName;
/*  23 */     this.slotsCount = slotCount;
/*  24 */     this.inventoryContents = new ItemStack[slotCount];
/*     */   }
/*     */ 
/*     */   
/*     */   public InventoryBasic(IChatComponent title, int slotCount) {
/*  29 */     this(title.getUnformattedText(), true, slotCount);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addInventoryChangeListener(IInvBasic listener) {
/*  34 */     if (this.changeListeners == null)
/*     */     {
/*  36 */       this.changeListeners = Lists.newArrayList();
/*     */     }
/*     */     
/*  39 */     this.changeListeners.add(listener);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeInventoryChangeListener(IInvBasic listener) {
/*  44 */     this.changeListeners.remove(listener);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getStackInSlot(int index) {
/*  49 */     return (index >= 0 && index < this.inventoryContents.length) ? this.inventoryContents[index] : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack decrStackSize(int index, int count) {
/*  54 */     if (this.inventoryContents[index] != null) {
/*     */       
/*  56 */       if ((this.inventoryContents[index]).stackSize <= count) {
/*     */         
/*  58 */         ItemStack itemstack1 = this.inventoryContents[index];
/*  59 */         this.inventoryContents[index] = null;
/*  60 */         markDirty();
/*  61 */         return itemstack1;
/*     */       } 
/*     */ 
/*     */       
/*  65 */       ItemStack itemstack = this.inventoryContents[index].splitStack(count);
/*     */       
/*  67 */       if ((this.inventoryContents[index]).stackSize == 0)
/*     */       {
/*  69 */         this.inventoryContents[index] = null;
/*     */       }
/*     */       
/*  72 */       markDirty();
/*  73 */       return itemstack;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  78 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack func_174894_a(ItemStack stack) {
/*  84 */     ItemStack itemstack = stack.copy();
/*     */     
/*  86 */     for (int i = 0; i < this.slotsCount; i++) {
/*     */       
/*  88 */       ItemStack itemstack1 = getStackInSlot(i);
/*     */       
/*  90 */       if (itemstack1 == null) {
/*     */         
/*  92 */         setInventorySlotContents(i, itemstack);
/*  93 */         markDirty();
/*  94 */         return null;
/*     */       } 
/*     */       
/*  97 */       if (ItemStack.areItemsEqual(itemstack1, itemstack)) {
/*     */         
/*  99 */         int j = Math.min(getInventoryStackLimit(), itemstack1.getMaxStackSize());
/* 100 */         int k = Math.min(itemstack.stackSize, j - itemstack1.stackSize);
/*     */         
/* 102 */         if (k > 0) {
/*     */           
/* 104 */           itemstack1.stackSize += k;
/* 105 */           itemstack.stackSize -= k;
/*     */           
/* 107 */           if (itemstack.stackSize <= 0) {
/*     */             
/* 109 */             markDirty();
/* 110 */             return null;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 116 */     if (itemstack.stackSize != stack.stackSize)
/*     */     {
/* 118 */       markDirty();
/*     */     }
/*     */     
/* 121 */     return itemstack;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeStackFromSlot(int index) {
/* 126 */     if (this.inventoryContents[index] != null) {
/*     */       
/* 128 */       ItemStack itemstack = this.inventoryContents[index];
/* 129 */       this.inventoryContents[index] = null;
/* 130 */       return itemstack;
/*     */     } 
/*     */ 
/*     */     
/* 134 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInventorySlotContents(int index, ItemStack stack) {
/* 140 */     this.inventoryContents[index] = stack;
/*     */     
/* 142 */     if (stack != null && stack.stackSize > getInventoryStackLimit())
/*     */     {
/* 144 */       stack.stackSize = getInventoryStackLimit();
/*     */     }
/*     */     
/* 147 */     markDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSizeInventory() {
/* 152 */     return this.slotsCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 157 */     return this.inventoryTitle;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasCustomName() {
/* 162 */     return this.hasCustomName;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCustomName(String inventoryTitleIn) {
/* 167 */     this.hasCustomName = true;
/* 168 */     this.inventoryTitle = inventoryTitleIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public IChatComponent getDisplayName() {
/* 173 */     return hasCustomName() ? (IChatComponent)new ChatComponentText(getName()) : (IChatComponent)new ChatComponentTranslation(getName(), new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInventoryStackLimit() {
/* 178 */     return 64;
/*     */   }
/*     */ 
/*     */   
/*     */   public void markDirty() {
/* 183 */     if (this.changeListeners != null)
/*     */     {
/* 185 */       for (int i = 0; i < this.changeListeners.size(); i++)
/*     */       {
/* 187 */         ((IInvBasic)this.changeListeners.get(i)).onInventoryChanged(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUseableByPlayer(EntityPlayer player) {
/* 194 */     return true;
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
/* 207 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getField(int id) {
/* 212 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setField(int id, int value) {}
/*     */ 
/*     */   
/*     */   public int getFieldCount() {
/* 221 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 226 */     for (int i = 0; i < this.inventoryContents.length; i++)
/*     */     {
/* 228 */       this.inventoryContents[i] = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\inventory\InventoryBasic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */