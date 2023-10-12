/*     */ package com.viaversion.viaversion.libs.kyori.adventure.text.event;
/*     */ 
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.audience.Audience;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.builder.AbstractBuilder;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.internal.Internals;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.format.StyleBuilderApplicable;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.util.Index;
/*     */ import com.viaversion.viaversion.libs.kyori.examination.Examinable;
/*     */ import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
/*     */ import java.net.URL;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Stream;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
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
/*     */ public final class ClickEvent
/*     */   implements Examinable, StyleBuilderApplicable
/*     */ {
/*     */   private final Action action;
/*     */   private final String value;
/*     */   
/*     */   @NotNull
/*     */   public static ClickEvent openUrl(@NotNull String url) {
/*  59 */     return new ClickEvent(Action.OPEN_URL, url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static ClickEvent openUrl(@NotNull URL url) {
/*  70 */     return openUrl(url.toExternalForm());
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
/*     */   @NotNull
/*     */   public static ClickEvent openFile(@NotNull String file) {
/*  83 */     return new ClickEvent(Action.OPEN_FILE, file);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static ClickEvent runCommand(@NotNull String command) {
/*  94 */     return new ClickEvent(Action.RUN_COMMAND, command);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static ClickEvent suggestCommand(@NotNull String command) {
/* 105 */     return new ClickEvent(Action.SUGGEST_COMMAND, command);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static ClickEvent changePage(@NotNull String page) {
/* 116 */     return new ClickEvent(Action.CHANGE_PAGE, page);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static ClickEvent changePage(int page) {
/* 127 */     return changePage(String.valueOf(page));
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
/*     */   public static ClickEvent copyToClipboard(@NotNull String text) {
/* 139 */     return new ClickEvent(Action.COPY_TO_CLIPBOARD, text);
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
/*     */   @NotNull
/*     */   public static ClickEvent callback(@NotNull ClickCallback<Audience> function) {
/* 152 */     return ClickCallbackInternals.PROVIDER.create(Objects.<ClickCallback<Audience>>requireNonNull(function, "function"), ClickCallbackOptionsImpl.DEFAULT);
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
/*     */   public static ClickEvent callback(@NotNull ClickCallback<Audience> function, ClickCallback.Options options) {
/* 164 */     return ClickCallbackInternals.PROVIDER.create(Objects.<ClickCallback<Audience>>requireNonNull(function, "function"), Objects.<ClickCallback.Options>requireNonNull(options, "options"));
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
/*     */   public static ClickEvent callback(@NotNull ClickCallback<Audience> function, @NotNull Consumer<ClickCallback.Options.Builder> optionsBuilder) {
/* 176 */     return ClickCallbackInternals.PROVIDER.create(
/* 177 */         Objects.<ClickCallback<Audience>>requireNonNull(function, "function"), 
/* 178 */         (ClickCallback.Options)AbstractBuilder.configureAndBuild(ClickCallback.Options.builder(), Objects.<Consumer>requireNonNull(optionsBuilder, "optionsBuilder")));
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
/*     */   @NotNull
/*     */   public static ClickEvent clickEvent(@NotNull Action action, @NotNull String value) {
/* 191 */     return new ClickEvent(action, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ClickEvent(@NotNull Action action, @NotNull String value) {
/* 198 */     this.action = Objects.<Action>requireNonNull(action, "action");
/* 199 */     this.value = Objects.<String>requireNonNull(value, "value");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public Action action() {
/* 209 */     return this.action;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public String value() {
/* 219 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public void styleApply(Style.Builder style) {
/* 224 */     style.clickEvent(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 229 */     if (this == other) return true; 
/* 230 */     if (other == null || getClass() != other.getClass()) return false; 
/* 231 */     ClickEvent that = (ClickEvent)other;
/* 232 */     return (this.action == that.action && Objects.equals(this.value, that.value));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 237 */     int result = this.action.hashCode();
/* 238 */     result = 31 * result + this.value.hashCode();
/* 239 */     return result;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public Stream<? extends ExaminableProperty> examinableProperties() {
/* 244 */     return Stream.of(new ExaminableProperty[] {
/* 245 */           ExaminableProperty.of("action", this.action), 
/* 246 */           ExaminableProperty.of("value", this.value)
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 252 */     return Internals.toString(this);
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
/*     */   public enum Action
/*     */   {
/* 266 */     OPEN_URL("open_url", true),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 274 */     OPEN_FILE("open_file", false),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 280 */     RUN_COMMAND("run_command", true),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 286 */     SUGGEST_COMMAND("suggest_command", true),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 292 */     CHANGE_PAGE("change_page", true),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 299 */     COPY_TO_CLIPBOARD("copy_to_clipboard", true);
/*     */     
/*     */     public static final Index<String, Action> NAMES;
/*     */     private final String name;
/*     */     private final boolean readable;
/*     */     
/*     */     static {
/* 306 */       NAMES = Index.create(Action.class, constant -> constant.name);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Action(String name, boolean readable) {
/* 316 */       this.name = name;
/* 317 */       this.readable = readable;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean readable() {
/* 328 */       return this.readable;
/*     */     }
/*     */     
/*     */     @NotNull
/*     */     public String toString() {
/* 333 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\text\event\ClickEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */