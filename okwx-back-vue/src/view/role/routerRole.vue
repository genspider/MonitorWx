<template>
    <div>
        <jian-she></jian-she>
        <div id="myChart" :style="{width: '900px', height: '300px'}"></div>
    </div>
</template>

<script>
    import JianShe from "../../components/jianShe";
    export default {
        name: "routerRole",
        components: {JianShe},
        data() {
            return {}
        },
        methods:{
            drawLine(){
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts.init(document.getElementById('myChart'))
                // 绘制图表
                var options = {
                    title: { text: '在Vue中使用echarts' },
                    tooltip: {},
                    xAxis: {
                        data: ["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
                    },
                    yAxis: {},
                    series: [{
                        name: '销量',
                        type: 'bar',
                        data: [5, 20, 36, 10, 10, 20]
                    }]
                }
                myChart.setOption(options);
            }
        },
        mounted(){
           this.drawLine()
        }
    }
</script>

<style>

</style>