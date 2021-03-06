package com.travelbank.knitprocessor.vp;

import com.travelbank.knit.KnitView;
import com.travelbank.knit.ModelEvent;
import com.travelbank.knit.Presenter;
import com.travelbank.knit.ViewEvent;
import com.travelbank.knitprocessor.KnitBaseProcessor;
import com.travelbank.knitprocessor.user.UserMirror;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by omerozer on 2/2/18.
 */

public class KnitPresenterProcessor extends KnitBaseProcessor {

    private Set<KnitPresenterMirror> presenters;
    private Set<KnitViewMirror> views;
    private Map<KnitPresenterMirror, KnitViewMirror> presenterToViewMap;
    private KnitPresenterWriter knitPresenterWriter;
    private PresenterExposerWriter presenterExposerWriter;
    private ContractWriter contractWriter;
    private ViewToPresenterMapWriter viewToPresenterMapWriter;


    public KnitPresenterProcessor(ProcessingEnvironment processingEnvironment) {
        super(processingEnvironment);
        this.presenters = new LinkedHashSet<>();
        this.views = new LinkedHashSet<>();
        this.presenterToViewMap = new LinkedHashMap<>();
        this.knitPresenterWriter = new KnitPresenterWriter();
        this.presenterExposerWriter = new PresenterExposerWriter();
        this.contractWriter = new ContractWriter();
        this.viewToPresenterMapWriter = new ViewToPresenterMapWriter();
    }

    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment,Set<UserMirror> userMirrors) {

        processViews(roundEnvironment.getElementsAnnotatedWith(KnitView.class));
        processPresenters(roundEnvironment.getElementsAnnotatedWith(Presenter.class));

        handleMapping();

        createViews(views);
        createPresenters(presenters, presenterToViewMap,userMirrors);
        createViewToPresenterMap(presenterToViewMap);

        return true;
    }

    private void processPresenters(Set<? extends Element> presenters) {
        for (Element presenter : presenters) {
            TypeElement clazz = (TypeElement) presenter;
            KnitPresenterMirror knitPresenterMirror = new KnitPresenterMirror();
            knitPresenterMirror.enclosingClass = clazz;
            knitPresenterMirror.targetView = getClassAnnotationValue(presenter, Presenter.class);
            String[] needs = presenter.getAnnotation(Presenter.class).needs();
            if(needs[0]!=""){
                knitPresenterMirror.needs.addAll(Arrays.<String>asList(needs));
            }
            for (Element element : clazz.getEnclosedElements()) {

                if (element.getKind().equals(ElementKind.METHOD)) {
                    if (element.getAnnotation(ModelEvent.class) != null) {
                        knitPresenterMirror.updatingMethodsMap.put(
                                element.getAnnotation(ModelEvent.class).value(),
                                (ExecutableElement) element);
                        knitPresenterMirror.needs.addAll(Collections.<String>singletonList(
                                element.getAnnotation(ModelEvent.class).value()));
                    }else if(element.getAnnotation(ViewEvent.class)!=null){
                        knitPresenterMirror.viewEventMethods.put(
                                element.getAnnotation(ViewEvent.class).value(),
                                (ExecutableElement) element);
                    }
                }
                this.presenters.add(knitPresenterMirror);
            }
        }
    }

    private void processViews(Set<? extends Element> presenters) {
        for (Element presenter : presenters) {
            TypeElement clazz = (TypeElement) presenter;
            KnitViewMirror knitViewMirror = new KnitViewMirror();
            knitViewMirror.enclosingClass = clazz;
            for (Element element : clazz.getEnclosedElements()) {
                if (element.getKind() == ElementKind.METHOD && AndroidViewMethodsFilter.filter(
                        element)) {
                    knitViewMirror.methods.add((ExecutableElement) element);
                }
            }
            this.views.add(knitViewMirror);
        }
    }

    private void handleMapping() {
        for (KnitPresenterMirror presenter : presenters) {
            for (KnitViewMirror views : views) {
                if (presenter.targetView.equals(views.enclosingClass.asType())) {
                    presenterToViewMap.put(presenter, views);
                }
            }
        }
    }


    private TypeMirror getClassAnnotationValue(Element element, Class<?> clazz) {
        for (AnnotationMirror mirror : element.getAnnotationMirrors()) {
            if (mirror.getAnnotationType().toString().equals(clazz.getName())) {
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
                        mirror.getElementValues().entrySet()) {
                    if (entry.getKey().getSimpleName().toString().equals("value")) {
                        return (TypeMirror) entry.getValue().getValue();
                    }
                }

            }
        }
        return null;
    }

    private void createPresenters(Set<KnitPresenterMirror> presenters,
            Map<KnitPresenterMirror, KnitViewMirror> map,Set<UserMirror> userMirrors) {
        for (KnitPresenterMirror knitPresenterMirror : presenters) {
            presenterExposerWriter.write(getEnv().getFiler(), knitPresenterMirror);
            knitPresenterWriter.write(getEnv().getFiler(), knitPresenterMirror, map,userMirrors);
        }
    }

    private void createViewToPresenterMap(Map<KnitPresenterMirror, KnitViewMirror> map) {
        viewToPresenterMapWriter.write(getEnv().getFiler(), map);
    }


    private void createViews(Set<KnitViewMirror> views) {
        for (KnitViewMirror viewMirror : views) {
            contractWriter.write(getEnv().getFiler(),viewMirror);
        }
    }



}
