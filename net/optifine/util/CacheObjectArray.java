/*     */ package net.optifine.util;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayDeque;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.src.Config;
/*     */ 
/*     */ 
/*     */ public class CacheObjectArray
/*     */ {
/*  11 */   private static ArrayDeque<int[]> arrays = (ArrayDeque)new ArrayDeque<>();
/*  12 */   private static int maxCacheSize = 10;
/*     */ 
/*     */   
/*     */   private static synchronized int[] allocateArray(int size) {
/*  16 */     int[] aint = arrays.pollLast();
/*     */     
/*  18 */     if (aint == null || aint.length < size)
/*     */     {
/*  20 */       aint = new int[size];
/*     */     }
/*     */     
/*  23 */     return aint;
/*     */   }
/*     */ 
/*     */   
/*     */   public static synchronized void freeArray(int[] ints) {
/*  28 */     if (arrays.size() < maxCacheSize)
/*     */     {
/*  30 */       arrays.add(ints);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/*  36 */     int i = 4096;
/*  37 */     int j = 500000;
/*  38 */     testNew(i, j);
/*  39 */     testClone(i, j);
/*  40 */     testNewObj(i, j);
/*  41 */     testCloneObj(i, j);
/*  42 */     testNewObjDyn(IBlockState.class, i, j);
/*  43 */     long k = testNew(i, j);
/*  44 */     long l = testClone(i, j);
/*  45 */     long i1 = testNewObj(i, j);
/*  46 */     long j1 = testCloneObj(i, j);
/*  47 */     long k1 = testNewObjDyn(IBlockState.class, i, j);
/*  48 */     Config.dbg("New: " + k);
/*  49 */     Config.dbg("Clone: " + l);
/*  50 */     Config.dbg("NewObj: " + i1);
/*  51 */     Config.dbg("CloneObj: " + j1);
/*  52 */     Config.dbg("NewObjDyn: " + k1);
/*     */   }
/*     */ 
/*     */   
/*     */   private static long testClone(int size, int count) {
/*  57 */     long i = System.currentTimeMillis();
/*  58 */     int[] aint = new int[size];
/*     */     
/*  60 */     for (int j = 0; j < count; j++)
/*     */     {
/*  62 */       int[] arrayOfInt = (int[])aint.clone();
/*     */     }
/*     */     
/*  65 */     long k = System.currentTimeMillis();
/*  66 */     return k - i;
/*     */   }
/*     */ 
/*     */   
/*     */   private static long testNew(int size, int count) {
/*  71 */     long i = System.currentTimeMillis();
/*     */     
/*  73 */     for (int j = 0; j < count; j++)
/*     */     {
/*  75 */       int[] arrayOfInt = (int[])Array.newInstance(int.class, size);
/*     */     }
/*     */     
/*  78 */     long k = System.currentTimeMillis();
/*  79 */     return k - i;
/*     */   }
/*     */ 
/*     */   
/*     */   private static long testCloneObj(int size, int count) {
/*  84 */     long i = System.currentTimeMillis();
/*  85 */     IBlockState[] aiblockstate = new IBlockState[size];
/*     */     
/*  87 */     for (int j = 0; j < count; j++)
/*     */     {
/*  89 */       IBlockState[] arrayOfIBlockState = (IBlockState[])aiblockstate.clone();
/*     */     }
/*     */     
/*  92 */     long k = System.currentTimeMillis();
/*  93 */     return k - i;
/*     */   }
/*     */ 
/*     */   
/*     */   private static long testNewObj(int size, int count) {
/*  98 */     long i = System.currentTimeMillis();
/*     */     
/* 100 */     for (int j = 0; j < count; j++)
/*     */     {
/* 102 */       IBlockState[] arrayOfIBlockState = new IBlockState[size];
/*     */     }
/*     */     
/* 105 */     long k = System.currentTimeMillis();
/* 106 */     return k - i;
/*     */   }
/*     */ 
/*     */   
/*     */   private static long testNewObjDyn(Class<?> cls, int size, int count) {
/* 111 */     long i = System.currentTimeMillis();
/*     */     
/* 113 */     for (int j = 0; j < count; j++)
/*     */     {
/* 115 */       Object[] arrayOfObject = (Object[])Array.newInstance(cls, size);
/*     */     }
/*     */     
/* 118 */     long k = System.currentTimeMillis();
/* 119 */     return k - i;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifin\\util\CacheObjectArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */