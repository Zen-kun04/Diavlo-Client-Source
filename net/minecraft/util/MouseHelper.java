/*    */ package net.minecraft.util;
/*    */ 
/*    */ import org.lwjgl.input.Mouse;
/*    */ import org.lwjgl.opengl.Display;
/*    */ 
/*    */ 
/*    */ public class MouseHelper
/*    */ {
/*    */   public int deltaX;
/*    */   public int deltaY;
/*    */   
/*    */   public void grabMouseCursor() {
/* 13 */     Mouse.setGrabbed(true);
/* 14 */     this.deltaX = 0;
/* 15 */     this.deltaY = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void ungrabMouseCursor() {
/* 20 */     Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
/* 21 */     Mouse.setGrabbed(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void mouseXYChange() {
/* 26 */     this.deltaX = Mouse.getDX();
/* 27 */     this.deltaY = Mouse.getDY();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\MouseHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */