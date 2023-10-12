/*     */ package net.minecraft.event;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ public class ClickEvent
/*     */ {
/*     */   private final Action action;
/*     */   private final String value;
/*     */   
/*     */   public ClickEvent(Action theAction, String theValue) {
/*  13 */     this.action = theAction;
/*  14 */     this.value = theValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public Action getAction() {
/*  19 */     return this.action;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue() {
/*  24 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object p_equals_1_) {
/*  29 */     if (this == p_equals_1_)
/*     */     {
/*  31 */       return true;
/*     */     }
/*  33 */     if (p_equals_1_ != null && getClass() == p_equals_1_.getClass()) {
/*     */       
/*  35 */       ClickEvent clickevent = (ClickEvent)p_equals_1_;
/*     */       
/*  37 */       if (this.action != clickevent.action)
/*     */       {
/*  39 */         return false;
/*     */       }
/*     */ 
/*     */       
/*  43 */       if (this.value != null) {
/*     */         
/*  45 */         if (!this.value.equals(clickevent.value))
/*     */         {
/*  47 */           return false;
/*     */         }
/*     */       }
/*  50 */       else if (clickevent.value != null) {
/*     */         
/*  52 */         return false;
/*     */       } 
/*     */       
/*  55 */       return true;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  60 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  66 */     return "ClickEvent{action=" + this.action + ", value='" + this.value + '\'' + '}';
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  71 */     int i = this.action.hashCode();
/*  72 */     i = 31 * i + ((this.value != null) ? this.value.hashCode() : 0);
/*  73 */     return i;
/*     */   }
/*     */   
/*     */   public enum Action
/*     */   {
/*  78 */     OPEN_URL("open_url", true),
/*  79 */     OPEN_FILE("open_file", false),
/*  80 */     RUN_COMMAND("run_command", true),
/*  81 */     TWITCH_USER_INFO("twitch_user_info", false),
/*  82 */     SUGGEST_COMMAND("suggest_command", true),
/*  83 */     CHANGE_PAGE("change_page", true);
/*     */     
/*  85 */     private static final Map<String, Action> nameMapping = Maps.newHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final boolean allowedInChat;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String canonicalName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/* 111 */       for (Action clickevent$action : values())
/*     */       {
/* 113 */         nameMapping.put(clickevent$action.getCanonicalName(), clickevent$action);
/*     */       }
/*     */     }
/*     */     
/*     */     Action(String canonicalNameIn, boolean allowedInChatIn) {
/*     */       this.canonicalName = canonicalNameIn;
/*     */       this.allowedInChat = allowedInChatIn;
/*     */     }
/*     */     
/*     */     public boolean shouldAllowInChat() {
/*     */       return this.allowedInChat;
/*     */     }
/*     */     
/*     */     public String getCanonicalName() {
/*     */       return this.canonicalName;
/*     */     }
/*     */     
/*     */     public static Action getValueByCanonicalName(String canonicalNameIn) {
/*     */       return nameMapping.get(canonicalNameIn);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\event\ClickEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */