export const m = {
  head: {
    ware: 'TASK',
    base: 'BASE',
    report: 'REPORT',
    welcome: '欢迎您',
    change: '更改',
    personal: '修改密码', // 个人中心
    website: '公司官网', // 公司官网
    workbench: '工作台', // 工作台
    print_config: '打印配置', // 打印配置
    app_download: 'App下载', // App下载
    help_document: '帮助文档', // 帮助文档
    language: '语言', // 语言
    logout: '注销', // 注销
  },
  msg: {
    switch_languages_title: '确定切换语言吗?', // 确定切换语言吗?
    prompt: '提示', // 提示
    yes: '确定', // 确定
    no: '取消', // 取消
    next: '下一步', // 下一步
    clear: '清除', // 清除
  },
  base: {
    itemBase:{
		Item:{
			productsetting: '商品设置', //物料设置
			changeItemTypeId: '请选择商品种类', // 请选择物料种类
			itemTypeId: '商品种类', // 物料种类
			itemCode: 'SKU', // SKU
			itemNewModal: '新增商品', // 新增物料
			itemName: 'SKU名称', // SKU名称
			inputItemName: '请输入SKU名称', // 请输入SKU名称
			updateItem: '修改商品', // 修改物料
			iD: '商品编号', // 
			enProduct: '商品英文名', // 产品英文名
		},
		ItemType:{
			dialogNewProductType: '新增商品种类', // 新增物料种类
			dialogUpdateProductType: '修改商品种类', // 修改物料种类
		},
		
	}
  },
   work: {
		billInManage:{
			buyBillIn:{
				BuyBillInDetail:{
					selectItem: '选择商品', // 选择物料
					itemName: 'SKU名称', // SKU名称
					itemCode: 'SKU', // SKU
					changeItem: '请选择商品', // 请选择物料
					itemType: '商品种类', // 物料种类
					sameItemCannotRepeatedly: '相同商品不能重复添加', // 相同物料不能重复添加
					noSelectItem: '未选择商品,请重新选择', // 未选择物料,请重新选择
				}
			},
			asnBill:{
				CreateAsnBill:{
					selectItem: '选择商品', // 选择物料
					itemName: 'SKU名称', // SKU名称
					itemCode: 'SKU', // SKU
					changeItem: '请选择商品', // 请选择物料
					itemType: '商品种类', // 物料种类
					inputItemCode: '请输入SKU', // 请输入SKU
					sameItemCannotRepeatedly: '相同商品不能重复添加', // 相同物料不能重复添加
					noSelectItem: '未选择商品,请重新选择', // 未选择物料,请重新选择
					noSelectItemCode: 'SKU不得为空,请重新填写！', // SKU不得为空,请重新填写！
					updateItemClean: '修改商品已选物料将清空？', // 修改货主已选物料将清空？
					noSelectItemCodePleaseInput: '未查询到此SKU,请确认后输入！', // 未查询到此SKU,请确认后输入！
				}
			},
		},
		billOutManage:{
			soBill:{
				CreateSoBill:{
					selectItem: '选择商品', // 选择物料
					itemName: 'SKU名称', // SKU名称
					itemCode: 'SKU', // SKU
					changeItem: '请选择商品', // 请选择物料
					itemType: '商品种类', // 物料种类
					inputItemCode: '请输入SKU', // 请输入SKU
					sameItemCannotRepeatedly: '相同商品不能重复添加!', // 相同物料不能重复添加!
					noSelectItemCode: 'SKU不得为空,请重新填写！', // SKU不得为空,请重新填写！
					updateItemClean: '修改商品已选物料将清空？', // 修改货主已选物料将清空？
					noSelectItemCodePleaseInput: '未查询到此SKU,请确认后输入！', // 未查询到此SKU,请确认后输入！
				},
				SoBillDetail:{
					selectItem: '选择商品', // 选择物料
					itemName: 'SKU名称', // SKU名称
					itemCode: 'SKU', // SKU
					changeItem: '请选择商品', // 请选择物料
					itemType: '商品种类', // 物料种类
					inputItemCode: '请输入SKU', // 请输入SKU
					sameItemCannotRepeatedly: '相同商品不能重复添加!', // 相同物料不能重复添加!
					noSelectItemCode: 'SKU不得为空,请重新填写！', // SKU不得为空,请重新填写！
					noSelectItem: '未选择商品,请重新选择', // 未选择物料,请重新选择
				}
			},
		}
   }
  
}