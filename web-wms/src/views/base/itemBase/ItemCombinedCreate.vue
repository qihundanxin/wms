<template>
  <div class="createSoBill">

    <el-dialog width="900px" title="选择物料" :visible.sync="innerVisibleItem" style="margin-top: -10vh;">

<!--      <div class="publicSearch body">-->
<!--        <div class="searchBar" :style="{height: isMoreSearch2?'auto':''}">-->
<!--          <el-button-group>-->
<!--            <el-button @click="getItemData" size="mini" icon="el-icon-search">查询</el-button>-->
<!--&lt;!&ndash;            <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>&ndash;&gt;-->
<!--          </el-button-group>-->
<!--          <div>-->
<!--            <span>关键字搜索</span>-->
<!--            <el-input v-model="searchData.keyWords" size="mini" placeholder="SKU名称/SKU" clearable></el-input>-->
<!--          </div>-->
<!--        </div>-->
<!--        <span class="moreSearchBtn"-->
<!--              :class="isMoreSearch2?'el-icon-caret-top':'el-icon-caret-bottom'"-->
<!--              @click="isMoreSearch2 = !isMoreSearch2"></span>-->
<!--      </div>-->

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
              <span>{{scope.row.one - (scope.row.two + scope.row.three)}}</span>
            </template>
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="packDescribe"
            label="单位"
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
      </div>

    </el-dialog>



    <div class="publicSearch bigFormBox3">
      <div class="searchBar" :style="{height: 'auto'}">
        <p style="font-size: 20px;font-weight: 600;line-height: 20px;margin-top: 15px;">产品组件信息</p>
        <el-form class="clearFloat" :model="itemCombined" :rules="rules" ref="itemCombinedForm">
          <div style="float: left;width: 100%;">
            <el-divider content-position="left"></el-divider>
          </div>
          <el-form-item label="客户" prop="organizationId" :label-width="formLabelWidth">
            <el-select v-model="itemCombined.organizationId" filterable placeholder="请选择货主" size="mini"
                       style="width: 100%">
              <el-option
                v-for="item in organizations"
                :key="item.organizationId"
                :label="item.organizationName"
                :value="item.organizationId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="组件" prop="itemCode" :label-width="formLabelWidth">
            <el-select v-model="itemCombined.itemCode" size="mini" style="width: 100%">
              <el-option
              v-for="item in items"
              :key="item.itemCode"
              :label="item.itemName"
              :value="item.itemCode"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="工序描述" prop="procedure" :label-width="formLabelWidth">
            <el-input v-model="itemCombined.procedure" size="mini"></el-input>
          </el-form-item>
          <el-form-item label="desc1" prop="desc1" :label-width="formLabelWidth">
            <el-input v-model="itemCombined.desc1" size="mini"></el-input>
          </el-form-item>
          <el-form-item label="desc2" prop="desc2" :label-width="formLabelWidth">
            <el-input v-model="itemCombined.desc2" size="mini"></el-input>
          </el-form-item>
          <el-form-item label="费率" prop="price" :label-width="formLabelWidth" style="width: 100%">
            <el-input-number v-model="itemCombined.price" size="mini"></el-input-number>
          </el-form-item>
          <el-form-item label="激活状态" prop="state" :label-width="formLabelWidth">
            <el-switch
                    v-model="itemCombined.state"
                    active-value=1
                    inactive-value=0>
            </el-switch>
          </el-form-item>
          <el-form-item label="包装材料标志" prop="isPacking" :label-width="formLabelWidth">
            <el-switch
                    v-model="itemCombined.isPacking"
                    active-value=1
                    inactive-value=0>
            </el-switch>
          </el-form-item>
          <el-form-item label="版本号" prop="ver" :label-width="formLabelWidth">
            <el-input v-model="itemCombined.ver" size="small"></el-input>
          </el-form-item>

        </el-form>
      </div>
    </div>

    <div class="tableBox">
      <el-table
        highlight-current-row
        :data="itemCombined.itemComponents"
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
<!--        <el-table-column-->
<!--          label="SKU"-->
<!--          width="240">-->
<!--          <template slot-scope="scope">-->
<!--            <el-input v-model="scope.row.itemCode" @blur="changeItemCode(scope.row.itemCode, scope.$index)"-->
<!--                      :disabled="form.soMaster.organizationId === '' || form.soMaster.organizationId === undefined || form.soMaster.organizationId === null-->
<!--                      || form.soDetails[scope.$index].id > 0"-->
<!--                      placeholder="请输入SKU" size="small" clearable style="width: 80%;"></el-input>-->
<!--            <el-button @click="showItemModal(scope.$index)"-->
<!--                       icon="iconfont icon-secondsearch_accurate" type="primary" circle plain size="mini"></el-button>-->
<!--          </template>-->
<!--        </el-table-column>-->

      </el-table>
    </div>

    <div class="createFootBox clearFloat" :style="{width: isCollapse ? 'calc(100% - 104px)' : 'calc(100% - 240px)'}">
      <div class="leftFloat">
        <el-button type="info" @click="$router.go(-1)">返 回</el-button>
      </div>
      <el-button size="small" type="primary" round icon="iconfont icon-jia1" @click="addRow"> 新增一行</el-button>
      <div class="rightFloat">
        <el-button @click="commitForm('itemCombinedForm')" type="primary">提 交</el-button>
      </div>
    </div>

  </div>
</template>

<script>
  export default {
    name: 'CreateSoBill',
    data() {
      return {
        organizations: [],
        customers: [],
        items: [],
        wares: [],
        ids: [],
        soUdfHs: [],
        soUdfDs: [],
        soBillTypes: [],
        receivingAddresss: [],
        carriers: [],

        selectDetailIndex: null,
        oldOrganizationId: null,

        innerVisibleItem: false,

        formLabelWidth: '120px',

        itemCombined: {
          organizationId: null,
          itemCode: '',
          desc1: '',
          desc2: '',
          price: null,
          procedure: '',
          state: null,
          isPacking: null,
          ver: '',
          itemComponents: []
        },

        rules: []

      }
    },
    computed: {
      isCollapse() {
        return this.$parent.leftMenuIsCollapse;
      },
      organizationId(){
        return this.form.soMaster.organizationId
      },
      customerId(){
        return this.form.soMaster.customerId
      },
    },
    watch: {
      organizationId(val){
        for(let i=0;i<this.organizations.length;i++){
          if(this.organizations[i].organizationId === val){
            this.form.soMaster.organizationName = this.organizations[i].organizationName;
          }
        }
      },
      customerId(val){
        for(let i=0;i<this.customers.length;i++){
          if(this.customers[i].customerId === val){
            this.form.soMaster.customerName = this.customers[i].customerName;
          }
        }
      },
    },
    methods: {
      getWareData(){
        IOT.getServerData('/ware/infos/list','get',{},(ret) => {
          if (ret.code === 200) {
            this.wares = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
      addRow() {
      },
      deleteRow(index) {
      },
      showItemModal(index){
        this.innerVisibleItem = true;
      },

      resetForm(formName) { // 重置
        this.$refs[formName].resetFields();
      },
      commitForm(formName) {
        IOT.showOverlay('保存中，请稍等...');
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let form = PF.JSON(this.itemCombined);

            IOT.getServerData('/item/combined/add', 'post', form, (ret) => {
              IOT.hideOverlay();
              if (ret.code === 200) {
                IOT.tips('保存成功！', 'success');
                this.$router.push({name: 'itemCombined'})
              } else {
                IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
              }
            });
            this.resetForm(formName);
          } else {
            IOT.hideOverlay();
            console.error('error submit!!');
            return false;
          }
        });
      },

      getItemData() {
        IOT.getServerData('/item/infos/list', 'get', {}, (ret) => {
          if (ret.code === 200) {
            this.items = ret.rows;
          } else {
            IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
          }
        });
      },
    },
    created() {
      PF.getOrganizations((rows) => {
        this.organizations = rows;
      });
      PF.getCustomers((rows) => {
        this.customers = rows;
      });
      this.getWareData();
      this.getItemData()
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .createSoBill {
    padding-bottom: 100px;
  }
</style>
