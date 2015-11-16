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
import javax.inject.Inject;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;

import eu.inloop.knight.builder.ActivityBuilders;
import eu.inloop.knight.builder.AppBuilders;
import eu.inloop.knight.util.ProcessorError;
import eu.inloop.knight.util.ProcessorUtils;

import static eu.inloop.knight.util.ProcessorUtils.getParamClasses;

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
    private boolean mProcessed;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mMessager = processingEnv.getMessager();
        mFiler = processingEnv.getFiler();
        mProcessed = false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return annotations(
                KnightApp.class,
                KnightActivity.class,
                AppProvided.class,
                ScreenProvided.class,
                ActivityProvided.class,
                Inject.class
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
        if (mProcessed) return false;
        mProcessed = true;

        try {
            // collect all classes with some @Inject field
            Map<ClassName, Injectable> classesWithInject = new HashMap<>();
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Inject.class);
            for (Element e : elements) {
                System.out.printf("@KnightProcessor : Inject %s", e.getSimpleName());
                if (e instanceof VariableElement && e.getEnclosingElement() instanceof TypeElement) {
                    TypeElement enclosing = (TypeElement) e.getEnclosingElement();
                    classesWithInject.put(ClassName.get(enclosing), new Injectable(enclosing));
                    System.out.printf(" is injectable in %s", enclosing.getSimpleName());
                }
                System.out.println();
            }

            // create Knight Application Builders
            elements = roundEnv.getElementsAnnotatedWith(KnightApp.class);
            TypeElement appElement = getKnightAppName(elements);
            ClassName appClassName = ClassName.get(appElement);
            AppBuilders appBuilders = new AppBuilders(appClassName);
            // add injectable classes
            appBuilders.setInjectables(getParamClasses(appElement.getAnnotation(KnightApp.class),
                    new ProcessorUtils.IGetter<KnightApp, Class<?>[]>() {
                        @Override
                        public Class<?>[] get(KnightApp a) {
                            return a.injects();
                        }
                    }), classesWithInject);

            // go through Knight Activities
            elements = roundEnv.getElementsAnnotatedWith(KnightActivity.class);
            Map<ClassName, ActivityBuilders> activityBuildersMap = new HashMap<>();
            for (Element e : elements) {
                // create Knight Activity Builders
                ClassName activityName = getKnightActivityName((TypeElement) e);
                ActivityBuilders activityBuilders = new ActivityBuilders(activityName);
                activityBuildersMap.put(activityName, activityBuilders);
                // add injectable classes
                activityBuilders.setInjectables(getParamClasses(e.getAnnotation(KnightActivity.class),
                        new ProcessorUtils.IGetter<KnightActivity, Class<?>[]>() {
                            @Override
                            public Class<?>[] get(KnightActivity a) {
                                return a.injects();
                            }
                        }), classesWithInject);
                // create navigator methods
                appBuilders.Navigator.integrate((TypeElement) e, activityBuilders);
            }

            // add method to Injector
            for (Map.Entry<ClassName, Injectable> entry : classesWithInject.entrySet()) {
                if (entry.getValue().isValid()) {
                    appBuilders.Injector.addInjectMethod(appBuilders.Knight, entry.getValue());
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
                List<ClassName> in = getParamClasses(e.getAnnotation(ScreenProvided.class), new ProcessorUtils.IGetter<ScreenProvided, Class<?>[]>() {
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
                List<ClassName> in = getParamClasses(e.getAnnotation(ActivityProvided.class), new ProcessorUtils.IGetter<ActivityProvided, Class<?>[]>() {
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

    private TypeElement getKnightAppName(Set<? extends Element> elements) throws ProcessorError {
        if (elements.isEmpty()) {
            throw new ProcessorError(null, ErrorMsg.Missing_Knight_App);
        }
        TypeElement appElement = null;
        for (Element e : elements) {
            checkKnightApp((TypeElement) e);
            if (appElement != null) {
                throw new ProcessorError(e, ErrorMsg.More_Knight_Apps);
            } else {
                appElement = (TypeElement) e;
            }
        }
        return appElement;
    }

    private void checkKnightApp(TypeElement e) throws ProcessorError {
        if (!e.getModifiers().contains(Modifier.PUBLIC)) {
            throw new ProcessorError(e, ErrorMsg.Invalid_Knight_App);
        }
        if (!ProcessorUtils.isSubClassOf(e, EClass.Application.getName())) {
            throw new ProcessorError(e, ErrorMsg.Invalid_Knight_App);
        }
    }

    private ClassName getKnightActivityName(TypeElement e) throws ProcessorError {
        if (!ProcessorUtils.isSubClassOf(e, EClass.AppCompatActivity.getName(), EClass.Activity.getName())) {
            throw new ProcessorError(e, ErrorMsg.Scoped_invalid);
        }
        return ClassName.get(e);
    }

    private void error(ProcessorError error) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, error.getMessage(), error.getElement());
    }

    private void error(Element e, ErrorMsg msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg.toString(), args), e);
    }

}
