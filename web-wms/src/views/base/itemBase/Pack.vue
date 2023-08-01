<template>
  <div class="pack clearFloat">

    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getPackMasterData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div class="item">
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU名称/SKU/联系人" clearable></el-input>
        </div>
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
      <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="dialogNewVisible = true">新增</el-button>
    </div>

    <el-dialog title="新增包装" :visible.sync="dialogNewVisible" :before-close="handleClose" width="900px">
      <div class="packMasterBox">
        <el-form class="clearFloat" :model="form.packMaster" :rules="rules" ref="form.packMaster">
          <el-form-item label="包装代码" prop="packCode" :label-width="formLabelWidth">
            <el-input v-model="form.packMaster.packCode" placeholder="请输入包装代码"></el-input>
          </el-form-item>
          <el-form-item label="包装描述" prop="packName" :label-width="formLabelWidth">
            <el-input v-model="form.packMaster.packName" placeholder="请输入包装描述"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <div class="packDetailsBox clearFloat">
       
        <ul style="width: 10%;"><li>&nbsp;</li><li v-for="(item,index) in form.packDetails" :key="index">{{labels[index]}}</li></ul>
        <ul style="width: 15%;"><li>转换系数</li><li v-for="(item,index) in form.packDetails" :key="index">
          <!--<el-input-number v-model="item.transRatio" controls-position="right" :min="0" :disabled="index===0?true:false" size="mini" style="width: 100%;"></el-input-number>-->
          <input type="number" v-model="item.transRatio" :disabled="index===0?true:false" class="packMiniInput">
<!--          <el-input v-model.number="item.transRatio" :disabled="index===0?true:false" size="mini"></el-input>-->
        </li></ul>
        <ul style="width: 15%;"><li>单位</li><li v-for="(item, index) in form.packDetails" :key="index"><el-input v-model="item.packDescribe" size="mini"></el-input></li></ul>
       <ul style="width: 15%;"><li>启用</li><li v-for="(item, index) in form.packDetails" :key="index"><el-checkbox :disabled="index===0?true:false" v-model="item.state"></el-checkbox></li></ul>
        <ul style="width: 10%;"><li>销售单位</li><li v-for="(item, index) in form.packDetails" :key="index"><el-checkbox @change="outLabel(index)" v-model="item.outboundLabel"></el-checkbox></li></ul>
       <!-- <ul style="width: 15%;"><li>包装材料</li><li v-for="(item, index) in form.packDetails" :key="index">
          <el-select v-model="item.packId" filterable placeholder="请选择包装材料" size="mini">
            <el-option
                    v-for="item in packDatas"
                    :key="item.id"
                    :label="item.packData"
                    :value="item.id">
            </el-option>
          </el-select>
        </li></ul>
        
        <ul style="width: 5%;"><li>装箱</li><li v-for="(item, index) in form.packDetails" :key="index"><el-checkbox v-model="item.boxing"></el-checkbox></li></ul>
        <ul style="width: 10%;"><li>入库标签</li><li v-for="(item, index) in form.packDetails" :key="index"><el-checkbox v-model="item.storageLabel"></el-checkbox></li></ul>
        <ul style="width: 10%;"><li>补货标签</li><li v-for="(item, index) in form.packDetails" :key="index"><el-checkbox v-model="item.replenishLabel"></el-checkbox></li></ul>
        <ul style="width: 10%;"><li>出库标签</li><li v-for="(item, index) in form.packDetails" :key="index"><el-checkbox v-model="item.outboundLabel"></el-checkbox></li></ul>
        <ul style="width: 10%;"><li>序列号</li><li v-for="(item, index) in form.packDetails" :key="index"><el-checkbox v-model="item.serialNumber"></el-checkbox></li></ul> -->
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form.packMaster');clearDetails();dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm('form.packMaster')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改包装" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="900px">
      <div class="packMasterBox">
        <el-form class="clearFloat" :model="rowData.packMaster" :rules="rules2" ref="rowData.packMaster">
          <el-form-item label="包装代码" prop="packCode" :label-width="formLabelWidth">
            <el-input v-model="rowData.packMaster.packCode" placeholder="请输入包装代码"></el-input>
          </el-form-item>
          <el-form-item label="包装描述" prop="packName" :label-width="formLabelWidth">
            <el-input v-model="rowData.packMaster.packName" placeholder="请输入包装描述"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <div class="packDetailsBox clearFloat">
        <ul style="width: 10%;"><li>&nbsp;</li><li v-for="(item,index) in rowData.packDetails" :key="index">{{labels[index]}}</li></ul>
        <ul style="width: 15%;"><li>转换系数</li><li v-for="(item,index) in rowData.packDetails" :key="index">
          <input type="number" v-model="item.transRatio" :disabled="index===0?true:false" class="packMiniInput">
        </li></ul>
        <ul style="width: 15%;"><li>单位</li><li v-for="(item,index) in rowData.packDetails" :key="index"><el-input v-model="item.packDescribe" size="mini"></el-input></li></ul>
        
        <ul style="width: 15%;"><li>启用</li><li v-for="(item, index) in rowData.packDetails" :key="index"><el-checkbox :disabled="index===0?true:false" v-model="item.state"></el-checkbox></li></ul>
        <ul style="width: 10%;"><li>销售单位</li><li v-for="(item,index) in rowData.packDetails" :key="index"><el-checkbox v-model="item.outboundLabel"></el-checkbox></li></ul>
        <!--<ul style="width: 15%;"><li>包装材料</li><li v-for="(item,index) in rowData.packDetails" :key="index">
          <el-select v-model="item.packId" filterable placeholder="请选择包装材料" size="mini">
            <el-option
                    v-for="item in packDatas"
                    :key="item.id"
                    :label="item.packData"
                    :value="item.id">
            </el-option>
          </el-select>
        </li></ul>
        <ul style="width: 5%;"><li>装箱</li><li v-for="(item,index) in rowData.packDetails" :key="index"><el-checkbox v-model="item.boxing"></el-checkbox></li></ul>
        <ul style="width: 10%;"><li>入库标签</li><li v-for="(item,index) in rowData.packDetails" :key="index"><el-checkbox v-model="item.storageLabel"></el-checkbox></li></ul>
        <ul style="width: 10%;"><li>补货标签</li><li v-for="(item,index) in rowData.packDetails" :key="index"><el-checkbox v-model="item.replenishLabel"></el-checkbox></li></ul>
        <ul style="width: 10%;"><li>出库标签</li><li v-for="(item,index) in rowData.packDetails" :key="index"><el-checkbox v-model="item.outboundLabel"></el-checkbox></li></ul>
        <ul style="width: 10%;"><li>序列号</li><li v-for="(item,index) in rowData.packDetails" :key="index"><el-checkbox v-model="item.serialNumber"></el-checkbox></li></ul>-->
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('rowData.packMaster');clearDetails();dialogUpdateVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateForm('rowData.packMaster')">确 定</el-button>
      </div>
    </el-dialog>

    <div class="tableBox organizationLeftTab">
      <el-table
              highlight-current-row
              @current-change="getLookRowData"
              :data="packMasters"
              style="width: 100%">
        <el-table-column
                type="index"
                width="50">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="packCode"
                label="包装代码">
        </el-table-column>
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="packName"
                label="包装描述">
        </el-table-column>
        <el-table-column
                label="操作"
                width="110">
          <template slot-scope="scope">
              <el-button v-if="scope.row.isSys==0" @click="showUpdateModal(scope.row)" icon="iconfont icon-bianji" type="primary" circle plain size="small"></el-button>
              <el-button  v-if="scope.row.isSys==0"  @click="isDelete(scope.row.packCode)" icon="iconfont icon-shanchu" type="danger" circle plain size="small"></el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
              style="text-align: right;"
              class="pagination-left-bottom"
              @current-change="handleCurrentChange"
              @size-change="handleSizeChange"
              :page-sizes="[15,30,50,100]"
              :current-page.sync="searchData.pageNum"
              :page-size="searchData.pageSize"
              layout="sizes, total, prev, pager, next, jumper"
              :total="searchData.total">
      </el-pagination>
    </div>

    <div class="organizationMiddle">
      <i class="el-icon-d-arrow-right"></i>
    </div>

    <div class="organizationRight">
      <div class="organizationRightTitle">包装信息</div>
      <p class="MasterBox">
        <span><b>包装代码：</b>{{nowPackData.packMaster.packCode}}</span>
        <span><b>包装描述：</b>{{nowPackData.packMaster.packName}}</span>
      </p>
      <el-divider content-position="left">详细信息</el-divider>

      <div class="tableBox">
        <el-table
                :data="nowPackData.packDetails"
                style="width: 100%">
          <el-table-column
                  label=""
                  width="80">
            <template slot-scope="scope">
              <span>{{labels[scope.$index]}}</span>
            </template>
          </el-table-column>
          <el-table-column
                  prop="transRatio"
                  label="转换系数">
          </el-table-column>
          <el-table-column
                  prop="packDescribe"
                  label="单位">
          </el-table-column>
           <el-table-column
                  label="启用">
            <template slot-scope="scope">
              <i v-show="scope.row.state" class="iconfont icon-xuanzhong1"></i>
              <i v-show="!scope.row.state" class="iconfont icon-weixuanzhong"></i>
            </template>
          </el-table-column>
          <el-table-column
                  label="销售单位">
            <template slot-scope="scope">
              <i v-show="scope.row.outboundLabel" class="iconfont icon-xuanzhong1"></i>
              <i v-show="!scope.row.outboundLabel" class="iconfont icon-weixuanzhong"></i>
            </template>
          </el-table-column>
         <!-- <el-table-column
                  prop="packData"
                  label="包装材料">
          </el-table-column>
          <el-table-column
                  label="装箱">
            <template slot-scope="scope">
              <i v-show="scope.row.boxing" class="iconfont icon-xuanzhong1"></i>
              <i v-show="!scope.row.boxing" class="iconfont icon-weixuanzhong"></i>
            </template>
          </el-table-column>
          <el-table-column
                  label="入库标签">
            <template slot-scope="scope">
              <i v-show="scope.row.storageLabel" class="iconfont icon-xuanzhong1"></i>
              <i v-show="!scope.row.storageLabel" class="iconfont icon-weixuanzhong"></i>
            </template>
          </el-table-column>
          <el-table-column
                  label="补货标签">
            <template slot-scope="scope">
              <i v-show="scope.row.replenishLabel" class="iconfont icon-xuanzhong1"></i>
              <i v-show="!scope.row.replenishLabel" class="iconfont icon-weixuanzhong"></i>
            </template>
          </el-table-column>
          <el-table-column
                  label="出库标签">
            <template slot-scope="scope">
              <i v-show="scope.row.outboundLabel" class="iconfont icon-xuanzhong1"></i>
              <i v-show="!scope.row.outboundLabel" class="iconfont icon-weixuanzhong"></i>
            </template>
          </el-table-column>
          <el-table-column
                  label="序列号">
            <template slot-scope="scope">
              <i v-show="scope.row.serialNumber" class="iconfont icon-xuanzhong1"></i>
              <i v-show="!scope.row.serialNumber" class="iconfont icon-weixuanzhong"></i>
            </template>
          </el-table-column> -->
        </el-table>
      </div>
    </div>

  </div>
</template>

<script>
  export default{
    name: 'Pack',
    data(){
      let validatePackCode = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'pack_master', 'pack_code', this.form.packMaster.packCode, 0);
      };
      let validatePackCode2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'pack_master', 'pack_code', this.rowData.packMaster.packCode, 1, this.rowData.packMaster.packMasterId);
      };
      let validatePackName = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'pack_master', 'pack_name', this.form.packMaster.packName, 0);
      };
      let validatePackName2 = (rule, value, callback) => {
        PF.validator(rule, value, callback, 'pack_master', 'pack_name', this.rowData.packMaster.packName, 1, this.rowData.packMaster.packMasterId);
      };
      return {
        isMoreSearch: false,

        searchData:{
          pageNum: 1,
          pageSize: 15,
          total: null,
          keyWords: '',
        },

        packDatas: [],
        packMasters: [],
        nowPackData: {
          packMaster: {},
          packDetails: [],
        },
        labels: ['主单位','单位2','单位3','单位4','单位5'],

        dialogNewVisible: false,
        dialogUpdateVisible: false,


        form: {
          packMaster: {
            packCode: '',
            packName: '',
            memo: ''
          },
          packDetails: [
            {
              packLevel: 1,
              transRatio: 1,
              packDescribe: '件',
              packId: null,
              boxing: false,
              storageLabel: false,
              replenishLabel: false,
              outboundLabel: true,
              serialNumber: false,
              state:true
            },
            {
              packLevel: 2,
              transRatio: 1,
              packDescribe: 'IP',
              packId: null,
              boxing: false,
              storageLabel: false,
              replenishLabel: false,
              outboundLabel: false,
              serialNumber: false,
              state:false
            },
            {
              packLevel: 3,
              transRatio: 1,
              packDescribe: '箱',
              packId: null,
              boxing: false,
              storageLabel: false,
              replenishLabel: false,
              outboundLabel: false,
              serialNumber: false,
              state:false
            },
            {
              packLevel: 4,
              transRatio: 1,
              packDescribe: 'PL',
              packId: null,
              boxing: false,
              storageLabel: false,
              replenishLabel: false,
              outboundLabel: false,
              serialNumber: false,
              state:false
            },
            {
              packLevel: 5,
              transRatio: 1,
              packDescribe: 'OT',
              packId: null,
              boxing: false,
              storageLabel: false,
              replenishLabel: false,
              outboundLabel: false,
              serialNumber: false,
              state:false
            }
          ]
        },
        rules:{
          packCode: [
            { required: true, message: '请输入包装代码', trigger: 'blur' },
            { validator: validatePackCode, trigger: 'blur'  }
          ],
          packName: [
            { required: true, message: '请输入包装描述', trigger: 'blur' },
            { validator: validatePackName, trigger: 'blur'  }
          ]
        },
        formLabelWidth: '120px',
        rowData: {
          packMaster: {
            packCode: '',
            packName: '',
            memo: ''
          },
          packDetails: [
            {
              packLevel: 1,
              transRatio: 1,
              packDescribe: '件',
              packId: null,
              boxing: false,
              storageLabel: false,
              replenishLabel: false,
              outboundLabel: false,
              serialNumber: false,
              state:false
            },
            {
              packLevel: 2,
              transRatio: 1,
              packDescribe: 'IP',
              packId: null,
              boxing: false,
              storageLabel: false,
              replenishLabel: false,
              outboundLabel: false,
              serialNumber: false,
              state:false
            },
            {
              packLevel: 3,
              transRatio: 1,
              packDescribe: '箱',
              packId: null,
              boxing: false,
              storageLabel: false,
              replenishLabel: false,
              outboundLabel: false,
              serialNumber: false,
              state:false
            },
            {
              packLevel: 4,
              transRatio: 1,
              packDescribe: 'PL',
              packId: null,
              boxing: false,
              storageLabel: false,
              replenishLabel: false,
              outboundLabel: false,
              serialNumber: false,
              state:false
            },
            {
              packLevel: 5,
              transRatio: 1,
              packDescribe: 'OT',
              packId: null,
              boxing: false,
              storageLabel: false,
              replenishLabel: false,
              outboundLabel: false,
              serialNumber: false,
              state:false
            }
          ]
        },
        rules2:{
          packCode: [
            { required: true, message: '请输入包装代码', trigger: 'blur' },
            { validator: validatePackCode2, trigger: 'blur'  }
          ],
          packName: [
            { required: true, message: '请输入包装描述', trigger: 'blur' },
            { validator: validatePackName2, trigger: 'blur'  }
          ]
        },
        normalPackDetail: [
          {
            packLevel: 1,
            transRatio: 1,
            packDescribe: '件',
            packId: null,
            boxing: false,
            storageLabel: false,
            replenishLabel: false,
            outboundLabel: false,
            serialNumber: false,
            state:true
          },
          {
            packLevel: 2,
            transRatio: 1,
            packDescribe: 'IP',
            packId: null,
            boxing: false,
            storageLabel: false,
            replenishLabel: false,
            outboundLabel: false,
            serialNumber: false,
            state:false
          },
          {
            packLevel: 3,
            transRatio: 1,
            packDescribe: '箱',
            packId: null,
            boxing: false,
            storageLabel: false,
            replenishLabel: false,
            outboundLabel: false,
            serialNumber: false,
            state:false
          },
          {
            packLevel: 4,
            transRatio: 1,
            packDescribe: 'PL',
            packId: null,
            boxing: false,
            storageLabel: false,
            replenishLabel: false,
            outboundLabel: false,
            serialNumber: false,
            state:false
          },
          {
            packLevel: 5,
            transRatio: 1,
            packDescribe: 'OT',
            packId: null,
            boxing: false,
            storageLabel: false,
            replenishLabel: false,
            outboundLabel: false,
            serialNumber: false,
            state:false
          }
        ],
      }
    },
    methods: {
      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getPackMasterData();
      },

      handleCurrentChange(val) {
        this.searchData.pageNum = val;
        this.getPackMasterData()
      },
      clearSearchData(){
        this.searchData.keyWords = '';
      },
      getPackMasterData(){
        IOT.getServerData('/pack/masters/list','get',this.searchData,(ret) => {
          if (ret.code === 200) {
            this.packMasters = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber;
            this.getLookRowData(ret.rows[0])
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getLookRowData(row){
        if (!row) {
          return
        }
        this.nowPackData.packMaster = JSON.parse(JSON.stringify(row));
        this.getPackDetailData(this.nowPackData.packMaster.packCode)
      },
      getPackDetailData(code,type){
        IOT.getServerData('/pack/details/list2','get',{packCode: code},(ret) => {
          if (ret.code === 200) {
            if(type === 'update'){
              this.rowData.packDetails = ret.rows
            }else{
              this.nowPackData.packDetails = ret.rows;
            }
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getPackDataData(){
        IOT.getServerData('/packs/list','get',this.searchData,(ret) => {
          if (ret.code === 200) {
            this.packDatas = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
      handleClose(done) {
        // this.$confirm('确认关闭？')
        //     .then(_ => {
        this.resetForm('form.packMaster');
        this.clearDetails();
        this.dialogNewVisible = false
        //     done();
        // })
        // .catch(_ => {});
      },
      handleClose2(done) {
        // this.$confirm('确认关闭？')
        //     .then(_ => {
        this.resetForm('rowData.packMaster');
        this.clearDetails();
        this.dialogUpdateVisible = false
        //     done();
        // })
        // .catch(_ => {});
      },
      clearDetails(){
        this.form.packDetails = PF.JSON(this.normalPackDetail);
        this.rowData.packDetails = PF.JSON(this.normalPackDetail);
      },
      commitForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let form = PF.JSON(this.form);
            for(let i=0;i<form.packDetails.length;i++){
              form.packDetails[i].transRatio = Number(form.packDetails[i].transRatio);
              if(form.packDetails[i].transRatio < 1){
                IOT.tips('系数需大于主单位！', 'error', 1000 , () => {
                  IOT.hideOverlay();
                });
                return false
              }
            }
           
            IOT.getServerData('/pack/masters/insert','post',form,(ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getPackMasterData()
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm('form.packMaster')
            this.clearDetails()
            this.dialogNewVisible = false
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      showUpdateModal(row){
        this.getPackDetailData(row.packCode,'update');
        this.rowData.packMaster = JSON.parse(JSON.stringify(row));
        this.dialogUpdateVisible = true
      },
      updateForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let rowData = PF.JSON(this.rowData);
            for(let i=0;i<rowData.packDetails.length;i++){
              rowData.packDetails[i].transRatio = Number(rowData.packDetails[i].transRatio);
              if(rowData.packDetails[i].transRatio < 1){
                IOT.tips('系数需大于主单位！', 'error', 1000 , () => {
                  IOT.hideOverlay();
                });
                return false
              }
            }
           
            IOT.getServerData('/pack/masters/update','post',rowData,(ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.nowPackData.packMaster = {};
                  that.getPackMasterData()
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm('rowData.packMaster')
            this.dialogUpdateVisible = false
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      deletePack(code){
        let that = this;
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/pack/masters/delete','get',{packCode: code},(ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('删除成功！', 'success', 1000 , () => {
              that.nowPackData.packMaster = {};
              that.getPackMasterData()
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      isDelete(code){
        this.$confirm('确认删除？')
          .then(_ => {
            this.deletePack(code)
          })
          .catch(_ => {});
      },
    },
    created() {
      this.getPackMasterData();
      this.getPackDataData();
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .pack{

  }
</style>
