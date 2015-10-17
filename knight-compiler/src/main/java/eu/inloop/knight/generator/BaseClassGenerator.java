package eu.inloop.knight.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.annotation.processing.Filer;

import eu.inloop.knight.util.ProcessorError;

public abstract class BaseClassGenerator {

    private static final String FULL_NAME_FORMAT = "%s.%s";

    private TypeSpec.Builder mBuilder;
    private ClassName mClassName = null;
    private final GCN mGenClassName;
    private final ClassName mGenClassNameArg;
    private final GPN[] mGenPackageName;

    public BaseClassGenerator(GCN genClassName, ClassName arg, GPN... genPackageNames) throws ProcessorError {
        mGenClassName = genClassName;
        mGenClassNameArg = arg;
        mGenPackageName = genPackageNames;
        mBuilder = TypeSpec.classBuilder(getClassName().simpleName());
        start();
    }

    public BaseClassGenerator(GCN genClassName, GPN... genPackageNames) throws ProcessorError {
        this(genClassName, null, genPackageNames);
    }

    public TypeSpec.Builder getBuilder() {
        return mBuilder;
    }

    public ClassName getClassName() {
        if (mClassName == null) {
            // build CLASS name
            String name;
            if (mGenClassNameArg == null) {
                name = mGenClassName.getmName();
            } else {
                name = String.format(mGenClassName.getmName(), mGenClassNameArg.simpleName());
            }
            // save
            mClassName = ClassName.get(GPN.toString(mGenPackageName), name);
        }
        return mClassName;
    }

    protected String getFullName() {
        return String.format(FULL_NAME_FORMAT, getClassName().packageName(), getClassName().simpleName());
    }

    public abstract void start() throws ProcessorError;
    public abstract void end() throws ProcessorError;

    public void build(Filer filer) throws ProcessorError, IOException {
        end();
        TypeSpec cls = mBuilder.build();
        // create file
        JavaFile javaFile = JavaFile.builder(getClassName().packageName(), cls).build();
        javaFile.writeTo(filer);
        //javaFile.writeTo(System.out);

        System.out.println(String.format("Class <%s> successfully generated.", getFullName()));
    }

}
