package eu.inloop.knight.weaving;

import com.github.stephanenicolas.afterburner.AfterBurner;

import java.util.Arrays;
import java.util.List;

import eu.inloop.knight.Injectable;
import eu.inloop.knight.weaving.util.BaseClassTransformer;
import javassist.CtClass;
import javassist.build.JavassistBuildException;


/**
 * Class {@link KnightClassTransformer}
 *
 * @author FrantisekGazo
 * @version 2015-10-31
 */
public class KnightClassTransformer extends BaseClassTransformer {

    private static final String KNIGHT_CLASS = "the.knight.Knight";
    private static final String KNIGHT_METHOD = "log";

    private static final List<String> REQUIRED_CLASSES = Arrays.asList(
            //KNIGHT_CLASS
    );

    private final boolean mDebug;
    private AfterBurner mAfterBurner = new AfterBurner();

    public KnightClassTransformer(boolean debug) {
        super(REQUIRED_CLASSES);
        mDebug = debug;
    }

    @Override
    public boolean canTransform(CtClass candidateClass) throws JavassistBuildException {
        try {
            log("shouldTransform ? %s", candidateClass.getName());
            //return true;
            return candidateClass.hasAnnotation(Injectable.class);
        } catch (Exception e) {
            log("shouldTransform Crashed");
            e.printStackTrace();
            throw new JavassistBuildException(e);
        }
    }

    @Override
    public void transform(CtClass classToTransform) throws JavassistBuildException {
        log("transform class %s", classToTransform.getName());
        try {
            //String body = String.format("%s.%s();", KNIGHT_CLASS, KNIGHT_METHOD);
            String body = String.format("android.util.Log.d(\"Logged....\", \"HAHAHA\");");

            //InsertableMethodBuilder builder = new InsertableMethodBuilder(mAfterBurner);

            try {
                classToTransform.getDeclaredMethod("onViewCreated")
                        .insertBefore(body);
                classToTransform.writeFile();

//                builder.insertIntoClass(classToTransform)
//                        .inMethodIfExists("onViewCreated")
//                        .afterACallTo("onViewCreated")
//                        .withBody(body)
//                        .elseCreateMethodIfNotExists("") //not used, we are sure the method exists
//                        .doIt();
            } catch (Exception e) {
                e.printStackTrace();
            }


//            InsertableMethodBuilder builder = new InsertableMethodBuilder(mAfterBurner);
//
//            builder.insertIntoClass(classToTransform)
//                    .inMethodIfExists("onViewCreated")
//                    .afterACallTo()
//                    .withBody(body)
//                    .doIt();

            log("transform done");
        } catch (Exception e) {
            log("transform failed !!! %s", e);
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
