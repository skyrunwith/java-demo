package com.fzd.language.map.source;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author fuzude
 * @version @Date: 2021-08-29
 */
public class HashMap<K, V> extends AbstractMap<K, V>
        implements Map<K, V>, Cloneable, Serializable {
    /**
     * 默认容量大小 16
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16
    /**
     * 最大容量 2的31次方
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;
    /**
     * 默认负载因子，0.75，
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    /**
     * 链表转红黑树的阈值，添加element时转换
     */
    static final int TREEIFY_THRESHOLD = 8;
    /**
     * 红黑树转链表的阈值，resize时转换, 红黑树长度小于8，最多6
     */
    static final int UNTREEIFY_THRESHOLD = 6;
    /**
     * 红黑树最小容量
     */
    static final int MIN_TREEIFY_CAPACITY = 64;

    /**
     * hash 算法：将高16位与低16位取异或
     * 1.为什么取异或呢？异或可以使得到的0,1 二进制尽量的平均
     * 0 0 1 1
     * 0 1 0 1 取&
     * 0 0 0 1 0 的概率0.75 1 的概率0.25
     * 0 0 11
     * 0 1 0 1 取|
     * 0 1 1 1 0 的概率0.25 1 的概率0.75
     * 0 0 1 1
     * 0 1 0 1 取^
     * 0 1 1 0 的概率 0.5 1的概率0.5
     */
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    /**
     * 格式化容量为2对幂
     * 通过给定容量计算大于给定容量的最小2的次方数。如cap=3，则结果为4，cap=10，结果为16
     */
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    /**
     * 存放key，value的地方，在第一次使用时初始化，table长度必须是2的幂
     */
    transient Node<K, V>[] table;

    transient Set<Map.Entry<K, V>> entrySet;
    /**
     * map中key、value的数量，即已使用的容量
     */
    transient int size;
    /**
     * HashMap被结构化修改的次数(用于迭代器fast-fail)
     */
    transient int modCount;
    /**
     * resize 阈值，下一次扩展阈值 = capacity * load factor
     */
    int threshold;
    /**
     * hashtable 的负载因子
     */
    final float loadFactor;

    /**
     * 构造方法，实际使用时推荐设置好初始容量，可以显著减少resize，提升效率
     */
    public HashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                    initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " +
                    loadFactor);
        this.loadFactor = loadFactor;
        //将初始容量放入threshold
        this.threshold = tableSizeFor(initialCapacity);
    }

    public HashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public HashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
    }

    public HashMap(Map<? extends K, ? extends V> m) {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        putMapEntries(m, false);
    }

    /**
     * 实现 Map.putAll 和 Map 构造函数。
     *
     * @param m
     * @param evict false 是构造方法，else true
     */
    final void putMapEntries(Map<? extends K, ? extends V> m, boolean evict) {
        int s = m.size();
        if (s > 0) {
            //如果 table 为null, 计算map m的容量，如果容量大于阈值，将重新计算阈值
            //问题：为什么不需要扩容呢？
            if (table == null) { // pre-size
                float ft = ((float) s / loadFactor) + 1.0F;
                int t = ((ft < (float) MAXIMUM_CAPACITY) ?
                        (int) ft : MAXIMUM_CAPACITY);
                if (t > threshold)
                    threshold = tableSizeFor(t);
            }
            //如果 table 不为null，并且新增的map.size大于阈值，则需要扩容
            else if (s > threshold)
                resize();
            //遍历并将新增map添加到table中
            for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
                K key = e.getKey();
                V value = e.getValue();
                putVal(hash(key), key, value, false, evict);
            }
        }
    }

    /**
     * map 中key-value对数量
     */
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 获取key映射对value(也坑为null)，key不存在时返回 null
     */
    public V get(Object key) {
        HashMap.Node<K, V> e;
        return (e = getNode(hash(key), key)) == null ? null : e.value;
    }

    /**
     * 实现 Map.get 和 相关方法
     * 根据 hash 和 key 获取相应node
     */
    final HashMap.Node<K, V> getNode(int hash, Object key) {
        HashMap.Node<K, V>[] tab;
        HashMap.Node<K, V> first, e;
        int n;
        K k;
        //根据hash值定位桶的位置，并获取桶的第一个节点
        if ((tab = table) != null && (n = tab.length) > 0 &&
                (first = tab[(n - 1) & hash]) != null) {
            //检查第一个元素：通过 hash 和 key 同时匹配，如果匹配得上，则返回第一个元素
            if (first.hash == hash && // always check first node
                    ((k = first.key) == key || (key != null && key.equals(k))))
                return first;
            if ((e = first.next) != null) {
                //如果 first 是TreeNode，则调用红黑树get方法
                if (first instanceof HashMap.TreeNode)
                    return ((HashMap.TreeNode<K, V>) first).getTreeNode(hash, key);
                //如果是链表，遍历链表即可
                do {
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
            }
        }
        return null;
    }

    /**
     * 如果 map 含有该key的mapping，则返回true
     */
    public boolean containsKey(Object key) {
        return getNode(hash(key), key) != null;
    }

    /**
     * 在 map 中关联 key 和 value，如果 key 已经存在，则替换掉旧的 value
     *
     * @param key
     * @param value
     * @return 返回该 key 关联的前一个 value(可以为null)，如果没有关联key，返回null
     */
    public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }

    /**
     * 实现 Map.put 和 相关方法
     *
     * @param hash         hash for key
     * @param key          the key
     * @param value        put 的值
     * @param onlyIfAbsent true：不改变存在的value
     * @param evict        false：table 是创建模式
     * @return
     */
    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        HashMap.Node<K, V>[] tab;
        HashMap.Node<K, V> p;
        int n, i;
        //table 为null或者空数组，则初始化table
        //table 初始化延迟到插入新数据时。HashMap构造方法并不会初始化table
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
        //如果桶中不包含key-value节点的引用，则将新key-value节点的引用存入桶table
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);
        else {
            HashMap.Node<K, V> e;
            K k;
            //如果hash和key的值等于链表中第一个key-value节点时，将e指向该节点
            if (p.hash == hash &&
                    ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;
                //如果桶中节点类型为TreeNode，则调用红黑树的插入方法
            else if (p instanceof HashMap.TreeNode)
                e = ((HashMap.TreeNode<K, V>) p).putTreeVal(this, tab, hash, key, value);
            else {
                //key-value节点是链表
                //遍历链表，如果key不存在，则将新key-value添加到链表末尾，然后根据桶大小判断是否需要转化为红黑树
                //         如果key存在，则退出遍历，e指向该节点
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {
                        p.next = newNode(hash, key, value, null);
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                            treeifyBin(tab, hash);
                        break;
                    }
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            //如果key-value已经存在map中，则执行更新value操作
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                //LinkedHashMap post-actions
                afterNodeAccess(e);
                return oldValue;
            }
        }
        ++modCount;
        //如果键值对数量大于阈值，则进行扩容
        if (++size > threshold)
            resize();
        //LinkedHashMap post-actions
        afterNodeInsertion(evict);
        return null;
    }

    /**
     * 作用：初始化、或扩容
     * 如果为table=null，则按照初始化容量分配容量
     * 如果table不为null，则扩容为原数组对两倍，并将存在的元素移动到新数组
     * <p>
     * 计算容量和阈值：
     * case 1. table为null：
     * 1.1 默认构造方法：使用默认容量和阈值，生成并返回空table
     * 1.2 带参构造方法：根据参数计算容量和阈值
     * case 2. table 不为 null
     * 2.1 容量达到最大值，将阈值设置为最大值，返回原table
     * 2.2 容量和阈值扩大为两倍
     *
     * @return
     */
    final Node<K, V>[] resize() {
        Node<K, V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        //table不为空，重新计算容量和阈值，容量和阈值为以前的两倍，且最大值为Integer.MAX_VALUE
        if (oldCap > 0) {
            //容量达到最大值，不再扩容，只将阈值扩大为最大值
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }
            //将旧容量和阈值扩大2倍
            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                    oldCap >= DEFAULT_INITIAL_CAPACITY)
                newThr = oldThr << 1; // double threshold
        } else if (oldThr > 0) // initial capacity was placed in threshold
            //HashMap在调用构造方法时，使用的是带有initialCapacity参数的构造器，使用threshold暂存容量的值，现在将threshold的赋值给newCap
            newCap = oldThr;
        else {               // zero initial threshold signifies using defaults
            //调用HashMap无参构造器时，使用默认容量和阈值
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        if (newThr == 0) {
            //根据新的容量值，计算新的阈值
            float ft = (float) newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float) MAXIMUM_CAPACITY ?
                    (int) ft : Integer.MAX_VALUE);
        }
        threshold = newThr;
        /* 计算容量和阈值的代码到此为止 */
        @SuppressWarnings({"rawtypes", "unchecked"})
        Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];
        table = newTab;
        //如果旧数组有值，则将旧数组的key-value mapping移动到新数组
        if (oldTab != null) {
            //遍历桶数组
            for (int j = 0; j < oldCap; ++j) {
                Node<K, V> e;
                //如果首节点不为null，则进行移动
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    //如果只有首节点，则重新计算hash位，本次循环结束
                    if (e.next == null)
                        newTab[e.hash & (newCap - 1)] = e;
                    else if (e instanceof TreeNode)
                        //如果是红黑树，调用红黑树的重定位方法
                        ((TreeNode<K, V>) e).split(this, newTab, j, oldCap);
                    else { // preserve order
                        Node<K, V> loHead = null, loTail = null;
                        Node<K, V> hiHead = null, hiTail = null;
                        Node<K, V> next;
                        /**
                         * 遍历链表，重定位按照是否需要移动进行分组
                         * 1.(e.hash & oldCap) == 0 表示不需要移动，保留原位
                         * 2.(e.hash & oldCap) != 0 表示需要移动到新的位置
                         * 3.为什么按照 e.hash & oldCap 进行分组？
                         * old:
                         *    26: 0001 1010 hashcode
                         *    15: 0000 1111
                         *     &: 0000 1010
                         * new:
                         *    26: 0001 1010 hashcode
                         *    31: 0001 1111
                         *     &: 0001 1010
                         * hash & oldCap => 26 & 16 = 16
                         * 从上面可以看出, 新的索引值与旧的索引值不一样，所以需要移动位置(hash & oldCap的值也不为0)
                         * 因为扩容的大小刚好是oldCap的大小，所以只需要判断原key的hash该位(oldCap的大小位：0001 0000)上是否为0，如果是，则不需要移动，否则需要移动
                         *
                         */
                        do {
                            next = e.next;
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            } else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }

    /**
     * 桶由链表转为红黑树
     * 替换给定hash值桶的所有链接节点，除非表太小的情况(会调整表大小)
     */
    final void treeifyBin(Node<K, V>[] tab, int hash) {
        int n, index;
        Node<K, V> e;
        //如果表为null或者table小于64，则调整表大小
        if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)
            resize();
        else if ((e = tab[index = (n - 1) & hash]) != null) {
            TreeNode<K, V> hd = null, tl = null;
            //遍历链表，将每个节点Node转为TreeNode
            do {
                TreeNode<K, V> p = replacementTreeNode(e, null);
                if (tl == null)
                    hd = p;
                else {
                    p.prev = tl;
                    tl.next = p;
                }
                tl = p;
            } while ((e = e.next) != null);
            if ((tab[index] = hd) != null)
                hd.treeify(tab);
        }
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        putMapEntries(m, true);
    }

    public V remove(Object key) {
        HashMap.Node<K, V> e;
        return (e = removeNode(hash(key), key, null, false, true)) == null ?
                null : e.value;
    }

    /**
     * 实现Map.remove 和相关方法
     *
     * @param hash       hash for key
     * @param key        the key
     * @param value      如果 matchValue 为 true，则该value为key匹配的value，否则忽略
     * @param matchValue 如果为true，仅移除value能够匹配上的key-value
     * @param movable    如果为false，删除该节点时，不移动其他节点
     * @return
     */
    final Node<K, V> removeNode(int hash, Object key, Object value,
                                boolean matchValue, boolean movable) {
        Node<K, V>[] tab;
        Node<K, V> p;
        int n, index;
        //如果table不为空，并且桶存在
        if ((tab = table) != null && (n = tab.length) > 0 &&
                (p = tab[index = (n - 1) & hash]) != null) {
            Node<K, V> node = null, e;
            K k;
            V v;
            //检查第一个元素，如果匹配上，则赋值给node
            if (p.hash == hash &&
                    ((k = p.key) == key || (key != null && key.equals(k))))
                node = p;
            else if ((e = p.next) != null) {
                //红黑树匹配
                if (p instanceof TreeNode)
                    node = ((TreeNode<K, V>) p).getTreeNode(hash, key);
                else {
                    //遍历链表匹配
                    do {
                        if (e.hash == hash &&
                                ((k = e.key) == key ||
                                        (key != null && key.equals(k)))) {
                            node = e;
                            break;
                        }
                        p = e;
                    } while ((e = e.next) != null);
                }
            }
            // node 不为null，表示找到 node。如果需要匹配上value(matchValue 为 true)，需要再进行value匹配
            if (node != null && (!matchValue || (v = node.value) == value ||
                    (value != null && value.equals(v)))) {
                if (node instanceof TreeNode)
                    //红黑树移除
                    ((TreeNode<K, V>) node).removeTreeNode(this, tab, movable);
                else if (node == p)
                    //链表首节点移除
                    tab[index] = node.next;
                else
                    //链表非首节点移除
                    p.next = node.next;
                ++modCount;
                --size;
                afterNodeRemoval(node);
                return node;
            }
        }
        return null;
    }

    /**
     * 清空 HashMap
     */
    public void clear() {
        Node<K, V>[] tab;
        modCount++;
        if ((tab = table) != null && size > 0) {
            size = 0;
            for (int i = 0; i < tab.length; ++i)
                tab[i] = null;
        }
    }

    /**
     * 遍历HashMap，判断是否存在key包含指定value
     */
    public boolean containsValue(Object value) {
        Node<K, V>[] tab;
        V v;
        if ((tab = table) != null && size > 0) {
            for (int i = 0; i < tab.length; ++i) {
                for (Node<K, V> e = tab[i]; e != null; e = e.next) {
                    if ((v = e.value) == value ||
                            (value != null && value.equals(v)))
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * 返回 map 中包含的所有 key {@link Set} 视图
     * 如果map有修改或删除操作，Set 视图也会相应变化(除了在迭代器中执行remove操作)
     * Set 视图执行remove、removeAll、retainAll、clear、Iterator.remove操作时，也会影响map
     */
    public Set<K> keySet() {
        Set<K> ks = keySet;
        if (ks == null) {
            ks = new KeySet();
            keySet = ks;
        }
        return ks;
    }

    /**
     * 返回 map 中包含的所有 value {@link Collection} 视图
     * 如果map有修改或删除操作，Set 视图也会相应变化(除了在迭代器中执行remove、setValue操作)
     * Set 视图执行remove、removeAll、retainAll、clear、Iterator.remove操作时，也会影响map
     */
    public Collection<V> values() {
        Collection<V> vs = values;
        if (vs == null) {
            vs = new Values();
            values = vs;
        }
        return vs;
    }

    /**
     * 返回 map 中包含的所有 mapping {@link Set} 视图
     * 如果map有修改或删除操作，Set 视图也会相应变化(除了在迭代器中执行remove操作)
     * Set 视图执行remove、removeAll、retainAll、clear、Iterator.remove操作时，也会影响map
     */
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> es;
        return (es = entrySet) == null ? (entrySet = new EntrySet()) : es;
    }

    // Overrides of JDK8 Map extension methods

    /**
     * 返回key匹配到到值或默认值
     */
    @Override
    public V getOrDefault(Object key, V defaultValue) {
        Node<K, V> e;
        return (e = getNode(hash(key), key)) == null ? defaultValue : e.value;
    }

    /**
     * 如果value为null，或者key-value不存在才存入桶数组
     */
    @Override
    public V putIfAbsent(K key, V value) {
        return putVal(hash(key), key, value, true, true);
    }

    /**
     * 如果key-value都匹配上才移除
     */
    @Override
    public boolean remove(Object key, Object value) {
        return removeNode(hash(key), key, value, true, true) != null;
    }

    /**
     * 如果 key 和 oldValue 能够匹配上，则使用 newValue 替换
     */
    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        Node<K, V> e;
        V v;
        if ((e = getNode(hash(key), key)) != null &&
                ((v = e.value) == oldValue || (v != null && v.equals(oldValue)))) {
            e.value = newValue;
            afterNodeAccess(e);
            return true;
        }
        return false;
    }

    /**
     * 如果 key 匹配上，则使用value替换
     */
    @Override
    public V replace(K key, V value) {
        Node<K, V> e;
        if ((e = getNode(hash(key), key)) != null) {
            V oldValue = e.value;
            e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
        return null;
    }

    /**
     * 如果 key - value 不存在或者value为null，将 mapping function计算出的值放入map
     */
    @Override
    public V computeIfAbsent(K key,
                             Function<? super K, ? extends V> mappingFunction) {
        if (mappingFunction == null)
            throw new NullPointerException();
        int hash = hash(key);
        Node<K, V>[] tab;
        Node<K, V> first;
        int n, i;
        int binCount = 0;
        TreeNode<K, V> t = null;
        Node<K, V> old = null;
        // 检查是否需要扩容，不知道为什么要判断 size> threshold
        // 这儿和put不一样，put是先插入新节点再 resize
        if (size > threshold || (tab = table) == null ||
                (n = tab.length) == 0)
            n = (tab = resize()).length;
        // key hash匹配到的桶不为null，
        if ((first = tab[i = (n - 1) & hash]) != null) {
            if (first instanceof TreeNode)
                old = (t = (TreeNode<K, V>) first).getTreeNode(hash, key);
            else {
                // 遍历链表，查找与key匹配的node
                Node<K, V> e = first;
                K k;
                do {
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k)))) {
                        old = e;
                        break;
                    }
                    ++binCount;
                } while ((e = e.next) != null);
            }
            // 如果通过key找到old节点，则直接返回，不更新map
            V oldValue;
            if (old != null && (oldValue = old.value) != null) {
                afterNodeAccess(old);
                return oldValue;
            }
        }
        // mapping function 计算value
        V v = mappingFunction.apply(key);
        if (v == null) {
            // value 为null，不存入桶
            return null;
        } else if (old != null) {
            // node存在，替换旧值
            old.value = v;
            afterNodeAccess(old);
            return v;
        } else if (t != null)
            // 如果桶是红黑树，则调用红黑树存入方法
            t.putTreeVal(this, tab, hash, key, v);
        else {
            // 如果 key 不存在，则新增桶节点
            tab[i] = newNode(hash, key, v, first);
            if (binCount >= TREEIFY_THRESHOLD - 1)
                treeifyBin(tab, hash);
        }
        ++modCount;
        ++size;
        afterNodeInsertion(true);
        return v;
    }

    /**
     * 如果 key - value 存在 and value不为null，将 mapping function计算出的值放入map
     * 如果 mapping 存在，mapping function 为 null，则移除 mapping
     */
    @Override
    public V computeIfPresent(K key,
                              BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        if (remappingFunction == null)
            throw new NullPointerException();
        Node<K, V> e;
        V oldValue;
        int hash = hash(key);
        if ((e = getNode(hash, key)) != null &&
                (oldValue = e.value) != null) {
            V v = remappingFunction.apply(key, oldValue);
            if (v != null) {
                // mapping function 计算结果不为null
                e.value = v;
                afterNodeAccess(e);
                return v;
            } else
                // mapping function 计算结果为null，则删除该节点
                removeNode(hash, key, null, false, true);
        }
        return null;
    }

    /**
     * 直接将 mapping function计算出的值放入map
     * 如果 mapping 存在，但mapping function 为 null，则移除 mapping
     */
    @Override
    public V compute(K key,
                     BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        if (remappingFunction == null)
            throw new NullPointerException();
        int hash = hash(key);
        Node<K, V>[] tab;
        Node<K, V> first;
        int n, i;
        int binCount = 0;
        TreeNode<K, V> t = null;
        Node<K, V> old = null;
        // 检查是否需要扩容，不知道为什么要判断 size> threshold
        // 这儿和put不一样，put是先插入新节点再 resize
        if (size > threshold || (tab = table) == null ||
                (n = tab.length) == 0)
            n = (tab = resize()).length;
        // key hash匹配到的桶不为null，
        if ((first = tab[i = (n - 1) & hash]) != null) {
            if (first instanceof TreeNode)
                old = (t = (TreeNode<K, V>) first).getTreeNode(hash, key);
            else {
                // 遍历链表，查找与key匹配的node
                Node<K, V> e = first;
                K k;
                do {
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k)))) {
                        old = e;
                        break;
                    }
                    ++binCount;
                } while ((e = e.next) != null);
            }
        }
        V oldValue = (old == null) ? null : old.value;
        V v = remappingFunction.apply(key, oldValue);
        //old != null and newValue != null，更新value，否则删除该节点
        if (old != null) {
            if (v != null) {
                old.value = v;
                afterNodeAccess(old);
            } else
                removeNode(hash, key, null, false, true);
        }
        // old == null，newValue !== null，则更新value
        else if (v != null) {
            if (t != null)
                t.putTreeVal(this, tab, hash, key, v);
            else {
                tab[i] = newNode(hash, key, v, first);
                if (binCount >= TREEIFY_THRESHOLD - 1)
                    treeifyBin(tab, hash);
            }
            ++modCount;
            ++size;
            afterNodeInsertion(true);
        }
        // old == null，newValue == null，保留 null
        return v;
    }

    /**
     * 将oldValue 和 value 通过 remapping function 合并为 newValue，放入map
     * 1.如果 mapping 不存在，则关联key和value
     * 2.如果 mapping 存在，mapping function 为 null，则移除 mapping
     */
    @Override
    public V merge(K key, V value,
                   BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        if (value == null)
            throw new NullPointerException();
        if (remappingFunction == null)
            throw new NullPointerException();
        int hash = hash(key);
        Node<K, V>[] tab;
        Node<K, V> first;
        int n, i;
        int binCount = 0;
        TreeNode<K, V> t = null;
        Node<K, V> old = null;
        // 检查是否需要扩容，不知道为什么要判断 size> threshold
        // 这儿和put不一样，put是先插入新节点再 resize
        if (size > threshold || (tab = table) == null ||
                (n = tab.length) == 0)
            n = (tab = resize()).length;
        // key hash匹配到的桶不为null，
        if ((first = tab[i = (n - 1) & hash]) != null) {
            if (first instanceof TreeNode)
                old = (t = (TreeNode<K, V>) first).getTreeNode(hash, key);
            else {
                // 遍历链表，查找与key匹配的node
                Node<K, V> e = first;
                K k;
                do {
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k)))) {
                        old = e;
                        break;
                    }
                    ++binCount;
                } while ((e = e.next) != null);
            }
        }
        //old != null and newValue != null，使用oldValue和newValue 通过mapping function 生成 value，否则删除该节点
        if (old != null) {
            V v;
            if (old.value != null)
                v = remappingFunction.apply(old.value, value);
            else
                v = value;
            if (v != null) {
                old.value = v;
                afterNodeAccess(old);
            } else
                removeNode(hash, key, null, false, true);
            return v;
        }
        // old == null，newValue !== null，则更新value
        if (value != null) {
            if (t != null)
                t.putTreeVal(this, tab, hash, key, value);
            else {
                tab[i] = newNode(hash, key, value, first);
                if (binCount >= TREEIFY_THRESHOLD - 1)
                    treeifyBin(tab, hash);
            }
            ++modCount;
            ++size;
            afterNodeInsertion(true);
        }
        return value;
    }

    /**
     * 双层for遍历桶数组、链表或红黑树
     * forEach里面的代码在很多地方都有用到，比如 KeyIterator
     *
     * @param action
     */
    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        Node<K, V>[] tab;
        if (action == null)
            throw new NullPointerException();
        if (size > 0 && (tab = table) != null) {
            int mc = modCount;
            for (int i = 0; i < tab.length; ++i) {
                for (Node<K, V> e = tab[i]; e != null; e = e.next)
                    action.accept(e.key, e.value);
            }
            if (modCount != mc)
                throw new ConcurrentModificationException();
        }
    }

    /**
     * 双层for遍历桶数组、链表或红黑树，并替换所有mapping的value
     * forEach里面的代码在很多地方都有用到，比如 KeyIterator
     *
     * @param action
     */
    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        Node<K, V>[] tab;
        if (function == null)
            throw new NullPointerException();
        if (size > 0 && (tab = table) != null) {
            int mc = modCount;
            for (int i = 0; i < tab.length; ++i) {
                for (Node<K, V> e = tab[i]; e != null; e = e.next) {
                    e.value = function.apply(e.key, e.value);
                }
            }
            if (modCount != mc)
                throw new ConcurrentModificationException();
        }
    }

    /**
     * 单向链表
     * 基本的hash bin(桶)，大多数entries都是使用该数据结构
     */
    static class Node<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final String toString() {
            return key + "=" + value;
        }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            if (o == this)
                return true;
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
                if (Objects.equals(key, e.getKey()) &&
                        Objects.equals(value, e.getValue()))
                    return true;
            }
            return false;
        }
    }
}
