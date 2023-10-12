/*     */ package net.minecraft.entity.passive;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.EntityAgeable;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAIEatGrass;
/*     */ import net.minecraft.entity.ai.EntityAIFollowParent;
/*     */ import net.minecraft.entity.ai.EntityAILookIdle;
/*     */ import net.minecraft.entity.ai.EntityAIMate;
/*     */ import net.minecraft.entity.ai.EntityAIPanic;
/*     */ import net.minecraft.entity.ai.EntityAISwimming;
/*     */ import net.minecraft.entity.ai.EntityAITempt;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.InventoryCrafting;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.crafting.CraftingManager;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntitySheep extends EntityAnimal {
/*  38 */   private final InventoryCrafting inventoryCrafting = new InventoryCrafting(new Container()
/*     */       {
/*     */         public boolean canInteractWith(EntityPlayer playerIn)
/*     */         {
/*  42 */           return false;
/*     */         }
/*     */       },  2, 1);
/*  45 */   private static final Map<EnumDyeColor, float[]> DYE_TO_RGB = Maps.newEnumMap(EnumDyeColor.class);
/*     */   private int sheepTimer;
/*  47 */   private EntityAIEatGrass entityAIEatGrass = new EntityAIEatGrass((EntityLiving)this);
/*     */ 
/*     */   
/*     */   public static float[] getDyeRgb(EnumDyeColor dyeColor) {
/*  51 */     return DYE_TO_RGB.get(dyeColor);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntitySheep(World worldIn) {
/*  56 */     super(worldIn);
/*  57 */     setSize(0.9F, 1.3F);
/*  58 */     ((PathNavigateGround)getNavigator()).setAvoidsWater(true);
/*  59 */     this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
/*  60 */     this.tasks.addTask(1, (EntityAIBase)new EntityAIPanic((EntityCreature)this, 1.25D));
/*  61 */     this.tasks.addTask(2, (EntityAIBase)new EntityAIMate(this, 1.0D));
/*  62 */     this.tasks.addTask(3, (EntityAIBase)new EntityAITempt((EntityCreature)this, 1.1D, Items.wheat, false));
/*  63 */     this.tasks.addTask(4, (EntityAIBase)new EntityAIFollowParent(this, 1.1D));
/*  64 */     this.tasks.addTask(5, (EntityAIBase)this.entityAIEatGrass);
/*  65 */     this.tasks.addTask(6, (EntityAIBase)new EntityAIWander((EntityCreature)this, 1.0D));
/*  66 */     this.tasks.addTask(7, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 6.0F));
/*  67 */     this.tasks.addTask(8, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
/*  68 */     this.inventoryCrafting.setInventorySlotContents(0, new ItemStack(Items.dye, 1, 0));
/*  69 */     this.inventoryCrafting.setInventorySlotContents(1, new ItemStack(Items.dye, 1, 0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateAITasks() {
/*  74 */     this.sheepTimer = this.entityAIEatGrass.getEatingGrassTimer();
/*  75 */     super.updateAITasks();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLivingUpdate() {
/*  80 */     if (this.worldObj.isRemote)
/*     */     {
/*  82 */       this.sheepTimer = Math.max(0, this.sheepTimer - 1);
/*     */     }
/*     */     
/*  85 */     super.onLivingUpdate();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyEntityAttributes() {
/*  90 */     super.applyEntityAttributes();
/*  91 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
/*  92 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  97 */     super.entityInit();
/*  98 */     this.dataWatcher.addObject(16, new Byte((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
/* 103 */     if (!getSheared())
/*     */     {
/* 105 */       entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, getFleeceColor().getMetadata()), 0.0F);
/*     */     }
/*     */     
/* 108 */     int i = this.rand.nextInt(2) + 1 + this.rand.nextInt(1 + lootingModifier);
/*     */     
/* 110 */     for (int j = 0; j < i; j++) {
/*     */       
/* 112 */       if (isBurning()) {
/*     */         
/* 114 */         dropItem(Items.cooked_mutton, 1);
/*     */       }
/*     */       else {
/*     */         
/* 118 */         dropItem(Items.mutton, 1);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Item getDropItem() {
/* 125 */     return Item.getItemFromBlock(Blocks.wool);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleStatusUpdate(byte id) {
/* 130 */     if (id == 10) {
/*     */       
/* 132 */       this.sheepTimer = 40;
/*     */     }
/*     */     else {
/*     */       
/* 136 */       super.handleStatusUpdate(id);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public float getHeadRotationPointY(float p_70894_1_) {
/* 142 */     return (this.sheepTimer <= 0) ? 0.0F : ((this.sheepTimer >= 4 && this.sheepTimer <= 36) ? 1.0F : ((this.sheepTimer < 4) ? ((this.sheepTimer - p_70894_1_) / 4.0F) : (-((this.sheepTimer - 40) - p_70894_1_) / 4.0F)));
/*     */   }
/*     */ 
/*     */   
/*     */   public float getHeadRotationAngleX(float p_70890_1_) {
/* 147 */     if (this.sheepTimer > 4 && this.sheepTimer <= 36) {
/*     */       
/* 149 */       float f = ((this.sheepTimer - 4) - p_70890_1_) / 32.0F;
/* 150 */       return 0.62831855F + 0.2199115F * MathHelper.sin(f * 28.7F);
/*     */     } 
/*     */ 
/*     */     
/* 154 */     return (this.sheepTimer > 0) ? 0.62831855F : (this.rotationPitch / 57.295776F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean interact(EntityPlayer player) {
/* 160 */     ItemStack itemstack = player.inventory.getCurrentItem();
/*     */     
/* 162 */     if (itemstack != null && itemstack.getItem() == Items.shears && !getSheared() && !isChild()) {
/*     */       
/* 164 */       if (!this.worldObj.isRemote) {
/*     */         
/* 166 */         setSheared(true);
/* 167 */         int i = 1 + this.rand.nextInt(3);
/*     */         
/* 169 */         for (int j = 0; j < i; j++) {
/*     */           
/* 171 */           EntityItem entityitem = entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, getFleeceColor().getMetadata()), 1.0F);
/* 172 */           entityitem.motionY += (this.rand.nextFloat() * 0.05F);
/* 173 */           entityitem.motionX += ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
/* 174 */           entityitem.motionZ += ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
/*     */         } 
/*     */       } 
/*     */       
/* 178 */       itemstack.damageItem(1, (EntityLivingBase)player);
/* 179 */       playSound("mob.sheep.shear", 1.0F, 1.0F);
/*     */     } 
/*     */     
/* 182 */     return super.interact(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/* 187 */     super.writeEntityToNBT(tagCompound);
/* 188 */     tagCompound.setBoolean("Sheared", getSheared());
/* 189 */     tagCompound.setByte("Color", (byte)getFleeceColor().getMetadata());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/* 194 */     super.readEntityFromNBT(tagCompund);
/* 195 */     setSheared(tagCompund.getBoolean("Sheared"));
/* 196 */     setFleeceColor(EnumDyeColor.byMetadata(tagCompund.getByte("Color")));
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getLivingSound() {
/* 201 */     return "mob.sheep.say";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getHurtSound() {
/* 206 */     return "mob.sheep.say";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getDeathSound() {
/* 211 */     return "mob.sheep.say";
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos pos, Block blockIn) {
/* 216 */     playSound("mob.sheep.step", 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumDyeColor getFleeceColor() {
/* 221 */     return EnumDyeColor.byMetadata(this.dataWatcher.getWatchableObjectByte(16) & 0xF);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFleeceColor(EnumDyeColor color) {
/* 226 */     byte b0 = this.dataWatcher.getWatchableObjectByte(16);
/* 227 */     this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & 0xF0 | color.getMetadata() & 0xF)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getSheared() {
/* 232 */     return ((this.dataWatcher.getWatchableObjectByte(16) & 0x10) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSheared(boolean sheared) {
/* 237 */     byte b0 = this.dataWatcher.getWatchableObjectByte(16);
/*     */     
/* 239 */     if (sheared) {
/*     */       
/* 241 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 0x10)));
/*     */     }
/*     */     else {
/*     */       
/* 245 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & 0xFFFFFFEF)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static EnumDyeColor getRandomSheepColor(Random random) {
/* 251 */     int i = random.nextInt(100);
/* 252 */     return (i < 5) ? EnumDyeColor.BLACK : ((i < 10) ? EnumDyeColor.GRAY : ((i < 15) ? EnumDyeColor.SILVER : ((i < 18) ? EnumDyeColor.BROWN : ((random.nextInt(500) == 0) ? EnumDyeColor.PINK : EnumDyeColor.WHITE))));
/*     */   }
/*     */ 
/*     */   
/*     */   public EntitySheep createChild(EntityAgeable ageable) {
/* 257 */     EntitySheep entitysheep = (EntitySheep)ageable;
/* 258 */     EntitySheep entitysheep1 = new EntitySheep(this.worldObj);
/* 259 */     entitysheep1.setFleeceColor(getDyeColorMixFromParents(this, entitysheep));
/* 260 */     return entitysheep1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void eatGrassBonus() {
/* 265 */     setSheared(false);
/*     */     
/* 267 */     if (isChild())
/*     */     {
/* 269 */       addGrowth(60);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
/* 275 */     livingdata = super.onInitialSpawn(difficulty, livingdata);
/* 276 */     setFleeceColor(getRandomSheepColor(this.worldObj.rand));
/* 277 */     return livingdata;
/*     */   }
/*     */ 
/*     */   
/*     */   private EnumDyeColor getDyeColorMixFromParents(EntityAnimal father, EntityAnimal mother) {
/* 282 */     int k, i = ((EntitySheep)father).getFleeceColor().getDyeDamage();
/* 283 */     int j = ((EntitySheep)mother).getFleeceColor().getDyeDamage();
/* 284 */     this.inventoryCrafting.getStackInSlot(0).setItemDamage(i);
/* 285 */     this.inventoryCrafting.getStackInSlot(1).setItemDamage(j);
/* 286 */     ItemStack itemstack = CraftingManager.getInstance().findMatchingRecipe(this.inventoryCrafting, ((EntitySheep)father).worldObj);
/*     */ 
/*     */     
/* 289 */     if (itemstack != null && itemstack.getItem() == Items.dye) {
/*     */       
/* 291 */       k = itemstack.getMetadata();
/*     */     }
/*     */     else {
/*     */       
/* 295 */       k = this.worldObj.rand.nextBoolean() ? i : j;
/*     */     } 
/*     */     
/* 298 */     return EnumDyeColor.byDyeDamage(k);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getEyeHeight() {
/* 303 */     return 0.95F * this.height;
/*     */   }
/*     */ 
/*     */   
/*     */   static {
/* 308 */     DYE_TO_RGB.put(EnumDyeColor.WHITE, new float[] { 1.0F, 1.0F, 1.0F });
/* 309 */     DYE_TO_RGB.put(EnumDyeColor.ORANGE, new float[] { 0.85F, 0.5F, 0.2F });
/* 310 */     DYE_TO_RGB.put(EnumDyeColor.MAGENTA, new float[] { 0.7F, 0.3F, 0.85F });
/* 311 */     DYE_TO_RGB.put(EnumDyeColor.LIGHT_BLUE, new float[] { 0.4F, 0.6F, 0.85F });
/* 312 */     DYE_TO_RGB.put(EnumDyeColor.YELLOW, new float[] { 0.9F, 0.9F, 0.2F });
/* 313 */     DYE_TO_RGB.put(EnumDyeColor.LIME, new float[] { 0.5F, 0.8F, 0.1F });
/* 314 */     DYE_TO_RGB.put(EnumDyeColor.PINK, new float[] { 0.95F, 0.5F, 0.65F });
/* 315 */     DYE_TO_RGB.put(EnumDyeColor.GRAY, new float[] { 0.3F, 0.3F, 0.3F });
/* 316 */     DYE_TO_RGB.put(EnumDyeColor.SILVER, new float[] { 0.6F, 0.6F, 0.6F });
/* 317 */     DYE_TO_RGB.put(EnumDyeColor.CYAN, new float[] { 0.3F, 0.5F, 0.6F });
/* 318 */     DYE_TO_RGB.put(EnumDyeColor.PURPLE, new float[] { 0.5F, 0.25F, 0.7F });
/* 319 */     DYE_TO_RGB.put(EnumDyeColor.BLUE, new float[] { 0.2F, 0.3F, 0.7F });
/* 320 */     DYE_TO_RGB.put(EnumDyeColor.BROWN, new float[] { 0.4F, 0.3F, 0.2F });
/* 321 */     DYE_TO_RGB.put(EnumDyeColor.GREEN, new float[] { 0.4F, 0.5F, 0.2F });
/* 322 */     DYE_TO_RGB.put(EnumDyeColor.RED, new float[] { 0.6F, 0.2F, 0.2F });
/* 323 */     DYE_TO_RGB.put(EnumDyeColor.BLACK, new float[] { 0.1F, 0.1F, 0.1F });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\passive\EntitySheep.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */