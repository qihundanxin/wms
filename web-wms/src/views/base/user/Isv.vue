<template>
  <div class="account">
    <div class="publicSearch">
      <div
          class="searchBar"
          :style="{ height: isMoreSearch ? 'auto' : '' }"
      >
        <el-button-group>
          <el-button
              @click="getAccountData"
              size="mini"
              icon="el-icon-search"
          >查询
          </el-button
          >
          <el-button
              @click="clearSearchData"
              size="mini"
              icon="el-icon-refresh"
          >重置
          </el-button
          >
        </el-button-group>
        <div>
          <span>关键字搜索</span>
          <el-input
              v-model="searchData.keyWords"
              size="mini"
              placeholder="姓名/账号/工号/联系电话/邮箱"
              clearable
          ></el-input>
        </div>
        <div>
          <span>状态</span>
          <el-select
              v-model="searchData.accountStatus"
              size="mini"
              placeholder="请选择状态"
          >
            <el-option
                v-for="item in searchStates"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            >
            </el-option>
          </el-select>
        </div>
        <!--<div>
  <span>部门</span>
  <el-select v-model="searchData.deptId" size="mini" placeholder="请选择部门">
    <el-option
            v-for="department in departments"
            :key="department.deptId"
            :label="department.deptName"
            :value="department.deptId">
    </el-option>
    </el-option>
  </el-select>
</div>-->
        <div>
          <span>部门</span>
          <el-cascader
              size="mini"
              placeholder="请选择部门"
              v-model="searchData.deptId"
              :options="departmentsTree"
              filterable
          ></el-cascader>
        </div>
      </div>
      <span
          class="moreSearchBtn"
          :class="
                    isMoreSearch ? 'el-icon-caret-top' : 'el-icon-caret-bottom'
                "
          @click="isMoreSearch = !isMoreSearch"
      ></span>
    </div>
    <div class="headerBtns">
      <el-button
          size="small"
          type="primary"
          round
          icon="iconfont icon-xinzeng"
          @click="dialogNewVisible = true"
      >新增
      </el-button
      >
      <el-button
          size="small"
          type="warning"
          round
          plain
          icon="iconfont icon-jiaose"
          @click="dialogRoleVisible = true"
      >角色
      </el-button
      >
    </div>
    <el-dialog
        title="新增账户"
        :visible.sync="dialogNewVisible"
        :before-close="handleClose"
        width="500px"
        style="margin-top: -10vh"
    >
      <el-form :model="form" :rules="rules" ref="form">
        <el-form-item
            label="账号"
            prop="account"
            :label-width="formLabelWidth"
        >
          <el-input
              v-model="form.account"
              placeholder="请输入账号"
          ></el-input>
        </el-form-item>
        <el-form-item
            label="密码"
            prop="password"
            :label-width="formLabelWidth"
        >
          <el-input
              v-model="form.password"
              placeholder="请输入密码"
          ></el-input>
        </el-form-item>
        <el-form-item
            label="姓名"
            prop="userName"
            :label-width="formLabelWidth"
        >
          <el-input
              v-model="form.userName"
              placeholder="请输入姓名"
          ></el-input>
        </el-form-item>
        <el-form-item
            label="性别"
            prop="gender"
            :label-width="formLabelWidth"
        >
          <el-radio v-model="form.gender" label="1">先生</el-radio>
          <el-radio v-model="form.gender" label="2">女士</el-radio>
        </el-form-item>
        <el-form-item
            label="工号"
            prop="jobNumber"
            :label-width="formLabelWidth"
        >
          <el-input
              v-model="form.jobNumber"
              placeholder="请输入工号"
          ></el-input>
        </el-form-item>
        <el-form-item
            label="联系电话"
            prop="mobile"
            :label-width="formLabelWidth"
        >
          <el-input
              v-model.number="form.mobile"
              placeholder="请输入联系电话"
          ></el-input>
        </el-form-item>
        <el-form-item
            label="邮箱"
            prop="email"
            :label-width="formLabelWidth"
        >
          <el-input
              v-model="form.email"
              placeholder="请输入邮箱"
          ></el-input>
        </el-form-item>
        <el-form-item
            label="token"
            prop="token"
            :label-width="formLabelWidth"
        >
          <el-input
              v-model="form.token"
              placeholder="请输入token"
          ></el-input>
        </el-form-item>
        <!--<el-form-item label="部门" prop="deptId" :label-width="formLabelWidth">
  <el-select v-model="form.deptId" filterable placeholder="请选择部门" style="width: 70%;">
    <el-option
            v-for="department,index in departments"
            :key="department.deptId"
            :label="department.deptName"
            :value="department.deptId">
    </el-option>
  </el-select>
</el-form-item>-->
        <el-form-item
            label="部门"
            prop="deptId"
            :label-width="formLabelWidth"
        >
          <el-cascader
              placeholder="请选择部门"
              v-model="form.deptId"
              :options="departmentsTree"
              style="width: 70%"
              filterable
          ></el-cascader>
        </el-form-item>
        <el-form-item
            label="菜单"
            prop="url"
            :label-width="formLabelWidth"
        >
          <el-cascader
              v-model="form.url"
              placeholder="请选择菜单"
              :options="menusOptions"
          >
          </el-cascader>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button
            @click="
                        resetForm('form');
                        dialogNewVisible = false;
                    "
        >取 消
        </el-button
        >
        <el-button type="primary" @click="commitForm('form')"
        >确 定
        </el-button
        >
      </div>
    </el-dialog>
    <el-dialog
        title="修改账户"
        :visible.sync="dialogUpdateVisible"
        :before-close="handleClose2"
        width="700px"
        style="margin-top: -10vh"
    >
      <el-form :model="rowData" :rules="rules2" ref="rowData">
        <el-form-item
            label="账号"
            prop="account"
            :label-width="formLabelWidth"
        >
          <el-input
              v-model="rowData.account"
              placeholder="请输入账号"
          ></el-input>
        </el-form-item>
        <el-form-item
            label="密码"
            prop="password"
            :label-width="formLabelWidth"
        >
          <el-input
              v-model="rowData.password"
              placeholder="请输入密码"
          ></el-input>
        </el-form-item>
        <el-form-item
            label="姓名"
            prop="userName"
            :label-width="formLabelWidth"
        >
          <el-input
              v-model="rowData.userName"
              placeholder="请输入姓名"
          ></el-input>
        </el-form-item>
        <el-form-item
            label="性别"
            prop="gender"
            :label-width="formLabelWidth"
        >
          <el-radio v-model="rowData.gender" label="1">先生</el-radio>
          <el-radio v-model="rowData.gender" label="2">女士</el-radio>
        </el-form-item>
        <el-form-item
            label="工号"
            prop="jobNumber"
            :label-width="formLabelWidth"
        >
          <el-input
              v-model="rowData.jobNumber"
              placeholder="请输入工号"
          ></el-input>
        </el-form-item>
        <el-form-item
            label="联系电话"
            prop="mobile"
            :label-width="formLabelWidth"
        >
          <el-input
              v-model.number="rowData.mobile"
              placeholder="请输入联系电话"
          ></el-input>
        </el-form-item>
        <el-form-item
            label="邮箱"
            prop="email"
            :label-width="formLabelWidth"
        >
          <el-input
              v-model="rowData.email"
              placeholder="请输入邮箱"
          ></el-input>
        </el-form-item>
        <el-form-item
            label="token"
            prop="token"
            :label-width="formLabelWidth"
        >
          <el-input
              v-model="rowData.token"
              placeholder="请输入token"
          ></el-input>
        </el-form-item>
        <!--<el-form-item label="部门" prop="deptId" :label-width="formLabelWidth">
  <el-select v-model="rowData.deptId" filterable placeholder="请选择部门" style="width: 70%;">
    <el-option
            v-for="department,index in departments"
            :key="department.deptId"
            :label="department.deptName"
            :value="department.deptId">
    </el-option>
  </el-select>
</el-form-item>-->
        <el-form-item
            label="部门"
            prop="deptId"
            :label-width="formLabelWidth"
        >
          <el-cascader
              placeholder="请选择部门"
              v-model="rowData.deptId"
              :options="departmentsTree"
              style="width: 70%"
              filterable
          ></el-cascader>
        </el-form-item>
        <el-form-item
            label="菜单"
            prop="url"
            :label-width="formLabelWidth"
        >
          <el-cascader
              v-model="rowData.url"
              placeholder="请选择菜单"
              :options="menusOptions"
          >
          </el-cascader>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogUpdateVisible = false"
        >取 消
        </el-button
        >
        <el-button type="primary" @click="updateForm('rowData')"
        >确 定
        </el-button
        >
      </div>
    </el-dialog>
    <el-dialog
        title="绑定权限"
        :visible.sync="dialogWareVisible"
        :before-close="handleCloseWare"
        width="700px"
        style="margin-top: -10vh"
    >
      <el-form :model="wareForm" ref="wareForm">
        <el-form-item
            label="绑定仓库"
            prop="checkWare"
            :label-width="formLabelWidth"
            class="checkItemBoxHeight"
        >
          <el-checkbox-group v-model="wareForm.checkWare">
            <el-checkbox
                v-for="(ware, index) in wares"
                :key="index"
                :label="ware.wareId"
            >{{ ware.wareName }}
            </el-checkbox
            >
          </el-checkbox-group>
        </el-form-item>
        <el-form-item
            label="绑定货主"
            prop="checkOrganization"
            :label-width="formLabelWidth"
            class="checkItemBoxHeight"
        >
          <el-checkbox-group v-model="wareForm.checkOrganization">
            <el-checkbox
                v-for="(organization, index) in organizations"
                :key="index"
                :label="organization.organizationId"
            >{{ organization.organizationName }}
            </el-checkbox
            >
          </el-checkbox-group>
        </el-form-item>
        <el-form-item
            label="绑定角色"
            prop="checkRole"
            :label-width="formLabelWidth"
            class="checkItemBoxHeight"
        >
          <el-checkbox-group v-model="wareForm.checkRole">
            <el-checkbox
                v-for="(role, index) in roles"
                :key="index"
                :label="role.roleId"
            >
              {{ role.roleName }}
              <el-button
                  @click="
                                    dialogBindLookRoleTreeVisible = true;
                                    getRoleDetail(role.roleId);
                                "
                  icon="el-icon-more"
                  type="text"
                  size="small"
              ></el-button>
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button
            @click="
                        resetForm('wareForm');
                        dialogWareVisible = false;
                    "
        >取 消
        </el-button
        >
        <el-button type="primary" @click="commitWareForm('wareForm')"
        >确 定
        </el-button
        >
      </div>
      <el-dialog
          append-to-body
          title="角色权限"
          :visible.sync="dialogBindLookRoleTreeVisible"
          width="500px"
          style="margin-top: -5vh"
      >
        <el-input placeholder="输入关键字进行过滤" v-model="filterText">
        </el-input>
        <el-tree
            style="height: 400px; overflow: auto"
            class="filter-tree"
            :data="roleTree"
            :props="defaultProps"
            accordion
            show-checkbox
            node-key="id"
            :default-expanded-keys="[0]"
            :default-checked-keys="defaultCheckRoleTree"
            :filter-node-method="filterNode"
            ref="tree"
        >
        </el-tree>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogBindLookRoleTreeVisible = false"
          >取 消
          </el-button
          >
        </div>
      </el-dialog>
    </el-dialog>
    <el-dialog
        title="查看权限"
        :visible.sync="dialogLookVisible"
        :before-close="handleCloseLook"
        width="700px"
        style="margin-top: -10vh"
    >
      <el-form :model="lookForm" ref="lookForm">
        <el-form-item
            label="绑定仓库"
            :label-width="formLabelWidth"
            class="checkItemBoxHeight"
        >
          <el-checkbox-group v-model="bindInfos.wareIds">
            <el-checkbox
                v-for="(ware, index) in lookForm.checkWare"
                :key="index"
                :label="ware.wareId"
                disabled
            >{{ ware.wareName }}
            </el-checkbox
            >
          </el-checkbox-group>
        </el-form-item>
        <el-form-item
            label="绑定货主"
            :label-width="formLabelWidth"
            class="checkItemBoxHeight"
        >
          <el-checkbox-group v-model="bindInfos.organizationIds">
            <el-checkbox
                v-for="(
                                organization, index
                            ) in lookForm.checkOrganization"
                :key="index"
                :label="organization.organizationId"
                disabled
            >{{ organization.organizationName }}
            </el-checkbox
            >
          </el-checkbox-group>
        </el-form-item>
        <el-form-item
            label="绑定角色"
            :label-width="formLabelWidth"
            class="checkItemBoxHeight"
        >
          <el-checkbox-group v-model="bindInfos.roleIds">
            <el-checkbox
                v-for="(role, index) in lookForm.checkRole"
                :key="index"
                :label="role.roleId"
                disabled
            >
              {{ role.roleName }}
              <el-button
                  @click="
                                    dialogLookRoleTreeVisible = true;
                                    getRoleDetail(role.roleId);
                                "
                  icon="el-icon-more"
                  type="text"
                  size="small"
              ></el-button>
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogLookVisible = false">取 消</el-button>
      </div>
      <el-dialog
          append-to-body
          title="角色权限"
          :visible.sync="dialogLookRoleTreeVisible"
          width="500px"
          style="margin-top: -5vh"
      >
        <el-input placeholder="输入关键字进行过滤" v-model="filterText">
        </el-input>
        <el-tree
            style="height: 400px; overflow: auto"
            class="filter-tree"
            :data="roleTree"
            :props="defaultProps"
            accordion
            show-checkbox
            node-key="id"
            :default-expanded-keys="[0]"
            :default-checked-keys="defaultCheckRoleTree"
            :filter-node-method="filterNode"
            ref="tree"
        >
        </el-tree>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogLookRoleTreeVisible = false"
          >取 消
          </el-button
          >
        </div>
      </el-dialog>
    </el-dialog>
    <el-dialog
        title="角色管理"
        :visible.sync="dialogRoleVisible"
        width="700px"
        class="account_roleModal"
        style="margin-top: -10vh"
    >
      <div class="searchBarBox">
        <div class="searchBar" style="height: 50px">
          <div>
            <span>关键字搜索</span>
            <el-input
                v-model="searchRoleData.keyWords"
                size="mini"
                placeholder="角色名称"
                clearable
            ></el-input>
          </div>
          <div>
            <el-button-group>
              <el-button
                  @click="getRoleData"
                  size="mini"
                  icon="el-icon-search"
              >查询
              </el-button
              >
              <el-button
                  @click="clearSearchRoleData"
                  size="mini"
                  icon="el-icon-refresh"
              >重置
              </el-button
              >
            </el-button-group>
          </div>
        </div>
      </div>
      <div class="headerBtns">
        <el-button
            size="small"
            plain
            icon="iconfont icon-xinzeng"
            @click="dialogNewRoleVisible = true"
        >新增角色
        </el-button
        >
      </div>
      <div class="tableBox">
        <el-table
            max-height="460"
            :data="roles"
            style="width: 100%"
            border
        >
          <el-table-column type="index" width="50" align="center">
          </el-table-column>
          <el-table-column
              sortable
              show-overflow-tooltip
              prop="roleName"
              label="角色名称"
              width="180"
          >
          </el-table-column>
          <el-table-column
              sortable
              show-overflow-tooltip
              prop="roleDescribe"
              label="角色描述"
          >
          </el-table-column>
          <!--<el-table-column
      sortable
      show-overflow-tooltip
        prop="companyName"
        label="所属机构">
</el-table-column>-->
          <el-table-column label="操作" width="200">
            <template slot-scope="scope">
              <el-button
                  @click="
                                    isUpdateRole = false;
                                    showRoleDetail(scope.row);
                                "
                  icon="iconfont icon-chakan1"
                  type="primary"
                  circle
                  plain
                  size="small"
              ></el-button>
              <el-button
                  v-if="scope.row.createUserId != -1"
                  @click="
                                    isUpdateRole = true;
                                    showRoleDetail(scope.row);
                                "
                  icon="iconfont icon-quanxian"
                  type="warning"
                  circle
                  plain
                  size="small"
              ></el-button>
              <el-button
                  v-if="scope.row.createUserId != -1"
                  @click="showUpdateRoleModal(scope.row)"
                  icon="iconfont icon-bianji"
                  type="primary"
                  circle
                  plain
                  size="small"
              ></el-button>
              <el-button
                  v-if="scope.row.createUserId != -1"
                  @click="isDeleteRole(scope.row.roleId)"
                  icon="iconfont icon-shanchu"
                  type="danger"
                  circle
                  plain
                  size="small"
              ></el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button
            @click="
                        clearSearchRoleData();
                        dialogRoleVisible = false;
                    "
        >取 消
        </el-button
        >
      </div>
      <el-dialog
          append-to-body
          title="新增角色"
          :visible.sync="dialogNewRoleVisible"
          :before-close="handleCloseRole"
          width="500px"
          style="margin-top: -5vh"
      >
        <el-form :model="roleForm" :rules="roleRules" ref="roleForm">
          <el-form-item
              label="角色名称"
              prop="roleName"
              :label-width="formLabelWidth"
          >
            <el-input
                v-model="roleForm.roleName"
                placeholder="请输入角色名称"
            ></el-input>
          </el-form-item>
          <el-form-item
              label="角色描述"
              prop="roleDescribe"
              :label-width="formLabelWidth"
          >
            <el-input
                v-model="roleForm.roleDescribe"
                placeholder="请输入角色描述"
            ></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button
              @click="
                            resetForm('roleForm');
                            dialogNewRoleVisible = false;
                        "
          >取 消
          </el-button
          >
          <el-button
              @click="commitRoleForm('roleForm')"
              type="primary"
          >确 定
          </el-button
          >
        </div>
      </el-dialog>
      <el-dialog
          append-to-body
          title="修改角色"
          :visible.sync="dialogUpdateRoleVisible"
          :before-close="handleCloseRole2"
          width="500px"
          style="margin-top: -5vh"
      >
        <el-form
            :model="roleRowData"
            :rules="roleRules2"
            ref="roleRowData"
        >
          <el-form-item
              label="角色名称"
              prop="roleName"
              :label-width="formLabelWidth"
          >
            <el-input
                v-model="roleRowData.roleName"
                placeholder="请输入角色名称"
            ></el-input>
          </el-form-item>
          <el-form-item
              label="角色描述"
              prop="roleDescribe"
              :label-width="formLabelWidth"
          >
            <el-input
                v-model="roleRowData.roleDescribe"
                placeholder="请输入角色描述"
            ></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button
              @click="
                            resetForm('roleRowData');
                            dialogUpdateRoleVisible = false;
                        "
          >取 消
          </el-button
          >
          <el-button
              @click="updateRoleForm('roleRowData')"
              type="primary"
          >确 定
          </el-button
          >
        </div>
      </el-dialog>
      <!-- tree -->
      <el-dialog
          append-to-body
          title="角色权限"
          :visible.sync="dialogRoleTreeVisible"
          :before-close="handleCloseRoleTree"
          width="500px"
          style="margin-top: -5vh"
      >
        <el-input
            placeholder="输入关键字进行过滤"
            v-model="filterText"
            clearable
        >
        </el-input>
        <el-tree
            style="height: 400px; overflow: auto"
            class="filter-tree"
            :data="roleTree"
            :props="defaultProps"
            accordion
            show-checkbox
            node-key="id"
            :default-expanded-keys="[0]"
            :default-checked-keys="defaultCheckRoleTree"
            :filter-node-method="filterNode"
            ref="tree"
        >
        </el-tree>
        <div slot="footer" class="dialog-footer">
          <el-button
              @click="
                            filterText = '';
                            dialogRoleTreeVisible = false;
                        "
          >取 消
          </el-button
          >
          <el-button
              v-if="isUpdateRole"
              type="primary"
              @click="getCheckedNodes"
          >保 存
          </el-button
          >
        </div>
      </el-dialog>
    </el-dialog>
    <div class="tableBox">
      <el-table
          v-loading="loading.masterTable"
          highlight-current-row
          :row-class-name="tableRowClassName"
          :data="accounts"
          style="width: 100%"
      >
        <el-table-column fixed="left" type="index" width="50">
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="userName"
            label="用户姓名"
            width="180"
        >
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="gender"
            label="性别"
            width="80"
        >
          <template slot-scope="scope">
            <i
                v-if="scope.row.gender == 1"
                class="iconfont icon-nan"
                style="color: #4597e0"
            ></i>
            <i
                v-if="scope.row.gender == 2"
                class="iconfont icon-nv"
                style="color: #ff7d97"
            ></i>
          </template>
        </el-table-column>
        <el-table-column label="数据权限" width="150">
          <template slot-scope="scope">
            <el-button
                @click="
                                dialogLookVisible = true;
                                bindUserId = scope.row.userId;
                                getLookData();
                            "
                icon="iconfont icon-chakan1"
                type="text"
                size="small"
            ></el-button>
            <el-button
                @click="
                                dialogWareVisible = true;
                                bindUserId = scope.row.userId;
                                getBindData();
                            "
                icon="iconfont icon-quanxian"
                type="text"
                size="small"
                style="color: #e6a23c"
            >绑定权限
            </el-button
            >
          </template>
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="account"
            label="账号"
            width="180"
        >
        </el-table-column>
        <!-- <el-table-column
            sortable
            show-overflow-tooltip
            prop="password"
            label="密码"
            width="180"
        >
        </el-table-column> -->
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="deptName"
            label="部门"
            width="180"
        >
        </el-table-column>
        <!--<el-table-column
        sortable
        show-overflow-tooltip
        prop="accountStatus"
        label="状态"
        width="80">
  <template slot-scope="scope">
    <el-tag v-if="scope.row.accountStatus === 'enable'" type="success">已启用</el-tag>
    <el-tag v-if="scope.row.accountStatus === 'disable'" type="info">已禁用</el-tag>
  </template>
</el-table-column>-->
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="jobNumber"
            label="工号"
            width="180"
        >
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="mobile"
            label="联系电话"
            width="180"
        >
        </el-table-column>
        <el-table-column
            sortable
            show-overflow-tooltip
            prop="email"
            label="邮箱"
            width="180"
        >
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="150">
          <template slot-scope="scope">
            <el-button
                v-if="
                                scope.row.createUserId != -1 &&
                                scope.row.accountStatus === 'disable'
                            "
                @click="enableAccount(scope.row.userId)"
                icon="iconfont icon-qiyong"
                type="success"
                circle
                plain
                size="small"
                title="点击启用"
            ></el-button>
            <el-button
                v-if="
                                scope.row.createUserId != -1 &&
                                scope.row.accountStatus === 'enable'
                            "
                @click="disableAccount(scope.row.userId)"
                icon="iconfont icon-user-jinyong"
                type="info"
                circle
                plain
                size="small"
                title="点击禁用"
            ></el-button>
            <el-button
                @click="showUpdateModal(scope.row)"
                icon="iconfont icon-bianji"
                type="primary"
                circle
                plain
                size="small"
            ></el-button>
            <el-button
                v-if="scope.row.createUserId != -1"
                @click="isDelete(scope.row.userId)"
                icon="iconfont icon-shanchu"
                type="danger"
                circle
                plain
                size="small"
            ></el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
          style="text-align: right"
          class="pagination-bottom"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
          :page-sizes="[15, 30, 50, 100]"
          :current-page.sync="searchData.pageNum"
          :page-size="searchData.pageSize"
          layout="sizes, total, prev, pager, next, jumper"
          :total="searchData.total"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  name: "Isv",
  data() {
    let validateAccount = (rule, value, callback) => {
      PF.validator(
          rule,
          value,
          callback,
          "user_info",
          "account",
          this.form.account,
          0
      );
    };
    let validateAccount2 = (rule, value, callback) => {
      PF.validator(
          rule,
          value,
          callback,
          "user_info",
          "account",
          this.rowData.account,
          1,
          this.rowData.userId
      );
    };
    let validateJobNumber = (rule, value, callback) => {
      PF.validator(
          rule,
          value,
          callback,
          "user_info",
          "job_number",
          this.form.jobNumber,
          0
      );
    };
    let validateJobNumber2 = (rule, value, callback) => {
      PF.validator(
          rule,
          value,
          callback,
          "user_info",
          "job_number",
          this.rowData.jobNumber,
          1,
          this.rowData.userId
      );
    };
    let validateRoleName = (rule, value, callback) => {
      PF.validator(
          rule,
          value,
          callback,
          "role",
          "role_name",
          this.roleForm.roleName,
          0
      );
    };
    let validateRoleName2 = (rule, value, callback) => {
      PF.validator(
          rule,
          value,
          callback,
          "role",
          "role_name",
          this.roleRowData.roleName,
          1,
          this.roleRowData.roleId
      );
    };
    let validateRoleDescribe = (rule, value, callback) => {
      PF.validator(
          rule,
          value,
          callback,
          "role",
          "role_describe",
          this.roleForm.roleDescribe,
          0
      );
    };
    let validateRoleDescribe2 = (rule, value, callback) => {
      PF.validator(
          rule,
          value,
          callback,
          "role",
          "role_describe",
          this.roleRowData.roleDescribe,
          1,
          this.roleRowData.roleId
      );
    };
    return {
      token: localStorage.getItem("token"),
      isMoreSearch: false,
      loading: {
        masterTable: true,
      },
      searchData: {
        pageNum: 1,
        pageSize: 15,
        total: null,
        keyWords: "",
        accountStatus: "",
        deptId: "",
        userType: 2,
      },
      searchStates: [
        {
          value: "enable",
          label: "启用",
        },
        {
          value: "disable",
          label: "禁用",
        },
      ],
      searchRoleData: {
        keyWords: "",
      },
      accounts: [],
      departments: [],
      departmentsTree: [],
      wares: [],
      roles: [],
      menusOptions: [], // 配置菜单
      organizations: [],
      bindInfos: [],
      dialogNewVisible: false,
      dialogWareVisible: false,
      dialogLookVisible: false,
      dialogUpdateVisible: false,
      dialogRoleVisible: false,
      dialogRoleTreeVisible: false,
      dialogNewRoleVisible: false,
      dialogLookRoleTreeVisible: false,
      dialogBindLookRoleTreeVisible: false,
      dialogUpdateRoleVisible: false,
      isUpdateRole: false,
      form: {
        account: "",
        password: "",
        userName: "",
        gender: null,
        jobNumber: "",
        email: "",
        mobile: null,
        deptId: null,
        token: null,
        url: "",
      },
      rules: {
        account: [
          {required: true, message: "请输入账号", trigger: "blur"},
          {validator: validateAccount, trigger: "blur"},
        ],
        password: [
          {required: true, message: "请输入密码", trigger: "blur"},
        ],
        userName: [
          {required: true, message: "请输入姓名", trigger: "blur"},
        ],
        gender: [
          {required: true, message: "请输入性别", trigger: "blur"},
        ],
        mobile: [
          {
            required: true,
            message: "联系电话不能为空",
            trigger: "blur",
          },
          {
            type: "number",
            message: "联系电话必须为数字值",
            trigger: ["blur", "change"],
          },
        ],
        jobNumber: [
          {required: true, message: "请输入工号", trigger: "blur"},
          {validator: validateJobNumber, trigger: "blur"},
        ],
        email: [
          // { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: ["blur", "change"],
          },
        ],
        deptId: [
          {required: true, message: "请选择部门", trigger: "blur"},
        ],
        url: [
          {required: true, message: "请选择菜单", trigger: "blur"},
        ],
      },
      formLabelWidth: "120px",
      // checkList: []
      bindUserId: null,
      wareForm: {
        checkWare: [],
        checkOrganization: [],
        checkRole: [],
      },
      lookForm: {
        checkWare: [],
        checkOrganization: [],
        checkRole: [],
      },
      rowData: {
        account: "",
        password: "",
        userName: "",
        gender: null,
        jobNumber: "",
        email: "",
        mobile: null,
        deptId: null,
        token: null,
        url: "",
      },
      rules2: {
        account: [
          {required: true, message: "请输入账号", trigger: "blur"},
          {validator: validateAccount2, trigger: "blur"},
        ],
        password: [
          {required: true, message: "请输入密码", trigger: "blur"},
        ],
        userName: [
          {required: true, message: "请输入姓名", trigger: "blur"},
        ],
        gender: [
          {required: true, message: "请输入性别", trigger: "blur"},
        ],
        mobile: [
          {
            required: true,
            message: "联系电话不能为空",
            trigger: "blur",
          },
          {
            type: "number",
            message: "联系电话必须为数字值",
            trigger: ["blur", "change"],
          },
        ],
        jobNumber: [
          {required: true, message: "请输入工号", trigger: "blur"},
          {validator: validateJobNumber2, trigger: "blur"},
        ],
        email: [
          // { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: ["blur", "change"],
          },
        ],
        deptId: [
          {required: true, message: "请选择部门", trigger: "blur"},
        ],
        url: [
          {required: true, message: "请选择菜单", trigger: "blur"},
        ],
      },
      filterText: "",
      roleTree: [],
      defaultProps: {
        children: "children",
        label: "name",
      },
      defaultCheckRoleTree: [],
      treeList: [],
      commitTreeList: [],
      roleRowInfo: {
        roleId: null,
        roleName: "",
        roleDescribe: "",
        permissions: [],
      },
      roleForm: {
        parentRoleId: "",
        roleName: "",
        roleDescribe: "",
        permissions: [],
      },
      roleRules: {
        roleName: [
          {
            required: true,
            message: "请输入角色名称",
            trigger: "blur",
          },
          {validator: validateRoleName, trigger: "blur"},
        ],
        roleDescribe: [
          {
            required: true,
            message: "请输入角色描述",
            trigger: "blur",
          },
          {validator: validateRoleDescribe, trigger: "blur"},
        ],
      },
      roleRowData: {
        roleId: "",
        roleName: "",
        roleDescribe: "",
      },
      roleRules2: {
        roleName: [
          {
            required: true,
            message: "请输入角色名称",
            trigger: "blur",
          },
          {validator: validateRoleName2, trigger: "blur"},
        ],
        roleDescribe: [
          {
            required: true,
            message: "请输入角色描述",
            trigger: "blur",
          },
          {validator: validateRoleDescribe2, trigger: "blur"},
        ],
      },
      menusRowData: [],
    };
  },
  computed: {
    formName() {
      return this.form.account;
    },
    roleFormName() {
      return this.roleForm.account;
    },
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    },
    formName(val) {
      this.form.account = val.replace(/[^a-zA-Z0-9]/g, "");
    },
    roleFormName(val) {
      this.roleForm.account = val.replace(/[^a-zA-Z0-9]/g, "");
    },
  },
  methods: {
    handleSizeChange(val) {
      this.searchData.pageSize = val;
      this.getAccountData();
    },
    handleCurrentChange(val) {
      this.searchData.pageNum = val;
      this.getAccountData();
    },
    clearSearchData() {
      this.searchData.keyWords = "";
      this.searchData.accountStatus = "";
      this.searchData.deptId = "";
    },
    clearSearchRoleData() {
      this.searchRoleData.keyWords = "";
    },
    getAccountData() {
      let that = this;
      that.loading.masterTable = true;
      let searchData = JSON.parse(JSON.stringify(this.searchData));
      searchData.deptId = searchData.deptId[searchData.deptId.length - 1];
      IOT.getServerData("/users/userList", "get", searchData, (ret) => {
        that.loading.masterTable = false;
        if (ret.code === 200) {
          this.accounts = ret.rows;
          this.searchData.total = ret.total;
          this.searchData.pageNum = ret.pageNumber;
        } else {
          IOT.tips(
              ret.message || "服务器请求失败，稍后再试！",
              "error"
          );
        }
      });
    },
    getWareData() {
      IOT.getServerData("/ware/infos/findSome", "get", {}, (ret) => {
        if (ret.code === 200) {
          this.wares = ret.rows;
        } else {
          IOT.tips(
              ret.message || "服务器请求失败，稍后再试！",
              "error"
          );
        }
      });
    },
    getMenuData() {
      IOT.getServerData("/resources/listPage", "get", {}, (ret) => {
        if (ret.code === 200) {
          this.menusRowData = ret && ret.rows;
          this.menusOptions = this.parseMenuData(this.menusRowData);
        } else {
          IOT.tips(
              ret.message || "服务器请求失败，稍后再试！",
              "error"
          );
        }
      });
    },
    parseMenuData(data) {
      data.forEach((item) => {
        item.label = item.resourceName;
        item.urlName = item.resourceName;
        item.value = item.url;
        item.id = item.resourceId;
      });
      let treeData = PF.toTree(data);
      return treeData;
    },
    getRoleData() {
      IOT.getServerData("/roles", "get", this.searchRoleData, (ret) => {
        if (ret.code === 200) {
          this.roles = ret.rows;
        } else {
          IOT.tips(
              ret.message || "服务器请求失败，稍后再试！",
              "error"
          );
        }
      });
    },
    getBindData() {
      this.bindInfos = [];
      IOT.getServerData(
          "/users/" + this.bindUserId + "/info",
          "get",
          {userId: this.bindUserId},
          (ret) => {
            if (ret.code === 200) {
              this.bindInfos = ret.data;
              this.wareForm.checkWare = ret.data.wareIds;
              this.wareForm.checkOrganization =
                  ret.data.organizationIds;
              this.wareForm.checkRole = ret.data.roleIds;
            } else {
              IOT.tips(
                  ret.message || "服务器请求失败，稍后再试！",
                  "error"
              );
            }
          },
          true
      );
    },
    getLookData() {
      this.bindInfos = [];
      this.lookForm = {
        checkWare: [],
        checkOrganization: [],
        checkRole: [],
      };
      IOT.getServerData(
          "/users/" + this.bindUserId + "/info",
          "get",
          {userId: this.bindUserId},
          (ret) => {
            if (ret.code === 200) {
              this.bindInfos = ret.data;

              for (
                  let i = 0;
                  i < this.bindInfos.wareIds.length;
                  i++
              ) {
                this.lookForm.checkWare.push({
                  wareId: this.bindInfos.wareIds[i],
                  wareName: this.bindInfos.wareNames[i],
                });
              }
              for (
                  let i = 0;
                  i < this.bindInfos.organizationIds.length;
                  i++
              ) {
                this.lookForm.checkOrganization.push({
                  organizationId: this.bindInfos.organizationIds[
                      i
                      ],
                  organizationName: this.bindInfos
                      .organizationNames[i],
                });
              }
              for (
                  let i = 0;
                  i < this.bindInfos.roleIds.length;
                  i++
              ) {
                this.lookForm.checkRole.push({
                  roleId: this.bindInfos.roleIds[i],
                  roleName: this.bindInfos.roleNames[i],
                });
              }
            } else {
              IOT.tips(
                  ret.message || "服务器请求失败，稍后再试！",
                  "error"
              );
            }
          },
          true
      );
    },
    getDepartmentData() {
      IOT.getServerData("/depts/list", "get", {}, (ret) => {
        if (ret.code === 200) {
          let list = ret.rows;
          this.departments = list;

          for (let i = 0; i < list.length; i++) {
            list[i].label = list[i].deptName;
            list[i].value = list[i].deptId;
            list[i].id = list[i].deptId;
          }
          let listTree = PF.toTree(list);
          this.departmentsTree = listTree;
        } else {
          IOT.tips(
              ret.message || "服务器请求失败，稍后再试！",
              "error"
          );
        }
      });
    },
    /*showLoading(){
        const loading = this.$loading({
            lock: true,
            text: '正在保存请稍等...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
        });
        return loading
    },
    hideLoading(loading){
        loading.close();
    },*/
    getMenuUrlName(url) {
      let urlName = "";
      for (let i = 0; i < this.menusRowData.length; i++) {
        const e = this.menusRowData[i];
        if (e.url == url) {
          urlName = e.urlName;
          break;
        }
      }
      return urlName;
    },
    commitForm(formName) {
      let that = this;
      IOT.showOverlay("保存中，请稍等...");
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let form = JSON.parse(JSON.stringify(this.form));
          form.deptId = form.deptId[form.deptId.length - 1];
          form.userType = 2
          form.url = form.url[0]
          form.urlName = this.getMenuUrlName(this.form.url);

          IOT.getServerData("/users", "post", form, (ret) => {
            IOT.hideOverlay();
            if (ret.code === 200) {
              IOT.tips("保存成功！", "success", 1000, () => {
                that.getAccountData();
              });
            } else if (ret.code === 100022) {
              IOT.tips(
                  ret.message || "该账号已存在,不可创建！",
                  "error"
              );
            } else {
              IOT.tips(
                  ret.message || "服务器请求失败，稍后再试！",
                  "error"
              );
            }
          });
          this.resetForm("form");
          this.dialogNewVisible = false;
        } else {
          IOT.hideOverlay();
          console.error("error submit!!");
          return false;
        }
      });
    },
    updateForm(formName) {
      let that = this;
      IOT.showOverlay("保存中，请稍等...");
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let rowData = JSON.parse(JSON.stringify(this.rowData));
          rowData.deptId = rowData.deptId[rowData.deptId.length - 1];
          rowData.userType = 2
          rowData.url = rowData.url[0]
          // 根据选择的菜单value，将对应的label也传给后端
          rowData.urlName = this.getMenuUrlName(this.rowData.url);

          IOT.getServerData(
              "/users/modifyUserInfo",
              "post",
              rowData,
              (ret) => {
                IOT.hideOverlay();

                if (ret.code === 200) {
                  IOT.tips("保存成功！", "success", 1000, () => {
                    that.getAccountData();
                  });
                } else {
                  IOT.tips(
                      ret.message || "服务器请求失败，稍后再试！",
                      "error"
                  );
                }
              }
          );
          this.resetForm("rowData");
          this.dialogUpdateVisible = false;
        } else {
          IOT.hideOverlay();
          console.error("error submit!!");
          return false;
        }
      });
    },
    updateRoleForm(formName) {
      let that = this;
      IOT.showOverlay("保存中，请稍等...");
      this.$refs[formName].validate((valid) => {
        if (valid) {
          IOT.getServerData(
              "/roles/" + this.roleRowData.roleId,
              "put",
              this.roleRowData,
              (ret) => {
                IOT.hideOverlay();
                if (ret && ret.code === 200) {
                  IOT.tips("保存成功！", "success", 1000, () => {
                    that.getRoleData();
                  });
                } else {
                  IOT.tips(
                      ret.msg || "操作失败，请重试！",
                      "error",
                      1000,
                      null
                  );
                }
              }
          );
          this.resetForm("roleRowData");
          this.dialogUpdateRoleVisible = false;
        } else {
          IOT.hideOverlay();
          console.error("error submit!!");
          return false;
        }
      });
    },
    showUpdateModal(row) {
      row.mobile = Number(row.mobile);
      row.gender = String(row.gender);
      this.rowData = JSON.parse(JSON.stringify(row));
      this.dialogUpdateVisible = true;
    },
    handleClose2(done) {
      // this.$confirm('确认关闭？')
      //     .then(_ => {
      this.resetForm("rowData");
      this.dialogUpdateVisible = false;
      //     done();
      // })
      // .catch(_ => {});
    },
    isDelete(id) {
      this.$confirm("确认删除？")
          .then((_) => {
            this.deleteAccount(id);
          })
          .catch((_) => {
          });
    },
    deleteAccount(id) {
      let that = this;
      IOT.showOverlay("删除中，请稍等...");
      IOT.getServerData("/users/delete", "get", {userId: id}, (ret) => {
        IOT.hideOverlay();
        if (ret.code === 200) {
          IOT.tips("删除成功！", "success", 1000, () => {
            that.getAccountData();
          });
        } else {
          IOT.tips(
              ret.message || "服务器请求失败，稍后再试！",
              "error"
          );
        }
      });
    },
    commitWareForm(formName) {
      IOT.showOverlay("保存中，请稍等...");
      this.$refs[formName].validate((valid) => {
        if (valid) {
          IOT.getServerData(
              "/users/bindWare",
              "post",
              {
                userId: this.bindUserId,
                wareIds: this.wareForm.checkWare,
                organizationIds: this.wareForm.checkOrganization,
                roleIds: this.wareForm.checkRole,
              },
              (ret) => {
                IOT.hideOverlay();
                if (ret.code === 200) {
                  IOT.tips("保存成功！", "success");
                } else {
                  IOT.tips(
                      ret.message || "服务器请求失败，稍后再试！",
                      "error"
                  );
                }
              }
          );
          this.resetForm("wareForm");
          this.dialogWareVisible = false;
        } else {
          IOT.hideOverlay();
          console.error("error submit!!");
          return false;
        }
      });
    },
    resetForm(formName) {
      // 重置
      this.$refs[formName].resetFields();
    },
    /*closeForm(){
        this.resetForm('form')
        this.dialogNewVisible = false
    },*/
    handleClose(done) {
      // this.$confirm('确认关闭？')
      //     .then(_ => {
      this.resetForm("form");
      this.dialogNewVisible = false;
      //     done();
      // })
      // .catch(_ => {});
    },
    handleCloseWare(done) {
      // this.$confirm('确认关闭？')
      //     .then(_ => {
      this.resetForm("wareForm");
      this.dialogWareVisible = false;
      //     done();
      // })
      // .catch(_ => {});
    },
    handleCloseLook() {
      this.dialogLookVisible = false;
    },
    enableAccount(id) {
      IOT.getServerData("/users/enable", "get", {userId: id}, (ret) => {
        if (ret.code === 200) {
          this.getAccountData();
          IOT.tips("已启用！", "success");
        } else {
          IOT.tips(
              ret.message || "服务器请求失败，稍后再试！",
              "error"
          );
        }
      });
    },
    disableAccount(id) {
      IOT.getServerData(
          "/users/disable",
          "get",
          {userId: id},
          (ret) => {
            if (ret.code === 200) {
              this.getAccountData();
              IOT.tips("已禁用！", "success");
            } else {
              IOT.tips(
                  ret.message || "服务器请求失败，稍后再试！",
                  "error"
              );
            }
          }
      );
    },
    getRoleDetail(id) {
      IOT.getServerData(
          "/roles/" + id,
          "get",
          {roleId: id},
          (ret) => {
            if (ret.code === 200) {
              let treeList = ret.data.treeList;
              treeList.push({
                id: 0,
                parentId: -1,
                name: "全选",
              });
              this.treeList = treeList;

              this.defaultCheckRoleTree = [];
              for (let i = 0; i < treeList.length; i++) {
                if (treeList[i].checked) {
                  this.defaultCheckRoleTree.push(treeList[i].id);
                }
              }
              this.roleTree = PF.toTree(treeList, !this.isUpdateRole);
            } else {
              IOT.tips(ret.msg, "error");
            }
          },
          true
      );
    },
    showRoleDetail(row) {
      this.roleRowInfo.roleId = row.roleId;
      this.roleRowInfo.roleName = row.roleName;
      this.roleRowInfo.roleDescribe = row.roleDescribe;
      this.roleRowInfo.permissions = [];
      this.getRoleDetail(row.roleId);
      this.dialogRoleTreeVisible = true;
    },
    showUpdateRoleModal(row) {
      this.roleRowData.roleId = row.roleId;
      this.roleRowData.roleName = row.roleName;
      this.roleRowData.roleDescribe = row.roleDescribe;
      this.dialogUpdateRoleVisible = true;
    },
    filterNode(value, data) {
      // 查询树形data
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    getCheckedNodes() {
      // 获取选中
      let CheckedNodes = this.$refs.tree.getCheckedNodes(false, false);
      let params = this.roleRowInfo;
      params.permissions = []

      for (let i in CheckedNodes) {
        params.permissions.push({
          objectId: params.roleId,
          objectType: "role",
          resourceId: CheckedNodes[i].id,
        });
      }
      IOT.showOverlay("保存中，请稍等...");
      IOT.getServerData(
          "/roles/" + params.roleId,
          "put",
          params,
          (ret) => {
            IOT.hideOverlay();
            if (ret && ret.code === 200) {
              IOT.tips("保存成功！", "success");
              this.dialogRoleTreeVisible = false;
            } else {
              IOT.tips(
                  ret.msg || "操作失败，请重试！",
                  "error",
                  1000,
                  null
              );
            }
          }
      );
    },
    isDeleteRole(id) {
      this.$confirm("确认删除？")
          .then((_) => {
            this.deleteRole(id);
          })
          .catch((_) => {
          });
    },
    deleteRole(id) {
      let that = this;
      IOT.showOverlay("删除中，请稍等...");
      IOT.getServerData("/roles/delete", "get", {roleId: id}, (ret) => {
        IOT.hideOverlay();
        if (ret.code === 200) {
          IOT.tips("删除成功！", "success", 1000, () => {
            that.getRoleData();
          });
        } else if (ret.code === 100021) {
          IOT.tips(ret.message || "该角色已绑定,不可删除！", "error");
        } else {
          IOT.tips(
              ret.message || "服务器请求失败，稍后再试！",
              "error"
          );
        }
      });
    },
    handleCloseRole(done) {
      // this.$confirm('确认关闭？')
      //     .then(_ => {
      this.resetForm("roleForm");
      this.dialogNewRoleVisible = false;
      //     done();
      // })
      // .catch(_ => {});
    },
    handleCloseRole2(done) {
      // this.$confirm('确认关闭？')
      //     .then(_ => {
      this.resetForm("roleRowData");
      this.dialogUpdateRoleVisible = false;
      //     done();
      // })
      // .catch(_ => {});
    },
    handleCloseRoleTree(done) {
      // this.$confirm('确认关闭？')
      //     .then(_ => {
      this.filterText = "";
      this.dialogRoleTreeVisible = false;
      //     done();
      // })
      // .catch(_ => {});
    },
    commitRoleForm(formName) {
      let that = this;
      IOT.showOverlay("保存中，请稍等...");
      this.$refs[formName].validate((valid) => {
        if (valid) {
          IOT.getServerData(
              "/roles",
              "post",
              this.roleForm,
              (ret) => {
                IOT.hideOverlay();
                if (ret.code === 200) {
                  IOT.tips("保存成功！", "success", 1000, () => {
                    that.getRoleData();
                  });
                } else {
                  IOT.tips(
                      ret.message || "服务器请求失败，稍后再试！",
                      "error"
                  );
                }
              }
          );
          this.resetForm("roleForm");
          this.dialogNewRoleVisible = false;
        } else {
          IOT.hideOverlay();
          console.error("error submit!!");
          return false;
        }
      });
    },
    tableRowClassName({row, rowIndex}) {
      if (row.accountStatus === "disable") {
        return "disable-row";
      } else if (row.accountStatus === "enable") {
        return "enable-row";
      }
      return "";
    },
  },
  created() {
    this.getWareData();
    this.getRoleData();
    PF.getOrganizations((rows) => {
      this.organizations = rows;
    });
    this.getAccountData();
    this.getDepartmentData();
    this.getMenuData();
  },
};
</script>
<style scoped lang="less">
</style>
