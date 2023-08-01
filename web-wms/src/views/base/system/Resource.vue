<template>
  <div class="resource">
    <div class="publicSearch">
      <div class="searchBar" :style="{height: isMoreSearch?'auto':''}">
        <el-button-group>
          <el-button @click="getResourceData" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="clearSearchData" size="mini" icon="el-icon-refresh">重置</el-button>
        </el-button-group>
        <div>
          <span>位置</span>
          <el-select  v-model="searchData.resourceGroup" clearable style="width: 70%;">
            <el-option label="仓库作业（TASK）" value=1></el-option>
            <el-option  label="基础信息（BASE）" value=2></el-option>
            <el-option  label="报表(REPORT)" value=3></el-option>
            <el-option label="PDA"  value=4></el-option>
            <el-option label="PDA-APP"  value=5></el-option>
          </el-select>
        </div>
      </div>
    </div>

    <div class="headerBtns">
      <el-button size="small" type="primary" round icon="iconfont icon-xinzeng" @click="dialogNewVisible = true">新增
      </el-button>
    </div>

    <el-dialog title="新增资源" :visible.sync="dialogNewVisible" :before-close="handleClose" width="500px"
               style="margin-top: -10vh;">
      <el-form :model="info" ref="info">
        <el-form-item label="资源类型" required :label-width="formLabelWidth" style="width: 100%;">
          <el-select   v-model="info.resourceType" style="width: 70%;">
            <el-option label="目录"  value="M">目录</el-option>
            <el-option  label="页面"   value="F">页面</el-option>
            <el-option  label="按钮"   value="B">按钮</el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="资源名称" required :label-width="formLabelWidth">
          <el-input v-model = "info.resourceName" maxlength="99" onkeyup="this.value=this.value.replace(/\s+/g,'')">
          </el-input>
        </el-form-item>
        <el-form-item label="资源编码" required :label-width="formLabelWidth">
          <el-input v-model = "info.resourceCode" maxlength="99" onkeyup="this.value=this.value.replace(/\s+/g,'')">
          </el-input>
        </el-form-item>

        <el-form-item label="父级资源" required :label-width="formLabelWidth">
          <el-input :disabled="true" v-model = "resourceName" maxlength="10" onkeyup="this.value=this.value.replace(/\s+/g,'')">
          </el-input>
        </el-form-item>
        <el-form-item label="URL" required v-if="info.resourceType=='F'" :label-width="formLabelWidth">
          <el-input v-model = "info.url" maxlength="99" onkeyup="this.value=this.value.replace(/\s+/g,'')">
          </el-input>
        </el-form-item>
        <el-form-item label="权限标识" required v-if="info.resourceType!='M'" :label-width="formLabelWidth">
          <el-input v-model = "info.authorityCode" maxlength="99" onkeyup="this.value=this.value.replace(/\s+/g,'')">
          </el-input>
        </el-form-item>
        <el-form-item label="等级" required v-if="info.resourceType!='B'" :label-width="formLabelWidth">
          <el-input-number style="width: 70%;" v-model="info.level" :min="0" label=" ">
          </el-input-number>
        </el-form-item>
        <el-form-item label="排序" required v-if="info.resourceType!='B'" :label-width="formLabelWidth">
          <el-input-number style="width: 70%;" v-model="info.sortNumber" :min="0" label=" ">
          </el-input-number>
        </el-form-item>
        <el-form-item label="icon类名" required v-if="info.resourceType!='B'" :label-width="formLabelWidth">
          <el-input v-model = "info.icon">
          </el-input>
        </el-form-item>
        <el-form-item label="位置" required :label-width="formLabelWidth">
           <el-select  v-model="info.resourceGroup" style="width: 70%;"> 
            <el-option label="仓库作业（TASK）" value=1></el-option>
            <el-option  label="基础信息（BASE）" value=2></el-option>
            <el-option  label="报表(REPORT)" value=3></el-option>
            <el-option label="PDA"  value=4></el-option>
            <el-option label="PDA-APP"  value=5></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="资源状态" required :label-width="formLabelWidth">
          <el-select   v-model="info.state" style="width: 70%;">
            <el-option label="正常的"  value="normal">正常的</el-option>
            <el-option  label="无效的"   value="invalid">无效的</el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" :label-width="formLabelWidth">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="insertResource">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改资源" :visible.sync="dialogUpdateVisible" :before-close="handleClose2" width="500px"
               style="margin-top: -5vh;">
      <el-form :model="rowData" ref="rowData" style="width: 100%;">
        <el-form-item label="资源类型" required :label-width="formLabelWidth">
          <el-select v-model="rowData.resourceType" style="width: 70%;">
            <el-option label="目录"  value="M">目录</el-option>
            <el-option  label="页面" value="F">页面</el-option>
            <el-option label="按钮"  value="B">按钮</el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="资源名称" required :label-width="formLabelWidth">
          <el-input v-model = "rowData.resourceName" maxlength="99" onkeyup="this.value=this.value.replace(/\s+/g,'')">
          </el-input>
        </el-form-item>
        <el-form-item label="资源编码" required :label-width="formLabelWidth">
          <el-input v-model = "rowData.resourceCode" maxlength="99" onkeyup="this.value=this.value.replace(/\s+/g,'')">
          </el-input>
        </el-form-item>
        <el-form-item label="父级资源" required :label-width="formLabelWidth">
          <el-input  v-model = "rowData.parentId" maxlength="10" onkeyup="this.value=this.value.replace(/\s+/g,'')">
          </el-input>
        </el-form-item>
        <el-form-item label="URL" required v-if="rowData.resourceType=='F'" :label-width="formLabelWidth">
          <el-input v-model = "rowData.url" maxlength="99" onkeyup="this.value=this.value.replace(/\s+/g,'')">
          </el-input>
        </el-form-item>
        <el-form-item label="权限标识" required v-if="rowData.resourceType!='M'" :label-width="formLabelWidth">
          <el-input v-model = "rowData.authorityCode" maxlength="99" onkeyup="this.value=this.value.replace(/\s+/g,'')">
          </el-input>
        </el-form-item>

        <el-form-item label="等级" required v-if="rowData.resourceType!='B'" :label-width="formLabelWidth">
          <el-input-number style="width: 70%;" v-model="rowData.level" :min="0" label=" ">
          </el-input-number>
        </el-form-item>
        <el-form-item label="排序" required v-if="rowData.resourceType!='B'" :label-width="formLabelWidth">
          <el-input-number style="width: 70%;" v-model="rowData.sortNumber" :min="0" label=" ">
          </el-input-number>
        </el-form-item>
        <el-form-item label="icon类名" v-if="rowData.resourceType!='B'" :label-width="formLabelWidth">
          <el-input v-model = "rowData.icon">
          </el-input>
        </el-form-item>
        <el-form-item label="位置" required :label-width="formLabelWidth">
          <el-select  v-model="rowData.resourceGroup" style="width: 70%;">
            <el-option label="仓库作业（TASK）" value=1></el-option>
            <el-option label="基础信息（BASE）" value=2></el-option>
            <el-option label="报表（REPORT）"  value=3></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="资源状态" required :label-width="formLabelWidth">
          <el-select   v-model="rowData.state" style="width: 70%;">
            <el-option label="正常的"  value="normal">正常的</el-option>
            <el-option  label="无效的"   value="invalid">无效的</el-option>
          </el-select>
        </el-form-item>


      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogUpdateVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateResource">确 定</el-button>
      </div>
    </el-dialog>

    <div class="tableBox table-800h">
      <el-table
          v-loading="loading.masterTable"
          highlight-current-row
          :data="resourcesTree"
          style="width: 100%"
          row-key="resourceId"
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
        <el-table-column
            sortable
            show-overflow-tooltip
            width="180"
            prop="resourceName"
            label="资源名称">
        </el-table-column>
        <el-table-column
            sortable
            width="90"
            show-overflow-tooltip
            prop="resourceId"
            label="资源ID">
        </el-table-column>
        <el-table-column
                sortable
                width="90"
                show-overflow-tooltip
                prop="resourceGroup"
                label="位置">
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="resourceType"
            label="资源类型"
            width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.resourceType === 'M'" type="success">目 录</el-tag>
            <el-tag v-if="scope.row.resourceType === 'F'" type="info">页 面</el-tag>
            <el-tag v-if="scope.row.resourceType === 'B'">按 钮</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            sortable
            width="170"
            show-overflow-tooltip
            prop="resourceCode"
            label="资源编码">
        </el-table-column>
        <el-table-column
            sortable
            width="180"
            show-overflow-tooltip
            prop="authorityCode"
            label="权限标识">
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="state"
            label="权限状态"
            width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.state === 'normal'" type="success">正常的</el-tag>
            <el-tag v-if="scope.row.state === 'invalid'" type="warning">无效的</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="parentId"
            label="父级资源">
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="level"
            label="等级">
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="sortNumber"
            label="排序">
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="icon"
            label="icon类名">
        </el-table-column>
        <el-table-column
            fixed="right"
            label="操作"
            width="250">
          <template slot-scope="scope">
              <el-button @click="creatResourceF(scope.row)" icon="iconfont icon-xinzeng" type="info" circle plain size="small"></el-button>
              <el-button @click="updateResourceF(scope.row)" icon="iconfont icon-bianji" type="primary" circle plain size="small"></el-button>
              <el-button @click="isDelete(scope.row.resourceId)" icon="iconfont icon-shanchu" type="danger" circle plain size="small"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

  </div>
</template>

<script>
    export default {
        name: 'Resource',
        data() {
            return {
                loading:{
                  masterTable:true,

                },
                dialogNewVisible: false,
                dialogUpdateVisible: false,
                formLabelWidth: '120px',

                resourcesTree: [],
                // weizhi: '',
                resourceName: '',
                info: {
                    resourceName: '',
                    resourceCode: '',
                    parentId: '',
                    url: '',
                    icon: '',
                    resourceType: '',
                    resourceGroup:1,
                    state:'normal'
                },

                rowData: {
                    resourceName: '',
                    resourceCode: '',
                    parentId: '',
                    url: '',
                    icon: '',
                    resourceType: '',
                    resourceGroup:1,
                    state:'normal'
                },
                searchData: {
                  resourceGroup: null
                }
            }
        },
        methods: {
            clearInfo(){
                this.info = {
                    resourceName: '',
                    resourceCode: '',
                    parentId: '',
                    url: '',
                    icon: '',
                    resourceType: '',
                    resourceGroup:1,
                    state:'normal'
                };
            },
            handleClose(done) {
                this.clearInfo();
                this.dialogNewVisible = false;
            },
            handleClose2(done) {
                this.dialogUpdateVisible = false;
            },

            getResourceData() {
                let that = this;
                that.loading.masterTable = true;
                IOT.getServerData('/resources/list','get', this.searchData, (ret) => {
                    that.loading.masterTable = false;
                    if (ret.code === 200) {
                        let list = ret.rows;
                        for (let i in list) {
                            list[i].id = list[i].resourceId
                        }
                        let listTree = PF.toTree(list);
                        this.resourcesTree = listTree;
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            creatResourceF(rowData) {
                // let resourceGroup = rowData.resourceGroup;
                this.resourceName = rowData.resourceName;
                this.info.parentId = rowData.resourceId;
                this.info.resourceGroup = rowData.resourceGroup;
                console.log(this.info.resourceGroup);
                // if (resourceGroup == 1) {
                //     this.weizhi = '仓库作业（TASK）';
                // }
                // if (resourceGroup == 2) {
                //     this.weizhi = '基础信息（BASE）';
                // }
                // if (resourceGroup == 3) {
                //     this.weizhi = '报表（REPORT）';
                // }


                this.dialogNewVisible = true;
            },
            insertResource(){
                let that = this;
                let resourceDto = PF.JSON(this.info);
                this.clearInfo();
                this.dialogNewVisible = false;
                IOT.showOverlay('保存中，请稍等...');
                IOT.getServerData('/resources/add','post', resourceDto, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('保存成功！', 'success', 1000 , () => {
                            that.getResourceData()
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            updateResource() {
                let that = this;
                let resourceDto = PF.JSON(this.rowData);
                resourceDto._nodes = '';
                resourceDto._last = '';
                resourceDto._parent = '';
                resourceDto._level = '';
                this.dialogUpdateVisible = false;
                console.log(this.rowData);
                IOT.showOverlay('保存中，请稍等...');
                IOT.getServerData('/resources/update','post', resourceDto, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('保存成功！', 'success', 1000 , () => {
                            that.getResourceData();
                            that.rowData = {
                                resourceName: '',
                                parentId: '',
                                url: '',
                                icon: '',
                                group: '',
                                resourceType: '',
                            };
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            updateResourceF(rowData) {
                this.rowData = $.extend({}, rowData);
                console.log(rowData);
                console.log(typeof(rowData));
                this.dialogUpdateVisible = true;
            },
            isDelete(id){
                this.$confirm('是否确认删除？所有子级均会被删除！请谨慎操作！')
                    .then(_ => {
                        this.deleteResourceF(id)
                    })
                    .catch(_ => {});
            },
            deleteResourceF(id) {
                let that = this;
                IOT.showOverlay('保存中，请稍等...');
                IOT.getServerData('/resources/delete','get', {resourceId: id}, (ret) => {
                    IOT.hideOverlay();
                    if (ret.code === 200) {
                        IOT.tips('保存成功！', 'success', 1000 , () => {
                            that.getResourceData();
                        });
                    } else {
                        IOT.tips(ret.message || '服务器请求失败，稍后再试！', 'error');
                    }
                });
            },
            clearSearchData() {
              this.searchData.resourceGroup = null;
            }
        },
        created() {
            this.getResourceData()
        },
        mounted() {
        }
    }
</script>

<style scoped lang="less">
  .resource {

  }
</style>
