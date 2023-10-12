/*     */ package net.optifine.util;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class LinkedList<T>
/*     */ {
/*     */   private Node<T> first;
/*     */   private Node<T> last;
/*     */   private int size;
/*     */   
/*     */   public void addFirst(Node<T> tNode) {
/*  12 */     checkNoParent(tNode);
/*     */     
/*  14 */     if (isEmpty()) {
/*     */       
/*  16 */       this.first = tNode;
/*  17 */       this.last = tNode;
/*     */     } else {
/*  19 */       Node<T> node = this.first;
/*  20 */       tNode.setNext(node);
/*  21 */       node.setPrev(tNode);
/*  22 */       this.first = tNode;
/*     */     } 
/*     */     
/*  25 */     tNode.setParent(this);
/*  26 */     this.size++;
/*     */   }
/*     */   
/*     */   public void addLast(Node<T> tNode) {
/*  30 */     checkNoParent(tNode);
/*     */     
/*  32 */     if (isEmpty()) {
/*     */       
/*  34 */       this.first = tNode;
/*  35 */       this.last = tNode;
/*     */     } else {
/*  37 */       Node<T> node = this.last;
/*  38 */       tNode.setPrev(node);
/*  39 */       node.setNext(tNode);
/*  40 */       this.last = tNode;
/*     */     } 
/*     */     
/*  43 */     tNode.setParent(this);
/*  44 */     this.size++;
/*     */   }
/*     */   
/*     */   public void addAfter(Node<T> nodePrev, Node<T> tNode) {
/*  48 */     if (nodePrev == null) {
/*  49 */       addFirst(tNode);
/*  50 */     } else if (nodePrev == this.last) {
/*  51 */       addLast(tNode);
/*     */     } else {
/*  53 */       checkParent(nodePrev);
/*  54 */       checkNoParent(tNode);
/*  55 */       Node<T> nodeNext = nodePrev.getNext();
/*  56 */       nodePrev.setNext(tNode);
/*  57 */       tNode.setPrev(nodePrev);
/*  58 */       nodeNext.setPrev(tNode);
/*  59 */       tNode.setNext(nodeNext);
/*  60 */       tNode.setParent(this);
/*  61 */       this.size++;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Node<T> remove(Node<T> tNode) {
/*  66 */     checkParent(tNode);
/*  67 */     Node<T> prev = tNode.getPrev();
/*  68 */     Node<T> next = tNode.getNext();
/*     */     
/*  70 */     if (prev != null) {
/*  71 */       prev.setNext(next);
/*     */     } else {
/*  73 */       this.first = next;
/*     */     } 
/*     */     
/*  76 */     if (next != null) {
/*  77 */       next.setPrev(prev);
/*     */     } else {
/*  79 */       this.last = prev;
/*     */     } 
/*     */     
/*  82 */     tNode.setPrev(null);
/*  83 */     tNode.setNext(null);
/*  84 */     tNode.setParent(null);
/*  85 */     this.size--;
/*  86 */     return tNode;
/*     */   }
/*     */ 
/*     */   
/*     */   public void moveAfter(Node<T> nodePrev, Node<T> node) {
/*  91 */     remove(node);
/*  92 */     addAfter(nodePrev, node);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean find(Node<T> nodeFind, Node<T> nodeFrom, Node<T> nodeTo) {
/*  97 */     checkParent(nodeFrom);
/*     */     
/*  99 */     if (nodeTo != null)
/*     */     {
/* 101 */       checkParent(nodeTo);
/*     */     }
/*     */     
/*     */     Node<T> node;
/*     */     
/* 106 */     for (node = nodeFrom; node != null && node != nodeTo; node = node.getNext()) {
/*     */       
/* 108 */       if (node == nodeFind)
/*     */       {
/* 110 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 114 */     if (node != nodeTo)
/*     */     {
/* 116 */       throw new IllegalArgumentException("Sublist is not linked, from: " + nodeFrom + ", to: " + nodeTo);
/*     */     }
/*     */ 
/*     */     
/* 120 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkParent(Node<T> node) {
/* 126 */     if (node.parent != this)
/*     */     {
/* 128 */       throw new IllegalArgumentException("Node has different parent, node: " + node + ", parent: " + node.parent + ", this: " + this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkNoParent(Node<T> node) {
/* 134 */     if (node.parent != null)
/*     */     {
/* 136 */       throw new IllegalArgumentException("Node has different parent, node: " + node + ", parent: " + node.parent + ", this: " + this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Node<T> node) {
/* 142 */     return (node.parent == this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<Node<T>> iterator() {
/* 147 */     Iterator<Node<T>> iterator = new Iterator<Node<T>>()
/*     */       {
/* 149 */         LinkedList.Node<T> node = LinkedList.this.getFirst();
/*     */         
/*     */         public boolean hasNext() {
/* 152 */           return (this.node != null);
/*     */         }
/*     */         
/*     */         public LinkedList.Node<T> next() {
/* 156 */           LinkedList.Node<T> node = this.node;
/*     */           
/* 158 */           if (this.node != null)
/*     */           {
/* 160 */             this.node = this.node.next;
/*     */           }
/*     */           
/* 163 */           return node;
/*     */         }
/*     */         
/*     */         public void remove() {
/* 167 */           throw new UnsupportedOperationException("remove");
/*     */         }
/*     */       };
/* 170 */     return iterator;
/*     */   }
/*     */ 
/*     */   
/*     */   public Node<T> getFirst() {
/* 175 */     return this.first;
/*     */   }
/*     */ 
/*     */   
/*     */   public Node<T> getLast() {
/* 180 */     return this.last;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSize() {
/* 185 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 190 */     return (this.size <= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 195 */     StringBuffer stringbuffer = new StringBuffer();
/*     */     
/* 197 */     for (Iterator<Node<T>> it = iterator(); it.hasNext(); ) {
/* 198 */       Node<T> node = it.next();
/* 199 */       if (stringbuffer.length() > 0)
/*     */       {
/* 201 */         stringbuffer.append(", ");
/*     */       }
/*     */       
/* 204 */       stringbuffer.append(node.getItem());
/*     */     } 
/*     */     
/* 207 */     return "" + this.size + " [" + stringbuffer.toString() + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Node<T>
/*     */   {
/*     */     private final T item;
/*     */     private Node<T> prev;
/*     */     private Node<T> next;
/*     */     private LinkedList<T> parent;
/*     */     
/*     */     public Node(T item) {
/* 219 */       this.item = item;
/*     */     }
/*     */ 
/*     */     
/*     */     public T getItem() {
/* 224 */       return this.item;
/*     */     }
/*     */ 
/*     */     
/*     */     public Node<T> getPrev() {
/* 229 */       return this.prev;
/*     */     }
/*     */ 
/*     */     
/*     */     public Node<T> getNext() {
/* 234 */       return this.next;
/*     */     }
/*     */ 
/*     */     
/*     */     private void setPrev(Node<T> prev) {
/* 239 */       this.prev = prev;
/*     */     }
/*     */ 
/*     */     
/*     */     private void setNext(Node<T> next) {
/* 244 */       this.next = next;
/*     */     }
/*     */ 
/*     */     
/*     */     private void setParent(LinkedList<T> parent) {
/* 249 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 254 */       return "" + this.item;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifin\\util\LinkedList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */