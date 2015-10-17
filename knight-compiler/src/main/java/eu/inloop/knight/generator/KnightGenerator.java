package eu.inloop.knight.generator;

import com.squareup.javapoet.ClassName;

import javax.lang.model.element.Modifier;

import eu.inloop.knight.Scoped;
import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link KnightGenerator}
 *
 * @author FrantisekGazo
 * @version 2015-10-15
 */
public class KnightGenerator extends BaseClassGenerator {

    public KnightGenerator() throws ProcessorError {
        super(GCN.KNIGHT, GPN.KNIGHT);
    }

    @Override
    public void start() throws ProcessorError {
        getBuilder().addModifiers(Modifier.FINAL, Modifier.PUBLIC);
    }

    @Override
    public void end() throws ProcessorError {

    }

    public void addFromMethod(ClassName scoped) {
        //getBuilder().addFromMethod(scoped, String.format("m%s", scoped.simpleName()), Modifier.PRIVATE);
    }
}
