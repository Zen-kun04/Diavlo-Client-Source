/*     */ package com.viaversion.viaversion.bukkit.providers;
/*     */ 
/*     */ import com.viaversion.viaversion.api.Via;
/*     */ import com.viaversion.viaversion.api.connection.UserConnection;
/*     */ import com.viaversion.viaversion.bukkit.util.NMSUtil;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.MovementTransmitterProvider;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.MovementTracker;
/*     */ import com.viaversion.viaversion.util.PipelineUtil;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.entity.Player;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BukkitViaMovementTransmitter
/*     */   extends MovementTransmitterProvider
/*     */ {
/*     */   private static boolean USE_NMS = true;
/*     */   private Object idlePacket;
/*     */   private Object idlePacket2;
/*     */   private Method getHandle;
/*     */   private Field connection;
/*     */   private Method handleFlying;
/*     */   
/*     */   public BukkitViaMovementTransmitter() {
/*     */     Class<?> idlePacketClass;
/*  45 */     USE_NMS = Via.getConfig().isNMSPlayerTicking();
/*     */ 
/*     */     
/*     */     try {
/*  49 */       idlePacketClass = NMSUtil.nms("PacketPlayInFlying");
/*  50 */     } catch (ClassNotFoundException e) {
/*     */       return;
/*     */     } 
/*     */     try {
/*  54 */       this.idlePacket = idlePacketClass.newInstance();
/*  55 */       this.idlePacket2 = idlePacketClass.newInstance();
/*     */       
/*  57 */       Field flying = idlePacketClass.getDeclaredField("f");
/*  58 */       flying.setAccessible(true);
/*     */       
/*  60 */       flying.set(this.idlePacket2, Boolean.valueOf(true));
/*  61 */     } catch (NoSuchFieldException|InstantiationException|IllegalArgumentException|IllegalAccessException e) {
/*  62 */       throw new RuntimeException("Couldn't make player idle packet, help!", e);
/*     */     } 
/*  64 */     if (USE_NMS) {
/*     */       try {
/*  66 */         this.getHandle = NMSUtil.obc("entity.CraftPlayer").getDeclaredMethod("getHandle", new Class[0]);
/*  67 */       } catch (NoSuchMethodException|ClassNotFoundException e) {
/*  68 */         throw new RuntimeException("Couldn't find CraftPlayer", e);
/*     */       } 
/*     */       
/*     */       try {
/*  72 */         this.connection = NMSUtil.nms("EntityPlayer").getDeclaredField("playerConnection");
/*  73 */       } catch (NoSuchFieldException|ClassNotFoundException e) {
/*  74 */         throw new RuntimeException("Couldn't find Player Connection", e);
/*     */       } 
/*     */       
/*     */       try {
/*  78 */         this.handleFlying = NMSUtil.nms("PlayerConnection").getDeclaredMethod("a", new Class[] { idlePacketClass });
/*  79 */       } catch (NoSuchMethodException|ClassNotFoundException e) {
/*  80 */         throw new RuntimeException("Couldn't find CraftPlayer", e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object getFlyingPacket() {
/*  86 */     if (this.idlePacket == null) throw new NullPointerException("Could not locate flying packet");
/*     */     
/*  88 */     return this.idlePacket;
/*     */   }
/*     */   
/*     */   public Object getGroundPacket() {
/*  92 */     if (this.idlePacket == null) throw new NullPointerException("Could not locate flying packet");
/*     */     
/*  94 */     return this.idlePacket2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendPlayer(UserConnection info) {
/*  99 */     if (USE_NMS) {
/* 100 */       Player player = Bukkit.getPlayer(info.getProtocolInfo().getUuid());
/* 101 */       if (player != null) {
/*     */         
/*     */         try {
/* 104 */           Object entityPlayer = this.getHandle.invoke(player, new Object[0]);
/* 105 */           Object pc = this.connection.get(entityPlayer);
/* 106 */           if (pc != null) {
/* 107 */             this.handleFlying.invoke(pc, new Object[] { ((MovementTracker)info.get(MovementTracker.class)).isGround() ? this.idlePacket2 : this.idlePacket });
/*     */             
/* 109 */             ((MovementTracker)info.get(MovementTracker.class)).incrementIdlePacket();
/*     */           } 
/* 111 */         } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
/* 112 */           e.printStackTrace();
/*     */         } 
/*     */       }
/*     */     } else {
/* 116 */       ChannelHandlerContext context = PipelineUtil.getContextBefore("decoder", info.getChannel().pipeline());
/* 117 */       if (context != null) {
/* 118 */         if (((MovementTracker)info.get(MovementTracker.class)).isGround()) {
/* 119 */           context.fireChannelRead(getGroundPacket());
/*     */         } else {
/* 121 */           context.fireChannelRead(getFlyingPacket());
/*     */         } 
/* 123 */         ((MovementTracker)info.get(MovementTracker.class)).incrementIdlePacket();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\bukkit\providers\BukkitViaMovementTransmitter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */