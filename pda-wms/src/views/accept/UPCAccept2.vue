<template>
  <div v-loading="loading"  class="baseAccept2">
    <FeaturesHeader :headerTitle="headerTitle" />
    <div class="scanInput">
        <el-input clearable   placeholder="请扫描UPC或者SKU"   id="upc" v-model="upc"></el-input>
        <i  class="el-icon-camera"   @click="scan(1)" ></i>
    </div>

    <el-dialog  title="异常" :visible.sync="exDialogVisible"   width="80%"  >
       <div style="width:90%" v-if="detail!=null"  >
              异常数量：
              <el-input-number :controls='controls' @focus="input0Set" v-model="detail.exQuantity"
                               controls-position="right" :min="0"
                               label="请输入数量" size="small"
                               style="width: 90%;"></el-input-number>
              <div style="height:5px"></div>
              异常类型：
              <el-select size="small" style="width:90%" placeholder="异常类型" v-model="detail.exType">
                <el-option value=1 key=1 label="包裹异常"></el-option>
                <el-option value=2 key=2 label="商品异常"></el-option>
              </el-select>
              <div style="height:5px"></div>
              异常原因：
              <el-select size="small" style="width:90%" placeholder="异常原因" v-model="detail.exReason">
                <el-option :value="item.value" :key="item.value" :label="item.label" v-for="item in exReasons">
                </el-option>
              </el-select>
              <div style="height:5px"></div>
              异常描述：
              <el-input
                  type="textarea"
                  :autosize="{ minRows: 2, maxRows: 4}"
                  placeholder="请输入异常描述"
                  v-model="detail.exMemo">
              </el-input>
              <!-- <div style="height:5px"></div>
              异常图片：
              <el-upload
                  :id="'img'+detail.index"
                  style="display:inline"
                  class="upload-demo"
                  name="model"
                  :data=shipInfo
                  ref="upload"
                  :action="baseUrl+'/accept/ex/imgs/add'"

                  :file-list="detail.fileList"
                  :auto-upload="true">
                <el-button slot="trigger" size="small" type="primary">选取文件</el-button> -->
                <!-- <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button> -->
                <!-- <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div> -->
              <!-- </el-upload> -->


     </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="exDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="acceptEx(detail)">确 定</el-button>
        
      </span>
    </el-dialog>

    <el-dialog  title="打印"   :visible.sync="printDialogVisible"   width="80%"  >
       <div style="width:90%"   >
              打印数量
              <el-input-number :controls='controls' @focus="input0Set" v-model="printQuantity"
                               controls-position="right" :min="0"
                               label="打印数量" size="small"
                               style="width: 90%;"></el-input-number> 
     </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="printDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="print1()">确 定</el-button>
        
      </span>
    </el-dialog>

    

    <div 
    v-if="detail!=null && asnDetails[0]!=undefined &&detail.asnDetailId==asnDetails[0].asnDetailId && detail.exState!=1"
     class="scanInput">
        <el-input clearable   placeholder="请扫描货位编码"   id="toCellCode" v-model="detail.toCellCode"></el-input>
        <i  class="el-icon-camera"   @click="scan(2)" ></i>
    </div>
    
     <div v-loading="loading" style="overflow: auto; height: 500px; margin-top:5px ">
        <el-form 
                      v-if="detail!=null&& asnDetails[0]!=undefined&&detail.asnDetailId==asnDetails[0].asnDetailId && detail.exState!=1"
                   :model="detail"   ref="acceptForm" size="mini"  label-width="70px"  :rules="rules" >
                    <el-form-item v-if="detail.toQuantity!=null" label="数量" prop="toQuantity"> 
                      <el-input   @input="onInput()"  v-model="detail.toQuantity"  style="width: 90%;"></el-input>
                    </el-form-item>
                    <el-form-item scope="toPackDetailId" label="单位">
                      <el-select v-model="detail.toPackDetailId"  @change="packChange(packDetail)"  filterable placeholder="请选择单位" style="width: 90%;">
                                          <el-option
                                            v-for="packDetail in detail.packDetails"
                                            :key="packDetail.packDetailId1"
                                            :label="packDetail.packDescribe1"
                                            :value="packDetail.packDetailId1">
                                          </el-option>
                                        </el-select>
                    </el-form-item>
                    <el-form-item  scope="toCellCode"  label="货位">
                        <el-select     v-model="detail.toCellCode"   filterable placeholder="请选择货位"  style="width:90%;">
                                          <el-option
                                            v-for="cell in cells"
                                            :key="cell.cellCode"
                                            :label="cell.cellCode"
                                            :value="cell.cellCode">
                                          </el-option>
                                        </el-select>
                      <el-input style="top: -28px;width:75%" v-model="detail.toCellCode"></el-input>
                       
                    </el-form-item>
                    <el-form-item  style="margin-top:-28px"  label="追踪号">
                      <el-input   v-model="detail.toBoxCode"  style="width: 90%;"></el-input>
                    </el-form-item>
                    
                  
                  <el-form-item 
                   v-if="item.detailCode != 'detailNo' 
                   && item.detailCode != 'supplierCode'
                   && item.detailCode != 'inDate'
                   &&  !(item.detailCode == 'supplierBatch' && detail.batch!=undefined  && detail.batch!=null    && detail.batch!='' )
                   "
                  v-for="item in batchTactics.batchTacticDetails" :label="item.detailName" :prop="item.detailCode"
                         >
                    <el-select
                        
                          v-if="item.detailCode === 'qcState'" v-model="detail[item.detailCode]" filterable size="small"
                              :placeholder="'请选择'+item.detailName" style="width: 90%;">
                      <el-option
                        v-for="item2 in qcStates"
                        :key="item2.value"
                        :label="item2.label"
                        :value="item2.value">
                      </el-option>
                    </el-select>

                    <el-input
                         
                          v-else-if="item.format === 0" v-model="detail[item.detailCode]" :placeholder="'请输入'+item.detailName"
                              size="small"
                              style="width: 90%;"></el-input>
                    <el-input-number
                         
                          v-else-if="item.format === 1" v-model="detail[item.detailCode]"
                                    :min="0" label="请输入" size="small" style="width: 90%;"></el-input-number>
                   
                    <el-date-picker
                         
                          v-else-if="item.format === 2" v-model="detail[item.detailCode]" value-format="yyyy-MM-dd"
                    
                                    type="date" :placeholder="'请选择'+item.detailName" size="small" style="width: 90%;"></el-date-picker>
                    <el-time-picker
                         
                          v-else-if="item.format === 3" v-model="detail[item.detailCode]" value-format="HH:mm:ss"
                                    :placeholder="'请选择'+item.detailName" size="small" style="width: 90%;"></el-time-picker>
                    <el-select
                         
                          v-else-if="item.format === 4" v-model="detail[item.detailCode]" filterable
                              :placeholder="'请选择'+item.detailName" size="small" style="width: 90%;">
                      <el-option
                        v-for="item2 in item.values"
                        :key="item2.batchTacticDetailDetailValue"
                        :label="item2.batchTacticDetailDetailValue"
                        :value="item2.batchTacticDetailDetailValue">
                      </el-option>
                    </el-select>
                  </el-form-item>
           
                  

                    

                  </el-form>
                  <div v-if="detail!=null && detail.exState==1">
                    <el-tag type="danger">异常已经上报，请等待。。</el-tag>
                  </div>
                  
                <div v-for="(asnDetail,index) in asnDetails"   style="  width:94%;   margin-top:10px ;margin-left:3%">
                  <el-row>
                    <el-col :span="9">   <el-image   style="width: 80px; height: 80px"  :src="asnDetail.imgUrl"   ></el-image> 
                    </el-col>
                    <el-col :span="15"> 
                        <div  style="padding-left:5px;padding-top:0px;font-size:15px"   > 
                                    <div  >  
                                          <span>SKU：</span>  
                                          <span>{{asnDetail.itemCode}} </span>  
                                    </div>  
                                     <div v-if=" asnDetail.upclist.length>0" >  
                                          <span>UPC：</span>  
                                          <span v-for="(upc,index) in asnDetail.upclist">
                                            <span v-if="index>0">-</span>
                                            {{upc.upc}} 
                                            </span>  
                                    </div>  
                                     <div  >  
                                          <span>名称：</span>  
                                          <span>{{asnDetail.itemName}} </span>
                                    </div>  
                                    <div  >  
                                          <span>预约数：</span>  
                                          <span>{{asnDetail.expectQuantity/asnDetail.transRatio}}{{asnDetail.packDescribe}} </span>

                                         
                                          （{{asnDetail.expectQuantity}}
                                          主单位）
                                    </div>  
                                     <div  >  
                                          <span>已验收数：</span>  
                                          <span>{{asnDetail.acceptQuantity/asnDetail.transRatio}}{{asnDetail.packDescribe}} </span> 
                                          
                                          （{{asnDetail.acceptQuantity}}
                                          主单位）
                                    </div> 
                                    <el-button type="text" @click="print(asnDetail.itemCode)">打印</el-button>
                                   
                                    
                                    
                                    <!-- <div>   
                                            <span v-if="exTask!=null &&item.pickTaskReturnId ==exTask.pickTaskReturnId" style="color:#67C23A; ">请上架</span> 
                                              <span v-else-if="item.state==1" style="color:#67C23A; ">完成</span>   
                                            <span v-else-if="item.state==0" style="color:#909399; ">待上架</span> 
                                    </div>  -->
                                    
                        </div> 
                    </el-col>
                  </el-row> 
                 
                 <div  v-if="detail!=null&&detail.asnDetailId==asnDetail.asnDetailId" >
                   <el-button style="width:47%" @click="exDialogVisible = true" type="warning" plain>异常</el-button>
                  
                  <el-button  @click="isCommit"   style="width:47%"   type="primary">验收</el-button>  
                   
                 </div>
 
  
                     <div v-else style="color:#FFFFFF;line-height:20px;width:100%;height:20px;background-color: #909399;"> 
                                {{asnDetail.itemCode}}            
                  </div>     
                </div>
         </div> 
         

  

  
    <!-- <FeaturesFooter @isCommit="isCommit" :footerBtn="footerBtn" /> -->
  </div>
</template>
<script>
  import FeaturesHeader from "@/components/FeaturesHeader";
  import FeaturesFooter from "@/components/FeaturesFooter";
  import {mapState} from "vuex";

  export default {
    name: "BaseAccept2",
    data() {
      return {
       
        printItemCode:'',
        printQuantity:'',
        controls: false,
        exDialogVisible:false,
        printDialogVisible:false,
        loading:false,
        headerTitle: "UPC验收",
        footerBtn: "验收",
        formLabelWidth: '120px',
        params: this.$route.params,
        upc:'',
        scanning:0,
        wareId:PF.getLocal('wareId', 'number'),
        wareName:PF.getLocal('wareName'),

        searchSelectCell: {
          pageNum: 1,
          pageSize: 5,
          total: null,
          keyWords: '',
          wareId: '',
          areaId: '',
          cellUseType: 0,
        },
        cells: [],

        activeName: 'first',
        upcOK:false,
        detail:null,
        asnDetails: [],
        batchTactics: {},
        detailIndex: null,

        form: {},
        rules: {
          wareId: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          organizationId: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          itemName: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          itemCode: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          toPackDetailId: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          toQuantity: [
          {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          toCellCode: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          'productDate': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'exDate': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'inDate': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'supplierCode': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'supplierBatch': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'qcState': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'costPrice': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'salePrice': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'detailNo': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'memo1': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'memo2': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'memo3': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'batchAttribute1': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
          'batchAttribute2': [
            {required: false, message: '必填项不得为空', trigger: 'blur'}
          ],
        },
        exReasons: [
          {
            value: '1',
            label: '多发'
          },
          {
            value: '2',
            label: '少发'
          },
          {
            value: '3',
            label: '错发'
          },
          {
            value: '4',
            label: '破损'
          },
          {
            value: '5',
            label: '其他'
          }
        ],

      };
    },
    watch:{
      upc:function(newVal, oldVal){
        let qrObject = PF.simpleQrEncode(newVal);
        if(qrObject && qrObject.index2 && qrObject.index2.trim().length > 1) {
          this.upc = qrObject.index2;
          return;
        }
        for(var i=0;i<this.asnDetails.length;i++){
            var asnDetail = this.asnDetails[i];
            if(
              this.mateUpc(newVal.trim(),asnDetail.upclist)
                    ||   asnDetail.itemCode.trim() ==newVal.trim()   ){  
                  this.asnDetails.unshift(this.asnDetails.splice(i , 1)[0]); 
                  if(this.asnDetails[0].toQuantity==undefined|| this.asnDetails[0].toQuantity==null){
                    this.asnDetails[0].toQuantity=1;
                    this.asnDetails[0].inDate = PF.getNowFormatDate();
                  
                    this.asnDetails[0].asnDetailNo = asnDetail.detailNo;  
                      this.getBatchTacticData(asnDetail.organizationId,asnDetail.itemCode);
                      
                      
                  }else{
                    this.asnDetails[0].toQuantity = parseInt(this.asnDetails[0].toQuantity);
                    this.asnDetails[0].toQuantity+=1;
                  } 
                  this.upc = '';
                  this.detail = asnDetail;
                  this.detail.toPackDetailId = this.detail.packDetailId;
                  this.packChange();
            }
        }
      },    

    },
    computed: {
      ...mapState([
        "qcStates",
      ]),
    },
    methods: {
      mateUpc(value,upcs){
        for(var i=0;i<upcs.length;i++){
          if(upcs[i].upc==value.trim()){
              return true;
          }
        }
        return false;
      },
       //打印
       print(itemCode){
          this.printDialogVisible = true;
          this.printItemCode = itemCode;
       },
      print1:function (){

        var that = this; 
        var itemCode = that.printItemCode;
         this.loading = true;
         IOT.getServerData('/item/infos/print', 'get', {itemCode:itemCode,wareId:this.wareId}, (ret) => {
            this.loading = false;
          if (ret.code === 200) { 
                var strHTML= ret.data; 
                IOT.printHtml(strHTML,null,that.printQuantity); 
                that.printDialogVisible = false;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        },true); 
      },
      input0Set(e) {
        if (e.currentTarget.value == '0') {
          e.currentTarget.ariaValueNow = '';
          e.currentTarget.ariaValueText = '';
          e.currentTarget.value = '';
        } 
      },
       onInput(){
          this.$forceUpdate();
      },
      scan(scanType){
        SCAN.startRecognize(
          (type, result)=>{
            if(scanType==1){
                this.upc = result;
            } 
            else if(scanType==2){
              this.detail.toCellCode=result;
            }
            SCAN.close(); 
         });
      },
      packChange( ){
        this.detail.packDetails.forEach(packDetail => { 
          if(this.detail.toPackDetailId == packDetail.packDetailId1){
            this.detail.toPackDetailId = packDetail.packDetailId1;
            this.detail.toPackDescribe = packDetail.packDescribe1;
            this.detail.toTransRatio = packDetail.transRatio1;
          } 
        });
      },
      getDetail(){
        var that = this;
        var details  =this.asnDetails;
        for (let i = 0; i < details.length; i++) { 
            if(details[i].batch.trim()==that.upc.trim()){ 
 
              
                // this.$notify({
                //   title: '成功',
                //   message: '请扫描SKU',
                //   type: 'success'
                // });
            }
        }

      },
      handleClick(tab, event) {
        // console.log(tab, event);
        if(this.activeName === 'second'){
          if(this.form.organizationId && this.form.itemCode){
            this.getBatchTacticData(this.form.organizationId,this.form.itemCode);
          }else{
            IOT.tips('请先选择需要验收的物料！', 'error');
            this.activeName = 'first';
          }
        }
      },
      getAsnDetailData(){
        this.loading = true;
        this.asnDetails = [];
        if(!PF.isNull(this.$route.params.billNo)){
          this.$router.go(-1);
        }
        IOT.getServerData('/asn/details/list','get',{billNo: this.$route.params.billNo},(ret) => {
          this.loading = false;
          if (ret.code === 200) {
            this.asnDetails = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      selectItem(val){
        let form = PF.JSON(this.asnDetails[val]);
        form.wareId = this.params.wareId;
        form.toCellCode = '';
        form.toQuantity = form.expectQuantity;
        form.toPackDetailId = form.packDetailId;
        this.form = form;
      },
      getBatchTacticData(organizationId,itemCode) {
        IOT.getServerData('/batch/tactics/findByOrganizationOrItemInfo', 'get', {
          organizationId: organizationId,
          itemCode: itemCode
        }, (ret) => {
           
          if (ret.code === 200) {
            let list = PF.JSON(ret.rows[0])
            let detailDetails = list.batchTacticDetailDetails;
            let testDetailDetails = [];
            list.batchTacticDetails[12].values = [];
            list.batchTacticDetails[13].values = [];
            for (let i = 0; i < detailDetails.length; i++) {
              if (detailDetails[i].detailCode === 'batchAttribute1') {
                // testDetailDetails.push(detailDetails[i])
                list.batchTacticDetails[12].values.push(detailDetails[i])
              } else if (detailDetails[i].detailCode === 'batchAttribute2') {
                list.batchTacticDetails[13].values.push(detailDetails[i])
              }
            }

            let batchTacticDetails = list.batchTacticDetails;
            let testBatchTacticDetails = [];
            for (let i = 0; i < batchTacticDetails.length; i++) {
              if (batchTacticDetails[i].state === 1) {
                testBatchTacticDetails.push(batchTacticDetails[i])
                if (batchTacticDetails[i].isRequired === 1) {
                  this.rules[batchTacticDetails[i].detailCode][0].required = true;
                }
                // if (batchTacticDetails[i].detailCode === 'inDate') {
                //   this.form[batchTacticDetails[i].detailCode] = PF.getNowFormatDate();
                // }
                // if (batchTacticDetails[i].detailCode === 'detailNo') {
                //   this.form.detailNo = this.form.detailNo;
                // }else if (batchTacticDetails[i].detailCode === 'supplierCode') {
                //   this.form.supplierCode = this.form.supplierCode;
                // }
              }
            }

            list.batchTacticDetailDetails = testDetailDetails;
            list.batchTacticDetails = testBatchTacticDetails;
            // this.form.detailNo = this.theInput.asnDetailNo;
            // this.form.supplierBatch = this.theInput.batchNo;
            // this.form.supplierCode = this.theInput.supplierCode;
            this.batchTactics = list;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getCellData() {
        IOT.getServerData('/cell/infos/list', 'get', this.searchSelectCell, (ret) => {
          if (ret.code === 200) {
             
            this.cells = ret.rows;
            this.searchSelectCell.total = ret.total;
            this.searchSelectCell.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      resetForm( ) { // 重置
        this.$refs["acceptForm"].resetFields();
      },
      commitForm( ){
        let that = this;
        if(this.detail.toQuantity<=0){
          IOT.tips( '验收数量至少为1', 'error');
          return;
        }
        this.$refs['acceptForm'].validate((valid) => {
          if (valid) {
           
            let form = PF.JSON(this.detail);

            if(form.toPackDetailId==undefined ||form.toPackDetailId==null ){
                IOT.tips( '请选择单位', 'error');
                return;
            }

            // form.wareId = this.params.wareId;数量
            form.wareName = this.params.wareName;
            form.billType = 2;
            form.toPackDetailId = form.toPackDetailId;
            form.toPackDescribe = form.toPackDescribe;
            if(!form.toTransRatio){form.toTransRatio=1};
            form.toQuantity = form.toQuantity*form.toTransRatio;
            // form.toQuantity = form.expectQuantity;
            this.loading = true;
             IOT.showOverlay('保存中，请稍等...');
            IOT.getServerData('/accept/records/acceptList', 'post', {acceptInserts: [form]}, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                that.$confirm(form.itemName+'已成功验收'+ form.toQuantity+(form.toPackDescribe || "件"), {
                  confirmButtonText: '确定',
                  showCancelButton: false,
                  type: 'seccess'
                }).then(() => {
                  this.resetForm('form');
                  this.detail = null;
                  this.batchTactics = {};
                  this.loading = false;
                 
                 this.getAsnDetailData();
                }).catch(() => {  
                  this.detail = null;
                  this.loading = false;  
                  this.getAsnDetailData();   
                });
                // IOT.tips('验收成功！', 'success', 3000, () => {
                  
                //  // this.$router.push({name: 'baseAccept'})
                // });
              } else {
                this.loading = false;
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },
      isCommit( ){
        if(JSON.stringify(this.detail) === '{}'){
          IOT.tips('请选择需要验收的物料！', 'error');
        }else if(JSON.stringify(this.batchTactics) === '{}'){
          IOT.tips('请填写批次信息！', 'error');
        }else if(this.detail.exState==1){
          IOT.tips('上报了异常信息，等待处理！', 'error');
        }else  {
          this.commitForm();
        }
      },

      acceptEx(asnDetail) {
        // if(this.cfAccept){
        //   return;
        // }
        // this.cfAccept  =true;
        const loading = this.$loading({
            lock: true,
            text: 'Loading',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          });
        
        var that = this;
        // if (that.shipInfo.acceptTime == null) {
        //   that.shipAccept();
        // }
        var asnMaster = this.$route.params;
        let exData = {
          wareId: this.wareId,
          wareName: this.wareName,
          organizationId: asnMaster.organizationId,
          organizationName: asnMaster.organizationName,
          itemCode: asnDetail.itemCode,
          itemName: asnDetail.itemName,
          spec: asnDetail.spec,
          model: asnDetail.model,
          imgUrl: asnDetail.imgUrl,
          quantity: asnDetail.exQuantity,
          inQuantity: asnDetail.acceptQuantity,
          orderQuantity: asnDetail.expectQuantity,
          type: asnDetail.exType,
          reason: asnDetail.exReason,
          memo: asnDetail.exMemo,
         
          billNo: asnMaster.billNo,
          billDetailNo: asnDetail.detailNo
        };

        if(asnDetail.exQuantity == null) {
          IOT.tips('异常数量不能为空！', 'error');
          this.cfAccept  =false;
          loading.close();
          return false
        }

        if(asnDetail.exQuantity != null && asnDetail.exQuantity <= 0) {
          IOT.tips('异常数量必须大于0！', 'error');
          this.cfAccept  =false;
          loading.close();
          return false
        }

        if(!asnDetail.exType) {
          IOT.tips('异常类型不能为空！', 'error');
          this.cfAccept  =false;
          loading.close();
          return false
        }

        if(!asnDetail.exReason) {
          IOT.tips('异常原因不能为空！', 'error');
          this.cfAccept  =false;
          loading.close();
          return false
        }
        IOT.getServerData('/accept/exs/add', 'post', exData, (ret) => {
          //this.cfAccept  =false;
          if (ret.code === 200) {
            this.exDialogVisible = false;
            this,this.detail.exState =1;
            this.$nextTick(()=>{
              setTimeout(()=>{
                this.getAsnDetailData();
                loading.close();
                this.$set(this.detail, 'exQuantity', null);
                this.$set(this.detail, 'exReason', null);
                this.$set(this.detail, 'exType', null);
                this.$set(this.detail, 'exMemo', null);
                IOT.tips('异常上报成功', 'success');
              },200);
            });
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
            loading.close();
          }
        }, true)
      },
      
    },
    created() {
      this.getAsnDetailData();
        
      this.getCellData();
      setTimeout(function () { 
          var domFocus = document.getElementById("upc"); 
         domFocus.focus();
      }, 100); 
  
    },
    components: {
      FeaturesHeader,
      FeaturesFooter
    }
  };
</script>
<style lang="less" scoped>
  .baseAccept2 {
    padding-bottom: 90px;
  }
</style>
