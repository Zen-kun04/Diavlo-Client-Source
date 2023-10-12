/*     */ package com.viaversion.viaversion.libs.kyori.adventure.text.renderer;
/*     */ 
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.BlockNBTComponent;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.ComponentBuilder;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.ComponentLike;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.EntityNBTComponent;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.KeybindComponent;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.ScoreComponent;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.SelectorComponent;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.StorageNBTComponent;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.TextComponent;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.TranslatableComponent;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEvent;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEventSource;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.translation.Translator;
/*     */ import java.text.AttributedCharacterIterator;
/*     */ import java.text.FieldPosition;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
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
/*     */ public abstract class TranslatableComponentRenderer<C>
/*     */   extends AbstractComponentRenderer<C>
/*     */ {
/*  60 */   private static final Set<Style.Merge> MERGES = Style.Merge.merges(new Style.Merge[] { Style.Merge.COLOR, Style.Merge.DECORATIONS, Style.Merge.INSERTION, Style.Merge.FONT });
/*     */ 
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
/*     */   public static TranslatableComponentRenderer<Locale> usingTranslationSource(@NotNull final Translator source) {
/*  73 */     Objects.requireNonNull(source, "source");
/*  74 */     return new TranslatableComponentRenderer<Locale>() {
/*     */         @Nullable
/*     */         protected MessageFormat translate(@NotNull String key, @NotNull Locale context) {
/*  77 */           return source.translate(key, context);
/*     */         }
/*     */         
/*     */         @NotNull
/*     */         protected Component renderTranslatable(@NotNull TranslatableComponent component, @NotNull Locale context) {
/*  82 */           Component translated = source.translate(component, context);
/*  83 */           if (translated != null) return translated; 
/*  84 */           return super.renderTranslatable(component, context);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected MessageFormat translate(@NotNull String key, @NotNull C context) {
/*  97 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected MessageFormat translate(@NotNull String key, @Nullable String fallback, @NotNull C context) {
/* 109 */     return translate(key, context);
/*     */   }
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   protected Component renderBlockNbt(@NotNull BlockNBTComponent component, @NotNull C context) {
/* 115 */     BlockNBTComponent.Builder builder = ((BlockNBTComponent.Builder)nbt(context, Component.blockNBT(), component)).pos(component.pos());
/* 116 */     return mergeStyleAndOptionallyDeepRender((Component)component, builder, context);
/*     */   }
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   protected Component renderEntityNbt(@NotNull EntityNBTComponent component, @NotNull C context) {
/* 122 */     EntityNBTComponent.Builder builder = ((EntityNBTComponent.Builder)nbt(context, Component.entityNBT(), component)).selector(component.selector());
/* 123 */     return mergeStyleAndOptionallyDeepRender((Component)component, builder, context);
/*     */   }
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   protected Component renderStorageNbt(@NotNull StorageNBTComponent component, @NotNull C context) {
/* 129 */     StorageNBTComponent.Builder builder = ((StorageNBTComponent.Builder)nbt(context, Component.storageNBT(), component)).storage(component.storage());
/* 130 */     return mergeStyleAndOptionallyDeepRender((Component)component, builder, context);
/*     */   }
/*     */   
/*     */   protected <O extends com.viaversion.viaversion.libs.kyori.adventure.text.NBTComponent<O, B>, B extends com.viaversion.viaversion.libs.kyori.adventure.text.NBTComponentBuilder<O, B>> B nbt(@NotNull C context, B builder, O oldComponent) {
/* 134 */     builder
/* 135 */       .nbtPath(oldComponent.nbtPath())
/* 136 */       .interpret(oldComponent.interpret());
/* 137 */     Component separator = oldComponent.separator();
/* 138 */     if (separator != null) {
/* 139 */       builder.separator((ComponentLike)render(separator, context));
/*     */     }
/* 141 */     return builder;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   protected Component renderKeybind(@NotNull KeybindComponent component, @NotNull C context) {
/* 146 */     KeybindComponent.Builder builder = Component.keybind().keybind(component.keybind());
/* 147 */     return mergeStyleAndOptionallyDeepRender((Component)component, builder, context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   protected Component renderScore(@NotNull ScoreComponent component, @NotNull C context) {
/* 156 */     ScoreComponent.Builder builder = Component.score().name(component.name()).objective(component.objective()).value(component.value());
/* 157 */     return mergeStyleAndOptionallyDeepRender((Component)component, builder, context);
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   protected Component renderSelector(@NotNull SelectorComponent component, @NotNull C context) {
/* 162 */     SelectorComponent.Builder builder = Component.selector().pattern(component.pattern());
/* 163 */     return mergeStyleAndOptionallyDeepRender((Component)component, builder, context);
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   protected Component renderText(@NotNull TextComponent component, @NotNull C context) {
/* 168 */     TextComponent.Builder builder = Component.text().content(component.content());
/* 169 */     return mergeStyleAndOptionallyDeepRender((Component)component, builder, context);
/*     */   }
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   protected Component renderTranslatable(@NotNull TranslatableComponent component, @NotNull C context) {
/* 175 */     MessageFormat format = translate(component.key(), component.fallback(), context);
/* 176 */     if (format == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 181 */       TranslatableComponent.Builder builder1 = Component.translatable().key(component.key()).fallback(component.fallback());
/* 182 */       if (!component.args().isEmpty()) {
/* 183 */         List<Component> list = new ArrayList<>(component.args());
/* 184 */         for (int i = 0, size = list.size(); i < size; i++) {
/* 185 */           list.set(i, render(list.get(i), context));
/*     */         }
/* 187 */         builder1.args(list);
/*     */       } 
/* 189 */       return mergeStyleAndOptionallyDeepRender((Component)component, builder1, context);
/*     */     } 
/*     */     
/* 192 */     List<Component> args = component.args();
/*     */     
/* 194 */     TextComponent.Builder builder = Component.text();
/* 195 */     mergeStyle((Component)component, builder, context);
/*     */ 
/*     */     
/* 198 */     if (args.isEmpty()) {
/* 199 */       builder.content(format.format((Object[])null, new StringBuffer(), (FieldPosition)null).toString());
/* 200 */       return optionallyRenderChildrenAppendAndBuild(component.children(), builder, context);
/*     */     } 
/*     */     
/* 203 */     Object[] nulls = new Object[args.size()];
/* 204 */     StringBuffer sb = format.format(nulls, new StringBuffer(), (FieldPosition)null);
/* 205 */     AttributedCharacterIterator it = format.formatToCharacterIterator(nulls);
/*     */     
/* 207 */     while (it.getIndex() < it.getEndIndex()) {
/* 208 */       int end = it.getRunLimit();
/* 209 */       Integer index = (Integer)it.getAttribute(MessageFormat.Field.ARGUMENT);
/* 210 */       if (index != null) {
/* 211 */         builder.append(render(args.get(index.intValue()), context));
/*     */       } else {
/* 213 */         builder.append((Component)Component.text(sb.substring(it.getIndex(), end)));
/*     */       } 
/* 215 */       it.setIndex(end);
/*     */     } 
/*     */     
/* 218 */     return optionallyRenderChildrenAppendAndBuild(component.children(), builder, context);
/*     */   }
/*     */   
/*     */   protected <O extends com.viaversion.viaversion.libs.kyori.adventure.text.BuildableComponent<O, B>, B extends ComponentBuilder<O, B>> O mergeStyleAndOptionallyDeepRender(Component component, B builder, C context) {
/* 222 */     mergeStyle(component, (ComponentBuilder<?, ?>)builder, context);
/* 223 */     return optionallyRenderChildrenAppendAndBuild(component.children(), builder, context);
/*     */   }
/*     */   
/*     */   protected <O extends com.viaversion.viaversion.libs.kyori.adventure.text.BuildableComponent<O, B>, B extends ComponentBuilder<O, B>> O optionallyRenderChildrenAppendAndBuild(List<Component> children, B builder, C context) {
/* 227 */     if (!children.isEmpty()) {
/* 228 */       children.forEach(child -> builder.append(render(child, (C)context)));
/*     */     }
/* 230 */     return (O)builder.build();
/*     */   }
/*     */   
/*     */   protected <B extends ComponentBuilder<?, ?>> void mergeStyle(Component component, B builder, C context) {
/* 234 */     builder.mergeStyle(component, MERGES);
/* 235 */     builder.clickEvent(component.clickEvent());
/* 236 */     HoverEvent<?> hoverEvent = component.hoverEvent();
/* 237 */     if (hoverEvent != null)
/* 238 */       builder.hoverEvent((HoverEventSource)hoverEvent.withRenderedValue(this, context)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\text\renderer\TranslatableComponentRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */