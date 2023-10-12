/*    */ package com.viaversion.viaversion.sponge.platform;
/*    */ 
/*    */ import com.viaversion.viaversion.platform.LegacyViaInjector;
/*    */ import com.viaversion.viaversion.platform.WrappedChannelInitializer;
/*    */ import com.viaversion.viaversion.sponge.handlers.SpongeChannelInitializer;
/*    */ import io.netty.channel.Channel;
/*    */ import io.netty.channel.ChannelHandler;
/*    */ import io.netty.channel.ChannelInitializer;
/*    */ import org.spongepowered.api.MinecraftVersion;
/*    */ import org.spongepowered.api.Sponge;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SpongeViaInjector
/*    */   extends LegacyViaInjector
/*    */ {
/*    */   public int getServerProtocolVersion() throws ReflectiveOperationException {
/* 34 */     MinecraftVersion version = Sponge.platform().minecraftVersion();
/*    */ 
/*    */     
/*    */     try {
/* 38 */       return ((Integer)version.getClass().getDeclaredMethod("getProtocol", new Class[0]).invoke(version, new Object[0])).intValue();
/* 39 */     } catch (NoSuchMethodException e) {
/* 40 */       return ((Integer)version.getClass().getDeclaredMethod("protocolVersion", new Class[0]).invoke(version, new Object[0])).intValue();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected Object getServerConnection() throws ReflectiveOperationException {
/* 46 */     Class<?> serverClazz = Class.forName("net.minecraft.server.MinecraftServer");
/* 47 */     return serverClazz.getDeclaredMethod("getConnection", new Class[0]).invoke(Sponge.server(), new Object[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   protected WrappedChannelInitializer createChannelInitializer(ChannelInitializer<Channel> oldInitializer) {
/* 52 */     return (WrappedChannelInitializer)new SpongeChannelInitializer(oldInitializer);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void blame(ChannelHandler bootstrapAcceptor) {
/* 57 */     throw new RuntimeException("Unable to find core component 'childHandler', please check your plugins. Issue: " + bootstrapAcceptor.getClass().getName());
/*    */   }
/*    */ 
/*    */   
/*    */   public String getEncoderName() {
/* 62 */     return "encoder";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDecoderName() {
/* 67 */     return "decoder";
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\sponge\platform\SpongeViaInjector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */