<template>
<div>
<div class="label-set">
    <form class="panel panel-default tab-content" autocomplete="off">
        <div class="panel panel-default tab-content">
            <div class="panel-heading">
                <p class="panel-title"></p>
            </div>
            <div class="panel-toolbar-wrapper">
                <div class="panel-toolbar">
                    <ul class="nav nav-tabs nav-justified semibold">
                        <li class="title"><a data-toggle="tab" href="#titelset">标题</a></li>
                        <li class="detail"><a data-toggle="tab" href="#detailset">内容</a></li>
                        <li class="date"><a data-toggle="tab" href="#dateset">日期</a></li>
                        <li class="field-address"><a data-toggle="tab" href="#fieldset-address">字段</a></li>
                        <li class="text"><a data-toggle="tab" href="#textset">文字</a></li>
                        <li class="imgurl"><a data-toggle="tab" href="#imageurl">路径</a></li>
                        <li class="img-upload"><a data-toggle="tab" href="#imageupload">上传</a></li>
                        <li class="field-item"><a data-toggle="tab" href="#fieldset-item">字段</a></li>
                        <li class="border"><a data-toggle="tab" href="#borderset">边框</a></li>
                        <li class="table"><a data-toggle="tab" href="#tableset">表格</a></li>
                        <li class="field-sku"><a data-toggle="tab" href="#fieldset-sku">字段</a></li>
                        <li class="field-platformsku"><a data-toggle="tab" href="#fieldset-platformsku">字段</a></li>
                        <li class="field-invoiceskulist"><a data-toggle="tab" href="#fieldset-invoiceskulist">字段</a>
                        </li>
                        <li class="field-declare"><a data-toggle="tab" href="#fieldset-declare">字段</a></li>
                        <li class="field-eubdeclare"><a data-toggle="tab" href="#fieldset-eubdeclare">字段</a></li>
                        <li class="field-eubdeclaresingle"><a data-toggle="tab" href="#fieldset-eubdeclaresingle">字段</a>
                        </li>
                        <li class="field-declareSku"><a data-toggle="tab" href="#fieldset-declareSku">字段</a></li>
                    </ul>
                </div>
            </div>
            <div class="panel-body tab-pane" id="titelset">
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="" name="" id="viewTitle"
                                                                   checked="">显示标题</label>
                </div>
                <div class="title-set">
                    <div class="form-group">
                        <p class="control-label mb5">标题文本:</p>
                        <input type="text" value="" name="" class="form-control" id="titleName">
                    </div>
                    <div class="form-group">
                        <label class="checkbox-inline semibold"><input type="checkbox" value="" name=""
                                                                       id="titleNowrap">标题整行显示</label>
                    </div>
                    <div class="moreinfo dis-none">
                        <div class="form-group">
                            <p class="control-label mb5">标题对齐方式:</p>
                            <select class="form-control" id="titleAlign">
                                <option value="left" selected="">左对齐</option>
                                <option value="center">中对齐</option>
                                <option value="right">右对齐</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <p class="control-label mb5">标题与内容间距:</p>
                            <select class="form-control" id="titlePaddingBottom">
                                <option value="0" selected="">0px</option>
                                <option value="1">1px</option>
                                <option value="2">2px</option>
                                <option value="3">3px</option>
                                <option value="4">4px</option>
                                <option value="5">5px</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <p class="control-label mb5">标题文字字体:</p>
                        <select class="form-control" id="titleFontFamily">
                            <option value="Arial" selected="">Arial</option>
                            <option value="Helvetica">Helvetica</option>
                            <option value="Tahoma">Tahoma</option>
                            <option value="Verdana">Verdana</option>
                            <option value="Lucida Grande">Lucida Grande</option>
                            <option value="Times New Roman">Times New Roman</option>
                            <option value="Georgia">Georgia</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <p class="control-label mb5">标题文字尺寸:</p>
                        <select class="form-control" id="titleFontSize">
                            <option value="6px">6px</option>
                            <option value="8px">8px</option>
                            <option value="9px">9px</option>
                            <option value="10px">10px</option>
                            <option value="11px">11px</option>
                            <option value="12px" selected="">12px</option>
                            <option value="14px">14px</option>
                            <option value="18px">18px</option>
                            <option value="24px">24px</option>
                            <option value="30px">30px</option>
                            <option value="36px">36px</option>
                            <option value="48px">48px</option>
                            <option value="60px">60px</option>
                            <option value="72px">72px</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <p class="control-label mb5">标题行距:</p>
                        <select class="form-control" id="titleLineHeight">
                            <option value="1" selected="">1倍</option>
                            <option value="1.5">1.5倍</option>
                            <option value="2">2倍</option>
                            <option value="2.5">2.5倍</option>
                            <option value="3">3倍</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label class="checkbox-inline semibold"><input type="checkbox" value="" name=""
                                                                       id="titleFontWeight" checked="">标题文字加粗</label>
                    </div>
                </div>
            </div>
            <div class="panel-body tab-pane" id="detailset">
                <div class="form-group">
                    <p class="control-label mb5">内容文字对齐方式:</p>
                    <select class="form-control" id="detailAlign">
                        <option value="left" selected="">左对齐</option>
                        <option value="center">中对齐</option>
                        <option value="right">右对齐</option>
                    </select>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">内容文字字体:</p>
                    <select class="form-control" id="detailFontFamily">
                        <option value="Arial" selected="">Arial</option>
                        <option value="Helvetica">Helvetica</option>
                        <option value="Tahoma">Tahoma</option>
                        <option value="Verdana">Verdana</option>
                        <option value="Lucida Grande">Lucida Grande</option>
                        <option value="Times New Roman">Times New Roman</option>
                        <option value="Georgia">Georgia</option>
                    </select>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">内容文字尺寸:</p>
                    <select class="form-control" id="detailFontSize">
                        <option value="6px">6px</option>
                        <option value="8px">8px</option>
                        <option value="9px">9px</option>
                        <option value="10px">10px</option>
                        <option value="11px">11px</option>
                        <option value="12px" selected="">12px</option>
                        <option value="14px">14px</option>
                        <option value="18px">18px</option>
                        <option value="24px">24px</option>
                        <option value="30px">30px</option>
                        <option value="36px">36px</option>
                        <option value="48px">48px</option>
                        <option value="60px">60px</option>
                        <option value="72px">72px</option>
                    </select>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">内容文字行距:</p>
                    <select class="form-control" id="detailLineHeight">
                        <option value="1" selected="">1倍</option>
                        <option value="1.5">1.5倍</option>
                        <option value="2">2倍</option>
                        <option value="2.5">2.5倍</option>
                        <option value="3">3倍</option>
                    </select>
                </div>
                <div class="form-group product-list">
                    <p class="control-label mb5">商品列表排序:</p>
                    <select class="form-control sortType" data-id="itemlist">
                        <option value="default" selected="">默认排序</option>
                        <option value="warehousePositionASC">按仓位正序</option>
                    </select>
                </div>
                <div class="form-group product-list">
                    <p class="control-label mb5">是否合并相同SKU:</p>
                    <select class="form-control mergeSKU" data-id="itemlist">
                        <option value="default" selected="">否</option>
                        <option value="mergeSKU">是</option>
                    </select>
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="" name=""
                                                                   id="detailFontWeight">内容文字加粗</label>
                </div>
                <div class="form-group postUpper">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="" name="" id="postUpper">邮编默认大写</label>
                </div>
            </div>
            <div class="panel-body tab-pane" id="dateset">
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input value="" name="" id="viewYear" checked="checked"
                                                                   type="checkbox">显示年份</label>
                </div>
                <div class="form-group">
                    <p class="control-label mb5 mt5">分隔符号:</p>
                    <select class="form-control" id="dateType">
                        <option value="-" selected="">中横杠(-)</option>
                        <option value="/">斜线(/)</option>
                        <option value=".">逗点(.)</option>
                    </select>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">日期文字对齐方式:</p>
                    <select class="form-control" id="dateAlign">
                        <option value="left" selected="">左对齐</option>
                        <option value="center">中对齐</option>
                        <option value="right">右对齐</option>
                    </select>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">日期文字字体:</p>
                    <select class="form-control" id="dateFontFamily">
                        <option value="Arial" selected="">Arial</option>
                        <option value="Helvetica">Helvetica</option>
                        <option value="Tahoma">Tahoma</option>
                        <option value="Verdana">Verdana</option>
                        <option value="Lucida Grande">Lucida Grande</option>
                        <option value="Times New Roman">Times New Roman</option>
                        <option value="Georgia">Georgia</option>
                    </select>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">日期文字尺寸:</p>
                    <select class="form-control" id="dateFontSize">
                        <option value="6px">6px</option>
                        <option value="8px">8px</option>
                        <option value="9px">9px</option>
                        <option value="10px">10px</option>
                        <option value="11px">11px</option>
                        <option value="12px" selected="">12px</option>
                        <option value="14px">14px</option>
                        <option value="18px">18px</option>
                        <option value="24px">24px</option>
                        <option value="30px">30px</option>
                        <option value="36px">36px</option>
                        <option value="48px">48px</option>
                        <option value="60px">60px</option>
                        <option value="72px">72px</option>
                    </select>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">日期文字行距:</p>
                    <select class="form-control" id="dateLineHeight">
                        <option value="1" selected="">1倍</option>
                        <option value="1.5">1.5倍</option>
                        <option value="2">2倍</option>
                        <option value="2.5">2.5倍</option>
                        <option value="3">3倍</option>
                    </select>
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="" name="" id="dateFontWeight">日期文字加粗</label>
                </div>
            </div>
            <div class="panel-body tab-pane" id="textset">
                <div class="form-group">
                    <p class="control-label mb5">自定义文本:</p>
                    <textarea value="" class="form-control" id="textDetail" rows="5" placeholder="请输入文本内容"></textarea>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">文本对齐方式:</p>
                    <select class="form-control" id="textAlign">
                        <option value="left" selected="">左对齐</option>
                        <option value="center">中对齐</option>
                        <option value="right">右对齐</option>
                    </select>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">文本字体:</p>
                    <select class="form-control" id="textFontFamily">
                        <option value="Arial" selected="">Arial</option>
                        <option value="Helvetica">Helvetica</option>
                        <option value="Tahoma">Tahoma</option>
                        <option value="Verdana">Verdana</option>
                        <option value="Lucida Grande">Lucida Grande</option>
                        <option value="Times New Roman">Times New Roman</option>
                        <option value="Georgia">Georgia</option>
                    </select>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">文本文字尺寸:</p>
                    <select class="form-control" id="textFontSize">
                        <option value="6px">6px</option>
                        <option value="8px">8px</option>
                        <option value="9px">9px</option>
                        <option value="10px">10px</option>
                        <option value="11px">11px</option>
                        <option value="12px" selected="">12px</option>
                        <option value="14px">14px</option>
                        <option value="18px">18px</option>
                        <option value="24px">24px</option>
                        <option value="30px">30px</option>
                        <option value="36px">36px</option>
                        <option value="48px">48px</option>
                        <option value="60px">60px</option>
                        <option value="72px">72px</option>
                    </select>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">文本行距:</p>
                    <select class="form-control" id="textLineHeight">
                        <option value="1" selected="">1倍</option>
                        <option value="1.5">1.5倍</option>
                        <option value="2">2倍</option>
                        <option value="2.5">2.5倍</option>
                        <option value="3">3倍</option>
                    </select>
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="" name="" id="textFontWeight">文本文字加粗</label>
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="" name="" id="textCheckBox">含复选框
                        (<i class="ico-checkbox-unchecked3 pl5 pr5"></i>)</label>
                </div>
                <div class="form-group dis-none">
                    <select class="form-control" id="checkBoxType">
                        <option value="ico-checkbox-unchecked2" selected="">未勾选</option>
                        <option value="ico-checkbox-unchecked2 inner-checkmark">已勾选</option>
                        <option value="ico-checkbox-unchecked2 inner-close">已叉选</option>
                    </select>
                </div>
            </div>
            <div class="panel-body tab-pane" id="borderset">
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" id="borderTop" name="" value="">显示上边框</label>
                    <div class="row mt5">
                        <div class="col-sm-6">
                            <p class="control-label mb5">上边框厚度:</p>
                            <select class="form-control" id="borderTopWidth" disabled="">
                                <option value="0px">0px</option>
                                <option value="1px" selected="">1px</option>
                                <option value="2px">2px</option>
                                <option value="3px">3px</option>
                                <option value="4px">4px</option>
                                <option value="5px">5px</option>
                            </select>
                        </div>
                        <div class="col-sm-6">
                            <p class="control-label mb5">上边距:</p>
                            <select class="form-control" id="paddingTop" disabled="">
                                <option value="0px" selected="">0px</option>
                                <option value="1px">1px</option>
                                <option value="2px">2px</option>
                                <option value="3px">3px</option>
                                <option value="4px">4px</option>
                                <option value="5px">5px</option>
                                <option value="6px">6px</option>
                                <option value="7px">7px</option>
                                <option value="8px">8px</option>
                                <option value="9px">9px</option>
                                <option value="10px">10px</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" id="borderBottom" name="" value="">显示下边框</label>
                    <div class="row mt5">
                        <div class="col-sm-6">
                            <p class="control-label mb5">下边框厚度:</p>
                            <select class="form-control" id="borderBottomWidth" disabled="">
                                <option value="0px">0px</option>
                                <option value="1px" selected="">1px</option>
                                <option value="2px">2px</option>
                                <option value="3px">3px</option>
                                <option value="4px">4px</option>
                                <option value="5px">5px</option>
                            </select>
                        </div>
                        <div class="col-sm-6">
                            <p class="control-label mb5">下边距:</p>
                            <select class="form-control" id="paddingBottom" disabled="">
                                <option value="0px" selected="">0px</option>
                                <option value="1px">1px</option>
                                <option value="2px">2px</option>
                                <option value="3px">3px</option>
                                <option value="4px">4px</option>
                                <option value="5px">5px</option>
                                <option value="6px">6px</option>
                                <option value="7px">7px</option>
                                <option value="8px">8px</option>
                                <option value="9px">9px</option>
                                <option value="10px">10px</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" id="borderLeft" name="" value="">显示左边框</label>
                    <div class="row mt5">
                        <div class="col-sm-6">
                            <p class="control-label mb5">左边框厚度:</p>
                            <select class="form-control" id="borderLeftWidth" disabled="">
                                <option value="0px">0px</option>
                                <option value="1px" selected="">1px</option>
                                <option value="2px">2px</option>
                                <option value="3px">3px</option>
                                <option value="4px">4px</option>
                                <option value="5px">5px</option>
                            </select>
                        </div>
                        <div class="col-sm-6">
                            <p class="control-label mb5">左边距:</p>
                            <select class="form-control" id="paddingLeft" disabled="">
                                <option value="0px" selected="">0px</option>
                                <option value="1px">1px</option>
                                <option value="2px">2px</option>
                                <option value="3px">3px</option>
                                <option value="4px">4px</option>
                                <option value="5px">5px</option>
                                <option value="6px">6px</option>
                                <option value="7px">7px</option>
                                <option value="8px">8px</option>
                                <option value="9px">9px</option>
                                <option value="10px">10px</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" id="borderRight" name="" value="">显示右边框</label>
                    <div class="row mt5">
                        <div class="col-sm-6">
                            <p class="control-label mb5">右边框厚度:</p>
                            <select class="form-control" id="borderRightWidth" disabled="">
                                <option value="0px">0px</option>
                                <option value="1px" selected="">1px</option>
                                <option value="2px">2px</option>
                                <option value="3px">3px</option>
                                <option value="4px">4px</option>
                                <option value="5px">5px</option>
                            </select>
                        </div>
                        <div class="col-sm-6">
                            <p class="control-label mb5">右边距:</p>
                            <select class="form-control" id="paddingRight" disabled="">
                                <option value="0px" selected="">0px</option>
                                <option value="1px">1px</option>
                                <option value="2px">2px</option>
                                <option value="3px">3px</option>
                                <option value="4px">4px</option>
                                <option value="5px">5px</option>
                                <option value="6px">6px</option>
                                <option value="7px">7px</option>
                                <option value="8px">8px</option>
                                <option value="9px">9px</option>
                                <option value="10px">10px</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
            <div class="panel-body tab-pane" id="line-x">
                <div class="form-group">
                    <p class="control-label mb5">线条宽度(单位:px):</p>
                    <span class="customnum"> <a href="javascript:void(0);"
                                                class="subtract ico-minus-sign-alt"></a> <input id="xLineWidth"
                                                                                                type="text"
                                                                                                class="form-control"
                                                                                                onbeforepaste="clipboardData.setData(&#39;text&#39;,clipboardData.getData(&#39;text&#39;).replace(/[^\d]/g,&#39;&#39;))"
                                                                                                onkeyup="value=value.replace(/[^\d]/g,&#39;&#39;) "
                                                                                                value="366"> <a
                            href="javascript:void(0);" class="add ico-plus-sign2"></a>
							</span>
                    <button type="button" class="btn btn-primary ml10" id="setMaxWidth">设为100%</button>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">线条类型:</p>
                    <select class="form-control" id="xLineStyle">
                        <option value="solid" selected="">实线</option>
                        <option value="dotted">点状线</option>
                        <option value="dashed">虚线</option>
                        <option value="double">双实线</option>
                    </select>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">线条粗细:</p>
                    <select class="form-control" id="xLineWeight">
                        <option value="1px">1px</option>
                        <option value="2px" selected="">2px</option>
                        <option value="3px">3px</option>
                        <option value="4px">4px</option>
                        <option value="5px">5px</option>
                        <option value="6px">6px</option>
                    </select>
                </div>
            </div>
            <div class="panel-body tab-pane" id="line-y">
                <div class="form-group">
                    <p class="control-label mb5">线条高度(单位:px):</p>
                    <span class="customnum"> <a href="javascript:void(0);"
                                                class="subtract ico-minus-sign-alt"></a> <input id="yLineHeight"
                                                                                                type="text"
                                                                                                class="form-control"
                                                                                                value="366"> <a
                            href="javascript:void(0);" class="add ico-plus-sign2"></a>
							</span>
                    <button type="button" class="btn btn-primary" id="setMaxHeight">设为100%</button>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">线条类型:</p>
                    <select class="form-control" id="yLineStyle">
                        <option value="solid" selected="">实线</option>
                        <option value="dotted">点状线</option>
                        <option value="dashed">虚线</option>
                        <option value="double">双实线</option>
                    </select>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">线条粗细:</p>
                    <select class="form-control" id="yLineWeight">
                        <option value="1px">1px</option>
                        <option value="2px" selected="">2px</option>
                        <option value="3px">3px</option>
                        <option value="4px">4px</option>
                        <option value="5px">5px</option>
                        <option value="6px">6px</option>
                    </select>
                </div>
            </div>
            <div class="panel-body tab-pane" id="circletext">
                <div class="form-group">
                    <p class="control-label mb5">圆边粗细:</p>
                    <select class="form-control" id="circleBorderWidth">
                        <option value="1px">1px</option>
                        <option value="2px" selected="">2px</option>
                        <option value="3px">3px</option>
                        <option value="4px">4px</option>
                        <option value="5px">5px</option>
                        <option value="6px">6px</option>
                    </select>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">内容文本:</p>
                    <input type="text" value="A" name="" class="form-control" id="circleText">
                </div>
                <div class="form-group">
                    <p class="control-label mb5">内容文字字体:</p>
                    <select class="form-control" id="circleFontFamily">
                        <option value="Arial" selected="">Arial</option>
                        <option value="Helvetica">Helvetica</option>
                        <option value="Tahoma">Tahoma</option>
                        <option value="Verdana">Verdana</option>
                        <option value="Lucida Grande">Lucida Grande</option>
                        <option value="Times New Roman">Times New Roman</option>
                        <option value="Georgia">Georgia</option>
                    </select>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">内容文字尺寸:</p>
                    <select class="form-control" id="circleFontSize">
                        <option value="6px">6px</option>
                        <option value="8px">8px</option>
                        <option value="9px">9px</option>
                        <option value="10px">10px</option>
                        <option value="11px">11px</option>
                        <option value="12px">12px</option>
                        <option value="14px">14px</option>
                        <option value="18px">18px</option>
                        <option value="24px" selected="">24px</option>
                        <option value="30px">30px</option>
                        <option value="36px">36px</option>
                        <option value="48px">48px</option>
                        <option value="60px">60px</option>
                        <option value="72px">72px</option>
                    </select>
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="" name=""
                                                                   id="circleFontWeight" checked="">文字内容加粗</label>
                </div>
            </div>
            <div class="panel-body tab-pane" id="barcode">
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="" name="" id="viewCodeNum">显示条码代码</label>
                </div>
                <div class="codenum-set">
                    <div class="form-group dis-none" id="codeTextType">
                        <p class="control-label mb5">条码代码显示位置:</p>
                        <label class="radio-inline semibold"><input type="radio" name="codetexttype"
                                                                    value="leftCodeText" id="leftCodeText"
                                                                    checked="checked">条码左侧</label>
                        <label class="radio-inline semibold"><input type="radio" name="codetexttype"
                                                                    value="rightCodeText"
                                                                    id="rightCodeText">条码右侧</label>
                    </div>
                    <div class="form-group">
                        <p class="control-label mb5">条码代码文字对齐方式:</p>
                        <select class="form-control" id="codeNumAlign">
                            <option value="left">左对齐</option>
                            <option value="center" selected="">中对齐</option>
                            <option value="right">右对齐</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <p class="control-label mb5">条码代码文字尺寸:</p>
                        <select class="form-control" id="codeNumFontSize">
                            <option value="6px">6px</option>
                            <option value="8px">8px</option>
                            <option value="9px">9px</option>
                            <option value="10px">10px</option>
                            <option value="11px">11px</option>
                            <option value="12px" selected="">12px</option>
                            <option value="14px">14px</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label class="checkbox-inline semibold"><input type="checkbox" value="" name=""
                                                                       id="codeNumFontWeight"
                                                                       checked="">条码代码文字加粗</label>
                    </div>
                    <div class="form-group">
                        <p class="control-label mb5">条码代码前缀:</p>
                        <input type="text" value="" name="" class="form-control" id="codePrefix">
                    </div>
                    <!--	<div class="form-group">
								<label class="checkbox-inline bold"><input type="checkbox" value="" name="" id="viewCodeCustomText" checked="">显示自定义条码文字</label><br>
								<input type="text" value="" name="" class="form-control mt5" id="codeCustomText" disabled="disabled">
							</div>-->
                </div>
            </div>
            <!--二维码-->
            <div class="panel-body tab-pane" id="qRcode">
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="" name="" id="qRviewCodeNum">显示二维码代码</label>
                </div>
                <div class="codenum-set">
                    <div class="form-group dis-none" id="qrcodeTextType">
                        <p class="control-label mb5">二维码代码显示位置:</p>
                        <label class="radio-inline semibold"><input type="radio" name="codetexttype"
                                                                    value="qRleftCodeText" id="qRleftCodeText"
                                                                    checked="checked">二维码左侧</label>
                        <label class="radio-inline semibold"><input type="radio" name="codetexttype"
                                                                    value="qRrightCodeText"
                                                                    id="qRrightCodeText">二维码右侧</label>
                    </div>
                    <div class="form-group">
                        <p class="control-label mb5">二维码代码文字对齐方式:</p>
                        <select class="form-control" id="qRcodeNumAlign">
                            <option value="left">左对齐</option>
                            <option value="center" selected="">中对齐</option>
                            <option value="right">右对齐</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <p class="control-label mb5">二维码代码文字尺寸:</p>
                        <select class="form-control" id="qRcodeNumFontSize">
                            <option value="6px">6px</option>
                            <option value="8px">8px</option>
                            <option value="9px">9px</option>
                            <option value="10px">10px</option>
                            <option value="11px">11px</option>
                            <option value="12px" selected="">12px</option>
                            <option value="14px">14px</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label class="checkbox-inline semibold"><input type="checkbox" value="" name=""
                                                                       id="qRcodeNumFontWeight"
                                                                       checked="">二维码代码文字加粗</label>
                    </div>
                    <div class="form-group">
                        <p class="control-label mb5">二维码代码前缀:</p>
                        <input type="text" value="" name="" class="form-control" id="qRcodePrefix">
                    </div>
                    <!--<div class="form-group">
								<label class="checkbox-inline bold"><input type="checkbox" value="" name="" id="qRviewCodeCustomText" checked="">显示自定义二维码文字</label><br>
								<input type="text" value="" name="" class="form-control mt5" id="qRcodeCustomText" disabled="disabled">
							</div> -->

                </div>
            </div>
            <div class="panel-body tab-pane" id="tableset">
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="" name="" id="viewTdBorder"
                                                                   checked="">显示表格框线</label>
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="" name="" id="viewThead"
                                                                   checked="">显示表头</label>
                </div>
                <div class="moreinfo">
                    <div class="form-group">
                        <p class="control-label mb5">表头文字字体:</p>
                        <select class="form-control" id="theadFontFamily">
                            <option value="Arial" selected="">Arial</option>
                            <option value="Helvetica">Helvetica</option>
                            <option value="Tahoma">Tahoma</option>
                            <option value="Verdana">Verdana</option>
                            <option value="Lucida Grande">Lucida Grande</option>
                            <option value="Times New Roman">Times New Roman</option>
                            <option value="Georgia">Georgia</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <p class="control-label mb5">表头文字尺寸:</p>
                        <select class="form-control" id="theadFontSize">
                            <option value="6px">6px</option>
                            <option value="8px">8px</option>
                            <option value="9px">9px</option>
                            <option value="10px">10px</option>
                            <option value="11px">11px</option>
                            <option value="12px" selected="">12px</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">表格内容文字字体:</p>
                    <select class="form-control" id="tbodyFontFamily">
                        <option value="Arial" selected="">Arial</option>
                        <option value="Helvetica">Helvetica</option>
                        <option value="Tahoma">Tahoma</option>
                        <option value="Verdana">Verdana</option>
                        <option value="Lucida Grande">Lucida Grande</option>
                        <option value="Times New Roman">Times New Roman</option>
                        <option value="Georgia">Georgia</option>
                    </select>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">表格内容文字尺寸:</p>
                    <select class="form-control" id="tbodyFontSize">
                        <option value="6px">6px</option>
                        <option value="8px">8px</option>
                        <option value="9px">9px</option>
                        <option value="10px">10px</option>
                        <option value="11px">11px</option>
                        <option value="12px" selected="">12px</option>
                    </select>
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="" name="" id="viewTfoot"
                                                                   checked="">显示脚注</label>
                </div>
                <div class="moreinfo">
                    <div class="form-group">
                        <p class="control-label mb5">脚注文字字体:</p>
                        <select class="form-control" id="tfootFontFamily">
                            <option value="Arial" selected="">Arial</option>
                            <option value="Helvetica">Helvetica</option>
                            <option value="Tahoma">Tahoma</option>
                            <option value="Verdana">Verdana</option>
                            <option value="Lucida Grande">Lucida Grande</option>
                            <option value="Times New Roman">Times New Roman</option>
                            <option value="Georgia">Georgia</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <p class="control-label mb5">脚注文字尺寸:</p>
                        <select class="form-control" id="tfootFontSize">
                            <option value="6px">6px</option>
                            <option value="8px">8px</option>
                            <option value="9px">9px</option>
                            <option value="10px">10px</option>
                            <option value="11px">11px</option>
                            <option value="12px" selected="">12px</option>
                        </select>
                    </div>
                </div>
                <div class="form-group product-list">
                    <p class="control-label mb5">商品列表排序:</p>
                    <select class="form-control sortType" data-id="skulist">
                        <option value="default" selected="">默认排序</option>
                        <option value="warehousePositionASC">按仓位正序</option>
                    </select>
                </div>
                <div class="form-group product-list">
                    <p class="control-label mb5">是否合并相同SKU:</p>
                    <select class="form-control mergeSKU" data-id="skulist">
                        <option value="default" selected="">否</option>
                        <option value="mergeSKU">是</option>
                    </select>
                </div>
            </div>
            <div class="panel-body tab-pane" id="fieldset-address">
                <div class="form-group multiple">
                    <p class="control-label mb5">显示字段设置:</p>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="name" name="viewField"
                                                                   checked="">显示姓名</label> <label
                        class="checkbox-inline semibold"><input type="checkbox" value="street" name="viewField"
                                                                checked="">显示街道</label> <label
                        class="checkbox-inline semibold ml10"><input type="checkbox" value="doorcode" name="viewField">显示门牌号</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="area" name="viewField"
                                                                   checked="">显示省份/城市/地区</label><label
                        class="checkbox-inline semibold"><input type="checkbox" value="postcode" name="viewField"
                                                                checked="">显示邮编</label> <label
                        class="checkbox-inline semibold"><input type="checkbox" value="country" name="viewField"
                                                                checked="">显示国家</label> <label
                        class="checkbox-inline semibold ml10"><input type="checkbox" value="country_cn"
                                                                     name="viewField">显示国家中文名</label> <label
                        class="checkbox-inline semibold"><input type="checkbox" value="tel1" name="viewField"
                                                                checked="">显示电话1</label> <label
                        class="checkbox-inline semibold"><input type="checkbox" value="tel2" name="viewField"
                                                                checked="">显示电话2</label> <label
                        class="checkbox-inline semibold"><input type="checkbox" value="email" name="viewField"
                                                                checked="">显示E-mail</label>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">字段换行设置:</p>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="" name="" id="newline"
                                                                   checked="">单字段换行</label>
                </div>
            </div>
            <div class="panel-body tab-pane" id="fieldset-sku">
                <!-- <div class="form-group">
							<label class="checkbox-inline semibold"><input type="checkbox" value="photo" name="viewField">显示商品缩略图</label> <input type="text" id="fieldTextPhoto" class="form-control mt5" name="" value="图片" disabled="">
						</div> -->
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="sku" name="viewField"
                                                                   checked="">显示商品编号</label> <input type="text"
                                                                                                    id="fieldTextSku"
                                                                                                    class="form-control mt5"
                                                                                                    name="" value="SKU">
                </div>
                <!-- <div class="form-group">
							<label class="checkbox-inline semibold"><input type="checkbox" value="sku_original" name="viewField">显示原厂编号</label> <input type="text" id="fieldTextOriginal" class="form-control mt5" name="" value="原厂SKU" disabled="">
						</div>
						<div class="form-group">
							<label class="checkbox-inline semibold"><input type="checkbox" value="itemid" name="viewField">显示itemID</label> <input type="text" id="fieldTextItemid" class="form-control mt5" name="" value="itemId" disabled="">
						</div>
						<div class="form-group">
							<label class="checkbox-inline semibold"><input type="checkbox" value="sku_platform" name="viewField">显示平台SKU</label> <input type="text" id="fieldTextPlatform" class="form-control mt5" name="" value="平台SKU" disabled="">
						</div>
						<div class="form-group">
							<label class="checkbox-inline semibold"><input type="checkbox" value="sku_combo" name="viewField">显示组合SKU</label> <input type="text" id="fieldTextComboSku" class="form-control mt5" name="" value="组合SKU" disabled="">
						</div> -->
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="name" name="viewField"
                                                                   checked="">显示中文名称</label> <input type="text"
                                                                                                    id="fieldTextName"
                                                                                                    class="form-control mt5"
                                                                                                    name="" value="名称">
                </div>
                <!-- <div class="form-group">
							<label class="checkbox-inline semibold"><input type="checkbox" value="name_en" name="viewField">显示英文名称</label> <input type="text" id="fieldTextNameEn" class="form-control mt5" name="" value="name" disabled="">
						</div>
						<div class="form-group">

							<label class="checkbox-inline semibold"><input type="checkbox" value="warehouse" name="viewField">显示仓库</label> <input type="text" id="fieldTextWarehouse" class="form-control mt5" name="" value="仓库" disabled="">
						</div>
						<div class="form-group">
							<label class="checkbox-inline semibold"><input type="checkbox" value="position" name="viewField" checked="">显示仓位</label> <input type="text" id="fieldTextPosition" class="form-control mt5" name="" value="仓位">
						</div>
						<div class="form-group">
							<label class="checkbox-inline semibold"><input type="checkbox" value="orderItem_package" name="viewField">显示包材名称</label> <input type="text" id="fieldTextOrderItemPackage" class="form-control mt5" name="" value="包材" disabled="">
						</div> -->
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="number" name="viewField"
                                                                   checked="">显示数量</label> <input type="text"
                                                                                                  id="fieldTextNumber"
                                                                                                  class="form-control mt5"
                                                                                                  name="" value="数量">
                </div>
                <!-- <div class="form-group">
							<label class="checkbox-inline semibold"><input type="checkbox" value="weight" name="viewField">显示重量</label> <input type="text" id="fieldTextWeight" class="form-control mt5" name="" value="重量(kg)" disabled="">
						</div>
						<div class="form-group">
							<label class="checkbox-inline semibold"><input type="checkbox" value="multi-property" name="viewField">显示多属性</label> <input type="text" id="fieldTextMultiproperty" class="form-control mt5" name="" value="多属性" disabled="">
						</div>
						<div class="form-group">
							<label class="checkbox-inline semibold"><input type="checkbox" value="product_unit" name="viewField">显示平台单位</label> <input type="text" id="fieldTextProductUnit" class="form-control mt5" name="" value="平台单位" disabled="">
						</div>
						<div class="form-group">
							<label class="checkbox-inline semibold"><input type="checkbox" value="salesPrice" name="viewField">显示售价</label> <input type="text" id="fieldTextSalesPrice" class="form-control mt5" name="" value="售价" disabled="">
						</div>
						<div class="form-group">
							<label class="checkbox-inline semibold"><input type="checkbox" value="price" name="viewField">显示单价</label> <input type="text" id="fieldTextPrice" class="form-control mt5" name="" value="单价" disabled="">
						</div>
						<div class="form-group">
							<label class="checkbox-inline semibold"><input type="checkbox" value="total" name="viewField">显示金额小计</label> <input type="text" id="fieldTextTotal" class="form-control mt5" name="" value="小计" disabled="">
						</div>
						<div class="form-group">
							<label class="checkbox-inline semibold"><input type="checkbox" value="totalSalesPrice" name="viewField">显示总售价</label> <input type="text" id="fieldTextTotalSalesPrice" class="form-control mt5" name="" value="总售价" disabled="">
						</div>
						<div class="form-group">
							<label class="checkbox-inline semibold"><input type="checkbox" value="total-sku" name="viewField" checked="">显示商品种类</label> <input type="text" id="fieldTextTotalKind" class="form-control mt5" name="" value="个商品种类">
						</div>
						<div class="form-group">
							<label class="checkbox-inline semibold"><input type="checkbox" value="total-number" name="viewField" checked="">显示件数</label> <input type="text" id="fieldTextTotalQuantity" class="form-control mt5" name="" value="件商品">
						</div> -->
            </div>
            <div class="panel-body tab-pane" id="fieldset-platformsku">
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="sku_platform" name="viewField"
                                                                   checked="">显示平台SKU</label> <input type="text"
                                                                                                     id="fieldTextPlatform2"
                                                                                                     class="form-control mt5"
                                                                                                     name=""
                                                                                                     value="平台SKU">
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="warehouse" name="viewField"
                                                                   checked="">显示仓库</label> <input type="text"
                                                                                                  id="fieldTextWarehouse2"
                                                                                                  class="form-control mt5"
                                                                                                  name="" value="仓库">
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="position" name="viewField">显示仓位</label>
                    <input type="text" id="fieldTextPosition2" class="form-control mt5" name="" value="仓位" disabled="">
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="orderItem_package"
                                                                   name="viewField">显示包材名称</label> <input type="text"
                                                                                                          id="fieldTextOrderItemPackage2"
                                                                                                          class="form-control mt5"
                                                                                                          name=""
                                                                                                          value="包材"
                                                                                                          disabled="">
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="number" name="viewField"
                                                                   checked="">显示数量</label> <input type="text"
                                                                                                  id="fieldTextNumber2"
                                                                                                  class="form-control mt5"
                                                                                                  name="" value="数量">
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="weight" name="viewField">显示重量</label>
                    <input type="text" id="fieldTextWeight2" class="form-control mt5" name="" value="重量(kg)"
                           disabled="">
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="multi-property"
                                                                   name="viewField" checked="">显示多属性</label> <input
                        type="text" id="fieldTextMultiproperty2" class="form-control mt5" name="" value="多属性">
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="product_unit"
                                                                   name="viewField">显示平台单位</label> <input type="text"
                                                                                                          id="fieldTextProductUnit2"
                                                                                                          class="form-control mt5"
                                                                                                          name=""
                                                                                                          value="平台单位"
                                                                                                          disabled="">
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="salesPrice" name="viewField">显示售价</label>
                    <input type="text" id="fieldTextSalesPrice2" class="form-control mt5" name="" value="售价"
                           disabled="">
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="price"
                                                                   name="viewField">显示单价</label> <input type="text"
                                                                                                        id="fieldTextPrice2"
                                                                                                        class="form-control mt5"
                                                                                                        name=""
                                                                                                        value="单价"
                                                                                                        disabled="">
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="total" name="viewField">显示金额小计</label>
                    <input type="text" id="fieldTextTotal2" class="form-control mt5" name="" value="小计" disabled="">
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="totalSalesPrice"
                                                                   name="viewField">显示总售价</label> <input type="text"
                                                                                                         id="fieldTextTotalSalesPrice2"
                                                                                                         class="form-control mt5"
                                                                                                         name=""
                                                                                                         value="总售价"
                                                                                                         disabled="">
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="total-sku" name="viewField"
                                                                   checked="">显示商品种类</label> <input type="text"
                                                                                                    id="fieldTextTotalKind2"
                                                                                                    class="form-control mt5"
                                                                                                    name=""
                                                                                                    value="个商品种类">
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="total-number" name="viewField"
                                                                   checked="">显示件数</label> <input type="text"
                                                                                                  id="fieldTextTotalQuantity2"
                                                                                                  class="form-control mt5"
                                                                                                  name="" value="件商品">
                </div>
            </div>
            <div class="panel-body tab-pane" id="fieldset-invoiceskulist">
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="name" name="viewField"
                                                                   checked="">显示中文名称</label> <input type="text"
                                                                                                    id="fieldTextName3"
                                                                                                    class="form-control mt5"
                                                                                                    name="" value="名称">
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="originTitle" name="viewField">显示原始标题</label>
                    <input type="text" id="fieldTextOriginTitle" class="form-control mt5" name="" value="原始标题"
                           disabled="">
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="productId" name="viewField">显示产品ID</label>
                    <input type="text" id="fieldTextProductId" class="form-control mt5" name="" value="产品ID"
                           disabled="">
                </div>

                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="sellPriceOrigin"
                                                                   name="viewField" checked="">显示售价</label> <input
                        type="text" id="fieldTextSalesPrice3" class="form-control mt5" name="" value="售价">
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="number" name="viewField"
                                                                   checked="">显示数量</label> <input type="text"
                                                                                                  id="fieldTextNumber3"
                                                                                                  class="form-control mt5"
                                                                                                  name="" value="数量">
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="excludTaxPrice"
                                                                   name="viewField" checked="">显示不含税售价</label> <input
                        type="text" id="fieldTextExcludTaxPrice" class="form-control mt5" name="" value="不含税售价">
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="tax" name="viewField"
                                                                   checked="">显示税</label> <input type="text"
                                                                                                 id="fieldTextTax"
                                                                                                 class="form-control mt5"
                                                                                                 name="" value="税">
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="includTaxPrice"
                                                                   name="viewField" checked="">显示含税价</label> <input
                        type="text" id="fieldTextIncludTaxPrice" class="form-control mt5" name="" value="含税价">
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="totalSalesPrice"
                                                                   name="viewField" checked="">显示总售价</label> <input
                        type="text" id="fieldTextTotalSalesPrice3" class="form-control mt5" name="" value="总售价">
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="total-sku" name="viewField"
                                                                   checked="">显示总金额</label> <input type="text"
                                                                                                   id="fieldTextTotalPrice"
                                                                                                   class="form-control mt5"
                                                                                                   name="" value="总金额">
                </div>
                <div>
                    <label class="semibold">税金比例(%)</label> <input type="text" id="taxRatio" class="form-control"
                                                                   name="" value="100"
                                                                   onkeyup="this.value=this.value.replace(/\D/g,&#39;&#39;)">
                </div>
            </div>
            <div class="panel-body tab-pane" id="fieldset-declare">
                <div class="form-group">
                    <p class="control-label mb5">报关品名表头文字:</p>
                    <textarea placeholder="请输入文本内容" rows="5" id="declareNameTitle" class="form-control"
                              value=""></textarea>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">报关品名显示内容:</p>
                    <select class="form-control" id="declareType">
                        <option value="sort" selected="">申报品名</option>
                        <option value="group">多字段组合</option>
                        <option value="custom">自定义文字</option>
                    </select>
                </div>
                <div id="declareName">
                    <div class="form-group multiple sort" style="display: block;">
                        <p class="control-label mb5">商品目录内容格式:</p>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="sort" name="sorttype"
                                                                       checked="">显示申报中文名</label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="sort_en" name="sorttype"
                                                                       checked="">显示申报英文名</label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="sort_sku" name="sorttype"
                                                                       checked="">商品sku</label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="sort_number"
                                                                       name="sorttype" checked="">商品数量</label>
                    </div>
                    <div class="form-group multiple group">
                        <p class="control-label mb5">多字段组合内容格式:</p>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="sku" name="catalogue"
                                                                       checked="">显示商品编号</label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="sku_original"
                                                                       name="catalogue"></label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="itemid" name="catalogue">显示itemID</label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="sku_platform"
                                                                       name="catalogue">显示平台SKU</label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="name" name="catalogue"
                                                                       checked="">显示商品中文名</label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="name_en" name="catalogue">显示商品英文名</label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="number" name="catalogue"
                                                                       checked="">显示数量</label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="warehouse"
                                                                       name="catalogue">显示仓库</label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="position"
                                                                       name="catalogue">显示仓位</label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="weight" name="catalogue">显示重量</label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="salesPrice"
                                                                       name="catalogue">显示售价</label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="price" name="catalogue">显示单价</label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="total" name="catalogue">显示金额小计</label>
                    </div>
                    <div class="form-group multiple custom">
                        <p class="control-label mb5">:</p>
                        <textarea placeholder="请输入文本内容" rows="5" id="declareNameCustom"
                                  class="form-control">自定义内容</textarea>
                    </div>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">报关重量表头文字:</p>
                    <textarea placeholder="请输入文本内容" rows="2" id="declareWeightTitle" class="form-control"
                              value=""></textarea>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">报关价值表头文字:</p>
                    <textarea placeholder="请输入文本内容" rows="2" id="declarePriceTitle" class="form-control"
                              value=""></textarea>
                </div>

                <label class="checkbox-inline semibold mb5"><input type="checkbox" checked="" id="viewTfootTitle"
                                                                   name="viewTfootTitle" value="">显示脚注标题行</label>
                <div class="declare-tfoot">
                    <div class="form-group">
                        <p class="control-label mb5">原产国表头文字:</p>
                        <textarea placeholder="请输入文本内容" rows="5" id="declareOriginTitle" class="form-control"
                                  value=""></textarea>
                    </div>
                    <div class="form-group">
                        <p class="control-label mb5">总重量表头文字:</p>
                        <textarea placeholder="请输入文本内容" rows="2" id="declareTotalWeightTitle" class="form-control"
                                  value=""></textarea>
                    </div>
                    <div class="form-group">
                        <p class="control-label mb5">总价值表头文字:</p>
                        <textarea placeholder="请输入文本内容" rows="2" id="declareTotalPriceTitle" class="form-control"
                                  value=""></textarea>
                    </div>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">原产国缩写内容文字:</p>
                    <textarea placeholder="请输入文本内容" rows="2" id="declareOriginCountryCode" class="form-control"
                              value=""></textarea>
                </div>

            </div>
            <div class="panel-body tab-pane" id="fieldset-eubdeclaresingle">
                <div class="form-group">
                    <p class="control-label mb5">SKU数量表头文字:</p>
                    <textarea placeholder="请输入文本内容" rows="2" id="eubDeclareSingleNoTitle" class="form-control"
                              value=""></textarea>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">产品总数量表头文字:</p>
                    <textarea placeholder="请输入文本内容" rows="2" id="eubDeclareSingleQtyTitle" class="form-control"
                              value=""></textarea>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">报关品名表头文字:</p>
                    <textarea placeholder="请输入文本内容" rows="5" id="eubDeclareSingleNameTitle" class="form-control"
                              value=""></textarea>
                </div>
                <div class="form-group">
                    <label class="checkbox-inline semibold"><input type="checkbox" value="custom"
                                                                   id="eubDeclareSingleCustom"
                                                                   name="eubDeclareSingleCustom">启用固定申报中英文名</label>
                    <textarea placeholder="请输入文本内容" rows="5" id="eubDeclareSingleCustomContent" class="form-control"
                              value="" disabled=""></textarea>
                </div>
                <div class="form-group multiple">
                    <p class="control-label mb5">申报品名格式:</p>
                    <label class="checkbox-inline semibold"><input value="sort" name="eubDeclareSingleSort" checked=""
                                                                   type="checkbox">申报品名中文</label>
                    <label class="checkbox-inline semibold"><input value="sort_en" name="eubDeclareSingleSort"
                                                                   checked="" type="checkbox">申报品名英文</label>
                </div>
                <div class="form-group multiple">
                    <p class="control-label mb5">多字段组合内容格式:</p>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="sku"
                                                                   name="eubDeclareSingleType" checked="">显示商品编号</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="sku_original"
                                                                   name="eubDeclareSingleType">显示原厂编号</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="itemid"
                                                                   name="eubDeclareSingleType">显示itemID</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="sku_platform"
                                                                   name="eubDeclareSingleType">显示平台SKU</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="name"
                                                                   name="eubDeclareSingleType">显示商品中文名</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="name_en"
                                                                   name="eubDeclareSingleType">显示商品英文名</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="number"
                                                                   name="eubDeclareSingleType" checked="">显示数量</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="warehouse"
                                                                   name="eubDeclareSingleType">显示仓库</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="position"
                                                                   name="eubDeclareSingleType">显示仓位</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="weight"
                                                                   name="eubDeclareSingleType">显示重量</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="salesPrice"
                                                                   name="eubDeclareSingleType">显示售价</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="price"
                                                                   name="eubDeclareSingleType">显示单价</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="salesTotal"
                                                                   name="eubDeclareSingleType">显示售价小计</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="total"
                                                                   name="eubDeclareSingleType">显示金额小计</label>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">报关总重量表头文字:</p>
                    <textarea placeholder="请输入文本内容" rows="2" id="eubDeclareSingleWeightTitle" class="form-control"
                              value=""></textarea>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">报关总价值表头文字:</p>
                    <textarea placeholder="请输入文本内容" rows="2" id="eubDeclareSinglePriceTitle" class="form-control"
                              value=""></textarea>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">原产国表头文字:</p>
                    <textarea placeholder="请输入文本内容" rows="2" id="eubDeclareSingleOriginTitle" class="form-control"
                              value=""></textarea>
                </div>
            </div>
            <div class="panel-body tab-pane" id="fieldset-eubdeclare">
                <div class="form-group">
                    <p class="control-label mb5">序号表头文字:</p>
                    <textarea placeholder="请输入文本内容" rows="2" id="eubDeclareNoTitle" class="form-control"
                              value=""></textarea>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">报关物品数量表头文字:</p>
                    <textarea placeholder="请输入文本内容" rows="2" id="eubDeclareQtyTitle" class="form-control"
                              value=""></textarea>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">报关品名表头文字:</p>
                    <textarea placeholder="请输入文本内容" rows="5" id="eubDeclareNameTitle" class="form-control"
                              value=""></textarea>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">报关品名显示内容:</p>
                    <select class="form-control" id="eubDeclareType">
                        <option value="sort" selected="">申报品名</option>
                        <option value="group">多字段组合</option>
                        <option value="custom">报关价值表头文字</option>
                    </select>
                </div>
                <div id="eubDeclareName">
                    <div class="form-group multiple sort" style="display:block;">
                        <p class="control-label mb5">商品目录内容格式:</p>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="sort" name="eubsorttype"
                                                                       checked="">申报品名中文</label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="sort_en"
                                                                       name="eubsorttype" checked="">申报品名英文</label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="sort_sku" name="sorttype"
                                                                       checked="">商品sku</label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="sort_number"
                                                                       name="eubsorttype" checked="">商品数量</label>
                    </div>
                    <div class="form-group multiple group">
                        <p class="control-label mb5">多字段组合内容格式:</p>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="sku" name="catalogue"
                                                                       checked="">显示商品编号</label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="sku_original"
                                                                       name="catalogue">显示原厂编号</label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="itemid" name="catalogue">显示itemID</label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="sku_platform"
                                                                       name="catalogue">显示平台SKU</label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="name" name="catalogue"
                                                                       checked="">显示商品中文名</label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="name_en" name="catalogue">显示商品英文名</label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="number" name="catalogue"
                                                                       checked="">显示数量</label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="warehouse"
                                                                       name="catalogue">显示仓库</label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="position"
                                                                       name="catalogue">显示仓位</label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="weight" name="catalogue">显示重量</label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="salesPrice"
                                                                       name="catalogue">显示售价</label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="price" name="catalogue">显示单价</label>
                        <label class="checkbox-inline semibold"><input type="checkbox" value="total" name="catalogue">显示金额小计</label>
                    </div>
                    <div class="form-group multiple custom">
                        <p class="control-label mb5">自定义文字内容:</p>
                        <textarea placeholder="请输入文本内容" rows="5" id="eubDeclareNameCustom"
                                  class="form-control">自定义内容</textarea>
                    </div>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">报关重量表头文字:</p>
                    <textarea placeholder="请输入文本内容" rows="2" id="eubDeclareWeightTitle" class="form-control"
                              value=""></textarea>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">报关价值表头文字:</p>
                    <textarea placeholder="请输入文本内容" rows="2" id="eubDeclarePriceTitle" class="form-control"
                              value=""></textarea>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">原产国表头文字:</p>
                    <textarea placeholder="请输入文本内容" rows="2" id="eubDeclareOriginTitle" class="form-control"
                              value=""></textarea>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">总重量表头文字:</p>
                    <textarea placeholder="请输入文本内容" rows="2" id="eubDeclareTotalWeightTitle" class="form-control"
                              value=""></textarea>
                </div>
            </div>
            <div class="panel-body tab-pane" id="fieldset-declareSku">
                <div class="form-group">
                    <p class="control-label mb5">报关品名表头文字:</p>
                    <textarea placeholder="请输入文本内容" rows="5" id="declareSkuNameTitle" class="form-control"
                              value=""></textarea>
                </div>
                <div class="form-group multiple">
                    <p class="control-label mb5">多字段组合内容格式:</p>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="sku" name="declareSkuSorttype"
                                                                   checked="">显示商品编号</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="sku_original"
                                                                   name="declareSkuSorttype">显示原厂编号</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="itemid"
                                                                   name="declareSkuSorttype">显示itemID</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="sku_platform"
                                                                   name="declareSkuSorttype">显示平台SKU</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="number"
                                                                   name="declareSkuSorttype" checked="">显示数量</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="name"
                                                                   name="declareSkuSorttype" checked="">显示商品中文名</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="sort"
                                                                   name="declareSkuSorttype">显示报中文关品名</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="name_en"
                                                                   name="declareSkuSorttype">显示商品英文名</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="sort_en"
                                                                   name="declareSkuSorttype">显示英文报关品名</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="warehouse"
                                                                   name="declareSkuSorttype">显示仓库</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="position"
                                                                   name="declareSkuSorttype">显示仓位</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="weight"
                                                                   name="declareSkuSorttype">显示重量</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="salesPrice" name="catalogue">显示售价</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="price"
                                                                   name="declareSkuSorttype">显示单价</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="total"
                                                                   name="declareSkuSorttype">显示金额小计</label>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">报关重量表头文字:</p>
                    <textarea placeholder="请输入文本内容" rows="2" id="declareSkuWeightTitle" class="form-control"
                              value=""></textarea>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">报关价值表头文字:</p>
                    <textarea placeholder="请输入文本内容" rows="2" id="declareSkuPriceTitle" class="form-control"
                              value=""></textarea>
                </div>
                <div class="form-group">
                    <p class="control-label mb5">总毛重脚注文字:</p>
                    <textarea placeholder="请输入文本内容" rows="2" id="declareSkuOriginTitle" class="form-control"
                              value=""></textarea>
                </div>
            </div>
            <div class="panel-body tab-pane" id="fieldset-item">
                <div class="form-group multiple">
                    <p class="control-label mb5">显示字段设置:</p>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="sku" name="itemField"
                                                                   checked="">显示sku编号</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="sku_original"
                                                                   name="itemField">显示原厂编号</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="itemid" name="itemField">显示itemID</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="sku_platform"
                                                                   name="itemField">显示平台SKU</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="name" name="itemField"
                                                                   checked="">显示商品中文名</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="name_en" name="itemField">显示商品英文名</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="number" name="itemField"
                                                                   checked="">显示数量</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="warehouse" name="itemField">显示仓库</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="position" name="itemField">显示仓位</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="provider" name="itemField">显示供应商</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="weight" name="itemField">显示重量</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="multi-property"
                                                                   name="itemField">显示多属性</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="salesPrice" name="itemField">显示售价</label>
                    <label class="checkbox-inline semibold"><input type="checkbox" value="price"
                                                                   name="itemField">显示单价</label>
                    <!-- 						<label class="checkbox-inline semibold"><input type="checkbox" value="total" name="itemField">显示金额小计</label> -->
                </div>
                <div class="form-group">
                    <p class="control-label mb5">商品换行设置:</p>
                    <label class="checkbox-inline semibold"><input type="checkbox" checked="checked" id="newItemline"
                                                                   name="" value="">单Sku换行</label>
                </div>
            </div>
            <div class="panel-body tab-pane" id="imageurl">
                <div class="form-group">
                    <p class="control-label mb5">在线地址:</p>
                    <textarea placeholder="请输入图片在线路径" rows="3" id="imageUrl" class="form-control" value=""></textarea>
                    <button type="button" class="btn btn-default mt5 pl10 pr10" id="loadImgUrl">加载</button>
                    <!-- <p class="text-muted mt10"
								style="word-break: break-all; word-wrap: break-word;">
								可输入以下路径测试: <small class="help-block text-danger mb0">http://my.deerapp.com/images/index-v2.0/logo.png</small>
								<small class="help-block text-danger mb0">http://crm.deerapp.com/icon/137785339076771900195.png</small>
								<small class="help-block text-danger mb0">https://global.deererp.com/image/labelimg/2.jpg</small>
							</p> -->
                </div>
                <!--<div class="form-group">
								<p class="control-label mb5">从图片服务器选择(稍候推出):</p>
								<select class="form-control" disabled>
									<option value="0" selected>-选择服务器-</option>
								</select>
								<select class="form-control mt5" disabled>
									<option value="0" selected>-选择图片-</option>
								</select>
							</div>-->
            </div>
            <div class="panel-body tab-pane" id="imageupload">
                <div class="form-group">
                    <p class="control-label mb5">选择本地图片:</p>
                    <div class="input-group">
                        <input type="file" id="templetfile" name="templetfile"
                               onchange="$(&#39;#modelfile&#39;).val($(this).val());">
                        <input class="form-control" readonly="" type="text" id="modelfile"
                               onclick="$(&#39;#templetfile&#39;).click();">
                        <span class="input-group-btn">
									<button class="btn btn-primary" type="button"
                                            onclick="$(&#39;#templetfile&#39;).click();">加载浏览</button>
								</span>
                    </div>
                    <img src="/printTemp/img/photo_default.jpg" id="previewImage" class="img-thumbnail pct60 mt10">
                </div>
                <div class="alert alert-info">
                    <p class="mb0">支持jpg、gif、png等图片格式，图片大小不得超过1MB</p>
                </div>
            </div>
            <div class="alert alert-warning fade in group-warning">
                <p class="mb0">文字尺寸小于12px，在使用Google Chrome浏览器可能会出现显示异常，所以我们建议您使用Firefox或IE9以上版本浏览器执行标签打印</p>
            </div>
        </div>
    </form>
    <div class="btn-group edit-btn">
        <button class="btn btn-success btn-copy" type="button">
            <i class="ico-copy4 mr5"></i>复制
        </button>
        <button class="btn btn-danger btn-clear-item" type="button">
            <i class="ico-close mr5"></i>删除
        </button>
    </div>
</div>
<div id="userVUE">
    <div class="label-type">
        <div class="panel-group nm" id="type-group" style="margin-top:10px;">
            
        </div>
    </div>
    <!--顶部主信息-->
    <header class="custom-header">
        <div class="panel-body">
            <div class="form-horizontal form-bordered min">
                <form name="frmPrintTemplate" method="post">
                    <div class="form-group">
                        <div class="col-sm-7">
                            <div class="row">
                                <div class="col-sm-2">
                                    <p class="loadtext text-muted semibold">
                                        <span class="text-primary pl5 pr10">{{ tempInfo.type }}</span>
                                    </p>

                                </div>
                                <div class="col-sm-3">
                                    <div class="input-group input-group">
                                        <span class="input-group-addon">模板名称</span> <input type="text"
                                                                                           class="form-control"
                                                                                           placeholder="" name="name"
                                                                                           v-model="tempInfo.tempName">
                                        <input type="hidden" id="id" name="id" value="104921"> <input type="hidden"
                                                                                                      id="html"
                                                                                                      name="html"
                                                                                                      value="">
                                    </div>
                                </div>


                                <div class="col-sm-2">
                                    <div class="input-group input-group">
                                        <span class="input-group-addon">宽</span> <input type="text" class="form-control"
                                                                                        placeholder="" name="name"
                                                                                        v-model="width"> <input
                                            type="hidden" id="id" name="id" value="104921"> <input type="hidden"
                                                                                                   id="html" name="html"
                                                                                                   value="">
                                    </div>

                                </div>
                                <div class="col-sm-2">
                                    <div class="input-group input-group">
                                        <span class="input-group-addon">高</span> <input type="text" class="form-control"
                                                                                        placeholder="" name="name"
                                                                                        v-model="height"> <input
                                            type="hidden" id="id" name="id" value="104921"> <input type="hidden"
                                                                                                   id="html" name="html"
                                                                                                   value="">
                                    </div>

                                </div>


                            </div>
                        </div>
                        <div class="col-sm-5 text-right">

                            <el-button class=" btn-reset" type="warning">清空画布</el-button>
                            <el-button @click='saveTemp' type="success">保存</el-button>

                        </div>
                    </div>
                </form>
            </div>
        </div>
    </header>
    <!--设计面板-->
    <section class="custom-label">
        <div class="custom-content">
            <div id="divPrintTemplateHtml" class="label-group  ui-draggable"
                 style="width: 10cm; height: 10cm; left: 387.5px; top: 164.5px;">
                <div class="refer-tools">
                    <a href="javascript:void(0)" class="refer-upload" data-toggle="popover" data-content="您可以选择一张图片作为参照"
                       data-container="body" data-trigger="hover" data-placement="top" data-original-title=""
                       title=""><i class="ico-image"></i><input type="file" accept="image/*" class="hide"
                                                                name="referImageUpload"></a>
                    <a href="javascript:void(0)" class="refer-remove" data-toggle="popover"
                       data-content="您可以选择一张图片作为参照移除参考图片" data-container="body" data-trigger="hover"
                       data-placement="top" data-original-title="" title=""><i class="ico-remove3 text-danger"></i></a>
                    <a href="javascript:void(0)" class="refer-switch" data-toggle="popover"
                       data-content="您可以选择一张图片作为参照切换参考图显示方式：右侧或底部背景" data-container="body" data-trigger="hover"
                       data-placement="top" data-original-title="" title=""><i class=" ico-transmission"></i></a>
                    <a href="javascript:void(0)" class="refer-view" data-toggle="popover"
                       data-content="您可以选择一张图片作为参照显示或隐藏参考图" data-container="body" data-trigger="hover"
                       data-placement="top" data-original-title="" title=""><i class="ico-eye-close text-muted"></i></a>
                </div>

                <div id="tempHtml" v-html="tempInfo.tempHtml">
                    <div class="label-content" style='height:123mm;width:1123mm'>
                        <div class="view-mask"></div>
                        <div class="custom-area ui-droppable"></div>
                        <div class="custom-drop ui-droppable"></div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>

<div class="hotkey-panel" style="opacity: 0;">
    <a data-placement="top" data-original-title="您可以选择一张图片作为参照我知道了" data-toggle="tooltip" href="javascript:void(0);"
       class="close-hotkey ico-cancel-circle"></a>
    <h3>您可以选择一张图片作为参照快捷键</h3>
    <ul>
        <li><span class="key"><i class="ico-arrow-up3"></i></span><strong class="text-muted">上移</strong></li>
        <li><span class="key"><i class="ico-arrow-down3"></i></span><strong class="text-muted">下移</strong></li>
        <li><span class="key"><i class="ico-arrow-left3"></i></span><strong class="text-muted">左移</strong></li>
        <li><span class="key"><i class="ico-arrow-right4"></i></span><strong class="text-muted">右移</strong></li>
        <li><span class="key">Delete</span>或<span class="key">Del</span><strong class="text-muted">删除</strong></li>
    </ul>
</div>
</div>
</template>

 

<script type="text/javascript" src="./js/jwerty.js"></script>
<!-- <script type="text/javascript" src="./js/ajaxfileupload.js"></script> -->
<script type="text/javascript" src="./js/printTemp.js"></script>

 
 <style    src="./css/library.min.css"></style> 
    <style    src="./css/style.css"></style>
    <script   src="./js/library.min.js"></script>
    <script   src="./js/vue.js"></script>
    <style   src="./css/print.css"></style> 
<!-- 引入组件库 -->
<script src="./js/element_index.js"></script>
<script type="text/javascript">


 export default {
        name: 'RouteTactic', 
        data(){
            return {
                site: "菜鸟教程",
                url: "www.runoob.com",
                alexa: "10000",
                tempInfo: {
                    tempName: '',
                    shipCode: '',
                    tempId: '',
                    height: 0,
                    width: 0,
                    type: '',
                    tempHtml: "<div class='label-content'  style='height:123mm;width:123mm' ><div class='view-mask'></div><div class='custom-area ui-droppable'></div><div class='custom-drop ui-droppable'></div></div>"

                },
                height: 0,
                width: 0
            }

        },
        
        watch: {
            height: function (newVal, oldVal) {

                this.tempInfo.height = newVal;
                $('#aaa').css("height", newVal + 'mm');
            },
            width: function (newVal, oldVal) {
                this.tempInfo.width = newVal;
                $('#aaa').css("width", newVal + 'mm');
            }
        },
        created: function () {

        },
        mounted() {
            this.getTemp();


        },
        methods: {
            //从浏览器url拿参数
            getQueryVariable: function (variable) {
                var url = decodeURI(decodeURI(location.search));  //解码  解决中文乱码问题
                var query = url.substring(1);
                var vars = query.split("&");
                for (var i = 0; i < vars.length; i++) {
                    var pair = vars[i].split("=");
                    if (pair[0] == variable) {
                        return pair[1];
                    }
                }
                return (undefined);
            },
            getTemp: function () {
                var that = this;
                var id = this.getQueryVariable("tempId");
                if (id == undefined || id == null) {
                    var name = this.getQueryVariable("tempName");
                    that.tempInfo.tempName = name;
                    that.tempInfo.type = this.getQueryVariable("type");
                    that.tempInfo.wareId = this.getQueryVariable("wareId");
                    that.tempInfo.wareName = this.getQueryVariable("wareName");
                    that.tempInfo.shipCode = this.getQueryVariable("shipCode");

                    var height = this.getQueryVariable("height");
                    var width = this.getQueryVariable("width");
                    that.tempInfo.height = height;
                    that.tempInfo.width = width;
                    that.height = height;
                    that.width = width;

                    if (height != undefined && width != undefined) {
                        that.tempInfo.tempHtml = "<div class='label-content' id='aaa'  style='height:" + height + "mm;width:" + width + "mm' ><div class='view-mask'></div><div class='custom-area ui-droppable'></div><div class='custom-drop ui-droppable'></div></div>"

                    }
                    return;
                }
                var url = localStorage.getItem('baseUrl');

                $.ajax({//jQuery方法，此处可以换成其它请求方式
                    url: url + "/print/temp/infos/detail?id=" + id,
                    dataType: "json",
                    type: "get",
                    async: false,
                    headers: {
                        "Accept": "*/*",
                        //	"access-token": token
                    },
                    beforeSend: function () {
                        $('.loading_large').show();
                    },
                    contentType: "application/json;charset=utf-8",//这个参数也是header参数
                    processData: false,//不去处理发送的数据

                    error: function (res) {
                        $('.loading_large').fadeOut();
                    },
                    success: function (res) {
                        $('.loading_large').fadeOut();
                        that.tempInfo = res.data;
                        that.height = that.tempInfo.height;
                        that.width = that.tempInfo.width;
                        // async:{
                        // 	that.width = that.tempInfo.width;
                        // 		that.height = that.tempInfo.height;
                        // }


                    }

                })

            },
            details: function () {

            },

            saveTemp: function () {
                var that = this;
                //  var labelHtml=$(".label-group").clone().find(".dropitem").removeClass("ui-draggable ui-resizable").end().find(".ui-resizable-handle, .refer-tools, .refer-content").remove().end().html();
                //$('#html').val(Base64.encode(labelHtml));
                var labelHtml = $(".label-group").clone().find(".dropitem").removeClass("ui-draggable ui-resizable").end().find(".ui-resizable-handle, .refer-tools, .refer-content").remove().end().html();
                // labelHtml=  $("#tempHtml").html();
                var data = this.tempInfo;

                data.tempHtml = labelHtml;
                data = JSON.stringify(data);
                var url = localStorage.getItem('baseUrl');
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                $.ajax({//jQuery方法，此处可以换成其它请求方式
                    url: url + "/print/temp/infos/add",
                    dataType: "json",
                    type: "post",
                    data: data,
                    headers: {
                        "Accept": "*/*",
                        //	"access-token": token
                    },
                    beforeSend: function () {
                        $('.loading_large').show();
                    },
                    contentType: "application/json;charset=utf-8",//这个参数也是header参数
                    processData: false,//不去处理发送的数据

                    error: function (res) {
                        $('.loading_large').fadeOut();
                    },
                    success: function (res) {
                        $('.loading_large').fadeOut();
                        loading.close();
                        that.getTemp();
                        window.location.href = '/#/base/printTemp'
                        //	window.history.back()
                        //window.location.go(-1);
                        //self.location = document.referrer;
                        //window.history.back(-1);
                        //window.location.href ='http://localhost:9011/#/base/printTemp';


                    }

                })


            },


        }
    }
</script>


 
<style type="text/css">
    .dis-none {
        display: none
    }
</style>

<style type="text/css">

    .jqstooltip {
        position: absolute;
        left: 0px;
        top: 0px;
        visibility: hidden;
        background: rgb(0, 0, 0) transparent;
        background-color: rgba(0, 0, 0, 0.6);
        filter: progid:DXImageTransform.Microsoft.gradient(startColorstr=#99000000, endColorstr=#99000000);
        -ms-filter: "progid:DXImageTransform.Microsoft.gradient(startColorstr=#99000000, endColorstr=#99000000)";
        color: white;
        font: 10px arial, san serif;
        text-align: left;
        white-space: nowrap;
        padding: 5px;
        border: 1px solid white;
        z-index: 10000;
    }

    .jqsfield {
        color: white;
        font: 10px arial, san serif;
        text-align: left;
    }
</style>
 