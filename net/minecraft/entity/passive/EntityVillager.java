/*      */ package net.minecraft.entity.passive;
/*      */ 
/*      */ import java.util.Random;
/*      */ import net.minecraft.enchantment.Enchantment;
/*      */ import net.minecraft.enchantment.EnchantmentData;
/*      */ import net.minecraft.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityAgeable;
/*      */ import net.minecraft.entity.EntityCreature;
/*      */ import net.minecraft.entity.EntityLiving;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.IEntityLivingData;
/*      */ import net.minecraft.entity.IMerchant;
/*      */ import net.minecraft.entity.INpc;
/*      */ import net.minecraft.entity.SharedMonsterAttributes;
/*      */ import net.minecraft.entity.ai.EntityAIAvoidEntity;
/*      */ import net.minecraft.entity.ai.EntityAIBase;
/*      */ import net.minecraft.entity.ai.EntityAIFollowGolem;
/*      */ import net.minecraft.entity.ai.EntityAIHarvestFarmland;
/*      */ import net.minecraft.entity.ai.EntityAILookAtTradePlayer;
/*      */ import net.minecraft.entity.ai.EntityAIMoveIndoors;
/*      */ import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
/*      */ import net.minecraft.entity.ai.EntityAIOpenDoor;
/*      */ import net.minecraft.entity.ai.EntityAIPlay;
/*      */ import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
/*      */ import net.minecraft.entity.ai.EntityAISwimming;
/*      */ import net.minecraft.entity.ai.EntityAITradePlayer;
/*      */ import net.minecraft.entity.ai.EntityAIVillagerInteract;
/*      */ import net.minecraft.entity.ai.EntityAIVillagerMate;
/*      */ import net.minecraft.entity.ai.EntityAIWander;
/*      */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*      */ import net.minecraft.entity.ai.EntityAIWatchClosest2;
/*      */ import net.minecraft.entity.effect.EntityLightningBolt;
/*      */ import net.minecraft.entity.item.EntityItem;
/*      */ import net.minecraft.entity.item.EntityXPOrb;
/*      */ import net.minecraft.entity.monster.EntityWitch;
/*      */ import net.minecraft.entity.monster.EntityZombie;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.inventory.InventoryBasic;
/*      */ import net.minecraft.item.EnumDyeColor;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.nbt.NBTBase;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.nbt.NBTTagList;
/*      */ import net.minecraft.pathfinding.PathNavigateGround;
/*      */ import net.minecraft.potion.Potion;
/*      */ import net.minecraft.potion.PotionEffect;
/*      */ import net.minecraft.stats.StatList;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.ChatComponentText;
/*      */ import net.minecraft.util.ChatComponentTranslation;
/*      */ import net.minecraft.util.DamageSource;
/*      */ import net.minecraft.util.EnumParticleTypes;
/*      */ import net.minecraft.util.IChatComponent;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.Tuple;
/*      */ import net.minecraft.village.MerchantRecipe;
/*      */ import net.minecraft.village.MerchantRecipeList;
/*      */ import net.minecraft.village.Village;
/*      */ import net.minecraft.world.DifficultyInstance;
/*      */ import net.minecraft.world.World;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class EntityVillager
/*      */   extends EntityAgeable
/*      */   implements IMerchant, INpc
/*      */ {
/*      */   private int randomTickDivider;
/*      */   private boolean isMating;
/*      */   private boolean isPlaying;
/*      */   Village villageObj;
/*      */   private EntityPlayer buyingPlayer;
/*      */   private MerchantRecipeList buyingList;
/*      */   private int timeUntilReset;
/*      */   private boolean needsInitilization;
/*   82 */   private static final ITradeList[][][][] DEFAULT_TRADE_LIST_MAP = new ITradeList[][][][] { { { { new EmeraldForItems(Items.wheat, new PriceInfo(18, 22)), new EmeraldForItems(Items.potato, new PriceInfo(15, 19)), new EmeraldForItems(Items.carrot, new PriceInfo(15, 19)), new ListItemForEmeralds(Items.bread, new PriceInfo(-4, -2)) }, { new EmeraldForItems(Item.getItemFromBlock(Blocks.pumpkin), new PriceInfo(8, 13)), new ListItemForEmeralds(Items.pumpkin_pie, new PriceInfo(-3, -2)) }, { new EmeraldForItems(Item.getItemFromBlock(Blocks.melon_block), new PriceInfo(7, 12)), new ListItemForEmeralds(Items.apple, new PriceInfo(-5, -7)) }, { new ListItemForEmeralds(Items.cookie, new PriceInfo(-6, -10)), new ListItemForEmeralds(Items.cake, new PriceInfo(1, 1)) } }, { { new EmeraldForItems(Items.string, new PriceInfo(15, 20)), new EmeraldForItems(Items.coal, new PriceInfo(16, 24)), new ItemAndEmeraldToItem(Items.fish, new PriceInfo(6, 6), Items.cooked_fish, new PriceInfo(6, 6)) }, { new ListEnchantedItemForEmeralds((Item)Items.fishing_rod, new PriceInfo(7, 8)) } }, { { new EmeraldForItems(Item.getItemFromBlock(Blocks.wool), new PriceInfo(16, 22)), new ListItemForEmeralds((Item)Items.shears, new PriceInfo(3, 4)) }, { new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 0), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 1), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 2), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 3), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 4), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 5), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 6), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 7), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 8), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 9), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 10), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 11), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 12), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 13), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 14), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 15), new PriceInfo(1, 2)) } }, { { new EmeraldForItems(Items.string, new PriceInfo(15, 20)), new ListItemForEmeralds(Items.arrow, new PriceInfo(-12, -8)) }, { new ListItemForEmeralds((Item)Items.bow, new PriceInfo(2, 3)), new ItemAndEmeraldToItem(Item.getItemFromBlock(Blocks.gravel), new PriceInfo(10, 10), Items.flint, new PriceInfo(6, 10)) } } }, { { { new EmeraldForItems(Items.paper, new PriceInfo(24, 36)), new ListEnchantedBookForEmeralds() }, { new EmeraldForItems(Items.book, new PriceInfo(8, 10)), new ListItemForEmeralds(Items.compass, new PriceInfo(10, 12)), new ListItemForEmeralds(Item.getItemFromBlock(Blocks.bookshelf), new PriceInfo(3, 4)) }, { new EmeraldForItems(Items.written_book, new PriceInfo(2, 2)), new ListItemForEmeralds(Items.clock, new PriceInfo(10, 12)), new ListItemForEmeralds(Item.getItemFromBlock(Blocks.glass), new PriceInfo(-5, -3)) }, { new ListEnchantedBookForEmeralds() }, { new ListEnchantedBookForEmeralds() }, { new ListItemForEmeralds(Items.name_tag, new PriceInfo(20, 22)) } } }, { { { new EmeraldForItems(Items.rotten_flesh, new PriceInfo(36, 40)), new EmeraldForItems(Items.gold_ingot, new PriceInfo(8, 10)) }, { new ListItemForEmeralds(Items.redstone, new PriceInfo(-4, -1)), new ListItemForEmeralds(new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeDamage()), new PriceInfo(-2, -1)) }, { new ListItemForEmeralds(Items.ender_eye, new PriceInfo(7, 11)), new ListItemForEmeralds(Item.getItemFromBlock(Blocks.glowstone), new PriceInfo(-3, -1)) }, { new ListItemForEmeralds(Items.experience_bottle, new PriceInfo(3, 11)) } } }, { { { new EmeraldForItems(Items.coal, new PriceInfo(16, 24)), new ListItemForEmeralds((Item)Items.iron_helmet, new PriceInfo(4, 6)) }, { new EmeraldForItems(Items.iron_ingot, new PriceInfo(7, 9)), new ListItemForEmeralds((Item)Items.iron_chestplate, new PriceInfo(10, 14)) }, { new EmeraldForItems(Items.diamond, new PriceInfo(3, 4)), new ListEnchantedItemForEmeralds((Item)Items.diamond_chestplate, new PriceInfo(16, 19)) }, { new ListItemForEmeralds((Item)Items.chainmail_boots, new PriceInfo(5, 7)), new ListItemForEmeralds((Item)Items.chainmail_leggings, new PriceInfo(9, 11)), new ListItemForEmeralds((Item)Items.chainmail_helmet, new PriceInfo(5, 7)), new ListItemForEmeralds((Item)Items.chainmail_chestplate, new PriceInfo(11, 15)) } }, { { new EmeraldForItems(Items.coal, new PriceInfo(16, 24)), new ListItemForEmeralds(Items.iron_axe, new PriceInfo(6, 8)) }, { new EmeraldForItems(Items.iron_ingot, new PriceInfo(7, 9)), new ListEnchantedItemForEmeralds(Items.iron_sword, new PriceInfo(9, 10)) }, { new EmeraldForItems(Items.diamond, new PriceInfo(3, 4)), new ListEnchantedItemForEmeralds(Items.diamond_sword, new PriceInfo(12, 15)), new ListEnchantedItemForEmeralds(Items.diamond_axe, new PriceInfo(9, 12)) } }, { { new EmeraldForItems(Items.coal, new PriceInfo(16, 24)), new ListEnchantedItemForEmeralds(Items.iron_shovel, new PriceInfo(5, 7)) }, { new EmeraldForItems(Items.iron_ingot, new PriceInfo(7, 9)), new ListEnchantedItemForEmeralds(Items.iron_pickaxe, new PriceInfo(9, 11)) }, { new EmeraldForItems(Items.diamond, new PriceInfo(3, 4)), new ListEnchantedItemForEmeralds(Items.diamond_pickaxe, new PriceInfo(12, 15)) } } }, { { { new EmeraldForItems(Items.porkchop, new PriceInfo(14, 18)), new EmeraldForItems(Items.chicken, new PriceInfo(14, 18)) }, { new EmeraldForItems(Items.coal, new PriceInfo(16, 24)), new ListItemForEmeralds(Items.cooked_porkchop, new PriceInfo(-7, -5)), new ListItemForEmeralds(Items.cooked_chicken, new PriceInfo(-8, -6)) } }, { { new EmeraldForItems(Items.leather, new PriceInfo(9, 12)), new ListItemForEmeralds((Item)Items.leather_leggings, new PriceInfo(2, 4)) }, { new ListEnchantedItemForEmeralds((Item)Items.leather_chestplate, new PriceInfo(7, 12)) }, { new ListItemForEmeralds(Items.saddle, new PriceInfo(8, 10)) } } } }; private boolean isWillingToMate; private int wealth; private String lastBuyingPlayer; private int careerId; private int careerLevel; private boolean isLookingForHome; private boolean areAdditionalTasksSet;
/*      */   private InventoryBasic villagerInventory;
/*      */   
/*      */   public EntityVillager(World worldIn) {
/*   86 */     this(worldIn, 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityVillager(World worldIn, int professionId) {
/*   91 */     super(worldIn);
/*   92 */     this.villagerInventory = new InventoryBasic("Items", false, 8);
/*   93 */     setProfession(professionId);
/*   94 */     setSize(0.6F, 1.8F);
/*   95 */     ((PathNavigateGround)getNavigator()).setBreakDoors(true);
/*   96 */     ((PathNavigateGround)getNavigator()).setAvoidsWater(true);
/*   97 */     this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
/*   98 */     this.tasks.addTask(1, (EntityAIBase)new EntityAIAvoidEntity((EntityCreature)this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
/*   99 */     this.tasks.addTask(1, (EntityAIBase)new EntityAITradePlayer(this));
/*  100 */     this.tasks.addTask(1, (EntityAIBase)new EntityAILookAtTradePlayer(this));
/*  101 */     this.tasks.addTask(2, (EntityAIBase)new EntityAIMoveIndoors((EntityCreature)this));
/*  102 */     this.tasks.addTask(3, (EntityAIBase)new EntityAIRestrictOpenDoor((EntityCreature)this));
/*  103 */     this.tasks.addTask(4, (EntityAIBase)new EntityAIOpenDoor((EntityLiving)this, true));
/*  104 */     this.tasks.addTask(5, (EntityAIBase)new EntityAIMoveTowardsRestriction((EntityCreature)this, 0.6D));
/*  105 */     this.tasks.addTask(6, (EntityAIBase)new EntityAIVillagerMate(this));
/*  106 */     this.tasks.addTask(7, (EntityAIBase)new EntityAIFollowGolem(this));
/*  107 */     this.tasks.addTask(9, (EntityAIBase)new EntityAIWatchClosest2((EntityLiving)this, EntityPlayer.class, 3.0F, 1.0F));
/*  108 */     this.tasks.addTask(9, (EntityAIBase)new EntityAIVillagerInteract(this));
/*  109 */     this.tasks.addTask(9, (EntityAIBase)new EntityAIWander((EntityCreature)this, 0.6D));
/*  110 */     this.tasks.addTask(10, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityLiving.class, 8.0F));
/*  111 */     setCanPickUpLoot(true);
/*      */   }
/*      */ 
/*      */   
/*      */   private void setAdditionalAItasks() {
/*  116 */     if (!this.areAdditionalTasksSet) {
/*      */       
/*  118 */       this.areAdditionalTasksSet = true;
/*      */       
/*  120 */       if (isChild()) {
/*      */         
/*  122 */         this.tasks.addTask(8, (EntityAIBase)new EntityAIPlay(this, 0.32D));
/*      */       }
/*  124 */       else if (getProfession() == 0) {
/*      */         
/*  126 */         this.tasks.addTask(6, (EntityAIBase)new EntityAIHarvestFarmland(this, 0.6D));
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onGrowingAdult() {
/*  133 */     if (getProfession() == 0)
/*      */     {
/*  135 */       this.tasks.addTask(8, (EntityAIBase)new EntityAIHarvestFarmland(this, 0.6D));
/*      */     }
/*      */     
/*  138 */     super.onGrowingAdult();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void applyEntityAttributes() {
/*  143 */     super.applyEntityAttributes();
/*  144 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void updateAITasks() {
/*  149 */     if (--this.randomTickDivider <= 0) {
/*      */       
/*  151 */       BlockPos blockpos = new BlockPos((Entity)this);
/*  152 */       this.worldObj.getVillageCollection().addToVillagerPositionList(blockpos);
/*  153 */       this.randomTickDivider = 70 + this.rand.nextInt(50);
/*  154 */       this.villageObj = this.worldObj.getVillageCollection().getNearestVillage(blockpos, 32);
/*      */       
/*  156 */       if (this.villageObj == null) {
/*      */         
/*  158 */         detachHome();
/*      */       }
/*      */       else {
/*      */         
/*  162 */         BlockPos blockpos1 = this.villageObj.getCenter();
/*  163 */         setHomePosAndDistance(blockpos1, (int)(this.villageObj.getVillageRadius() * 1.0F));
/*      */         
/*  165 */         if (this.isLookingForHome) {
/*      */           
/*  167 */           this.isLookingForHome = false;
/*  168 */           this.villageObj.setDefaultPlayerReputation(5);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  173 */     if (!isTrading() && this.timeUntilReset > 0) {
/*      */       
/*  175 */       this.timeUntilReset--;
/*      */       
/*  177 */       if (this.timeUntilReset <= 0) {
/*      */         
/*  179 */         if (this.needsInitilization) {
/*      */           
/*  181 */           for (MerchantRecipe merchantrecipe : this.buyingList) {
/*      */             
/*  183 */             if (merchantrecipe.isRecipeDisabled())
/*      */             {
/*  185 */               merchantrecipe.increaseMaxTradeUses(this.rand.nextInt(6) + this.rand.nextInt(6) + 2);
/*      */             }
/*      */           } 
/*      */           
/*  189 */           populateBuyingList();
/*  190 */           this.needsInitilization = false;
/*      */           
/*  192 */           if (this.villageObj != null && this.lastBuyingPlayer != null) {
/*      */             
/*  194 */             this.worldObj.setEntityState((Entity)this, (byte)14);
/*  195 */             this.villageObj.setReputationForPlayer(this.lastBuyingPlayer, 1);
/*      */           } 
/*      */         } 
/*      */         
/*  199 */         addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, 0));
/*      */       } 
/*      */     } 
/*      */     
/*  203 */     super.updateAITasks();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean interact(EntityPlayer player) {
/*  208 */     ItemStack itemstack = player.inventory.getCurrentItem();
/*  209 */     boolean flag = (itemstack != null && itemstack.getItem() == Items.spawn_egg);
/*      */     
/*  211 */     if (!flag && isEntityAlive() && !isTrading() && !isChild()) {
/*      */       
/*  213 */       if (!this.worldObj.isRemote && (this.buyingList == null || this.buyingList.size() > 0)) {
/*      */         
/*  215 */         setCustomer(player);
/*  216 */         player.displayVillagerTradeGui(this);
/*      */       } 
/*      */       
/*  219 */       player.triggerAchievement(StatList.timesTalkedToVillagerStat);
/*  220 */       return true;
/*      */     } 
/*      */ 
/*      */     
/*  224 */     return super.interact(player);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void entityInit() {
/*  230 */     super.entityInit();
/*  231 */     this.dataWatcher.addObject(16, Integer.valueOf(0));
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/*  236 */     super.writeEntityToNBT(tagCompound);
/*  237 */     tagCompound.setInteger("Profession", getProfession());
/*  238 */     tagCompound.setInteger("Riches", this.wealth);
/*  239 */     tagCompound.setInteger("Career", this.careerId);
/*  240 */     tagCompound.setInteger("CareerLevel", this.careerLevel);
/*  241 */     tagCompound.setBoolean("Willing", this.isWillingToMate);
/*      */     
/*  243 */     if (this.buyingList != null)
/*      */     {
/*  245 */       tagCompound.setTag("Offers", (NBTBase)this.buyingList.getRecipiesAsTags());
/*      */     }
/*      */     
/*  248 */     NBTTagList nbttaglist = new NBTTagList();
/*      */     
/*  250 */     for (int i = 0; i < this.villagerInventory.getSizeInventory(); i++) {
/*      */       
/*  252 */       ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
/*      */       
/*  254 */       if (itemstack != null)
/*      */       {
/*  256 */         nbttaglist.appendTag((NBTBase)itemstack.writeToNBT(new NBTTagCompound()));
/*      */       }
/*      */     } 
/*      */     
/*  260 */     tagCompound.setTag("Inventory", (NBTBase)nbttaglist);
/*      */   }
/*      */ 
/*      */   
/*      */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/*  265 */     super.readEntityFromNBT(tagCompund);
/*  266 */     setProfession(tagCompund.getInteger("Profession"));
/*  267 */     this.wealth = tagCompund.getInteger("Riches");
/*  268 */     this.careerId = tagCompund.getInteger("Career");
/*  269 */     this.careerLevel = tagCompund.getInteger("CareerLevel");
/*  270 */     this.isWillingToMate = tagCompund.getBoolean("Willing");
/*      */     
/*  272 */     if (tagCompund.hasKey("Offers", 10)) {
/*      */       
/*  274 */       NBTTagCompound nbttagcompound = tagCompund.getCompoundTag("Offers");
/*  275 */       this.buyingList = new MerchantRecipeList(nbttagcompound);
/*      */     } 
/*      */     
/*  278 */     NBTTagList nbttaglist = tagCompund.getTagList("Inventory", 10);
/*      */     
/*  280 */     for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*      */       
/*  282 */       ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttaglist.getCompoundTagAt(i));
/*      */       
/*  284 */       if (itemstack != null)
/*      */       {
/*  286 */         this.villagerInventory.func_174894_a(itemstack);
/*      */       }
/*      */     } 
/*      */     
/*  290 */     setCanPickUpLoot(true);
/*  291 */     setAdditionalAItasks();
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean canDespawn() {
/*  296 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   protected String getLivingSound() {
/*  301 */     return isTrading() ? "mob.villager.haggle" : "mob.villager.idle";
/*      */   }
/*      */ 
/*      */   
/*      */   protected String getHurtSound() {
/*  306 */     return "mob.villager.hit";
/*      */   }
/*      */ 
/*      */   
/*      */   protected String getDeathSound() {
/*  311 */     return "mob.villager.death";
/*      */   }
/*      */ 
/*      */   
/*      */   public void setProfession(int professionId) {
/*  316 */     this.dataWatcher.updateObject(16, Integer.valueOf(professionId));
/*      */   }
/*      */ 
/*      */   
/*      */   public int getProfession() {
/*  321 */     return Math.max(this.dataWatcher.getWatchableObjectInt(16) % 5, 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isMating() {
/*  326 */     return this.isMating;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setMating(boolean mating) {
/*  331 */     this.isMating = mating;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setPlaying(boolean playing) {
/*  336 */     this.isPlaying = playing;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isPlaying() {
/*  341 */     return this.isPlaying;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRevengeTarget(EntityLivingBase livingBase) {
/*  346 */     super.setRevengeTarget(livingBase);
/*      */     
/*  348 */     if (this.villageObj != null && livingBase != null) {
/*      */       
/*  350 */       this.villageObj.addOrRenewAgressor(livingBase);
/*      */       
/*  352 */       if (livingBase instanceof EntityPlayer) {
/*      */         
/*  354 */         int i = -1;
/*      */         
/*  356 */         if (isChild())
/*      */         {
/*  358 */           i = -3;
/*      */         }
/*      */         
/*  361 */         this.villageObj.setReputationForPlayer(livingBase.getName(), i);
/*      */         
/*  363 */         if (isEntityAlive())
/*      */         {
/*  365 */           this.worldObj.setEntityState((Entity)this, (byte)13);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void onDeath(DamageSource cause) {
/*  373 */     if (this.villageObj != null) {
/*      */       
/*  375 */       Entity entity = cause.getEntity();
/*      */       
/*  377 */       if (entity != null) {
/*      */         
/*  379 */         if (entity instanceof EntityPlayer)
/*      */         {
/*  381 */           this.villageObj.setReputationForPlayer(entity.getName(), -2);
/*      */         }
/*  383 */         else if (entity instanceof net.minecraft.entity.monster.IMob)
/*      */         {
/*  385 */           this.villageObj.endMatingSeason();
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  390 */         EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity((Entity)this, 16.0D);
/*      */         
/*  392 */         if (entityplayer != null)
/*      */         {
/*  394 */           this.villageObj.endMatingSeason();
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  399 */     super.onDeath(cause);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCustomer(EntityPlayer p_70932_1_) {
/*  404 */     this.buyingPlayer = p_70932_1_;
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityPlayer getCustomer() {
/*  409 */     return this.buyingPlayer;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isTrading() {
/*  414 */     return (this.buyingPlayer != null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getIsWillingToMate(boolean updateFirst) {
/*  419 */     if (!this.isWillingToMate && updateFirst && func_175553_cp()) {
/*      */       
/*  421 */       boolean flag = false;
/*      */       
/*  423 */       for (int i = 0; i < this.villagerInventory.getSizeInventory(); i++) {
/*      */         
/*  425 */         ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
/*      */         
/*  427 */         if (itemstack != null)
/*      */         {
/*  429 */           if (itemstack.getItem() == Items.bread && itemstack.stackSize >= 3) {
/*      */             
/*  431 */             flag = true;
/*  432 */             this.villagerInventory.decrStackSize(i, 3);
/*      */           }
/*  434 */           else if ((itemstack.getItem() == Items.potato || itemstack.getItem() == Items.carrot) && itemstack.stackSize >= 12) {
/*      */             
/*  436 */             flag = true;
/*  437 */             this.villagerInventory.decrStackSize(i, 12);
/*      */           } 
/*      */         }
/*      */         
/*  441 */         if (flag) {
/*      */           
/*  443 */           this.worldObj.setEntityState((Entity)this, (byte)18);
/*  444 */           this.isWillingToMate = true;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/*  450 */     return this.isWillingToMate;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setIsWillingToMate(boolean willingToTrade) {
/*  455 */     this.isWillingToMate = willingToTrade;
/*      */   }
/*      */ 
/*      */   
/*      */   public void useRecipe(MerchantRecipe recipe) {
/*  460 */     recipe.incrementToolUses();
/*  461 */     this.livingSoundTime = -getTalkInterval();
/*  462 */     playSound("mob.villager.yes", getSoundVolume(), getSoundPitch());
/*  463 */     int i = 3 + this.rand.nextInt(4);
/*      */     
/*  465 */     if (recipe.getToolUses() == 1 || this.rand.nextInt(5) == 0) {
/*      */       
/*  467 */       this.timeUntilReset = 40;
/*  468 */       this.needsInitilization = true;
/*  469 */       this.isWillingToMate = true;
/*      */       
/*  471 */       if (this.buyingPlayer != null) {
/*      */         
/*  473 */         this.lastBuyingPlayer = this.buyingPlayer.getName();
/*      */       }
/*      */       else {
/*      */         
/*  477 */         this.lastBuyingPlayer = null;
/*      */       } 
/*      */       
/*  480 */       i += 5;
/*      */     } 
/*      */     
/*  483 */     if (recipe.getItemToBuy().getItem() == Items.emerald)
/*      */     {
/*  485 */       this.wealth += (recipe.getItemToBuy()).stackSize;
/*      */     }
/*      */     
/*  488 */     if (recipe.getRewardsExp())
/*      */     {
/*  490 */       this.worldObj.spawnEntityInWorld((Entity)new EntityXPOrb(this.worldObj, this.posX, this.posY + 0.5D, this.posZ, i));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void verifySellingItem(ItemStack stack) {
/*  496 */     if (!this.worldObj.isRemote && this.livingSoundTime > -getTalkInterval() + 20) {
/*      */       
/*  498 */       this.livingSoundTime = -getTalkInterval();
/*      */       
/*  500 */       if (stack != null) {
/*      */         
/*  502 */         playSound("mob.villager.yes", getSoundVolume(), getSoundPitch());
/*      */       }
/*      */       else {
/*      */         
/*  506 */         playSound("mob.villager.no", getSoundVolume(), getSoundPitch());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public MerchantRecipeList getRecipes(EntityPlayer p_70934_1_) {
/*  513 */     if (this.buyingList == null)
/*      */     {
/*  515 */       populateBuyingList();
/*      */     }
/*      */     
/*  518 */     return this.buyingList;
/*      */   }
/*      */ 
/*      */   
/*      */   private void populateBuyingList() {
/*  523 */     ITradeList[][][] aentityvillager$itradelist = DEFAULT_TRADE_LIST_MAP[getProfession()];
/*      */     
/*  525 */     if (this.careerId != 0 && this.careerLevel != 0) {
/*      */       
/*  527 */       this.careerLevel++;
/*      */     }
/*      */     else {
/*      */       
/*  531 */       this.careerId = this.rand.nextInt(aentityvillager$itradelist.length) + 1;
/*  532 */       this.careerLevel = 1;
/*      */     } 
/*      */     
/*  535 */     if (this.buyingList == null)
/*      */     {
/*  537 */       this.buyingList = new MerchantRecipeList();
/*      */     }
/*      */     
/*  540 */     int i = this.careerId - 1;
/*  541 */     int j = this.careerLevel - 1;
/*  542 */     ITradeList[][] aentityvillager$itradelist1 = aentityvillager$itradelist[i];
/*      */     
/*  544 */     if (j >= 0 && j < aentityvillager$itradelist1.length) {
/*      */       
/*  546 */       ITradeList[] aentityvillager$itradelist2 = aentityvillager$itradelist1[j];
/*      */       
/*  548 */       for (ITradeList entityvillager$itradelist : aentityvillager$itradelist2)
/*      */       {
/*  550 */         entityvillager$itradelist.modifyMerchantRecipeList(this.buyingList, this.rand);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRecipes(MerchantRecipeList recipeList) {}
/*      */ 
/*      */   
/*      */   public IChatComponent getDisplayName() {
/*  561 */     String s = getCustomNameTag();
/*      */     
/*  563 */     if (s != null && s.length() > 0) {
/*      */       
/*  565 */       ChatComponentText chatcomponenttext = new ChatComponentText(s);
/*  566 */       chatcomponenttext.getChatStyle().setChatHoverEvent(getHoverEvent());
/*  567 */       chatcomponenttext.getChatStyle().setInsertion(getUniqueID().toString());
/*  568 */       return (IChatComponent)chatcomponenttext;
/*      */     } 
/*      */ 
/*      */     
/*  572 */     if (this.buyingList == null)
/*      */     {
/*  574 */       populateBuyingList();
/*      */     }
/*      */     
/*  577 */     String s1 = null;
/*      */     
/*  579 */     switch (getProfession()) {
/*      */       
/*      */       case 0:
/*  582 */         if (this.careerId == 1) {
/*      */           
/*  584 */           s1 = "farmer"; break;
/*      */         } 
/*  586 */         if (this.careerId == 2) {
/*      */           
/*  588 */           s1 = "fisherman"; break;
/*      */         } 
/*  590 */         if (this.careerId == 3) {
/*      */           
/*  592 */           s1 = "shepherd"; break;
/*      */         } 
/*  594 */         if (this.careerId == 4)
/*      */         {
/*  596 */           s1 = "fletcher";
/*      */         }
/*      */         break;
/*      */ 
/*      */       
/*      */       case 1:
/*  602 */         s1 = "librarian";
/*      */         break;
/*      */       
/*      */       case 2:
/*  606 */         s1 = "cleric";
/*      */         break;
/*      */       
/*      */       case 3:
/*  610 */         if (this.careerId == 1) {
/*      */           
/*  612 */           s1 = "armor"; break;
/*      */         } 
/*  614 */         if (this.careerId == 2) {
/*      */           
/*  616 */           s1 = "weapon"; break;
/*      */         } 
/*  618 */         if (this.careerId == 3)
/*      */         {
/*  620 */           s1 = "tool";
/*      */         }
/*      */         break;
/*      */ 
/*      */       
/*      */       case 4:
/*  626 */         if (this.careerId == 1) {
/*      */           
/*  628 */           s1 = "butcher"; break;
/*      */         } 
/*  630 */         if (this.careerId == 2)
/*      */         {
/*  632 */           s1 = "leather";
/*      */         }
/*      */         break;
/*      */     } 
/*  636 */     if (s1 != null) {
/*      */       
/*  638 */       ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("entity.Villager." + s1, new Object[0]);
/*  639 */       chatcomponenttranslation.getChatStyle().setChatHoverEvent(getHoverEvent());
/*  640 */       chatcomponenttranslation.getChatStyle().setInsertion(getUniqueID().toString());
/*  641 */       return (IChatComponent)chatcomponenttranslation;
/*      */     } 
/*      */ 
/*      */     
/*  645 */     return super.getDisplayName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getEyeHeight() {
/*  652 */     float f = 1.62F;
/*      */     
/*  654 */     if (isChild())
/*      */     {
/*  656 */       f = (float)(f - 0.81D);
/*      */     }
/*      */     
/*  659 */     return f;
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleStatusUpdate(byte id) {
/*  664 */     if (id == 12) {
/*      */       
/*  666 */       spawnParticles(EnumParticleTypes.HEART);
/*      */     }
/*  668 */     else if (id == 13) {
/*      */       
/*  670 */       spawnParticles(EnumParticleTypes.VILLAGER_ANGRY);
/*      */     }
/*  672 */     else if (id == 14) {
/*      */       
/*  674 */       spawnParticles(EnumParticleTypes.VILLAGER_HAPPY);
/*      */     }
/*      */     else {
/*      */       
/*  678 */       super.handleStatusUpdate(id);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void spawnParticles(EnumParticleTypes particleType) {
/*  684 */     for (int i = 0; i < 5; i++) {
/*      */       
/*  686 */       double d0 = this.rand.nextGaussian() * 0.02D;
/*  687 */       double d1 = this.rand.nextGaussian() * 0.02D;
/*  688 */       double d2 = this.rand.nextGaussian() * 0.02D;
/*  689 */       this.worldObj.spawnParticle(particleType, this.posX + (this.rand.nextFloat() * this.width * 2.0F) - this.width, this.posY + 1.0D + (this.rand.nextFloat() * this.height), this.posZ + (this.rand.nextFloat() * this.width * 2.0F) - this.width, d0, d1, d2, new int[0]);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
/*  695 */     livingdata = super.onInitialSpawn(difficulty, livingdata);
/*  696 */     setProfession(this.worldObj.rand.nextInt(5));
/*  697 */     setAdditionalAItasks();
/*  698 */     return livingdata;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setLookingForHome() {
/*  703 */     this.isLookingForHome = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityVillager createChild(EntityAgeable ageable) {
/*  708 */     EntityVillager entityvillager = new EntityVillager(this.worldObj);
/*  709 */     entityvillager.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos((Entity)entityvillager)), (IEntityLivingData)null);
/*  710 */     return entityvillager;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean allowLeashing() {
/*  715 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void onStruckByLightning(EntityLightningBolt lightningBolt) {
/*  720 */     if (!this.worldObj.isRemote && !this.isDead) {
/*      */       
/*  722 */       EntityWitch entitywitch = new EntityWitch(this.worldObj);
/*  723 */       entitywitch.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
/*  724 */       entitywitch.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos((Entity)entitywitch)), (IEntityLivingData)null);
/*  725 */       entitywitch.setNoAI(isAIDisabled());
/*      */       
/*  727 */       if (hasCustomName()) {
/*      */         
/*  729 */         entitywitch.setCustomNameTag(getCustomNameTag());
/*  730 */         entitywitch.setAlwaysRenderNameTag(getAlwaysRenderNameTag());
/*      */       } 
/*      */       
/*  733 */       this.worldObj.spawnEntityInWorld((Entity)entitywitch);
/*  734 */       setDead();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public InventoryBasic getVillagerInventory() {
/*  740 */     return this.villagerInventory;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void updateEquipmentIfNeeded(EntityItem itemEntity) {
/*  745 */     ItemStack itemstack = itemEntity.getEntityItem();
/*  746 */     Item item = itemstack.getItem();
/*      */     
/*  748 */     if (canVillagerPickupItem(item)) {
/*      */       
/*  750 */       ItemStack itemstack1 = this.villagerInventory.func_174894_a(itemstack);
/*      */       
/*  752 */       if (itemstack1 == null) {
/*      */         
/*  754 */         itemEntity.setDead();
/*      */       }
/*      */       else {
/*      */         
/*  758 */         itemstack.stackSize = itemstack1.stackSize;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean canVillagerPickupItem(Item itemIn) {
/*  765 */     return (itemIn == Items.bread || itemIn == Items.potato || itemIn == Items.carrot || itemIn == Items.wheat || itemIn == Items.wheat_seeds);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean func_175553_cp() {
/*  770 */     return hasEnoughItems(1);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canAbondonItems() {
/*  775 */     return hasEnoughItems(2);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean func_175557_cr() {
/*  780 */     boolean flag = (getProfession() == 0);
/*  781 */     return flag ? (!hasEnoughItems(5)) : (!hasEnoughItems(1));
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean hasEnoughItems(int multiplier) {
/*  786 */     boolean flag = (getProfession() == 0);
/*      */     
/*  788 */     for (int i = 0; i < this.villagerInventory.getSizeInventory(); i++) {
/*      */       
/*  790 */       ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
/*      */       
/*  792 */       if (itemstack != null) {
/*      */         
/*  794 */         if ((itemstack.getItem() == Items.bread && itemstack.stackSize >= 3 * multiplier) || (itemstack.getItem() == Items.potato && itemstack.stackSize >= 12 * multiplier) || (itemstack.getItem() == Items.carrot && itemstack.stackSize >= 12 * multiplier))
/*      */         {
/*  796 */           return true;
/*      */         }
/*      */         
/*  799 */         if (flag && itemstack.getItem() == Items.wheat && itemstack.stackSize >= 9 * multiplier)
/*      */         {
/*  801 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  806 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFarmItemInInventory() {
/*  811 */     for (int i = 0; i < this.villagerInventory.getSizeInventory(); i++) {
/*      */       
/*  813 */       ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
/*      */       
/*  815 */       if (itemstack != null && (itemstack.getItem() == Items.wheat_seeds || itemstack.getItem() == Items.potato || itemstack.getItem() == Items.carrot))
/*      */       {
/*  817 */         return true;
/*      */       }
/*      */     } 
/*      */     
/*  821 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
/*  826 */     if (super.replaceItemInInventory(inventorySlot, itemStackIn))
/*      */     {
/*  828 */       return true;
/*      */     }
/*      */ 
/*      */     
/*  832 */     int i = inventorySlot - 300;
/*      */     
/*  834 */     if (i >= 0 && i < this.villagerInventory.getSizeInventory()) {
/*      */       
/*  836 */       this.villagerInventory.setInventorySlotContents(i, itemStackIn);
/*  837 */       return true;
/*      */     } 
/*      */ 
/*      */     
/*  841 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   static class EmeraldForItems
/*      */     implements ITradeList
/*      */   {
/*      */     public Item sellItem;
/*      */     
/*      */     public EntityVillager.PriceInfo price;
/*      */     
/*      */     public EmeraldForItems(Item itemIn, EntityVillager.PriceInfo priceIn) {
/*  853 */       this.sellItem = itemIn;
/*  854 */       this.price = priceIn;
/*      */     }
/*      */ 
/*      */     
/*      */     public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
/*  859 */       int i = 1;
/*      */       
/*  861 */       if (this.price != null)
/*      */       {
/*  863 */         i = this.price.getPrice(random);
/*      */       }
/*      */       
/*  866 */       recipeList.add(new MerchantRecipe(new ItemStack(this.sellItem, i, 0), Items.emerald));
/*      */     }
/*      */   }
/*      */   
/*      */   static interface ITradeList
/*      */   {
/*      */     void modifyMerchantRecipeList(MerchantRecipeList param1MerchantRecipeList, Random param1Random);
/*      */   }
/*      */   
/*      */   static class ItemAndEmeraldToItem
/*      */     implements ITradeList
/*      */   {
/*      */     public ItemStack buyingItemStack;
/*      */     public EntityVillager.PriceInfo buyingPriceInfo;
/*      */     public ItemStack sellingItemstack;
/*      */     public EntityVillager.PriceInfo field_179408_d;
/*      */     
/*      */     public ItemAndEmeraldToItem(Item p_i45813_1_, EntityVillager.PriceInfo p_i45813_2_, Item p_i45813_3_, EntityVillager.PriceInfo p_i45813_4_) {
/*  884 */       this.buyingItemStack = new ItemStack(p_i45813_1_);
/*  885 */       this.buyingPriceInfo = p_i45813_2_;
/*  886 */       this.sellingItemstack = new ItemStack(p_i45813_3_);
/*  887 */       this.field_179408_d = p_i45813_4_;
/*      */     }
/*      */ 
/*      */     
/*      */     public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
/*  892 */       int i = 1;
/*      */       
/*  894 */       if (this.buyingPriceInfo != null)
/*      */       {
/*  896 */         i = this.buyingPriceInfo.getPrice(random);
/*      */       }
/*      */       
/*  899 */       int j = 1;
/*      */       
/*  901 */       if (this.field_179408_d != null)
/*      */       {
/*  903 */         j = this.field_179408_d.getPrice(random);
/*      */       }
/*      */       
/*  906 */       recipeList.add(new MerchantRecipe(new ItemStack(this.buyingItemStack.getItem(), i, this.buyingItemStack.getMetadata()), new ItemStack(Items.emerald), new ItemStack(this.sellingItemstack.getItem(), j, this.sellingItemstack.getMetadata())));
/*      */     }
/*      */   }
/*      */   
/*      */   static class ListEnchantedBookForEmeralds
/*      */     implements ITradeList
/*      */   {
/*      */     public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
/*  914 */       Enchantment enchantment = Enchantment.enchantmentsBookList[random.nextInt(Enchantment.enchantmentsBookList.length)];
/*  915 */       int i = MathHelper.getRandomIntegerInRange(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
/*  916 */       ItemStack itemstack = Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(enchantment, i));
/*  917 */       int j = 2 + random.nextInt(5 + i * 10) + 3 * i;
/*      */       
/*  919 */       if (j > 64)
/*      */       {
/*  921 */         j = 64;
/*      */       }
/*      */       
/*  924 */       recipeList.add(new MerchantRecipe(new ItemStack(Items.book), new ItemStack(Items.emerald, j), itemstack));
/*      */     }
/*      */   }
/*      */   
/*      */   static class ListEnchantedItemForEmeralds
/*      */     implements ITradeList
/*      */   {
/*      */     public ItemStack enchantedItemStack;
/*      */     public EntityVillager.PriceInfo priceInfo;
/*      */     
/*      */     public ListEnchantedItemForEmeralds(Item p_i45814_1_, EntityVillager.PriceInfo p_i45814_2_) {
/*  935 */       this.enchantedItemStack = new ItemStack(p_i45814_1_);
/*  936 */       this.priceInfo = p_i45814_2_;
/*      */     }
/*      */ 
/*      */     
/*      */     public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
/*  941 */       int i = 1;
/*      */       
/*  943 */       if (this.priceInfo != null)
/*      */       {
/*  945 */         i = this.priceInfo.getPrice(random);
/*      */       }
/*      */       
/*  948 */       ItemStack itemstack = new ItemStack(Items.emerald, i, 0);
/*  949 */       ItemStack itemstack1 = new ItemStack(this.enchantedItemStack.getItem(), 1, this.enchantedItemStack.getMetadata());
/*  950 */       itemstack1 = EnchantmentHelper.addRandomEnchantment(random, itemstack1, 5 + random.nextInt(15));
/*  951 */       recipeList.add(new MerchantRecipe(itemstack, itemstack1));
/*      */     }
/*      */   }
/*      */   
/*      */   static class ListItemForEmeralds
/*      */     implements ITradeList
/*      */   {
/*      */     public ItemStack itemToBuy;
/*      */     public EntityVillager.PriceInfo priceInfo;
/*      */     
/*      */     public ListItemForEmeralds(Item par1Item, EntityVillager.PriceInfo priceInfo) {
/*  962 */       this.itemToBuy = new ItemStack(par1Item);
/*  963 */       this.priceInfo = priceInfo;
/*      */     }
/*      */ 
/*      */     
/*      */     public ListItemForEmeralds(ItemStack stack, EntityVillager.PriceInfo priceInfo) {
/*  968 */       this.itemToBuy = stack;
/*  969 */       this.priceInfo = priceInfo;
/*      */     }
/*      */     
/*      */     public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
/*      */       ItemStack itemstack, itemstack1;
/*  974 */       int i = 1;
/*      */       
/*  976 */       if (this.priceInfo != null)
/*      */       {
/*  978 */         i = this.priceInfo.getPrice(random);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  984 */       if (i < 0) {
/*      */         
/*  986 */         itemstack = new ItemStack(Items.emerald, 1, 0);
/*  987 */         itemstack1 = new ItemStack(this.itemToBuy.getItem(), -i, this.itemToBuy.getMetadata());
/*      */       }
/*      */       else {
/*      */         
/*  991 */         itemstack = new ItemStack(Items.emerald, i, 0);
/*  992 */         itemstack1 = new ItemStack(this.itemToBuy.getItem(), 1, this.itemToBuy.getMetadata());
/*      */       } 
/*      */       
/*  995 */       recipeList.add(new MerchantRecipe(itemstack, itemstack1));
/*      */     }
/*      */   }
/*      */   
/*      */   static class PriceInfo
/*      */     extends Tuple<Integer, Integer>
/*      */   {
/*      */     public PriceInfo(int p_i45810_1_, int p_i45810_2_) {
/* 1003 */       super(Integer.valueOf(p_i45810_1_), Integer.valueOf(p_i45810_2_));
/*      */     }
/*      */ 
/*      */     
/*      */     public int getPrice(Random rand) {
/* 1008 */       return (((Integer)getFirst()).intValue() >= ((Integer)getSecond()).intValue()) ? ((Integer)getFirst()).intValue() : (((Integer)getFirst()).intValue() + rand.nextInt(((Integer)getSecond()).intValue() - ((Integer)getFirst()).intValue() + 1));
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\passive\EntityVillager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */