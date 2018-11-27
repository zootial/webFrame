package com.jonly.test.dev;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import com.jonly.frame.dao.IDao;
import com.jonly.frame.util.pagination.Page;

/**
 *
 * @author jonly
 * @version 1.00
 *
 */
public class MyBatisGenPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        String examplePackage = properties.getProperty("examplePackage");
        if (stringHasValue(examplePackage)) {
            StringBuilder sb = new StringBuilder();
            sb.append(examplePackage);
            sb.append('.');
            sb.append(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
            sb.append("Example");
            introspectedTable.setExampleType(sb.toString());
        }
        super.initialized(introspectedTable);
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType domainType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        FullyQualifiedJavaType exampleType = new FullyQualifiedJavaType(introspectedTable.getExampleType());
        interfaze.addImportedType(domainType);
        interfaze.addImportedType(exampleType);
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(IDao.class.getSimpleName());
        fqjt.addTypeArgument(domainType);
        fqjt.addTypeArgument(exampleType);
        interfaze.addSuperInterface(fqjt);
        interfaze.addImportedType(new FullyQualifiedJavaType(IDao.class.getName()));
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

    @Override
    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        return this.handleClient(method, interfaze, introspectedTable);
    }

    @Override
    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        return this.handleClient(method, interfaze, introspectedTable);
    }

    boolean handleClient(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        interfaze.addImportedType(new FullyQualifiedJavaType(Page.class.getName()));
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(Page.class.getSimpleName());
        type.addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
        if (method.getParameters().size() == 1) {
            Parameter param = method.getParameters().get(0);
            param.addAnnotation("@Param(\"example\")");
        }
        method.addParameter(new Parameter(type, "page", "@Param(\"page\")"));
        return super.clientSelectByExampleWithBLOBsMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        element.getElements().clear();
        element.getAttributes().clear();

        XmlElement answer = element;

        answer.addAttribute(new Attribute("id", introspectedTable.getSelectByExampleStatementId()));
        answer.addAttribute(new Attribute("resultMap", introspectedTable.getBaseResultMapId()));
        answer.addAttribute(new Attribute("parameterType", "map"));

        context.getCommentGenerator().addComment(answer);

        answer.addElement(new TextElement("select"));
        XmlElement ifElement = new XmlElement("if");
        ifElement.addAttribute(new Attribute("test", "example.distinct")); //$NON-NLS-2$
        ifElement.addElement(new TextElement("distinct"));
        answer.addElement(ifElement);

        StringBuilder sb = new StringBuilder();
        if (stringHasValue(introspectedTable.getSelectByExampleQueryId())) {
            sb.append('\'');
            sb.append(introspectedTable.getSelectByExampleQueryId());
            sb.append("' as QUERYID,");
            answer.addElement(new TextElement(sb.toString()));
        }
        answer.addElement(getBaseColumnListElement(introspectedTable));

        sb.setLength(0);
        sb.append("from ");
        sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        answer.addElement(getUpdateByExampleIncludeElement(introspectedTable));

        ifElement = new XmlElement("if");
        ifElement.addAttribute(new Attribute("test", "example.orderByClause != null"));
        ifElement.addElement(new TextElement("order by ${example.orderByClause}"));
        answer.addElement(ifElement);
        return true;
    }

    @Override
    public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        element.getElements().clear();
        element.getAttributes().clear();

        XmlElement answer = element;
        answer.addAttribute(new Attribute("id", introspectedTable.getSelectByExampleWithBLOBsStatementId()));
        answer.addAttribute(new Attribute("resultMap", introspectedTable.getResultMapWithBLOBsId()));
        answer.addAttribute(new Attribute("parameterType", "map"));

        context.getCommentGenerator().addComment(answer);

        answer.addElement(new TextElement("select"));
        XmlElement ifElement = new XmlElement("if");
        ifElement.addAttribute(new Attribute("test", "example.distinct"));
        ifElement.addElement(new TextElement("distinct"));
        answer.addElement(ifElement);

        StringBuilder sb = new StringBuilder();
        if (stringHasValue(introspectedTable.getSelectByExampleQueryId())) {
            sb.append('\'');
            sb.append(introspectedTable.getSelectByExampleQueryId());
            sb.append("' as QUERYID,");
            answer.addElement(new TextElement(sb.toString()));
        }

        answer.addElement(getBaseColumnListElement(introspectedTable));
        answer.addElement(new TextElement(","));
        answer.addElement(getBlobColumnListElement(introspectedTable));

        sb.setLength(0);
        sb.append("from ");
        sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        answer.addElement(getUpdateByExampleIncludeElement(introspectedTable));

        ifElement = new XmlElement("if");
        ifElement.addAttribute(new Attribute("test", "example.orderByClause != null"));
        ifElement.addElement(new TextElement("order by ${example.orderByClause}"));
        answer.addElement(ifElement);
        return true;
    }

    protected XmlElement getBaseColumnListElement(IntrospectedTable introspectedTable) {
        XmlElement answer = new XmlElement("include");
        answer.addAttribute(new Attribute("refid", introspectedTable.getBaseColumnListId()));
        return answer;
    }

    protected XmlElement getBlobColumnListElement(IntrospectedTable introspectedTable) {
        XmlElement answer = new XmlElement("include");
        answer.addAttribute(new Attribute("refid", introspectedTable.getBlobColumnListId()));
        return answer;
    }

    protected XmlElement getExampleIncludeElement(IntrospectedTable introspectedTable) {
        XmlElement ifElement = new XmlElement("if");
        ifElement.addAttribute(new Attribute("test", "_parameter != null"));

        XmlElement includeElement = new XmlElement("include");
        includeElement.addAttribute(new Attribute("refid", introspectedTable.getExampleWhereClauseId()));
        ifElement.addElement(includeElement);

        return ifElement;
    }

    protected XmlElement getUpdateByExampleIncludeElement(IntrospectedTable introspectedTable) {
        XmlElement ifElement = new XmlElement("if");
        ifElement.addAttribute(new Attribute("test", "_parameter != null")); //$NON-NLS-2$

        XmlElement includeElement = new XmlElement("include");
        includeElement
                .addAttribute(new Attribute("refid", introspectedTable.getMyBatis3UpdateByExampleWhereClauseId()));
        ifElement.addElement(includeElement);

        return ifElement;
    }
}
