<template>
  <div class="check">
    <h1>复核工作台</h1>

    <div class="checkTop">
      <div class="checkSearch">
        <span style="color: #F56C6C;margin-right: 4px;">*</span>
        <span>SO单号</span>
        <el-input v-model="searchData.billNo" size="mini" placeholder="请输入SO单号" clearable style="width: 200px;"></el-input>
        <el-button-group style="margin-left: 10px;">
          <el-button @click="getPickTaskData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
      </div>

      <div class="tableBox">
        <el-table
            v-loading="loading.pickTaskTable"
          highlight-current-row
          :data="pickTasks"
          style="width: 100%">
          <el-table-column
            fixed="left"
            type="index"
            width="50">
          </el-table-column>
          <el-table-column
            fixed="left"
            show-overflow-tooltip
            prop="itemName"
            label="SKU名称"
            width="150">
            <template slot-scope="scope">
              <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemName}}</span>
            </template>
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="itemCode"
            label="SKU"
            width="150">
            <template slot-scope="scope">
              <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemCode}}</span>
            </template>
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="organizationName"
            label="货主"
            width="150">
            <template slot-scope="scope">
              <span class="textBtn" @click="$parent.showPublicModal(scope.row.organizationId, 'organization')">{{scope.row.organizationName}}</span>
            </template>
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="batchName"
            label="批次"
            width="150">
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="pickQuantity"
            label="拣货数量"
            width="150">
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="reviewQuantity"
            label="复核数量"
            width="150">
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            label="未复核数量"
            width="150">
            <template slot-scope="scope">
              <span>{{scope.row.pickQuantity - scope.row.reviewQuantity}}</span>
            </template>
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="packDescribe"
            label="单位"
            width="150">
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="boxCode"
            label="追踪号"
            width="150">
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="lpn"
            label="lpn"
            width="150">
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="areaName"
            label="货区"
            width="150">
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="shelfName"
            label="货架"
            width="150">
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="cellName"
            label="货位"
            width="150">
          </el-table-column>
        </el-table>
        <el-pagination
          style="text-align: right;"
          @current-change="handleCurrentChange"
          :current-page.sync="searchData.pageNum"
          :page-size="searchData.pageSize"
          layout="total, prev, pager, next, jumper"
          :total="searchData.total">
        </el-pagination>
      </div>
    </div>

    <div class="checkBottom clearFloat">
      <div class="leftFloat">
        <div class="checkSearch">
          <span style="color: #F56C6C;margin-right: 4px;">*</span>
          <span>箱号</span>
          <el-input v-model="searchData2.boxNo" size="mini" placeholder="请输入箱号" :disabled="isCheckBox" clearable style="width: 200px;"></el-input>
          <el-button @click="checkBoxNo" size="mini" icon="el-icon-check" style="margin-left: 10px;">确定</el-button>
          <el-button @click="clearBoxNo" size="mini" icon="el-icon-refresh">换箱</el-button>
          <el-button @click="getBoxDetailData" size="mini" icon="el-icon-search">查询</el-button>
        </div>

        <div class="tableBox">
          <el-table
              v-loading="loading.boxTable"
            highlight-current-row
            :data="boxDetails"
            style="width: 100%">
            <el-table-column
              fixed="left"
              type="index"
              width="50">
            </el-table-column>
            <el-table-column
              fixed="left"
              show-overflow-tooltip
              prop="itemName"
              label="SKU名称"
              width="120">
              <template slot-scope="scope">
                <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemName}}</span>
              </template>
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="itemCode"
              label="SKU"
              width="120">
              <template slot-scope="scope">
                <span class="textBtn" @click="$parent.showPublicModal(scope.row.itemCode, 'item')">{{scope.row.itemCode}}</span>
              </template>
            </el-table-column>
            <el-table-column
                    show-overflow-tooltip
                    prop="pickQuantity"
                    label="拣货数量"
                    width="120">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="quantity"
              label="装箱数量"
              width="120">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="packDescribe"
              label="单位"
              width="120">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="boxNo"
              label="箱号"
              width="120">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="billNo"
              label="SO单号"
              width="120">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="memo"
              label="memo"
              width="120">
            </el-table-column>
            <el-table-column
                show-overflow-tooltip
                prop="pickTaskId"
                label="拣货任务"
                width="120">
            </el-table-column>
            <el-table-column
              fixed="right"
              label="操作"
              width="60">
              <template slot-scope="scope">
                <el-button @click="isWithdraw(scope.row)" icon="iconfont icon-chexiao" type="danger" circle plain size="small"></el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            style="text-align: right;"
            @current-change="handleCurrentChange2"
            :current-page.sync="searchData2.pageNum"
            :page-size="searchData2.pageSize"
            layout="total, prev, pager, next, jumper"
            :total="searchData2.total">
          </el-pagination>
        </div>
      </div>
      <div class="rightFloat">
        <el-form class="clearFloat" :model="form" :rules="rules" ref="form">
          <el-form-item label="SKU" prop="itemCode" :label-width="formLabelWidth">
            <el-input v-model="form.itemCode" @blur="getDetailItemData" placeholder="请输入SKU" clearable style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="SKU名称" prop="itemName" :label-width="formLabelWidth">
            <el-input v-model="form.itemName" placeholder="请输入SKU名称" disabled clearable style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="批次" prop="batchId" :label-width="formLabelWidth">
            <el-select v-model="form.batchId" filterable placeholder="请选择批次" :disabled="form.itemName === ''" clearable style="width: 90%;">
              <el-option
                v-for="item in form.batchs"
                :key="item.batchId"
                :label="item.batchName"
                :value="item.batchId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="拣货数量" prop="pickQuantity" :label-width="formLabelWidth">
            <span>{{form.pickQuantity}}</span>
          </el-form-item>
          <el-form-item label="数量" prop="quantity" :label-width="formLabelWidth">
            <el-input-number v-model="form.quantity" :min="0" :max="form.pickQuantity" label="请输入数量" :disabled="form.itemName === ''" style="width: 90%;"></el-input-number>
          </el-form-item>
          <el-form-item label="单位" prop="packDetailId" :label-width="formLabelWidth">
            <el-select v-model="form.packDetailId" @change="changePackDetailId()" filterable placeholder="请选择单位" disabled style="width: 90%;">
              <el-option
                v-for="item in packDetails"
                :key="item.packDetailId"
                :label="item.packDescribe"
                :value="item.packDetailId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="备注" prop="memo" :label-width="formLabelWidth">
            <el-input v-model="form.memo" placeholder="请输入备注" :disabled="form.itemName === ''" clearable style="width: 90%;"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </div>

    <div class="createFootBox clearFloat" :style="{width: isCollapse ? 'calc(100% - 104px)' : 'calc(100% - 240px)'}">
      <div class="leftFloat">
        <el-button type="info" @click="$router.go(-1)">返 回</el-button>
      </div>
      <div class="rightFloat">
        <el-button @click="commitForm('form')" type="primary">确 认</el-button>
      </div>
    </div>

  </div>
</template>

<script>
  import {mapState} from "vuex";

  export default {
    name: 'Check',
    data(){
      let validateQuantityNo0 = (rule, value, callback) => {
        if (value > 0) {
          callback();
        } else {
          callback(new Error('数量不得为0!'));
        }
      };
      return {
        loading:{
          boxTable: false,
          pickTaskTable: false,
        },
        searchData: {
          pageNum: 1,
          pageSize: 5,
          total: null,
          wareId: PF.getLocal('wareId', 'number'),
          state: 4,
          billNo: 'SO-49',
        },
        searchData2: {
          pageNum: 1,
          pageSize: 5,
          total: null,
          wareId: PF.getLocal('wareId', 'number'),
          boxNo: '',
        },

        formLabelWidth: '150px',

        pickTasks: [],
        boxDetails: [],
        packDetails: [],

        form: {
          soMasterId: null,
          soDetailId: null,
          billNo: '',
          boxNo: '',
          itemCode: '',
          itemName: '',
          batchs: '',
          batchId: '',
          batchName: '',
          quantity: 0,
          pickTaskId: null,
          pickQuantity: 0,
          packDetailId: null,
          packDescribe: '',
          memo: '',
        },
        rules: {
          itemCode: [
            {required: true, message: '请输入SKU', trigger: 'blur'}
          ],
          batchId: [
            {required: true, message: '请选择批次', trigger: 'blur'}
          ],
          quantity: [
            {required: true, message: '请输入数量', trigger: 'blur'},
            {validator: validateQuantityNo0, trigger: 'blur'}
          ],
          packDetailId: [
            {required: true, message: '请选择单位', trigger: 'blur'}
          ],
        },
      }
    },
    computed: {
      isCollapse() {
        return this.$parent.leftMenuIsCollapse;
      },
      isCheckBox(){
        return PF.isNull(this.form.boxNo)
      },
    },
    methods: {
      handleCurrentChange(val) {
        this.pageNum = val;
        this.getPickTaskData()
      },
      handleCurrentChange2(val) {
        this.pageNum = val;
        this.getBoxDetailData()
      },
      clearSearchData() {
        this.searchData.billNo = '';
      },
      getPickTaskData() {
        let searchData = PF.JSON(this.searchData);
        if(!PF.isNull(searchData.billNo)){
          IOT.tips('请输入单号！', 'error');
          return false;
        }
        this.loading.pickTaskTable = true;
        IOT.getServerData('/pick/tasks/list', 'get', searchData, (ret) => {
          this.loading.pickTaskTable = false;
          if (ret.code === 200) {
            this.pickTasks = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getBoxDetailData(){
        this.loading.boxTable = true;
        IOT.getServerData('/box/details/list','get',this.searchData2,(ret) => {
          this.loading.boxTable = false;
          if (ret.code === 200) {
            this.boxDetails = ret.rows;
            this.searchData2.total = ret.total;
            this.searchData2.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getPackDetailData(code){
        IOT.getServerData('/pack/details/list','get',{packCode: code},(ret) => {
          if (ret.code === 200) {
            this.packDetails = ret.rows
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getDetailItemData(){
        if(!PF.isNull(this.form.itemCode)){
          return false
        }
        let searchData = PF.JSON(this.searchData);
        if(!PF.isNull(searchData.billNo)){
          IOT.tips('请输入单号！', 'error');
          return false;
        }
        searchData.itemCode = this.form.itemCode;
        IOT.getServerData('/pick/tasks/list', 'get', searchData, (ret) => {
          if (ret.code === 200) {
            if(ret.rows.length > 0){
              let list = ret.rows[0];
              list.batchs = [];
              for(let i=0;i<ret.rows.length;i++){
                list.batchs.push({
                  batchId: ret.rows[i].batchId,
                  batchName: ret.rows[i].batchName,
                })
              }
              this.form.batchs = list.batchs;
              // this.form.inventoryId = list.inventoryId;
              this.form.soMasterId = list.soMasterId;
              this.form.soDetailId = list.soDetailId;
              this.form.itemCode = list.itemCode;
              this.form.itemName = list.itemName;
              this.form.batchId = list.batchId;
              this.form.batchName = list.batchName;
              this.form.packDetailId = list.packDetailId;
              this.form.packDescribe = list.packDescribe;
              this.form.pickTaskId = list.pickTaskId
              this.form.pickQuantity = list.pickQuantity;
              this.getPackDetailData(list.packCode);
            }else {
              return false;
            }
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      checkBoxNo(){
        this.form.boxNo = this.searchData2.boxNo;
      },
      clearBoxNo(){
        this.form.boxNo = '';
      },
      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
      commitForm(formName) {
        let that = this;
        if(!PF.isNull(this.searchData.billNo)){
          IOT.tips('请输入单号！', 'error');
          return false
        }
        if(!PF.isNull(this.form.boxNo)){
          IOT.tips('未输入箱号,或箱号未锁定！', 'error');
          return false
        }
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let form = PF.JSON(this.form);
            form.billNo = this.searchData.billNo;
            IOT.getServerData('/box/details/insert','post',form,(ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getPickTaskData()
                  that.getBoxDetailData()
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm('form');
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      changePackDetailId(){
        let form = PF.JSON(this.form);
        let packDetails = PF.JSON(this.packDetails);
        for(let i=0;i<packDetails.length;i++){
          if(packDetails[i].packDetailId === form.packDetailId){
            this.form.packDescribe = packDetails[i].packDescribe;
          }
        }
      },
      isWithdraw(row){
        this.$confirm('确认撤回？')
          .then(_ => {
            this.withdrawBoxDetail(row)
          })
          .catch(_ => {});
      },
      withdrawBoxDetail(row){
        IOT.getServerData('/box/details/withdraw','get',row,(ret) => {
          if (ret.code === 200) {
            IOT.tips('撤回成功！', 'success');
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },

    },
  }
</script>

<style scoped lang="less">
  .check {
    padding-bottom: 100px;
    .tableBox{
      padding-bottom: 0;
    }
    .checkSearch{
      border-bottom: 1px solid #DCDFE6;
      padding-bottom: 10px;
      >span{
        margin-right: 5px;
      }
    }
    .checkTop{
      margin-top: 5px;
      margin-right: 5px;
      border: 1px solid #DCDFE6;
      border-radius: 10px;
      padding: 10px 20px;
      background: #ffffff;
    }
    .checkBottom{
      margin-top: 10px;
      margin-right: 5px;
      >div{
        border: 1px solid #DCDFE6;
        border-radius: 10px;
        padding: 10px 20px;
        background: #ffffff;
      }
      .leftFloat{
        width: calc(55% - 47px);
      }
      .rightFloat{
        width: calc(45% - 47px);
      }
    }
  }
</style>
