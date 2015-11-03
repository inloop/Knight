package eu.inloop.knight.weaving;

import com.github.stephanenicolas.afterburner.AfterBurner;

import eu.inloop.knight.Injectable;
import javassist.CtClass;
import javassist.build.IClassTransformer;
import javassist.build.JavassistBuildException;


/**
 * Class {@link KnightClassTransformer}
 *
 * @author FrantisekGazo
 * @version 2015-10-31
 */
public class KnightClassTransformer implements IClassTransformer {

    private final boolean mDebug;
    private AfterBurner mAfterBurner = new AfterBurner();

    public KnightClassTransformer(boolean debug) {
        mDebug = debug;
        log("constructor %b", debug);
    }

    @Override
    public boolean shouldTransform(CtClass candidateClass) throws JavassistBuildException {
        try {
            //log("shouldTransform ?");
            return candidateClass.hasAnnotation(Injectable.class);
        } catch (Exception e) {
            log("shouldTransform Crashed");
            throw new JavassistBuildException(e);
        }
    }

    @Override
    public void applyTransformations(CtClass classToTransform) throws JavassistBuildException {
        String classToTransformName = classToTransform.getName();
        log("applyTransformations to %s", classToTransformName);

        /* TODO : do something

        try {
//            Injectable i = (Injectable) classToTransform.getAnnotation(Injectable.class);
//            for (Class c : i.from()) {
//                log("injectable from %s", c.getSimpleName());
//            }

            InsertableConstructorBuilder builder = new InsertableConstructorBuilder(mAfterBurner);
            ClassPool pool = ClassPool.getDefault();
            pool.getClassLoader().(new ClassClassPath(classToTransform.toClass()));

            CtClass c = pool.get("eu.inloop.knight.sample.adapter.ContactRecyclerAdapter");
            log("Found Knight %s", c.getName());
            //String body = "android.util.Log.d(\"KnigthByteCodeWeaving\", \"%s Logged with Byte Code Weaving :)\");";
            //String body = "the.knight.Knight.log();";
            String body = "";

            builder.insertIntoClass(classToTransform)
                    .withBody(String.format(body, classToTransform.getSimpleName()))
                    .doIt();

            log("applyTransformations done");
        } catch (UndeclaredThrowableException e) {
            log("applyTransformations failed to get Activity classes !!! %s", e);
            e.printStackTrace();
        } catch (Exception e) {
            log("applyTransformations failed !!! %s", e);
            e.printStackTrace();
            throw new JavassistBuildException(e);
        }
        */
    }

    private void log(String msg, Object... args) {
        if (mDebug) {
            System.out.printf("@ KnightClassTransformer : " + msg + "\n", args);
        }
    }

}
