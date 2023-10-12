/*     */ package net.minecraft.entity.item;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityHanging;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemMap;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.storage.MapData;
/*     */ 
/*     */ public class EntityItemFrame extends EntityHanging {
/*  19 */   private float itemDropChance = 1.0F;
/*     */ 
/*     */   
/*     */   public EntityItemFrame(World worldIn) {
/*  23 */     super(worldIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityItemFrame(World worldIn, BlockPos p_i45852_2_, EnumFacing p_i45852_3_) {
/*  28 */     super(worldIn, p_i45852_2_);
/*  29 */     updateFacingWithBoundingBox(p_i45852_3_);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  34 */     getDataWatcher().addObjectByDataType(8, 5);
/*  35 */     getDataWatcher().addObject(9, Byte.valueOf((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   public float getCollisionBorderSize() {
/*  40 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityFrom(DamageSource source, float amount) {
/*  45 */     if (isEntityInvulnerable(source))
/*     */     {
/*  47 */       return false;
/*     */     }
/*  49 */     if (!source.isExplosion() && getDisplayedItem() != null) {
/*     */       
/*  51 */       if (!this.worldObj.isRemote) {
/*     */         
/*  53 */         dropItemOrSelf(source.getEntity(), false);
/*  54 */         setDisplayedItem((ItemStack)null);
/*     */       } 
/*     */       
/*  57 */       return true;
/*     */     } 
/*     */ 
/*     */     
/*  61 */     return super.attackEntityFrom(source, amount);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getWidthPixels() {
/*  67 */     return 12;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeightPixels() {
/*  72 */     return 12;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInRangeToRenderDist(double distance) {
/*  77 */     double d0 = 16.0D;
/*  78 */     d0 = d0 * 64.0D * this.renderDistanceWeight;
/*  79 */     return (distance < d0 * d0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBroken(Entity brokenEntity) {
/*  84 */     dropItemOrSelf(brokenEntity, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void dropItemOrSelf(Entity p_146065_1_, boolean p_146065_2_) {
/*  89 */     if (this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
/*     */       
/*  91 */       ItemStack itemstack = getDisplayedItem();
/*     */       
/*  93 */       if (p_146065_1_ instanceof EntityPlayer) {
/*     */         
/*  95 */         EntityPlayer entityplayer = (EntityPlayer)p_146065_1_;
/*     */         
/*  97 */         if (entityplayer.capabilities.isCreativeMode) {
/*     */           
/*  99 */           removeFrameFromMap(itemstack);
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/* 104 */       if (p_146065_2_)
/*     */       {
/* 106 */         entityDropItem(new ItemStack(Items.item_frame), 0.0F);
/*     */       }
/*     */       
/* 109 */       if (itemstack != null && this.rand.nextFloat() < this.itemDropChance) {
/*     */         
/* 111 */         itemstack = itemstack.copy();
/* 112 */         removeFrameFromMap(itemstack);
/* 113 */         entityDropItem(itemstack, 0.0F);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void removeFrameFromMap(ItemStack p_110131_1_) {
/* 120 */     if (p_110131_1_ != null) {
/*     */       
/* 122 */       if (p_110131_1_.getItem() == Items.filled_map) {
/*     */         
/* 124 */         MapData mapdata = ((ItemMap)p_110131_1_.getItem()).getMapData(p_110131_1_, this.worldObj);
/* 125 */         mapdata.mapDecorations.remove("frame-" + getEntityId());
/*     */       } 
/*     */       
/* 128 */       p_110131_1_.setItemFrame((EntityItemFrame)null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getDisplayedItem() {
/* 134 */     return getDataWatcher().getWatchableObjectItemStack(8);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDisplayedItem(ItemStack p_82334_1_) {
/* 139 */     setDisplayedItemWithUpdate(p_82334_1_, true);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setDisplayedItemWithUpdate(ItemStack p_174864_1_, boolean p_174864_2_) {
/* 144 */     if (p_174864_1_ != null) {
/*     */       
/* 146 */       p_174864_1_ = p_174864_1_.copy();
/* 147 */       p_174864_1_.stackSize = 1;
/* 148 */       p_174864_1_.setItemFrame(this);
/*     */     } 
/*     */     
/* 151 */     getDataWatcher().updateObject(8, p_174864_1_);
/* 152 */     getDataWatcher().setObjectWatched(8);
/*     */     
/* 154 */     if (p_174864_2_ && this.hangingPosition != null)
/*     */     {
/* 156 */       this.worldObj.updateComparatorOutputLevel(this.hangingPosition, Blocks.air);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRotation() {
/* 162 */     return getDataWatcher().getWatchableObjectByte(9);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItemRotation(int p_82336_1_) {
/* 167 */     func_174865_a(p_82336_1_, true);
/*     */   }
/*     */ 
/*     */   
/*     */   private void func_174865_a(int p_174865_1_, boolean p_174865_2_) {
/* 172 */     getDataWatcher().updateObject(9, Byte.valueOf((byte)(p_174865_1_ % 8)));
/*     */     
/* 174 */     if (p_174865_2_ && this.hangingPosition != null)
/*     */     {
/* 176 */       this.worldObj.updateComparatorOutputLevel(this.hangingPosition, Blocks.air);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/* 182 */     if (getDisplayedItem() != null) {
/*     */       
/* 184 */       tagCompound.setTag("Item", (NBTBase)getDisplayedItem().writeToNBT(new NBTTagCompound()));
/* 185 */       tagCompound.setByte("ItemRotation", (byte)getRotation());
/* 186 */       tagCompound.setFloat("ItemDropChance", this.itemDropChance);
/*     */     } 
/*     */     
/* 189 */     super.writeEntityToNBT(tagCompound);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/* 194 */     NBTTagCompound nbttagcompound = tagCompund.getCompoundTag("Item");
/*     */     
/* 196 */     if (nbttagcompound != null && !nbttagcompound.hasNoTags()) {
/*     */       
/* 198 */       setDisplayedItemWithUpdate(ItemStack.loadItemStackFromNBT(nbttagcompound), false);
/* 199 */       func_174865_a(tagCompund.getByte("ItemRotation"), false);
/*     */       
/* 201 */       if (tagCompund.hasKey("ItemDropChance", 99))
/*     */       {
/* 203 */         this.itemDropChance = tagCompund.getFloat("ItemDropChance");
/*     */       }
/*     */       
/* 206 */       if (tagCompund.hasKey("Direction"))
/*     */       {
/* 208 */         func_174865_a(getRotation() * 2, false);
/*     */       }
/*     */     } 
/*     */     
/* 212 */     super.readEntityFromNBT(tagCompund);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean interactFirst(EntityPlayer playerIn) {
/* 217 */     if (getDisplayedItem() == null) {
/*     */       
/* 219 */       ItemStack itemstack = playerIn.getHeldItem();
/*     */       
/* 221 */       if (itemstack != null && !this.worldObj.isRemote)
/*     */       {
/* 223 */         setDisplayedItem(itemstack);
/*     */         
/* 225 */         if (!playerIn.capabilities.isCreativeMode && --itemstack.stackSize <= 0)
/*     */         {
/* 227 */           playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, (ItemStack)null);
/*     */         }
/*     */       }
/*     */     
/* 231 */     } else if (!this.worldObj.isRemote) {
/*     */       
/* 233 */       setItemRotation(getRotation() + 1);
/*     */     } 
/*     */     
/* 236 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int func_174866_q() {
/* 241 */     return (getDisplayedItem() == null) ? 0 : (getRotation() % 8 + 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\item\EntityItemFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */