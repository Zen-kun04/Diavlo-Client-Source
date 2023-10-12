/*    */ package net.minecraft.server.management;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.io.File;
/*    */ 
/*    */ public class UserListOps
/*    */   extends UserList<GameProfile, UserListOpsEntry>
/*    */ {
/*    */   public UserListOps(File saveFile) {
/* 11 */     super(saveFile);
/*    */   }
/*    */ 
/*    */   
/*    */   protected UserListEntry<GameProfile> createEntry(JsonObject entryData) {
/* 16 */     return new UserListOpsEntry(entryData);
/*    */   }
/*    */ 
/*    */   
/*    */   public String[] getKeys() {
/* 21 */     String[] astring = new String[getValues().size()];
/* 22 */     int i = 0;
/*    */     
/* 24 */     for (UserListOpsEntry userlistopsentry : getValues().values())
/*    */     {
/* 26 */       astring[i++] = userlistopsentry.getValue().getName();
/*    */     }
/*    */     
/* 29 */     return astring;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean bypassesPlayerLimit(GameProfile profile) {
/* 34 */     UserListOpsEntry userlistopsentry = getEntry(profile);
/* 35 */     return (userlistopsentry != null) ? userlistopsentry.bypassesPlayerLimit() : false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getObjectKey(GameProfile obj) {
/* 40 */     return obj.getId().toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public GameProfile getGameProfileFromName(String username) {
/* 45 */     for (UserListOpsEntry userlistopsentry : getValues().values()) {
/*    */       
/* 47 */       if (username.equalsIgnoreCase(userlistopsentry.getValue().getName()))
/*    */       {
/* 49 */         return userlistopsentry.getValue();
/*    */       }
/*    */     } 
/*    */     
/* 53 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\server\management\UserListOps.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */