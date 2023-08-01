<template>
  <div class="Scan2"  v-if="scanning">
    <div id="bcid"> 
		</div> 
  </div>
</template>
<script type="text/javascript"> 
			// 从相册中选择二维码图片 
			function scanPicture() {
				plus.gallery.pick(function(path) {
					plus.barcode.scan(path, onmarked, function(error) {
						plus.nativeUI.alert("无法识别此图片");
					});
				}, function(err) {
					plus.nativeUI.alert("Failed: " + err.message);
				});
			}
		</script>
<script>


    import '../assets/js/mui.js'
    
    export default {
        name: "Scan",
        data() {
            return {
              result:'',
              scan:null,
            }
        },
        props: {
            scanning: Boolean
        },
        methods: {
        
            
             
        },
        created() {
         
         // this.startRecognize();
             
        },

        mounted(){
           window.startRecognize = this.startRecognize;
           window.aaa = this.aaa;
             mui.plusReady(function() {
                mui.init();  
                 startRecognize();
              });
        }
    };
</script>
<style lang="less" scoped>
//@import '../assets/css/mui.less';
  .Scan2 {
    #bcid {
				margin-top:20%;
				width: 100%;
				height: 500px;
				position: absolute;
				background: #000000;
			} 
			.fbt {
				color: #0E76E1;
				width: 50%;
				background-color: #ffffff;
				float: left;
				line-height: 44px;
				text-align: center;
			}
			#bcid2 {
				top:300px;
				width: 100%;
				 
				position: absolute;
			 

				padding: 0px;position: absolute;
			} 


			
  }
</style>
