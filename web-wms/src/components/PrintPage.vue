<template>
    <!--
        打印页面 组件

        slot插槽
        class 宽高倍率：
            通过class设置宽、高、字号、行高，比使用n更方便。优先级高于行内样式。
            例如：class="height-6"，代表在标准尺寸下，设置为6mm高。相当于style="{ 'height': scope.n * 5 + 'mm' }"
            哪些CSS支持此写法？参考cssMapper变量。
        n 宽高倍率：
            插槽内设置宽高需要乘以倍率，以达到自动缩放的效果（尤其是高度）
        强制：
            div td .style: overflow=hidden 超出内容不显示
        默认：
            textAlign=center
            font-size=2mm
        二维码：
            <div class="qr">二维码内容</div>
            这样的div会自动转为二维码

        width 纸张宽度，单位毫米
        height 纸张高度，单位毫米（实际高度会减少一毫米）
            【为什么要减少1毫米？】因为如果刚好，会触发换页，但实际可能偏离几像素，导致换页不精准。
            减少1毫米可以保证内容高度不足换页，再通过CSS强制换页，防止像素级别的偏差进行累计，越打印越歪。

        version 用于触发组件重新渲染的属性，一般用法是version++，仅version变化时才会触发页面变化
        
        pageOver 【默认false】是否允许超出本页内容范围，自动换页打印。不允许换页，超出部分将不打印。
        printonly 【默认false】仅打印。
        debug 【默认false】开启样式调试，给每个div一个背景色

        @auther: xuesinuo
    -->
    <div>
        <div ref="printPageBox" class="print-page-box" :class="printonly ? 'printonly' : ''"
            :style="{ width: width - 1 + 'mm', height: pageOver ? null : height - 1 + 'mm', 'min-height': pageOver ? height - 1 + 'mm' : null }"
            style="font-size: 2mm;">
            <slot :n="n"></slot>
        </div>
        <div class="print-page-break"></div>
        <div class="noprint" style="height: 5mm;"></div>
    </div>
</template>

<script>
const cssMapper = {
    'width-': 'width',
    'min-width-': 'minWidth',
    'max-width-': 'maxWidth',
    'height-': 'height',
    'min-height-': 'minHeight',
    'max-height-': 'maxHeight',
    'font-size-': 'fontSize',
    'line-height-': 'lineHeight',
}
let debugBg = 0;
const debugBgs = ['#daecfe', '#e2f3da', '#faecda', '#fce3e3', '#e9e9eb', '#f9fbd9', '#d9fbfb']
import QRCode from 'qrcodejs2'
export default {
    name: "PrintSettings",
    props: {
        "width": Number,
        "height": Number,
        "version": Number,
        "pageOver": Boolean,
        "printonly": Boolean,
        "debug": Boolean,
    },
    data() {
        return {
            "oWidth": 1,
            "oHeight": 1,
            "cssMapper": cssMapper
        };
    },
    components: {
    },
    computed: {
        n() {
            let nw = 0
            let nh = 0
            if (this.width && this.oWidth) {
                nw = (this.width - 1) / this.oWidth
            }
            if (this.height && this.oHeight) {
                nh = (this.height - 1) / this.oHeight
            }
            if (nw && nh) {
                return nw < nh ? nw : nh
            }
            return 1
        },
    },
    watch: {
        version: function () {
            this.compute(this.$refs.printPageBox)
        }
    },
    methods: {
        compute(dom) {
            // 设置DIV样式
            if (dom.nodeName && dom.nodeName.toUpperCase() == 'DIV') {
                dom.style.overflow = 'hidden'
                if (!dom.style.textAlign) {
                    dom.style.textAlign = 'center'
                }
                if (this.debug) {
                    dom.style.backgroundColor = debugBgs[debugBg++ % debugBgs.length]
                } else {
                    dom.style.backgroundColor = '#FFF'
                }
            }
            if (dom.classList && dom.classList.length > 0) {
                for (let iClassName = 0; iClassName < dom.classList.length; iClassName++) {
                    const className = dom.classList[iClassName]
                    // 设置长宽字号
                    for (const classKey in this.cssMapper) {
                        const styleKey = this.cssMapper[classKey]
                        if (className.indexOf(classKey) == 0) {
                            const number = new Number(className.substring(classKey.length))
                            if (number && number > 0) {
                                dom.style[styleKey] = this.n * number + 'mm'
                            }
                        }
                    }
                    // 设置二维码
                    if (className == 'qr' && dom.nodeName.toUpperCase() == 'DIV') {
                        if (dom.innerHTML == dom.innerText && dom.innerText != '') {
                            const value = dom.innerHTML
                            dom.innerHTML = null
                            dom.id = 'qr' + Math.random()
                            new QRCode(dom.id, {
                                text: value,
                                width: 640,
                                height: 640,
                                correctLevel: QRCode.CorrectLevel.L
                            })
                        }
                    }
                }
            }
            if (dom.childNodes && dom.childNodes.length > 0) {
                const childs = dom.childNodes
                for (let c = 0; c < childs.length; c++) {
                    const child = childs[c]
                    if (child) {
                        this.compute(child)
                    }
                }
            }
        },
    },
    created() {
        this.oWidth = this.width - 1
        this.oHeight = this.height - 1
    },
    mounted() {
        this.compute(this.$refs.printPageBox)
    },
};
</script>

<style lang="less" scoped>
.print-page-box {
    margin: auto;
    background-color: #FFF;
}

/deep/ .qr>canvas {
    width: 100% !important;
    height: 100% !important;
}

/deep/ .qr>img {
    width: 100% !important;
    height: 100% !important;
}

@media screen {
    .print-page-box {
        border-radius: 4mm;
    }
}
</style>
