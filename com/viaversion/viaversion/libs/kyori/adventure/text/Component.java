/*      */ package com.viaversion.viaversion.libs.kyori.adventure.text;
/*      */ 
/*      */ import com.viaversion.viaversion.libs.kyori.adventure.builder.AbstractBuilder;
/*      */ import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
/*      */ import com.viaversion.viaversion.libs.kyori.adventure.text.event.ClickEvent;
/*      */ import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEvent;
/*      */ import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEventSource;
/*      */ import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
/*      */ import com.viaversion.viaversion.libs.kyori.adventure.text.format.StyleBuilderApplicable;
/*      */ import com.viaversion.viaversion.libs.kyori.adventure.text.format.StyleGetter;
/*      */ import com.viaversion.viaversion.libs.kyori.adventure.text.format.StyleSetter;
/*      */ import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextColor;
/*      */ import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextDecoration;
/*      */ import com.viaversion.viaversion.libs.kyori.adventure.translation.Translatable;
/*      */ import com.viaversion.viaversion.libs.kyori.adventure.util.ForwardingIterator;
/*      */ import com.viaversion.viaversion.libs.kyori.adventure.util.IntFunction2;
/*      */ import com.viaversion.viaversion.libs.kyori.adventure.util.MonkeyBars;
/*      */ import com.viaversion.viaversion.libs.kyori.examination.Examinable;
/*      */ import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.Spliterator;
/*      */ import java.util.Spliterators;
/*      */ import java.util.function.BiPredicate;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.function.UnaryOperator;
/*      */ import java.util.regex.Pattern;
/*      */ import java.util.stream.Collector;
/*      */ import java.util.stream.Stream;
/*      */ import org.jetbrains.annotations.ApiStatus.NonExtendable;
/*      */ import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
/*      */ import org.jetbrains.annotations.Contract;
/*      */ import org.jetbrains.annotations.NotNull;
/*      */ import org.jetbrains.annotations.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @NonExtendable
/*      */ public interface Component
/*      */   extends ComponentBuilderApplicable, ComponentLike, Examinable, HoverEventSource<Component>, StyleGetter, StyleSetter<Component>
/*      */ {
/*  116 */   public static final BiPredicate<? super Component, ? super Component> EQUALS = Objects::equals;
/*      */   
/*      */   public static final BiPredicate<? super Component, ? super Component> EQUALS_IDENTITY;
/*      */   public static final Predicate<? super Component> IS_NOT_EMPTY;
/*      */   
/*      */   static {
/*  122 */     EQUALS_IDENTITY = ((a, b) -> (a == b));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  128 */     IS_NOT_EMPTY = (component -> (component != empty()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NotNull
/*      */   static TextComponent empty() {
/*  137 */     return TextComponentImpl.EMPTY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NotNull
/*      */   static TextComponent newline() {
/*  147 */     return TextComponentImpl.NEWLINE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NotNull
/*      */   static TextComponent space() {
/*  157 */     return TextComponentImpl.SPACE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @ScheduledForRemoval(inVersion = "5.0.0")
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent join(@NotNull ComponentLike separator, @NotNull ComponentLike... components) {
/*  173 */     return join(separator, Arrays.asList(components));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @ScheduledForRemoval(inVersion = "5.0.0")
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent join(@NotNull ComponentLike separator, Iterable<? extends ComponentLike> components) {
/*  189 */     Component component = join(JoinConfiguration.separator(separator), components);
/*      */     
/*  191 */     if (component instanceof TextComponent) return (TextComponent)component; 
/*  192 */     return text().append(component).build();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   static Component join(JoinConfiguration.Builder configBuilder, @NotNull ComponentLike... components) {
/*  208 */     return join(configBuilder, Arrays.asList(components));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   static Component join(JoinConfiguration.Builder configBuilder, @NotNull Iterable<? extends ComponentLike> components) {
/*  224 */     return JoinConfigurationImpl.join((JoinConfiguration)configBuilder.build(), components);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   static Component join(@NotNull JoinConfiguration config, @NotNull ComponentLike... components) {
/*  240 */     return join(config, Arrays.asList(components));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   static Component join(@NotNull JoinConfiguration config, @NotNull Iterable<? extends ComponentLike> components) {
/*  256 */     return JoinConfigurationImpl.join(config, components);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NotNull
/*      */   static Collector<Component, ? extends ComponentBuilder<?, ?>, Component> toComponent() {
/*  266 */     return toComponent(empty());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NotNull
/*      */   static Collector<Component, ? extends ComponentBuilder<?, ?>, Component> toComponent(@NotNull Component separator) {
/*  277 */     return Collector.of(Component::text, (builder, add) -> { if (separator != empty() && !builder.children().isEmpty()) builder.append(separator);  builder.append(add); }(a, b) -> { List<Component> aChildren = a.children(); TextComponent.Builder ret = text().append((Iterable)aChildren); if (!aChildren.isEmpty()) ret.append(separator);  ret.append((Iterable)b.children()); return ret; }ComponentBuilder::build, new Collector.Characteristics[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   static BlockNBTComponent.Builder blockNBT() {
/*  311 */     return new BlockNBTComponentImpl.BuilderImpl();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract("_ -> new")
/*      */   @NotNull
/*      */   static BlockNBTComponent blockNBT(@NotNull Consumer<? super BlockNBTComponent.Builder> consumer) {
/*  323 */     return (BlockNBTComponent)AbstractBuilder.configureAndBuild(blockNBT(), consumer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static BlockNBTComponent blockNBT(@NotNull String nbtPath, BlockNBTComponent.Pos pos) {
/*  336 */     return blockNBT(nbtPath, false, pos);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static BlockNBTComponent blockNBT(@NotNull String nbtPath, boolean interpret, BlockNBTComponent.Pos pos) {
/*  350 */     return blockNBT(nbtPath, interpret, null, pos);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static BlockNBTComponent blockNBT(@NotNull String nbtPath, boolean interpret, @Nullable ComponentLike separator, BlockNBTComponent.Pos pos) {
/*  365 */     return BlockNBTComponentImpl.create(Collections.emptyList(), Style.empty(), nbtPath, interpret, separator, pos);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   static EntityNBTComponent.Builder entityNBT() {
/*  382 */     return new EntityNBTComponentImpl.BuilderImpl();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract("_ -> new")
/*      */   @NotNull
/*      */   static EntityNBTComponent entityNBT(@NotNull Consumer<? super EntityNBTComponent.Builder> consumer) {
/*  394 */     return (EntityNBTComponent)AbstractBuilder.configureAndBuild(entityNBT(), consumer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract("_, _ -> new")
/*      */   @NotNull
/*      */   static EntityNBTComponent entityNBT(@NotNull String nbtPath, @NotNull String selector) {
/*  407 */     return entityNBT().nbtPath(nbtPath).selector(selector).build();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   static KeybindComponent.Builder keybind() {
/*  424 */     return new KeybindComponentImpl.BuilderImpl();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract("_ -> new")
/*      */   @NotNull
/*      */   static KeybindComponent keybind(@NotNull Consumer<? super KeybindComponent.Builder> consumer) {
/*  436 */     return (KeybindComponent)AbstractBuilder.configureAndBuild(keybind(), consumer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_ -> new", pure = true)
/*      */   @NotNull
/*      */   static KeybindComponent keybind(@NotNull String keybind) {
/*  448 */     return keybind(keybind, Style.empty());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_ -> new", pure = true)
/*      */   @NotNull
/*      */   static KeybindComponent keybind(KeybindComponent.KeybindLike keybind) {
/*  460 */     return keybind(((KeybindComponent.KeybindLike)Objects.<KeybindComponent.KeybindLike>requireNonNull(keybind, "keybind")).asKeybind(), Style.empty());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static KeybindComponent keybind(@NotNull String keybind, @NotNull Style style) {
/*  473 */     return KeybindComponentImpl.create(Collections.emptyList(), Objects.<Style>requireNonNull(style, "style"), keybind);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static KeybindComponent keybind(KeybindComponent.KeybindLike keybind, @NotNull Style style) {
/*  486 */     return KeybindComponentImpl.create(Collections.emptyList(), Objects.<Style>requireNonNull(style, "style"), ((KeybindComponent.KeybindLike)Objects.<KeybindComponent.KeybindLike>requireNonNull(keybind, "keybind")).asKeybind());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static KeybindComponent keybind(@NotNull String keybind, @Nullable TextColor color) {
/*  499 */     return keybind(keybind, Style.style(color));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static KeybindComponent keybind(KeybindComponent.KeybindLike keybind, @Nullable TextColor color) {
/*  512 */     return keybind(((KeybindComponent.KeybindLike)Objects.<KeybindComponent.KeybindLike>requireNonNull(keybind, "keybind")).asKeybind(), Style.style(color));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static KeybindComponent keybind(@NotNull String keybind, @Nullable TextColor color, TextDecoration... decorations) {
/*  526 */     return keybind(keybind, Style.style(color, decorations));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static KeybindComponent keybind(KeybindComponent.KeybindLike keybind, @Nullable TextColor color, TextDecoration... decorations) {
/*  540 */     return keybind(((KeybindComponent.KeybindLike)Objects.<KeybindComponent.KeybindLike>requireNonNull(keybind, "keybind")).asKeybind(), Style.style(color, decorations));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static KeybindComponent keybind(@NotNull String keybind, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations) {
/*  554 */     return keybind(keybind, Style.style(color, decorations));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static KeybindComponent keybind(KeybindComponent.KeybindLike keybind, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations) {
/*  568 */     return keybind(((KeybindComponent.KeybindLike)Objects.<KeybindComponent.KeybindLike>requireNonNull(keybind, "keybind")).asKeybind(), Style.style(color, decorations));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   static ScoreComponent.Builder score() {
/*  585 */     return new ScoreComponentImpl.BuilderImpl();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract("_ -> new")
/*      */   @NotNull
/*      */   static ScoreComponent score(@NotNull Consumer<? super ScoreComponent.Builder> consumer) {
/*  597 */     return (ScoreComponent)AbstractBuilder.configureAndBuild(score(), consumer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static ScoreComponent score(@NotNull String name, @NotNull String objective) {
/*  610 */     return score(name, objective, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static ScoreComponent score(@NotNull String name, @NotNull String objective, @Nullable String value) {
/*  626 */     return ScoreComponentImpl.create(Collections.emptyList(), Style.empty(), name, objective, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   static SelectorComponent.Builder selector() {
/*  643 */     return new SelectorComponentImpl.BuilderImpl();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract("_ -> new")
/*      */   @NotNull
/*      */   static SelectorComponent selector(@NotNull Consumer<? super SelectorComponent.Builder> consumer) {
/*  655 */     return (SelectorComponent)AbstractBuilder.configureAndBuild(selector(), consumer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_ -> new", pure = true)
/*      */   @NotNull
/*      */   static SelectorComponent selector(@NotNull String pattern) {
/*  667 */     return selector(pattern, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static SelectorComponent selector(@NotNull String pattern, @Nullable ComponentLike separator) {
/*  680 */     return SelectorComponentImpl.create(Collections.emptyList(), Style.empty(), pattern, separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   static StorageNBTComponent.Builder storageNBT() {
/*  697 */     return new StorageNBTComponentImpl.BuilderImpl();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract("_ -> new")
/*      */   @NotNull
/*      */   static StorageNBTComponent storageNBT(@NotNull Consumer<? super StorageNBTComponent.Builder> consumer) {
/*  709 */     return (StorageNBTComponent)AbstractBuilder.configureAndBuild(storageNBT(), consumer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static StorageNBTComponent storageNBT(@NotNull String nbtPath, @NotNull Key storage) {
/*  722 */     return storageNBT(nbtPath, false, storage);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static StorageNBTComponent storageNBT(@NotNull String nbtPath, boolean interpret, @NotNull Key storage) {
/*  736 */     return storageNBT(nbtPath, interpret, null, storage);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static StorageNBTComponent storageNBT(@NotNull String nbtPath, boolean interpret, @Nullable ComponentLike separator, @NotNull Key storage) {
/*  751 */     return StorageNBTComponentImpl.create(Collections.emptyList(), Style.empty(), nbtPath, interpret, separator, storage);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   static TextComponent.Builder text() {
/*  768 */     return new TextComponentImpl.BuilderImpl();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NotNull
/*      */   static TextComponent textOfChildren(@NotNull ComponentLike... components) {
/*  779 */     if (components.length == 0) return empty(); 
/*  780 */     return TextComponentImpl.create(Arrays.asList(components), Style.empty(), "");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract("_ -> new")
/*      */   @NotNull
/*      */   static TextComponent text(@NotNull Consumer<? super TextComponent.Builder> consumer) {
/*  792 */     return (TextComponent)AbstractBuilder.configureAndBuild(text(), consumer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(@NotNull String content) {
/*  804 */     if (content.isEmpty()) return empty(); 
/*  805 */     return text(content, Style.empty());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(@NotNull String content, @NotNull Style style) {
/*  818 */     return TextComponentImpl.create(Collections.emptyList(), Objects.<Style>requireNonNull(style, "style"), content);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(@NotNull String content, @Nullable TextColor color) {
/*  831 */     return text(content, Style.style(color));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(@NotNull String content, @Nullable TextColor color, TextDecoration... decorations) {
/*  845 */     return text(content, Style.style(color, decorations));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(@NotNull String content, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations) {
/*  859 */     return text(content, Style.style(color, decorations));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(boolean value) {
/*  871 */     return text(String.valueOf(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(boolean value, @NotNull Style style) {
/*  884 */     return text(String.valueOf(value), style);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(boolean value, @Nullable TextColor color) {
/*  897 */     return text(String.valueOf(value), color);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(boolean value, @Nullable TextColor color, TextDecoration... decorations) {
/*  911 */     return text(String.valueOf(value), color, decorations);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(boolean value, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations) {
/*  925 */     return text(String.valueOf(value), color, decorations);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(char value) {
/*  937 */     if (value == '\n') return newline(); 
/*  938 */     if (value == ' ') return space(); 
/*  939 */     return text(String.valueOf(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(char value, @NotNull Style style) {
/*  952 */     return text(String.valueOf(value), style);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(char value, @Nullable TextColor color) {
/*  965 */     return text(String.valueOf(value), color);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(char value, @Nullable TextColor color, TextDecoration... decorations) {
/*  979 */     return text(String.valueOf(value), color, decorations);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(char value, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations) {
/*  993 */     return text(String.valueOf(value), color, decorations);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(double value) {
/* 1005 */     return text(String.valueOf(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(double value, @NotNull Style style) {
/* 1018 */     return text(String.valueOf(value), style);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(double value, @Nullable TextColor color) {
/* 1031 */     return text(String.valueOf(value), color);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(double value, @Nullable TextColor color, TextDecoration... decorations) {
/* 1045 */     return text(String.valueOf(value), color, decorations);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(double value, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations) {
/* 1059 */     return text(String.valueOf(value), color, decorations);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(float value) {
/* 1071 */     return text(String.valueOf(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(float value, @NotNull Style style) {
/* 1084 */     return text(String.valueOf(value), style);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(float value, @Nullable TextColor color) {
/* 1097 */     return text(String.valueOf(value), color);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(float value, @Nullable TextColor color, TextDecoration... decorations) {
/* 1111 */     return text(String.valueOf(value), color, decorations);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(float value, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations) {
/* 1125 */     return text(String.valueOf(value), color, decorations);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(int value) {
/* 1137 */     return text(String.valueOf(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(int value, @NotNull Style style) {
/* 1150 */     return text(String.valueOf(value), style);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(int value, @Nullable TextColor color) {
/* 1163 */     return text(String.valueOf(value), color);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(int value, @Nullable TextColor color, TextDecoration... decorations) {
/* 1177 */     return text(String.valueOf(value), color, decorations);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(int value, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations) {
/* 1191 */     return text(String.valueOf(value), color, decorations);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(long value) {
/* 1203 */     return text(String.valueOf(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(long value, @NotNull Style style) {
/* 1216 */     return text(String.valueOf(value), style);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(long value, @Nullable TextColor color) {
/* 1229 */     return text(String.valueOf(value), color);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(long value, @Nullable TextColor color, TextDecoration... decorations) {
/* 1243 */     return text(String.valueOf(value), color, decorations);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TextComponent text(long value, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations) {
/* 1257 */     return text(String.valueOf(value), color, decorations);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   static TranslatableComponent.Builder translatable() {
/* 1274 */     return new TranslatableComponentImpl.BuilderImpl();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract("_ -> new")
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull Consumer<? super TranslatableComponent.Builder> consumer) {
/* 1286 */     return (TranslatableComponent)AbstractBuilder.configureAndBuild(translatable(), consumer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull String key) {
/* 1298 */     return translatable(key, Style.empty());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull Translatable translatable) {
/* 1310 */     return translatable(((Translatable)Objects.<Translatable>requireNonNull(translatable, "translatable")).translationKey(), Style.empty());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull String key, @Nullable String fallback) {
/* 1324 */     return translatable(key, fallback, Style.empty());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable String fallback) {
/* 1338 */     return translatable(((Translatable)Objects.<Translatable>requireNonNull(translatable, "translatable")).translationKey(), fallback, Style.empty());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull String key, @NotNull Style style) {
/* 1351 */     return TranslatableComponentImpl.create(Collections.emptyList(), Objects.<Style>requireNonNull(style, "style"), key, (String)null, Collections.emptyList());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull Translatable translatable, @NotNull Style style) {
/* 1364 */     return translatable(((Translatable)Objects.<Translatable>requireNonNull(translatable, "translatable")).translationKey(), style);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull String key, @Nullable String fallback, @NotNull Style style) {
/* 1379 */     return TranslatableComponentImpl.create(Collections.emptyList(), Objects.<Style>requireNonNull(style, "style"), key, fallback, Collections.emptyList());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable String fallback, @NotNull Style style) {
/* 1394 */     return translatable(((Translatable)Objects.<Translatable>requireNonNull(translatable, "translatable")).translationKey(), fallback, style);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull String key, @Nullable String fallback, @NotNull StyleBuilderApplicable... style) {
/* 1409 */     return translatable(Objects.<String>requireNonNull(key, "key"), fallback, Style.style(style));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable String fallback, @NotNull Iterable<StyleBuilderApplicable> style) {
/* 1424 */     return translatable(((Translatable)Objects.<Translatable>requireNonNull(translatable, "translatable")).translationKey(), fallback, Style.style(style));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull String key, @Nullable String fallback, @NotNull ComponentLike... args) {
/* 1439 */     return translatable(key, fallback, Style.empty(), args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable String fallback, @NotNull ComponentLike... args) {
/* 1454 */     return translatable(((Translatable)Objects.<Translatable>requireNonNull(translatable, "translatable")).translationKey(), fallback, args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull String key, @Nullable String fallback, @NotNull Style style, @NotNull ComponentLike... args) {
/* 1470 */     return TranslatableComponentImpl.create(Collections.emptyList(), Objects.<Style>requireNonNull(style, "style"), key, fallback, Objects.<ComponentLike[]>requireNonNull(args, "args"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable String fallback, @NotNull Style style, @NotNull ComponentLike... args) {
/* 1486 */     return translatable(((Translatable)Objects.<Translatable>requireNonNull(translatable, "translatable")).translationKey(), fallback, style, args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull String key, @Nullable String fallback, @NotNull Style style, @NotNull List<? extends ComponentLike> args) {
/* 1502 */     return TranslatableComponentImpl.create(Collections.emptyList(), style, key, fallback, Objects.<List<? extends ComponentLike>>requireNonNull(args, "args"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable String fallback, @NotNull Style style, @NotNull List<? extends ComponentLike> args) {
/* 1518 */     return translatable(((Translatable)Objects.<Translatable>requireNonNull(translatable, "translatable")).translationKey(), fallback, style, args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull String key, @Nullable String fallback, @NotNull List<? extends ComponentLike> args, @NotNull Iterable<StyleBuilderApplicable> style) {
/* 1534 */     return TranslatableComponentImpl.create(Collections.emptyList(), Style.style(style), key, fallback, Objects.<List<? extends ComponentLike>>requireNonNull(args, "args"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable String fallback, @NotNull List<? extends ComponentLike> args, @NotNull Iterable<StyleBuilderApplicable> style) {
/* 1550 */     return translatable(((Translatable)Objects.<Translatable>requireNonNull(translatable, "translatable")).translationKey(), fallback, args, style);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull String key, @Nullable String fallback, @NotNull List<? extends ComponentLike> args, @NotNull StyleBuilderApplicable... style) {
/* 1566 */     return TranslatableComponentImpl.create(Collections.emptyList(), Style.style(style), key, fallback, Objects.<List<? extends ComponentLike>>requireNonNull(args, "args"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable String fallback, @NotNull List<? extends ComponentLike> args, @NotNull StyleBuilderApplicable... style) {
/* 1582 */     return translatable(((Translatable)Objects.<Translatable>requireNonNull(translatable, "translatable")).translationKey(), fallback, args, style);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull String key, @Nullable TextColor color) {
/* 1595 */     return translatable(key, Style.style(color));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable TextColor color) {
/* 1608 */     return translatable(((Translatable)Objects.<Translatable>requireNonNull(translatable, "translatable")).translationKey(), color);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull String key, @Nullable TextColor color, TextDecoration... decorations) {
/* 1622 */     return translatable(key, Style.style(color, decorations));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable TextColor color, TextDecoration... decorations) {
/* 1636 */     return translatable(((Translatable)Objects.<Translatable>requireNonNull(translatable, "translatable")).translationKey(), color, decorations);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull String key, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations) {
/* 1650 */     return translatable(key, Style.style(color, decorations));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations) {
/* 1664 */     return translatable(((Translatable)Objects.<Translatable>requireNonNull(translatable, "translatable")).translationKey(), color, decorations);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull String key, @NotNull ComponentLike... args) {
/* 1677 */     return translatable(key, Style.empty(), args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull Translatable translatable, @NotNull ComponentLike... args) {
/* 1690 */     return translatable(((Translatable)Objects.<Translatable>requireNonNull(translatable, "translatable")).translationKey(), args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull String key, @NotNull Style style, @NotNull ComponentLike... args) {
/* 1704 */     return TranslatableComponentImpl.create(Collections.emptyList(), Objects.<Style>requireNonNull(style, "style"), key, (String)null, Objects.<ComponentLike[]>requireNonNull(args, "args"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull Translatable translatable, @NotNull Style style, @NotNull ComponentLike... args) {
/* 1718 */     return translatable(((Translatable)Objects.<Translatable>requireNonNull(translatable, "translatable")).translationKey(), style, args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull String key, @Nullable TextColor color, @NotNull ComponentLike... args) {
/* 1732 */     return translatable(key, Style.style(color), args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable TextColor color, @NotNull ComponentLike... args) {
/* 1746 */     return translatable(((Translatable)Objects.<Translatable>requireNonNull(translatable, "translatable")).translationKey(), color, args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull String key, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations, @NotNull ComponentLike... args) {
/* 1761 */     return translatable(key, Style.style(color, decorations), args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations, @NotNull ComponentLike... args) {
/* 1776 */     return translatable(((Translatable)Objects.<Translatable>requireNonNull(translatable, "translatable")).translationKey(), color, decorations, args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull String key, @NotNull List<? extends ComponentLike> args) {
/* 1789 */     return TranslatableComponentImpl.create(Collections.emptyList(), Style.empty(), key, (String)null, Objects.<List<? extends ComponentLike>>requireNonNull(args, "args"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull Translatable translatable, @NotNull List<? extends ComponentLike> args) {
/* 1802 */     return translatable(((Translatable)Objects.<Translatable>requireNonNull(translatable, "translatable")).translationKey(), args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull String key, @NotNull Style style, @NotNull List<? extends ComponentLike> args) {
/* 1816 */     return TranslatableComponentImpl.create(Collections.emptyList(), Objects.<Style>requireNonNull(style, "style"), key, (String)null, Objects.<List<? extends ComponentLike>>requireNonNull(args, "args"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull Translatable translatable, @NotNull Style style, @NotNull List<? extends ComponentLike> args) {
/* 1830 */     return translatable(((Translatable)Objects.<Translatable>requireNonNull(translatable, "translatable")).translationKey(), style, args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   static TranslatableComponent translatable(@NotNull String key, @Nullable TextColor color, @NotNull List<? extends ComponentLike> args) {
/* 1844 */     return translatable(key, Style.style(color), args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _ -> new", pure = true)
/*      */   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable TextColor color, @NotNull List<? extends ComponentLike> args) {
/* 1858 */     return translatable(((Translatable)Objects.<Translatable>requireNonNull(translatable, "translatable")).translationKey(), color, args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull String key, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations, @NotNull List<? extends ComponentLike> args) {
/* 1873 */     return translatable(key, Style.style(color, decorations), args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(value = "_, _, _, _ -> new", pure = true)
/*      */   @NotNull
/*      */   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations, @NotNull List<? extends ComponentLike> args) {
/* 1888 */     return translatable(((Translatable)Objects.<Translatable>requireNonNull(translatable, "translatable")).translationKey(), color, decorations, args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   default boolean contains(@NotNull Component that) {
/* 1923 */     return contains(that, EQUALS_IDENTITY);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   default boolean contains(@NotNull Component that, @NotNull BiPredicate<? super Component, ? super Component> equals) {
/* 1936 */     if (equals.test(this, that)) return true; 
/* 1937 */     for (Component child : children()) {
/* 1938 */       if (child.contains(that, equals)) return true; 
/*      */     } 
/* 1940 */     HoverEvent<?> hoverEvent = hoverEvent();
/* 1941 */     if (hoverEvent != null) {
/* 1942 */       Object value = hoverEvent.value();
/* 1943 */       Component component = null;
/* 1944 */       if (value instanceof Component) {
/* 1945 */         component = (Component)hoverEvent.value();
/* 1946 */       } else if (value instanceof HoverEvent.ShowEntity) {
/* 1947 */         component = ((HoverEvent.ShowEntity)value).name();
/*      */       } 
/* 1949 */       if (component != null) {
/* 1950 */         if (equals.test(that, component)) return true; 
/* 1951 */         for (Component child : component.children()) {
/* 1952 */           if (child.contains(that, equals)) return true; 
/*      */         } 
/*      */       } 
/*      */     } 
/* 1956 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @ScheduledForRemoval(inVersion = "5.0.0")
/*      */   default void detectCycle(@NotNull Component that) {
/* 1969 */     if (that.contains(this)) {
/* 1970 */       throw new IllegalStateException("Component cycle detected between " + this + " and " + that);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   default Component append(@NotNull Component component) {
/* 1983 */     return append(component);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NotNull
/*      */   default Component append(@NotNull ComponentLike like) {
/* 1994 */     Objects.requireNonNull(like, "like");
/* 1995 */     Component component = like.asComponent();
/* 1996 */     Objects.requireNonNull(component, "component");
/* 1997 */     if (component == empty()) return this; 
/* 1998 */     List<Component> oldChildren = children();
/* 1999 */     return children(MonkeyBars.addOne(oldChildren, component));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   default Component append(@NotNull ComponentBuilder<?, ?> builder) {
/* 2011 */     return append((Component)builder.build());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   default Component appendNewline() {
/* 2022 */     return append(newline());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   default Component appendSpace() {
/* 2033 */     return append(space());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   default Component applyFallbackStyle(@NotNull Style style) {
/* 2047 */     Objects.requireNonNull(style, "style");
/* 2048 */     return style(style().merge(style, Style.Merge.Strategy.IF_ABSENT_ON_TARGET));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   Component applyFallbackStyle(@NotNull StyleBuilderApplicable... style) {
/* 2062 */     return applyFallbackStyle(Style.style(style));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   default Component style(@NotNull Consumer<Style.Builder> consumer) {
/* 2092 */     return style(style().edit(consumer));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   default Component style(@NotNull Consumer<Style.Builder> consumer, Style.Merge.Strategy strategy) {
/* 2105 */     return style(style().edit(consumer, strategy));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   default Component style(Style.Builder style) {
/* 2117 */     return style(style.build());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   default Component mergeStyle(@NotNull Component that) {
/* 2129 */     return mergeStyle(that, Style.Merge.all());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   Component mergeStyle(@NotNull Component that, Style.Merge... merges) {
/* 2142 */     return mergeStyle(that, Style.Merge.merges(merges));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   default Component mergeStyle(@NotNull Component that, @NotNull Set<Style.Merge> merges) {
/* 2155 */     return style(style().merge(that.style(), merges));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default Key font() {
/* 2166 */     return style().font();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NotNull
/*      */   default Component font(@Nullable Key key) {
/* 2178 */     return style(style().font(key));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default TextColor color() {
/* 2189 */     return style().color();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   default Component color(@Nullable TextColor color) {
/* 2202 */     return style(style().color(color));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   default Component colorIfAbsent(@Nullable TextColor color) {
/* 2215 */     if (color() == null) return color(color); 
/* 2216 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   default boolean hasDecoration(@NotNull TextDecoration decoration) {
/* 2229 */     return super.hasDecoration(decoration);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   default Component decorate(@NotNull TextDecoration decoration) {
/* 2242 */     return (Component)super.decorate(decoration);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   default TextDecoration.State decoration(@NotNull TextDecoration decoration) {
/* 2256 */     return style().decoration(decoration);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   default Component decoration(@NotNull TextDecoration decoration, boolean flag) {
/* 2271 */     return (Component)super.decoration(decoration, flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   default Component decoration(@NotNull TextDecoration decoration, TextDecoration.State state) {
/* 2288 */     return style(style().decoration(decoration, state));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NotNull
/*      */   default Component decorationIfAbsent(@NotNull TextDecoration decoration, TextDecoration.State state) {
/* 2302 */     Objects.requireNonNull(state, "state");
/*      */     
/* 2304 */     TextDecoration.State oldState = decoration(decoration);
/* 2305 */     if (oldState == TextDecoration.State.NOT_SET) {
/* 2306 */       return style(style().decoration(decoration, state));
/*      */     }
/* 2308 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NotNull
/*      */   default Map<TextDecoration, TextDecoration.State> decorations() {
/* 2319 */     return style().decorations();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   default Component decorations(@NotNull Map<TextDecoration, TextDecoration.State> decorations) {
/* 2334 */     return style(style().decorations(decorations));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default ClickEvent clickEvent() {
/* 2345 */     return style().clickEvent();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   default Component clickEvent(@Nullable ClickEvent event) {
/* 2358 */     return style(style().clickEvent(event));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default HoverEvent<?> hoverEvent() {
/* 2369 */     return style().hoverEvent();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   default Component hoverEvent(@Nullable HoverEventSource<?> source) {
/* 2382 */     return style(style().hoverEvent(source));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default String insertion() {
/* 2393 */     return style().insertion();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   default Component insertion(@Nullable String insertion) {
/* 2406 */     return style(style().insertion(insertion));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   default boolean hasStyling() {
/* 2417 */     return !style().isEmpty();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   default Component replaceText(@NotNull Consumer<TextReplacementConfig.Builder> configurer) {
/* 2429 */     Objects.requireNonNull(configurer, "configurer");
/* 2430 */     return replaceText((TextReplacementConfig)AbstractBuilder.configureAndBuild(TextReplacementConfig.builder(), configurer));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   default Component replaceText(@NotNull TextReplacementConfig config) {
/* 2442 */     Objects.requireNonNull(config, "replacement");
/* 2443 */     if (!(config instanceof TextReplacementConfigImpl)) {
/* 2444 */       throw new IllegalArgumentException("Provided replacement was a custom TextReplacementConfig implementation, which is not supported.");
/*      */     }
/* 2446 */     return TextReplacementRenderer.INSTANCE.render(this, ((TextReplacementConfigImpl)config).createState());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NotNull
/*      */   default Component compact() {
/* 2456 */     return ComponentCompaction.compact(this, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NotNull
/*      */   Iterable<Component> iterable(@NotNull ComponentIteratorType type, @NotNull ComponentIteratorFlag... flags) {
/* 2468 */     return iterable(type, (flags == null) ? Collections.<ComponentIteratorFlag>emptySet() : MonkeyBars.enumSet(ComponentIteratorFlag.class, (Enum[])flags));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NotNull
/*      */   default Iterable<Component> iterable(@NotNull ComponentIteratorType type, @NotNull Set<ComponentIteratorFlag> flags) {
/* 2480 */     Objects.requireNonNull(type, "type");
/* 2481 */     Objects.requireNonNull(flags, "flags");
/* 2482 */     return (Iterable<Component>)new ForwardingIterator(() -> iterator(type, flags), () -> spliterator(type, flags));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NotNull
/*      */   Iterator<Component> iterator(@NotNull ComponentIteratorType type, @NotNull ComponentIteratorFlag... flags) {
/* 2496 */     return iterator(type, (flags == null) ? Collections.<ComponentIteratorFlag>emptySet() : MonkeyBars.enumSet(ComponentIteratorFlag.class, (Enum[])flags));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NotNull
/*      */   default Iterator<Component> iterator(@NotNull ComponentIteratorType type, @NotNull Set<ComponentIteratorFlag> flags) {
/* 2510 */     return new ComponentIterator(this, Objects.<ComponentIteratorType>requireNonNull(type, "type"), Objects.<Set<ComponentIteratorFlag>>requireNonNull(flags, "flags"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NotNull
/*      */   Spliterator<Component> spliterator(@NotNull ComponentIteratorType type, @NotNull ComponentIteratorFlag... flags) {
/* 2524 */     return spliterator(type, (flags == null) ? Collections.<ComponentIteratorFlag>emptySet() : MonkeyBars.enumSet(ComponentIteratorFlag.class, (Enum[])flags));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NotNull
/*      */   default Spliterator<Component> spliterator(@NotNull ComponentIteratorType type, @NotNull Set<ComponentIteratorFlag> flags) {
/* 2538 */     return Spliterators.spliteratorUnknownSize(iterator(type, flags), 1296);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @ScheduledForRemoval(inVersion = "5.0.0")
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   default Component replaceText(@NotNull String search, @Nullable ComponentLike replacement) {
/* 2554 */     return replaceText(b -> b.matchLiteral(search).replacement(replacement));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @ScheduledForRemoval(inVersion = "5.0.0")
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   default Component replaceText(@NotNull Pattern pattern, @NotNull Function<TextComponent.Builder, ComponentLike> replacement) {
/* 2570 */     return replaceText(b -> b.match(pattern).replacement(replacement));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @ScheduledForRemoval(inVersion = "5.0.0")
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   default Component replaceFirstText(@NotNull String search, @Nullable ComponentLike replacement) {
/* 2586 */     return replaceText(b -> b.matchLiteral(search).once().replacement(replacement));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @ScheduledForRemoval(inVersion = "5.0.0")
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   default Component replaceFirstText(@NotNull Pattern pattern, @NotNull Function<TextComponent.Builder, ComponentLike> replacement) {
/* 2602 */     return replaceText(b -> b.match(pattern).once().replacement(replacement));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @ScheduledForRemoval(inVersion = "5.0.0")
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   default Component replaceText(@NotNull String search, @Nullable ComponentLike replacement, int numberOfReplacements) {
/* 2619 */     return replaceText(b -> b.matchLiteral(search).times(numberOfReplacements).replacement(replacement));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @ScheduledForRemoval(inVersion = "5.0.0")
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   default Component replaceText(@NotNull Pattern pattern, @NotNull Function<TextComponent.Builder, ComponentLike> replacement, int numberOfReplacements) {
/* 2636 */     return replaceText(b -> b.match(pattern).times(numberOfReplacements).replacement(replacement));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @ScheduledForRemoval(inVersion = "5.0.0")
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   default Component replaceText(@NotNull String search, @Nullable ComponentLike replacement, @NotNull IntFunction2<PatternReplacementResult> fn) {
/* 2655 */     return replaceText(b -> b.matchLiteral(search).replacement(replacement).condition(fn));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @ScheduledForRemoval(inVersion = "5.0.0")
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   default Component replaceText(@NotNull Pattern pattern, @NotNull Function<TextComponent.Builder, ComponentLike> replacement, @NotNull IntFunction2<PatternReplacementResult> fn) {
/* 2674 */     return replaceText(b -> b.match(pattern).replacement(replacement).condition(fn));
/*      */   }
/*      */ 
/*      */   
/*      */   default void componentBuilderApply(@NotNull ComponentBuilder<?, ?> component) {
/* 2679 */     component.append(this);
/*      */   }
/*      */   
/*      */   @NotNull
/*      */   default Component asComponent() {
/* 2684 */     return this;
/*      */   }
/*      */   
/*      */   @NotNull
/*      */   default HoverEvent<Component> asHoverEvent(@NotNull UnaryOperator<Component> op) {
/* 2689 */     return HoverEvent.showText(op.apply(this));
/*      */   }
/*      */   
/*      */   @NotNull
/*      */   default Stream<? extends ExaminableProperty> examinableProperties() {
/* 2694 */     return Stream.of(new ExaminableProperty[] {
/* 2695 */           ExaminableProperty.of("style", style()), 
/* 2696 */           ExaminableProperty.of("children", children())
/*      */         });
/*      */   }
/*      */   
/*      */   @NotNull
/*      */   List<Component> children();
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   Component children(@NotNull List<? extends ComponentLike> paramList);
/*      */   
/*      */   @NotNull
/*      */   Style style();
/*      */   
/*      */   @Contract(pure = true)
/*      */   @NotNull
/*      */   Component style(@NotNull Style paramStyle);
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\text\Component.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */