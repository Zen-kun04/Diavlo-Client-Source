/*    */ package net.minecraft.client.audio;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.util.ITickable;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class MusicTicker
/*    */   implements ITickable {
/* 11 */   private final Random rand = new Random();
/*    */   private final Minecraft mc;
/*    */   private ISound currentMusic;
/* 14 */   private int timeUntilNextMusic = 100;
/*    */ 
/*    */   
/*    */   public MusicTicker(Minecraft mcIn) {
/* 18 */     this.mc = mcIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void update() {
/* 23 */     MusicType musicticker$musictype = this.mc.getAmbientMusicType();
/*    */     
/* 25 */     if (this.currentMusic != null) {
/*    */       
/* 27 */       if (!musicticker$musictype.getMusicLocation().equals(this.currentMusic.getSoundLocation())) {
/*    */         
/* 29 */         this.mc.getSoundHandler().stopSound(this.currentMusic);
/* 30 */         this.timeUntilNextMusic = MathHelper.getRandomIntegerInRange(this.rand, 0, musicticker$musictype.getMinDelay() / 2);
/*    */       } 
/*    */       
/* 33 */       if (!this.mc.getSoundHandler().isSoundPlaying(this.currentMusic)) {
/*    */         
/* 35 */         this.currentMusic = null;
/* 36 */         this.timeUntilNextMusic = Math.min(MathHelper.getRandomIntegerInRange(this.rand, musicticker$musictype.getMinDelay(), musicticker$musictype.getMaxDelay()), this.timeUntilNextMusic);
/*    */       } 
/*    */     } 
/*    */     
/* 40 */     if (this.currentMusic == null && this.timeUntilNextMusic-- <= 0)
/*    */     {
/* 42 */       func_181558_a(musicticker$musictype);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_181558_a(MusicType p_181558_1_) {
/* 48 */     this.currentMusic = PositionedSoundRecord.create(p_181558_1_.getMusicLocation());
/* 49 */     this.mc.getSoundHandler().playSound(this.currentMusic);
/* 50 */     this.timeUntilNextMusic = Integer.MAX_VALUE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_181557_a() {
/* 55 */     if (this.currentMusic != null) {
/*    */       
/* 57 */       this.mc.getSoundHandler().stopSound(this.currentMusic);
/* 58 */       this.currentMusic = null;
/* 59 */       this.timeUntilNextMusic = 0;
/*    */     } 
/*    */   }
/*    */   
/*    */   public enum MusicType
/*    */   {
/* 65 */     MENU((String)new ResourceLocation("minecraft:music.menu"), 20, 600),
/* 66 */     GAME((String)new ResourceLocation("minecraft:music.game"), 12000, 24000),
/* 67 */     CREATIVE((String)new ResourceLocation("minecraft:music.game.creative"), 1200, 3600),
/* 68 */     CREDITS((String)new ResourceLocation("minecraft:music.game.end.credits"), 2147483647, 2147483647),
/* 69 */     NETHER((String)new ResourceLocation("minecraft:music.game.nether"), 1200, 3600),
/* 70 */     END_BOSS((String)new ResourceLocation("minecraft:music.game.end.dragon"), 0, 0),
/* 71 */     END((String)new ResourceLocation("minecraft:music.game.end"), 6000, 24000);
/*    */     
/*    */     private final ResourceLocation musicLocation;
/*    */     
/*    */     private final int minDelay;
/*    */     private final int maxDelay;
/*    */     
/*    */     MusicType(ResourceLocation location, int minDelayIn, int maxDelayIn) {
/* 79 */       this.musicLocation = location;
/* 80 */       this.minDelay = minDelayIn;
/* 81 */       this.maxDelay = maxDelayIn;
/*    */     }
/*    */ 
/*    */     
/*    */     public ResourceLocation getMusicLocation() {
/* 86 */       return this.musicLocation;
/*    */     }
/*    */ 
/*    */     
/*    */     public int getMinDelay() {
/* 91 */       return this.minDelay;
/*    */     }
/*    */ 
/*    */     
/*    */     public int getMaxDelay() {
/* 96 */       return this.maxDelay;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\audio\MusicTicker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */