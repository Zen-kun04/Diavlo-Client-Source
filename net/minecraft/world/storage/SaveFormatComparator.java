/*    */ package net.minecraft.world.storage;
/*    */ 
/*    */ import net.minecraft.world.WorldSettings;
/*    */ 
/*    */ public class SaveFormatComparator
/*    */   implements Comparable<SaveFormatComparator>
/*    */ {
/*    */   private final String fileName;
/*    */   private final String displayName;
/*    */   private final long lastTimePlayed;
/*    */   private final long sizeOnDisk;
/*    */   private final boolean requiresConversion;
/*    */   private final WorldSettings.GameType theEnumGameType;
/*    */   private final boolean hardcore;
/*    */   private final boolean cheatsEnabled;
/*    */   
/*    */   public SaveFormatComparator(String fileNameIn, String displayNameIn, long lastTimePlayedIn, long sizeOnDiskIn, WorldSettings.GameType theEnumGameTypeIn, boolean requiresConversionIn, boolean hardcoreIn, boolean cheatsEnabledIn) {
/* 18 */     this.fileName = fileNameIn;
/* 19 */     this.displayName = displayNameIn;
/* 20 */     this.lastTimePlayed = lastTimePlayedIn;
/* 21 */     this.sizeOnDisk = sizeOnDiskIn;
/* 22 */     this.theEnumGameType = theEnumGameTypeIn;
/* 23 */     this.requiresConversion = requiresConversionIn;
/* 24 */     this.hardcore = hardcoreIn;
/* 25 */     this.cheatsEnabled = cheatsEnabledIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getFileName() {
/* 30 */     return this.fileName;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDisplayName() {
/* 35 */     return this.displayName;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getSizeOnDisk() {
/* 40 */     return this.sizeOnDisk;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean requiresConversion() {
/* 45 */     return this.requiresConversion;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getLastTimePlayed() {
/* 50 */     return this.lastTimePlayed;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareTo(SaveFormatComparator p_compareTo_1_) {
/* 55 */     return (this.lastTimePlayed < p_compareTo_1_.lastTimePlayed) ? 1 : ((this.lastTimePlayed > p_compareTo_1_.lastTimePlayed) ? -1 : this.fileName.compareTo(p_compareTo_1_.fileName));
/*    */   }
/*    */ 
/*    */   
/*    */   public WorldSettings.GameType getEnumGameType() {
/* 60 */     return this.theEnumGameType;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isHardcoreModeEnabled() {
/* 65 */     return this.hardcore;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean getCheatsEnabled() {
/* 70 */     return this.cheatsEnabled;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\storage\SaveFormatComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */