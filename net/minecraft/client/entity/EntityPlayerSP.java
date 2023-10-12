/*     */ package net.minecraft.client.entity;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.audio.ISound;
/*     */ import net.minecraft.client.audio.MovingSoundMinecartRiding;
/*     */ import net.minecraft.client.gui.GuiCommandBlock;
/*     */ import net.minecraft.client.gui.GuiEnchantment;
/*     */ import net.minecraft.client.gui.GuiHopper;
/*     */ import net.minecraft.client.gui.GuiRepair;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiScreenBook;
/*     */ import net.minecraft.client.gui.inventory.GuiBeacon;
/*     */ import net.minecraft.client.gui.inventory.GuiChest;
/*     */ import net.minecraft.client.gui.inventory.GuiFurnace;
/*     */ import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
/*     */ import net.minecraft.client.network.NetHandlerPlayClient;
/*     */ import net.minecraft.command.server.CommandBlockLogic;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.IMerchant;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.passive.EntityHorse;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.C01PacketChatMessage;
/*     */ import net.minecraft.network.play.client.C03PacketPlayer;
/*     */ import net.minecraft.network.play.client.C07PacketPlayerDigging;
/*     */ import net.minecraft.network.play.client.C0BPacketEntityAction;
/*     */ import net.minecraft.network.play.client.C0CPacketInput;
/*     */ import net.minecraft.network.play.client.C0DPacketCloseWindow;
/*     */ import net.minecraft.network.play.client.C16PacketClientStatus;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.stats.StatBase;
/*     */ import net.minecraft.stats.StatFileWriter;
/*     */ import net.minecraft.tileentity.TileEntitySign;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.world.IInteractionObject;
/*     */ import net.minecraft.world.IWorldNameable;
/*     */ import net.minecraft.world.World;
/*     */ import rip.diavlo.base.Client;
/*     */ import rip.diavlo.base.events.player.UpdateEvent;
/*     */ import rip.diavlo.base.events.render.ChatEvent;
/*     */ 
/*     */ public class EntityPlayerSP extends AbstractClientPlayer {
/*     */   public final NetHandlerPlayClient sendQueue;
/*     */   private final StatFileWriter statWriter;
/*     */   private double lastReportedPosX;
/*     */   private double lastReportedPosY;
/*     */   private double lastReportedPosZ;
/*     */   private float lastReportedYaw;
/*     */   private float lastReportedPitch;
/*     */   private boolean serverSneakState;
/*     */   private boolean serverSprintState;
/*     */   private int positionUpdateTicks;
/*     */   private boolean hasValidHealth;
/*     */   private String clientBrand;
/*     */   
/*     */   public EntityPlayerSP(Minecraft mcIn, World worldIn, NetHandlerPlayClient netHandler, StatFileWriter statFile) {
/*  63 */     super(worldIn, netHandler.getGameProfile());
/*  64 */     this.sendQueue = netHandler;
/*  65 */     this.statWriter = statFile;
/*  66 */     this.mc = mcIn;
/*  67 */     this.dimension = 0;
/*     */   }
/*     */   public MovementInput movementInput; protected Minecraft mc; protected int sprintToggleTimer; public int sprintingTicksLeft; public float renderArmYaw; public float renderArmPitch; public float prevRenderArmYaw; public float prevRenderArmPitch; private int horseJumpPowerCounter; private float horseJumpPower; public float timeInPortal; public float prevTimeInPortal;
/*     */   
/*     */   public boolean attackEntityFrom(DamageSource source, float amount) {
/*  72 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void heal(float healAmount) {}
/*     */ 
/*     */   
/*     */   public void mountEntity(Entity entityIn) {
/*  81 */     super.mountEntity(entityIn);
/*     */     
/*  83 */     if (entityIn instanceof EntityMinecart)
/*     */     {
/*  85 */       this.mc.getSoundHandler().playSound((ISound)new MovingSoundMinecartRiding(this, (EntityMinecart)entityIn));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  91 */     if (this.worldObj.isBlockLoaded(new BlockPos(this.posX, 0.0D, this.posZ))) {
/*     */       
/*  93 */       super.onUpdate();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  98 */       Client.getInstance().getEventBus().post(new UpdateEvent());
/*     */       
/* 100 */       if (isRiding()) {
/*     */         
/* 102 */         this.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C05PacketPlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
/* 103 */         this.sendQueue.addToSendQueue((Packet)new C0CPacketInput(this.moveStrafing, this.moveForward, this.movementInput.jump, this.movementInput.sneak));
/*     */       }
/*     */       else {
/*     */         
/* 107 */         onUpdateWalkingPlayer();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdateWalkingPlayer() {
/* 114 */     boolean flag = isSprinting();
/*     */     
/* 116 */     if (flag != this.serverSprintState) {
/*     */       
/* 118 */       if (flag) {
/*     */         
/* 120 */         this.sendQueue.addToSendQueue((Packet)new C0BPacketEntityAction((Entity)this, C0BPacketEntityAction.Action.START_SPRINTING));
/*     */       }
/*     */       else {
/*     */         
/* 124 */         this.sendQueue.addToSendQueue((Packet)new C0BPacketEntityAction((Entity)this, C0BPacketEntityAction.Action.STOP_SPRINTING));
/*     */       } 
/*     */       
/* 127 */       this.serverSprintState = flag;
/*     */     } 
/*     */     
/* 130 */     boolean flag1 = isSneaking();
/*     */     
/* 132 */     if (flag1 != this.serverSneakState) {
/*     */       
/* 134 */       if (flag1) {
/*     */         
/* 136 */         this.sendQueue.addToSendQueue((Packet)new C0BPacketEntityAction((Entity)this, C0BPacketEntityAction.Action.START_SNEAKING));
/*     */       }
/*     */       else {
/*     */         
/* 140 */         this.sendQueue.addToSendQueue((Packet)new C0BPacketEntityAction((Entity)this, C0BPacketEntityAction.Action.STOP_SNEAKING));
/*     */       } 
/*     */       
/* 143 */       this.serverSneakState = flag1;
/*     */     } 
/*     */     
/* 146 */     if (isCurrentViewEntity()) {
/*     */       
/* 148 */       double d0 = this.posX - this.lastReportedPosX;
/* 149 */       double d1 = (getEntityBoundingBox()).minY - this.lastReportedPosY;
/* 150 */       double d2 = this.posZ - this.lastReportedPosZ;
/* 151 */       double d3 = (this.rotationYaw - this.lastReportedYaw);
/* 152 */       double d4 = (this.rotationPitch - this.lastReportedPitch);
/* 153 */       boolean flag2 = (d0 * d0 + d1 * d1 + d2 * d2 > 9.0E-4D || this.positionUpdateTicks >= 20);
/* 154 */       boolean flag3 = (d3 != 0.0D || d4 != 0.0D);
/*     */       
/* 156 */       if (this.ridingEntity == null) {
/*     */         
/* 158 */         if (flag2 && flag3)
/*     */         {
/* 160 */           this.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(this.posX, (getEntityBoundingBox()).minY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround));
/*     */         }
/* 162 */         else if (flag2)
/*     */         {
/* 164 */           this.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.posX, (getEntityBoundingBox()).minY, this.posZ, this.onGround));
/*     */         }
/* 166 */         else if (flag3)
/*     */         {
/* 168 */           this.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C05PacketPlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
/*     */         }
/*     */         else
/*     */         {
/* 172 */           this.sendQueue.addToSendQueue((Packet)new C03PacketPlayer(this.onGround));
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 177 */         this.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(this.motionX, -999.0D, this.motionZ, this.rotationYaw, this.rotationPitch, this.onGround));
/* 178 */         flag2 = false;
/*     */       } 
/*     */       
/* 181 */       this.positionUpdateTicks++;
/*     */       
/* 183 */       if (flag2) {
/*     */         
/* 185 */         this.lastReportedPosX = this.posX;
/* 186 */         this.lastReportedPosY = (getEntityBoundingBox()).minY;
/* 187 */         this.lastReportedPosZ = this.posZ;
/* 188 */         this.positionUpdateTicks = 0;
/*     */       } 
/*     */       
/* 191 */       if (flag3) {
/*     */         
/* 193 */         this.lastReportedYaw = this.rotationYaw;
/* 194 */         this.lastReportedPitch = this.rotationPitch;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityItem dropOneItem(boolean dropAll) {
/* 201 */     C07PacketPlayerDigging.Action c07packetplayerdigging$action = dropAll ? C07PacketPlayerDigging.Action.DROP_ALL_ITEMS : C07PacketPlayerDigging.Action.DROP_ITEM;
/* 202 */     this.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(c07packetplayerdigging$action, BlockPos.ORIGIN, EnumFacing.DOWN));
/* 203 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void joinEntityItemWithWorld(EntityItem itemIn) {}
/*     */ 
/*     */   
/*     */   public void sendChatMessage(String message) {
/* 212 */     ChatEvent event = new ChatEvent(message);
/* 213 */     Client.getInstance().getEventBus().post(event);
/* 214 */     if (event.isCancelled())
/*     */       return; 
/* 216 */     this.sendQueue.addToSendQueue((Packet)new C01PacketChatMessage(event.getMessage()));
/*     */   }
/*     */   
/*     */   public void swingItem() {
/* 220 */     super.swingItem();
/* 221 */     this.sendQueue.addToSendQueue((Packet)new C0APacketAnimation());
/*     */   }
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
/*     */   public void respawnPlayer() {
/* 235 */     this.sendQueue.addToSendQueue((Packet)new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void damageEntity(DamageSource damageSrc, float damageAmount) {
/* 240 */     if (!isEntityInvulnerable(damageSrc))
/*     */     {
/* 242 */       setHealth(getHealth() - damageAmount);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeScreen() {
/* 248 */     this.sendQueue.addToSendQueue((Packet)new C0DPacketCloseWindow(this.openContainer.windowId));
/* 249 */     closeScreenAndDropStack();
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeScreenAndDropStack() {
/* 254 */     this.inventory.setItemStack((ItemStack)null);
/* 255 */     super.closeScreen();
/* 256 */     this.mc.displayGuiScreen((GuiScreen)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlayerSPHealth(float health) {
/* 261 */     if (this.hasValidHealth) {
/*     */       
/* 263 */       float f = getHealth() - health;
/*     */       
/* 265 */       if (f <= 0.0F)
/*     */       {
/* 267 */         setHealth(health);
/*     */         
/* 269 */         if (f < 0.0F)
/*     */         {
/* 271 */           this.hurtResistantTime = this.maxHurtResistantTime / 2;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 276 */         this.lastDamage = f;
/* 277 */         setHealth(getHealth());
/* 278 */         this.hurtResistantTime = this.maxHurtResistantTime;
/* 279 */         damageEntity(DamageSource.generic, f);
/* 280 */         this.hurtTime = this.maxHurtTime = 10;
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 285 */       setHealth(health);
/* 286 */       this.hasValidHealth = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addStat(StatBase stat, int amount) {
/* 292 */     if (stat != null)
/*     */     {
/* 294 */       if (stat.isIndependent)
/*     */       {
/* 296 */         super.addStat(stat, amount);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendPlayerAbilities() {
/* 303 */     this.sendQueue.addToSendQueue((Packet)new C13PacketPlayerAbilities(this.capabilities));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUser() {
/* 308 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void sendHorseJump() {
/* 313 */     this.sendQueue.addToSendQueue((Packet)new C0BPacketEntityAction((Entity)this, C0BPacketEntityAction.Action.RIDING_JUMP, (int)(getHorseJumpPower() * 100.0F)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendHorseInventory() {
/* 318 */     this.sendQueue.addToSendQueue((Packet)new C0BPacketEntityAction((Entity)this, C0BPacketEntityAction.Action.OPEN_INVENTORY));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setClientBrand(String brand) {
/* 323 */     this.clientBrand = brand;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getClientBrand() {
/* 328 */     return this.clientBrand;
/*     */   }
/*     */ 
/*     */   
/*     */   public StatFileWriter getStatFileWriter() {
/* 333 */     return this.statWriter;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addChatComponentMessage(IChatComponent chatComponent) {
/* 338 */     this.mc.ingameGUI.getChatGUI().printChatMessage(chatComponent);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean pushOutOfBlocks(double x, double y, double z) {
/* 343 */     if (this.noClip)
/*     */     {
/* 345 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 349 */     BlockPos blockpos = new BlockPos(x, y, z);
/* 350 */     double d0 = x - blockpos.getX();
/* 351 */     double d1 = z - blockpos.getZ();
/*     */     
/* 353 */     if (!isOpenBlockSpace(blockpos)) {
/*     */       
/* 355 */       int i = -1;
/* 356 */       double d2 = 9999.0D;
/*     */       
/* 358 */       if (isOpenBlockSpace(blockpos.west()) && d0 < d2) {
/*     */         
/* 360 */         d2 = d0;
/* 361 */         i = 0;
/*     */       } 
/*     */       
/* 364 */       if (isOpenBlockSpace(blockpos.east()) && 1.0D - d0 < d2) {
/*     */         
/* 366 */         d2 = 1.0D - d0;
/* 367 */         i = 1;
/*     */       } 
/*     */       
/* 370 */       if (isOpenBlockSpace(blockpos.north()) && d1 < d2) {
/*     */         
/* 372 */         d2 = d1;
/* 373 */         i = 4;
/*     */       } 
/*     */       
/* 376 */       if (isOpenBlockSpace(blockpos.south()) && 1.0D - d1 < d2) {
/*     */         
/* 378 */         d2 = 1.0D - d1;
/* 379 */         i = 5;
/*     */       } 
/*     */       
/* 382 */       float f = 0.1F;
/*     */       
/* 384 */       if (i == 0)
/*     */       {
/* 386 */         this.motionX = -f;
/*     */       }
/*     */       
/* 389 */       if (i == 1)
/*     */       {
/* 391 */         this.motionX = f;
/*     */       }
/*     */       
/* 394 */       if (i == 4)
/*     */       {
/* 396 */         this.motionZ = -f;
/*     */       }
/*     */       
/* 399 */       if (i == 5)
/*     */       {
/* 401 */         this.motionZ = f;
/*     */       }
/*     */     } 
/*     */     
/* 405 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isOpenBlockSpace(BlockPos pos) {
/* 411 */     return (!this.worldObj.getBlockState(pos).getBlock().isNormalCube() && !this.worldObj.getBlockState(pos.up()).getBlock().isNormalCube());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSprinting(boolean sprinting) {
/* 416 */     super.setSprinting(sprinting);
/* 417 */     this.sprintingTicksLeft = sprinting ? 600 : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setXPStats(float currentXP, int maxXP, int level) {
/* 422 */     this.experience = currentXP;
/* 423 */     this.experienceTotal = maxXP;
/* 424 */     this.experienceLevel = level;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addChatMessage(IChatComponent component) {
/* 429 */     this.mc.ingameGUI.getChatGUI().printChatMessage(component);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
/* 434 */     return (permLevel <= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos getPosition() {
/* 439 */     return new BlockPos(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void playSound(String name, float volume, float pitch) {
/* 444 */     this.worldObj.playSound(this.posX, this.posY, this.posZ, name, volume, pitch, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isServerWorld() {
/* 449 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRidingHorse() {
/* 454 */     return (this.ridingEntity != null && this.ridingEntity instanceof EntityHorse && ((EntityHorse)this.ridingEntity).isHorseSaddled());
/*     */   }
/*     */ 
/*     */   
/*     */   public float getHorseJumpPower() {
/* 459 */     return this.horseJumpPower;
/*     */   }
/*     */ 
/*     */   
/*     */   public void openEditSign(TileEntitySign signTile) {
/* 464 */     this.mc.displayGuiScreen((GuiScreen)new GuiEditSign(signTile));
/*     */   }
/*     */ 
/*     */   
/*     */   public void openEditCommandBlock(CommandBlockLogic cmdBlockLogic) {
/* 469 */     this.mc.displayGuiScreen((GuiScreen)new GuiCommandBlock(cmdBlockLogic));
/*     */   }
/*     */ 
/*     */   
/*     */   public void displayGUIBook(ItemStack bookStack) {
/* 474 */     Item item = bookStack.getItem();
/*     */     
/* 476 */     if (item == Items.writable_book)
/*     */     {
/* 478 */       this.mc.displayGuiScreen((GuiScreen)new GuiScreenBook(this, bookStack, true));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void displayGUIChest(IInventory chestInventory) {
/* 484 */     String s = (chestInventory instanceof IInteractionObject) ? ((IInteractionObject)chestInventory).getGuiID() : "minecraft:container";
/*     */     
/* 486 */     if ("minecraft:chest".equals(s)) {
/*     */       
/* 488 */       this.mc.displayGuiScreen((GuiScreen)new GuiChest((IInventory)this.inventory, chestInventory));
/*     */     }
/* 490 */     else if ("minecraft:hopper".equals(s)) {
/*     */       
/* 492 */       this.mc.displayGuiScreen((GuiScreen)new GuiHopper(this.inventory, chestInventory));
/*     */     }
/* 494 */     else if ("minecraft:furnace".equals(s)) {
/*     */       
/* 496 */       this.mc.displayGuiScreen((GuiScreen)new GuiFurnace(this.inventory, chestInventory));
/*     */     }
/* 498 */     else if ("minecraft:brewing_stand".equals(s)) {
/*     */       
/* 500 */       this.mc.displayGuiScreen((GuiScreen)new GuiBrewingStand(this.inventory, chestInventory));
/*     */     }
/* 502 */     else if ("minecraft:beacon".equals(s)) {
/*     */       
/* 504 */       this.mc.displayGuiScreen((GuiScreen)new GuiBeacon(this.inventory, chestInventory));
/*     */     }
/* 506 */     else if (!"minecraft:dispenser".equals(s) && !"minecraft:dropper".equals(s)) {
/*     */       
/* 508 */       this.mc.displayGuiScreen((GuiScreen)new GuiChest((IInventory)this.inventory, chestInventory));
/*     */     }
/*     */     else {
/*     */       
/* 512 */       this.mc.displayGuiScreen((GuiScreen)new GuiDispenser(this.inventory, chestInventory));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void displayGUIHorse(EntityHorse horse, IInventory horseInventory) {
/* 518 */     this.mc.displayGuiScreen((GuiScreen)new GuiScreenHorseInventory((IInventory)this.inventory, horseInventory, horse));
/*     */   }
/*     */ 
/*     */   
/*     */   public void displayGui(IInteractionObject guiOwner) {
/* 523 */     String s = guiOwner.getGuiID();
/*     */     
/* 525 */     if ("minecraft:crafting_table".equals(s)) {
/*     */       
/* 527 */       this.mc.displayGuiScreen((GuiScreen)new GuiCrafting(this.inventory, this.worldObj));
/*     */     }
/* 529 */     else if ("minecraft:enchanting_table".equals(s)) {
/*     */       
/* 531 */       this.mc.displayGuiScreen((GuiScreen)new GuiEnchantment(this.inventory, this.worldObj, (IWorldNameable)guiOwner));
/*     */     }
/* 533 */     else if ("minecraft:anvil".equals(s)) {
/*     */       
/* 535 */       this.mc.displayGuiScreen((GuiScreen)new GuiRepair(this.inventory, this.worldObj));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void displayVillagerTradeGui(IMerchant villager) {
/* 541 */     this.mc.displayGuiScreen((GuiScreen)new GuiMerchant(this.inventory, villager, this.worldObj));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCriticalHit(Entity entityHit) {
/* 546 */     this.mc.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnchantmentCritical(Entity entityHit) {
/* 551 */     this.mc.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT_MAGIC);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSneaking() {
/* 556 */     boolean flag = (this.movementInput != null) ? this.movementInput.sneak : false;
/* 557 */     return (flag && !this.sleeping);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateEntityActionState() {
/* 562 */     super.updateEntityActionState();
/*     */     
/* 564 */     if (isCurrentViewEntity()) {
/*     */       
/* 566 */       this.moveStrafing = this.movementInput.moveStrafe;
/* 567 */       this.moveForward = this.movementInput.moveForward;
/* 568 */       this.isJumping = this.movementInput.jump;
/* 569 */       this.prevRenderArmYaw = this.renderArmYaw;
/* 570 */       this.prevRenderArmPitch = this.renderArmPitch;
/* 571 */       this.renderArmPitch = (float)(this.renderArmPitch + (this.rotationPitch - this.renderArmPitch) * 0.5D);
/* 572 */       this.renderArmYaw = (float)(this.renderArmYaw + (this.rotationYaw - this.renderArmYaw) * 0.5D);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isCurrentViewEntity() {
/* 578 */     return (this.mc.getRenderViewEntity() == this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLivingUpdate() {
/* 583 */     if (this.sprintingTicksLeft > 0) {
/*     */       
/* 585 */       this.sprintingTicksLeft--;
/*     */       
/* 587 */       if (this.sprintingTicksLeft == 0)
/*     */       {
/* 589 */         setSprinting(false);
/*     */       }
/*     */     } 
/*     */     
/* 593 */     if (this.sprintToggleTimer > 0)
/*     */     {
/* 595 */       this.sprintToggleTimer--;
/*     */     }
/*     */     
/* 598 */     this.prevTimeInPortal = this.timeInPortal;
/*     */     
/* 600 */     if (this.inPortal) {
/*     */       
/* 602 */       if (this.mc.currentScreen != null && !this.mc.currentScreen.doesGuiPauseGame())
/*     */       {
/* 604 */         this.mc.displayGuiScreen((GuiScreen)null);
/*     */       }
/*     */       
/* 607 */       if (this.timeInPortal == 0.0F)
/*     */       {
/* 609 */         this.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("portal.trigger"), this.rand.nextFloat() * 0.4F + 0.8F));
/*     */       }
/*     */       
/* 612 */       this.timeInPortal += 0.0125F;
/*     */       
/* 614 */       if (this.timeInPortal >= 1.0F)
/*     */       {
/* 616 */         this.timeInPortal = 1.0F;
/*     */       }
/*     */       
/* 619 */       this.inPortal = false;
/*     */     }
/* 621 */     else if (isPotionActive(Potion.confusion) && getActivePotionEffect(Potion.confusion).getDuration() > 60) {
/*     */       
/* 623 */       this.timeInPortal += 0.006666667F;
/*     */       
/* 625 */       if (this.timeInPortal > 1.0F)
/*     */       {
/* 627 */         this.timeInPortal = 1.0F;
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 632 */       if (this.timeInPortal > 0.0F)
/*     */       {
/* 634 */         this.timeInPortal -= 0.05F;
/*     */       }
/*     */       
/* 637 */       if (this.timeInPortal < 0.0F)
/*     */       {
/* 639 */         this.timeInPortal = 0.0F;
/*     */       }
/*     */     } 
/*     */     
/* 643 */     if (this.timeUntilPortal > 0)
/*     */     {
/* 645 */       this.timeUntilPortal--;
/*     */     }
/*     */     
/* 648 */     boolean flag = this.movementInput.jump;
/* 649 */     boolean flag1 = this.movementInput.sneak;
/* 650 */     float f = 0.8F;
/* 651 */     boolean flag2 = (this.movementInput.moveForward >= f);
/* 652 */     this.movementInput.updatePlayerMoveState();
/*     */     
/* 654 */     if (isUsingItem() && !isRiding()) {
/*     */       
/* 656 */       this.movementInput.moveStrafe *= 0.2F;
/* 657 */       this.movementInput.moveForward *= 0.2F;
/* 658 */       this.sprintToggleTimer = 0;
/*     */     } 
/*     */     
/* 661 */     pushOutOfBlocks(this.posX - this.width * 0.35D, (getEntityBoundingBox()).minY + 0.5D, this.posZ + this.width * 0.35D);
/* 662 */     pushOutOfBlocks(this.posX - this.width * 0.35D, (getEntityBoundingBox()).minY + 0.5D, this.posZ - this.width * 0.35D);
/* 663 */     pushOutOfBlocks(this.posX + this.width * 0.35D, (getEntityBoundingBox()).minY + 0.5D, this.posZ - this.width * 0.35D);
/* 664 */     pushOutOfBlocks(this.posX + this.width * 0.35D, (getEntityBoundingBox()).minY + 0.5D, this.posZ + this.width * 0.35D);
/* 665 */     boolean flag3 = (getFoodStats().getFoodLevel() > 6.0F || this.capabilities.allowFlying);
/*     */     
/* 667 */     if (this.onGround && !flag1 && !flag2 && this.movementInput.moveForward >= f && !isSprinting() && flag3 && !isUsingItem() && !isPotionActive(Potion.blindness))
/*     */     {
/* 669 */       if (this.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.isKeyDown()) {
/*     */         
/* 671 */         this.sprintToggleTimer = 7;
/*     */       }
/*     */       else {
/*     */         
/* 675 */         setSprinting(true);
/*     */       } 
/*     */     }
/*     */     
/* 679 */     if (!isSprinting() && this.movementInput.moveForward >= f && flag3 && !isUsingItem() && !isPotionActive(Potion.blindness) && this.mc.gameSettings.keyBindSprint.isKeyDown())
/*     */     {
/* 681 */       setSprinting(true);
/*     */     }
/*     */     
/* 684 */     if (isSprinting() && (this.movementInput.moveForward < f || this.isCollidedHorizontally || !flag3))
/*     */     {
/* 686 */       setSprinting(false);
/*     */     }
/*     */     
/* 689 */     if (this.capabilities.allowFlying)
/*     */     {
/* 691 */       if (this.mc.playerController.isSpectatorMode()) {
/*     */         
/* 693 */         if (!this.capabilities.isFlying)
/*     */         {
/* 695 */           this.capabilities.isFlying = true;
/* 696 */           sendPlayerAbilities();
/*     */         }
/*     */       
/* 699 */       } else if (!flag && this.movementInput.jump) {
/*     */         
/* 701 */         if (this.flyToggleTimer == 0) {
/*     */           
/* 703 */           this.flyToggleTimer = 7;
/*     */         }
/*     */         else {
/*     */           
/* 707 */           this.capabilities.isFlying = !this.capabilities.isFlying;
/* 708 */           sendPlayerAbilities();
/* 709 */           this.flyToggleTimer = 0;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 714 */     if (this.capabilities.isFlying && isCurrentViewEntity()) {
/*     */       
/* 716 */       if (this.movementInput.sneak)
/*     */       {
/* 718 */         this.motionY -= (this.capabilities.getFlySpeed() * 3.0F);
/*     */       }
/*     */       
/* 721 */       if (this.movementInput.jump)
/*     */       {
/* 723 */         this.motionY += (this.capabilities.getFlySpeed() * 3.0F);
/*     */       }
/*     */     } 
/*     */     
/* 727 */     if (isRidingHorse()) {
/*     */       
/* 729 */       if (this.horseJumpPowerCounter < 0) {
/*     */         
/* 731 */         this.horseJumpPowerCounter++;
/*     */         
/* 733 */         if (this.horseJumpPowerCounter == 0)
/*     */         {
/* 735 */           this.horseJumpPower = 0.0F;
/*     */         }
/*     */       } 
/*     */       
/* 739 */       if (flag && !this.movementInput.jump) {
/*     */         
/* 741 */         this.horseJumpPowerCounter = -10;
/* 742 */         sendHorseJump();
/*     */       }
/* 744 */       else if (!flag && this.movementInput.jump) {
/*     */         
/* 746 */         this.horseJumpPowerCounter = 0;
/* 747 */         this.horseJumpPower = 0.0F;
/*     */       }
/* 749 */       else if (flag) {
/*     */         
/* 751 */         this.horseJumpPowerCounter++;
/*     */         
/* 753 */         if (this.horseJumpPowerCounter < 10)
/*     */         {
/* 755 */           this.horseJumpPower = this.horseJumpPowerCounter * 0.1F;
/*     */         }
/*     */         else
/*     */         {
/* 759 */           this.horseJumpPower = 0.8F + 2.0F / (this.horseJumpPowerCounter - 9) * 0.1F;
/*     */         }
/*     */       
/*     */       } 
/*     */     } else {
/*     */       
/* 765 */       this.horseJumpPower = 0.0F;
/*     */     } 
/*     */     
/* 768 */     super.onLivingUpdate();
/*     */     
/* 770 */     if (this.onGround && this.capabilities.isFlying && !this.mc.playerController.isSpectatorMode()) {
/*     */       
/* 772 */       this.capabilities.isFlying = false;
/* 773 */       sendPlayerAbilities();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\entity\EntityPlayerSP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */