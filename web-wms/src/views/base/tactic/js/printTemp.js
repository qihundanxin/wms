//左侧组件清单自适应高度
function autoTypeHeight(){
	var toolsHeight=parseInt($(".label-type").height())-5,
		headingHeight=$("#type-group").find(".panel:visible").length*40,
		panelBodyHeight=toolsHeight-headingHeight;
	$("#type-group .panel-body").height(panelBodyHeight);
};
autoTypeHeight();
$(window).resize(function(){autoTypeHeight();});

//获取当前日期
function getCurrentDate(year,divide){
	var getFullNum = function(num){
		num = num < 10 ?  "0" + num : num;
		return num;
	};
	var d = new Date(), dateArr="";
	if(year === true){ dateArr += d.getFullYear() + divide}
	dateArr += getFullNum(d.getMonth() + 1) + divide;
	dateArr += getFullNum(d.getDate());
	return  dateArr;
};

var headHeight=0,footHeight=0,loopHeight=31,
	maxNum=1,//第一页可显示商品行数
	moreNum=1;//延伸页可显示商品行数
$(function(){
	
	//=====================全局控制====================
	$(".label-group").draggable();//标签在画布内改变位置
	$(".custom-drop .dropitem").each(function(index, element) {//初始化标签内容
		dropCreate($(this));
	});
	registerTableDragEvent();
	//显示区域居中
	var tempItem,
		labelWidth=$(".label-content").width(),
		labelHeight=$(".label-content").height(),
		labelLeft=($(".custom-content").width()/2)-(labelWidth/2),
		labelTop=($(".custom-content").height()/2)-(labelHeight/2);
	$(".label-group").css({
		"left":labelLeft,
		"top":labelTop,
	});
	//离开页面提示
	//$(window).bind('beforeunload',function(){return '离开页面前，请确保您编辑的内容已经保存完毕';});
	
	//计算商品清单第二页可显示行数
	function countNum(){
		var looplist=$(".custom-drop .skulist-table");
		headHeight=looplist.find("thead tr").height();
		footHeight=looplist.find("tfoot tr").height();
		loopHeight=looplist.find("tbody tr").height();
		maxNum=parseInt(looplist.find("tbody").height()/loopHeight);
		if(looplist.hasClass("declare"))moreNum=parseInt((labelHeight-10)/loopHeight);
		else moreNum=parseInt((labelHeight-headHeight-footHeight-10)/loopHeight);
		var loopTr=$(".custom-drop .skulist-table tbody tr:first").html(),loopTbody="";
		for(var i=1;i<=moreNum;i++){
			loopTbody+='<tr id="ITEM_LIST_NO_DETAIL">'+loopTr+'</tr>';
		};
		$(".copy-drop .skulist-table tbody").empty().append(loopTbody).find("tr:not(:first) td span").empty().removeAttr("title");
		return loopHeight, moreNum, maxNum, headHeight, footHeight;
	};
	
	//选择模板
	$("#template-name").on("change",function(){
		$(this).val()==0?$(".loadtemplate").addClass("disabled"):$(".loadtemplate").removeClass("disabled");
	});
	//清除模板
	$(".btn-reset").on("click",function(){
		$(".custom-drop").empty();
		$(".label-type .complete").removeClass("complete");
		$(".label-content.overflow").remove();
		closeLabelSet();
		maxNum = moreNum = 0;
	});
	
	//关闭属性面板
	function closeLabelSet(){
		$(".label-set").removeClass("opened");
		$(".custom-label").removeClass("haslabelset");
		$(".custom-drop").find(".active").removeClass("active");
	};
	$(document).bind("click",function(e){
		var target  = $(e.target);
		if(target.closest(".label-set").length == 0&&target.closest(".dropitem").length == 0){
			closeLabelSet();
			$(".hotkey-panel").css("opacity","0");
		}
	});
	
	//===========================预览模式==========================
	$(".btn-view").on("click",function(){
		$("body").addClass("viewtype");
		$(".view-mask, .view-panel").show();
		//$(".label-group").css({"left":"50%","top":"50%"});
	});
	$(".btn-close-view").on("click",function(){
		$("body").removeClass("viewtype");
		$(".view-mask, .view-panel").hide();
		$(".label-group").css({"left":labelLeft,"top":labelTop}).removeClass("scale150 scale200 scale300");
		$(".viewpct .text").text("100%");
	});
	//调整显示比例
	$(".viewpct .dropdown-menu a").on("click",function(){
		var newClass=$(this).attr("rel");
		switch(newClass){
			case "default":$(".label-group").removeClass("scale150 scale200 scale300");break;
			case "scale150":$(".label-group").removeClass("scale200 scale300").addClass("scale150");break;
			case "scale200":$(".label-group").removeClass("scale150 scale300").addClass("scale200");break;
			case "scale300":$(".label-group").removeClass("scale200 scale150").addClass("scale300");break;
		}
	});
	
	//===========================快捷键功能==========================
	jwerty.key("right", function (){
		var itemLeft=parseInt($(".dropitem.active").css("left")),
			itemWidth=parseInt($(".dropitem.active").outerWidth()),
			parentWidth=parseInt($(".custom-drop").width());
		if(itemLeft<(parentWidth-itemWidth))$(".dropitem.active").css("left",(parseInt($(".dropitem.active").css("left"))+1)+"px");
	});
	jwerty.key("left", function (){
		var itemLeft=parseInt($(".dropitem.active").css("left"));
		if(itemLeft>0)$(".dropitem.active").css("left",(parseInt($(".dropitem.active").css("left"))-1)+"px");
	});
	jwerty.key("up", function (){
		var itemTop=parseInt($(".dropitem.active").css("top"));
		if(itemTop>0)$(".dropitem.active").css("top",(parseInt($(".dropitem.active").css("top"))-1)+"px");
	});
	jwerty.key("down", function (){
		var itemTop=parseInt($(".dropitem.active").css("top")),
			itemHeight=parseInt($(".dropitem.active").outerHeight()),
			parentHeight=parseInt($(".custom-drop").height());
		if(itemTop<(parentHeight-itemHeight))$(".dropitem.active").css("top",(parseInt($(".dropitem.active").css("top"))+1)+"px");
	});
	jwerty.key("delete", function (){$(".btn-clear-item").click()});
	
	//=====================拖拽动作====================
	//元素移动位置及拉伸
	function dropCreate(obj){
		obj.draggable({
			containment: "parent",
			revert: "invalid",
			start:function(event,ui){
				if(!$(this).hasClass("active")){
					getTempSet($(this));
					tempItem=$(this);
					return tempItem;
				}
			},
			stop:function(event,ui){
				$(".label-set").addClass("opened");
			}
		});
		//拖拽改变尺寸
		if(obj.hasClass("character")){
			obj.resizable({containment: ".custom-drop"});
		};
		if(obj.hasClass("imageitem")){
			obj.resizable({
				containment: ".custom-drop",
				aspectRatio: true,
				stop:function(event,ui){
					$(this).css("lineHeight",$(this).css("height"));
				}
			});
		};
		if(obj.hasClass("vertical-type")){
			obj.resizable({
				handles: "s",
				minHeight:obj.attr("data-minheight"),
				stop:function(event,ui){
					$(this).find(".codemunber").width($(this).height());
				}
			});
		}else if(obj.hasClass("barcode") || obj.hasClass("fbatext")){
			if(obj.hasClass("qRcode")){
				obj.resizable({
					handles: "e",
					minWidth:25,
					maxWidth:250,
					grid: 25
				});
			}else if(obj.hasClass("barcode-resize")){
				var codemunber = obj.children(".codemunber").height();
				obj.resizable({
					minWidth:obj.attr("data-minwidth"),
					minHeight:obj.attr("data-minheight"),
					stop:function(event,ui){
						console.log(obj);
						obj.find("img").height(obj.height() - codemunber);
						obj.find("img").width(obj.width());
					}
				});
			}else{
				obj.resizable({
					handles: "e",
					minWidth:obj.attr("data-minwidth")
				});
			}
		};
		if(obj.hasClass("skulist") || obj.hasClass("platformskulist") || obj.hasClass("invoiceskulist")){
			var heightOld;
			obj.resizable({
				//handles: "s",
				minHeight:loopHeight+headHeight+footHeight,
				grid: loopHeight,
				containment: ".custom-drop",
				start:function(event,ui){
					heightOld=obj.height();
				},
				stop:function(event,ui){
					var colspanNum=obj.find(".skulist-table th:not('.dis-none')").length, heightNew=obj.height()-heightOld;
					var changeNum=parseInt(heightNew/loopHeight);
					var loopTr=obj.find(".skulist-table tbody tr").html(),loopTbody="";
					if(heightNew>0){
						for(var i=1;i<=changeNum;i++){
							loopTbody+='<tr id="ITEM_LIST_NO_DETAIL">'+loopTr+'</tr>';
						};
						obj.find(".skulist-table tbody").append(loopTbody).find("tr:not(:first) td span").empty().removeAttr("title");
						maxNum=maxNum+changeNum;
					}else if(heightNew<0){
						for(var i=1;i<=Math.abs(changeNum);i++){
							obj.find(".skulist-table tbody tr:last").remove();
						};
						maxNum=maxNum-Math.abs(changeNum);
					};
					return maxNum;	
				}
			});
		};
		if(obj.hasClass("declarelist")  || obj.hasClass("eubdeclarelist") || obj.hasClass("manualdeclare") || obj.hasClass("manualeubdeclarelist")){// || obj.hasClass("declarenewlist")
			var heightOld;
			obj.resizable({
				minHeight:loopHeight+headHeight+footHeight,
				grid: loopHeight,
				containment: ".custom-drop",
				start:function(event,ui){
					heightOld=obj.height();
				},
				stop:function(event,ui){
					var colspanNum=obj.find(".skulist-table th:not('.dis-none')").length, heightNew=obj.height()-heightOld;
					var changeNum=parseInt(heightNew/loopHeight);
					var loopTr=obj.find(".skulist-table tbody tr").html(),loopTbody="";
					if(heightNew>0){
						for(var i=1;i<=changeNum;i++){
							loopTbody+='<tr id="ITEM_LIST_NO_DETAIL">'+loopTr+'</tr>';
						};
						obj.find(".skulist-table tbody").append(loopTbody).find("tr:not(:first) td span").empty().removeAttr("title");
						maxNum=maxNum+changeNum;
					}else if(heightNew<0){
						for(var i=1;i<=Math.abs(changeNum);i++){
							obj.find(".skulist-table tbody tr:last").remove();
						};
						maxNum=maxNum-Math.abs(changeNum);
					};
					return maxNum;	
				}
			});
		};
		
	};
	//临时接收drop
	$(".custom-area").droppable({
		accept: '.dragitem:not(".complete")',
		drop:function(event,ui){
			 
			var positionLeft, positionTop,tempstyle;
			positionLeft=Math.round(ui.offset.left-$(".custom-area").offset().left),
			positionTop=Math.round(ui.offset.top-$(".custom-area").offset().top),
			labeltype=ui.draggable.attr("data-type"),
			defaultWidth=ui.draggable.attr("data-default-width"),
			defaultHeight=ui.draggable.attr("data-default-height");
			$(".product-list").hide();
			$(".postUpper").hide();
			 
			switch(labeltype){
				//爆款标识
				case "baokuan":$(".custom-drop").append('<div class="baokuan dropitem imageitem" data-type="'+labeltype+'" data-title="图片" style="width:'+defaultWidth+'px;height:'+defaultHeight+'px;left:'+positionLeft+'px;top:'+positionTop+'px"><img src="'+ui.draggable.find("img").attr("src")+'"></div>');break;
				
				case "COD_IMG":$(".custom-drop").append('<div   class=" COD_IMG  dropitem imageitem" data-type="'+labeltype+'" data-title="图片" style="width:'+defaultWidth+'px;height:'+defaultHeight+'px;left:'+positionLeft+'px;top:'+positionTop+'px"><img src="'+ui.draggable.find("img").attr("src")+'"></div>');break;
			 
				 //创建字段属性
				case "FULL_ITEMS_DETAIL_STR2":$(".custom-drop").append('<div class="dropitem character" id="COD" data-type="'+labeltype+'"  style="width:'+defaultWidth+'px;left:'+positionLeft+'px;top:'+positionTop+'px"><div id="FULL_ITEMS_DETAIL_STR2" class="detail">'+ui.draggable.find(".detail").html()+'</div></div>');
				break;
				case "COD":$(".custom-drop").append('<div class="dropitem  COD character"  data-type="'+labeltype+'" data-title="'+ui.draggable.find(".title").text()+'" style="width:'+defaultWidth+'px;left:'+positionLeft+'px;top:'+positionTop+'px"><strong class="title">COD</strong><span class="detail">'+ui.draggable.find(".detail").html()+'</span></div>');
				break;
				case "COD_DIS_VALUE":$(".custom-drop").append('<div class="dropitem  COD character"  data-type="'+labeltype+'" data-title="'+ui.draggable.find(".title").text()+'" style="width:'+defaultWidth+'px;left:'+positionLeft+'px;top:'+positionTop+'px"><strong class="title">COD</strong><span class="detail">'+ui.draggable.find(".detail").html()+'</span></div>');
				break;
				//创建字段属性
				case "character":$(".custom-drop").append('<div class="dropitem character" data-type="'+labeltype+'" data-title="'+ui.draggable.find(".title").text()+'" style="width:'+defaultWidth+'px;left:'+positionLeft+'px;top:'+positionTop+'px"><strong class="title">'+ui.draggable.find(".title").text()+'</strong><span class="detail">'+ui.draggable.find(".detail").html()+'</span></div>');
				break;
				//创建打印日期字段
				case "date":
				$(".custom-drop").append('<div class="dropitem character" data-type="'+labeltype+'" data-title="'+ui.draggable.find(".title").text()+'" style="width:'+defaultWidth+'px;left:'+positionLeft+'px;top:'+positionTop+'px"><strong class="title">'+ui.draggable.find(".title").text()+'</strong><span class="detail" data-year="true" data-divide="-">'+ getCurrentDate(true,"-") +'</span></div>');
				break;
				//创建地址组合字段
				case "address":$(".custom-drop").append('<div class="dropitem character address" data-type="'+labeltype+'" data-title="'+ui.draggable.find(".title").text()+'" style="width:'+defaultWidth+'px;left:'+positionLeft+'px;top:'+positionTop+'px"><span class="detail block">'+ui.draggable.find(".detail").html()+'</span></div>');
				break;
				//创建条码
				case "barcode":$(".custom-drop").append('<div class="dropitem barcode" data-type="'+labeltype+'" data-title="'+ui.draggable.find(".title").text()+'" data-minwidth="'+defaultWidth+'" style="width:'+defaultWidth+'px;left:'+positionLeft+'px;top:'+positionTop+'px">'+ui.draggable.find(".detail").html()+'</div>');
				break;
				//创建图片可伸缩条码
				case "barcode-resize":$(".custom-drop").append('<div class="dropitem barcode barcode-resize" data-type="barcode" data-title="'+ui.draggable.find(".title").text()+'" data-minwidth="'+defaultWidth+'" style="width:'+defaultWidth+'px;left:'+positionLeft+'px;top:'+positionTop+'px">'+ui.draggable.find(".detail").html()+'</div>');
				break;
				//创建纵向条码
				case "barcode-vertical":$(".custom-drop").append('<div class="dropitem barcode vertical-type" data-type="'+labeltype+'" data-title="'+ui.draggable.find(".title").text()+'" data-minheight="'+defaultHeight+'" style="height:'+defaultHeight+'px;left:'+positionLeft+'px;top:'+positionTop+'px">'+ui.draggable.find(".detail").html()+'</div>');
				break;
				//创建二维码
				case "qRcode":$(".custom-drop").append('<div class="dropitem barcode qRcode" data-type="'+labeltype+'" data-title="'+ui.draggable.find(".title").text()+'" data-minwidth="'+defaultWidth+'" style="width:'+defaultWidth+'px;left:'+positionLeft+'px;top:'+positionTop+'px">'+ui.draggable.find(".detail").html()+'</div>');
				break;
				//创建商品清单
				case "skulist":
					$(".custom-drop").append('<div class="dropitem skulist" data-type="'+labeltype+'" data-title="'+ui.draggable.find(".title").text()+'" style="width:100%;height:'+defaultHeight+'px;left:0px;top:'+positionTop+'px">'+ui.draggable.find(".detail").html()+'</div>');
					ui.draggable.addClass("complete");
					//$(".label-group").append('<div id="PRODUCT_LIST_OVERFLOW" class="label-content overflow" style="width:'+labelWidth+'px;height:'+labelHeight+'px;"><div class="copy-drop">'+ui.draggable.find(".detail").html().replace('FULL_ITEMS_DETAIL_TABLE', 'FULL_ITEMS_DETAIL_TABLE_COPY')+'</div></div>');
					countNum();
				break;
				case "platformskulist":
				$(".custom-drop").append('<div class="dropitem platformskulist" data-type="'+labeltype+'" data-title="'+ui.draggable.find(".title").text()+'" style="width:100%;height:'+defaultHeight+'px;left:0px;top:'+positionTop+'px">'+ui.draggable.find(".detail").html()+'</div>');
				ui.draggable.addClass("complete");
				$(".label-group").append('<div id="PRODUCT_LIST_OVERFLOW" class="label-content overflow" style="width:'+labelWidth+'px;height:'+labelHeight+'px;"><div class="copy-drop">'+ui.draggable.find(".detail").html().replace('PLATFORM_SKU_FULL_ITEMS_DETAIL_TABLE', 'FULL_ITEMS_DETAIL_TABLE_COPY')+'</div></div>');
				countNum();
				break;
				case "invoiceskulist":
					$(".custom-drop").append('<div class="dropitem invoiceskulist" data-type="'+labeltype+'" data-title="'+ui.draggable.find(".title").text()+'" style="width:100%;height:'+defaultHeight+'px;left:0px;top:'+positionTop+'px">'+ui.draggable.find(".detail").html()+'</div>');
					ui.draggable.addClass("complete");
					$(".label-group").append('<div id="PRODUCT_LIST_OVERFLOW" class="label-content overflow" style="width:'+labelWidth+'px;height:'+labelHeight+'px;"><div class="copy-drop">'+ui.draggable.find(".detail").html().replace('INVOICE_SKU_FULL_ITEMS_DETAIL_TABLE', 'FULL_ITEMS_DETAIL_TABLE_COPY')+'</div></div>');
					countNum();
					break;
				//创建报关单
				case "declarelist":
				$(".custom-drop").append('<div class="dropitem declarelist" data-type="'+labeltype+'" data-title="'+ui.draggable.find(".title").text()+'" style="width:100%;height:'+defaultHeight+'px;left:0px;top:'+positionTop+'px">'+ui.draggable.find(".detail").html()+'</div>');
				ui.draggable.addClass("complete");
				$(".label-group").append('<div id="PRODUCT_LIST_OVERFLOW" class="label-content overflow dis-none" style="width:'+labelWidth+'px;height:'+labelHeight+'px;"><div class="copy-drop">'+ui.draggable.find(".detail").html().replace('FULL_ITEMS_DETAIL_TABLE', 'FULL_ITEMS_DETAIL_TABLE_COPY')+'</div></div>');
				$(".copy-drop .skulist-table").find("thead, tfoot").remove();
				countNum();
				break;
				case "manualdeclare"://创建手动报关单
				$(".custom-drop").append('<div class="dropitem manualdeclare" data-type="'+labeltype+'" data-title="'+ui.draggable.find(".title").text()+'" style="width:100%;height:'+defaultHeight+'px;left:0px;top:'+positionTop+'px">'+ui.draggable.find(".detail").html()+'</div>');
				ui.draggable.addClass("complete");
				countNum();
				break;
				//创建EUB报关单
				case "eubdeclarelist":
				$(".custom-drop").append('<div class="dropitem eubdeclarelist" data-type="'+labeltype+'" data-title="'+ui.draggable.find(".title").text()+'" style="width:100%;height:'+defaultHeight+'px;left:0px;top:'+positionTop+'px">'+ui.draggable.find(".detail").html()+'</div>');
				ui.draggable.addClass("complete");
				//$(".label-group").append('<div id="PRODUCT_LIST_OVERFLOW" class="label-content overflow dis-none" style="width:'+labelWidth+'px;height:'+labelHeight+'px;"><div class="copy-drop">'+ui.draggable.find(".detail").html()+'</div></div>');
				$(".label-group").append('<div id="PRODUCT_LIST_OVERFLOW" class="label-content overflow dis-none" style="width:'+labelWidth+'px;height:'+labelHeight+'px;"><div class="copy-drop">'+ui.draggable.find(".detail").html().replace('FULL_ITEMS_DETAIL_TABLE', 'FULL_ITEMS_DETAIL_TABLE_COPY')+'</div></div>');
				$(".copy-drop .skulist-table").find("thead, tfoot").remove();
				countNum();
				break;
				case "manualeubdeclarelist":
				$(".custom-drop").append('<div class="dropitem manualeubdeclarelist" data-type="'+labeltype+'" data-title="'+ui.draggable.find(".title").text()+'" style="width:100%;height:'+defaultHeight+'px;left:0px;top:'+positionTop+'px">'+ui.draggable.find(".detail").html()+'</div>');
				ui.draggable.addClass("complete");
				countNum();
				break;
				//创建单行EUB报关单
				case "eubdeclarelistSingleRow":
					$(".custom-drop").append('<div class="dropitem eubdeclarelistSingleRow" data-type="'+labeltype+'" data-title="'+ui.draggable.find(".title").text()+'" style="width:100%;height:'+defaultHeight+'px;left:0px;top:'+positionTop+'px">'+ui.draggable.find(".detail").html()+'</div>');
					ui.draggable.addClass("complete");
				break;
				//创建报关配货单
				case "declareskulist":
				$(".custom-drop").append('<div class="dropitem declareskulist" data-type="'+labeltype+'" data-title="'+ui.draggable.find(".title").text()+'" style="width:100%;height:'+defaultHeight+'px;left:0px;top:'+positionTop+'px">'+ui.draggable.find(".detail").html()+'</div>');
				ui.draggable.addClass("complete");
				//$(".label-group").append('<div id="PRODUCT_LIST_OVERFLOW" class="label-content overflow" style="width:'+labelWidth+'px;height:'+labelHeight+'px;"><div class="copy-drop">'+ui.draggable.find(".detail").html()+'</div></div>');
				$(".label-group").append('<div id="PRODUCT_LIST_OVERFLOW" class="label-content overflow" style="width:'+labelWidth+'px;height:'+labelHeight+'px;"><div class="copy-drop">'+ui.draggable.find(".detail").html().replace('FULL_ITEMS_DETAIL_TABLE', 'FULL_ITEMS_DETAIL_TABLE_COPY')+'</div></div>');
				$(".copy-drop .skulist-table").find("thead, tfoot").remove();
				countNum();
				break;
				//创建New报关单
			   /* case "declarenewlist":
					$(".custom-drop").append('<div class="dropitem declarenewlist" data-type="'+labeltype+'" data-title="'+ui.draggable.find(".title").text()+'" style="width:100%;height:'+defaultHeight+'px;left:0px;top:'+positionTop+'px">'+ui.draggable.find(".detail").html()+'</div>');
					ui.draggable.addClass("complete");
				   $(".label-group").append('<div id="PRODUCT_LIST_OVERFLOW" class="label-content overflow dis-none" style="width:'+labelWidth+'px;height:'+labelHeight+'px;"><div class="copy-drop">'+ui.draggable.find(".detail").html().replace('FULL_ITEMS_DETAIL_TABLE_NEW', 'FULL_ITEMS_DETAIL_TABLE_COPY')+'</div></div>');
				   $(".copy-drop .skulist-table").find("thead, tfoot").remove();
				   countNum();
					break;*/
				//创建配货信息
				case "itemlist":$(".custom-drop").append('<div class="dropitem character" data-type="'+labeltype+'" data-title="'+ui.draggable.find(".title").text()+'" style="width:'+defaultWidth+'px;left:'+positionLeft+'px;top:'+positionTop+'px"><span class="detail">'+ui.draggable.find(".detail").html()+'</span></div>');
				break;
				//创建自订文本
				case "customtext":$(".custom-drop").append('<div class="dropitem character" data-type="'+labeltype+'" data-title="'+ui.draggable.find(".title").text()+'" style="width:'+defaultWidth+'px;left:'+positionLeft+'px;top:'+positionTop+'px"><i class="ico-checkbox-unchecked2"></i><span class="detail">自定义文本内容</span></div>');break;
				//创建水平线
				case "line-x":$(".custom-drop").append('<div class="dropitem linetype" data-type="'+labeltype+'" data-title="'+ui.draggable.find(".title").text()+'" style="width:100%;border-top-color:#000;height:2px;border-top-style:solid;border-top-width:2px;top:'+(positionTop+30)+'px"><div class="line-handle"></div></div>');break;
				//创建垂直线
				case "line-y":$(".custom-drop").append('<div class="dropitem linetype" data-type="'+labeltype+'" data-title="'+ui.draggable.find(".title").text()+'" style="width:2px;border-left-color:#000;height:100%;border-left-style:solid;border-left-width:2px;top:0px;left:'+(positionLeft+30)+'px;"><div class="line-handle"></div></div>');break;
				//创建圆形文本框
				
				case "circletext":$(".custom-drop").append('<div class="dropitem imageitem circletext" data-type="'+labeltype+'" data-title="'+ui.draggable.find(".title").text()+'" style="width:40px;height:40px;line-height:40px;border-width:2px;left:'+positionLeft+'px;top:'+positionTop+'px"><span class="detail" style="font-size:24px;">A</span></div>');break;
				//创建本地上传的图片
				case "uploadimage":$(".custom-drop").append('<div class="dropitem character onlineimage" data-type="'+labeltype+'" data-title="'+ui.draggable.find(".title").text()+'" style="width:50px;height:50px;left:'+positionLeft+'px;top:'+positionTop+'px"><span></span><img src="/printTemp/img/photo_default.jpg"></div>');break;
				//创建预设图片
				case "image":$(".custom-drop").append('<div class="dropitem imageitem" data-type="'+labeltype+'" data-title="图片" style="width:'+defaultWidth+'px;height:'+defaultHeight+'px;left:'+positionLeft+'px;top:'+positionTop+'px"><img src="'+ui.draggable.find("img").attr("src")+'"></div>');break;
				
				case "productImg":$(".custom-drop").append('<div class="dropitem imageitem" data-type="'+labeltype+'" data-title="'+ui.draggable.find(".title").text()+'" style="width:'+defaultWidth+'px;height:'+defaultHeight+'px;left:'+positionLeft+'px;top:'+positionTop+'px"><div class="detail ellipsis">'+ui.draggable.find(".detail").html()+'</div></div>');break;
				//创建FBA商品标题
				case "fbatext":$(".custom-drop").append('<div class="dropitem fbatext" data-type="'+labeltype+'" data-title="'+ui.draggable.find(".title").text()+'" style="width:'+defaultWidth+'px;left:0px;top:'+positionTop+'px" data-minwidth="50"><div class="detail ellipsis">'+ui.draggable.find(".detail").html()+'</div></div>');break;

			};
			dropCreate($(".custom-drop .dropitem").last());
			registerTableDragEvent();
		}
	});
	//调整位置
	$(".custom-drop").droppable({
		activeClass: "active",
		accept: '.dropitem'
	});

	//=====================点击属性标签返回设定值====================
	//拖拽属性名称标签
	$(".label-type .dragitem").draggable({
		helper:"clone",
		appendTo: "body",
		revert: "invalid",
		start:function(event,ui){
			closeLabelSet();
			$(".custom-drop").addClass("active");
		},
		stop:function(event,ui){
			$(".custom-drop").removeClass("active");
		}
	});
	function getTempSet(obj){
		obj.addClass("active").siblings().removeClass("active");
		//面板初始化
		$(".label-set .panel-title").text(obj.attr("data-title"));
		$(".label-set .nav-tabs li").css("display","").removeClass("active");
		$(".label-set .tab-pane").removeClass("active");
		//不同类别区分处理
		switch(obj.attr("data-type")){
			//固定属性
			case "COD":
				$(".label-set").find(".panel-toolbar-wrapper").show();
				$(".label-set .edit-btn .btn-copy").removeClass("disabled");
				//获取目标css
				var itemTitle=obj.find(".title"),
					itemDetail=obj.find(".detail");
				//返回标题设定
				$(".label-set .nav-tabs").find("li.title").css("display","table-cell").addClass("active");
				$("#titelset").addClass("active");
				if(itemTitle.css("display")=="none"){
					$("#viewTitle").attr("checked",false);$("#titelset .title-set").find("input,select").attr("disabled",true);
				}else{
					$("#viewTitle").attr("checked",true);$("#titelset .title-set").find("input,select").attr("disabled",false);
				}
				itemTitle.css("display")=="block"?$("#titleNowrap").attr("checked",true):$("#titleNowrap").attr("checked",false);
				$("#titleName").val(itemTitle.text());
				$("#titleAlign").val(itemTitle.css("textAlign"));
				$("#titleFontFamily").val(itemTitle.css("font-family"));
				$("#titleFontSize").val(itemTitle.css("font-size"));
				$("#titleLineHeight").val(Math.round(parseInt(itemTitle.css("lineHeight"))/parseInt(itemTitle.css("font-size"))*10)/10);
				itemTitle.css("fontWeight")>="700"?$("#titleFontWeight").attr("checked",true):$("#titleFontWeight").attr("checked",false);
				//返回内容设定
				$(".label-set .nav-tabs").find("li.detail").css("display","table-cell");
				$("#detailAlign").val(itemDetail.css("textAlign"));
				$("#detailFontFamily").val(itemDetail.css("font-family"));
				$("#detailFontSize").val(itemDetail.css("font-size"));
				$("#detailLineHeight").val(Math.round(parseInt(itemDetail.css("lineHeight"))/parseInt(itemDetail.css("font-size"))*10)/10);
				itemDetail.css("fontWeight")>="700"?$("#detailFontWeight").attr("checked",true):$("#detailFontWeight").attr("checked",false);
				itemDetail.css("text-transform")=="uppercase"?$("#postUpper").attr("checked",true):$("#postUpper").attr("checked",false);
				//返回边框设定
				setBorder(obj);
			break;
			//固定属性
			case "COD_DIS_VALUE":
				$(".label-set").find(".panel-toolbar-wrapper").show();
				$(".label-set .edit-btn .btn-copy").removeClass("disabled");
				//获取目标css
				var itemTitle=obj.find(".title"),
					itemDetail=obj.find(".detail");
				//返回标题设定
				$(".label-set .nav-tabs").find("li.title").css("display","table-cell").addClass("active");
				$("#titelset").addClass("active");
				if(itemTitle.css("display")=="none"){
					$("#viewTitle").attr("checked",false);$("#titelset .title-set").find("input,select").attr("disabled",true);
				}else{
					$("#viewTitle").attr("checked",true);$("#titelset .title-set").find("input,select").attr("disabled",false);
				}
				itemTitle.css("display")=="block"?$("#titleNowrap").attr("checked",true):$("#titleNowrap").attr("checked",false);
				$("#titleName").val(itemTitle.text());
				$("#titleAlign").val(itemTitle.css("textAlign"));
				$("#titleFontFamily").val(itemTitle.css("font-family"));
				$("#titleFontSize").val(itemTitle.css("font-size"));
				$("#titleLineHeight").val(Math.round(parseInt(itemTitle.css("lineHeight"))/parseInt(itemTitle.css("font-size"))*10)/10);
				itemTitle.css("fontWeight")>="700"?$("#titleFontWeight").attr("checked",true):$("#titleFontWeight").attr("checked",false);
				//返回内容设定
				$(".label-set .nav-tabs").find("li.detail").css("display","table-cell");
				$("#detailAlign").val(itemDetail.css("textAlign"));
				$("#detailFontFamily").val(itemDetail.css("font-family"));
				$("#detailFontSize").val(itemDetail.css("font-size"));
				$("#detailLineHeight").val(Math.round(parseInt(itemDetail.css("lineHeight"))/parseInt(itemDetail.css("font-size"))*10)/10);
				itemDetail.css("fontWeight")>="700"?$("#detailFontWeight").attr("checked",true):$("#detailFontWeight").attr("checked",false);
				itemDetail.css("text-transform")=="uppercase"?$("#postUpper").attr("checked",true):$("#postUpper").attr("checked",false);
				//返回边框设定
				setBorder(obj);
			break;
			//固定属性
			case "character":
				$(".label-set").find(".panel-toolbar-wrapper").show();
				$(".label-set .edit-btn .btn-copy").removeClass("disabled");
				//获取目标css
				var itemTitle=obj.find(".title"),
					itemDetail=obj.find(".detail");
				//返回标题设定
				$(".label-set .nav-tabs").find("li.title").css("display","table-cell").addClass("active");
				$("#titelset").addClass("active");
				if(itemTitle.css("display")=="none"){
					$("#viewTitle").attr("checked",false);$("#titelset .title-set").find("input,select").attr("disabled",true);
				}else{
					$("#viewTitle").attr("checked",true);$("#titelset .title-set").find("input,select").attr("disabled",false);
				}
				itemTitle.css("display")=="block"?$("#titleNowrap").attr("checked",true):$("#titleNowrap").attr("checked",false);
				$("#titleName").val(itemTitle.text());
				$("#titleAlign").val(itemTitle.css("textAlign"));
				$("#titleFontFamily").val(itemTitle.css("font-family"));
				$("#titleFontSize").val(itemTitle.css("font-size"));
				$("#titleLineHeight").val(Math.round(parseInt(itemTitle.css("lineHeight"))/parseInt(itemTitle.css("font-size"))*10)/10);
				itemTitle.css("fontWeight")>="700"?$("#titleFontWeight").attr("checked",true):$("#titleFontWeight").attr("checked",false);
				//返回内容设定
				$(".label-set .nav-tabs").find("li.detail").css("display","table-cell");
				$("#detailAlign").val(itemDetail.css("textAlign"));
				$("#detailFontFamily").val(itemDetail.css("font-family"));
				$("#detailFontSize").val(itemDetail.css("font-size"));
				$("#detailLineHeight").val(Math.round(parseInt(itemDetail.css("lineHeight"))/parseInt(itemDetail.css("font-size"))*10)/10);
				itemDetail.css("fontWeight")>="700"?$("#detailFontWeight").attr("checked",true):$("#detailFontWeight").attr("checked",false);
				itemDetail.css("text-transform")=="uppercase"?$("#postUpper").attr("checked",true):$("#postUpper").attr("checked",false);
				//返回边框设定
				setBorder(obj);
			break;
			//固定属性
			case "character":
				$(".label-set").find(".panel-toolbar-wrapper").show();
				$(".label-set .edit-btn .btn-copy").removeClass("disabled");
				//获取目标css
				var itemTitle=obj.find(".title"),
					itemDetail=obj.find(".detail");
				//返回标题设定
				$(".label-set .nav-tabs").find("li.title").css("display","table-cell").addClass("active");
				$("#titelset").addClass("active");
				if(itemTitle.css("display")=="none"){
					$("#viewTitle").attr("checked",false);$("#titelset .title-set").find("input,select").attr("disabled",true);
				}else{
					$("#viewTitle").attr("checked",true);$("#titelset .title-set").find("input,select").attr("disabled",false);
				}
				itemTitle.css("display")=="block"?$("#titleNowrap").attr("checked",true):$("#titleNowrap").attr("checked",false);
				$("#titleName").val(itemTitle.text());
				$("#titleAlign").val(itemTitle.css("textAlign"));
				$("#titleFontFamily").val(itemTitle.css("font-family"));
				$("#titleFontSize").val(itemTitle.css("font-size"));
				$("#titleLineHeight").val(Math.round(parseInt(itemTitle.css("lineHeight"))/parseInt(itemTitle.css("font-size"))*10)/10);
				itemTitle.css("fontWeight")>="700"?$("#titleFontWeight").attr("checked",true):$("#titleFontWeight").attr("checked",false);
				//返回内容设定
				$(".label-set .nav-tabs").find("li.detail").css("display","table-cell");
				$("#detailAlign").val(itemDetail.css("textAlign"));
				$("#detailFontFamily").val(itemDetail.css("font-family"));
				$("#detailFontSize").val(itemDetail.css("font-size"));
				$("#detailLineHeight").val(Math.round(parseInt(itemDetail.css("lineHeight"))/parseInt(itemDetail.css("font-size"))*10)/10);
				itemDetail.css("fontWeight")>="700"?$("#detailFontWeight").attr("checked",true):$("#detailFontWeight").attr("checked",false);
				itemDetail.css("text-transform")=="uppercase"?$("#postUpper").attr("checked",true):$("#postUpper").attr("checked",false);
				//返回边框设定
				setBorder(obj);
			break;
			//日期属性
			case "date":
				$(".label-set").find(".panel-toolbar-wrapper").show();
				$(".label-set .edit-btn .btn-copy").removeClass("disabled");
				//获取目标css
				var itemTitle=obj.find(".title"),
					itemDetail=obj.find(".detail");
				//返回标题设定
				$(".label-set .nav-tabs").find("li.title").css("display","table-cell").addClass("active");
				$("#titelset").addClass("active");
				if(itemTitle.css("display")=="none"){
					$("#viewTitle").attr("checked",false);$("#titelset .title-set").find("input,select").attr("disabled",true);
				}else{
					$("#viewTitle").attr("checked",true);$("#titelset .title-set").find("input,select").attr("disabled",false);
				}
				itemTitle.css("display")=="block"?$("#titleNowrap").attr("checked",true):$("#titleNowrap").attr("checked",false);
				$("#titleName").val(itemTitle.text());
				$("#titleAlign").val(itemTitle.css("textAlign"));
				$("#titleFontFamily").val(itemTitle.css("font-family"));
				$("#titleFontSize").val(itemTitle.css("font-size"));
				$("#titleLineHeight").val(Math.round(parseInt(itemTitle.css("lineHeight"))/parseInt(itemTitle.css("font-size"))*10)/10);
				itemTitle.css("fontWeight")>="700"?$("#titleFontWeight").attr("checked",true):$("#titleFontWeight").attr("checked",false);
				//返回日期设定
				$(".label-set .nav-tabs").find("li.date").css("display","table-cell");
				
				itemDetail.attr("data-year")=="true"?$("#viewYear").attr("checked",true):$("#viewYear").attr("checked",false);
				$("#dateType").val(itemDetail.attr("data-divide"));
				
				$("#dateAlign").val(itemDetail.css("textAlign"));
				$("#dateFontFamily").val(itemDetail.css("font-family"));
				$("#dateFontSize").val(itemDetail.css("font-size"));
				$("#dateLineHeight").val(Math.round(parseInt(itemDetail.css("lineHeight"))/parseInt(itemDetail.css("font-size"))*10)/10);
				itemDetail.css("fontWeight")>="700"?$("#dateFontWeight").attr("checked",true):$("#dateFontWeight").attr("checked",false);
				//返回边框设定
				setBorder(obj);
			break;
			//地址字段组合
			case "address":
				$(".postUpper").show();
				$(".label-set").find(".panel-toolbar-wrapper").show();
				$(".label-set .edit-btn .btn-copy").removeClass("disabled");
				//获取目标css
				var itemDetail=obj.find(".detail");
				var itemDetailpostcode=itemDetail.find(".postcode");
				//返回内容设定
				$(".label-set .nav-tabs").find("li.detail").css("display","table-cell").addClass("active");
				$("#detailset").addClass("active");
				$("#detailAlign").val(itemDetail.css("textAlign"));
				$("#detailFontFamily").val(itemDetail.css("font-family"));
				$("#detailFontSize").val(itemDetail.css("font-size"));
				$("#detailLineHeight").val(Math.round(parseInt(itemDetail.css("lineHeight"))/parseInt(itemDetail.css("font-size"))*10)/10);
				itemDetail.css("fontWeight")>="700"?$("#detailFontWeight").attr("checked",true):$("#detailFontWeight").attr("checked",false);
				itemDetailpostcode.css("text-transform")=="uppercase"?$("#postUpper").attr("checked",true):$("#postUpper").attr("checked",false)
				//返回字段设定
				$(".label-set .nav-tabs").find("li.field-address").css("display","table-cell");
				itemDetail.find("span").each(function(){
					var	className=$(this).attr("class"),
						itemForm=$("#fieldset-address .form-group.multiple");
					if($(this).css("display")=="none"){
						itemForm.find("input[type='checkbox'][value='"+className+"']").attr("checked",false);
					}else{
						itemForm.find("input[type='checkbox'][value='"+className+"']").attr("checked",true);
					}
				});
				if(itemDetail.find(".country").css("display")=="none"){
					$("#fieldset-address .multiple input[name='viewField'][value='country_cn']").attr("disabled",true);
				}else{
					$("#fieldset-address .multiple input[name='viewField'][value='country_cn']").attr("disabled",false);
				};
				if(itemDetail.find(".street").css("display")=="none"){
					$("#fieldset-address .multiple input[name='viewField'][value='doorcode']").attr("disabled",true);
				}else{
					$("#fieldset-address .multiple input[name='viewField'][value='doorcode']").attr("disabled",false);
				};
				itemDetail.hasClass("block")?$("#newline").attr("checked",true):$("#newline").attr("checked",false);
				//返回边框设定
				setBorder(obj);
			break;
			//条码
			case "barcode":
				$(".label-set").find(".panel-toolbar-wrapper").hide();
				$(".label-set .edit-btn .btn-copy").addClass("disabled");
				//返回条码设置
				var codeNum=obj.find(".codemunber");
				$("#barcode").addClass("active");
				$("#codeTextType").hide();
				$("#codeType").val(obj.attr("data-code-type"));
				if(codeNum.css("display")=="none"){
					$("#viewCodeNum").attr("checked",false);$("#barcode .codenum-set").find("input,select").attr("disabled",true);
				}else{
					$("#viewCodeNum").attr("checked",true);$("#barcode .codenum-set").find("input,select").attr("disabled",false);
				};
				$("#codeNumAlign").val(codeNum.css("textAlign"));
				$("#codeNumFontSize").val(codeNum.css("font-size"));
				codeNum.css("fontWeight")>="700"?$("#codeNumFontWeight").attr("checked",true):$("#codeNumFontWeight").attr("checked",false);
				$("#codePrefix").val(codeNum.find(".prefix").text());
			break;
			//纵向条码
			case "barcode-vertical":
				$(".label-set").find(".panel-toolbar-wrapper").hide();
				$(".label-set .edit-btn .btn-copy").addClass("disabled");
				//返回条码设置
				var codeNum=obj.find(".codemunber");
				$("#barcode").addClass("active");
				$("#codeTextType").show();
				$("#codeType").val(obj.attr("data-code-type"));
				if(codeNum.css("display")=="none"){
					$("#viewCodeNum").attr("checked",false);$("#barcode .codenum-set").find("input,select").attr("disabled",true);
				}else{
					$("#viewCodeNum").attr("checked",true);$("#barcode .codenum-set").find("input,select").attr("disabled",false);
				};
				if(obj.hasClass("vertical-reverse")){$("#leftCodeText").prop("checked",false);$("#rightCodeText").prop("checked",true);}
				else {$("#leftCodeText").prop("checked",true);$("#rightCodeText").prop("checked",false);}
				$("#codeNumAlign").val(codeNum.css("textAlign"));
				$("#codeNumFontSize").val(codeNum.css("font-size"));
				codeNum.css("fontWeight")>="700"?$("#codeNumFontWeight").attr("checked",true):$("#codeNumFontWeight").attr("checked",false);
				$("#codePrefix").val(codeNum.find(".prefix").text());
				if(customText.css("display") == "none"){
					$("#viewCodeCustomText").attr("checked",false);$("#codeCustomText").attr("disabled",true).val(customText.text());
				}else{
					$("#viewCodeCustomText").attr("checked",true);$("#codeCustomText").attr("disabled",false).val(customText.text());
				};
			break;
			//商品清单
			case "skulist": 
			$(".label-set").find(".panel-toolbar-wrapper").show();
				$(".label-set .edit-btn .btn-copy").addClass("disabled");
				//获取目标css
				var table=obj.find(".skulist-table"),
					thead=table.find("thead"),
					tbody=table.find("tbody"),
					tfoot=table.find("tfoot");
				//返回表格设定
				$(".label-set .nav-tabs").find("li.table").css("display","table-cell").addClass("active");
				$("#tableset").addClass("active");
				table.hasClass("no-tdborder")?$("#viewTdBorder").attr("checked",false):$("#viewTdBorder").attr("checked",true);
				thead.is(":hidden")?$("#viewThead").attr("checked",false).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",true):$("#viewThead").attr("checked",true).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",false);
				$("#theadFontFamily").val(thead.css("font-family"));
				$("#theadFontSize").val(thead.css("font-size"));
				$("#tbodyFontFamily").val(tbody.css("font-family"));
				$("#tbodyFontSize").val(tbody.css("font-size"));
				tfoot.is(":hidden")?$("#viewTfoot").attr("checked",false).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",true):$("#viewTfoot").attr("checked",true).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",false);
				$("#tfootFontFamily").val(tfoot.css("font-family"));
				$("#tfootFontSize").val(tfoot.css("font-size"));
				//$("#tfootAlign").val(tfoot.css("textAlign"));
				$(".product-list").show();
				$(".sortType").val((table.attr('data-sort') ? table.attr('data-sort') : 'default'));
				$(".mergeSKU").val((table.attr('data-merge-sku') ? table.attr('data-merge-sku') : 'default'));
				//返回字段设定
				$(".label-set .nav-tabs").find("li.field-sku").css("display","table-cell");
				thead.find("th").each(function(){
					var index=$(this).index(),
						itemForm=$("#fieldset-sku .form-group").eq(index);
					if($(this).hasClass("dis-none")){
						itemForm.find("input[type='checkbox']").attr("checked",false);
						itemForm.find("input.form-control").attr("disabled",true).val($(this).find("span").text());
					}else{
						itemForm.find("input[type='checkbox']").attr("checked",true);
						itemForm.find("input.form-control").attr("disabled",false).val($(this).find("span").text());
					}
				});
				break;
			case "platformskulist":
				$(".label-set").find(".panel-toolbar-wrapper").show();
				$(".label-set .edit-btn .btn-copy").addClass("disabled");
				//获取目标css
				var table=obj.find(".skulist-table"),
					thead=table.find("thead"),
					tbody=table.find("tbody"),
					tfoot=table.find("tfoot");
				//返回表格设定
				$(".label-set .nav-tabs").find("li.table").css("display","table-cell").addClass("active");
				$("#tableset").addClass("active");
				table.hasClass("no-tdborder")?$("#viewTdBorder").attr("checked",false):$("#viewTdBorder").attr("checked",true);
				thead.is(":hidden")?$("#viewThead").attr("checked",false).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",true):$("#viewThead").attr("checked",true).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",false);
				$("#theadFontFamily").val(thead.css("font-family"));
				$("#theadFontSize").val(thead.css("font-size"));
				$("#tbodyFontFamily").val(tbody.css("font-family"));
				$("#tbodyFontSize").val(tbody.css("font-size"));
				tfoot.is(":hidden")?$("#viewTfoot").attr("checked",false).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",true):$("#viewTfoot").attr("checked",true).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",false);
				$("#tfootFontFamily").val(tfoot.css("font-family"));
				$("#tfootFontSize").val(tfoot.css("font-size"));
				//$("#tfootAlign").val(tfoot.css("textAlign"));
				$(".product-list").hide();
				//返回字段设定
				$(".label-set .nav-tabs").find("li.field-platformsku").css("display","table-cell");
				thead.find("th").each(function(){
					var index=$(this).index(),
						itemForm=$("#fieldset-platformsku .form-group").eq(index);
					if($(this).hasClass("dis-none")){
						itemForm.find("input[type='checkbox']").attr("checked",false);
						itemForm.find("input.form-control").attr("disabled",true).val($(this).find("span").text());
					}else{
						itemForm.find("input[type='checkbox']").attr("checked",true);
						itemForm.find("input.form-control").attr("disabled",false).val($(this).find("span").text());
					}
				});
			break;
			case "invoiceskulist":
				$(".label-set").find(".panel-toolbar-wrapper").show();
				$(".label-set .edit-btn .btn-copy").addClass("disabled");
				//获取目标css
				var table=obj.find(".skulist-table"),
					thead=table.find("thead"),
					tbody=table.find("tbody"),
					tfoot=table.find("tfoot");
				//返回表格设定
				$(".label-set .nav-tabs").find("li.table").css("display","table-cell").addClass("active");
				$("#tableset").addClass("active");
				table.hasClass("no-tdborder")?$("#viewTdBorder").attr("checked",false):$("#viewTdBorder").attr("checked",true);
				thead.is(":hidden")?$("#viewThead").attr("checked",false).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",true):$("#viewThead").attr("checked",true).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",false);
				$("#theadFontFamily").val(thead.css("font-family"));
				$("#theadFontSize").val(thead.css("font-size"));
				$("#tbodyFontFamily").val(tbody.css("font-family"));
				$("#tbodyFontSize").val(tbody.css("font-size"));
				tfoot.is(":hidden")?$("#viewTfoot").attr("checked",false).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",true):$("#viewTfoot").attr("checked",true).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",false);
				$("#tfootFontFamily").val(tfoot.css("font-family"));
				$("#tfootFontSize").val(tfoot.css("font-size"));
				//$("#tfootAlign").val(tfoot.css("textAlign"));
				$(".product-list").hide();
				//返回字段设定
				$(".label-set .nav-tabs").find("li.field-invoiceskulist").css("display","table-cell");
				thead.find("th").each(function(){
					var index=$(this).index(),
						itemForm=$("#fieldset-invoiceskulist .form-group").eq(index);
					if($(this).hasClass("dis-none")){
						itemForm.find("input[type='checkbox']").attr("checked",false);
						itemForm.find("input.form-control").attr("disabled",true).val($(this).find("span").text());
					}else{
						itemForm.find("input[type='checkbox']").attr("checked",true);
						itemForm.find("input.form-control").attr("disabled",false).val($(this).find("span").text());
					}
				});
				break;
			//报关物品
			case "declarelist":
				$(".label-set").find(".panel-toolbar-wrapper").show();
				$(".label-set .edit-btn .btn-copy").addClass("disabled");
				//获取目标css
				var table=obj.find(".skulist-table.declare"),
					thead=table.find("thead"),
					tbody=table.find("tbody"),
					tfoot=table.find("tfoot");
				//返回表格设定
				$(".label-set .nav-tabs").find("li.table").css("display","table-cell").addClass("active");
				$("#tableset").addClass("active");
				table.hasClass("no-tdborder")?$("#viewTdBorder").attr("checked",false):$("#viewTdBorder").attr("checked",true);
				thead.is(":hidden")?$("#viewThead").attr("checked",false).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",true):$("#viewThead").attr("checked",true).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",false);
				$("#theadFontFamily").val(thead.css("font-family"));
				$("#theadFontSize").val(thead.css("font-size"));
				$("#tbodyFontFamily").val(tbody.css("font-family"));
				$("#tbodyFontSize").val(tbody.css("font-size"));
				tfoot.is(":hidden")?$("#viewTfoot").attr("checked",false).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",true):$("#viewTfoot").attr("checked",true).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",false);
				$("#tfootFontFamily").val(tfoot.css("font-family"));
				$("#tfootFontSize").val(tfoot.css("font-size"));
				//$("#tfootAlign").val(tfoot.css("textAlign"));
				$(".product-list").hide();
				//返回字段设定
				$(".label-set .nav-tabs").find("li.field-declare").css("display","table-cell");
				$("#declareNameTitle").val(thead.find(".name_declare span").html().replace(/<br>/g,"\n"));
				$("#declareWeightTitle").val(thead.find(".weight_declare span").html().replace(/<br>/g,"\n"));
				$("#declarePriceTitle").val(thead.find(".price_declare span").html().replace(/<br>/g,"\n"));
				$("#declareOriginTitle").val(tfoot.find("th.origin_declare span").html().replace(/<br>/g,"\n"));
				$("#declareTotalWeightTitle").val(tfoot.find("th.weight_declare span").html().replace(/<br>/g,"\n"));
				$("#declareTotalPriceTitle").val(tfoot.find("th.price_declare span").html().replace(/<br>/g,"\n"));
				$("#declareOriginCountryCode").val(tfoot.find(".countrycode_declare span").html().replace(/<br>/g,"\n"));
				
				if(tfoot.children("tr").first().hasClass("dis-none")){
					$("#viewTfootTitle").attr("checked",false);
					$("#fieldset-declare .declare-tfoot textarea").attr("disabled",true);
				}else{
					$("#viewTfootTitle").attr("checked",true);
					$("#fieldset-declare .declare-tfoot textarea").attr("disabled",false);
				};
				
				$("#declareNameCustom").val(tbody.find(".custom span").html().replace(/<br>/g,"\n"));
				var nameClass=tbody.find(".name_declare:not('.dis-none')").attr("class"),
					nameNum=nameClass.length,
					typeName=nameClass.indexOf(" ") + 1;
				$("#declareType").val(nameClass.substr(typeName, nameNum));
				$("#declareName").find("."+nameClass.substr(typeName, nameNum)).show().siblings(".multiple").hide();

				tbody.find(".name_declare.sort").children("span").children("span").each(function(){
					if($(this).hasClass("dis-none")){
						var classArr = $(this).attr("class").split(" ");
						$("input[name='sorttype'][value='"+ classArr[0] +"']").prop("checked",false);
					}else{
						console.log($(this).attr("class"));
						$("input[name='sorttype'][value='"+ $(this).attr("class") +"']").prop("checked",true);
					}
				});
				tbody.find(".name_declare.group").children("span").children("span").each(function(){
					if($(this).hasClass("dis-none")){
						var classArr = $(this).attr("class").split(" ");
						$("input[name='catalogue'][value='"+ classArr[0] +"']").prop("checked",false);
					}else{
						$("input[name='catalogue'][value='"+ $(this).attr("class") +"']").prop("checked",true);
					}
				});
				break;
			//报关物品(手动报关)
			case "manualdeclare":
				$(".label-set").find(".panel-toolbar-wrapper").show();
				$(".label-set .edit-btn .btn-copy").addClass("disabled");
				//获取目标css
				var table=obj.find(".skulist-table.manualdeclare"),
					thead=table.find("thead"),
					tbody=table.find("tbody"),
					tfoot=table.find("tfoot");
				//返回表格设定
				$(".label-set .nav-tabs").find("li.table").css("display","table-cell").addClass("active");
				$("#tableset").addClass("active");
				table.hasClass("no-tdborder")?$("#viewTdBorder").attr("checked",false):$("#viewTdBorder").attr("checked",true);
				thead.is(":hidden")?$("#viewThead").attr("checked",false).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",true):$("#viewThead").attr("checked",true).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",false);
				$("#theadFontFamily").val(thead.css("font-family"));
				$("#theadFontSize").val(thead.css("font-size"));
				$("#tbodyFontFamily").val(tbody.css("font-family"));
				$("#tbodyFontSize").val(tbody.css("font-size"));
				tfoot.is(":hidden")?$("#viewTfoot").attr("checked",false).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",true):$("#viewTfoot").attr("checked",true).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",false);
				$("#tfootFontFamily").val(tfoot.css("font-family"));
				$("#tfootFontSize").val(tfoot.css("font-size"));
				//$("#tfootAlign").val(tfoot.css("textAlign"));
				$(".product-list").hide();
				//返回字段设定
				$(".label-set .nav-tabs").find("li.field-declare").css("display","table-cell");
				$("#declareNameTitle").val(thead.find(".name_declare span").html().replace(/<br>/g,"\n"));
				$("#declareWeightTitle").val(thead.find(".weight_declare span").html().replace(/<br>/g,"\n"));
				$("#declarePriceTitle").val(thead.find(".price_declare span").html().replace(/<br>/g,"\n"));
				$("#declareOriginTitle").val(tfoot.find("th.origin_declare span").html().replace(/<br>/g,"\n"));
				$("#declareTotalWeightTitle").val(tfoot.find("th.weight_declare span").html().replace(/<br>/g,"\n"));
				$("#declareTotalPriceTitle").val(tfoot.find("th.price_declare span").html().replace(/<br>/g,"\n"));
				$("#declareOriginCountryCode").val(tfoot.find(".countrycode_declare span").html().replace(/<br>/g,"\n"));

				if(tfoot.children("tr").first().hasClass("dis-none")){
					$("#viewTfootTitle").attr("checked",false);
					$("#fieldset-declare .declare-tfoot textarea").attr("disabled",true);
				}else{
					$("#viewTfootTitle").attr("checked",true);
					$("#fieldset-declare .declare-tfoot textarea").attr("disabled",false);
				};
				if(tbody.find(".custom span").html()){
					$("#declareNameCustom").val(tbody.find(".custom span").html().replace(/<br>/g,"\n"));
				}
				var nameClass=tbody.find(".name_declare:not('.dis-none')").attr("class"),
					nameNum=nameClass.length,
					typeName=nameClass.indexOf(" ") + 1;
				$("#declareType").val(nameClass.substr(typeName, nameNum));
				$("#declareName").find("."+nameClass.substr(typeName, nameNum)).show().siblings(".multiple").hide();

				tbody.find(".name_declare.sort").children("span").children("span").each(function(){
					if($(this).hasClass("dis-none")){
						var classArr = $(this).attr("class").split(" ");
						console.log(classArr);
						$("input[name='sorttype'][value='"+ classArr[0] +"']").prop("checked",false);
					}else{
						console.log($(this).attr("class"));
						$("input[name='sorttype'][value='"+ $(this).attr("class") +"']").prop("checked",true);
					}
				});
				tbody.find(".name_declare.group").children("span").children("span").each(function(){
					if($(this).hasClass("dis-none")){
						var classArr = $(this).attr("class").split(" ");
						$("input[name='catalogue'][value='"+ classArr[0] +"']").prop("checked",false);
					}else{
						$("input[name='catalogue'][value='"+ $(this).attr("class") +"']").prop("checked",true);
					}
				});
				break;
			 
			//EUB报关物品
			case "eubdeclarelist":
				$(".label-set").find(".panel-toolbar-wrapper").show();
				$(".label-set .edit-btn .btn-copy").addClass("disabled");
				//获取目标css
				var table=obj.find(".skulist-table.eubdeclare"),
					thead=table.find("thead"),
					tbody=table.find("tbody"),
					tfoot=table.find("tfoot");
				//返回表格设定
				$(".label-set .nav-tabs").find("li.table").css("display","table-cell").addClass("active");
				$("#tableset").addClass("active");
				table.hasClass("no-tdborder")?$("#viewTdBorder").attr("checked",false):$("#viewTdBorder").attr("checked",true);
				thead.is(":hidden")?$("#viewThead").attr("checked",false).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",true):$("#viewThead").attr("checked",true).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",false);
				$("#theadFontFamily").val(thead.css("font-family"));
				$("#theadFontSize").val(thead.css("font-size"));
				$("#tbodyFontFamily").val(tbody.css("font-family"));
				$("#tbodyFontSize").val(tbody.css("font-size"));
				tfoot.is(":hidden")?$("#viewTfoot").attr("checked",false).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",true):$("#viewTfoot").attr("checked",true).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",false);
				$("#tfootFontFamily").val(tfoot.css("font-family"));
				$("#tfootFontSize").val(tfoot.css("font-size"));
				//$("#tfootAlign").val(tfoot.css("textAlign"));
				$(".product-list").hide();
				//返回字段设定
				$(".label-set .nav-tabs").find("li.field-eubdeclare").css("display","table-cell");
				$("#eubDeclareNoTitle").val(thead.find(".no_declare span").html().replace(/<br>/g,"\n"));
				$("#eubDeclareQtyTitle").val(thead.find(".qty_declare span").html().replace(/<br>/g,"\n"));
				$("#eubDeclareNameTitle").val(thead.find(".name_declare span").html().replace(/<br>/g,"\n"));
				$("#eubDeclareWeightTitle").val(thead.find(".weight_declare span").html().replace(/<br>/g,"\n"));
				$("#eubDeclarePriceTitle").val(thead.find(".price_declare span").html().replace(/<br>/g,"\n"));
				$("#eubDeclareOriginTitle").val(thead.find("th.origin_declare span").html().replace(/<br>/g,"\n"));
				$("#eubDeclareTotalWeightTitle").val(tfoot.find("th.origin_declare span").html().replace(/<br>/g,"\n"));
				
				$("#eubDeclareNameCustom").val(tbody.find(".custom span").html().replace(/<br>/g,"\n"));
				var nameClass=tbody.find(".name_declare:not('.dis-none')").attr("class"),
					nameNum=nameClass.length,
					typeName=nameClass.indexOf(" ") + 1;
				$("#eubDeclareType").val(nameClass.substr(typeName, nameNum));
				$("#eubDeclareName").find("."+nameClass.substr(typeName, nameNum)).show().siblings(".multiple").hide();
				tbody.find(".name_declare.sort").children("span").children("span").each(function(){
					if($(this).hasClass("dis-none")){
						var classArr = $(this).attr("class").split(" ");
						console.log(classArr);
						$("input[name='eubsorttype'][value='"+ classArr[0] +"']").prop("checked",false);
					}else{
						console.log($(this).attr("class"));
						$("input[name='eubsorttype'][value='"+ $(this).attr("class") +"']").prop("checked",true);
					}
				});
				tbody.find(".name_declare.group").children("span").children("span").each(function(){
					if($(this).hasClass("dis-none")){
						var classArr = $(this).attr("class").split(" ");
						$("input[name='catalogue'][value='"+ classArr[0] +"']").prop("checked",false);
					}else{
						$("input[name='catalogue'][value='"+ $(this).attr("class") +"']").prop("checked",true);
					}
				});
				break;
			case "manualeubdeclarelist":
				$(".label-set").find(".panel-toolbar-wrapper").show();
				$(".label-set .edit-btn .btn-copy").addClass("disabled");
				//获取目标css
				var table=obj.find(".skulist-table.manualeubdeclarelist"),
					thead=table.find("thead"),
					tbody=table.find("tbody"),
					tfoot=table.find("tfoot");
				//返回表格设定
				$(".label-set .nav-tabs").find("li.table").css("display","table-cell").addClass("active");
				$("#tableset").addClass("active");
				table.hasClass("no-tdborder")?$("#viewTdBorder").attr("checked",false):$("#viewTdBorder").attr("checked",true);
				thead.is(":hidden")?$("#viewThead").attr("checked",false).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",true):$("#viewThead").attr("checked",true).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",false);
				$("#theadFontFamily").val(thead.css("font-family"));
				$("#theadFontSize").val(thead.css("font-size"));
				$("#tbodyFontFamily").val(tbody.css("font-family"));
				$("#tbodyFontSize").val(tbody.css("font-size"));
				tfoot.is(":hidden")?$("#viewTfoot").attr("checked",false).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",true):$("#viewTfoot").attr("checked",true).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",false);
				$("#tfootFontFamily").val(tfoot.css("font-family"));
				$("#tfootFontSize").val(tfoot.css("font-size"));
				$(".product-list").hide();
				//返回字段设定
				$(".label-set .nav-tabs").find("li.field-eubdeclare").css("display","table-cell");
				$("#eubDeclareNoTitle").val(thead.find(".no_declare span").html().replace(/<br>/g,"\n"));
				$("#eubDeclareQtyTitle").val(thead.find(".qty_declare span").html().replace(/<br>/g,"\n"));
				$("#eubDeclareNameTitle").val(thead.find(".name_declare span").html().replace(/<br>/g,"\n"));
				$("#eubDeclareWeightTitle").val(thead.find(".weight_declare span").html().replace(/<br>/g,"\n"));
				$("#eubDeclarePriceTitle").val(thead.find(".price_declare span").html().replace(/<br>/g,"\n"));
				$("#eubDeclareOriginTitle").val(thead.find("th.origin_declare span").html().replace(/<br>/g,"\n"));
				$("#eubDeclareTotalWeightTitle").val(tfoot.find("th.origin_declare span").html().replace(/<br>/g,"\n"));
				
				$("#eubDeclareNameCustom").val(tbody.find(".custom span").html().replace(/<br>/g,"\n"));
				var nameClass=tbody.find(".name_declare:not('.dis-none')").attr("class"),
					nameNum=nameClass.length,
					typeName=nameClass.indexOf(" ") + 1;
				$("#eubDeclareType").val(nameClass.substr(typeName, nameNum));
				$("#eubDeclareName").find("."+nameClass.substr(typeName, nameNum)).show().siblings(".multiple").hide();
				tbody.find(".name_declare.sort").children("span").children("span").each(function(){
					if($(this).hasClass("dis-none")){
						var classArr = $(this).attr("class").split(" ");
						console.log(classArr);
						$("input[name='eubsorttype'][value='"+ classArr[0] +"']").prop("checked",false);
					}else{
						console.log($(this).attr("class"));
						$("input[name='eubsorttype'][value='"+ $(this).attr("class") +"']").prop("checked",true);
					}
				});
				tbody.find(".name_declare.group").children("span").children("span").each(function(){
					if($(this).hasClass("dis-none")){
						var classArr = $(this).attr("class").split(" ");
						$("input[name='catalogue'][value='"+ classArr[0] +"']").prop("checked",false);
					}else{
						$("input[name='catalogue'][value='"+ $(this).attr("class") +"']").prop("checked",true);
					}
				});
				break;

			//单行EUB报关物品
			case "eubdeclarelistSingleRow":
				$(".label-set").find(".panel-toolbar-wrapper").show();
				$(".label-set .edit-btn .btn-copy").addClass("disabled");
				//获取目标css
				var table=obj.find(".skulist-table.eubdeclaresingle"),
					thead=table.find("thead"),
					tbody=table.find("tbody"),
					tfoot=table.find("tfoot");
				//返回表格设定
				$(".label-set .nav-tabs").find("li.table").css("display","table-cell").addClass("active");
				$("#tableset").addClass("active");
				table.hasClass("no-tdborder")?$("#viewTdBorder").attr("checked",false):$("#viewTdBorder").attr("checked",true);
				thead.is(":hidden")?$("#viewThead").attr("checked",false).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",true):$("#viewThead").attr("checked",true).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",false);
				$("#theadFontFamily").val(thead.css("font-family"));
				$("#theadFontSize").val(thead.css("font-size"));
				$("#tbodyFontFamily").val(tbody.css("font-family"));
				$("#tbodyFontSize").val(tbody.css("font-size"));
				$("#viewTfoot").attr("checked",false).attr("disabled",true).parents(".form-group").next("	.moreinfo").find("input,select").attr("disabled",true);
				$(".product-list").hide();
				//返回字段设定
				$(".label-set .nav-tabs").find("li.field-eubdeclaresingle").css("display","table-cell");
				$("#eubDeclareSingleNoTitle").val(thead.find(".no_declare span").html().replace(/<br>/g,"\n"));
				$("#eubDeclareSingleQtyTitle").val(thead.find(".qty_declare span").html().replace(/<br>/g,"\n"));
				$("#eubDeclareSingleNameTitle").val(thead.find(".name_declare span").html().replace(/<br>/g,"\n"));
				$("#eubDeclareSingleWeightTitle").val(thead.find(".weight_declare span").html().replace(/<br>/g,"\n"));
				$("#eubDeclareSinglePriceTitle").val(thead.find(".price_declare span").html().replace(/<br>/g,"\n"));
				$("#eubDeclareSingleOriginTitle").val(thead.find("th.origin_declare span").html().replace(/<br>/g,"\n"));
				$("#eubDeclareSingleCustomContent").val(tbody.find(".name_declare .custom").html().replace(/<br>/g,"\n"));

				if(tbody.find(".name_declare").children(".custom").hasClass("hide")){
					$("#eubDeclareSingleCustom").prop("checked",false);
					$("#eubDeclareSingleCustomContent").attr("disabled",true);
					$("input[name='eubDeclareSingleSort']").attr("disabled",false);
				}else{
					$("#eubDeclareSingleCustom").prop("checked",true);
					$("#eubDeclareSingleCustomContent").attr("disabled",false);
					$("input[name='eubDeclareSingleSort']").attr("disabled",true);
				}

				tbody.find(".name_declare").children(".sort").children("span").each(function(){
					if($(this).hasClass("dis-none")){
						var classArr = $(this).attr("class").split(" ");
						$("input[name='eubDeclareSingleSort'][value='"+ classArr[0] +"']").prop("checked",false);
					}else{
						$("input[name='eubDeclareSingleSort'][value='"+ $(this).attr("class") +"']").prop("checked",true);
					}
				});
				tbody.find(".name_declare").children(".group").children("span").each(function(){
					if($(this).hasClass("dis-none")){
						var classArr = $(this).attr("class").split(" ");
						$("input[name='eubDeclareSingleType'][value='"+ classArr[0] +"']").prop("checked",false);
					}else{
						$("input[name='eubDeclareSingleType'][value='"+ $(this).attr("class") +"']").prop("checked",true);
					}
				});
				break;
			//配货兼报关表格
			case "declareskulist":
				$(".label-set").find(".panel-toolbar-wrapper").show();
				$(".label-set .edit-btn .btn-copy").addClass("disabled");
				//获取目标css
				var table=obj.find(".skulist-table.declareSkuType"),
					thead=table.find("thead"),
					tbody=table.find("tbody"),
					tfoot=table.find("tfoot");
				//返回表格设定
				$(".label-set .nav-tabs").find("li.table").css("display","table-cell").addClass("active");
				$("#tableset").addClass("active");
				table.hasClass("no-tdborder")?$("#viewTdBorder").attr("checked",false):$("#viewTdBorder").attr("checked",true);
				thead.is(":hidden")?$("#viewThead").attr("checked",false).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",true):$("#viewThead").attr("checked",true).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",false);
				$("#theadFontFamily").val(thead.css("font-family"));
				$("#theadFontSize").val(thead.css("font-size"));
				$("#tbodyFontFamily").val(tbody.css("font-family"));
				$("#tbodyFontSize").val(tbody.css("font-size"));
				tfoot.is(":hidden")?$("#viewTfoot").attr("checked",false).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",true):$("#viewTfoot").attr("checked",true).parent(".form-group").next(".moreinfo").find("input,select").attr("disabled",false);
				$("#tfootFontFamily").val(tfoot.css("font-family"));
				$("#tfootFontSize").val(tfoot.css("font-size"));
				$(".product-list").hide();
				//返回字段设定
				$(".label-set .nav-tabs").find("li.field-declareSku").css("display","table-cell");
				$("#declareSkuNameTitle").val(thead.find(".name_declare span").html().replace(/<br>/g,"\n"));
				$("#declareSkuWeightTitle").val(thead.find(".weight_declare span").html().replace(/<br>/g,"\n"));
				$("#declareSkuPriceTitle").val(thead.find(".price_declare span").html().replace(/<br>/g,"\n"));
				$("#declareSkuOriginTitle").val(tfoot.find("th.origin_declare span").html().replace(/<br>/g,"\n"));
				
				tbody.children("tr").first().children(".name_declare").children("span").children("span").each(function(){
					if($(this).hasClass("dis-none")){
						$("input[name='declareSkuSorttype'][value='"+ $(this).attr("class") +"']").prop("checked",false);
					}else{
						$("input[name='declareSkuSorttype'][value='"+ $(this).attr("class") +"']").prop("checked",true);
					}
				});
			break;
			
			//配货商品字段组合
			case "itemlist":
				$(".label-set").find(".panel-toolbar-wrapper").show();
				$(".label-set .edit-btn .btn-copy").removeClass("disabled");
				//获取目标css
				var itemDetail=obj.find(".detail");
				//返回内容设定
				$(".label-set .nav-tabs").find("li.detail").css("display","table-cell").addClass("active");
				$("#detailset").addClass("active");
				$("#detailAlign").val(itemDetail.css("textAlign"));
				$("#detailFontFamily").val(itemDetail.css("font-family"));
				$("#detailFontSize").val(itemDetail.css("font-size"));
				$("#detailLineHeight").val(Math.round(parseInt(itemDetail.css("lineHeight"))/parseInt(itemDetail.css("font-size"))*10)/10);
				$(".product-list").show();
				$(".sortType").val((itemDetail.find("em").attr('data-sort') ? itemDetail.find("em").attr('data-sort') : 'default'));
				$(".mergeSKU").val((itemDetail.find("em").attr('data-merge-sku') ? itemDetail.find("em").attr('data-merge-sku') : 'default'));
				itemDetail.css("fontWeight")>="700"?$("#detailFontWeight").attr("checked",true):$("#detailFontWeight").attr("checked",false);
				//返回字段设定
				$(".label-set .nav-tabs").find("li.field-item").css("display","table-cell");
				itemDetail.find("span").each(function(){
					var	className=$(this).attr("class"),
						itemForm=$("#fieldset-item .form-group.multiple");
					if($(this).css("display")=="none"){
						itemForm.find("input[type='checkbox'][value='"+className+"']").attr("checked",false);
					}else{
						itemForm.find("input[type='checkbox'][value='"+className+"']").attr("checked",true);
					}
				});
				itemDetail.hasClass("block")?$("#newItemline").attr("checked",true):$("#newItemline").attr("checked",false);
				//返回边框设定
				setBorder(obj);
			break;
			//自定义文字
			case "customtext":
				$(".label-set").find(".panel-toolbar-wrapper").show();
				$(".label-set .edit-btn .btn-copy").removeClass("disabled");
				//获取目标css
				var textDetail=obj.find(".detail"),
					textData = textDetail.html().replace(/<br>/g,"\n"),
					checkBox=obj.find("i"),
					textLineHeight=parseInt(obj.css("lineHeight"))/parseInt(obj.css("font-size"));
				//返回文字设定
				$(".label-set .nav-tabs").find("li.text").css("display","table-cell").addClass("active");
				$("#textset").addClass("active");
				$("#textDetail").val(textData);
				$("#textAlign").val(obj.css("textAlign"));
				$("#textFontFamily").val(textDetail.css("font-family"));
				$("#textFontSize").val(obj.css("font-size"));
				$("#textLineHeight").val(Math.round(parseInt(obj.css("lineHeight"))/parseInt(obj.css("font-size"))*10)/10);
				textDetail.css("fontWeight")>="700"?$("#textFontWeight").attr("checked",true):$("#textFontWeight").attr("checked",false);
				if(checkBox.css("display")=="none"){
					$("#textCheckBox").attr("checked",false).parents(".form-group").next(".form-group").hide();
				}else{
					$("#textCheckBox").attr("checked",true).parents(".form-group").next(".form-group").show();
					$("#checkBoxType").val(checkBox.attr("class"));
				}
				//返回边框设定
				setBorder(obj);
			break;
			//水平线条
			case "line-x":
				$(".label-set").find(".panel-toolbar-wrapper").hide();
				$(".label-set .edit-btn .btn-copy").removeClass("disabled");
				//返回水平线设置
				$("#line-x").addClass("active");
				$("#xLineStyle").val(obj.css("border-top-style"));
				$("#xLineWeight").val(obj.css("border-top-width"));
				$("#xLineWidth").val(parseInt(obj.css("width")));
			break;
			//垂直线条
			case "line-y":
				$(".label-set").find(".panel-toolbar-wrapper").hide();
				$(".label-set .edit-btn .btn-copy").removeClass("disabled");
				//返回垂直线设置
				$("#line-y").addClass("active");
				$("#yLineStyle").val(obj.css("border-left-style"));
				$("#yLineWeight").val(obj.css("border-left-width"));
				$("#yLineWidth").val(parseInt(obj.css("height")));
			break;
			//圆框文字
			case "circletext":
			
				$(".label-set").find(".panel-toolbar-wrapper").hide();
				$(".label-set .edit-btn .btn-copy").removeClass("disabled");
				var textDetail=obj.find(".detail");
				$("#circletext").addClass("active");
				$("#circleBorderWidth").val(obj.css("border-top-width"));
				$("#circleText").text(textDetail.text());
				$("#circleFontFamily").val(textDetail.css("font-family"));
				$("#circleFontSize").val(textDetail.css("font-size"));
				textDetail.css("fontWeight")>="700"?$("#circleFontWeight").attr("checked",true):$("#circleFontWeight").attr("checked",false);
			break;
			//在线图片
			case "onlineimage":
				$(".label-set").find(".panel-toolbar-wrapper").show();
				$(".label-set .edit-btn .btn-copy").removeClass("disabled");
				//获取目标css
				var image=obj.find("img");
				//返回图片路径设定
				$(".label-set .nav-tabs").find("li.imgurl").css("display","table-cell").addClass("active");
				$("#imageurl").addClass("active");
				
				image.attr("src")=="/printTemp/img/photo_default.jpg"?$("#imageUrl").val(""):$("#imageUrl").val(image.attr("src"));
				//返回边框设定
				setBorder(obj);
			break;
			//本地上传图片
			case "uploadimage":
				$(".label-set").find(".panel-toolbar-wrapper").show();
				$(".label-set .edit-btn .btn-copy").removeClass("disabled");
				//获取目标css
				var image=obj.find("img");
				//返回图片路径设定
				$(".label-set .nav-tabs").find("li.img-upload").css("display","table-cell").addClass("active");
				$("#imageupload").addClass("active");
				$("#previewImage").attr("src",image.attr("src"));
				$("#modelfile").val(image.attr("src"));
				//返回边框设定
				setBorder(obj);
			break;
			//图片
			case "image":
			case "productImg":
				$(".label-set").find(".panel-toolbar-wrapper").hide();
				$(".label-set .edit-btn .btn-copy").removeClass("disabled");
				//返回边框设定
				$("#borderset").addClass("active");
				setBorder(obj);
			break;
			//FBA标题
			case "fbatext":
				$(".label-set").find(".panel-toolbar-wrapper").show();
				$(".label-set .edit-btn .btn-copy").removeClass("disabled");
				//返回边框设定
				$("#borderset").addClass("active");
				setBorder(obj);
				break;
			case "qRcode":
				$(".label-set").find(".panel-toolbar-wrapper").hide();
				$(".label-set .edit-btn .btn-copy").addClass("disabled");
				//返回条码设置
				var codeNum=obj.find(".codemunber");
				$("#qRcode").addClass("active");
				$("#qRcodeTextType").hide();
				$("#qRcodeType").val(obj.attr("data-code-type"));
				if(codeNum.css("display")=="none"){
					$("#qRviewCodeNum").attr("checked",false);$("#qRcode .codenum-set").find("input,select").attr("disabled",true);
				}else{
					$("#qRviewCodeNum").attr("checked",true);$("#qRcode .codenum-set").find("input,select").attr("disabled",false);
				};
				$("#qRcodeNumAlign").val(codeNum.css("textAlign"));
				$("#qRcodeNumFontSize").val(codeNum.css("font-size"));
				codeNum.css("fontWeight")>="700"?$("#qRcodeNumFontWeight").attr("checked",true):$("#qRcodeNumFontWeight").attr("checked",false);
				$("#qRcodePrefix").val(codeNum.find(".prefix").text());
				break;
		}
	};
	//返回边框设定
	function setBorder(obj){
		$(".label-set .nav-tabs").find("li.border").css("display","table-cell");
		if(parseInt(obj.css("border-top-width"))==0){
			$("#borderTop").attr("checked",false).parents(".form-group").find("select.form-control").attr("disabled",true);
		}else{
			$("#borderTop").attr("checked",true).parents(".form-group").find("select.form-control").attr("disabled",false);
		};
		$("#borderTopWidth").val(obj.css("border-top-width"));
		$("#paddingTop").val(obj.css("padding-top"));
		if(parseInt(obj.css("border-bottom-width"))==0){
			$("#borderBottom").attr("checked",false).parents(".form-group").find("select.form-control").attr("disabled",true);
		}else{
			$("#borderBottom").attr("checked",true).parents(".form-group").find("select.form-control").attr("disabled",false);
		};
		$("#borderBottomWidth").val(obj.css("border-bottom-width"));
		$("#paddingBottom").val(obj.css("padding-bottom"));
		if(parseInt(obj.css("border-left-width"))==0){
			$("#borderLeft").attr("checked",false).parents(".form-group").find("select.form-control").attr("disabled",true);
		}else{
			$("#borderLeft").attr("checked",true).parents(".form-group").find("select.form-control").attr("disabled",false);
		};
		$("#borderLeftWidth").val(obj.css("border-left-width"));
		$("#paddingLeft").val(obj.css("padding-left"));
		if(parseInt(obj.css("border-right-width"))==0){
			$("#borderRight").attr("checked",false).parents(".form-group").find("select.form-control").attr("disabled",true);
		}else{
			$("#borderRight").attr("checked",true).parents(".form-group").find("select.form-control").attr("disabled",false);
		};
		$("#borderRightWidth").val(obj.css("border-right-width"));
		$("#paddingRight").val(obj.css("padding-right"));
	}
	//标签点击
	$(".custom-content").on("click",".dropitem",function(){
		tempItem=$(this);
		$(".label-set").addClass("opened");
		$(".custom-label").addClass("haslabelset");
		$(".hotkey-panel").css({"opacity":"1","z-index":"3"});
		dropCreate($(this));
		getTempSet($(this));
		return tempItem;
	});
	
	//=====================属性设置参数=====================
	
	//========标题修改=========
	//是否显示标题
	$("#viewTitle").on("click",function(){
		if($(this).attr("checked")){
			tempItem.find(".title").css("display","");
			$("#titelset .title-set").find("input,select").attr("disabled",false);
		}else{
			tempItem.find(".title").css("display","none");
			$("#titelset .title-set").find("input,select").attr("disabled",true);
		}
	});
	//标题整行显示
	$("#titleNowrap").on("click",function(){
		if($(this).attr("checked")){
			$(this).parents(".form-group").next(".moreinfo").show();
			tempItem.find(".title").css("display","block")
		}else {
			$(this).parents(".form-group").next(".moreinfo").hide();
			tempItem.find(".title").css("display","");
		};
	});
	//修改标题对齐方式
	$("#titleAlign").on("change",function(){
		tempItem.find(".title").css("textAlign",$(this).val());
	});
	
	//修改标题与内容间距
	$("#titlePaddingBottom").on("change",function(){
		tempItem.find(".title").css("paddingBottom",$(this).val()+"px");
	});
	
	//修改标题文本
	$("#titleName").on("keyup",function(){
		tempItem.find(".title").text($(this).val());
	});
	//修改标题文字字体
	$("#titleFontFamily").on("change",function(){
		tempItem.find(".title").css("font-family",$(this).val());
	});
	//修改标题文字尺寸
	$("#titleFontSize").on("change",function(){
		if(parseInt($(this).val())<="11"||parseInt($("#detailFontSize").val())<="11")$(".label-set .group-warning").show();
		else $(".label-set .group-warning").hide();
		tempItem.find(".title").css("font-size",$(this).val());
		tempItem.find(".title").css("lineHeight",parseInt($(this).val())*$("#titleLineHeight").val()+"px");
	});
	//修改标题文字行距
	$("#titleLineHeight").on("change",function(){
		tempItem.find(".title").css("lineHeight",$(this).val()*parseInt($("#titleFontSize").val())+"px");
	});
	//标题文字是否加粗
	$("#titleFontWeight").on("click",function(){
		$(this).attr("checked")?tempItem.find(".title").css("fontWeight","700"):tempItem.find(".title").css("fontWeight","400");
	});
	
	//========内容修改=========
	//修改内容对齐方式
	$("#detailAlign").on("change",function(){
		tempItem.css("textAlign",$(this).val());
	});
	//修改内容文字字体
	$("#detailFontFamily").on("change",function(){
		tempItem.find(".detail").css("font-family",$(this).val());
	});
	//修改内容文字尺寸
	$("#detailFontSize").on("change",function(){
		if(parseInt($(this).val())<="11"||parseInt($("#titleFontSize").val())<="11")$(".label-set .group-warning").show();
		else $(".label-set .group-warning").hide();
		tempItem.find(".detail").css("font-size",$(this).val());
		tempItem.find(".detail").css("lineHeight",parseInt($(this).val())*$("#detailLineHeight").val()+"px");
	});
	//修改内容文字行距
	$("#detailLineHeight").on("change",function(){
		tempItem.find(".detail").css("lineHeight",$(this).val()*parseInt($("#detailFontSize").val())+"px");
	});
	//内容文字是否加粗
	$("#detailFontWeight").on("click",function(){
		$(this).attr("checked")?tempItem.find(".detail").css("fontWeight","700"):tempItem.find(".detail").css("fontWeight","400");
	});
	//邮编默认大写
	$("#postUpper").on("click",function(){
		if(tempItem.find(".title").text()=="发件人邮编" || tempItem.find(".title").text()=="收件人邮编"){
			$(this).attr("checked")?tempItem.find(".detail").css("text-transform","Uppercase"):tempItem.find(".detail").css("text-transform","");
		}else{
			$(this).attr("checked")?tempItem.find(".postcode").css("text-transform","Uppercase"):tempItem.find(".postcode").css("text-transform","");
		}
	});
	//========日期字段修改=========
	//修改是否显示年份
	$("#viewYear").on("click",function(){
		if($(this).attr("checked")){
			tempItem.find(".detail").attr("data-year","true").text(getCurrentDate(true, $("#dateType").val()));
		}else{
			tempItem.find(".detail").attr("data-year","false").text(getCurrentDate(false, $("#dateType").val()));
		}
	});
	//修改分隔符
	$("#dateType").on("change",function(){
		var divide = $(this).val();
		var year = $("#viewYear").is(":checked") ? true : false;
		tempItem.find(".detail").attr("data-divide",divide).text(getCurrentDate(year, divide));
	});
	
	//修改内容对齐方式
	$("#dateAlign").on("change",function(){
		tempItem.css("textAlign",$(this).val());
	});
	//修改内容文字字体
	$("#dateFontFamily").on("change",function(){
		tempItem.find(".detail").css("font-family",$(this).val());
	});
	//修改内容文字尺寸
	$("#dateFontSize").on("change",function(){
		if(parseInt($(this).val())<="11"||parseInt($("#titleFontSize").val())<="11")$(".label-set .group-warning").show();
		else $(".label-set .group-warning").hide();
		tempItem.find(".detail").css("font-size",$(this).val());
		tempItem.find(".detail").css("lineHeight",parseInt($(this).val())*$("#detailLineHeight").val()+"px");
	});
	//修改内容文字行距
	$("#dateLineHeight").on("change",function(){
		tempItem.find(".detail").css("lineHeight",$(this).val()*parseInt($("#dateFontSize").val())+"px");
	});
	//内容文字是否加粗
	$("#dateFontWeight").on("click",function(){
		$(this).attr("checked")?tempItem.find(".detail").css("fontWeight","700"):tempItem.find(".detail").css("fontWeight","400");
	});
	//========组合地址字段修改=======
	//修改显示字段格式
	$("#fieldset-address .multiple input[type='checkbox']").on("click",function(){
		var cName=$(this).val(),
			viewField=$(this).parents(".multiple").find("input[type='checkbox']:checked").length;
		if($(this).attr("checked")){
			tempItem.find(".detail").find("."+cName).css("display","");
		}else{
			if(viewField==0){
				$.gritter.add({
					class_name:'gritter-error',
					title: "操作错误",
					text: "至少需勾选一项",
					time: 2000,
					sticky: false
				});
				return false;
			}else{
				tempItem.find(".detail").find("."+cName).css("display","none");
			}
		}
	});
	//是否显示中文国家名
	$("#fieldset-address .multiple input[name='viewField'][value='country']").on("click",function(){
		$(this).attr("checked")?$("#fieldset-address .multiple input[name='viewField'][value='country_cn']").attr("disabled",false):$("#fieldset-address .multiple input[name='viewField'][value='country_cn']").attr("disabled",true);
	});
	$("#fieldset-address .multiple input[name='viewField'][value='street']").on("click",function(){
		$(this).attr("checked")?$("#fieldset-address .multiple input[name='viewField'][value='doorcode']").attr("disabled",false):$("#fieldset-address .multiple input[name='viewField'][value='doorcode']").attr("disabled",true);
	});
	//单个字段是否整行显示
	$("#newline").on("click",function(){
		$(this).attr("checked")?tempItem.find(".detail").addClass("block"):tempItem.find(".detail").removeClass("block");
	});
	
	//========自定义文字修改========
	//修改自定义文本
	$("#textDetail").on("keyup",function(){
		var newText=$(this).val().replace(/\n/gi,"<br>");
		tempItem.find(".detail").html(newText);
	});
	$("#textDetail").on("blur",function(){
		if($(this).val().length<=0){
			$(this).val("自定义文本内容");
			tempItem.find(".detail").html("自定义文本内容");
		};
	});
	//文字对齐方式
	$("#textAlign").on("change",function(){
		tempItem.css("textAlign",$(this).val());
	});
	//修改自定义文本字体
	$("#textFontFamily").on("change",function(){
		tempItem.find(".detail").css("font-family",$(this).val());
	});
	//修改自定义文本文字尺寸
	$("#textFontSize").on("change",function(){
		parseInt($(this).val())<="11"?$(".label-set .group-warning").show():$(".label-set .group-warning").hide();
		tempItem.css("font-size",$(this).val());
		tempItem.css("lineHeight",parseInt($(this).val())*$("#textLineHeight").val()+"px");
	});
	//修改自定义文本行距
	$("#textLineHeight").on("change",function(){
		tempItem.css("lineHeight",$(this).val()*parseInt($("#textFontSize").val())+"px");
	});
	//自定义文本是否加粗
	$("#textFontWeight").on("click",function(){
		$(this).attr("checked")?tempItem.find(".detail").css("fontWeight","700"):tempItem.find(".detail").css("fontWeight","400");
	});
	//否是显示复选框
	$("#textCheckBox").on("click",function(){
		if($(this).attr("checked")){
			tempItem.find("i").show();
			$(this).parents(".form-group").next(".form-group").show();
		}else{
			tempItem.find("i").hide();
			$(this).parents(".form-group").next(".form-group").hide();
		}
	});
	//修改复选框勾选状态
// 			$("#checkBoxType").on("change",function(){
// 				tempItem.find("i").attr("class",$(this).val());
// 			});
	$("#checkBoxType").on("change",function(){
		tempItem.find(".ico-checkbox-unchecked2").attr("class",$(this).val());
	});
	
	//========边框修改=========
	//显示上边框
	$("#borderTop").on("click",function(){
		if($(this).attr("checked")){
			$(this).parents(".form-group").find("select.form-control").attr("disabled",false);
			tempItem.css("border-top-width",$("#paddingTop").val());
			if(parseInt($("#borderTopWidth").val())>=1){
				tempItem.css("border-top-width", $("#borderTopWidth").val());
			}else{
				tempItem.css("border-top-width", "1px");
				$("#borderTopWidth").val("1px");
			}
		}else {
			$(this).parents(".form-group").find("select.form-control").attr("disabled",true);
			tempItem.css({
				"border-top-width":"0px",
				"padding-top":"0px"
			});
			$("#borderTopWidth, #paddingTop").val("0px");
		};
	});
	//上边框厚度
	$("#borderTopWidth").on("change",function(){
		tempItem.css("border-top-width",$(this).val());
	});
	//上边距
	$("#paddingTop").on("change",function(){
		tempItem.css("padding-top",$(this).val());
	});
	//显示下边框
	$("#borderBottom").on("click",function(){
		if($(this).attr("checked")){
			$(this).parents(".form-group").find("select.form-control").attr("disabled",false);
			tempItem.css("border-bottom-width",$("#paddingBottom").val());
			if(parseInt($("#borderBottomWidth").val())>=1){
				tempItem.css("border-bottom-width", $("#borderBottomWidth").val());
			}else{
				tempItem.css("border-bottom-width", "1px");
				$("#borderBottomWidth").val("1px");
			}
		}else {
			$(this).parents(".form-group").find("select.form-control").attr("disabled",true);
			tempItem.css({
				"border-bottom-width":"0px",
				"padding-bottom":"0px"
			});
			$("#borderBottomWidth, #paddingBottom").val("0px");
		};
	});
	//下边框厚度
	$("#borderBottomWidth").on("change",function(){
		tempItem.css("border-bottom-width",$(this).val());
	});
	//下边距
	$("#paddingBottom").on("change",function(){
		tempItem.css("padding-bottom",$(this).val());
	});
	//显示左边框
	$("#borderLeft").on("click",function(){
		if($(this).attr("checked")){
			$(this).parents(".form-group").find("select.form-control").attr("disabled",false);
			tempItem.css("border-left-width",$("#paddingLeft").val());
			if(parseInt($("#borderLeftWidth").val())>=1){
				tempItem.css("border-left-width", $("#borderLeftWidth").val());
			}else{
				tempItem.css("border-left-width", "1px");
				$("#borderLeftWidth").val("1px");
			}
		}else {
			$(this).parents(".form-group").find("select.form-control").attr("disabled",true);
			tempItem.css({
				"border-left-width":"0px",
				"padding-left":"0px"
			});
			$("#borderLeftWidth, #paddingLeft").val("0px");
		};
	});
	//左边框厚度
	$("#borderLeftWidth").on("change",function(){
		tempItem.css("border-left-width",$(this).val());
	});
	//左边距
	$("#paddingLeft").on("change",function(){
		tempItem.css("padding-left",$(this).val());
	});
	//显示右边框
	$("#borderRight").on("click",function(){
		if($(this).attr("checked")){
			$(this).parents(".form-group").find("select.form-control").attr("disabled",false);
			tempItem.css("border-right-width",$("#paddingRight").val());
			if(parseInt($("#borderRightWidth").val())>=1){
				tempItem.css("border-right-width", $("#borderRightWidth").val());
			}else{
				tempItem.css("border-right-width", "1px");
				$("#borderRightWidth").val("1px");
			}
		}else {
			$(this).parents(".form-group").find("select.form-control").attr("disabled",true);
			tempItem.css({
				"border-right-width":"0px",
				"padding-right":"0px"
			});
			$("#borderRightWidth, #paddingRight").val("0px");
		};
	});
	//右边框厚度
	$("#borderRightWidth").on("change",function(){
		tempItem.css("border-right-width",$(this).val());
	});
	//右边距
	$("#paddingRight").on("change",function(){
		tempItem.css("padding-right",$(this).val());
	});
	
	//========水平线修改========
	//修改水平线宽度
	$("#xLineWidth").on("keyup",function(){
		tempItem.css("width",$(this).val()+"px");
	});
	
	$("#xLineWidth").on("blur",function(){
		var value=$(this).val(),
			leftWidth=parseInt(tempItem.css("left")),
			maxWidth=parseInt(tempItem.parent(".custom-drop").width())-leftWidth;
		if(value<=15){
			tempItem.css("width","15px");
			$(this).val("15");
		}else if(value>=maxWidth){
			tempItem.css("width",maxWidth+"px");
			$(this).val(maxWidth);
		}
	});
	//设为100%宽度
	$("#setMaxWidth").on("click",function(){
		var maxWidth=tempItem.parent(".custom-drop").width();
		tempItem.css({
			"width":maxWidth,
			"left":"0px"
		});
		$("#xLineWidth").val(parseInt(maxWidth));
	});
	
	//水平线数字输入框组件
	$("#line-x .customnum .add").on("click",function(){
		var input=$("#xLineWidth"),
			value=parseInt(input.val()),
			newValue=value*1+1,
			leftWidth=parseInt(tempItem.css("left")),
			maxWidth=parseInt(tempItem.parent(".custom-drop").width())-leftWidth;
		if(value<maxWidth){
			input.val(newValue);
			tempItem.css("width",newValue+"px");
		};
	});
	$("#line-x .customnum .subtract").on("click",function(){
		var input=$("#xLineWidth"),
			value=parseInt(input.val()),
			newValue=value*1-1;
		if(value>15){
			input.val(newValue);
			tempItem.css("width",newValue+"px");
		}
	});
	//修改水平线样式
	$("#xLineStyle").on("change",function(){
		tempItem.css("border-top-style",$(this).val());
	});
	//修改水平线厚度
	$("#xLineWeight").on("change",function(){
		tempItem.css({
			"border-top-width":$(this).val(),
			"height":$(this).val()
		});
	});
	
	//========垂直线修改========
	//修改垂直线高度
	$("#yLineHeight").on("keyup",function(){
		tempItem.css("height",$(this).val()+"px");
	});
	$("#yLineHeight").on("blur",function(){
		var value=$(this).val(),
			topHeight=parseInt(tempItem.css("top")),
			maxHeigh=parseInt(tempItem.parent(".custom-drop").height())-topHeight;
		if(value<=15){
			tempItem.css("height","15px");
			$(this).val("15");
		}else if(value>=maxHeigh){
			tempItem.css("height",maxHeigh+"px");
			$(this).val(maxHeigh);
		}
	});
	//设为100%宽度
	$("#setMaxHeight").on("click",function(){
		var maxHeight=tempItem.parent(".custom-drop").height();
		tempItem.css({
			"height":maxHeight,
			"top":"0px"
		});
		$("#yLineHeight").val(parseInt(maxHeight));
	});
	//垂直线数字输入框组件
	$("#line-y .customnum .add").on("click",function(){
		var input=$("#yLineHeight"),
			value=parseInt(input.val()),
			newValue=value*1+1,
			topHeight=parseInt(tempItem.css("top")),
			maxHeigh=parseInt(tempItem.parent(".custom-drop").height())-topHeight;
		if(value<maxHeigh){
			input.val(newValue);
			tempItem.css("height",newValue+"px");
		};
	});
	$("#line-y .customnum .subtract").on("click",function(){
		var input=$("#yLineHeight"),
			value=parseInt(input.val()),
			newValue=value*1-1;
		if(value>15){
			input.val(newValue);
			tempItem.css("height",newValue+"px");
		}
	});
	//修改水平线样式
	$("#yLineStyle").on("change",function(){
		tempItem.css("border-left-style",$(this).val());
	});
	//修改水平线厚度
	$("#yLineWeight").on("change",function(){
		tempItem.css({
			"border-left-width":$(this).val(),
			"width":$(this).val()
		});
	});
	
	//========圆边文字框修改========
	//修改圆边厚度
	$("#circleBorderWidth").on("change",function(){
		tempItem.css("border-width",$(this).val());
	});
	//修改圆框文本
	$("#circleText").on("keyup",function(){
		var newText=$(this).val();
		tempItem.find(".detail").text(newText);
	});
	$("#circleText").on("blur",function(){
		if($(this).val().length<=0){
			$(this).val("A");
			tempItem.find(".detail").text("A");
		};
	});
	//修改圆框文本字体
	$("#circleFontFamily").on("change",function(){
		tempItem.find(".detail").css("font-family",$(this).val());
	});
	//修改圆框文本文字尺寸
	$("#circleFontSize").on("change",function(){
		parseInt($(this).val())<="11"?$(".label-set .group-warning").show():$(".label-set .group-warning").hide();
		tempItem.find(".detail").css("font-size",$(this).val());
	});
	//圆框文本是否加粗
	$("#circleFontWeight").on("click",function(){
		$(this).attr("checked")?tempItem.find(".detail").css("fontWeight","700"):tempItem.find(".detail").css("fontWeight","400");
	});
	//条码前缀
	$("#codePrefix").on("keyup",function(){
		var newPrefix=$(this).val();
		tempItem.find(".prefix").text(newPrefix);
	});
	//是否显示自定义纵向文字
	$("#viewCodeCustomText").on("click",function(){
		if($(this).attr("checked")){
			tempItem.find(".customText").css("display","block");
			$("#codeCustomText").attr("disabled",false);
		}else{
			tempItem.find(".customText").css("display","none");
			$("#codeCustomText").attr("disabled",true);
		}
	});
	//编辑自定义条码文字
	$("#codeCustomText").on("keyup",function(){
		var newPrefix=$(this).val();
		tempItem.find(".customText").text(newPrefix);
	});
	//========条码修改========
	//修改条码类别
	$("#codeType").on("change",function(){
		tempItem.attr("data-code-type",$(this).val());
	});
	//是否显示条码编码文字
	$("#viewCodeNum").on("click",function(){
		if($(this).attr("checked")){
			tempItem.find(".codemunber").show();
			$("#barcode .codenum-set").find("input,select").attr("disabled",false);
		}else{
			tempItem.find(".codemunber").hide();
			$("#barcode .codenum-set").find("input,select").attr("disabled",true);
		}
	});
	//修改条码代码显示位置
	$("#leftCodeText").on("click",function(){
		tempItem.removeClass("vertical-reverse");
	});
	$("#rightCodeText").on("click",function(){
		tempItem.addClass("vertical-reverse");
	});
	//修改条码编码文字对齐方式
	$("#codeNumAlign").on("change",function(){
		tempItem.find(".codemunber").css("textAlign",$(this).val());
	});
	//修改条码编码文字尺寸
	$("#codeNumFontSize").on("change",function(){
		parseInt($(this).val())<="11"?$(".label-set .group-warning").show():$(".label-set .group-warning").hide();
		tempItem.find(".codemunber").css("font-size",$(this).val());
	});
	//圆框文本是否加粗
	$("#codeNumFontWeight").on("click",function(){
		$(this).attr("checked")?tempItem.find(".codemunber").css("fontWeight","700"):tempItem.find(".codemunber").css("fontWeight","400");
	});
	//条码前缀
	$("#codePrefix").on("keyup",function(){
		var newPrefix=$(this).val();
		tempItem.find(".prefix").text(newPrefix);
		//alert(tempItem.find(".codemunber").text());  //保存条码
	});
	//========二维码修改========

	//是否显示二维码文字
	$("#qRviewCodeNum").on("click",function(){
		if($(this).attr("checked")){
			tempItem.find(".codemunber").show();
			$("#qRcode .codenum-set").find("input,select").attr("disabled",false);
		}else{
			tempItem.find(".codemunber").hide();
			$("#qRcode .codenum-set").find("input,select").attr("disabled",true);
		}
	});
	//修改条码代码显示位置
	$("#qRleftCodeText").on("click",function(){
		tempItem.removeClass("vertical-reverse");
	});
	$("#qRrightCodeText").on("click",function(){
		tempItem.addClass("vertical-reverse");
	});
	//修改条码编码文字对齐方式
	$("#qRcodeNumAlign").on("change",function(){
		console.log(111)
		tempItem.find(".codemunber").css("textAlign",$(this).val());
	});
	//修改条码编码文字尺寸
	$("#qRcodeNumFontSize").on("change",function(){
		parseInt($(this).val())<="11"?$(".label-set .group-warning").show():$(".label-set .group-warning").hide();
		tempItem.find(".codemunber").css("font-size",$(this).val());
	});
	//圆框文本是否加粗
	$("#qRcodeNumFontWeight").on("click",function(){
		$(this).attr("checked")?tempItem.find(".codemunber").css("fontWeight","700"):tempItem.find(".codemunber").css("fontWeight","400");
	});
	//条码前缀
	$("#qRcodePrefix").on("keyup",function(){
		var newPrefix=$(this).val();
		tempItem.find(".prefix").show();
		tempItem.find(".prefix").text(newPrefix);
		//alert(tempItem.find(".codemunber").text());  //保存条码
	});

	//是否显示自定义纵向文字
	$("#qRviewCodeCustomText").on("click",function(){
		if($(this).attr("checked")){
			tempItem.find(".customText").css("display","block");
			$("#qRcodeCustomText").attr("disabled",false);
		}else{
			tempItem.find(".customText").css("display","none");
			$("#qRcodeCustomText").attr("disabled",true);
		}
	});
	//编辑自定义条码文字
	$("#qRcodeCustomText").on("keyup",function(){
		var newPrefix=$(this).val();
		tempItem.find(".customText").html(newPrefix);
	});
	//========商品清单表格修改========
	//表格编号是否显示
	$("#viewTdBorder").on("click",function(){
		$(this).attr("checked")?$(".label-content .skulist-table").removeClass("no-tdborder"):$(".label-content .skulist-table").addClass("no-tdborder");
	});
	$("#viewThead").on("click",function(){
		if($(this).attr("checked")){
			tempItem.find(".skulist-table thead").show();
			$(".copy-drop").find(".skulist-table thead").show();
			headHeight=tempItem.find(".skulist-table thead").height();
			tempItem.height(tempItem.height()+headHeight);
			$(this).parents(".form-group").next(".moreinfo").find("input,select").attr("disabled",false);
		}else{
			tempItem.find(".skulist-table thead").hide();
			$(".copy-drop").find(".skulist-table thead").hide();
			tempItem.height(tempItem.height()-headHeight);
			$(this).parents(".form-group").next(".moreinfo").find("input,select").attr("disabled",true);
			headHeight=0;
		}
		countNum();
		return headHeight;
	});
	$("#viewTfoot").on("click",function(){
		if($(this).attr("checked")){
			tempItem.find(".skulist-table tfoot").show();
			$(".copy-drop").find(".skulist-table tfoot").show();
			footHeight=tempItem.find(".skulist-table tfoot").height();
			tempItem.height(tempItem.height()+footHeight);
			$(this).parents(".form-group").next(".moreinfo").find("input,select").attr("disabled",false);
		}else{
			tempItem.find(".skulist-table tfoot").hide();
			$(".copy-drop").find(".skulist-table tfoot").hide();
			footHeight=tempItem.find(".skulist-table tfoot").height();
			tempItem.height(tempItem.height()-footHeight);
			$(this).parents(".form-group").next(".moreinfo").find("input,select").attr("disabled",true);
			footHeight=0;
		}
		countNum();
		return footHeight;
	});

	//修改表头字体
	$("#theadFontFamily").on("change",function(){
		$(".label-content .skulist-table thead").css("font-family",$(this).val());
	});
	//修改表头文字尺寸
	$("#theadFontSize").on("change",function(){
		if(parseInt($("#theadFontSize").val())<="11"||parseInt($("#tbodyFontSize").val())<="11"||parseInt($("#tfootFontSize").val())<="11")$(".label-set .group-warning").show();else $(".label-set .group-warning").hide();
		$(".label-content .skulist-table thead").css("font-size",$(this).val());
	});
	//修改内容字体
	$("#tbodyFontFamily").on("change",function(){
		$(".label-content .skulist-table tbody").css("font-family",$(this).val());
	});
	//修改内容文字尺寸
	$("#tbodyFontSize").on("change",function(){
		if(parseInt($("#theadFontSize").val())<="11"||parseInt($("#tbodyFontSize").val())<="11"||parseInt($("#tfootFontSize").val())<="11")$(".label-set .group-warning").show();else $(".label-set .group-warning").hide();
		$(".label-content .skulist-table tbody").css("font-size",$(this).val());
	});
	//修改脚注字体
	$("#tfootFontFamily").on("change",function(){
		$(".label-content .skulist-table tfoot").css("font-family",$(this).val());
	});
	//修改脚注文字尺寸
	$("#tfootFontSize").on("change",function(){
		if(parseInt($("#theadFontSize").val())<="11"||parseInt($("#tbodyFontSize").val())<="11"||parseInt($("#tfootFontSize").val())<="11")$(".label-set .group-warning").show();else $(".label-set .group-warning").hide();
		$(".label-content .skulist-table tfoot").css("font-size",$(this).val());
	});
	//修改脚注文字对齐方式
	$("#tfootAlign").on("change",function(){
		$(".label-content .skulist-table tfoot").css("textAlign",$(this).val());
	});

	//设置配货表格sku排序
	$(".sortType").on("change",function(){
		if($(this).attr('data-id') == "itemlist"){
			$(".label-content .product-item").attr("data-sort",$(this).val());
		}else{
			$(".label-content .skulist-table").attr("data-sort",$(this).val());
		}
	});

	//设置是否合并重复sku
	$(".mergeSKU").on("change",function(){
		if($(this).attr('data-id') == "itemlist"){
			$(".label-content .product-item").attr("data-merge-sku",$(this).val());
		}else{
			$(".label-content .skulist-table").attr("data-merge-sku",$(this).val());
		}
	});
	
	//========商品清单字段修改=======
	//勾选显示字段可编辑显示文
	$("#fieldset-sku .checkbox-inline input[type='checkbox']").on("click",function(){
		var cName=$(this).val(),
			viewField=$("#fieldset-sku").find("input[type='checkbox']:checked[value!='total-sku'][value!='total-number']").length;
		if($(this).attr("checked")){
			$(this).parents(".form-group").find("input.form-control").attr("disabled",false);
			$(".label-content .skulist-table").find("."+cName).removeClass("dis-none");
			$(".label-content .skulist-table tfoot td").attr("colspan",viewField);
		}else{
			if(viewField==0){
				$.gritter.add({
					class_name:'gritter-error',
					title: "操作错误",
					text: "至少需勾选一项",
					time: 2000,
					sticky: false
				});
				return false;
			}else{
				$(this).parents(".form-group").find("input.form-control").attr("disabled",true);
				$(".label-content .skulist-table").find("."+cName).addClass("dis-none");
				$(".label-content .skulist-table tfoot td").attr("colspan",viewField);
			}
		}
	});
	//勾选显示字段可编辑显示文
	$("#fieldset-platformsku .checkbox-inline input[type='checkbox']").on("click",function(){
		var cName=$(this).val(),
			viewField=$("#fieldset-platformsku").find("input[type='checkbox']:checked[value!='total-sku'][value!='total-number']").length;
		if($(this).attr("checked")){
			$(this).parents(".form-group").find("input.form-control").attr("disabled",false);
			$(".label-content .skulist-table").find("."+cName).removeClass("dis-none");
			$(".label-content .skulist-table tfoot td").attr("colspan",viewField);
		}else{
			if(viewField==0){
				$.gritter.add({
					class_name:'gritter-error',
					title: "操作错误",
					text: "至少需勾选一项",
					time: 2000,
					sticky: false
				});
				return false;
			}else{
				$(this).parents(".form-group").find("input.form-control").attr("disabled",true);
				$(".label-content .skulist-table").find("."+cName).addClass("dis-none");
				$(".label-content .skulist-table tfoot td").attr("colspan",viewField);
			}
		}
	});
	$("#fieldset-invoiceskulist .checkbox-inline input[type='checkbox']").on("click",function(){
		var cName=$(this).val(),
			viewField=$("#fieldset-invoiceskulist").find("input[type='checkbox']:checked[value!='total-sku'][value!='total-number']").length;
		if($(this).attr("checked")){
			$(this).parents(".form-group").find("input.form-control").attr("disabled",false);
			$(".label-content .skulist-table").find("."+cName).removeClass("dis-none");
			$(".label-content .skulist-table tfoot td").attr("colspan",viewField);
		}else{
			if(viewField==0){
				$.gritter.add({
					class_name:'gritter-error',
					title: "操作错误",
					text: "至少需勾选一项",
					time: 2000,
					sticky: false
				});
				return false;
			}else{
				$(this).parents(".form-group").find("input.form-control").attr("disabled",true);
				$(".label-content .skulist-table").find("."+cName).addClass("dis-none");
				$(".label-content .skulist-table tfoot td").attr("colspan",viewField);
			}
		}
	});
	
	//修改商品缩略图文本
	$("#fieldTextPhoto").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .photo span").text(newText);
	});
	//修改商品编号文本
	$("#fieldTextSku").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .sku span").text(newText);
	});
	//修改原厂编号文本
	$("#fieldTextOriginal").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .sku_original span").text(newText);
	});
	//修改itemID文本
	$("#fieldTextItemid").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .itemid span").text(newText);
	});
	//修改平台SKU文本
	$("#fieldTextPlatform").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .sku_platform span").text(newText);
	});
	//修改组合SKU文本
	$("#fieldTextComboSku").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .sku_combo span").text(newText);
	});
	//修改中文名称文本
	$("#fieldTextName").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .name span").text(newText);
	});
	//修改中文名称文本
	$("#fieldTextName3").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .name span").text(newText);
	});
	//修改中文名称文本
	$("#fieldTextOriginTitle").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .originTitle span").text(newText);
	});
	//修改中文名称文本
	$("#fieldTextProductId").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .productId span").text(newText);
	});
	//修改中文名称文本
	$("#fieldTextNameEn").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .name_en span").text(newText);
	});
	//修改申报品名文本
	$("#fieldTextNameDeclare").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .name_declare span").text(newText);
	});
	//修改仓库文本
	$("#fieldTextWarehouse").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .warehouse span").text(newText);
	});
	//修改仓位文本
	$("#fieldTextPosition").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .position span").text(newText);
	});
	//修改包材文本
	$("#fieldTextOrderItemPackage").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .orderItem_package span").text(newText);
	});
	//修改数量文本
	$("#fieldTextNumber").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .number span").text(newText);
	});
	//修改重量文本
	$("#fieldTextWeight").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .weight span").text(newText);
	});
	//修改多属性文本
	$("#fieldTextMultiproperty").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .multi-property span").text(newText);
	});
	//修改平台单位文本
	$("#fieldTextProductUnit").on("keyup",function(){
		var newText = $(this).val();
		$(".label-content .skulist-table thead .product_unit span").text(newText);
	})
	//修改售价文本
	$("#fieldTextSalesPrice").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .salesPrice span").text(newText);
	});
	//修改单价文本
	$("#fieldTextPrice").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .price span").text(newText);
	});
	//修改小计文本
	$("#fieldTextTotal").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .total span").text(newText);
	});
	//修改总售价文本
	$("#fieldTextTotalSalesPrice").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .totalSalesPrice span").text(newText);
	});

	//修改商品种类文本
	$("#fieldTextTotalKind").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table tfoot .totalKind").text(newText);
	});

	//修改总件数文本
	$("#fieldTextTotalQuantity").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table tfoot .totalQuantity").text(newText);
	});

	//=====组合sku====
	//修改平台SKU文本
	$("#fieldTextPlatform22").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .sku_platform span").text(newText);
	});
	//修改仓库文本
	$("#fieldTextWarehouse2").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .warehouse span").text(newText);
	});
	//修改仓位文本
	$("#fieldTextPosition2").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .position span").text(newText);
	});
	//修改包材文本
	$("#fieldTextOrderItemPackage2").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .orderItem_package span").text(newText);
	});
	//修改数量文本
	$("#fieldTextNumber2").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .number span").text(newText);
	});
	//修改数量文本
	$("#fieldTextNumber3").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .number span").text(newText);
	});
	//修改重量文本
	$("#fieldTextWeight2").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .weight span").text(newText);
	});
	//修改多属性文本
	$("#fieldTextMultiproperty2").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .multi-property span").text(newText);
	});
	//修改平台单位文本
	$("#fieldTextProductUnit2").on("keyup",function(){
		var newText = $(this).val();
		$(".label-content .skulist-table thead .product_unit span").text(newText);
	})
	//修改售价文本
	$("#fieldTextSalesPrice2").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .salesPrice span").text(newText);
	});
	//修改售价文本
	$("#fieldTextSalesPrice3").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .sellPriceOrigin span").text(newText);
	});
	//修改售价文本
	$("#fieldTextIncludTaxPrice").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .includTaxPrice span").text(newText);
	});
	//修改售价文本
	$("#fieldTextExcludTaxPrice").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .excludTaxPrice span").text(newText);
	});
	//修改售价文本
	$("#fieldTextTax").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .tax span").text(newText);
	});
	//修改单价文本
	$("#fieldTextPrice2").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .price span").text(newText);
	});
	//修改小计文本
	$("#fieldTextTotal2").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .total span").text(newText);
	});
	//修改总售价文本
	$("#fieldTextTotalSalesPrice2").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .totalSalesPrice span").text(newText);
	});
	//修改总售价文本
	$("#fieldTextTotalSalesPrice3").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table thead .totalSalesPrice span").text(newText);
	});//修改总售价文本
	$("#fieldTextTotalPrice").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table tfoot .price").text(newText);
	});

	//修改商品种类文本
	$("#fieldTextTotalKind2").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table tfoot .totalKind").text(newText);
	});

	//修改总件数文本
	$("#fieldTextTotalQuantity2").on("keyup",function(){
		var newText=$(this).val();
		$(".label-content .skulist-table tfoot .totalQuantity").text(newText);
	});
	//========报关物品字段修改=======
	//修改报关品名表头文字
	$("#declareNameTitle").on("keyup",function(){
		var newText=$(this).val().replace(/\n/gi,"<br>");
		tempItem.find(".skulist-table thead .name_declare span").html(newText);
	});
	//修改报关品名显示格式
	$("#declareType").on("change",function(){
		var type=$(this).val();
		$("#declareName").find("."+type).show().siblings(".multiple").hide();
		$(".label-content .skulist-table tbody").find("."+type).removeClass("dis-none").siblings(".name_declare").addClass("dis-none");
		type=="sort"?$(".label-content.overflow").addClass("dis-none"):$(".label-content.overflow").removeClass("dis-none");
	});
	//修改商品目录内容格式
	$("#declareName .multiple input[type='checkbox']").on("click",function(){
		var cName=$(this).val(),
			viewField=$(this).parents(".multiple").find("input[type='checkbox']:checked").length;
		if($(this).attr("checked")){
			$(".label-content .skulist-table tbody .name_declare").find("."+cName).removeClass("dis-none");
		}else{
			if(viewField==0){
				$.gritter.add({
					class_name:'gritter-error',
					title: "操作错误",
					text: "至少需勾选一项",
					time: 2000,
					sticky: false
				});
				return false;
			}else{
				$(".label-content .skulist-table tbody .name_declare").find("."+cName).addClass("dis-none");
			}
		}
	});
	//修改自定义文字内容
	$("#declareNameCustom").on("keyup",function(){
		var newText=$(this).val().replace(/\n/gi,"<br>");
		tempItem.find(".skulist-table tbody tr:first .name_declare.custom span").html(newText);
		$(".copy-drop .skulist-table tbody tr:first .name_declare.custom span").html(newText);
	});
	//修改报关重量表头文字
	$("#declareWeightTitle").on("keyup",function(){
		var newText=$(this).val().replace(/\n/gi,"<br>");
		tempItem.find(".skulist-table thead .weight_declare span").html(newText);
	});
	//修改报关价值表头文字
	$("#declarePriceTitle").on("keyup",function(){
		var newText=$(this).val().replace(/\n/gi,"<br>");
		tempItem.find(".skulist-table thead .price_declare span").html(newText);
	});
	//是否显示脚注标题行
	$("#viewTfootTitle").on("click",function(){
		if($(this).attr("checked")){
			$("#fieldset-declare .declare-tfoot textarea").attr("disabled",false);
			tempItem.find(".skulist-table tfoot tr:first").removeClass("dis-none");
		}else{
			$("#fieldset-declare .declare-tfoot textarea").attr("disabled",true);
			tempItem.find(".skulist-table tfoot tr:first").addClass("dis-none");
		};
		tempItem.height(tempItem.find(".skulist-table").height());
		footHeight=tempItem.find(".skulist-table tfoot").height();
		dropCreate(tempItem);
	})
	//修改总毛重脚注文字
	$("#declareOriginTitle").on("keyup",function(){
		var newText=$(this).val().replace(/\n/gi,"<br>");
		tempItem.find(".skulist-table tfoot th.origin_declare span").html(newText);
	});
	//修改总重量脚注文字
	$("#declareTotalWeightTitle").on("keyup",function(){
		var newText=$(this).val().replace(/\n/gi,"<br>");
		tempItem.find(".skulist-table tfoot th.weight_declare span").html(newText);
	});
	//修改总价值脚注文字
	$("#declareTotalPriceTitle").on("keyup",function(){
		var newText=$(this).val().replace(/\n/gi,"<br>");
		tempItem.find(".skulist-table tfoot th.price_declare span").html(newText);
	});

	//修改原产国缩写文字
	$("#declareOriginCountryCode").on("keyup",function(){
		var newText=$(this).val().replace(/\n/gi,"<br>");
		tempItem.find(".skulist-table tfoot .countrycode_declare span").html(newText);
	});

	//========EUB报关物品字段修改=======
	//修改EUB报关物品序号表头文字
	$("#eubDeclareNoTitle").on("keyup",function(){
		var newText=$(this).val().replace(/\n/gi,"<br>");
		tempItem.find(".skulist-table thead .no_declare span").html(newText);
	});
	//修改EUB报关物品数量表头文字
	$("#eubDeclareQtyTitle").on("keyup",function(){
		var newText=$(this).val().replace(/\n/gi,"<br>");
		tempItem.find(".skulist-table thead .qty_declare span").html(newText);
	});
	//修改EUB报关品名表头文字
	$("#eubDeclareNameTitle").on("keyup",function(){
		var newText=$(this).val().replace(/\n/gi,"<br>");
		tempItem.find(".skulist-table thead .name_declare span").html(newText);
	});
	//修改报关品名显示格式
	$("#eubDeclareType").on("change",function(){
		var type=$(this).val();
		$("#eubDeclareName").find("."+type).show().siblings(".multiple").hide();
		$(".label-content .skulist-table tbody").find("."+type).removeClass("dis-none").siblings(".name_declare").addClass("dis-none");
		type=="sort"?$(".label-content.overflow").addClass("dis-none"):$(".label-content.overflow").removeClass("dis-none");
	});
	//修改EUB商品目录内容格式
	$("#eubDeclareName .multiple input[type='checkbox']").on("click",function(){
		var cName=$(this).val(),
			viewField=$(this).parents(".multiple").find("input[type='checkbox']:checked").length;
		if($(this).attr("checked")){
			$(".label-content .skulist-table tbody .name_declare").find("."+cName).removeClass("dis-none");
		}else{
			if(viewField==0){
				$.gritter.add({
					class_name:'gritter-error',
					title: "操作错误",
					text: "至少需勾选一项",
					time: 2000,
					sticky: false
				});
				return false;
			}else{
				$(".label-content .skulist-table tbody .name_declare").find("."+cName).addClass("dis-none");
			}
		}
	});
	//修改EUB自定义文字内容
	$("#eubDeclareNameCustom").on("keyup",function(){
		var newText=$(this).val().replace(/\n/gi,"<br>");
		tempItem.find(".skulist-table tbody tr:first .name_declare.custom span").html(newText);
		$(".copy-drop .skulist-table tbody tr:first .name_declare.custom span").html(newText);
	});
	//修改EUB报关重量表头文字
	$("#eubDeclareWeightTitle").on("keyup",function(){
		var newText=$(this).val().replace(/\n/gi,"<br>");
		tempItem.find(".skulist-table thead .weight_declare span").html(newText);
	});
	//修改EUB报关价值表头文字
	$("#eubDeclarePriceTitle").on("keyup",function(){
		var newText=$(this).val().replace(/\n/gi,"<br>");
		tempItem.find(".skulist-table thead .price_declare span").html(newText);
	});
	//修改原产国表头文字
	$("#eubDeclareOriginTitle").on("keyup",function(){
		var newText=$(this).val().replace(/\n/gi,"<br>");
		tempItem.find(".skulist-table thead th.origin_declare span").html(newText);
	});
	//修改总重量表头文字
	$("#eubDeclareTotalWeightTitle").on("keyup",function(){
		var newText=$(this).val().replace(/\n/gi,"<br>");
		tempItem.find(".skulist-table tfoot th.origin_declare span").html(newText);
	});
//========EUB单行报关物品字段修改=======
	//修改SKU数量表头文字
	$("#eubDeclareSingleNoTitle").on("keyup",function(){
		var newText=$(this).val().replace(/\n/gi,"<br>");
		tempItem.find(".skulist-table thead .no_declare span").html(newText);
	});
	//修改产品总数量表头文字
	$("#eubDeclareSingleQtyTitle").on("keyup",function(){
		var newText=$(this).val().replace(/\n/gi,"<br>");
		tempItem.find(".skulist-table thead .qty_declare span").html(newText);
	});
	//修改EUB报关品名表头文字
	$("#eubDeclareSingleNameTitle").on("keyup",function(){
		var newText=$(this).val().replace(/\n/gi,"<br>");
		tempItem.find(".skulist-table thead .name_declare span").html(newText);
	});
	//启用固定申报中英文名
	$("#eubDeclareSingleCustom").on("click",function(){
		if($(this).attr("checked")){
			tempItem.find(".skulist-table tbody .name_declare > .custom").removeClass("hide");
			tempItem.find(".skulist-table tbody .name_declare > .sort").addClass("hide");
			$("#eubDeclareSingleCustomContent").attr("disabled",false);
			$("input[name='eubDeclareSingleSort']").attr("disabled",true);
		}else{
			tempItem.find(".skulist-table tbody .name_declare > .custom").addClass("hide");
			tempItem.find(".skulist-table tbody .name_declare > .sort").removeClass("hide");
			$("#eubDeclareSingleCustomContent").attr("disabled",true);
			$("input[name='eubDeclareSingleSort']").attr("disabled",false);
		}
	});

	//修改固定申报中英文名
	$("#eubDeclareSingleCustomContent").on("keyup",function(){
		var newText=$(this).val().replace(/\n/gi,"<br>");
		tempItem.find(".skulist-table tbody .name_declare > .custom").html(newText);
	});

	//修改申报品名格式
	$("input[name='eubDeclareSingleSort']").on("click",function(){
		var cName=$(this).val(),
			viewField=$(this).parents(".multiple").find("input[type='checkbox']:checked").length;
		if($(this).attr("checked")){
			$(".label-content .skulist-table tbody .name_declare > .sort").find("."+cName).removeClass("dis-none");
		}else{
			if(viewField==0){
				$.gritter.add({
					class_name:'gritter-error',
					title: "操作错误",
					text: "至少需勾选一项",
					time: 2000,
					sticky: false
				});
				return false;
			}else{
				$(".label-content .skulist-table tbody .name_declare > .sort").find("."+cName).addClass("dis-none");
			}
		}
	});
	//修改多字段组合内容格式
	$("input[name='eubDeclareSingleType']").on("click",function(){
		var cName=$(this).val(),
			viewField=$(this).parents(".multiple").find("input[type='checkbox']:checked").length;
		if($(this).attr("checked")){
			$(".label-content .skulist-table tbody .name_declare > .group").find("."+cName).removeClass("dis-none");
		}else{
			if(viewField==0){
				$.gritter.add({
					class_name:'gritter-error',
					title: "操作错误",
					text: "至少需勾选一项",
					time: 2000,
					sticky: false
				});
				return false;
			}else{
				$(".label-content .skulist-table tbody .name_declare > .group").find("."+cName).addClass("dis-none");
			}
		}
	});

	//修改EUB报关重量表头文字
	$("#eubDeclareSingleWeightTitle").on("keyup",function(){
		var newText=$(this).val().replace(/\n/gi,"<br>");
		tempItem.find(".skulist-table thead .weight_declare span").html(newText);
	});
	//修改EUB报关价值表头文字
	$("#eubDeclareSinglePriceTitle").on("keyup",function(){
		var newText=$(this).val().replace(/\n/gi,"<br>");
		tempItem.find(".skulist-table thead .price_declare span").html(newText);
	});
	//修改原产国表头文字
	$("#eubDeclareSingleOriginTitle").on("keyup",function(){
		var newText=$(this).val().replace(/\n/gi,"<br>");
		tempItem.find(".skulist-table thead th.origin_declare span").html(newText);
	});
	//========配货兼报关表格字段修改=======
	//修改配货报关品名表头文字
	$("#declareSkuNameTitle").on("keyup",function(){
		var newText=$(this).val().replace(/\n/gi,"<br>");
		tempItem.find(".skulist-table thead .name_declare span").html(newText);
	});
	//修改报关品名显示格式
// 			$("#declareSkuType").on("change",function(){
// 				var type=$(this).val();
// 				$("#declareSkuName").find("."+type).show().siblings(".multiple").hide();
// 				$(".label-content .skulist-table tbody").find("."+type).removeClass("dis-none").siblings(".name_declare").addClass("dis-none");
// 				type=="sort"?$(".label-content.overflow").addClass("dis-none"):$(".label-content.overflow").removeClass("dis-none");
// 			});
	//修改配货报关商品目录内容格式
	$("input[type='checkbox'][name='declareSkuSorttype']").on("click",function(){
// 			$("#declareSkuName .multiple input[type='checkbox']").on("click",function(){
		var cName=$(this).val(),
			viewField=$(this).parents(".multiple").find("input[type='checkbox']:checked").length;
		if($(this).attr("checked")){
			$(".label-content .skulist-table tbody .name_declare").find("."+cName).removeClass("dis-none");
		}else{
			if(viewField==0){
				$.gritter.add({
					class_name:'gritter-error',
					title: "操作错误",
					text: "至少需勾选一项",
					time: 2000,
					sticky: false
				});
				return false;
			}else{
				$(".label-content .skulist-table tbody .name_declare").find("."+cName).addClass("dis-none");
			}
		}
	});
	//修改配货报关自定义文字内容
