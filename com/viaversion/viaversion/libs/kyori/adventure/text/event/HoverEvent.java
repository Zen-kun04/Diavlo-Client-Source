/*     */ package com.viaversion.viaversion.libs.kyori.adventure.text.event;
/*     */ 
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.internal.Internals;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.key.Keyed;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.nbt.api.BinaryTagHolder;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.ComponentLike;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.format.StyleBuilderApplicable;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.renderer.ComponentRenderer;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.util.Index;
/*     */ import com.viaversion.viaversion.libs.kyori.examination.Examinable;
/*     */ import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import java.util.function.UnaryOperator;
/*     */ import java.util.stream.Stream;
/*     */ import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class HoverEvent<V>
/*     */   implements Examinable, HoverEventSource<V>, StyleBuilderApplicable
/*     */ {
/*     */   private final Action<V> action;
/*     */   private final V value;
/*     */   
/*     */   @NotNull
/*     */   public static HoverEvent<Component> showText(@NotNull ComponentLike text) {
/*  67 */     return showText(text.asComponent());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static HoverEvent<Component> showText(@NotNull Component text) {
/*  78 */     return new HoverEvent<>(Action.SHOW_TEXT, text);
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
/*     */   public static HoverEvent<ShowItem> showItem(@NotNull Key item, int count) {
/*  90 */     return showItem(item, count, (BinaryTagHolder)null);
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
/*     */   public static HoverEvent<ShowItem> showItem(@NotNull Keyed item, int count) {
/* 102 */     return showItem(item, count, (BinaryTagHolder)null);
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
/*     */   public static HoverEvent<ShowItem> showItem(@NotNull Key item, int count, @Nullable BinaryTagHolder nbt) {
/* 115 */     return showItem(ShowItem.of(item, count, nbt));
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
/*     */   public static HoverEvent<ShowItem> showItem(@NotNull Keyed item, int count, @Nullable BinaryTagHolder nbt) {
/* 128 */     return showItem(ShowItem.of(item, count, nbt));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static HoverEvent<ShowItem> showItem(@NotNull ShowItem item) {
/* 139 */     return new HoverEvent<>(Action.SHOW_ITEM, item);
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
/*     */   @NotNull
/*     */   public static HoverEvent<ShowEntity> showEntity(@NotNull Key type, @NotNull UUID id) {
/* 153 */     return showEntity(type, id, (Component)null);
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
/*     */   @NotNull
/*     */   public static HoverEvent<ShowEntity> showEntity(@NotNull Keyed type, @NotNull UUID id) {
/* 167 */     return showEntity(type, id, (Component)null);
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
/*     */   public static HoverEvent<ShowEntity> showEntity(@NotNull Key type, @NotNull UUID id, @Nullable Component name) {
/* 182 */     return showEntity(ShowEntity.of(type, id, name));
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
/*     */   public static HoverEvent<ShowEntity> showEntity(@NotNull Keyed type, @NotNull UUID id, @Nullable Component name) {
/* 197 */     return showEntity(ShowEntity.of(type, id, name));
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
/*     */   public static HoverEvent<ShowEntity> showEntity(@NotNull ShowEntity entity) {
/* 210 */     return new HoverEvent<>(Action.SHOW_ENTITY, entity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @NotNull
/*     */   public static HoverEvent<String> showAchievement(@NotNull String value) {
/* 223 */     return new HoverEvent<>(Action.SHOW_ACHIEVEMENT, value);
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
/*     */   public static <V> HoverEvent<V> hoverEvent(@NotNull Action<V> action, @NotNull V value) {
/* 236 */     return new HoverEvent<>(action, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private HoverEvent(@NotNull Action<V> action, @NotNull V value) {
/* 243 */     this.action = Objects.<Action<V>>requireNonNull(action, "action");
/* 244 */     this.value = Objects.requireNonNull(value, "value");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public Action<V> action() {
/* 254 */     return this.action;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public V value() {
/* 264 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public HoverEvent<V> value(@NotNull V value) {
/* 275 */     return new HoverEvent(this.action, value);
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
/*     */   public <C> HoverEvent<V> withRenderedValue(@NotNull ComponentRenderer<C> renderer, @NotNull C context) {
/* 288 */     V oldValue = this.value;
/* 289 */     V newValue = this.action.renderer.render(renderer, context, oldValue);
/* 290 */     if (newValue != oldValue) return new HoverEvent(this.action, newValue); 
/* 291 */     return this;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public HoverEvent<V> asHoverEvent() {
/* 296 */     return this;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public HoverEvent<V> asHoverEvent(@NotNull UnaryOperator<V> op) {
/* 301 */     if (op == UnaryOperator.identity()) return this; 
/* 302 */     return new HoverEvent(this.action, op.apply(this.value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void styleApply(Style.Builder style) {
/* 307 */     style.hoverEvent(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 312 */     if (this == other) return true; 
/* 313 */     if (other == null || getClass() != other.getClass()) return false; 
/* 314 */     HoverEvent<?> that = (HoverEvent)other;
/* 315 */     return (this.action == that.action && this.value.equals(that.value));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 320 */     int result = this.action.hashCode();
/* 321 */     result = 31 * result + this.value.hashCode();
/* 322 */     return result;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public Stream<? extends ExaminableProperty> examinableProperties() {
/* 327 */     return Stream.of(new ExaminableProperty[] {
/* 328 */           ExaminableProperty.of("action", this.action), 
/* 329 */           ExaminableProperty.of("value", this.value)
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 335 */     return Internals.toString(this);
/*     */   }
/*     */ 
/*     */   
/*     */   @FunctionalInterface
/*     */   static interface Renderer<V>
/*     */   {
/*     */     @NotNull
/*     */     <C> V render(@NotNull ComponentRenderer<C> param1ComponentRenderer, @NotNull C param1C, @NotNull V param1V);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class ShowItem
/*     */     implements Examinable
/*     */   {
/*     */     private final Key item;
/*     */     private final int count;
/*     */     @Nullable
/*     */     private final BinaryTagHolder nbt;
/*     */     
/*     */     @NotNull
/*     */     public static ShowItem showItem(@NotNull Key item, int count) {
/* 357 */       return showItem(item, count, (BinaryTagHolder)null);
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
/*     */     @Deprecated
/*     */     @ScheduledForRemoval(inVersion = "5.0.0")
/*     */     @NotNull
/*     */     public static ShowItem of(@NotNull Key item, int count) {
/* 372 */       return of(item, count, (BinaryTagHolder)null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @NotNull
/*     */     public static ShowItem showItem(@NotNull Keyed item, int count) {
/* 384 */       return showItem(item, count, (BinaryTagHolder)null);
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
/*     */     @Deprecated
/*     */     @ScheduledForRemoval(inVersion = "5.0.0")
/*     */     @NotNull
/*     */     public static ShowItem of(@NotNull Keyed item, int count) {
/* 399 */       return of(item, count, (BinaryTagHolder)null);
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
/*     */     @NotNull
/*     */     public static ShowItem showItem(@NotNull Key item, int count, @Nullable BinaryTagHolder nbt) {
/* 412 */       return new ShowItem(Objects.<Key>requireNonNull(item, "item"), count, nbt);
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
/*     */     @Deprecated
/*     */     @ScheduledForRemoval(inVersion = "5.0.0")
/*     */     @NotNull
/*     */     public static ShowItem of(@NotNull Key item, int count, @Nullable BinaryTagHolder nbt) {
/* 428 */       return new ShowItem(Objects.<Key>requireNonNull(item, "item"), count, nbt);
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
/*     */     @NotNull
/*     */     public static ShowItem showItem(@NotNull Keyed item, int count, @Nullable BinaryTagHolder nbt) {
/* 441 */       return new ShowItem(((Keyed)Objects.<Keyed>requireNonNull(item, "item")).key(), count, nbt);
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
/*     */     @Deprecated
/*     */     @ScheduledForRemoval(inVersion = "5.0.0")
/*     */     @NotNull
/*     */     public static ShowItem of(@NotNull Keyed item, int count, @Nullable BinaryTagHolder nbt) {
/* 457 */       return new ShowItem(((Keyed)Objects.<Keyed>requireNonNull(item, "item")).key(), count, nbt);
/*     */     }
/*     */     
/*     */     private ShowItem(@NotNull Key item, int count, @Nullable BinaryTagHolder nbt) {
/* 461 */       this.item = item;
/* 462 */       this.count = count;
/* 463 */       this.nbt = nbt;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @NotNull
/*     */     public Key item() {
/* 473 */       return this.item;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @NotNull
/*     */     public ShowItem item(@NotNull Key item) {
/* 484 */       if (((Key)Objects.<Key>requireNonNull(item, "item")).equals(this.item)) return this; 
/* 485 */       return new ShowItem(item, this.count, this.nbt);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int count() {
/* 495 */       return this.count;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @NotNull
/*     */     public ShowItem count(int count) {
/* 506 */       if (count == this.count) return this; 
/* 507 */       return new ShowItem(this.item, count, this.nbt);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public BinaryTagHolder nbt() {
/* 517 */       return this.nbt;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @NotNull
/*     */     public ShowItem nbt(@Nullable BinaryTagHolder nbt) {
/* 528 */       if (Objects.equals(nbt, this.nbt)) return this; 
/* 529 */       return new ShowItem(this.item, this.count, nbt);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object other) {
/* 534 */       if (this == other) return true; 
/* 535 */       if (other == null || getClass() != other.getClass()) return false; 
/* 536 */       ShowItem that = (ShowItem)other;
/* 537 */       return (this.item.equals(that.item) && this.count == that.count && Objects.equals(this.nbt, that.nbt));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 542 */       int result = this.item.hashCode();
/* 543 */       result = 31 * result + Integer.hashCode(this.count);
/* 544 */       result = 31 * result + Objects.hashCode(this.nbt);
/* 545 */       return result;
/*     */     }
/*     */     
/*     */     @NotNull
/*     */     public Stream<? extends ExaminableProperty> examinableProperties() {
/* 550 */       return Stream.of(new ExaminableProperty[] {
/* 551 */             ExaminableProperty.of("item", this.item), 
/* 552 */             ExaminableProperty.of("count", this.count), 
/* 553 */             ExaminableProperty.of("nbt", this.nbt)
/*     */           });
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 559 */       return Internals.toString(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class ShowEntity
/*     */     implements Examinable
/*     */   {
/*     */     private final Key type;
/*     */ 
/*     */ 
/*     */     
/*     */     private final UUID id;
/*     */ 
/*     */     
/*     */     private final Component name;
/*     */ 
/*     */ 
/*     */     
/*     */     @NotNull
/*     */     public static ShowEntity showEntity(@NotNull Key type, @NotNull UUID id) {
/* 582 */       return showEntity(type, id, (Component)null);
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
/*     */     @Deprecated
/*     */     @ScheduledForRemoval(inVersion = "5.0.0")
/*     */     @NotNull
/*     */     public static ShowEntity of(@NotNull Key type, @NotNull UUID id) {
/* 597 */       return of(type, id, (Component)null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @NotNull
/*     */     public static ShowEntity showEntity(@NotNull Keyed type, @NotNull UUID id) {
/* 609 */       return showEntity(type, id, (Component)null);
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
/*     */     @Deprecated
/*     */     @ScheduledForRemoval(inVersion = "5.0.0")
/*     */     @NotNull
/*     */     public static ShowEntity of(@NotNull Keyed type, @NotNull UUID id) {
/* 624 */       return of(type, id, (Component)null);
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
/*     */     @NotNull
/*     */     public static ShowEntity showEntity(@NotNull Key type, @NotNull UUID id, @Nullable Component name) {
/* 637 */       return new ShowEntity(Objects.<Key>requireNonNull(type, "type"), Objects.<UUID>requireNonNull(id, "id"), name);
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
/*     */     @Deprecated
/*     */     @ScheduledForRemoval(inVersion = "5.0.0")
/*     */     @NotNull
/*     */     public static ShowEntity of(@NotNull Key type, @NotNull UUID id, @Nullable Component name) {
/* 653 */       return new ShowEntity(Objects.<Key>requireNonNull(type, "type"), Objects.<UUID>requireNonNull(id, "id"), name);
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
/*     */     @NotNull
/*     */     public static ShowEntity showEntity(@NotNull Keyed type, @NotNull UUID id, @Nullable Component name) {
/* 666 */       return new ShowEntity(((Keyed)Objects.<Keyed>requireNonNull(type, "type")).key(), Objects.<UUID>requireNonNull(id, "id"), name);
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
/*     */     @Deprecated
/*     */     @ScheduledForRemoval(inVersion = "5.0.0")
/*     */     @NotNull
/*     */     public static ShowEntity of(@NotNull Keyed type, @NotNull UUID id, @Nullable Component name) {
/* 682 */       return new ShowEntity(((Keyed)Objects.<Keyed>requireNonNull(type, "type")).key(), Objects.<UUID>requireNonNull(id, "id"), name);
/*     */     }
/*     */     
/*     */     private ShowEntity(@NotNull Key type, @NotNull UUID id, @Nullable Component name) {
/* 686 */       this.type = type;
/* 687 */       this.id = id;
/* 688 */       this.name = name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @NotNull
/*     */     public Key type() {
/* 698 */       return this.type;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @NotNull
/*     */     public ShowEntity type(@NotNull Key type) {
/* 709 */       if (((Key)Objects.<Key>requireNonNull(type, "type")).equals(this.type)) return this; 
/* 710 */       return new ShowEntity(type, this.id, this.name);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @NotNull
/*     */     public ShowEntity type(@NotNull Keyed type) {
/* 721 */       return type(((Keyed)Objects.<Keyed>requireNonNull(type, "type")).key());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @NotNull
/*     */     public UUID id() {
/* 731 */       return this.id;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @NotNull
/*     */     public ShowEntity id(@NotNull UUID id) {
/* 742 */       if (((UUID)Objects.<UUID>requireNonNull(id)).equals(this.id)) return this; 
/* 743 */       return new ShowEntity(this.type, id, this.name);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Component name() {
/* 753 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @NotNull
/*     */     public ShowEntity name(@Nullable Component name) {
/* 764 */       if (Objects.equals(name, this.name)) return this; 
/* 765 */       return new ShowEntity(this.type, this.id, name);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object other) {
/* 770 */       if (this == other) return true; 
/* 771 */       if (other == null || getClass() != other.getClass()) return false; 
/* 772 */       ShowEntity that = (ShowEntity)other;
/* 773 */       return (this.type.equals(that.type) && this.id.equals(that.id) && Objects.equals(this.name, that.name));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 778 */       int result = this.type.hashCode();
/* 779 */       result = 31 * result + this.id.hashCode();
/* 780 */       result = 31 * result + Objects.hashCode(this.name);
/* 781 */       return result;
/*     */     }
/*     */     
/*     */     @NotNull
/*     */     public Stream<? extends ExaminableProperty> examinableProperties() {
/* 786 */       return Stream.of(new ExaminableProperty[] {
/* 787 */             ExaminableProperty.of("type", this.type), 
/* 788 */             ExaminableProperty.of("id", this.id), 
/* 789 */             ExaminableProperty.of("name", this.name)
/*     */           });
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 795 */       return Internals.toString(this);
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
/*     */   public static final class Action<V>
/*     */   {
/* 811 */     public static final Action<Component> SHOW_TEXT = new Action("show_text", (Class)Component.class, true, (Renderer)new Renderer<Component>() {
/*     */           @NotNull
/*     */           public <C> Component render(@NotNull ComponentRenderer<C> renderer, @NotNull C context, @NotNull Component value) {
/* 814 */             return renderer.render(value, context);
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 822 */     public static final Action<HoverEvent.ShowItem> SHOW_ITEM = new Action("show_item", (Class)HoverEvent.ShowItem.class, true, (Renderer)new Renderer<HoverEvent.ShowItem>() {
/*     */           @NotNull
/*     */           public <C> HoverEvent.ShowItem render(@NotNull ComponentRenderer<C> renderer, @NotNull C context, @NotNull HoverEvent.ShowItem value) {
/* 825 */             return value;
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 833 */     public static final Action<HoverEvent.ShowEntity> SHOW_ENTITY = new Action("show_entity", (Class)HoverEvent.ShowEntity.class, true, (Renderer)new Renderer<HoverEvent.ShowEntity>() {
/*     */           @NotNull
/*     */           public <C> HoverEvent.ShowEntity render(@NotNull ComponentRenderer<C> renderer, @NotNull C context, @NotNull HoverEvent.ShowEntity value) {
/* 836 */             if (value.name == null) return value; 
/* 837 */             return value.name(renderer.render(value.name, context));
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/* 847 */     public static final Action<String> SHOW_ACHIEVEMENT = new Action("show_achievement", (Class)String.class, true, (Renderer)new Renderer<String>() {
/*     */           @NotNull
/*     */           public <C> String render(@NotNull ComponentRenderer<C> renderer, @NotNull C context, @NotNull String value) {
/* 850 */             return value;
/*     */           }
/*     */         });
/*     */ 
/*     */     
/*     */     public static final Index<String, Action<?>> NAMES;
/*     */     private final String name;
/*     */     
/*     */     static {
/* 859 */       NAMES = Index.create(constant -> constant.name, (Object[])new Action[] { SHOW_TEXT, SHOW_ITEM, SHOW_ENTITY, SHOW_ACHIEVEMENT });
/*     */     }
/*     */ 
/*     */     
/*     */     private final Class<V> type;
/*     */     
/*     */     private final boolean readable;
/*     */     
/*     */     private final Renderer<V> renderer;
/*     */ 
/*     */     
/*     */     Action(String name, Class<V> type, boolean readable, Renderer<V> renderer) {
/* 871 */       this.name = name;
/* 872 */       this.type = type;
/* 873 */       this.readable = readable;
/* 874 */       this.renderer = renderer;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @NotNull
/*     */     public Class<V> type() {
/* 884 */       return this.type;
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
/* 895 */       return this.readable;
/*     */     }
/*     */     
/*     */     @NotNull
/*     */     public String toString() {
/* 900 */       return this.name;
/*     */     }
/*     */     
/*     */     @FunctionalInterface
/*     */     static interface Renderer<V> {
/*     */       @NotNull
/*     */       <C> V render(@NotNull ComponentRenderer<C> param2ComponentRenderer, @NotNull C param2C, @NotNull V param2V);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\text\event\HoverEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */