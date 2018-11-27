package com.jonly.test.util;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class DataTableStructure {

    private String userId;
    private String password;
    private String connectionURL;
    private String driverClass;
    private Properties otherProperties;
    private JavaTypeResolver javaTypeResolver;

    public static void main(String[] args) throws Exception {
        DataTableStructure dts = new DataTableStructure();
        dts.javaTypeResolver = new JavaTypeResolver();
        dts.connectionURL = "jdbc:mysql://172.18.0.202:3306/fgw?useUnicode=true&amp;characterEncoding=utf-8";
        dts.driverClass = "com.mysql.jdbc.Driver";
        dts.userId = "fgw";
        dts.password = "changxin";
        
        PrintStream out = new PrintStream(new FileOutputStream("d:\\peptest2.txt"), true);
        System.setOut(out);
        
        List<String> list = Arrays.asList(
//                "PE_ADVERTISE（广告表）",
//                "PE_APPOINT_ORDER（预约单表）",
                "PE_CARDS_COMMODITY（体检卡表）"
//                ,
//                "PE_CARDS_ITEMS（体检卡关联体检项目表）",
//                "PE_CARD_ORG（体检卡适用体检机构表）",
//                "PE_CARD_SEND（企业体检卡分发记表表）",
//                "PE_COMMODITY_GROUND（商品上架表）",
//                "PE_COMPANY（企业表）",
//                "PE_COMPANY_DEPT（企业关联部门表）",
//                "PE_COMPANY_PLAN（企业体检计划表）",
//                "PE_COM_PACKAGE_PLAN（企业套餐预约计划表）",
//                "PE_COUPON（优惠券表）",
//                "PE_EMPLOYEE（员工表）",
//                "PE_EVALUATE（个人评价表）",
//                "PE_GROUND_FOLLOW（商品上架记录表）",
//                "PE_INVOICE（发票表）",
//                "PE_ITEMS（体检项目表）",
//                "PE_MERCHANT_ORG（体检机构关联商户表）",
//                "PE_ORDER（订单表）",
//                "PE_ORDER_CHANGE（订单状态变更日志表）",
//                "PE_ORDER_COMMODITY（订单明细表）",
//                "PE_ORGANZATION（体检机构表）",
//                "PE_ORG_COUPON（机构关联优惠券表）",
//                "PE_ORG_PACKAGE（机构关联体检套餐表）",
//                "PE_ORG_PLATFORM（体检机构关联支付平台表）",
//                "PE_PACKAGE_ITEMS（体检套餐关联体检项目表）",
//                "PE_PERSONAL_CARD（个人体检卡表）",
//                "PE_PERSONAL_CARD_TRACK（个人体检卡日志表）",
//                "PE_PERSONAL_CODE（个人体检码表）",
//                "PE_PERSONAL_COLLECTION（个人收藏表）",
//                "PE_PERSONAL_COUPON（个人优惠券表）",
//                "PE_SERVICE_INVOKE（支付接口日志表）",
//                "PE_SHOPPING_CART（个人收物车表）",
//                "PE_TRANSACTION_FLOW（交易流水表）",
//                "PE_REPORT_DETAIL（体检报告项目明细表）",
//                "PE_REPORT_GROUP（体检报告项目分组表）",
//                "PE_REPORT_HOMEPAGE（体检报告主表）",
//                "CMH_PAY_PLATFORM（支付平台表）",
//                "CMH_PAY_MERCHANT（支付平台商户表）",
//                "CMH_BILL_MAIN（对账单主表）",
//                "CMH_BILL_DETAIL（对账单明细表）",
//                "CMH_BILL_RESULT_MAIN（对账结果主表）",
//                "CMH_BILL_RESULT_DETAIL（对账结果明细表）",
//                "CBF_USER（用户表）",
//                "CBF_PORTAL_USER_EXTEND（公众门户用户扩展表）",
//                "CBF_PORTAL_USER_FAMILY（公众门户用户家属关联表）"
                );
        for(String t : list) {
            dts.print(t);
        }
    }
    
    private void print(String tableName) throws Exception {
        String localCatalog = "";
        String localSchema = "";
        String localTableName = tableName.replaceFirst("（.*）", "");

        List<Column> list = this.getColumns(this.getConnection(), localCatalog, localSchema, localTableName);
        int i = 1;
        System.out.println();
        System.out.println(tableName);
        for (Column col : list) {
            StringBuilder str = new StringBuilder();
            str.append(i++).append("\t").append(col.getActualColumnName());
            if(col.getLength() > 0) {
                str.append("\t").append(col.getJdbcTypeName()).append("(").append(col.getLength());
                if(col.getScale() > 0) {
                    str.append(",").append(col.getScale());
                }
                str.append(")");
            }
            str.append("\t").append(col.getJdbcTypeName()).append("\t").append(col.getLength());
            str.append("\t").append(col.isNullable() ? "" : "NO");
            str.append("\t").append(col.getDefaultValue() == null ? " " : col.getDefaultValue());
            str.append("\t").append(col.getRemarks().replaceAll("\n", " "));

            System.out.println(str);
        }
    }

    public List<Column> getColumns(Connection con, String localCatalog, String localSchema, String localTableName)
            throws Exception {
        ResultSet rs = con.getMetaData().getColumns(localCatalog, localSchema, localTableName, "%");

        boolean supportsIsAutoIncrement = false;
        boolean supportsIsGeneratedColumn = false;
        ResultSetMetaData rsmd = rs.getMetaData();
        int colCount = rsmd.getColumnCount();
        for (int i = 1; i <= colCount; i++) {
            if ("IS_AUTOINCREMENT".equals(rsmd.getColumnName(i))) {
                supportsIsAutoIncrement = true;
            }
            if ("IS_GENERATEDCOLUMN".equals(rsmd.getColumnName(i))) {
                supportsIsGeneratedColumn = true;
            }
        }

        List<Column> cols = new ArrayList<Column>();

        while (rs.next()) {
            Column introspectedColumn = new Column();

            introspectedColumn.setJdbcType(rs.getInt("DATA_TYPE"));
            introspectedColumn
                    .setJdbcTypeName(javaTypeResolver.calculateJdbcTypeName(introspectedColumn.getJdbcType()));
            introspectedColumn.setLength(rs.getInt("COLUMN_SIZE"));
            introspectedColumn.setActualColumnName(rs.getString("COLUMN_NAME"));
            introspectedColumn.setNullable(rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable);
            introspectedColumn.setScale(rs.getInt("DECIMAL_DIGITS"));
            introspectedColumn.setRemarks(rs.getString("REMARKS"));
            introspectedColumn.setDefaultValue(rs.getString("COLUMN_DEF"));

            if (supportsIsAutoIncrement) {
                introspectedColumn.setAutoIncrement("YES".equals(rs.getString("IS_AUTOINCREMENT")));
            }

            if (supportsIsGeneratedColumn) {
                introspectedColumn.setGeneratedColumn("YES".equals(rs.getString("IS_GENERATEDCOLUMN")));
            }
            cols.add(introspectedColumn);
        }

        closeResultSet(rs);

        return cols;
    }

    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }

    public Connection getConnection() throws SQLException {

        Properties props = new Properties();

        if (stringHasValue(userId)) {
            props.setProperty("user", userId);
        }

        if (stringHasValue(password)) {
            props.setProperty("password", password);
        }

        if(otherProperties != null) {
            props.putAll(otherProperties);
        }

        Driver driver = getDriver();
        Connection conn = driver.connect(connectionURL, props);

        if (conn == null) {
            throw new SQLException(getString("RuntimeError.7"));
        }

        return conn;
    }

    private Driver getDriver() {
        Driver driver;

        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            Class<?> clazz = Class.forName(driverClass, true, cl);
            driver = (Driver) clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(getString("RuntimeError.8"), e);
        }

        return driver;
    }

    public static class Column {
        protected String actualColumnName;

        protected int jdbcType;

        protected String jdbcTypeName;

        protected boolean nullable;

        protected int length;

        protected int scale;

        protected boolean identity;

        protected boolean isSequenceColumn;

        protected String tableAlias;

        protected boolean isColumnNameDelimited;

        // any database comment associated with this column. May be null
        protected String remarks;

        protected String defaultValue;

        /**
         * true if the JDBC driver reports that this column is auto-increment.
         */
        protected boolean isAutoIncrement;

        /**
         * true if the JDBC driver reports that this column is generated.
         */
        protected boolean isGeneratedColumn;

        /**
         * True if there is a column override that defines this column as
         * GENERATED ALWAYS.
         */
        protected boolean isGeneratedAlways;

        public String getActualColumnName() {
            return actualColumnName;
        }

        public void setActualColumnName(String actualColumnName) {
            this.actualColumnName = actualColumnName;
        }

        public int getJdbcType() {
            return jdbcType;
        }

        public void setJdbcType(int jdbcType) {
            this.jdbcType = jdbcType;
        }

        public String getJdbcTypeName() {
            return jdbcTypeName;
        }

        public void setJdbcTypeName(String jdbcTypeName) {
            this.jdbcTypeName = jdbcTypeName;
        }

        public boolean isNullable() {
            return nullable;
        }

        public void setNullable(boolean nullable) {
            this.nullable = nullable;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public int getScale() {
            return scale;
        }

        public void setScale(int scale) {
            this.scale = scale;
        }

        public boolean isIdentity() {
            return identity;
        }

        public void setIdentity(boolean identity) {
            this.identity = identity;
        }

        public boolean isSequenceColumn() {
            return isSequenceColumn;
        }

        public void setSequenceColumn(boolean isSequenceColumn) {
            this.isSequenceColumn = isSequenceColumn;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public boolean isAutoIncrement() {
            return isAutoIncrement;
        }

        public void setAutoIncrement(boolean isAutoIncrement) {
            this.isAutoIncrement = isAutoIncrement;
        }

        public boolean isGeneratedColumn() {
            return isGeneratedColumn;
        }

        public void setGeneratedColumn(boolean isGeneratedColumn) {
            this.isGeneratedColumn = isGeneratedColumn;
        }

        public boolean isGeneratedAlways() {
            return isGeneratedAlways;
        }

        public void setGeneratedAlways(boolean isGeneratedAlways) {
            this.isGeneratedAlways = isGeneratedAlways;
        }

    }

    public static class JavaTypeResolver {
        protected Map<Integer, JdbcTypeInformation> typeMap;

        public JavaTypeResolver() {
            typeMap = new HashMap<Integer, JdbcTypeInformation>();

            typeMap.put(Types.ARRAY, new JdbcTypeInformation("ARRAY", //$NON-NLS-1$
                    Object.class));
            typeMap.put(Types.BIGINT, new JdbcTypeInformation("BIGINT", //$NON-NLS-1$
                    Long.class));
            typeMap.put(Types.BINARY, new JdbcTypeInformation("BINARY", //$NON-NLS-1$
                    byte[].class)); // $NON-NLS-1$
            typeMap.put(Types.BIT, new JdbcTypeInformation("BIT", //$NON-NLS-1$
                    Boolean.class));
            typeMap.put(Types.BLOB, new JdbcTypeInformation("BLOB", //$NON-NLS-1$
                    byte[].class)); // $NON-NLS-1$
            typeMap.put(Types.BOOLEAN, new JdbcTypeInformation("BOOLEAN", //$NON-NLS-1$
                    Boolean.class));
            typeMap.put(Types.CHAR, new JdbcTypeInformation("CHAR", //$NON-NLS-1$
                    String.class));
            typeMap.put(Types.CLOB, new JdbcTypeInformation("CLOB", //$NON-NLS-1$
                    String.class));
            typeMap.put(Types.DATALINK, new JdbcTypeInformation("DATALINK", //$NON-NLS-1$
                    Object.class));
            typeMap.put(Types.DATE, new JdbcTypeInformation("DATE", //$NON-NLS-1$
                    Date.class));
            typeMap.put(Types.DECIMAL, new JdbcTypeInformation("DECIMAL", //$NON-NLS-1$
                    BigDecimal.class));
            typeMap.put(Types.DISTINCT, new JdbcTypeInformation("DISTINCT", //$NON-NLS-1$
                    Object.class));
            typeMap.put(Types.DOUBLE, new JdbcTypeInformation("DOUBLE", //$NON-NLS-1$
                    Double.class));
            typeMap.put(Types.FLOAT, new JdbcTypeInformation("FLOAT", //$NON-NLS-1$
                    Double.class));
            typeMap.put(Types.INTEGER, new JdbcTypeInformation("INTEGER", //$NON-NLS-1$
                    Integer.class));
            typeMap.put(Types.JAVA_OBJECT, new JdbcTypeInformation("JAVA_OBJECT", //$NON-NLS-1$
                    Object.class));
            typeMap.put(Types.LONGNVARCHAR, new JdbcTypeInformation("LONGNVARCHAR", //$NON-NLS-1$
                    String.class));
            typeMap.put(Types.LONGVARBINARY, new JdbcTypeInformation("LONGVARBINARY", //$NON-NLS-1$
                    byte[].class)); // $NON-NLS-1$
            typeMap.put(Types.LONGVARCHAR, new JdbcTypeInformation("LONGVARCHAR", //$NON-NLS-1$
                    String.class));
            typeMap.put(Types.NCHAR, new JdbcTypeInformation("NCHAR", //$NON-NLS-1$
                    String.class));
            typeMap.put(Types.NCLOB, new JdbcTypeInformation("NCLOB", //$NON-NLS-1$
                    String.class));
            typeMap.put(Types.NVARCHAR, new JdbcTypeInformation("NVARCHAR", //$NON-NLS-1$
                    String.class));
            typeMap.put(Types.NULL, new JdbcTypeInformation("NULL", //$NON-NLS-1$
                    Object.class));
            typeMap.put(Types.NUMERIC, new JdbcTypeInformation("NUMERIC", //$NON-NLS-1$
                    BigDecimal.class));
            typeMap.put(Types.OTHER, new JdbcTypeInformation("OTHER", //$NON-NLS-1$
                    Object.class));
            typeMap.put(Types.REAL, new JdbcTypeInformation("REAL", //$NON-NLS-1$
                    Float.class));
            typeMap.put(Types.REF, new JdbcTypeInformation("REF", //$NON-NLS-1$
                    Object.class));
            typeMap.put(Types.SMALLINT, new JdbcTypeInformation("SMALLINT", //$NON-NLS-1$
                    Short.class));
            typeMap.put(Types.STRUCT, new JdbcTypeInformation("STRUCT", //$NON-NLS-1$
                    Object.class));
            typeMap.put(Types.TIME, new JdbcTypeInformation("TIME", //$NON-NLS-1$
                    Date.class));
            typeMap.put(Types.TIMESTAMP, new JdbcTypeInformation("TIMESTAMP", //$NON-NLS-1$
                    Date.class));
            typeMap.put(Types.TINYINT, new JdbcTypeInformation("TINYINT", //$NON-NLS-1$
                    Byte.class));
            typeMap.put(Types.VARBINARY, new JdbcTypeInformation("VARBINARY", //$NON-NLS-1$
                    byte[].class)); // $NON-NLS-1$
            typeMap.put(Types.VARCHAR, new JdbcTypeInformation("VARCHAR", //$NON-NLS-1$
                    String.class));
        }

        public String calculateJdbcTypeName(int jdbcType) {
            String answer = null;
            JdbcTypeInformation jdbcTypeInformation = typeMap.get(jdbcType);

            if (jdbcTypeInformation != null) {
                answer = jdbcTypeInformation.getJdbcTypeName();
            }

            return answer;
        }
    }

    public static class JdbcTypeInformation {
        private String jdbcTypeName;

        private Class<?> javaType;

        public JdbcTypeInformation(String jdbcTypeName, Class<?> javaType) {
            this.jdbcTypeName = jdbcTypeName;
            this.javaType = javaType;
        }

        public String getJdbcTypeName() {
            return jdbcTypeName;
        }

        public Class<?> getJavaType() {
            return javaType;
        }
    }
}
