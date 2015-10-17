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
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import eu.inloop.knight.builder.activity.ActivityBuilders;
import eu.inloop.knight.builder.app.AppBuilders;
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
        Set<String> annotations = new HashSet<>();
        annotations.add(Scoped.class.getCanonicalName());
        annotations.add(Injectable.class.getCanonicalName());
        return annotations;
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

                appBuilders.add(activityName, activityBuilders);
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


            // build everything
            for (Map.Entry<ClassName, ActivityBuilders> activityBuilders : activityBuildersMap.entrySet()) {
                activityBuilders.getValue().buildAll(mFiler);
            }
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
            throw new ProcessorError(e, "@%s can contain only Activity classes annotated with @%s.", Injectable.class.getName(), Scoped.class.getName());
        }
        activityBuilders.AC.addInjectMethod(ClassName.get(e));
    }

    private ClassName getScopedActivityName(TypeElement e) throws ProcessorError {
        if (!ProcessorUtils.isSubClassOf(e, EClass.AppCompatActivity.getName(), EClass.Activity.getName())) {
            throw new ProcessorError(e, "Only Activity class can be annotated with @%s.", Scoped.class.getName());
        }
        return ClassName.get(e);
    }

    private void error(ProcessorError error) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, error.getMessage(), error.getmElement());
    }

    private void error(Element e, String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }

}