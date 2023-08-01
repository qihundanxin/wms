<template>
    <div class="inventorySafetyReport">
        <el-row class="tac">
            <el-col :span="4">
                <el-button @click="sheji()" style="margin-bottom: 15px;" type="primary">设计报表</el-button>
               
                <el-menu  >
                    <el-menu-item  @click="selectReport(reportName.fileName)" :index="reportName.fileName" v-for="(reportName,index) in reportNames" :key="index">
                        <i class="el-icon-menu">&nbsp;&nbsp;&nbsp;&nbsp;{{getReportName(reportName.fileName)}}</i>
                         
                        <el-button style="margin-left:60%" @click="aaaa(reportName.fileName)" type="primary" size='mini' >设计</el-button>
                        <!-- <el-button style="margin-left:5px" @click="deleteReport(reportName)" type="primary" size='mini' >删除</el-button> -->
              
            
                    </el-menu-item>
                
                
               
                </el-menu>
            </el-col>
            <el-col :span="20">
                <!--<div style="margin:2px 2px 2px 20px;background:white;border-right: solid 1px #e6e6e6;"> <div v-html="reportHtml" > </div></div> -->
               <iframe v-if="reportName!=''" style=" margin:2px 2px 2px 20px; overflow-x: auto;"   v-bind:src="loadUrl+reportName" width="100%"   height="800"  >   
                </iframe>
             
            </el-col>
        </el-row>
    </div>
</template>

<script>
    export default {
        name: "UReport",
        data() {
            return {
                loadUrl :localStorage.getItem('baseUrl')+'/ureport/preview?_i=2&_u=mysql:',
                baseIp: localStorage.getItem('baseUrl'),
                searchData: {
                    pageNum: 1,
                    pageSize: 10,
                    total: null,
                    keyWords: '',
                    inventorySafetyState: 0
                },
                loading: false,
                tableData: [],
                states: [
                    {
                        value: 1,
                        label: '超储'
                    },
                    {
                        value: -1,
                        label: '短缺'
                    },
                    {
                        value: 0,
                        label: '全部'
                    }
                ],
                reportHtml: '',
                reportNames:[],
                reportName:'',
                pageNum:'',
            }
        },
        methods: {
            aaaa(reportFileName){
                window.open(this.baseIp+"/ureport/designer?_u=mysql:"+reportFileName);     
            },
            PageCurrentChange(){
                 
                 $("iframe").load(location.href + " iframe");  

            },

            sheji(){
                   
                window.open(this.baseIp+"/ureport/designer");    
            },

            selectReport(index){
                this.getReportData(index);
            },

            getReportName(fileName){ 
                return fileName.split(".")[0];

            },
            deleteReport(report){
                let that = this 
                 
                IOT.getServerData('/ureports/delete', 'get', {
                    id: report.id ,
                }, (ret) => {
                    
                    if (ret.code === 200) {
                       
                         that.getReportData();
                         
                    } else {
                        IOT.tips(ret.message || 'server error')
                    }
                },true)
            },
           

            getReportData(reportFileName) {
                
                let that = this
                this.loading = true
                this.reportName=reportFileName
                 
                IOT.getServerData('/ureports/loadReport', 'post', {
                    fileName: reportFileName ,
                }, (ret) => {
                    
                    if (ret.code === 200) {
                        that.reportHtml = ret.data
                         
                         
                    } else {
                        IOT.tips(ret.message || 'server error')
                    }
                },true)
            },


            getFileName() {
                 let that = this
                IOT.getServerData('/ureports/findAll', 'get', {
                    
                }, (ret) => {
                   
                    if (ret.code === 200) {
                        that.reportNames = ret.data;
                        that.getReportData(ret.data[0].fileName);
                         
                    } else {
                        IOT.tips(ret.message || 'server error')
                    }
                },true)
            },
            clearSearchData() {
                this.searchData = {
                    pageNum: 1,
                    pageSize: 10,
                    total: null,
                    keyWords: ''
                }
            },
            handlePageChange(val) {
                this.searchData.pageNum = val
                this.getTableData()
            }
        },
        created() {
          
           
            this.getFileName();
        },
        mounted() {
        }
    }
</script>