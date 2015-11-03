package eu.inloop.knight.plugin

import com.github.stephanenicolas.morpheus.AbstractMorpheusPlugin
import eu.inloop.knight.weaving.KnightClassTransformer
import javassist.build.IClassTransformer
import org.gradle.api.Project

/**
 * Class {@link KnightPlugin}
 *
 * @author FrantisekGazo
 * @version 2015-10-31
 */
public class KnightPlugin extends AbstractMorpheusPlugin {

    @Override
    public IClassTransformer[] getTransformers(Project project) {
        System.out.println("@ KnightPlugin -> getTransformers")
        return new KnightClassTransformer(project.knight.debug);
    }

    @Override
    protected void configure(Project project) {
        System.out.println("@ KnightPlugin -> configure")
        project.android {
            packagingOptions {
                exclude 'META-INF/services/javax.annotation.processing.Processor'
            }
        }
        project.dependencies {
            // Knight
            compile 'eu.inloop.knight:knight:0.0.4'
            apt 'eu.inloop.knight:knight-compiler:0.0.4'
        }
    }

    @Override
    protected Class getPluginExtension() {
        System.out.println("@ KnightPlugin -> getPluginExtension")
        return KnightExtension.class
    }

    @Override
    protected String getExtension() {
        System.out.println("@ KnightPlugin -> getExtension")
        return "knight"
    }

}
