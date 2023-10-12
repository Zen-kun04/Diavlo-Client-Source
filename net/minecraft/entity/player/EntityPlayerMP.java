/*      */ package net.minecraft.entity.player;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Sets;
/*      */ import com.mojang.authlib.GameProfile;
/*      */ import io.netty.buffer.Unpooled;
/*      */ import java.util.Arrays;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.crash.CrashReport;
/*      */ import net.minecraft.crash.CrashReportCategory;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityList;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.IMerchant;
/*      */ import net.minecraft.entity.passive.EntityHorse;
/*      */ import net.minecraft.entity.projectile.EntityArrow;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.inventory.Container;
/*      */ import net.minecraft.inventory.ContainerChest;
/*      */ import net.minecraft.inventory.ContainerHorseInventory;
/*      */ import net.minecraft.inventory.ContainerMerchant;
/*      */ import net.minecraft.inventory.ICrafting;
/*      */ import net.minecraft.inventory.IInventory;
/*      */ import net.minecraft.inventory.InventoryMerchant;
/*      */ import net.minecraft.item.EnumAction;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemMapBase;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.network.NetHandlerPlayServer;
/*      */ import net.minecraft.network.Packet;
/*      */ import net.minecraft.network.PacketBuffer;
/*      */ import net.minecraft.network.play.client.C15PacketClientSettings;
/*      */ import net.minecraft.network.play.server.S02PacketChat;
/*      */ import net.minecraft.network.play.server.S06PacketUpdateHealth;
/*      */ import net.minecraft.network.play.server.S0APacketUseBed;
/*      */ import net.minecraft.network.play.server.S0BPacketAnimation;
/*      */ import net.minecraft.network.play.server.S13PacketDestroyEntities;
/*      */ import net.minecraft.network.play.server.S19PacketEntityStatus;
/*      */ import net.minecraft.network.play.server.S1BPacketEntityAttach;
/*      */ import net.minecraft.network.play.server.S1DPacketEntityEffect;
/*      */ import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
/*      */ import net.minecraft.network.play.server.S1FPacketSetExperience;
/*      */ import net.minecraft.network.play.server.S21PacketChunkData;
/*      */ import net.minecraft.network.play.server.S26PacketMapChunkBulk;
/*      */ import net.minecraft.network.play.server.S29PacketSoundEffect;
/*      */ import net.minecraft.network.play.server.S2BPacketChangeGameState;
/*      */ import net.minecraft.network.play.server.S2DPacketOpenWindow;
/*      */ import net.minecraft.network.play.server.S2EPacketCloseWindow;
/*      */ import net.minecraft.network.play.server.S2FPacketSetSlot;
/*      */ import net.minecraft.network.play.server.S30PacketWindowItems;
/*      */ import net.minecraft.network.play.server.S31PacketWindowProperty;
/*      */ import net.minecraft.network.play.server.S36PacketSignEditorOpen;
/*      */ import net.minecraft.network.play.server.S39PacketPlayerAbilities;
/*      */ import net.minecraft.network.play.server.S3FPacketCustomPayload;
/*      */ import net.minecraft.network.play.server.S42PacketCombatEvent;
/*      */ import net.minecraft.network.play.server.S43PacketCamera;
/*      */ import net.minecraft.network.play.server.S48PacketResourcePackSend;
/*      */ import net.minecraft.potion.PotionEffect;
/*      */ import net.minecraft.scoreboard.IScoreObjectiveCriteria;
/*      */ import net.minecraft.scoreboard.Score;
/*      */ import net.minecraft.scoreboard.ScoreObjective;
/*      */ import net.minecraft.scoreboard.Team;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.server.management.ItemInWorldManager;
/*      */ import net.minecraft.server.management.UserListOpsEntry;
/*      */ import net.minecraft.stats.AchievementList;
/*      */ import net.minecraft.stats.StatBase;
/*      */ import net.minecraft.stats.StatList;
/*      */ import net.minecraft.stats.StatisticsFile;
/*      */ import net.minecraft.tileentity.TileEntity;
/*      */ import net.minecraft.tileentity.TileEntitySign;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.ChatComponentTranslation;
/*      */ import net.minecraft.util.DamageSource;
/*      */ import net.minecraft.util.IChatComponent;
/*      */ import net.minecraft.util.IJsonSerializable;
/*      */ import net.minecraft.util.JsonSerializableSet;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.ReportedException;
/*      */ import net.minecraft.village.MerchantRecipeList;
/*      */ import net.minecraft.world.ChunkCoordIntPair;
/*      */ import net.minecraft.world.IInteractionObject;
/*      */ import net.minecraft.world.ILockableContainer;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.WorldServer;
/*      */ import net.minecraft.world.WorldSettings;
/*      */ import net.minecraft.world.biome.BiomeGenBase;
/*      */ import net.minecraft.world.chunk.Chunk;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ 
/*      */ public class EntityPlayerMP
/*      */   extends EntityPlayer
/*      */   implements ICrafting
/*      */ {
/*  101 */   private static final Logger logger = LogManager.getLogger();
/*  102 */   private String translator = "en_US";
/*      */   public NetHandlerPlayServer playerNetServerHandler;
/*      */   public final MinecraftServer mcServer;
/*      */   public final ItemInWorldManager theItemInWorldManager;
/*      */   public double managedPosX;
/*      */   public double managedPosZ;
/*  108 */   public final List<ChunkCoordIntPair> loadedChunks = Lists.newLinkedList();
/*  109 */   private final List<Integer> destroyedItemsNetCache = Lists.newLinkedList();
/*      */   private final StatisticsFile statsFile;
/*  111 */   private float combinedHealth = Float.MIN_VALUE;
/*  112 */   private float lastHealth = -1.0E8F;
/*  113 */   private int lastFoodLevel = -99999999;
/*      */   private boolean wasHungry = true;
/*  115 */   private int lastExperience = -99999999;
/*  116 */   private int respawnInvulnerabilityTicks = 60;
/*      */   private EntityPlayer.EnumChatVisibility chatVisibility;
/*      */   private boolean chatColours = true;
/*  119 */   private long playerLastActiveTime = System.currentTimeMillis();
/*  120 */   private Entity spectatingEntity = null;
/*      */   
/*      */   private int currentWindowId;
/*      */   public boolean isChangingQuantityOnly;
/*      */   public int ping;
/*      */   public boolean playerConqueredTheEnd;
/*      */   
/*      */   public EntityPlayerMP(MinecraftServer server, WorldServer worldIn, GameProfile profile, ItemInWorldManager interactionManager) {
/*  128 */     super((World)worldIn, profile);
/*  129 */     interactionManager.thisPlayerMP = this;
/*  130 */     this.theItemInWorldManager = interactionManager;
/*  131 */     BlockPos blockpos = worldIn.getSpawnPoint();
/*      */     
/*  133 */     if (!worldIn.provider.getHasNoSky() && worldIn.getWorldInfo().getGameType() != WorldSettings.GameType.ADVENTURE) {
/*      */       
/*  135 */       int i = Math.max(5, server.getSpawnProtectionSize() - 6);
/*  136 */       int j = MathHelper.floor_double(worldIn.getWorldBorder().getClosestDistance(blockpos.getX(), blockpos.getZ()));
/*      */       
/*  138 */       if (j < i)
/*      */       {
/*  140 */         i = j;
/*      */       }
/*      */       
/*  143 */       if (j <= 1)
/*      */       {
/*  145 */         i = 1;
/*      */       }
/*      */       
/*  148 */       blockpos = worldIn.getTopSolidOrLiquidBlock(blockpos.add(this.rand.nextInt(i * 2) - i, 0, this.rand.nextInt(i * 2) - i));
/*      */     } 
/*      */     
/*  151 */     this.mcServer = server;
/*  152 */     this.statsFile = server.getConfigurationManager().getPlayerStatsFile(this);
/*  153 */     this.stepHeight = 0.0F;
/*  154 */     moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
/*      */     
/*  156 */     while (!worldIn.getCollidingBoundingBoxes((Entity)this, getEntityBoundingBox()).isEmpty() && this.posY < 255.0D)
/*      */     {
/*  158 */       setPosition(this.posX, this.posY + 1.0D, this.posZ);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/*  164 */     super.readEntityFromNBT(tagCompund);
/*      */     
/*  166 */     if (tagCompund.hasKey("playerGameType", 99))
/*      */     {
/*  168 */       if (MinecraftServer.getServer().getForceGamemode()) {
/*      */         
/*  170 */         this.theItemInWorldManager.setGameType(MinecraftServer.getServer().getGameType());
/*      */       }
/*      */       else {
/*      */         
/*  174 */         this.theItemInWorldManager.setGameType(WorldSettings.GameType.getByID(tagCompund.getInteger("playerGameType")));
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/*  181 */     super.writeEntityToNBT(tagCompound);
/*  182 */     tagCompound.setInteger("playerGameType", this.theItemInWorldManager.getGameType().getID());
/*      */   }
/*      */ 
/*      */   
/*      */   public void addExperienceLevel(int levels) {
/*  187 */     super.addExperienceLevel(levels);
/*  188 */     this.lastExperience = -1;
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeExperienceLevel(int levels) {
/*  193 */     super.removeExperienceLevel(levels);
/*  194 */     this.lastExperience = -1;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addSelfToInternalCraftingInventory() {
/*  199 */     this.openContainer.onCraftGuiOpened(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendEnterCombat() {
/*  204 */     super.sendEnterCombat();
/*  205 */     this.playerNetServerHandler.sendPacket((Packet)new S42PacketCombatEvent(getCombatTracker(), S42PacketCombatEvent.Event.ENTER_COMBAT));
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendEndCombat() {
/*  210 */     super.sendEndCombat();
/*  211 */     this.playerNetServerHandler.sendPacket((Packet)new S42PacketCombatEvent(getCombatTracker(), S42PacketCombatEvent.Event.END_COMBAT));
/*      */   }
/*      */ 
/*      */   
/*      */   public void onUpdate() {
/*  216 */     this.theItemInWorldManager.updateBlockRemoving();
/*  217 */     this.respawnInvulnerabilityTicks--;
/*      */     
/*  219 */     if (this.hurtResistantTime > 0)
/*      */     {
/*  221 */       this.hurtResistantTime--;
/*      */     }
/*      */     
/*  224 */     this.openContainer.detectAndSendChanges();
/*      */     
/*  226 */     if (!this.worldObj.isRemote && !this.openContainer.canInteractWith(this)) {
/*      */       
/*  228 */       closeScreen();
/*  229 */       this.openContainer = this.inventoryContainer;
/*      */     } 
/*      */     
/*  232 */     while (!this.destroyedItemsNetCache.isEmpty()) {
/*      */       
/*  234 */       int i = Math.min(this.destroyedItemsNetCache.size(), 2147483647);
/*  235 */       int[] aint = new int[i];
/*  236 */       Iterator<Integer> iterator = this.destroyedItemsNetCache.iterator();
/*  237 */       int j = 0;
/*      */       
/*  239 */       while (iterator.hasNext() && j < i) {
/*      */         
/*  241 */         aint[j++] = ((Integer)iterator.next()).intValue();
/*  242 */         iterator.remove();
/*      */       } 
/*      */       
/*  245 */       this.playerNetServerHandler.sendPacket((Packet)new S13PacketDestroyEntities(aint));
/*      */     } 
/*      */     
/*  248 */     if (!this.loadedChunks.isEmpty()) {
/*      */       
/*  250 */       List<Chunk> list = Lists.newArrayList();
/*  251 */       Iterator<ChunkCoordIntPair> iterator1 = this.loadedChunks.iterator();
/*  252 */       List<TileEntity> list1 = Lists.newArrayList();
/*      */       
/*  254 */       while (iterator1.hasNext() && list.size() < 10) {
/*      */         
/*  256 */         ChunkCoordIntPair chunkcoordintpair = iterator1.next();
/*      */         
/*  258 */         if (chunkcoordintpair != null) {
/*      */           
/*  260 */           if (this.worldObj.isBlockLoaded(new BlockPos(chunkcoordintpair.chunkXPos << 4, 0, chunkcoordintpair.chunkZPos << 4))) {
/*      */             
/*  262 */             Chunk chunk = this.worldObj.getChunkFromChunkCoords(chunkcoordintpair.chunkXPos, chunkcoordintpair.chunkZPos);
/*      */             
/*  264 */             if (chunk.isPopulated()) {
/*      */               
/*  266 */               list.add(chunk);
/*  267 */               list1.addAll(((WorldServer)this.worldObj).getTileEntitiesIn(chunkcoordintpair.chunkXPos * 16, 0, chunkcoordintpair.chunkZPos * 16, chunkcoordintpair.chunkXPos * 16 + 16, 256, chunkcoordintpair.chunkZPos * 16 + 16));
/*  268 */               iterator1.remove();
/*      */             } 
/*      */           } 
/*      */           
/*      */           continue;
/*      */         } 
/*  274 */         iterator1.remove();
/*      */       } 
/*      */ 
/*      */       
/*  278 */       if (!list.isEmpty()) {
/*      */         
/*  280 */         if (list.size() == 1) {
/*      */           
/*  282 */           this.playerNetServerHandler.sendPacket((Packet)new S21PacketChunkData(list.get(0), true, 65535));
/*      */         }
/*      */         else {
/*      */           
/*  286 */           this.playerNetServerHandler.sendPacket((Packet)new S26PacketMapChunkBulk(list));
/*      */         } 
/*      */         
/*  289 */         for (TileEntity tileentity : list1)
/*      */         {
/*  291 */           sendTileEntityUpdate(tileentity);
/*      */         }
/*      */         
/*  294 */         for (Chunk chunk1 : list)
/*      */         {
/*  296 */           getServerForPlayer().getEntityTracker().func_85172_a(this, chunk1);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  301 */     Entity entity = getSpectatingEntity();
/*      */     
/*  303 */     if (entity != this)
/*      */     {
/*  305 */       if (!entity.isEntityAlive()) {
/*      */         
/*  307 */         setSpectatingEntity((Entity)this);
/*      */       }
/*      */       else {
/*      */         
/*  311 */         setPositionAndRotation(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
/*  312 */         this.mcServer.getConfigurationManager().serverUpdateMountedMovingPlayer(this);
/*      */         
/*  314 */         if (isSneaking())
/*      */         {
/*  316 */           setSpectatingEntity((Entity)this);
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onUpdateEntity() {
/*      */     try {
/*  326 */       super.onUpdate();
/*      */       
/*  328 */       for (int i = 0; i < this.inventory.getSizeInventory(); i++) {
/*      */         
/*  330 */         ItemStack itemstack = this.inventory.getStackInSlot(i);
/*      */         
/*  332 */         if (itemstack != null && itemstack.getItem().isMap()) {
/*      */           
/*  334 */           Packet packet = ((ItemMapBase)itemstack.getItem()).createMapDataPacket(itemstack, this.worldObj, this);
/*      */           
/*  336 */           if (packet != null)
/*      */           {
/*  338 */             this.playerNetServerHandler.sendPacket(packet);
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/*  343 */       if (getHealth() != this.lastHealth || this.lastFoodLevel != this.foodStats.getFoodLevel() || ((this.foodStats.getSaturationLevel() == 0.0F)) != this.wasHungry) {
/*      */         
/*  345 */         this.playerNetServerHandler.sendPacket((Packet)new S06PacketUpdateHealth(getHealth(), this.foodStats.getFoodLevel(), this.foodStats.getSaturationLevel()));
/*  346 */         this.lastHealth = getHealth();
/*  347 */         this.lastFoodLevel = this.foodStats.getFoodLevel();
/*  348 */         this.wasHungry = (this.foodStats.getSaturationLevel() == 0.0F);
/*      */       } 
/*      */       
/*  351 */       if (getHealth() + getAbsorptionAmount() != this.combinedHealth) {
/*      */         
/*  353 */         this.combinedHealth = getHealth() + getAbsorptionAmount();
/*      */         
/*  355 */         for (ScoreObjective scoreobjective : getWorldScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.health)) {
/*      */           
/*  357 */           getWorldScoreboard().getValueFromObjective(getName(), scoreobjective).func_96651_a(Arrays.asList(new EntityPlayer[] { this }));
/*      */         } 
/*      */       } 
/*      */       
/*  361 */       if (this.experienceTotal != this.lastExperience) {
/*      */         
/*  363 */         this.lastExperience = this.experienceTotal;
/*  364 */         this.playerNetServerHandler.sendPacket((Packet)new S1FPacketSetExperience(this.experience, this.experienceTotal, this.experienceLevel));
/*      */       } 
/*      */       
/*  367 */       if (this.ticksExisted % 20 * 5 == 0 && !getStatFile().hasAchievementUnlocked(AchievementList.exploreAllBiomes))
/*      */       {
/*  369 */         updateBiomesExplored();
/*      */       }
/*      */     }
/*  372 */     catch (Throwable throwable) {
/*      */       
/*  374 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking player");
/*  375 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Player being ticked");
/*  376 */       addEntityCrashInfo(crashreportcategory);
/*  377 */       throw new ReportedException(crashreport);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void updateBiomesExplored() {
/*  383 */     BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(new BlockPos(MathHelper.floor_double(this.posX), 0, MathHelper.floor_double(this.posZ)));
/*  384 */     String s = biomegenbase.biomeName;
/*  385 */     JsonSerializableSet jsonserializableset = (JsonSerializableSet)getStatFile().func_150870_b((StatBase)AchievementList.exploreAllBiomes);
/*      */     
/*  387 */     if (jsonserializableset == null)
/*      */     {
/*  389 */       jsonserializableset = (JsonSerializableSet)getStatFile().func_150872_a((StatBase)AchievementList.exploreAllBiomes, (IJsonSerializable)new JsonSerializableSet());
/*      */     }
/*      */     
/*  392 */     jsonserializableset.add(s);
/*      */     
/*  394 */     if (getStatFile().canUnlockAchievement(AchievementList.exploreAllBiomes) && jsonserializableset.size() >= BiomeGenBase.explorationBiomesList.size()) {
/*      */       
/*  396 */       Set<BiomeGenBase> set = Sets.newHashSet(BiomeGenBase.explorationBiomesList);
/*      */       
/*  398 */       for (String s1 : jsonserializableset) {
/*      */         
/*  400 */         Iterator<BiomeGenBase> iterator = set.iterator();
/*      */         
/*  402 */         while (iterator.hasNext()) {
/*      */           
/*  404 */           BiomeGenBase biomegenbase1 = iterator.next();
/*      */           
/*  406 */           if (biomegenbase1.biomeName.equals(s1))
/*      */           {
/*  408 */             iterator.remove();
/*      */           }
/*      */         } 
/*      */         
/*  412 */         if (set.isEmpty()) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/*  418 */       if (set.isEmpty())
/*      */       {
/*  420 */         triggerAchievement((StatBase)AchievementList.exploreAllBiomes);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void onDeath(DamageSource cause) {
/*  427 */     if (this.worldObj.getGameRules().getBoolean("showDeathMessages")) {
/*      */       
/*  429 */       Team team = getTeam();
/*      */       
/*  431 */       if (team != null && team.getDeathMessageVisibility() != Team.EnumVisible.ALWAYS) {
/*      */         
/*  433 */         if (team.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OTHER_TEAMS)
/*      */         {
/*  435 */           this.mcServer.getConfigurationManager().sendMessageToAllTeamMembers(this, getCombatTracker().getDeathMessage());
/*      */         }
/*  437 */         else if (team.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OWN_TEAM)
/*      */         {
/*  439 */           this.mcServer.getConfigurationManager().sendMessageToTeamOrEvryPlayer(this, getCombatTracker().getDeathMessage());
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  444 */         this.mcServer.getConfigurationManager().sendChatMsg(getCombatTracker().getDeathMessage());
/*      */       } 
/*      */     } 
/*      */     
/*  448 */     if (!this.worldObj.getGameRules().getBoolean("keepInventory"))
/*      */     {
/*  450 */       this.inventory.dropAllItems();
/*      */     }
/*      */     
/*  453 */     for (ScoreObjective scoreobjective : this.worldObj.getScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.deathCount)) {
/*      */       
/*  455 */       Score score = getWorldScoreboard().getValueFromObjective(getName(), scoreobjective);
/*  456 */       score.func_96648_a();
/*      */     } 
/*      */     
/*  459 */     EntityLivingBase entitylivingbase = getAttackingEntity();
/*      */     
/*  461 */     if (entitylivingbase != null) {
/*      */       
/*  463 */       EntityList.EntityEggInfo entitylist$entityegginfo = (EntityList.EntityEggInfo)EntityList.entityEggs.get(Integer.valueOf(EntityList.getEntityID((Entity)entitylivingbase)));
/*      */       
/*  465 */       if (entitylist$entityegginfo != null)
/*      */       {
/*  467 */         triggerAchievement(entitylist$entityegginfo.field_151513_e);
/*      */       }
/*      */       
/*  470 */       entitylivingbase.addToPlayerScore((Entity)this, this.scoreValue);
/*      */     } 
/*      */     
/*  473 */     triggerAchievement(StatList.deathsStat);
/*  474 */     func_175145_a(StatList.timeSinceDeathStat);
/*  475 */     getCombatTracker().reset();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean attackEntityFrom(DamageSource source, float amount) {
/*  480 */     if (isEntityInvulnerable(source))
/*      */     {
/*  482 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  486 */     boolean flag = (this.mcServer.isDedicatedServer() && canPlayersAttack() && "fall".equals(source.damageType));
/*      */     
/*  488 */     if (!flag && this.respawnInvulnerabilityTicks > 0 && source != DamageSource.outOfWorld)
/*      */     {
/*  490 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  494 */     if (source instanceof net.minecraft.util.EntityDamageSource) {
/*      */       
/*  496 */       Entity entity = source.getEntity();
/*      */       
/*  498 */       if (entity instanceof EntityPlayer && !canAttackPlayer((EntityPlayer)entity))
/*      */       {
/*  500 */         return false;
/*      */       }
/*      */       
/*  503 */       if (entity instanceof EntityArrow) {
/*      */         
/*  505 */         EntityArrow entityarrow = (EntityArrow)entity;
/*      */         
/*  507 */         if (entityarrow.shootingEntity instanceof EntityPlayer && !canAttackPlayer((EntityPlayer)entityarrow.shootingEntity))
/*      */         {
/*  509 */           return false;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  514 */     return super.attackEntityFrom(source, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canAttackPlayer(EntityPlayer other) {
/*  521 */     return !canPlayersAttack() ? false : super.canAttackPlayer(other);
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean canPlayersAttack() {
/*  526 */     return this.mcServer.isPVPEnabled();
/*      */   }
/*      */ 
/*      */   
/*      */   public void travelToDimension(int dimensionId) {
/*  531 */     if (this.dimension == 1 && dimensionId == 1) {
/*      */       
/*  533 */       triggerAchievement((StatBase)AchievementList.theEnd2);
/*  534 */       this.worldObj.removeEntity((Entity)this);
/*  535 */       this.playerConqueredTheEnd = true;
/*  536 */       this.playerNetServerHandler.sendPacket((Packet)new S2BPacketChangeGameState(4, 0.0F));
/*      */     }
/*      */     else {
/*      */       
/*  540 */       if (this.dimension == 0 && dimensionId == 1) {
/*      */         
/*  542 */         triggerAchievement((StatBase)AchievementList.theEnd);
/*  543 */         BlockPos blockpos = this.mcServer.worldServerForDimension(dimensionId).getSpawnCoordinate();
/*      */         
/*  545 */         if (blockpos != null)
/*      */         {
/*  547 */           this.playerNetServerHandler.setPlayerLocation(blockpos.getX(), blockpos.getY(), blockpos.getZ(), 0.0F, 0.0F);
/*      */         }
/*      */         
/*  550 */         dimensionId = 1;
/*      */       }
/*      */       else {
/*      */         
/*  554 */         triggerAchievement((StatBase)AchievementList.portal);
/*      */       } 
/*      */       
/*  557 */       this.mcServer.getConfigurationManager().transferPlayerToDimension(this, dimensionId);
/*  558 */       this.lastExperience = -1;
/*  559 */       this.lastHealth = -1.0F;
/*  560 */       this.lastFoodLevel = -1;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSpectatedByPlayer(EntityPlayerMP player) {
/*  566 */     return player.isSpectator() ? ((getSpectatingEntity() == this)) : (isSpectator() ? false : super.isSpectatedByPlayer(player));
/*      */   }
/*      */ 
/*      */   
/*      */   private void sendTileEntityUpdate(TileEntity p_147097_1_) {
/*  571 */     if (p_147097_1_ != null) {
/*      */       
/*  573 */       Packet packet = p_147097_1_.getDescriptionPacket();
/*      */       
/*  575 */       if (packet != null)
/*      */       {
/*  577 */         this.playerNetServerHandler.sendPacket(packet);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void onItemPickup(Entity p_71001_1_, int p_71001_2_) {
/*  584 */     super.onItemPickup(p_71001_1_, p_71001_2_);
/*  585 */     this.openContainer.detectAndSendChanges();
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityPlayer.EnumStatus trySleep(BlockPos bedLocation) {
/*  590 */     EntityPlayer.EnumStatus entityplayer$enumstatus = super.trySleep(bedLocation);
/*      */     
/*  592 */     if (entityplayer$enumstatus == EntityPlayer.EnumStatus.OK) {
/*      */       
/*  594 */       S0APacketUseBed s0APacketUseBed = new S0APacketUseBed(this, bedLocation);
/*  595 */       getServerForPlayer().getEntityTracker().sendToAllTrackingEntity((Entity)this, (Packet)s0APacketUseBed);
/*  596 */       this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
/*  597 */       this.playerNetServerHandler.sendPacket((Packet)s0APacketUseBed);
/*      */     } 
/*      */     
/*  600 */     return entityplayer$enumstatus;
/*      */   }
/*      */ 
/*      */   
/*      */   public void wakeUpPlayer(boolean immediately, boolean updateWorldFlag, boolean setSpawn) {
/*  605 */     if (isPlayerSleeping())
/*      */     {
/*  607 */       getServerForPlayer().getEntityTracker().func_151248_b((Entity)this, (Packet)new S0BPacketAnimation((Entity)this, 2));
/*      */     }
/*      */     
/*  610 */     super.wakeUpPlayer(immediately, updateWorldFlag, setSpawn);
/*      */     
/*  612 */     if (this.playerNetServerHandler != null)
/*      */     {
/*  614 */       this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void mountEntity(Entity entityIn) {
/*  620 */     Entity entity = this.ridingEntity;
/*  621 */     super.mountEntity(entityIn);
/*      */     
/*  623 */     if (entityIn != entity) {
/*      */       
/*  625 */       this.playerNetServerHandler.sendPacket((Packet)new S1BPacketEntityAttach(0, (Entity)this, this.ridingEntity));
/*  626 */       this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void updateFallState(double y, boolean onGroundIn, Block blockIn, BlockPos pos) {}
/*      */ 
/*      */   
/*      */   public void handleFalling(double p_71122_1_, boolean p_71122_3_) {
/*  636 */     int i = MathHelper.floor_double(this.posX);
/*  637 */     int j = MathHelper.floor_double(this.posY - 0.20000000298023224D);
/*  638 */     int k = MathHelper.floor_double(this.posZ);
/*  639 */     BlockPos blockpos = new BlockPos(i, j, k);
/*  640 */     Block block = this.worldObj.getBlockState(blockpos).getBlock();
/*      */     
/*  642 */     if (block.getMaterial() == Material.air) {
/*      */       
/*  644 */       Block block1 = this.worldObj.getBlockState(blockpos.down()).getBlock();
/*      */       
/*  646 */       if (block1 instanceof net.minecraft.block.BlockFence || block1 instanceof net.minecraft.block.BlockWall || block1 instanceof net.minecraft.block.BlockFenceGate) {
/*      */         
/*  648 */         blockpos = blockpos.down();
/*  649 */         block = this.worldObj.getBlockState(blockpos).getBlock();
/*      */       } 
/*      */     } 
/*      */     
/*  653 */     super.updateFallState(p_71122_1_, p_71122_3_, block, blockpos);
/*      */   }
/*      */ 
/*      */   
/*      */   public void openEditSign(TileEntitySign signTile) {
/*  658 */     signTile.setPlayer(this);
/*  659 */     this.playerNetServerHandler.sendPacket((Packet)new S36PacketSignEditorOpen(signTile.getPos()));
/*      */   }
/*      */ 
/*      */   
/*      */   private void getNextWindowId() {
/*  664 */     this.currentWindowId = this.currentWindowId % 100 + 1;
/*      */   }
/*      */ 
/*      */   
/*      */   public void displayGui(IInteractionObject guiOwner) {
/*  669 */     getNextWindowId();
/*  670 */     this.playerNetServerHandler.sendPacket((Packet)new S2DPacketOpenWindow(this.currentWindowId, guiOwner.getGuiID(), guiOwner.getDisplayName()));
/*  671 */     this.openContainer = guiOwner.createContainer(this.inventory, this);
/*  672 */     this.openContainer.windowId = this.currentWindowId;
/*  673 */     this.openContainer.onCraftGuiOpened(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public void displayGUIChest(IInventory chestInventory) {
/*  678 */     if (this.openContainer != this.inventoryContainer)
/*      */     {
/*  680 */       closeScreen();
/*      */     }
/*      */     
/*  683 */     if (chestInventory instanceof ILockableContainer) {
/*      */       
/*  685 */       ILockableContainer ilockablecontainer = (ILockableContainer)chestInventory;
/*      */       
/*  687 */       if (ilockablecontainer.isLocked() && !canOpen(ilockablecontainer.getLockCode()) && !isSpectator()) {
/*      */         
/*  689 */         this.playerNetServerHandler.sendPacket((Packet)new S02PacketChat((IChatComponent)new ChatComponentTranslation("container.isLocked", new Object[] { chestInventory.getDisplayName() }), (byte)2));
/*  690 */         this.playerNetServerHandler.sendPacket((Packet)new S29PacketSoundEffect("random.door_close", this.posX, this.posY, this.posZ, 1.0F, 1.0F));
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/*  695 */     getNextWindowId();
/*      */     
/*  697 */     if (chestInventory instanceof IInteractionObject) {
/*      */       
/*  699 */       this.playerNetServerHandler.sendPacket((Packet)new S2DPacketOpenWindow(this.currentWindowId, ((IInteractionObject)chestInventory).getGuiID(), chestInventory.getDisplayName(), chestInventory.getSizeInventory()));
/*  700 */       this.openContainer = ((IInteractionObject)chestInventory).createContainer(this.inventory, this);
/*      */     }
/*      */     else {
/*      */       
/*  704 */       this.playerNetServerHandler.sendPacket((Packet)new S2DPacketOpenWindow(this.currentWindowId, "minecraft:container", chestInventory.getDisplayName(), chestInventory.getSizeInventory()));
/*  705 */       this.openContainer = (Container)new ContainerChest(this.inventory, chestInventory, this);
/*      */     } 
/*      */     
/*  708 */     this.openContainer.windowId = this.currentWindowId;
/*  709 */     this.openContainer.onCraftGuiOpened(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public void displayVillagerTradeGui(IMerchant villager) {
/*  714 */     getNextWindowId();
/*  715 */     this.openContainer = (Container)new ContainerMerchant(this.inventory, villager, this.worldObj);
/*  716 */     this.openContainer.windowId = this.currentWindowId;
/*  717 */     this.openContainer.onCraftGuiOpened(this);
/*  718 */     InventoryMerchant inventoryMerchant = ((ContainerMerchant)this.openContainer).getMerchantInventory();
/*  719 */     IChatComponent ichatcomponent = villager.getDisplayName();
/*  720 */     this.playerNetServerHandler.sendPacket((Packet)new S2DPacketOpenWindow(this.currentWindowId, "minecraft:villager", ichatcomponent, inventoryMerchant.getSizeInventory()));
/*  721 */     MerchantRecipeList merchantrecipelist = villager.getRecipes(this);
/*      */     
/*  723 */     if (merchantrecipelist != null) {
/*      */       
/*  725 */       PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
/*  726 */       packetbuffer.writeInt(this.currentWindowId);
/*  727 */       merchantrecipelist.writeToBuf(packetbuffer);
/*  728 */       this.playerNetServerHandler.sendPacket((Packet)new S3FPacketCustomPayload("MC|TrList", packetbuffer));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void displayGUIHorse(EntityHorse horse, IInventory horseInventory) {
/*  734 */     if (this.openContainer != this.inventoryContainer)
/*      */     {
/*  736 */       closeScreen();
/*      */     }
/*      */     
/*  739 */     getNextWindowId();
/*  740 */     this.playerNetServerHandler.sendPacket((Packet)new S2DPacketOpenWindow(this.currentWindowId, "EntityHorse", horseInventory.getDisplayName(), horseInventory.getSizeInventory(), horse.getEntityId()));
/*  741 */     this.openContainer = (Container)new ContainerHorseInventory(this.inventory, horseInventory, horse, this);
/*  742 */     this.openContainer.windowId = this.currentWindowId;
/*  743 */     this.openContainer.onCraftGuiOpened(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public void displayGUIBook(ItemStack bookStack) {
/*  748 */     Item item = bookStack.getItem();
/*      */     
/*  750 */     if (item == Items.written_book)
/*      */     {
/*  752 */       this.playerNetServerHandler.sendPacket((Packet)new S3FPacketCustomPayload("MC|BOpen", new PacketBuffer(Unpooled.buffer())));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) {
/*  758 */     if (!(containerToSend.getSlot(slotInd) instanceof net.minecraft.inventory.SlotCrafting))
/*      */     {
/*  760 */       if (!this.isChangingQuantityOnly)
/*      */       {
/*  762 */         this.playerNetServerHandler.sendPacket((Packet)new S2FPacketSetSlot(containerToSend.windowId, slotInd, stack));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendContainerToPlayer(Container p_71120_1_) {
/*  769 */     updateCraftingInventory(p_71120_1_, p_71120_1_.getInventory());
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateCraftingInventory(Container containerToSend, List<ItemStack> itemsList) {
/*  774 */     this.playerNetServerHandler.sendPacket((Packet)new S30PacketWindowItems(containerToSend.windowId, itemsList));
/*  775 */     this.playerNetServerHandler.sendPacket((Packet)new S2FPacketSetSlot(-1, -1, this.inventory.getItemStack()));
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendProgressBarUpdate(Container containerIn, int varToUpdate, int newValue) {
/*  780 */     this.playerNetServerHandler.sendPacket((Packet)new S31PacketWindowProperty(containerIn.windowId, varToUpdate, newValue));
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendAllWindowProperties(Container p_175173_1_, IInventory p_175173_2_) {
/*  785 */     for (int i = 0; i < p_175173_2_.getFieldCount(); i++)
/*      */     {
/*  787 */       this.playerNetServerHandler.sendPacket((Packet)new S31PacketWindowProperty(p_175173_1_.windowId, i, p_175173_2_.getField(i)));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void closeScreen() {
/*  793 */     this.playerNetServerHandler.sendPacket((Packet)new S2EPacketCloseWindow(this.openContainer.windowId));
/*  794 */     closeContainer();
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateHeldItem() {
/*  799 */     if (!this.isChangingQuantityOnly)
/*      */     {
/*  801 */       this.playerNetServerHandler.sendPacket((Packet)new S2FPacketSetSlot(-1, -1, this.inventory.getItemStack()));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void closeContainer() {
/*  807 */     this.openContainer.onContainerClosed(this);
/*  808 */     this.openContainer = this.inventoryContainer;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setEntityActionState(float p_110430_1_, float p_110430_2_, boolean p_110430_3_, boolean sneaking) {
/*  813 */     if (this.ridingEntity != null) {
/*      */       
/*  815 */       if (p_110430_1_ >= -1.0F && p_110430_1_ <= 1.0F)
/*      */       {
/*  817 */         this.moveStrafing = p_110430_1_;
/*      */       }
/*      */       
/*  820 */       if (p_110430_2_ >= -1.0F && p_110430_2_ <= 1.0F)
/*      */       {
/*  822 */         this.moveForward = p_110430_2_;
/*      */       }
/*      */       
/*  825 */       this.isJumping = p_110430_3_;
/*  826 */       setSneaking(sneaking);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void addStat(StatBase stat, int amount) {
/*  832 */     if (stat != null) {
/*      */       
/*  834 */       this.statsFile.increaseStat(this, stat, amount);
/*      */       
/*  836 */       for (ScoreObjective scoreobjective : getWorldScoreboard().getObjectivesFromCriteria(stat.getCriteria()))
/*      */       {
/*  838 */         getWorldScoreboard().getValueFromObjective(getName(), scoreobjective).increseScore(amount);
/*      */       }
/*      */       
/*  841 */       if (this.statsFile.func_150879_e())
/*      */       {
/*  843 */         this.statsFile.func_150876_a(this);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void func_175145_a(StatBase p_175145_1_) {
/*  850 */     if (p_175145_1_ != null) {
/*      */       
/*  852 */       this.statsFile.unlockAchievement(this, p_175145_1_, 0);
/*      */       
/*  854 */       for (ScoreObjective scoreobjective : getWorldScoreboard().getObjectivesFromCriteria(p_175145_1_.getCriteria()))
/*      */       {
/*  856 */         getWorldScoreboard().getValueFromObjective(getName(), scoreobjective).setScorePoints(0);
/*      */       }
/*      */       
/*  859 */       if (this.statsFile.func_150879_e())
/*      */       {
/*  861 */         this.statsFile.func_150876_a(this);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void mountEntityAndWakeUp() {
/*  868 */     if (this.riddenByEntity != null)
/*      */     {
/*  870 */       this.riddenByEntity.mountEntity((Entity)this);
/*      */     }
/*      */     
/*  873 */     if (this.sleeping)
/*      */     {
/*  875 */       wakeUpPlayer(true, false, false);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void setPlayerHealthUpdated() {
/*  881 */     this.lastHealth = -1.0E8F;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addChatComponentMessage(IChatComponent chatComponent) {
/*  886 */     this.playerNetServerHandler.sendPacket((Packet)new S02PacketChat(chatComponent));
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onItemUseFinish() {
/*  891 */     this.playerNetServerHandler.sendPacket((Packet)new S19PacketEntityStatus((Entity)this, (byte)9));
/*  892 */     super.onItemUseFinish();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setItemInUse(ItemStack stack, int duration) {
/*  897 */     super.setItemInUse(stack, duration);
/*      */     
/*  899 */     if (stack != null && stack.getItem() != null && stack.getItem().getItemUseAction(stack) == EnumAction.EAT)
/*      */     {
/*  901 */       getServerForPlayer().getEntityTracker().func_151248_b((Entity)this, (Packet)new S0BPacketAnimation((Entity)this, 3));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void clonePlayer(EntityPlayer oldPlayer, boolean respawnFromEnd) {
/*  907 */     super.clonePlayer(oldPlayer, respawnFromEnd);
/*  908 */     this.lastExperience = -1;
/*  909 */     this.lastHealth = -1.0F;
/*  910 */     this.lastFoodLevel = -1;
/*  911 */     this.destroyedItemsNetCache.addAll(((EntityPlayerMP)oldPlayer).destroyedItemsNetCache);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onNewPotionEffect(PotionEffect id) {
/*  916 */     super.onNewPotionEffect(id);
/*  917 */     this.playerNetServerHandler.sendPacket((Packet)new S1DPacketEntityEffect(getEntityId(), id));
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onChangedPotionEffect(PotionEffect id, boolean p_70695_2_) {
/*  922 */     super.onChangedPotionEffect(id, p_70695_2_);
/*  923 */     this.playerNetServerHandler.sendPacket((Packet)new S1DPacketEntityEffect(getEntityId(), id));
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onFinishedPotionEffect(PotionEffect effect) {
/*  928 */     super.onFinishedPotionEffect(effect);
/*  929 */     this.playerNetServerHandler.sendPacket((Packet)new S1EPacketRemoveEntityEffect(getEntityId(), effect));
/*      */   }
/*      */ 
/*      */   
/*      */   public void setPositionAndUpdate(double x, double y, double z) {
/*  934 */     this.playerNetServerHandler.setPlayerLocation(x, y, z, this.rotationYaw, this.rotationPitch);
/*      */   }
/*      */ 
/*      */   
/*      */   public void onCriticalHit(Entity entityHit) {
/*  939 */     getServerForPlayer().getEntityTracker().func_151248_b((Entity)this, (Packet)new S0BPacketAnimation(entityHit, 4));
/*      */   }
/*      */ 
/*      */   
/*      */   public void onEnchantmentCritical(Entity entityHit) {
/*  944 */     getServerForPlayer().getEntityTracker().func_151248_b((Entity)this, (Packet)new S0BPacketAnimation(entityHit, 5));
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendPlayerAbilities() {
/*  949 */     if (this.playerNetServerHandler != null) {
/*      */       
/*  951 */       this.playerNetServerHandler.sendPacket((Packet)new S39PacketPlayerAbilities(this.capabilities));
/*  952 */       updatePotionMetadata();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public WorldServer getServerForPlayer() {
/*  958 */     return (WorldServer)this.worldObj;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setGameType(WorldSettings.GameType gameType) {
/*  963 */     this.theItemInWorldManager.setGameType(gameType);
/*  964 */     this.playerNetServerHandler.sendPacket((Packet)new S2BPacketChangeGameState(3, gameType.getID()));
/*      */     
/*  966 */     if (gameType == WorldSettings.GameType.SPECTATOR) {
/*      */       
/*  968 */       mountEntity((Entity)null);
/*      */     }
/*      */     else {
/*      */       
/*  972 */       setSpectatingEntity((Entity)this);
/*      */     } 
/*      */     
/*  975 */     sendPlayerAbilities();
/*  976 */     markPotionsDirty();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSpectator() {
/*  981 */     return (this.theItemInWorldManager.getGameType() == WorldSettings.GameType.SPECTATOR);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addChatMessage(IChatComponent component) {
/*  986 */     this.playerNetServerHandler.sendPacket((Packet)new S02PacketChat(component));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
/*  991 */     if ("seed".equals(commandName) && !this.mcServer.isDedicatedServer())
/*      */     {
/*  993 */       return true;
/*      */     }
/*  995 */     if (!"tell".equals(commandName) && !"help".equals(commandName) && !"me".equals(commandName) && !"trigger".equals(commandName)) {
/*      */       
/*  997 */       if (this.mcServer.getConfigurationManager().canSendCommands(getGameProfile())) {
/*      */         
/*  999 */         UserListOpsEntry userlistopsentry = (UserListOpsEntry)this.mcServer.getConfigurationManager().getOppedPlayers().getEntry(getGameProfile());
/* 1000 */         return (userlistopsentry != null) ? ((userlistopsentry.getPermissionLevel() >= permLevel)) : ((this.mcServer.getOpPermissionLevel() >= permLevel));
/*      */       } 
/*      */ 
/*      */       
/* 1004 */       return false;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1009 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPlayerIP() {
/* 1015 */     String s = this.playerNetServerHandler.netManager.getRemoteAddress().toString();
/* 1016 */     s = s.substring(s.indexOf("/") + 1);
/* 1017 */     s = s.substring(0, s.indexOf(":"));
/* 1018 */     return s;
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleClientSettings(C15PacketClientSettings packetIn) {
/* 1023 */     this.translator = packetIn.getLang();
/* 1024 */     this.chatVisibility = packetIn.getChatVisibility();
/* 1025 */     this.chatColours = packetIn.isColorsEnabled();
/* 1026 */     getDataWatcher().updateObject(10, Byte.valueOf((byte)packetIn.getModelPartFlags()));
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityPlayer.EnumChatVisibility getChatVisibility() {
/* 1031 */     return this.chatVisibility;
/*      */   }
/*      */ 
/*      */   
/*      */   public void loadResourcePack(String url, String hash) {
/* 1036 */     this.playerNetServerHandler.sendPacket((Packet)new S48PacketResourcePackSend(url, hash));
/*      */   }
/*      */ 
/*      */   
/*      */   public BlockPos getPosition() {
/* 1041 */     return new BlockPos(this.posX, this.posY + 0.5D, this.posZ);
/*      */   }
/*      */ 
/*      */   
/*      */   public void markPlayerActive() {
/* 1046 */     this.playerLastActiveTime = MinecraftServer.getCurrentTimeMillis();
/*      */   }
/*      */ 
/*      */   
/*      */   public StatisticsFile getStatFile() {
/* 1051 */     return this.statsFile;
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeEntity(Entity p_152339_1_) {
/* 1056 */     if (p_152339_1_ instanceof EntityPlayer) {
/*      */       
/* 1058 */       this.playerNetServerHandler.sendPacket((Packet)new S13PacketDestroyEntities(new int[] { p_152339_1_.getEntityId() }));
/*      */     }
/*      */     else {
/*      */       
/* 1062 */       this.destroyedItemsNetCache.add(Integer.valueOf(p_152339_1_.getEntityId()));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void updatePotionMetadata() {
/* 1068 */     if (isSpectator()) {
/*      */       
/* 1070 */       resetPotionEffectMetadata();
/* 1071 */       setInvisible(true);
/*      */     }
/*      */     else {
/*      */       
/* 1075 */       super.updatePotionMetadata();
/*      */     } 
/*      */     
/* 1078 */     getServerForPlayer().getEntityTracker().func_180245_a(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public Entity getSpectatingEntity() {
/* 1083 */     return (this.spectatingEntity == null) ? (Entity)this : this.spectatingEntity;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSpectatingEntity(Entity entityToSpectate) {
/* 1088 */     Entity entity = getSpectatingEntity();
/* 1089 */     this.spectatingEntity = (entityToSpectate == null) ? (Entity)this : entityToSpectate;
/*      */     
/* 1091 */     if (entity != this.spectatingEntity) {
/*      */       
/* 1093 */       this.playerNetServerHandler.sendPacket((Packet)new S43PacketCamera(this.spectatingEntity));
/* 1094 */       setPositionAndUpdate(this.spectatingEntity.posX, this.spectatingEntity.posY, this.spectatingEntity.posZ);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void attackTargetEntityWithCurrentItem(Entity targetEntity) {
/* 1100 */     if (this.theItemInWorldManager.getGameType() == WorldSettings.GameType.SPECTATOR) {
/*      */       
/* 1102 */       setSpectatingEntity(targetEntity);
/*      */     }
/*      */     else {
/*      */       
/* 1106 */       super.attackTargetEntityWithCurrentItem(targetEntity);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public long getLastActiveTime() {
/* 1112 */     return this.playerLastActiveTime;
/*      */   }
/*      */ 
/*      */   
/*      */   public IChatComponent getTabListDisplayName() {
/* 1117 */     return null;
/*      */   }
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\player\EntityPlayerMP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */