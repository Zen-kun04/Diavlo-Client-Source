/*     */ package com.viaversion.viaversion.libs.kyori.adventure.audience;
/*     */ 
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.bossbar.BossBar;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.chat.ChatType;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.chat.SignedMessage;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.identity.Identified;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.identity.Identity;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.inventory.Book;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.pointer.Pointered;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.sound.Sound;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.sound.SoundStop;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.ComponentLike;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.title.Title;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.title.TitlePart;
/*     */ import java.util.Arrays;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collector;
/*     */ import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
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
/*     */ public interface Audience
/*     */   extends Pointered
/*     */ {
/*     */   @NotNull
/*     */   static Audience empty() {
/*  98 */     return EmptyAudience.INSTANCE;
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
/*     */   static Audience audience(@NotNull Audience... audiences) {
/* 110 */     int length = audiences.length;
/* 111 */     if (length == 0)
/* 112 */       return empty(); 
/* 113 */     if (length == 1) {
/* 114 */       return audiences[0];
/*     */     }
/* 116 */     return audience(Arrays.asList(audiences));
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
/*     */   static ForwardingAudience audience(@NotNull Iterable<? extends Audience> audiences) {
/* 131 */     return () -> audiences;
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
/*     */   static Collector<? super Audience, ?, ForwardingAudience> toAudience() {
/* 143 */     return Audiences.COLLECTOR;
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
/*     */   
/*     */   @NotNull
/*     */   default Audience filterAudience(@NotNull Predicate<? super Audience> filter) {
/* 160 */     return filter.test(this) ? 
/* 161 */       this : 
/* 162 */       empty();
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
/*     */ 
/*     */   
/*     */   default void forEachAudience(@NotNull Consumer<? super Audience> action) {
/* 179 */     action.accept(this);
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
/*     */   @ForwardingAudienceOverrideNotRequired
/*     */   default void sendMessage(@NotNull ComponentLike message) {
/* 194 */     sendMessage(message.asComponent());
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
/*     */   default void sendMessage(@NotNull Component message) {
/* 208 */     sendMessage(message, MessageType.SYSTEM);
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
/*     */   @Deprecated
/*     */   @ForwardingAudienceOverrideNotRequired
/*     */   @ScheduledForRemoval(inVersion = "5.0.0")
/*     */   default void sendMessage(@NotNull ComponentLike message, @NotNull MessageType type) {
/* 226 */     sendMessage(message.asComponent(), type);
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
/*     */   @Deprecated
/*     */   @ForwardingAudienceOverrideNotRequired
/*     */   @ScheduledForRemoval(inVersion = "5.0.0")
/*     */   default void sendMessage(@NotNull Component message, @NotNull MessageType type) {
/* 244 */     sendMessage(Identity.nil(), message, type);
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
/*     */   @Deprecated
/*     */   @ForwardingAudienceOverrideNotRequired
/*     */   default void sendMessage(@NotNull Identified source, @NotNull ComponentLike message) {
/* 261 */     sendMessage(source, message.asComponent());
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
/*     */   @Deprecated
/*     */   @ForwardingAudienceOverrideNotRequired
/*     */   default void sendMessage(@NotNull Identity source, @NotNull ComponentLike message) {
/* 276 */     sendMessage(source, message.asComponent());
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
/*     */   @Deprecated
/*     */   @ForwardingAudienceOverrideNotRequired
/*     */   default void sendMessage(@NotNull Identified source, @NotNull Component message) {
/* 291 */     sendMessage(source, message, MessageType.CHAT);
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
/*     */   @Deprecated
/*     */   @ForwardingAudienceOverrideNotRequired
/*     */   default void sendMessage(@NotNull Identity source, @NotNull Component message) {
/* 306 */     sendMessage(source, message, MessageType.CHAT);
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
/*     */   @ForwardingAudienceOverrideNotRequired
/*     */   @ScheduledForRemoval(inVersion = "5.0.0")
/*     */   default void sendMessage(@NotNull Identified source, @NotNull ComponentLike message, @NotNull MessageType type) {
/* 323 */     sendMessage(source, message.asComponent(), type);
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
/*     */   @ForwardingAudienceOverrideNotRequired
/*     */   @ScheduledForRemoval(inVersion = "5.0.0")
/*     */   default void sendMessage(@NotNull Identity source, @NotNull ComponentLike message, @NotNull MessageType type) {
/* 340 */     sendMessage(source, message.asComponent(), type);
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
/*     */   default void sendMessage(@NotNull Identified source, @NotNull Component message, @NotNull MessageType type) {
/* 356 */     sendMessage(source.identity(), message, type);
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
/*     */   
/*     */   @Deprecated
/*     */   @ScheduledForRemoval(inVersion = "5.0.0")
/*     */   default void sendMessage(@NotNull Identity source, @NotNull Component message, @NotNull MessageType type) {}
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
/*     */   default void sendMessage(@NotNull Component message, ChatType.Bound boundChatType) {
/* 387 */     sendMessage(message, MessageType.CHAT);
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
/*     */   @ForwardingAudienceOverrideNotRequired
/*     */   default void sendMessage(@NotNull ComponentLike message, ChatType.Bound boundChatType) {
/* 400 */     sendMessage(message.asComponent(), boundChatType);
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
/*     */   default void sendMessage(@NotNull SignedMessage signedMessage, ChatType.Bound boundChatType) {
/* 415 */     Component content = (signedMessage.unsignedContent() != null) ? signedMessage.unsignedContent() : (Component)Component.text(signedMessage.message());
/* 416 */     if (signedMessage.isSystem()) {
/* 417 */       sendMessage(content);
/*     */     } else {
/* 419 */       sendMessage(signedMessage.identity(), content, MessageType.CHAT);
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
/*     */   @ForwardingAudienceOverrideNotRequired
/*     */   default void deleteMessage(@NotNull SignedMessage signedMessage) {
/* 433 */     if (signedMessage.canDelete()) {
/* 434 */       deleteMessage(Objects.<SignedMessage.Signature>requireNonNull(signedMessage.signature()));
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
/*     */   default void deleteMessage(SignedMessage.Signature signature) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ForwardingAudienceOverrideNotRequired
/*     */   default void sendActionBar(@NotNull ComponentLike message) {
/* 458 */     sendActionBar(message.asComponent());
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
/*     */   default void sendActionBar(@NotNull Component message) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ForwardingAudienceOverrideNotRequired
/*     */   default void sendPlayerListHeader(@NotNull ComponentLike header) {
/* 482 */     sendPlayerListHeader(header.asComponent());
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
/*     */   default void sendPlayerListHeader(@NotNull Component header) {
/* 495 */     sendPlayerListHeaderAndFooter(header, (Component)Component.empty());
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
/*     */   @ForwardingAudienceOverrideNotRequired
/*     */   default void sendPlayerListFooter(@NotNull ComponentLike footer) {
/* 509 */     sendPlayerListFooter(footer.asComponent());
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
/*     */   default void sendPlayerListFooter(@NotNull Component footer) {
/* 522 */     sendPlayerListHeaderAndFooter((Component)Component.empty(), footer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ForwardingAudienceOverrideNotRequired
/*     */   default void sendPlayerListHeaderAndFooter(@NotNull ComponentLike header, @NotNull ComponentLike footer) {
/* 534 */     sendPlayerListHeaderAndFooter(header.asComponent(), footer.asComponent());
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
/*     */   default void sendPlayerListHeaderAndFooter(@NotNull Component header, @NotNull Component footer) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ForwardingAudienceOverrideNotRequired
/*     */   default void showTitle(@NotNull Title title) {
/* 556 */     Title.Times times = title.times();
/* 557 */     if (times != null) sendTitlePart(TitlePart.TIMES, times);
/*     */     
/* 559 */     sendTitlePart(TitlePart.SUBTITLE, title.subtitle());
/* 560 */     sendTitlePart(TitlePart.TITLE, title.title());
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
/*     */   default <T> void sendTitlePart(@NotNull TitlePart<T> part, @NotNull T value) {}
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
/*     */   default void clearTitle() {}
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
/*     */   default void resetTitle() {}
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
/*     */   default void showBossBar(@NotNull BossBar bar) {}
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
/*     */   default void hideBossBar(@NotNull BossBar bar) {}
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
/*     */   default void playSound(@NotNull Sound sound) {}
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
/*     */   default void playSound(@NotNull Sound sound, double x, double y, double z) {}
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
/*     */   default void playSound(@NotNull Sound sound, Sound.Emitter emitter) {}
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
/*     */   @ForwardingAudienceOverrideNotRequired
/*     */   default void stopSound(@NotNull Sound sound) {
/* 665 */     stopSound(((Sound)Objects.<Sound>requireNonNull(sound, "sound")).asStop());
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
/*     */   default void stopSound(@NotNull SoundStop stop) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ForwardingAudienceOverrideNotRequired
/*     */   default void openBook(Book.Builder book) {
/* 689 */     openBook(book.build());
/*     */   }
/*     */   
/*     */   default void openBook(@NotNull Book book) {}
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\audience\Audience.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */