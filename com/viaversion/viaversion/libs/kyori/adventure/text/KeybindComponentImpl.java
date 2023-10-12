/*     */ package com.viaversion.viaversion.libs.kyori.adventure.text;
/*     */ 
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.internal.Internals;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.util.Buildable;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
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
/*     */ final class KeybindComponentImpl
/*     */   extends AbstractComponent
/*     */   implements KeybindComponent
/*     */ {
/*     */   private final String keybind;
/*     */   
/*     */   static KeybindComponent create(@NotNull List<? extends ComponentLike> children, @NotNull Style style, @NotNull String keybind) {
/*  39 */     return new KeybindComponentImpl(
/*  40 */         ComponentLike.asComponents(children, IS_NOT_EMPTY), 
/*  41 */         Objects.<Style>requireNonNull(style, "style"), 
/*  42 */         Objects.<String>requireNonNull(keybind, "keybind"));
/*     */   }
/*     */ 
/*     */   
/*     */   KeybindComponentImpl(@NotNull List<Component> children, @NotNull Style style, @NotNull String keybind) {
/*  47 */     super((List)children, style);
/*  48 */     this.keybind = keybind;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public String keybind() {
/*  53 */     return this.keybind;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public KeybindComponent keybind(@NotNull String keybind) {
/*  58 */     if (Objects.equals(this.keybind, keybind)) return this; 
/*  59 */     return create((List)this.children, this.style, keybind);
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public KeybindComponent children(@NotNull List<? extends ComponentLike> children) {
/*  64 */     return create(children, this.style, this.keybind);
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public KeybindComponent style(@NotNull Style style) {
/*  69 */     return create((List)this.children, style, this.keybind);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/*  74 */     if (this == other) return true; 
/*  75 */     if (!(other instanceof KeybindComponent)) return false; 
/*  76 */     if (!super.equals(other)) return false; 
/*  77 */     KeybindComponent that = (KeybindComponent)other;
/*  78 */     return Objects.equals(this.keybind, that.keybind());
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  83 */     int result = super.hashCode();
/*  84 */     result = 31 * result + this.keybind.hashCode();
/*  85 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  90 */     return Internals.toString(this);
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public KeybindComponent.Builder toBuilder() {
/*  95 */     return new BuilderImpl(this);
/*     */   }
/*     */   
/*     */   static final class BuilderImpl extends AbstractComponentBuilder<KeybindComponent, KeybindComponent.Builder> implements KeybindComponent.Builder {
/*     */     @Nullable
/*     */     private String keybind;
/*     */     
/*     */     BuilderImpl() {}
/*     */     
/*     */     BuilderImpl(@NotNull KeybindComponent component) {
/* 105 */       super(component);
/* 106 */       this.keybind = component.keybind();
/*     */     }
/*     */     
/*     */     @NotNull
/*     */     public KeybindComponent.Builder keybind(@NotNull String keybind) {
/* 111 */       this.keybind = Objects.<String>requireNonNull(keybind, "keybind");
/* 112 */       return this;
/*     */     }
/*     */     
/*     */     @NotNull
/*     */     public KeybindComponent build() {
/* 117 */       if (this.keybind == null) throw new IllegalStateException("keybind must be set"); 
/* 118 */       return KeybindComponentImpl.create((List)this.children, buildStyle(), this.keybind);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\text\KeybindComponentImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */