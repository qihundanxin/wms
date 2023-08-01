<template>

    <div class="Seeding">  
        <div id="inside">
             <h1>一单多品播种工位</h1>
             <!-- <el-dropdown style="margin-left:10px;margin-right:10px"  size="medium"   type="primary"    icon="iconfont icon-print"  @click="demoPrint()" split-button  >
                打印
                <el-dropdown-menu slot="dropdown">
                <el-dropdown-item><el-button   size="small" type="primary"  @click="$parent.setPrinter">打印设置</el-button></el-dropdown-item> 
                <el-dropdown-item><el-button  style="margin-top:2px; " size="small" type="primary"  @click="demoPrint(1)">打印预览1</el-button></el-dropdown-item> 
                
                <el-dropdown-item><el-button  style="margin-top:2px; " size="small" type="primary"  @click="demoPrint(2)">打印预览2</el-button></el-dropdown-item> 

                </el-dropdown-menu>
            </el-dropdown> -->
            <hr/>
            <div class="clearFloat">
                <el-card class="box-card">
                            <el-form :inline="true" class="clearFloat" ref="form"> 
                                 <el-form-item label="扫描模式：" label-width="formLabelWidth"  > 
                                                <el-radio-group v-model="scanType">
                                                    <el-radio
                                                    v-for="item in scanTypes"
                                                    :key="item.value"
                                                    :label="item.value"
                                                     >{{item.label}}
                                                    </el-radio>  
                                                </el-radio-group>  
                                </el-form-item>
                                
                                 <el-form-item style="margin-left:20px" label="CODE：" label-width="formLabelWidth"  >
                                        <el-input v-model="code"  s placeholder="code" clearable></el-input> 
                                </el-form-item>
                                 <el-form-item  style="display: none"  label="code" label-width="formLabelWidth"    >
                                        <el-input v-model="code"  style="width:100px" s placeholder="code" clearable></el-input> 
                                </el-form-item> 
                                <!-- <el-divider></el-divider>
                                 <el-form-item label="SKU" label-width="formLabelWidth"
                                            style="width: 35%; "> 
                                            <span>{{itemCode}}</span> 
                                </el-form-item>
                              
                            
                                <el-form-item label="订单号" label-width="formLabelWidth"
                                            style="width: 35%;  ">
                                            <span   >{{billNo}}</span>
                                            
                                    
                                </el-form-item> -->
                                
                              
                            </el-form>
                        </el-card>
                <div class="div-inline1 leftFloat">


                     
                        <!-- <el-card class="box-card">
                            <el-form class="clearFloat" ref="form">
                               
                              
                                 <el-form-item label="扫描模式" label-width="formLabelWidth"
                                            >
                                            
                                                <el-radio-group v-model="scanType">
                                                    <el-radio
                                                    v-for="item in scanTypes"
                                                    :key="item.value"
                                                    :label="item.value"
                                                     >{{item.label}}
                                                    </el-radio>

                                                   
                                                </el-radio-group>
                                           
                                            
                                    
                                </el-form-item>
                                 <el-form-item label="code" label-width="formLabelWidth"
                                            >
                                        <el-input v-model="code"  s placeholder="code" clearable></el-input>
                                            
                                    
                                </el-form-item>
                                 <el-form-item  style="display: none"  label="code" label-width="formLabelWidth"
                                            >
                                        <el-input v-model="code"  s placeholder="code" clearable></el-input>
                                            
                                    
                                </el-form-item>
                           
                                <el-divider></el-divider>
                                 <el-form-item label="SKU" label-width="formLabelWidth"
                                            style="width: 35%; ">
                                            
                                            <span>{{itemCode}}</span>
                                    
                                </el-form-item>
                               
                            
                                <el-form-item label="订单号" label-width="formLabelWidth"
                                            style="width: 35%;  ">
                                            <span   >{{billNo}}</span>
                                            
                                    
                                </el-form-item>
                                
                              
                            </el-form>
                        </el-card> -->
                        
                    

                    

                    
                        <el-card class="box-card">
                            <h2 style="text-align: center;margin-bottom: 20px;">出库单</h2>
                            <div class="tableBox" style="height: 500px; overflow: auto">
                                <el-table
                                        v-loading="loading"
                                        highlight-current-row
                                        :data="soMasters"
                                        style="width: 100%">
                                    
                                    <el-table-column
                                            prop="billNo"
                                            min-width="120px"
                                            label="单号">
                                            <template slot-scope="scope">
                                                    <el-tag v-if="scope.row.state==7" type="success">单号：{{scope.row.billNo}}</el-tag>
                                                    <br/>
                                                    <el-tag v-if="scope.row.state!=7"  >单号：{{scope.row.billNo}}</el-tag>
                                                    <br/>
                                                    
                                                    <el-tag   >数量：{{scope.row.quantity}}</el-tag>
                                                    <br/>
                                                    <el-tag   >isOK：{{scope.row.isOK}}</el-tag>
                                                     
                                                    
                                                    
                                                    <el-tag v-if="scope.row.cellId!=undefined"  >位置：{{scope.row.cellId}}</el-tag>
                                                    <br/>
                                                </template>
                                            </el-table-column>
                                <el-table-column
                                min-width="280px"
                                            prop="itemCode"
                                            label="SKU">
                                        <template slot-scope="scope">
                                            <div style="border-style:solid" v-for="(detail,index) in scope.row.list">
                                                <span>{{ detail.itemCode }}</span>
                                            <br/>
                                            <span>{{ detail.itemName }}</span>
                                            <br/>
                                            <span style="font-size: 30px;">{{detail.checkQuantity}}/{{detail.orderQuantity}}</span>
                                        
                                            </div>
                                            
                                        </template>
                                    </el-table-column>
                                    
                            
                                </el-table>
                            </div>
                        </el-card>
                        
                     
                </div>

                <div class="div-inline-right rightFloat">
                    
                        <div class="inventoryListBox">
                            <el-card class="box-card">
                            <h2 style="text-align: center;margin-bottom: 20px;">
                                播种墙
                                <el-select
                                        v-model="seedingWallIndex"
                                        @change="changeSeedingWall"
                                >
                                    <el-option
                                            v-for="(item,index) in seedingWalls"
                                            :key="index"
                                            :label="item.code"
                                            :value="index"
                                    >  
                                    </el-option>
                                </el-select>
                            </h2>
                            <div     style=" padding-top:10px;padding-left:10px;border-radius:15px">
                               
                                
                            </div>

                            <div id="seed_wall" v-html="htmlStr" style="border: 2px solid dodgerblue">
                            </div>
                            
                            </el-card>
                        </div>
                    
                    
                </div>

            </div>
        </div>

        <!-- <div class="createFootBox clearFloat"
             :style="{width: isCollapse ? 'calc(100% - 104px)' : 'calc(100% - 240px)'}">
            <div class="leftFloat">
                <el-button type="info" @click="$router.go(-1)">返 回</el-button>
            </div>
            <div class="rightFloat">
                <el-button @click="commitForm()" type="primary">清空数据</el-button>
            </div>
        </div> -->

    </div>
</template>

<script>
    function myFunction() {
        alert("fadf");
    }
    import pako from 'pako';
    import {mapState} from "vuex";

    export default {
        name: 'seeding',
        data() {
            return {
                timer:'',
                code:'',
                scanTypes:[
                    {
                        label:"扫描商品",
                        value:0,
                    },
                    {
                        label:"打印面单",
                        value:1,
                    }
                    ,
                    {
                        label:"清除位置",
                        value:2,
                    }
                ],
                scanType:0,
                bill:{},
                seedingWallIndex:'',
                wareId:PF.getLocal('wareId', 'number'),
                cellIndex:0,
                itemCode:'',
                quantity:0,
                billNo:'',
                htmlStr:'',
                formLabelWidth: '400px',
                formData: {
                    waveCode: '',
                    seedingWallNo: '',
                    scanData: '',
                    itemCode: '',
                    inventoryId: ''
                },
                dialogHoleInfoVisible: false,
                dialogSoSelVisible: false,
                dialogQuantityVisible: false,
                holeInfo: {
                    holeId: '',
                    soNo: ''
                },
                soBills: [],
                seedingWalls: [],
                seedingWall: {
                    rowCount:4,
                    columnCount:4
                },
                soMasters: [],
                loading: false,
                cells: [],
                updateInfo: {
                    soNo: '',
                    itemCode: '',
                    quantity: null,
                    inventoryId: null
                }

            }
        },
        computed: {
            ...mapState([]),
            isCollapse() {
                return this.$parent.leftMenuIsCollapse;
            },
             
            seedingCell: function (x,y) { 
                    this.cells.forEach(cell => {
                        if(cell.x==x && cell.y==y){
                        return cell;
                        }
                    });
            }
        },
        
        methods: {
            
            aaa(str){
                alert(str);
            },
            
            printDzmd(billNo){
               var that = this; 
                IOT.getServerData('/so/masters/getDzmd', 'get', {billNo:billNo}, (ret) => {
                if (ret.code === 200) { 
                        var strHTML= ret.data; 
                        that.$parent.printHtml(strHTML);  
                } else {
                    IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                }
                },true);
            },
            checkBill(cellId,index){
                var that  =this; 
                var billNo= that.soMasters[index].billNo;
                IOT.getServerData('/so/masters/seedingCheck', 'get', {cellCode:cellId,billNo:billNo}, (ret) => { 
                    if (ret.code === 200) { 
                         
                        that.soMasters[index].state=7; 
                    } else {
                        IOT.tips(ret.message || 'server error', 'error')
                    }
                })
            },
            getBillNo(itemCode){
                var that = this;
                var soMasters = that.soMasters;
                var billNo = null;
                var soMaster = null;
                for(var i=0;i<soMasters.length;i++){
                    var details = soMasters[i].list;
                    for(var j=0;j<details.length;j++){
                        var element2 = details[j]; 
                        if(element2.itemCode == itemCode && element2.orderQuantity>element2.checkQuantity ){
                            element2.checkQuantity+=1;
                            soMasters[i].lable = element2.checkQuantity+"/"+element2.orderQuantity; 
                            var aaa = soMasters[0];
                            soMasters[0] = soMasters[i];
                            soMasters[i] = aaa;
                            soMasters[i].quantity = soMasters[i].quantity+1;
                            that.itemCode = itemCode;
                            that.selectBill(soMasters[0]);
                            return ;
                        }
                    }
                }
                var cellId = that.cells[that.getNewIndex()];
                if(cellId==null|| cellId==undefined || cellId=="" || cellId==NaN){
                    this.$notify({
                                    title: '警告',
                                    message: '播种强位置不足！',
                                    type: 'warning'
                                    }); 
                }
                IOT.getServerData('/so/masters/getSeedingByItemCode', 'get', {itemCode:itemCode,wareId:this.wareId,waveMasterId:this.formData.waveCode,cellCode:cellId}, (ret) => { 
                    if (ret.code === 200) {  
                        that.bill=ret.data; 
                        if(ret.data==null){
                                this.$parent.playMusic("wms未匹配到订单");
                                this.$notify({
                                    title: '警告',
                                    message: '无该商品的订单（'+itemCode+'）',
                                    type: 'warning'
                                    }); 
                                     return ; 
                         }else{
                             var soMaster = ret.data;
                             var details = soMaster.list; 
                            for(var j=0;j<details.length;j++){
                                var detail = details[j]; 
                                if(detail.itemCode == itemCode && detail.orderQuantity>detail.checkQuantity ){
                                    detail.checkQuantity+=1;
                                    soMaster.lable = detail.checkQuantity+"/"+detail.orderQuantity;   
                                    soMaster.cellId = that.cellId;
                                    soMaster.quantity = 1;
                                    soMaster.isOK = false;
                                    that.soMasters.push(soMaster);
                                    var aaa = that.soMasters[0];
                                    soMasters[0] = soMasters[that.soMasters.length-1];
                                    soMasters[that.soMasters.length-1] = aaa;
                                    that.selectBill(soMaster);
                                    return ; 
                                }
                            } 
                         }
                           
                       
                    } else {
                        IOT.tips(ret.message || 'server error', 'error')
                    }
                },true)  
            }, 
            isOK(cellId){
                var that = this;
                var list = that.soMasters;
                var billNo = null;
                for(var i=0;i<list.length;i++){
                    var details = list[i].list; 
                    if(list[i].cellId  ==cellId && list[i].isOK==true){
                        return true;
                    }
                    if(list[i].cellId==cellId){
                        for(var j=0;j<details.length;j++){
                            var element2 = details[j]; 
                            if(element2.orderQuantity>element2.checkQuantity ){ 
                                return false;
                            }
                        }
                        if(details.length>0){
                            if(!list[i].isOK){
                                that.checkBill(cellId,i);
                                list[i].isOK = true;
                                return true;
                            }
                        } 
                       
                    } 
                }
                return  false; 
            },

            //有货但是还没完成
            isHasGoods(cellId){
                var that = this;
                var list = that.soMasters;
                var billNo = null;
                for(var i=0;i<list.length;i++){
                    var details = list[i].list; 
                    if(list[i].cellId  ==cellId && list[i].isOK==false){
                        return true;
                    } 
                }
                return  false; 
            },

            scanItemCode(itemCode){
                var that = this;
                 
               
               // that.quantity+=1;
                if(that.scanType==0){
                    that.getBillNo(itemCode,that.quantity);   
                }else if(that.scanType==1){
                    that.printDzmd(itemCode) ;
                }else if(that.scanType==2){
                    that.downSeeding(itemCode) ;
                } 
                
            }, 
             downSeeding(code){
                 var that = this;
                IOT.getServerData('/so/masters/downSeeding', 'get', {
                  data:code,
                  wareId:this.wareId
                }, (ret) => { 
                  if (ret.code === 200) {  
                      that.clearCell(code);
                  } else {
                    IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                  }
                });    
             },
             clearCell(cellCode){
                 var that = this;
                 for(var j =0;j<that.soMasters.length ;j++){ 
                    var soMaster = that.soMasters[j];
                    if(soMaster.cellId == cellCode){
                        that.soMasters.splice(j,1); 
                    }
                } ;

                this.changeCellColor(cellCode,"white",null,null);

             },


            selectBill(bill){
                var that = this;
                if(bill==null){
                    that.billNo = '';
                    that.changeCellColor(null,"cyan"); 
                    return;
                }
                that.billNo = bill.billNo;
                var list = that.soMasters;
                for(var i=0;i<list.length;i++){
                    if( list[i]  == bill ){
                        var cellId = list[i].cellId;
                        if(cellId==undefined || cellId==null){  
                             cellId = that.cells[that.getNewIndex()];
                             that.soMasters[i].cellId = cellId;
                             that.changeCellColor(cellId,"#F56C6C",bill.lable,bill.billNo); 
                             that.getNewIndex();
                        }else{
                            that.changeCellColor(cellId,"#F56C6C",bill.lable,bill.billNo);
                        } 
                    }
                }
            },
            getNewIndex(){
                
                var that = this;
                for(var i = 0;i<this.cells.length;i++){
                     var flag = true;
                     for(var j =0;j<this.soMasters.length && flag;j++){ 
                        var soMaster = this.soMasters[j];
                        if(soMaster.cellId == this.cells[i]){
                            flag = false;
                        }
                     }
                     if(flag){
                        that.cellIndex = i;
                        return i;
                     }
                }
                
            },

            changeCellColor(cellId,color,lable,billNo){
                var that = this;
                that.cells.forEach((element,index) => {
                    if(that.isOK(element)){ 
                        document.getElementById(element).style.backgroundColor = "#67C23A";
                        document.getElementById('lable-' + element).innerText = "OK";
                        return;
                    }else if(element==cellId){
                            document.getElementById('billNo-' + cellId).innerText = billNo;
                            document.getElementById(cellId).style.backgroundColor = color;
                            document.getElementById('lable-' + cellId).innerText = lable;
                             return;
                    }else if(that.isHasGoods(element)){
                            document.getElementById(element).style.backgroundColor = "#909399"; 
                             return;
                    }
                    else{ 
                    } 
                }); 
            },
            changeCellColor2(cellId,color,lable,billNo){
                var that = this;
                that.cells.forEach((element,index) => {
                    if(that.isOK(element)){ 
                        document.getElementById('billNo-' + cellId).innerText = billNo;
                        document.getElementById(element).style.backgroundColor = "#67C23A";
                        document.getElementById('lable-' + element).innerText = "OK";
                        return;
                    }else if(element==cellId){
                            document.getElementById('billNo-' + cellId).innerText = billNo;
                            document.getElementById(cellId).style.backgroundColor = color;
                            document.getElementById('lable-' + cellId).innerText = lable;
                             return;
                    }else if(that.isHasGoods(element)){
                            document.getElementById(element).style.backgroundColor = "#909399"; 
                             return;
                    }
                    else{ 
                    } 
                }); 
            },

             changeCellColor2(cellId,color,lable,billNo){
                var that = this;
                that.cells.forEach((element,index) => {
                     if(element==cellId){
                            document.getElementById('billNo-' + cellId).innerText = billNo;
                            document.getElementById(cellId).style.backgroundColor = color;
                            document.getElementById('lable-' + cellId).innerText = lable;
                             return;
                    }  
                });
                
            },

            getCode(){
                var that = this;
                var code = "";
                var lastTime, nextTime;
                var lastCode, nextCode;
                document.onkeypress = function (e) {
                   
                    nextCode = e.which;
                    nextTime = new Date().getTime();
                    var now = new Date().getTime();
                    if(lastCode == null && lastTime == null){
                        if(e.key=="Enter"){
                             
                             //这里监听正常的键盘回车键
                            that.scanItemCode(that.code);
                            that.code  ="";
                            code='';
                            lastCode = null;
                            lastTime = null;
                        }else{
                            code = e.key;
                            lastCode = nextCode;
                            lastTime = nextTime;
                        }   
                    }
                    //间隔大于500毫秒的是键盘输入的
                    else if(lastCode != null && lastTime != null && nextTime - lastTime > 50){ 
                        if(e.key=="Enter"){
                            //这里监听正常的键盘回车键
                            that.scanItemCode(that.code);
                            that.code  ="";
                            code='';
                            lastCode = null;
                            lastTime = null;
                        }else {
                          code =e.key;
                          lastCode = nextCode;
                          lastTime = nextTime;
                        }   
                    }
                     //间隔小于500毫秒的是扫码或者称重进来的
                    else if (lastCode != null && lastTime != null && nextTime - lastTime <= 50) {// 扫码枪
                        
                        if(e.key=="Enter"){
                            that.scanItemCode(code);
                            that.code  ="";
                            code='';
                            lastCode = null;
                            lastTime = null;
                        }else{
                            code += e.key;
                            lastCode = nextCode;
                            lastTime = nextTime;
                        }
                        
                    }  

                 
                } 
        },

            getSoMaster() {
                this.loading = true
                IOT.getServerData('/so/masters/findByWaveCode', 'get', {id: this.formData.waveCode,wareId:this.wareId}, (ret) => {
                    this.loading = false
                    if (ret.code === 200) {
                        this.soMasters = ret.rows
                    } else {
                        IOT.tips(ret.message || 'server error', 'error')
                    }
                })
            },
            handleCurrentChange(val) {
            },

            commitForm(formName) {

            },
            resetForm(formName) { // 重置
                this.$refs[formName].resetFields();
            },
            asd(){
                    alert("fas");
            },


            generateSeedWall(code, row, column) {
                this.cells = [];
                let str = ''
                var width1 =97 / column;
                var width =  width1.toFixed(2);
                width = width+"%";
                var fontSize1 = 100/column +"px";
                var fontSize2 = 120/column +"px";
                 
               
     
                let height = 600 / row
                for (let i = 1; i <= row; i++) {
                    for (let j = 1; j <= column; j++) {
                       
                        let cellId = code+'-'+i+'-'+j
                        let cellLabelId ='lable-'+  cellId;
                        let cellBillNoId = 'billNo-'+cellId;
                        //let digit = (i - 1) * column + j
                       
                        str += '<div @click="asd()" id="' + cellId + '" class="seed_hole_class" style="width: ' + width + ';height: ' + height + 'px;float: left; border: 1px solid dodgerblue; text-align: center">' +
                                     '<div id="'+cellBillNoId+'" style="height; ' + width / 6 + '; font-size: ' + fontSize1 + '; margin-top:  10%; color: #FFFFFF;"></div>' +
                                    '<div id="' + cellLabelId + '" style="width:90%;    margin-top: 13%; font-size:'+fontSize2+'; color: white"></div>' +
                           
                                '</div>' 
                        this.cells.push(cellId);

                    }
                    str += '<div style="clear: both"></div>'
                }
                this.htmlStr = str;
                this.getSeedingCells(code);
                //document.getElementById('seed_wall').innerHTML = str
            },
             getSeedingCells(code){
                var that = this;
                that.soMasters = [];
                IOT.getServerData('/seeding/cells/findByWallCode', 'get', {wallCode:code,wareId:this.wareId}, (ret) => {
                    if (ret.code === 200) {
                        var cells = ret.rows; 
                        for(var i =0;i<cells.length;i++){
                            var cell = cells[i];
                            if(cell.billNo!=null && cell.state==1){ 
                                var soMaster = {billNo:cell.billNo,cellId:cell.seedingCellCode,list:[],isOK:true,quantity:cell.quantity}
                                that.soMasters.push(soMaster);
                                 this.changeCellColor2(cell.seedingCellCode,"#67C23A","OK",cell.billNo);
                            }
                            if(cell.billNo!=null && cell.state==0){ 
                                var soMaster = {billNo:cell.billNo,cellId:cell.seedingCellCode,list:[],isOK:false,quantity:cell.quantity}
                                that.soMasters.push(soMaster);
                                this.changeCellColor2(cell.seedingCellCode,"#909399","重新扫描",cell.billNo);
                            }
                        }  
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                })
            },
         
            handleCloseHoleInfo() {
                this.dialogHoleInfoVisible = false
            },
            showSoSelModal() {
              //  this.getSoData()
                this.dialogSoSelVisible = true
            },
            handleCloseSoSel() {
                this.dialogSoSelVisible = false
            },
            handleSoSelChange(soNo) {
                this.dialogSoSelVisible = false
                this.holeInfo.soNo = soNo
            },
            handleCloseQuantity() {
                this.dialogQuantityVisible = false
            },
            commitChangeHoleInfo() {
                PF.setLocal(this.holeInfo.holeId, this.holeInfo.soNo)
                this.dialogHoleInfoVisible = false

                this.displayHoleLabel()
            },
            getSoData() {
                IOT.getServerData('/so/masters/list', 'get', {}, (ret) => {
                    
                    if (ret.code === 200) {
                        this.soBills = ret.rows;
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            getSeedingWallData() {
                IOT.getServerData('/seeding/walls', 'get', {wareId:this.wareId}, (ret) => {
                    if (ret.code === 200) {
                        this.seedingWalls = ret.rows
                        if(this.seedingWalls.length>0){
                            this.seedingWallIndex=0;
                            this.changeSeedingWall();
                        }
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                })
            },
            changeSeedingWall() { 
                this.seedingWall = this.seedingWalls[this.seedingWallIndex];
                let row = this.seedingWall.rowCount
                let column = this.seedingWall.columnCount
                this.generateSeedWall(this.seedingWall.code, row, column)
                // this.addBtnClickEvent()
                // this.displayHoleLabel()
            },
           
      
            cellBtnClicked(cellId) {
                alert('fewjofeiwjf')
                this.holeInfo.holeId = cellId
                this.holeInfo.soNo = PF.getLocal(cellId)
                this.dialogHoleInfoVisible = true
            },
            addBtnClickEvent() {
                let that = this
                let arr = document.querySelectorAll('.seed_hole_class');
                for(let item of arr){
                    item.children[2].onclick = function () {
                        that.holeInfo.holeId = 'seed_hole_' + this.id
                        that.holeInfo.soNo = PF.getLocal(that.holeInfo.holeId)
                        that.dialogHoleInfoVisible = true
                    }
                }
            },
            doAlert() {
                let that = this
                let param = {
                    waveCode: this.formData.waveCode,
                    itemCode: this.formData.itemCode,
                    inventoryId: this.formData.inventoryId
                }
                IOT.getServerData('/pick/tasks/getSeedingWallAlertInfo', 'post', param, (ret) => {
                    if (ret.code === 200) {
                        let list = ret.rows
                        list.forEach(item => {
                            this.cells.forEach(idSuffix => {
                                let cellId = 'seed_hole_' + idSuffix
                                let cellLabelId = 'seed_hole_label_' + idSuffix
                                let cellSoNo = PF.getLocal(cellId)
                                if (cellSoNo === item.soNo) {
                                    document.getElementById(cellId).style.backgroundColor = "cyan"
                                    document.getElementById(cellLabelId).innerText = item.neededQuantity + '/' + item.pickQuantity

                                    document.getElementById(cellId).onclick = function () {
                                        that.updateInfo.soNo = item.soNo
                                        that.updateInfo.inventoryId = param.inventoryId
                                        that.updateInfo.itemCode = param.itemCode
                                        that.dialogQuantityVisible = true
                                    }
                                }
                            })
                        })

                    } else {
                        IOT.tips(ret.message || 'server error', 'error')
                    }
                })
            },
            commitSeeding() {
                let that = this
                IOT.tips('保存中~~~')
                IOT.getServerData('/pick/tasks/updateSeededQuantity', 'get', this.updateInfo, (ret) => {
                    IOT.hideOverlay()
                    if (ret.code === 200) {
                        IOT.tips(ret.messages || '保存成功！', 'success', 1000,() => {
                            that.dialogQuantityVisible = false
                            that.doAlert()
                            that.getPickTaskData()
                        })
                    } else {
                        IOT.tips(ret.message || 'server error', 'error')
                    }
                })
            },
            clearSoMasterByCellId(cellId){
                let that = this ;
                var soMasters = that.soMasters;
                for(var i =0;i<soMasters.length;i++){
                    var soMaster = soMasters[i];
                    if(soMaster.cellId == cellId){
                         
                        that.soMasters.splice(i,1);
                        return;
                    }
                }
                
            },
            refresh() { 
                var that  =this;
                IOT.getServerData('/seeding/cells/findByWallCode', 'get', {wallCode:that.seedingWall.code,wareId:this.wareId}, (ret) => {
                    if (ret.code === 200) {
                        var cells = ret.rows; 
                         
                        cells.forEach(cell => {
                            if(cell.billNo==null || cell.billNo.trim()==""){
                                that.clearSoMasterByCellId(cell.seedingCellCode);
                                that.changeCellColor2(cell.seedingCellCode,"white","","");
                            }
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                })
                 
            }
        },
        created() {
             this.getCode();
             this.getSeedingWallData()
        },
        mounted() {
           // this.getSeedingWallData()
        },
        destoryed(){
            document.onkeypress = null;
        },
        mounted() {
       // this.timer = setInterval(this.refresh, 2000);
        },
        beforeDestroy() {
        clearInterval(this.timer);
        }
    }
</script>

<style scoped lang="less">
    .Seeding {
        .box-card{
            border-color: #c1deff;
            border-width: 1mm;
            margin-bottom:10px;
        }
        padding-bottom: 100px;

        #billin {
            font-size: 30px;
            color: #4499FF;
        }

        .div-inline1 {
            margin-top: 5px;
            width: 25%;
        }

        .div-inline-right {
            width: 70%;
            float: left;
            margin-top: 5px;
            margin-left: 10px; 
        }
        .el-form-item {
            margin-bottom: 2px; 
        }
        .el-card__body {
            padding: 10px 15px 10px 20px;
        }
    }
</style>
