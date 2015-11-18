package eu.inloop.knight.weaving;

import com.github.stephanenicolas.afterburner.AfterBurner;
import com.github.stephanenicolas.afterburner.exception.AfterBurnerImpossibleException;
import com.github.stephanenicolas.afterburner.inserts.InsertableConstructor;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import eu.inloop.knight.KnightApp;
import eu.inloop.knight.weaving.util.AWeaver;
import javassist.CtClass;
import javassist.CtField;
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
        String SERVICE = "android.app.Service";
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
        String ON_ATTACH = "onAttach";
        // generated classes
        String INJECT = "inject";
        String INIT = "init";
    }

    private interface Field {
        String INJECTED = "mInjectedByKnight";
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
                    || hasInjectField(candidateClass);
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
            // APP
            if (classToTransform.hasAnnotation(KnightApp.class)) {
                weaveKnightApp(classToTransform);
            }
            // SERVICE
            else if (isSubclassOf(classToTransform, Class.SERVICE)) {
                weaveService(classToTransform);
            }
            // FRAGMENT
            else if (isSubclassOf(classToTransform, Class.FRAGMENT)
                    || isSubclassOf(classToTransform, Class.SUPPORT_FRAGMENT)) {
                weaveFragment(classToTransform);
            }
            // VIEW
            else if (isSubclassOf(classToTransform, Class.VIEW)) {
                weaveView(classToTransform);
            }
            // nothing done
            else {
                log("applyTransformations - NOTHING done");
            }
            log("applyTransformations - done");
        } catch (Exception e) {
            log("applyTransformations - failed");
            e.printStackTrace();
            throw new JavassistBuildException(e);
        }
    }

    /**
     * Weaves into Application subclass
     */
    private void weaveKnightApp(CtClass classToTransform) throws Exception {
        String body = String.format("{ %s.%s(this); }", Class.INJECTOR, Method.INIT);
        log("Weaved: %s", body);
        mAfterBurner.beforeOverrideMethod(classToTransform, Method.ON_CREATE, body);
    }

    /**
     * Weaves into Service subclass
     */
    private void weaveService(CtClass classToTransform) throws Exception {
        String field = String.format("private boolean %s = false;", Field.INJECTED);
        log("Weaved: %s", field);
        classToTransform.addField(CtField.make(field, classToTransform));

        final String body = String.format("{ if (!%s) { %s.%s(this, null); %s = true; } }",
                Field.INJECTED, Class.INJECTOR, Method.INJECT, Field.INJECTED);
        log("Weaved: %s", body);

        // weave into constructors
        mAfterBurner.insertConstructor(new MyInsertableConstructor(classToTransform, body));
    }

    /**
     * Weaves into Fragment subclass
     */
    private void weaveFragment(CtClass classToTransform) throws Exception {
        String body = String.format("{ %s.%s(this, this.getContext()); }", Class.INJECTOR, Method.INJECT);
        log("Weaved: %s", body);

        // weave into method
        mAfterBurner.beforeOverrideMethod(classToTransform, Method.ON_ATTACH, body);
    }

    /**
     * Weaves into View subclass
     */
    private void weaveView(CtClass classToTransform) throws Exception {
        String field = String.format("private boolean %s = false;", Field.INJECTED);
        log("Weaved: %s", field);
        classToTransform.addField(CtField.make(field, classToTransform));

        final String body = String.format("{ if (!%s) { %s.%s(this, getContext()); %s = true; } }",
                Field.INJECTED, Class.INJECTOR, Method.INJECT, Field.INJECTED);
        log("Weaved: %s", body);

        // weave into constructors
        mAfterBurner.insertConstructor(new MyInsertableConstructor(classToTransform, body));
    }

    private boolean hasInjectField(CtClass candidateClass) {
        for (CtField field : candidateClass.getDeclaredFields()) {
            if (field.hasAnnotation(Inject.class)) return true;
        }
        return false;
    }

    private static class MyInsertableConstructor extends InsertableConstructor {

        private String mBody;

        public MyInsertableConstructor(CtClass classToInsertInto, String body) {
            super(classToInsertInto);
            this.mBody = body;
        }

        @Override
        public String getConstructorBody(CtClass[] paramClasses) throws AfterBurnerImpossibleException {
            return mBody;
        }

        @Override
        public boolean acceptParameters(CtClass[] paramClasses) throws AfterBurnerImpossibleException {
            return true; // into all
        }
    }

}
