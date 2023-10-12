/*    */ package net.minecraft.util;
/*    */ 
/*    */ import org.apache.commons.lang3.Validate;
/*    */ 
/*    */ public class RegistryNamespacedDefaultedByKey<K, V>
/*    */   extends RegistryNamespaced<K, V>
/*    */ {
/*    */   private final K defaultValueKey;
/*    */   private V defaultValue;
/*    */   
/*    */   public RegistryNamespacedDefaultedByKey(K defaultValueKeyIn) {
/* 12 */     this.defaultValueKey = defaultValueKeyIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void register(int id, K key, V value) {
/* 17 */     if (this.defaultValueKey.equals(key))
/*    */     {
/* 19 */       this.defaultValue = value;
/*    */     }
/*    */     
/* 22 */     super.register(id, key, value);
/*    */   }
/*    */ 
/*    */   
/*    */   public void validateKey() {
/* 27 */     Validate.notNull(this.defaultValueKey);
/*    */   }
/*    */ 
/*    */   
/*    */   public V getObject(K name) {
/* 32 */     V v = super.getObject(name);
/* 33 */     return (v == null) ? this.defaultValue : v;
/*    */   }
/*    */ 
/*    */   
/*    */   public V getObjectById(int id) {
/* 38 */     V v = super.getObjectById(id);
/* 39 */     return (v == null) ? this.defaultValue : v;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\RegistryNamespacedDefaultedByKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */