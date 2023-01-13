package cn.apecode.blog.utils;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.fill.Property;

import java.util.Collections;
import java.util.Scanner;

/**
 * @description: 代码生成器
 * @author: apecode
 * @date: 2022-05-25 18:24
 **/
@SuppressWarnings("all")
public class CodeGenerator  {

    // 数据库url
    private static final String URL = "";
    // 数据库账号
    private static final String USERNAME = "";
    // 数据库密码
    private static final String PASSWORD = "";
    private static final DataSourceConfig.Builder DATA_SOURCE_CONFIG = new DataSourceConfig.Builder(URL, USERNAME, PASSWORD);
    private static final String projectPath = System.getProperty("user.dir");

    public static void main(String[] args) {
        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                .globalConfig((scanner, builder) -> builder
                        .author(scanner.apply("作者名称："))
                        .enableSwagger()
                        .outputDir(projectPath + "/src/main/java"))
                .packageConfig(builder -> builder.parent("cn.apecode")
                        .moduleName("blog")
                        .entity("entity")
                        .service("service")
                        .serviceImpl("service.impl")
                        .mapper("mapper")
                        .controller("controller")
                        .pathInfo(Collections.singletonMap(OutputFile.mapper, projectPath + "/src/main/java/cn/apecode/blog/mapper/")).build())
                .strategyConfig(builder -> builder.addInclude(scanner("表名，多个英文逗号分割").split(","))
                        .addTablePrefix("t_")
                        .controllerBuilder().enableRestStyle()
                        .mapperBuilder().enableMapperAnnotation().enableBaseResultMap().enableBaseColumnList()
                        .serviceBuilder().formatServiceFileName("%sService").formatServiceImplFileName("%sServiceImpl")
                        .entityBuilder().enableLombok()
                        .enableChainModel()
                        .addTableFills(new Column("createTime", FieldFill.INSERT))
                        .addTableFills(new Property("updateTime", FieldFill.INSERT_UPDATE))
                        .idType(IdType.AUTO)
                        .build())
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }

    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入" + tip + "：");
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

}
