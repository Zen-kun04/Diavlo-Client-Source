/*     */ package rip.diavlo.base.api.font;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import org.lwjgl.BufferUtils;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.util.vector.Vector2f;
/*     */ 
/*     */ public class CustomFontRenderer
/*     */ {
/*     */   private final boolean antiAlias;
/*     */   private final Font font;
/*     */   private final boolean fractionalMetrics;
/*  23 */   private final int[] colorCodes = new int[32]; private final CharacterData[] regularData; private final CharacterData[] boldData; private final CharacterData[] italicsData;
/*     */   private static final int RANDOM_OFFSET = 1;
/*     */   
/*     */   public CustomFontRenderer(Font font) {
/*  27 */     this(font, 256);
/*     */   }
/*     */   
/*     */   public CustomFontRenderer(Font font, int characterCount) {
/*  31 */     this(font, characterCount, true);
/*     */   }
/*     */   
/*     */   public CustomFontRenderer(Font font, int characterCount, boolean antiAlias) {
/*  35 */     this.font = font;
/*  36 */     this.fractionalMetrics = true;
/*  37 */     this.antiAlias = antiAlias;
/*  38 */     this.regularData = setup(new CharacterData[characterCount], 0);
/*  39 */     this.boldData = setup(new CharacterData[characterCount], 1);
/*  40 */     this.italicsData = setup(new CharacterData[characterCount], 2);
/*     */   }
/*     */   
/*     */   public CustomFontRenderer(Font font, boolean antiAlias) {
/*  44 */     this(font, 256, antiAlias);
/*     */   }
/*     */   
/*     */   private CharacterData[] setup(CharacterData[] characterData, int type) {
/*  48 */     generateColors();
/*  49 */     Font font = this.font.deriveFont(type);
/*  50 */     BufferedImage utilityImage = new BufferedImage(1, 1, 2);
/*  51 */     Graphics2D utilityGraphics = (Graphics2D)utilityImage.getGraphics();
/*  52 */     utilityGraphics.setFont(font);
/*  53 */     FontMetrics fontMetrics = utilityGraphics.getFontMetrics();
/*  54 */     for (int index = 0; index < characterData.length; index++) {
/*  55 */       char character = (char)index;
/*  56 */       Rectangle2D characterBounds = fontMetrics.getStringBounds(String.valueOf(character), utilityGraphics);
/*  57 */       float width = (float)characterBounds.getWidth() + 8.0F;
/*  58 */       float height = (float)characterBounds.getHeight();
/*     */       
/*  60 */       BufferedImage characterImage = new BufferedImage(MathHelper.ceiling_double_int(width), MathHelper.ceiling_double_int(height), 2);
/*  61 */       Graphics2D graphics = (Graphics2D)characterImage.getGraphics();
/*  62 */       graphics.setFont(font);
/*  63 */       graphics.setColor(new Color(255, 255, 255, 0));
/*  64 */       graphics.fillRect(0, 0, characterImage.getWidth(), characterImage.getHeight());
/*  65 */       graphics.setColor(Color.WHITE);
/*  66 */       if (this.antiAlias) {
/*  67 */         graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
/*  68 */         graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*  69 */         graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
/*  70 */         graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, this.fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
/*     */       } 
/*     */ 
/*     */       
/*  74 */       graphics.drawString(String.valueOf(character), 4, fontMetrics.getAscent());
/*  75 */       int textureId = GL11.glGenTextures();
/*  76 */       createTexture(textureId, characterImage);
/*  77 */       characterData[index] = new CharacterData(character, characterImage.getWidth(), characterImage.getHeight(), textureId);
/*     */     } 
/*     */     
/*  80 */     return characterData;
/*     */   }
/*     */   
/*     */   private void createTexture(int textureId, BufferedImage image) {
/*  84 */     int[] pixels = new int[image.getWidth() * image.getHeight()];
/*  85 */     image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
/*  86 */     ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
/*  87 */     for (int y2 = 0; y2 < image.getHeight(); y2++) {
/*  88 */       for (int x = 0; x < image.getWidth(); x++) {
/*  89 */         int pixel = pixels[y2 * image.getWidth() + x];
/*  90 */         buffer.put((byte)(pixel >> 16 & 0xFF));
/*  91 */         buffer.put((byte)(pixel >> 8 & 0xFF));
/*  92 */         buffer.put((byte)(pixel & 0xFF));
/*  93 */         buffer.put((byte)(pixel >> 24 & 0xFF));
/*     */       } 
/*     */     } 
/*  96 */     buffer.flip();
/*  97 */     GlStateManager.bindTexture(textureId);
/*  98 */     GL11.glTexParameteri(3553, 10241, 9728);
/*  99 */     GL11.glTexParameteri(3553, 10240, 9728);
/* 100 */     GL11.glTexImage2D(3553, 0, 6408, image.getWidth(), image.getHeight(), 0, 6408, 5121, buffer);
/*     */   }
/*     */   
/*     */   public void drawString(String text, double x, double y2, int color) {
/* 104 */     renderString(getText(text), (float)x, (float)y2, color, false);
/*     */   }
/*     */   
/*     */   public void drawCenteredString(String text, double x, double y2, int color) {
/* 108 */     float width = getWidth(getText(text)) / 2.0F;
/* 109 */     renderString(getText(text), (float)(x - width), (float)(y2 - (getHeight(getText(text)) / 2.0F)), color, false);
/*     */   }
/*     */   
/*     */   public void drawStringWithShadow(String text, float x, float y2, int color) {
/* 113 */     GL11.glTranslated(0.5D, 0.5D, 0.0D);
/* 114 */     renderString(getText(text), x, y2, color, true);
/* 115 */     GL11.glTranslated(-0.5D, -0.5D, 0.0D);
/* 116 */     renderString(getText(text), x, y2, color, false);
/*     */   }
/*     */   
/*     */   private void renderString(String text, float x, float y2, int color, boolean shadow) {
/* 120 */     if (text == "" || text.length() == 0) {
/*     */       return;
/*     */     }
/* 123 */     text = getText(text);
/* 124 */     GL11.glPushMatrix();
/* 125 */     GlStateManager.scale(0.5D, 0.5D, 1.0D);
/* 126 */     GlStateManager.enableBlend();
/* 127 */     GlStateManager.blendFunc(770, 771);
/* 128 */     x -= 2.0F;
/* 129 */     y2 -= 2.0F;
/* 130 */     x += 0.5F;
/* 131 */     y2 += 0.5F;
/* 132 */     x *= 2.0F;
/* 133 */     y2 *= 2.0F;
/* 134 */     CharacterData[] characterData = this.regularData;
/* 135 */     boolean underlined = false;
/* 136 */     boolean strikethrough = false;
/* 137 */     boolean obfuscated = false;
/* 138 */     int length = text.length();
/* 139 */     double multiplier = 255.0D * (shadow ? 4 : true);
/* 140 */     Color c2 = new Color(color);
/* 141 */     GlStateManager.color((float)(c2.getRed() / multiplier), (float)(c2.getGreen() / multiplier), (float)(c2.getBlue() / multiplier), (float)((color >> 24 & 0xFF) / 255.0D));
/* 142 */     for (int i = 0; i < length; i++) {
/*     */       
/* 144 */       char character = text.charAt(i);
/* 145 */       int previous = (i > 0) ? text.charAt(i - 1) : 46;
/* 146 */       if (previous != 167)
/*     */       {
/*     */         
/* 149 */         if (character == '§' && i < length) {
/* 150 */           int index = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
/* 151 */           if (index < 16) {
/* 152 */             obfuscated = false;
/* 153 */             strikethrough = false;
/* 154 */             underlined = false;
/* 155 */             characterData = this.regularData;
/* 156 */             if (index < 0 || index > 15) {
/* 157 */               index = 15;
/*     */             }
/* 159 */             if (shadow) {
/* 160 */               index += 16;
/*     */             }
/* 162 */             int textColor = this.colorCodes[index];
/* 163 */             GL11.glColor4d((textColor >> 16) / 255.0D, (textColor >> 8 & 0xFF) / 255.0D, (textColor & 0xFF) / 255.0D, (color >> 24 & 0xFF) / 255.0D);
/*     */ 
/*     */           
/*     */           }
/* 167 */           else if (index == 16) {
/* 168 */             obfuscated = true;
/*     */           
/*     */           }
/* 171 */           else if (index == 17) {
/* 172 */             characterData = this.boldData;
/*     */           
/*     */           }
/* 175 */           else if (index == 18) {
/* 176 */             strikethrough = true;
/*     */           
/*     */           }
/* 179 */           else if (index == 19) {
/* 180 */             underlined = true;
/*     */           
/*     */           }
/* 183 */           else if (index == 20) {
/* 184 */             characterData = this.italicsData;
/*     */           
/*     */           }
/* 187 */           else if (index == 21) {
/*     */ 
/*     */             
/* 190 */             obfuscated = false;
/* 191 */             strikethrough = false;
/* 192 */             underlined = false;
/* 193 */             characterData = this.regularData;
/* 194 */             GL11.glColor4d(1.0D * (shadow ? 0.25D : 1.0D), 1.0D * (shadow ? 0.25D : 1.0D), 1.0D * (shadow ? 0.25D : 1.0D), (color >> 24 & 0xFF) / 255.0D);
/*     */           }
/*     */         
/*     */         }
/* 198 */         else if (character <= 'ÿ') {
/*     */ 
/*     */           
/* 201 */           if (obfuscated) {
/* 202 */             character = (char)(character + 1);
/*     */           }
/* 204 */           drawChar(character, characterData, x, y2);
/* 205 */           CharacterData charData = characterData[character];
/* 206 */           if (strikethrough) {
/* 207 */             drawLine(new Vector2f(0.0F, charData.height / 2.0F), new Vector2f(charData.width, charData.height / 2.0F), 3.0F);
/*     */           }
/*     */           
/* 210 */           if (underlined) {
/* 211 */             drawLine(new Vector2f(0.0F, charData.height - 15.0F), new Vector2f(charData.width, charData.height - 15.0F), 3.0F);
/*     */           }
/*     */           
/* 214 */           x += charData.width - 8.0F;
/*     */         }  } 
/* 216 */     }  GL11.glPopMatrix();
/* 217 */     GlStateManager.disableBlend();
/* 218 */     GlStateManager.bindTexture(0);
/* 219 */     GL11.glColor4d(1.0D, 1.0D, 1.0D, 1.0D);
/*     */   }
/*     */   
/*     */   public double width(String text) {
/* 223 */     return getWidth(text);
/*     */   }
/*     */   
/*     */   public float getWidth(String text) {
/* 227 */     text = getText(text);
/* 228 */     float width = 0.0F;
/* 229 */     CharacterData[] characterData = this.regularData;
/* 230 */     int length = text.length();
/* 231 */     for (int i = 0; i < length; i++) {
/*     */       
/* 233 */       char character = text.charAt(i);
/* 234 */       int previous = (i > 0) ? text.charAt(i - 1) : 46;
/* 235 */       if (previous != 167)
/*     */       {
/*     */         
/* 238 */         if (character == '§' && i < length) {
/* 239 */           int index = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
/* 240 */           if (index == 17) {
/* 241 */             characterData = this.boldData;
/*     */           
/*     */           }
/* 244 */           else if (index == 20) {
/* 245 */             characterData = this.italicsData;
/*     */           
/*     */           }
/* 248 */           else if (index == 21) {
/*     */ 
/*     */             
/* 251 */             characterData = this.regularData;
/*     */           }
/*     */         
/* 254 */         } else if (character <= 'ÿ') {
/*     */ 
/*     */           
/* 257 */           CharacterData charData = characterData[character];
/* 258 */           width += (charData.width - 8.0F) / 2.0F;
/*     */         }  } 
/* 260 */     }  return width + 2.0F;
/*     */   }
/*     */   
/*     */   public double height() {
/* 264 */     return this.font.getSize();
/*     */   }
/*     */   
/*     */   public float getHeight(String text) {
/* 268 */     text = getText(text);
/* 269 */     float height = 0.0F;
/* 270 */     CharacterData[] characterData = this.regularData;
/* 271 */     int length = text.length();
/* 272 */     for (int i = 0; i < length; i++) {
/*     */       
/* 274 */       char character = text.charAt(i);
/* 275 */       int previous = (i > 0) ? text.charAt(i - 1) : 46;
/* 276 */       if (previous != 167)
/*     */       {
/*     */         
/* 279 */         if (character == '§' && i < length) {
/* 280 */           int index = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
/* 281 */           if (index == 17) {
/* 282 */             characterData = this.boldData;
/*     */           
/*     */           }
/* 285 */           else if (index == 20) {
/* 286 */             characterData = this.italicsData;
/*     */           
/*     */           }
/* 289 */           else if (index == 21) {
/*     */ 
/*     */             
/* 292 */             characterData = this.regularData;
/*     */           }
/*     */         
/* 295 */         } else if (character <= 'ÿ') {
/*     */ 
/*     */           
/* 298 */           CharacterData charData = characterData[character];
/* 299 */           height = Math.max(height, charData.height);
/*     */         }  } 
/* 301 */     }  return height / 2.0F - 2.0F;
/*     */   }
/*     */   
/*     */   private void drawChar(char character, CharacterData[] characterData, float x, float y2) {
/* 305 */     CharacterData charData = characterData[character];
/* 306 */     charData.bind();
/* 307 */     GL11.glBegin(7);
/* 308 */     GL11.glTexCoord2f(0.0F, 0.0F);
/* 309 */     GL11.glVertex2d(x, y2);
/* 310 */     GL11.glTexCoord2f(0.0F, 1.0F);
/* 311 */     GL11.glVertex2d(x, (y2 + charData.height));
/* 312 */     GL11.glTexCoord2f(1.0F, 1.0F);
/* 313 */     GL11.glVertex2d((x + charData.width), (y2 + charData.height));
/* 314 */     GL11.glTexCoord2f(1.0F, 0.0F);
/* 315 */     GL11.glVertex2d((x + charData.width), y2);
/* 316 */     GL11.glEnd();
/*     */   }
/*     */   
/*     */   private void drawLine(Vector2f start, Vector2f end, float width) {
/* 320 */     GL11.glDisable(3553);
/* 321 */     GL11.glLineWidth(width);
/* 322 */     GL11.glBegin(1);
/* 323 */     GL11.glVertex2f(start.x, start.y);
/* 324 */     GL11.glVertex2f(end.x, end.y);
/* 325 */     GL11.glEnd();
/* 326 */     GL11.glEnable(3553);
/*     */   }
/*     */   
/*     */   private void generateColors() {
/* 330 */     for (int i = 0; i < 32; i++) {
/* 331 */       int thingy = (i >> 3 & 0x1) * 85;
/* 332 */       int red = (i >> 2 & 0x1) * 170 + thingy;
/* 333 */       int green = (i >> 1 & 0x1) * 170 + thingy;
/* 334 */       int blue = (i >> 0 & 0x1) * 170 + thingy;
/* 335 */       if (i == 6) {
/* 336 */         red += 85;
/*     */       }
/* 338 */       if (i >= 16) {
/* 339 */         red /= 4;
/* 340 */         green /= 4;
/* 341 */         blue /= 4;
/*     */       } 
/* 343 */       this.colorCodes[i] = (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String trimStringToWidth(String text, float width) {
/* 351 */     return trimStringToWidth(text, width, false);
/*     */   }
/*     */   
/*     */   public String trimStringToWidth(String text, float width, boolean reverse) {
/* 355 */     text = getText(text);
/* 356 */     StringBuilder stringbuilder = new StringBuilder();
/* 357 */     float f = 0.0F;
/* 358 */     int i = reverse ? (text.length() - 1) : 0;
/* 359 */     float j = reverse ? -1.0F : 1.0F;
/* 360 */     boolean flag = false;
/* 361 */     boolean flag1 = false; int k;
/* 362 */     for (k = i; k >= 0 && k < text.length() && f < width; k = (int)(k + j)) {
/* 363 */       char c = text.charAt(k);
/* 364 */       float f1 = getWidth(String.valueOf(c));
/* 365 */       if (flag)
/* 366 */       { flag = false;
/* 367 */         if (c != 'l' && c != 'L')
/* 368 */         { if (c == 'r' || c == 'R') flag1 = false;  }
/* 369 */         else { flag1 = true; }  }
/* 370 */       else if (f1 < 0.0F) { flag = true; }
/*     */       else
/* 372 */       { f += f1;
/* 373 */         if (flag1) f++;  }
/*     */       
/* 375 */       if (f > width)
/* 376 */         break;  if (reverse) { stringbuilder.insert(0, c); }
/* 377 */       else { stringbuilder.append(c); }
/*     */     
/* 379 */     }  return stringbuilder.toString();
/*     */   }
/*     */   
/*     */   class CharacterData {
/*     */     public char character;
/*     */     public float width;
/*     */     public float height;
/*     */     private final int textureId;
/*     */     
/*     */     public CharacterData(char character, float width, float height, int textureId) {
/* 389 */       this.character = character;
/* 390 */       this.width = width;
/* 391 */       this.height = height;
/* 392 */       this.textureId = textureId;
/*     */     }
/*     */     
/*     */     public void bind() {
/* 396 */       GL11.glBindTexture(3553, this.textureId);
/*     */     }
/*     */   }
/*     */   
/*     */   private String getText(String text) {
/* 401 */     return text;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\api\font\CustomFontRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */