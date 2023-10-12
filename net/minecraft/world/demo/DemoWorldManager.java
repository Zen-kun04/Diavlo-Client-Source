/*     */ package net.minecraft.world.demo;
/*     */ 
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.server.S2BPacketChangeGameState;
/*     */ import net.minecraft.server.management.ItemInWorldManager;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class DemoWorldManager extends ItemInWorldManager {
/*     */   private boolean field_73105_c;
/*     */   private boolean demoTimeExpired;
/*     */   private int field_73104_e;
/*     */   private int field_73102_f;
/*     */   
/*     */   public DemoWorldManager(World worldIn) {
/*  21 */     super(worldIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateBlockRemoving() {
/*  26 */     super.updateBlockRemoving();
/*  27 */     this.field_73102_f++;
/*  28 */     long i = this.theWorld.getTotalWorldTime();
/*  29 */     long j = i / 24000L + 1L;
/*     */     
/*  31 */     if (!this.field_73105_c && this.field_73102_f > 20) {
/*     */       
/*  33 */       this.field_73105_c = true;
/*  34 */       this.thisPlayerMP.playerNetServerHandler.sendPacket((Packet)new S2BPacketChangeGameState(5, 0.0F));
/*     */     } 
/*     */     
/*  37 */     this.demoTimeExpired = (i > 120500L);
/*     */     
/*  39 */     if (this.demoTimeExpired)
/*     */     {
/*  41 */       this.field_73104_e++;
/*     */     }
/*     */     
/*  44 */     if (i % 24000L == 500L) {
/*     */       
/*  46 */       if (j <= 6L)
/*     */       {
/*  48 */         this.thisPlayerMP.addChatMessage((IChatComponent)new ChatComponentTranslation("demo.day." + j, new Object[0]));
/*     */       }
/*     */     }
/*  51 */     else if (j == 1L) {
/*     */       
/*  53 */       if (i == 100L)
/*     */       {
/*  55 */         this.thisPlayerMP.playerNetServerHandler.sendPacket((Packet)new S2BPacketChangeGameState(5, 101.0F));
/*     */       }
/*  57 */       else if (i == 175L)
/*     */       {
/*  59 */         this.thisPlayerMP.playerNetServerHandler.sendPacket((Packet)new S2BPacketChangeGameState(5, 102.0F));
/*     */       }
/*  61 */       else if (i == 250L)
/*     */       {
/*  63 */         this.thisPlayerMP.playerNetServerHandler.sendPacket((Packet)new S2BPacketChangeGameState(5, 103.0F));
/*     */       }
/*     */     
/*  66 */     } else if (j == 5L && i % 24000L == 22000L) {
/*     */       
/*  68 */       this.thisPlayerMP.addChatMessage((IChatComponent)new ChatComponentTranslation("demo.day.warning", new Object[0]));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void sendDemoReminder() {
/*  74 */     if (this.field_73104_e > 100) {
/*     */       
/*  76 */       this.thisPlayerMP.addChatMessage((IChatComponent)new ChatComponentTranslation("demo.reminder", new Object[0]));
/*  77 */       this.field_73104_e = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockClicked(BlockPos pos, EnumFacing side) {
/*  83 */     if (this.demoTimeExpired) {
/*     */       
/*  85 */       sendDemoReminder();
/*     */     }
/*     */     else {
/*     */       
/*  89 */       super.onBlockClicked(pos, side);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void blockRemoving(BlockPos pos) {
/*  95 */     if (!this.demoTimeExpired)
/*     */     {
/*  97 */       super.blockRemoving(pos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean tryHarvestBlock(BlockPos pos) {
/* 103 */     return this.demoTimeExpired ? false : super.tryHarvestBlock(pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean tryUseItem(EntityPlayer player, World worldIn, ItemStack stack) {
/* 108 */     if (this.demoTimeExpired) {
/*     */       
/* 110 */       sendDemoReminder();
/* 111 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 115 */     return super.tryUseItem(player, worldIn, stack);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean activateBlockOrUseItem(EntityPlayer player, World worldIn, ItemStack stack, BlockPos pos, EnumFacing side, float offsetX, float offsetY, float offsetZ) {
/* 121 */     if (this.demoTimeExpired) {
/*     */       
/* 123 */       sendDemoReminder();
/* 124 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 128 */     return super.activateBlockOrUseItem(player, worldIn, stack, pos, side, offsetX, offsetY, offsetZ);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\demo\DemoWorldManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */