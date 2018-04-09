package com.omerozer.knit;

import java.util.List;

/**
 *
 * When KnitProcessor scans classpath, it creates a class called {@code ModelMap} that implements {@link ModelMapInterface}.
 * This class contains all data such as which model requires/generates which data and what their parent classes are.
 * This is how {@link com.omerozer.knit.components.graph.UsageGraph} determines dependencies.
 * This class is loaded via {@link com.omerozer.knit.classloaders.KnitUtilsLoader}.
 *
 * @author Omer Ozer
 */

public interface ModelMapInterface {

     /**
      * Returns all {@link InternalModel} classes that are created by KnitProcessor.
      * @return
      */
     List<Class<? extends InternalModel>> getAll();

     /**
      * Returns all data tags generated by a particular Model.
      * @param clazz Class of the given model.
      * @return List of Strings with all data tags.
      */
     List<String> getGeneratedValues(Class<?> clazz);

     /**
      * Returns all data tags required by a particular Model.
      * @param clazz Class of the given model.
      * @return List of Strings with all data tags.
      */
     List<String> getRequiredValues(Class<?> clazz);

     /**
      * Returns whether a given model is tagged as a {@link InstanceType#SINGLETON}.
      * @param clazz Class of the given model.
      * @return Whether given model is a singleton or not.
      */
     boolean isModelSingleton(Class<? extends InternalModel> clazz);

     /**
      * Returns class type of the {@link InternalModel} for a given {@link KnitModel} class.
      * @param target Given {@link KnitModel} class.
      * @return {@link InternalModel} class that manages the given {@link KnitModel}.
      */
     Class<? extends InternalModel> getModelClassForModel(Class<? extends KnitModel> target);
}
