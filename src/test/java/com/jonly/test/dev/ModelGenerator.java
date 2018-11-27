package com.jonly.test.dev;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

public class ModelGenerator {

	public static void main(String[] args) {
		try {
			
			List<String> warnings = new ArrayList<String>();
			boolean overwrite = true;
			File confFile = new File(ModelGenerator.class.getResource("conf/generatorConfig.xml").getFile());
			
			ConfigurationParser cp = new ConfigurationParser(warnings);
			Configuration config = cp.parseConfiguration(confFile);
			DefaultShellCallback callback = new DefaultShellCallback(overwrite);
			MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
			myBatisGenerator.generate(null);
			
			for (String warning:warnings){
				System.out.println("warning: "+warning);
			}
			
			System.out.println(confFile + " finish");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
    public InputStream openFile(String fileName) {
    	return this.getClass().getClassLoader().getResourceAsStream(fileName);
    }

}
