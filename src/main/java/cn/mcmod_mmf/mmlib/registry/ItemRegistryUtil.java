package cn.mcmod_mmf.mmlib.registry;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;

import cn.mcmod_mmf.mmlib.item.info.FoodInfo;

public class ItemRegistryUtil {
    public static <V> Map<String, V> registerAllItemInList(List<String> list, Function<String, V> valueMapper) {
        return registerAllItemInList(list, key -> true, valueMapper);
    }

    public static <V> Map<String, V> registerAllItemInList(List<String> list, Predicate<String> keyPredicate,
            Function<String, V> valueMapper) {
        return list.stream().filter(keyPredicate)
                .collect(Collectors.toMap(Function.identity(), valueMapper, (v, v2) -> v, Maps::newHashMap));
    }

    public static <V> Map<FoodInfo, V> registerAllFoodInList(List<FoodInfo> list, Function<FoodInfo, V> valueMapper) {
        return registerAllFoodInList(list, key -> true, valueMapper);
    }

    public static <V> Map<FoodInfo, V> registerAllFoodInList(List<FoodInfo> list, Predicate<FoodInfo> keyPredicate,
            Function<FoodInfo, V> valueMapper) {
        return list.stream().filter(keyPredicate)
                .collect(Collectors.toMap(Function.identity(), valueMapper, (v, v2) -> v, Maps::newHashMap));
    }

}