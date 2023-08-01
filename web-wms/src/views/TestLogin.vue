<template>
    <div class="login">
        <div class="loginBox clearFloat">
            <div class="imgBox leftFloat">
                <img src="../assets/images/loginBanner.png" alt="" />
            </div>
            <div class="loginModal rightFloat">
                <ul>
                    <li>
                        <span>账号登录</span>
                    </li>
                    <li>
                        <el-input
                            @keyup.enter.native="commit"
                            size="small"
                            placeholder="请输入账号"
                            v-model="form.account"
                            prefix-icon="iconfont icon-ef-zhanghao"
                            clearable
                        >
                        </el-input>
                    </li>
                    <li>
                        <el-input
                            @keyup.enter.native="commit"
                            size="small"
                            placeholder="请输入密码"
                            v-model="form.password"
                            prefix-icon="iconfont icon-mima"
                            show-password
                            clearable
                        ></el-input>
                    </li>
                    <li>
                        <el-button
                            @click="commit"
                            size="small"
                            type="primary"
                            round
                            :disabled="
                                form.account === '' ||
                                form.password === '' 
                            "
                            style="width: 70%"
                            >登录</el-button
                        >
                    </li>
                    <!-- <li>
                        <el-button type="text">微信登录</el-button>
                        <el-button type="text">免密登录</el-button>
                    </li> -->
                </ul>
            </div>
        </div>
        <Footer></Footer>
        
    </div>
    
    
    
     
       
</template>

<script>
 
     
      
import Footer from "@/components/Footer.vue";

export default {
    name: "Login",
    data() {
        return {
            form: {
                account: "",
                password: "",
            },
            sliderValue: 0,
            rds:"",
        };
    },
    computed: {
        account() {
            return this.form.account;
        },
        password() {
            return this.form.password;
        },
    },
    watch: {
        sliderValue(val) {
            if (val !== 100) {
                this.sliderValue = 0;
            }
        },
        account(val) {
            this.sliderValue = 0;
        },
        password(val) {
            this.sliderValue = 0;
        },
    },
    components: {
        Footer,
    },
    methods: {
        getMenuIndex(url, m1, m2, m3) {
            let index = 0;
            for (let i = 0; i < m1.length; i++) {
                const e = m1[i];
                if (e.url === url) {
                    index = 0;
                    break;
                }
            }
            for (let i = 0; i < m2.length; i++) {
                const e = m2[i];
                if (e.url === url) {
                    index = 1;
                    break;
                }
            }
            for (let i = 0; i < m3.length; i++) {
                const e = m3[i];
                if (e.url === url) {
                    index = 2;
                    break;
                }
            }
            return index;
        },
        commit() {
            if (!this.form.account) {
                PF.publicMessage(
                    '请输入<i class="iconfont icon-zhanghao"></i>账号!',
                    "error"
                );
                return false;
            } else if (!this.form.password) {
                PF.publicMessage(
                    '请输入<i class="iconfont icon-mima"></i>密码!',
                    "error"
                );
                return false;
            }
            PF.publicLoadingShow("登录中,请稍后...");

            IOT.getServerData(
                "/login",
                "post",
                this.form,
                (ret) => {
                    PF.publicLoadingHide();
                    if (ret.code === 200) {
                        PF.publicMessage("登录成功!", "success", 1000);
                        
                        localStorage.setItem("token", ret.data.token);
                        localStorage.setItem("userName", ret.data.userName);
                        localStorage.setItem(
                            "menus1",
                            JSON.stringify(ret.data.menus1)
                        );
                        localStorage.setItem(
                            "menus2",
                            JSON.stringify(ret.data.menus2)
                        );
                        localStorage.setItem(
                            "menus3",
                            JSON.stringify(ret.data.menus3)
                        );

                        // 根据返回的角色路径匹配到菜单所属的4个选项
                        let login_direct_url = ret && ret.data && ret.data.url;
                        let menu_index = this.getMenuIndex(
                            login_direct_url,
                            ret.data.menus1,
                            ret.data.menus2,
                            ret.data.menus3
                        );
                        localStorage.setItem("menu_index", menu_index);
                        this.$router.push({ path: login_direct_url });
                    } else {
                        PF.publicMessage(
                            ret.message || "服务器请求错误!",
                            "error"
                        );
                    }
                },
                true
            );

            /*this.$http.post('/login', this.form).then(res=>{
                    PF.publicLoadingHide();
                    if(res.data.code === 200){
                        PF.publicMessage('登录成功!','success');
                        localStorage.setItem("token",res.data.data.token);
                        localStorage.setItem("userName",res.data.data.userName);
                        localStorage.setItem("menus1",JSON.stringify(res.data.data.menus1));
                        localStorage.setItem("menus2",JSON.stringify(res.data.data.menus2));
                        localStorage.setItem("menus3",JSON.stringify(res.data.data.menus3));
                        this.$router.push({name: 'home'})
                    }else{
                        PF.publicMessage(res.data.message || '服务器请求错误!','error');
                    }
                }).catch(err=>{
                    console.error(err || '未知的错误!','error');
                });*/
        },
    },
    created() {},
    mounted() {   
    },
};
</script>

<style scoped lang="less">
.login {
    color: #08274c;
    padding-bottom: 30px;
    .loginBox {
        width: 1000px;
        height: 500px;
        margin: 66px auto;
        .imgBox {
            width: 500px;
            height: 400px;
            > img {
                width: 500px;
            }
        }
        .loginModal {
            width: 450px;
            height: 400px;
            border: 1px solid #ddd;
            background: #fff;
            ul {
                width: 280px;
                margin: 50px auto;
                li {
                    text-align: center;
                    margin: 20px 0;
                }
                li:first-child {
                    span {
                        display: inline-block;
                        width: 50%;
                        line-height: 30px;
                        border-bottom: 1px solid #409eff;
                    }
                }
            }
        }
    }
}
</style>
