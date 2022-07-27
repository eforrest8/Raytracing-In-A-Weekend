package io.github.eforrest8.rt;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Graph<T> {
	private Map<T, T[]> connections = new HashMap<>();
	
	// Only objects of type T are ever assigned to anything,
	// so SafeVarargs should be appropriate here.
	@SafeVarargs
	public final void addConnection(T key, T... values) {
		connections.merge(key, values, this::mergeArrays);
	}
	
	// Result is of the appropriate type before filtering,
	// so it should be impossible for the cast to fail.
	@SuppressWarnings("unchecked")
	private T[] mergeArrays(T[] current, T[] newValues) {
		T[] result = Arrays.copyOf(current, current.length + newValues.length);
		for (int i = 0; i < newValues.length; i++) {
			boolean matched = false;
			for (int j = 0; j < result.length; j++) {
				matched = matched ? true : result[j] == newValues[i];
			}
			if (!matched) {
				result[current.length + i] = newValues[i];
			}
		}
		return (T[]) Arrays.stream(result).filter(Objects::nonNull).toArray();
	}
	
	public T[] getConnections(T key) {
		return connections.get(key);
	}
}