// 			$("#declareSkuNameCustom").on("keyup",function(){
// 				var newText=$(this).val().replace(/\n/gi,"<br>");
// 				tempItem.find(".skulist-table tbody tr:first .name_declare.custom span").html(newText);
// 				$(".copy-drop .skulist-table tbody tr:first .name_declare.custom span").html(newText);
// 			});
	//修改配货报关重量表头文字
	$("#declareSkuWeightTitle").on("keyup",function(){
		var newText=$(this).val().replace(/\n/gi,"<br>");
		tempItem.find(".skulist-table thead .weight_declare span").html(newText);
	});
	//修改配货报关价值表头文字
	$("#declareSkuPriceTitle").on("keyup",function(){
		var newText=$(this).val().replace(/\n/gi,"<br>");
		tempItem.find(".skulist-table thead .price_declare span").html(newText);
	});
	//修改配货报关总毛重脚注文字
	$("#declareSkuOriginTitle").on("keyup",function(){
		var newText=$(this).val().replace(/\n/gi,"<br>");
		tempItem.find(".skulist-table tfoot th.origin_declare span").html(newText);
	});
	
	//========配货商品字段修改========
	//修改配货商品
	$("#fieldset-item .multiple input[type='checkbox']").on("click",function(){
		var cName=$(this).val(),
			viewField=$(this).parents(".multiple").find("input[type='checkbox']:checked").length;
		if($(this).attr("checked")){
			tempItem.find("."+cName).removeClass("dis-none");
		}else{
			if(viewField==0){
				$.gritter.add({
					class_name:'gritter-error',
					title: "操作错误",
					text: "至少需勾选一项",
					time: 2000,
					sticky: false
				});
				return false;
			}else{
				tempItem.find("."+cName).addClass("dis-none");
			}
		}
	});
	//单个字段是否整行显示
	$("#newItemline").on("click",function(){
		$(this).attr("checked")?tempItem.find(".detail").addClass("block"):tempItem.find(".detail").removeClass("block");
	})
	
	//========在线图片修改========
	//修改在线图片路径
	$("#loadImgUrl").on("click",function(){
		var ImgObj = new Image(); //判断图片是否存在  
			ImgObj.src = $("#imageUrl").val();  //没有图片，则返回-1
		tempItem.find("img").attr("src",$("#imageUrl").val());
	});

	//========修改本地上传的图片========
	$("#templetfile").on("change",function(){
		
		if (window.File && window.FileList) {
			var	file=document.getElementById("templetfile").files;
			//文件尺寸过滤，这里设置最大容量为1mb
			if (file[0].size >= 1024000) {
				$.gritter.add({
					class_name:'gritter-error',
					title: "不能添加",
					text:"图片容量过大，单张图片容量不能大于1MB！",
					time: 2000,
					sticky: false
				});
				return false;
			};
			
			//文件格式过滤
			if(!/image\/\w+/.test(file[0].type)){
				$.gritter.add({
					class_name:'gritter-error',
					title: "格式错误",
					text: "请上传图片格式文件",
					time: 1500,
					sticky: false
				});
				return false;	
			};
			
			//创建预览
			var reader = new FileReader();
			reader.onload = function(e) {
			   $("#previewImage").attr("src",e.target.result);
			   tempItem.find("img").attr("src",e.target.result);
			};
			reader.readAsDataURL(file[0]);
			$('.loading_large').show();
			
			//现在是用base64存储图片，如果加载速度满，可以改成在这里上传图片，返回url
			// $("#previewImage").attr("src",data.imageUrl);
			// tempItem.find("img").attr("src",data.imageUrl);
		}else {
			$.gritter.add({
				class_name:'gritter-error',
				title: "浏览器不支持",
				text: "抱歉，你的浏览器不支持FileAPI，请升级浏览器！",
				time: 2000,
				sticky: false
			});
			return false;
		}
	});
	
	//========编辑控制=======
	//复制该项属性
	$(".btn-copy:not('.disabled')").on("click",function(){
		var	itemLeft=parseInt(tempItem.css("left")),
			itemTop=parseInt(tempItem.css("top")),
			newLeft=itemLeft+20,
			newTop=itemTop+20;
		tempItem.clone().css({
			"left":newLeft,
			"top":newTop
		}).appendTo(tempItem.parent(".custom-drop")).removeClass("active").find(".ui-resizable-handle").remove();
		dropCreate($(".custom-drop .dropitem:last"));
	});
	//删除该项属性
	$(".btn-clear-item").on("click",function(){
		var itemSort=tempItem.attr("data-title"),
			repeatSort =$(".label-type .dragitem.complete strong:contains('"+itemSort+"')").map(function(){ 
				if ($(this).text() == itemSort) {return this;}
			});
		repeatSort.parent(".dragitem").removeClass("complete");
		if(tempItem.hasClass("skulist")|| tempItem.hasClass("platformskulist")|| tempItem.hasClass("invoiceskulist") ||tempItem.hasClass("declarelist")||tempItem.hasClass("eubdeclarelist")||tempItem.hasClass("declareskulist")||tempItem.hasClass("manualdeclare")){$(".label-content.overflow").remove();maxNum=1;}//||tempItem.hasClass("declarenewlist")
		//if(tempItem.hasClass("skulist")||tempItem.hasClass("declarelist") ||tempItem.hasClass("eubdeclarelist")){$(".label-content.overflow").remove();maxNum=1;}
		tempItem.remove();
		closeLabelSet();
		$("#templetfile").val(""); 
		$(".hotkey-panel").css({"opacity":"0","z-index":"0"});
		return maxNum;
	});

	//================参照图片===============
	//上传参考图
	$(".refer-upload i").on("click",function(){$(this).next().click()});
	$(".refer-upload input[type='file']").on("change",function(){
		if (window.File && window.FileList) {
			var $this = $(this);
			var	file=this.files[0];
			//文件尺寸过滤
			if (file.size >= 2048000) {
				$.gritter.add({
					class_name:'gritter-error',
					text: "图片容量过大，应小于2MB",
					time: 1500,
					sticky: false
				});
				return false;
			};
			//文件格式过滤
			if(!/image\/\w+/.test(file.type)){
				$.gritter.add({
					class_name:'gritter-error',
					text: "请上传图片格式文件",
					time: 1500,
					sticky: false
				});
				return false;
			};
			//创建参考图容器
			var reader = new FileReader(), htmlImage;
			reader.onload = function(e) {
				var newImage='<div class="refer-content"><span></span><img src="'+ e.target.result +'" /></div>';
				$(".label-content").first().append(newImage);
				$(".refer-upload").hide().siblings("a").show();
			};
			reader.readAsDataURL(file);
			
		}else {
			$.gritter.add({
				class_name:'gritter-error',
				text: "抱歉，你的浏览器不支持FileAPI，请升级浏览器！",
				time: 2000,
				sticky: false
			});
			return false;
		}
	});
	
	//移除参考图
	$(".refer-remove").on("click",function(){
		bootbox.dialog({
			//backdrop: "static",
			message: "确定删除参考图？",
			buttons: {
				success: {
					label: "确定",
					className: "btn-primary",
					callback: function () {
						$(".refer-content").remove();
						$(".refer-upload").show().children("input[type='file']").val("").end().siblings("a").hide();
					}
				},
				cancel: {
					label: "取消",
					className: "btn-default",
					callback: function () {
						return null;
					}
				}
				
			}
		});
	});
	
	//切换显示位置
	$(".refer-switch").on("click",function(){
		$(".refer-content").toggleClass("multiply");
	});
	
	//切换是否显示
	$(".refer-view").on("click",function(){
		if($(".refer-content").hasClass("dis-none")){
			$(".refer-content").removeClass("dis-none");
			$(this).children("i").attr("class","ico-eye-close text-muted");
		}else{
			$(".refer-content").addClass("dis-none");
			$(this).children("i").attr("class","ico-eye-open");
		};
	});
});
function doSavePrintTemplate(ctrl,type)
{
	var a = 0;
	$(".label-content").each(function() {
		if($(this).find(".dropitem[data-title='收件人地址']").length>0 || 
				$(this).find(".dropitem[data-title='ebay地址']").length>0 || 
					$(this).find(".dropitem[data-title='发件人地址']").length>0){
			//收件人地址
			if($(this).find(".dropitem[data-title='收件人地址']").length>0){
				if($(this).find(".dropitem[data-title='收件人地址']").find("#RECEIVER_NAME").parent().attr("style") == 'display: none;'){
					if($(this).find(".dropitem[data-title='收件人姓名']").length==0){
						$.gritter.add({
							class_name:'gritter-error',
							title: "保存失败",
							text: "收件人名称必选",
							time: 2000,
							sticky: false
						});
						a++;
						return false;
					}
				}
				if($(this).find(".dropitem[data-title='收件人地址']").find("#RECEIVER_ADDRESS").parent().attr("style") == 'display: none;'){
					if($(this).find(".dropitem[data-title='收件人街道']").length==0){
						$.gritter.add({
							class_name:'gritter-error',
							title: "保存失败",
							text: "收件人街道必选",
							time: 2000,
							sticky: false
						});
						a++;
						return false;
					}
				}
				if($(this).find(".dropitem[data-title='收件人地址']").find("#RECEIVER_ZIPCODE").parent().attr("style") == 'display: none;'){
					if($(this).find(".dropitem[data-title='收件人邮编']").length==0){
						$.gritter.add({
							class_name:'gritter-error',
							title: "保存失败",
							text: "收件人邮编必选",
							time: 2000,
							sticky: false
						});
						a++;
						return false;
					}
				}
				if($(this).find(".dropitem[data-title='收件人地址']").find("#RECEIVER_COUNTRY_EN").parent().attr("style") == 'display: none;'){
					if($(this).find(".dropitem[data-title='收件人国家(英)']").length==0){
						$.gritter.add({
							class_name:'gritter-error',
							title: "保存失败",
							text: "收件人国家必选",
							time: 2000,
							sticky: false
						});
						a++;
						return false;
					}
				}
				if($(this).find(".dropitem[data-title='收件人地址']").find("#RECEIVER_TELEPHONE").parent().attr("style") == 'display: none;'){
					if($(this).find(".dropitem[data-title='收件人电话']").length==0){
						$.gritter.add({
							class_name:'gritter-error',
							title: "保存失败",
							text: "收件人电话必选",
							time: 2000,
							sticky: false
						});
						a++;
						return false;
					}
				}
			}
			//ebay地址判断字段
			if($(this).find(".dropitem[data-title='ebay地址']").length>0){
				if($(this).find(".dropitem[data-title='ebay地址']").find("#RECEIVER_NAME_EBAY").parent().attr("style") == 'display: none;'){
					if($(this).find(".dropitem[data-title='收件人姓名']").length==0){
						$.gritter.add({
							class_name:'gritter-error',
							title: "保存失败",
							text: "收件人名称必选",
							time: 2000,
							sticky: false
						});
						a++;
						return false;
					}
				}
				if($(this).find(".dropitem[data-title='ebay地址']").find("#RECEIVER_ADDRESS_EBAY").parent().attr("style") == 'display: none;'){
					if($(this).find(".dropitem[data-title='收件人街道']").length==0){
						$.gritter.add({
							class_name:'gritter-error',
							title: "保存失败",
							text: "收件人街道必选",
							time: 2000,
							sticky: false
						});
						a++;
						return false;
					}
				}
				if($(this).find(".dropitem[data-title='ebay地址']").find("#RECEIVER_ZIPCODE_EBAY").parent().attr("style") == 'display: none;'){
					if($(this).find(".dropitem[data-title='收件人邮编']").length==0){
						$.gritter.add({
							class_name:'gritter-error',
							title: "保存失败",
							text: "收件人邮编必选",
							time: 2000,
							sticky: false
						});
						a++;
						return false;
					}
				}
				if($(this).find(".dropitem[data-title='ebay地址']").find("#RECEIVER_COUNTRY_EN_EBAY").parent().attr("style") == 'display: none;'){
					if($(this).find(".dropitem[data-title='收件人国家(英)']").length==0){
						$.gritter.add({
							class_name:'gritter-error',
							title: "保存失败",
							text: "{lang 收件人国家必选",
							time: 2000,
							sticky: false
						});
						a++;
						return false;
					}
				}
				if($(this).find(".dropitem[data-title='ebay地址']").find("#RECEIVER_TELEPHONE_EBAY").parent().attr("style") == 'display: none;'){
					if($(this).find(".dropitem[data-title='收件人电话']").length==0){
						$.gritter.add({
							class_name:'gritter-error',
							title: "保存失败",
							text: "收件人电话1必选",
							time: 2000,
							sticky: false
						});
						a++;
						return false;
					}
				}
			}
		}
		//发件人地址
		if($(this).find(".dropitem[data-title='发件人地址']").length>0){
			if($(this).find(".dropitem[data-title='发件人地址']").find("#SENDER_NAME").parent().attr("style") == 'display: none;'){
				if($(this).find(".dropitem[data-title='发件人姓名']").length==0){
					$.gritter.add({
						class_name:'gritter-error',
						title: "保存失败",
						text: "发件人名称必选",
						time: 2000,
						sticky: false
					});
					a++;
					return false;
				}
			}
			if($(this).find(".dropitem[data-title='发件人地址']").find("#SENDER_ADDRESS").parent().attr("style") == 'display: none;'){
				if($(this).find(".dropitem[data-title='发件人街道']").length==0){
					$.gritter.add({
						class_name:'gritter-error',
						title: "保存失败",
						text: "发件人街道必选",
						time: 2000,
						sticky: false
					});
					a++;
					return false;
				}
			}
			if($(this).find(".dropitem[data-title='发件人地址']").find("#SENDER_ZIPCODE").parent().attr("style") == 'display: none;'){
				if($(this).find(".dropitem[data-title='发件人邮编']").length==0){
					$.gritter.add({
						class_name:'gritter-error',
						title: "保存失败",
						text: "发件人邮编必选",
						time: 2000,
						sticky: false
					});
					a++;
					return false;
				}
			}
			if($(this).find(".dropitem[data-title='发件人地址']").find("#SENDER_COUNTRY_EN").parent().attr("style") == 'display: none;'){
				if($(this).find(".dropitem[data-title='发件人国家']").length==0){
					$.gritter.add({
						class_name:'gritter-error',
						title: "保存失败",
						text: "发件人国家必选",
						time: 2000,
						sticky: false
					});
					a++;
					return false;
				}
			}
			if($(this).find(".dropitem[data-title='发件人地址']").find("#SENDER_TELEPHONE").parent().attr("style") == 'display: none;'){
				if($(this).find(".dropitem[data-title='发件人电话']").length==0){
					$.gritter.add({
						class_name:'gritter-error',
						title: "保存失败",
						text: "发件人电话1必选",
						time: 2000,
						sticky: false
					});
					a++;
					return false;
				}
			}
		}
	});
	if(a > 0){
		return false;
	}
	var $form = $("form[name=frmPrintTemplate]");
	var $this = $(ctrl);
	var taxRatio = $("#taxRatio").val();
	if(Number(taxRatio) < 1 || Number(taxRatio)>100){
		$.gritter.add({
			class_name:'gritter-error',
			title: "保存失败",
			text: "{lang 税金比例只允许在1-100之间}",
			time: 2000,
			sticky: false
		});
		return false;
	}
	$this.prop("disabled", true);
	//$('#divPrintTemplateHtml').find(".dropitem").removeClass("ui-draggable ui-resizable").find(".ui-resizable-handle").remove();
	var labelHtml=$(".label-group").clone().find(".dropitem").removeClass("ui-draggable ui-resizable").end().find(".ui-resizable-handle, .refer-tools, .refer-content").remove().end().html();
	//$('#html').val(Base64.encode(labelHtml));
	$('#html').val(labelHtml);
	var data =  $form.serialize() ;
	
	//没问题之后，保存
}

var Base64 = {
	// private property
	_keyStr : "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",
	// public method for encoding
	encode : function (input) {
		var output = "";
		var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
		var i = 0;
		input = Base64._utf8_encode(input);
		while (i < input.length) {
 
			chr1 = input.charCodeAt(i++);
			chr2 = input.charCodeAt(i++);
			chr3 = input.charCodeAt(i++);
 
			enc1 = chr1 >> 2;
			enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
			enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
			enc4 = chr3 & 63;
 
			if (isNaN(chr2)) {
				enc3 = enc4 = 64;
			} else if (isNaN(chr3)) {
				enc4 = 64;
			}
			output = output +
			this._keyStr.charAt(enc1) + this._keyStr.charAt(enc2) +
			this._keyStr.charAt(enc3) + this._keyStr.charAt(enc4);
		}
		return output;
	},
	// public method for decoding
	decode : function (input) {
		var output = "";
		var chr1, chr2, chr3;
		var enc1, enc2, enc3, enc4;
		var i = 0;
		input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
		while (i < input.length) {
 
			enc1 = this._keyStr.indexOf(input.charAt(i++));
			enc2 = this._keyStr.indexOf(input.charAt(i++));
			enc3 = this._keyStr.indexOf(input.charAt(i++));
			enc4 = this._keyStr.indexOf(input.charAt(i++));
 
			chr1 = (enc1 << 2) | (enc2 >> 4);
			chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
			chr3 = ((enc3 & 3) << 6) | enc4;
 
			output = output + String.fromCharCode(chr1);
 
			if (enc3 != 64) {
				output = output + String.fromCharCode(chr2);
			}
			if (enc4 != 64) {
				output = output + String.fromCharCode(chr3);
			}
		}
		output = Base64._utf8_decode(output);
		return output;
	},
	// private method for UTF-8 encoding
	_utf8_encode : function (string) {
		string = string.replace(/\r\n/g,"\n");
		var utftext = "";
 
		for (var n = 0; n < string.length; n++) {
 
			var c = string.charCodeAt(n);
 
			if (c < 128) {
				utftext += String.fromCharCode(c);
			}
			else if((c > 127) && (c < 2048)) {
				utftext += String.fromCharCode((c >> 6) | 192);
				utftext += String.fromCharCode((c & 63) | 128);
			}
			else {
				utftext += String.fromCharCode((c >> 12) | 224);
				utftext += String.fromCharCode(((c >> 6) & 63) | 128);
				utftext += String.fromCharCode((c & 63) | 128);
			}
		}
		return utftext;
	},
 
	// private method for UTF-8 decoding
	_utf8_decode : function (utftext) {
		var string = "";
		var i = 0;
		var c = c1 = c2 = 0;
 
		while ( i < utftext.length ) {
			c = utftext.charCodeAt(i);
			if (c < 128) {
				string += String.fromCharCode(c);
				i++;
			}
			else if((c > 191) && (c < 224)) {
				c2 = utftext.charCodeAt(i+1);
				string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
				i += 2;
			}
			else {
				c2 = utftext.charCodeAt(i+1);
				c3 = utftext.charCodeAt(i+2);
				string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
				i += 3;
			}
		}
		return string;
	}
}
/*
	$(".custom-drop").find(".dropitem").removeClass("ui-draggable ui-resizable").find(".ui-resizable-handle").remove();
	*/


//================表格宽度拖拽===============
function registerTableDragEvent() {
	function isNullOrUndefined(obj) { if (typeof(obj) == "undefined" || obj == null) { return true; } return false; }
	var dragTH; //记录拖拽的列
	//第一步按下
	$('.dropitem .skulist-table th').mousedown(function(e) {
		e = e || window.event;
		if (e.offsetX > $(this).innerWidth() - 10) {
			dragTH = $(this);
			dragTH.mouseDown = true;
			dragTH.oldX = e.pageX || e.clientX;
			dragTH.oldWidth = $(this).innerWidth();
		}
	})
	//第二步 拖动
	$('.dropitem .skulist-table th').mousemove(function(e) {
		//改鼠标样式
		if (e.offsetX > $(this).innerWidth() - 10) { $(this).css({ cursor: "e-resize" }); } else { $(this).css({ cursor: "default" }); }
		if (isNullOrUndefined(dragTH)) { dragTH = $(this); }
		if (!isNullOrUndefined(dragTH.mouseDown) && dragTH.mouseDown == true) {
//                    if(e.pageX>987 || e.clientX >987) {return false;}
			var difference = (e.pageX - dragTH.oldX) || (e.clientX - dragTH.oldX);
			var newWidth = dragTH.oldWidth + difference; //新的宽度
			dragTH.width(newWidth)
		}
	})
	// 第三步，释放
	$(".dropitem .skulist-table th").mouseup(function(e) {
		dragTH.mouseDown = false;
		//更新到溢出表格
		if($(".custom-content .label-content.overflow").length > 0){
			$(".custom-content .label-content.overflow thead").html($(this).parent().html());
		}
	});
}