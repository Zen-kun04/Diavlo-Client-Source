/*     */ package net.minecraft.entity.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.ContainerHopper;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.tileentity.IHopper;
/*     */ import net.minecraft.tileentity.TileEntityHopper;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EntitySelectors;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityMinecartHopper extends EntityMinecartContainer implements IHopper {
/*  22 */   private int transferTicker = -1; private boolean isBlocked = true;
/*  23 */   private BlockPos field_174900_c = BlockPos.ORIGIN;
/*     */ 
/*     */   
/*     */   public EntityMinecartHopper(World worldIn) {
/*  27 */     super(worldIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityMinecartHopper(World worldIn, double x, double y, double z) {
/*  32 */     super(worldIn, x, y, z);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityMinecart.EnumMinecartType getMinecartType() {
/*  37 */     return EntityMinecart.EnumMinecartType.HOPPER;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getDefaultDisplayTile() {
/*  42 */     return Blocks.hopper.getDefaultState();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDefaultDisplayTileOffset() {
/*  47 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSizeInventory() {
/*  52 */     return 5;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean interactFirst(EntityPlayer playerIn) {
/*  57 */     if (!this.worldObj.isRemote)
/*     */     {
/*  59 */       playerIn.displayGUIChest((IInventory)this);
/*     */     }
/*     */     
/*  62 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onActivatorRailPass(int x, int y, int z, boolean receivingPower) {
/*  67 */     boolean flag = !receivingPower;
/*     */     
/*  69 */     if (flag != getBlocked())
/*     */     {
/*  71 */       setBlocked(flag);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getBlocked() {
/*  77 */     return this.isBlocked;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlocked(boolean p_96110_1_) {
/*  82 */     this.isBlocked = p_96110_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public World getWorld() {
/*  87 */     return this.worldObj;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getXPos() {
/*  92 */     return this.posX;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getYPos() {
/*  97 */     return this.posY + 0.5D;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getZPos() {
/* 102 */     return this.posZ;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 107 */     super.onUpdate();
/*     */     
/* 109 */     if (!this.worldObj.isRemote && isEntityAlive() && getBlocked()) {
/*     */       
/* 111 */       BlockPos blockpos = new BlockPos(this);
/*     */       
/* 113 */       if (blockpos.equals(this.field_174900_c)) {
/*     */         
/* 115 */         this.transferTicker--;
/*     */       }
/*     */       else {
/*     */         
/* 119 */         setTransferTicker(0);
/*     */       } 
/*     */       
/* 122 */       if (!canTransfer()) {
/*     */         
/* 124 */         setTransferTicker(0);
/*     */         
/* 126 */         if (func_96112_aD()) {
/*     */           
/* 128 */           setTransferTicker(4);
/* 129 */           markDirty();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean func_96112_aD() {
/* 137 */     if (TileEntityHopper.captureDroppedItems(this))
/*     */     {
/* 139 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 143 */     List<EntityItem> list = this.worldObj.getEntitiesWithinAABB(EntityItem.class, getEntityBoundingBox().expand(0.25D, 0.0D, 0.25D), EntitySelectors.selectAnything);
/*     */     
/* 145 */     if (list.size() > 0)
/*     */     {
/* 147 */       TileEntityHopper.putDropInInventoryAllSlots((IInventory)this, list.get(0));
/*     */     }
/*     */     
/* 150 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void killMinecart(DamageSource source) {
/* 156 */     super.killMinecart(source);
/*     */     
/* 158 */     if (this.worldObj.getGameRules().getBoolean("doEntityDrops"))
/*     */     {
/* 160 */       dropItemWithOffset(Item.getItemFromBlock((Block)Blocks.hopper), 1, 0.0F);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeEntityToNBT(NBTTagCompound tagCompound) {
/* 166 */     super.writeEntityToNBT(tagCompound);
/* 167 */     tagCompound.setInteger("TransferCooldown", this.transferTicker);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readEntityFromNBT(NBTTagCompound tagCompund) {
/* 172 */     super.readEntityFromNBT(tagCompund);
/* 173 */     this.transferTicker = tagCompund.getInteger("TransferCooldown");
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTransferTicker(int p_98042_1_) {
/* 178 */     this.transferTicker = p_98042_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canTransfer() {
/* 183 */     return (this.transferTicker > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGuiID() {
/* 188 */     return "minecraft:hopper";
/*     */   }
/*     */ 
/*     */   
/*     */   public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
/* 193 */     return (Container)new ContainerHopper(playerInventory, (IInventory)this, playerIn);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\item\EntityMinecartHopper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */