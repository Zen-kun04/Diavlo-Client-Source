/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Random;
/*    */ 
/*    */ 
/*    */ public class WeightedRandom
/*    */ {
/*    */   public static int getTotalWeight(Collection<? extends Item> collection) {
/* 10 */     int i = 0;
/*    */     
/* 12 */     for (Item weightedrandom$item : collection)
/*    */     {
/* 14 */       i += weightedrandom$item.itemWeight;
/*    */     }
/*    */     
/* 17 */     return i;
/*    */   }
/*    */ 
/*    */   
/*    */   public static <T extends Item> T getRandomItem(Random random, Collection<T> collection, int totalWeight) {
/* 22 */     if (totalWeight <= 0)
/*    */     {
/* 24 */       throw new IllegalArgumentException();
/*    */     }
/*    */ 
/*    */     
/* 28 */     int i = random.nextInt(totalWeight);
/* 29 */     return getRandomItem(collection, i);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T extends Item> T getRandomItem(Collection<T> collection, int weight) {
/* 35 */     for (Item item : collection) {
/*    */       
/* 37 */       weight -= item.itemWeight;
/*    */       
/* 39 */       if (weight < 0)
/*    */       {
/* 41 */         return (T)item;
/*    */       }
/*    */     } 
/*    */     
/* 45 */     return (T)null;
/*    */   }
/*    */ 
/*    */   
/*    */   public static <T extends Item> T getRandomItem(Random random, Collection<T> collection) {
/* 50 */     return getRandomItem(random, collection, getTotalWeight(collection));
/*    */   }
/*    */ 
/*    */   
/*    */   public static class Item
/*    */   {
/*    */     protected int itemWeight;
/*    */     
/*    */     public Item(int itemWeightIn) {
/* 59 */       this.itemWeight = itemWeightIn;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\WeightedRandom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */