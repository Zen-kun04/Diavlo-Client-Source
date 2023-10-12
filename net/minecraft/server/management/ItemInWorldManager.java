/*     */ package net.minecraft.server.management;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockChest;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.server.S23PacketBlockChange;
/*     */ import net.minecraft.network.play.server.S38PacketPlayerListItem;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.ILockableContainer;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldServer;
/*     */ import net.minecraft.world.WorldSettings;
/*     */ 
/*     */ 
/*     */ public class ItemInWorldManager
/*     */ {
/*     */   public World theWorld;
/*     */   public EntityPlayerMP thisPlayerMP;
/*  27 */   private WorldSettings.GameType gameType = WorldSettings.GameType.NOT_SET;
/*     */   private boolean isDestroyingBlock;
/*     */   private int initialDamage;
/*  30 */   private BlockPos field_180240_f = BlockPos.ORIGIN;
/*     */   private int curblockDamage;
/*     */   private boolean receivedFinishDiggingPacket;
/*  33 */   private BlockPos field_180241_i = BlockPos.ORIGIN;
/*     */   private int initialBlockDamage;
/*  35 */   private int durabilityRemainingOnBlock = -1;
/*     */ 
/*     */   
/*     */   public ItemInWorldManager(World worldIn) {
/*  39 */     this.theWorld = worldIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setGameType(WorldSettings.GameType type) {
/*  44 */     this.gameType = type;
/*  45 */     type.configurePlayerCapabilities(this.thisPlayerMP.capabilities);
/*  46 */     this.thisPlayerMP.sendPlayerAbilities();
/*  47 */     this.thisPlayerMP.mcServer.getConfigurationManager().sendPacketToAllPlayers((Packet)new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.UPDATE_GAME_MODE, new EntityPlayerMP[] { this.thisPlayerMP }));
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldSettings.GameType getGameType() {
/*  52 */     return this.gameType;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean survivalOrAdventure() {
/*  57 */     return this.gameType.isSurvivalOrAdventure();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCreative() {
/*  62 */     return this.gameType.isCreative();
/*     */   }
/*     */ 
/*     */   
/*     */   public void initializeGameType(WorldSettings.GameType type) {
/*  67 */     if (this.gameType == WorldSettings.GameType.NOT_SET)
/*     */     {
/*  69 */       this.gameType = type;
/*     */     }
/*     */     
/*  72 */     setGameType(this.gameType);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateBlockRemoving() {
/*  77 */     this.curblockDamage++;
/*     */     
/*  79 */     if (this.receivedFinishDiggingPacket) {
/*     */       
/*  81 */       int i = this.curblockDamage - this.initialBlockDamage;
/*  82 */       Block block = this.theWorld.getBlockState(this.field_180241_i).getBlock();
/*     */       
/*  84 */       if (block.getMaterial() == Material.air) {
/*     */         
/*  86 */         this.receivedFinishDiggingPacket = false;
/*     */       }
/*     */       else {
/*     */         
/*  90 */         float f = block.getPlayerRelativeBlockHardness((EntityPlayer)this.thisPlayerMP, this.thisPlayerMP.worldObj, this.field_180241_i) * (i + 1);
/*  91 */         int j = (int)(f * 10.0F);
/*     */         
/*  93 */         if (j != this.durabilityRemainingOnBlock) {
/*     */           
/*  95 */           this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180241_i, j);
/*  96 */           this.durabilityRemainingOnBlock = j;
/*     */         } 
/*     */         
/*  99 */         if (f >= 1.0F)
/*     */         {
/* 101 */           this.receivedFinishDiggingPacket = false;
/* 102 */           tryHarvestBlock(this.field_180241_i);
/*     */         }
/*     */       
/*     */       } 
/* 106 */     } else if (this.isDestroyingBlock) {
/*     */       
/* 108 */       Block block1 = this.theWorld.getBlockState(this.field_180240_f).getBlock();
/*     */       
/* 110 */       if (block1.getMaterial() == Material.air) {
/*     */         
/* 112 */         this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180240_f, -1);
/* 113 */         this.durabilityRemainingOnBlock = -1;
/* 114 */         this.isDestroyingBlock = false;
/*     */       }
/*     */       else {
/*     */         
/* 118 */         int k = this.curblockDamage - this.initialDamage;
/* 119 */         float f1 = block1.getPlayerRelativeBlockHardness((EntityPlayer)this.thisPlayerMP, this.thisPlayerMP.worldObj, this.field_180241_i) * (k + 1);
/* 120 */         int l = (int)(f1 * 10.0F);
/*     */         
/* 122 */         if (l != this.durabilityRemainingOnBlock) {
/*     */           
/* 124 */           this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180240_f, l);
/* 125 */           this.durabilityRemainingOnBlock = l;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockClicked(BlockPos pos, EnumFacing side) {
/* 133 */     if (isCreative()) {
/*     */       
/* 135 */       if (!this.theWorld.extinguishFire((EntityPlayer)null, pos, side))
/*     */       {
/* 137 */         tryHarvestBlock(pos);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 142 */       Block block = this.theWorld.getBlockState(pos).getBlock();
/*     */       
/* 144 */       if (this.gameType.isAdventure()) {
/*     */         
/* 146 */         if (this.gameType == WorldSettings.GameType.SPECTATOR) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/* 151 */         if (!this.thisPlayerMP.isAllowEdit()) {
/*     */           
/* 153 */           ItemStack itemstack = this.thisPlayerMP.getCurrentEquippedItem();
/*     */           
/* 155 */           if (itemstack == null) {
/*     */             return;
/*     */           }
/*     */ 
/*     */           
/* 160 */           if (!itemstack.canDestroy(block)) {
/*     */             return;
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 167 */       this.theWorld.extinguishFire((EntityPlayer)null, pos, side);
/* 168 */       this.initialDamage = this.curblockDamage;
/* 169 */       float f = 1.0F;
/*     */       
/* 171 */       if (block.getMaterial() != Material.air) {
/*     */         
/* 173 */         block.onBlockClicked(this.theWorld, pos, (EntityPlayer)this.thisPlayerMP);
/* 174 */         f = block.getPlayerRelativeBlockHardness((EntityPlayer)this.thisPlayerMP, this.thisPlayerMP.worldObj, pos);
/*     */       } 
/*     */       
/* 177 */       if (block.getMaterial() != Material.air && f >= 1.0F) {
/*     */         
/* 179 */         tryHarvestBlock(pos);
/*     */       }
/*     */       else {
/*     */         
/* 183 */         this.isDestroyingBlock = true;
/* 184 */         this.field_180240_f = pos;
/* 185 */         int i = (int)(f * 10.0F);
/* 186 */         this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), pos, i);
/* 187 */         this.durabilityRemainingOnBlock = i;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void blockRemoving(BlockPos pos) {
/* 194 */     if (pos.equals(this.field_180240_f)) {
/*     */       
/* 196 */       int i = this.curblockDamage - this.initialDamage;
/* 197 */       Block block = this.theWorld.getBlockState(pos).getBlock();
/*     */       
/* 199 */       if (block.getMaterial() != Material.air) {
/*     */         
/* 201 */         float f = block.getPlayerRelativeBlockHardness((EntityPlayer)this.thisPlayerMP, this.thisPlayerMP.worldObj, pos) * (i + 1);
/*     */         
/* 203 */         if (f >= 0.7F) {
/*     */           
/* 205 */           this.isDestroyingBlock = false;
/* 206 */           this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), pos, -1);
/* 207 */           tryHarvestBlock(pos);
/*     */         }
/* 209 */         else if (!this.receivedFinishDiggingPacket) {
/*     */           
/* 211 */           this.isDestroyingBlock = false;
/* 212 */           this.receivedFinishDiggingPacket = true;
/* 213 */           this.field_180241_i = pos;
/* 214 */           this.initialBlockDamage = this.initialDamage;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancelDestroyingBlock() {
/* 222 */     this.isDestroyingBlock = false;
/* 223 */     this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180240_f, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean removeBlock(BlockPos pos) {
/* 228 */     IBlockState iblockstate = this.theWorld.getBlockState(pos);
/* 229 */     iblockstate.getBlock().onBlockHarvested(this.theWorld, pos, iblockstate, (EntityPlayer)this.thisPlayerMP);
/* 230 */     boolean flag = this.theWorld.setBlockToAir(pos);
/*     */     
/* 232 */     if (flag)
/*     */     {
/* 234 */       iblockstate.getBlock().onBlockDestroyedByPlayer(this.theWorld, pos, iblockstate);
/*     */     }
/*     */     
/* 237 */     return flag;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean tryHarvestBlock(BlockPos pos) {
/* 242 */     if (this.gameType.isCreative() && this.thisPlayerMP.getHeldItem() != null && this.thisPlayerMP.getHeldItem().getItem() instanceof net.minecraft.item.ItemSword)
/*     */     {
/* 244 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 248 */     IBlockState iblockstate = this.theWorld.getBlockState(pos);
/* 249 */     TileEntity tileentity = this.theWorld.getTileEntity(pos);
/*     */     
/* 251 */     if (this.gameType.isAdventure()) {
/*     */       
/* 253 */       if (this.gameType == WorldSettings.GameType.SPECTATOR)
/*     */       {
/* 255 */         return false;
/*     */       }
/*     */       
/* 258 */       if (!this.thisPlayerMP.isAllowEdit()) {
/*     */         
/* 260 */         ItemStack itemstack = this.thisPlayerMP.getCurrentEquippedItem();
/*     */         
/* 262 */         if (itemstack == null)
/*     */         {
/* 264 */           return false;
/*     */         }
/*     */         
/* 267 */         if (!itemstack.canDestroy(iblockstate.getBlock()))
/*     */         {
/* 269 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 274 */     this.theWorld.playAuxSFXAtEntity((EntityPlayer)this.thisPlayerMP, 2001, pos, Block.getStateId(iblockstate));
/* 275 */     boolean flag1 = removeBlock(pos);
/*     */     
/* 277 */     if (isCreative()) {
/*     */       
/* 279 */       this.thisPlayerMP.playerNetServerHandler.sendPacket((Packet)new S23PacketBlockChange(this.theWorld, pos));
/*     */     }
/*     */     else {
/*     */       
/* 283 */       ItemStack itemstack1 = this.thisPlayerMP.getCurrentEquippedItem();
/* 284 */       boolean flag = this.thisPlayerMP.canHarvestBlock(iblockstate.getBlock());
/*     */       
/* 286 */       if (itemstack1 != null) {
/*     */         
/* 288 */         itemstack1.onBlockDestroyed(this.theWorld, iblockstate.getBlock(), pos, (EntityPlayer)this.thisPlayerMP);
/*     */         
/* 290 */         if (itemstack1.stackSize == 0)
/*     */         {
/* 292 */           this.thisPlayerMP.destroyCurrentEquippedItem();
/*     */         }
/*     */       } 
/*     */       
/* 296 */       if (flag1 && flag)
/*     */       {
/* 298 */         iblockstate.getBlock().harvestBlock(this.theWorld, (EntityPlayer)this.thisPlayerMP, pos, iblockstate, tileentity);
/*     */       }
/*     */     } 
/*     */     
/* 302 */     return flag1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean tryUseItem(EntityPlayer player, World worldIn, ItemStack stack) {
/* 308 */     if (this.gameType == WorldSettings.GameType.SPECTATOR)
/*     */     {
/* 310 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 314 */     int i = stack.stackSize;
/* 315 */     int j = stack.getMetadata();
/* 316 */     ItemStack itemstack = stack.useItemRightClick(worldIn, player);
/*     */     
/* 318 */     if (itemstack != stack || (itemstack != null && (itemstack.stackSize != i || itemstack.getMaxItemUseDuration() > 0 || itemstack.getMetadata() != j))) {
/*     */       
/* 320 */       player.inventory.mainInventory[player.inventory.currentItem] = itemstack;
/*     */       
/* 322 */       if (isCreative()) {
/*     */         
/* 324 */         itemstack.stackSize = i;
/*     */         
/* 326 */         if (itemstack.isItemStackDamageable())
/*     */         {
/* 328 */           itemstack.setItemDamage(j);
/*     */         }
/*     */       } 
/*     */       
/* 332 */       if (itemstack.stackSize == 0)
/*     */       {
/* 334 */         player.inventory.mainInventory[player.inventory.currentItem] = null;
/*     */       }
/*     */       
/* 337 */       if (!player.isUsingItem())
/*     */       {
/* 339 */         ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
/*     */       }
/*     */       
/* 342 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 346 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean activateBlockOrUseItem(EntityPlayer player, World worldIn, ItemStack stack, BlockPos pos, EnumFacing side, float offsetX, float offsetY, float offsetZ) {
/* 353 */     if (this.gameType == WorldSettings.GameType.SPECTATOR) {
/*     */       
/* 355 */       TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */       
/* 357 */       if (tileentity instanceof ILockableContainer) {
/*     */         
/* 359 */         Block block = worldIn.getBlockState(pos).getBlock();
/* 360 */         ILockableContainer ilockablecontainer = (ILockableContainer)tileentity;
/*     */         
/* 362 */         if (ilockablecontainer instanceof net.minecraft.tileentity.TileEntityChest && block instanceof BlockChest)
/*     */         {
/* 364 */           ilockablecontainer = ((BlockChest)block).getLockableContainer(worldIn, pos);
/*     */         }
/*     */         
/* 367 */         if (ilockablecontainer != null)
/*     */         {
/* 369 */           player.displayGUIChest((IInventory)ilockablecontainer);
/* 370 */           return true;
/*     */         }
/*     */       
/* 373 */       } else if (tileentity instanceof IInventory) {
/*     */         
/* 375 */         player.displayGUIChest((IInventory)tileentity);
/* 376 */         return true;
/*     */       } 
/*     */       
/* 379 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 383 */     if (!player.isSneaking() || player.getHeldItem() == null) {
/*     */       
/* 385 */       IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */       
/* 387 */       if (iblockstate.getBlock().onBlockActivated(worldIn, pos, iblockstate, player, side, offsetX, offsetY, offsetZ))
/*     */       {
/* 389 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 393 */     if (stack == null)
/*     */     {
/* 395 */       return false;
/*     */     }
/* 397 */     if (isCreative()) {
/*     */       
/* 399 */       int j = stack.getMetadata();
/* 400 */       int i = stack.stackSize;
/* 401 */       boolean flag = stack.onItemUse(player, worldIn, pos, side, offsetX, offsetY, offsetZ);
/* 402 */       stack.setItemDamage(j);
/* 403 */       stack.stackSize = i;
/* 404 */       return flag;
/*     */     } 
/*     */ 
/*     */     
/* 408 */     return stack.onItemUse(player, worldIn, pos, side, offsetX, offsetY, offsetZ);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWorld(WorldServer serverWorld) {
/* 415 */     this.theWorld = (World)serverWorld;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\server\management\ItemInWorldManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */