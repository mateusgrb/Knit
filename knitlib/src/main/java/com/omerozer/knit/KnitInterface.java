package com.omerozer.knit;

import com.omerozer.knit.classloaders.KnitModelLoader;
import com.omerozer.knit.classloaders.KnitPresenterLoader;
import com.omerozer.knit.classloaders.KnitUtilsLoader;
import com.omerozer.knit.components.ModelManager;
import com.omerozer.knit.schedulers.SchedulerProvider;

/**
 * Base interface for {@link Knit} class. Has getters for all shared Knit resources.
 * Allows creation of a different Knit environment for tests.
 *
 * @author Omer Ozer
 */

public interface KnitInterface {

    /**
     * Supplies a {@link SchedulerProvider}.
     * @return supplied {@link SchedulerProvider}
     */
    SchedulerProvider getSchedulerProvider();

    /**
     * Supplies a {@link ModelManager}.
     * @return supplied {@link ModelManager}
     */
    ModelManager getModelManager();

    /**
     * Supplies a {@link KnitNavigator}.
     * @return supplied {@link KnitNavigator}
     */
    KnitNavigator getNavigator();

    /**
     * Supplies a {@link KnitModelLoader}.
     * @return supplied {@link KnitModelLoader}
     */
    KnitModelLoader getModelLoader();

    /**
     * Supplies a {@link KnitPresenterLoader}.
     * @return supplied {@link KnitPresenterLoader}
     */
    KnitPresenterLoader getPresenterLoader();

    /**
     * Supplies a {@link KnitUtilsLoader}.
     * @return supplied {@link KnitUtilsLoader}
     */
    KnitUtilsLoader getUtilsLoader();

    /**
     * Supplies a {@link ViewEvents}.
     * @return supplied {@link ViewEvents}
     */
    ViewEvents getViewEvents();

    /**
     * Supplies a {@link MessagePool}.
     * @return supplied {@link MessagePool}
     */
    MessagePool getMessagePool();

    /**
     * Supplies a {@link MessageTrain}.
     * @return supplied {@link MessageTrain}
     */
    MessageTrain getMessageTrain();

    /**
     * Supplies a {@link ViewToPresenterMapInterface}.
     * @return supplied {@link ViewToPresenterMapInterface}
     */
    ViewToPresenterMapInterface getViewToPresenterMap();

    /**
     * Supplies a {@link ModelMapInterface}.
     * @return supplied {@link ModelMapInterface}
     */
    ModelMapInterface getModelMap();
}
