/*     */ package rip.diavlo.base.api.ui.click.component.components.impl;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import rip.diavlo.base.Client;
/*     */ import rip.diavlo.base.api.ui.click.component.Component;
/*     */ import rip.diavlo.base.api.ui.click.component.components.Button;
/*     */ import rip.diavlo.base.api.value.impl.StringValue;
/*     */ import rip.diavlo.base.modules.render.ClickGUI;
/*     */ 
/*     */ public class StringSetting
/*     */   extends Component
/*     */ {
/*  19 */   private final FontRenderer fontRenderer = (Minecraft.getMinecraft()).fontRendererObj;
/*     */   
/*     */   private boolean hovered;
/*     */   
/*     */   private StringValue set;
/*     */   
/*     */   private Button parent;
/*     */   private int offset;
/*     */   private int x;
/*     */   private int y;
/*     */   private boolean typing = false;
/*     */   
/*     */   public StringSetting(StringValue value, Button button, int offset) {
/*  32 */     this.set = value;
/*  33 */     this.parent = button;
/*  34 */     this.x = button.parent.getX() + button.parent.getWidth();
/*  35 */     this.y = button.parent.getY() + button.offset;
/*  36 */     this.offset = offset;
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderComponent() {
/*  41 */     switch ((String)((ClickGUI)Client.getInstance().getModuleManager().get(ClickGUI.class)).mode.getValue()) {
/*     */       case "Normal":
/*  43 */         Gui.drawRect((this.parent.parent.getX() + 2), (this.parent.parent.getY() + this.offset), (this.parent.parent.getX() + this.parent.parent.getWidth() * 1), (this.parent.parent.getY() + this.offset + 12), this.hovered ? -14540254 : -15658735);
/*  44 */         Gui.drawRect(this.parent.parent.getX(), (this.parent.parent.getY() + this.offset), (this.parent.parent.getX() + 2), (this.parent.parent.getY() + this.offset + 12), -15658735);
/*  45 */         GL11.glPushMatrix();
/*  46 */         GL11.glScalef(0.5F, 0.5F, 0.5F);
/*  47 */         this.fontRenderer.drawStringWithShadow("§7" + this.set.getName() + ": §f" + this.set.getValue(), ((this.parent.parent.getX() + 7) * 2), ((this.parent.parent.getY() + this.offset + 2) * 2 + 5), -1);
/*  48 */         GL11.glPopMatrix();
/*     */         
/*  50 */         if (this.typing)
/*  51 */           Gui.drawRect((this.parent.parent.getX() + 30), (this.parent.parent.getY() + this.offset + 12), (this.parent.parent.getX() + this.parent.parent.getWidth() - 20), (this.parent.parent.getY() + this.offset + 11), -1); 
/*     */         break;
/*     */       case "MetaClient":
/*  54 */         Gui.drawRect((this.parent.parent.getX() + 2), (this.parent.parent.getY() + this.offset), (this.parent.parent.getX() + this.parent.parent.getWidth() * 1), (this.parent.parent.getY() + this.offset + 12), this.hovered ? -14540254 : 1073741824);
/*  55 */         Gui.drawRect(this.parent.parent.getX(), (this.parent.parent.getY() + this.offset), (this.parent.parent.getX() + 2), (this.parent.parent.getY() + this.offset + 12), 1073741824);
/*  56 */         GL11.glPushMatrix();
/*  57 */         GL11.glScalef(0.5F, 0.5F, 0.5F);
/*  58 */         this.fontRenderer.drawStringWithShadow("§7" + this.set.getName() + ": §f" + this.set.getValue(), ((this.parent.parent.getX() + 7) * 2), ((this.parent.parent.getY() + this.offset + 2) * 2 + 5), -1);
/*  59 */         GL11.glPopMatrix();
/*     */         
/*  61 */         if (this.typing) {
/*  62 */           Gui.drawRect((this.parent.parent.getX() + 30), (this.parent.parent.getY() + this.offset + 12), (this.parent.parent.getX() + this.parent.parent.getWidth() - 20), (this.parent.parent.getY() + this.offset + 11), -1);
/*     */         }
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setOff(int newOff) {
/*  69 */     this.offset = newOff;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateComponent(int mouseX, int mouseY) {
/*  74 */     this.hovered = isMouseOnButton(mouseX, mouseY);
/*  75 */     this.y = this.parent.parent.getY() + this.offset;
/*  76 */     this.x = this.parent.parent.getX();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void keyTyped(char typedChar, int keyCode) {
/*  82 */     if (keyCode == 54 || keyCode == 42 || keyCode == 58) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  88 */     if (keyCode == 28) {
/*  89 */       this.typing = false;
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  95 */     if (keyCode == 14 && this.typing) {
/*  96 */       if ((this.set.getValue().toCharArray()).length == 0) {
/*     */         return;
/*     */       }
/*  99 */       this.set.setValue(removeLastChar(this.set.getValue()));
/*     */       
/*     */       return;
/*     */     } 
/* 103 */     List<Character> whitelistedChars = Arrays.asList(new Character[] { 
/* 104 */           Character.valueOf('&'), Character.valueOf(' '), Character.valueOf('#'), Character.valueOf('['), Character.valueOf(']'), Character.valueOf('('), Character.valueOf(')'), 
/* 105 */           Character.valueOf('.'), Character.valueOf(','), Character.valueOf('<'), Character.valueOf('>'), Character.valueOf('-'), Character.valueOf('$'), 
/* 106 */           Character.valueOf('!'), Character.valueOf('"'), Character.valueOf('\''), Character.valueOf('\\'), Character.valueOf('/'), Character.valueOf('='), 
/* 107 */           Character.valueOf('+'), Character.valueOf(','), Character.valueOf('|'), Character.valueOf('^'), Character.valueOf('?'), Character.valueOf('`'), Character.valueOf(';'), Character.valueOf(':'), 
/* 108 */           Character.valueOf('@'), Character.valueOf('£'), Character.valueOf('%'), Character.valueOf('{'), Character.valueOf('}'), Character.valueOf('_'), Character.valueOf('*'), Character.valueOf('»') });
/*     */ 
/*     */     
/* 111 */     for (Iterator<Character> iterator = whitelistedChars.iterator(); iterator.hasNext(); ) { char whitelistedChar = ((Character)iterator.next()).charValue();
/* 112 */       if (typedChar == whitelistedChar && this.typing) {
/* 113 */         this.set.setValue(this.set.getValue() + typedChar);
/*     */         
/*     */         return;
/*     */       }  }
/*     */     
/* 118 */     if (!Character.isLetterOrDigit(typedChar)) {
/*     */       return;
/*     */     }
/* 121 */     if (this.typing)
/* 122 */       this.set.setValue(this.set.getValue() + typedChar); 
/*     */   }
/*     */   
/*     */   private String removeLastChar(String s) {
/* 126 */     return s.substring(0, s.length() - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseClicked(int mouseX, int mouseY, int button) {
/* 131 */     if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
/* 132 */       this.typing = !this.typing;
/*     */     }
/*     */   }
/*     */   
/*     */   public void mouseReleased(int mouseX, int mouseY, int mouseButton) {}
/*     */   
/*     */   public boolean isMouseOnButton(int x, int y) {
/* 139 */     if (x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12) {
/* 140 */       return true;
/*     */     }
/* 142 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\ap\\ui\click\component\components\impl\StringSetting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */