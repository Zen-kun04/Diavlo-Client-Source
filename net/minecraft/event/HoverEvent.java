/*     */ package net.minecraft.event;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ 
/*     */ 
/*     */ public class HoverEvent
/*     */ {
/*     */   private final Action action;
/*     */   private final IChatComponent value;
/*     */   
/*     */   public HoverEvent(Action actionIn, IChatComponent valueIn) {
/*  14 */     this.action = actionIn;
/*  15 */     this.value = valueIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public Action getAction() {
/*  20 */     return this.action;
/*     */   }
/*     */ 
/*     */   
/*     */   public IChatComponent getValue() {
/*  25 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object p_equals_1_) {
/*  30 */     if (this == p_equals_1_)
/*     */     {
/*  32 */       return true;
/*     */     }
/*  34 */     if (p_equals_1_ != null && getClass() == p_equals_1_.getClass()) {
/*     */       
/*  36 */       HoverEvent hoverevent = (HoverEvent)p_equals_1_;
/*     */       
/*  38 */       if (this.action != hoverevent.action)
/*     */       {
/*  40 */         return false;
/*     */       }
/*     */ 
/*     */       
/*  44 */       if (this.value != null) {
/*     */         
/*  46 */         if (!this.value.equals(hoverevent.value))
/*     */         {
/*  48 */           return false;
/*     */         }
/*     */       }
/*  51 */       else if (hoverevent.value != null) {
/*     */         
/*  53 */         return false;
/*     */       } 
/*     */       
/*  56 */       return true;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  61 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  67 */     return "HoverEvent{action=" + this.action + ", value='" + this.value + '\'' + '}';
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  72 */     int i = this.action.hashCode();
/*  73 */     i = 31 * i + ((this.value != null) ? this.value.hashCode() : 0);
/*  74 */     return i;
/*     */   }
/*     */   
/*     */   public enum Action
/*     */   {
/*  79 */     SHOW_TEXT("show_text", true),
/*  80 */     SHOW_ACHIEVEMENT("show_achievement", true),
/*  81 */     SHOW_ITEM("show_item", true),
/*  82 */     SHOW_ENTITY("show_entity", true);
/*     */     
/*  84 */     private static final Map<String, Action> nameMapping = Maps.newHashMap();
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
/* 110 */       for (Action hoverevent$action : values())
/*     */       {
/* 112 */         nameMapping.put(hoverevent$action.getCanonicalName(), hoverevent$action);
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


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\event\HoverEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */