package com.deer.wms.project.root.core.result;

/**
 * 系统级别的响应码枚举
 * <p>
 * Created by guo on 2018/11/15.
 */
public enum CommonCode implements Code {
    /**
     * 服务器正常接收和处理请求
     */
    SUCCESS(200, "操作成功"),

    /**
     * 严禁在业务流程中使用！服务器内部错误
     */
    SYSTEM_ERROR(500, "服务器内部错误，请联系管理员"),

    /**
     * 业务流程卡住的通用异常！
     */
    SERVICE_ERROR(10002, ""),

    PICK_TASK_CANCEL(1234, "该拣货任务已取消"), PICK_TASK_QUANTITY(12345, "数量已改变，请重新确认！"),

    /** Http请求错误 */
    HTTP_METHOD_ERROR(415, "HTTP方法错误，请选择正确的方法"),

    /** 参数错误 */
    PARAMETER_ERROR(422, "参数错误"),

    /** 上传文件失败 */
    UPLOAD_FILE_ERROR(10099, "上传文件失败：上传的文件过大"),

    ILLEGAL_LANDING(110, "非法登录"),

    /** 需要授权认证的api接口要求请求方提供授权认证的参数值，而请求方没有提供该参数 */
    MISSING_PERMISSION_PARAMTER(10006, "缺少访问令牌参数"),

    /** 但请求方向服务器获取服务器不支持的媒体类型时会出现该错误，比如系统只支持mp4格式，而请求却获取rm格式的数据 */
    TOKEN_INVALID(10007, "访问令牌失效"),

    /** ERP创建入库单 */
    WARE_NULL(1101, "wareCode无效"), ORG_NULL(1102, "orgCode无效"), WARE_INVALID(1103, "wareCode无效"), ORG_INVALID(1104, "orgCode无效"), BILL_NO(1105, "单号重复"), ITEM_CODE(1106, "商品编码重复"),
    SHIP_CODE(1107, "快递单重复"), CANCEL_ACCEPT_CODE(1108, "入库单中已经有商品已经验收，不支持取消"),

    /** 上架车上架 */

    /** 判断名称、编码是否存在 */
    NAME_EXISTS(100001, "系统中已存在相同名称，请重新输入！"), CODE_EXISTS(100002, "系统中已存在相同编码，请重新输入！"), NAME_CODE_NO_EXISTS(100003, "不重复"), EQUALE_EXISTS(100004, "系统中已存在相同，请重新输入！"),
    BATCH_TACTIC_ERROR(120, "不存在该批次属性，请在物料或物料类型或货主中添加批次策略"), UPPER_TACTIC_ERROR(122, "不存在该上架策略，请操作人员自行选择上架货位"), QC_TACTIC_ERROR(126, "不存在该质检策略，请在物料或物料类型或货主中新建质检策略"),
    REQUEST_FILE_ERROR(123, "存在未验收的采购单，无法归档"), ASN_FILE_ERROR(124, "存在未完成的单据，无法归档"), SO_FILE_ERROR(126, "存在未完成的单据，无法归档"), CASE_NULL(125, "此单据未绑定审核流"),
    CANT_DELETE_REVIEW_NODE(100005, "该节点有正在审核的单据，不可删除！"), WRONG_EXCEL_CONTENT(100008, "Excel 内容有问题，无法导入！"), FAIL(611718, "数量不足"), ASN_ERROR(200216, "存在未上架的ASN单据"),
    EXPECT_QUANTITY_ERROR(200229, "预期数量位0，无法生成质检单"), CELL_INVENTORY_ERROR(200220, "该货位没有库存"), FREEZE_ERROR(191226, "冻结数量大于库存余量"), MOVE_ERROR(191227, "转移数量大于库存余量"), INVENTORY_NULL(20191226, "没有该库存"),
    CELL_LOCK(100018, "此货位已锁定"), CELL_ERROR(100058, "此货位已存有库存"), ORGANIZATION_BOUND(100019, "货主已绑定账号"), DEPT_BOUND(100020, "部门已绑定账号"), ROLE_BOUND(100021, "角色已绑定账号"), ACCOUNT_EXISTS(100022, "此账户已存在"),
    ROLE_EXISTS(100023, "此角色已存在"), ORGANIZATION_CODE_EXISTS(100024, "此货主编码已存在"), ORGANIZATION_EXISTS(100025, "此货主已存在"), DEPT_EXISTS(100026, "此部门已存在"), DEPT_CODE_EXISTS(100027, "此部门编码已存在"),
    PACK_CODE_EXISTS(100028, "此包装编码已存在"),
    /** 包装材料 */
    PACK_DATA_EXISTS(100029, ""), CUSTOMER_CODE_EXISTS(100030, "此客户编码已存在"), CUSTOMER_EXISTS(100031, "此客户名称已存在"), ITEM_TYPE_EXISTS(100032, "此物料类型正在使用"), SUPPLIER_EXISTS(100033, "此供应商已存在"),
    SUPPLIER_CODE_EXISTS(100034, "此供应商编码已存在"), CARRIER_EXISTS(100035, "此承运商已存在"), CARRIER_CODE_EXISTS(100036, "此承运商编码已存在"), DATA_DICT_EXISTS(100037, "此字典名称已存在"),
    DATA_DICT_CODE_EXISTS(100038, "此字典编码已存在"), SYSPARAM_EXISTS(100039, "此系统参数已存在"), SYSPARAM_CODE_EXISTS(100040, "此系统参数编码已存在"), AREA_EXISTS(100041, "此货区已存在"), AREA_CODE_EXISTS(100042, "此货区编码已存在"),
    WARE_EXISTS(100043, "此仓库已存在"),
    /* enable=启用；disable=停用 */
    UPPER_TACTIC_DISABLE(100044, "此上架策略已禁用"), ITEM_NO_EXISTS(100045, "此物料未存在"), ITEM_EXISTS(100046, "此物料已存在"), ITEM_TYPE_NO_EXISTS(100047, "此物料类型未存在"),
    PACK_CODE_NO_EXISTS(100048, "此包装编码未存在"), WARE_UPPER_TACTIC_BOUND(100049, "此仓库已绑定上架策略"), AREA_UPPER_TACTIC_BOUND(100050, "此货区已绑定上架策略"), SHELF_CELL_BOUND(100051, "此货位组已绑定货位"),
    WARE_SHIPPADDRESS_BOUND(100052, "此仓库已绑定发货地址"), PICK_TASK_ALREADY_EXIST(100053, "此详情单已存在拣货任务"), KDN_E_ORDER_ERROR(100054, "快递鸟电子面单错误"), KDN_NO_SHIPPING_ADDRESS(100055, "没有合适的发货地址"),
    NO_ROUTE_TACTIC(100056, "没有合适的路线规则"), WAVE_TACTIC_SCHEDULE_ERROR(100057, "波次定时任务错误"), SO_BILL_NO_NOT_EXISTS(100058, "此订单不存在"), ITEM_ORGANIZATION_ERROR(100059, "此物料和货主不匹配"),
    WARE_CELL_ERROR(100060, "此仓库和货位不匹配"), INVENTORY_ERROR(100061, "此单据未生成库存"), NO_SUCH_ITEM_COMBINED(100062, "没有此产品组合"), SEEDING_ERROR(100063, "播种错误"), NEED_DETAILS(100064, "缺少明细"),
    PICK_QUANTITY_ERR(100065, "拣货时库存不足"), INVENTORY_VERSION(100066, "库存数据版本错误"), SO_VERSION(100067, "出库单数据版本错误"), PICK_TASK_VERSION(100067, "拣货数据版本错误"), ASN_VERSION(100068, "出库单数据版本错误"),
    NO_WAVE(100069, "波次无效"),
    SKU_SEND_END(100070,"此SKU已全部完成发运"),
    SKU_SEND_OVER(100071,"此SKU输入产品数量超过下单数量"),
    NO_AUTH(100072, "您当前没有权限！"),
    CODE_NULL(110001, "编码无效"), NOT_DELETE_AREA(110002, "货区下有货位组不允许删除"), NOT_DELETE_SHELF(110003, "货位组下有货位不允许删除"),
    /** 批量波次拣货 **/
    PICK_BATCH_ERROR(201800, "")

    ;

    /**
     * 错误代码
     */
    private int code;

    /**
     * 错误消息
     */
    private String message;

    CommonCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
