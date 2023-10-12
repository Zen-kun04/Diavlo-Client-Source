/*     */ package net.minecraft.util;
/*     */ 
/*     */ public class LongHashMap<V>
/*     */ {
/*   5 */   private transient Entry<V>[] hashArray = (Entry<V>[])new Entry[4096];
/*     */   private transient int numHashElements;
/*     */   private int mask;
/*   8 */   private int capacity = 3072;
/*   9 */   private final float percentUseable = 0.75F;
/*     */   
/*     */   private volatile transient int modCount;
/*     */   
/*     */   public LongHashMap() {
/*  14 */     this.mask = this.hashArray.length - 1;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int getHashedKey(long originalKey) {
/*  19 */     return (int)(originalKey ^ originalKey >>> 27L);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int hash(int integer) {
/*  24 */     integer = integer ^ integer >>> 20 ^ integer >>> 12;
/*  25 */     return integer ^ integer >>> 7 ^ integer >>> 4;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int getHashIndex(int p_76158_0_, int p_76158_1_) {
/*  30 */     return p_76158_0_ & p_76158_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumHashElements() {
/*  35 */     return this.numHashElements;
/*     */   }
/*     */ 
/*     */   
/*     */   public V getValueByKey(long p_76164_1_) {
/*  40 */     int i = getHashedKey(p_76164_1_);
/*     */     
/*  42 */     for (Entry<V> entry = this.hashArray[getHashIndex(i, this.mask)]; entry != null; entry = entry.nextEntry) {
/*     */       
/*  44 */       if (entry.key == p_76164_1_)
/*     */       {
/*  46 */         return entry.value;
/*     */       }
/*     */     } 
/*     */     
/*  50 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsItem(long p_76161_1_) {
/*  55 */     return (getEntry(p_76161_1_) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   final Entry<V> getEntry(long p_76160_1_) {
/*  60 */     int i = getHashedKey(p_76160_1_);
/*     */     
/*  62 */     for (Entry<V> entry = this.hashArray[getHashIndex(i, this.mask)]; entry != null; entry = entry.nextEntry) {
/*     */       
/*  64 */       if (entry.key == p_76160_1_)
/*     */       {
/*  66 */         return entry;
/*     */       }
/*     */     } 
/*     */     
/*  70 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(long p_76163_1_, V p_76163_3_) {
/*  75 */     int i = getHashedKey(p_76163_1_);
/*  76 */     int j = getHashIndex(i, this.mask);
/*     */     
/*  78 */     for (Entry<V> entry = this.hashArray[j]; entry != null; entry = entry.nextEntry) {
/*     */       
/*  80 */       if (entry.key == p_76163_1_) {
/*     */         
/*  82 */         entry.value = p_76163_3_;
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*  87 */     this.modCount++;
/*  88 */     createKey(i, p_76163_1_, p_76163_3_, j);
/*     */   }
/*     */ 
/*     */   
/*     */   private void resizeTable(int p_76153_1_) {
/*  93 */     Entry<V>[] entry = this.hashArray;
/*  94 */     int i = entry.length;
/*     */     
/*  96 */     if (i == 1073741824) {
/*     */       
/*  98 */       this.capacity = Integer.MAX_VALUE;
/*     */     }
/*     */     else {
/*     */       
/* 102 */       Entry[] arrayOfEntry = new Entry[p_76153_1_];
/* 103 */       copyHashTableTo((Entry<V>[])arrayOfEntry);
/* 104 */       this.hashArray = (Entry<V>[])arrayOfEntry;
/* 105 */       this.mask = this.hashArray.length - 1;
/* 106 */       float f = p_76153_1_;
/* 107 */       getClass();
/* 108 */       this.capacity = (int)(f * 0.75F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void copyHashTableTo(Entry<V>[] p_76154_1_) {
/* 114 */     Entry<V>[] entry = this.hashArray;
/* 115 */     int i = p_76154_1_.length;
/*     */     
/* 117 */     for (int j = 0; j < entry.length; j++) {
/*     */       
/* 119 */       Entry<V> entry1 = entry[j];
/*     */       
/* 121 */       if (entry1 != null) {
/*     */         Entry<V> entry2;
/* 123 */         entry[j] = null;
/*     */ 
/*     */         
/*     */         do {
/* 127 */           entry2 = entry1.nextEntry;
/* 128 */           int k = getHashIndex(entry1.hash, i - 1);
/* 129 */           entry1.nextEntry = p_76154_1_[k];
/* 130 */           p_76154_1_[k] = entry1;
/* 131 */           entry1 = entry2;
/*     */         }
/* 133 */         while (entry2 != null);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V remove(long p_76159_1_) {
/* 144 */     Entry<V> entry = removeKey(p_76159_1_);
/* 145 */     return (entry == null) ? null : entry.value;
/*     */   }
/*     */ 
/*     */   
/*     */   final Entry<V> removeKey(long p_76152_1_) {
/* 150 */     int i = getHashedKey(p_76152_1_);
/* 151 */     int j = getHashIndex(i, this.mask);
/* 152 */     Entry<V> entry = this.hashArray[j];
/*     */     
/*     */     Entry<V> entry1;
/*     */     
/* 156 */     for (entry1 = entry; entry1 != null; entry1 = entry2) {
/*     */       
/* 158 */       Entry<V> entry2 = entry1.nextEntry;
/*     */       
/* 160 */       if (entry1.key == p_76152_1_) {
/*     */         
/* 162 */         this.modCount++;
/* 163 */         this.numHashElements--;
/*     */         
/* 165 */         if (entry == entry1) {
/*     */           
/* 167 */           this.hashArray[j] = entry2;
/*     */         }
/*     */         else {
/*     */           
/* 171 */           entry.nextEntry = entry2;
/*     */         } 
/*     */         
/* 174 */         return entry1;
/*     */       } 
/*     */       
/* 177 */       entry = entry1;
/*     */     } 
/*     */     
/* 180 */     return entry1;
/*     */   }
/*     */ 
/*     */   
/*     */   private void createKey(int p_76156_1_, long p_76156_2_, V p_76156_4_, int p_76156_5_) {
/* 185 */     Entry<V> entry = this.hashArray[p_76156_5_];
/* 186 */     this.hashArray[p_76156_5_] = new Entry<>(p_76156_1_, p_76156_2_, p_76156_4_, entry);
/*     */     
/* 188 */     if (this.numHashElements++ >= this.capacity)
/*     */     {
/* 190 */       resizeTable(2 * this.hashArray.length);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public double getKeyDistribution() {
/* 196 */     int i = 0;
/*     */     
/* 198 */     for (int j = 0; j < this.hashArray.length; j++) {
/*     */       
/* 200 */       if (this.hashArray[j] != null)
/*     */       {
/* 202 */         i++;
/*     */       }
/*     */     } 
/*     */     
/* 206 */     return 1.0D * i / this.numHashElements;
/*     */   }
/*     */ 
/*     */   
/*     */   static class Entry<V>
/*     */   {
/*     */     final long key;
/*     */     V value;
/*     */     Entry<V> nextEntry;
/*     */     final int hash;
/*     */     
/*     */     Entry(int p_i1553_1_, long p_i1553_2_, V p_i1553_4_, Entry<V> p_i1553_5_) {
/* 218 */       this.value = p_i1553_4_;
/* 219 */       this.nextEntry = p_i1553_5_;
/* 220 */       this.key = p_i1553_2_;
/* 221 */       this.hash = p_i1553_1_;
/*     */     }
/*     */ 
/*     */     
/*     */     public final long getKey() {
/* 226 */       return this.key;
/*     */     }
/*     */ 
/*     */     
/*     */     public final V getValue() {
/* 231 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean equals(Object p_equals_1_) {
/* 236 */       if (!(p_equals_1_ instanceof Entry))
/*     */       {
/* 238 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 242 */       Entry<V> entry = (Entry<V>)p_equals_1_;
/* 243 */       Object object = Long.valueOf(getKey());
/* 244 */       Object object1 = Long.valueOf(entry.getKey());
/*     */       
/* 246 */       if (object == object1 || (object != null && object.equals(object1))) {
/*     */         
/* 248 */         Object object2 = getValue();
/* 249 */         Object object3 = entry.getValue();
/*     */         
/* 251 */         if (object2 == object3 || (object2 != null && object2.equals(object3)))
/*     */         {
/* 253 */           return true;
/*     */         }
/*     */       } 
/*     */       
/* 257 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public final int hashCode() {
/* 263 */       return LongHashMap.getHashedKey(this.key);
/*     */     }
/*     */ 
/*     */     
/*     */     public final String toString() {
/* 268 */       return getKey() + "=" + getValue();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\LongHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */