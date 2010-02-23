package test.com.pyxis.petstore.view;

import static com.pyxis.petstore.controller.ItemsController.MATCHING_ITEMS_KEY;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.apache.commons.lang.StringUtils.isEmpty;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;

import com.pyxis.petstore.domain.Item;

public class VelocityRendering {

	private static final String VELOCITY_EXTENSION = ".vm";

	private static VelocityEngine velocityEngine;

	private final String template;

	private String encoding = "UTF-8";

	private VelocityRendering(String template) {
		this.template = template;
	}

	public static VelocityRendering render(String template) {
		if (velocityEngine == null)
			loadVelocityEngine();
		return new VelocityRendering(template);
	}

	private static void loadVelocityEngine() {
		try {
			VelocityConfigurer velocityConfigurer = new VelocityConfigurer();
			Properties viewsProperties = new Properties();
			viewsProperties.load(SearchItemViewTest.class.getResourceAsStream("/views.properties"));
			String templatesBaseUrl = viewsProperties.getProperty("templates.base.url");
			velocityConfigurer.setResourceLoaderPath(templatesBaseUrl);
			velocityConfigurer.afterPropertiesSet();
			velocityEngine = velocityConfigurer.getVelocityEngine();
		} catch (Exception e) {
			// TODO use ExceptionImposter when available
			throw new RuntimeException(e);
		}
	}

	public void withEncoding(String encoding) {
		if(isEmpty(encoding))
			throw new IllegalArgumentException("Invalid encoding: " + encoding);
		this.encoding = encoding;
	}

	public String using(Map<String, Object> model) {		
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateFileName(), this.encoding, model);
	}

	private String templateFileName() {
		return template + VELOCITY_EXTENSION;
	}

}