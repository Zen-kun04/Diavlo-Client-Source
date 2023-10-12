/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.audio.ISound;
/*     */ import net.minecraft.client.audio.PositionedSoundRecord;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.network.NetHandlerPlayClient;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemBlock;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.C02PacketUseEntity;
/*     */ import net.minecraft.network.play.client.C07PacketPlayerDigging;
/*     */ import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
/*     */ import net.minecraft.network.play.client.C09PacketHeldItemChange;
/*     */ import net.minecraft.network.play.client.C0EPacketClickWindow;
/*     */ import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
/*     */ import net.minecraft.network.play.client.C11PacketEnchantItem;
/*     */ import net.minecraft.stats.StatFileWriter;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldSettings;
/*     */ 
/*     */ public class PlayerControllerMP
/*     */ {
/*     */   private final Minecraft mc;
/*     */   private final NetHandlerPlayClient netClientHandler;
/*  36 */   private BlockPos currentBlock = new BlockPos(-1, -1, -1);
/*     */   private ItemStack currentItemHittingBlock;
/*     */   public float curBlockDamageMP;
/*     */   private float stepSoundTickCounter;
/*     */   private int blockHitDelay;
/*     */   private boolean isHittingBlock;
/*  42 */   private WorldSettings.GameType currentGameType = WorldSettings.GameType.SURVIVAL;
/*     */   
/*     */   private int currentPlayerItem;
/*     */   
/*     */   public PlayerControllerMP(Minecraft mcIn, NetHandlerPlayClient netHandler) {
/*  47 */     this.mc = mcIn;
/*  48 */     this.netClientHandler = netHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void clickBlockCreative(Minecraft mcIn, PlayerControllerMP playerController, BlockPos pos, EnumFacing facing) {
/*  53 */     if (!mcIn.theWorld.extinguishFire((EntityPlayer)mcIn.thePlayer, pos, facing))
/*     */     {
/*  55 */       playerController.onPlayerDestroyBlock(pos, facing);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlayerCapabilities(EntityPlayer player) {
/*  61 */     this.currentGameType.configurePlayerCapabilities(player.capabilities);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSpectator() {
/*  66 */     return (this.currentGameType == WorldSettings.GameType.SPECTATOR);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setGameType(WorldSettings.GameType type) {
/*  71 */     this.currentGameType = type;
/*  72 */     this.currentGameType.configurePlayerCapabilities(this.mc.thePlayer.capabilities);
/*     */   }
/*     */ 
/*     */   
/*     */   public void flipPlayer(EntityPlayer playerIn) {
/*  77 */     playerIn.rotationYaw = -180.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldDrawHUD() {
/*  82 */     return this.currentGameType.isSurvivalOrAdventure();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onPlayerDestroyBlock(BlockPos pos, EnumFacing side) {
/*  87 */     if (this.currentGameType.isAdventure()) {
/*     */       
/*  89 */       if (this.currentGameType == WorldSettings.GameType.SPECTATOR)
/*     */       {
/*  91 */         return false;
/*     */       }
/*     */       
/*  94 */       if (!this.mc.thePlayer.isAllowEdit()) {
/*     */         
/*  96 */         Block block = this.mc.theWorld.getBlockState(pos).getBlock();
/*  97 */         ItemStack itemstack = this.mc.thePlayer.getCurrentEquippedItem();
/*     */         
/*  99 */         if (itemstack == null)
/*     */         {
/* 101 */           return false;
/*     */         }
/*     */         
/* 104 */         if (!itemstack.canDestroy(block))
/*     */         {
/* 106 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 111 */     if (this.currentGameType.isCreative() && this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof net.minecraft.item.ItemSword)
/*     */     {
/* 113 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 117 */     World world = this.mc.theWorld;
/* 118 */     IBlockState iblockstate = world.getBlockState(pos);
/* 119 */     Block block1 = iblockstate.getBlock();
/*     */     
/* 121 */     if (block1.getMaterial() == Material.air)
/*     */     {
/* 123 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 127 */     world.playAuxSFX(2001, pos, Block.getStateId(iblockstate));
/* 128 */     boolean flag = world.setBlockToAir(pos);
/*     */     
/* 130 */     if (flag)
/*     */     {
/* 132 */       block1.onBlockDestroyedByPlayer(world, pos, iblockstate);
/*     */     }
/*     */     
/* 135 */     this.currentBlock = new BlockPos(this.currentBlock.getX(), -1, this.currentBlock.getZ());
/*     */     
/* 137 */     if (!this.currentGameType.isCreative()) {
/*     */       
/* 139 */       ItemStack itemstack1 = this.mc.thePlayer.getCurrentEquippedItem();
/*     */       
/* 141 */       if (itemstack1 != null) {
/*     */         
/* 143 */         itemstack1.onBlockDestroyed(world, block1, pos, (EntityPlayer)this.mc.thePlayer);
/*     */         
/* 145 */         if (itemstack1.stackSize == 0)
/*     */         {
/* 147 */           this.mc.thePlayer.destroyCurrentEquippedItem();
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 152 */     return flag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean clickBlock(BlockPos loc, EnumFacing face) {
/* 159 */     if (this.currentGameType.isAdventure()) {
/*     */       
/* 161 */       if (this.currentGameType == WorldSettings.GameType.SPECTATOR)
/*     */       {
/* 163 */         return false;
/*     */       }
/*     */       
/* 166 */       if (!this.mc.thePlayer.isAllowEdit()) {
/*     */         
/* 168 */         Block block = this.mc.theWorld.getBlockState(loc).getBlock();
/* 169 */         ItemStack itemstack = this.mc.thePlayer.getCurrentEquippedItem();
/*     */         
/* 171 */         if (itemstack == null)
/*     */         {
/* 173 */           return false;
/*     */         }
/*     */         
/* 176 */         if (!itemstack.canDestroy(block))
/*     */         {
/* 178 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 183 */     if (!this.mc.theWorld.getWorldBorder().contains(loc))
/*     */     {
/* 185 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 189 */     if (this.currentGameType.isCreative()) {
/*     */       
/* 191 */       this.netClientHandler.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, loc, face));
/* 192 */       clickBlockCreative(this.mc, this, loc, face);
/* 193 */       this.blockHitDelay = 5;
/*     */     }
/* 195 */     else if (!this.isHittingBlock || !isHittingPosition(loc)) {
/*     */       
/* 197 */       if (this.isHittingBlock)
/*     */       {
/* 199 */         this.netClientHandler.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.currentBlock, face));
/*     */       }
/*     */       
/* 202 */       this.netClientHandler.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, loc, face));
/* 203 */       Block block1 = this.mc.theWorld.getBlockState(loc).getBlock();
/* 204 */       boolean flag = (block1.getMaterial() != Material.air);
/*     */       
/* 206 */       if (flag && this.curBlockDamageMP == 0.0F)
/*     */       {
/* 208 */         block1.onBlockClicked(this.mc.theWorld, loc, (EntityPlayer)this.mc.thePlayer);
/*     */       }
/*     */       
/* 211 */       if (flag && block1.getPlayerRelativeBlockHardness((EntityPlayer)this.mc.thePlayer, this.mc.thePlayer.worldObj, loc) >= 1.0F) {
/*     */         
/* 213 */         onPlayerDestroyBlock(loc, face);
/*     */       }
/*     */       else {
/*     */         
/* 217 */         this.isHittingBlock = true;
/* 218 */         this.currentBlock = loc;
/* 219 */         this.currentItemHittingBlock = this.mc.thePlayer.getHeldItem();
/* 220 */         this.curBlockDamageMP = 0.0F;
/* 221 */         this.stepSoundTickCounter = 0.0F;
/* 222 */         this.mc.theWorld.sendBlockBreakProgress(this.mc.thePlayer.getEntityId(), this.currentBlock, (int)(this.curBlockDamageMP * 10.0F) - 1);
/*     */       } 
/*     */     } 
/*     */     
/* 226 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetBlockRemoving() {
/* 232 */     if (this.isHittingBlock) {
/*     */       
/* 234 */       this.netClientHandler.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.currentBlock, EnumFacing.DOWN));
/* 235 */       this.isHittingBlock = false;
/* 236 */       this.curBlockDamageMP = 0.0F;
/* 237 */       this.mc.theWorld.sendBlockBreakProgress(this.mc.thePlayer.getEntityId(), this.currentBlock, -1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onPlayerDamageBlock(BlockPos posBlock, EnumFacing directionFacing) {
/* 243 */     syncCurrentPlayItem();
/*     */     
/* 245 */     if (this.blockHitDelay > 0) {
/*     */       
/* 247 */       this.blockHitDelay--;
/* 248 */       return true;
/*     */     } 
/* 250 */     if (this.currentGameType.isCreative() && this.mc.theWorld.getWorldBorder().contains(posBlock)) {
/*     */       
/* 252 */       this.blockHitDelay = 5;
/* 253 */       this.netClientHandler.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, posBlock, directionFacing));
/* 254 */       clickBlockCreative(this.mc, this, posBlock, directionFacing);
/* 255 */       return true;
/*     */     } 
/* 257 */     if (isHittingPosition(posBlock)) {
/*     */       
/* 259 */       Block block = this.mc.theWorld.getBlockState(posBlock).getBlock();
/*     */       
/* 261 */       if (block.getMaterial() == Material.air) {
/*     */         
/* 263 */         this.isHittingBlock = false;
/* 264 */         return false;
/*     */       } 
/*     */ 
/*     */       
/* 268 */       this.curBlockDamageMP += block.getPlayerRelativeBlockHardness((EntityPlayer)this.mc.thePlayer, this.mc.thePlayer.worldObj, posBlock);
/*     */       
/* 270 */       if (this.stepSoundTickCounter % 4.0F == 0.0F)
/*     */       {
/* 272 */         this.mc.getSoundHandler().playSound((ISound)new PositionedSoundRecord(new ResourceLocation(block.stepSound.getStepSound()), (block.stepSound.getVolume() + 1.0F) / 8.0F, block.stepSound.getFrequency() * 0.5F, posBlock.getX() + 0.5F, posBlock.getY() + 0.5F, posBlock.getZ() + 0.5F));
/*     */       }
/*     */       
/* 275 */       this.stepSoundTickCounter++;
/*     */       
/* 277 */       if (this.curBlockDamageMP >= 1.0F) {
/*     */         
/* 279 */         this.isHittingBlock = false;
/* 280 */         this.netClientHandler.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, posBlock, directionFacing));
/* 281 */         onPlayerDestroyBlock(posBlock, directionFacing);
/* 282 */         this.curBlockDamageMP = 0.0F;
/* 283 */         this.stepSoundTickCounter = 0.0F;
/* 284 */         this.blockHitDelay = 5;
/*     */       } 
/*     */       
/* 287 */       this.mc.theWorld.sendBlockBreakProgress(this.mc.thePlayer.getEntityId(), this.currentBlock, (int)(this.curBlockDamageMP * 10.0F) - 1);
/* 288 */       return true;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 293 */     return clickBlock(posBlock, directionFacing);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getBlockReachDistance() {
/* 299 */     return this.currentGameType.isCreative() ? 5.0F : 4.5F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateController() {
/* 304 */     syncCurrentPlayItem();
/*     */     
/* 306 */     if (this.netClientHandler.getNetworkManager().isChannelOpen()) {
/*     */       
/* 308 */       this.netClientHandler.getNetworkManager().processReceivedPackets();
/*     */     }
/*     */     else {
/*     */       
/* 312 */       this.netClientHandler.getNetworkManager().checkDisconnected();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isHittingPosition(BlockPos pos) {
/* 318 */     ItemStack itemstack = this.mc.thePlayer.getHeldItem();
/* 319 */     boolean flag = (this.currentItemHittingBlock == null && itemstack == null);
/*     */     
/* 321 */     if (this.currentItemHittingBlock != null && itemstack != null)
/*     */     {
/* 323 */       flag = (itemstack.getItem() == this.currentItemHittingBlock.getItem() && ItemStack.areItemStackTagsEqual(itemstack, this.currentItemHittingBlock) && (itemstack.isItemStackDamageable() || itemstack.getMetadata() == this.currentItemHittingBlock.getMetadata()));
/*     */     }
/*     */     
/* 326 */     return (pos.equals(this.currentBlock) && flag);
/*     */   }
/*     */ 
/*     */   
/*     */   private void syncCurrentPlayItem() {
/* 331 */     int i = this.mc.thePlayer.inventory.currentItem;
/*     */     
/* 333 */     if (i != this.currentPlayerItem) {
/*     */       
/* 335 */       this.currentPlayerItem = i;
/* 336 */       this.netClientHandler.addToSendQueue((Packet)new C09PacketHeldItemChange(this.currentPlayerItem));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onPlayerRightClick(EntityPlayerSP player, WorldClient worldIn, ItemStack heldStack, BlockPos hitPos, EnumFacing side, Vec3 hitVec) {
/* 342 */     syncCurrentPlayItem();
/* 343 */     float f = (float)(hitVec.xCoord - hitPos.getX());
/* 344 */     float f1 = (float)(hitVec.yCoord - hitPos.getY());
/* 345 */     float f2 = (float)(hitVec.zCoord - hitPos.getZ());
/* 346 */     boolean flag = false;
/*     */     
/* 348 */     if (!this.mc.theWorld.getWorldBorder().contains(hitPos))
/*     */     {
/* 350 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 354 */     if (this.currentGameType != WorldSettings.GameType.SPECTATOR) {
/*     */       
/* 356 */       IBlockState iblockstate = worldIn.getBlockState(hitPos);
/*     */       
/* 358 */       if ((!player.isSneaking() || player.getHeldItem() == null) && iblockstate.getBlock().onBlockActivated(worldIn, hitPos, iblockstate, (EntityPlayer)player, side, f, f1, f2))
/*     */       {
/* 360 */         flag = true;
/*     */       }
/*     */       
/* 363 */       if (!flag && heldStack != null && heldStack.getItem() instanceof ItemBlock) {
/*     */         
/* 365 */         ItemBlock itemblock = (ItemBlock)heldStack.getItem();
/*     */         
/* 367 */         if (!itemblock.canPlaceBlockOnSide(worldIn, hitPos, side, (EntityPlayer)player, heldStack))
/*     */         {
/* 369 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 374 */     this.netClientHandler.addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(hitPos, side.getIndex(), player.inventory.getCurrentItem(), f, f1, f2));
/*     */     
/* 376 */     if (!flag && this.currentGameType != WorldSettings.GameType.SPECTATOR) {
/*     */       
/* 378 */       if (heldStack == null)
/*     */       {
/* 380 */         return false;
/*     */       }
/* 382 */       if (this.currentGameType.isCreative()) {
/*     */         
/* 384 */         int i = heldStack.getMetadata();
/* 385 */         int j = heldStack.stackSize;
/* 386 */         boolean flag1 = heldStack.onItemUse((EntityPlayer)player, worldIn, hitPos, side, f, f1, f2);
/* 387 */         heldStack.setItemDamage(i);
/* 388 */         heldStack.stackSize = j;
/* 389 */         return flag1;
/*     */       } 
/*     */ 
/*     */       
/* 393 */       return heldStack.onItemUse((EntityPlayer)player, worldIn, hitPos, side, f, f1, f2);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 398 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean sendUseItem(EntityPlayer playerIn, World worldIn, ItemStack itemStackIn) {
/* 405 */     if (this.currentGameType == WorldSettings.GameType.SPECTATOR)
/*     */     {
/* 407 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 411 */     syncCurrentPlayItem();
/* 412 */     this.netClientHandler.addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(playerIn.inventory.getCurrentItem()));
/* 413 */     int i = itemStackIn.stackSize;
/* 414 */     ItemStack itemstack = itemStackIn.useItemRightClick(worldIn, playerIn);
/*     */     
/* 416 */     if (itemstack != itemStackIn || (itemstack != null && itemstack.stackSize != i)) {
/*     */       
/* 418 */       playerIn.inventory.mainInventory[playerIn.inventory.currentItem] = itemstack;
/*     */       
/* 420 */       if (itemstack.stackSize == 0)
/*     */       {
/* 422 */         playerIn.inventory.mainInventory[playerIn.inventory.currentItem] = null;
/*     */       }
/*     */       
/* 425 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 429 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityPlayerSP func_178892_a(World worldIn, StatFileWriter statWriter) {
/* 436 */     return new EntityPlayerSP(this.mc, worldIn, this.netClientHandler, statWriter);
/*     */   }
/*     */ 
/*     */   
/*     */   public void attackEntity(EntityPlayer playerIn, Entity targetEntity) {
/* 441 */     syncCurrentPlayItem();
/* 442 */     this.netClientHandler.addToSendQueue((Packet)new C02PacketUseEntity(targetEntity, C02PacketUseEntity.Action.ATTACK));
/*     */     
/* 444 */     if (this.currentGameType != WorldSettings.GameType.SPECTATOR)
/*     */     {
/* 446 */       playerIn.attackTargetEntityWithCurrentItem(targetEntity);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean interactWithEntitySendPacket(EntityPlayer playerIn, Entity targetEntity) {
/* 452 */     syncCurrentPlayItem();
/* 453 */     this.netClientHandler.addToSendQueue((Packet)new C02PacketUseEntity(targetEntity, C02PacketUseEntity.Action.INTERACT));
/* 454 */     return (this.currentGameType != WorldSettings.GameType.SPECTATOR && playerIn.interactWith(targetEntity));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPlayerRightClickingOnEntity(EntityPlayer player, Entity entityIn, MovingObjectPosition movingObject) {
/* 459 */     syncCurrentPlayItem();
/* 460 */     Vec3 vec3 = new Vec3(movingObject.hitVec.xCoord - entityIn.posX, movingObject.hitVec.yCoord - entityIn.posY, movingObject.hitVec.zCoord - entityIn.posZ);
/* 461 */     this.netClientHandler.addToSendQueue((Packet)new C02PacketUseEntity(entityIn, vec3));
/* 462 */     return (this.currentGameType != WorldSettings.GameType.SPECTATOR && entityIn.interactAt(player, vec3));
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack windowClick(int windowId, int slotId, int mouseButtonClicked, int mode, EntityPlayer playerIn) {
/* 467 */     short short1 = playerIn.openContainer.getNextTransactionID(playerIn.inventory);
/* 468 */     ItemStack itemstack = playerIn.openContainer.slotClick(slotId, mouseButtonClicked, mode, playerIn);
/* 469 */     this.netClientHandler.addToSendQueue((Packet)new C0EPacketClickWindow(windowId, slotId, mouseButtonClicked, mode, itemstack, short1));
/* 470 */     return itemstack;
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendEnchantPacket(int windowID, int button) {
/* 475 */     this.netClientHandler.addToSendQueue((Packet)new C11PacketEnchantItem(windowID, button));
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendSlotPacket(ItemStack itemStackIn, int slotId) {
/* 480 */     if (this.currentGameType.isCreative())
/*     */     {
/* 482 */       this.netClientHandler.addToSendQueue((Packet)new C10PacketCreativeInventoryAction(slotId, itemStackIn));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendPacketDropItem(ItemStack itemStackIn) {
/* 488 */     if (this.currentGameType.isCreative() && itemStackIn != null)
/*     */     {
/* 490 */       this.netClientHandler.addToSendQueue((Packet)new C10PacketCreativeInventoryAction(-1, itemStackIn));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onStoppedUsingItem(EntityPlayer playerIn) {
/* 496 */     syncCurrentPlayItem();
/* 497 */     this.netClientHandler.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
/* 498 */     playerIn.stopUsingItem();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean gameIsSurvivalOrAdventure() {
/* 503 */     return this.currentGameType.isSurvivalOrAdventure();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isNotCreative() {
/* 508 */     return !this.currentGameType.isCreative();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInCreativeMode() {
/* 513 */     return this.currentGameType.isCreative();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean extendedReach() {
/* 518 */     return this.currentGameType.isCreative();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRidingHorse() {
/* 523 */     return (this.mc.thePlayer.isRiding() && this.mc.thePlayer.ridingEntity instanceof net.minecraft.entity.passive.EntityHorse);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSpectatorMode() {
/* 528 */     return (this.currentGameType == WorldSettings.GameType.SPECTATOR);
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldSettings.GameType getCurrentGameType() {
/* 533 */     return this.currentGameType;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getIsHittingBlock() {
/* 538 */     return this.isHittingBlock;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\multiplayer\PlayerControllerMP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */