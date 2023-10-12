/*     */ package net.minecraft.tileentity;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockFurnace;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.ContainerFurnace;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.inventory.ISidedInventory;
/*     */ import net.minecraft.inventory.SlotFurnaceFuel;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemHoe;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.ItemSword;
/*     */ import net.minecraft.item.ItemTool;
/*     */ import net.minecraft.item.crafting.FurnaceRecipes;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.ITickable;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ public class TileEntityFurnace
/*     */   extends TileEntityLockable implements ITickable, ISidedInventory {
/*  30 */   private static final int[] slotsTop = new int[] { 0 };
/*  31 */   private static final int[] slotsBottom = new int[] { 2, 1 };
/*  32 */   private static final int[] slotsSides = new int[] { 1 };
/*  33 */   private ItemStack[] furnaceItemStacks = new ItemStack[3];
/*     */   
/*     */   private int furnaceBurnTime;
/*     */   private int currentItemBurnTime;
/*     */   private int cookTime;
/*     */   private int totalCookTime;
/*     */   private String furnaceCustomName;
/*     */   
/*     */   public int getSizeInventory() {
/*  42 */     return this.furnaceItemStacks.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getStackInSlot(int index) {
/*  47 */     return this.furnaceItemStacks[index];
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack decrStackSize(int index, int count) {
/*  52 */     if (this.furnaceItemStacks[index] != null) {
/*     */       
/*  54 */       if ((this.furnaceItemStacks[index]).stackSize <= count) {
/*     */         
/*  56 */         ItemStack itemstack1 = this.furnaceItemStacks[index];
/*  57 */         this.furnaceItemStacks[index] = null;
/*  58 */         return itemstack1;
/*     */       } 
/*     */ 
/*     */       
/*  62 */       ItemStack itemstack = this.furnaceItemStacks[index].splitStack(count);
/*     */       
/*  64 */       if ((this.furnaceItemStacks[index]).stackSize == 0)
/*     */       {
/*  66 */         this.furnaceItemStacks[index] = null;
/*     */       }
/*     */       
/*  69 */       return itemstack;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  74 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack removeStackFromSlot(int index) {
/*  80 */     if (this.furnaceItemStacks[index] != null) {
/*     */       
/*  82 */       ItemStack itemstack = this.furnaceItemStacks[index];
/*  83 */       this.furnaceItemStacks[index] = null;
/*  84 */       return itemstack;
/*     */     } 
/*     */ 
/*     */     
/*  88 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInventorySlotContents(int index, ItemStack stack) {
/*  94 */     boolean flag = (stack != null && stack.isItemEqual(this.furnaceItemStacks[index]) && ItemStack.areItemStackTagsEqual(stack, this.furnaceItemStacks[index]));
/*  95 */     this.furnaceItemStacks[index] = stack;
/*     */     
/*  97 */     if (stack != null && stack.stackSize > getInventoryStackLimit())
/*     */     {
/*  99 */       stack.stackSize = getInventoryStackLimit();
/*     */     }
/*     */     
/* 102 */     if (index == 0 && !flag) {
/*     */       
/* 104 */       this.totalCookTime = getCookTime(stack);
/* 105 */       this.cookTime = 0;
/* 106 */       markDirty();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 112 */     return hasCustomName() ? this.furnaceCustomName : "container.furnace";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasCustomName() {
/* 117 */     return (this.furnaceCustomName != null && this.furnaceCustomName.length() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCustomInventoryName(String p_145951_1_) {
/* 122 */     this.furnaceCustomName = p_145951_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public void readFromNBT(NBTTagCompound compound) {
/* 127 */     super.readFromNBT(compound);
/* 128 */     NBTTagList nbttaglist = compound.getTagList("Items", 10);
/* 129 */     this.furnaceItemStacks = new ItemStack[getSizeInventory()];
/*     */     
/* 131 */     for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*     */       
/* 133 */       NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/* 134 */       int j = nbttagcompound.getByte("Slot");
/*     */       
/* 136 */       if (j >= 0 && j < this.furnaceItemStacks.length)
/*     */       {
/* 138 */         this.furnaceItemStacks[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
/*     */       }
/*     */     } 
/*     */     
/* 142 */     this.furnaceBurnTime = compound.getShort("BurnTime");
/* 143 */     this.cookTime = compound.getShort("CookTime");
/* 144 */     this.totalCookTime = compound.getShort("CookTimeTotal");
/* 145 */     this.currentItemBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);
/*     */     
/* 147 */     if (compound.hasKey("CustomName", 8))
/*     */     {
/* 149 */       this.furnaceCustomName = compound.getString("CustomName");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeToNBT(NBTTagCompound compound) {
/* 155 */     super.writeToNBT(compound);
/* 156 */     compound.setShort("BurnTime", (short)this.furnaceBurnTime);
/* 157 */     compound.setShort("CookTime", (short)this.cookTime);
/* 158 */     compound.setShort("CookTimeTotal", (short)this.totalCookTime);
/* 159 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/* 161 */     for (int i = 0; i < this.furnaceItemStacks.length; i++) {
/*     */       
/* 163 */       if (this.furnaceItemStacks[i] != null) {
/*     */         
/* 165 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 166 */         nbttagcompound.setByte("Slot", (byte)i);
/* 167 */         this.furnaceItemStacks[i].writeToNBT(nbttagcompound);
/* 168 */         nbttaglist.appendTag((NBTBase)nbttagcompound);
/*     */       } 
/*     */     } 
/*     */     
/* 172 */     compound.setTag("Items", (NBTBase)nbttaglist);
/*     */     
/* 174 */     if (hasCustomName())
/*     */     {
/* 176 */       compound.setString("CustomName", this.furnaceCustomName);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInventoryStackLimit() {
/* 182 */     return 64;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBurning() {
/* 187 */     return (this.furnaceBurnTime > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isBurning(IInventory p_174903_0_) {
/* 192 */     return (p_174903_0_.getField(0) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void update() {
/* 197 */     boolean flag = isBurning();
/* 198 */     boolean flag1 = false;
/*     */     
/* 200 */     if (isBurning())
/*     */     {
/* 202 */       this.furnaceBurnTime--;
/*     */     }
/*     */     
/* 205 */     if (!this.worldObj.isRemote) {
/*     */       
/* 207 */       if (isBurning() || (this.furnaceItemStacks[1] != null && this.furnaceItemStacks[0] != null)) {
/*     */         
/* 209 */         if (!isBurning() && canSmelt()) {
/*     */           
/* 211 */           this.currentItemBurnTime = this.furnaceBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);
/*     */           
/* 213 */           if (isBurning()) {
/*     */             
/* 215 */             flag1 = true;
/*     */             
/* 217 */             if (this.furnaceItemStacks[1] != null) {
/*     */               
/* 219 */               (this.furnaceItemStacks[1]).stackSize--;
/*     */               
/* 221 */               if ((this.furnaceItemStacks[1]).stackSize == 0) {
/*     */                 
/* 223 */                 Item item = this.furnaceItemStacks[1].getItem().getContainerItem();
/* 224 */                 this.furnaceItemStacks[1] = (item != null) ? new ItemStack(item) : null;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 230 */         if (isBurning() && canSmelt()) {
/*     */           
/* 232 */           this.cookTime++;
/*     */           
/* 234 */           if (this.cookTime == this.totalCookTime)
/*     */           {
/* 236 */             this.cookTime = 0;
/* 237 */             this.totalCookTime = getCookTime(this.furnaceItemStacks[0]);
/* 238 */             smeltItem();
/* 239 */             flag1 = true;
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 244 */           this.cookTime = 0;
/*     */         }
/*     */       
/* 247 */       } else if (!isBurning() && this.cookTime > 0) {
/*     */         
/* 249 */         this.cookTime = MathHelper.clamp_int(this.cookTime - 2, 0, this.totalCookTime);
/*     */       } 
/*     */       
/* 252 */       if (flag != isBurning()) {
/*     */         
/* 254 */         flag1 = true;
/* 255 */         BlockFurnace.setState(isBurning(), this.worldObj, this.pos);
/*     */       } 
/*     */     } 
/*     */     
/* 259 */     if (flag1)
/*     */     {
/* 261 */       markDirty();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCookTime(ItemStack stack) {
/* 267 */     return 200;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean canSmelt() {
/* 272 */     if (this.furnaceItemStacks[0] == null)
/*     */     {
/* 274 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 278 */     ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.furnaceItemStacks[0]);
/* 279 */     return (itemstack == null) ? false : ((this.furnaceItemStacks[2] == null) ? true : (!this.furnaceItemStacks[2].isItemEqual(itemstack) ? false : (((this.furnaceItemStacks[2]).stackSize < getInventoryStackLimit() && (this.furnaceItemStacks[2]).stackSize < this.furnaceItemStacks[2].getMaxStackSize()) ? true : (((this.furnaceItemStacks[2]).stackSize < itemstack.getMaxStackSize())))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void smeltItem() {
/* 285 */     if (canSmelt()) {
/*     */       
/* 287 */       ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.furnaceItemStacks[0]);
/*     */       
/* 289 */       if (this.furnaceItemStacks[2] == null) {
/*     */         
/* 291 */         this.furnaceItemStacks[2] = itemstack.copy();
/*     */       }
/* 293 */       else if (this.furnaceItemStacks[2].getItem() == itemstack.getItem()) {
/*     */         
/* 295 */         (this.furnaceItemStacks[2]).stackSize++;
/*     */       } 
/*     */       
/* 298 */       if (this.furnaceItemStacks[0].getItem() == Item.getItemFromBlock(Blocks.sponge) && this.furnaceItemStacks[0].getMetadata() == 1 && this.furnaceItemStacks[1] != null && this.furnaceItemStacks[1].getItem() == Items.bucket)
/*     */       {
/* 300 */         this.furnaceItemStacks[1] = new ItemStack(Items.water_bucket);
/*     */       }
/*     */       
/* 303 */       (this.furnaceItemStacks[0]).stackSize--;
/*     */       
/* 305 */       if ((this.furnaceItemStacks[0]).stackSize <= 0)
/*     */       {
/* 307 */         this.furnaceItemStacks[0] = null;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getItemBurnTime(ItemStack p_145952_0_) {
/* 314 */     if (p_145952_0_ == null)
/*     */     {
/* 316 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 320 */     Item item = p_145952_0_.getItem();
/*     */     
/* 322 */     if (item instanceof net.minecraft.item.ItemBlock && Block.getBlockFromItem(item) != Blocks.air) {
/*     */       
/* 324 */       Block block = Block.getBlockFromItem(item);
/*     */       
/* 326 */       if (block == Blocks.wooden_slab)
/*     */       {
/* 328 */         return 150;
/*     */       }
/*     */       
/* 331 */       if (block.getMaterial() == Material.wood)
/*     */       {
/* 333 */         return 300;
/*     */       }
/*     */       
/* 336 */       if (block == Blocks.coal_block)
/*     */       {
/* 338 */         return 16000;
/*     */       }
/*     */     } 
/*     */     
/* 342 */     return (item instanceof ItemTool && ((ItemTool)item).getToolMaterialName().equals("WOOD")) ? 200 : ((item instanceof ItemSword && ((ItemSword)item).getToolMaterialName().equals("WOOD")) ? 200 : ((item instanceof ItemHoe && ((ItemHoe)item).getMaterialName().equals("WOOD")) ? 200 : ((item == Items.stick) ? 100 : ((item == Items.coal) ? 1600 : ((item == Items.lava_bucket) ? 20000 : ((item == Item.getItemFromBlock(Blocks.sapling)) ? 100 : ((item == Items.blaze_rod) ? 2400 : 0)))))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isItemFuel(ItemStack p_145954_0_) {
/* 348 */     return (getItemBurnTime(p_145954_0_) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUseableByPlayer(EntityPlayer player) {
/* 353 */     return (this.worldObj.getTileEntity(this.pos) != this) ? false : ((player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D));
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
/* 366 */     return (index == 2) ? false : ((index != 1) ? true : ((isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack))));
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] getSlotsForFace(EnumFacing side) {
/* 371 */     return (side == EnumFacing.DOWN) ? slotsBottom : ((side == EnumFacing.UP) ? slotsTop : slotsSides);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
/* 376 */     return isItemValidForSlot(index, itemStackIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
/* 381 */     if (direction == EnumFacing.DOWN && index == 1) {
/*     */       
/* 383 */       Item item = stack.getItem();
/*     */       
/* 385 */       if (item != Items.water_bucket && item != Items.bucket)
/*     */       {
/* 387 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 391 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGuiID() {
/* 396 */     return "minecraft:furnace";
/*     */   }
/*     */ 
/*     */   
/*     */   public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
/* 401 */     return (Container)new ContainerFurnace(playerInventory, (IInventory)this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getField(int id) {
/* 406 */     switch (id) {
/*     */       
/*     */       case 0:
/* 409 */         return this.furnaceBurnTime;
/*     */       
/*     */       case 1:
/* 412 */         return this.currentItemBurnTime;
/*     */       
/*     */       case 2:
/* 415 */         return this.cookTime;
/*     */       
/*     */       case 3:
/* 418 */         return this.totalCookTime;
/*     */     } 
/*     */     
/* 421 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setField(int id, int value) {
/* 427 */     switch (id) {
/*     */       
/*     */       case 0:
/* 430 */         this.furnaceBurnTime = value;
/*     */         break;
/*     */       
/*     */       case 1:
/* 434 */         this.currentItemBurnTime = value;
/*     */         break;
/*     */       
/*     */       case 2:
/* 438 */         this.cookTime = value;
/*     */         break;
/*     */       
/*     */       case 3:
/* 442 */         this.totalCookTime = value;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getFieldCount() {
/* 448 */     return 4;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 453 */     for (int i = 0; i < this.furnaceItemStacks.length; i++)
/*     */     {
/* 455 */       this.furnaceItemStacks[i] = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\tileentity\TileEntityFurnace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */