/*     */ package com.viaversion.viaversion.libs.kyori.adventure.text;
/*     */ 
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.internal.Internals;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.util.Buildable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Collectors;
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
/*     */ final class TranslatableComponentImpl
/*     */   extends AbstractComponent
/*     */   implements TranslatableComponent
/*     */ {
/*     */   private final String key;
/*     */   @Nullable
/*     */   private final String fallback;
/*     */   private final List<Component> args;
/*     */   
/*     */   static TranslatableComponent create(@NotNull List<Component> children, @NotNull Style style, @NotNull String key, @Nullable String fallback, @NotNull ComponentLike[] args) {
/*  41 */     Objects.requireNonNull(args, "args");
/*  42 */     return create((List)children, style, key, fallback, Arrays.asList(args));
/*     */   }
/*     */   
/*     */   static TranslatableComponent create(@NotNull List<? extends ComponentLike> children, @NotNull Style style, @NotNull String key, @Nullable String fallback, @NotNull List<? extends ComponentLike> args) {
/*  46 */     return new TranslatableComponentImpl(
/*  47 */         ComponentLike.asComponents(children, IS_NOT_EMPTY), 
/*  48 */         Objects.<Style>requireNonNull(style, "style"), 
/*  49 */         Objects.<String>requireNonNull(key, "key"), fallback, 
/*     */         
/*  51 */         ComponentLike.asComponents(args));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   TranslatableComponentImpl(@NotNull List<Component> children, @NotNull Style style, @NotNull String key, @Nullable String fallback, @NotNull List<Component> args) {
/*  60 */     super((List)children, style);
/*  61 */     this.key = key;
/*  62 */     this.fallback = fallback;
/*  63 */     this.args = args;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public String key() {
/*  68 */     return this.key;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public TranslatableComponent key(@NotNull String key) {
/*  73 */     if (Objects.equals(this.key, key)) return this; 
/*  74 */     return create((List)this.children, this.style, key, this.fallback, (List)this.args);
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public List<Component> args() {
/*  79 */     return this.args;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public TranslatableComponent args(@NotNull ComponentLike... args) {
/*  84 */     return create(this.children, this.style, this.key, this.fallback, args);
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public TranslatableComponent args(@NotNull List<? extends ComponentLike> args) {
/*  89 */     return create((List)this.children, this.style, this.key, this.fallback, args);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public String fallback() {
/*  94 */     return this.fallback;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public TranslatableComponent fallback(@Nullable String fallback) {
/*  99 */     return create((List)this.children, this.style, this.key, fallback, (List)this.args);
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public TranslatableComponent children(@NotNull List<? extends ComponentLike> children) {
/* 104 */     return create(children, this.style, this.key, this.fallback, (List)this.args);
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public TranslatableComponent style(@NotNull Style style) {
/* 109 */     return create((List)this.children, style, this.key, this.fallback, (List)this.args);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 114 */     if (this == other) return true; 
/* 115 */     if (!(other instanceof TranslatableComponent)) return false; 
/* 116 */     if (!super.equals(other)) return false; 
/* 117 */     TranslatableComponent that = (TranslatableComponent)other;
/* 118 */     return (Objects.equals(this.key, that.key()) && Objects.equals(this.fallback, that.fallback()) && Objects.equals(this.args, that.args()));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 123 */     int result = super.hashCode();
/* 124 */     result = 31 * result + this.key.hashCode();
/* 125 */     result = 31 * result + Objects.hashCode(this.fallback);
/* 126 */     result = 31 * result + this.args.hashCode();
/* 127 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 132 */     return Internals.toString(this);
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public TranslatableComponent.Builder toBuilder() {
/* 137 */     return new BuilderImpl(this);
/*     */   }
/*     */   
/*     */   static final class BuilderImpl extends AbstractComponentBuilder<TranslatableComponent, TranslatableComponent.Builder> implements TranslatableComponent.Builder {
/*     */     @Nullable
/*     */     private String key;
/* 143 */     private List<? extends Component> args = Collections.emptyList();
/*     */     
/*     */     @Nullable
/*     */     private String fallback;
/*     */     
/*     */     BuilderImpl(@NotNull TranslatableComponent component) {
/* 149 */       super(component);
/* 150 */       this.key = component.key();
/* 151 */       this.args = component.args();
/* 152 */       this.fallback = component.fallback();
/*     */     }
/*     */     
/*     */     @NotNull
/*     */     public TranslatableComponent.Builder key(@NotNull String key) {
/* 157 */       this.key = key;
/* 158 */       return this;
/*     */     }
/*     */     
/*     */     @NotNull
/*     */     public TranslatableComponent.Builder args(@NotNull ComponentBuilder<?, ?> arg) {
/* 163 */       return args(Collections.singletonList((ComponentLike)((ComponentBuilder)Objects.<ComponentBuilder>requireNonNull(arg, "arg")).build()));
/*     */     }
/*     */ 
/*     */     
/*     */     @NotNull
/*     */     public TranslatableComponent.Builder args(@NotNull ComponentBuilder<?, ?>... args) {
/* 169 */       Objects.requireNonNull(args, "args");
/* 170 */       if (args.length == 0) return args(Collections.emptyList()); 
/* 171 */       return args((List<? extends ComponentLike>)Stream.<ComponentBuilder<?, ?>>of(args).map(ComponentBuilder::build).collect(Collectors.toList()));
/*     */     }
/*     */     
/*     */     @NotNull
/*     */     public TranslatableComponent.Builder args(@NotNull Component arg) {
/* 176 */       return args(Collections.singletonList(Objects.<Component>requireNonNull(arg, "arg")));
/*     */     }
/*     */     
/*     */     @NotNull
/*     */     public TranslatableComponent.Builder args(@NotNull ComponentLike... args) {
/* 181 */       Objects.requireNonNull(args, "args");
/* 182 */       if (args.length == 0) return args(Collections.emptyList()); 
/* 183 */       return args(Arrays.asList(args));
/*     */     }
/*     */     
/*     */     @NotNull
/*     */     public TranslatableComponent.Builder args(@NotNull List<? extends ComponentLike> args) {
/* 188 */       this.args = ComponentLike.asComponents(Objects.<List<? extends ComponentLike>>requireNonNull(args, "args"));
/* 189 */       return this;
/*     */     }
/*     */     
/*     */     @NotNull
/*     */     public TranslatableComponent.Builder fallback(@Nullable String fallback) {
/* 194 */       this.fallback = fallback;
/* 195 */       return this;
/*     */     }
/*     */     
/*     */     @NotNull
/*     */     public TranslatableComponent build() {
/* 200 */       if (this.key == null) throw new IllegalStateException("key must be set"); 
/* 201 */       return TranslatableComponentImpl.create((List)this.children, buildStyle(), this.key, this.fallback, (List)this.args);
/*     */     }
/*     */     
/*     */     BuilderImpl() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\text\TranslatableComponentImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */