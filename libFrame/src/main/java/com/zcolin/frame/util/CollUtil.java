package com.zcolin.frame.util;

import com.zcolin.frame.comparator.PinyinComparator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 集合相关工具类
 * <p>
 * 此工具方法针对{@link Collection}及其实现类封装的工具。
 * <p>
 *
 * @author xiaoleilu
 * @since 3.1.1
 */
public class CollUtil {

    /**
     * 如果提供的集合为{@code null}，返回一个不可变的默认空集合，否则返回原集合<br>
     * 空集合使用{@link Collections#emptySet()}
     *
     * @param <T> 集合元素类型
     * @param set 提供的集合，可能为null
     *
     * @return 原集合，若为null返回空集合
     *
     * @since 4.6.3
     */
    public static <T> Set<T> emptyIfNull(Set<T> set) {
        return (null == set) ? Collections.emptySet() : set;
    }

    /**
     * 如果提供的集合为{@code null}，返回一个不可变的默认空集合，否则返回原集合<br>
     * 空集合使用{@link Collections#emptyList()}
     *
     * @param <T> 集合元素类型
     * @param set 提供的集合，可能为null
     *
     * @return 原集合，若为null返回空集合
     *
     * @since 4.6.3
     */
    public static <T> List<T> emptyIfNull(List<T> set) {
        return (null == set) ? Collections.emptyList() : set;
    }


    /**
     * 多个集合的并集<br>
     * 针对一个集合中存在多个相同元素的情况，计算两个集合中此元素的个数，保留最多的个数<br>
     * 例如：集合1：[a, b, c, c, c]，集合2：[a, b, c, c]<br>
     * 结果：[a, b, c, c, c]，此结果中只保留了三个c
     *
     * @param <T>        集合元素类型
     * @param coll1      集合1
     * @param coll2      集合2
     * @param otherColls 其它集合
     *
     * @return 并集的集合，返回 {@link ArrayList}
     */
    @SafeVarargs
    public static <T> Collection<T> union(Collection<T> coll1, Collection<T> coll2, Collection<T>... otherColls) {
        Collection<T> union = union(coll1, coll2);
        for (Collection<T> coll : otherColls) {
            union = union(union, coll);
        }
        return union;
    }


    /**
     * 多个集合的交集<br>
     * 针对一个集合中存在多个相同元素的情况，计算两个集合中此元素的个数，保留最少的个数<br>
     * 例如：集合1：[a, b, c, c, c]，集合2：[a, b, c, c]<br>
     * 结果：[a, b, c, c]，此结果中只保留了两个c
     *
     * @param <T>        集合元素类型
     * @param coll1      集合1
     * @param coll2      集合2
     * @param otherColls 其它集合
     *
     * @return 并集的集合，返回 {@link ArrayList}
     */
    @SafeVarargs
    public static <T> Collection<T> intersection(Collection<T> coll1, Collection<T> coll2,
            Collection<T>... otherColls) {
        Collection<T> intersection = intersection(coll1, coll2);
        if (isEmpty(intersection)) {
            return intersection;
        }
        for (Collection<T> coll : otherColls) {
            intersection = intersection(intersection, coll);
            if (isEmpty(intersection)) {
                return intersection;
            }
        }
        return intersection;
    }


    /**
     * 判断指定集合是否包含指定值，如果集合为空（null或者空），返回{@code false}，否则找到元素返回{@code true}
     *
     * @param collection 集合
     * @param value      需要查找的值
     *
     * @return 如果集合为空（null或者空），返回{@code false}，否则找到元素返回{@code true}
     *
     * @since 4.1.10
     */
    public static boolean contains(Collection<?> collection, Object value) {
        return isNotEmpty(collection) && collection.contains(value);
    }

    /**
     * 其中一个集合在另一个集合中是否至少包含一个元素，即是两个集合是否至少有一个共同的元素
     *
     * @param coll1 集合1
     * @param coll2 集合2
     *
     * @return 其中一个集合在另一个集合中是否至少包含一个元素
     *
     * @see #intersection
     * @since 2.1
     */
    public static boolean containsAny(Collection<?> coll1, Collection<?> coll2) {
        if (isEmpty(coll1) || isEmpty(coll2)) {
            return false;
        }
        if (coll1.size() < coll2.size()) {
            for (Object object : coll1) {
                if (coll2.contains(object)) {
                    return true;
                }
            }
        } else {
            for (Object object : coll2) {
                if (coll1.contains(object)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 集合1中是否包含集合2中所有的元素，即集合2是否为集合1的子集
     *
     * @param coll1 集合1
     * @param coll2 集合2
     *
     * @return 集合1中是否包含集合2中所有的元素
     *
     * @since 4.5.12
     */
    public static boolean containsAll(Collection<?> coll1, Collection<?> coll2) {
        if (isEmpty(coll1) || isEmpty(coll2) || coll1.size() < coll2.size()) {
            return false;
        }

        for (Object object : coll2) {
            if (!coll1.contains(object)) {
                return false;
            }
        }
        return true;
    }


    /**
     * 切取部分数据<br>
     * 切取后的栈将减少这些元素
     *
     * @param <T>             集合元素类型
     * @param surplusAlaDatas 原数据
     * @param partSize        每部分数据的长度
     *
     * @return 切取出的数据或null
     */
    public static <T> List<T> popPart(Stack<T> surplusAlaDatas, int partSize) {
        if (isEmpty(surplusAlaDatas)) {
            return null;
        }

        final List<T> currentAlaDatas = new ArrayList<>();
        int size = surplusAlaDatas.size();
        // 切割
        if (size > partSize) {
            for (int i = 0; i < partSize; i++) {
                currentAlaDatas.add(surplusAlaDatas.pop());
            }
        } else {
            for (int i = 0; i < size; i++) {
                currentAlaDatas.add(surplusAlaDatas.pop());
            }
        }
        return currentAlaDatas;
    }

    /**
     * 切取部分数据<br>
     * 切取后的栈将减少这些元素
     *
     * @param <T>             集合元素类型
     * @param surplusAlaDatas 原数据
     * @param partSize        每部分数据的长度
     *
     * @return 切取出的数据或null
     */
    public static <T> List<T> popPart(Deque<T> surplusAlaDatas, int partSize) {
        if (isEmpty(surplusAlaDatas)) {
            return null;
        }

        final List<T> currentAlaDatas = new ArrayList<>();
        int size = surplusAlaDatas.size();
        // 切割
        if (size > partSize) {
            for (int i = 0; i < partSize; i++) {
                currentAlaDatas.add(surplusAlaDatas.pop());
            }
        } else {
            for (int i = 0; i < size; i++) {
                currentAlaDatas.add(surplusAlaDatas.pop());
            }
        }
        return currentAlaDatas;
    }

    // ----------------------------------------------------------------------------------------------- new HashMap

    /**
     * 新建一个HashMap
     *
     * @param <K> Key类型
     * @param <V> Value类型
     *
     * @return HashMap对象
     *
     * @see MapUtil#newHashMap()
     */
    public static <K, V> HashMap<K, V> newHashMap() {
        return MapUtil.newHashMap();
    }

    /**
     * 新建一个HashMap
     *
     * @param <K>     Key类型
     * @param <V>     Value类型
     * @param size    初始大小，由于默认负载因子0.75，传入的size会实际初始大小为size / 0.75
     * @param isOrder Map的Key是否有序，有序返回 {@link LinkedHashMap}，否则返回 {@link HashMap}
     *
     * @return HashMap对象
     *
     * @see MapUtil#newHashMap(int, boolean)
     * @since 3.0.4
     */
    public static <K, V> HashMap<K, V> newHashMap(int size, boolean isOrder) {
        return MapUtil.newHashMap(size, isOrder);
    }

    /**
     * 新建一个HashMap
     *
     * @param <K>  Key类型
     * @param <V>  Value类型
     * @param size 初始大小，由于默认负载因子0.75，传入的size会实际初始大小为size / 0.75
     *
     * @return HashMap对象
     *
     * @see MapUtil#newHashMap(int)
     */
    public static <K, V> HashMap<K, V> newHashMap(int size) {
        return MapUtil.newHashMap(size);
    }

    // ----------------------------------------------------------------------------------------------- new HashSet

    /**
     * 新建一个HashSet
     *
     * @param <T> 集合元素类型
     * @param ts  元素数组
     *
     * @return HashSet对象
     */
    @SafeVarargs
    public static <T> HashSet<T> newHashSet(T... ts) {
        return newHashSet(false, ts);
    }

    /**
     * 新建一个LinkedHashSet
     *
     * @param <T> 集合元素类型
     * @param ts  元素数组
     *
     * @return HashSet对象
     *
     * @since 4.1.10
     */
    @SafeVarargs
    public static <T> LinkedHashSet<T> newLinkedHashSet(T... ts) {
        return (LinkedHashSet<T>) newHashSet(true, ts);
    }

    /**
     * 新建一个HashSet
     *
     * @param <T>      集合元素类型
     * @param isSorted 是否有序，有序返回 {@link LinkedHashSet}，否则返回 {@link HashSet}
     * @param ts       元素数组
     *
     * @return HashSet对象
     */
    @SafeVarargs
    public static <T> HashSet<T> newHashSet(boolean isSorted, T... ts) {
        if (null == ts) {
            return isSorted ? new LinkedHashSet<>() : new HashSet<>();
        }
        int initialCapacity = Math.max((int) (ts.length / .75f) + 1, 16);
        final HashSet<T> set = isSorted ? new LinkedHashSet<>(initialCapacity) : new HashSet<>(initialCapacity);
        Collections.addAll(set, ts);
        return set;
    }

    /**
     * 新建一个HashSet
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     *
     * @return HashSet对象
     */
    public static <T> HashSet<T> newHashSet(Collection<T> collection) {
        return newHashSet(false, collection);
    }

    /**
     * 新建一个HashSet
     *
     * @param <T>        集合元素类型
     * @param isSorted   是否有序，有序返回 {@link LinkedHashSet}，否则返回{@link HashSet}
     * @param collection 集合，用于初始化Set
     *
     * @return HashSet对象
     */
    public static <T> HashSet<T> newHashSet(boolean isSorted, Collection<T> collection) {
        return isSorted ? new LinkedHashSet<>(collection) : new HashSet<>(collection);
    }

    /**
     * 新建一个HashSet
     *
     * @param <T>      集合元素类型
     * @param isSorted 是否有序，有序返回 {@link LinkedHashSet}，否则返回{@link HashSet}
     * @param iter     {@link Iterator}
     *
     * @return HashSet对象
     *
     * @since 3.0.8
     */
    public static <T> HashSet<T> newHashSet(boolean isSorted, Iterator<T> iter) {
        if (null == iter) {
            return newHashSet(isSorted, (T[]) null);
        }
        final HashSet<T> set = isSorted ? new LinkedHashSet<>() : new HashSet<>();
        while (iter.hasNext()) {
            set.add(iter.next());
        }
        return set;
    }

    /**
     * 新建一个HashSet
     *
     * @param <T>         集合元素类型
     * @param isSorted    是否有序，有序返回 {@link LinkedHashSet}，否则返回{@link HashSet}
     * @param enumeration {@link Enumeration}
     *
     * @return HashSet对象
     *
     * @since 3.0.8
     */
    public static <T> HashSet<T> newHashSet(boolean isSorted, Enumeration<T> enumeration) {
        if (null == enumeration) {
            return newHashSet(isSorted, (T[]) null);
        }
        final HashSet<T> set = isSorted ? new LinkedHashSet<>() : new HashSet<>();
        while (enumeration.hasMoreElements()) {
            set.add(enumeration.nextElement());
        }
        return set;
    }

    // ----------------------------------------------------------------------------------------------- List

    /**
     * 新建一个空List
     *
     * @param <T>      集合元素类型
     * @param isLinked 是否新建LinkedList
     *
     * @return List对象
     *
     * @since 4.1.2
     */
    public static <T> List<T> list(boolean isLinked) {
        return isLinked ? new LinkedList<>() : new ArrayList<>();
    }

    /**
     * 新建一个List
     *
     * @param <T>      集合元素类型
     * @param isLinked 是否新建LinkedList
     * @param values   数组
     *
     * @return List对象
     *
     * @since 4.1.2
     */
    @SafeVarargs
    public static <T> List<T> list(boolean isLinked, T... values) {
        if (ArrayUtil.isEmpty(values)) {
            return list(isLinked);
        }
        final List<T> arrayList = isLinked ? new LinkedList<>() : new ArrayList<>(values.length);
        Collections.addAll(arrayList, values);
        return arrayList;
    }

    /**
     * 新建一个List
     *
     * @param <T>        集合元素类型
     * @param isLinked   是否新建LinkedList
     * @param collection 集合
     *
     * @return List对象
     *
     * @since 4.1.2
     */
    public static <T> List<T> list(boolean isLinked, Collection<T> collection) {
        if (null == collection) {
            return list(isLinked);
        }
        return isLinked ? new LinkedList<>(collection) : new ArrayList<>(collection);
    }

    /**
     * 新建一个List<br>
     * 提供的参数为null时返回空{@link ArrayList}
     *
     * @param <T>      集合元素类型
     * @param isLinked 是否新建LinkedList
     * @param iterable {@link Iterable}
     *
     * @return List对象
     *
     * @since 4.1.2
     */
    public static <T> List<T> list(boolean isLinked, Iterable<T> iterable) {
        if (null == iterable) {
            return list(isLinked);
        }
        return list(isLinked, iterable.iterator());
    }

    /**
     * 新建一个ArrayList<br>
     * 提供的参数为null时返回空{@link ArrayList}
     *
     * @param <T>      集合元素类型
     * @param isLinked 是否新建LinkedList
     * @param iter     {@link Iterator}
     *
     * @return ArrayList对象
     *
     * @since 4.1.2
     */
    public static <T> List<T> list(boolean isLinked, Iterator<T> iter) {
        final List<T> list = list(isLinked);
        if (null != iter) {
            while (iter.hasNext()) {
                list.add(iter.next());
            }
        }
        return list;
    }

    /**
     * 新建一个List<br>
     * 提供的参数为null时返回空{@link ArrayList}
     *
     * @param <T>        集合元素类型
     * @param isLinked   是否新建LinkedList
     * @param enumration {@link Enumeration}
     *
     * @return ArrayList对象
     *
     * @since 3.0.8
     */
    public static <T> List<T> list(boolean isLinked, Enumeration<T> enumration) {
        final List<T> list = list(isLinked);
        if (null != enumration) {
            while (enumration.hasMoreElements()) {
                list.add(enumration.nextElement());
            }
        }
        return list;
    }

    /**
     * 新建一个ArrayList
     *
     * @param <T>    集合元素类型
     * @param values 数组
     *
     * @return ArrayList对象
     */
    @SafeVarargs
    public static <T> ArrayList<T> newArrayList(T... values) {
        return (ArrayList<T>) list(false, values);
    }

    /**
     * 数组转为ArrayList
     *
     * @param <T>    集合元素类型
     * @param values 数组
     *
     * @return ArrayList对象
     *
     * @since 4.0.11
     */
    @SafeVarargs
    public static <T> ArrayList<T> toList(T... values) {
        return newArrayList(values);
    }

    /**
     * 新建一个ArrayList
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     *
     * @return ArrayList对象
     */
    public static <T> ArrayList<T> newArrayList(Collection<T> collection) {
        return (ArrayList<T>) list(false, collection);
    }

    /**
     * 新建一个ArrayList<br>
     * 提供的参数为null时返回空{@link ArrayList}
     *
     * @param <T>      集合元素类型
     * @param iterable {@link Iterable}
     *
     * @return ArrayList对象
     *
     * @since 3.1.0
     */
    public static <T> ArrayList<T> newArrayList(Iterable<T> iterable) {
        return (ArrayList<T>) list(false, iterable);
    }

    /**
     * 新建一个ArrayList<br>
     * 提供的参数为null时返回空{@link ArrayList}
     *
     * @param <T>  集合元素类型
     * @param iter {@link Iterator}
     *
     * @return ArrayList对象
     *
     * @since 3.0.8
     */
    public static <T> ArrayList<T> newArrayList(Iterator<T> iter) {
        return (ArrayList<T>) list(false, iter);
    }

    /**
     * 新建一个ArrayList<br>
     * 提供的参数为null时返回空{@link ArrayList}
     *
     * @param <T>        集合元素类型
     * @param enumration {@link Enumeration}
     *
     * @return ArrayList对象
     *
     * @since 3.0.8
     */
    public static <T> ArrayList<T> newArrayList(Enumeration<T> enumration) {
        return (ArrayList<T>) list(false, enumration);
    }

    // ----------------------------------------------------------------------new LinkedList

    /**
     * 新建LinkedList
     *
     * @param values 数组
     * @param <T>    类型
     *
     * @return LinkedList
     *
     * @since 4.1.2
     */
    @SafeVarargs
    public static <T> LinkedList<T> newLinkedList(T... values) {
        return (LinkedList<T>) list(true, values);
    }

    /**
     * 新建一个CopyOnWriteArrayList
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     *
     * @return {@link CopyOnWriteArrayList}
     */
    public static <T> CopyOnWriteArrayList<T> newCopyOnWriteArrayList(Collection<T> collection) {
        return (null == collection) ? (new CopyOnWriteArrayList<>()) : (new CopyOnWriteArrayList<>(collection));
    }

    /**
     * 新建{@link BlockingQueue}<br>
     * 在队列为空时，获取元素的线程会等待队列变为非空。当队列满时，存储元素的线程会等待队列可用。
     *
     * @param <T>      集合类型
     * @param capacity 容量
     * @param isLinked 是否为链表形式
     *
     * @return {@link BlockingQueue}
     *
     * @since 3.3.0
     */
    public static <T> BlockingQueue<T> newBlockingQueue(int capacity, boolean isLinked) {
        BlockingQueue<T> queue;
        if (isLinked) {
            queue = new LinkedBlockingDeque<>(capacity);
        } else {
            queue = new ArrayBlockingQueue<>(capacity);
        }
        return queue;
    }


    /**
     * 去重集合
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     *
     * @return {@link ArrayList}
     */
    public static <T> ArrayList<T> distinct(Collection<T> collection) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        } else if (collection instanceof Set) {
            return new ArrayList<>(collection);
        } else {
            return new ArrayList<>(new LinkedHashSet<>(collection));
        }
    }

    /**
     * 截取集合的部分
     *
     * @param <T>   集合元素类型
     * @param list  被截取的数组
     * @param start 开始位置（包含）
     * @param end   结束位置（不包含）
     *
     * @return 截取后的数组，当开始位置超过最大时，返回空的List
     */
    public static <T> List<T> sub(List<T> list, int start, int end) {
        return sub(list, start, end, 1);
    }

    /**
     * 截取集合的部分
     *
     * @param <T>   集合元素类型
     * @param list  被截取的数组
     * @param start 开始位置（包含）
     * @param end   结束位置（不包含）
     * @param step  步进
     *
     * @return 截取后的数组，当开始位置超过最大时，返回空的List
     *
     * @since 4.0.6
     */
    public static <T> List<T> sub(List<T> list, int start, int end, int step) {
        if (list == null) {
            return null;
        }

        if (list.isEmpty()) {
            return new ArrayList<>(0);
        }

        final int size = list.size();
        if (start < 0) {
            start += size;
        }
        if (end < 0) {
            end += size;
        }
        if (start == size) {
            return new ArrayList<>(0);
        }
        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
        }
        if (end > size) {
            if (start >= size) {
                return new ArrayList<>(0);
            }
            end = size;
        }

        if (step <= 1) {
            return list.subList(start, end);
        }

        final List<T> result = new ArrayList<>();
        for (int i = start; i < end; i += step) {
            result.add(list.get(i));
        }
        return result;
    }

    /**
     * 截取集合的部分
     *
     * @param <T>        集合元素类型
     * @param collection 被截取的数组
     * @param start      开始位置（包含）
     * @param end        结束位置（不包含）
     *
     * @return 截取后的数组，当开始位置超过最大时，返回null
     */
    public static <T> List<T> sub(Collection<T> collection, int start, int end) {
        return sub(collection, start, end, 1);
    }

    /**
     * 截取集合的部分
     *
     * @param <T>   集合元素类型
     * @param list  被截取的数组
     * @param start 开始位置（包含）
     * @param end   结束位置（不包含）
     * @param step  步进
     *
     * @return 截取后的数组，当开始位置超过最大时，返回空集合
     *
     * @since 4.0.6
     */
    public static <T> List<T> sub(Collection<T> list, int start, int end, int step) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        return sub(new ArrayList<>(list), start, end, step);
    }

    /**
     * 对集合按照指定长度分段，每一个段为单独的集合，返回这个集合的列表
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @param size       每个段的长度
     *
     * @return 分段列表
     */
    public static <T> List<List<T>> split(Collection<T> collection, int size) {
        final List<List<T>> result = new ArrayList<>();

        ArrayList<T> subList = new ArrayList<>(size);
        for (T t : collection) {
            if (subList.size() >= size) {
                result.add(subList);
                subList = new ArrayList<>(size);
            }
            subList.add(t);
        }
        result.add(subList);
        return result;
    }


    /**
     * 过滤<br>
     * 过滤过程通过传入的Editor实现来返回需要的元素内容，这个Editor实现可以实现以下功能：
     *
     * <pre>
     * 1、过滤出需要的对象，如果返回null表示这个元素对象抛弃
     * 2、修改元素对象，返回集合中为修改后的对象
     * </pre>
     *
     * @param <T>    集合元素类型
     * @param list   集合
     * @param editor 编辑器接口
     *
     * @return 过滤后的数组
     *
     * @since 4.1.8
     */
    public static <T> List<T> filter(List<T> list, Editor<T> editor) {
        if (null == list || null == editor) {
            return list;
        }

        final List<T> list2 = (list instanceof LinkedList) ? new LinkedList<>() : new ArrayList<>(list.size());
        T modified;
        for (T t : list) {
            modified = editor.edit(t);
            if (null != modified) {
                list2.add(modified);
            }
        }
        return list2;
    }

    /**
     * 去掉集合中的多个元素，此方法直接修改原集合
     *
     * @param <T>         集合类型
     * @param <E>         集合元素类型
     * @param collection  集合
     * @param elesRemoved 被去掉的元素数组
     *
     * @return 原集合
     *
     * @since 4.1.0
     */
    @SuppressWarnings("unchecked")
    public static <T extends Collection<E>, E> T removeAny(T collection, E... elesRemoved) {
        collection.removeAll(newHashSet(elesRemoved));
        return collection;
    }


    /**
     * 通过Editor抽取集合元素中的某些值返回为新列表<br>
     * 例如提供的是一个Bean列表，通过Editor接口实现获取某个字段值，返回这个字段值组成的新列表
     *
     * @param collection 原集合
     * @param editor     编辑器
     *
     * @return 抽取后的新列表
     */
    public static List<Object> extract(Iterable<?> collection, Editor<Object> editor) {
        return extract(collection, editor, false);
    }

    /**
     * 通过Editor抽取集合元素中的某些值返回为新列表<br>
     * 例如提供的是一个Bean列表，通过Editor接口实现获取某个字段值，返回这个字段值组成的新列表
     *
     * @param collection 原集合
     * @param editor     编辑器
     * @param ignoreNull 是否忽略空值
     *
     * @return 抽取后的新列表
     *
     * @since 4.5.7
     */
    public static List<Object> extract(Iterable<?> collection, Editor<Object> editor, boolean ignoreNull) {
        final List<Object> fieldValueList = new ArrayList<>();
        if (null == collection) {
            return fieldValueList;
        }

        Object value;
        for (Object bean : collection) {
            value = editor.edit(bean);
            if (null == value && ignoreNull) {
                continue;
            }
            fieldValueList.add(value);
        }
        return fieldValueList;
    }


    // ---------------------------------------------------------------------- isEmpty

    /**
     * 集合是否为空
     *
     * @param collection 集合
     *
     * @return 是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 如果给定集合为空，返回默认集合
     *
     * @param <T>               集合类型
     * @param <E>               集合元素类型
     * @param collection        集合
     * @param defaultCollection 默认数组
     *
     * @return 非空（empty）的原集合或默认集合
     *
     * @since 4.6.9
     */
    public static <T extends Collection<E>, E> T defaultIfEmpty(T collection, T defaultCollection) {
        return isEmpty(collection) ? defaultCollection : collection;
    }

    /**
     * Map是否为空
     *
     * @param map 集合
     *
     * @return 是否为空
     *
     * @see MapUtil#isEmpty(Map)
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return MapUtil.isEmpty(map);
    }


    /**
     * Enumeration是否为空
     *
     * @param enumeration {@link Enumeration}
     *
     * @return 是否为空
     */
    public static boolean isEmpty(Enumeration<?> enumeration) {
        return null == enumeration || !enumeration.hasMoreElements();
    }

    // ---------------------------------------------------------------------- isNotEmpty

    /**
     * 集合是否为非空
     *
     * @param collection 集合
     *
     * @return 是否为非空
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * Map是否为非空
     *
     * @param map 集合
     *
     * @return 是否为非空
     *
     * @see MapUtil#isNotEmpty(Map)
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return MapUtil.isNotEmpty(map);
    }

    /**
     * Enumeration是否为空
     *
     * @param enumeration {@link Enumeration}
     *
     * @return 是否为空
     */
    public static boolean isNotEmpty(Enumeration<?> enumeration) {
        return null != enumeration && enumeration.hasMoreElements();
    }


    // ---------------------------------------------------------------------- zip


    /**
     * 映射键值（参考Python的zip()函数）<br>
     * 例如：<br>
     * keys = [a,b,c,d]<br>
     * values = [1,2,3,4]<br>
     * 则得到的Map是 {a=1, b=2, c=3, d=4}<br>
     * 如果两个数组长度不同，则只对应最短部分
     *
     * @param <K>    键类型
     * @param <V>    值类型
     * @param keys   键列表
     * @param values 值列表
     *
     * @return Map
     */
    public static <K, V> Map<K, V> zip(Collection<K> keys, Collection<V> values) {
        if (isEmpty(keys) || isEmpty(values)) {
            return null;
        }

        int entryCount = Math.min(keys.size(), values.size());
        final Map<K, V> map = newHashMap(entryCount);

        final Iterator<K> keyIterator = keys.iterator();
        final Iterator<V> valueIterator = values.iterator();
        while (entryCount > 0) {
            map.put(keyIterator.next(), valueIterator.next());
            entryCount--;
        }

        return map;
    }


    /**
     * 将数组转换为Map（HashMap），支持数组元素类型为：
     *
     * <pre>
     * Map.Entry
     * 长度大于1的数组（取前两个值），如果不满足跳过此元素
     * Iterable 长度也必须大于1（取前两个值），如果不满足跳过此元素
     * Iterator 长度也必须大于1（取前两个值），如果不满足跳过此元素
     * </pre>
     *
     * <pre>
     * Map&lt;Object, Object&gt; colorMap = CollectionUtil.toMap(new String[][] {{
     *     {"RED", "#FF0000"},
     *     {"GREEN", "#00FF00"},
     *     {"BLUE", "#0000FF"}});
     * </pre>
     * <p>
     * 参考：commons-lang
     *
     * @param array 数组。元素类型为Map.Entry、数组、Iterable、Iterator
     *
     * @return {@link HashMap}
     *
     * @see MapUtil#of(Object[])
     * @since 3.0.8
     */
    public static HashMap<Object, Object> toMap(Object[] array) {
        return MapUtil.of(array);
    }

    /**
     * 将集合转换为排序后的TreeSet
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @param comparator 比较器
     *
     * @return treeSet
     */
    public static <T> TreeSet<T> toTreeSet(Collection<T> collection, Comparator<T> comparator) {
        final TreeSet<T> treeSet = new TreeSet<>(comparator);
        treeSet.addAll(collection);
        return treeSet;
    }


    /**
     * {@link Iterable}转为{@link Collection}<br>
     * 首先尝试强转，强转失败则构建一个新的{@link ArrayList}
     *
     * @param <E>      集合元素类型
     * @param iterable {@link Iterable}
     *
     * @return {@link Collection} 或者 {@link ArrayList}
     *
     * @since 3.0.9
     */
    public static <E> Collection<E> toCollection(Iterable<E> iterable) {
        return (iterable instanceof Collection) ? (Collection<E>) iterable : newArrayList(iterable.iterator());
    }

    /**
     * 行转列，合并相同的键，值合并为列表<br>
     * 将Map列表中相同key的值组成列表做为Map的value<br>
     * 是{@link #toMapList(Map)}的逆方法<br>
     * 比如传入数据：
     *
     * <pre>
     * [
     *  {a: 1, b: 1, c: 1}
     *  {a: 2, b: 2}
     *  {a: 3, b: 3}
     *  {a: 4}
     * ]
     * </pre>
     * <p>
     * 结果是：
     *
     * <pre>
     * {
     *   a: [1,2,3,4]
     *   b: [1,2,3,]
     *   c: [1]
     * }
     * </pre>
     *
     * @param <K>     键类型
     * @param <V>     值类型
     * @param mapList Map列表
     *
     * @return Map
     *
     * @see MapUtil#toListMap(Iterable)
     */
    public static <K, V> Map<K, List<V>> toListMap(Iterable<? extends Map<K, V>> mapList) {
        return MapUtil.toListMap(mapList);
    }

    /**
     * 列转行。将Map中值列表分别按照其位置与key组成新的map。<br>
     * 是{@link #toListMap(Iterable)}的逆方法<br>
     * 比如传入数据：
     *
     * <pre>
     * {
     *   a: [1,2,3,4]
     *   b: [1,2,3,]
     *   c: [1]
     * }
     * </pre>
     * <p>
     * 结果是：
     *
     * <pre>
     * [
     *  {a: 1, b: 1, c: 1}
     *  {a: 2, b: 2}
     *  {a: 3, b: 3}
     *  {a: 4}
     * ]
     * </pre>
     *
     * @param <K>     键类型
     * @param <V>     值类型
     * @param listMap 列表Map
     *
     * @return Map列表
     *
     * @see MapUtil#toMapList(Map)
     */
    public static <K, V> List<Map<K, V>> toMapList(Map<K, ? extends Iterable<V>> listMap) {
        return MapUtil.toMapList(listMap);
    }


    /**
     * 加入全部
     *
     * @param <T>        集合元素类型
     * @param collection 被加入的集合 {@link Collection}
     * @param iterator   要加入的{@link Iterator}
     *
     * @return 原集合
     */
    public static <T> Collection<T> addAll(Collection<T> collection, Iterator<T> iterator) {
        if (null != collection && null != iterator) {
            while (iterator.hasNext()) {
                collection.add(iterator.next());
            }
        }
        return collection;
    }

    /**
     * 加入全部
     *
     * @param <T>        集合元素类型
     * @param collection 被加入的集合 {@link Collection}
     * @param iterable   要加入的内容{@link Iterable}
     *
     * @return 原集合
     */
    public static <T> Collection<T> addAll(Collection<T> collection, Iterable<T> iterable) {
        return addAll(collection, iterable.iterator());
    }

    /**
     * 加入全部
     *
     * @param <T>         集合元素类型
     * @param collection  被加入的集合 {@link Collection}
     * @param enumeration 要加入的内容{@link Enumeration}
     *
     * @return 原集合
     */
    public static <T> Collection<T> addAll(Collection<T> collection, Enumeration<T> enumeration) {
        if (null != collection && null != enumeration) {
            while (enumeration.hasMoreElements()) {
                collection.add(enumeration.nextElement());
            }
        }
        return collection;
    }

    /**
     * 加入全部
     *
     * @param <T>        集合元素类型
     * @param collection 被加入的集合 {@link Collection}
     * @param values     要加入的内容数组
     *
     * @return 原集合
     *
     * @since 3.0.8
     */
    public static <T> Collection<T> addAll(Collection<T> collection, T[] values) {
        if (null != collection && null != values) {
            Collections.addAll(collection, values);
        }
        return collection;
    }

    /**
     * 将另一个列表中的元素加入到列表中，如果列表中已经存在此元素则忽略之
     *
     * @param <T>       集合元素类型
     * @param list      列表
     * @param otherList 其它列表
     *
     * @return 此列表
     */
    public static <T> List<T> addAllIfNotContains(List<T> list, List<T> otherList) {
        for (T t : otherList) {
            if (!list.contains(t)) {
                list.add(t);
            }
        }
        return list;
    }

    /**
     * 获取集合中指定下标的元素值，下标可以为负数，例如-1表示最后一个元素<br>
     * 如果元素越界，返回null
     *
     * @param <T>        元素类型
     * @param collection 集合
     * @param index      下标，支持负数
     *
     * @return 元素值
     *
     * @since 4.0.6
     */
    public static <T> T get(Collection<T> collection, int index) {
        if (null == collection) {
            return null;
        }

        final int size = collection.size();
        if (0 == size) {
            return null;
        }

        if (index < 0) {
            index += size;
        }

        // 检查越界
        if (index >= size) {
            return null;
        }

        if (collection instanceof List) {
            final List<T> list = ((List<T>) collection);
            return list.get(index);
        } else {
            int i = 0;
            for (T t : collection) {
                if (i > index) {
                    break;
                } else if (i == index) {
                    return t;
                }
                i++;
            }
        }
        return null;
    }

    /**
     * 获取集合中指定多个下标的元素值，下标可以为负数，例如-1表示最后一个元素
     *
     * @param <T>        元素类型
     * @param collection 集合
     * @param indexes    下标，支持负数
     *
     * @return 元素值列表
     *
     * @since 4.0.6
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> getAny(Collection<T> collection, int... indexes) {
        final int size = collection.size();
        final ArrayList<T> result = new ArrayList<>();
        if (collection instanceof List) {
            final List<T> list = ((List<T>) collection);
            for (int index : indexes) {
                if (index < 0) {
                    index += size;
                }
                result.add(list.get(index));
            }
        } else {
            final Object[] array = collection.toArray();
            for (int index : indexes) {
                if (index < 0) {
                    index += size;
                }
                result.add((T) array[index]);
            }
        }
        return result;
    }


    /**
     * 获取集合的最后一个元素
     *
     * @param <T>        集合元素类型
     * @param collection {@link Collection}
     *
     * @return 最后一个元素
     *
     * @since 4.1.10
     */
    public static <T> T getLast(Collection<T> collection) {
        return get(collection, -1);
    }

    /**
     * 从Map中获取指定键列表对应的值列表<br>
     * 如果key在map中不存在或key对应值为null，则返回值列表对应位置的值也为null
     *
     * @param <K>  键类型
     * @param <V>  值类型
     * @param map  {@link Map}
     * @param keys 键列表
     *
     * @return 值列表
     *
     * @since 3.0.8
     */
    @SuppressWarnings("unchecked")
    public static <K, V> ArrayList<V> valuesOfKeys(Map<K, V> map, K... keys) {
        final ArrayList<V> values = new ArrayList<>();
        for (K k : keys) {
            values.add(map.get(k));
        }
        return values;
    }

    /**
     * 从Map中获取指定键列表对应的值列表<br>
     * 如果key在map中不存在或key对应值为null，则返回值列表对应位置的值也为null
     *
     * @param <K>  键类型
     * @param <V>  值类型
     * @param map  {@link Map}
     * @param keys 键列表
     *
     * @return 值列表
     *
     * @since 3.0.9
     */
    public static <K, V> ArrayList<V> valuesOfKeys(Map<K, V> map, Iterable<K> keys) {
        return valuesOfKeys(map, keys.iterator());
    }

    /**
     * 从Map中获取指定键列表对应的值列表<br>
     * 如果key在map中不存在或key对应值为null，则返回值列表对应位置的值也为null
     *
     * @param <K>  键类型
     * @param <V>  值类型
     * @param map  {@link Map}
     * @param keys 键列表
     *
     * @return 值列表
     *
     * @since 3.0.9
     */
    public static <K, V> ArrayList<V> valuesOfKeys(Map<K, V> map, Iterator<K> keys) {
        final ArrayList<V> values = new ArrayList<>();
        while (keys.hasNext()) {
            values.add(map.get(keys.next()));
        }
        return values;
    }

    // ------------------------------------------------------------------------------------------------- sort

    /**
     * 对指定List分页取值
     *
     * @param <T>      集合元素类型
     * @param pageNo   页码，从1开始计数，0和1效果相同
     * @param pageSize 每页的条目数
     * @param list     列表
     *
     * @return 分页后的段落内容
     *
     * @since 4.1.20
     */
    public static <T> List<T> page(int pageNo, int pageSize, List<T> list) {
        if (isEmpty(list)) {
            return new ArrayList<>(0);
        }

        int resultSize = list.size();
        // 每页条目数大于总数直接返回所有
        if (resultSize <= pageSize) {
            if (pageNo <= 1) {
                return Collections.unmodifiableList(list);
            } else {
                // 越界直接返回空
                return new ArrayList<>(0);
            }
        }
        final int[] startEnd = PageUtil.transToStartEnd(pageNo, pageSize);
        if (startEnd[1] > resultSize) {
            startEnd[1] = resultSize;
        }
        return list.subList(startEnd[0], startEnd[1]);
    }

    /**
     * 排序集合，排序不会修改原集合
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @param comparator 比较器
     *
     * @return treeSet
     */
    public static <T> List<T> sort(Collection<T> collection, Comparator<? super T> comparator) {
        List<T> list = new ArrayList<>(collection);
        Collections.sort(list, comparator);
        return list;
    }

    /**
     * 针对List排序，排序会修改原List
     *
     * @param <T>  元素类型
     * @param list 被排序的List
     * @param c    {@link Comparator}
     *
     * @return 原list
     *
     * @see Collections#sort(List, Comparator)
     */
    public static <T> List<T> sort(List<T> list, Comparator<? super T> c) {
        Collections.sort(list, c);
        return list;
    }


    /**
     * 根据汉字的拼音顺序排序
     *
     * @param collection 集合，会被转换为List
     *
     * @return 排序后的List
     *
     * @since 4.0.8
     */
    public static List<String> sortByPinyin(Collection<String> collection) {
        return sort(collection, new PinyinComparator());
    }

    /**
     * 根据汉字的拼音顺序排序
     *
     * @param list List
     *
     * @return 排序后的List
     *
     * @since 4.0.8
     */
    public static List<String> sortByPinyin(List<String> list) {
        return sort(list, new PinyinComparator());
    }

    /**
     * 排序Map
     *
     * @param <K>        键类型
     * @param <V>        值类型
     * @param map        Map
     * @param comparator Entry比较器
     *
     * @return {@link TreeMap}
     *
     * @since 3.0.9
     */
    public static <K, V> TreeMap<K, V> sort(Map<K, V> map, Comparator<? super K> comparator) {
        final TreeMap<K, V> result = new TreeMap<>(comparator);
        result.putAll(map);
        return result;
    }

    /**
     * 通过Entry排序，可以按照键排序，也可以按照值排序，亦或者两者综合排序
     *
     * @param <K>             键类型
     * @param <V>             值类型
     * @param entryCollection Entry集合
     * @param comparator      {@link Comparator}
     *
     * @return {@link LinkedList}
     *
     * @since 3.0.9
     */
    public static <K, V> LinkedHashMap<K, V> sortToMap(Collection<Entry<K, V>> entryCollection,
            Comparator<Entry<K, V>> comparator) {
        List<Entry<K, V>> list = new LinkedList<>(entryCollection);
        Collections.sort(list, comparator);

        LinkedHashMap<K, V> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * 通过Entry排序，可以按照键排序，也可以按照值排序，亦或者两者综合排序
     *
     * @param <K>        键类型
     * @param <V>        值类型
     * @param map        被排序的Map
     * @param comparator {@link Comparator}
     *
     * @return {@link LinkedList}
     *
     * @since 3.0.9
     */
    public static <K, V> LinkedHashMap<K, V> sortByEntry(Map<K, V> map, Comparator<Entry<K, V>> comparator) {
        return sortToMap(map.entrySet(), comparator);
    }

    /**
     * 将Set排序（根据Entry的值）
     *
     * @param <K>        键类型
     * @param <V>        值类型
     * @param collection 被排序的{@link Collection}
     *
     * @return 排序后的Set
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <K, V> List<Entry<K, V>> sortEntryToList(Collection<Entry<K, V>> collection) {
        List<Entry<K, V>> list = new LinkedList<>(collection);
        Collections.sort(list, (o1, o2) -> {
            V v1 = o1.getValue();
            V v2 = o2.getValue();

            if (v1 instanceof Comparable) {
                return ((Comparable) v1).compareTo(v2);
            } else {
                return v1.toString().compareTo(v2.toString());
            }
        });
        return list;
    }

    // ------------------------------------------------------------------------------------------------- forEach

    /**
     * 循环遍历 {@link Iterator}，使用{@link Consumer} 接受遍历的每条数据，并针对每条数据做处理
     *
     * @param <T>      集合元素类型
     * @param iterator {@link Iterator}
     * @param consumer {@link Consumer} 遍历的每条数据处理器
     */
    public static <T> void forEach(Iterator<T> iterator, Consumer<T> consumer) {
        int index = 0;
        while (iterator.hasNext()) {
            consumer.accept(iterator.next(), index);
            index++;
        }
    }

    /**
     * 循环遍历 {@link Enumeration}，使用{@link Consumer} 接受遍历的每条数据，并针对每条数据做处理
     *
     * @param <T>         集合元素类型
     * @param enumeration {@link Enumeration}
     * @param consumer    {@link Consumer} 遍历的每条数据处理器
     */
    public static <T> void forEach(Enumeration<T> enumeration, Consumer<T> consumer) {
        int index = 0;
        while (enumeration.hasMoreElements()) {
            consumer.accept(enumeration.nextElement(), index);
            index++;
        }
    }

    /**
     * 循环遍历Map，使用{@link KVConsumer} 接受遍历的每条数据，并针对每条数据做处理
     *
     * @param <K>        Key类型
     * @param <V>        Value类型
     * @param map        {@link Map}
     * @param kvConsumer {@link KVConsumer} 遍历的每条数据处理器
     */
    public static <K, V> void forEach(Map<K, V> map, KVConsumer<K, V> kvConsumer) {
        int index = 0;
        for (Entry<K, V> entry : map.entrySet()) {
            kvConsumer.accept(entry.getKey(), entry.getValue(), index);
            index++;
        }
    }

    /**
     * 分组，按照{@link Hash}接口定义的hash算法，集合中的元素放入hash值对应的子列表中
     *
     * @param <T>        元素类型
     * @param collection 被分组的集合
     * @param hash       Hash值算法，决定元素放在第几个分组的规则
     *
     * @return 分组后的集合
     */
    public static <T> List<List<T>> group(Collection<T> collection, Hash<T> hash) {
        final List<List<T>> result = new ArrayList<>();
        if (isEmpty(collection)) {
            return result;
        }
        if (null == hash) {
            // 默认hash算法，按照元素的hashCode分组
            hash = t -> (null == t) ? 0 : t.hashCode();
        }

        int index;
        List<T> subList;
        for (T t : collection) {
            index = hash.hash(t);
            if (result.size() - 1 < index) {
                while (result.size() - 1 < index) {
                    result.add(null);
                }
                result.set(index, newArrayList(t));
            } else {
                subList = result.get(index);
                if (null == subList) {
                    result.set(index, newArrayList(t));
                } else {
                    subList.add(t);
                }
            }
        }
        return result;
    }


    /**
     * 反序给定List，会在原List基础上直接修改
     *
     * @param <T>  元素类型
     * @param list 被反转的List
     *
     * @return 反转后的List
     *
     * @since 4.0.6
     */
    public static <T> List<T> reverse(List<T> list) {
        Collections.reverse(list);
        return list;
    }

    /**
     * 设置或增加元素。当index小于List的长度时，替换指定位置的值，否则在尾部追加
     *
     * @param <T>     元素类型
     * @param list    List列表
     * @param index   位置
     * @param element 新元素
     *
     * @return 原List
     *
     * @since 4.1.2
     */
    public static <T> List<T> setOrAppend(List<T> list, int index, T element) {
        if (index < list.size()) {
            list.set(index, element);
        } else {
            list.add(element);
        }
        return list;
    }

    /**
     * 获取指定Map列表中所有的Key
     *
     * @param <K>           键类型
     * @param mapCollection Map列表
     *
     * @return key集合
     *
     * @since 4.5.12
     */
    public static <K> Set<K> keySet(Collection<Map<K, ?>> mapCollection) {
        if (isEmpty(mapCollection)) {
            return new HashSet<>();
        }
        final HashSet<K> set = new HashSet<>(mapCollection.size() * 16);
        for (Map<K, ?> map : mapCollection) {
            set.addAll(map.keySet());
        }

        return set;
    }

    /**
     * 获取指定Map列表中所有的Value
     *
     * @param <V>           值类型
     * @param mapCollection Map列表
     *
     * @return Value集合
     *
     * @since 4.5.12
     */
    public static <V> List<V> values(Collection<Map<?, V>> mapCollection) {
        final List<V> values = new ArrayList<>();
        for (Map<?, V> map : mapCollection) {
            values.addAll(map.values());
        }

        return values;
    }

    /**
     * 取最大值
     *
     * @param <T>  元素类型
     * @param coll 集合
     *
     * @return 最大值
     *
     * @see Collections#max(Collection)
     * @since 4.6.5
     */
    public static <T extends Comparable<? super T>> T max(Collection<T> coll) {
        return Collections.max(coll);
    }

    /**
     * 取最大值
     *
     * @param <T>  元素类型
     * @param coll 集合
     *
     * @return 最大值
     *
     * @see Collections#min(Collection)
     * @since 4.6.5
     */
    public static <T extends Comparable<? super T>> T min(Collection<T> coll) {
        return Collections.min(coll);
    }

    // ---------------------------------------------------------------------------------------------- Interface start

    /**
     * 针对一个参数做相应的操作
     *
     * @param <T> 处理参数类型
     *
     * @author Looly
     */
    public interface Consumer<T> {
        /**
         * 接受并处理一个参数
         *
         * @param value 参数值
         * @param index 参数在集合中的索引
         */
        void accept(T value, int index);
    }

    /**
     * 针对两个参数做相应的操作，例如Map中的KEY和VALUE
     *
     * @param <K> KEY类型
     * @param <V> VALUE类型
     *
     * @author Looly
     */
    public interface KVConsumer<K, V> {
        /**
         * 接受并处理一对参数
         *
         * @param key   键
         * @param value 值
         * @param index 参数在集合中的索引
         */
        void accept(K key, V value, int index);
    }

    /**
     * Hash计算接口
     *
     * @param <T> 被计算hash的对象类型
     *
     * @author looly
     * @since 3.2.2
     */
    public interface Hash<T> {
        /**
         * 计算Hash值
         *
         * @param t 对象
         *
         * @return hash
         */
        int hash(T t);
    }
    // ---------------------------------------------------------------------------------------------- Interface end


}
