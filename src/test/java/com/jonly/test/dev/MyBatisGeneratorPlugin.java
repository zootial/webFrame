package com.jonly.test.dev;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

import com.jonly.frame.dao.IDao;
import com.jonly.frame.util.pagination.Page;

/**
 *
 * @author jonly
 * @version 1.00
 *
 */
public class MyBatisGeneratorPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String sqlParamClass = properties.getProperty("sqlParamClass");
        if (stringHasValue(sqlParamClass)) {
            return false;
        }
        return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        String sqlParamClass = properties.getProperty("sqlParamClass");
        if (stringHasValue(sqlParamClass)) {
            introspectedTable.setExampleType(sqlParamClass);
        }
        introspectedTable.setCountByExampleStatementId("count");
        introspectedTable.setDeleteByExampleStatementId("delete");
        introspectedTable.setDeleteByPrimaryKeyStatementId("deleteById");
        introspectedTable.setInsertStatementId("insert");
        introspectedTable.setInsertSelectiveStatementId("insertSelective");
        introspectedTable.setSelectAllStatementId("selectAll");
        introspectedTable.setSelectByExampleStatementId("select");
        introspectedTable.setSelectByExampleWithBLOBsStatementId("selectWithBLOBs"); //
        introspectedTable.setSelectByPrimaryKeyStatementId("selectById");
        introspectedTable.setUpdateByExampleStatementId("update");
        introspectedTable.setUpdateByExampleSelectiveStatementId("updateSelective");
        introspectedTable.setUpdateByExampleWithBLOBsStatementId("updateWithBLOBs"); //
        introspectedTable.setUpdateByPrimaryKeyStatementId("updateById");
        introspectedTable.setUpdateByPrimaryKeySelectiveStatementId("updateByIdSelective");
        introspectedTable.setUpdateByPrimaryKeyWithBLOBsStatementId("updateByIdWithBLOBs"); //

        introspectedTable.setExampleWhereClauseId("ParamWhereClause");
        introspectedTable.setMyBatis3UpdateByExampleWhereClauseId("MapParamWhereClause");
        super.initialized(introspectedTable);
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType domainType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        interfaze.addImportedType(domainType);
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(IDao.class.getSimpleName());
        fqjt.addTypeArgument(domainType);
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
        if (method.getParameters().size() == 1) {
            Parameter param = method.getParameters().get(0);
            param.addAnnotation("@Param(\"example\")");
        }
        method.addParameter(new Parameter(type, "page", "@Param(\"page\")"));
        return super.clientSelectByExampleWithBLOBsMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean sqlMapExampleWhereClauseElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        Attribute id = null;
        if (element.getAttributes().size() > 0) {
            id = element.getAttributes().get(0);
        } else {
            id = new Attribute("id", introspectedTable.getExampleWhereClauseId());
        }


        element.getElements().clear();
        element.getAttributes().clear();

        element.addAttribute(id);

        XmlElement dynamicElement = new XmlElement("where");
        element.addElement(dynamicElement);

        
        boolean appendPrefix = true;
        if (introspectedTable.getExampleWhereClauseId().equals(id.getValue())) {
            appendPrefix = false;
        }
        StringBuilder prefixBuild = new StringBuilder();
        String alias = properties.getProperty("sqlParamAlias");
        if (appendPrefix) {
            prefixBuild.append("example.");
        }
        if (stringHasValue(properties.getProperty("sqlParamClass")) && stringHasValue(alias)) {
            prefixBuild.append(alias).append(".");
        }
        String prefix = prefixBuild.toString();
        
        StringBuilder sb = new StringBuilder();
        for (IntrospectedColumn introspectedColumn : ListUtilities
                .removeGeneratedAlwaysColumns(introspectedTable.getAllColumns())) {
            sb.setLength(0);
            sb.append(introspectedColumn.getJavaProperty(prefix));
            sb.append(" != null");
            XmlElement isNotNullElement = new XmlElement("if");
            isNotNullElement.addAttribute(new Attribute("test", sb.toString()));
            dynamicElement.addElement(isNotNullElement);

            sb.setLength(0);
            sb.append(" and ");
            if(appendPrefix) {
                sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
            } else {
                sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            }
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, prefix));

            isNotNullElement.addElement(new TextElement(sb.toString()));
        }
        return true;
    }

    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        XmlElement root = document.getRootElement();
        document.setRootElement(proxy(root));
        return true;
    }

    private XmlElement proxy(XmlElement src) {
        XmlElement ret = src;
        List<Element> newElements = new ArrayList<Element>(src.getElements().size());
        for (Element e : src.getElements()) {
            if (e instanceof XmlElement) {
                newElements.add(proxy((XmlElement) e));
            } else {
                newElements.add(e);
            }
        }
        src.getElements().clear();
        src.getElements().addAll(newElements);
        if ("if".equalsIgnoreCase(src.getName())) {
            ret = new IfXmlElement(src);
        }
        return ret;
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
        answer.addElement(getExampleIncludeElement(introspectedTable,
                introspectedTable.getMyBatis3UpdateByExampleWhereClauseId()));

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
        answer.addElement(getExampleIncludeElement(introspectedTable,
                introspectedTable.getMyBatis3UpdateByExampleWhereClauseId()));

        return true;
    }

    private XmlElement getBaseColumnListElement(IntrospectedTable introspectedTable) {
        XmlElement answer = new XmlElement("include");
        answer.addAttribute(new Attribute("refid", introspectedTable.getBaseColumnListId()));
        return answer;
    }

    private XmlElement getBlobColumnListElement(IntrospectedTable introspectedTable) {
        XmlElement answer = new XmlElement("include");
        answer.addAttribute(new Attribute("refid", introspectedTable.getBlobColumnListId()));
        return answer;
    }

    private XmlElement getExampleIncludeElement(IntrospectedTable introspectedTable, String id) {
        XmlElement ifElement = new XmlElement("if");
        ifElement.addAttribute(new Attribute("test", "_parameter != null"));

        XmlElement includeElement = new XmlElement("include");
        includeElement.addAttribute(new Attribute("refid", id));
        ifElement.addElement(includeElement);

        return ifElement;
    }

    public static class IfXmlElement extends XmlElement {
        public IfXmlElement() {
            super("if");
        }

        public IfXmlElement(XmlElement original) {
            super(original);
        }

        @Override
        public String getFormattedContent(int indentLevel) {
            StringBuilder sb = new StringBuilder();

            OutputUtilities.xmlIndent(sb, indentLevel);
            sb.append('<');
            sb.append(super.getName());

            Collections.sort(super.getAttributes());
            for (Attribute att : super.getAttributes()) {
                sb.append(' ');
                sb.append(att.getFormattedContent());
            }

            int count = super.getElements().size();
            if (count > 0) {
                sb.append(">");
                for (Element element : super.getElements()) {
                    if (count > 1) {
                        OutputUtilities.newLine(sb);
                    }
                    sb.append(element.getFormattedContent((count > 1 ? indentLevel : 0) + 1));
                }
                if (count > 1) {
                    OutputUtilities.newLine(sb);
                }
                OutputUtilities.xmlIndent(sb, (count > 1 ? indentLevel : 1));
                sb.append("</");
                sb.append(super.getName());
                sb.append('>');

            } else {
                sb.append(" />");
            }

            return sb.toString();
        }
    }
}
