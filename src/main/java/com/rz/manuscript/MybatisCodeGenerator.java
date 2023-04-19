package com.rz.manuscript;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class MybatisCodeGenerator {
    public static void main(String[] args) {
        FastAutoGenerator root = FastAutoGenerator.create("jdbc:mysql://182.61.26.201:3306/manuscript?useSSL=false&allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull",
                "root", "Snow2021$");

        root.globalConfig(builder -> {
                    builder.author("baomidou") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("D://manuscript"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.rz.manuscript") // 设置父包名
                            .moduleName("") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.entity, "D://manuscript")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("menu,user_menu_default,user_menu_mapping") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_","crm_"); // 设置过滤表前缀
                    builder.entityBuilder().enableLombok();
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
