/*    */ package org.yaml.snakeyaml.util;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ArrayStack<T>
/*    */ {
/*    */   private final ArrayList<T> stack;
/*    */   
/*    */   public ArrayStack(int initSize) {
/* 33 */     this.stack = new ArrayList<>(initSize);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void push(T obj) {
/* 42 */     this.stack.add(obj);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public T pop() {
/* 51 */     return this.stack.remove(this.stack.size() - 1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isEmpty() {
/* 60 */     return this.stack.isEmpty();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void clear() {
/* 67 */     this.stack.clear();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\org\yaml\snakeyam\\util\ArrayStack.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */