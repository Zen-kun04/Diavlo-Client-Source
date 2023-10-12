/*    */ package net.minecraft.client.main;
/*    */ 
/*    */ import com.mojang.authlib.properties.PropertyMap;
/*    */ import java.io.File;
/*    */ import java.net.Proxy;
/*    */ import net.minecraft.util.Session;
/*    */ 
/*    */ 
/*    */ public class GameConfiguration
/*    */ {
/*    */   public final UserInformation userInfo;
/*    */   public final DisplayInformation displayInfo;
/*    */   public final FolderInformation folderInfo;
/*    */   public final GameInformation gameInfo;
/*    */   public final ServerInformation serverInfo;
/*    */   
/*    */   public GameConfiguration(UserInformation userInfoIn, DisplayInformation displayInfoIn, FolderInformation folderInfoIn, GameInformation gameInfoIn, ServerInformation serverInfoIn) {
/* 18 */     this.userInfo = userInfoIn;
/* 19 */     this.displayInfo = displayInfoIn;
/* 20 */     this.folderInfo = folderInfoIn;
/* 21 */     this.gameInfo = gameInfoIn;
/* 22 */     this.serverInfo = serverInfoIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public static class DisplayInformation
/*    */   {
/*    */     public final int width;
/*    */     public final int height;
/*    */     public final boolean fullscreen;
/*    */     public final boolean checkGlErrors;
/*    */     
/*    */     public DisplayInformation(int widthIn, int heightIn, boolean fullscreenIn, boolean checkGlErrorsIn) {
/* 34 */       this.width = widthIn;
/* 35 */       this.height = heightIn;
/* 36 */       this.fullscreen = fullscreenIn;
/* 37 */       this.checkGlErrors = checkGlErrorsIn;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public static class FolderInformation
/*    */   {
/*    */     public final File mcDataDir;
/*    */     public final File resourcePacksDir;
/*    */     public final File assetsDir;
/*    */     public final String assetIndex;
/*    */     
/*    */     public FolderInformation(File mcDataDirIn, File resourcePacksDirIn, File assetsDirIn, String assetIndexIn) {
/* 50 */       this.mcDataDir = mcDataDirIn;
/* 51 */       this.resourcePacksDir = resourcePacksDirIn;
/* 52 */       this.assetsDir = assetsDirIn;
/* 53 */       this.assetIndex = assetIndexIn;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public static class GameInformation
/*    */   {
/*    */     public final boolean isDemo;
/*    */     public final String version;
/*    */     
/*    */     public GameInformation(boolean isDemoIn, String versionIn) {
/* 64 */       this.isDemo = isDemoIn;
/* 65 */       this.version = versionIn;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public static class ServerInformation
/*    */   {
/*    */     public final String serverName;
/*    */     public final int serverPort;
/*    */     
/*    */     public ServerInformation(String serverNameIn, int serverPortIn) {
/* 76 */       this.serverName = serverNameIn;
/* 77 */       this.serverPort = serverPortIn;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public static class UserInformation
/*    */   {
/*    */     public final Session session;
/*    */     public final PropertyMap userProperties;
/*    */     public final PropertyMap profileProperties;
/*    */     public final Proxy proxy;
/*    */     
/*    */     public UserInformation(Session sessionIn, PropertyMap userPropertiesIn, PropertyMap profilePropertiesIn, Proxy proxyIn) {
/* 90 */       this.session = sessionIn;
/* 91 */       this.userProperties = userPropertiesIn;
/* 92 */       this.profileProperties = profilePropertiesIn;
/* 93 */       this.proxy = proxyIn;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\main\GameConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */