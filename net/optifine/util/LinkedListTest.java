/*     */ package net.optifine.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.optifine.render.VboRange;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LinkedListTest
/*     */ {
/*     */   public static void main(String[] args) throws Exception {
/*  13 */     LinkedList<VboRange> linkedlist = new LinkedList<>();
/*  14 */     List<VboRange> list = new ArrayList<>();
/*  15 */     List<VboRange> list1 = new ArrayList<>();
/*  16 */     Random random = new Random();
/*  17 */     int i = 100;
/*     */     
/*  19 */     for (int j = 0; j < i; j++) {
/*     */       
/*  21 */       VboRange vborange = new VboRange();
/*  22 */       vborange.setPosition(j);
/*  23 */       list.add(vborange);
/*     */     } 
/*     */     
/*  26 */     for (int k = 0; k < 100000; k++) {
/*     */       
/*  28 */       checkLists(list, list1, i);
/*  29 */       checkLinkedList(linkedlist, list1.size());
/*     */       
/*  31 */       if (k % 5 == 0)
/*     */       {
/*  33 */         dbgLinkedList(linkedlist);
/*     */       }
/*     */       
/*  36 */       if (random.nextBoolean()) {
/*     */         
/*  38 */         if (!list.isEmpty()) {
/*     */           
/*  40 */           VboRange vborange3 = list.get(random.nextInt(list.size()));
/*  41 */           LinkedList.Node<VboRange> node2 = vborange3.getNode();
/*     */           
/*  43 */           if (random.nextBoolean()) {
/*     */             
/*  45 */             linkedlist.addFirst(node2);
/*  46 */             dbg("Add first: " + vborange3.getPosition());
/*     */           }
/*  48 */           else if (random.nextBoolean()) {
/*     */             
/*  50 */             linkedlist.addLast(node2);
/*  51 */             dbg("Add last: " + vborange3.getPosition());
/*     */           }
/*     */           else {
/*     */             
/*  55 */             if (list1.isEmpty()) {
/*     */               continue;
/*     */             }
/*     */ 
/*     */             
/*  60 */             VboRange vborange1 = list1.get(random.nextInt(list1.size()));
/*  61 */             LinkedList.Node<VboRange> node1 = vborange1.getNode();
/*  62 */             linkedlist.addAfter(node1, node2);
/*  63 */             dbg("Add after: " + vborange1.getPosition() + ", " + vborange3.getPosition());
/*     */           } 
/*     */           
/*  66 */           list.remove(vborange3);
/*  67 */           list1.add(vborange3);
/*     */         }  continue;
/*     */       } 
/*  70 */       if (!list1.isEmpty()) {
/*     */         
/*  72 */         VboRange vborange2 = list1.get(random.nextInt(list1.size()));
/*  73 */         LinkedList.Node<VboRange> node = vborange2.getNode();
/*  74 */         linkedlist.remove(node);
/*  75 */         dbg("Remove: " + vborange2.getPosition());
/*  76 */         list1.remove(vborange2);
/*  77 */         list.add(vborange2);
/*     */       } 
/*     */       continue;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void dbgLinkedList(LinkedList<VboRange> linkedList) {
/*  84 */     StringBuffer stringbuffer = new StringBuffer();
/*     */     
/*  86 */     linkedList.iterator().forEachRemaining(vboRangeNode -> {
/*     */           LinkedList.Node<VboRange> node = vboRangeNode;
/*     */           
/*     */           if (node.getItem() == null) {
/*     */             return;
/*     */           }
/*     */           
/*     */           VboRange vborange = node.getItem();
/*     */           
/*     */           if (stringbuffer.length() > 0) {
/*     */             stringbuffer.append(", ");
/*     */           }
/*     */           
/*     */           stringbuffer.append(vborange.getPosition());
/*     */         });
/* 101 */     dbg("List: " + stringbuffer);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void checkLinkedList(LinkedList<VboRange> linkedList, int used) {
/* 106 */     if (linkedList.getSize() != used)
/*     */     {
/* 108 */       throw new RuntimeException("Wrong size, linked: " + linkedList.getSize() + ", used: " + used);
/*     */     }
/*     */ 
/*     */     
/* 112 */     int i = 0;
/*     */     
/* 114 */     for (LinkedList.Node<VboRange> node = linkedList.getFirst(); node != null; node = node.getNext())
/*     */     {
/* 116 */       i++;
/*     */     }
/*     */     
/* 119 */     if (linkedList.getSize() != i)
/*     */     {
/* 121 */       throw new RuntimeException("Wrong count, linked: " + linkedList.getSize() + ", count: " + i);
/*     */     }
/*     */ 
/*     */     
/* 125 */     int j = 0;
/*     */     
/* 127 */     for (LinkedList.Node<VboRange> node1 = linkedList.getLast(); node1 != null; node1 = node1.getPrev())
/*     */     {
/* 129 */       j++;
/*     */     }
/*     */     
/* 132 */     if (linkedList.getSize() != j)
/*     */     {
/* 134 */       throw new RuntimeException("Wrong count back, linked: " + linkedList.getSize() + ", count: " + j);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void checkLists(List<VboRange> listFree, List<VboRange> listUsed, int count) {
/* 142 */     int i = listFree.size() + listUsed.size();
/*     */     
/* 144 */     if (i != count)
/*     */     {
/* 146 */       throw new RuntimeException("Total size: " + i);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void dbg(String str) {
/* 152 */     System.out.println(str);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifin\\util\LinkedListTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */