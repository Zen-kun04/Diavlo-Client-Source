/*     */ package net.minecraft.tileentity;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockChest;
/*     */ import net.minecraft.block.BlockHopper;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.ContainerHopper;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.inventory.ISidedInventory;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EntitySelectors;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.ITickable;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.ILockableContainer;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class TileEntityHopper extends TileEntityLockable implements IHopper, ITickable {
/*  28 */   private ItemStack[] inventory = new ItemStack[5];
/*     */   private String customName;
/*  30 */   private int transferCooldown = -1;
/*     */ 
/*     */   
/*     */   public void readFromNBT(NBTTagCompound compound) {
/*  34 */     super.readFromNBT(compound);
/*  35 */     NBTTagList nbttaglist = compound.getTagList("Items", 10);
/*  36 */     this.inventory = new ItemStack[getSizeInventory()];
/*     */     
/*  38 */     if (compound.hasKey("CustomName", 8))
/*     */     {
/*  40 */       this.customName = compound.getString("CustomName");
/*     */     }
/*     */     
/*  43 */     this.transferCooldown = compound.getInteger("TransferCooldown");
/*     */     
/*  45 */     for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*     */       
/*  47 */       NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/*  48 */       int j = nbttagcompound.getByte("Slot");
/*     */       
/*  50 */       if (j >= 0 && j < this.inventory.length)
/*     */       {
/*  52 */         this.inventory[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeToNBT(NBTTagCompound compound) {
/*  59 */     super.writeToNBT(compound);
/*  60 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/*  62 */     for (int i = 0; i < this.inventory.length; i++) {
/*     */       
/*  64 */       if (this.inventory[i] != null) {
/*     */         
/*  66 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  67 */         nbttagcompound.setByte("Slot", (byte)i);
/*  68 */         this.inventory[i].writeToNBT(nbttagcompound);
/*  69 */         nbttaglist.appendTag((NBTBase)nbttagcompound);
/*     */       } 
/*     */     } 
/*     */     
/*  73 */     compound.setTag("Items", (NBTBase)nbttaglist);
/*  74 */     compound.setInteger("TransferCooldown", this.transferCooldown);
/*     */     
/*  76 */     if (hasCustomName())
/*     */     {
/*  78 */       compound.setString("CustomName", this.customName);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void markDirty() {
/*  84 */     super.markDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSizeInventory() {
/*  89 */     return this.inventory.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getStackInSlot(int index) {
/*  94 */     return this.inventory[index];
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack decrStackSize(int index, int count) {
/*  99 */     if (this.inventory[index] != null) {
/*     */       
/* 101 */       if ((this.inventory[index]).stackSize <= count) {
/*     */         
/* 103 */         ItemStack itemstack1 = this.inventory[index];
/* 104 */         this.inventory[index] = null;
/* 105 */         return itemstack1;
/*     */       } 
/*     */ 
/*     */       
/* 109 */       ItemStack itemstack = this.inventory[index].splitStack(count);
/*     */       
/* 111 */       if ((this.inventory[index]).stackSize == 0)
/*     */       {
/* 113 */         this.inventory[index] = null;
/*     */       }
/*     */       
/* 116 */       return itemstack;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 121 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack removeStackFromSlot(int index) {
/* 127 */     if (this.inventory[index] != null) {
/*     */       
/* 129 */       ItemStack itemstack = this.inventory[index];
/* 130 */       this.inventory[index] = null;
/* 131 */       return itemstack;
/*     */     } 
/*     */ 
/*     */     
/* 135 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInventorySlotContents(int index, ItemStack stack) {
/* 141 */     this.inventory[index] = stack;
/*     */     
/* 143 */     if (stack != null && stack.stackSize > getInventoryStackLimit())
/*     */     {
/* 145 */       stack.stackSize = getInventoryStackLimit();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 151 */     return hasCustomName() ? this.customName : "container.hopper";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasCustomName() {
/* 156 */     return (this.customName != null && this.customName.length() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCustomName(String customNameIn) {
/* 161 */     this.customName = customNameIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInventoryStackLimit() {
/* 166 */     return 64;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUseableByPlayer(EntityPlayer player) {
/* 171 */     return (this.worldObj.getTileEntity(this.pos) != this) ? false : ((player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D));
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
/* 184 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void update() {
/* 189 */     if (this.worldObj != null && !this.worldObj.isRemote) {
/*     */       
/* 191 */       this.transferCooldown--;
/*     */       
/* 193 */       if (!isOnTransferCooldown()) {
/*     */         
/* 195 */         setTransferCooldown(0);
/* 196 */         updateHopper();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean updateHopper() {
/* 203 */     if (this.worldObj != null && !this.worldObj.isRemote) {
/*     */       
/* 205 */       if (!isOnTransferCooldown() && BlockHopper.isEnabled(getBlockMetadata())) {
/*     */         
/* 207 */         boolean flag = false;
/*     */         
/* 209 */         if (!isEmpty())
/*     */         {
/* 211 */           flag = transferItemsOut();
/*     */         }
/*     */         
/* 214 */         if (!isFull())
/*     */         {
/* 216 */           flag = (captureDroppedItems(this) || flag);
/*     */         }
/*     */         
/* 219 */         if (flag) {
/*     */           
/* 221 */           setTransferCooldown(8);
/* 222 */           markDirty();
/* 223 */           return true;
/*     */         } 
/*     */       } 
/*     */       
/* 227 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 231 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isEmpty() {
/* 237 */     for (ItemStack itemstack : this.inventory) {
/*     */       
/* 239 */       if (itemstack != null)
/*     */       {
/* 241 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 245 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isFull() {
/* 250 */     for (ItemStack itemstack : this.inventory) {
/*     */       
/* 252 */       if (itemstack == null || itemstack.stackSize != itemstack.getMaxStackSize())
/*     */       {
/* 254 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 258 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean transferItemsOut() {
/* 263 */     IInventory iinventory = getInventoryForHopperTransfer();
/*     */     
/* 265 */     if (iinventory == null)
/*     */     {
/* 267 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 271 */     EnumFacing enumfacing = BlockHopper.getFacing(getBlockMetadata()).getOpposite();
/*     */     
/* 273 */     if (isInventoryFull(iinventory, enumfacing))
/*     */     {
/* 275 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 279 */     for (int i = 0; i < getSizeInventory(); i++) {
/*     */       
/* 281 */       if (getStackInSlot(i) != null) {
/*     */         
/* 283 */         ItemStack itemstack = getStackInSlot(i).copy();
/* 284 */         ItemStack itemstack1 = putStackInInventoryAllSlots(iinventory, decrStackSize(i, 1), enumfacing);
/*     */         
/* 286 */         if (itemstack1 == null || itemstack1.stackSize == 0) {
/*     */           
/* 288 */           iinventory.markDirty();
/* 289 */           return true;
/*     */         } 
/*     */         
/* 292 */         setInventorySlotContents(i, itemstack);
/*     */       } 
/*     */     } 
/*     */     
/* 296 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isInventoryFull(IInventory inventoryIn, EnumFacing side) {
/* 303 */     if (inventoryIn instanceof ISidedInventory) {
/*     */       
/* 305 */       ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
/* 306 */       int[] aint = isidedinventory.getSlotsForFace(side);
/*     */       
/* 308 */       for (int k = 0; k < aint.length; k++)
/*     */       {
/* 310 */         ItemStack itemstack1 = isidedinventory.getStackInSlot(aint[k]);
/*     */         
/* 312 */         if (itemstack1 == null || itemstack1.stackSize != itemstack1.getMaxStackSize())
/*     */         {
/* 314 */           return false;
/*     */         }
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 320 */       int i = inventoryIn.getSizeInventory();
/*     */       
/* 322 */       for (int j = 0; j < i; j++) {
/*     */         
/* 324 */         ItemStack itemstack = inventoryIn.getStackInSlot(j);
/*     */         
/* 326 */         if (itemstack == null || itemstack.stackSize != itemstack.getMaxStackSize())
/*     */         {
/* 328 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 333 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isInventoryEmpty(IInventory inventoryIn, EnumFacing side) {
/* 338 */     if (inventoryIn instanceof ISidedInventory) {
/*     */       
/* 340 */       ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
/* 341 */       int[] aint = isidedinventory.getSlotsForFace(side);
/*     */       
/* 343 */       for (int i = 0; i < aint.length; i++)
/*     */       {
/* 345 */         if (isidedinventory.getStackInSlot(aint[i]) != null)
/*     */         {
/* 347 */           return false;
/*     */         }
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 353 */       int j = inventoryIn.getSizeInventory();
/*     */       
/* 355 */       for (int k = 0; k < j; k++) {
/*     */         
/* 357 */         if (inventoryIn.getStackInSlot(k) != null)
/*     */         {
/* 359 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 364 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean captureDroppedItems(IHopper p_145891_0_) {
/* 369 */     IInventory iinventory = getHopperInventory(p_145891_0_);
/*     */     
/* 371 */     if (iinventory != null) {
/*     */       
/* 373 */       EnumFacing enumfacing = EnumFacing.DOWN;
/*     */       
/* 375 */       if (isInventoryEmpty(iinventory, enumfacing))
/*     */       {
/* 377 */         return false;
/*     */       }
/*     */       
/* 380 */       if (iinventory instanceof ISidedInventory) {
/*     */         
/* 382 */         ISidedInventory isidedinventory = (ISidedInventory)iinventory;
/* 383 */         int[] aint = isidedinventory.getSlotsForFace(enumfacing);
/*     */         
/* 385 */         for (int i = 0; i < aint.length; i++)
/*     */         {
/* 387 */           if (pullItemFromSlot(p_145891_0_, iinventory, aint[i], enumfacing))
/*     */           {
/* 389 */             return true;
/*     */           }
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 395 */         int j = iinventory.getSizeInventory();
/*     */         
/* 397 */         for (int k = 0; k < j; k++)
/*     */         {
/* 399 */           if (pullItemFromSlot(p_145891_0_, iinventory, k, enumfacing))
/*     */           {
/* 401 */             return true;
/*     */           }
/*     */         }
/*     */       
/*     */       } 
/*     */     } else {
/*     */       
/* 408 */       for (EntityItem entityitem : func_181556_a(p_145891_0_.getWorld(), p_145891_0_.getXPos(), p_145891_0_.getYPos() + 1.0D, p_145891_0_.getZPos())) {
/*     */         
/* 410 */         if (putDropInInventoryAllSlots(p_145891_0_, entityitem))
/*     */         {
/* 412 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 417 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean pullItemFromSlot(IHopper hopper, IInventory inventoryIn, int index, EnumFacing direction) {
/* 422 */     ItemStack itemstack = inventoryIn.getStackInSlot(index);
/*     */     
/* 424 */     if (itemstack != null && canExtractItemFromSlot(inventoryIn, itemstack, index, direction)) {
/*     */       
/* 426 */       ItemStack itemstack1 = itemstack.copy();
/* 427 */       ItemStack itemstack2 = putStackInInventoryAllSlots(hopper, inventoryIn.decrStackSize(index, 1), (EnumFacing)null);
/*     */       
/* 429 */       if (itemstack2 == null || itemstack2.stackSize == 0) {
/*     */         
/* 431 */         inventoryIn.markDirty();
/* 432 */         return true;
/*     */       } 
/*     */       
/* 435 */       inventoryIn.setInventorySlotContents(index, itemstack1);
/*     */     } 
/*     */     
/* 438 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean putDropInInventoryAllSlots(IInventory p_145898_0_, EntityItem itemIn) {
/* 443 */     boolean flag = false;
/*     */     
/* 445 */     if (itemIn == null)
/*     */     {
/* 447 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 451 */     ItemStack itemstack = itemIn.getEntityItem().copy();
/* 452 */     ItemStack itemstack1 = putStackInInventoryAllSlots(p_145898_0_, itemstack, (EnumFacing)null);
/*     */     
/* 454 */     if (itemstack1 != null && itemstack1.stackSize != 0) {
/*     */       
/* 456 */       itemIn.setEntityItemStack(itemstack1);
/*     */     }
/*     */     else {
/*     */       
/* 460 */       flag = true;
/* 461 */       itemIn.setDead();
/*     */     } 
/*     */     
/* 464 */     return flag;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ItemStack putStackInInventoryAllSlots(IInventory inventoryIn, ItemStack stack, EnumFacing side) {
/* 470 */     if (inventoryIn instanceof ISidedInventory && side != null) {
/*     */       
/* 472 */       ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
/* 473 */       int[] aint = isidedinventory.getSlotsForFace(side);
/*     */       
/* 475 */       for (int k = 0; k < aint.length && stack != null && stack.stackSize > 0; k++)
/*     */       {
/* 477 */         stack = insertStack(inventoryIn, stack, aint[k], side);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 482 */       int i = inventoryIn.getSizeInventory();
/*     */       
/* 484 */       for (int j = 0; j < i && stack != null && stack.stackSize > 0; j++)
/*     */       {
/* 486 */         stack = insertStack(inventoryIn, stack, j, side);
/*     */       }
/*     */     } 
/*     */     
/* 490 */     if (stack != null && stack.stackSize == 0)
/*     */     {
/* 492 */       stack = null;
/*     */     }
/*     */     
/* 495 */     return stack;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean canInsertItemInSlot(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side) {
/* 500 */     return !inventoryIn.isItemValidForSlot(index, stack) ? false : ((!(inventoryIn instanceof ISidedInventory) || ((ISidedInventory)inventoryIn).canInsertItem(index, stack, side)));
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean canExtractItemFromSlot(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side) {
/* 505 */     return (!(inventoryIn instanceof ISidedInventory) || ((ISidedInventory)inventoryIn).canExtractItem(index, stack, side));
/*     */   }
/*     */ 
/*     */   
/*     */   private static ItemStack insertStack(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side) {
/* 510 */     ItemStack itemstack = inventoryIn.getStackInSlot(index);
/*     */     
/* 512 */     if (canInsertItemInSlot(inventoryIn, stack, index, side)) {
/*     */       
/* 514 */       boolean flag = false;
/*     */       
/* 516 */       if (itemstack == null) {
/*     */         
/* 518 */         inventoryIn.setInventorySlotContents(index, stack);
/* 519 */         stack = null;
/* 520 */         flag = true;
/*     */       }
/* 522 */       else if (canCombine(itemstack, stack)) {
/*     */         
/* 524 */         int i = stack.getMaxStackSize() - itemstack.stackSize;
/* 525 */         int j = Math.min(stack.stackSize, i);
/* 526 */         stack.stackSize -= j;
/* 527 */         itemstack.stackSize += j;
/* 528 */         flag = (j > 0);
/*     */       } 
/*     */       
/* 531 */       if (flag) {
/*     */         
/* 533 */         if (inventoryIn instanceof TileEntityHopper) {
/*     */           
/* 535 */           TileEntityHopper tileentityhopper = (TileEntityHopper)inventoryIn;
/*     */           
/* 537 */           if (tileentityhopper.mayTransfer())
/*     */           {
/* 539 */             tileentityhopper.setTransferCooldown(8);
/*     */           }
/*     */           
/* 542 */           inventoryIn.markDirty();
/*     */         } 
/*     */         
/* 545 */         inventoryIn.markDirty();
/*     */       } 
/*     */     } 
/*     */     
/* 549 */     return stack;
/*     */   }
/*     */ 
/*     */   
/*     */   private IInventory getInventoryForHopperTransfer() {
/* 554 */     EnumFacing enumfacing = BlockHopper.getFacing(getBlockMetadata());
/* 555 */     return getInventoryAtPosition(getWorld(), (this.pos.getX() + enumfacing.getFrontOffsetX()), (this.pos.getY() + enumfacing.getFrontOffsetY()), (this.pos.getZ() + enumfacing.getFrontOffsetZ()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static IInventory getHopperInventory(IHopper hopper) {
/* 560 */     return getInventoryAtPosition(hopper.getWorld(), hopper.getXPos(), hopper.getYPos() + 1.0D, hopper.getZPos());
/*     */   }
/*     */ 
/*     */   
/*     */   public static List<EntityItem> func_181556_a(World p_181556_0_, double p_181556_1_, double p_181556_3_, double p_181556_5_) {
/* 565 */     return p_181556_0_.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(p_181556_1_ - 0.5D, p_181556_3_ - 0.5D, p_181556_5_ - 0.5D, p_181556_1_ + 0.5D, p_181556_3_ + 0.5D, p_181556_5_ + 0.5D), EntitySelectors.selectAnything);
/*     */   }
/*     */   
/*     */   public static IInventory getInventoryAtPosition(World worldIn, double x, double y, double z) {
/*     */     ILockableContainer iLockableContainer;
/* 570 */     IInventory iInventory1, iinventory = null;
/* 571 */     int i = MathHelper.floor_double(x);
/* 572 */     int j = MathHelper.floor_double(y);
/* 573 */     int k = MathHelper.floor_double(z);
/* 574 */     BlockPos blockpos = new BlockPos(i, j, k);
/* 575 */     Block block = worldIn.getBlockState(blockpos).getBlock();
/*     */     
/* 577 */     if (block.hasTileEntity()) {
/*     */       
/* 579 */       TileEntity tileentity = worldIn.getTileEntity(blockpos);
/*     */       
/* 581 */       if (tileentity instanceof IInventory) {
/*     */         
/* 583 */         iinventory = (IInventory)tileentity;
/*     */         
/* 585 */         if (iinventory instanceof TileEntityChest && block instanceof BlockChest)
/*     */         {
/* 587 */           iLockableContainer = ((BlockChest)block).getLockableContainer(worldIn, blockpos);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 592 */     if (iLockableContainer == null) {
/*     */       
/* 594 */       List<Entity> list = worldIn.getEntitiesInAABBexcluding((Entity)null, new AxisAlignedBB(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D), EntitySelectors.selectInventories);
/*     */       
/* 596 */       if (list.size() > 0)
/*     */       {
/* 598 */         iInventory1 = (IInventory)list.get(worldIn.rand.nextInt(list.size()));
/*     */       }
/*     */     } 
/*     */     
/* 602 */     return iInventory1;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean canCombine(ItemStack stack1, ItemStack stack2) {
/* 607 */     return (stack1.getItem() != stack2.getItem()) ? false : ((stack1.getMetadata() != stack2.getMetadata()) ? false : ((stack1.stackSize > stack1.getMaxStackSize()) ? false : ItemStack.areItemStackTagsEqual(stack1, stack2)));
/*     */   }
/*     */ 
/*     */   
/*     */   public double getXPos() {
/* 612 */     return this.pos.getX() + 0.5D;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getYPos() {
/* 617 */     return this.pos.getY() + 0.5D;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getZPos() {
/* 622 */     return this.pos.getZ() + 0.5D;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTransferCooldown(int ticks) {
/* 627 */     this.transferCooldown = ticks;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOnTransferCooldown() {
/* 632 */     return (this.transferCooldown > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mayTransfer() {
/* 637 */     return (this.transferCooldown <= 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGuiID() {
/* 642 */     return "minecraft:hopper";
/*     */   }
/*     */ 
/*     */   
/*     */   public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
/* 647 */     return (Container)new ContainerHopper(playerInventory, this, playerIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getField(int id) {
/* 652 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setField(int id, int value) {}
/*     */ 
/*     */   
/*     */   public int getFieldCount() {
/* 661 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 666 */     for (int i = 0; i < this.inventory.length; i++)
/*     */     {
/* 668 */       this.inventory[i] = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\tileentity\TileEntityHopper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */