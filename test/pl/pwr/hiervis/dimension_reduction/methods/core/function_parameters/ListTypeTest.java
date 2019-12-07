package pl.pwr.hiervis.dimension_reduction.methods.core.function_parameters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import pl.pwr.hiervis.dimension_reduction.distance_measures.DistanceMeasure;
import pl.pwr.hiervis.dimension_reduction.distance_measures.Euclidean;
import pl.pwr.hiervis.dimension_reduction.distance_measures.Manhattan;
import pl.pwr.hiervis.dimension_reduction.distance_measures.Minkowski;

public class ListTypeTest {
	List<DistanceMeasure> list;
	ListType<DistanceMeasure> l;

	@Before
	public void init() {
		list = new ArrayList<>();
		list.add(new Euclidean());
		list.add(new Manhattan());
		list.add(new Minkowski());
		l = new ListType<>(list);
	}

	@Test
	public void testListTypeListOfTMapOfTString() {
		Map<DistanceMeasure, String[]> enables = new HashMap<>();
		enables.put(new Minkowski(), new String[] { "Minkowski p value" });

		ListType<DistanceMeasure> l2 = new ListType<>(list, enables);
		assertNotNull(l2);
	}

	@Test
	public void testGetValues() {
		assertNotNull(l.getValues());
		assertEquals(list, l.getValues());
	}

	@Test
	public void testGetEnableParameterNames() {
		assertEquals(null, l.getEnableParameterNames());
	}

}
