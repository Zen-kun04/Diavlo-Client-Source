/*     */ package net.optifine.util;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ public class ArrayUtils
/*     */ {
/*     */   public static boolean contains(Object[] arr, Object val) {
/*  13 */     if (arr == null)
/*     */     {
/*  15 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  19 */     for (int i = 0; i < arr.length; i++) {
/*     */       
/*  21 */       Object object = arr[i];
/*     */       
/*  23 */       if (object == val)
/*     */       {
/*  25 */         return true;
/*     */       }
/*     */     } 
/*     */     
/*  29 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] addIntsToArray(int[] intArray, int[] copyFrom) {
/*  35 */     if (intArray != null && copyFrom != null) {
/*     */       
/*  37 */       int i = intArray.length;
/*  38 */       int j = i + copyFrom.length;
/*  39 */       int[] aint = new int[j];
/*  40 */       System.arraycopy(intArray, 0, aint, 0, i);
/*     */       
/*  42 */       for (int k = 0; k < copyFrom.length; k++)
/*     */       {
/*  44 */         aint[k + i] = copyFrom[k];
/*     */       }
/*     */       
/*  47 */       return aint;
/*     */     } 
/*     */ 
/*     */     
/*  51 */     throw new NullPointerException("The given array is NULL");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] addIntToArray(int[] intArray, int intValue) {
/*  57 */     return addIntsToArray(intArray, new int[] { intValue });
/*     */   }
/*     */ 
/*     */   
/*     */   public static Object[] addObjectsToArray(Object[] arr, Object[] objs) {
/*  62 */     if (arr == null)
/*     */     {
/*  64 */       throw new NullPointerException("The given array is NULL");
/*     */     }
/*  66 */     if (objs.length == 0)
/*     */     {
/*  68 */       return arr;
/*     */     }
/*     */ 
/*     */     
/*  72 */     int i = arr.length;
/*  73 */     int j = i + objs.length;
/*  74 */     Object[] aobject = (Object[])Array.newInstance(arr.getClass().getComponentType(), j);
/*  75 */     System.arraycopy(arr, 0, aobject, 0, i);
/*  76 */     System.arraycopy(objs, 0, aobject, i, objs.length);
/*  77 */     return aobject;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object[] addObjectToArray(Object[] arr, Object obj) {
/*  83 */     if (arr == null)
/*     */     {
/*  85 */       throw new NullPointerException("The given array is NULL");
/*     */     }
/*     */ 
/*     */     
/*  89 */     int i = arr.length;
/*  90 */     int j = i + 1;
/*  91 */     Object[] aobject = (Object[])Array.newInstance(arr.getClass().getComponentType(), j);
/*  92 */     System.arraycopy(arr, 0, aobject, 0, i);
/*  93 */     aobject[i] = obj;
/*  94 */     return aobject;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object[] addObjectToArray(Object[] arr, Object obj, int index) {
/* 100 */     List<Object> list = new ArrayList(Arrays.asList(arr));
/* 101 */     list.add(index, obj);
/* 102 */     Object[] aobject = (Object[])Array.newInstance(arr.getClass().getComponentType(), list.size());
/* 103 */     return list.toArray(aobject);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String arrayToString(boolean[] arr, String separator) {
/* 108 */     if (arr == null)
/*     */     {
/* 110 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 114 */     StringBuffer stringbuffer = new StringBuffer(arr.length * 5);
/*     */     
/* 116 */     for (int i = 0; i < arr.length; i++) {
/*     */       
/* 118 */       boolean flag = arr[i];
/*     */       
/* 120 */       if (i > 0)
/*     */       {
/* 122 */         stringbuffer.append(separator);
/*     */       }
/*     */       
/* 125 */       stringbuffer.append(String.valueOf(flag));
/*     */     } 
/*     */     
/* 128 */     return stringbuffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String arrayToString(float[] arr) {
/* 134 */     return arrayToString(arr, ", ");
/*     */   }
/*     */ 
/*     */   
/*     */   public static String arrayToString(float[] arr, String separator) {
/* 139 */     if (arr == null)
/*     */     {
/* 141 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 145 */     StringBuffer stringbuffer = new StringBuffer(arr.length * 5);
/*     */     
/* 147 */     for (int i = 0; i < arr.length; i++) {
/*     */       
/* 149 */       float f = arr[i];
/*     */       
/* 151 */       if (i > 0)
/*     */       {
/* 153 */         stringbuffer.append(separator);
/*     */       }
/*     */       
/* 156 */       stringbuffer.append(String.valueOf(f));
/*     */     } 
/*     */     
/* 159 */     return stringbuffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String arrayToString(float[] arr, String separator, String format) {
/* 165 */     if (arr == null)
/*     */     {
/* 167 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 171 */     StringBuffer stringbuffer = new StringBuffer(arr.length * 5);
/*     */     
/* 173 */     for (int i = 0; i < arr.length; i++) {
/*     */       
/* 175 */       float f = arr[i];
/*     */       
/* 177 */       if (i > 0)
/*     */       {
/* 179 */         stringbuffer.append(separator);
/*     */       }
/*     */       
/* 182 */       stringbuffer.append(String.format(format, new Object[] { Float.valueOf(f) }));
/*     */     } 
/*     */     
/* 185 */     return stringbuffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String arrayToString(int[] arr) {
/* 191 */     return arrayToString(arr, ", ");
/*     */   }
/*     */ 
/*     */   
/*     */   public static String arrayToString(int[] arr, String separator) {
/* 196 */     if (arr == null)
/*     */     {
/* 198 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 202 */     StringBuffer stringbuffer = new StringBuffer(arr.length * 5);
/*     */     
/* 204 */     for (int i = 0; i < arr.length; i++) {
/*     */       
/* 206 */       int j = arr[i];
/*     */       
/* 208 */       if (i > 0)
/*     */       {
/* 210 */         stringbuffer.append(separator);
/*     */       }
/*     */       
/* 213 */       stringbuffer.append(String.valueOf(j));
/*     */     } 
/*     */     
/* 216 */     return stringbuffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String arrayToHexString(int[] arr, String separator) {
/* 222 */     if (arr == null)
/*     */     {
/* 224 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 228 */     StringBuffer stringbuffer = new StringBuffer(arr.length * 5);
/*     */     
/* 230 */     for (int i = 0; i < arr.length; i++) {
/*     */       
/* 232 */       int j = arr[i];
/*     */       
/* 234 */       if (i > 0)
/*     */       {
/* 236 */         stringbuffer.append(separator);
/*     */       }
/*     */       
/* 239 */       stringbuffer.append("0x");
/* 240 */       stringbuffer.append(Integer.toHexString(j));
/*     */     } 
/*     */     
/* 243 */     return stringbuffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String arrayToString(Object[] arr) {
/* 249 */     return arrayToString(arr, ", ");
/*     */   }
/*     */ 
/*     */   
/*     */   public static String arrayToString(Object[] arr, String separator) {
/* 254 */     if (arr == null)
/*     */     {
/* 256 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 260 */     StringBuffer stringbuffer = new StringBuffer(arr.length * 5);
/*     */     
/* 262 */     for (int i = 0; i < arr.length; i++) {
/*     */       
/* 264 */       Object object = arr[i];
/*     */       
/* 266 */       if (i > 0)
/*     */       {
/* 268 */         stringbuffer.append(separator);
/*     */       }
/*     */       
/* 271 */       stringbuffer.append(String.valueOf(object));
/*     */     } 
/*     */     
/* 274 */     return stringbuffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object[] collectionToArray(Collection coll, Class<?> elementClass) {
/* 280 */     if (coll == null)
/*     */     {
/* 282 */       return null;
/*     */     }
/* 284 */     if (elementClass == null)
/*     */     {
/* 286 */       return null;
/*     */     }
/* 288 */     if (elementClass.isPrimitive())
/*     */     {
/* 290 */       throw new IllegalArgumentException("Can not make arrays with primitive elements (int, double), element class: " + elementClass);
/*     */     }
/*     */ 
/*     */     
/* 294 */     Object[] aobject = (Object[])Array.newInstance(elementClass, coll.size());
/* 295 */     return coll.toArray(aobject);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean equalsOne(int val, int[] vals) {
/* 301 */     for (int i = 0; i < vals.length; i++) {
/*     */       
/* 303 */       if (vals[i] == val)
/*     */       {
/* 305 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 309 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean equalsOne(Object a, Object[] bs) {
/* 314 */     if (bs == null)
/*     */     {
/* 316 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 320 */     for (int i = 0; i < bs.length; i++) {
/*     */       
/* 322 */       Object object = bs[i];
/*     */       
/* 324 */       if (equals(a, object))
/*     */       {
/* 326 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 330 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean equals(Object o1, Object o2) {
/* 336 */     return (o1 == o2) ? true : ((o1 == null) ? false : o1.equals(o2));
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isSameOne(Object a, Object[] bs) {
/* 341 */     if (bs == null)
/*     */     {
/* 343 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 347 */     for (int i = 0; i < bs.length; i++) {
/*     */       
/* 349 */       Object object = bs[i];
/*     */       
/* 351 */       if (a == object)
/*     */       {
/* 353 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 357 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object[] removeObjectFromArray(Object[] arr, Object obj) {
/* 363 */     List list = new ArrayList(Arrays.asList(arr));
/* 364 */     list.remove(obj);
/* 365 */     Object[] aobject = collectionToArray(list, arr.getClass().getComponentType());
/* 366 */     return aobject;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int[] toPrimitive(Integer[] arr) {
/* 371 */     if (arr == null)
/*     */     {
/* 373 */       return null;
/*     */     }
/* 375 */     if (arr.length == 0)
/*     */     {
/* 377 */       return new int[0];
/*     */     }
/*     */ 
/*     */     
/* 381 */     int[] aint = new int[arr.length];
/*     */     
/* 383 */     for (int i = 0; i < aint.length; i++)
/*     */     {
/* 385 */       aint[i] = arr[i].intValue();
/*     */     }
/*     */     
/* 388 */     return aint;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifin\\util\ArrayUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */