/*     */ package com.viaversion.viaversion.libs.kyori.adventure.bossbar;
/*     */ 
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.audience.Audience;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.ComponentLike;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.util.Index;
/*     */ import com.viaversion.viaversion.libs.kyori.examination.Examinable;
/*     */ import java.util.Set;
/*     */ import org.jetbrains.annotations.ApiStatus.NonExtendable;
/*     */ import org.jetbrains.annotations.ApiStatus.OverrideOnly;
/*     */ import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
/*     */ import org.jetbrains.annotations.Contract;
/*     */ import org.jetbrains.annotations.NotNull;
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
/*     */ @NonExtendable
/*     */ public interface BossBar
/*     */   extends Examinable
/*     */ {
/*     */   public static final float MIN_PROGRESS = 0.0F;
/*     */   public static final float MAX_PROGRESS = 1.0F;
/*     */   @Deprecated
/*     */   @ScheduledForRemoval(inVersion = "5.0.0")
/*     */   public static final float MIN_PERCENT = 0.0F;
/*     */   @Deprecated
/*     */   @ScheduledForRemoval(inVersion = "5.0.0")
/*     */   public static final float MAX_PERCENT = 1.0F;
/*     */   
/*     */   @NotNull
/*     */   static BossBar bossBar(@NotNull ComponentLike name, float progress, @NotNull Color color, @NotNull Overlay overlay) {
/* 100 */     BossBarImpl.checkProgress(progress);
/* 101 */     return bossBar(name.asComponent(), progress, color, overlay);
/*     */   }
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
/*     */   @NotNull
/*     */   static BossBar bossBar(@NotNull Component name, float progress, @NotNull Color color, @NotNull Overlay overlay) {
/* 116 */     BossBarImpl.checkProgress(progress);
/* 117 */     return new BossBarImpl(name, progress, color, overlay);
/*     */   }
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
/*     */   @NotNull
/*     */   static BossBar bossBar(@NotNull ComponentLike name, float progress, @NotNull Color color, @NotNull Overlay overlay, @NotNull Set<Flag> flags) {
/* 133 */     BossBarImpl.checkProgress(progress);
/* 134 */     return bossBar(name.asComponent(), progress, color, overlay, flags);
/*     */   }
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
/*     */   @NotNull
/*     */   static BossBar bossBar(@NotNull Component name, float progress, @NotNull Color color, @NotNull Overlay overlay, @NotNull Set<Flag> flags) {
/* 150 */     BossBarImpl.checkProgress(progress);
/* 151 */     return new BossBarImpl(name, progress, color, overlay, flags);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   Component name();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Contract("_ -> this")
/*     */   @NotNull
/*     */   default BossBar name(@NotNull ComponentLike name) {
/* 171 */     return name(name.asComponent());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Contract("_ -> this")
/*     */   @NotNull
/*     */   BossBar name(@NotNull Component paramComponent);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float progress();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Contract("_ -> this")
/*     */   @NotNull
/*     */   BossBar progress(float paramFloat);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @ScheduledForRemoval(inVersion = "5.0.0")
/*     */   default float percent() {
/* 219 */     return progress();
/*     */   }
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
/*     */   @Deprecated
/*     */   @ScheduledForRemoval(inVersion = "5.0.0")
/*     */   @Contract("_ -> this")
/*     */   @NotNull
/*     */   default BossBar percent(float progress) {
/* 237 */     return progress(progress);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   Color color();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Contract("_ -> this")
/*     */   @NotNull
/*     */   BossBar color(@NotNull Color paramColor);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   Overlay overlay();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Contract("_ -> this")
/*     */   @NotNull
/*     */   BossBar overlay(@NotNull Overlay paramOverlay);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   Set<Flag> flags();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Contract("_ -> this")
/*     */   @NotNull
/*     */   BossBar flags(@NotNull Set<Flag> paramSet);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean hasFlag(@NotNull Flag paramFlag);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Contract("_ -> this")
/*     */   @NotNull
/*     */   BossBar addFlag(@NotNull Flag paramFlag);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Contract("_ -> this")
/*     */   @NotNull
/*     */   BossBar removeFlag(@NotNull Flag paramFlag);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Contract("_ -> this")
/*     */   @NotNull
/*     */   BossBar addFlags(@NotNull Flag... paramVarArgs);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Contract("_ -> this")
/*     */   @NotNull
/*     */   BossBar removeFlags(@NotNull Flag... paramVarArgs);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Contract("_ -> this")
/*     */   @NotNull
/*     */   BossBar addFlags(@NotNull Iterable<Flag> paramIterable);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Contract("_ -> this")
/*     */   @NotNull
/*     */   BossBar removeFlags(@NotNull Iterable<Flag> paramIterable);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Contract("_ -> this")
/*     */   @NotNull
/*     */   BossBar addListener(@NotNull Listener paramListener);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Contract("_ -> this")
/*     */   @NotNull
/*     */   BossBar removeListener(@NotNull Listener paramListener);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   Iterable<? extends BossBarViewer> viewers();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   default BossBar addViewer(@NotNull Audience viewer) {
/* 403 */     viewer.showBossBar(this);
/* 404 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   default BossBar removeViewer(@NotNull Audience viewer) {
/* 416 */     viewer.hideBossBar(this);
/* 417 */     return this;
/*     */   }
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
/*     */   @OverrideOnly
/*     */   public static interface Listener
/*     */   {
/*     */     default void bossBarNameChanged(@NotNull BossBar bar, @NotNull Component oldName, @NotNull Component newName) {}
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
/*     */     default void bossBarProgressChanged(@NotNull BossBar bar, float oldProgress, float newProgress) {
/* 447 */       bossBarPercentChanged(bar, oldProgress, newProgress);
/*     */     }
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
/*     */     @Deprecated
/*     */     @ScheduledForRemoval(inVersion = "5.0.0")
/*     */     default void bossBarPercentChanged(@NotNull BossBar bar, float oldProgress, float newProgress) {}
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
/*     */     default void bossBarColorChanged(@NotNull BossBar bar, @NotNull BossBar.Color oldColor, @NotNull BossBar.Color newColor) {}
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
/*     */     default void bossBarOverlayChanged(@NotNull BossBar bar, @NotNull BossBar.Overlay oldOverlay, @NotNull BossBar.Overlay newOverlay) {}
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
/*     */     default void bossBarFlagsChanged(@NotNull BossBar bar, @NotNull Set<BossBar.Flag> flagsAdded, @NotNull Set<BossBar.Flag> flagsRemoved) {}
/*     */   }
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
/*     */   public enum Color
/*     */   {
/* 515 */     PINK("pink"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 522 */     BLUE("blue"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 529 */     RED("red"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 536 */     GREEN("green"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 543 */     YELLOW("yellow"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 549 */     PURPLE("purple"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 556 */     WHITE("white");
/*     */     
/*     */     public static final Index<String, Color> NAMES;
/*     */     
/*     */     private final String name;
/*     */     
/*     */     static {
/* 563 */       NAMES = Index.create(Color.class, color -> color.name);
/*     */     }
/*     */     
/*     */     Color(String name) {
/* 567 */       this.name = name;
/*     */     }
/*     */   }
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
/*     */   public enum Flag
/*     */   {
/* 583 */     DARKEN_SCREEN("darken_screen"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 590 */     PLAY_BOSS_MUSIC("play_boss_music"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 597 */     CREATE_WORLD_FOG("create_world_fog");
/*     */ 
/*     */     
/*     */     public static final Index<String, Flag> NAMES;
/*     */     
/*     */     private final String name;
/*     */ 
/*     */     
/*     */     static {
/* 606 */       NAMES = Index.create(Flag.class, flag -> flag.name);
/*     */     }
/*     */     
/*     */     Flag(String name) {
/* 610 */       this.name = name;
/*     */     }
/*     */   }
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
/*     */   public enum Overlay
/*     */   {
/* 625 */     PROGRESS("progress"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 632 */     NOTCHED_6("notched_6"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 639 */     NOTCHED_10("notched_10"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 646 */     NOTCHED_12("notched_12"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 653 */     NOTCHED_20("notched_20");
/*     */     
/*     */     public static final Index<String, Overlay> NAMES;
/*     */     
/*     */     private final String name;
/*     */     
/*     */     static {
/* 660 */       NAMES = Index.create(Overlay.class, overlay -> overlay.name);
/*     */     }
/*     */     
/*     */     Overlay(String name) {
/* 664 */       this.name = name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\bossbar\BossBar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */