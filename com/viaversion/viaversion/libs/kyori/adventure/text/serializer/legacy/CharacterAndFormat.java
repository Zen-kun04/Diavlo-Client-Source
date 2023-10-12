/*     */ package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.legacy;
/*     */ 
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.format.NamedTextColor;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextDecoration;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextFormat;
/*     */ import com.viaversion.viaversion.libs.kyori.examination.Examinable;
/*     */ import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
/*     */ import java.util.List;
/*     */ import java.util.stream.Stream;
/*     */ import org.jetbrains.annotations.ApiStatus.NonExtendable;
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
/*     */ @NonExtendable
/*     */ public interface CharacterAndFormat
/*     */   extends Examinable
/*     */ {
/*  49 */   public static final CharacterAndFormat BLACK = characterAndFormat('0', (TextFormat)NamedTextColor.BLACK);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   public static final CharacterAndFormat DARK_BLUE = characterAndFormat('1', (TextFormat)NamedTextColor.DARK_BLUE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   public static final CharacterAndFormat DARK_GREEN = characterAndFormat('2', (TextFormat)NamedTextColor.DARK_GREEN);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   public static final CharacterAndFormat DARK_AQUA = characterAndFormat('3', (TextFormat)NamedTextColor.DARK_AQUA);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   public static final CharacterAndFormat DARK_RED = characterAndFormat('4', (TextFormat)NamedTextColor.DARK_RED);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   public static final CharacterAndFormat DARK_PURPLE = characterAndFormat('5', (TextFormat)NamedTextColor.DARK_PURPLE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   public static final CharacterAndFormat GOLD = characterAndFormat('6', (TextFormat)NamedTextColor.GOLD);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   public static final CharacterAndFormat GRAY = characterAndFormat('7', (TextFormat)NamedTextColor.GRAY);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   public static final CharacterAndFormat DARK_GRAY = characterAndFormat('8', (TextFormat)NamedTextColor.DARK_GRAY);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   public static final CharacterAndFormat BLUE = characterAndFormat('9', (TextFormat)NamedTextColor.BLUE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   public static final CharacterAndFormat GREEN = characterAndFormat('a', (TextFormat)NamedTextColor.GREEN);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   public static final CharacterAndFormat AQUA = characterAndFormat('b', (TextFormat)NamedTextColor.AQUA);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   public static final CharacterAndFormat RED = characterAndFormat('c', (TextFormat)NamedTextColor.RED);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   public static final CharacterAndFormat LIGHT_PURPLE = characterAndFormat('d', (TextFormat)NamedTextColor.LIGHT_PURPLE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 133 */   public static final CharacterAndFormat YELLOW = characterAndFormat('e', (TextFormat)NamedTextColor.YELLOW);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 139 */   public static final CharacterAndFormat WHITE = characterAndFormat('f', (TextFormat)NamedTextColor.WHITE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 146 */   public static final CharacterAndFormat OBFUSCATED = characterAndFormat('k', (TextFormat)TextDecoration.OBFUSCATED);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 152 */   public static final CharacterAndFormat BOLD = characterAndFormat('l', (TextFormat)TextDecoration.BOLD);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 158 */   public static final CharacterAndFormat STRIKETHROUGH = characterAndFormat('m', (TextFormat)TextDecoration.STRIKETHROUGH);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 164 */   public static final CharacterAndFormat UNDERLINED = characterAndFormat('n', (TextFormat)TextDecoration.UNDERLINED);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 170 */   public static final CharacterAndFormat ITALIC = characterAndFormat('o', (TextFormat)TextDecoration.ITALIC);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 177 */   public static final CharacterAndFormat RESET = characterAndFormat('r', Reset.INSTANCE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   static CharacterAndFormat characterAndFormat(char character, @NotNull TextFormat format) {
/* 188 */     return new CharacterAndFormatImpl(character, format);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   static List<CharacterAndFormat> defaults() {
/* 199 */     return CharacterAndFormatImpl.Defaults.DEFAULTS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   char character();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   TextFormat format();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   default Stream<? extends ExaminableProperty> examinableProperties() {
/* 220 */     return Stream.of(new ExaminableProperty[] {
/* 221 */           ExaminableProperty.of("character", character()), 
/* 222 */           ExaminableProperty.of("format", format())
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\text\serializer\legacy\CharacterAndFormat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */