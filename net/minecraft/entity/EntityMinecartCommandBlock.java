/*     */ package net.minecraft.entity;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.command.server.CommandBlockLogic;
/*     */ import net.minecraft.entity.item.EntityMinecart;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityMinecartCommandBlock
/*     */   extends EntityMinecart {
/*  17 */   private final CommandBlockLogic commandBlockLogic = new CommandBlockLogic()
/*     */     {
/*     */       public void updateCommand()
/*     */       {
/*  21 */         EntityMinecartCommandBlock.this.getDataWatcher().updateObject(23, getCommand());
/*  22 */         EntityMinecartCommandBlock.this.getDataWatcher().updateObject(24, IChatComponent.Serializer.componentToJson(getLastOutput()));
/*     */       }
/*     */       
/*     */       public int func_145751_f() {
/*  26 */         return 1;
/*     */       }
/*     */       
/*     */       public void func_145757_a(ByteBuf p_145757_1_) {
/*  30 */         p_145757_1_.writeInt(EntityMinecartCommandBlock.this.getEntityId());
/*     */       }
/*     */       
/*     */       public BlockPos getPosition() {
/*  34 */         return new BlockPos(EntityMinecartCommandBlock.this.posX, EntityMinecartCommandBlock.this.posY + 0.5D, EntityMinecartCommandBlock.this.posZ);
/*     */       }
/*     */       
/*     */       public Vec3 getPositionVector() {
/*  38 */         return new Vec3(EntityMinecartCommandBlock.this.posX, EntityMinecartCommandBlock.this.posY, EntityMinecartCommandBlock.this.posZ);
/*     */       }
/*     */       
/*     */       public World getEntityWorld() {
/*  42 */         return EntityMinecartCommandBlock.this.worldObj;
/*     */       }
/*     */       
/*     */       public Entity getCommandSenderEntity() {
/*  46 */         return (Entity)EntityMinecartCommandBlock.this;
/*     */       }
/*     */     };
/*  49 */   private int activatorRailCooldown = 0;
/*     */ 
/*     */   
/*     */   public EntityMinecartCommandBlock(World worldIn) {
/*  53 */     super(worldIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityMinecartCommandBlock(World worldIn, double x, double y, double z) {
/*  58 */     super(worldIn, x, y, z);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  63 */     super.entityInit();
/*  64 */     getDataWatcher().addObject(23, "");
/*  65 */     getDataWatcher().addObject(24, "");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readEntityFromNBT(NBTTagCompound tagCompund) {
/*  70 */     super.readEntityFromNBT(tagCompund);
/*  71 */     this.commandBlockLogic.readDataFromNBT(tagCompund);
/*  72 */     getDataWatcher().updateObject(23, getCommandBlockLogic().getCommand());
/*  73 */     getDataWatcher().updateObject(24, IChatComponent.Serializer.componentToJson(getCommandBlockLogic().getLastOutput()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeEntityToNBT(NBTTagCompound tagCompound) {
/*  78 */     super.writeEntityToNBT(tagCompound);
/*  79 */     this.commandBlockLogic.writeDataToNBT(tagCompound);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityMinecart.EnumMinecartType getMinecartType() {
/*  84 */     return EntityMinecart.EnumMinecartType.COMMAND_BLOCK;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getDefaultDisplayTile() {
/*  89 */     return Blocks.command_block.getDefaultState();
/*     */   }
/*     */ 
/*     */   
/*     */   public CommandBlockLogic getCommandBlockLogic() {
/*  94 */     return this.commandBlockLogic;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onActivatorRailPass(int x, int y, int z, boolean receivingPower) {
/*  99 */     if (receivingPower && this.ticksExisted - this.activatorRailCooldown >= 4) {
/*     */       
/* 101 */       getCommandBlockLogic().trigger(this.worldObj);
/* 102 */       this.activatorRailCooldown = this.ticksExisted;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean interactFirst(EntityPlayer playerIn) {
/* 108 */     this.commandBlockLogic.tryOpenEditCommandBlock(playerIn);
/* 109 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDataWatcherUpdate(int dataID) {
/* 114 */     super.onDataWatcherUpdate(dataID);
/*     */     
/* 116 */     if (dataID == 24) {
/*     */ 
/*     */       
/*     */       try {
/* 120 */         this.commandBlockLogic.setLastOutput(IChatComponent.Serializer.jsonToComponent(getDataWatcher().getWatchableObjectString(24)));
/*     */       }
/* 122 */       catch (Throwable throwable) {}
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 127 */     else if (dataID == 23) {
/*     */       
/* 129 */       this.commandBlockLogic.setCommand(getDataWatcher().getWatchableObjectString(23));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\EntityMinecartCommandBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */