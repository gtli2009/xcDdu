var {add}=require("./model01")
var Vue = require("./vue.min")
  /*<script src="../webpacktest01/vue.min.js"></script>*/
var VM=new Vue({
    el:"#app",
    data:{
        name:'黑马程序员',
        num1:0,
        num2:0,
        result:0,
        url:'www.itcast.com'
    },
    methods:{
        change:function () {
            this.result= add(Number.parseInt(this.num1), Number.parseInt(this.num2))
          /*  alert("计算结果="+this.result)*/
        }


    }
})