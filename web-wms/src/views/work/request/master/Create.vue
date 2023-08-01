<template>
  <div class="createAsnBill">

    <el-dialog width="900px" title="选择物料" :visible.sync="innerVisibleItem" style="margin-top: -10vh;">

      <div class="publicSearch body">
        <div class="searchBar" :style="{height: isMoreSearch2?'auto':''}">
          <el-button-group>
            <el-button @click="getItemData" size="mini" icon="el-icon-search">查询</el-button>
            <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
          </el-button-group>
          <div>
            <span>关键字搜索</span>
            <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU名称/SKU" clearable></el-input>
          </div>
        </div>
        <span class="moreSearchBtn"
              :class="isMoreSearch2?'el-icon-caret-top':'el-icon-caret-bottom'"
              @click="isMoreSearch2 = !isMoreSearch2"></span>
      </div>

      <div class="tableBox">
        <el-table
            v-loading="loading.itemTable"
            max-height="460"
            :data="items"
            style="width: 100%">
          <el-table-column
              fixed="left"
              label="操作"
              width="60">
            <template slot-scope="scope">
              <el-button @click="selectItem(scope.row)" icon="iconfont icon-jia1" type="primary" circle plain size="small"></el-button>
            </template>
          </el-table-column>
          <el-table-column
              fixed="left"
              type="index"
              width="50">
          </el-table-column>
          <el-table-column
              sortable
              show-overflow-tooltip
              fixed="left"
              prop="itemName"
              label="SKU名称"
              width="180">
          </el-table-column>
          <el-table-column
              sortable
              show-overflow-tooltip
              prop="itemCode"
              label="SKU"
              width="180">
          </el-table-column>
          <el-table-column
              sortable
              show-overflow-tooltip
              prop="itemTypeName"
              label="物料种类"
              width="180">
          </el-table-column>
          <el-table-column
              sortable
              show-overflow-tooltip
              prop="supplierName"
              label="供应商"
              width="180">
          </el-table-column>
          <!--<el-table-column
              sortable
              show-overflow-tooltip
              prop="organizationName"
              label="货主"
              width="180">
          </el-table-column>-->
          <el-table-column
              sortable
              show-overflow-tooltip
              prop="batchTacticName"
              label="批次策略"
              width="180">
          </el-table-column>
          <el-table-column
              sortable
              show-overflow-tooltip
              prop="upperTacticName"
              label="上架策略"
              width="180">
          </el-table-column>
          <el-table-column
              sortable
              show-overflow-tooltip
              prop="defaultPrice"
              label="默认单价"
              width="180">
          </el-table-column>
          <el-table-column
              sortable
              show-overflow-tooltip
              prop="spec"
              label="规格"
              width="180">
          </el-table-column>
          <el-table-column
              sortable
              show-overflow-tooltip
              prop="model"
              label="型号"
              width="180">
          </el-table-column>
          <el-table-column
              sortable
              show-overflow-tooltip
              prop="packName"
              label="包装编码"
              width="180">
          </el-table-column>
          <el-table-column
              sortable
              show-overflow-tooltip
              prop="upperLimit"
              label="库存上限"
              width="180">
          </el-table-column>
          <el-table-column
              sortable
              show-overflow-tooltip
              prop="floorLimit"
              label="库存下限"
              width="180">
          </el-table-column>
          <el-table-column
              sortable
              show-overflow-tooltip
              prop="weight"
              label="重量"
              width="180">
          </el-table-column>
        </el-table>
        <el-pagination
            style="text-align: right;"
            class="pagination-bottom"
            @current-change="handleCurrentChange"
            :current-page.sync="searchData.pageNum"
            :page-size="searchData.pageSize"
            layout="total, prev, pager, next, jumper"
            :total="searchData.total">
        </el-pagination>
      </div>

    </el-dialog>

    <div class="publicSearch  ">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <p style="font-size: 20px;font-weight: 600;line-height: 20px;margin-top: 15px;margin-left: 15px;
      padding: 0px 20px 20px 20px;">领料单信息</p>
        <el-form class="clearFloat" :model="form.asnMaster" :rules="rules" ref="form.asnMaster">
          <el-row :gutter="10">
            <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6"> 
                <el-form-item label="仓库" :label-width="formLabelWidth">
                <el-select v-model="form.asnMaster.wareId" disabled filterable placeholder="请选择仓库" size="small"
                          style="width: 100%;">
                  <el-option
                    :key="form.asnMaster.wareId"
                    :label="form.asnMaster.wareName"
                    :value="form.asnMaster.wareId">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6"> 
                <el-form-item label="货主" prop="organizationId" :label-width="formLabelWidth">
                <el-select v-model="form.asnMaster.organizationId" @change="changeOrganizationId" filterable placeholder="请选择货主" size="small"
                          style="width: 100%;">
                  <el-option
                    v-for="item in organizations"
                    :key="item.organizationId"
                    :label="item.organizationName"
                    :value="item.organizationId">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
             <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6"> 
                <el-form-item label="领料部门编码" prop="request_dept_id" :label-width="formLabelWidth">
                 <el-input   style="width: 100%;" v-model="form.asnMaster.requestDeptId" placeholder="请输入工单" size="small"></el-input>
            
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6"> 
                <el-form-item label="领料部门名称" prop="request_dept_name" :label-width="formLabelWidth">
                 <el-input   style="width: 100%;" v-model="form.asnMaster.requestDeptName" placeholder="请输入工单" size="small"></el-input>
            
              </el-form-item>
            </el-col>
           <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6"> 
             <el-form-item label="工单" :label-width="formLabelWidth">
              <el-input   style="width: 100%;" v-model="form.asnMaster.workOrderNo" placeholder="请输入工单" size="small"></el-input>
             </el-form-item>
            </el-col>
           
           <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6"> 
                  <el-form-item label="备注" :label-width="formLabelWidth">
                  <el-input   style="width: 100%;"  v-model="form.asnMaster.memo" placeholder="请输入备注" size="small"></el-input>
                </el-form-item> 
            </el-col>

            <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6"> 
                  <el-form-item label="用途" :label-width="formLabelWidth">
                  <el-input   style="width: 100%;"  v-model="form.asnMaster.purpose" placeholder="请输入备注" size="small"></el-input>
                </el-form-item> 
            </el-col>

            <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6"> 
                  <el-form-item label="发料方式" :label-width="formLabelWidth">


                     
                      <el-radio-group v-model="form.asnMaster.way">
                        <el-radio :label="1">仓库送料 </el-radio>
                        <el-radio :label="2">车间领料</el-radio>
                         
                      </el-radio-group>
                    

                  </el-form-item> 
            </el-col>

             

             </el-row>
        </el-form>
            
          
          
          
          
          
         
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="tableBox">
      <el-table
        highlight-current-row
        :data="form.asnDetails"
        style="width: 100%">
        <el-table-column
          fixed="left"
          type="index"
          width="50">
        </el-table-column>
        <el-table-column
          fixed="left"
          label="操作"
          width="60">
          <template slot-scope="scope">
            <el-button @click="deleteRow(scope.$index)" icon="iconfont icon-shanchu" type="danger" circle plain
                       size="small"></el-button>
          </template>
        </el-table-column>
        <el-table-column
          label="SKU"
           >
          <template slot-scope="scope">
            <el-input v-model="scope.row.itemCode" @change="changeItemCode(scope.row.itemCode, scope.$index)"
                      @clear="clearItemCode(scope.$index)"
                      :disabled="isOrganization && (form.asnMaster.organizationId === '' || form.asnMaster.organizationId === undefined || form.asnMaster.organizationId === null
                      || form.asnDetails[scope.$index].id > 0)"
                      placeholder="请输入SKU" size="small" clearable style="width: 80%;"></el-input>
            <el-button @click="showItemModal(scope.$index)"
                       icon="iconfont icon-secondsearch_accurate" type="primary" circle plain size="mini"></el-button>
          </template>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="itemName"
          label="SKU名称">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemName}}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="数量"
          >
          <template slot-scope="scope">
            <input type="number" class="my_input small" v-model.number="scope.row.requestQuantity" :disabled="scope.row.id === '' || scope.row.id === undefined || scope.row.id === null" placeholder="请输入数量" style="width: 80%;">
<!--            <el-input-number v-model="scope.row.expectQuantity" :disabled="scope.row.id === '' || scope.row.id === undefined || scope.row.id === null" controls-position="right" :min="0" label="请输入数量" size="small" style="width: 90%;"></el-input-number>-->
          </template>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          
          label="单位"
          >
          <template slot-scope="scope">
            <el-select v-model="scope.row.packDetailId" @change="changePackDetailId(scope.$index)" :disabled="scope.row.id === '' || scope.row.id === undefined || scope.row.id === null" filterable placeholder="请选择单位" size="small">
              <el-option
                v-for="item in scope.row.packDetails"
                :key="item.packDetailId"
                :label="item.packDescribe"
                :value="item.packDetailId">
              </el-option>
            </el-select>
          </template>
        </el-table-column>
        
      </el-table>
    </div>

    <div class="createFootBox clearFloat" :style="{width: isCollapse ? 'calc(100% - 104px)' : 'calc(100% - 240px)'}">
      <div class="leftFloat">
        <el-button type="info" @click="$router.go(-1)">返 回</el-button>
      </div>
      <el-button size="small" type="primary" round icon="iconfont icon-jia1" @click="addRow"> 新增一行</el-button>
      <div class="rightFloat">
        <!-- <el-button @click="commitForm('form.asnMaster',-2)" type="info" plain>保存为草稿</el-button> -->
        <el-button @click="commitForm('form.asnMaster',0)" type="primary">提 交</el-button>
      </div>
    </div>

  </div>
</template>

<script>
  export default {
    name: 'CreateAsnBill',
    data() {
      let validateExpectTime = (rule, value, callback) => {
        let expectTime = this.form.asnMaster.expectTime;
        let nowTime = PF.getNowFormatDate();
        if(!PF.isNull(expectTime)){
          callback(new Error('请选择预计到货时间(早)'));
        }else if(!PF.isNull(nowTime)){
          callback(new Error('未获取到当前日期'));
        }else if(nowTime > expectTime){
          callback(new Error('预计到货时间应该大于等于当前日期'));
        }else {
          callback();
        }
      };
      return {
        isMoreSearch: true,
        isMoreSearch2: true,
        loading:{
          itemTable:true,
        },

        searchData: {
          pageNum: 1,
          pageSize: 10,
          total: null,
          keyWords: '',
          wareId: PF.getLocal('wareId', 'number'),
          organizationId: null,
        },

        organizations: [],
        suppliers: [],
        platforms: [],
        carriers: [],
        items: [],
        ids: [],
        asnUdfHs: [],
        asnUdfDs: [],
        asnTypes: [],

        isOrganization: true,
        selectDetailIndex: null,
        oldOrganizationId: null,

        innerVisibleItem: false,

        formLabelWidth: '120px',

        form: {
          billSource: 1,
          insertType: 0,
          asnMaster: {
            wareId: PF.getLocal('wareId', 'number'),
            wareName: PF.getLocal('wareName'),
            organizationId: null,
            organizationName: '',
           
            workOrderNo: "",
            purpose:'',
            memo: '',
            way:2,
          
          },
          asnDetails: [
            {
              itemCode: '',
              itemName: '',
              requestQuantity: null,
              packDetails: [],
              packDetailId: null,
              packDescribe: '',
             
            }
          ]
        },
        asnDetail: {
          itemCode: '',
          itemName: '',
          requestQuantity: null,
          packDetails: [],
          packDetailId: null,
          packDescribe: '',
        
        },
        rules: {
          organizationId: [
            {required: true, message: '请选择货主', trigger: 'blur'}
          ],
          supplierId: [
            {required: true, message: '请选择供应商', trigger: 'blur'}
          ],
          // asnType: [
          //   {required: true, message: '请选择入库单类型', trigger: 'blur'}
          // ],
          expectTime: [
            {required: true, message: '预计到货时间不得为空', trigger: 'blur'},
            { validator: validateExpectTime, trigger: 'blur'  }
          ],
        }

      }
    },
    computed: {
      isCollapse() {
        return this.$parent.leftMenuIsCollapse;
      },
      organizationId(){
        return this.form.asnMaster.organizationId
      },
      supplierId(){
        return this.form.asnMaster.supplierId
      },
    },
    watch: {
      organizationId(val){
        for(let i=0;i<this.organizations.length;i++){
          if(this.organizations[i].organizationId === val){
            this.form.asnMaster.organizationName = this.organizations[i].organizationName;
          }
        }
      },
      // supplierId(val){
      //   for(let i=0;i<this.suppliers.length;i++){
      //     if(this.suppliers[i].supplierId === val){
      //       this.form.asnMaster.supplierName = this.suppliers[i].supplierName;
      //     }
      //   }
      // },
    },
    methods: {
      handleCurrentChange(val) {
        this.searchData.pageNum = val;
        this.getItemData()
      },
      clearSearchData() {
        this.searchData.keyWords = '';
      },
      getItemData() {
        this.loading.itemTable = true;
        IOT.getServerData('/item/infos/list', 'get', this.searchData, (ret) => {
          this.loading.itemTable = false;
          if (ret.code === 200) {
            this.items = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      // getPlatformData(){
      //   IOT.getServerData('/platforms/list','get',{wareId: this.form.asnMaster.wareId},(ret) => {
      //     if (ret.code === 200) {
      //       let list = ret.rows;
      //       for(let i=0;i<list.length;i++){
      //         list[i].label = list[i].platformCode
      //       }
      //       this.platforms = list;
      //     } else {
      //       IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
      //     }
      //   });
      // },
      // getCarrierData(){
      //   IOT.getServerData('/carriers/list','get',{},(ret) => {
      //     if (ret.code === 200) {
      //       this.carriers = ret.rows;
      //     } else {
      //       IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
      //     }
      //   });
      // },
      addRow() {
        this.form.asnDetails.push(PF.JSON(this.asnDetail));
      },
      deleteRow(index) {
        this.form.asnDetails.splice(index, 1);
        this.ids.splice(index, 1);
      },
      showItemModal(index){
        if(this.isOrganization){
          if(!PF.isNull(this.form.asnMaster.organizationId)){
            IOT.tips('请选择货主！', 'error');
            return false
          }
          this.searchData.organizationId = this.form.asnMaster.organizationId;
        }else{
          this.searchData.organizationId = null;
        }
        this.selectDetailIndex = index;
        this.getItemData();
        this.innerVisibleItem = true;
      },
      selectItem(row){
        if(this.ids.indexOf(row.id) !== -1){
          IOT.tips('相同物料不能重复添加！', 'error');
          this.innerVisibleItem = false;
          return false
        }
        IOT.getServerData('/pack/details/list','get',{packCode: row.packCode},(ret) => {
          if (ret.code === 200) {
            row.packDetails = ret.rows;
            row.packDetailId = ret.rows[0].packDetailId;
            row.packDescribe = ret.rows[0].packDescribe;
            row.transRatio = ret.rows[0].transRatio;
            this.ids.push(row.id);
            for(let key in row){
              this.form.asnDetails[this.selectDetailIndex][key] = row[key];
            }
            this.innerVisibleItem = false;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
      commitForm(formName,insertType) {
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let form = PF.JSON(this.form);
            let form1 = {requestMaster:form.asnMaster,requestDetailList:form.asnDetails};

            for(let i=0;i<form.asnDetails.length;i++){
              if(!PF.isNull(form.asnDetails[i].id)){
                IOT.tips('不得有空行！', 'error', 1000 , () => {
                  IOT.hideOverlay();
                });
                return false
              }
              if(!PF.isNull(form.asnDetails[i].itemCode)){
                IOT.tips('SKU不得为空,请重新填写！', 'error', 1000 , () => {
                  IOT.hideOverlay();
                });
                return false
              }
              if(!PF.isNull(form.asnDetails[i].requestQuantity) || form.asnDetails[i].requestQuantity <= 0){
                IOT.tips('数量需大于零！', 'error', 1000 , () => {
                  IOT.hideOverlay();
                });
                  return false
              }
              if(!PF.isNull(form.asnDetails[i].packDetailId)){
                IOT.tips('未选择单位！', 'error', 1000 , () => {
                  IOT.hideOverlay();
                });
                return false
              }
              // 主单位数量
              form.asnDetails[i].expectQuantity = form.asnDetails[i].expectQuantity * form.asnDetails[i].transRatio;
            }
            form.insertType = insertType;
            IOT.getServerData('/request/masters/insert', 'post', form1, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success');
                this.$router.push({name: 'asnBill'})
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm(formName);
            this.cleaerDetails();
            this.form.asnMaster.memo = '';
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      cleaerDetails(){
        this.ids = [];
        this.form.asnDetails = [{
          itemCode: '',
          itemName: '',
          expectQuantity: null,
        }];
      },
      changeOrganizationId(val){
        if(this.isOrganization){
          if(!PF.isNull(this.oldOrganizationId)){
            this.oldOrganizationId = val;
          }else{
            this.$confirm('修改货主已选物料将清空？')
                .then(_ => {
                  this.oldOrganizationId = val;
                  this.cleaerDetails();
                })
                .catch(_ => {
                  this.form.asnMaster.organizationId = this.oldOrganizationId;
                });
          }
        }
      },
      clearItemCode(index){
        this.form.requestBuyDetails[index] = this.requestBuyDetail;
        delete that.ids[index];
      },
      changeItemCode(itemCode, index){
        let that = this;
        if(!PF.isNull(itemCode)){
          that.form.asnDetails[index] = that.asnDetail;
          return false
        }
        let searchData = {
          wareId: this.form.asnMaster.wareId,
          organizationId: this.form.asnMaster.organizationId,
          itemCode: itemCode,
        };
        if(!that.isOrganization){
          delete searchData.organizationId ;
        }
        IOT.showOverlay('查询中，请稍等...');
        IOT.getServerData('/item/infos/list', 'get', searchData, (ret) => {
          if (ret.code === 200) {
            let list = ret.rows;
            if(list.length === 1){
              if(this.ids.indexOf(list[0].id) !== -1){
                IOT.tips('相同物料不能重复添加！', 'error');
                that.form.asnDetails[index].itemCode = '';
                IOT.hideOverlay();
                return false
              }

              let row = list[0];
              IOT.getServerData('/pack/details/list','get',{packCode: row.packCode},(ret) => {
                IOT.hideOverlay();
                if (ret.code === 200) {
                  that.ids.push(row.id);
                  row.packDetails = ret.rows;
                  row.packDetailId = ret.rows[0].packDetailId;
                  for(let key in row){
                    that.form.asnDetails[index][key] = row[key];
                  }
                } else {
                   IOT.hideOverlay();
                  IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                }
              });

            }else {
               IOT.hideOverlay();
              IOT.tips('未查询到此SKU,请确认后输入！', 'error');
            }
          } else {
             IOT.hideOverlay();
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      changePackDetailId(index){
        let detail = PF.JSON(this.form.asnDetails[index]);
        for(let i=0;i<detail.packDetails.length;i++){
          if(detail.packDetails[i].packDetailId === detail.packDetailId){
            this.form.asnDetails[index].packDescribe = detail.packDetails[i].packDescribe;
            this.form.asnDetails[index].transRatio = detail.packDetails[i].transRatio;
          }
        }
      },
    },
    created() {
      PF.getSysParamUdfData('isOrganization', [], (data, udfs) => {
        if(udfs[0].value === '1'){
          this.isOrganization = true;
        }else if(udfs[0].value === '0'){
          this.isOrganization = false;
        }
      });
      PF.getOrganizations((rows) => {
        this.organizations = rows;
      });
      // PF.getSuppliers((rows) => {
      //   this.suppliers = rows;
      // });
      // PF.getSysParamUdfData('asnUdfHs', [], (data, udfs) => {
      //   this.asnUdfHs = udfs;
      // });
      // PF.getSysParamUdfData('asnUdfDs', [], (data, udfs) => {
      //   this.asnUdfDs = udfs;
      // });
      // this.getPlatformData();
      // this.getCarrierData();
      // PF.getDataDictUdfData('asnType', (rows) => {
      //   this.asnTypes = rows;
      // });
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .createAsnBill {
    padding-bottom: 100px;
    input:focus{
      outline: none;    // 如何把蓝色去掉？
      border: 1px solid #30B101; // 如何改成别的颜色？
    }
  }
</style>
