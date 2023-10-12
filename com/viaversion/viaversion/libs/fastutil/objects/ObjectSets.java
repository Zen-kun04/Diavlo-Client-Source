/*     */ package com.viaversion.viaversion.libs.fastutil.objects;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObjectSets
/*     */ {
/*     */   static final int ARRAY_SET_CUTOFF = 4;
/*     */   
/*     */   public static class EmptySet<K>
/*     */     extends ObjectCollections.EmptyCollection<K>
/*     */     implements ObjectSet<K>, Serializable, Cloneable
/*     */   {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */     
/*     */     public boolean remove(Object ok) {
/*  48 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public Object clone() {
/*  53 */       return ObjectSets.EMPTY_SET;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/*  59 */       return (o instanceof Set && ((Set)o).isEmpty());
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/*  63 */       return ObjectSets.EMPTY_SET;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   public static final EmptySet EMPTY_SET = new EmptySet();
/*     */ 
/*     */ 
/*     */   
/*  75 */   static final ObjectSet UNMODIFIABLE_EMPTY_SET = unmodifiable(new ObjectArraySet(ObjectArrays.EMPTY_ARRAY));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K> ObjectSet<K> emptySet() {
/*  87 */     return EMPTY_SET;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Singleton<K>
/*     */     extends AbstractObjectSet<K>
/*     */     implements Serializable, Cloneable
/*     */   {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */     
/*     */     protected final K element;
/*     */ 
/*     */     
/*     */     protected Singleton(K element) {
/* 101 */       this.element = element;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object k) {
/* 106 */       return Objects.equals(k, this.element);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object k) {
/* 111 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public ObjectListIterator<K> iterator() {
/* 116 */       return ObjectIterators.singleton(this.element);
/*     */     }
/*     */ 
/*     */     
/*     */     public ObjectSpliterator<K> spliterator() {
/* 121 */       return ObjectSpliterators.singleton(this.element);
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 126 */       return 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 131 */       return new Object[] { this.element };
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(Consumer<? super K> action) {
/* 136 */       action.accept(this.element);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean addAll(Collection<? extends K> c) {
/* 141 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeAll(Collection<?> c) {
/* 146 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean retainAll(Collection<?> c) {
/* 151 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeIf(Predicate<? super K> filter) {
/* 156 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public Object clone() {
/* 161 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K> ObjectSet<K> singleton(K element) {
/* 173 */     return new Singleton<>(element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K> ObjectSet<K> synchronize(ObjectSet<K> s) {
/* 204 */     return (ObjectSet<K>)new SynchronizedSet(s);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K> ObjectSet<K> synchronize(ObjectSet<K> s, Object sync) {
/* 217 */     return (ObjectSet<K>)new SynchronizedSet(s, sync);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K> ObjectSet<K> unmodifiable(ObjectSet<? extends K> s) {
/* 253 */     return (ObjectSet<K>)new UnmodifiableSet(s);
/*     */   }
/*     */   
/*     */   public static class ObjectSets {}
/*     */   
/*     */   public static class ObjectSets {}
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\fastutil\objects\ObjectSets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */