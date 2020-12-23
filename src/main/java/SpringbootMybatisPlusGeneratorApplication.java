//package

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

@SpringBootApplication
public class SpringbootMybatisPlusGeneratorApplication {

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        String projectPath = globalConfig(mpg);
        // 数据源配置
        dataSourceConfig(mpg);
        // 包配置
        PackageConfig pc = getPackageConfig(mpg);
        // 自定义配置
        InjectionConfig cfg = getInjectionConfig();
        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focListAddOutputFile(projectPath, pc, templatePath, focList);
        // setFileCreate(projectPath, cfg);
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        // 配置模板
        templateConfig(mpg);
        // 策略配置
        strategyConfig(mpg, pc);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }
    /**
     *
     *自定义配置会被优先输出
     * @param projectPath
     * @param pc
     * @param templatePath
     * @param focList
     * @author xingzhe
     * @date 2020/5/2 22:27
     * @return
     */
    private static void focListAddOutputFile(final String projectPath, final PackageConfig pc, String templatePath, List<FileOutConfig> focList) {
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
    }

    private static void setFileCreate(final String projectPath, InjectionConfig cfg) {
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir( projectPath + "/src/main/resources/template");
                if (fileType == FileType.MAPPER) {
                    // 已经生成 mapper 文件判断存在，不想重新生成返回 false
                    return !new File(filePath).exists();
                }
                // 允许生成模板文件
                return true;
            }
        });
    }
    /**
     *
     * 自定义配置
     * @author xingzhe
     * @date 2020/5/2 22:26
     * @return
     */
    private static InjectionConfig getInjectionConfig() {
        return new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
    }
    /**
     *
     *配置模板
     * @param mpg
     * @author xingzhe
     * @date 2020/5/2 22:33
     * @return
     */
    private static void templateConfig(AutoGenerator mpg) {
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        // templateConfig.setController();
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);
    }
    /**
     *
     * 策略配置
     * @param mpg
     * @param pc
     * @author xingzhe
     * @date 2020/5/2 22:28
     * @return
     */
    private static void strategyConfig(AutoGenerator mpg, PackageConfig pc) {
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // strategy.setSuperEntityClass("BaseEntity"); // 公共父类实体,没有就不用设置!
        strategy.setEntityLombokModel(true); //是否开启lombok注解
        strategy.setEntityBuilderModel(true);
        strategy.setRestControllerStyle(true);
        //    strategy.setSuperControllerClass("BaseController"); //公共父类控制器,没有就不用设置!
        //    strategy.setSuperEntityColumns("id"); 写于父类中的公共字段
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
    }
    /**
     *
     *包配置
     * @param mpg
     * @author xingzhe
     * @date 2020/5/2 22:25
     * @return
     */
    private static PackageConfig getPackageConfig(AutoGenerator mpg) {
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(scanner("模块名"));
        pc.setParent("com.example.test");
        mpg.setPackageInfo(pc);
        return pc;
    }
    /**
     *
     *数据源配置
     * @param mpg
     * @author xingzhe
     * @date 2020/5/2 22:25
     * @return
     */
    private static void dataSourceConfig(AutoGenerator mpg) {
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:8889/test?&useUnicode=true&characterEncoding=utf8&useSSL=FALSE"
                + "&serverTimezone=Asia/Shanghai");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        mpg.setDataSource(dsc);
    }
    /**
     *全局配置
     * @param mpg
     * @author xingzhe
     * @date 2020/5/2 22:24
     * @return
     */
    private static String globalConfig(AutoGenerator mpg) {
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java"); //生成文件的输出目录
        gc.setAuthor("zhangxiang01");  //作者名称
        gc.setOpen(false);
        gc.setSwagger2(true); //实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);
        return projectPath;
    }

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

}