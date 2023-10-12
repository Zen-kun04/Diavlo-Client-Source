/*     */ package net.minecraft.util;
/*     */ 
/*     */ public class IntHashMap<V>
/*     */ {
/*   5 */   private transient Entry<V>[] slots = (Entry<V>[])new Entry[16];
/*     */   private transient int count;
/*   7 */   private int threshold = 12;
/*   8 */   private final float growFactor = 0.75F;
/*     */ 
/*     */   
/*     */   private static int computeHash(int integer) {
/*  12 */     integer = integer ^ integer >>> 20 ^ integer >>> 12;
/*  13 */     return integer ^ integer >>> 7 ^ integer >>> 4;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int getSlotIndex(int hash, int slotCount) {
/*  18 */     return hash & slotCount - 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public V lookup(int p_76041_1_) {
/*  23 */     int i = computeHash(p_76041_1_);
/*     */     
/*  25 */     for (Entry<V> entry = this.slots[getSlotIndex(i, this.slots.length)]; entry != null; entry = entry.nextEntry) {
/*     */       
/*  27 */       if (entry.hashEntry == p_76041_1_)
/*     */       {
/*  29 */         return entry.valueEntry;
/*     */       }
/*     */     } 
/*     */     
/*  33 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsItem(int p_76037_1_) {
/*  38 */     return (lookupEntry(p_76037_1_) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   final Entry<V> lookupEntry(int p_76045_1_) {
/*  43 */     int i = computeHash(p_76045_1_);
/*     */     
/*  45 */     for (Entry<V> entry = this.slots[getSlotIndex(i, this.slots.length)]; entry != null; entry = entry.nextEntry) {
/*     */       
/*  47 */       if (entry.hashEntry == p_76045_1_)
/*     */       {
/*  49 */         return entry;
/*     */       }
/*     */     } 
/*     */     
/*  53 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addKey(int p_76038_1_, V p_76038_2_) {
/*  58 */     int i = computeHash(p_76038_1_);
/*  59 */     int j = getSlotIndex(i, this.slots.length);
/*     */     
/*  61 */     for (Entry<V> entry = this.slots[j]; entry != null; entry = entry.nextEntry) {
/*     */       
/*  63 */       if (entry.hashEntry == p_76038_1_) {
/*     */         
/*  65 */         entry.valueEntry = p_76038_2_;
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*  70 */     insert(i, p_76038_1_, p_76038_2_, j);
/*     */   }
/*     */ 
/*     */   
/*     */   private void grow(int p_76047_1_) {
/*  75 */     Entry<V>[] entry = this.slots;
/*  76 */     int i = entry.length;
/*     */     
/*  78 */     if (i == 1073741824) {
/*     */       
/*  80 */       this.threshold = Integer.MAX_VALUE;
/*     */     }
/*     */     else {
/*     */       
/*  84 */       Entry[] arrayOfEntry = new Entry[p_76047_1_];
/*  85 */       copyTo((Entry<V>[])arrayOfEntry);
/*  86 */       this.slots = (Entry<V>[])arrayOfEntry;
/*  87 */       getClass(); this.threshold = (int)(p_76047_1_ * 0.75F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void copyTo(Entry<V>[] p_76048_1_) {
/*  93 */     Entry<V>[] entry = this.slots;
/*  94 */     int i = p_76048_1_.length;
/*     */     
/*  96 */     for (int j = 0; j < entry.length; j++) {
/*     */       
/*  98 */       Entry<V> entry1 = entry[j];
/*     */       
/* 100 */       if (entry1 != null) {
/*     */         Entry<V> entry2;
/* 102 */         entry[j] = null;
/*     */ 
/*     */         
/*     */         do {
/* 106 */           entry2 = entry1.nextEntry;
/* 107 */           int k = getSlotIndex(entry1.slotHash, i);
/* 108 */           entry1.nextEntry = p_76048_1_[k];
/* 109 */           p_76048_1_[k] = entry1;
/* 110 */           entry1 = entry2;
/*     */         }
/* 112 */         while (entry2 != null);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V removeObject(int p_76049_1_) {
/* 123 */     Entry<V> entry = removeEntry(p_76049_1_);
/* 124 */     return (entry == null) ? null : entry.valueEntry;
/*     */   }
/*     */ 
/*     */   
/*     */   final Entry<V> removeEntry(int p_76036_1_) {
/* 129 */     int i = computeHash(p_76036_1_);
/* 130 */     int j = getSlotIndex(i, this.slots.length);
/* 131 */     Entry<V> entry = this.slots[j];
/*     */     
/*     */     Entry<V> entry1;
/*     */     
/* 135 */     for (entry1 = entry; entry1 != null; entry1 = entry2) {
/*     */       
/* 137 */       Entry<V> entry2 = entry1.nextEntry;
/*     */       
/* 139 */       if (entry1.hashEntry == p_76036_1_) {
/*     */         
/* 141 */         this.count--;
/*     */         
/* 143 */         if (entry == entry1) {
/*     */           
/* 145 */           this.slots[j] = entry2;
/*     */         }
/*     */         else {
/*     */           
/* 149 */           entry.nextEntry = entry2;
/*     */         } 
/*     */         
/* 152 */         return entry1;
/*     */       } 
/*     */       
/* 155 */       entry = entry1;
/*     */     } 
/*     */     
/* 158 */     return entry1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearMap() {
/* 163 */     Entry<V>[] entry = this.slots;
/*     */     
/* 165 */     for (int i = 0; i < entry.length; i++)
/*     */     {
/* 167 */       entry[i] = null;
/*     */     }
/*     */     
/* 170 */     this.count = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private void insert(int p_76040_1_, int p_76040_2_, V p_76040_3_, int p_76040_4_) {
/* 175 */     Entry<V> entry = this.slots[p_76040_4_];
/* 176 */     this.slots[p_76040_4_] = new Entry<>(p_76040_1_, p_76040_2_, p_76040_3_, entry);
/*     */     
/* 178 */     if (this.count++ >= this.threshold)
/*     */     {
/* 180 */       grow(2 * this.slots.length);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class Entry<V>
/*     */   {
/*     */     final int hashEntry;
/*     */     V valueEntry;
/*     */     Entry<V> nextEntry;
/*     */     final int slotHash;
/*     */     
/*     */     Entry(int p_i1552_1_, int p_i1552_2_, V p_i1552_3_, Entry<V> p_i1552_4_) {
/* 193 */       this.valueEntry = p_i1552_3_;
/* 194 */       this.nextEntry = p_i1552_4_;
/* 195 */       this.hashEntry = p_i1552_2_;
/* 196 */       this.slotHash = p_i1552_1_;
/*     */     }
/*     */ 
/*     */     
/*     */     public final int getHash() {
/* 201 */       return this.hashEntry;
/*     */     }
/*     */ 
/*     */     
/*     */     public final V getValue() {
/* 206 */       return this.valueEntry;
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean equals(Object p_equals_1_) {
/* 211 */       if (!(p_equals_1_ instanceof Entry))
/*     */       {
/* 213 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 217 */       Entry<V> entry = (Entry<V>)p_equals_1_;
/* 218 */       Object object = Integer.valueOf(getHash());
/* 219 */       Object object1 = Integer.valueOf(entry.getHash());
/*     */       
/* 221 */       if (object == object1 || (object != null && object.equals(object1))) {
/*     */         
/* 223 */         Object object2 = getValue();
/* 224 */         Object object3 = entry.getValue();
/*     */         
/* 226 */         if (object2 == object3 || (object2 != null && object2.equals(object3)))
/*     */         {
/* 228 */           return true;
/*     */         }
/*     */       } 
/*     */       
/* 232 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public final int hashCode() {
/* 238 */       return IntHashMap.computeHash(this.hashEntry);
/*     */     }
/*     */ 
/*     */     
/*     */     public final String toString() {
/* 243 */       return getHash() + "=" + getValue();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\IntHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */