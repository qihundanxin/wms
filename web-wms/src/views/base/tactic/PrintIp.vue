<template>
  <div class="batchTactic clearFloat">

    <div class="publicSearch">
      <div class="searchBar" >
        <el-button-group>
          <el-button @click="getBatchTacticData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <!-- <div class="item">
          <span>关键字搜索</span>
          <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU名称/SKU" clearable></el-input>
        </div> -->
      </div>
      <span class="moreSearchBtn"
            :class="isMoreSearch?'el-icon-caret-top':'el-icon-caret-bottom'"
            @click="isMoreSearch = !isMoreSearch"></span>
    </div>

    <div class="headerBtns">
      <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="dialogNewVisible = true">新增</el-button>
    </div>

    <el-dialog title="新增打印机参数" :visible.sync="dialogNewVisible" :before-close="handleClose" width="1200px" style="margin-top: -10vh">
      <div class="batchTacticMasterBox">
        <el-form class="clearFloat" :model="form.printIp" :rules="rules" ref="form.printIp">
                 
                
          <el-form-item label="LODOP_IP" prop="ip" :label-width="formLabelWidth">
            <el-input  @blur="openYc(form.printIp.ip,1)" v-model="form.printIp.ip" placeholder="请输LODOP_IP"></el-input>
          </el-form-item>
          <el-form-item label="备注" prop="memo" :label-width="formLabelWidth">
            <el-input v-model="form.printIp.memo" placeholder="请输入备注"></el-input>
          </el-form-item>
          
        </el-form>
      </div>
      <div class="batchTacticDetailBox clearFloat">  
        <ul style="width: 10%;"><li>打印模板</li><li v-for="(item,index) in form.list" :key="index">{{item.type}}</li></ul>
        <!-- <ul style="width: 10%;"><li>LODOP_IP</li><li v-for="(item,index) in form.list" :key="index">
          <el-input  @blur="openYc(item.ip,item.index)" v-model="item.ip" size="mini"></el-input>
          </li></ul> -->
        
        <ul style="width: 20%;"><li>打印机</li><li v-for="(item,index) in form.list" :key="index">
          
                    <select
                        :id="item.index"
                        @change="demoCreatePagSizeList(item.printerIndex,'size'+item.index)"
                        v-model="item.printerIndex"
                        style="
                            height: 28px;
                            width: 100%;
                            border: 1px solid rgba(220, 223, 230);
                        "
                    >
                      
                    </select>
               
          
          <!-- <el-input v-model="item.printIndex" size="mini"></el-input> -->
        </li></ul> 
        <ul style="width: 15%;"><li>纸张尺寸</li><li v-for="(item,index) in form.list" :key="index">
          <select      :id="'size'+item.index"
                        v-model="item.printSize"
                        style="
                            height: 28px;
                            width: 100%;
                            border: 1px solid rgba(220, 223, 230);   "   >
                       
                    </select>
          <!-- <el-input v-model="item.printSize" size="mini"></el-input> -->
          </li></ul> 
        <ul style="width: 8%;"><li>上边距</li><li v-for="(item,index) in form.list" :key="index"><el-input v-model="item.printTop" size="mini"></el-input></li></ul> 
        <ul style="width: 8%;"><li>左边距</li><li v-for="(item,index) in form.list" :key="index"><el-input v-model="item.printLeft" size="mini"></el-input></li></ul> 
        <!-- <ul style="width: 8%;"><li>宽</li><li v-for="(item,index) in form.list" :key="index"><el-input v-model="item.printWidth" size="mini"></el-input></li></ul> 
        <ul style="width: 8%;"><li>高</li><li v-for="(item,index) in form.list" :key="index"><el-input v-model="item.printHeight" size="mini"></el-input></li></ul>   -->
        <ul style="width: 10%;"><li>方向</li><li v-for="(item,index) in form.list" :key="index">
          <el-select  size="mini" v-model="item.printFangxiang" placeholder="请选择">
              <el-option    key=1   label="纵向"   value=1>   </el-option>
               <el-option    key=2   label="横向"   value=2>   </el-option>
            </el-select>
            <!-- <el-radio size="mini" v-model="item.printFangxiang" label=1
                            >纵向</el-radio
                        >
                        <el-radio v-model="item.printFangxiang" label=2
                            >横向</el-radio
                        > -->
          <!-- <el-input v-model="item.printFangxiang" size="mini"></el-input> -->
          
          </li></ul>  
        <ul style="width: 10%;"><li>单面/双面</li><li v-for="(item,index) in form.list" :key="index">
           <el-select size="mini" v-model="item.printFangxiang" placeholder="请选择">
              <el-option    key=1   label="单面"   value=1>   </el-option>
               <el-option    key=2   label="双面"   value=2>   </el-option>
            </el-select>
          <!-- <el-input v-model="item.printDuplex" size="mini"></el-input> -->
          </li></ul>  
          
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form.printIp');clearDetails();dialogNewVisible = false">取 消</el-button>
        <el-button type="primary" @click="commitForm('form.printIp')">确 定</el-button>
      </div>
    </el-dialog>
  

    <div class="tableBox organizationLeftTab">
      <el-table
              v-loading="loading"
              highlight-current-row
              @current-change="getLookRowData"
              :data="batchTactics"
              style="width: 100%">
        <el-table-column
                type="index"
                width="50">
        </el-table-column>
        <!-- <el-table-column
                sortable
                show-overflow-tooltip
                prop="ip"
                label="LODOP_IP">
        </el-table-column> -->
        <el-table-column
                sortable
                show-overflow-tooltip
                prop="ip"
                label="LODOP_IP">
                <template slot-scope="scope">
                  <span v-if="ip==scope.row.printIpId">{{scope.row.ip}}（当前）</span>
                  <span v-else >{{scope.row.ip}} </span>
                   
          </template>
         
                  
          
        </el-table-column>

         
         <el-table-column
                sortable
                show-overflow-tooltip
                prop="memo"
                label="备注">
        </el-table-column>
        <el-table-column
                label="操作"
                width="110">
          <template slot-scope="scope">
              <el-button v-if="!scope.row.isSys" @click="setPrintParam(scope.row.printIpId)"  type="primary"     size="small">设为当前</el-button>
              
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
      <div class="organizationRightTitle">打印机信息</div>
      <p class="MasterBox">
        <span><b>IP：</b>{{nowPrintIp.ip}}</span>
        <span><b>描述：</b>{{nowPrintIp.memo}}</span>
      </p>
      <el-divider content-position="left">详细信息</el-divider>

      <div class="tableBox" style="height: 400px;overflow: auto">
        <el-table
                v-loading="loading"
                highlight-current-row
                :data="nowPrintIp.list"
                style="width: 100%">
          <el-table-column
                  type="index"
                  width="50">
          </el-table-column>
          <el-table-column     show-overflow-tooltip  prop="type"  label="打印模板">  </el-table-column> 
          <el-table-column    show-overflow-tooltip    prop="printerIndex"   label="打印机Index"> </el-table-column> 
          <el-table-column    show-overflow-tooltip    prop="printSize"   label="纸张尺寸"> </el-table-column> 
          <el-table-column    show-overflow-tooltip    prop="printTop"   label="上边距"> </el-table-column> 
          <el-table-column    show-overflow-tooltip    prop="printLeft"   label="左边距"> </el-table-column> 
          <!-- <el-table-column    show-overflow-tooltip    prop="printWidth"   label="宽"> </el-table-column> 
          <el-table-column    show-overflow-tooltip    prop="printHeight"   label="高"> </el-table-column>  -->
          <el-table-column    show-overflow-tooltip    prop="printFangxiang"   label="方向"> </el-table-column> 
          <el-table-column    show-overflow-tooltip    prop="printDuplex"   label="单页/双页"> </el-table-column> 
          
         
        </el-table>
      </div>
    </div>

  </div>
</template>

<script>
  export default{
    name: 'BatchTactic',
    data(){
       return {
        ip:PF.getLocal("lodop_id"),
        isMoreSearch: false,
        
        loading:false,
        searchData:{
          wareId:PF.getLocal('wareId', 'number'),
          pageNum: 1,
          pageSize: 100,
          total: null,
          keyWords: '',
        },

        batchTactics: [],
        nowPrintIp: {
         
        },

        dialogNewVisible: false,
        dialogUpdateVisible: false,

        innerVisible1: false,
        innerVisible2: false,

       
        form: {
          printIp: {
            ip: 'localhost',
            memo: '',
            wareId:PF.getLocal('wareId', 'number'),
            wareName:PF.getLocal('wareName'),
          },
          list: [
            {index:'1', type:'验收标签',printerIp:'',printerIndex:'', printSize:'', printTop:0, printLeft:0, printWidth:'0',  printHeight:'0', printFangxiang:"1",  printDuplex:1, }
           ,{index:'2',  type:'商品标签',printerIp:'',printerIndex:'', printSize:'', printTop:0, printLeft:0, printWidth:'0',  printHeight:'0', printFangxiang:"1",  printDuplex:1, }
           ,{index:'3',  type:'货位标签',printerIp:'',printerIndex:'', printSize:'', printTop:0, printLeft:0, printWidth:'0',  printHeight:'0', printFangxiang:"1",  printDuplex:1, }
           ,{index:'4',  type:'拣货标签',printerIp:'',printerIndex:'', printSize:'', printTop:0, printLeft:0, printWidth:'0',  printHeight:'0', printFangxiang:"1",  printDuplex:1, }
           ,{index:'5',  type:'快递面单',printerIp:'',printerIndex:'', printSize:'', printTop:0, printLeft:0, printWidth:'0',  printHeight:'0', printFangxiang:"1",  printDuplex:1, }
           ,{index:'6',  type:'装箱标签',printerIp:'',printerIndex:'', printSize:'', printTop:0, printLeft:0, printWidth:'0',  printHeight:'0', printFangxiang:"1",  printDuplex:1, }
           ,{index:'7',  type:'货位组标签',printerIp:'',printerIndex:'', printSize:'', printTop:0, printLeft:0, printWidth:'0',  printHeight:'0', printFangxiang:"1",  printDuplex:1, }
           ,{index:'8',  type:'货位标签',printerIp:'',printerIndex:'', printSize:'', printTop:0, printLeft:0, printWidth:'0',  printHeight:'0', printFangxiang:"1",  printDuplex:1, }
           ,{index:'9',  type:'二维码/条形码',printerIp:'',printerIndex:'', printSize:'', printTop:0, printLeft:0, printWidth:'0',  printHeight:'0', printFangxiang:"1",  printDuplex:1, }
           ,{index:'10',  type:'出库单',printerIp:'',printerIndex:'', printSize:'', printTop:0, printLeft:0, printWidth:'0',  printHeight:'0', printFangxiang:"1",  printDuplex:1, }
          
          
          
          ],
         
        },

        normalForm: {
          printIp: {
            ip: 'localhost',
            memo: '',
            wareId:PF.getLocal('wareId', 'number'),
            wareName:PF.getLocal('wareName'),
          },
          list: [
            {index:'1', type:'验收标签',printerIp:'',printerIndex:'', printSize:'', printTop:0, printLeft:0, printWidth:'0',  printHeight:'0', printFangxiang:"1",  printDuplex:1, }
           ,{index:'2',  type:'商品标签',printerIp:'',printerIndex:'', printSize:'', printTop:0, printLeft:0, printWidth:'0',  printHeight:'0', printFangxiang:"1",  printDuplex:1, }
           ,{index:'3',  type:'货位标签',printerIp:'',printerIndex:'', printSize:'', printTop:0, printLeft:0, printWidth:'0',  printHeight:'0', printFangxiang:"1",  printDuplex:1, }
           ,{index:'4',  type:'拣货标签',printerIp:'',printerIndex:'', printSize:'', printTop:0, printLeft:0, printWidth:'0',  printHeight:'0', printFangxiang:"1",  printDuplex:1, }
           ,{index:'5',  type:'快递面单',printerIp:'',printerIndex:'', printSize:'', printTop:0, printLeft:0, printWidth:'0',  printHeight:'0', printFangxiang:"1",  printDuplex:1, }
           ,{index:'6',  type:'装箱标签',printerIp:'',printerIndex:'', printSize:'', printTop:0, printLeft:0, printWidth:'0',  printHeight:'0', printFangxiang:"1",  printDuplex:1, }
           ,{index:'7',  type:'货位组标签',printerIp:'',printerIndex:'', printSize:'', printTop:0, printLeft:0, printWidth:'0',  printHeight:'0', printFangxiang:"1",  printDuplex:1, }
           ,{index:'8',  type:'货位标签',printerIp:'',printerIndex:'', printSize:'', printTop:0, printLeft:0, printWidth:'0',  printHeight:'0', printFangxiang:"1",  printDuplex:1, }
           ,{index:'9',  type:'二维码/条形码',printerIp:'',printerIndex:'', printSize:'', printTop:0, printLeft:0, printWidth:'0',  printHeight:'0', printFangxiang:"1",  printDuplex:1, }
           ,{index:'10',  type:'出库单',printerIp:'',printerIndex:'', printSize:'', printTop:0, printLeft:0, printWidth:'0',  printHeight:'0', printFangxiang:"1",  printDuplex:1, }
          
          
          ],
 
            
        },
        rules:{
          ip: [
            { required: true, message: '请输入IP', trigger: 'blur' },
            
          ] 
        },
        formLabelWidth: '120px',
        rowData: {
         printIp: {
            ip: 'localhost',
            memo: ''
          },
          list: [
            { type:'',printerIndex:'', printSize:'', printTop:0, printLeft:0, printWidth:'0',  printHeight:'0', printFangxiang:'1',  printDuplex:'1', }
           
          ],
        },
        
        
      }
    },
    methods: {
      setPrintParam(id){
        var that = this;
          IOT.setPrintParam(id,
            () =>{ 
                that.ip =PF.getLocal("lodop_id");
            }
        );
      },
      //获取打印机纸张大小
        demoCreatePagSizeList(printIndex,sizeId) { 
            var oSelect = document.getElementById(sizeId);
            CLODOP.Create_PageSize_List(oSelect, printIndex); 
        },
      //获取打印机列表
        demoCreatePrinterList(id) { 
            var ele = document.getElementById(id);
            CLODOP.Create_Printer_List(ele); 
        },
      setPrinter(id) { 
            setTimeout(this.demoCreatePrinterList(id), 1000); 
      },
      openYc(ip,type){
        var aaa = IOT.userSetClodopJS(ip+":8000");
           if(aaa!=undefined && aaa!=null){
               if(type==1){
                  this.setPrinter("1"); 
                  this.setPrinter("2"); 
                  this.setPrinter("3"); 
                  this.setPrinter("4"); 
                  this.setPrinter("5"); 
                  this.setPrinter("6"); 
                  this.setPrinter("7"); 
                  this.setPrinter("8"); 
                  this.setPrinter("9"); 
                  this.setPrinter("10"); 
              } 
           }  ;
           
      },
      handleSizeChange(val){
        this.searchData.pageSize = val;
        this.getBatchTacticData();
      },

      handleCurrentChange(val) {
        this.searchData.pageNum = val;
        this.getBatchTacticData()
      },
      clearSearchData(){
        this.searchData.keyWords = '';
      },
      getBatchTacticData(){
        let that = this;
        that.loading = true;
        this.batchTactics = [];
        let searchData = PF.JSON(this.searchData);
        IOT.getServerData('/print/ips/list','get',searchData,(ret) => {
          that.loading = false;
          if (ret.code === 200) {
            this.batchTactics = ret.rows;
            this.searchData.total = ret.total;
            this.searchData.pageNum = ret.pageNumber;
            this.getLookRowData(ret.rows[0])
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getLookRowData(row){
        if(row === null){
          return false
        }
        this.nowPrintIp = JSON.parse(JSON.stringify(row));
        
      },
      getBatchTacticDetailData(code,type){

        IOT.getServerData('/batch/tactic/details/list','get',{batchTacticCode: code},(ret) => {
          if (ret.code === 200) {
            this.loading = false;
            if(type === 'update'){
              let list = JSON.parse(JSON.stringify(ret.rows));
              for(let i in list){
                list[i].state = String(list[i].state);
                list[i].isRequired = String(list[i].isRequired);
                list[i].isShow = String(list[i].isShow);
              }
              if(list[12].format === 4){
                this.nowDetailDetails1 = [];
                for(let i in list[12].batchTacticDetailDetails){
                  this.nowDetailDetails1.push({detailCode: 'batchAttribute1',batchTacticDetailDetailValue: list[12].batchTacticDetailDetails[i].batchTacticDetailDetailValue})
                }
              }
              if(list[13].format === 4){
                this.nowDetailDetails2 = [];
                for(let i in list[13].batchTacticDetailDetails){
                  this.nowDetailDetails2.push({detailCode: 'batchAttribute2',batchTacticDetailDetailValue: list[13].batchTacticDetailDetails[i].batchTacticDetailDetailValue})
                }
              }
              this.rowData.batchTacticDetails = list
            }else{
              this.nowBatchTactic.batchTacticDetails = ret.rows;
            }
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
        this.resetForm('form.batchTactic');
        this.clearDetails();
        this.dialogNewVisible = false
        //     done();
        // })
        // .catch(_ => {});
      },
      handleClose2(done) {
        // this.$confirm('确认关闭？')
        //     .then(_ => {
        this.resetForm('rowData.batchTactic');
        this.clearDetails();
        this.dialogUpdateVisible = false
        //     done();
        // })
        // .catch(_ => {});
      },
      clearDetails(){ 
        this.form =this.normalForm; 
      },
      commitForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let form = JSON.parse(JSON.stringify(this.form));
            
            IOT.getServerData('/print/ips/insert','post',form,(ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.getBatchTacticData()
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm('form.batchTactic');
            this.clearDetails();
            this.dialogNewVisible = false
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      showUpdateModal(row){
        this.getBatchTacticDetailData(row.batchTacticCode,'update');
        this.rowData.batchTactic = JSON.parse(JSON.stringify(row));
        this.dialogUpdateVisible = true
      },
      updateForm(formName) {
        let that = this;
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let rowData = JSON.parse(JSON.stringify(this.rowData));
            let nowDetailDetails1 = JSON.parse(JSON.stringify(this.nowDetailDetails1));
            let nowDetailDetails2 = JSON.parse(JSON.stringify(this.nowDetailDetails2));
            for(let i in rowData.batchTacticDetails){
              rowData.batchTacticDetails[i].state = Number(rowData.batchTacticDetails[i].state);
              rowData.batchTacticDetails[i].isRequired = Number(rowData.batchTacticDetails[i].isRequired);
              rowData.batchTacticDetails[i].isShow = Number(rowData.batchTacticDetails[i].isShow);
            }
            if(rowData.batchTacticDetails[12].format === 4){
              for(let i in nowDetailDetails1){
                rowData.batchTacticDetailDetails.push(nowDetailDetails1[i])
              }
            }
            if(rowData.batchTacticDetails[13].format === 4){
              for(let i in nowDetailDetails2){
                rowData.batchTacticDetailDetails.push(nowDetailDetails2[i])
              }
            }
            IOT.getServerData('/batch/tactics/update','post',rowData,(ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success', 1000 , () => {
                  that.nowBatchTactic.batchTactic = {};
                  that.getBatchTacticData()
                });
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm('rowData.batchTactic')
            this.dialogUpdateVisible = false
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      deleteBatchTactic(id){
        let that = this;
        IOT.showOverlay('删除中，请稍等...');
        IOT.getServerData('/batch/tactics/delete','get',{id: id},(ret) => {
          IOT.hideOverlay();
          if (ret.code === 200) {
            IOT.tips('删除成功！', 'success', 1000 , () => {
              that.nowBatchTactic.batchTactic = {};
              that.getBatchTacticData()
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      isDelete(code){
        this.$confirm('确认删除？')
          .then(_ => {
            this.deleteBatchTactic(code)
          })
          .catch(_ => {});
      },
      showDetailDetailValue(index){
        if(index === 12){
          this.innerVisible1 = true
        }else if(index === 13){
          this.innerVisible2 = true
        }
        // this.form.batchTacticDetails[index].format = 4
      },
      addInput(index,i){
        if(i === 1){
          this.nowDetailDetails1.splice(index+1, 0, {detailCode: "batchAttribute1",batchTacticDetailDetailValue: ""});
        }else if(i === 2){
          this.nowDetailDetails2.splice(index+1, 0, {detailCode: "batchAttribute2",batchTacticDetailDetailValue: ""});
        }
      },
      deleteInput(index,i){
        if(i === 1){
          this.nowDetailDetails1.splice(index,1)
        }else if(i === 2){
          this.nowDetailDetails2.splice(index,1)
        }
      },
      handleCloseInnerVisible1(){
        for(let i in this.nowDetailDetails1){
          if(this.nowDetailDetails1[i].batchTacticDetailDetailValue === ''){
              IOT.tips('不能为空！', 'error');
            return false
          }
        }
        this.form.batchTacticDetails[12].format = 4;
        this.innerVisible1 = false
      },
      handleCloseInnerVisible2(){
        for(let i in this.nowDetailDetails2){
          if(this.nowDetailDetails2[i].batchTacticDetailDetailValue === ''){
              IOT.tips('不能为空！', 'error');
            return false
          }
        }
        this.form.batchTacticDetails[13].format = 4;
        this.innerVisible2 = false
      },
      closeInput(i){
        if(i === 1){
          this.nowDetailDetails1 = [{detailCode: "batchAttribute1",batchTacticDetailDetailValue: ''}]
          this.form.batchTacticDetails[12].format = 0;
          this.innerVisible1 = false
        }else if(i === 2){
          this.nowDetailDetails2 = [{detailCode: "batchAttribute2",batchTacticDetailDetailValue: ''}]
          this.form.batchTacticDetails[13].format = 0;
          this.innerVisible2 = false
        }
      },
      getSysParamMasterDetailData(){
        IOT.getServerData('/sys/params','get',{moduleCode: "tactic"},(ret) => {
          if (ret.code === 200) {
            let sysParams = ret.rows;
            for(let i in this.form.batchTacticDetails){
              for(let j in sysParams){
                if(this.form.batchTacticDetails[i].detailCode === sysParams[j].paramCode){
                  this.form.batchTacticDetails[i].detailName = sysParams[j].value;
                  this.rowData.batchTacticDetails[i].detailName = sysParams[j].value;
                  this.normalFormDetails[i].detailName = sysParams[j].value;
                }
              }
            }
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
    },
    created() {
      this.getBatchTacticData();
      this.getSysParamMasterDetailData();
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .batchTactic{

  }
</style>
