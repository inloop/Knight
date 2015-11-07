package eu.inloop.knight.weaving;

import eu.inloop.knight.Injectable;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.build.IClassTransformer;
import javassist.build.JavassistBuildException;

/**
 * Class {@link KnightClassTransformer}
 *
 * @author FrantisekGazo
 * @version 2015-10-31
 */
public class KnightClassTransformer implements IClassTransformer {

    private static final String VIEW_CLASS = "android.view.View";
    private static final String BUNDLE_CLASS = "android.os.Bundle";
    private static final String INJECTOR_CLASS = "a.Injector";
    private static final String INJECTOR_METHOD = "inject";

    private final boolean mDebug;

    public KnightClassTransformer(boolean debug) {
        mDebug = debug;
    }

    @Override
    public boolean shouldTransform(CtClass candidateClass) throws JavassistBuildException {
        try {
            log("shouldTransform ? %s", candidateClass.getName());
            //log("POOL %s", candidateClass.getClassPool().get("eu.inloop.knight.sample.util.A"));
            return candidateClass.hasAnnotation(Injectable.class);
        } catch (Exception e) {
            log("shouldTransform Crashed %s", e.getMessage());
            e.printStackTrace();
            throw new JavassistBuildException(e);
        }
    }

    @Override
    public void applyTransformations(CtClass classToTransform) throws JavassistBuildException {
        log("transform class %s", classToTransform.getName());
        try {
            ClassPool pool = classToTransform.getClassPool();

            //String body = String.format("android.util.Log.d(\"Logged....\", \"HAHAHA\");");
            //String body = "a.Injector.log();";

            try {
                CtMethod method = classToTransform.getDeclaredMethod("onViewCreated",
                        new CtClass[]{pool.get(VIEW_CLASS), pool.get(BUNDLE_CLASS)});

                StringBuilder body = new StringBuilder("");

                body.append(INJECTOR_CLASS)
                        .append(".")
                        .append(INJECTOR_METHOD)
                        .append("(")
                        .append("this")
                        .append(",")
                        .append("this.getContext()")
                        .append(");");

                log("INJECTED: %s", body.toString());

                method.insertBefore(body.toString());
                classToTransform.writeFile();
            } catch (Exception e) {
                log("transform METHOD NOT FOUND !!!!!!!!!!!!!!! %s", e.getMessage());
                //throw new JavassistBuildException(e);
            }

            log("transform done");
        } catch (Exception e) {
            log("transform failed !!!!!!!!!!!!! %s", e);
            e.printStackTrace();
            throw new JavassistBuildException(e);
        }
    }

    private void log(String msg, Object... args) {
        if (mDebug) {
            System.out.printf("@ KnightClassTransformer : " + msg + "\n", args);
        }
    }

}
