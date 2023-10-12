/*     */ package net.minecraft.tileentity;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockStainedGlass;
/*     */ import net.minecraft.block.BlockStainedGlassPane;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.passive.EntitySheep;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.ContainerBeacon;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.stats.AchievementList;
/*     */ import net.minecraft.stats.StatBase;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ITickable;
/*     */ 
/*     */ public class TileEntityBeacon extends TileEntityLockable implements ITickable, IInventory {
/*  32 */   public static final Potion[][] effectsList = new Potion[][] { { Potion.moveSpeed, Potion.digSpeed }, { Potion.resistance, Potion.jump }, { Potion.damageBoost }, { Potion.regeneration } };
/*  33 */   private final List<BeamSegment> beamSegments = Lists.newArrayList();
/*     */   private long beamRenderCounter;
/*     */   private float field_146014_j;
/*     */   private boolean isComplete;
/*  37 */   private int levels = -1;
/*     */   
/*     */   private int primaryEffect;
/*     */   private int secondaryEffect;
/*     */   private ItemStack payment;
/*     */   private String customName;
/*     */   
/*     */   public void update() {
/*  45 */     if (this.worldObj.getTotalWorldTime() % 80L == 0L)
/*     */     {
/*  47 */       updateBeacon();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateBeacon() {
/*  53 */     updateSegmentColors();
/*  54 */     addEffectsToPlayers();
/*     */   }
/*     */ 
/*     */   
/*     */   private void addEffectsToPlayers() {
/*  59 */     if (this.isComplete && this.levels > 0 && !this.worldObj.isRemote && this.primaryEffect > 0) {
/*     */       
/*  61 */       double d0 = (this.levels * 10 + 10);
/*  62 */       int i = 0;
/*     */       
/*  64 */       if (this.levels >= 4 && this.primaryEffect == this.secondaryEffect)
/*     */       {
/*  66 */         i = 1;
/*     */       }
/*     */       
/*  69 */       int j = this.pos.getX();
/*  70 */       int k = this.pos.getY();
/*  71 */       int l = this.pos.getZ();
/*  72 */       AxisAlignedBB axisalignedbb = (new AxisAlignedBB(j, k, l, (j + 1), (k + 1), (l + 1))).expand(d0, d0, d0).addCoord(0.0D, this.worldObj.getHeight(), 0.0D);
/*  73 */       List<EntityPlayer> list = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);
/*     */       
/*  75 */       for (EntityPlayer entityplayer : list)
/*     */       {
/*  77 */         entityplayer.addPotionEffect(new PotionEffect(this.primaryEffect, 180, i, true, true));
/*     */       }
/*     */       
/*  80 */       if (this.levels >= 4 && this.primaryEffect != this.secondaryEffect && this.secondaryEffect > 0)
/*     */       {
/*  82 */         for (EntityPlayer entityplayer1 : list)
/*     */         {
/*  84 */           entityplayer1.addPotionEffect(new PotionEffect(this.secondaryEffect, 180, 0, true, true));
/*     */         }
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateSegmentColors() {
/*  92 */     int i = this.levels;
/*  93 */     int j = this.pos.getX();
/*  94 */     int k = this.pos.getY();
/*  95 */     int l = this.pos.getZ();
/*  96 */     this.levels = 0;
/*  97 */     this.beamSegments.clear();
/*  98 */     this.isComplete = true;
/*  99 */     BeamSegment tileentitybeacon$beamsegment = new BeamSegment(EntitySheep.getDyeRgb(EnumDyeColor.WHITE));
/* 100 */     this.beamSegments.add(tileentitybeacon$beamsegment);
/* 101 */     boolean flag = true;
/* 102 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*     */     
/* 104 */     int i1 = k + 1; while (true) { float[] afloat; if (i1 < 256)
/*     */       
/* 106 */       { IBlockState iblockstate = this.worldObj.getBlockState((BlockPos)blockpos$mutableblockpos.set(j, i1, l));
/*     */ 
/*     */         
/* 109 */         if (iblockstate.getBlock() == Blocks.stained_glass)
/*     */         
/* 111 */         { afloat = EntitySheep.getDyeRgb((EnumDyeColor)iblockstate.getValue((IProperty)BlockStainedGlass.COLOR)); }
/*     */         
/*     */         else
/*     */         
/* 115 */         { if (iblockstate.getBlock() != Blocks.stained_glass_pane)
/*     */           
/* 117 */           { if (iblockstate.getBlock().getLightOpacity() >= 15 && iblockstate.getBlock() != Blocks.bedrock) {
/*     */               
/* 119 */               this.isComplete = false;
/* 120 */               this.beamSegments.clear();
/*     */               
/*     */               break;
/*     */             } 
/* 124 */             tileentitybeacon$beamsegment.incrementHeight(); }
/*     */           
/*     */           else
/*     */           
/* 128 */           { afloat = EntitySheep.getDyeRgb((EnumDyeColor)iblockstate.getValue((IProperty)BlockStainedGlassPane.COLOR));
/*     */ 
/*     */             
/* 131 */             if (!flag)
/*     */             {
/* 133 */               afloat = new float[] { (tileentitybeacon$beamsegment.getColors()[0] + afloat[0]) / 2.0F, (tileentitybeacon$beamsegment.getColors()[1] + afloat[1]) / 2.0F, (tileentitybeacon$beamsegment.getColors()[2] + afloat[2]) / 2.0F }; }  }  i1++; }  } else { break; }  if (!flag) afloat = new float[] { (tileentitybeacon$beamsegment.getColors()[0] + afloat[0]) / 2.0F, (tileentitybeacon$beamsegment.getColors()[1] + afloat[1]) / 2.0F, (tileentitybeacon$beamsegment.getColors()[2] + afloat[2]) / 2.0F };
/*     */        }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 149 */     if (this.isComplete) {
/*     */       
/* 151 */       for (int l1 = 1; l1 <= 4; this.levels = l1++) {
/*     */         
/* 153 */         int i2 = k - l1;
/*     */         
/* 155 */         if (i2 < 0) {
/*     */           break;
/*     */         }
/*     */ 
/*     */         
/* 160 */         boolean flag1 = true;
/*     */         
/* 162 */         for (int j1 = j - l1; j1 <= j + l1 && flag1; j1++) {
/*     */           
/* 164 */           for (int k1 = l - l1; k1 <= l + l1; k1++) {
/*     */             
/* 166 */             Block block = this.worldObj.getBlockState(new BlockPos(j1, i2, k1)).getBlock();
/*     */             
/* 168 */             if (block != Blocks.emerald_block && block != Blocks.gold_block && block != Blocks.diamond_block && block != Blocks.iron_block) {
/*     */               
/* 170 */               flag1 = false;
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } 
/* 176 */         if (!flag1) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 182 */       if (this.levels == 0)
/*     */       {
/* 184 */         this.isComplete = false;
/*     */       }
/*     */     } 
/*     */     
/* 188 */     if (!this.worldObj.isRemote && this.levels == 4 && i < this.levels)
/*     */     {
/* 190 */       for (EntityPlayer entityplayer : this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, (new AxisAlignedBB(j, k, l, j, (k - 4), l)).expand(10.0D, 5.0D, 10.0D)))
/*     */       {
/* 192 */         entityplayer.triggerAchievement((StatBase)AchievementList.fullBeacon);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BeamSegment> getBeamSegments() {
/* 199 */     return this.beamSegments;
/*     */   }
/*     */ 
/*     */   
/*     */   public float shouldBeamRender() {
/* 204 */     if (!this.isComplete)
/*     */     {
/* 206 */       return 0.0F;
/*     */     }
/*     */ 
/*     */     
/* 210 */     int i = (int)(this.worldObj.getTotalWorldTime() - this.beamRenderCounter);
/* 211 */     this.beamRenderCounter = this.worldObj.getTotalWorldTime();
/*     */     
/* 213 */     if (i > 1) {
/*     */       
/* 215 */       this.field_146014_j -= i / 40.0F;
/*     */       
/* 217 */       if (this.field_146014_j < 0.0F)
/*     */       {
/* 219 */         this.field_146014_j = 0.0F;
/*     */       }
/*     */     } 
/*     */     
/* 223 */     this.field_146014_j += 0.025F;
/*     */     
/* 225 */     if (this.field_146014_j > 1.0F)
/*     */     {
/* 227 */       this.field_146014_j = 1.0F;
/*     */     }
/*     */     
/* 230 */     return this.field_146014_j;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Packet getDescriptionPacket() {
/* 236 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 237 */     writeToNBT(nbttagcompound);
/* 238 */     return (Packet)new S35PacketUpdateTileEntity(this.pos, 3, nbttagcompound);
/*     */   }
/*     */ 
/*     */   
/*     */   public double getMaxRenderDistanceSquared() {
/* 243 */     return 65536.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   private int func_183001_h(int p_183001_1_) {
/* 248 */     if (p_183001_1_ >= 0 && p_183001_1_ < Potion.potionTypes.length && Potion.potionTypes[p_183001_1_] != null) {
/*     */       
/* 250 */       Potion potion = Potion.potionTypes[p_183001_1_];
/* 251 */       return (potion != Potion.moveSpeed && potion != Potion.digSpeed && potion != Potion.resistance && potion != Potion.jump && potion != Potion.damageBoost && potion != Potion.regeneration) ? 0 : p_183001_1_;
/*     */     } 
/*     */ 
/*     */     
/* 255 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void readFromNBT(NBTTagCompound compound) {
/* 261 */     super.readFromNBT(compound);
/* 262 */     this.primaryEffect = func_183001_h(compound.getInteger("Primary"));
/* 263 */     this.secondaryEffect = func_183001_h(compound.getInteger("Secondary"));
/* 264 */     this.levels = compound.getInteger("Levels");
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeToNBT(NBTTagCompound compound) {
/* 269 */     super.writeToNBT(compound);
/* 270 */     compound.setInteger("Primary", this.primaryEffect);
/* 271 */     compound.setInteger("Secondary", this.secondaryEffect);
/* 272 */     compound.setInteger("Levels", this.levels);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSizeInventory() {
/* 277 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getStackInSlot(int index) {
/* 282 */     return (index == 0) ? this.payment : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack decrStackSize(int index, int count) {
/* 287 */     if (index == 0 && this.payment != null) {
/*     */       
/* 289 */       if (count >= this.payment.stackSize) {
/*     */         
/* 291 */         ItemStack itemstack = this.payment;
/* 292 */         this.payment = null;
/* 293 */         return itemstack;
/*     */       } 
/*     */ 
/*     */       
/* 297 */       this.payment.stackSize -= count;
/* 298 */       return new ItemStack(this.payment.getItem(), count, this.payment.getMetadata());
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 303 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack removeStackFromSlot(int index) {
/* 309 */     if (index == 0 && this.payment != null) {
/*     */       
/* 311 */       ItemStack itemstack = this.payment;
/* 312 */       this.payment = null;
/* 313 */       return itemstack;
/*     */     } 
/*     */ 
/*     */     
/* 317 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInventorySlotContents(int index, ItemStack stack) {
/* 323 */     if (index == 0)
/*     */     {
/* 325 */       this.payment = stack;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 331 */     return hasCustomName() ? this.customName : "container.beacon";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasCustomName() {
/* 336 */     return (this.customName != null && this.customName.length() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 341 */     this.customName = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInventoryStackLimit() {
/* 346 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUseableByPlayer(EntityPlayer player) {
/* 351 */     return (this.worldObj.getTileEntity(this.pos) != this) ? false : ((player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D));
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
/* 364 */     return (stack.getItem() == Items.emerald || stack.getItem() == Items.diamond || stack.getItem() == Items.gold_ingot || stack.getItem() == Items.iron_ingot);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGuiID() {
/* 369 */     return "minecraft:beacon";
/*     */   }
/*     */ 
/*     */   
/*     */   public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
/* 374 */     return (Container)new ContainerBeacon((IInventory)playerInventory, this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getField(int id) {
/* 379 */     switch (id) {
/*     */       
/*     */       case 0:
/* 382 */         return this.levels;
/*     */       
/*     */       case 1:
/* 385 */         return this.primaryEffect;
/*     */       
/*     */       case 2:
/* 388 */         return this.secondaryEffect;
/*     */     } 
/*     */     
/* 391 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setField(int id, int value) {
/* 397 */     switch (id) {
/*     */       
/*     */       case 0:
/* 400 */         this.levels = value;
/*     */         break;
/*     */       
/*     */       case 1:
/* 404 */         this.primaryEffect = func_183001_h(value);
/*     */         break;
/*     */       
/*     */       case 2:
/* 408 */         this.secondaryEffect = func_183001_h(value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getFieldCount() {
/* 414 */     return 3;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 419 */     this.payment = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean receiveClientEvent(int id, int type) {
/* 424 */     if (id == 1) {
/*     */       
/* 426 */       updateBeacon();
/* 427 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 431 */     return super.receiveClientEvent(id, type);
/*     */   }
/*     */ 
/*     */   
/*     */   public static class BeamSegment
/*     */   {
/*     */     private final float[] colors;
/*     */     
/*     */     private int height;
/*     */     
/*     */     public BeamSegment(float[] p_i45669_1_) {
/* 442 */       this.colors = p_i45669_1_;
/* 443 */       this.height = 1;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void incrementHeight() {
/* 448 */       this.height++;
/*     */     }
/*     */ 
/*     */     
/*     */     public float[] getColors() {
/* 453 */       return this.colors;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getHeight() {
/* 458 */       return this.height;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\tileentity\TileEntityBeacon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */