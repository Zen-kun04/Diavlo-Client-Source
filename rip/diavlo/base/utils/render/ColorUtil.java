/*    */ package rip.diavlo.base.utils.render;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ import rip.diavlo.base.utils.MathUtil;
/*    */ 
/*    */ 
/*    */ public interface ColorUtil
/*    */ {
/*    */   static void glColor(int hex) {
/* 11 */     float a = (hex >> 24 & 0xFF) / 255.0F;
/* 12 */     float r = (hex >> 16 & 0xFF) / 255.0F;
/* 13 */     float g = (hex >> 8 & 0xFF) / 255.0F;
/* 14 */     float b = (hex & 0xFF) / 255.0F;
/* 15 */     GL11.glColor4f(r, g, b, a);
/*    */   }
/*    */   
/*    */   static void glColor(Color color) {
/* 19 */     GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
/*    */   }
/*    */   
/*    */   static Color darker(Color color, float FACTOR) {
/* 23 */     return new Color(Math.max((int)(color.getRed() * FACTOR), 0), 
/* 24 */         Math.max((int)(color.getGreen() * FACTOR), 0), 
/* 25 */         Math.max((int)(color.getBlue() * FACTOR), 0), color
/* 26 */         .getAlpha());
/*    */   }
/*    */   
/*    */   static Color brighter(Color color, float factor) {
/* 30 */     int red = color.getRed();
/* 31 */     int green = color.getGreen();
/* 32 */     int blue = color.getBlue();
/* 33 */     int alpha = color.getAlpha();
/*    */     
/* 35 */     int i = (int)(1.0F / (1.0F - factor));
/* 36 */     if (red == 0 && green == 0 && blue == 0) {
/* 37 */       return new Color(i, i, i, alpha);
/*    */     }
/*    */     
/* 40 */     if (red > 0 && red < i) red = i; 
/* 41 */     if (green > 0 && green < i) green = i; 
/* 42 */     if (blue > 0 && blue < i) blue = i;
/*    */     
/* 44 */     return new Color(Math.min((int)(red / factor), 255), 
/* 45 */         Math.min((int)(green / factor), 255), 
/* 46 */         Math.min((int)(blue / factor), 255), alpha);
/*    */   }
/*    */ 
/*    */   
/*    */   static Color withRed(Color color, int red) {
/* 51 */     return new Color(red, color.getGreen(), color.getBlue());
/*    */   }
/*    */   
/*    */   static Color withGreen(Color color, int green) {
/* 55 */     return new Color(color.getRed(), green, color.getBlue());
/*    */   }
/*    */   
/*    */   static Color withBlue(Color color, int blue) {
/* 59 */     return new Color(color.getRed(), color.getGreen(), blue);
/*    */   }
/*    */   
/*    */   static Color withAlpha(Color color, float alpha) {
/* 63 */     return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)MathUtil.clamp(0.0D, 255.0D, alpha));
/*    */   }
/*    */   
/*    */   static Color withAlpha(Color color, int alpha) {
/* 67 */     return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)MathUtil.clamp(0.0D, 255.0D, alpha));
/*    */   }
/*    */   
/*    */   static Color mixColors(Color color1, Color color2, double percent) {
/* 71 */     double inverse_percent = 1.0D - percent;
/* 72 */     int redPart = (int)(color1.getRed() * percent + color2.getRed() * inverse_percent);
/* 73 */     int greenPart = (int)(color1.getGreen() * percent + color2.getGreen() * inverse_percent);
/* 74 */     int bluePart = (int)(color1.getBlue() * percent + color2.getBlue() * inverse_percent);
/* 75 */     return new Color(redPart, greenPart, bluePart);
/*    */   }
/*    */   
/*    */   static Color withOpacity(Color color, float opacity) {
/* 79 */     opacity = Math.min(1.0F, Math.max(0.0F, opacity));
/* 80 */     return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(color.getAlpha() * opacity));
/*    */   }
/*    */ 
/*    */   
/*    */   static Color invert(Color color) {
/* 85 */     return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\bas\\utils\render\ColorUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */