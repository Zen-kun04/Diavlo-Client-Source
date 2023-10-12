/*     */ package net.minecraft.entity;
/*     */ 
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class EntityAgeable
/*     */   extends EntityCreature {
/*     */   protected int growingAge;
/*     */   protected int field_175502_b;
/*     */   protected int field_175503_c;
/*  16 */   private float ageWidth = -1.0F;
/*     */   
/*     */   private float ageHeight;
/*     */   
/*     */   public EntityAgeable(World worldIn) {
/*  21 */     super(worldIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract EntityAgeable createChild(EntityAgeable paramEntityAgeable);
/*     */   
/*     */   public boolean interact(EntityPlayer player) {
/*  28 */     ItemStack itemstack = player.inventory.getCurrentItem();
/*     */     
/*  30 */     if (itemstack != null && itemstack.getItem() == Items.spawn_egg) {
/*     */       
/*  32 */       if (!this.worldObj.isRemote) {
/*     */         
/*  34 */         Class<? extends Entity> oclass = EntityList.getClassFromID(itemstack.getMetadata());
/*     */         
/*  36 */         if (oclass != null && getClass() == oclass) {
/*     */           
/*  38 */           EntityAgeable entityageable = createChild(this);
/*     */           
/*  40 */           if (entityageable != null) {
/*     */             
/*  42 */             entityageable.setGrowingAge(-24000);
/*  43 */             entityageable.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
/*  44 */             this.worldObj.spawnEntityInWorld(entityageable);
/*     */             
/*  46 */             if (itemstack.hasDisplayName())
/*     */             {
/*  48 */               entityageable.setCustomNameTag(itemstack.getDisplayName());
/*     */             }
/*     */             
/*  51 */             if (!player.capabilities.isCreativeMode) {
/*     */               
/*  53 */               itemstack.stackSize--;
/*     */               
/*  55 */               if (itemstack.stackSize <= 0)
/*     */               {
/*  57 */                 player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/*  64 */       return true;
/*     */     } 
/*     */ 
/*     */     
/*  68 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  74 */     super.entityInit();
/*  75 */     this.dataWatcher.addObject(12, Byte.valueOf((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getGrowingAge() {
/*  80 */     return this.worldObj.isRemote ? this.dataWatcher.getWatchableObjectByte(12) : this.growingAge;
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_175501_a(int p_175501_1_, boolean p_175501_2_) {
/*  85 */     int i = getGrowingAge();
/*  86 */     int j = i;
/*  87 */     i += p_175501_1_ * 20;
/*     */     
/*  89 */     if (i > 0) {
/*     */       
/*  91 */       i = 0;
/*     */       
/*  93 */       if (j < 0)
/*     */       {
/*  95 */         onGrowingAdult();
/*     */       }
/*     */     } 
/*     */     
/*  99 */     int k = i - j;
/* 100 */     setGrowingAge(i);
/*     */     
/* 102 */     if (p_175501_2_) {
/*     */       
/* 104 */       this.field_175502_b += k;
/*     */       
/* 106 */       if (this.field_175503_c == 0)
/*     */       {
/* 108 */         this.field_175503_c = 40;
/*     */       }
/*     */     } 
/*     */     
/* 112 */     if (getGrowingAge() == 0)
/*     */     {
/* 114 */       setGrowingAge(this.field_175502_b);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addGrowth(int growth) {
/* 120 */     func_175501_a(growth, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setGrowingAge(int age) {
/* 125 */     this.dataWatcher.updateObject(12, Byte.valueOf((byte)MathHelper.clamp_int(age, -1, 1)));
/* 126 */     this.growingAge = age;
/* 127 */     setScaleForAge(isChild());
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/* 132 */     super.writeEntityToNBT(tagCompound);
/* 133 */     tagCompound.setInteger("Age", getGrowingAge());
/* 134 */     tagCompound.setInteger("ForcedAge", this.field_175502_b);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/* 139 */     super.readEntityFromNBT(tagCompund);
/* 140 */     setGrowingAge(tagCompund.getInteger("Age"));
/* 141 */     this.field_175502_b = tagCompund.getInteger("ForcedAge");
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLivingUpdate() {
/* 146 */     super.onLivingUpdate();
/*     */     
/* 148 */     if (this.worldObj.isRemote) {
/*     */       
/* 150 */       if (this.field_175503_c > 0) {
/*     */         
/* 152 */         if (this.field_175503_c % 4 == 0)
/*     */         {
/* 154 */           this.worldObj.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (this.rand.nextFloat() * this.width * 2.0F) - this.width, this.posY + 0.5D + (this.rand.nextFloat() * this.height), this.posZ + (this.rand.nextFloat() * this.width * 2.0F) - this.width, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */         }
/*     */         
/* 157 */         this.field_175503_c--;
/*     */       } 
/*     */       
/* 160 */       setScaleForAge(isChild());
/*     */     }
/*     */     else {
/*     */       
/* 164 */       int i = getGrowingAge();
/*     */       
/* 166 */       if (i < 0) {
/*     */         
/* 168 */         i++;
/* 169 */         setGrowingAge(i);
/*     */         
/* 171 */         if (i == 0)
/*     */         {
/* 173 */           onGrowingAdult();
/*     */         }
/*     */       }
/* 176 */       else if (i > 0) {
/*     */         
/* 178 */         i--;
/* 179 */         setGrowingAge(i);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onGrowingAdult() {}
/*     */ 
/*     */   
/*     */   public boolean isChild() {
/* 190 */     return (getGrowingAge() < 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setScaleForAge(boolean p_98054_1_) {
/* 195 */     setScale(p_98054_1_ ? 0.5F : 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void setSize(float width, float height) {
/* 200 */     boolean flag = (this.ageWidth > 0.0F);
/* 201 */     this.ageWidth = width;
/* 202 */     this.ageHeight = height;
/*     */     
/* 204 */     if (!flag)
/*     */     {
/* 206 */       setScale(1.0F);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void setScale(float scale) {
/* 212 */     super.setSize(this.ageWidth * scale, this.ageHeight * scale);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\EntityAgeable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */