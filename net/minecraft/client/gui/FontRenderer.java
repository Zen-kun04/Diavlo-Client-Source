/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.ibm.icu.text.ArabicShaping;
/*     */ import com.ibm.icu.text.ArabicShapingException;
/*     */ import com.ibm.icu.text.Bidi;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Properties;
/*     */ import java.util.Random;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.renderer.texture.TextureUtil;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.client.resources.IResourceManager;
/*     */ import net.minecraft.client.resources.IResourceManagerReloadListener;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.src.Config;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.optifine.CustomColors;
/*     */ import net.optifine.render.GlBlendState;
/*     */ import net.optifine.util.FontUtils;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class FontRenderer
/*     */   implements IResourceManagerReloadListener {
/*  34 */   private static final ResourceLocation[] unicodePageLocations = new ResourceLocation[256];
/*  35 */   private final int[] charWidth = new int[256];
/*  36 */   public int FONT_HEIGHT = 9;
/*  37 */   public Random fontRandom = new Random();
/*  38 */   private byte[] glyphWidth = new byte[65536];
/*  39 */   private int[] colorCode = new int[32];
/*     */   private ResourceLocation locationFontTexture;
/*     */   private final TextureManager renderEngine;
/*     */   private float posX;
/*     */   private float posY;
/*     */   private boolean unicodeFlag;
/*     */   private boolean bidiFlag;
/*     */   private float red;
/*     */   private float blue;
/*     */   private float green;
/*     */   private float alpha;
/*     */   private int textColor;
/*     */   private boolean randomStyle;
/*     */   private boolean boldStyle;
/*     */   private boolean italicStyle;
/*     */   private boolean underlineStyle;
/*     */   private boolean strikethroughStyle;
/*     */   public GameSettings gameSettings;
/*     */   public ResourceLocation locationFontTextureBase;
/*  58 */   public float offsetBold = 1.0F;
/*  59 */   private float[] charWidthFloat = new float[256];
/*     */   private boolean blend = false;
/*  61 */   private GlBlendState oldBlendState = new GlBlendState();
/*     */ 
/*     */   
/*     */   public FontRenderer(GameSettings gameSettingsIn, ResourceLocation location, TextureManager textureManagerIn, boolean unicode) {
/*  65 */     this.gameSettings = gameSettingsIn;
/*  66 */     this.locationFontTextureBase = location;
/*  67 */     this.locationFontTexture = location;
/*  68 */     this.renderEngine = textureManagerIn;
/*  69 */     this.unicodeFlag = unicode;
/*  70 */     this.locationFontTexture = FontUtils.getHdFontLocation(this.locationFontTextureBase);
/*  71 */     bindTexture(this.locationFontTexture);
/*     */     
/*  73 */     for (int i = 0; i < 32; i++) {
/*     */       
/*  75 */       int j = (i >> 3 & 0x1) * 85;
/*  76 */       int k = (i >> 2 & 0x1) * 170 + j;
/*  77 */       int l = (i >> 1 & 0x1) * 170 + j;
/*  78 */       int i1 = (i >> 0 & 0x1) * 170 + j;
/*     */       
/*  80 */       if (i == 6)
/*     */       {
/*  82 */         k += 85;
/*     */       }
/*     */       
/*  85 */       if (gameSettingsIn.anaglyph) {
/*     */         
/*  87 */         int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
/*  88 */         int k1 = (k * 30 + l * 70) / 100;
/*  89 */         int l1 = (k * 30 + i1 * 70) / 100;
/*  90 */         k = j1;
/*  91 */         l = k1;
/*  92 */         i1 = l1;
/*     */       } 
/*     */       
/*  95 */       if (i >= 16) {
/*     */         
/*  97 */         k /= 4;
/*  98 */         l /= 4;
/*  99 */         i1 /= 4;
/*     */       } 
/*     */       
/* 102 */       this.colorCode[i] = (k & 0xFF) << 16 | (l & 0xFF) << 8 | i1 & 0xFF;
/*     */     } 
/*     */     
/* 105 */     readGlyphSizes();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onResourceManagerReload(IResourceManager resourceManager) {
/* 110 */     this.locationFontTexture = FontUtils.getHdFontLocation(this.locationFontTextureBase);
/*     */     
/* 112 */     for (int i = 0; i < unicodePageLocations.length; i++)
/*     */     {
/* 114 */       unicodePageLocations[i] = null;
/*     */     }
/*     */     
/* 117 */     readFontTexture();
/* 118 */     readGlyphSizes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readFontTexture() {
/*     */     BufferedImage bufferedimage;
/*     */     try {
/* 127 */       bufferedimage = TextureUtil.readBufferedImage(getResourceInputStream(this.locationFontTexture));
/*     */     }
/* 129 */     catch (IOException ioexception1) {
/*     */       
/* 131 */       throw new RuntimeException(ioexception1);
/*     */     } 
/*     */     
/* 134 */     Properties properties = FontUtils.readFontProperties(this.locationFontTexture);
/* 135 */     this.blend = FontUtils.readBoolean(properties, "blend", false);
/* 136 */     int i = bufferedimage.getWidth();
/* 137 */     int j = bufferedimage.getHeight();
/* 138 */     int k = i / 16;
/* 139 */     int l = j / 16;
/* 140 */     float f = i / 128.0F;
/* 141 */     float f1 = Config.limit(f, 1.0F, 2.0F);
/* 142 */     this.offsetBold = 1.0F / f1;
/* 143 */     float f2 = FontUtils.readFloat(properties, "offsetBold", -1.0F);
/*     */     
/* 145 */     if (f2 >= 0.0F)
/*     */     {
/* 147 */       this.offsetBold = f2;
/*     */     }
/*     */     
/* 150 */     int[] aint = new int[i * j];
/* 151 */     bufferedimage.getRGB(0, 0, i, j, aint, 0, i);
/*     */     
/* 153 */     for (int i1 = 0; i1 < 256; i1++) {
/*     */       
/* 155 */       int j1 = i1 % 16;
/* 156 */       int k1 = i1 / 16;
/* 157 */       int l1 = 0;
/*     */       
/* 159 */       for (l1 = k - 1; l1 >= 0; l1--) {
/*     */         
/* 161 */         int i2 = j1 * k + l1;
/* 162 */         boolean flag = true;
/*     */         
/* 164 */         for (int j2 = 0; j2 < l && flag; j2++) {
/*     */           
/* 166 */           int k2 = (k1 * l + j2) * i;
/* 167 */           int l2 = aint[i2 + k2];
/* 168 */           int i3 = l2 >> 24 & 0xFF;
/*     */           
/* 170 */           if (i3 > 16)
/*     */           {
/* 172 */             flag = false;
/*     */           }
/*     */         } 
/*     */         
/* 176 */         if (!flag) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 182 */       if (i1 == 65)
/*     */       {
/* 184 */         i1 = i1;
/*     */       }
/*     */       
/* 187 */       if (i1 == 32)
/*     */       {
/* 189 */         if (k <= 8) {
/*     */           
/* 191 */           l1 = (int)(2.0F * f);
/*     */         }
/*     */         else {
/*     */           
/* 195 */           l1 = (int)(1.5F * f);
/*     */         } 
/*     */       }
/*     */       
/* 199 */       this.charWidthFloat[i1] = (l1 + 1) / f + 1.0F;
/*     */     } 
/*     */     
/* 202 */     FontUtils.readCustomCharWidths(properties, this.charWidthFloat);
/*     */     
/* 204 */     for (int j3 = 0; j3 < this.charWidth.length; j3++)
/*     */     {
/* 206 */       this.charWidth[j3] = Math.round(this.charWidthFloat[j3]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void readGlyphSizes() {
/* 212 */     InputStream inputstream = null;
/*     */ 
/*     */     
/*     */     try {
/* 216 */       inputstream = getResourceInputStream(new ResourceLocation("font/glyph_sizes.bin"));
/* 217 */       inputstream.read(this.glyphWidth);
/*     */     }
/* 219 */     catch (IOException ioexception) {
/*     */       
/* 221 */       throw new RuntimeException(ioexception);
/*     */     }
/*     */     finally {
/*     */       
/* 225 */       IOUtils.closeQuietly(inputstream);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private float renderChar(char ch, boolean italic) {
/* 231 */     if (ch != ' ' && ch != ' ') {
/*     */       
/* 233 */       int i = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\000\000\000\000\000\000\000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\000".indexOf(ch);
/* 234 */       return (i != -1 && !this.unicodeFlag) ? renderDefaultChar(i, italic) : renderUnicodeChar(ch, italic);
/*     */     } 
/*     */ 
/*     */     
/* 238 */     return !this.unicodeFlag ? this.charWidthFloat[ch] : 4.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private float renderDefaultChar(int ch, boolean italic) {
/* 244 */     int i = ch % 16 * 8;
/* 245 */     int j = ch / 16 * 8;
/* 246 */     int k = italic ? 1 : 0;
/* 247 */     bindTexture(this.locationFontTexture);
/* 248 */     float f = this.charWidthFloat[ch];
/* 249 */     float f1 = 7.99F;
/* 250 */     GL11.glBegin(5);
/* 251 */     GL11.glTexCoord2f(i / 128.0F, j / 128.0F);
/* 252 */     GL11.glVertex3f(this.posX + k, this.posY, 0.0F);
/* 253 */     GL11.glTexCoord2f(i / 128.0F, (j + 7.99F) / 128.0F);
/* 254 */     GL11.glVertex3f(this.posX - k, this.posY + 7.99F, 0.0F);
/* 255 */     GL11.glTexCoord2f((i + f1 - 1.0F) / 128.0F, j / 128.0F);
/* 256 */     GL11.glVertex3f(this.posX + f1 - 1.0F + k, this.posY, 0.0F);
/* 257 */     GL11.glTexCoord2f((i + f1 - 1.0F) / 128.0F, (j + 7.99F) / 128.0F);
/* 258 */     GL11.glVertex3f(this.posX + f1 - 1.0F - k, this.posY + 7.99F, 0.0F);
/* 259 */     GL11.glEnd();
/* 260 */     return f;
/*     */   }
/*     */ 
/*     */   
/*     */   private ResourceLocation getUnicodePageLocation(int page) {
/* 265 */     if (unicodePageLocations[page] == null) {
/*     */       
/* 267 */       unicodePageLocations[page] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", new Object[] { Integer.valueOf(page) }));
/* 268 */       unicodePageLocations[page] = FontUtils.getHdFontLocation(unicodePageLocations[page]);
/*     */     } 
/*     */     
/* 271 */     return unicodePageLocations[page];
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadGlyphTexture(int page) {
/* 276 */     bindTexture(getUnicodePageLocation(page));
/*     */   }
/*     */ 
/*     */   
/*     */   private float renderUnicodeChar(char ch, boolean italic) {
/* 281 */     if (this.glyphWidth[ch] == 0)
/*     */     {
/* 283 */       return 0.0F;
/*     */     }
/*     */ 
/*     */     
/* 287 */     int i = ch / 256;
/* 288 */     loadGlyphTexture(i);
/* 289 */     int j = this.glyphWidth[ch] >>> 4;
/* 290 */     int k = this.glyphWidth[ch] & 0xF;
/* 291 */     float f = j;
/* 292 */     float f1 = (k + 1);
/* 293 */     float f2 = (ch % 16 * 16) + f;
/* 294 */     float f3 = ((ch & 0xFF) / 16 * 16);
/* 295 */     float f4 = f1 - f - 0.02F;
/* 296 */     float f5 = italic ? 1.0F : 0.0F;
/* 297 */     GL11.glBegin(5);
/* 298 */     GL11.glTexCoord2f(f2 / 256.0F, f3 / 256.0F);
/* 299 */     GL11.glVertex3f(this.posX + f5, this.posY, 0.0F);
/* 300 */     GL11.glTexCoord2f(f2 / 256.0F, (f3 + 15.98F) / 256.0F);
/* 301 */     GL11.glVertex3f(this.posX - f5, this.posY + 7.99F, 0.0F);
/* 302 */     GL11.glTexCoord2f((f2 + f4) / 256.0F, f3 / 256.0F);
/* 303 */     GL11.glVertex3f(this.posX + f4 / 2.0F + f5, this.posY, 0.0F);
/* 304 */     GL11.glTexCoord2f((f2 + f4) / 256.0F, (f3 + 15.98F) / 256.0F);
/* 305 */     GL11.glVertex3f(this.posX + f4 / 2.0F - f5, this.posY + 7.99F, 0.0F);
/* 306 */     GL11.glEnd();
/* 307 */     return (f1 - f) / 2.0F + 1.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int drawStringWithShadow(String text, float x, float y, int color) {
/* 313 */     return drawString(text, x, y, color, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public int drawString(String text, double x, double y, int color) {
/* 318 */     return drawString(text, (float)x, (float)y, color, false);
/*     */   }
/*     */   
/*     */   public int drawString(String text, float x, float y, int color, boolean dropShadow) {
/*     */     int i;
/* 323 */     enableAlpha();
/*     */     
/* 325 */     if (this.blend) {
/*     */       
/* 327 */       GlStateManager.getBlendState(this.oldBlendState);
/* 328 */       GlStateManager.enableBlend();
/* 329 */       GlStateManager.blendFunc(770, 771);
/*     */     } 
/*     */     
/* 332 */     resetStyles();
/*     */ 
/*     */     
/* 335 */     if (dropShadow) {
/*     */       
/* 337 */       i = renderString(text, x + 1.0F, y + 1.0F, color, true);
/* 338 */       i = Math.max(i, renderString(text, x, y, color, false));
/*     */     }
/*     */     else {
/*     */       
/* 342 */       i = renderString(text, x, y, color, false);
/*     */     } 
/*     */     
/* 345 */     if (this.blend)
/*     */     {
/* 347 */       GlStateManager.setBlendState(this.oldBlendState);
/*     */     }
/*     */     
/* 350 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String bidiReorder(String text) {
/*     */     try {
/* 357 */       Bidi bidi = new Bidi((new ArabicShaping(8)).shape(text), 127);
/* 358 */       bidi.setReorderingMode(0);
/* 359 */       return bidi.writeReordered(2);
/*     */     }
/* 361 */     catch (ArabicShapingException var3) {
/*     */       
/* 363 */       return text;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void resetStyles() {
/* 369 */     this.randomStyle = false;
/* 370 */     this.boldStyle = false;
/* 371 */     this.italicStyle = false;
/* 372 */     this.underlineStyle = false;
/* 373 */     this.strikethroughStyle = false;
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderStringAtPos(String text, boolean shadow) {
/* 378 */     for (int i = 0; i < text.length(); i++) {
/*     */       
/* 380 */       char c0 = text.charAt(i);
/*     */       
/* 382 */       if (c0 == '§' && i + 1 < text.length()) {
/*     */         
/* 384 */         int l = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
/*     */         
/* 386 */         if (l < 16) {
/*     */           
/* 388 */           this.randomStyle = false;
/* 389 */           this.boldStyle = false;
/* 390 */           this.strikethroughStyle = false;
/* 391 */           this.underlineStyle = false;
/* 392 */           this.italicStyle = false;
/*     */           
/* 394 */           if (l < 0 || l > 15)
/*     */           {
/* 396 */             l = 15;
/*     */           }
/*     */           
/* 399 */           if (shadow)
/*     */           {
/* 401 */             l += 16;
/*     */           }
/*     */           
/* 404 */           int i1 = this.colorCode[l];
/*     */           
/* 406 */           if (Config.isCustomColors())
/*     */           {
/* 408 */             i1 = CustomColors.getTextColor(l, i1);
/*     */           }
/*     */           
/* 411 */           this.textColor = i1;
/* 412 */           setColor((i1 >> 16) / 255.0F, (i1 >> 8 & 0xFF) / 255.0F, (i1 & 0xFF) / 255.0F, this.alpha);
/*     */         }
/* 414 */         else if (l == 16) {
/*     */           
/* 416 */           this.randomStyle = true;
/*     */         }
/* 418 */         else if (l == 17) {
/*     */           
/* 420 */           this.boldStyle = true;
/*     */         }
/* 422 */         else if (l == 18) {
/*     */           
/* 424 */           this.strikethroughStyle = true;
/*     */         }
/* 426 */         else if (l == 19) {
/*     */           
/* 428 */           this.underlineStyle = true;
/*     */         }
/* 430 */         else if (l == 20) {
/*     */           
/* 432 */           this.italicStyle = true;
/*     */         }
/* 434 */         else if (l == 21) {
/*     */           
/* 436 */           this.randomStyle = false;
/* 437 */           this.boldStyle = false;
/* 438 */           this.strikethroughStyle = false;
/* 439 */           this.underlineStyle = false;
/* 440 */           this.italicStyle = false;
/* 441 */           setColor(this.red, this.blue, this.green, this.alpha);
/*     */         } 
/*     */         
/* 444 */         i++;
/*     */       }
/*     */       else {
/*     */         
/* 448 */         int j = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\000\000\000\000\000\000\000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\000".indexOf(c0);
/*     */         
/* 450 */         if (this.randomStyle && j != -1) {
/*     */           char c1;
/* 452 */           int k = getCharWidth(c0);
/*     */ 
/*     */ 
/*     */           
/*     */           do {
/* 457 */             j = this.fontRandom.nextInt("ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\000\000\000\000\000\000\000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\000".length());
/* 458 */             c1 = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\000\000\000\000\000\000\000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\000".charAt(j);
/*     */           }
/* 460 */           while (k != getCharWidth(c1));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 466 */           c0 = c1;
/*     */         } 
/*     */         
/* 469 */         float f1 = (j != -1 && !this.unicodeFlag) ? this.offsetBold : 0.5F;
/* 470 */         boolean flag = ((c0 == '\000' || j == -1 || this.unicodeFlag) && shadow);
/*     */         
/* 472 */         if (flag) {
/*     */           
/* 474 */           this.posX -= f1;
/* 475 */           this.posY -= f1;
/*     */         } 
/*     */         
/* 478 */         float f = renderChar(c0, this.italicStyle);
/*     */         
/* 480 */         if (flag) {
/*     */           
/* 482 */           this.posX += f1;
/* 483 */           this.posY += f1;
/*     */         } 
/*     */         
/* 486 */         if (this.boldStyle) {
/*     */           
/* 488 */           this.posX += f1;
/*     */           
/* 490 */           if (flag) {
/*     */             
/* 492 */             this.posX -= f1;
/* 493 */             this.posY -= f1;
/*     */           } 
/*     */           
/* 496 */           renderChar(c0, this.italicStyle);
/* 497 */           this.posX -= f1;
/*     */           
/* 499 */           if (flag) {
/*     */             
/* 501 */             this.posX += f1;
/* 502 */             this.posY += f1;
/*     */           } 
/*     */           
/* 505 */           f += f1;
/*     */         } 
/*     */         
/* 508 */         doDraw(f);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDraw(float p_doDraw_1_) {
/* 515 */     if (this.strikethroughStyle) {
/*     */       
/* 517 */       Tessellator tessellator = Tessellator.getInstance();
/* 518 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 519 */       GlStateManager.disableTexture2D();
/* 520 */       worldrenderer.begin(7, DefaultVertexFormats.POSITION);
/* 521 */       worldrenderer.pos(this.posX, (this.posY + (this.FONT_HEIGHT / 2)), 0.0D).endVertex();
/* 522 */       worldrenderer.pos((this.posX + p_doDraw_1_), (this.posY + (this.FONT_HEIGHT / 2)), 0.0D).endVertex();
/* 523 */       worldrenderer.pos((this.posX + p_doDraw_1_), (this.posY + (this.FONT_HEIGHT / 2) - 1.0F), 0.0D).endVertex();
/* 524 */       worldrenderer.pos(this.posX, (this.posY + (this.FONT_HEIGHT / 2) - 1.0F), 0.0D).endVertex();
/* 525 */       tessellator.draw();
/* 526 */       GlStateManager.enableTexture2D();
/*     */     } 
/*     */     
/* 529 */     if (this.underlineStyle) {
/*     */       
/* 531 */       Tessellator tessellator1 = Tessellator.getInstance();
/* 532 */       WorldRenderer worldrenderer1 = tessellator1.getWorldRenderer();
/* 533 */       GlStateManager.disableTexture2D();
/* 534 */       worldrenderer1.begin(7, DefaultVertexFormats.POSITION);
/* 535 */       int i = this.underlineStyle ? -1 : 0;
/* 536 */       worldrenderer1.pos((this.posX + i), (this.posY + this.FONT_HEIGHT), 0.0D).endVertex();
/* 537 */       worldrenderer1.pos((this.posX + p_doDraw_1_), (this.posY + this.FONT_HEIGHT), 0.0D).endVertex();
/* 538 */       worldrenderer1.pos((this.posX + p_doDraw_1_), (this.posY + this.FONT_HEIGHT - 1.0F), 0.0D).endVertex();
/* 539 */       worldrenderer1.pos((this.posX + i), (this.posY + this.FONT_HEIGHT - 1.0F), 0.0D).endVertex();
/* 540 */       tessellator1.draw();
/* 541 */       GlStateManager.enableTexture2D();
/*     */     } 
/*     */     
/* 544 */     this.posX += p_doDraw_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   private int renderStringAligned(String text, int x, int y, int width, int color, boolean dropShadow) {
/* 549 */     if (this.bidiFlag) {
/*     */       
/* 551 */       int i = getStringWidth(bidiReorder(text));
/* 552 */       x = x + width - i;
/*     */     } 
/*     */     
/* 555 */     return renderString(text, x, y, color, dropShadow);
/*     */   }
/*     */ 
/*     */   
/*     */   private int renderString(String text, float x, float y, int color, boolean dropShadow) {
/* 560 */     if (text == null)
/*     */     {
/* 562 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 566 */     if (this.bidiFlag)
/*     */     {
/* 568 */       text = bidiReorder(text);
/*     */     }
/*     */     
/* 571 */     if ((color & 0xFC000000) == 0)
/*     */     {
/* 573 */       color |= 0xFF000000;
/*     */     }
/*     */     
/* 576 */     if (dropShadow)
/*     */     {
/* 578 */       color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
/*     */     }
/*     */     
/* 581 */     this.red = (color >> 16 & 0xFF) / 255.0F;
/* 582 */     this.blue = (color >> 8 & 0xFF) / 255.0F;
/* 583 */     this.green = (color & 0xFF) / 255.0F;
/* 584 */     this.alpha = (color >> 24 & 0xFF) / 255.0F;
/* 585 */     setColor(this.red, this.blue, this.green, this.alpha);
/* 586 */     this.posX = x;
/* 587 */     this.posY = y;
/* 588 */     renderStringAtPos(text, dropShadow);
/* 589 */     return (int)this.posX;
/*     */   }
/*     */ 
/*     */   
/*     */   public int width(String text) {
/* 594 */     return getStringWidth(text);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStringWidth(String text) {
/* 599 */     if (text == null)
/*     */     {
/* 601 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 605 */     float f = 0.0F;
/* 606 */     boolean flag = false;
/*     */     
/* 608 */     for (int i = 0; i < text.length(); i++) {
/*     */       
/* 610 */       char c0 = text.charAt(i);
/* 611 */       float f1 = getCharWidthFloat(c0);
/*     */       
/* 613 */       if (f1 < 0.0F && i < text.length() - 1) {
/*     */         
/* 615 */         i++;
/* 616 */         c0 = text.charAt(i);
/*     */         
/* 618 */         if (c0 != 'l' && c0 != 'L') {
/*     */           
/* 620 */           if (c0 == 'r' || c0 == 'R')
/*     */           {
/* 622 */             flag = false;
/*     */           }
/*     */         }
/*     */         else {
/*     */           
/* 627 */           flag = true;
/*     */         } 
/*     */         
/* 630 */         f1 = 0.0F;
/*     */       } 
/*     */       
/* 633 */       f += f1;
/*     */       
/* 635 */       if (flag && f1 > 0.0F)
/*     */       {
/* 637 */         f += this.unicodeFlag ? 1.0F : this.offsetBold;
/*     */       }
/*     */     } 
/*     */     
/* 641 */     return Math.round(f);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCharWidth(char character) {
/* 647 */     return Math.round(getCharWidthFloat(character));
/*     */   }
/*     */ 
/*     */   
/*     */   private float getCharWidthFloat(char p_getCharWidthFloat_1_) {
/* 652 */     if (p_getCharWidthFloat_1_ == '§')
/*     */     {
/* 654 */       return -1.0F;
/*     */     }
/* 656 */     if (p_getCharWidthFloat_1_ != ' ' && p_getCharWidthFloat_1_ != ' ') {
/*     */       
/* 658 */       int i = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\000\000\000\000\000\000\000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\000".indexOf(p_getCharWidthFloat_1_);
/*     */       
/* 660 */       if (p_getCharWidthFloat_1_ > '\000' && i != -1 && !this.unicodeFlag)
/*     */       {
/* 662 */         return this.charWidthFloat[i];
/*     */       }
/* 664 */       if (this.glyphWidth[p_getCharWidthFloat_1_] != 0) {
/*     */         
/* 666 */         int j = this.glyphWidth[p_getCharWidthFloat_1_] >>> 4;
/* 667 */         int k = this.glyphWidth[p_getCharWidthFloat_1_] & 0xF;
/*     */         
/* 669 */         if (k > 7) {
/*     */           
/* 671 */           k = 15;
/* 672 */           j = 0;
/*     */         } 
/*     */         
/* 675 */         k++;
/* 676 */         return ((k - j) / 2 + 1);
/*     */       } 
/*     */ 
/*     */       
/* 680 */       return 0.0F;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 685 */     return this.charWidthFloat[32];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String trimStringToWidth(String text, int width) {
/* 691 */     return trimStringToWidth(text, width, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public String trimStringToWidth(String text, int width, boolean reverse) {
/* 696 */     StringBuilder stringbuilder = new StringBuilder();
/* 697 */     float f = 0.0F;
/* 698 */     int i = reverse ? (text.length() - 1) : 0;
/* 699 */     int j = reverse ? -1 : 1;
/* 700 */     boolean flag = false;
/* 701 */     boolean flag1 = false;
/*     */     int k;
/* 703 */     for (k = i; k >= 0 && k < text.length() && f < width; k += j) {
/*     */       
/* 705 */       char c0 = text.charAt(k);
/* 706 */       float f1 = getCharWidthFloat(c0);
/*     */       
/* 708 */       if (flag) {
/*     */         
/* 710 */         flag = false;
/*     */         
/* 712 */         if (c0 != 'l' && c0 != 'L')
/*     */         {
/* 714 */           if (c0 == 'r' || c0 == 'R')
/*     */           {
/* 716 */             flag1 = false;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 721 */           flag1 = true;
/*     */         }
/*     */       
/* 724 */       } else if (f1 < 0.0F) {
/*     */         
/* 726 */         flag = true;
/*     */       }
/*     */       else {
/*     */         
/* 730 */         f += f1;
/*     */         
/* 732 */         if (flag1)
/*     */         {
/* 734 */           f++;
/*     */         }
/*     */       } 
/*     */       
/* 738 */       if (f > width) {
/*     */         break;
/*     */       }
/*     */ 
/*     */       
/* 743 */       if (reverse) {
/*     */         
/* 745 */         stringbuilder.insert(0, c0);
/*     */       }
/*     */       else {
/*     */         
/* 749 */         stringbuilder.append(c0);
/*     */       } 
/*     */     } 
/*     */     
/* 753 */     return stringbuilder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private String trimStringNewline(String text) {
/* 758 */     while (text != null && text.endsWith("\n"))
/*     */     {
/* 760 */       text = text.substring(0, text.length() - 1);
/*     */     }
/*     */     
/* 763 */     return text;
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawSplitString(String str, int x, int y, int wrapWidth, int textColor) {
/* 768 */     if (this.blend) {
/*     */       
/* 770 */       GlStateManager.getBlendState(this.oldBlendState);
/* 771 */       GlStateManager.enableBlend();
/* 772 */       GlStateManager.blendFunc(770, 771);
/*     */     } 
/*     */     
/* 775 */     resetStyles();
/* 776 */     this.textColor = textColor;
/* 777 */     str = trimStringNewline(str);
/* 778 */     renderSplitString(str, x, y, wrapWidth, false);
/*     */     
/* 780 */     if (this.blend)
/*     */     {
/* 782 */       GlStateManager.setBlendState(this.oldBlendState);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderSplitString(String str, int x, int y, int wrapWidth, boolean addShadow) {
/* 788 */     for (String s : listFormattedStringToWidth(str, wrapWidth)) {
/*     */       
/* 790 */       renderStringAligned(s, x, y, wrapWidth, this.textColor, addShadow);
/* 791 */       y += this.FONT_HEIGHT;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int splitStringWidth(String str, int maxLength) {
/* 797 */     return this.FONT_HEIGHT * listFormattedStringToWidth(str, maxLength).size();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUnicodeFlag(boolean unicodeFlagIn) {
/* 802 */     this.unicodeFlag = unicodeFlagIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getUnicodeFlag() {
/* 807 */     return this.unicodeFlag;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBidiFlag(boolean bidiFlagIn) {
/* 812 */     this.bidiFlag = bidiFlagIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> listFormattedStringToWidth(String str, int wrapWidth) {
/* 817 */     return Arrays.asList(wrapFormattedStringToWidth(str, wrapWidth).split("\n"));
/*     */   }
/*     */ 
/*     */   
/*     */   String wrapFormattedStringToWidth(String str, int wrapWidth) {
/* 822 */     if (str.length() <= 1)
/*     */     {
/* 824 */       return str;
/*     */     }
/*     */ 
/*     */     
/* 828 */     int i = sizeStringToWidth(str, wrapWidth);
/*     */     
/* 830 */     if (str.length() <= i)
/*     */     {
/* 832 */       return str;
/*     */     }
/*     */ 
/*     */     
/* 836 */     String s = str.substring(0, i);
/* 837 */     char c0 = str.charAt(i);
/* 838 */     boolean flag = (c0 == ' ' || c0 == '\n');
/* 839 */     String s1 = getFormatFromString(s) + str.substring(i + (flag ? 1 : 0));
/* 840 */     return s + "\n" + wrapFormattedStringToWidth(s1, wrapWidth);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int sizeStringToWidth(String str, int wrapWidth) {
/* 847 */     int i = str.length();
/* 848 */     float f = 0.0F;
/* 849 */     int j = 0;
/* 850 */     int k = -1;
/*     */     
/* 852 */     for (boolean flag = false; j < i; j++) {
/*     */       
/* 854 */       char c0 = str.charAt(j);
/*     */       
/* 856 */       switch (c0) {
/*     */         
/*     */         case '\n':
/* 859 */           j--;
/*     */           break;
/*     */         
/*     */         case ' ':
/* 863 */           k = j;
/*     */         
/*     */         default:
/* 866 */           f += getCharWidth(c0);
/*     */           
/* 868 */           if (flag)
/*     */           {
/* 870 */             f++;
/*     */           }
/*     */           break;
/*     */ 
/*     */         
/*     */         case '§':
/* 876 */           if (j < i - 1) {
/*     */             
/* 878 */             j++;
/* 879 */             char c1 = str.charAt(j);
/*     */             
/* 881 */             if (c1 != 'l' && c1 != 'L') {
/*     */               
/* 883 */               if (c1 == 'r' || c1 == 'R' || isFormatColor(c1))
/*     */               {
/* 885 */                 flag = false;
/*     */               }
/*     */               
/*     */               break;
/*     */             } 
/* 890 */             flag = true;
/*     */           } 
/*     */           break;
/*     */       } 
/*     */       
/* 895 */       if (c0 == '\n') {
/*     */ 
/*     */         
/* 898 */         k = ++j;
/*     */         
/*     */         break;
/*     */       } 
/* 902 */       if (Math.round(f) > wrapWidth) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 908 */     return (j != i && k != -1 && k < j) ? k : j;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isFormatColor(char colorChar) {
/* 913 */     return ((colorChar >= '0' && colorChar <= '9') || (colorChar >= 'a' && colorChar <= 'f') || (colorChar >= 'A' && colorChar <= 'F'));
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isFormatSpecial(char formatChar) {
/* 918 */     return ((formatChar >= 'k' && formatChar <= 'o') || (formatChar >= 'K' && formatChar <= 'O') || formatChar == 'r' || formatChar == 'R');
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getFormatFromString(String text) {
/* 923 */     String s = "";
/* 924 */     int i = -1;
/* 925 */     int j = text.length();
/*     */     
/* 927 */     while ((i = text.indexOf('§', i + 1)) != -1) {
/*     */       
/* 929 */       if (i < j - 1) {
/*     */         
/* 931 */         char c0 = text.charAt(i + 1);
/*     */         
/* 933 */         if (isFormatColor(c0)) {
/*     */           
/* 935 */           s = "§" + c0; continue;
/*     */         } 
/* 937 */         if (isFormatSpecial(c0))
/*     */         {
/* 939 */           s = s + "§" + c0;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 944 */     return s;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getBidiFlag() {
/* 949 */     return this.bidiFlag;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getColorCode(char character) {
/* 954 */     int i = "0123456789abcdef".indexOf(character);
/*     */     
/* 956 */     if (i >= 0 && i < this.colorCode.length) {
/*     */       
/* 958 */       int j = this.colorCode[i];
/*     */       
/* 960 */       if (Config.isCustomColors())
/*     */       {
/* 962 */         j = CustomColors.getTextColor(i, j);
/*     */       }
/*     */       
/* 965 */       return j;
/*     */     } 
/*     */ 
/*     */     
/* 969 */     return 16777215;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setColor(float p_setColor_1_, float p_setColor_2_, float p_setColor_3_, float p_setColor_4_) {
/* 975 */     GlStateManager.color(p_setColor_1_, p_setColor_2_, p_setColor_3_, p_setColor_4_);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void enableAlpha() {
/* 980 */     GlStateManager.enableAlpha();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void bindTexture(ResourceLocation p_bindTexture_1_) {
/* 985 */     this.renderEngine.bindTexture(p_bindTexture_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   protected InputStream getResourceInputStream(ResourceLocation p_getResourceInputStream_1_) throws IOException {
/* 990 */     return Minecraft.getMinecraft().getResourceManager().getResource(p_getResourceInputStream_1_).getInputStream();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\FontRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */