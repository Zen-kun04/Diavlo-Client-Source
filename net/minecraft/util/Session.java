/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import com.mojang.util.UUIDTypeAdapter;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Session
/*    */ {
/*    */   private final String username;
/*    */   private final String playerID;
/*    */   private final String token;
/*    */   private final Type sessionType;
/*    */   
/*    */   public Session(String usernameIn, String playerIDIn, String tokenIn, String sessionTypeIn) {
/* 19 */     this.username = usernameIn;
/* 20 */     this.playerID = playerIDIn;
/* 21 */     this.token = tokenIn;
/* 22 */     this.sessionType = Type.setSessionType(sessionTypeIn);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSessionID() {
/* 27 */     return "token:" + this.token + ":" + this.playerID;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getPlayerID() {
/* 32 */     return this.playerID;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUsername() {
/* 37 */     return this.username;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getToken() {
/* 42 */     return this.token;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public GameProfile getProfile() {
/*    */     try {
/* 49 */       UUID uuid = UUIDTypeAdapter.fromString(getPlayerID());
/* 50 */       return new GameProfile(uuid, getUsername());
/*    */     }
/* 52 */     catch (IllegalArgumentException var2) {
/*    */       
/* 54 */       return new GameProfile((UUID)null, getUsername());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Type getSessionType() {
/* 60 */     return this.sessionType;
/*    */   }
/*    */   
/*    */   public enum Type
/*    */   {
/* 65 */     LEGACY("legacy"),
/* 66 */     MOJANG("mojang");
/*    */     
/* 68 */     private static final Map<String, Type> SESSION_TYPES = Maps.newHashMap();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     private final String sessionType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     static {
/* 82 */       for (Type session$type : values())
/*    */       {
/* 84 */         SESSION_TYPES.put(session$type.sessionType, session$type);
/*    */       }
/*    */     }
/*    */     
/*    */     Type(String sessionTypeIn) {
/*    */       this.sessionType = sessionTypeIn;
/*    */     }
/*    */     
/*    */     public static Type setSessionType(String sessionTypeIn) {
/*    */       return SESSION_TYPES.get(sessionTypeIn.toLowerCase());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\Session.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */