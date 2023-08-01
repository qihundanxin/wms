<template>
  <div class="createQc">

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
            <template slot-scope="scope">
              <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemName}}</span>
            </template>
          </el-table-column>
          <el-table-column
            sortable
            show-overflow-tooltip
            prop="itemCode"
            label="SKU"
            width="180">
            <template slot-scope="scope">
              <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemCode}}</span>
            </template>
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            label="库存数量"
            width="180">
            <template slot-scope="scope">
              <span>{{scope.row.quantity - (scope.row.allotQuantity + scope.row.freezeQuantity)}}</span>
            </template>
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="packDescribe"
            label="单位"
            width="180">
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="batchName"
            label="批次"
            width="180">
          </el-table-column>
          <!--<el-table-column
            sortable
            show-overflow-tooltip
            prop="organizationName"
            label="货主"
            width="180">
            <template slot-scope="scope">
              <span class="textBtn" @click="$parent.showPublicModal(scope.row.organizationId, 'organization')">{{scope.row.organizationName}}</span>
            </template>
          </el-table-column>-->
        </el-table>
        <el-pagination
        style="text-align: right;"
        @current-change="handleCurrentChange"
         @size-change="handleSizeChange"      
        :current-page.sync="searchData.pageNum"
        :page-sizes="[15, 30, 50, 100]"
        :page-size="searchData.pageSize"
        layout="sizes,total, prev, pager, next"
        :total="searchData.total">
      </el-pagination>
      </div>

    </el-dialog>

    <div class="publicSearch bigFormBox3">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <p style="font-size: 20px;font-weight: 600;line-height: 20px;margin-top: 15px;">质检单信息</p>
        <el-form class="clearFloat" :model="form.qcMaster" :rules="rules" ref="form.qcMaster">
          <!--<el-form-item label="入库单号" :label-width="formLabelWidth">
             <el-input v-model="form.qcMaster.asnBillNo" placeholder="请输入入库单号" size="small" disabled></el-input>
          </el-form-item>-->
          <el-form-item label="仓库" :label-width="formLabelWidth">
            <el-select v-model="form.qcMaster.wareId" disabled filterable placeholder="请选择仓库" size="small"
                       style="width: 100%;">
              <el-option
                :key="form.qcMaster.wareId"
                :label="form.qcMaster.wareName"
                :value="form.qcMaster.wareId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="货主" prop="organizationId" :label-width="formLabelWidth">
            <el-select v-model="form.qcMaster.organizationId" @change="changeOrganizationId" filterable placeholder="请选择货主" size="small"
                       style="width: 100%;">
              <el-option
                v-for="item in organizations"
                :key="item.organizationId"
                :label="item.organizationName"
                :value="item.organizationId">
              </el-option>
            </el-select>
          </el-form-item>
         <!-- <el-form-item label="供应商" prop="supplierId" :label-width="formLabelWidth">
            <el-select v-model="form.qcMaster.supplierId" filterable placeholder="请选择供应商" size="small" style="width: 100%;">
              <el-option
                      v-for="item in suppliers"
                      :key="item.supplierId"
                      :label="item.supplierName"
                      :value="item.supplierId">
              </el-option>
            </el-select>
          </el-form-item>-->
          <el-form-item label="质检类型" prop="qcType" :label-width="formLabelWidth">
            <el-select v-model="form.qcMaster.qcType" filterable placeholder="请选择质检类型"
                       style="width: 100%;">
              <el-option
                v-for="item in qcTypes"
                :key="item.dataDictDetailId"
                :label="item.dictValue"
                :value="item.dataDictDetailId">
              </el-option>
            </el-select>
          </el-form-item>





          <el-form-item label="备注" :label-width="formLabelWidth">
            <el-input v-model="form.qcMaster.memo" placeholder="请输入备注" size="small"></el-input>
          </el-form-item>
          <el-form-item v-for="(item,index) in qcUdfHs" :key="index" :label="item.value" :label-width="formLabelWidth">
            <el-input v-model="form.qcMaster[item.paramCode]" :placeholder="'请输入'+item.value" size="small"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="tableBox">
      <el-table
        highlight-current-row
        :data="form.qcDetails"
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
          width="240">
          <template slot-scope="scope">
            <el-input v-model="scope.row.itemCode" @change="changeItemCode(scope.row.itemCode, scope.$index)"
                      :disabled="form.qcMaster.organizationId === '' || form.qcMaster.organizationId === undefined || form.qcMaster.organizationId === null
                      || form.qcDetails[scope.$index].id > 0"
                      placeholder="请输入SKU" size="small" clearable style="width: 80%;"></el-input>
            <el-button @click="showItemModal(scope.$index)"
                       icon="iconfont icon-secondsearch_accurate" type="primary" circle plain size="mini"></el-button>
          </template>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="itemName"
          label="SKU名称"
          width="180">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemName}}</span>
          </template>
        </el-table-column>
        <el-table-column
            show-overflow-tooltip
            prop="batchName"
            label="批次"
            width="180">
          <template slot-scope="scope">
            <span class="textBtn" @click="$parent.showPublicModal(scope.row.batchId, 'batch')">{{scope.row.batchName}}</span>
          </template>
        </el-table-column>
        <el-table-column
                show-overflow-tooltip
                label="库存数量"
                width="150">
          <template slot-scope="scope">
            <span>{{scope.row.quantity - (scope.row.allotQuantity + scope.row.freezeQuantity)}}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="质检数量"
          width="180">
          <template slot-scope="scope">
            <input type="number" class="my_input small" v-model.number="scope.row.expectQuantity" :disabled="scope.row.batchId === '' || scope.row.batchId === undefined || scope.row.batchId === null" placeholder="请输入数量" style="width: 80%;">
<!--            <el-input-number v-model="scope.row.expectQuantity" :disabled="scope.row.id === '' || scope.row.id === undefined || scope.row.id === null" controls-position="right" :min="0" label="请输入数量" size="small" style="width: 90%;"></el-input-number>-->
          </template>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="itemTypeName"
          label="单位"
          width="180">
          <template slot-scope="scope">
            <el-select v-model="scope.row.packDetailId" @change="changePackDetailId(scope.$index)" :disabled="scope.row.batchId === '' || scope.row.batchId === undefined || scope.row.batchId === null" filterable placeholder="请选择单位" size="small">
              <el-option
                v-for="item in scope.row.packDetails"
                :key="item.packDetailId"
                :label="item.packDescribe"
                :value="item.packDetailId">
              </el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          v-for="(item,index) in qcUdfDs"
          :key="index"
          :prop="item.paramCode"
          :label="item.value"
          width="180">
          <template slot-scope="scope">
            <el-input v-model="scope.row[item.paramCode]" :disabled="scope.row.batchId === '' || scope.row.batchId === undefined || scope.row.batchId === null" :placeholder="'请输入' + item.value" size="small" clearable></el-input>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="createFootBox clearFloat" :style="{width: isCollapse ? 'calc(100% - 104px)' : 'calc(100% - 240px)'}">
      <div class="leftFloat">
        <el-button type="info" @click="$router.go(-1)">返 回</el-button>
      </div>
      <el-button size="small" type="primary"    icon="iconfont icon-jia1" @click="addRow"> 新增一行</el-button>
      <div class="rightFloat">
        <el-button @click="commitForm('form.qcMaster',-2)" type="info" plain>保存为草稿</el-button>
        <el-button @click="commitForm('form.qcMaster',0)" type="primary">提 交</el-button>
      </div>
    </div>

  </div>
</template>

<script>
  export default {
    name: 'CreateQc',
    data() {
      return {
        isMoreSearch: true,
        isMoreSearch2: true,

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
        qcUdfHs: [],
        qcUdfDs: [],
        qcTypes: [],

        selectDetailIndex: null,
        oldOrganizationId: null,

        innerVisibleItem: false,

        formLabelWidth: '120px',

        form: {
          billSource: 1,
          insertType: 0,
          qcMaster: {
            wareId: PF.getLocal('wareId', 'number'),
            wareName: PF.getLocal('wareName'),
            organizationId: null,
            organizationName: '',
            supplierId: null,
            supplierName: '',
            qcType: null,
            expectTime: '',
            orderNo: 0,
            loadingPlace: '',
            deliveryPlace: '',
            trafficDescribe: '',
            memo: '',
            "qcUdfHs1": "",
            "qcUdfHs2": "",
            "qcUdfHs3": ""
          },
          qcDetails: [
            {
              itemCode: '',
              itemName: '',
              expectQuantity: null,
              packDetails: [],
              packDetailId: null,
              packDescribe: '',
              "qcUdfDs1": "",
              "qcUdfDs2": "",
              "qcUdfDs3": ""
            }
          ]
        },
        qcDetail: {
          itemCode: '',
          itemName: '',
          expectQuantity: null,
          packDetails: [],
          packDetailId: null,
          packDescribe: '',
          "qcUdfDs1": "",
          "qcUdfDs2": "",
          "qcUdfDs3": ""
        },
        rules: {
          organizationId: [
            {required: true, message: '请选择货主', trigger: 'blur'}
          ],
          expectTime: [
            {required: true, message: '预计到货时间不得为空', trigger: 'blur'}
          ],
          qcType: [
            {required: true, message: '请选择质检类型', trigger: 'blur'}
          ],
        }

      }
    },
    computed: {
      isCollapse() {
        return this.$parent.leftMenuIsCollapse;
      },
      organizationId(){
        return this.form.qcMaster.organizationId
      },
      supplierId(){
        return this.form.qcMaster.supplierId
      },
    },
    watch: {
      organizationId(val){
        for(let i=0;i<this.organizations.length;i++){
          if(this.organizations[i].organizationId === val){
            this.form.qcMaster.organizationName = this.organizations[i].organizationName;
          }
        }
      },
      supplierId(val){
        for(let i=0;i<this.suppliers.length;i++){
          if(this.suppliers[i].supplierId === val){
            this.form.qcMaster.supplierName = this.suppliers[i].supplierName;
          }
        }
      },
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
        IOT.getServerData('/inventorys/list', 'get', this.searchData, (ret) => {
          if (ret.code === 200) {
            this.items = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getPlatformData(){
        IOT.getServerData('/platforms/list','get',{wareId: this.form.qcMaster.wareId},(ret) => {
          if (ret.code === 200) {
            let list = ret.rows;
            for(let i=0;i<list.length;i++){
              list[i].label = list[i].platformCode
            }
            this.platforms = list;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getCarrierData(){
        IOT.getServerData('/carriers/list','get',{},(ret) => {
          if (ret.code === 200) {
            this.carriers = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      addRow() {
        this.form.qcDetails.push(PF.JSON(this.qcDetail));
      },
      deleteRow(index) {
        this.form.qcDetails.splice(index, 1);
        this.ids.splice(index, 1);
      },
      showItemModal(index){
        if(!PF.isNull(this.form.qcMaster.organizationId)){
          IOT.tips('请选择货主！', 'error');
          return false
        }
        this.searchData.organizationId = this.form.qcMaster.organizationId;
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
            this.ids.push(row.batchId);
            for(let key in row){
              this.form.qcDetails[this.selectDetailIndex][key] = row[key];
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
            for(let i=0;i<form.qcDetails.length;i++){
              if(!PF.isNull(form.qcDetails[i].batchId)){
                IOT.tips('不得有空行！', 'error', 1000 , () => {
                  IOT.hideOverlay();
                });
                return false
              }
              if(!PF.isNull(form.qcDetails[i].itemCode)){
                IOT.tips('SKU不得为空,请重新填写！', 'error', 1000 , () => {
                  IOT.hideOverlay();
                });
                return false
              }
              if(!PF.isNull(form.qcDetails[i].expectQuantity) || form.qcDetails[i].expectQuantity <= 0){
                IOT.tips('数量需大于零！', 'error', 1000 , () => {
                  IOT.hideOverlay();
                });
                  return false
              }
              if(!PF.isNull(form.qcDetails[i].packDetailId)){
                IOT.tips('未选择单位！', 'error', 1000 , () => {
                  IOT.hideOverlay();
                });
                return false
              }
            }
            form.insertType = insertType;
            IOT.getServerData('/qc/masters/insert', 'post', form, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success');
                this.$router.push({name: 'qcBill'})
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm(formName);
            this.cleaerDetails();
            this.form.qcMaster.memo = '';
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      cleaerDetails(){
        this.ids = [];
        this.form.qcDetails = [{
          itemCode: '',
          itemName: '',
          expectQuantity: null,
        }];
      },
      changeOrganizationId(val){
        if(!PF.isNull(this.oldOrganizationId)){
          this.oldOrganizationId = val;
        }else{
          this.$confirm('修改货主已选物料将清空？')
            .then(_ => {
              this.oldOrganizationId = val;
              this.cleaerDetails();
            })
            .catch(_ => {
              this.form.qcMaster.organizationId = this.oldOrganizationId;
            });
        }
      },
      changeItemCode(itemCode, index){
        let that = this;
        if(!PF.isNull(itemCode)){
          return false
        }
        let searchData = {
          wareId: this.form.qcMaster.wareId,
          organizationId: this.form.qcMaster.organizationId,
          itemCode: itemCode,
        };
        IOT.getServerData('/item/infos/list', 'get', searchData, (ret) => {
          if (ret.code === 200) {
            let list = ret.rows;
            if(list.length === 1){
              if(this.ids.indexOf(list[0].id) !== -1){
                IOT.tips('相同物料不能重复添加！', 'error');
                return false
              }

              let row = list[0];
              IOT.getServerData('/pack/details/list','get',{packCode: row.packCode},(ret) => {
                if (ret.code === 200) {
                  that.ids.push(row.id);
                  row.packDetails = ret.rows;
                  row.packDetailId = ret.rows[0].packDetailId;
                  for(let key in row){
                    that.form.qcDetails[index][key] = row[key];
                  }
                } else {
                  IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                }
              });

            }else {
              IOT.tips('未查询到此SKU,请确认后输入！', 'error');
            }
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      changePackDetailId(index){
        let detail = PF.JSON(this.form.qcDetails[index]);
        for(let i=0;i<detail.packDetails.length;i++){
          if(detail.packDetails[i].packDetailId === detail.packDetailId){
            this.form.qcDetails[index].packDescribe = detail.packDetails[i].packDescribe;
          }
        }
      }
    },
    created() {
      PF.getOrganizations((rows) => {
        this.organizations = rows;
      });
      PF.getSuppliers((rows) => {
        this.suppliers = rows;
      });
      PF.getSysParamUdfData('qcUdfHs', [], (data, udfs) => {
        this.qcUdfHs = udfs;
      });
      PF.getSysParamUdfData('qcUdfDs', [], (data, udfs) => {
        this.qcUdfDs = udfs;
      });
      this.getPlatformData();
      this.getCarrierData();
      PF.getDataDictUdfData('qcType', (rows) => {
        this.qcTypes = rows;
      });
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .createQc {
    padding-bottom: 100px;
    input:focus{
      outline: none;    // 如何把蓝色去掉？
      border: 1px solid #30B101; // 如何改成别的颜色？
    }
  }
</style>
