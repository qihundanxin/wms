<template>
  <div v-loading="loading" class="baseMove2">
    <FeaturesHeader :headerTitle="headerTitle" />

    <div class="baseAcceptBillBox">
      <el-card>

        <div class="cardBody">
          <div class="topData clearFloat">
            <!-- 头部信息 -->
            <span></span>
            <span>{{params.itemName}}</span>
            <span>{{params.itemCode}}</span>
          </div>
          <div class="minorData ">
            <!-- 基础信息 -->
            <ul class=" ">
              <li>
                <span style="width:60px" v-for="cellUseType in cellUseTypes" v-if="cellUseType.value === params.cellUseType">{{cellUseType.label}}</span>
                <span>&nbsp;{{params.cellCode}}</span> 
              </li>
              <!-- <li>
                <span>LPN:</span>
                <span>&nbsp;{{params.lpn}}</span>
              </li> -->
            </ul>
            <ul class="rightFloat">
              <!-- <li>
                <span>追踪号:</span>
                <span>&nbsp;{{params.boxCode}}</span>
              </li> -->
            </ul>
          </div>
          <div class="mainData clearFloat">
            <!-- 重要信息 -->
            <ul class="leftFloat">
              <li>
                <span v-if="params.quantity">&nbsp;{{params.quantity}}</span>
                <span v-else-if="params.quantity === 0">&nbsp;0</span>
                <span v-else>&nbsp;-</span>
                <span>数量:</span>
              </li>
              <li>
                <span v-if="params.allotQuantity">&nbsp;{{params.allotQuantity}}</span>
                <span v-else-if="params.allotQuantity === 0">&nbsp;0</span>
                <span v-else>&nbsp;-</span>
                <span>分配数:</span>
              </li>
            </ul>
            <ul class="rightFloat">
              <li>
                <span v-if="params.packDescribe">&nbsp;{{params.packDescribe}}</span>
                <span v-else>&nbsp;-</span>
                <span>单位:</span>
              </li>
              <li>
                <span v-if="params.freezeQuantity">&nbsp;{{params.freezeQuantity}}</span>
                <span v-else-if="params.freezeQuantity === 0">&nbsp;0</span>
                <span v-else>&nbsp;-</span>
                <span>冻结数:</span>
              </li>
            </ul>
          </div>
          <div class="minorData">
            <!-- 次要/整行信息 -->
            <ul>
              <li>
                <span>仓库:</span>
                <span>&nbsp;{{params.wareName}}</span>
              </li>
              <li>
                <span>货主:</span>
                <span>&nbsp;{{params.organizationName}}</span>
              </li>
              <li>
                <span>批次:</span>
                <span>&nbsp;{{params.batchName}}</span>
              </li>
            </ul>
          </div>
        </div>

      </el-card>
    </div>

    <div style="text-align: center;">
      <el-form class="noMargin" :model="form" :rules="rules" ref="form">
        <el-form-item v-show="isFindUpperCell" label="推荐货位" :label-width="formLabelWidth">
            <span>{{ form.findUpperCell }}</span>
          </el-form-item>
            <el-form-item  label="" :label-width="formLabelWidth">
           <el-input id="toCellCode"   style="width: 95%;"  v-model="form.toCellCode" size="mini" filterable placeholder="请输入目标货位"  clearable  ></el-input>
             </el-form-item>
             <el-input  placeholder="请输入数量"  v-model="form.toQuantity" size="mini" :min="0"   style="width: 80%;"></el-input>
          <el-button type="primary" size="mini" @click="showAll()">全部</el-button>

          <!-- 上传附件 -->
          <div style="width:100%;height:40px;"><el-button type="primary" round size="mini" style="float: left;margin-top: 10px;margin-left: 10px;" @click="openUpload">附件上传</el-button></div>
          <div class="uploadImg" v-show="uploadImgVisible"  style="text-align: left;">
            <el-upload
            :class="hideUpload==true? 'hide' :'uploadImg1'"
            ref="upload"
            :action="uploadUrl"
            list-type="picture-card"
            :headers="headerObj"
            :multiple="true"
            :limit=3
            :on-exceed="exceedTips"
            :on-change="ChangeImage" 
             accept="image/*"
             capture="camera"
            :on-success="handleSuccess"
            :before-upload="beforeUpload"
            :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove"
            :on-progress="onProgressTest">
            <i class="el-icon-plus"></i>
          </el-upload>
          <el-dialog  :visible.sync="dialogVisible">
            <img width="100%" :src="dialogImageUrl" alt="">
          </el-dialog>
          </div>
          <p style="color:#409eff;cursor: pointer; font-weight: bold; text-align: left;margin-left:20px;margin-top: 10px;" @click="deleteImgs" v-show="imgs.length>0">删除</p>

        </el-form>
    </div>
    <div style="height: 50px;"><!--留白，为底部悬浮按钮留空间，保证不遮挡上面文字--></div>
    <el-button v-show="params.cellUseType==3?false:true" @click="commitForm('form')" type="primary" size="mini" round icon="iconfont icon-shangjia" style="position: fixed;bottom: 30px;left:14vw; height: 40px; width: 70%;">移库</el-button>
  </div>
</template>
<script>
  import FeaturesHeader from "@/components/FeaturesHeader";
  import {mapState} from "vuex";
  import server from "../../server"

  export default {
    name: "BaseMove2",
    computed: {
            ...mapState([
                "cellUseTypes",
            ]),
        },
    data() {
      let validateQuantity = (rule, value, callback) => {
        if (value <= 0) {
          callback(new Error('数量不得为零'));
          callback();
        } else if (value > 0) {
          callback();
        } else {
          callback(new Error('请输入正确格式'));
        }
      };
      return {
        hideUpload:false,
        headerObj: {
           "access-token": localStorage.getItem('pda-token')
        },
        uploadImgs:'',
        uploadUrl:server.baseUrl + '/wms/file/move/uploadImg',
        dialogImageUrl: '',
        uploadImgVisible:false,
        dialogVisible: false,
        headerTitle: "基础移库",
        formLabelWidth: '0px',
        params: this.$route.params,

        isFindUpperCell: false,

        inventoryBalances: [],
        cells: [],
        loading:false,
        searchData: {
          pageNum: 1,
          pageSize: 2,
          total: null,
          keyWords: '',
          wareId: PF.getLocal('wareId', 'number'),
          toBoxCode: '',
          itemCode: '',
        },
         wareId: PF.getLocal('wareId', 'number'),
        form: {
          row: this.$route.params,
          findUpperCell: '',
          toCellCode: '',
          toBoxCode: '',
          toQuantity: null,
          toLpn:''
        },
        nowQuantity:null,
        rules: {
          toCellCode: [
            {required: true, message: '请选择目标货位', trigger: 'blur'}
          ],
          // toBoxCode: [
          //   {required: true, message: '请选择目标追踪号', trigger: 'blur'}
          // ],
          toQuantity: [
            {required: true, message: '请输入数量', trigger: 'blur'},
            {validator: validateQuantity, trigger: 'blur'}
          ],
        },
        imgs:[],
       
      };
    },
    methods: {
      
      ChangeImage(file, fileList) {
          this.hideUpload = fileList.length >= 3;
      },
      deleteImgs(){
        this.$refs.upload.clearFiles();
        this.imgs ="";
        this.hideUpload = false;
      },
      //进度条
      onProgressTest (event, file, fileList) {
        let index = 0
        // 只有fileList 跟file 里面的uid是唯一的互相对应的
        fileList.forEach((v, i) => {
          if (v.uid === file.uid) {
            index = i
          }
        })
        // 获取动态的进度
        let percent = parseInt(event.percent)
        this.$nextTick(() => {
          setTimeout(() => {
            if (percent >= 99) {
            // 当动态的进度超过99就获取dom 让值一直显示99直到上传成功
              let dom = document.getElementsByClassName('el-upload-list__item')[index]
              let progressText = dom.getElementsByClassName('el-progress__text')
              if (progressText && progressText.length) {
                progressText[0].style.opacity = 0
                progressText[0].innerText = '99%'
                progressText[0].style.opacity = 1
              }
            }
          })
        })
      },
      handleSuccess(response, file, fileList) {
      if (response.code != 200) {
        this.$message.error('上传出错：' + response.message);
        // 从文件列表中删除上传出错的文件
        const index = fileList.findIndex(item => item.uid === file.uid);
        if (index !== -1) {
          fileList.splice(index, 1);
        }
      } else {
        this.$message.success(file.name + '上传成功');
        this.imgs = fileList.map((item)=>{
          if(item && item.response && item.response.data && item.response.data.fullPath){
            return item.response.data.fullPath
          }
        }).join(",")
      }
    },
      //图片上传前校验
      beforeUpload(file) {
        const fileSuffix = file.name.substring(file.name.lastIndexOf(".") + 1);
        const whiteList = ["jpg", "png", "jpeg"];
        if (whiteList.indexOf(fileSuffix) === -1) {
          this.$message.error('上传图片格式只能是jpg、png、jpeg');
          return false;
        }
        const isLt2M = file.size / 1024 / 1024 < 10;
        if (!isLt2M) {
          this.$message.error('上传文件大小不能超过 10MB');
          return false;
        }
      },

      handleRemove(file, fileList) {
        this.hideUpload = fileList.length >= 3;
        this.imgs = fileList.map((item)=>{
          return item.response.data.fullPath
        }).join(",");
      },
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url;
        this.dialogVisible = true;
      },
      openUpload(){
        this.uploadImgVisible = true;
      },
      exceedTips(){
        IOT.tips('最多上传3张图片！', 'warning');
      },
      showAll(){
        this.form.toQuantity = this.nowQuantity;
      },
      findUpperCell() {
        this.loading =true;
        IOT.getServerData('/upper/tasks/findUpperCell', 'get', this.params, (ret) => {
           this.loading =false;
          if (ret.code === 200) {
            this.isFindUpperCell = true;
            this.form.findUpperCell = ret.data;
            this.form.toCellCode = ret.data;
          } else if (ret.code === 122) {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'warning');
            this.isFindUpperCell = false;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        }, true);
      },
      getCellData(){
        IOT.getServerData('/cell/infos/list','get',{},(ret) => {
          if (ret.code === 200) {
            this.cells = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
    //移库操作
    commitForm(formName) {
      let that = this;
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let form = PF.JSON(this.form);
          if (form.toCellCode == this.params.cellCode) {
            IOT.tips('移库前后货位相同，请重新填写', 'error');
            return false;
          }
          if (form.toQuantity > 0) {

          } else {
            IOT.tips('请校验数量', 'error');
            return false;
          }
          if (Number(form.toQuantity) === form.row.quantity) {
            // 全部移库 不限制
          } else if (form.row.allotQuantity + form.row.freezeQuantity + Number(form.toQuantity) > form.row.quantity) {
            // 部分移库 限制只能移走可分配库存部分
            let tip = '最多可移库' + (form.row.quantity - form.row.freezeQuantity - form.row.allotQuantity);
            if (form.row.freezeQuantity + form.row.allotQuantity != 0) {
              tip = tip + '，或将' + form.row.quantity + '一次性全部移走';
            }
            IOT.tips(tip, 'error', 2000);
            return false;
          }
          this.$confirm('移库' + form.toQuantity + '件，到' + form.toCellCode, '确认移库', {
            confirmButtonText: '确定',
            cancelButtonText: '取消'
          }).then(() => {
            this.loading = true;
            // var imgs = JSON.stringify(this.imgs).replace(/\\/g, '');
            if(this.imgs.length  === 0 ){
              this.imgs = '';
            }
            IOT.getServerData('/inventorys/move', 'post', {
              inventoryId: form.row.inventoryId,
              cellCode: form.toCellCode,
              quantity: form.toQuantity,
              wareId: that.wareId,
              transactImgs:this.imgs,
            }, (ret) => {
              if (ret.code === 200) {
                this.resetForm(formName);
                this.uploadImgVisible = false;
                this.$refs.upload.clearFiles();
                this.loading = false;
                IOT.tips('保存成功！', 'success', 1000, () => {
                  this.form.toCellCode= '';
                  this.form.toQuantity= null;
                  this.imgs = '';
                  if(ret.data){
                      this.params = ret.data;
                      this.$set(this.form, 'row', this.params);
                      this.nowQuantity = this.params.quantity;
                    }else{
                      this.params.quantity = 0;
                      this.params.allotQuantity = 0;
                      this.params.freezeQuantity = 0;
                    }
                  // if (this.params == null) {
                  //   this.params.quantity = 0;
                    // this.$router.push({ name: 'baseMove', params: {} });
                  }
                  //  this.$router.push({name: '',params: {toBoxCode: form.fromBoxCode}})
                  //挑转到上一个页面
                // }
                );
              } else {
                this.loading = false;
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            }, true);
          });
        } else {
          IOT.hideOverlay();
          return false;
        }
      });
    },
      open(){
        if(this.params.cellUseType==3){
         IOT.tips('警告，集货位不可操作基础移库！','warning',2000)
       }
      }
    },
    created() {
      this.nowQuantity = this.params.quantity;
      if(this.params.quantity==null ){
        this.PageParam_BaseMove_Search_Use = false;// 此种形式回退不携带查询条件
        this.$router.push({name: 'baseMove',params: {}});
      }
     // this.findUpperCell();
     setTimeout(function () { 
         var domFocus = document.getElementById("toCellCode"); 
         domFocus.focus();
      }, 100);

     // this.getCellData();
    },
  mounted() {
    this.open();
  },
  beforeDestroy() {
    if (PF.getLocal('PageParam_BaseMove_Search') && this.PageParam_BaseMove_Search_Use !== false) {
      PF.setLocal('PageParam_BaseMove_Search_Use', true);
      setTimeout(() => {
        PF.removeLocal('PageParam_BaseMove_Search_Use');
      }, 1000);// 1秒内返回到baseMove页面，则携带查询条件
    }
  },
    components: {
      FeaturesHeader,
    }
  
  };
</script>
<style lang="less">
  .baseMove2 {
    .uploadImg{
      margin-top:10px !important;
      .uploadImg1{
        text-align: left;
        margin-left:20px;
      }
    }
  }
  .el-upload-list--picture-card .el-upload-list__item{
    width:100px !important;
    height: 100px !important;
    border-radius: 50% !important; 
  }
  .el-upload--picture-card{
    width:100px !important;
    height: 100px !important;
    border-radius: 50% !important;
    line-height: 100px !important;
  }
  
.hide .el-upload--picture-card {
	display: none;
}
</style>
