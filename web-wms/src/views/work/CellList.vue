<template>
  <div class="cellList clearFloat">

    <el-dialog title="货位信息"
               :visible.sync="dialogCellVisible"
               width="80%"
               style="margin-top: -14vh;">
       
          <el-row>
            <el-col :span="12" v-for="(item,index) in lookCell" :key="index">
                <el-row>
                    <el-col :span="10">
                        <div style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                          margin: 5px 0px 0px 25px;
                          font-weight:500;">
                            <span v-if="item.label">{{item.label}}:</span>
                            <span v-else>-</span>
                            <i></i>
                          </div>
                    </el-col>
                    <el-col  :span="14">
                      <div style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        margin: 5px 0px 0px 0px;
                        font-weight:700;">
                          <span v-if="item.label === '状态'" style="background: #67C23A;color: #fff;padding: 0 5px;">启用</span>
                          <span v-else-if="item.label === '状态'" style="background: #F56C6C;color: #fff;padding: 0 5px;">禁用</span>
                          <span v-else-if="item.value">{{item.value}}</span>
                          <span v-else>-</span>
                        </div>
                    </el-col>
          </el-row>
            </el-col>
          </el-row>
          
         
         
       <!-- {{inventorys}} -->

        <el-table
        v-loading="inventorysLoading"
          :data="inventorys"
          stripe
          style="width: 100%;margin-top:20px">
          
          <el-table-column
            prop="organizationName"
            label="货主"
            min-width="120">
          </el-table-column>
           <el-table-column
            prop="cellCode"
            label="货位"
            min-width="180">
          </el-table-column>
          <el-table-column
            prop="itemCode"
            label="SKU"
            min-width="180">
          </el-table-column>
          <el-table-column
            prop="itemName"
            label="SKU名称"
            min-width="180">
          </el-table-column>
          <el-table-column
            prop="batchName"
            label="批次"
            min-width="120">
          </el-table-column>
          <el-table-column
            prop="quantity"
            label="数量"
            min-width="100">
          </el-table-column>
          <el-table-column
            prop="packDescribe"
            min-width="80"
            label="单位">
          </el-table-column>
        </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogCellVisible = false">取 消</el-button>
      </div>
    </el-dialog>

    <div class="leftFloat clearFloat">
      <ul class="leftFloat">
        <li>货区<b class="rightFloat">></b></li>
         <div  v-loading="areaLoading" style="height:500px;overflow-y:auto;  ">
        <li v-for="(area,index) in areas" :key="index">
          <span :title="area.areaName" @click="getShelfData(area.areaId)" :class="now.areaId === area.areaId ? 'hover' : ''">{{area.areaCode}}</span>
        </li>
         </div>
      </ul>
      <ul    class="rightFloat">
        <li><b class="leftFloat">></b>货架</li>
        <div v-loading="shelfLoading" style="height:500px;overflow-y:auto;  ">
          <li v-for="(shelf,index) in shelfs" :key="index">
            <span :title="shelf.shelfCode" @click="getCellData(shelf)"
                  :class="now.shelfInfoId === shelf.shelfInfoId ? 'hover' : ''">{{shelf.shelfCode}}</span>
          </li>
        </div>
       
        
      </ul>
    </div>

    <div  v-loading="cellLoading"  class="rightFloat">
      <div class="top">
        <el-card>
          <h1 v-if="cells && cells.length != 0" style="text-align: center;text-decoration: underline;margin-top: -10px;color: #606266;">{{shelfCode}}</h1>
          <div v-if="cells && cells.length != 0" class="bigCellBox" v-loading="loading.calcW">
            <div :style="'width:'+width " v-for="(cell,index) in cells" class="cellBox" :key="index"
                 :class="cell.state === 'disable' ? 'danger' : (cell.status === 1 ? 'success' : 'info')"
                 @click="showCellModal(cell)"
                 :title="cell.title">{{cell.sRow}}-{{cell.sColumn}} 
            </div>
          </div>
          <p v-if="!cells || cells.length === 0" class="messageP">未查询到货位</p>
        </el-card>
      </div>
      <div class="bottom clearFloat">
        <div class="leftFloat">
          <div>
            <p>
              <span class="colorBox info"></span>
              <span>无货</span>
            </p>
            <!-- <p>
              <span>数量</span>
              <span>:</span>
              <span>0</span>
            </p> -->
          </div>
        </div>
        <div class="leftFloat">
          <div>
            <p>
              <span class="colorBox success"></span>
              <span>有货</span>
            </p>
            <!-- <p>
              <span>数量</span>
              <span>:</span>
              <span>0</span>
            </p> -->
          </div>
        </div>
        <div class="leftFloat">
          <div>
            <p>
              <span class="colorBox danger"></span>
              <span>停用</span>
            </p>
            <!-- <p>
              <span>数量</span>
              <span>:</span>
              <span>0</span>
            </p> -->
          </div>
        </div>

      </div>
    </div>

  </div>
</template>

<script>
  export default{
    name: 'CellList',
    data(){
      return {
        wareId: PF.getLocal('wareId', 'number'),
        loading:{
          calcW: false,
        },
        areaLoading:false,
        shelfLoading:false,
        cellLoading:false,
        inventorysLoading:false,
        dialogCellVisible: false,
        areas: [],
        shelfs: [],
        cells: [],
        style: {},
        width:'',
        // row: 2,
        // clo:3,
        shelfCode: '',
        inventorys:[],
        lookCell: {},
        now: {
          areaId: null,
          shelfInfoId: null,
        },
      }
    },
    watch: {
    },
    methods: {
      getAreaData() {
        this.areaLoading=true;
        IOT.getServerData('/area/infos/list', 'get', {wareId: this.wareId}, (ret) => {
           this.areaLoading=false;
          if (ret.code === 200) {
            let list = ret.rows;
            this.areas = list;
            if (list.length == 0 ) {
              return
            }
            this.getShelfData(list[0].areaId);
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getShelfData(areaId) {
        this.loading.calcW = true;
        this.now.areaId = areaId;
         this.shelfLoading=true;
        IOT.getServerData('/shelf/infos/list', 'get', {wareId: this.wareId,areaId: areaId}, (ret) => {
           this.shelfLoading=false;
          if (ret.code === 200) {
            let list = ret.rows;
            this.shelfs = list;
            if (list.length == 0) {
              this.loading.calcW = false;
              this.cells = [];
              this.shelfCode = "";
              return
            }
            this.getCellData(list[0]);
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      getCellData(shelf) {
        var that = this;
        this.shelfCode = shelf.shelfCode;
        this.now.shelfInfoId = shelf.shelfInfoId;
          this.cellLoading=true;
        IOT.getServerData('/cell/infos/findByShelfInfoId', 'get', {wareId: this.wareId,shelfInfoId: shelf.shelfInfoId}, (ret) => {
            this.cellLoading=false;
          if (ret.code === 200) {
            let list = ret.data;
            for(let i=0;i<list.length;i++){
              list[i].title = `
                名   称 : ${list[i].cellName}
                编   码 : ${list[i].cellCode}
                状   态 : ${list[i].state} 
               `;
            }
             
            that.isDom(list, shelf.type);
            that.cells = list;
            that.loading.calcW = false;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        },true);
      },
      isDom(list, type){
        var that = this;
        //let cells = document.querySelectorAll('.cellBox');
         
        that.calcWidth(list[list.length -1].sRow, list[list.length -1].sColumn, type);
        // if(cells.length === list.length){
        //   that.calcWidth(list[list.length -1].sRow, list[list.length -1].sColumn, type);
        // }else{
        //   setTimeout(()=>{
        //     that.isDom(list, type);
        //   },100);
        // }
      },
      calcWidth(r, c, type){
         
        let that = this;
        let cells = document.querySelectorAll('.cellBox');
        let oneR = (100 / Number(r));
        var width='calc( ' + oneR + '% - 10px )';
        that.width=width;
          // for(let i=0;i<cells.length;i++){
          //   cells[i].style.width = 'calc( ' + oneR + '% - 10px )';
          //    
          // }
        // if(type === 1){
        //   for(let i=0;i<cells.length;i++){
        //     cells[i].style.width = 'calc( 20% - 20px )';
        //   }
        // }else if(type === 2){
        //   let oneR = (100 / Number(r));
        //   for(let i=0;i<cells.length;i++){
        //     cells[i].style.width = 'calc( ' + oneR + '% - 20px )';
        //   }
        // }
      },
      showCellModal(row){
        let list =[];
        for(let key in row){
          if(key === 'cellName'){list.push({label: '名称', value: row[key]})}
          else if(key === 'cellCode'){list.push({label: '编码', value: row[key]})}
          // else if(key === 'shelfCode'){list.push({label: '所属货位组', value: row[key]})}
          else if(key === 'cellUseType'){list.push({label: '货位使用类型', value: row[key]})}
          // else if(key === 'cellAbcLevel'){list.push({label: '周转需求', value: row[key]})}
          // else if(key === 'cellPickType'){list.push({label: '拣货方式', value: row[key]})}
          else if(key === 'groundOrder'){list.push({label: '上架顺序', value: row[key]})}
          else if(key === 'pickOrder'){list.push({label: '拣货顺序', value: row[key]})}
          // else if(key === 'length'){list.push({label: '长', value: row[key]})}
          // else if(key === 'wide'){list.push({label: '宽', value: row[key]})}
          // else if(key === 'high'){list.push({label: '高', value: row[key]})}
          // else if(key === 'volumeLimit'){list.push({label: '体积限制', value: row[key]})}
          // else if(key === 'weightLimit'){list.push({label: '重量限制', value: row[key]})}
          // else if(key === 'boxLimit'){list.push({label: '箱数限制', value: row[key]})}
          // else if(key === 'palletLimit'){list.push({label: '托盘限制', value: row[key]})}
          else if(key === 'xCoordinate'){list.push({label: 'X坐标', value: row[key]})}
          else if(key === 'yCoordinate'){list.push({label: 'Y坐标', value: row[key]})}
          else if(key === 'state'){list.push({label: '状态', value: row[key]})}
        }
         
        this.lookCell = list;
        this.inventorys =[];
        this.inventorysLoading = true;
         IOT.getServerData('/inventorys/list', 'get', {
           wareId:row.wareId,
           cellCode:row.cellCode
         }, (ret) => {
           this.inventorysLoading = false;
          if (ret.code === 200) { 
            this.inventorys = ret.rows;
            // this.searchData.total = ret.total;
            // this.searchData.pageNum = ret.pageNumber
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
        this.dialogCellVisible = true;
      },

    },
    created() {
      this.getAreaData();
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .cellList{
    color: #606266;
    >.leftFloat{
      width: 260px;
      ul{
        width: 50%;
        min-width: 100px;
        text-align: center;
        font-size: 20px;
        li{
          line-height: 40px;
          >span{
            display: inline-block;
            width: 70%;
            height: 30px;
            line-height: 30px;
            border: 1px solid #DCDFE6;
            background: #fff;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
            cursor: pointer;
            padding: 5px 10px;
            border-radius: 4px;
          }
          >span.hover{
            background: #3a8ee6;
            color: #fff;
          }
          >span:hover{
            background: #3a8ee6;
            color: #fff;
          }
          >span:active{
            background: #3a8ee6;
            color: #fff;
          }
        }
        >li:first-child{
          border-bottom: 1px solid #DCDFE6;
          margin-bottom: 10px;
        }
      }
    }
    >.rightFloat{
      width: calc(100% - 300px);
      padding: 0 20px;
      >.top{
        margin-bottom: 20px;
        .bigCellBox{
          .cellBox{
            display: inline-block;
            height: 50px;
            line-height: 50px;
            text-align: center;
            overflow: hidden;
            outline: 1px solid #EBEEF5;
            width: calc( 20% - 10px );
            margin: 5px 5px;
            box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
            cursor: pointer;
            border-radius: 4px;
          }
          .cellBox.info{
            /*background: #d3d3d3;*/
            background: #909399;
            color: #fff;
          }
          .cellBox.success{
            /*background: #90ee90;*/
            background: #67C23A;
            color: #fff;
          }
          .cellBox.danger{
            /*background: #f08080;*/
            background: #F56C6C;
            color: #fff;
          }
        }
      }
      >.bottom{
        border-top: 1px solid #DCDFE6;
        >.leftFloat{
          padding: 20px;
          >div{
            border-left: 8px solid #409eff;
            border-radius: 4px 0 0 4px;
            padding: 10px 20px;
            >p{
              line-height: 20px;
              >span{
                float: left;
                margin-right: 5px;
              }
              .colorBox{
                display: inline-block;
                width: 50px;
                height: 20px;
                border: 1px solid;
                border-radius: 4px;
              }
              .colorBox.info{
                /*border-color: #d3d3d3;*/
                /*background: #d3d3d3;*/
                border-color: #909399;
                background: #909399;
              }
              .colorBox.success{
                /*border-color: #90ee90;*/
                /*background: #90ee90;*/
                border-color: #67C23A;
                background: #67C23A;
              }
              .colorBox.danger{
                /*border-color: #f08080;*/
                /*background: #f08080;*/
                border-color: #F56C6C;
                background: #F56C6C;
              }
            }
            >p:first-child{
              margin-bottom: 10px;
            }
            >p:last-child{
              span:last-child{
                color: red;
              }
            }

            >p:after {
              display: block;
              clear: both;
              content: "";
              visibility: hidden;
              height: 0
            }
            >p {
              zoom: 1
            }
          }
        }
      }
    }
  }
</style>
