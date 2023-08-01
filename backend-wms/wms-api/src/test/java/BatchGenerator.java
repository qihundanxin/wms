import com.deer.wms.project.root.generator.Generator;
import com.deer.wms.project.root.generator.configurer.DatasourceConfigurer;
import com.deer.wms.project.root.generator.configurer.Table;

public class BatchGenerator {

    public static void main(String[] args) {
        DatasourceConfigurer configurer = new DatasourceConfigurer();
        //设置数据库连接信息
        configurer.setUrl("jdbc:mysql://sh-cdb-60jldfhk.sql.tencentcdb.com:62802/xhc_wms?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&useSSL=false");
//        configurer.setUrl("jdbc:mysql://localhost:3306/company_product?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&useSSL=false");

        configurer.setUserName("root");
        configurer.setPassword("yyyx0000");
//        configurer.setPassword("1234");
        configurer.setDiverClassName("com.mysql.jdbc.Driver");

        //设置需要生成代码的基础路径，生成的代码都会在该路径下面
        configurer.setBasePackage("com.deer.wms.busine.tactic");
        configurer.setAuthor("");

        //设置子模块名称(如果要生成子模块的代码，一定要设置该值，否则代码将会生成到父项目中了)
        configurer.setModuleName("/wms-busine-tactic");

        //设置需要生成的表

        //configurer.getTables().add(new Table("print_ip",null,"print_ip_id","Integer"));
        configurer.getTables().add(new Table("print_detail",null,"print_detail_id","Integer"));

        //生成代码
        Generator.genCode(configurer);
    }
}
