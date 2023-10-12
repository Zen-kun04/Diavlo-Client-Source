/*    */ package de.gerrygames.viarewind.api;
/*    */ 
/*    */ import com.viaversion.viaversion.api.Via;
/*    */ import com.viaversion.viaversion.api.protocol.Protocol;
/*    */ import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
/*    */ import de.gerrygames.viarewind.ViaRewind;
/*    */ import de.gerrygames.viarewind.protocol.protocol1_7_0_5to1_7_6_10.Protocol1_7_0_5to1_7_6_10;
/*    */ import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.Protocol1_7_6_10TO1_8;
/*    */ import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ public interface ViaRewindPlatform {
/*    */   default void init(ViaRewindConfig config) {
/* 14 */     ViaRewind.init(this, config);
/*    */     
/* 16 */     String version = ViaRewind.class.getPackage().getImplementationVersion();
/* 17 */     Via.getManager().getSubPlatforms().add((version != null) ? version : "UNKNOWN");
/*    */     
/* 19 */     Via.getManager().getProtocolManager().registerProtocol((Protocol)new Protocol1_8TO1_9(), ProtocolVersion.v1_8, ProtocolVersion.v1_9);
/* 20 */     Via.getManager().getProtocolManager().registerProtocol((Protocol)new Protocol1_7_6_10TO1_8(), ProtocolVersion.v1_7_6, ProtocolVersion.v1_8);
/* 21 */     Via.getManager().getProtocolManager().registerProtocol((Protocol)new Protocol1_7_0_5to1_7_6_10(), ProtocolVersion.v1_7_1, ProtocolVersion.v1_7_6);
/*    */   }
/*    */   
/*    */   Logger getLogger();
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\de\gerrygames\viarewind\api\ViaRewindPlatform.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */