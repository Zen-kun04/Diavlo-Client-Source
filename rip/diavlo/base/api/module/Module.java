/*    */ package rip.diavlo.base.api.module;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import rip.diavlo.base.Client;
/*    */ import rip.diavlo.base.api.value.Value;
/*    */ import rip.diavlo.base.utils.MinecraftUtil;
/*    */ import rip.diavlo.base.utils.NotificationUtils;
/*    */ 
/*    */ public abstract class Module implements MinecraftUtil {
/*    */   private String name;
/*    */   
/*    */   public void setName(String name) {
/* 14 */     this.name = name; } public void setCategory(Category category) { this.category = category; } public void setKey(int key) { this.key = key; } public void setSuffix(String suffix) { this.suffix = suffix; } public void setDescription(String description) { this.description = description; }
/*    */    protected boolean toggled = false; private Category category;
/*    */   public String getName() {
/* 17 */     return this.name;
/*    */   } private int key; public boolean isToggled() {
/* 19 */     return this.toggled;
/*    */   } public Category getCategory() {
/* 21 */     return this.category;
/*    */   } public int getKey() {
/* 23 */     return this.key;
/*    */   }
/* 25 */   private String suffix = ""; private String description; private final List<Value<?>> values; public String getSuffix() { return this.suffix; }
/*    */   
/* 27 */   public String getDescription() { return this.description; } public List<Value<?>> getValues() {
/* 28 */     return this.values;
/*    */   }
/*    */   public Module(String name, int key, Category category, String description) {
/* 31 */     this.name = name;
/* 32 */     this.category = category;
/* 33 */     this.key = key;
/* 34 */     this.description = description;
/* 35 */     this.values = new ArrayList<>();
/*    */   }
/*    */   
/*    */   public Module(String name, int key, Category category) {
/* 39 */     this.name = name;
/* 40 */     this.category = category;
/* 41 */     this.key = key;
/* 42 */     this.description = "";
/* 43 */     this.values = new ArrayList<>();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 49 */     if (!this.name.equalsIgnoreCase("ClickGUI")) {
/* 50 */       NotificationUtils.drawNotification(this.name + " §aactivado");
/*    */     }
/*    */   }
/*    */   
/*    */   public void onDisable() {
/* 55 */     if (!this.name.equalsIgnoreCase("ClickGUI")) {
/* 56 */       NotificationUtils.drawNotification(this.name + " §cdesactivado");
/*    */     }
/*    */   }
/*    */   
/*    */   public void toggle() {
/* 61 */     this.toggled = !this.toggled;
/*    */     
/* 63 */     if (this.toggled) {
/* 64 */       Client.getInstance().getEventBus().register(this);
/* 65 */       onEnable();
/*    */     } else {
/* 67 */       Client.getInstance().getEventBus().unregister(this);
/* 68 */       onDisable();
/*    */     } 
/*    */   }
/*    */   
/*    */   public void setToggled(boolean toggled) {
/* 73 */     this.toggled = toggled;
/*    */     
/* 75 */     if (toggled) {
/* 76 */       Client.getInstance().getEventBus().register(this);
/* 77 */       onEnable();
/*    */     } else {
/* 79 */       Client.getInstance().getEventBus().unregister(this);
/* 80 */       onDisable();
/*    */     } 
/*    */   }
/*    */   
/*    */   public List<Value<?>> getAllValues() {
/* 85 */     return new ArrayList<>(this.values);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\api\module\Module.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */