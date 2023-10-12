/*     */ package net.minecraft.tileentity;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockChest;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.ContainerChest;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.inventory.InventoryLargeChest;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.ITickable;
/*     */ 
/*     */ public class TileEntityChest extends TileEntityLockable implements ITickable, IInventory {
/*  21 */   private ItemStack[] chestContents = new ItemStack[27];
/*     */   
/*     */   public boolean adjacentChestChecked;
/*     */   public TileEntityChest adjacentChestZNeg;
/*     */   public TileEntityChest adjacentChestXPos;
/*     */   public TileEntityChest adjacentChestXNeg;
/*     */   public TileEntityChest adjacentChestZPos;
/*     */   public float lidAngle;
/*     */   public float prevLidAngle;
/*     */   public int numPlayersUsing;
/*     */   private int ticksSinceSync;
/*     */   private int cachedChestType;
/*     */   private String customName;
/*     */   
/*     */   public TileEntityChest() {
/*  36 */     this.cachedChestType = -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public TileEntityChest(int chestType) {
/*  41 */     this.cachedChestType = chestType;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSizeInventory() {
/*  46 */     return 27;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getStackInSlot(int index) {
/*  51 */     return this.chestContents[index];
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack decrStackSize(int index, int count) {
/*  56 */     if (this.chestContents[index] != null) {
/*     */       
/*  58 */       if ((this.chestContents[index]).stackSize <= count) {
/*     */         
/*  60 */         ItemStack itemstack1 = this.chestContents[index];
/*  61 */         this.chestContents[index] = null;
/*  62 */         markDirty();
/*  63 */         return itemstack1;
/*     */       } 
/*     */ 
/*     */       
/*  67 */       ItemStack itemstack = this.chestContents[index].splitStack(count);
/*     */       
/*  69 */       if ((this.chestContents[index]).stackSize == 0)
/*     */       {
/*  71 */         this.chestContents[index] = null;
/*     */       }
/*     */       
/*  74 */       markDirty();
/*  75 */       return itemstack;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  80 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack removeStackFromSlot(int index) {
/*  86 */     if (this.chestContents[index] != null) {
/*     */       
/*  88 */       ItemStack itemstack = this.chestContents[index];
/*  89 */       this.chestContents[index] = null;
/*  90 */       return itemstack;
/*     */     } 
/*     */ 
/*     */     
/*  94 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInventorySlotContents(int index, ItemStack stack) {
/* 100 */     this.chestContents[index] = stack;
/*     */     
/* 102 */     if (stack != null && stack.stackSize > getInventoryStackLimit())
/*     */     {
/* 104 */       stack.stackSize = getInventoryStackLimit();
/*     */     }
/*     */     
/* 107 */     markDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 112 */     return hasCustomName() ? this.customName : "container.chest";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasCustomName() {
/* 117 */     return (this.customName != null && this.customName.length() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCustomName(String name) {
/* 122 */     this.customName = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public void readFromNBT(NBTTagCompound compound) {
/* 127 */     super.readFromNBT(compound);
/* 128 */     NBTTagList nbttaglist = compound.getTagList("Items", 10);
/* 129 */     this.chestContents = new ItemStack[getSizeInventory()];
/*     */     
/* 131 */     if (compound.hasKey("CustomName", 8))
/*     */     {
/* 133 */       this.customName = compound.getString("CustomName");
/*     */     }
/*     */     
/* 136 */     for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*     */       
/* 138 */       NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/* 139 */       int j = nbttagcompound.getByte("Slot") & 0xFF;
/*     */       
/* 141 */       if (j >= 0 && j < this.chestContents.length)
/*     */       {
/* 143 */         this.chestContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeToNBT(NBTTagCompound compound) {
/* 150 */     super.writeToNBT(compound);
/* 151 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/* 153 */     for (int i = 0; i < this.chestContents.length; i++) {
/*     */       
/* 155 */       if (this.chestContents[i] != null) {
/*     */         
/* 157 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 158 */         nbttagcompound.setByte("Slot", (byte)i);
/* 159 */         this.chestContents[i].writeToNBT(nbttagcompound);
/* 160 */         nbttaglist.appendTag((NBTBase)nbttagcompound);
/*     */       } 
/*     */     } 
/*     */     
/* 164 */     compound.setTag("Items", (NBTBase)nbttaglist);
/*     */     
/* 166 */     if (hasCustomName())
/*     */     {
/* 168 */       compound.setString("CustomName", this.customName);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInventoryStackLimit() {
/* 174 */     return 64;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUseableByPlayer(EntityPlayer player) {
/* 179 */     return (this.worldObj.getTileEntity(this.pos) != this) ? false : ((player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D));
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateContainingBlockInfo() {
/* 184 */     super.updateContainingBlockInfo();
/* 185 */     this.adjacentChestChecked = false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void func_174910_a(TileEntityChest chestTe, EnumFacing side) {
/* 191 */     if (chestTe.isInvalid()) {
/*     */       
/* 193 */       this.adjacentChestChecked = false;
/*     */     }
/* 195 */     else if (this.adjacentChestChecked) {
/*     */       
/* 197 */       switch (side) {
/*     */         
/*     */         case NORTH:
/* 200 */           if (this.adjacentChestZNeg != chestTe)
/*     */           {
/* 202 */             this.adjacentChestChecked = false;
/*     */           }
/*     */           break;
/*     */ 
/*     */         
/*     */         case SOUTH:
/* 208 */           if (this.adjacentChestZPos != chestTe)
/*     */           {
/* 210 */             this.adjacentChestChecked = false;
/*     */           }
/*     */           break;
/*     */ 
/*     */         
/*     */         case EAST:
/* 216 */           if (this.adjacentChestXPos != chestTe)
/*     */           {
/* 218 */             this.adjacentChestChecked = false;
/*     */           }
/*     */           break;
/*     */ 
/*     */         
/*     */         case WEST:
/* 224 */           if (this.adjacentChestXNeg != chestTe)
/*     */           {
/* 226 */             this.adjacentChestChecked = false;
/*     */           }
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void checkForAdjacentChests() {
/* 234 */     if (!this.adjacentChestChecked) {
/*     */       
/* 236 */       this.adjacentChestChecked = true;
/* 237 */       this.adjacentChestXNeg = getAdjacentChest(EnumFacing.WEST);
/* 238 */       this.adjacentChestXPos = getAdjacentChest(EnumFacing.EAST);
/* 239 */       this.adjacentChestZNeg = getAdjacentChest(EnumFacing.NORTH);
/* 240 */       this.adjacentChestZPos = getAdjacentChest(EnumFacing.SOUTH);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected TileEntityChest getAdjacentChest(EnumFacing side) {
/* 246 */     BlockPos blockpos = this.pos.offset(side);
/*     */     
/* 248 */     if (isChestAt(blockpos)) {
/*     */       
/* 250 */       TileEntity tileentity = this.worldObj.getTileEntity(blockpos);
/*     */       
/* 252 */       if (tileentity instanceof TileEntityChest) {
/*     */         
/* 254 */         TileEntityChest tileentitychest = (TileEntityChest)tileentity;
/* 255 */         tileentitychest.func_174910_a(this, side.getOpposite());
/* 256 */         return tileentitychest;
/*     */       } 
/*     */     } 
/*     */     
/* 260 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isChestAt(BlockPos posIn) {
/* 265 */     if (this.worldObj == null)
/*     */     {
/* 267 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 271 */     Block block = this.worldObj.getBlockState(posIn).getBlock();
/* 272 */     return (block instanceof BlockChest && ((BlockChest)block).chestType == getChestType());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void update() {
/* 278 */     checkForAdjacentChests();
/* 279 */     int i = this.pos.getX();
/* 280 */     int j = this.pos.getY();
/* 281 */     int k = this.pos.getZ();
/* 282 */     this.ticksSinceSync++;
/*     */     
/* 284 */     if (!this.worldObj.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + i + j + k) % 200 == 0) {
/*     */       
/* 286 */       this.numPlayersUsing = 0;
/* 287 */       float f = 5.0F;
/*     */       
/* 289 */       for (EntityPlayer entityplayer : this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB((i - f), (j - f), (k - f), ((i + 1) + f), ((j + 1) + f), ((k + 1) + f)))) {
/*     */         
/* 291 */         if (entityplayer.openContainer instanceof ContainerChest) {
/*     */           
/* 293 */           IInventory iinventory = ((ContainerChest)entityplayer.openContainer).getLowerChestInventory();
/*     */           
/* 295 */           if (iinventory == this || (iinventory instanceof InventoryLargeChest && ((InventoryLargeChest)iinventory).isPartOfLargeChest(this)))
/*     */           {
/* 297 */             this.numPlayersUsing++;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 303 */     this.prevLidAngle = this.lidAngle;
/* 304 */     float f1 = 0.1F;
/*     */     
/* 306 */     if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null) {
/*     */       
/* 308 */       double d1 = i + 0.5D;
/* 309 */       double d2 = k + 0.5D;
/*     */       
/* 311 */       if (this.adjacentChestZPos != null)
/*     */       {
/* 313 */         d2 += 0.5D;
/*     */       }
/*     */       
/* 316 */       if (this.adjacentChestXPos != null)
/*     */       {
/* 318 */         d1 += 0.5D;
/*     */       }
/*     */       
/* 321 */       this.worldObj.playSoundEffect(d1, j + 0.5D, d2, "random.chestopen", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
/*     */     } 
/*     */     
/* 324 */     if ((this.numPlayersUsing == 0 && this.lidAngle > 0.0F) || (this.numPlayersUsing > 0 && this.lidAngle < 1.0F)) {
/*     */       
/* 326 */       float f2 = this.lidAngle;
/*     */       
/* 328 */       if (this.numPlayersUsing > 0) {
/*     */         
/* 330 */         this.lidAngle += f1;
/*     */       }
/*     */       else {
/*     */         
/* 334 */         this.lidAngle -= f1;
/*     */       } 
/*     */       
/* 337 */       if (this.lidAngle > 1.0F)
/*     */       {
/* 339 */         this.lidAngle = 1.0F;
/*     */       }
/*     */       
/* 342 */       float f3 = 0.5F;
/*     */       
/* 344 */       if (this.lidAngle < f3 && f2 >= f3 && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null) {
/*     */         
/* 346 */         double d3 = i + 0.5D;
/* 347 */         double d0 = k + 0.5D;
/*     */         
/* 349 */         if (this.adjacentChestZPos != null)
/*     */         {
/* 351 */           d0 += 0.5D;
/*     */         }
/*     */         
/* 354 */         if (this.adjacentChestXPos != null)
/*     */         {
/* 356 */           d3 += 0.5D;
/*     */         }
/*     */         
/* 359 */         this.worldObj.playSoundEffect(d3, j + 0.5D, d0, "random.chestclosed", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
/*     */       } 
/*     */       
/* 362 */       if (this.lidAngle < 0.0F)
/*     */       {
/* 364 */         this.lidAngle = 0.0F;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean receiveClientEvent(int id, int type) {
/* 371 */     if (id == 1) {
/*     */       
/* 373 */       this.numPlayersUsing = type;
/* 374 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 378 */     return super.receiveClientEvent(id, type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void openInventory(EntityPlayer player) {
/* 384 */     if (!player.isSpectator()) {
/*     */       
/* 386 */       if (this.numPlayersUsing < 0)
/*     */       {
/* 388 */         this.numPlayersUsing = 0;
/*     */       }
/*     */       
/* 391 */       this.numPlayersUsing++;
/* 392 */       this.worldObj.addBlockEvent(this.pos, getBlockType(), 1, this.numPlayersUsing);
/* 393 */       this.worldObj.notifyNeighborsOfStateChange(this.pos, getBlockType());
/* 394 */       this.worldObj.notifyNeighborsOfStateChange(this.pos.down(), getBlockType());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeInventory(EntityPlayer player) {
/* 400 */     if (!player.isSpectator() && getBlockType() instanceof BlockChest) {
/*     */       
/* 402 */       this.numPlayersUsing--;
/* 403 */       this.worldObj.addBlockEvent(this.pos, getBlockType(), 1, this.numPlayersUsing);
/* 404 */       this.worldObj.notifyNeighborsOfStateChange(this.pos, getBlockType());
/* 405 */       this.worldObj.notifyNeighborsOfStateChange(this.pos.down(), getBlockType());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isItemValidForSlot(int index, ItemStack stack) {
/* 411 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void invalidate() {
/* 416 */     super.invalidate();
/* 417 */     updateContainingBlockInfo();
/* 418 */     checkForAdjacentChests();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getChestType() {
/* 423 */     if (this.cachedChestType == -1) {
/*     */       
/* 425 */       if (this.worldObj == null || !(getBlockType() instanceof BlockChest))
/*     */       {
/* 427 */         return 0;
/*     */       }
/*     */       
/* 430 */       this.cachedChestType = ((BlockChest)getBlockType()).chestType;
/*     */     } 
/*     */     
/* 433 */     return this.cachedChestType;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGuiID() {
/* 438 */     return "minecraft:chest";
/*     */   }
/*     */ 
/*     */   
/*     */   public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
/* 443 */     return (Container)new ContainerChest((IInventory)playerInventory, this, playerIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getField(int id) {
/* 448 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setField(int id, int value) {}
/*     */ 
/*     */   
/*     */   public int getFieldCount() {
/* 457 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 462 */     for (int i = 0; i < this.chestContents.length; i++)
/*     */     {
/* 464 */       this.chestContents[i] = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\tileentity\TileEntityChest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */