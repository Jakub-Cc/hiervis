package pl.pwr.hiervis.measures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import basic_hierarchy.interfaces.Hierarchy;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

/**
 * Factory of {@link MeasureTask} objects created from Javascript files.
 * 
 * @author Tomasz Bachmiński
 *
 */
public class JavascriptMeasureTaskFactory implements MeasureTaskFactory {
	private static final Logger log = LogManager.getLogger(JavascriptMeasureTaskFactory.class);

	private ScriptEngine engine = null;

	/**
	 * 
	 * @param restrictedAccess whether scripts eval'd by this factory should have
	 *                         restricted access to classes. If true, scripts will
	 *                         only be able to access classes from the following
	 *                         packages:
	 *                         <li>internal_measures</li>
	 *                         <li>external_measures</li>
	 *                         <li>distance_measures</li>
	 */
	public JavascriptMeasureTaskFactory(boolean restrictedAccess) {
		NashornScriptEngineFactory factory = new NashornScriptEngineFactory();

		if (restrictedAccess) {
			// Apply a class filter for some semblance of security.
			engine = factory.getScriptEngine(JavascriptMeasureTaskFactory::isClassAccessibleFromScript);
		} else {
			engine = factory.getScriptEngine();
		}
		File jarDir = new File("measure-jars");
		try (Stream<Path> files = Files.list(jarDir.toPath())) {
			files.forEach(p -> {
				if (p.endsWith(".jar")) {
					loadJar(p.toFile());
				}
			});
		} catch (IOException e) {
			log.error(e);
		}
	}

	/**
	 * Checks whether the {@link MeasureTask} script files are allowed to load the
	 * specified class.
	 * 
	 * @param classPath fully qualified name of the class to check
	 * @return whether the specified class can be loaded by the script
	 */
	private static boolean isClassAccessibleFromScript(String classPath) {
		return (classPath.startsWith("internal_measures.") || classPath.startsWith("external_measures.")
				|| classPath.startsWith("distance_measures."));
	}

	@Override
	public MeasureTask getMeasureTask(Path path) {
		if (path.toFile().isDirectory()) {
			throw new IllegalArgumentException("Path passed in argument must denote a single file!");
		}
		return evalFile(engine, path);
	}

	@Override
	public Collection<MeasureTask> getMeasureTasks(Path path) {
		if (path.toFile().isDirectory()) {
			List<MeasureTask> result = new ArrayList<>();
			evalDirRecursive(engine, path, result);
			return result;
		} else {
			throw new UnsupportedOperationException();
		}
	}

	private static void evalDirRecursive(ScriptEngine engine, Path path, Collection<MeasureTask> results) {
		if (path.toFile().isDirectory()) {
			try (Stream<Path> files = Files.list(path)) {
				files.forEach(p -> evalDirRecursive(engine, p, results));
			} catch (IOException e) {
				log.error("Error while recursively eval'ing script directories:\n", e);
			}
		} else {
			results.add(evalFile(engine, path));
		}
	}

	private static MeasureTask evalFile(ScriptEngine engine, Path path) {
		Invocable inv = (Invocable) engine;
		try {
			JSObject scriptCallback = (JSObject) engine.eval(new FileReader(path.toAbsolutePath().toFile()),
					engine.createBindings());
			return constructMeasureTask(path, inv, scriptCallback);
		} catch (FileNotFoundException e) {
			log.error("Could not find MeasureTask script file: {}", path);
		} catch (ScriptException e) {
			log.error("Error while parsing MeasureTask script file: " + path.toString() + "\n", e);
		}
		return null;
	}

	private static MeasureTask constructMeasureTask(Path path, Invocable inv, JSObject scriptCallback) {
		try {
			if (!scriptCallback.isFunction()) {
				throw new IllegalArgumentException("Return value of script is not a Function!");
			}

			JSObject measureData = (JSObject) scriptCallback.call(null);
			Object measure = getOptionalMember(measureData, "measure", null);
			String id = getMember(measureData, "id");
			JSObject computeCallback = getMember(measureData, "callback");

			boolean autoCompute = getOptionalMember(measureData, "autoCompute", false);
			JSObject applicabilityCallback = getOptionalMember(measureData, "isApplicable", null);

			if (!computeCallback.isFunction()) {
				throw new IllegalArgumentException("Member 'callback' is not a Function!");
			}
			if (applicabilityCallback != null && !applicabilityCallback.isFunction()) {
				throw new IllegalArgumentException("Member 'isApplicable' is not a Function!");
			}

			Function<Hierarchy, Boolean> applicabilityFunction = hierarchy -> {
				try {
					return (Boolean) inv.invokeMethod(measureData, "isApplicable", hierarchy);
				} catch (NoSuchMethodException e) {
					return true;
				} catch (ScriptException e) {
					log.error(String.format("Unexpected error while invoking applicability callback for measure '%s': ",
							id), e);
				}
				return false;
			};

			Function<Hierarchy, Object> computeFunction = hierarchy -> {
				try {
					return inv.invokeMethod(measureData, "callback", hierarchy);
				} catch (NoSuchMethodException | ScriptException e) {
					log.error(String.format("Error while invoking compute callback for measure '%s': ", id), e);
				}
				return null;
			};

			return new MeasureTask(measure, id, autoCompute, applicabilityFunction, computeFunction);
		} catch (IllegalArgumentException e) {
			log.error(String.format("Error while constructing MeasureTask from script file '%s': %s", path.toString(),
					e.getMessage()));
		} catch (NoSuchFieldException e) {
			log.error(String.format("Error while constructing MeasureTask from script file '%s':"
					+ " Could not find member '%s' in returned object.", path.toString(), e.getMessage()));
		}

		return null;
	}

	/**
	 * Use a nasty reflection hack to dynamically load jars that define measures.
	 * 
	 * @param jarFile the jar file to load
	 */
	private static void loadJar(File jarFile) {
		try {
			URL url = jarFile.toURI().toURL();

			Class<?>[] parameters = new Class[] { URL.class };

			URLClassLoader sysLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
			Class<?> sysClass = URLClassLoader.class;
			Method method = sysClass.getDeclaredMethod("addURL", parameters);
			method.setAccessible(true);
			method.invoke(sysLoader, url);
		} catch (Exception e) {
			throw new RuntimeException(
					String.format("Exception while loading measure jarfile '%s'.", jarFile.getPath()), e);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T getOptionalMember(JSObject jsObject, String member, T defaultValue) {
		return jsObject.hasMember(member) ? (T) jsObject.getMember(member) : defaultValue;
	}

	@SuppressWarnings("unchecked")
	private static <T> T getMember(JSObject jsObject, String member) throws NoSuchFieldException {
		if (jsObject.hasMember(member))
			return (T) jsObject.getMember(member);
		else
			throw new NoSuchFieldException(member);
	}
}
