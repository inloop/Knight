package eu.inloop.knight.weaving;

import com.github.stephanenicolas.afterburner.AfterBurner;

import java.util.Arrays;
import java.util.List;

import eu.inloop.knight.KnightApp;
import eu.inloop.knight.KnightView;
import eu.inloop.knight.weaving.util.AWeaver;
import javassist.CtClass;
import javassist.build.JavassistBuildException;

import static eu.inloop.knight.weaving.util.WeavingUtil.isSubclassOf;

/**
 * Class {@link KnightWeaver}
 *
 * @author FrantisekGazo
 * @version 2015-10-31
 */
public class KnightWeaver extends AWeaver {

    private interface Class {
        // android classes
        String FRAGMENT = "android.app.Fragment";
        String SUPPORT_FRAGMENT = "android.support.v4.app.Fragment";
        String VIEW = "android.view.View";
        String BUNDLE = "android.os.Bundle";
        // generated classes
        String INJECTOR = "the.knight.Injector";
    }

    private interface Method {
        // android classes
        String ON_CREATE = "onCreate";
        String ON_VIEW_CREATED = "onViewCreated";
        // generated classes
        String INJECT = "inject";
        String INIT = "init";
    }

    private static final List<String> REQUIRED_CLASSES = Arrays.asList(
            Class.INJECTOR
    );

    private AfterBurner mAfterBurner;

    /**
     * Constructor
     */
    public KnightWeaver() {
        super(REQUIRED_CLASSES);
        mAfterBurner = new AfterBurner();
    }

    @Override
    public boolean needTransformation(CtClass candidateClass) throws JavassistBuildException {
        try {
            //log("needTransformation ? %s", candidateClass.getName());
            return candidateClass.hasAnnotation(KnightApp.class)
                    || candidateClass.hasAnnotation(KnightView.class);
        } catch (Exception e) {
            log("needTransformation - failed");
            e.printStackTrace();
            throw new JavassistBuildException(e);
        }
    }

    @Override
    public void applyTransformations(CtClass classToTransform) throws JavassistBuildException {
        log("applyTransformations - %s", classToTransform.getName());
        try {
            if (classToTransform.hasAnnotation(KnightApp.class)) {
                weaveKnightInitialization(classToTransform);
            } else if (classToTransform.hasAnnotation(KnightView.class)) {
                weaveInjection(classToTransform);
            }
            log("applyTransformations - done");
        } catch (Exception e) {
            log("applyTransformations - failed");
            e.printStackTrace();
            throw new JavassistBuildException(e);
        }
    }

    private void weaveInjection(CtClass classToTransform) throws Exception {
        if (isSubclassOf(classToTransform, Class.FRAGMENT)
                || isSubclassOf(classToTransform, Class.SUPPORT_FRAGMENT)) {

            String body = String.format("{ %s.%s(this, this.getContext()); }", Class.INJECTOR, Method.INJECT);
            log("INJECTED: %s", body);
            mAfterBurner.beforeOverrideMethod(classToTransform, Method.ON_VIEW_CREATED, body);
        }
    }

    private void weaveKnightInitialization(CtClass classToTransform) throws Exception {
        String body = String.format("{ %s.%s(this); }", Class.INJECTOR, Method.INIT);
        log("INJECTED: %s", body);
        mAfterBurner.beforeOverrideMethod(classToTransform, Method.ON_CREATE, body);
    }

}
