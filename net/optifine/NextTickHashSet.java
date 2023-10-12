/*     */ package net.optifine;
/*     */ import com.google.common.collect.Iterators;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.LongHashMap;
/*     */ import net.minecraft.world.ChunkCoordIntPair;
/*     */ import net.minecraft.world.NextTickListEntry;
/*     */ 
/*     */ public class NextTickHashSet extends TreeSet {
/*  13 */   private LongHashMap longHashMap = new LongHashMap();
/*  14 */   private int minX = Integer.MIN_VALUE;
/*  15 */   private int minZ = Integer.MIN_VALUE;
/*  16 */   private int maxX = Integer.MIN_VALUE;
/*  17 */   private int maxZ = Integer.MIN_VALUE;
/*     */   
/*     */   private static final int UNDEFINED = -2147483648;
/*     */   
/*     */   public NextTickHashSet(Set oldSet) {
/*  22 */     for (Object object : oldSet)
/*     */     {
/*  24 */       add(object);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object obj) {
/*  30 */     if (!(obj instanceof NextTickListEntry))
/*     */     {
/*  32 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  36 */     NextTickListEntry nextticklistentry = (NextTickListEntry)obj;
/*  37 */     Set set = getSubSet(nextticklistentry, false);
/*  38 */     return (set == null) ? false : set.contains(nextticklistentry);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(Object obj) {
/*  44 */     if (!(obj instanceof NextTickListEntry))
/*     */     {
/*  46 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  50 */     NextTickListEntry nextticklistentry = (NextTickListEntry)obj;
/*     */     
/*  52 */     if (nextticklistentry == null)
/*     */     {
/*  54 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  58 */     Set<NextTickListEntry> set = getSubSet(nextticklistentry, true);
/*  59 */     boolean flag = set.add(nextticklistentry);
/*  60 */     boolean flag1 = super.add(obj);
/*     */     
/*  62 */     if (flag != flag1)
/*     */     {
/*  64 */       throw new IllegalStateException("Added: " + flag + ", addedParent: " + flag1);
/*     */     }
/*     */ 
/*     */     
/*  68 */     return flag1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean remove(Object obj) {
/*  76 */     if (!(obj instanceof NextTickListEntry))
/*     */     {
/*  78 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  82 */     NextTickListEntry nextticklistentry = (NextTickListEntry)obj;
/*  83 */     Set set = getSubSet(nextticklistentry, false);
/*     */     
/*  85 */     if (set == null)
/*     */     {
/*  87 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  91 */     boolean flag = set.remove(nextticklistentry);
/*  92 */     boolean flag1 = super.remove(nextticklistentry);
/*     */     
/*  94 */     if (flag != flag1)
/*     */     {
/*  96 */       throw new IllegalStateException("Added: " + flag + ", addedParent: " + flag1);
/*     */     }
/*     */ 
/*     */     
/* 100 */     return flag1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Set getSubSet(NextTickListEntry entry, boolean autoCreate) {
/* 108 */     if (entry == null)
/*     */     {
/* 110 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 114 */     BlockPos blockpos = entry.position;
/* 115 */     int i = blockpos.getX() >> 4;
/* 116 */     int j = blockpos.getZ() >> 4;
/* 117 */     return getSubSet(i, j, autoCreate);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Set getSubSet(int cx, int cz, boolean autoCreate) {
/* 123 */     long i = ChunkCoordIntPair.chunkXZ2Int(cx, cz);
/* 124 */     HashSet hashset = (HashSet)this.longHashMap.getValueByKey(i);
/*     */     
/* 126 */     if (hashset == null && autoCreate) {
/*     */       
/* 128 */       hashset = new HashSet();
/* 129 */       this.longHashMap.add(i, hashset);
/*     */     } 
/*     */     
/* 132 */     return hashset;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator iterator() {
/* 137 */     if (this.minX == Integer.MIN_VALUE)
/*     */     {
/* 139 */       return super.iterator();
/*     */     }
/* 141 */     if (size() <= 0)
/*     */     {
/* 143 */       return (Iterator)Iterators.emptyIterator();
/*     */     }
/*     */ 
/*     */     
/* 147 */     int i = this.minX >> 4;
/* 148 */     int j = this.minZ >> 4;
/* 149 */     int k = this.maxX >> 4;
/* 150 */     int l = this.maxZ >> 4;
/* 151 */     List<Iterator> list = new ArrayList();
/*     */     
/* 153 */     for (int i1 = i; i1 <= k; i1++) {
/*     */       
/* 155 */       for (int j1 = j; j1 <= l; j1++) {
/*     */         
/* 157 */         Set set = getSubSet(i1, j1, false);
/*     */         
/* 159 */         if (set != null)
/*     */         {
/* 161 */           list.add(set.iterator());
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 166 */     if (list.size() <= 0)
/*     */     {
/* 168 */       return (Iterator)Iterators.emptyIterator();
/*     */     }
/* 170 */     if (list.size() == 1)
/*     */     {
/* 172 */       return list.get(0);
/*     */     }
/*     */ 
/*     */     
/* 176 */     return Iterators.concat(list.iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIteratorLimits(int minX, int minZ, int maxX, int maxZ) {
/* 183 */     this.minX = Math.min(minX, maxX);
/* 184 */     this.minZ = Math.min(minZ, maxZ);
/* 185 */     this.maxX = Math.max(minX, maxX);
/* 186 */     this.maxZ = Math.max(minZ, maxZ);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearIteratorLimits() {
/* 191 */     this.minX = Integer.MIN_VALUE;
/* 192 */     this.minZ = Integer.MIN_VALUE;
/* 193 */     this.maxX = Integer.MIN_VALUE;
/* 194 */     this.maxZ = Integer.MIN_VALUE;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\NextTickHashSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */