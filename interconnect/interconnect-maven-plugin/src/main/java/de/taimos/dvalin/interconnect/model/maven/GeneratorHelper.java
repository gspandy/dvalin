package de.taimos.dvalin.interconnect.model.maven;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Properties;

import javax.xml.bind.JAXBContext;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class GeneratorHelper {

	static void writeFile(Log log, VelocityContext context, String templateName, String pkgName, String clazzName, String outputDirectory, String targetDir) throws MojoExecutionException {
		Template template = null;
		try {
			template = Velocity.getTemplate(templateName, "UTF-8");
		} catch (Exception e) {
			log.error("Failed to retrieve Template " + templateName, e);
			throw new MojoExecutionException("Failed to retrieve Template " + templateName, e);
		}

		try {
			File pckDir = new File(outputDirectory + targetDir + pkgName.replace('.', '/'));
			if (!pckDir.exists()) {
				pckDir.mkdirs();
			}
			try (OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(outputDirectory + targetDir + pkgName.replace('.', '/') + "/" + clazzName + ".java"), "UTF-8")) {
				template.merge(context, fw);
			}
		} catch (IOException e) {
			log.error("Failed to write the generated file " + clazzName, e);
			throw new MojoExecutionException("Failed to write the generated file " + clazzName, e);
		}
	}

	static Properties getDefaultProps() {
		Properties props = new Properties();
		props.put("resource.loader", "class");
		props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		return props;
	}

	@SuppressWarnings("unchecked")
	static <T> T parseXML(Class<T> clazz, Log log, File f) throws MojoExecutionException {
		try {
			JAXBContext jcontext = JAXBContext.newInstance(clazz);
			return (T) jcontext.createUnmarshaller().unmarshal(f);
		} catch (Exception e) {
			log.error("Failed to read input file " + f.getAbsolutePath(), e);
			throw new MojoExecutionException("Failed to read input file " + f.getAbsolutePath(), e);
		}
	}
}