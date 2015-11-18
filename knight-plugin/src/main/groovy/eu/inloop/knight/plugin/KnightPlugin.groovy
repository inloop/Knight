package eu.inloop.knight.plugin

import eu.inloop.knight.plugin.util.AWeavingPlugin
import eu.inloop.knight.weaving.KnightWeaver
import eu.inloop.knight.weaving.util.IWeaver
import org.gradle.api.Project

/**
 * Class {@link KnightPlugin} adds dependencies to gradle project and applies Bytecode Weaving.
 *
 * @author FrantisekGazo
 * @version 2015-11-09
 */
public class KnightPlugin extends AWeavingPlugin {

    public static final String NAME = "knight"

    @Override
    public IWeaver[] getTransformers(Project project) {
        return [
                new KnightWeaver(/* add extension params if necessary */)
        ]
    }

    @Override
    protected void configure(Project project) {
        project.android {
            packagingOptions {
                exclude 'META-INF/services/javax.annotation.processing.Processor'
            }
        }
        project.dependencies {
            // Dagger
            apt 'com.google.dagger:dagger-compiler:2.0'
            // Knight
            compile 'eu.inloop.knight:knight:0.0.5'
            apt 'eu.inloop.knight:knight-compiler:0.0.5'
        }
    }

    @Override
    protected Class getPluginExtension() {
        return KnightExtension.class
    }

    @Override
    protected String getExtension() {
        return NAME
    }

}
