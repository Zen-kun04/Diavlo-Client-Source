/*     */ package net.minecraft.client.audio;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ 
/*     */ public class SoundList
/*     */ {
/*   8 */   private final List<SoundEntry> soundList = Lists.newArrayList();
/*     */   
/*     */   private boolean replaceExisting;
/*     */   private SoundCategory category;
/*     */   
/*     */   public List<SoundEntry> getSoundList() {
/*  14 */     return this.soundList;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canReplaceExisting() {
/*  19 */     return this.replaceExisting;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setReplaceExisting(boolean p_148572_1_) {
/*  24 */     this.replaceExisting = p_148572_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundCategory getSoundCategory() {
/*  29 */     return this.category;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSoundCategory(SoundCategory soundCat) {
/*  34 */     this.category = soundCat;
/*     */   }
/*     */   
/*     */   public static class SoundEntry
/*     */   {
/*     */     private String name;
/*  40 */     private float volume = 1.0F;
/*  41 */     private float pitch = 1.0F;
/*  42 */     private int weight = 1;
/*  43 */     private Type type = Type.FILE;
/*     */     
/*     */     private boolean streaming = false;
/*     */     
/*     */     public String getSoundEntryName() {
/*  48 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSoundEntryName(String nameIn) {
/*  53 */       this.name = nameIn;
/*     */     }
/*     */ 
/*     */     
/*     */     public float getSoundEntryVolume() {
/*  58 */       return this.volume;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSoundEntryVolume(float volumeIn) {
/*  63 */       this.volume = volumeIn;
/*     */     }
/*     */ 
/*     */     
/*     */     public float getSoundEntryPitch() {
/*  68 */       return this.pitch;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSoundEntryPitch(float pitchIn) {
/*  73 */       this.pitch = pitchIn;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getSoundEntryWeight() {
/*  78 */       return this.weight;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSoundEntryWeight(int weightIn) {
/*  83 */       this.weight = weightIn;
/*     */     }
/*     */ 
/*     */     
/*     */     public Type getSoundEntryType() {
/*  88 */       return this.type;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSoundEntryType(Type typeIn) {
/*  93 */       this.type = typeIn;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isStreaming() {
/*  98 */       return this.streaming;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setStreaming(boolean isStreaming) {
/* 103 */       this.streaming = isStreaming;
/*     */     }
/*     */     
/*     */     public enum Type
/*     */     {
/* 108 */       FILE("file"),
/* 109 */       SOUND_EVENT("event");
/*     */       
/*     */       private final String field_148583_c;
/*     */ 
/*     */       
/*     */       Type(String p_i45109_3_) {
/* 115 */         this.field_148583_c = p_i45109_3_;
/*     */       }
/*     */ 
/*     */       
/*     */       public static Type getType(String p_148580_0_) {
/* 120 */         for (Type soundlist$soundentry$type : values()) {
/*     */           
/* 122 */           if (soundlist$soundentry$type.field_148583_c.equals(p_148580_0_))
/*     */           {
/* 124 */             return soundlist$soundentry$type;
/*     */           }
/*     */         } 
/*     */         
/* 128 */         return null;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\audio\SoundList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */