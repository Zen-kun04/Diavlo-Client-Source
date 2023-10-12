/*    */ package rip.diavlo.base.api.ui.click.component.components.impl;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.FontRenderer;
/*    */ import net.minecraft.client.gui.Gui;
/*    */ import org.lwjgl.input.Keyboard;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ import rip.diavlo.base.Client;
/*    */ import rip.diavlo.base.api.ui.click.ClickGui;
/*    */ import rip.diavlo.base.api.ui.click.component.Component;
/*    */ import rip.diavlo.base.api.ui.click.component.components.Button;
/*    */ import rip.diavlo.base.modules.render.ClickGUI;
/*    */ 
/*    */ public class KeybindSetting
/*    */   extends Component {
/* 16 */   private final FontRenderer fontRenderer = (Minecraft.getMinecraft()).fontRendererObj;
/*    */   
/*    */   private boolean hovered;
/*    */   private boolean binding;
/*    */   private Button parent;
/*    */   private int offset;
/*    */   private int x;
/*    */   private int y;
/*    */   private int scrolledOffset;
/*    */   
/*    */   public KeybindSetting(Button button, int offset) {
/* 27 */     this.parent = button;
/* 28 */     this.x = button.parent.getX() + button.parent.getWidth();
/* 29 */     this.y = button.parent.getY() + button.offset;
/* 30 */     this.offset = offset;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setOff(int newOff) {
/* 35 */     this.offset = newOff;
/*    */   }
/*    */ 
/*    */   
/*    */   public void renderComponent() {
/* 40 */     this.scrolledOffset = ClickGui.offset;
/*    */     
/* 42 */     switch ((String)((ClickGUI)Client.getInstance().getModuleManager().get(ClickGUI.class)).mode.getValue()) {
/*    */       case "Normal":
/* 44 */         Gui.drawRect((this.parent.parent.getX() + 2), (this.parent.parent.getY() + this.offset - this.scrolledOffset), (this.parent.parent
/* 45 */             .getX() + this.parent.parent.getWidth() * 1), (this.parent.parent
/* 46 */             .getY() + this.offset + 12 - this.scrolledOffset), this.hovered ? -14540254 : -15658735);
/* 47 */         Gui.drawRect(this.parent.parent.getX(), (this.parent.parent.getY() + this.offset - this.scrolledOffset), (this.parent.parent
/* 48 */             .getX() + 2), (this.parent.parent.getY() + this.offset + 12 - this.scrolledOffset), -15658735);
/* 49 */         GL11.glPushMatrix();
/* 50 */         GL11.glScalef(0.5F, 0.5F, 0.5F);
/* 51 */         this.fontRenderer.drawStringWithShadow(this.binding ? "Press a key..." : ("§7Key: §f" + 
/* 52 */             Keyboard.getKeyName(this.parent.mod.getKey())), ((this.parent.parent
/* 53 */             .getX() + 7) * 2), ((this.parent.parent.getY() + this.offset + 2) * 2 + 5 - this.scrolledOffset * 2), -1);
/*    */         
/* 55 */         GL11.glPopMatrix();
/*    */         break;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void updateComponent(int mouseX, int mouseY) {
/* 63 */     this.hovered = isMouseOnButton(mouseX, mouseY);
/* 64 */     this.y = this.parent.parent.getY() + this.offset;
/* 65 */     this.x = this.parent.parent.getX();
/*    */   }
/*    */ 
/*    */   
/*    */   public void mouseClicked(int mouseX, int mouseY, int button) {
/* 70 */     if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
/* 71 */       this.binding = !this.binding;
/*    */     }
/*    */   }
/*    */   
/*    */   public void keyTyped(char typedChar, int key) {
/* 76 */     if (this.binding) {
/* 77 */       this.parent.mod.setKey(key);
/* 78 */       this.binding = false;
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean isMouseOnButton(int x, int y) {
/* 83 */     if (x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12) {
/* 84 */       return true;
/*    */     }
/* 86 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\ap\\ui\click\component\components\impl\KeybindSetting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */