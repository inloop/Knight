package eu.inloop.knight;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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

import eu.inloop.knight.generator.AppScopedGenerators;
import eu.inloop.knight.generator.ApplicationComponentGenerator;
import eu.inloop.knight.generator.ApplicationModuleGenerator;
import eu.inloop.knight.generator.KnightGenerator;
import eu.inloop.knight.generator.ScopedGenerators;
import eu.inloop.knight.util.ProcessorError;

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
        annotations.add(ScopedApp.class.getCanonicalName());
        annotations.add(Scoped.class.getCanonicalName());
        return annotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            Set<? extends Element> scopedActivities = roundEnv.getElementsAnnotatedWith(Scoped.class);
            if (scopedActivities.isEmpty()) {
                return false;
            }

            // create generators for application scope
            AppScopedGenerators gensAppScoped = new AppScopedGenerators();
            // create generators for scoped Activities
            Map<ClassName, ScopedGenerators> gensScoped = new HashMap<>();
            for (Element e : scopedActivities) {
                ClassName scoped = ClassName.get((TypeElement) e);
                gensScoped.put(scoped, new ScopedGenerators(scoped));

                gensAppScoped.knight.addFromMethod(scoped);
            }




            // build everything
            for (Map.Entry<ClassName, ScopedGenerators> entry : gensScoped.entrySet()) {
                entry.getValue().buildAll(mFiler);
            }
            gensAppScoped.buildAll(mFiler);
        } catch (ProcessorError pe) {
            error(pe);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return false;
    }

    private void error(ProcessorError error) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, error.getMessage(), error.getmElement());
    }

    private void error(Element e, String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }

}
