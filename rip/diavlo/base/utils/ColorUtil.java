/*     */ package rip.diavlo.base.utils;
/*     */ 
/*     */ import com.viaversion.viaversion.util.MathUtil;
/*     */ import java.awt.Color;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ 
/*     */ public class ColorUtil
/*     */ {
/*     */   public static int rainbow(int delay) {
/*  11 */     double rainbowState = Math.ceil(((System.currentTimeMillis() + delay) / 4L));
/*  12 */     rainbowState %= 360.0D;
/*  13 */     return Color.getHSBColor((float)(rainbowState / 360.0D), 0.2F, 2.0F).getRGB();
/*     */   }
/*     */   
/*     */   public static int rainbow2(int paramInt) {
/*  17 */     double d = Math.ceil((System.currentTimeMillis() + paramInt) / 20.0D);
/*  18 */     d %= 360.0D;
/*  19 */     return Color.getHSBColor((float)(d / 360.0D), 0.8F, 0.7F).getRGB();
/*     */   }
/*     */   
/*     */   public static Color trasparent(Color colorType, int alpha) {
/*  23 */     return new Color(colorType.getRed(), colorType.getGreen(), colorType.getBlue(), alpha);
/*     */   }
/*     */   
/*     */   public static int rainbow3(int delay) {
/*  27 */     double rainbowState = Math.ceil(((System.currentTimeMillis() + delay) / 4L));
/*  28 */     rainbowState %= 480.0D;
/*  29 */     return Color.getHSBColor((float)(rainbowState / 480.0D), 0.5F, 1.0F).getRGB();
/*     */   }
/*     */   
/*     */   public static int rainbow4(int paramInt) {
/*  33 */     double d = Math.ceil((System.currentTimeMillis() + paramInt) / 20.0D);
/*  34 */     d %= 480.0D;
/*  35 */     return Color.getHSBColor((float)(d / 480.0D), 0.5F, 1.0F).getRGB();
/*     */   }
/*     */   
/*     */   public static int redRainbow(int delay) {
/*  39 */     double rainbowState = Math.ceil(((System.currentTimeMillis() + delay) / 4L));
/*  40 */     rainbowState %= 360.0D;
/*  41 */     return Color.getHSBColor((float)(rainbowState / 360.0D), 1.0F, 1.0F).getRGB();
/*     */   }
/*     */   
/*     */   public static int whiteRainbow(int delay) {
/*  45 */     double rainbowState = Math.ceil(((System.currentTimeMillis() + delay) / 4L));
/*  46 */     rainbowState %= 360.0D;
/*  47 */     return Color.getHSBColor((float)(rainbowState / 360.0D), 0.0F, 1.0F).getRGB();
/*     */   }
/*     */   
/*     */   static void glColor(int hex) {
/*  51 */     float a = (hex >> 24 & 0xFF) / 255.0F;
/*  52 */     float r = (hex >> 16 & 0xFF) / 255.0F;
/*  53 */     float g = (hex >> 8 & 0xFF) / 255.0F;
/*  54 */     float b = (hex & 0xFF) / 255.0F;
/*  55 */     GL11.glColor4f(r, g, b, a);
/*     */   }
/*     */   
/*     */   static void glColor(Color color) {
/*  59 */     GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
/*     */   }
/*     */   
/*     */   static Color darker(Color color, float FACTOR) {
/*  63 */     return new Color(Math.max((int)(color.getRed() * FACTOR), 0), 
/*  64 */         Math.max((int)(color.getGreen() * FACTOR), 0), 
/*  65 */         Math.max((int)(color.getBlue() * FACTOR), 0), color
/*  66 */         .getAlpha());
/*     */   }
/*     */   
/*     */   static Color brighter(Color color, float factor) {
/*  70 */     int red = color.getRed();
/*  71 */     int green = color.getGreen();
/*  72 */     int blue = color.getBlue();
/*  73 */     int alpha = color.getAlpha();
/*     */     
/*  75 */     int i = (int)(1.0F / (1.0F - factor));
/*  76 */     if (red == 0 && green == 0 && blue == 0) {
/*  77 */       return new Color(i, i, i, alpha);
/*     */     }
/*     */     
/*  80 */     if (red > 0 && red < i) red = i; 
/*  81 */     if (green > 0 && green < i) green = i; 
/*  82 */     if (blue > 0 && blue < i) blue = i;
/*     */     
/*  84 */     return new Color(Math.min((int)(red / factor), 255), 
/*  85 */         Math.min((int)(green / factor), 255), 
/*  86 */         Math.min((int)(blue / factor), 255), alpha);
/*     */   }
/*     */ 
/*     */   
/*     */   static Color withRed(Color color, int red) {
/*  91 */     return new Color(red, color.getGreen(), color.getBlue());
/*     */   }
/*     */   
/*     */   static Color withGreen(Color color, int green) {
/*  95 */     return new Color(color.getRed(), green, color.getBlue());
/*     */   }
/*     */   
/*     */   static Color withBlue(Color color, int blue) {
/*  99 */     return new Color(color.getRed(), color.getGreen(), blue);
/*     */   }
/*     */   
/*     */   static Color withAlpha(Color color, float alpha) {
/* 103 */     return new Color(color.getRed(), color.getGreen(), color.getBlue(), MathUtil.clamp(0, 255, (int)alpha));
/*     */   }
/*     */   
/*     */   static Color withAlpha(Color color, int alpha) {
/* 107 */     return new Color(color.getRed(), color.getGreen(), color.getBlue(), MathUtil.clamp(0, 255, alpha));
/*     */   }
/*     */   
/*     */   static Color mixColors(Color color1, Color color2, double percent) {
/* 111 */     double inverse_percent = 1.0D - percent;
/* 112 */     int redPart = (int)(color1.getRed() * percent + color2.getRed() * inverse_percent);
/* 113 */     int greenPart = (int)(color1.getGreen() * percent + color2.getGreen() * inverse_percent);
/* 114 */     int bluePart = (int)(color1.getBlue() * percent + color2.getBlue() * inverse_percent);
/* 115 */     return new Color(redPart, greenPart, bluePart);
/*     */   }
/*     */ 
/*     */   
/*     */   static Color invert(Color color) {
/* 120 */     return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\bas\\utils\ColorUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */