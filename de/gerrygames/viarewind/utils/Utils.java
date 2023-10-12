/*    */ package de.gerrygames.viarewind.utils;
/*    */ 
/*    */ import com.viaversion.viaversion.api.connection.UserConnection;
/*    */ import java.util.UUID;
/*    */ 
/*    */ 
/*    */ public class Utils
/*    */ {
/*    */   public static UUID getUUID(UserConnection user) {
/* 10 */     return user.getProtocolInfo().getUuid();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\de\gerrygames\viarewin\\utils\Utils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */