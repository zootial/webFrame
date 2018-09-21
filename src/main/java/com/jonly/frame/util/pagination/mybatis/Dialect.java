package com.jonly.frame.util.pagination.mybatis;

public enum Dialect {
	mysql, oracle;

	public static Dialect of(String dialect) {
		try {
			Dialect d = Dialect.valueOf(dialect.toLowerCase());
			return d;
		} catch (IllegalArgumentException e) {
			String dialects = null;
			for (Dialect d : Dialect.values()) {
				if (dialects == null) {
					dialects = d.toString();
				} else {
					dialects += "," + d;
				}
			}
			throw new IllegalArgumentException("options: [" + dialects + "]");
		}
	}

	public static String[] dialects() {
		Dialect[] dialects = Dialect.values();
		String[] ds = new String[dialects.length];
		for (int i = 0; i < dialects.length; i++) {
			ds[i] = dialects[i].toString();
		}
		return ds;
	}

	public static String fromJdbcUrl(String jdbcUrl) {
		String[] dialects = dialects();
		for (String dialect : dialects) {
			if (jdbcUrl.indexOf(":" + dialect + ":") != -1) {
				return dialect;
			}
		}
		return null;
	}
}
