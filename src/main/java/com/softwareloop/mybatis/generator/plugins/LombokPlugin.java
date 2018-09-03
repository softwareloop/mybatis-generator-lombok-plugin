package com.softwareloop.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.*;


/**
 * A MyBatis Generator plugin to use Lombok's annotations.
 * For example, use @Data annotation instead of getter ands setter.
 *
 * @author Paolo Predonzani (http://softwareloop.com/)
 */
public class LombokPlugin extends PluginAdapter {

    private final Collection<Annotations> annotations;

    private final String MAPPER = "mapper";

    /**
     * LombokPlugin constructor
     */
    public LombokPlugin() {
        annotations = new LinkedHashSet<Annotations>(Annotations.values().length);
    }

    /**
     * @param warnings list of warnings
     * @return always true
     */
    public boolean validate(List<String> warnings) {
        return true;
    }

    /**
     * Intercepts base record class generation
     *
     * @param topLevelClass     the generated base record class
     * @param introspectedTable The class containing information about the table as
     *                          introspected from the database
     * @return always true
     */
    @Override
    public boolean modelBaseRecordClassGenerated(
            TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable
    ) {
        addAnnotations(topLevelClass);
        return true;
    }

    /**
     * Intercepts primary key class generation
     *
     * @param topLevelClass     the generated primary key class
     * @param introspectedTable The class containing information about the table as
     *                          introspected from the database
     * @return always true
     */
    @Override
    public boolean modelPrimaryKeyClassGenerated(
            TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable
    ) {
        addAnnotations(topLevelClass);
        return true;
    }

    /**
     * Intercepts "record with blob" class generation
     *
     * @param topLevelClass     the generated record with BLOBs class
     * @param introspectedTable The class containing information about the table as
     *                          introspected from the database
     * @return always true
     */
    @Override
    public boolean modelRecordWithBLOBsClassGenerated(
            TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable
    ) {
        addAnnotations(topLevelClass);
        return true;
    }

    /**
     * Prevents all getters from being generated.
     * See SimpleModelGenerator
     *
     * @param method             the getter, or accessor, method generated for the specified
     *                           column
     * @param topLevelClass      the partially implemented model class
     * @param introspectedColumn The class containing information about the column related
     *                           to this field as introspected from the database
     * @param introspectedTable  The class containing information about the table as
     *                           introspected from the database
     * @param modelClassType     the type of class that the field is generated for
     */
    @Override
    public boolean modelGetterMethodGenerated(
            Method method,
            TopLevelClass topLevelClass,
            IntrospectedColumn introspectedColumn,
            IntrospectedTable introspectedTable,
            ModelClassType modelClassType
    ) {
        return false;
    }

    /**
     * Prevents all setters from being generated
     * See SimpleModelGenerator
     *
     * @param method             the setter, or mutator, method generated for the specified
     *                           column
     * @param topLevelClass      the partially implemented model class
     * @param introspectedColumn The class containing information about the column related
     *                           to this field as introspected from the database
     * @param introspectedTable  The class containing information about the table as
     *                           introspected from the database
     * @param modelClassType     the type of class that the field is generated for
     * @return always false
     */
    @Override
    public boolean modelSetterMethodGenerated(
            Method method,
            TopLevelClass topLevelClass,
            IntrospectedColumn introspectedColumn,
            IntrospectedTable introspectedTable,
            ModelClassType modelClassType
    ) {
        return false;
    }

    /**
     * Adds the lombok annotations' imports and annotations to the class
     *
     * @param topLevelClass the partially implemented model class
     */
    private void addAnnotations(TopLevelClass topLevelClass) {
        for (Annotations annotation : annotations) {
            topLevelClass.addImportedType(annotation.javaType);
            topLevelClass.addAnnotation(annotation.asAnnotation());
        }
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);

        //@Data is default annotation
        annotations.add(Annotations.DATA);

        for (String annotationName : properties.stringPropertyNames()) {
            if (annotationName.contains(".")) {
                // Not an annotation name
                continue;
            }
            String value = properties.getProperty(annotationName);
            if (!Boolean.parseBoolean(value)) {
                // The annotation is disabled, skip it
                continue;
            }
            Annotations annotation = Annotations.getValueOf(annotationName);
            if (annotation == null) {
                continue;
            }
            String optionsPrefix = annotationName + ".";
            for (String propertyName : properties.stringPropertyNames()) {
                if (!propertyName.startsWith(optionsPrefix)) {
                    // A property not related to this annotation
                    continue;
                }
                String propertyValue = properties.getProperty(propertyName);
                annotation.appendOptions(propertyName, propertyValue);
                annotations.add(annotation);
                annotations.addAll(Annotations.getDependencies(annotation));
            }
        }
    }

    @Override
    public boolean clientGenerated(
            Interface interfaze,
            TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable
    ) {
        String excludeMapper = this.properties.getProperty(MAPPER, "true");
        if(Boolean.parseBoolean(excludeMapper)) {
            interfaze.addImportedType(new FullyQualifiedJavaType(
                    "org.apache.ibatis.annotations.Mapper"));
            interfaze.addAnnotation("@Mapper");
        }
        return true;
    }

    private enum Annotations {
        DATA("data", "@Data", "lombok.Data"),
        BUILDER("builder", "@Builder", "lombok.Builder"),
        ALL_ARGS_CONSTRUCTOR("allArgsConstructor", "@AllArgsConstructor", "lombok.AllArgsConstructor"),
        NO_ARGS_CONSTRUCTOR("noArgsConstructor", "@NoArgsConstructor", "lombok.NoArgsConstructor"),
        ACCESSORS("accessors", "@Accessors", "lombok.experimental.Accessors"),
        TO_STRING("toString", "@ToString", "lombok.ToString");


        private final String paramName;
        private final String name;
        private final FullyQualifiedJavaType javaType;
        private final List<String> options;


        Annotations(String paramName, String name, String className) {
            this.paramName = paramName;
            this.name = name;
            this.javaType = new FullyQualifiedJavaType(className);
            this.options = new ArrayList<String>();
        }

        private static Annotations getValueOf(String paramName) {
            for (Annotations annotation : Annotations.values())
                if (String.CASE_INSENSITIVE_ORDER.compare(paramName, annotation.paramName) == 0)
                    return annotation;

            return null;
        }

        private static Collection<Annotations> getDependencies(Annotations annotation) {
            if (annotation == ALL_ARGS_CONSTRUCTOR)
                return Collections.singleton(NO_ARGS_CONSTRUCTOR);
            else
                return Collections.emptyList();
        }

        // A trivial quoting.
        // Because Lombok annotation options type is almost String or boolean.
        private static String quote(String value) {
            if (Boolean.TRUE.toString().equals(value) || Boolean.FALSE.toString().equals(value))
                // case of boolean, not passed as an array.
                return value;
            return value.replaceAll("[\\w]+", "\"$0\"");
        }

        private void appendOptions(String key, String value) {
            String keyPart = key.substring(key.indexOf(".") + 1);
            String valuePart = value.contains(",") ? String.format("{%s}", value) : value;
            this.options.add(String.format("%s=%s", keyPart, quote(valuePart)));
        }

        private String asAnnotation() {
            if (options.isEmpty()) {
                return name;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(name);
            sb.append("(");
            boolean first = true;
            for (String option : options) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                }
                sb.append(option);
            }
            sb.append(")");
            return sb.toString();
        }
    }
}
