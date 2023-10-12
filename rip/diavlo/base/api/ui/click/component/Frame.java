/*    */ package rip.diavlo.base.api.ui.click.component;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import net.minecraft.client.gui.FontRenderer;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ import rip.diavlo.base.Client;
/*    */ import rip.diavlo.base.api.module.Category;
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ import rip.diavlo.base.api.ui.click.ClickGui;
/*    */ import rip.diavlo.base.api.ui.click.component.components.Button;
/*    */ import rip.diavlo.base.modules.render.ClickGUI;
/*    */ 
/*    */ public class Frame {
/*    */   public ArrayList<Component> components;
/*    */   public Category category;
/*    */   private boolean open;
/*    */   private int x;
/*    */   private int y;
/*    */   
/* 20 */   public void setComponents(ArrayList<Component> components) { this.components = components; } private int width; private int barHeight; private boolean isDragging; public int dragX; public int dragY; private int scrolledOffset; public void setCategory(Category category) { this.category = category; } public void setOpen(boolean open) { this.open = open; } public void setX(int x) { this.x = x; } public void setY(int y) { this.y = y; } public void setWidth(int width) { this.width = width; } public void setBarHeight(int barHeight) { this.barHeight = barHeight; } public void setDragging(boolean isDragging) { this.isDragging = isDragging; } public void setDragX(int dragX) { this.dragX = dragX; } public void setDragY(int dragY) { this.dragY = dragY; } public void setScrolledOffset(int scrolledOffset) { this.scrolledOffset = scrolledOffset; }
/*    */ 
/*    */   
/* 23 */   public ArrayList<Component> getComponents() { return this.components; }
/* 24 */   public Category getCategory() { return this.category; }
/* 25 */   public boolean isOpen() { return this.open; }
/* 26 */   public int getX() { return this.x; } public int getY() { return this.y; } public int getWidth() { return this.width; } public int getBarHeight() { return this.barHeight; }
/* 27 */   public boolean isDragging() { return this.isDragging; }
/* 28 */   public int getDragX() { return this.dragX; } public int getDragY() { return this.dragY; } public int getScrolledOffset() {
/* 29 */     return this.scrolledOffset;
/*    */   }
/*    */   public Frame(Category cat) {
/* 32 */     this.components = new ArrayList<>();
/* 33 */     this.category = cat;
/* 34 */     this.scrolledOffset = ClickGui.offset;
/* 35 */     this.width = 88;
/* 36 */     this.x = 5;
/* 37 */     this.y = 5;
/* 38 */     this.barHeight = 13;
/* 39 */     this.dragX = 0;
/* 40 */     this.open = false;
/* 41 */     this.isDragging = false;
/* 42 */     int tY = this.barHeight;
/*    */     
/* 44 */     for (Module mod : Client.getInstance().getModuleManager().get(this.category)) {
/* 45 */       Button modButton = new Button(mod, this, tY);
/* 46 */       this.components.add(modButton);
/* 47 */       tY += 12;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void renderFrame(FontRenderer fontRenderer) {
/* 52 */     this.scrolledOffset = ClickGui.offset;
/*    */     
/* 54 */     switch ((String)((ClickGUI)Client.getInstance().getModuleManager().get(ClickGUI.class)).mode.getValue()) {
/*    */       case "Normal":
/* 56 */         if (this.open && !this.components.isEmpty())
/* 57 */           for (Component component : this.components)
/* 58 */             component.renderComponent();  
/* 59 */         Gui.drawRect(this.x, this.y, (this.x + this.width), (this.y + this.barHeight), ColorUtil.rainbow(50));
/* 60 */         GL11.glPushMatrix();
/* 61 */         GL11.glScalef(0.5F, 0.5F, 0.5F);
/* 62 */         fontRenderer.drawStringWithShadow(this.category.name(), ((this.x + 2) * 2 + 5), (this.y + 2.5F) * 2.0F + 5.0F, -1);
/* 63 */         fontRenderer.drawStringWithShadow(this.open ? "-" : "+", ((this.x + this.width - 10) * 2 + 5), (this.y + 2.5F) * 2.0F + 5.0F, -1);
/* 64 */         GL11.glPopMatrix();
/*    */         break;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void refresh() {
/* 71 */     int off = this.barHeight;
/* 72 */     for (Component comp : this.components) {
/* 73 */       comp.setOff(off);
/* 74 */       off += comp.getHeight();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void updatePosition(int mouseX, int mouseY) {
/* 80 */     if (this.isDragging) {
/* 81 */       setX(mouseX - this.dragX);
/* 82 */       setY(mouseY - this.dragY);
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean isWithinHeader(int x, int y) {
/* 87 */     if (x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight) {
/* 88 */       return true;
/*    */     }
/* 90 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\ap\\ui\click\component\Frame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */