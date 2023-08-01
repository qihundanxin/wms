<template>
    <div class="leftMenu">
        <div class="collapseBox">
            <i
                v-show="!isCollapse"
                @click="changeCollapse"
                class="iconfont icon-shousuo active"
            ></i>
            <i
                v-show="isCollapse"
                @click="changeCollapse"
                class="iconfont icon-zhankai active"
                style="margin-right: 7px"
            ></i>
        </div>
        <el-menu
            router
            unique-opened
            :default-active="this.$route.path"
            class="el-menu-vertical-demo"
            @mouseover="showSpan"
            @select="handleSelect"
            @open="handleOpen"
            @close="handleClose"
            :text-color="menuColor"
            active-text-color="#409EFF"
            active-background-color="#409EFF"
            :collapse="isCollapse"
            ref="elMenu"
        >
            <div
                v-for="(item) in nowMenus"
                :key="item.id"
                @mouseover="showSpan"
            >
                <el-submenu v-if="item.children" :index="String(item.id)">
                    <template slot="title">
                        <i
                            v-if="item.icon === ''"
                            class="iconfont icon-qita2"
                        ></i>
                        <i v-else :class="item.icon"></i>
                        <span v-show="!isCollapse" slot="title"
                            >&nbsp;{{ item.name}}</span
                        >
                    </template>
                    <div v-for="(item2) in item.children" :key="item2.id">
                        <el-submenu
                            v-if="item2.children"
                            :index="String(item.id) + '-' + String(item2.id)"
                        >
                            <template slot="title">
                                <i
                                    v-if="item2.icon === ''"
                                    class="iconfont icon-qita2"
                                ></i>
                                <i v-else :class="item2.icon"></i>
                                <span slot="title">&nbsp;{{ item2.name }}</span>
                            </template>
                            <el-menu-item
                                :index="String(item3.url)"
                                v-for="(item3) in item2.children"
                                :key="item3.id"
                            >
                                <i
                                    v-if="item3.icon === ''"
                                    class="iconfont icon-qita2"
                                ></i>
                                <i v-else :class="item3.icon"></i>
                                <span slot="title">&nbsp;{{ item3.name }}</span>
                            </el-menu-item>
                        </el-submenu>
                        <el-menu-item v-else :index="String(item2.url)">
                            <i
                                v-if="item2.icon === ''"
                                class="iconfont icon-qita2"
                            ></i>
                            <i v-else :class="item2.icon"></i>
                            <span slot="title">&nbsp;{{ item2.name }}</span>
                        </el-menu-item>
                    </div>
                </el-submenu>
                <el-menu-item v-else :index="String(item.url)" :key="item.id">
                    <i v-if="item.icon === ''" class="iconfont icon-qita2"></i>
                    <i v-else :class="item.icon"></i>
                    <span slot="title">&nbsp;{{ item.name }}</span>
                </el-menu-item>
            </div>
        </el-menu>
    </div>
</template>

<script>
export default {
    name: "LeftMenu",
    data() {
        return {
            menus: {
                menus1: PF.toTree(JSON.parse(localStorage.getItem("menus1"))),
                menus2: PF.toTree(JSON.parse(localStorage.getItem("menus2"))),
                menus3: PF.toTree(JSON.parse(localStorage.getItem("menus3"))),
            },
            nowMenus: [],
            isCollapse: false,
            // menuColor: '#A9B0C6',
            menuColor: "#333333",
        };
    },
    props: ["leftMenusIndex"],
    watch: {
        leftMenusIndex(val) {
            this.nowMenus = [];
            if (val === 0) {
                this.nowMenus = this.menus.menus1;
            } else if (val === 1) {
                this.nowMenus = this.menus.menus2;
            } else if (val === 2) {
                this.nowMenus = this.menus.menus3;
            } else {
                this.nowMenus = this.menus.menus1;
            }
        },

        
    },
    methods: {
        showSpan() {
            $(".el-tooltip__popper>div>span").css("display", "");
            /*setTimeout(() => {
            $('.el-tooltip__popper>div>span').css('display','')
        },100)*/
        },
        handleSelect(index, indexPath) {
            // console.log(key, keyPath);
        },
        handleOpen(key, keyPath) {
            // console.log(key, keyPath);
        },
        handleClose(key, keyPath) {
            // console.log(key, keyPath);
        },
        toRouter(url) {
            this.$router.push(url);
        },
        changeCollapse(val) {
            if (typeof val === "boolean") {
                this.isCollapse = val;
            } else {
                this.isCollapse = !this.isCollapse;
            }
            this.$emit("isCollapse", this.isCollapse);
            if (this.isCollapse) {
                document.querySelector(".leftMenu").style.width = "64px";
            } else {
                document.querySelector(".leftMenu").style.width = "150px";
            }
        },
        isModule() {
            let path = this.$route.path;
            if (path.length <= 1) {
                return false;
            }
            let isHave = false;
            let moduleName = "";
            for (let i = 0; i < path.length; i++) {
                if (path[i] === "/") {
                    if (!isHave) {
                        isHave = true;
                    } else {
                        if (moduleName === "work") {
                            this.nowMenus = this.menus.menus1;
                        } else if (moduleName === "base") {
                            this.nowMenus = this.menus.menus2;
                        } else if (moduleName === "report") {
                            this.nowMenus = this.menus.menus3;
                        }

                        return false;
                    }
                } else {
                    moduleName += path[i];
                }
            }
        },
        getSysParamMasterDetailData() {
            IOT.getServerData(
                "/sys/params",
                "get",
                { paramCode: "isCollapse" },
                (ret) => {
                    if (ret.code === 200) {
                        if (ret.rows[0].value === "1") {
                            this.changeCollapse(false);
                        } else if (ret.rows[0].value === "0") {
                            this.changeCollapse(true);
                        }
                    } else {
                        IOT.tips(
                            ret.message || "服务器请求失败，稍后再试！",
                            "error"
                        );
                    }
                }
            );
        },
    },
    created() {
        this.getSysParamMasterDetailData();

        // 获取到登录后角色的登录页所在的菜单
        let val = localStorage.getItem("menu_index");
        if (val === 0) {
            this.nowMenus = this.menus.menus1;
        } else if (val === 1) {
            this.nowMenus = this.menus.menus2;
        } else if (val === 2) {
            this.nowMenus = this.menus.menus3;
        } else {
            this.nowMenus = this.menus.menus1;
        }
    },
    mounted() {
        this.isModule();
        
    },

};
</script>

<style scoped lang="less">
.leftMenu {
    color: #adb4c9;
    transition: width 0.3s;
    padding-bottom: 30px;

    .collapseBox {
        text-align: right;
        padding-right: 20px;
        margin: 10px 0px;
    }

    .active {
        cursor: pointer;
    }

    .active:hover {
        color: #409eff;
    }

    .el-menu-vertical-demo:not(.el-menu--collapse) {
        width: 150px;
    }

    .el-menu {
        border-right: 0;
        .iconfont {
            color: #333333;
        }
        .el-submenu__icon-arrow {
            top: 55%;
            right: 15px;
        }
        .el-menu-item.is-active {
            /*color: #409EFF;*/
            border-left: 8px solid #409eff;
            /*background: #d9ecff;*/
            background-image: linear-gradient(to right, #ecf5ff, #ffffff);
            /*span{
          color: #ffffff;
        }*/
        }
    }
}
</style>
