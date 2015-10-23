package eu.inloop.knight.builder;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

import eu.inloop.knight.EClass;
import eu.inloop.knight.util.ProcessorError;

public abstract class BaseClassBuilder {

    private static final String FULL_NAME_FORMAT = "%s.%s";

    private static final List<ClassName> UNALLOWED_ANNOTATIONS = Arrays.asList(
            EClass.Override.getName(),
            EClass.AppProvided.getName(),
            EClass.ScreenProvided.getName(),
            EClass.ActivityProvided.getName(),
            EClass.AppScope.getName(),
            EClass.ScreenScope.getName(),
            EClass.ActivityScope.getName()
    );

    private TypeSpec.Builder mBuilder;
    private ClassName mClassName = null;
    private final GCN mGenClassName;
    private final ClassName mGenClassNameArg;
    private final GPN[] mGenPackageName;

    public BaseClassBuilder(boolean isClass, GCN genClassName, ClassName arg, GPN... genPackageNames) throws ProcessorError {
        mGenClassName = genClassName;
        mGenClassNameArg = arg;
        mGenPackageName = genPackageNames;
        if (isClass) {
            mBuilder = TypeSpec.classBuilder(getClassName().simpleName());
        } else {
            mBuilder = TypeSpec.interfaceBuilder(getClassName().simpleName());
        }
        start();
    }

    public BaseClassBuilder(GCN genClassName, ClassName arg, GPN... genPackageNames) throws ProcessorError {
        this(true, genClassName, arg, genPackageNames);
    }

    public BaseClassBuilder(GCN genClassName, GPN... genPackageNames) throws ProcessorError {
        this(true, genClassName, null, genPackageNames);
    }

    public BaseClassBuilder(boolean isClass, GCN genClassName, GPN... genPackageNames) throws ProcessorError {
        this(isClass, genClassName, null, genPackageNames);
    }

    public TypeSpec.Builder getBuilder() {
        return mBuilder;
    }

    protected ClassName getArgClassName() {
        return mGenClassNameArg;
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

    public void start() throws ProcessorError {
    }

    public void end() throws ProcessorError {
    }

    public void build(Filer filer) throws ProcessorError, IOException {
        end();
        TypeSpec cls = mBuilder.build();
        // create file
        JavaFile javaFile = JavaFile.builder(getClassName().packageName(), cls).build();
        javaFile.writeTo(filer);
        //javaFile.writeTo(System.out);

        System.out.println(String.format("Class <%s> successfully generated.", getFullName()));
    }

    public List<AnnotationSpec> getAnnotations(Element e) {
        List<AnnotationSpec> list = new ArrayList<>();
        for (AnnotationMirror a : e.getAnnotationMirrors()) {
            ClassName annotClassName = (ClassName) ClassName.get(a.getAnnotationType());
            if (!UNALLOWED_ANNOTATIONS.contains(annotClassName)) {
                AnnotationSpec.Builder annotation = AnnotationSpec.builder(annotClassName);
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : a.getElementValues().entrySet()) {
                    String format = (entry.getValue().getValue() instanceof String) ? "$S" : "$L";
                    annotation.addMember(entry.getKey().getSimpleName().toString(), format, entry.getValue().getValue());
                }
                list.add(annotation.build());
            }
        }
        return list;
    }

}
