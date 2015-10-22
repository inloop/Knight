package eu.inloop.knight;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import eu.inloop.knight.builder.ActivityBuilders;
import eu.inloop.knight.builder.AppBuilders;
import eu.inloop.knight.util.ProcessorError;
import eu.inloop.knight.util.ProcessorUtils;

/**
 * Class {@link KnightProcessor}
 *
 * @author FrantisekGazo
 * @version 2015-10-15
 */
@AutoService(Processor.class)
public class KnightProcessor extends AbstractProcessor {

    private Messager mMessager;
    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mMessager = processingEnv.getMessager();
        mFiler = processingEnv.getFiler();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return annotations(
                Scoped.class,
                Injectable.class,
                AppProvided.class,
                ScreenProvided.class,
                ActivityProvided.class
        );
    }

    private Set<String> annotations(Class... annotations) {
        Set<String> set = new HashSet<>();
        for (Class a : annotations) {
            set.add(a.getCanonicalName());
        }
        return set;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Scoped.class);
            if (elements.isEmpty()) {
                return false;
            }

            // create generators for application scope
            AppBuilders appBuilders = new AppBuilders();
            // create generators for scoped Activities
            Map<ClassName, ActivityBuilders> activityBuildersMap = new HashMap<>();
            for (Element e : elements) {
                ClassName activityName = getScopedActivityName((TypeElement) e);
                ActivityBuilders activityBuilders = new ActivityBuilders(activityName);
                activityBuildersMap.put(activityName, activityBuilders);
                // create navigator methods
                appBuilders.Navigator.integrate((TypeElement) e, activityBuilders);
            }

            // add Injectable classes
            elements = roundEnv.getElementsAnnotatedWith(Injectable.class);
            for (Element e : elements) {
                List<ClassName> activities = ProcessorUtils.getParamClasses(e.getAnnotation(Injectable.class),
                        new ProcessorUtils.IGetter<Injectable, Class<?>[]>() {
                            @Override
                            public Class<?>[] get(Injectable a) {
                                return a.from();
                            }
                        });

                if (activities.isEmpty()) { // injectable by Application scope only
                    appBuilders.AppC.addInjectMethod(ClassName.get((TypeElement) e));
                } else { // injectable by Activity scope
                    for (ClassName activity : activities) {
                        addInjectable(activityBuildersMap.get(activity), (TypeElement) e);
                    }
                }
            }

            // add Provided
            elements = roundEnv.getElementsAnnotatedWith(AppProvided.class);
            for (Element e : elements) {
                switch (e.getKind()) {
                    case CONSTRUCTOR:
                        appBuilders.AppM.addProvidesConstructor((ExecutableElement) e);
                        break;
                    case METHOD:
                        appBuilders.AppM.addProvidesMethod((ExecutableElement) e);
                        break;
                    case CLASS:
                        appBuilders.AppC.addModule((TypeElement) e);
                        break;
                }
            }
            elements = roundEnv.getElementsAnnotatedWith(ScreenProvided.class);
            for (Element e : elements) {
                List<ClassName> in = ProcessorUtils.getParamClasses(e.getAnnotation(ScreenProvided.class), new ProcessorUtils.IGetter<ScreenProvided, Class<?>[]>() {
                    @Override
                    public Class<?>[] get(ScreenProvided annotation) {
                        return annotation.in();
                    }
                });
                ActivityBuilders activityBuilders;
                for (ClassName activity : in) {
                    activityBuilders = activityBuildersMap.get(activity);
                    if (activityBuilders == null) {
                        throw new ProcessorError(e, ErrorMsg.Provided_outside_Scoped_Activity);
                    }
                    switch (e.getKind()) {
                        case CONSTRUCTOR:
                            activityBuilders.SM.addProvidesConstructor((ExecutableElement) e);
                            break;
                        case METHOD:
                            activityBuilders.SM.addProvidesMethod((ExecutableElement) e);
                            break;
                        case CLASS:
                            activityBuilders.SC.addModule((TypeElement) e);
                            break;
                    }
                }
            }
            elements = roundEnv.getElementsAnnotatedWith(ActivityProvided.class);
            for (Element e : elements) {
                List<ClassName> in = ProcessorUtils.getParamClasses(e.getAnnotation(ActivityProvided.class), new ProcessorUtils.IGetter<ActivityProvided, Class<?>[]>() {
                    @Override
                    public Class<?>[] get(ActivityProvided annotation) {
                        return annotation.in();
                    }
                });
                ActivityBuilders activityBuilders;
                for (ClassName activity : in) {
                    activityBuilders = activityBuildersMap.get(activity);
                    if (activityBuilders == null) {
                        throw new ProcessorError(e, ErrorMsg.Provided_outside_Scoped_Activity);
                    }
                    switch (e.getKind()) {
                        case CONSTRUCTOR:
                            activityBuilders.AM.addProvidesConstructor((ExecutableElement) e);
                            break;
                        case METHOD:
                            activityBuilders.AM.addProvidesMethod((ExecutableElement) e);
                            break;
                        case CLASS:
                            activityBuilders.AC.addModule((TypeElement) e);
                            break;
                    }
                }
            }

            // build everything
            for (Map.Entry<ClassName, ActivityBuilders> activityBuildersEntry : activityBuildersMap.entrySet()) {
                activityBuildersEntry.getValue().buildAll(appBuilders, mFiler);
            }
            appBuilders.add(activityBuildersMap.values());
            appBuilders.buildAll(mFiler);
        } catch (ProcessorError pe) {
            error(pe);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return false;
    }

    private void addInjectable(ActivityBuilders activityBuilders, TypeElement e) throws ProcessorError {
        if (activityBuilders == null) {
            throw new ProcessorError(e, ErrorMsg.Injectable_outside_Scoped_Activity);
        }
        activityBuilders.AC.addInjectMethod(ClassName.get(e));
    }

    private ClassName getScopedActivityName(TypeElement e) throws ProcessorError {
        if (!ProcessorUtils.isSubClassOf(e, EClass.AppCompatActivity.getName(), EClass.Activity.getName())) {
            throw new ProcessorError(e, ErrorMsg.Scoped_invalid);
        }
        return ClassName.get(e);
    }

    private void error(ProcessorError error) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, error.getMessage(), error.getmElement());
    }

    private void error(Element e, ErrorMsg msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg.toString(), args), e);
    }

}
